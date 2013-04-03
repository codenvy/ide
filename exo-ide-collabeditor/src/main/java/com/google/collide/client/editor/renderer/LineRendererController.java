// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.collide.client.editor.renderer;

import elemental.css.CSSStyleDeclaration;
import elemental.dom.Node;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.Element;
import elemental.html.SpanElement;

import com.codenvy.ide.client.util.Elements;
import com.codenvy.ide.client.util.logging.Log;
import com.google.collide.client.Resources;
import com.google.collide.client.document.linedimensions.LineDimensionsUtils;
import com.google.collide.client.editor.Buffer;
import com.google.collide.client.editor.folding.FoldMarker;
import com.google.collide.client.editor.folding.FoldingManager;
import com.google.collide.shared.document.Line;
import com.google.common.base.Preconditions;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

import org.exoplatform.ide.json.shared.JsonArray;
import org.exoplatform.ide.json.shared.JsonCollections;
import org.exoplatform.ide.shared.util.SortedList;
import org.exoplatform.ide.shared.util.StringUtils;

/**
 * A class to maintain the list of {@link LineRenderer LineRenderers} and render
 * a line by delegating to each of the renderers.
 */
class LineRendererController {

    /*
     * TODO: consider recycling these if GC performance during
     * rendering is an issue
     */
    private static class LineRendererTarget implements LineRenderer.Target {

        private static class Comparator implements SortedList.Comparator<LineRendererTarget> {
            @Override
            public int compare(LineRendererTarget a, LineRendererTarget b) {
                return a.remainingCount - b.remainingCount;
            }
        }

        /** The line renderer for which this is the target */
        private final LineRenderer lineRenderer;

        /**
         * The remaining number of characters that should receive {@link #styleName}
         * . Once this is 0, the {@link #lineRenderer} will be asked to render its
         * next chunk
         */
        private int remainingCount;

        /** The style to be applied to the {@link #remainingCount} */
        private String styleName;

        public LineRendererTarget(LineRenderer lineRenderer) {
            this.lineRenderer = lineRenderer;
        }

        @Override
        public void render(int characterCount, String styleName) {
            remainingCount = characterCount;
            this.styleName = styleName;
        }
    }

    /**
     * A sorted list storing targets for the line renderers that are participating
     * in rendering the current line
     */
    private final SortedList<LineRendererTarget> currentLineRendererTargets;

    /**
     * A list of all of the line renderers that are registered on the editor (Note
     * that some may not be participating in the current line)
     */
    private final JsonArray<LineRenderer> lineRenderers;
    private final Buffer                  buffer;
    private final FoldingManager          foldingManager;
    private final Resources               resources;

    LineRendererController(Buffer buffer, FoldingManager foldingManager, Resources res) {
        this.buffer = buffer;
        this.foldingManager = foldingManager;
        this.resources = res;
        currentLineRendererTargets = new SortedList<LineRendererController.LineRendererTarget>(
                new LineRendererTarget.Comparator());
        lineRenderers = JsonCollections.createArray();
    }

    void addLineRenderer(LineRenderer lineRenderer) {
        if (!lineRenderers.contains(lineRenderer)) {
      /*
       * Prevent line renderer from appearing twice in the list if it is already
       * added.
       */
            lineRenderers.add(lineRenderer);
        }
    }

    void removeLineRenderer(LineRenderer lineRenderer) {
        lineRenderers.remove(lineRenderer);
    }

