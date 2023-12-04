/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ConsultaProvvisorioModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaProvvisorioCassaAction extends WizardConsultaProvvisorioAction<ConsultaProvvisorioModel> {

	private static final long serialVersionUID = -8192912865379160597L;
	
	protected static final String TIPO_ORDINATIVO_PAGAMENTO_P = "P";
	
	@Autowired
	protected transient DocumentoSpesaService documentoSpesaService;
	
	@Autowired
	protected transient DocumentoEntrataService documentoEntrataService;

	public String getActionKey() {
		return "consultaProvvisorio";
	}

	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Consulta provvisorio");
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		setMethodName("execute");

		//CARICAMENTO DATI DEL PRIMO TAB:
		if(caricaProvvisorio()){
			RicercaProvvisorioDiCassaPerChiaveResponse respRicerca = ricaricaProvvisorio();
			if(isFallimento(respRicerca)){
				//ci sono errori
				return INPUT;
			}
		}
		
		
		//CARICAMENTO DATI DEL SECONDO TAB:
		if(caricaQuote()){
			ServiceResponse respQuote = ricaricaQuote();
			if(isFallimento(respQuote)){
				//ci sono errori
				return INPUT;
			}
		}
		
		//CARICAMENTO DATI DEL TERZO TAB:
		if(caricaOrdinativi()){
			RicercaOrdinativoResponse respOrd = executeRicercaOrdinativi();
			if(isFallimento(respOrd)){
				//errori
				return INPUT;
			}
		}
	
		//TUTTO OK
		return "success";
	}
	
	private ServiceResponse ricaricaQuote(){
		if(isDiSpesa()){
			RicercaSinteticaModulareQuoteSpesaResponse respQuote = executeRicercaQuoteSpesa();
			if(isFallimento(respQuote)){
				//errori
				addErrori("ricaricaQuote", respQuote);
			}
			return respQuote;
		} else {
			RicercaSinteticaModulareQuoteEntrataResponse respQuote = executeRicercaQuoteEntrata();
			if(isFallimento(respQuote)){
				//errori
				addErrori("ricaricaQuote", respQuote);
			}
			return respQuote;
		}
	}
	
	private RicercaProvvisorioDiCassaPerChiaveResponse ricaricaProvvisorio(){
		//servizio di ricerca:
		//Prepariamo la request per il servizio di ricerca del provvisiorio di cassa:
		RicercaProvvisorioDiCassaPerChiave reqRicercaPerChiave = componiRequestRicercaProvvisorioDiCassaPerChiave();
		
		//Invochiamo il servizio:
		RicercaProvvisorioDiCassaPerChiaveResponse respRicerca = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRicercaPerChiave);
	   	
		//Analizziamo la risposta ottenuta:
		if(isFallimento(respRicerca)){
			//ci sono errori
			addErrori("executeRicercaProvvisorioDiCassaPerChiave", respRicerca);
			return respRicerca;
		}

		//provvisorio trovato:
		ProvvisorioDiCassa provvisorioCassa = respRicerca.getProvvisorioDiCassa();
		
		//setto il provvisorio nel model:
		model.setProvvisorioCassa(provvisorioCassa);
		
		//dati del model:
		impostaDatiNeiCampi(provvisorioCassa);
		
		return respRicerca;
	}
	
	private boolean caricaProvvisorio(){
		return isActiveTabProvvisorioCassa();
	}
	
	private boolean caricaQuote(){
		return isActiveTabProvvisorioCassa() || isActiveTabDocumenti();
	}
	
	private boolean caricaOrdinativi(){
		return isActiveTabProvvisorioCassa() || isActiveTabOrdinativi();
	}
	
	/**
	 * Crea la request per richiamare il servizio di ricerca del provvisiorio
	 * @return
	 */
	private RicercaProvvisorioDiCassaPerChiave componiRequestRicercaProvvisorioDiCassaPerChiave(){
	   	RicercaProvvisorioDiCassaPerChiave reqRicercaPerChiave = new RicercaProvvisorioDiCassaPerChiave();
	   	
	   	//bilancio
	   	reqRicercaPerChiave.setBilancio(sessionHandler.getBilancio());
	   	
	   	//ente
	   	reqRicercaPerChiave.setEnte(sessionHandler.getEnte());
	   	
	   	//richiedente
	   	reqRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
	   	
	   	//parametro provvisiorio:
	   	RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
	   	//anno provvisorio:
	   	pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(new Integer(annoProvv));
		//numero provvisorio:
	   	pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(new Integer(numeroProvv));
	   	
	   	//Tipo provvisorio:
	   	TipoProvvisorioDiCassa tipoProvvisorioDiCassa = TipoProvvisorioDiCassa.S;
	   	if(TipoProvvisorioDiCassa.E.toString().equals(tipoProvv)){
	   		tipoProvvisorioDiCassa = TipoProvvisorioDiCassa.E;
	   	}
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(tipoProvvisorioDiCassa);
		
		//setto il parametro nella request:
		reqRicercaPerChiave.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		//ritorno la request al chiamante:
		return reqRicercaPerChiave;
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
	
	public boolean isDiSpesa(){
		return TipoProvvisorioDiCassa.S.equals(model.getProvvisorioCassa().getTipoProvvisorioDiCassa());
	}
	
	protected RicercaSinteticaModulareQuoteSpesaResponse executeRicercaQuoteSpesa() {

		RicercaSinteticaModulareQuoteSpesa reqRicerca = convertiModelPerChiamataServizioRicercaQuoteSpesa();
		RicercaSinteticaModulareQuoteSpesaResponse respRicerca = documentoSpesaService.ricercaSinteticaModulareQuoteSpesa(reqRicerca);
		
		if(respRicerca.isFallimento()){
			addErrori(methodName, respRicerca);
			return null;
		}
		
		List<SubdocumentoSpesa> lista = (respRicerca.getSubdocumentiSpesa()!=null? respRicerca.getSubdocumentiSpesa() : new ArrayList<SubdocumentoSpesa>());
		
		//Metto in sessione la lista ricevuta
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA, lista);
		model.setListaSubDocSpesa(lista);
		model.setResultSizeSubDocSpesa(respRicerca.getTotaleElementi());
		
		//totale importi doc:
		model.setTotImportiSubDoc(respRicerca.getTotale());
		
		return respRicerca;
	}
	
	private RicercaSinteticaModulareQuoteSpesa convertiModelPerChiamataServizioRicercaQuoteSpesa(){
		RicercaSinteticaModulareQuoteSpesa ricercaReq = new RicercaSinteticaModulareQuoteSpesa();
		ricercaReq.setRichiedente(sessionHandler.getRichiedente());
		
		ricercaReq.setRichiedente(sessionHandler.getRichiedente());
		ricercaReq.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setRequireTotale(false);
		
		ricercaReq.setModelDetails(SubdocumentoSpesaModelDetail.DocPadreModelDetail, DocumentoSpesaModelDetail.TipoDocumento, 
				DocumentoSpesaModelDetail.Stato, DocumentoSpesaModelDetail.Soggetto);
		
		//Filtro sul provvisorio di cassa:
		SubdocumentoSpesa subDocFiltro = new SubdocumentoSpesa();
		ProvvisorioDiCassa provvisorioCassaFiltro = new ProvvisorioDiCassa();
		provvisorioCassaFiltro.setAnno(model.getProvvisorioCassa().getAnno());
		provvisorioCassaFiltro.setNumero(model.getProvvisorioCassa().getNumero());
		subDocFiltro.setProvvisorioCassa(provvisorioCassaFiltro);
		ricercaReq.setSubdocumentoSpesa(subDocFiltro);
		//
		
		//paginazione:
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		addNumAndPageSize(parametriPaginazione, "ricercaQuoteSpesaID");
		//
		
		//addNumAndPageSize parte dalla pagina uno mentro questo servizio lavora dalla zero:
		changePageNumber(parametriPaginazione, -1);
		//
		
		//richiedo il totale degli importi:
		ricercaReq.setRequireTotale(true);
		
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		
		return ricercaReq;
	}
	
	protected RicercaSinteticaModulareQuoteEntrataResponse executeRicercaQuoteEntrata() {

		RicercaSinteticaModulareQuoteEntrata reqRicerca = convertiModelPerChiamataServizioRicercaQuoteEntrata();
		RicercaSinteticaModulareQuoteEntrataResponse respRicerca = documentoEntrataService.ricercaSinteticaModulareQuoteEntrata(reqRicerca);
		
		if(respRicerca.isFallimento()){
			addErrori(methodName, respRicerca);
			return null;
		}
		
		List<SubdocumentoEntrata> lista = (respRicerca.getSubdocumentiEntrata()!=null? respRicerca.getSubdocumentiEntrata() : new ArrayList<SubdocumentoEntrata>());
		
		//Metto in sessione la lista ricevuta
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_QUOTE_ENTRATA, lista);
		model.setListaSubDocEntrata(lista);
		model.setResultSizeSubDocEntrata(respRicerca.getTotaleElementi());
		
		//totale importi doc:
		model.setTotImportiSubDoc(respRicerca.getTotale());
		
		return respRicerca;
	}
	
	private RicercaSinteticaModulareQuoteEntrata convertiModelPerChiamataServizioRicercaQuoteEntrata(){
		RicercaSinteticaModulareQuoteEntrata ricercaReq = new RicercaSinteticaModulareQuoteEntrata();
		ricercaReq.setRichiedente(sessionHandler.getRichiedente());
		ricercaReq.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setRequireTotale(false);
		
		ricercaReq.setModelDetails(SubdocumentoEntrataModelDetail.DocPadreModelDetail, DocumentoEntrataModelDetail.TipoDocumento, 
				DocumentoEntrataModelDetail.Stato, DocumentoEntrataModelDetail.Sogg);
		
		//Filtro sul provvisorio di cassa:
		SubdocumentoEntrata subDocFiltro = new SubdocumentoEntrata();
		ProvvisorioDiCassa provvisorioCassaFiltro = new ProvvisorioDiCassa();
		provvisorioCassaFiltro.setAnno(model.getProvvisorioCassa().getAnno());
		provvisorioCassaFiltro.setNumero(model.getProvvisorioCassa().getNumero());
		subDocFiltro.setProvvisorioCassa(provvisorioCassaFiltro);
		ricercaReq.setSubdocumentoEntrata(subDocFiltro);
		//
		
		//paginazione:
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		addNumAndPageSize(parametriPaginazione, "ricercaQuoteEntrataID");
		
		//addNumAndPageSize parte dalla pagina uno mentro questo servizio lavora dalla zero:
		changePageNumber(parametriPaginazione, -1);
		//
		
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		//
		
		//richiedo il totale degli importi:
		ricercaReq.setRequireTotale(true);
		
		return ricercaReq;
	}
	
	
	protected RicercaOrdinativoResponse executeRicercaOrdinativi() {

		RicercaOrdinativo reqRicercaSub = convertiModelPerChiamataServizioRicercaOrdinativo();
		
		RicercaOrdinativoResponse respRicercaSub = null;
		
		if(isDiSpesa()){
			//respRicerca = ordinativoService.ricercaOrdinativoPagamento(reqRicerca);
			respRicercaSub = ordinativoService.ricercaOrdinativoPagamento(reqRicercaSub);
		} else {
			//respRicerca = ordinativoService.ricercaOrdinativoIncasso(reqRicerca);
			respRicercaSub = ordinativoService.ricercaOrdinativoIncasso(reqRicercaSub);
		}
		
		if(respRicercaSub.isFallimento()){
			addErrori("executeRicercaSubOrdinativi", respRicercaSub);
			return respRicercaSub;
		}
		
		if(isDiSpesa()){
			List<OrdinativoPagamento> lista = (respRicercaSub.getElencoOrdinativoPagamento()!=null? respRicercaSub.getElencoOrdinativoPagamento() : new ArrayList<OrdinativoPagamento>());
			//Metto in sessione la lista ricevuta
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ORD_PAG, lista);
			model.setListaOrdPag(lista);
			model.setResultSizeSubOrdPag(respRicercaSub.getNumRisultati());
		} else {
			List<OrdinativoIncasso> lista = (respRicercaSub.getElencoOrdinativoIncasso()!=null? respRicercaSub.getElencoOrdinativoIncasso() : new ArrayList<OrdinativoIncasso>());
			//Metto in sessione la lista ricevuta
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ORD_INC, lista);
			model.setListaOrdInc(lista);
			model.setResultSizeSubOrdInc(respRicercaSub.getNumRisultati());
		}
		//tot importi a fattor comune tra inc e pag:
		model.setTotImportiOrdinativi(respRicercaSub.getTotImporti());
		//
		return respRicercaSub;
	}
	
	/**
	 * Compone la request per il servizio di ricerca degli ordinativi
	 * @return
	 */
	protected RicercaOrdinativo convertiModelPerChiamataServizioRicercaOrdinativo() {
		//Istanzio la request:
		RicercaOrdinativo ricercaOrdinativo = new RicercaOrdinativo();
		//Popolo opportunamente i campi della request:
		
		//Richiedente
		ricercaOrdinativo.setRichiedente(sessionHandler.getRichiedente());
		//ente
		ricercaOrdinativo.setEnte(sessionHandler.getAccount().getEnte());
		//Anno Esercizio
		
		//bilancio corrente:
		ricercaOrdinativo.setBilancio(sessionHandler.getBilancio());
		
		if(isDiSpesa()){
			ParametroRicercaOrdinativoPagamento prop = new ParametroRicercaOrdinativoPagamento();
			prop.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			prop.setAnnoProvvCassa(model.getProvvisorioCassa().getAnno());
			prop.setNumeroProvvCassa(new BigDecimal(model.getProvvisorioCassa().getNumero()));
			ricercaOrdinativo.setParametroRicercaOrdinativoPagamento(prop);
			addNumAndPageSize(ricercaOrdinativo, "ricercaOrdPagID");
		} else {
			ParametroRicercaOrdinativoIncasso prop = new ParametroRicercaOrdinativoIncasso();
			prop.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			prop.setAnnoProvvCassa(model.getProvvisorioCassa().getAnno());
			prop.setNumeroProvvCassa(new BigDecimal(model.getProvvisorioCassa().getNumero()));
			ricercaOrdinativo.setParametroRicercaOrdinativoIncasso(prop);
			addNumAndPageSize(ricercaOrdinativo, "ricercaOrdIncID");
		}
		
		
		return ricercaOrdinativo;
	}



}
