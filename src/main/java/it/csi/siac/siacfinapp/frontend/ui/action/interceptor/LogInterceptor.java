/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommon.util.log.LogUtil;

public class LogInterceptor extends AbstractInterceptor{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4619938649975264689L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final String methodName = invocation.getProxy().getMethod();
		
		// Inserito all'interno del metodo in quanto non utilizzato altrove
		
		LogUtil log = new LogUtil(invocation.getAction().getClass());
		
		//start del debug
		log.debugStart(methodName, "");
		
		//inizializzo la variabile che dovra'
		//leggere il risultato dell'invocazione:
		String risultatoInvocazione = null;
		
		try{
			//invocazione:
			risultatoInvocazione = invocation.invoke();
		} catch(Exception e) {
			//errore
			log.error(methodName, "Errore nell'invocazione: " + e.getMessage(), e);
			throw e;
		} finally {
			//end
			log.debugEnd(methodName, "Risultato invocazione: " + risultatoInvocazione);
		}
		
		//ritorniamo il risultato:
		return risultatoInvocazione;
	}

}
