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

package org.exoplatform.ide.texteditor.selection;

import org.exoplatform.ide.AppContext;
import org.exoplatform.ide.json.JsonArray;
import org.exoplatform.ide.json.JsonCollections;
import org.exoplatform.ide.text.store.LineInfo;
import org.exoplatform.ide.texteditor.Buffer;
import org.exoplatform.ide.texteditor.Editor;
import org.exoplatform.ide.texteditor.Editor.ReadOnlyListener;
import org.exoplatform.ide.texteditor.FocusManager;
import org.exoplatform.ide.util.ListenerRegistrar;



/**
 * A controller responsible for keeping the local user's cursor renderer
 * up-to-date.
 *
 */
public class LocalCursorController
    implements
      SelectionModel.CursorListener,
      FocusManager.FocusListener,
      ReadOnlyListener {

  public static LocalCursorController create(AppContext appContext, FocusManager focusManager,
      SelectionModel selectionModel, Buffer buffer, Editor editor) {

    CursorView cursorView = CursorView.create(appContext, true);

    return new LocalCursorController(focusManager, selectionModel, cursorView, buffer, editor);
  }

  private final Buffer buffer;
  private final CursorView cursorView;
  private final JsonArray<ListenerRegistrar.Remover> listenerRemovers =
      JsonCollections.createArray();
  private final SelectionModel selectionModel;

  private LocalCursorController(FocusManager focusManager, SelectionModel selectionModel,
      CursorView cursorView, Buffer buffer, Editor editor) {

    this.selectionModel = selectionModel;
    this.cursorView = cursorView;
    this.buffer = buffer;

    resetCursorView();

    listenerRemovers.add(focusManager.getFocusListenerRegistrar().add(this));
    listenerRemovers.add(selectionModel.getCursorListenerRegistrar().add(this));
    listenerRemovers.add(editor.getReadOnlyListenerRegistrar().add(this));

    attachCursorElement();

    onFocusChange(focusManager.hasFocus());
    onReadOnlyChanged(editor.isReadOnly());
  }

  private void attachCursorElement() {
    buffer.addAnchoredElement(selectionModel.getCursorAnchor(), cursorView.getView().getElement());
  }

  private void detachCursorElement() {
    buffer.removeAnchoredElement(selectionModel.getCursorAnchor(), cursorView.getView()
        .getElement());
  }

  @Override
  public void onCursorChange(LineInfo lineInfo, int column, boolean isExplicitChange) {
    cursorView.forceSolidBlinkState();
  }

  @Override
  public void onFocusChange(boolean hasFocus) {
    cursorView.setVisibility(hasFocus);
  }

  @Override
  public void onReadOnlyChanged(boolean isReadOnly) {
    if (isReadOnly) {
      detachCursorElement();
    } else {
      attachCursorElement();
    }
  }

  public void resetCursorView() {
    cursorView.setColor("black");
    cursorView.setBlockMode(false);
  }

  /**
   * TODO: let block mode use and set the color of the
   * character beneath it to create an inverted color effect.
   */
  public void setBlockMode(boolean enabled) {
    cursorView.setBlockMode(enabled);
  }

  public void setColor(String color) {
    cursorView.setColor(color);
  }

  public void teardown() {
    for (int i = 0, n = listenerRemovers.size(); i < n; i++) {
      listenerRemovers.get(i).remove();
    }

    detachCursorElement();
  }
}