    void renderLine(Line line, int lineNumber, Element targetElement, boolean isTargetElementEmpty) {

        currentLineRendererTargets.clear();

        if (!resetLineRenderers(line, lineNumber)) {
            // No line renderers are participating, so exit early.
            setTextContentSafely(targetElement, line.getText());
            return;
        }

        if (!isTargetElementEmpty) {
            targetElement.setInnerHTML("");
        }

        Element contentElement = Elements.createSpanElement();
        // TODO: file a Chrome bug, place link here
        contentElement.getStyle().setDisplay(CSSStyleDeclaration.Display.INLINE_BLOCK);
        for (int indexInLine = 0, lineSize = line.getText().length();
             indexInLine < lineSize && ensureAllRenderersHaveARenderedNextChunk(); ) {

            int chunkSize = currentLineRendererTargets.get(0).remainingCount;
            if (chunkSize == 0) {
                // Bad news, revert to naive rendering and log
                setTextContentSafely(targetElement, line.getText());
                Log.error(getClass(), "Line renderers do not have remaining chunks");
                return;
            }

            renderChunk(line.getText(), indexInLine, chunkSize, contentElement);
            markChunkRendered(chunkSize);

            indexInLine += chunkSize;
        }
        targetElement.appendChild(contentElement);

        if (line.getText().endsWith("\n")) {
            Element lastChunk = (Element)contentElement.getLastChild();
            Preconditions.checkState(lastChunk != null, "This line has no chunks!");
            if (!StringUtils.isNullOrWhitespace(lastChunk.getClassName())) {
                contentElement.getStyle().setProperty("float", "left");
                Element newlineCharacterElement = createLastChunkElement(targetElement);
                // Created on demand only because it is rarely used.
                Element remainingSpaceElement = null;
                for (int i = 0, n = currentLineRendererTargets.size(); i < n; i++) {
                    LineRendererTarget target = currentLineRendererTargets.get(i);
                    if (target.styleName != null) {
                        if (!target.lineRenderer.shouldLastChunkFillToRight()) {
                            newlineCharacterElement.addClassName(target.styleName);
                        } else {
                            if (remainingSpaceElement == null) {
                                newlineCharacterElement.getStyle().setProperty("float", "left");
                                remainingSpaceElement = createLastChunkElement(targetElement);
                                remainingSpaceElement.getStyle().setWidth("100%");
                            }
                            // Also apply to last chunk element so that there's no gap.
                            newlineCharacterElement.addClassName(target.styleName);
                            remainingSpaceElement.addClassName(target.styleName);
                        }
                    }
                }
            }
        }

        FoldMarker foldMarker = foldingManager.getFoldMarkerOfLine(lineNumber, true);
        if (foldMarker != null && foldMarker.isCollapsed()) {
            Element expandElement = createFoldingSignElement(contentElement, foldMarker, lineNumber);
            line.putTag(ViewportRenderer.LINE_TAG_EXPAND_ELEMENT, expandElement);
        }
    }

    private static Element createLastChunkElement(Element parent) {
        // we need to give them a whitespace element so that it can be styled.
        Element whitespaceElement = Elements.createSpanElement();
        whitespaceElement.setTextContent("\u00A0");
        whitespaceElement.getStyle().setDisplay("inline-block");
        parent.appendChild(whitespaceElement);
        return whitespaceElement;
    }

