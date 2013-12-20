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
package org.exoplatform.ide.editor.codemirror;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.editor.api.CodeLine;
import org.exoplatform.ide.editor.api.codeassitant.Token;
import org.exoplatform.ide.editor.api.codeassitant.TokenBeenImpl;
import org.exoplatform.ide.editor.api.codeassitant.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:dmitry.nochevnov@exoplatform.com">Dmytro Nochevnov</a>
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: CodeValidator Feb 9, 2011 5:48:28 PM $
 */
public abstract class CodeValidator {
    /**
     * Get list of code errors in the tokenList
     *
     * @param tokenList
     * @return
     */
    public abstract List<CodeLine> getCodeErrorList(List<? extends Token> tokenList);

    /**
     * Get import statement "import <fqn>;" in the appropriate place of file
     *
     * @param tokenList
     * @param fqn
     */
    public abstract CodeLine getImportStatement(List<? extends Token> tokenList, String fqn);

    /**
     * Returns list of code error in line with lineNumber
     *
     * @param lineNumber
     * @return
     */
    public static List<CodeLine> getCodeErrorList(int lineNumber, List<CodeLine> codeErrors) {
        List<CodeLine> lineCodeErrorList = new ArrayList<CodeLine>();

        for (CodeLine codeError : codeErrors) {
            if (codeError.getLineNumber() == lineNumber) {
                lineCodeErrorList.add(codeError);
            }
        }

        return lineCodeErrorList;
    }

    /**
     * Get text summary of registered errors from the lineCodeErrorList within the line
     *
     * @param lineCodeErrorList
     * @return text summary of errors within the line
     */
    public static String getErrorSummary(List<CodeLine> lineCodeErrorList) {
        StringBuffer errorSummary = new StringBuffer();

        for (CodeLine codeError : lineCodeErrorList) {
            switch (codeError.getType()) {
                case TYPE_ERROR:
                    errorSummary.append("'").append(codeError.getLineContent()).append("' cannot be resolved to a type; ");
                    break;

                default:
            }
        }
        return errorSummary.toString();
    }

    /**
     * @param lineNumber
     * @return true if there is at list one code error in the line with lineNumber
     */
    public static boolean isExistedCodeError(int lineNumber, List<CodeLine> lineCodeErrorList) {
        for (CodeLine codeError : lineCodeErrorList) {
            if (codeError.getLineNumber() == lineNumber) {
                return true;
            }
        }

        return false;
    }

    /**
     * Extract tokens with mimeType from tokenList
     *
     * @param tokenList
     * @param code
     * @param mimeType
     * @return
     */
    public static List<? extends Token> extractCode(List<TokenBeenImpl> tokenList, List<TokenBeenImpl> code,
                                                    String mimeType) {
        for (TokenBeenImpl token : tokenList) {
            analizeToken(token, code, mimeType); // update groovyCode
        }

        return code;
    }

    private static void analizeToken(TokenBeenImpl currentToken, List<TokenBeenImpl> code, String mimeType) {
        if (currentToken == null) {
            return;
        }

        if (mimeType.equals(currentToken.getMimeType())) {
            if (MimeType.APPLICATION_GROOVY.equals(mimeType) && TokenType.GROOVY_TAG.equals(currentToken.getType()) // add subtokens
                // of Groovy
                // Template "<%"
                // tag
                || MimeType.APPLICATION_JAVA.equals(mimeType) && TokenType.JSP_TAG.equals(currentToken.getType()) // add subtokens of
                // JSP file "<%" tag
                || MimeType.APPLICATION_JAVASCRIPT.equals(mimeType) && TokenType.TAG.equals(currentToken.getType()) // add subtokens
                // of "<script>"
                    ) {
                if (currentToken.getSubTokenList() != null) {
                    code.addAll(currentToken.getSubTokenList());
                }
            } else {
                code.add(currentToken);
            }
        } else {
            // search target token among subtokens
            if (currentToken.getSubTokenList() != null) {
                for (TokenBeenImpl subtoken : currentToken.getSubTokenList()) {
                    analizeToken(subtoken, code, mimeType);
                }
            }
        }
    }

}