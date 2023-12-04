/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an action whose Model is to be put in session.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/07/2014
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PutModelInSession {
	
	/**
     * Name of the session attribute to be stored in session.
     * <br>
     * If no name is supplied, defaults to the actionName.
     */
    String value() default "";
}
