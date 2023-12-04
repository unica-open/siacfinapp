/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.RicercaProvvisorioModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

public class WizardRicercaProvvisorioAction extends GenericPopupAction<RicercaProvvisorioModel> {

	private static final long serialVersionUID = 1L;
	
	private Boolean isAmministratore, isDecentrato, isLettore;
	
	@Override
	public String getActionKey() {
		return "ricercaProvvisorio";
	}

	public boolean isUtenteAmministratore() {
		if (isAmministratore == null){
			//valutiamo se abilitato all'azione OP_SPE_AGGPROVVCASSA:
			isAmministratore = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGPROVVCASSA);
		}
		return isAmministratore;
	}

	public boolean isUtenteDecentrato() {
		if (isDecentrato == null){
			//valutiamo se abilitato all'azione OP_OIL_AGG_DEC_PROVV_CASSA:
			isDecentrato = isAzioneConsentita(AzioneConsentitaEnum.OP_OIL_AGG_DEC_PROVV_CASSA) && !isUtenteAmministratore();
		}
		return isDecentrato;
	}

	public boolean isUtenteLettore() {
		if (isLettore == null){
			//valutiamo se abilitato all'azione OP_SPE_LEGGIPROVVCASSA:
			isLettore = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_LEGGIPROVVCASSA) && !isUtenteDecentrato() && !isUtenteAmministratore();
		}
		return isLettore;
	}

	
	/**
	 * convertitore utilizzato per la chiamata al servizio di ricerca provvisorio
	 * @return
	 */
	protected RicercaProvvisoriDiCassa convertiModelPerChiamataServizioRicercaProvvisorio() {
		ParametroRicercaProvvisorio prp = new ParametroRicercaProvvisorio();
		RicercaProvvisoriDiCassa ricercaProvvisorio = new RicercaProvvisoriDiCassa();
		
		//Richiedente
		ricercaProvvisorio.setRichiedente(sessionHandler.getRichiedente());
		
		//Ente
		ricercaProvvisorio.setEnte(sessionHandler.getAccount().getEnte());
		
		//Anno Esercizio/Bilancio
		prp.setAnno(sessionHandler.getAnnoBilancio());
		
		//NUMERO PROVVISORIO:		
		if(model.getNumeroProvvisorio() != null){
			prp.setNumero(model.getNumeroProvvisorio().intValue());
		}
		
		//anno provvisorio da
		if(model.getAnnoProvvisorioDa() != null){
			prp.setAnnoDa(model.getAnnoProvvisorioDa());
		}	
		
		//anno provvisorio a
		if(model.getAnnoProvvisorioA() != null){
			prp.setAnnoA(model.getAnnoProvvisorioA());
		}
		
		//numero provvisorio da
		if(model.getNumeroProvvisorioDa() != null){
			prp.setNumeroDa(model.getNumeroProvvisorioDa().intValue());
		}	
		
		//numero provvisorio a
		if(model.getNumeroProvvisorioA() != null){
			prp.setNumeroA(model.getNumeroProvvisorioA().intValue());
		}
		
		//desc causale
		if(StringUtils.isNotEmpty(model.getDescCausale())){
			prp.setDescCausale(model.getDescCausale());
		}
		
		//denominazione soggetto
		if(StringUtils.isNotEmpty(model.getDenominazioneSoggetto())){
			prp.setDenominazioneSoggetto(model.getDenominazioneSoggetto());
		}
		
		//sub causale
		if(StringUtils.isNotEmpty(model.getSubCausale())){
			prp.setSubCausale(model.getSubCausale());
		}	
		
		//conto tesoriere
		if(StringUtils.isNotEmpty(model.getContoTesoriere())){
			prp.setContoTesoriere(model.getContoTesoriere());
		}	
		
		//data inizio emissione
		if(StringUtils.isNotEmpty(model.getDataInizioEmissione())){	
			prp.setDataInizioEmissione(DateUtility.parse(model.getDataInizioEmissione()));
		}
		
		//data fine emissione
		if(StringUtils.isNotEmpty(model.getDataFineEmissione())){	
			prp.setDataFineEmissione(DateUtility.parse(model.getDataFineEmissione()));
		}
		
		//data inizio trasmissione
		if(StringUtils.isNotEmpty(model.getDataInizioTrasmissione())){	
			prp.setDataInizioTrasmissione(DateUtility.parse(model.getDataInizioTrasmissione()));
		}
		
		//data fine trasmissione
		if(StringUtils.isNotEmpty(model.getDataFineTrasmissione())){	
			prp.setDataFineTrasmissione(DateUtility.parse(model.getDataFineTrasmissione()));
		}

		if(StringUtils.isNotEmpty(model.getDataInizioInvioServizio())){	
			prp.setDataInizioInvioServizio(DateUtility.parse(model.getDataInizioInvioServizio()));
		}
		if(StringUtils.isNotEmpty(model.getDataFineInvioServizio())){	
			prp.setDataFineInvioServizio(DateUtility.parse(model.getDataFineInvioServizio()));
		}
		
		if(StringUtils.isNotEmpty(model.getDataInizioPresaInCaricoServizio())){	
			prp.setDataInizioPresaInCaricoServizio(DateUtility.parse(model.getDataInizioPresaInCaricoServizio()));
		}
		if(StringUtils.isNotEmpty(model.getDataFinePresaInCaricoServizio())){	
			prp.setDataFinePresaInCaricoServizio(DateUtility.parse(model.getDataFinePresaInCaricoServizio()));
		}
		
		if(StringUtils.isNotEmpty(model.getDataInizioRifiutoErrataAttribuzione())){	
			prp.setDataInizioRifiutoErrataAttribuzione(DateUtility.parse(model.getDataInizioRifiutoErrataAttribuzione()));
		}
		if(StringUtils.isNotEmpty(model.getDataFineRifiutoErrataAttribuzione())){	
			prp.setDataFineRifiutoErrataAttribuzione(DateUtility.parse(model.getDataFineRifiutoErrataAttribuzione()));
		}
		
		
		//Tipo Documento
		if(StringUtils.isNotEmpty(model.getTipoDocumentoProv())){	
			if(model.getTipoDocumentoProv().equalsIgnoreCase("Entrata")){
				prp.setTipoProvvisorio(TipoProvvisorioDiCassa.E);
			}else{
				prp.setTipoProvvisorio(TipoProvvisorioDiCassa.S);
			}
		}
		
		//verifica sul flag annullato
		if(StringUtils.isNotEmpty(model.getFlagAnnullatoProv())){	
			if(model.getFlagAnnullatoProv().equalsIgnoreCase("si")){	 
				prp.setFlagAnnullato(CostantiFin.TRUE);
			}	
			if(model.getFlagAnnullatoProv().equalsIgnoreCase("no")){	
				prp.setFlagAnnullato(CostantiFin.FALSE);
			}
		}

		
		if (StringUtils.isNotBlank(model.getFlagAccettato())){	
			prp.setFlagAccettato("si".equalsIgnoreCase(model.getFlagAccettato()));
		} else {
			prp.setFlagAccettato(null);
		}

		
		//Struttura amministrativa:
		if(StringUtils.isNotBlank(model.getStrutturaSelezionataSuPagina())){
			prp.setIdStrutturaAmministrativa(new Integer(model.getStrutturaSelezionataSuPagina()));
		}
		
		//importo da
		if(model.getImportoDa() != null){
			prp.setImportoDa(model.getImportoDa());
		}	
		
		//importo a
		if(model.getImportoA() != null){
			prp.setImportoA(model.getImportoA());
		}
		
		//flag da regolarizzare
		if(StringUtils.isNotEmpty(model.getFlagDaRegolarizzare())){	
			if(model.getFlagDaRegolarizzare().equalsIgnoreCase("si")){ 
				prp.setFlagDaRegolarizzare(CostantiFin.TRUE);
			}else{
				prp.setFlagDaRegolarizzare(CostantiFin.FALSE);
			}
		} else {
			prp.setFlagDaRegolarizzare(null);
		}
			
		//settiamo il parametro di ricerca nella request:
		ricercaProvvisorio.setParametroRicercaProvvisorio(prp);
		
		//settiamo i dati di paginazione:
		addNumAndPageSize(ricercaProvvisorio, "ricercaProvvisorioCassaID");
		
		return ricercaProvvisorio;
	}
	
	public String annullaRicercaProvvisori(){
		// pulisce i campi
		pulisciCampi();
		return "goToRicercaProvvisorioCassa";
	}
	
	/**
	 * Metodo per pulire il model (annulla)
	 */
	protected void pulisciCampi(){
	
		model.setAnnoProvvisorio(null);
		model.setNumeroProvvisorio(null);
		model.setAnnoProvvisorioDa(null);
		model.setAnnoProvvisorioA(null);
		model.setNumeroProvvisorioDa(null);
		model.setNumeroProvvisorioA(null);
		model.setDescCausale(null);		
		model.setDenominazioneSoggetto(null);
		model.setSubCausale(null);
		model.setContoTesoriere(null);
		model.setFlagDaRegolarizzare(null);
		model.setFlagAnnullato(null);
		model.setDataInizioEmissione(null);
		model.setDataFineEmissione(null);
		model.setDataInizioTrasmissione(null);
		model.setDataFineTrasmissione(null);
		model.setDataInizioInvioServizio(null);
		model.setDataFineInvioServizio(null);
		model.setDataInizioRifiutoErrataAttribuzione(null);
		model.setDataFineRifiutoErrataAttribuzione(null);
		model.setTipoDocumentoProv(null);
		model.setDataInizioPresaInCaricoServizio(null);
		model.setDataFinePresaInCaricoServizio(null);
		model.setFlagAnnullatoProv(null);
		model.setFlagAccettato(null);
		model.setFlagDaRegolarizzare(null);
		model.setImportoDa(null);
		model.setImportoA(null);
		model.setStrutturaSelezionataSuPagina(null);
	}
	
	/**
	 * lancio della funznioe di ricerca
	 * @return
	 */
	protected String executeRicercaProvvisoriCassa() {
	
		//1. componiamo la request per il servizio di ricerca:
		RicercaProvvisoriDiCassa reqRicerca = convertiModelPerChiamataServizioRicercaProvvisorio();
		
		//2. invochiamo servizio di ricerca:
		RicercaProvvisoriDiCassaResponse response = provvisorioService.ricercaProvvisoriDiCassa(reqRicerca );
				
		//3. analizziamo la response del servizio:
		if(response.isFallimento()){
			//ci sono errori
			addErrori("executeRicercaRicercaProvvisorioCassa", response);
			return INPUT;
		}
		
		//tutto ok:
		model.setElencoProvvisoriCassa(response.getElencoProvvisoriDiCassa());
		model.setResultSize(response.getNumRisultati());			

		addNumAndPageSize(reqRicerca,"ricercaProvvisorioCassaID");
		
		return "gotoElencoProvvisorioCassa";
		    
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
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}
}
