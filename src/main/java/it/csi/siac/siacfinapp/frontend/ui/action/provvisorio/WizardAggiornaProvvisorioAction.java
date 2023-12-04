/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaProvvisorioDiCassaResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

public class WizardAggiornaProvvisorioAction extends AbstractGestioneProvvisiorioDiCassa {

	private static final long serialVersionUID = 1L;
	
	protected String numeroProvv;
	protected String annoProvv;
	protected String tipoProvv;
	protected String arrivoDaInserimento;
	
	protected boolean successSalva = false;
	
	@Override
	public String getActionKey() {
		return "aggiornaProvvisorio";
	}
		
	/**
	 * convertitore utilizzato per la chiamata al servizio di aggiorna provvisorio
	 * @return
	 */
	protected AggiornaProvvisorioDiCassa convertiModelPerChiamataServizioAggiornamentoProvvisorio() {
		ProvvisorioDiCassa provvisorioDaAggiornare = new ProvvisorioDiCassa();
		AggiornaProvvisorioDiCassa aggiornaProvvisorio = new AggiornaProvvisorioDiCassa();
		
		//RICHIEDENTE
		aggiornaProvvisorio.setRichiedente(sessionHandler.getRichiedente());
		
		//Ente
		aggiornaProvvisorio.setEnte(sessionHandler.getAccount().getEnte());
		
		//BILANCIO
		aggiornaProvvisorio.setBilancio(sessionHandler.getBilancio());
		
		//Anno Esercizio/Bilancio
		provvisorioDaAggiornare.setAnno(sessionHandler.getAnnoBilancio());
		
		//Provvisorio di cassa	
		
		//numero provvisorio
		if(model.getNumeroProvvisorio() != null){
			provvisorioDaAggiornare.setNumero(model.getNumeroProvvisorio().intValue());
		}
		
		//anno provvisorio
		if(model.getAnnoProvvisorio() != null){
			provvisorioDaAggiornare.setAnno(model.getAnnoProvvisorio());
		}	
		
		//descrizione causale
		if(StringUtils.isNotEmpty(model.getDescCausale())){
			provvisorioDaAggiornare.setCausale(model.getDescCausale());
		}
		
		//denominazione soggetto
		if(StringUtils.isNotEmpty(model.getDenominazioneSoggetto())){
			provvisorioDaAggiornare.setDenominazioneSoggetto(model.getDenominazioneSoggetto());
		}
		
		//sub causale
		if(StringUtils.isNotEmpty(model.getSubCausale())){
			provvisorioDaAggiornare.setSubCausale(model.getSubCausale());
		}
		
		//data emissione
		if(StringUtils.isNotBlank(model.getDataEmissione())){	
			provvisorioDaAggiornare.setDataEmissione(DateUtility.parse(model.getDataEmissione()));
		}
		
		//data trasmissione
		if(StringUtils.isNotBlank(model.getDataTrasmissione())){	
			provvisorioDaAggiornare.setDataTrasmissione(DateUtility.parse(model.getDataTrasmissione()));
		}

		if(StringUtils.isNotBlank(model.getDataInvioServizio())){	
			provvisorioDaAggiornare.setDataInvioServizio(DateUtility.parse(model.getDataInvioServizio()));
		}
		if(StringUtils.isNotBlank(model.getDataPresaInCaricoServizio())){	
			provvisorioDaAggiornare.setDataPresaInCaricoServizio(DateUtility.parse(model.getDataPresaInCaricoServizio()));
		}
		if(StringUtils.isNotBlank(model.getDataRifiutoErrataAttribuzione())){	
			provvisorioDaAggiornare.setDataRifiutoErrataAttribuzione(DateUtility.parse(model.getDataRifiutoErrataAttribuzione()));
		}
		
		
		//flag accettato
		provvisorioDaAggiornare.setAccettato(model.getAccettato());

		//NOTE:
		provvisorioDaAggiornare.setNote(model.getNote());

		//TIPO DOCUMENTO:
		if(StringUtils.isNotEmpty(model.getTipoDocumentoProv())){
			if(model.getTipoDocumentoProv().equalsIgnoreCase("Entrata")){
				//ENTRATA
				provvisorioDaAggiornare.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
			}else{
				//SPESA
				provvisorioDaAggiornare.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
			}
		}
		
		//IMPORTO
		if(model.getImportoFormattato() != null){
			BigDecimal importo = convertiImportoToBigDecimal(model.getImportoFormattato());
			provvisorioDaAggiornare.setImporto(importo);
		}
		
		provvisorioDaAggiornare.setCodiceContoEvidenza(model.getCodiceContoEvidenza());
		provvisorioDaAggiornare.setDescrizioneContoEvidenza(model.getDescrizioneContoEvidenza());
		
		//SAC SELEZIONATA:
		if(StringUtils.isNotBlank(model.getStrutturaSelezionataSuPagina())){
			StrutturaAmministrativoContabile struttAmm = new StrutturaAmministrativoContabile();
			struttAmm.setUid(new Integer(model.getStrutturaSelezionataSuPagina()));
			provvisorioDaAggiornare.setStrutturaAmministrativoContabile(struttAmm);
		} else {
			provvisorioDaAggiornare.setStrutturaAmministrativoContabile(null);
		}
			
		//SETTIAMO L'ID:
		provvisorioDaAggiornare.setIdProvvisorioDiCassa(model.getIdProvvisorioDiCassa());
		//
		
		//SETTO IL PROVVISORIO NELLA REQUEST:
		aggiornaProvvisorio.setProvvisorioDaAggiornare(provvisorioDaAggiornare);
		
		//RESTITUISCO LA REQUEST AL CHIAMANTE:
		return aggiornaProvvisorio;
	}
	
