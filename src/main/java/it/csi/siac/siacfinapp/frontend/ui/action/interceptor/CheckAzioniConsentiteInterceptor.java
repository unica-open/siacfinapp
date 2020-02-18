/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;

/**
 * Interceptor per la gestione del check sulle azioni consentite.
 * Da posizionare tra i primi interceptor dello stack
 * 
 * @author luca.romanello
 *
 */
public class CheckAzioniConsentiteInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static final String UNAUTHORIZED = "unauthorized";

	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		GenericFinAction gfa = null;
		// nel caso in cui la action invocata sia del tipo GenericFinAction, devo effettuare la verifica
		if (invocation.getAction() instanceof GenericFinAction) {
			gfa = (GenericFinAction)invocation.getAction();
			// nel caso in cui il check ritorna risultato false, bisogna ridirezionare a una pagina di errore
			if (!gfa.checkAllAzioni()) result = UNAUTHORIZED;
		}
		// a questo punto, result == null solo se 1. il check non e' stato invocato o 2. e' stato invocato e ha restituito true
		result = result != null ? result : invocation.invoke();
		return result;
	}

}
