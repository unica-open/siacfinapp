/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.commons;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.action.RedirectToCruscottoAction;


/**
 * wrappa la redirect della common e mi serve
 * per pulire le robe in sessione, ad esempio i valori delle action con scope=session
 * 
 * @author paolos
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class FinRedirectCruscottoAction extends RedirectToCruscottoAction{
	
	
	private static final long serialVersionUID = -5614038111236579398L;

	String esci="";
	
	@Override
	public String execute() throws Exception{
		
		String result ="";
		
		// pulisco la sessione
		sessionHandler.cleanAll();
		
		
		if(getSession()!=null && !getSession().isEmpty()){
			log.debug("execute", "la sessione non e' stata ripulita completamente, la invalido! " );
			
			if(!esci.isEmpty()){
				result = "redirectCruscotto";
			}else result = SUCCESS;
		}
		
		return result;
	}

	/**
	 * @return the esci
	 */
	public String getEsci() {
		return esci;
	}

	/**
	 * @param esci the esci to set
	 */
	public void setEsci(String esci) {
		this.esci = esci;
	}
	
	
	 
}