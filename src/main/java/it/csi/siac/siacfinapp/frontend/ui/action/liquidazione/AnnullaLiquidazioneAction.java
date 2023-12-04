/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAtto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAttoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AnnullaLiquidazioneAction extends WizardRicercaLiquidazioneAction{

	
	private static final long serialVersionUID = 1L;
	
	//numero liquidazione annullata
	private BigDecimal numeroLiquidazioneAnnullato;
	
	//anno liquidazione annullata
	private Integer annoLiquidazioneAnnullato;
	
	//num liq
	private BigDecimal numeroLiquidazione;
	
	//anno liq
	private Integer annoLiquidazione;
	
	//numero pagina
	private Integer numeroPaginaElenco;
	
	/**
	 * Metodo execute di pagina
	 */
	public String execute() throws Exception {
		
		//settiamo da annulla a true:
		model.setFromAnnulla(true);
		
		debug("execute", "da annullare --> "+getAnnoLiquidazione()+" / "+getNumeroLiquidazioneAnnullato());
		
		// CR - 3503
		// Prima di eseguire l'annulla liquidazione controllo che essa non derivi da allegato atto
		
		//istanzio la request per il servizio ricercaLiquidazioneConAllegatoAtto:
		RicercaLiquidazioneConAllegatoAtto req = new RicercaLiquidazioneConAllegatoAtto();
		req.setEnte(sessionHandler.getEnte());
		req.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		req.setRichiedente(sessionHandler.getRichiedente());
		
		Liquidazione liquidazioneDaVerificare = new Liquidazione();
		liquidazioneDaVerificare.setAnnoLiquidazione(getAnnoLiquidazioneAnnullato());
		liquidazioneDaVerificare.setNumeroLiquidazione(getNumeroLiquidazioneAnnullato());
		req.setLiquidazione(liquidazioneDaVerificare);
		
		//invoco il servizio ricercaLiquidazioneConAllegatoAtto:
		RicercaLiquidazioneConAllegatoAttoResponse res = liquidazioneService.ricercaLiquidazioneConAllegatoAtto(req);
		
		//analizzo la response del servizio ricercaLiquidazioneConAllegatoAtto:
		
		if(res!=null && res.isFallimento()){
			if(null!=res.getErrori() && null!=res.getErrori().get(0)){
				// ci sono errori
				//task-263
				addErrore(res.getErrori().get(0).getCodice()+" "+res.getErrori().get(0).getDescrizione(), res.getErrori().get(0));
				return "gotoElencoLiquidazioni";
			}
		}
		
		//istanzio la request per il servizio annullaLiquidazione:
		AnnullaLiquidazione annullaLiquidazione = new AnnullaLiquidazione();
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(getAnnoLiquidazioneAnnullato());
		liquidazione.setNumeroLiquidazione(getNumeroLiquidazioneAnnullato());
		annullaLiquidazione.setLiquidazioneDaAnnullare(liquidazione);
		annullaLiquidazione.setRichiedente(sessionHandler.getRichiedente());
		annullaLiquidazione.setEnte(sessionHandler.getEnte());
		annullaLiquidazione.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
		
		setNumeroLiquidazione(getNumeroLiquidazioneAnnullato());
		setAnnoLiquidazione(getAnnoLiquidazioneAnnullato());
		
		//invoco il servizio annullaLiquidazione:
		AnnullaLiquidazioneResponse response =  liquidazioneService.annullaLiquidazione(annullaLiquidazione);
		
		//analizzo la response del servizio annullaLiquidazione:
		
		if(response.isFallimento()) {
			if(null!=response.getErrori() && null!=response.getErrori().get(0)){
				// ci sono errori
				//task-263
				addErrore(res.getErrori().get(0).getCodice()+" "+res.getErrori().get(0).getDescrizione(), res.getErrori().get(0));
				
			}
			return "gotoElencoLiquidazioni";
		}	
		
		//OPERAZIONE OK:
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return "gotoElencoLiquidazioni";
	}

	//GETTER E SETTER:
	
	public BigDecimal getNumeroLiquidazioneAnnullato() {
		return numeroLiquidazioneAnnullato;
	}

	public void setNumeroLiquidazioneAnnullato(BigDecimal numeroLiquidazioneAnnullato) {
		this.numeroLiquidazioneAnnullato = numeroLiquidazioneAnnullato;
	}

	public Integer getAnnoLiquidazioneAnnullato() {
		return annoLiquidazioneAnnullato;
	}

	public void setAnnoLiquidazioneAnnullato(Integer annoLiquidazioneAnnullato) {
		this.annoLiquidazioneAnnullato = annoLiquidazioneAnnullato;
	}

	public BigDecimal getNumeroLiquidazione() {
		return numeroLiquidazione;
	}

	public void setNumeroLiquidazione(BigDecimal numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}

	public Integer getAnnoLiquidazione() {
		return annoLiquidazione;
	}

	public void setAnnoLiquidazione(Integer annoLiquidazione) {
		this.annoLiquidazione = annoLiquidazione;
	}
	public Integer getNumeroPaginaElenco() {
		return numeroPaginaElenco;
	}
	public void setNumeroPaginaElenco(Integer numeroPaginaElenco) {
		this.numeroPaginaElenco = numeroPaginaElenco;
	}
	
}
