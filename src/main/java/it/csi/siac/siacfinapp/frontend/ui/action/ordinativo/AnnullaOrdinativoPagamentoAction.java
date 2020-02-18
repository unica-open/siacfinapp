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
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaOrdinativoPagamentoAction extends WizardRicercaOrdinativoAction {
	
	private static final long serialVersionUID = 1L;

	//numero ordinativo pagamento annullato 
	private Integer numeroOrdinativoPagamentoAnnullato;
	
	//anno ordinativo pagamento annullato 
	private Integer annoOrdinativoPagamentoAnnullato;

	/**
	 * Execute di pagina
	 */
	public String execute() throws Exception {
		
		//istanzio la request per il servizio annullaOrdinativoPagamento:
		AnnullaOrdinativoPagamento annullaOrdinativoPagamento=new AnnullaOrdinativoPagamento();
		
		//setto i dati nella request:
		OrdinativoPagamento ordinativoPagamento=new OrdinativoPagamento();
		ordinativoPagamento.setAnno(getAnnoOrdinativoPagamentoAnnullato());
		ordinativoPagamento.setNumero(getNumeroOrdinativoPagamentoAnnullato());
		annullaOrdinativoPagamento.setOrdinativoPagamentoDaAnnullare(ordinativoPagamento);
		annullaOrdinativoPagamento.setEnte(sessionHandler.getEnte());
		annullaOrdinativoPagamento.setRichiedente(sessionHandler.getRichiedente());
		annullaOrdinativoPagamento.setBilancio(sessionHandler.getBilancio());
		
		// richiamo il servizio di annulla ordinativo
		AnnullaOrdinativoPagamentoResponse response =  ordinativoService.annullaOrdinativoPagamento(annullaOrdinativoPagamento);
		
		//analizzo la response:
		if(response.isFallimento()) {
			if(null!=response.getErrori() && null!=response.getErrori().get(0)){
				// ci sono errori
				addPersistentActionError(response.getErrori().get(0).getCodice()+" "+response.getErrori().get(0).getDescrizione());
			}
			return "gotoElencoOrdinativoPagamento";
		}else if(response.hasErrori()){
			// ci sono warning
			List<Errore> erroriWarning=response.getErrori();
			for (Errore erroreWarning : erroriWarning) {
				addPersistentActionWarning(erroreWarning.getCodice()+" "+erroreWarning.getDescrizione());
			}
		}
		
		for (Messaggio messaggio : response.getMessaggi()) {
			addPersistentActionWarning(messaggio.getCodice()+" - "+messaggio.getDescrizione());
		}
		
		//tutto ok:
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return "gotoElencoOrdinativoPagamento";
	}

	//GETTER E SETTER:
	
	public Integer getNumeroOrdinativoPagamentoAnnullato() {
		return numeroOrdinativoPagamentoAnnullato;
	}

	public void setNumeroOrdinativoPagamentoAnnullato(Integer numeroOrdinativoPagamentoAnnullato) {
		this.numeroOrdinativoPagamentoAnnullato = numeroOrdinativoPagamentoAnnullato;
	}

	public Integer getAnnoOrdinativoPagamentoAnnullato() {
		return annoOrdinativoPagamentoAnnullato;
	}

	public void setAnnoOrdinativoPagamentoAnnullato(Integer annoOrdinativoPagamentoAnnullato) {
		this.annoOrdinativoPagamentoAnnullato = annoOrdinativoPagamentoAnnullato;
	}
}