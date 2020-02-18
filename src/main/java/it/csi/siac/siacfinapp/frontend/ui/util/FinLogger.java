/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import org.apache.log4j.Logger;


/**
* Classe di accesso a qualsiasi operazione di logging.
*
* Questa implementazione  basata sull'incapsulamento dell'oggetto log4j di
* classe org.apache.log4j.Logger.
*
*/
public class FinLogger  {

		//name
		String name;
		
		//logger
		Logger log4j;

		public static FinLogger getLogger(String name) {
			return new FinLogger(name);
		}
		
		public static FinLogger getLoggerCSV(String name) {
			return new FinLogger(name) {

				@Override
				protected String composeMessage(String classe, String metodo, Object... message) {
					//compone il messaggio
					StringBuilder msg = new StringBuilder(40);
					Thread me = Thread.currentThread();
					msg.append("thId=").append(me.getId()).append(";").append(classe).append(";").append(metodo).append(";");
					if (message != null){
						for (Object obj : message){
							msg.append(obj).append(";");
						}	
					}	
					return msg.toString();
				}
				
			};
		}

		protected FinLogger(String name) {
			this.log4j = Logger.getLogger(name);
		}

		public void fatal(String classe, String metodo, Object... message) {
			this.log4j.fatal(composeMessage(classe, metodo, message));
		}

		public void fatal(String classe, String metodo, Throwable t, Object... message) {
			this.log4j.fatal(composeMessage(classe, metodo, message), t);
		}

		public void error(String message) {
			this.log4j.error(message);
		}

		public void error(String message, Throwable t) {
			String exc = decodeException(t);
			this.log4j.error(message + exc);
		}

		public void error(String classe, String metodo, String message) {
			this.log4j.error(composeMessage(classe, metodo, message));
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
		
		public void error(String classe, String metodo, String message, Throwable t) {
			String exc = decodeException(t);
			this.log4j.error(composeMessage(classe, metodo, message) + exc);
		}

		public void error(String classe, String metodo, Throwable t) {
			String exc = decodeException(t);
			this.log4j.error(composeMessage(classe, metodo) + exc);
		}

		public void error(String classe, String metodo, Throwable t, Object... message) {
			this.log4j.error(composeMessage(classe, metodo, message), t);
		}

		public void warn(String classe, String metodo, Object... message) {
			this.log4j.warn(composeMessage(classe, metodo, message));
		}

		public void warn(String classe, String metodo, Throwable t, Object... message) {
			this.log4j.warn(composeMessage(classe, metodo, message), t);
		}

		public void info(String classe, String metodo, Object... message) {
			if (this.log4j.isInfoEnabled()) {
				this.log4j.info(composeMessage(classe, metodo, message));
			}
		}

		public void info(String classe, String metodo, Throwable t, Object... message) {
			if (this.log4j.isInfoEnabled()) {
				this.log4j.info(composeMessage(classe, metodo, message), t);
			}
		}

		public void debug(String classe, String metodo, Object... message) {
			if (this.log4j.isDebugEnabled()) {
				this.log4j.debug(composeMessage(classe, metodo, message));
			}
		}

		public void debug(String classe, String metodo, Throwable t, Object... message) {
			if (this.log4j.isDebugEnabled()) {
				this.log4j.debug(composeMessage(classe, metodo, message), t);
			}
		}

		/**
		 * Metodo che compone in maniera omogenea per tutti i livelli il messaggio completo da visualizzare
		 * 
		 * @param classe classe che ha invocato il logger
		 * @param metodo metodo nel quale  stata effettuata la chiamata
		 * @param message messaggio associato
		 * @return
		 */
		protected String composeMessage(String classe, String metodo, Object... message) {
         StringBuilder msg = new StringBuilder(40);
         Thread me = Thread.currentThread();
			msg.append("thId=").append(me.getId()).append(" [").append(classe).append("::").append(metodo).append("] ");
			if (message != null){
				for (Object obj : message){
					msg.append(obj);
				}	
			}	
			return msg.toString();
		}

		public void fatal(String message) {
			this.log4j.fatal(message);
		}

		public void fatal(String message, Throwable t) {
			this.log4j.fatal(message, t);
		}

		public void warn(String message) {
			this.log4j.warn(message);
		}

		public void warn(String message, Throwable t) {
			this.log4j.warn(message, t);
		}

		public void info(String message) {
			this.log4j.info(message);
		}

		public void info(String message, Throwable t) {
			this.log4j.info(message, t);
		}

		public void debug(String message) {
			this.log4j.debug(message);
		}

		public void debug(String message, Throwable t) {
			this.log4j.debug(message, t);
		}

		public boolean isDebugEnabled() {
			return this.log4j.isDebugEnabled();
		}
		
		public boolean isInfoEnabled() {
			return this.log4j.isInfoEnabled();
		}
		//classe		
}