    /**
     * Creates element for expanding collapsed text block.
     *
     * @param parent
     *         parent element
     * @param foldMarker
     *         <code>foldMarker</code> to expand
     * @param lineNumber
     *         line number to generate element ID (for Selenium tests)
     * @return created element for expanding folded text block
     */
    Element createFoldingSignElement(Element parent, final FoldMarker foldMarker, int lineNumber) {
        final SpanElement element = Elements.createSpanElement(resources.workspaceEditorBufferCss().expandMarker());
        // TODO: file a Chrome bug, place link here
        element.getStyle().setDisplay(CSSStyleDeclaration.Display.INLINE_BLOCK);
        element.setId("expandMarker_line:" + (lineNumber + 1));

        element.addEventListener(Event.MOUSEOVER, new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                element.addClassName(resources.workspaceEditorBufferCss().expandMarkerOver());
            }
        }, false);
        element.addEventListener(Event.MOUSEOUT, new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                element.removeClassName(resources.workspaceEditorBufferCss().expandMarkerOver());
            }
        }, false);
        element.addEventListener(Event.MOUSEDOWN, new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                foldingManager.expand(foldMarker);
            }
        }, false);

        com.google.gwt.user.client.Element expandImageElement = new Image(resources.expandArrows()).getElement();
        expandImageElement.getStyle().setWidth(20, Unit.PX);
        expandImageElement.getStyle().setOpacity(0.5);
        expandImageElement.getStyle().setMarginBottom(4, Unit.PX);
        element.appendChild((Node)expandImageElement);

        parent.appendChild(element);

        return element;
    }

    /**
     * Ensures all renderer targets (that want to render) have rendered each of
     * their next chunks.
     */
    private boolean ensureAllRenderersHaveARenderedNextChunk() {
        while (currentLineRendererTargets.size() > 0
               && currentLineRendererTargets.get(0).remainingCount == 0) {
            LineRendererTarget target = currentLineRendererTargets.get(0);
            try {
                target.lineRenderer.renderNextChunk(target);
            } catch (Throwable t) {
                // Cause naive rendering
                target.remainingCount = 0;
                Log.warn(getClass(), "An exception was thrown from renderNextChunk", t);
            }

            if (target.remainingCount > 0) {
                currentLineRendererTargets.repositionItem(0);
            } else {
                // Remove the line renderer because it has broken our contract
                currentLineRendererTargets.remove(0);
                Log.warn(getClass(), "The line renderer " + target.lineRenderer
                                     + " is lacking a next chunk, removing from rendering");
            }
        }

        return currentLineRendererTargets.size() > 0;
    }

    /** Marks the chunk rendered on all the renderers. */
    private void markChunkRendered(int chunkSize) {
        for (int i = 0, n = currentLineRendererTargets.size(); i < n; i++) {
            LineRendererTarget target = currentLineRendererTargets.get(i);
            target.remainingCount -= chunkSize;
        }
    }

    /**
     * Renders the chunk by creating a span with all of the individual line
     * renderer's styles.
     */
    private void renderChunk(String lineText, int lineIndex, int chunkLength, Element targetElement) {
        SpanElement element = Elements.createSpanElement();
        // TODO: file a Chrome bug, place link here
        element.getStyle().setDisplay(CSSStyleDeclaration.Display.INLINE_BLOCK);
        setTextContentSafely(element, lineText.substring(lineIndex, lineIndex + chunkLength));
        applyStyles(element);
        targetElement.appendChild(element);
    }

    private void applyStyles(Element element) {
        for (int i = 0, n = currentLineRendererTargets.size(); i < n; i++) {
            LineRendererTarget target = currentLineRendererTargets.get(i);
            if (target.styleName != null) {
                element.addClassName(target.styleName);
            }
        }
    }

    /**
     * Resets the line renderers, preparing for a new line to be rendered.
     * <p/>
     * This method fills the {@link #currentLineRendererTargets} with targets for
     * line renderers that will participate in rendering this line.
     *
     * @return true if there is at least one line renderer participating for the
     *         given @{link Line} line.
     */
    private boolean resetLineRenderers(Line line, int lineNumber) {
        boolean hasAtLeastOneParticipatingLineRenderer = false;
        for (int i = 0; i < lineRenderers.size(); i++) {
            LineRenderer lineRenderer = lineRenderers.get(i);
            boolean isParticipating = lineRenderer.resetToBeginningOfLine(line, lineNumber);
            if (isParticipating) {
                currentLineRendererTargets.add(new LineRendererTarget(lineRenderer));
                hasAtLeastOneParticipatingLineRenderer = true;
            }
        }

        return hasAtLeastOneParticipatingLineRenderer;
    }

    private void setTextContentSafely(Element element, String text) {
        String cleansedText = text.replaceAll("\t", LineDimensionsUtils.getTabAsSpaces());
        element.setTextContent(cleansedText);
    }
}
