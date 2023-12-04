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

public class RedirectMessageInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -2546103329621884813L;

	enum RedirectMessageInterceptorCacheType { MESSAGES, ERRORS, WARNINGS };
	
	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		GenericFinAction gfa = null;
		if (invocation.getAction() instanceof GenericFinAction) {
			gfa = (GenericFinAction)invocation.getAction();
			
			Map<RedirectMessageInterceptorCacheType, Collection<String>> cache = gfa.getSessionHandler().getParametro(FinSessionParameter.MESSAGES);
			
			if (cache != null && !cache.isEmpty()) {
				Collection<String> coll = cache.get(RedirectMessageInterceptorCacheType.ERRORS);
				if (coll != null) {
					gfa.setActionErrors(coll);
				}
				coll = cache.get(RedirectMessageInterceptorCacheType.MESSAGES);
				if (coll != null) {
					gfa.setActionMessages(coll);
				}
				coll = cache.get(RedirectMessageInterceptorCacheType.WARNINGS);
				if (coll != null) {
					gfa.setActionWarnings(coll);
				}
				gfa.getSession().remove(FinSessionParameter.MESSAGES);
			}
		}

		String result = invocation.invoke();
		
		if (gfa != null) {
			Map<RedirectMessageInterceptorCacheType, Collection<String>> cache = new HashMap<RedirectMessageInterceptorCacheType, Collection<String>>(0);
			if (gfa.hasPersistentActionErrors()) {
				cache.put(RedirectMessageInterceptorCacheType.ERRORS, gfa.getActionErrors());
			}
			if (gfa.hasPersistentActionMessages()) {
				cache.put(RedirectMessageInterceptorCacheType.MESSAGES, gfa.getActionMessages());
			}
			if (gfa.hasPersistentActionWarnings()) {
				cache.put(RedirectMessageInterceptorCacheType.WARNINGS, gfa.getActionWarnings());
			}
			if (!cache.isEmpty()) {
				gfa.getSessionHandler().setParametro(FinSessionParameter.MESSAGES, cache);
			}
		}
		return result;
	}

}
