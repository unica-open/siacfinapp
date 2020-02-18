/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.interceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
/*
 *   variato perche' non cancella da sessione occhio che potrebbe essere
 *   un problema legato alle common lib
 */
public class RedirectMessageInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static final Integer MESSAGES = 1;
	private static final Integer ERRORS = 2;
	private static final Integer WARNINGS = 3;

	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		GenericFinAction gfa = null;
		if (invocation.getAction() instanceof GenericFinAction) {
			gfa = (GenericFinAction)invocation.getAction();
			Map<Integer, Collection<String>> cache = gfa.getSessionHandler().getParametro(FinSessionParameter.MESSAGES);
			if (cache != null && !cache.isEmpty()) {
				Collection<String> coll = cache.get(ERRORS);
				if (coll != null) {
					gfa.setActionErrors(coll);
				}
				coll = cache.get(MESSAGES);
				if (coll != null) {
					gfa.setActionMessages(coll);
				}
				coll = cache.get(WARNINGS);
				if (coll != null) {
					gfa.setActionWarnings(coll);
				}
				gfa.getSession().remove(FinSessionParameter.MESSAGES); // variato perche' non cancella da sessione
			}
		}
		result = invocation.invoke();
		if (gfa != null) {
			Map<Integer, Collection<String>> cache = new HashMap<Integer, Collection<String>>(0);
			if (gfa.hasPersistentActionErrors()) {
				cache.put(ERRORS, gfa.getActionErrors());
			}
			if (gfa.hasPersistentActionMessages()) {
				cache.put(MESSAGES, gfa.getActionMessages());
			}
			if (gfa.hasPersistentActionWarnings()) {
				cache.put(WARNINGS, gfa.getActionWarnings());
			}
			if (!cache.isEmpty()) {
				gfa.getSessionHandler().setParametro(FinSessionParameter.MESSAGES, cache);
			} else {
				gfa.getSession().remove(FinSessionParameter.MESSAGES); // variato perche' non cancella da sessione
			}
		}
		return result;
	}

}
