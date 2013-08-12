/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */

package com.codenvy.ide.ui.tree;

import elemental.css.CSSStyleDeclaration;
import elemental.html.DivElement;
import elemental.html.Element;
import elemental.html.SpanElement;
import elemental.js.html.JsLIElement;
import elemental.js.html.JsUListElement;

import com.codenvy.ide.ui.tree.Tree.Css;
import com.codenvy.ide.util.AnimationController;
import com.codenvy.ide.util.CssUtils;
import com.codenvy.ide.util.dom.Elements;

/**
 * Overlay type for the base element for a Node in the tree.
 * <p/>
 * Nodes with no children have no UL element.
 * <p/>
 * Nodes that have children, but that have never been expanded (nodes render
 * lazily on expansion), have an empty UL element.
 * <p/>
 * <pre>
 *
 * <li class="treeNode">
 *   <div class="treeNodeBody">
 *     <div class="expandControl"></div><span class="treeNodeLabel"></span>
 *   </div>
 *   <ul class="childrenContainer">
 *   </ul>
 * </li>
 *
 * </pre>
 */
public class TreeNodeElement<D> extends JsLIElement {

    /**
     * Creates a TreeNodeElement from some data. Should only be called by
     * {@link Tree}.
     *
     * @param <D>
     *         the type of data
     * @param dataAdapter
     *         An {@link NodeDataAdapter} that allows us to visit the
     *         NodeData
     * @return a new {@link TreeNodeElement} created from the supplied data.
     */
    static <D> TreeNodeElement<D> create(
            D data, NodeDataAdapter<D> dataAdapter, NodeRenderer<D> nodeRenderer, Tree.Css css) {
        // Make the node base.
        @SuppressWarnings("unchecked")
        TreeNodeElement<D> treeNode = (TreeNodeElement<D>)Elements.createElement("li", css.treeNode());
        treeNode.setData(data);
        treeNode.setRenderer(nodeRenderer);
        // Associate the rendered node with the underlying model data.
        dataAdapter.setRenderedTreeNode(data, treeNode);

        // Attach the Tree node body.
        DivElement treeNodeBody = Elements.createDivElement(css.treeNodeBody());
        treeNodeBody.setAttribute("draggable", "true");
        DivElement expandControl = Elements.createDivElement();
        SpanElement nodeContents = nodeRenderer.renderNodeContents(data);
        nodeContents.addClassName(css.treeNodeLabel());

        treeNodeBody.appendChild(expandControl);
        treeNodeBody.appendChild(nodeContents);

        treeNode.appendChild(treeNodeBody);
        treeNode.ensureChildrenContainer(dataAdapter, css);

        return treeNode;
    }

    protected TreeNodeElement() {
    }

    /**
     * Appends the specified child to this TreeNodeElement's child container
     * element.
     *
     * @param child
     *         The {@link TreeNodeElement} we want to append to as a child of
     *         this node.
     */
    public final void addChild(
            NodeDataAdapter<D> dataAdapter, TreeNodeElement<D> child, Tree.Css css) {
        ensureChildrenContainer(dataAdapter, css);

        getChildrenContainer().appendChild(child);
    }

    /**
     * @return The associated NodeData that is a bound to this node when it was
     *         rendered.
     */
    public final native D getData() /*-{
        return this.__nodeData;
    }-*/;

    /**
     * Nodes with no children have no UL element, only a DIV for the Node body.
     *
     * @return whether or not this node has children.
     */
    public final boolean hasChildrenContainer() {
        int length = this.getChildren().getLength();

        assert (length < 3) : "TreeNodeElement has more than 2 children of its root element!";

        return (length == 2);
    }

    public final boolean isActive(Tree.Css css) {
        return CssUtils.containsClassName(getSelectionElement(), css.active());
    }

    /** Checks whether or not this node is open. */
    public final native boolean isOpen() /*-{
        return !!this.__nodeOpen;
    }-*/;

    private void setOpen(Tree.Css css, boolean isOpen) {
        if (isOpen != isOpen()) {
            CssUtils.setClassNameEnabled(getExpandControl(), css.openedIcon(), isOpen);
            CssUtils.setClassNameEnabled(getExpandControl(), css.closedIcon(), !isOpen);
            setOpenImpl(isOpen);
            getRenderer().updateNodeContents(this);
        }
    }

    private native void setOpenImpl(boolean isOpen) /*-{
        this.__nodeOpen = isOpen;
    }-*/;

    public final boolean isSelected(Tree.Css css) {
        return CssUtils.containsClassName(getSelectionElement(), css.selected());
    }

    /** Makes this node into a leaf node. */
    public final void makeLeafNode(Tree.Css css) {
        getExpandControl().setClassName(css.expandControl() + " " + css.leafIcon());
        if (hasChildrenContainer()) {
            getChildrenContainer().removeFromParent();
        }
    }

