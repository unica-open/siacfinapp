/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class EliminaVoceMutuoAction extends GenericVoceDiMutuoAction {
	
	private static final long serialVersionUID = 1L;
	
	private String numeroVoceMutuoEliminato;
	private String codiceMutuo;
	
	/**
	 * Metodo execute della action
	 */
	public String execute() throws Exception {
		
		//info per degub:
		debug("execute", "da annullare --> "+getNumeroVoceMutuoEliminato());
	
		//Preparo la request per il servizio di aggiornamento del mutuo
		//tramite il quale avverra' l'eliminazione della voce:
		AggiornaMutuo aggiornaMutuo = new AggiornaMutuo();
		
		//ente
		aggiornaMutuo.setEnte(sessionHandler.getEnte());
		
		//richiedente
		aggiornaMutuo.setRichiedente(sessionHandler.getRichiedente());
		
		// clono la lista voci per operare senza problemi
		List<VoceMutuo> listaVociDaNonRimuovere = clone(model.getMutuoSelezionato().getListaVociMutuo());
		
		for (VoceMutuo itVM : model.getMutuoSelezionato().getListaVociMutuo()) {
			if(itVM.getNumeroVoceMutuo().equals(getNumeroVoceMutuoEliminato())){
			    // trovata la voce la elimino
				model.getMutuoSelezionato().getListaVociMutuo().remove(itVM);
				break;
			}
		}
		//ora in model.mutuoSelezionato.listaVociMutuo mi ritrovo
		//tutte le voci tranne quella che dovra' essere eliminita
		aggiornaMutuo.setMutuo(model.getMutuoSelezionato());
		
		// aggiorno il mutuo senza la voce eliminata richiamando il servizio:
		AggiornaMutuoResponse response = mutuoService.aggiornaMutuo(aggiornaMutuo);
		
		//analizzo l'esito del servizio:
		if (response != null && !response.isFallimento()) {
			//tutto ok presento il messaggio di esito positivo:
			setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			return SUCCESS;
		} else {
			//errori nel servizio
			
			// rimetto le eventuali voci che ho cercato di eliminare
			// e che per il quale il back end mi e' andato in errore
			setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
			model.getMutuoSelezionato().setListaVociMutuo(listaVociDaNonRimuovere);
			
			//presento gli errrori:
			addErrori(getMethodName(), response);
			return INPUT;
		}
	
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getNumeroVoceMutuoEliminato() {
		return numeroVoceMutuoEliminato;
	}

	public void setNumeroVoceMutuoEliminato(String numeroVoceMutuoEliminato) {
		this.numeroVoceMutuoEliminato = numeroVoceMutuoEliminato;
	}

	public String getCodiceMutuo() {
		return codiceMutuo;
	}

	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}
}