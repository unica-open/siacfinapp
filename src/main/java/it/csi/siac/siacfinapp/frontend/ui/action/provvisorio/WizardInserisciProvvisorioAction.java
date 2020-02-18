/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassaResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

public class WizardInserisciProvvisorioAction extends AbstractGestioneProvvisiorioDiCassa {

	private static final long serialVersionUID = 1L;
	
	private String numeroProvvStruts;
	private String annoProvvStruts;
	private String tipoProvvStruts;
	
	@Override
	public String getActionKey() {
		return "inserisciProvvisorio";
	}
		
	/**
	 * convertitore utilizzato per la chiamata al servizio di inserimento provvisorio
	 * @return
	 */
	protected InserisciProvvisorioDiCassa convertiModelPerChiamataServizioInserimentoProvvisorio() {
		
		//istanzio la request:
		InserisciProvvisorioDiCassa inserisciProvvisorio = new InserisciProvvisorioDiCassa();
		
		//istanzio l'oggetto ProvvisorioDiCassa 
		ProvvisorioDiCassa provvisorioDaInserire = new ProvvisorioDiCassa();
		
		//VADO A POPOLARE l'oggetto ProvvisorioDiCassa con i dati del model:
		
		//Richiedente
		inserisciProvvisorio.setRichiedente(sessionHandler.getRichiedente());
		
		//Ente
		inserisciProvvisorio.setEnte(sessionHandler.getAccount().getEnte());
		
		//Bilancio
		inserisciProvvisorio.setBilancio(sessionHandler.getBilancio());
		
		//Anno Esercizio/Bilancio
		provvisorioDaInserire.setAnno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//Provvisorio di cassa			
		if(model.getNumeroProvvisorio() != null){
			provvisorioDaInserire.setNumero(model.getNumeroProvvisorio().intValue());
		}
		
		//Anno Provvisorio
		if(model.getAnnoProvvisorio() != null){
			provvisorioDaInserire.setAnno(model.getAnnoProvvisorio());
		}		
		
		//Desc Causale
		if(StringUtils.isNotEmpty(model.getDescCausale())){
			provvisorioDaInserire.setCausale(model.getDescCausale());
		}
		
		//Denominazione Soggetto
		if(StringUtils.isNotEmpty(model.getDenominazioneSoggetto())){
			provvisorioDaInserire.setDenominazioneSoggetto(model.getDenominazioneSoggetto());
		}
		
		//Sub Causale
		if(StringUtils.isNotEmpty(model.getSubCausale())){
			provvisorioDaInserire.setSubCausale(model.getSubCausale());
		}
		
		//Data Emissione
		if(StringUtils.isNotEmpty(model.getDataEmissione())){	
			provvisorioDaInserire.setDataEmissione(DateUtility.parse(model.getDataEmissione()));
		}
		
		//Data Trasmissione
		if(StringUtils.isNotBlank(model.getDataTrasmissione())){	
			provvisorioDaInserire.setDataTrasmissione(DateUtility.parse(model.getDataTrasmissione()));
		}

		if(StringUtils.isNotBlank(model.getDataInvioServizio())){	
			provvisorioDaInserire.setDataInvioServizio(DateUtility.parse(model.getDataInvioServizio()));
		}
		if(StringUtils.isNotBlank(model.getDataPresaInCaricoServizio())){	
			provvisorioDaInserire.setDataPresaInCaricoServizio(DateUtility.parse(model.getDataPresaInCaricoServizio()));
		}
		if(StringUtils.isNotBlank(model.getDataRifiutoErrataAttribuzione())){	
			provvisorioDaInserire.setDataRifiutoErrataAttribuzione(DateUtility.parse(model.getDataRifiutoErrataAttribuzione()));
		}

		//Accettato
		provvisorioDaInserire.setAccettato(model.getTipoDocumentoProv().equalsIgnoreCase("Spesa") ? model.getAccettato() : null);

		//Note
		provvisorioDaInserire.setNote(model.getNote());

		//Tipo Provvisorio
		if(StringUtils.isNotEmpty(model.getTipoDocumentoProv())){	
			if(model.getTipoDocumentoProv().equalsIgnoreCase("Entrata")){
				provvisorioDaInserire.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
			}else{
				provvisorioDaInserire.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
			}
		}
		
		//Import
		if(StringUtils.isNotEmpty(model.getImportoFormattato())){
			BigDecimal importo = convertiImportoToBigDecimal(model.getImportoFormattato());
			provvisorioDaInserire.setImporto(importo);
		}		
		
		
		//Setto il provvisorio nella request:
		inserisciProvvisorio.setProvvisorioDaInserire(provvisorioDaInserire);
		
		//ritorno la request al chiamante:
		return inserisciProvvisorio;
	}
	