    /**
     * Removes this node from the {@link Tree} and breaks the back reference from
     * the underlying node data.
     */
    public final void removeFromTree() {
        removeFromParent();
    }

    /** Sets whether or not this node has the active node styling applied. */
    public final void setActive(boolean isActive, Css css) {
        // Show the selection on the element returned by the node renderer
        Element selectionElement = getSelectionElement();
        CssUtils.setClassNameEnabled(selectionElement, css.active(), isActive);
        if (isActive) {
            selectionElement.focus();
        }
    }

    /** Sets whether or not this node has the selected styling applied. */
    public final void setSelected(boolean isSelected, Tree.Css css) {
        CssUtils.setClassNameEnabled(getSelectionElement(), css.selected(), isSelected);
    }

    /** Sets whether or not this node is the active drop target. */
    public final void setIsDropTarget(boolean isDropTarget, Tree.Css css) {
        CssUtils.setClassNameEnabled(this, css.isDropTarget(), isDropTarget);
    }

    /**
     * Closes the current node. Must have children if you call this!
     *
     * @param css
     *         The {@link Tree.Css} instance that contains relevant selector
     *         names.
     * @param shouldAnimate
     *         whether to do the animation or not
     */
    final void closeNode(NodeDataAdapter<D> dataAdapter, Tree.Css css, AnimationController closer,
                         boolean shouldAnimate) {
        ensureChildrenContainer(dataAdapter, css);

        Element expandControl = getExpandControl();

        assert (hasChildrenContainer() && CssUtils.containsClassName(expandControl,
                                                                     css.expandControl())) :
                "Tried to close a node that didn't have an expand control";

        setOpen(css, false);

        Element childrenContainer = getChildrenContainer();
        if (shouldAnimate) {
            closer.hide(childrenContainer);
        } else {
            closer.hideWithoutAnimating(childrenContainer);
        }
    }

    /**
     * You should call hasChildren() before calling this method. This will throw
     * an exception if a Node is a leaf node.
     *
     * @return The UL element containing children of this TreeNodeElement.
     */
    final JsUListElement getChildrenContainer() {
        return (JsUListElement)this.getChildren().item(1);
    }

    public final SpanElement getNodeLabel() {
        return (SpanElement)getNodeBody().getChildren().item(1);
    }

    /**
     * Expands the current node. Must have children if you call this!
     *
     * @param css
     *         The {@link Tree.Css} instance that contains relevant selector
     *         names.
     * @param shouldAnimate
     *         whether to do the animation or not
     */
    final void openNode(NodeDataAdapter<D> dataAdapter, Tree.Css css, AnimationController opener,
                        boolean shouldAnimate) {
        ensureChildrenContainer(dataAdapter, css);

        Element expandControl = getExpandControl();

        assert (hasChildrenContainer() && CssUtils.containsClassName(expandControl,
                                                                     css.expandControl())) :
                "Tried to open a node that didn't have an expand control";

        setOpen(css, true);

        Element childrenContainer = getChildrenContainer();
        if (shouldAnimate) {
            opener.show(childrenContainer);
        } else {
            opener.showWithoutAnimating(childrenContainer);
        }
    }

    /**
     * If this node does not have a children container, but has children data,
     * then we coerce a children container into existence.
     */
    final void ensureChildrenContainer(NodeDataAdapter<D> dataAdapter, Tree.Css css) {
        if (!hasChildrenContainer()) {
            D data = getData();
            if (dataAdapter.hasChildren(data)) {
                Element childrenContainer = Elements.createElement("ul", css.childrenContainer());
                this.appendChild(childrenContainer);
                childrenContainer.getStyle().setDisplay(CSSStyleDeclaration.Display.NONE);
                getExpandControl().setClassName(css.expandControl() + " " + css.closedIcon());
            } else {
                getExpandControl().setClassName(css.expandControl() + " " + css.leafIcon());
            }
        }
    }

    private Element getExpandControl() {
        return getNodeBody().getChildren().item(0);
    }

    /**
     * @return The node body element that contains the expansion control and the
     *         node contents.
     */
    private Element getNodeBody() {
        return getChildren().item(0);
    }

    final Element getSelectionElement() {
        return getNodeBody();
    }

    /**
     * Stashes associate NodeData as an expando on our element, and also sets up a
     * reverse mapping.
     *
     * @param data
     *         The NodeData we want to associate with this node element.
     */
    private native void setData(D data) /*-{
        this.__nodeData = data;
    }-*/;

    private native NodeRenderer<D> getRenderer() /*-{
        return this.__nodeRenderer;
    }-*/;

    private native void setRenderer(NodeRenderer<D> renderer) /*-{
        this.__nodeRenderer = renderer;
    }-*/;
}
