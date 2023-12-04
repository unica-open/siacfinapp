/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.customdto.EsitoControlliDto;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCartaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.FiltriAggiuntiviRicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCartaResponse;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoDocumentiPerRegolazioneCartaAction extends WizardRegolarizzaCartaAction {

	
private static final long serialVersionUID = 1L;
	
	private String pulisciPagine;
	private boolean clearPagina;
	
	private String ckRigaSelezionata;
	
	public ElencoDocumentiPerRegolazioneCartaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	/**
	 * Costruise il titolo della pagina
	 */
	protected void buildTitoloDiPagina(){
		
		//setto il titolo:
		this.model.setTitolo("Collega a documento - Seleziona");

		StringBuffer titolo=new StringBuffer();
		titolo.append("Carta ");
		titolo.append(model.getDescrizioneCarta());
		
		if (this.model.isOptionsPagamentoEstero()) {
			titolo.append(" - Valuta Estera ");
			titolo.append(this.model.getCodiceDivisa());
		}
		
		//setto il titolo di step:
		this.model.setTitoloStep(titolo.toString());
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		buildTitoloDiPagina();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		if(null!=pulisciPagine && pulisciPagine.equalsIgnoreCase(WebAppConstants.Si)){
			setClearPagina(true);
		}
		
		//se e' presente la paginazione nell'elenco allora la mantengo in sessione
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			
			// ho cliccato sulla paginazione
			int paginata = paginazioneRichiesta(ServletActionContext.getRequest());
			if(paginata!=0){
				model.getRicercaDocumenti().setPremutoPaginazione(paginata);
			}
			
		}
		
		model.getRicercaDocumenti().setCheckWarningSenzaProvvisorioDiCassa(false);
		
		//exeguiamo la ricerca:
		executeRicercaSubDocumentiDaAssociare();
		
		
		// pulisce le liste quando effettuo una ricerca
		puliscoDatiRicerca();
		
	    return SUCCESS;
	}
	
	protected String executeRicercaSubDocumentiDaAssociare(){
		
		//1. Costruisco l'oggetto RicercaQuotaSpesa:
		RicercaQuotaSpesa ricercaQuotaSpesa = buildRicercaQuotaSpesaPerRicercaSubDocumenti(this.model.getRicercaDocumenti());
		//
		
		//2. Costruisco la requst per il servizio:
		RicercaDocumentiCarta ricercaDocumentiCarta = buildRicercaDocumentiCartaPerRicercaSubDocumenti(ricercaQuotaSpesa);
		
		//2.5 Filtri aggiuntivi specifici di questo scenario:
		FiltriAggiuntiviRicercaDocumentiCarta filtriAggiuntivi = new FiltriAggiuntiviRicercaDocumentiCarta();
		filtriAggiuntivi.setTipiDocDaEscludere(FinStringUtils.toList("NCD","CCN"));
		
		BigDecimal importoLiquidabileEsatto = model.getDettaglioRiga().getImporto();
		// se serve fake per fare dei test: BigDecimal importoLiquidabileEsatto = new BigDecimal(236141.57)
		
		importoLiquidabileEsatto = importoLiquidabileEsatto.setScale(2, RoundingMode.DOWN);
		
		filtriAggiuntivi.setImportoLiquidabileEsatto(importoLiquidabileEsatto);
		ricercaDocumentiCarta.setFiltriAggiuntivi(filtriAggiuntivi);
		//
		
		//3. Chiamata al servizio di ricerca 
		RicercaDocumentiCartaResponse ricercaDocCartaResp = cartaContabileService.ricercaDocumentiCarta(ricercaDocumentiCarta);
		
		//4. Controllo errori nella response:
		if(ricercaDocCartaResp!=null && ricercaDocCartaResp.isFallimento()){
			addErrori(ricercaDocCartaResp.getErrori());
			return INPUT;
		}
		
		RicercaQuotaSpesaResponse response = ricercaDocCartaResp.getResponseQuoteSpesa();
		
		if(response!=null && response.isFallimento()){
			addErrori(response.getErrori());
			return INPUT;
		}
		
		//5. Gestione della response:
		gestioneResponseRicercaSubDocumenti(response, ricercaQuotaSpesa, this.model.getRicercaDocumenti());
		
		return SUCCESS;
	}
	
	private void puliscoDatiRicerca(){
		// pulisci il form di ricerca con 
		// tutti i suoi campi
		model.getRicercaDocumenti().setProvvedimento(new  ProvvedimentoImpegnoModel());
		model.getRicercaDocumenti().setHasImpegnoSelezionatoXPopup(false);
		model.getRicercaDocumenti().setProvvedimentoSelezionato(false);
		model.getRicercaDocumenti().setSoggettoSelezionato(false);
		model.getRicercaDocumenti().setAnnoImpegno(null);
		// jira-1380
		model.getRicercaDocumenti().setNumeroImpegno(null);
		model.getRicercaDocumenti().setNumeroSub(null);
		model.getRicercaDocumenti().setnImpegno(null);
		model.getRicercaDocumenti().setnSubImpegno(null);
		model.getRicercaDocumenti().setSoggetto(new SoggettoImpegnoModel());
	}
	
	protected void pulisciChecksWarningPopUp(){
		//pulisco i warning per evitare la comparsa dei popup:
		model.getRicercaDocumenti().setCheckConfermaCollega(false);
		model.getRicercaDocumenti().setCheckWarningSenzaProvvisorioDiCassa(false);
		//
	}
	
	/**
	 * Passo 1 dopo aver cliccato collega
	 * @return
	 */
	public String preCheckCollegaDocumento(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		//Questo metodo non fa controlli, presenta sempre il pop-up con 
		//cui viene chiesto se si vuole procedere (per evitare il click per errore)
		
		//A questo punto posso avere dei warning da sottoporre nel pop up
		//oppure proseguo senza segnalazioni sui soggetti:
		
	    if(!model.getRicercaDocumenti().isCheckConfermaCollega()){
	    	//setto il flag a true:
	    	model.getRicercaDocumenti().setCheckConfermaCollega(true);
	    	return INPUT;
	    } else {
	    	//return prosegui();
	    	return preCheckControlliDiMerito();
	    }
		
	}
	
	/**
	 * Passo 2 dopo aver cliccato collega
	 * @return
	 */
	public String preCheckControlliDiMerito(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		//lancio i controlli di merito:
		EsitoControlliDto cotrolliDiMerito = controlliDiMerito();
		
		//se ci sono errori bloccanti blocco tutto subito:
		if(!checkAndAddErrors(cotrolliDiMerito.getListaErrori())){
			return INPUT;
		}
		
		//A questo punto posso avere dei warning da sottoporre nel pop up
		//oppure proseguo senza segnalazioni sui soggetti:
		
	    return preCheckPresenzaProvvisiorioDiCassa();
		
	}
	
	/**
	 * Passo 3 dopo aver cliccato collega
	 * @return
	 */
	public String preCheckPresenzaProvvisiorioDiCassa(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		
		//CONTROLLI DI OBBLIGATORIETA: non servono perche'
		// sono sequenziale al preCheckCoerenzaSoggetti
		
		
		//lancio i controlli di merito:
		EsitoControlliDto controlliProvvCassa = controlloPresenzaProvvisoriDiCassa();
		
		//se ci sono errori bloccanti blocco tutto subito:
		if(!checkAndAddErrors(controlliProvvCassa.getListaErrori())){
			return INPUT;
		}
		
		//A questo punto posso avere dei warning da sottoporre nel pop up
		//oppure proseguo senza segnalazioni sui soggetti:
		
	    if(controlliProvvCassa.presenzaWarning()){
	    	//Aggiungo i messaggi di warning ottenuti:
//	    	addActionWarnings(controlliProvvCassa.getListaWarning());
	    	//setto il flag a true:
	    	model.getRicercaDocumenti().setCheckWarningSenzaProvvisorioDiCassa(true);
	    	return INPUT;
	    } else {
	    	return collega();
	    }
		
	}
	
	/**
	 * Chiama il servizio CollegaQuotaDocumentoARigaCarta
	 * @return
	 */
	public String collega(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		//2. RICHIAMIAMO IL SERVIZIO:
		
		//Costruisco la request
		CollegaQuotaDocumentoARigaCarta requestCollega = buildCollegaRequest();
		
		//invoco il servizio:
		CollegaQuotaDocumentoARigaCartaResponse collegaResp = cartaContabileService.collegaQuotaDocumentoARigaCarta(requestCollega);
		
		//analizzo la resp del servizio:
		if(isFallimento(collegaResp)){
			addErrori(collegaResp);
			return INPUT;
		}
		
		//ok
		return "tornaARegolazioneCartaPerDocCollegato";
	}
	
	/**
	 * Crea la request per l'invocazionde del servizio CollegaQuotaDocumentoARigaCartaService
	 * @return
	 */
	private CollegaQuotaDocumentoARigaCarta buildCollegaRequest(){
		
		//istanzio l'oggetto di request:
		CollegaQuotaDocumentoARigaCarta requestCollega = new CollegaQuotaDocumentoARigaCarta();
		
		//leggo il sub documento selezioanto:
		SubdocumentoSpesa subDoc = getSelezionato();
		// e lo setto in request:
		requestCollega.setSubDocumentoDaCollegare(subDoc);
		
		//leggo la riga carta in questione:
		PreDocumentoCarta preDocCarta = model.getDettaglioRiga();
		
		//e la setto nella request:
		requestCollega.setRigaCarta(preDocCarta);
		
		//setto la carta:
		requestCollega.setCartaContabile(model.getCartaContabileDaRicerca());
		
		//setto l'anno di bilancio:
		requestCollega.setAnnoBilancio(new Integer(sessionHandler.getAnnoEsercizio()));
		
		//data di invocazione:
		requestCollega.setDataOra(new Date());
		
		//ente:
		requestCollega.setEnte(sessionHandler.getEnte());
		
		//richiedente:
		requestCollega.setRichiedente(sessionHandler.getRichiedente());
		
		//BILANCIO
		requestCollega.setBilancio(sessionHandler.getBilancio());
		
		//termino restituendo la request appena composta:
		return requestCollega;
	}
	
	private SubdocumentoSpesa getSelezionato(){
		return FinUtility.getById(model.getRicercaDocumenti().getElencoSubdocumentoSpesa(), model.getRicercaDocumenti().getRadioUidDocSelezionato());
	}
	
	
	/**
	 * Qui implementare i vari controlli bloccanti al collegamento della quota selezionato
	 * @return
	 */
	private EsitoControlliDto controlliDiMerito() {
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		//1. Deve essere stato selezionato un documento:
		int uidSelezionato = model.getRicercaDocumenti().getRadioUidDocSelezionato();
		if(uidSelezionato<=0){
			esito.addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare una quota documento da collegare"));
			return esito;
		}
		
		//2. Nel caso l'elemento selezionato non trovasse riscontro in lista documenti:
		SubdocumentoSpesa selezionato = getSelezionato();
		if(selezionato==null){
			esito.addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare una quota documento da collegare"));
			return esito;
		}
		
		return esito;
	}
	
	/**
	 * Qui implementare i controlli sulla presenza di 
	 * provvisori di cassa per la quota selezionata
	 * @return
	 */
	private EsitoControlliDto controlloPresenzaProvvisoriDiCassa() {
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		SubdocumentoSpesa subdoc = getSelezionato();
		
		if(subdoc == null || subdoc.getUid() == 0) {
			esito.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento"));
			return esito;
		}
		
		if(subdoc.getProvvisorioCassa() == null || subdoc.getProvvisorioCassa().getUid() == 0) {
			esito.addWarning(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("subdocumento senza provvisorio di cassa"));
		}
		
		
		return esito;
	}
	
	/*
	public String inserisciInCarta(){
		
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		
		//CONTROLLIAMO LA SELEZIONE:
		if(StringUtils.isEmpty(getCkRigaSelezionata())){
			//KO
			addErrore(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore(""));	
		}
		if(hasActionErrors()) return INPUT;
		//
		
		String[] righeSelezionate = getCkRigaSelezionata().split(",");
		
		List<SubdocumentoSpesa> subDocSelezionati = FinUtility.getById(model.getRicercaDocumenti().getElencoSubdocumentoSpesa(), righeSelezionate);
		
		List<PreDocumentoCarta> righeDaDoc = new ArrayList<PreDocumentoCarta>();
		
		for(SubdocumentoSpesa itSel : subDocSelezionati){
			PreDocumentoCarta itRiga = popolaRigaDaDocumento(itSel);
			righeDaDoc.add(itRiga);
		}
		
		//salviamo la selezione:
		putListaPreDocDaDocumentiInSession(righeDaDoc);
		
		clearActionData();
		
		//Rimuoviamo la fotocopia del model che usiamo per comunicare i dati al sotto caso d'uso nuova riga da documenti:
		ActionContext.getContext().getSession().remove("KEY_GESTIONE_CARTA_MODEL");

		return "vaiStep2";
	}
	
	
	private PreDocumentoCarta popolaRigaDaDocumento(SubdocumentoSpesa subDocumento){
		
		PreDocumentoCarta riga = new PreDocumentoCarta();
		DocumentoSpesa docSpesa = subDocumento.getDocumento();
		
		//DESCRIZIONE:
		riga.setDescrizione(subDocumento.getDescrizione());
		
		//documenti, collegato al sub doc da cui deriva:
		List<SubdocumentoSpesa> subDocs = new ArrayList<SubdocumentoSpesa>();
		subDocs.add(subDocumento);
		riga.setListaSubDocumentiSpesaCollegati(subDocs);
		
		//IMPORTO:
		riga.setImporto(subDocumento.getImporto());
		
		//campi Soggetto
		Soggetto soggetto = docSpesa.getSoggetto();
		if(soggetto!=null && !FinStringUtils.isEmpty(soggetto.getCodiceSoggetto())){			
			riga.setSoggetto(new Soggetto());
			riga.getSoggetto().setCodiceSoggetto(soggetto.getCodiceSoggetto());
			if(!FinStringUtils.isEmpty(soggetto.getDenominazione())){
				riga.getSoggetto().setDenominazione(soggetto.getDenominazione());
			}
			List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoList.add(subDocumento.getModalitaPagamentoSoggetto());
			riga.getSoggetto().setElencoModalitaPagamento(modalitaPagamentoList);
		}
		
		//campi impegno
		Impegno impegno = subDocumento.getImpegno();
		SubImpegno subImp = subDocumento.getSubImpegno();
		if(impegno!=null && impegno.getAnnoMovimento()!=0 && impegno.getNumero()!=null){
			riga.setImpegno(new Impegno());
			riga.getImpegno().setNumero(impegno.getNumero());
			riga.getImpegno().setAnnoMovimento( impegno.getAnnoMovimento());
			if(subImp!=null){
				SubImpegno subImpegno = new SubImpegno();
				List<SubImpegno> listSubImpegni = new ArrayList<SubImpegno>();
				subImpegno.setNumero(subImp.getNumero());
				listSubImpegni.add(subImpegno);
				riga.getImpegno().setElencoSubImpegni(listSubImpegni);
			}			
		}

		//CONTO TESORERIA
		if(subDocumento.getContoTesoreria()!=null){
			ContoTesoreria contoTesoreria = new ContoTesoreria();
			contoTesoreria.setCodice(subDocumento.getContoTesoreria().getCodice());
			riga.setContoTesoreria(contoTesoreria);
		} else {
			riga.setContoTesoreria(null);
		}
		
		//MODALITA PAGAMENTO SOGGETTO
		riga.setModalitaPagamentoSoggetto(subDocumento.getModalitaPagamentoSoggetto());
		
		return riga;
	}	*/
	
	
	public String indietro(){
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		return "vaiARicercaDocumenti";
	}
	

	public String getPulisciPagine() {
		return pulisciPagine;
	}

	public void setPulisciPagine(String pulisciPagine) {
		this.pulisciPagine = pulisciPagine;
	}

	public boolean isClearPagina() {
		return clearPagina;
	}

	public void setClearPagina(boolean clearPagina) {
		this.clearPagina = clearPagina;
	}

	public String getCkRigaSelezionata() {
		return ckRigaSelezionata;
	}

	public void setCkRigaSelezionata(String ckRigaSelezionata) {
		this.ckRigaSelezionata = ckRigaSelezionata;
	}
	
}
