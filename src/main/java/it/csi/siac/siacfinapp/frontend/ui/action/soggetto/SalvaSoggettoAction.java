/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;


/**
 * Action per la gestione del inserimento soggetto step3
 * 
 * @author paolos
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SalvaSoggettoAction<M extends SoggettoModel> extends WizardScriviSoggettoAction {
	
	private static final long serialVersionUID = 1L;

	/**
	 * prepare della action
	 * @throws Exception
	 */
	@Override
	public void prepare() throws Exception {
		clearErrorsAndMessages();
	}
	
	/**
	 * execute della action
	 * @return
	 */
	@Override
	public String execute() {
		
		final String methodName = "execute";
		
		debug(methodName, "BEGIN");
		
		if(!model.isFallimento()){
			//info per debug:
			log.debug(methodName, "OK INSERIMENTO");
			// I dati sono stati salvati correttamente.
			//E' stato inserito un soggetto "VALIDO
			StringBuffer sb = new StringBuffer("I dati sono stati salvati correttamente. E' stato inserito un soggetto ")
			.append(model.getStatoSoggetto())
			.append(" con codice ").append(model.getCodiceSoggetto());
			
			//aggiungo il messaggio:
			addActionMessage(sb.toString());
		}

		//pulisco i dati del model:
		clearActionData();
		
		//info per debug:
		log.debugEnd(methodName, "");
		
		return SUCCESS;
	}
	
	/**
	 * per tornare indietro
	 * @return
	 */
	public String indietroStep2() {
		final String methodName = "indietroStep2";
		//info per debug:
		log.debugEnd(methodName, "");
		return "indietroStep2";
	}
}
