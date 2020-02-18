/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaMutuoAction extends WizardRicercaMutuoAction{

	
	private static final long serialVersionUID = 1L;
	
	private String codiceMutuoAnnullato;
	private String codiceMutuo;
	/**
	 * chiamato dal tasto annulla di elenco mutuo
	 * tramite la methodName=..
	 */
	public String execute() throws Exception {
		
		debug("execute", "da annullare --> "+getCodiceMutuoAnnullato());
		
		AnnullaMutuo annullaMutuo = new AnnullaMutuo();
		Mutuo mutuo = new Mutuo();
		mutuo.setCodiceMutuo(getCodiceMutuoAnnullato());
		annullaMutuo.setMutuoDaAnnullare(mutuo);
		annullaMutuo.setRichiedente(sessionHandler.getRichiedente());
		annullaMutuo.setEnte(sessionHandler.getEnte());
		
		// Jira-957
		// altrimenti cerca puntuale
		AnnullaMutuoResponse response =  mutuoService.annullaMutuo(annullaMutuo);
		if(response.isFallimento()) {
			if(null!=response.getErrori() && null!=response.getErrori().get(0)){
				addPersistentActionError(response.getErrori().get(0).getCodice()+" "+response.getErrori().get(0).getDescrizione());
			}
			return GOTO_ELENCO_MUTUI;
		}	
		
		// ricarico la lista aggiornata
		if(model.getResultSize()>1){
	          setCodiceMutuo(null);
	
		}
		// rilancia la ricerca per ricaricare i dati aggiornati
		executeRicerca();
		
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
	            + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return GOTO_ELENCO_MUTUI;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getCodiceMutuoAnnullato() {
		return codiceMutuoAnnullato;
	}

	public void setCodiceMutuoAnnullato(String codiceMutuoAnnullato) {
		this.codiceMutuoAnnullato = codiceMutuoAnnullato;
	}



	public String getCodiceMutuo() {
		return codiceMutuo;
	}

	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}	
}
