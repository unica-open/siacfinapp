/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import it.csi.siac.siaccommon.util.log.LogUtil;


/**
* Classe di accesso a qualsiasi operazione di logging.
*
* Questa implementazione  basata sull'incapsulamento dell'oggetto log4j di
* classe org.apache.log4j.Logger.
*
*/
public class FinCSVLogger extends LogUtil {

	public FinCSVLogger(Class<?> clazz) {
		super(clazz);
	}
	
	@Override
	protected String composeMessage(String methodName, Object message) {
		//compone il messaggio
		StringBuilder msg = new StringBuilder(40);
		Thread me = Thread.currentThread();
		msg.append("thId=").append(me.getId()).append(";").append(className).append(";").append(methodName).append(";");
		if (message != null){
			msg.append(message);
		}	
		return msg.toString();
	}
	
	@Override
	public void error(String methodName, Object message, Throwable t) {
		String exc = decodeException(t);
		super.error(methodName, message + exc, t);
	}

	private static boolean doLog(String cl) {
		if (cl.startsWith("sun.reflect")){
			return false;
		}
		if (cl.startsWith("java.lang.reflect")){
			return false;
		}
		if (cl.startsWith("javax.faces.component")){
			return false;
		}
		if (cl.startsWith("com.opensymphony.xwork2")){
			return false;
		}
		if (cl.startsWith("org.apache.struts2")){
			return false;
		}
		if (cl.startsWith("org.apache.coyote")){
			return false;
		}
		if (cl.startsWith("org.ajax4jsf.component")){
			return false;
		}
		if (cl.startsWith("org.apache.catalina")){
			return false;
		}
		if (cl.startsWith("org.hibernate.cfg")){
			return false;
		}
		if (cl.startsWith("org.hibernate.type")){
			return false;
		}
		if (cl.startsWith("org.hibernate.type")){
			return false;
		}
		return true;
	}

	public static String decodeException(Throwable t) {
		try {
			Throwable cause = t.getCause();
			String clazz = t.getClass().getName();
			StackTraceElement elem[] = t.getStackTrace();
			StringBuilder sb = new StringBuilder(4000);
			sb.append("\nException ").append(clazz);
			if (cause != null){
				sb.append("\nCause ").append(cause);
			}	
			sb.append('\n');
			int i = 0;
			int last = -1;
			for (StackTraceElement e : elem) {
				String cl = e.getClassName();
				boolean out = doLog(cl);
				if (out) {
					if (last < i - 1){
						sb.append("\t... skip ").append("" + (i - last)).append(" stack frame(s)\n");
					}	
					String fn = e.getFileName();
					sb.append("\tat ").append(cl).append('.').append(e.getMethodName());
					if (fn != null){
						sb.append('(').append(e.getFileName()).append(':').append(e.getLineNumber()).append(')');
					}	
					sb.append('\n');
					last = i;
				} 
				i++;
			} 
			return sb.toString();
		} catch (Exception tthh) {
			return "got exception while decoding sorry!";
		}
	}
}