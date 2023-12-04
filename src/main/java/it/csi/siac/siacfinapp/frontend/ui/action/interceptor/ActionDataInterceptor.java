/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.interceptor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumbInterceptor;
import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;

/*
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class ActionDataInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	private transient LogWebUtil log = new LogWebUtil(this.getClass());
	
	private static Map<String, Method> METHOD_CACHE = new HashMap<String, Method>();
	
	//metodi da escludere
	private String excludeMethods;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		final String methodName = "intercept";
		debug(methodName, "Start");
		Object a = actionInvocation.getAction();
		GenericFinAction gfa = null;
		String actionKey = null;
		String[] keys = null;
		PropertyDescriptor[] pd = null;
		boolean management = true;
		if (a instanceof GenericFinAction) {
			// Prendo dalla sessione gli action data, se presenti per la action, e li setto
			gfa = (GenericFinAction)a;
			actionKey = gfa.getActionKey();
			if (actionKey != null && actionKey.length() > 0) {
				String ks = gfa.getActionDataKeys();
				if (ks != null && ks.length() > 0) {
					keys = ks.split(",");
					BeanInfo info = Introspector.getBeanInfo(gfa.getClass());
					pd = info.getPropertyDescriptors();
					management = isManagement(excludeMethods, actionInvocation) || isManagement(gfa.getExcludedMethods(), actionInvocation);
					debug(methodName, "ActionKey:", actionKey, "Keys:", ks, "Management:", management);
					if (management) {
						Map<String, Object> data = getActionData(actionKey, gfa.getSession());
						outer:
						for (String current : keys) {
							for (PropertyDescriptor currentPD : pd) {
								if (current.trim().equals(currentPD.getName())) {
									Method m = currentPD.getWriteMethod();
									if (m != null) {
										debug(methodName, "Attribute [", current,"] found with set method available, value", data.get(current));
										m.invoke(gfa, data.get(current));
									} else {
										debug(methodName, "Attribute [", current,"] found but no set method available, searching with reflection");
										String key = actionKey + "_" + current;
										Method method = METHOD_CACHE.get(key);
										if (method != null) {
											method.invoke(gfa, data.get(current));
										} else {
											Method[] ms = gfa.getClass().getMethods();
											String mName = "set" + Character.toUpperCase(current.trim().charAt(0)) + current.trim().substring(1);
											for (Method currentM : ms) {
												if (currentM.getName().equals(mName)) {
													METHOD_CACHE.put(key, currentM);
													currentM.invoke(gfa, data.get(current));
													continue outer;
												}
											}
										}
									}
									continue outer;
								}
							}
						}
						debug(methodName, "Saved from action data");
					}
				}
			}
		}
		
		//RIMOZIONE DELLE CRUMB DA FILO DI ARIANNA
		
		if(gfa!=null && gfa.getSession().get(BreadCrumbInterceptor.CRUMB_KEY)!= null){
			BreadCrumbTrail aas=(BreadCrumbTrail)gfa.getSession().get(BreadCrumbInterceptor.CRUMB_KEY);
			gestioneCrumb(aas,actionKey,gfa);
		}
		String result = actionInvocation.invoke();
		if (gfa != null && actionKey != null && keys != null) {
			// Prendo dalla action gli action data e li metto in sessione
			switch (gfa.getActionDataMode()) {
			case SAVE:
				Map<String, Object> data = getActionData(actionKey, gfa.getSession());
				for (String current : keys) {
					for (PropertyDescriptor currentPD : pd) {
						if (current.trim().equals(currentPD.getName())) {
							//elemento trovato
							Object v = currentPD.getReadMethod().invoke(gfa);
							debug(methodName, "Saving [", current, "], value", v);
							data.put(current, v);
							break;
						}
					}
				}
				debug(methodName, "Saved to action data");
				break;
			case CLEAR:
				getActionDataContainer(gfa.getSession()).remove(actionKey);
				debug(methodName, "Cleared action data");
				break;
			case IGNORE:
				debug(methodName, "Ignored action data");
			}
			debug(methodName, "End with action data management");
		} else {
			debug(methodName, "End without action data management");
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getActionDataContainer(Map<String, Object> session) {
		Map<String, Object> adatas = (Map<String, Object>)session.get(FinSessionParameter.ACTION_DATA.getName());
		if (adatas == null) {
			//la mappa e' nulla, devo istanziarla:
			adatas = new HashMap<String, Object>(1);
			session.put(FinSessionParameter.ACTION_DATA.getName(), adatas);
		}
		//ritorno la mappa:
		return adatas;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getActionData(String key, Map<String, Object> session) {
		Map<String, Object> data = null, adatas = getActionDataContainer(session);
		data = (Map<String, Object>)adatas.get(key);
		if (data == null) {
			//la mappa e' nulla, devo istanziarla:
			data = new HashMap<String, Object>();
			adatas.put(key, data);
		}
		//ritorno la mappa:
		return data;
	}
	
	protected void debug(String methodName, Object... parameters) {
		if (log.isDebugEnabled()){
			//degub attivo
			log.debug(methodName, getLogMessage(parameters));
		}
	}
	
	private String getLogMessage(Object... parameters) {
		if (parameters == null || parameters.length == 0) return "";
		StringBuilder logMessage = new StringBuilder();
		//ciclo sui parameters:
		for (int i=0; i<parameters.length; i++) {
			logMessage.append(parameters[i]).append(i == parameters.length - 1 ? "" : " ");
		}
		//restituisco l'oggetto da ritornare:
		return logMessage.toString();
	}
	
	private boolean isManagement(String excludeMethods, ActionInvocation actionInvocation) {
		boolean management = true;
		if (excludeMethods != null && excludeMethods.trim().length() > 0) {
			String[] ems = excludeMethods.split(",");
			ActionProxy proxy = actionInvocation.getProxy();
			for (String em : ems) {
				if (em.trim().equalsIgnoreCase(proxy.getMethod())) {
					//elemento trovato
					management = false;
					break;
				}
			}
		}
		//restituisco l'oggetto da ritornare:
		return management;
	}
	
	@SuppressWarnings("unchecked")
	protected <M1 extends GenericFinModel, M2 extends GenericFinModel> void gestioneCrumb(BreadCrumbTrail breadCrumb, String actionKey, GenericFinAction<M1> gfaAction){
		boolean check = false;
		ActionProxy proxy = ActionContext.getContext().getActionInvocation().getProxy();
		String nameAK= proxy.getActionName();
		String nameSpaceAK= proxy.getNamespace();
		GenericFinAction<M2> gfac= null;
		
		/* task-155 : con check a true errori su tasto indietro
		 *  (quando aggiunge Action per il recupero del bean - es. _doExecuteAction)
		 */
		/*
		//itero:
		for(Crumb c: breadCrumb.getCrumbs()){
			if(check){
				//check true
				gfac = (GenericFinAction<M2>) gfaAction.getApplicationContext().getBean(c.getAction() + "Action");
				String actionKeyCrumb= gfac.getActionKey();
				getActionDataContainer(gfaAction.getSession()).remove(actionKeyCrumb);
			}
			
			if(nameAK.equals(c.getAction()) && nameSpaceAK.equals(c.getNamespace())){
				//set del check a true:
				check=true;
			} 
		} */
	}
	
	//GETTER E SETTER:
	
	public String getExcludeMethods() {
		return excludeMethods;
	}

	public void setExcludeMethods(String excludeMethods) {
		this.excludeMethods = excludeMethods;
	}

	
}
