/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.exception;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action per la gestione del fallimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 07/11/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class FailureAction extends ActionSupport implements ServletResponseAware {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6426800689880117433L;
	
	private transient HttpServletResponse response;
	private Exception exception;
	
	@Override
	public String execute() throws Exception {
		// Imposto i parametri di fallimento
		response.setStatus(500);
		
		return SUCCESS;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
