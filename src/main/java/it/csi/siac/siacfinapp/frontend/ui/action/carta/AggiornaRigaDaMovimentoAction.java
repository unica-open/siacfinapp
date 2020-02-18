/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCartaResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaRigaDaMovimentoAction extends ActionKeyGestioneCartaAction {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected transient DocumentoSpesaService documentoSpesaService;
	
	//riga id
	private String rigaId;
	
	@Override
	public void prepare() throws Exception {
		//richiamo il prepare della super classe
		super.prepare();
		//setto il titolo di pagina
		this.model.setTitolo("Aggiorna riga da movimento");					
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//Pulisco i campi model riga
		pulisciCampiRiga();		
		
		//lista dei tipo documenti spesa:
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_DOCUMENTO_SPESA);
	   	model.setListaDocTipoSpesa((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_DOCUMENTO_SPESA));	   
		
	   	//caricamento liste per ricerca soggetto
	  	caricaListePerRicercaSoggetto();
	   	
		//carico i dati della riga selezionata nel model
		if(getRigaId()!=null && !StringUtils.isEmpty(getRigaId())){
			caricaDatiRiga(getRigaId(),false);
			model.setNumeroRigaSelezionata(getRigaId());			
		}
		
		//calcolo la somma dei documenti collegati:
		sommaDocumentiCollegati();
		
		return SUCCESS;
	}
	
	/**
	 * cambiamdo codice creditore in step1 intercetta 
	 * il cambio e fa il refresh della tabella modpagamento
	 * @return
	 */
	public String modpagamento(){
		//leggo l'id dall'evento nella pagina jsp:
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		//carico le liste rispetto al nuovo valore:
		caricaListeCreditore(cod);
		return MODPAGAMENTO;
	}
	
	public String sedisecondarie(){
		return SEDISECONDARIE;
	}
			
	public String caricaTitoloModPag(){
	   model.getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getCodice();
	   model.getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getDescrizione();
	   return MODPAGAMENTO;
	}		
	   
	public String caricaTitoloSedi(){
	   if(model.getRadioSediSecondarieSoggettoSelezionato()!=0){
		   model.getSedeSelezionata().getDenominazione();
	   }
	   return SEDISECONDARIE;
	}		
	
	@Override
	public String selezionaSoggetto() {
		return selezionaSoggettoRiga();
	}
	
	@Override
	public String remodpagamento(){
		// aggiorna la tabella modalita di pagamento 
		// su selezione della sede e aggiorna quindi la lista 
		// sedi associate alla modalita' di pagamento scelta(
 		return super.remodpagamento();
	}
	
	@Override	
	public String resede(){
		return super.resede();
	}
	
	public boolean disableAggiornaRiga() {
		boolean disabled=false;
		
		//se in aggiornamento:
		if (model.isAggiornamento()) {
			
			//PROVVISIORIO
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			}
			
			//COMPLETATO
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			}

			//TRASMESSO
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				disabled=true;
			}	
			
		}
		
		return disabled;
	}
	
	
	public boolean disableAggiornaImportoRiga() {
		boolean disabled=false;
		
		//se in aggiornamento:
		if (model.isAggiornamento()) {
			
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				
				//PROVVISORIO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
				
				if(model.getListaSubDocumentoSpesa()!=null && model.getListaSubDocumentoSpesa().size()>0){
					disabled=true;
				}	
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				
				//COMPLETATO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}

				if(model.getListaSubDocumentoSpesa()!=null && model.getListaSubDocumentoSpesa().size()>0){
					disabled=true;
				}				
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				
				//TRASMESSO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_regCarta)) {
					disabled=true;
				}
				
				if (FinStringUtils.isEmpty(model.getCodiceDivisa())) {
					disabled=true;
				}
			}
		}
		
		return disabled;
	}	
	
	public boolean disableCancellaDocumento(Integer docId) {
		boolean disabled=false;
		
		//se in aggiornamento:
		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				
				//PROVVISORIO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				
				//COMPLETATO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				
				//TRASMESSO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_regCarta)) {
					disabled=true;
				} else if (model.getListaSubDocumentoSpesa()!=null && model.getListaSubDocumentoSpesa().size()>0){
					
					for(SubdocumentoSpesa subDoc : model.getListaSubDocumentoSpesa()){
						if(subDoc.getUid()==docId){
							
							if(subDoc.getDocumento()!=null && subDoc.getDocumento().getTipoDocumento()!=null && subDoc.getDocumento().getTipoDocumento().getCodice()!=null && 
									!subDoc.getDocumento().getTipoDocumento().getCodice().equalsIgnoreCase(Constanti.D_DOC_TIPO_CARTA_CONTABILE_CODE)){
								disabled=true;
							}							
						}
					}				
				}	
			}
						
		}
		
		return disabled;
	}
	
	public boolean disableNuovoDocumento(Integer docId) {
		boolean disabled=false;
		
		//se in aggiornamento:
		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				
				//PROVVISORIO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				
				//COMPLETATO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				
				//TRASMESSO
				
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_regCarta)) {
					disabled=true;
				}
			}
						
		}
		
		return disabled;
	}
	
		
	public String annullaAggiornaRiga(){
		rigaId = model.getNumeroRigaSelezionata();
		return "goToAggiornaRiga";
	}
	
	private List<Errore> controlloDatiPerRicercaGuidataDocumento(){
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		HttpServletRequest request = ServletActionContext.getRequest();		
		
		
		//CONTROLLO ANNO DOCUMENTO OBBLITAGORIO:
		String annoDocumento = request.getParameter("anno");
		if (model.getAnnoDoc() != null && !model.getAnnoDoc().toString().isEmpty()) {
				if (model.getAnnoDoc() <= 1900) {
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno", annoDocumento, ">1900"));
				}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Documento :Anno "));
			model.setAnnoDoc(null);
		}
		
		//NUMERO DOC OBBLIGATORIO:
		if (StringUtils.isEmpty(model.getNumDoc())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Documento :Numero "));
		} 
		
		//TIPO DOC:
		if (model.getCodiceTipoDoc() != null && !model.getCodiceTipoDoc().isEmpty()) {
		} 
		
		//NUMERO DOC:
		if( model.getNumDoc()== null && model.getSoggDoc()== null && model.getCodiceTipoDoc()== null ){
			listaErrori.add(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}
		
		
		return listaErrori;
	}
	
	/**
	 * Costruisce la request per la ricerca dei documenti carta
	 * @return
	 */
	private RicercaDocumentiCarta builRequestRicercaDocumentiDaCollegare(){
		RicercaDocumentiCarta ricercaDocumentiCarta = new RicercaDocumentiCarta();
		ricercaDocumentiCarta.setRichiedente(sessionHandler.getRichiedente());
		ricercaDocumentiCarta.setEnte(sessionHandler.getEnte());
		
		//ricerca quota spesa:
		RicercaQuotaSpesa ricercaQuotaSpesa = new RicercaQuotaSpesa();
		ricercaQuotaSpesa.setRichiedente(sessionHandler.getRichiedente());
		ricercaQuotaSpesa.setEnte(sessionHandler.getEnte());
		
		ricercaQuotaSpesa.setAnnoDocumento(model.getAnnoDoc());
		if(!StringUtils.isEmpty(model.getNumDoc())){
			ricercaQuotaSpesa.setNumeroDocumento(model.getNumDoc());
		}
		
		//soggetto documento:
		if(model.getSoggDoc()!= null){
			Soggetto soggetto = new Soggetto();
			soggetto.setCodiceSoggetto(model.getSoggDoc().toString());
			ricercaQuotaSpesa.setSoggetto(soggetto);
		}
		
		//codice tipo doc:
		if(model.getCodiceTipoDoc()!= null && StringUtils.isNotEmpty(model.getCodiceTipoDoc())){
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setUid(Integer.parseInt(model.getCodiceTipoDoc()));
			ricercaQuotaSpesa.setTipoDocumento(tipoDocumento );
		}
		
		//parametri paginazione:
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(10);
		parametriPaginazione.setNumeroPagina(1);
		ricercaQuotaSpesa.setParametriPaginazione(parametriPaginazione );
		
		//
		
		ricercaDocumentiCarta.setRicercaQuotaSpesa(ricercaQuotaSpesa);
		
		ricercaDocumentiCarta.setNumRisultatiPerPagina(10);
		ricercaDocumentiCarta.setNumPagina(1);
		
		return ricercaDocumentiCarta;
	}
	
	/**
	 * per la ricerca guidata del documento
	 * @return
	 */
	public String ricercaGuidataDocumento(){
		
		model.setInPopup(true);
		model.setHasSubDocumentiSpesa(false);
		
		//CONTROLLI DI OBBLIGATORIETA' E COERENZA DATI IMMESSI:
		List<Errore> listaErrori = controlloDatiPerRicercaGuidataDocumento();
		if (!listaErrori.isEmpty()) {
			model.setListaRicercaDocumento(new ArrayList<DocumentoSpesa>());
			addErrori(listaErrori);
			return "refreshPopupModalDocumento";
		}
		//END CONTROLLI
		
		//RICERCA:
		RicercaDocumentiCarta ricercaDocumentiCarta = builRequestRicercaDocumentiDaCollegare();
		RicercaDocumentiCartaResponse response = cartaContabileService.ricercaDocumentiCarta(ricercaDocumentiCarta );
		
		if(isFallimento(response)){
			// ci sono errori nel servizio
			model.setListaRicercaDocumento(new ArrayList<DocumentoSpesa>());
			addErrori(response.getErrori());
			return "refreshPopupModalDocumento";
		}
		
		//setto i dati ottenuti nel model:
		model.setIsnSubImpegno(true);
		model.setListaRicercaSubDocumentoSpesa(response.getResponseQuoteSpesa().getListaSubdocumenti());
		model.setHasSubDocumentiSpesa(true);
		
		return "refreshPopupModalDocumento";
	}
	
	@Override
	public String pulisciRicercaDocumento(){
		if(model.getSoggetto().getCodCreditore()!= null && StringUtils.isNotEmpty(model.getSoggetto().getCodCreditore())){
			model.setSoggDoc(Integer.parseInt(model.getSoggetto().getCodCreditore()));
		}else{
			model.setSoggDoc(null);
		}
		return super.pulisciRicercaDocumento();
	}
	
	
	public String ricercaQuoteDocumento(){
		model.setListaRicercaSubDocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
		
		//devo individure il documento selezionato:
		DocumentoSpesa documentoSelezionato= new DocumentoSpesa();
		for(DocumentoSpesa doc:model.getListaRicercaDocumento()){
			if(doc.getUid()== model.getRadioDocumentoSelezionato()){
				documentoSelezionato= doc;
				break;
			}
		}
		
		//istanzio la requeste del servizio
		RicercaDettaglioDocumentoSpesa req= new RicercaDettaglioDocumentoSpesa();
		
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setDocumentoSpesa(documentoSelezionato);
		
		//richiamo il servizio:
		RicercaDettaglioDocumentoSpesaResponse response= documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
		
		//analizzo la risposta del servizio:
		if(response!= null && response.isFallimento() ){
			//ci sono errori
			addErrori(response.getErrori());
			return "refreshPopupModalDocumento";
		}
		
		//setto di dati trovati nel model:
		if(response.getDocumento().getListaSubdocumenti() != null && response.getDocumento().getListaSubdocumenti().size()> 0 ){
			model.setListaRicercaSubDocumentoSpesa(response.getDocumento().getListaSubdocumenti());
			model.setHasSubDocumentiSpesa(true);
		}
		
		
		return "refreshPopupModalDocumento";
	}

	/**
	 *  aggiorna la sommatoria della tabella dei documenti collegati
	 */
	private void sommaDocumentiCollegati(){
		
		BigDecimal sommatoria = BigDecimal.ZERO;
		
		//itero sui sub documenti spesa:
		if(model.getListaSubDocumentoSpesa()!=null && model.getListaSubDocumentoSpesa().size()>0){
			Iterator<SubdocumentoSpesa> it = model.getListaSubDocumentoSpesa().iterator();
			while(it.hasNext()){
				SubdocumentoSpesa s = it.next();
				sommatoria = sommatoria.add(s.getImporto());
				
			}
		}
		
		//setto la sommatoria nel model:
		model.setSommaDocumentiCollegati(sommatoria);
		
	}

	/**
	 * click sulla pop up di ricerca documento
	 * 
	 */
	public String confermaCompGuidataDocumento(){
		SubdocumentoSpesa subSelezionato = null;
		
		//itero sui sub documenti spesa:
		for(SubdocumentoSpesa sub : model.getListaRicercaSubDocumentoSpesa()){
			if(sub.getUid()== model.getRadioSubDocumentoSelezionato()){
				subSelezionato= sub;
				break;
			}
		}
		
		if(subSelezionato!=null){
			
			boolean presente = FinUtility.presenteInLista(model.getListaSubDocumentoSpesa(), subSelezionato.getUid());
			
			if(!presente){
				model.getListaSubDocumentoSpesa().add(subSelezionato);
				// aggiorno il totale 
				sommaDocumentiCollegati();	
			}
			
		}
		
		return SUCCESS;
	}
	
	public String eliminaDocDaTabella(){
		
		HttpServletRequest request = ServletActionContext.getRequest();		
		
		String idDocDaEliminare = request.getParameter("idDocDaEliminare");
		SubdocumentoSpesa subSelezionato= new SubdocumentoSpesa();
		if(idDocDaEliminare!= null && StringUtils.isNotEmpty(idDocDaEliminare)){
			
			
			//itero sui sub documenti spesa:
			for(SubdocumentoSpesa sub:model.getListaSubDocumentoSpesa()){
				
				if(sub.getUid()== Integer.parseInt(idDocDaEliminare)){
					subSelezionato= sub;
					break;
				}
			}
				
			
			
		}
		List<SubdocumentoSpesa> cloneLista=new ArrayList<SubdocumentoSpesa>();
		for(SubdocumentoSpesa sub:model.getListaSubDocumentoSpesa()){
			if(sub.getUid() != subSelezionato.getUid()){
				cloneLista.add(sub);
			}
			
		}
		
		
		model.setListaSubDocumentoSpesa(cloneLista);
		// aggiorno il totale 
		sommaDocumentiCollegati();
		return "refreshListaDaElimina";
		
	}
	
	@Override 
	public String getActionDataKeys() {
		return "model, rigaId";
	}
	
	//GETTER E SETTER:
	
	public String getRigaId() {
		return rigaId;
	}

	public void setRigaId(String rigaId) {
		this.rigaId = rigaId;
	}

	
}
