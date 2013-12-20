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
package org.exoplatform.ide.extension.java.jdi.server.model;

import org.exoplatform.ide.extension.java.jdi.shared.Field;
import org.exoplatform.ide.extension.java.jdi.shared.VariablePath;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class FieldImpl extends VariableImpl implements Field {
    private boolean isFinal;
    private boolean isStatic;
    private boolean isTransient;
    private boolean isVolatile;

    public FieldImpl(String name,
                     String value,
                     String type,
                     VariablePath variablePath,
                     boolean isFinal,
                     boolean isStatic,
                     boolean isTransient,
                     boolean isVolatile,
                     boolean primitive) {
        super(name, value, type, variablePath, primitive);
        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.isTransient = isTransient;
        this.isVolatile = isVolatile;
    }

    public FieldImpl() {
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public void setFinal(boolean value) {
        this.isFinal = value;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public void setStatic(boolean value) {
        this.isStatic = value;
    }

    @Override
    public boolean isTransient() {
        return isTransient;
    }

    @Override
    public void setTransient(boolean value) {
        this.isTransient = value;
    }

    @Override
    public boolean isVolatile() {
        return isVolatile;
    }

    @Override
    public void setVolatile(boolean value) {
        this.isVolatile = value;
    }

    @Override
    public String toString() {
        return "FieldImpl{" +
               "name='" + getName() + '\'' +
               ", value='" + getValue() + '\'' +
               ", type='" + getType() + '\'' +
               ", variablePath=" + getVariablePath() +
               ", final=" + isFinal +
               ", static=" + isStatic +
               ", transient=" + isTransient +
               ", volatile=" + isVolatile +
               ", primitive=" + isPrimitive() +
               '}';
    }
}