	public String annullaAggiornaProvvisorio(){
		//pulisce i campi
		pulisciCampi();
		
		//set radio tipo
	   	model.getTipoDocumentoProvList().add("Entrata");
	   	model.getTipoDocumentoProvList().add("Spesa");
	   	
	   	//RIPORTA I CAMPI A PRIMA DELLE EVENTUALI MODIFICHE:
	   	impostaDatiNeiCampi(model.getProvvisorioDiCassaInAggiornamento());
		
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
		model.setStruttAmmOriginale(null);
		model.setStrutturaSelezionataSuPagina(null);
	}
	
	
	protected void impostaDatiNeiCampi(ProvvisorioDiCassa provvisorioDiCassa){
		
		//DATO CHE SIAMO IN AGGIORNA DOBBIAMO SALVARE ANCHE L'ID:
		model.setIdProvvisorioDiCassa(provvisorioDiCassa.getIdProvvisorioDiCassa());
		//
		
		//ANNO PROVVISORIO:
		model.setAnnoProvvisorio(provvisorioDiCassa.getAnno());
		
		//NUMERO PROVVISORIO:
		if(provvisorioDiCassa.getNumero()!=null){
			model.setNumeroProvvisorio(new BigInteger(provvisorioDiCassa.getNumero().toString()));
		}
		
		//desc causale:
		model.setDescCausale(provvisorioDiCassa.getCausale());
		
		//denominazione soggetto:
		model.setDenominazioneSoggetto(provvisorioDiCassa.getDenominazioneSoggetto());
		
		//sub causale:
		model.setSubCausale(provvisorioDiCassa.getSubCausale());
		
		//data emissione:
		model.setDataEmissione(DateUtility.convertiDataInGgMmYyyy(provvisorioDiCassa.getDataEmissione()));
		
		//data trasmissione:
		model.setDataTrasmissione(DateUtility.convertiDataInGgMmYyyy(provvisorioDiCassa.getDataTrasmissione()));
		
		model.setDataInvioServizio(DateUtility.convertiDataInGgMmYyyy(provvisorioDiCassa.getDataInvioServizio()));
		model.setDataPresaInCaricoServizio(DateUtility.convertiDataInGgMmYyyy(provvisorioDiCassa.getDataPresaInCaricoServizio()));
		model.setDataRifiutoErrataAttribuzione(DateUtility.convertiDataInGgMmYyyy(provvisorioDiCassa.getDataRifiutoErrataAttribuzione()));
		
		
		//accettato:
		model.setAccettato(provvisorioDiCassa.getAccettato());
		
		//note:
		model.setNote(provvisorioDiCassa.getNote());

		model.setCodiceContoEvidenza(provvisorioDiCassa.getCodiceContoEvidenza());
		model.setDescrizioneContoEvidenza(provvisorioDiCassa.getDescrizioneContoEvidenza());

		//tipo provvisiorio
		if(TipoProvvisorioDiCassa.E.equals(provvisorioDiCassa.getTipoProvvisorioDiCassa())){
			//Entrata
			model.setTipoDocumentoProv("Entrata");
		} else {
			//spesa
			model.setTipoDocumentoProv("Spesa");
		}
		
		//IMPORTO:
		model.setImporto(provvisorioDiCassa.getImporto());
		model.setImportoFormattato(convertiBigDecimalToImporto(provvisorioDiCassa.getImporto()));
		
	}
	