	public String annullaInserisciProvvisorio(){
		// pulisce i campi
		pulisciCampi();
		impostaDatiIniziali();
		return SUCCESS;
	}
	
	/**
	 * Metodo per pulire il model (annulla)
	 */
	protected void pulisciCampi(){
		model.setAnnoProvvisorio(null);
		model.setNumeroProvvisorio(null);
		model.setDescCausale(null);		
		model.setDenominazioneSoggetto(null);
		model.setSubCausale(null);
		model.setDataEmissione(null);
		model.setDataTrasmissione(null);
		model.setDataInvioServizio(null);
		model.setDataPresaInCaricoServizio(null);
		model.setDataRifiutoErrataAttribuzione(null);
		model.setAccettato(false);
		model.setNote(null);
		model.setTipoDocumentoProv(null);
		model.setTipoDocumentoProvList(new ArrayList<String>());
		model.setImporto(null);
		// SIAC-6104
		model.setImportoFormattato(null);
	}
	
	
	protected void impostaDatiIniziali(){
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.setAnnoProvvisorio(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   	
	   	//set radio tipo
	   	model.getTipoDocumentoProvList().add("Entrata");
	   	model.getTipoDocumentoProvList().add("Spesa");
	   	model.setTipoDocumentoProv("Entrata");
	}
	
	/**
	 * lancio della funznioe di inserimento
	 * @return
	 */
	protected String executeInserisciProvvisorioCassa() {
	
		//1. componiamo la request per il servizio di inserimento:
		InserisciProvvisorioDiCassa requestInserisci = convertiModelPerChiamataServizioInserimentoProvvisorio();
		
		//2. invochiamo servizio di inserimento:
		InserisciProvvisorioDiCassaResponse response = provvisorioService.inserisciProvvisorioDiCassa(requestInserisci);
		
		//3. analizziamo la response del servizio:
		if(response.isFallimento()){
			//ci sono errori
			addErrori("executeInserisciProvvisorioCassa", response);
			return INPUT;
		}
		
		//tutto ok:
	
		ProvvisorioDiCassa provvisorioCreato = response.getProvvisorioDiCassa();
		
		setNumeroProvvStruts(provvisorioCreato.getNumero().toString());
		setAnnoProvvStruts(provvisorioCreato.getAnno().toString());
		setTipoProvvStruts(provvisorioCreato.getTipoProvvisorioDiCassa().toString());
		
		return "gotoAggiorna";
		    
	}

	@Override
	public String listaClasseSoggettoChanged() {
		return null;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		
	}

	@Override
	protected void setErroreCapitolo() {
		
	}

	@Override
	protected void setSoggettoSelezionato(
			SoggettoImpegnoModel soggettoImpegnoModel) {
		
	}

	@Override
	protected void setProvvedimentoSelezionato(
			ProvvedimentoImpegnoModel currentProvvedimento) {
		
	}

	@Override
	public boolean missioneProgrammaAttivi() {
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		return false;
	}

	@Override
	public boolean cupAttivo() {
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return null;
	}

	@Override
	public String confermaSiope() {
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		return false;
	}

	public String getNumeroProvvStruts() {
		return numeroProvvStruts;
	}

	public void setNumeroProvvStruts(String numeroProvvStruts) {
		this.numeroProvvStruts = numeroProvvStruts;
	}

	public String getAnnoProvvStruts() {
		return annoProvvStruts;
	}

	public void setAnnoProvvStruts(String annoProvvStruts) {
		this.annoProvvStruts = annoProvvStruts;
	}

	public String getTipoProvvStruts() {
		return tipoProvvStruts;
	}

	public void setTipoProvvStruts(String tipoProvvStruts) {
		this.tipoProvvStruts = tipoProvvStruts;
	}
	
}
