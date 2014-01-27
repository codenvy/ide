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
package org.exoplatform.ide.editor.shared.text.edits;

/**
 * A visitor for text edits.
 * <p>
 * For each different concrete text edit type <it>T</it> there is a method:
 * <ul>
 * <li><code>public boolean visit(<it>T</it> node)</code> - Visits the given edit to perform some arbitrary operation. If
 * <code>true </code> is returned, the given edit's child edits will be visited next; however, if <code>false</code> is returned,
 * the given edit's child edits will not be visited. The default implementation provided by this class calls a generic method
 * <code>visitNode(<it>TextEdit</it> node)</code>. Subclasses may reimplement these method as needed.</li>
 * </ul>
 * </p>
 * <p>
 * In addition, there are methods for visiting text edits in the abstract, regardless of node type:
 * <ul>
 * <li><code>public void preVisit(TextEdit edit)</code> - Visits the given edit to perform some arbitrary operation. This method
 * is invoked prior to the appropriate type-specific <code>visit</code> method. The default implementation of this method does
 * nothing. Subclasses may reimplement this method as needed.</li>
 * <p/>
 * <li><code>public void postVisit(TextEdit edit)</code> - Visits the given edit to perform some arbitrary operation. This method
 * is invoked after the appropriate type-specific <code>endVisit</code> method. The default implementation of this method does
 * nothing. Subclasses may reimplement this method as needed.</li>
 * </ul>
 * </p>
 * <p>
 * For edits with children, the child nodes are visited in increasing order.
 * </p>
 *
 * @see TextEdit#accept(TextEditVisitor)
 * @since 3.0
 */
public class TextEditVisitor {

    /**
     * Visits the given text edit prior to the type-specific visit. (before <code>visit</code>).
     * <p>
     * The default implementation does nothing. Subclasses may reimplement.
     * </p>
     *
     * @param edit
     *         the node to visit
     */
    public void preVisit(TextEdit edit) {
        // default implementation: do nothing
    }

    /**
     * Visits the given text edit following the type-specific visit (after <code>endVisit</code>).
     * <p>
     * The default implementation does nothing. Subclasses may reimplement.
     * </p>
     *
     * @param edit
     *         the node to visit
     */
    public void postVisit(TextEdit edit) {
        // default implementation: do nothing
    }

    /**
     * Visits the given text edit. This method is called by default from type-specific visits. It is not called by an edit's accept
     * method. The default implementation returns <code>true</code>.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visitNode(TextEdit edit) {
        return true;
    }

    /**
     * Visits a <code>CopySourceEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(CopySourceEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>CopyTargetEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(CopyTargetEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>MoveSourceEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(MoveSourceEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>MoveTargetEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(MoveTargetEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>RangeMarker</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(RangeMarker edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>CopyingRangeMarker</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(CopyingRangeMarker edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>DeleteEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(DeleteEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>InsertEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(InsertEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>ReplaceEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(ReplaceEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>UndoEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(UndoEdit edit) {
        return visitNode(edit);
    }

    /**
     * Visits a <code>MultiTextEdit</code> instance.
     *
     * @param edit
     *         the node to visit
     * @return If <code>true</code> is returned, the given node's child nodes will be visited next; however, if <code>false</code>
     *         is returned, the given node's child nodes will not be visited.
     */
    public boolean visit(MultiTextEdit edit) {
        return visitNode(edit);
    }
}