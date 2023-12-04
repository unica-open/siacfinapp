/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaRicercaDocumentoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.RicercaQuotaSpesaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.CartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class AbstractCartaAction<M extends GenericPopupModel> extends GenericPopupAction<M> {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected transient CartaContabileService cartaContabileService;
	
	@Autowired
	protected transient DocumentoSpesaService documentoSpesaService;
	
	protected void putListaPreDocDaDocumentiInSession(List<PreDocumentoCarta> righeDaDoc){
		ActionContext.getContext().getSession().put(WebAppConstants.KEY_LISTA_PRE_DOC_DA_DOCUMENTI, righeDaDoc);
	}
	
	protected void removeListaPreDocDaDocumentiInSession(){
		ActionContext.getContext().getSession().remove(WebAppConstants.KEY_LISTA_PRE_DOC_DA_DOCUMENTI);
	}
	
	/**
	 * utility per capire se la ricerca e' stata lanciata piena o meno
	 * @return
	 */
	protected boolean parametriRicercaSelezionati(){
		boolean selezionati = true;
		
		RicercaQuotaSpesaModel prcc = (RicercaQuotaSpesaModel)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA);
		
		// se il richiedente non e' settato allora sono passato dalla pagina
		// di ricerca al click di cerca con solamento l'oggetto RicercaCartaContabile istanziato
		
		if(prcc == null || prcc.getRichiedente()==null) selezionati = false;
		
		return selezionati;
		
	}
	
	/**
	 * Convertitore su DTO Serializzabile di comodo
	 * dato che non posso salvare direttamente RicercaQuotaSpesa in Sessione in quanto NON e' serializzabile
	 * @param model
	 * @return
	 */
	protected RicercaQuotaSpesa ricercaQuotaSpesaModelToReq(RicercaQuotaSpesaModel model){
		RicercaQuotaSpesa req = new RicercaQuotaSpesa();
		
		if(model!=null){
			req.setEnte(model.getEnte());
			req.setBilancio(model.getBilancio());
			req.setUidElenco(model.getUidElenco());
			req.setAnnoElenco(model.getAnnoElenco());
			req.setNumeroElenco(model.getNumeroElenco());
			req.setAnnoProvvisorio(model.getAnnoProvvisorio());
			req.setNumeroProvvisorio(model.getNumeroProvvisorio());
			req.setDataProvvisorio(model.getDataProvvisorio());
			req.setTipoDocumento(model.getTipoDocumento());
			req.setAnnoDocumento(model.getAnnoDocumento());
			req.setNumeroDocumento(model.getNumeroDocumento());
			req.setDataEmissioneDocumento(model.getDataEmissioneDocumento());
			req.setNumeroQuota(model.getNumeroQuota());
			req.setNumeroMovimento(model.getNumeroMovimento());
			req.setAnnoMovimento(model.getAnnoMovimento());
			req.setSoggetto(model.getSoggetto());
			req.setUidProvvedimento(model.getUidProvvedimento());
			req.setAnnoProvvedimento(model.getAnnoProvvedimento());
			req.setNumeroProvvedimento(model.getNumeroProvvedimento());
			req.setTipoAtto(model.getTipoAtto());
			req.setStruttAmmContabile(model.getStruttAmmContabile());
			req.setStatiOperativoDocumento(model.getStatiOperativoDocumento());
			req.setCollegatoAMovimentoDelloStessoBilancio(model.getCollegatoAMovimentoDelloStessoBilancio());
			req.setAssociatoAProvvedimentoOAdElenco(model.getAssociatoAProvvedimentoOAdElenco());
			req.setImportoDaPagareZero(model.getImportoDaPagareZero());
			req.setRilevatiIvaConRegistrazioneONonRilevantiIva(model.getRilevatiIvaConRegistrazioneONonRilevantiIva());
			req.setParametriPaginazione(model.getParametriPaginazione());
			req.setRichiedente(model.getRichiedente());
		}
		
		return req;
	}
	
	/**
	 * Convertitore su DTO Serializzabile di comodo
	 * dato che non posso salvare direttamente RicercaQuotaSpesa in Sessione in quanto NON e' serializzabile
	 * @param req
	 * @return
	 */
	protected RicercaQuotaSpesaModel ricercaQuotaSpesaReqToModel(RicercaQuotaSpesa req){
		RicercaQuotaSpesaModel model = new RicercaQuotaSpesaModel();
		
		if(req!=null){
			model.setEnte(req.getEnte());
			model.setBilancio(req.getBilancio());
			model.setUidElenco(req.getUidElenco());
			model.setAnnoElenco(req.getAnnoElenco());
			model.setNumeroElenco(req.getNumeroElenco());
			model.setAnnoProvvisorio(req.getAnnoProvvisorio());
			model.setNumeroProvvisorio(req.getNumeroProvvisorio());
			model.setDataProvvisorio(req.getDataProvvisorio());
			model.setTipoDocumento(req.getTipoDocumento());
			model.setAnnoDocumento(req.getAnnoDocumento());
			model.setNumeroDocumento(req.getNumeroDocumento());
			model.setDataEmissioneDocumento(req.getDataEmissioneDocumento());
			model.setNumeroQuota(req.getNumeroQuota());
			model.setNumeroMovimento(req.getNumeroMovimento());
			model.setAnnoMovimento(req.getAnnoMovimento());
			model.setSoggetto(req.getSoggetto());
			model.setUidProvvedimento(req.getUidProvvedimento());
			model.setAnnoProvvedimento(req.getAnnoProvvedimento());
			model.setNumeroProvvedimento(req.getNumeroProvvedimento());
			model.setTipoAtto(req.getTipoAtto());
			model.setStruttAmmContabile(req.getStruttAmmContabile());
			model.setStatiOperativoDocumento(req.getStatiOperativoDocumento());
			model.setCollegatoAMovimentoDelloStessoBilancio(req.getCollegatoAMovimentoDelloStessoBilancio());
			model.setAssociatoAProvvedimentoOAdElenco(req.getAssociatoAProvvedimentoOAdElenco());
			model.setImportoDaPagareZero(req.getImportoDaPagareZero());
			model.setRilevatiIvaConRegistrazioneONonRilevantiIva(req.getRilevatiIvaConRegistrazioneONonRilevantiIva());
			model.setParametriPaginazione(req.getParametriPaginazione());
			model.setRichiedente(req.getRichiedente());
		}
		
		return model;
	}
	
	
	protected RicercaQuotaSpesa convertModelPerChiamataRicercaCarta(GestioneCartaRicercaDocumentoModel modelRicerca) {
		RicercaQuotaSpesa ricercaQuotaSpesa = new RicercaQuotaSpesa();
		
		ricercaQuotaSpesa.setRichiedente(sessionHandler.getRichiedente());
		ricercaQuotaSpesa.setEnte(sessionHandler.getEnte());
		
		//Documenti
		
		//Tipo documento
		if(StringUtils.isNotEmpty(modelRicerca.getCodiceTipoDoc())){
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setUid(Integer.parseInt(modelRicerca.getCodiceTipoDoc()));
			ricercaQuotaSpesa.setTipoDocumento(tipoDocumento );
		}

		//Anno
		if(StringUtils.isNotEmpty(modelRicerca.getDatiDocumentoAnno())){
			 ricercaQuotaSpesa.setAnnoDocumento(Integer.parseInt(modelRicerca.getDatiDocumentoAnno()));
		}
		
		//Numero
		if(StringUtils.isNotEmpty(modelRicerca.getDatiDocumentoNumero())){
			 ricercaQuotaSpesa.setNumeroDocumento(modelRicerca.getDatiDocumentoNumero());
		}
	
		//Quota
		if(StringUtils.isNotEmpty(modelRicerca.getDatiDocumentoQuota())){
			ricercaQuotaSpesa.setNumeroQuota(Integer.parseInt(modelRicerca.getDatiDocumentoQuota()));
		}
		 
	
		//Data documento
		if(StringUtils.isNotEmpty(modelRicerca.getDataDocumento())){
			ricercaQuotaSpesa.setDataEmissioneDocumento(DateUtility.parse(modelRicerca.getDataDocumento()));
		}
		
		//Soggetto
		if(modelRicerca.getSoggetto()!=null && StringUtils.isNotEmpty(modelRicerca.getSoggetto().getCodCreditore())){
			Soggetto soggettoRicerca = new Soggetto();
			soggettoRicerca.setCodiceSoggetto(modelRicerca.getSoggetto().getCodCreditore());
			ricercaQuotaSpesa.setSoggetto(soggettoRicerca);
		}
				
				
		//ELENCO:
		if(StringUtils.isNotEmpty(modelRicerca.getElencoAnno())){
			ricercaQuotaSpesa.setAnnoElenco(Integer.parseInt(modelRicerca.getElencoAnno()));
		}
		if(StringUtils.isNotEmpty(modelRicerca.getElencoNumero())){
			ricercaQuotaSpesa.setNumeroElenco(Integer.parseInt(modelRicerca.getElencoNumero()));
		}
		
		
		//Provvedimento
		ProvvedimentoImpegnoModel provvedimentoSelezionato = modelRicerca.getProvvedimento();
		
		if(provvedimentoSelezionato!=null){
			
			//Anno
			if(provvedimentoSelezionato.getAnnoProvvedimento()!=null){
				ricercaQuotaSpesa.setAnnoProvvedimento(provvedimentoSelezionato.getAnnoProvvedimento());
			}
			
			
			//Numero
			if(provvedimentoSelezionato.getNumeroProvvedimento()!=null){
				ricercaQuotaSpesa.setNumeroProvvedimento(provvedimentoSelezionato.getNumeroProvvedimento().intValue());
			}
			
			//Tipo
			if(provvedimentoSelezionato.getTipoProvvedimento()!=null){
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setCodice(provvedimentoSelezionato.getTipoProvvedimento());
				ricercaQuotaSpesa.setTipoAtto(tipoAtto);
			}
			
			//Strutt amm
			if(provvedimentoSelezionato.getStrutturaAmministrativa()!=null){
				StrutturaAmministrativoContabile struttAmmContabile = new StrutturaAmministrativoContabile();
				struttAmmContabile.setCodice(provvedimentoSelezionato.getStrutturaAmministrativa());
				ricercaQuotaSpesa.setStruttAmmContabile(struttAmmContabile );
			}
			
		}
	  
		    
		// paginazione
	    ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
	    addNumAndPageSize(parametriPaginazione, "ricercaSubDocPerNuovaRigaCartaID");
	    ricercaQuotaSpesa.setParametriPaginazione(parametriPaginazione);
		
	    // memorizzo in sessione i parametri con il quale ho lanciato la ricerca
	    sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, ricercaQuotaSpesaReqToModel(ricercaQuotaSpesa));
		
		return ricercaQuotaSpesa;
	}
	
	/**
	 * 
	 * Metodo di comodo per mettere a fattore comune il codice che gestisce l'invocazione della ricerca sub documenti di spese.
	 * 
	 * @param modelRicerca
	 * @return
	 */
	protected RicercaQuotaSpesa buildRicercaQuotaSpesaPerRicercaSubDocumenti(GestioneCartaRicercaDocumentoModel modelRicerca){

		RicercaQuotaSpesa ricercaQuotaSpesa = new RicercaQuotaSpesa();
		
		if(parametriRicercaSelezionati()){
			// costruisco l'oggetto per la ricerca
			RicercaQuotaSpesaModel ricercaQuotaSpesaModel =  (RicercaQuotaSpesaModel)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA);
			ricercaQuotaSpesa = ricercaQuotaSpesaModelToReq(ricercaQuotaSpesaModel);
			
			ricercaQuotaSpesa.setRichiedente(sessionHandler.getRichiedente());
			ricercaQuotaSpesa.setEnte(sessionHandler.getEnte());
			
			ParametriPaginazione parametriPaginazione = ricercaQuotaSpesa.getParametriPaginazione();
			ricercaQuotaSpesa.setParametriPaginazione(parametriPaginazione);
			
			// MEMORIZZO I PARAMETRI DI RICERCA CARTA
			addNumAndPageSize(parametriPaginazione, "ricercaSubDocPerNuovaRigaCartaID");
			
			
			sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, ricercaQuotaSpesaReqToModel(ricercaQuotaSpesa));
			
		}else{
			// costruisco i parametri di ricerca a partire dai valori in maschera:	
			ricercaQuotaSpesa = convertModelPerChiamataRicercaCarta(modelRicerca);
		}
		
		return ricercaQuotaSpesa;
	}
	
	/**
	 * Metodo di comodo per mettere a fattore comune il codice che gestisce l'invocazione della ricerca sub documenti di spese. 
	 * @param ricercaQuotaSpesa
	 * @return
	 */
	protected RicercaDocumentiCarta buildRicercaDocumentiCartaPerRicercaSubDocumenti(RicercaQuotaSpesa ricercaQuotaSpesa){
		// chiamata al servizio di ricerca 
		
		RicercaDocumentiCarta ricercaDocumentiCarta = new RicercaDocumentiCarta();
		ricercaDocumentiCarta.setRichiedente(sessionHandler.getRichiedente());
		ricercaDocumentiCarta.setEnte(sessionHandler.getEnte());
		ricercaDocumentiCarta.setRicercaQuotaSpesa(ricercaQuotaSpesa);
		
		ricercaDocumentiCarta.setNumRisultatiPerPagina(ricercaQuotaSpesa.getParametriPaginazione().getElementiPerPagina());
		ricercaDocumentiCarta.setNumPagina(ricercaQuotaSpesa.getParametriPaginazione().getNumeroPagina());
		
		//setto in bilancio:
		ricercaDocumentiCarta.getRicercaQuotaSpesa().setBilancio(sessionHandler.getBilancio());
		
		//e chiedo solo quelli collegati a movimento dello stesso anno di bilancio:
		ricercaDocumentiCarta.getRicercaQuotaSpesa().setCollegatoAMovimentoDelloStessoBilancio(true);
		
		return ricercaDocumentiCarta;
	}
	
	/**
	 * Metodo di comodo che gestisce i risultati ottenuti dalla ricerca salvando opportunanemnte i dati di paginazione e i risultati
	 * ottenuti nei model e oggetti opportuni.
	 * 
	 * @param response
	 * @param ricercaQuotaSpesa
	 * @param modelRicerca
	 */
	protected void gestioneResponseRicercaSubDocumenti(RicercaQuotaSpesaResponse response,RicercaQuotaSpesa ricercaQuotaSpesa,GestioneCartaRicercaDocumentoModel modelRicerca){
		if(response!=null && response.getListaSubdocumenti()!=null && response.getListaSubdocumenti().size()>0){
			// setto i risultati della ricerca
			modelRicerca.setElencoSubdocumentoSpesa(response.getListaSubdocumenti());
			
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, response.getListaSubdocumenti());
			
			modelRicerca.setResultSize(response.getTotaleElementi());
		}else{
			modelRicerca.setElencoSubdocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new ArrayList<SubdocumentoSpesa>());
			modelRicerca.setResultSize(0);
		}
		if(modelRicerca.getPremutoPaginazione()!=0){
			ricercaQuotaSpesa.getParametriPaginazione().setNumeroPagina(modelRicerca.getPremutoPaginazione());
			ricercaQuotaSpesa.getParametriPaginazione().setElementiPerPagina(DEFAULT_PAGE_SIZE);
	    }else{
		    //numeratore per la paginazione
		   addNumAndPageSize(ricercaQuotaSpesa.getParametriPaginazione(), "ricercaSubDocPerNuovaRigaCartaID");
	    }
	}
	
	/**
	 * Pulisce i dati del un model di ricerca indicato.
	 * @param modelRicerca
	 */
	protected void pulisciModelRicerca(GestioneCartaRicercaDocumentoModel modelRicerca) {
		
		//Tipo documento
		modelRicerca.setCodiceTipoDoc(null);
		
		//Anno	
		modelRicerca.setDatiDocumentoAnno(null);
		
		//Numero
		modelRicerca.setDatiDocumentoNumero(null);
	
		//Quota
		modelRicerca.setDatiDocumentoQuota(null);
		 
	
		//Data documento
		modelRicerca.setDataDocumento(null);
		
		//Soggetto
		modelRicerca.setSoggetto(new SoggettoImpegnoModel());
		modelRicerca.setSoggettoSelezionato(false);
		modelRicerca.setSoggettoRicerca(new SoggettoImpegnoModel());
				
		//ELENCO:
		modelRicerca.setElencoAnno(null);
		modelRicerca.setElencoNumero(null);
		
		//Provvedimento
		modelRicerca.setProvvedimento(null);
		modelRicerca.setProvvedimentoSelezionato(false);
		modelRicerca.setProvvedimentoRicerca(new ProvvedimentoImpegnoModel());
		    
	}
	
	/* **************************************************************************** */
	/* generic pop up action														*/
	/* **************************************************************************** */
	
	@Override
	public String listaClasseSoggettoChanged() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		//  Auto-generated method stub
		
	}

	@Override
	protected void setErroreCapitolo() {
		//  Auto-generated method stub
		
	}

	@Override
	protected void setSoggettoSelezionato(
			SoggettoImpegnoModel soggettoImpegnoModel) {
		//  Auto-generated method stub
		
	}

	@Override
	protected void setProvvedimentoSelezionato(
			ProvvedimentoImpegnoModel currentProvvedimento) {
		//  Auto-generated method stub
		
	}

	/* **************************************************************************** */
	/*  transazione elementare														*/
	/* **************************************************************************** */
	
	@Override
	public boolean missioneProgrammaAttivi() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean cupAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public String confermaPdc() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String confermaSiope() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		//  Auto-generated method stub
		return false;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		// Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
	}
}