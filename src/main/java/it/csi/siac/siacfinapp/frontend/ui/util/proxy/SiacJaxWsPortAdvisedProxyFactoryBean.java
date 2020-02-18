/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.proxy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.jaxws.JaxWsPortClientInterceptor;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Factory bean per il proxy verso JAX-WS. Con la presente classe &eacute;
 * possibile definire molteplici advice che saranno eseguiti sequenzialmente
 * prima dell'invocazione del servizio.
 * <p>
 * La definizione del bean per la presente classe deve avere un tag
 * <code>&lt;property name="advices"/&gt;</code> contenente una sequenza di
 * classi implementanti l'interfaccia {@link Advice}, completamente qualificate.
 * </p>
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/08/2015
 * @see JaxWsPortProxyFactoryBean
 *
 */
public class SiacJaxWsPortAdvisedProxyFactoryBean extends JaxWsPortClientInterceptor implements FactoryBean<Object> {

	private static final LogUtil LOG = new LogUtil(SiacJaxWsPortAdvisedProxyFactoryBean.class);

	private Object serviceProxy;
	private final List<Class<?>> advices = new ArrayList<Class<?>>();
	private boolean logSOAPMessages = false;

	/**
	 * Sets the advices: treats the string as comma-separed values and obtains the
	 * class defined for each value.
	 * 
	 * @param advices the services to set
	 */
	public void setAdvices(String advices) {
		if (advices == null) {
			return;
		}
		String[] split = advices.split("\\s*,\\s*");
		for (String splinter : split) {
			if (StringUtils.isNotBlank(splinter)) {
				try {
					this.advices.add(Class.forName(splinter));
				} catch (ClassNotFoundException e) {
					// Salto la classe fornita
					LOG.warn("setAdvices", "Class not found for name " + splinter + ": it shall be ignored");
				}
			}
		}
	}

	/**
	 * Sets the property telling whether to lof the SOAP messages.
	 * 
	 * @param logSOAPMessages whether to log the SOAP messages
	 */
	public void setLogSOAPMessages(boolean logSOAPMessages) {
		this.logSOAPMessages = logSOAPMessages;
	}

	@Override
	public Object getObject() {
		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() {
		if (logSOAPMessages) {
			addLogHandler();
		}
		super.afterPropertiesSet();
		createProxy();
	}

	/**
	 * Aggiunge l'handler di logging per le richieste in- e out-bound.
	 */
	private void addLogHandler() {
		SiacHandlerResolver hr = new SiacHandlerResolver();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = new ArrayList<Handler>();
		handlers.add(new LoggingHandler());
		hr.setHandlerList(handlers);
		setHandlerResolver(hr);
	}

	/**
	 * Crea il proxy del servizio
	 */
	private void createProxy() {
		ProxyFactory pf = new ProxyFactory();
		pf.addInterface(getServiceInterface());
		pf.addInterface(BindingProvider.class);

		addAdvices(pf);

		this.serviceProxy = pf.getProxy(getBeanClassLoader());
	}

	/**
	 * Aggiunge i var&icirc; advice alla proxy factory.
	 * 
	 * @param proxyFactory la proxyFactory da popolare
	 */
	protected void addAdvices(ProxyFactory proxyFactory) {
		for (Class<?> clazz : advices) {
			try {
				Advice advice = (Advice) clazz.getDeclaredConstructor().newInstance();
				proxyFactory.addAdvice(advice);
			} catch (InstantiationException e) {
				LOG.warn("addAdvices", "InstantiationException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			} catch (IllegalAccessException e) {
				LOG.warn("addAdvices", "IllegalAccessException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			} catch (IllegalArgumentException e) {
				LOG.warn("addAdvices", "IllegalArgumentException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			} catch (SecurityException e) {
				LOG.warn("addAdvices", "SecurityException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			} catch (InvocationTargetException e) {
				LOG.warn("addAdvices", "InvocationTargetException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			} catch (NoSuchMethodException e) {
				LOG.warn("addAdvices", "NoSuchMethodException for class " + clazz.getName() + ": it shall be ignored");
				// Ignore, for now
			}
		}
		proxyFactory.addAdvice(this);
	}

}
