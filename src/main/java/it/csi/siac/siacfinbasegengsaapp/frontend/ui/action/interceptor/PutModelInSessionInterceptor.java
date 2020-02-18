/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.interceptor;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommonapp.action.GenericAction;
import it.csi.siac.siaccommonapp.model.GenericModel;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;


/**
 * The Class PutModelInSessionInterceptor.
 * @author elisa
 * @version 1.0.0 - 24/12/2018
 */
public class PutModelInSessionInterceptor extends AbstractInterceptor{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5863997329211335038L;
	
	private String prefix = "PutModelInSessionInterceptor_";
	private String suffix = "";

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		
		// Parameters
		final Object action = actionInvocation.getAction();
		final Class<?> clazz = action.getClass();
		final PutModelInSession annotation = clazz.getAnnotation(PutModelInSession.class);
		String sessionParameter = "";
		final boolean isAnnotationToProcess = annotation != null && action instanceof GenericAction;
		final HttpSession session = ServletActionContext.getRequest().getSession(false);

		if (isAnnotationToProcess) {
			// Gets the parameter name to put into the session map 
			sessionParameter = getSessionParamName(action, annotation);
			getModelFromSession((GenericAction<?>) action, session, sessionParameter);
		}

		String result = null;

		try {
			result = actionInvocation.invoke();
		} finally {
			if (isAnnotationToProcess) {
				// The sessionParameter was already defined
				putModelInSession((GenericAction<?>) action, session, sessionParameter);
			}
		}

		return result;
		
	}
	
	/**
	 * Obtains the model from the session, if present, and injects it into the Action.
	 * 
	 * @param action    the action into which the model should be set
	 * @param session   the session
	 * @param paramName the name of the session parameter from which to obtain the model
	 * 
	 * @throws NullPointerException if action is null
	 */
	private <M extends GenericModel> void getModelFromSession(GenericAction<M> action, HttpSession session, String paramName) {
		if (session == null) {
			// If no session is present, we have nothing to obtain
			return;
		}

		@SuppressWarnings("unchecked")
		M model = (M) session.getAttribute(paramName);
		action.setModel(model);
	}

	/**
	 * Obtains the model from the Action and puts it into the session.
	 * 
	 * @param action    the action from which the model should be gotten
	 * @param session   the session
	 * @param paramName the name of the session parameter to which link the model
	 * 
	 * @throws NullPointerException if action is null
	 */
	private <M extends GenericModel> void putModelInSession(GenericAction<M> action, HttpSession session, String paramName) {
		if (session == null || action.getModel() == null) {
			return;
		}

		session.setAttribute(paramName, action.getModel());
	}

	/**
	 * Obtains the param name for the use with the HttpSession.
	 * 
	 * @param action     the object whose session name is to be obtained
	 * @param annotation the annotation from which to obtain the session name, if applicable
	 * 
	 * @return the session name
	 * 
	 * @throws NullPointerException if either action or annotation is null
	 */
	private String getSessionParamName(Object action, PutModelInSession annotation) {
		String sessionParamName = action.getClass().getName();
		if (annotation.value() != null && !annotation.value().isEmpty()) {
			sessionParamName = annotation.value();
			StringUtils.isNotBlank(sessionParamName);
		}

		return prefix + sessionParamName + suffix;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}


}
