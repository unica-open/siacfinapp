/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaOrdinativoIncassoAction extends WizardRicercaOrdinativoAction {
	
	private static final long serialVersionUID = 1L;

	//anno e numero:
	private Integer numeroOrdinativoIncassoAnnullato;
	private Integer annoOrdinativoIncassoAnnullato;

	public String execute() throws Exception {
		
		//istanzio la request per il servizio annullaOrdinativoIncasso:
		AnnullaOrdinativoIncasso annullaOrdinativoIncasso=new AnnullaOrdinativoIncasso();
		
		//setto i dati dell'ordinativo che intendo annullare:
		OrdinativoIncasso ordinativoIncasso=new OrdinativoIncasso();
		ordinativoIncasso.setAnno(getAnnoOrdinativoIncassoAnnullato());
		ordinativoIncasso.setNumero(getNumeroOrdinativoIncassoAnnullato());
		annullaOrdinativoIncasso.setOrdinativoIncassoDaAnnullare(ordinativoIncasso);
		annullaOrdinativoIncasso.setEnte(sessionHandler.getEnte());
		annullaOrdinativoIncasso.setRichiedente(sessionHandler.getRichiedente());
		annullaOrdinativoIncasso.setBilancio(sessionHandler.getBilancio());
		
		//invoco il servizio annullaOrdinativoIncasso:
		AnnullaOrdinativoIncassoResponse response =  ordinativoService.annullaOrdinativoIncasso(annullaOrdinativoIncasso);
		
		//analizzo l'esisto del serivio:
		if(response.isFallimento()) {
			//esito negatito
			if(null!=response.getErrori() && null!=response.getErrori().get(0)){
				addPersistentActionError(response.getErrori().get(0).getCodice()+" "+response.getErrori().get(0).getDescrizione());
			}
			return "gotoElencoOrdinativoIncasso";
		}else if(response.hasErrori()){
			//esito non fallimentare ma con dei warning
			List<Errore> erroriWarning=response.getErrori();
			for (Errore erroreWarning : erroriWarning) {
				addPersistentActionWarning(erroreWarning.getCodice()+" "+erroreWarning.getDescrizione());
			}
		}
		
		for (Messaggio messaggio : response.getMessaggi()) {
			addPersistentActionWarning(messaggio.getCodice()+" - "+messaggio.getDescrizione());
		}
		//SIAC-6365
		
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
	
		return "gotoElencoOrdinativoIncasso";
	}

	// GETTER E SETTER:
	
	public Integer getNumeroOrdinativoIncassoAnnullato() {
		return numeroOrdinativoIncassoAnnullato;
	}

	public void setNumeroOrdinativoIncassoAnnullato(
			Integer numeroOrdinativoIncassoAnnullato) {
		this.numeroOrdinativoIncassoAnnullato = numeroOrdinativoIncassoAnnullato;
	}

	public Integer getAnnoOrdinativoIncassoAnnullato() {
		return annoOrdinativoIncassoAnnullato;
	}

	public void setAnnoOrdinativoIncassoAnnullato(Integer annoOrdinativoIncassoAnnullato) {
		this.annoOrdinativoIncassoAnnullato = annoOrdinativoIncassoAnnullato;
	}
}