	/**
	 * lancio della funzione di aggiorna provvisorio di cassa
	 * @return
	 */
	protected String executeAggiornaProvvisorioCassa() {
	
		//1. Compongo la request per il servizio di aggiornamento:
		AggiornaProvvisorioDiCassa requestAggiorna = convertiModelPerChiamataServizioAggiornamentoProvvisorio();
		
		//2. Invoco il servizio:
		AggiornaProvvisorioDiCassaResponse response = provvisorioService.aggiornaProvvisorioDiCassa(requestAggiorna);
		
		//3. Analizzo la request ottenuta:
		
		if(response.isFallimento()){
			//ci sono errori
			addErrori("executeAggiornaProvvisorioCassa", response);
			return INPUT;
		}
		
		//ESITO OK (altrimenti sarebbe gia' terminato per errori)
		setSuccessSalva(true);
		
		//setto i dati ottenuti:
		
		ProvvisorioDiCassa provvisorioAggiornato = response.getProvvisorioDiCassa();
		
		setNumeroProvv(provvisorioAggiornato.getNumero().toString());
		setAnnoProvv(provvisorioAggiornato.getAnno().toString());
		setTipoProvv(provvisorioAggiornato.getTipoProvvisorioDiCassa().toString());
		
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
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
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

	public String getNumeroProvv() {
		return numeroProvv;
	}

	public void setNumeroProvv(String numeroProvv) {
		this.numeroProvv = numeroProvv;
	}

	public String getAnnoProvv() {
		return annoProvv;
	}

	public void setAnnoProvv(String annoProvv) {
		this.annoProvv = annoProvv;
	}

	public String getTipoProvv() {
		return tipoProvv;
	}

	public void setTipoProvv(String tipoProvv) {
		this.tipoProvv = tipoProvv;
	}

	public String getArrivoDaInserimento() {
		return arrivoDaInserimento;
	}

	public void setArrivoDaInserimento(String arrivoDaInserimento) {
		this.arrivoDaInserimento = arrivoDaInserimento;
	}

	public boolean isSuccessSalva() {
		return successSalva;
	}

	public void setSuccessSalva(boolean successSalva) {
		this.successSalva = successSalva;
	}
	
	private Boolean isAmministratore, isDecentrato, isLettore;

	public boolean isUtenteAmministratore(){
		if (isAmministratore == null)
			isAmministratore = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGPROVVCASSA);

		return isAmministratore;
	}

	public boolean isUtenteDecentrato(){
		if (isDecentrato == null)
			isDecentrato = isAzioneConsentita(AzioneConsentitaEnum.OP_OIL_AGG_DEC_PROVV_CASSA) && !isUtenteAmministratore();

		return isDecentrato;
	}

	public boolean isUtenteLettore(){
		if (isLettore == null)
			isLettore = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_LEGGIPROVVCASSA) && !isUtenteDecentrato()
					&& !isUtenteAmministratore();

		return isLettore;
	}

}
