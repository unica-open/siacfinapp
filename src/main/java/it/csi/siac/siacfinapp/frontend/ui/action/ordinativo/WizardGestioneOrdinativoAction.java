/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.customdto.SoggSedeModPagOrdinativoDto;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.CausaleEntrataTendinoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.DettaglioQuotaOrdinativoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.GestioneOrdinativoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.GestioneOrdinativoStep2Model;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.RicercaProvvisorioModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.EntitaUtils;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaLiquidazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.SoggettoSedeModPagInfo;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class WizardGestioneOrdinativoAction extends GenericOrdinativoAction<GestioneOrdinativoModel>{

	private static final long serialVersionUID = 1L;   

	
	protected static final String MODPAGAMENTO = "modpagamento";
	protected static final String SEDISECONDARIE = "sedisecondarie";
	protected static final String BENEFICIARIO = "BN";
	
	protected static final String FORM = "FORM";
	protected static final String LABEL_TITOLO = "TITOLO";
	protected static final String LABEL_SOGGETTO = "SOGGETTO";
	protected static final String DETTAGLIO_AGGIORNA_QUOTA ="dettaglioAggiornaQuota";
	protected static final String DETTAGLIO_CONSULTA_QUOTA ="dettaglioConsultaQuota";

	protected static final String DETTAGLIO_AGGIORNA_COPERTURE ="dettaglioAggiornaCoperture";
	protected static final String GOTO_CONSULTA = "gotoConsulta";
	public static final String RETURN_AGGIORNA ="gotoGestioneOrdinativo"; 
	protected static final String TIPO_IMPEGNO_I = "I";
	protected static final String TIPO_ACCERTAMENTO_A = "A";
	
	protected static final String TIPO_ORDINATIVO_INCASSO_I = "I";
	protected static final String TIPO_ORDINATIVO_PAGAMENTO_P = "P";
	
	private static final String ERRORE_FORMATO_DATA_POP_UP = "Il formato del parametro data fine non e' valido e del tipo dd/MM/yyyy"; 
	private static final String ERRORE_DATA_NON_IN_ESERCIZIO = "La data della ricerca deve appartenere all'anno esercizio"; 
	protected static final String ERRORE_CONGRUENZA_COPERTURE = "Verificare che l'importo delle quote sia congruente con quello delle coperture";
	protected static final String ERRORE_TOT_COPERTURE = "E' necessario collegare dei provvisori affinche' il totale da collegare sia uguale a zero";
	protected static final String ERRORE_ORDINATIVO_COLLEGATO_PRESENTE = "Ordinativo gia' presente nella lista dei collegati";
	protected static final String ERRORE_TOTALE_IMPORTO_COLLEGATI = "L'importo degli ordinativi collegati e' superiore all'importo del mandato di pagamento";
	protected static final String ERRORE_ORDINATIVI_COLLEGATI_SPESA= "L'ordinativo indicato e' gia' collegato con altri ordinativi di spesa";
	protected static final String ERRORE_ORDINATIVI_COLLEGATI_ENTRATA= "L'ordinativo indicato e' gia' collegato con altri ordinativi di entrata";
	protected static final String ERRORE_IMPORTO_REGOLARIZZATO_MAGGIORE_DEL_REGOLARIZZABILE_QUOTA = "FIN_ERR_018 - Importo da regolarizzare errato: e' superiore all'importo dell'ordinativo";
	protected static final String ERRORE_IMPORTO_REGOLARIZZATO = "FIN_ERR_018 - Importo da regolarizzare errato";
	protected static final String ERRORE_IMPORTO_REGOLARIZZATO_SUL_REGOLARIZZABILE_PROVVISORIO_DI_CASSA = "FIN_ERR_018 - Importo da regolarizzare errato: e' superiore all'importo da regolarizzare";
	
	private String numeroOrdinativo;
	private String annoOrdinativo;
	
	private boolean idSedeCreditoreChecked;
	
	private String numeroOrdinativoStruts;
	private String annoOrdinativoStruts;
	private AttoAmministrativo provvedimentoOrdinativo = new AttoAmministrativo();
	private Soggetto soggettoOrdinativo = new Soggetto();
	private ClasseSoggetto classeOrdinativo = new ClasseSoggetto();
	private CapitoloUscitaGestione capitoloUscitaTrovato = new CapitoloUscitaGestione();
	private CapitoloEntrataGestione capitoloEntrataTrovato = new CapitoloEntrataGestione();
	
	private boolean quotaIncassoImpOver;
	
	private boolean ancoraVisualizza = false;
	private boolean ancoraVisualizzaInserisciProv = false;
	private boolean ancoraVisualizzaInserisciProvErrori = false;
	
	@Autowired
	protected transient OrdinativoService ordinativoService;
	
	@Autowired
	protected transient ProvvisorioService provvisorioService;
	
	
	private boolean attivaBtnSalva;
	
	private boolean disabilitaBtnAzioni;
	
	private String numeroQuotaEliminato;
	private String numeroPerDettaglio;
	private boolean toggleRicercaProvvAperto = false;
	
	private String numeroPerDettaglioCopertura;

	private String idCoperturaDaEliminare;
	
	
	private boolean clearStatus;
	
	protected static final String OPERAZIONE_INSERIMENTO = "inserimento";
	protected static final String OPERAZIONE_MODIFICA = "modifica";
	
	protected static final String SELEZIONARE_PROVV = "Selezionare un provvisorio di cassa";
		
	// usato per il seleziona provvisorio
	private String uidProvvDaRicerca;
	
	
	// CR-1911 costanti per la gestione dei controlli dei campi seguenti, per profilo e stato ordinativo
	protected static final String FIELD_TESORIERE = "TESORIERE";
	protected static final String FIELD_BENEFICIARI_MULTIPLI = "BENEFICIARI_MULTIPLI";
	protected static final String FIELD_A_COPERTURA = "A_COPERTURA";
	protected static final String FIELD_DESCRQUOTA = "DESCRQUOTA"; 
	protected static final String FIELD_IMPQUOTA = "IMPQUOTA";
	protected static final String TAB_PROVVISORI = "TAB_PROVVISORI"; 
		
	
	
	/**
	 * Si occupa di impostare nell'Ordinativo di Incasso la causale editata dall'utente nella
	 * maschera e salvata nel model.
	 * 
	 * Da utilizzare al momento del salvataggio (inserimento o aggiorna) per settare il 
	 * dato nella composizione della request.
	 * 
	 * @param ordinativoIncasso
	 */
	protected OrdinativoIncasso impostaCausaleEntrataDalModel(OrdinativoIncasso ordinativoIncasso){
		//Causale model:
		CausaleEntrataTendinoModel modelCausali = model.getGestioneOrdinativoStep1Model().getCausaleEntrataTendino();
		if(modelCausali!=null){
			String codCausale = modelCausali.getCodiceCausale();
			if(codCausale!=null){
				CausaleEntrata causale = causaleByCodiceSelezionato(codCausale,modelCausali);
				ordinativoIncasso.setCausale(causale);
			} else {
				//il dato e' facoltativo
				ordinativoIncasso.setCausale(null);
			}
		}
		return ordinativoIncasso;
	}
	
	/**
	 * Si occupa di settare opportunamente nelle variabili di appoggio nel model
	 * i dati della causale dell'ordinativo appena caricato.
	 * 
	 * Provvede anche al caricamento della lista delle causali dello stesso tipo di quella 
	 * dell'ordinativo per poter permettere all'utente di cambiarla (eventualemnte).
	 * 
	 * @param ordinativoCaricato
	 */
	protected void impostaCausaleEntrataNelModel(OrdinativoIncasso ordinativoCaricato){
		if(ordinativoCaricato!=null){
			CausaleEntrata causaleEntrata = ordinativoCaricato.getCausale();
			CausaleEntrataTendinoModel modelCausali = model.getGestioneOrdinativoStep1Model().getCausaleEntrataTendino();
			if(causaleEntrata!=null && modelCausali!=null){
				//leggo il tipo della causale:
				TipoCausale tipo = causaleEntrata.getTipoCausale();
				if(tipo!=null){
					//invoco il metodo che carica le causali (a partire dal tipo):
					List<CausaleEntrata> listaCausali = caricaTendinoCausaliEntrata(tipo);
					//setto i codici nel model:
					modelCausali.setCodiceCausale(causaleEntrata.getCodice());
					modelCausali.setCodiceTipoCausale(tipo.getCodice());
					//setto la lista della causali nel model:
					modelCausali.setListaCausali(listaCausali);
				}
			}
		}
	}
	
	/**
	 * oggettoDaPopolarePagamento
	 * @return
	 */
	public boolean oggettoDaPopolarePagamento(){
		//tipologia pagamento
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO);
	}
	
	/**
	 * getNumeroPerDettaglioCopertura
	 * @return
	 */
	public String getNumeroPerDettaglioCopertura() {
		return numeroPerDettaglioCopertura;
	}

	/**
	 * setNumeroPerDettaglioCopertura
	 * @param numeroPerDettaglioCopertura
	 */
	public void setNumeroPerDettaglioCopertura(String numeroPerDettaglioCopertura) {
		this.numeroPerDettaglioCopertura = numeroPerDettaglioCopertura;
	}

	/**
	 * implementazione dell'interfaccia listaClasseSoggettoChanged
	 */
	@Override
	public String listaClasseSoggettoChanged() {
		//resetto il soggetto
		model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore(null);
		model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(false);
		//aggiorna header soggetto
		return "headerSoggetto";	
	}

	/**
	 * setSoggettoSelezionato dell'oggetto passato al model dello step 1
	 */
	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		//imposto nello step 1 model:
		model.getGestioneOrdinativoStep1Model().setSoggetto(soggettoImpegnoModel);
		model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(true);
	}
	
	/**
	 * setCapitoloSelezionato dal model passato al model dello step 1
	 */
	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		//setto nel model dello step1:
		model.getGestioneOrdinativoStep1Model().setCapitolo(supportCapitolo);
		model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(true);
		//dato per visualizza:
		model.setDatoPerVisualizza(supportCapitolo);
		//resete lista ricerca capitolo:
		model.setListaRicercaCapitolo(null);
		//pongo a false il capitolo trovato:
		model.setCapitoloTrovato(false);
	}

	/**
	 * setErroreCapitolo
	 */
	@Override
	protected void setErroreCapitolo() {
		StringBuilder supportMessaggio = new StringBuilder(); 
		//costruisco il messaggio tramite append
		supportMessaggio.append(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno());
		supportMessaggio.append((model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo() != null) ? "/" + model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo() : "");
		supportMessaggio.append((model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo() != null) ? "/" + model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo() : "");
		supportMessaggio.append((model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb() != null) ? "/" + model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb() : "");
		//aggiungo l'errore
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", supportMessaggio.toString()));
	}

	/**
	 *  attiva o disattiva il btn della pagina step2
	 */
	protected void attivaBottoneSalva(){
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
			// attivo il btn prosegui
			setAttivaBtnSalva(false);
			
		}else {
			// attivo il btn salva
			setAttivaBtnSalva(true);	 
		}
	}
	
	/**
	 * 
	 * Attiva Bottone Azioni
	 * 
	 * @param attivaPulsanteAggiornaQuota
	 * @param aggiornaOrdinativo
	 */
	protected void attivaBottoneAzioni(boolean attivaPulsanteAggiornaQuota, boolean aggiornaOrdinativo){
		if(!attivaPulsanteAggiornaQuota && aggiornaOrdinativo){
			//disabilitato
			setDisabilitaBtnAzioni(true);
		}else{
			//abilitato
			setDisabilitaBtnAzioni(false);
		}
	}
	
	
	/**
	 *  effettua la sommatoria delle righe delle quote nello step2
	 * 
	 */
	protected void sommatoriaQuoteSubOrdPagamento() {
		if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaLiquidazioni())){
			//ci sono liquidazioni
			BigDecimal sommatoria = FinUtility.sommaImportiAttuali(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti());
			model.getGestioneOrdinativoStep2Model().setSommatoriaQuote(sommatoria);
		}
	}
	
	/**
	 *  effettua la sommatoria delle righe delle quote nello step2
	 * 
	 */
	protected void sommatoriaQuoteSubOrdIncasso() {
		if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaAccertamento())){
			//ci sono accertamenti
			BigDecimal sommatoria =  FinUtility.sommaImportiAttuali(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso());
			model.getGestioneOrdinativoStep2Model().setSommatoriaQuote(sommatoria);
		}
	}
	
	/**
	 * SommatoriaQuoteSubOrdPagamentoPerAggiorna
	 */
	protected void sommatoriaQuoteSubOrdPagamentoPerAggiorna(){
		if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti())){
			//ci sono sub ordinativi
			BigDecimal sommatoria = FinUtility.sommaImportiAttuali(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti());
			model.getGestioneOrdinativoStep2Model().setSommatoriaQuote(sommatoria);
		}
	}
	
	/**
	 * sommatoriaQuoteSubOrdIncassoPerAggiorna
	 */
	protected void sommatoriaQuoteSubOrdIncassoPerAggiorna(){
		if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso())){
			//ci sono sub ordinativi
			BigDecimal sommatoria = FinUtility.sommaImportiAttuali(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso());
			model.getGestioneOrdinativoStep2Model().setSommatoriaQuote(sommatoria);
		}
	}
	
	/**
	 * Carica le liste per le combo
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListeCombo() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.DISTINTA, TipiLista.CONTO_TESORERIA, TipiLista.CODICE_BOLLO, TipiLista.COMMISSIONI,TipiLista.AVVISO,TipiLista.NOTE_TESORIERE);
		
		//DISTINTA
		model.getGestioneOrdinativoStep1Model().setListaDistinta(((List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA)));
		
		//CONTO_TESORERIA
		model.getGestioneOrdinativoStep1Model().setListaContoTesoriere(((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)));		
		//model.getGestioneOrdinativoStep3Model().setListaContoTesoriere(((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)));
		
		//CODICE_BOLLO
		model.getGestioneOrdinativoStep1Model().setListaBollo(((List<CodificaFin>)mappaListe.get(TipiLista.CODICE_BOLLO)));
		
		//COMMISSIONI
		model.getGestioneOrdinativoStep1Model().setListaCommissioni(((List<CodificaFin>)mappaListe.get(TipiLista.COMMISSIONI)));
		
		//AVVISO
		model.getGestioneOrdinativoStep1Model().setListaAvviso(((List<CodificaFin>)mappaListe.get(TipiLista.AVVISO)));
		
		//NOTE_TESORIERE
		model.getGestioneOrdinativoStep1Model().setListaNoteTesoriere (((List<CodificaFin>)mappaListe.get(TipiLista.NOTE_TESORIERE)));
		
	}
	
	@SuppressWarnings("unchecked")
	protected void caricaListeStep3() {
		if(model != null && model.getGestioneOrdinativoStep3Model() !=null){
			Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CONTO_TESORERIA);
			List<CodificaFin> listaCt = ((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA));
			model.getGestioneOrdinativoStep3Model().setListaContoTesoriere(listaCt);
		}
	}

	protected void impostaDefaultContoTesoriere(boolean inAggiornamento){
		// SIAC-5933 - SIAC-6029
		if(!inAggiornamento && model.getGestioneOrdinativoStep1Model()!=null && !isEmpty(model.getGestioneOrdinativoStep1Model().getListaContoTesoriere())){
			for(CodificaFin it: model.getGestioneOrdinativoStep1Model().getListaContoTesoriere()){
				if(it.getCodice().startsWith("0000100")){
					if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria()==null){
						//se e' null sono appena entrato nel caso d'uso di inserimento
						model.getGestioneOrdinativoStep1Model().getOrdinativo().setContoTesoreria(new ContoTesoreria());
						model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().setCodice(it.getCodice());
					}
				}
			}
		}
		//
	}

	/**
	 * Setta nel model dello step 1 il provvedimento selezionato
	 * 
	 * Implemente l'interfaccia generica per le action con provvedimenti
	 * 
	 */
	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.getGestioneOrdinativoStep1Model().setProvvedimento(currentProvvedimento);
		model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(true);
	}
	
	
	protected String selezionaSoggettoOrdinativo() {
		
		// recupero solo i soggetti validi o sospesi
		String ritorno = super.selezionaSoggettoControlloInSospeso();
		
		if(model.getGestioneOrdinativoStep1Model().getSoggetto()!=null){
			
			ricercaSoggettoPerChiaveOrdinativo();
			
			if(oggettoDaPopolarePagamento()){
				model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(null);
				model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(0);
				model.getGestioneOrdinativoStep1Model().setSedeSelezionata(null);
				model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
				model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiPagamenti(new ArrayList<SubOrdinativoPagamento>());
			}else{
				
				// pulisco la lista di incasso
				model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiIncasso(new ArrayList<SubOrdinativoIncasso>());
				
			}
		}
		
		return ritorno;
		
	}
	
	
	/**
	 * carica oggetto per il capitolo entrata
	 * @param cug
	 * @return
	 */
	protected CapitoloImpegnoModel caricaDatiCapitoloEntrata(CapitoloEntrataGestione cug){	
			
			CapitoloImpegnoModel capitoloImpegnoModel = new CapitoloImpegnoModel();
			
			if(cug.getAnnoCapitolo()!=null){ 
				capitoloImpegnoModel.setAnno(cug.getAnnoCapitolo());
			}
			
			if(cug.getNumeroCapitolo()!=null){ 
				capitoloImpegnoModel.setNumCapitolo(cug.getNumeroCapitolo());
			}
			
			if(cug.getNumeroArticolo()!=null){ 
				capitoloImpegnoModel.setArticolo(cug.getNumeroArticolo());
			}
			
			if(cug.getNumeroUEB()!=null){ 
				capitoloImpegnoModel.setUeb(new BigInteger(String.valueOf(cug.getNumeroUEB())));
			}
			
			if(cug.getUid()!=0){ 
				capitoloImpegnoModel.setUid(cug.getUid());
			}
			
			if(cug.getTipoFinanziamento()!=null){ 
				capitoloImpegnoModel.setTipoFinanziamento(cug.getTipoFinanziamento().getDescrizione());
			}
			
				
			if (cug.getElementoPianoDeiConti() != null) {
				capitoloImpegnoModel.setCodicePdcFinanziario(cug.getElementoPianoDeiConti().getCodice());
				capitoloImpegnoModel.setDescrizionePdcFinanziario(cug.getElementoPianoDeiConti().getDescrizione());
				capitoloImpegnoModel.setIdPianoDeiConti(cug.getElementoPianoDeiConti().getUid());
			} 

			//non macroaggragato per capEntrata
			
						
			if (cug.getDescrizione() != null) {
				capitoloImpegnoModel.setDescrizione(cug.getDescrizione());
			} 
		
			if (cug.getElementoPianoDeiConti() != null) {
				capitoloImpegnoModel.setElementoPianoDeiConti(cug.getElementoPianoDeiConti());
			} 
			
			if (cug.getTipoFinanziamento()!= null) {
				capitoloImpegnoModel.setFinanziamento(cug.getTipoFinanziamento());
			} 
			
			// siope
			if (cug.getSiopeEntrata() != null) {
				// lo metto comunque in siope spesa
				capitoloImpegnoModel.setCodiceSiopeSpesa(cug.getSiopeEntrata().getCodice());
				capitoloImpegnoModel.setDescrizioneSiopeSpesa(cug.getSiopeEntrata().getDescrizione());
			}
			
			if (cug.getStrutturaAmministrativoContabile()!= null && cug.getStrutturaAmministrativoContabile().getDescrizione() != null) {
				capitoloImpegnoModel.setCodiceStrutturaAmministrativa(cug.getStrutturaAmministrativoContabile().getCodice());
				capitoloImpegnoModel.setDescrizioneStrutturaAmministrativa(cug.getStrutturaAmministrativoContabile().getDescrizione());
				capitoloImpegnoModel.setUidStruttura(cug.getStrutturaAmministrativoContabile().getUid());
			} 
			
			if(!isEmpty(cug.getListaImportiCapitolo())){
				capitoloImpegnoModel.setImportiCapitoloEG(cug.getListaImportiCapitolo());
				ImportiCapitoloModel supportImporti;
				for (ImportiCapitoloEG currentImporto : cug.getListaImportiCapitolo()) {
					supportImporti = new ImportiCapitoloModel();
					supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
					supportImporti.setCassa(currentImporto.getStanziamentoCassa());
					supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
					supportImporti.setCompetenza(currentImporto.getStanziamento());
					// RM 08042015, commentato perche' non usato, verifica pulizia functione e attibuti entite' non piu usate
					capitoloImpegnoModel.getImportiCapitolo().add(supportImporti);
				}
			} 
			
			// COFOG NO

			
			// transazione eurotpea entrata
			
			if(cug.getTransazioneUnioneEuropeaEntrata()!=null){
				capitoloImpegnoModel.setCodiceTransazioneEuropeaEntrata(cug.getTransazioneUnioneEuropeaEntrata().getCodice());
			}
			
			
			// CUP NO

			
			// RICORRENTE ENTRATA
			if(cug.getRicorrenteEntrata()!=null){
				capitoloImpegnoModel.setCodiceRicorrenteEntrata(cug.getRicorrenteEntrata().getCodice());
			}
			
			// capitolo sanitario entrata
			if(cug.getPerimetroSanitarioEntrata()!=null){
				capitoloImpegnoModel.setCodicePerimetroSanitarioEntrata(cug.getPerimetroSanitarioEntrata().getCodice());
			}
			
		
			// politiche regionali NO
			
			return capitoloImpegnoModel;
			
		}	
	
	/**
	 * carica oggetto per il capitolo uscita
	 * @param cug
	 * @return
	 */
protected CapitoloImpegnoModel caricaDatiCapitolo(CapitoloUscitaGestione cug){	//Da spostare	
		
		CapitoloImpegnoModel capitoloImpegnoModel = new CapitoloImpegnoModel();
		
		if(cug.getAnnoCapitolo()!=null){ 
			capitoloImpegnoModel.setAnno(cug.getAnnoCapitolo());
		}
		
		if(cug.getNumeroCapitolo()!=null){ 
			capitoloImpegnoModel.setNumCapitolo(cug.getNumeroCapitolo());
		}
		
		if(cug.getNumeroArticolo()!=null){ 
			capitoloImpegnoModel.setArticolo(cug.getNumeroArticolo());
		}
		
		if(cug.getNumeroUEB()!=null){ 
			capitoloImpegnoModel.setUeb(new BigInteger(String.valueOf(cug.getNumeroUEB())));
		}
		
		if(cug.getUid()!=0){ 
			capitoloImpegnoModel.setUid(cug.getUid());
		}
		
		if(cug.getTipoFinanziamento()!=null){ 
			capitoloImpegnoModel.setTipoFinanziamento(cug.getTipoFinanziamento().getDescrizione());
		}
		
		if (cug.getElementoPianoDeiConti() != null) {
			capitoloImpegnoModel.setCodicePdcFinanziario(cug.getElementoPianoDeiConti().getCodice());
			capitoloImpegnoModel.setDescrizionePdcFinanziario(cug.getElementoPianoDeiConti().getDescrizione());
			capitoloImpegnoModel.setIdPianoDeiConti(cug.getElementoPianoDeiConti().getUid());
		} 
		
		if (cug.getMacroaggregato() != null) {
			capitoloImpegnoModel.setIdMacroAggregato(cug.getMacroaggregato().getUid());
		}//non macroaggragato per capEntrata
		
		if (cug.getMissione() != null) {
			capitoloImpegnoModel.setCodiceMissione(cug.getMissione().getCodice());
			capitoloImpegnoModel.setDescrizioneMissione(cug.getMissione().getDescrizione());
		}
		
		if (cug.getProgramma() != null) {
			capitoloImpegnoModel.setCodiceProgramma(cug.getProgramma().getCodice());
			capitoloImpegnoModel.setDescrizioneProgramma(cug.getProgramma().getDescrizione());
			capitoloImpegnoModel.setIdProgramma(cug.getProgramma().getUid());
		} 
		
		if (cug.getDescrizione() != null) {
			capitoloImpegnoModel.setDescrizione(cug.getDescrizione());
		} 
	
		if (cug.getElementoPianoDeiConti() != null) {
			capitoloImpegnoModel.setElementoPianoDeiConti(cug.getElementoPianoDeiConti());
		} 
		
		if (cug.getTipoFinanziamento()!= null) {
			capitoloImpegnoModel.setFinanziamento(cug.getTipoFinanziamento());
		} 
		
		// siope
		if (cug.getSiopeSpesa() != null) {
			capitoloImpegnoModel.setCodiceSiopeSpesa(cug.getSiopeSpesa().getCodice());
			capitoloImpegnoModel.setDescrizioneSiopeSpesa(cug.getSiopeSpesa().getDescrizione());
		}
		
		if (cug.getStrutturaAmministrativoContabile()!= null && cug.getStrutturaAmministrativoContabile().getDescrizione() != null) {
			capitoloImpegnoModel.setCodiceStrutturaAmministrativa(cug.getStrutturaAmministrativoContabile().getCodice());
			capitoloImpegnoModel.setDescrizioneStrutturaAmministrativa(cug.getStrutturaAmministrativoContabile().getDescrizione());
			capitoloImpegnoModel.setUidStruttura(cug.getStrutturaAmministrativoContabile().getUid());
		} 
		
		if(!isEmpty(cug.getListaImportiCapitolo())){
			capitoloImpegnoModel.setImportiCapitoloUG(cug.getListaImportiCapitolo());
			ImportiCapitoloModel supportImporti;
			for (ImportiCapitoloUG currentImporto : cug.getListaImportiCapitolo()) {
				supportImporti = new ImportiCapitoloModel();
				supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
				supportImporti.setCassa(currentImporto.getStanziamentoCassa());
				supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
				supportImporti.setCompetenza(currentImporto.getStanziamento());
				capitoloImpegnoModel.getImportiCapitolo().add(supportImporti);
			}
		} 
		
		// COFOG
		if(cug.getClassificazioneCofog()!=null){
			capitoloImpegnoModel.setCodiceClassificazioneCofog(cug.getClassificazioneCofog().getCodice());
		}
		
		// transazione eurotpea
		
		if(cug.getTransazioneUnioneEuropeaSpesa()!=null){
			capitoloImpegnoModel.setCodiceTransazioneEuropeaSpesa(cug.getTransazioneUnioneEuropeaSpesa().getCodice());
		}
		
		
		// CUP
		capitoloImpegnoModel.setCup(model.getCupImpegnoSelezionato());
		
		// RICORRENTE
		if(cug.getRicorrenteSpesa()!=null){
			capitoloImpegnoModel.setCodiceRicorrenteSpesa(cug.getRicorrenteSpesa().getCodice());
		}
		
		// capitolo sanitario
		if(cug.getPerimetroSanitarioSpesa()!=null){
			capitoloImpegnoModel.setCodicePerimetroSanitarioSpesa(cug.getPerimetroSanitarioSpesa().getCodice());
		}
		
	
		// politiche regionali
		if(cug.getPoliticheRegionaliUnitarie()!=null){
			capitoloImpegnoModel.setCodicePoliticheRegionaliUnitarie(cug.getPoliticheRegionaliUnitarie().getCodice());
		}
		
		return capitoloImpegnoModel;
		
	}	
	
	public String ricaricaTEByIdLiquidazione(){
		
		debug("ricaricaTEByIdLiquidazione", "liquidazione selezionata "+model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione());
		
		Liquidazione liquidazioneScelta = null;
		
		// se NON ci sono delle quote devo ricaricate la TE
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()==0){
		
			if(StringUtils.isNotEmpty(model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione())){
			
				// recupero la liquidazione selezionta
				if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaLiquidazioni())){
					
					for (Liquidazione itLiq : model.getGestioneOrdinativoStep2Model().getListaLiquidazioni()) {
						
						if(itLiq.getIdLiquidazione() == (Integer.parseInt(model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione()))){
							// trovata quella scelta
							model.getGestioneOrdinativoStep2Model().setDescrizioneQuota(itLiq.getDescrizioneLiquidazione());
						    liquidazioneScelta = itLiq;
						    break;
							
						}
					}
					
				}
				
				if(null!=liquidazioneScelta){
					
					RicercaSinteticaCapitoloUscitaGestione r = new RicercaSinteticaCapitoloUscitaGestione();
					RicercaSinteticaCapitoloUGest ru = new RicercaSinteticaCapitoloUGest();
					 
					 ru.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
					 
					 if (liquidazioneScelta.getCapitoloUscitaGestione().getNumeroCapitolo() != null) {
						ru.setNumeroCapitolo(liquidazioneScelta.getCapitoloUscitaGestione().getNumeroCapitolo());
					 }
					 if (liquidazioneScelta.getCapitoloUscitaGestione().getNumeroArticolo() != null) {
						ru.setNumeroArticolo(liquidazioneScelta.getCapitoloUscitaGestione().getNumeroArticolo());
					 }
					 if (liquidazioneScelta.getCapitoloUscitaGestione().getNumeroUEB() != null) {
						ru.setNumeroUEB(liquidazioneScelta.getCapitoloUscitaGestione().getNumeroUEB());
					 }
					
					 r.setParametriPaginazione(new ParametriPaginazione());
					 r.setEnte(sessionHandler.getEnte());
					 r.setRichiedente(sessionHandler.getRichiedente());
					 r.setRicercaSinteticaCapitoloUGest(ru);
					 RicercaSinteticaCapitoloUscitaGestioneResponse respCapUsc = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(r);
						
					 if(null!=respCapUsc && !respCapUsc.isFallimento() && respCapUsc.getCapitoli()!=null){
						CapitoloUscitaGestione cug =  respCapUsc.getCapitoli().get(0);
						 
						CapitoloImpegnoModel cim = caricaDatiCapitolo(cug);
						 
						caricaMissione(cim);
						caricaProgramma(cim);
						caricaListaCofog(cim.getIdProgramma());
						
						// piano dei conti
						teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
						teSupport.getPianoDeiConti().setCodice(liquidazioneScelta.getCodPdc());
						teSupport.getPianoDeiConti().setDescrizione(liquidazioneScelta.getCodPdc()+" - "+liquidazioneScelta.getDescPdc());
						 
						RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
						rip.setEnte(sessionHandler.getEnte());
						rip.setRichiedente(sessionHandler.getRichiedente());
						RicercaImpegnoK k = new RicercaImpegnoK();
						k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
						k.setAnnoImpegno(liquidazioneScelta.getImpegno().getAnnoMovimento());
						k.setNumeroImpegno(liquidazioneScelta.getImpegno().getNumero());
						rip.setpRicercaImpegnoK(k);
						
						//MARZO 2016: ottimizzazioni
						rip.setCaricaSub(false);
						
						RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
					 	 
						if(respRk.getImpegno()!= null){
							 teSupport.setCup(respRk.getImpegno().getCup());
						}
						 
						teSupport.setCofogSelezionato(liquidazioneScelta.getCodCofog());
						teSupport.setTransazioneEuropeaSelezionato(liquidazioneScelta.getCodTransazioneEuropeaSpesa());
						teSupport.setRicorrenteSpesaSelezionato(liquidazioneScelta.getCodRicorrenteSpesa());
						// preimentro sanitario spesa
						teSupport.setPerimetroSanitarioSpesaSelezionato(liquidazioneScelta.getCodCapitoloSanitarioSpesa());
						 
						// politiche regionali
						teSupport.setPoliticaRegionaleSelezionato(liquidazioneScelta.getCodPrgPolReg());
						 
						//TE SUPPORT:
						codiceSiopeChangedInternal(liquidazioneScelta.getCodSiope());
						//
							 
					 }
				}	
			}
		}
		
		return "refreshTE";
	}
	
	
	/**
	 * RM 25/02/2016
	 * ho ripulito il metodo dalle chiamate inutili
	 * mi limito a ricaricare la TE
	 * 
	 * @return
	 */
	public String ricaricaTEByIdAccertamento(){
		
		debug("ricaricaTEByIdAccertamento", "accertamento selezionato: "+model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento());
		
		Accertamento accertamentoScelto = null;
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_INCASSO.toString());
		
		// se NON ci sono delle quote devo ricaricate la TE
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size()==0){
		
			if(StringUtils.isNotEmpty(model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento())){
			
				// recupero l'accertamento selezionto
				if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaAccertamento())){
					
					for (Accertamento itAcc : model.getGestioneOrdinativoStep2Model().getListaAccertamento()) {
						
						if(itAcc.getUid() == (Integer.parseInt(model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento()))){
							// trovata quella scelta
							accertamentoScelto = itAcc;
						    break;
							
						}
					}
				}
				
				if(null!=accertamentoScelto){
									 
					 // piano dei conti
					 if(accertamentoScelto.getCodPdc()!=null){
						 teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
						 teSupport.getPianoDeiConti().setCodice(accertamentoScelto.getCodPdc());
						 teSupport.getPianoDeiConti().setDescrizione(accertamentoScelto.getCodPdc()+" - "+accertamentoScelto.getDescPdc());
						 teSupport.getPianoDeiConti().setUid(accertamentoScelto.getIdPdc());
					 }
							
					 // siope
					 if(accertamentoScelto.getCodSiope()!=null){
						 teSupport.setSiopeSpesaCod(accertamentoScelto.getCodSiope());
						 codiceSiopeChangedInternal(accertamentoScelto.getCodSiope());
					 }
							 
					 teSupport.setTransazioneEuropeaSelezionato(accertamentoScelto.getCodTransazioneEuropeaSpesa());
					 teSupport.setRicorrenteEntrataSelezionato(accertamentoScelto.getCodRicorrenteSpesa());
					 teSupport.setPerimetroSanitarioEntrataSelezionato(accertamentoScelto.getCodCapitoloSanitarioSpesa());

				}	
			}
		}
		
		return "refreshTE";
	}
	

	protected void ricercaSoggettoPerChiaveOrdinativo() {
		RicercaSoggettoPerChiave rsc = new RicercaSoggettoPerChiave();
		rsc.setEnte(sessionHandler.getEnte());
		rsc.setRichiedente(sessionHandler.getRichiedente());
		ParametroRicercaSoggettoK k = new ParametroRicercaSoggettoK();
		
		k.setCodice(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
		
		rsc.setParametroSoggettoK(k);
		
		RicercaSoggettoPerChiaveResponse response =  soggettoService.ricercaSoggettoPerChiave(rsc);
		
		if(response!=null && !response.isFallimento()){
			
			if(!isEmpty(response.getListaSecondariaSoggetto())){
				List<SedeSecondariaSoggetto> listaSede = new ArrayList<SedeSecondariaSoggetto>();
				for(SedeSecondariaSoggetto sede : response.getListaSecondariaSoggetto()){
					if(sede.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(Constanti.STATO_VALIDO) ){
						listaSede.add(sede);
					}
				}
				model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(listaSede);
			}else  {
				model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(null);
			}
			
			if(!isEmpty(response.getListaModalitaPagamentoSoggetto())){
				
				List<ModalitaPagamentoSoggetto> listaDaFiltrare = response.getListaModalitaPagamentoSoggetto();
				List<ModalitaPagamentoSoggetto> listMod= FinUtility.filtraValidi(listaDaFiltrare);
				
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamento(listMod);
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(listMod);
			
			}else{
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamento(null);
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(null);
			}
			
		}
	}
	
	protected String remodpagamento() {
		
		if(idSedeCreditoreChecked){
		
			String cod = String.valueOf(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato());
			
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento();
			List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getGestioneOrdinativoStep1Model().getListaSediSecondarie();
			SedeSecondariaSoggetto sedeSelezionata = null;
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(Integer.parseInt(cod) == listaSediSecondarie.get(i).getUid()){
					sedeSelezionata = listaSediSecondarie.get(i);
					model.getGestioneOrdinativoStep1Model().setSedeSelezionata(sedeSelezionata);
					break;
				}
			}
			
			if(sedeSelezionata!=null){
				List<ModalitaPagamentoSoggetto> relistaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
				int first = 0;
				if(!isEmpty(listaModalitaPagamento)){
					for(int j=0; j<listaModalitaPagamento.size(); j++){				
						if(listaModalitaPagamento.get(j).getAssociatoA().equals(sedeSelezionata.getDenominazione())){
							//Inserisco le modalita' di pagamento solamente se sono in stato valida e se la data scadenza e' > della data attuale
							if(FinUtility.isValida(listaModalitaPagamento.get(j))){
								first = first + 1;
								relistaModalitaPagamento.add(listaModalitaPagamento.get(j));
							}
							if(first==1){	
								model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(listaModalitaPagamento.get(j).getUid());
								model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(listaModalitaPagamento.get(j));
							}
						}
					}
			    } 
				if(!isEmpty(relistaModalitaPagamento)){
					model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(relistaModalitaPagamento);
				}else{
					model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(null);
					model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(null);
					model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(0);
				}
			}
		} else {
			model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento());
			model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(null);
			model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(0);
			model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
		}
		
		return MODPAGAMENTO;
	}
	
	protected String resede() {
		String cod ="";
		if(0!=model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()){
			cod = String.valueOf(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato());
		}
		
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento =  model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento();
		ModalitaPagamentoSoggetto modpagamentoSelezionato = null; 
		for(int j=0; j<listaModalitaPagamento.size(); j++){
			if(Integer.parseInt(cod) == listaModalitaPagamento.get(j).getUid()){
				modpagamentoSelezionato = listaModalitaPagamento.get(j);
				model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(listaModalitaPagamento.get(j));
			}
		}
		List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getGestioneOrdinativoStep1Model().getListaSediSecondarie();
		if(listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()){
		boolean corrispondenzaSede = false;
		
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(modpagamentoSelezionato.getAssociatoA().equals(listaSediSecondarie.get(i).getDenominazione()) &&
						listaSediSecondarie.get(i).getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(Constanti.STATO_VALIDO)){
					model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(listaSediSecondarie.get(i).getUid());
					//PROVA
					model.getGestioneOrdinativoStep1Model().setSedeSelezionata(listaSediSecondarie.get(i));
					corrispondenzaSede = true;
					break;
				}
			}
		
			if(!corrispondenzaSede){
				model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
			}
		}
		return SEDISECONDARIE;
	}
	
	
	private OrdinativoPagamento copiaTE(OrdinativoPagamento op){
		
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
			op.setCodPdc(teSupport.getPianoDeiConti().getCodice());
			//CR-2023 si elimina CE 
			
			op.setCodCofog(teSupport.getCofogSelezionato());
			// transazione europea
			op.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
			
			
			// ricorrente spesa
			op.setCodRicorrenteSpesa(teSupport.getRicorrenteSpesaSelezionato());
			op.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioSpesaSelezionato());
			op.setCodPrgPolReg(teSupport.getPoliticaRegionaleSelezionato());
			
			// altri classificatori  I CLASSIFICATORI SONO SBAGLIATI 
			
			op.setCodClassGen11(teSupport.getClassGenSelezionato1());
			op.setCodClassGen12(teSupport.getClassGenSelezionato2());
			op.setCodClassGen13(teSupport.getClassGenSelezionato3());
			op.setCodClassGen14(teSupport.getClassGenSelezionato4());
			op.setCodClassGen15(teSupport.getClassGenSelezionato5());
			
			
			op.setCodSiope(teSupport.getSiopeSpesa().getCodice());
		}
		
		return op;
		
	}
		
	private OrdinativoIncasso copiaTEIncasso(OrdinativoIncasso op){
			
			if(sonoInAggiornamento()){
				setTeSupport(model.getTransazioneElementareOrdinativoCache());
			}
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
				op.setCodPdc(teSupport.getPianoDeiConti().getCodice());
				//CR-2023 si elimina CE
				
				// transazione europea
				op.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
				
				
				// ricorrente spesa
				op.setCodRicorrenteSpesa(teSupport.getRicorrenteEntrataSelezionato());
				op.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioEntrataSelezionato());
				if(teSupport.getSiopeSpesa()!= null){
					op.setCodSiope(teSupport.getSiopeSpesa().getCodice());
				}	
				// altri classificatori   I CLASSIFICATORI SONO SBAGLIATI 
				op.setCodClassGen16(teSupport.getClassGenSelezionato1());
				op.setCodClassGen17(teSupport.getClassGenSelezionato2());
				op.setCodClassGen18(teSupport.getClassGenSelezionato3());
				op.setCodClassGen19(teSupport.getClassGenSelezionato4());
				op.setCodClassGen20(teSupport.getClassGenSelezionato5());
				
			}
			
			return op;
			
		}
	
	
	private InserisceOrdinativoPagamento composizioneSalva(){
		InserisceOrdinativoPagamento request = new InserisceOrdinativoPagamento();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		
		
		
		request.setOrdinativoPagamento(model.getGestioneOrdinativoStep1Model().getOrdinativo());
		if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
			request.getOrdinativoPagamento().setCapitoloUscitaGestione(convertiCapitoloCustomToCapitolo(model.getGestioneOrdinativoStep1Model().getCapitolo()));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
			request.getOrdinativoPagamento().setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
		}
		if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {
			request.getOrdinativoPagamento().setSoggetto(convertiSoggettoCustomToSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto()));
			request.getOrdinativoPagamento().getSoggetto().setElencoModalitaPagamento(new ArrayList<ModalitaPagamentoSoggetto>());
			request.getOrdinativoPagamento().getSoggetto().getElencoModalitaPagamento().add(model.getGestioneOrdinativoStep1Model().getModpagamentoSelezionata());
		}
		request.getOrdinativoPagamento().setElencoSubOrdinativiDiPagamento(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti());
		//mock stato--------------------------------------------------------------------------------------------------
		request.getOrdinativoPagamento().setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		//------------------------------------------------------------------------------------------------------------
		
		//---------- SIOPE PLUS ----------
		impostaDatiSiopePlusPerInserisciAggiorna(model.getGestioneOrdinativoStep2Model(), request.getOrdinativoPagamento());
		//---------- FINE SIOPE PLUS ----------
		
		//CLASSIFICATORE STIPENDI
		impostaClassificatoreStipendi(model.getGestioneOrdinativoStep2Model(), request.getOrdinativoPagamento());
		
		// copio i dati di transazione ELEMENTARE
		request.setOrdinativoPagamento(copiaTE(request.getOrdinativoPagamento()));
		
		
		return request;
	}
	
	private void impostaClassificatoreStipendi(GestioneOrdinativoStep2Model gestioneOrdinativoStep2Model, Ordinativo ordinativo) {
		if(teSupport.getUidClassStipendiSelezionato() == 0) {
			ordinativo.setClassificatoreStipendi(null);
			return;
		}
		ClassificatoreStipendi cs = new ClassificatoreStipendi();
		cs.setUid(teSupport.getUidClassStipendiSelezionato());
		ordinativo.setClassificatoreStipendi(cs);
		
	}

	/**
	 * prende i dati in GestioneOrdinativoStep2Model e li travasa nell'oggetto OrdinativoPagamento che
	 * sta per essere usato in inserimento o aggiornamento service
	 * @param datiImpegno
	 * @param impegno
	 */
	protected void impostaDatiSiopePlusPerInserisciAggiorna(GestioneOrdinativoStep2Model modelDaCuiLeggere, OrdinativoPagamento ordPag){
		//---------- SIOPE PLUS ----------
		
		//CIG:
		ordPag.setCig(modelDaCuiLeggere.getCig());
		
		//SIOPE TIPO DEBITO:
		SiopeTipoDebito siopeTipoDebito = siopeTipoDebitoDaSceltaNelModel(modelDaCuiLeggere.getTipoDebitoSiope());
		ordPag.setSiopeTipoDebito(siopeTipoDebito);
		
		//SIOPE MOTIVAZIONE ASSENZA CIG:
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = siopeAssenzaMotivazioneDaSceltaNelModel(modelDaCuiLeggere.getMotivazioneAssenzaCig());
		ordPag.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		//---------- FINE SIOPE PLUS ----------
	}
	
	private InserisceOrdinativoIncasso composizioneSalvaIncasso(){
		InserisceOrdinativoIncasso request = new InserisceOrdinativoIncasso();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		request.setOrdinativoIncasso(travasaOrdinativo(model.getGestioneOrdinativoStep1Model().getOrdinativo()));
		if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
			request.getOrdinativoIncasso().setCapitoloEntrataGestione(convertiCapitoloCustomToCapitoloEntrata(model.getGestioneOrdinativoStep1Model().getCapitolo()));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
			request.getOrdinativoIncasso().setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
		}
		if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {
			request.getOrdinativoIncasso().setSoggetto(convertiSoggettoCustomToSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto()));
			request.getOrdinativoIncasso().getSoggetto().setSediSecondarie(new ArrayList<SedeSecondariaSoggetto>());
			if(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()!= 0){
				SedeSecondariaSoggetto sedeA= new SedeSecondariaSoggetto();
				for(SedeSecondariaSoggetto sede :model.getGestioneOrdinativoStep1Model().getListaSediSecondarie()){
					if(sede.getUid()== model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()){
						sedeA= sede;
					}
				}
				
				request.getOrdinativoIncasso().getSoggetto().getSediSecondarie().add(sedeA);
			}
		}
		//setto la lista dei subordinativi con relative modifiche di importo
		request.getOrdinativoIncasso().setElencoSubOrdinativiDiIncasso(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso());
		//mock stato--------------------------------------------------------------------------------------------------
		request.getOrdinativoIncasso().setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		//------------------------------------------------------------------------------------------------------------
		
		// copio i dati di transazione ELEMENTARE
		request.setOrdinativoIncasso(copiaTEIncasso(request.getOrdinativoIncasso()));
		
		impostaClassificatoreStipendi(model.getGestioneOrdinativoStep2Model(), request.getOrdinativoIncasso());
		
		//Causale:
		request.setOrdinativoIncasso(impostaCausaleEntrataDalModel(request.getOrdinativoIncasso()));
		
		
		return request;
	}
	
	private AggiornaOrdinativoIncasso composizioneAggiornaIncasso(){
		AggiornaOrdinativoIncasso request = new AggiornaOrdinativoIncasso();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		request.setOrdinativoIncasso(travasaOrdinativo(model.getGestioneOrdinativoStep1Model().getOrdinativo()));
		if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
			request.getOrdinativoIncasso().setCapitoloEntrataGestione(convertiCapitoloCustomToCapitoloEntrata(model.getGestioneOrdinativoStep1Model().getCapitolo()));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
			request.getOrdinativoIncasso().setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
		}
		if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {
			request.getOrdinativoIncasso().setSoggetto(convertiSoggettoCustomToSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto()));
			request.getOrdinativoIncasso().getSoggetto().setSediSecondarie(new ArrayList<SedeSecondariaSoggetto>());
			if(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()!= 0){
				SedeSecondariaSoggetto sedeA= new SedeSecondariaSoggetto();
				for(SedeSecondariaSoggetto sede :model.getGestioneOrdinativoStep1Model().getListaSediSecondarie()){
					if(sede.getUid()== model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()){
						sedeA= sede;
					}
				}
				request.getOrdinativoIncasso().getSoggetto().setSediSecondarie(new ArrayList<SedeSecondariaSoggetto>());
				request.getOrdinativoIncasso().getSoggetto().getSediSecondarie().add(sedeA);
			}
		}
		//setto la lista dei subordinativi con relative modifiche di importo
		request.getOrdinativoIncasso().setElencoSubOrdinativiDiIncasso(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso());
		//mock stato--------------------------------------------------------------------------------------------------
		//SIAC-7009
		if(request.getOrdinativoIncasso().getStatoOperativoOrdinativo() == null) {
			request.getOrdinativoIncasso().setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
		}
		
		//------------------------------------------------------------------------------------------------------------
		
		List<RegolarizzazioneProvvisorio> rp = new ArrayList<RegolarizzazioneProvvisorio>();
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				
				RegolarizzazioneProvvisorio rego = new RegolarizzazioneProvvisorio();
				rego.setImporto(pc.getImporto());
				rego.setProvvisorioDiCassa(pc);
				rego.setIdRegolarizzazioneProvvisorio(pc.getIdRegolarizzazione());
				
				// popolo la lista di regolarizzazione
				rp.add(rego);
				
			}
		}
		// setto il valore nella request
		request.getOrdinativoIncasso().setElencoRegolarizzazioneProvvisori(rp);
		
		
		// copio i dati di transazione ELEMENTARE
		request.setOrdinativoIncasso(copiaTEIncasso(request.getOrdinativoIncasso()));
		
		impostaClassificatoreStipendi(model.getGestioneOrdinativoStep2Model(), request.getOrdinativoIncasso());
		
		//Causale:
		request.setOrdinativoIncasso(impostaCausaleEntrataDalModel(request.getOrdinativoIncasso()));
		
		return request;
	}
	
	protected OrdinativoIncasso travasaOrdinativo(OrdinativoPagamento ordPag){
		OrdinativoIncasso ordInc = new OrdinativoIncasso();
		
		
		ordInc.setIdOrdinativo(ordPag.getIdOrdinativo());
		ordInc.setAnnoBilancio(ordPag.getAnnoBilancio());
		ordInc.setDataVariazione(ordPag.getDataVariazione());
		ordInc.setDataSpostamento(ordPag.getDataSpostamento());
		ordInc.setNote(ordPag.getNote());
		ordInc.setFlagAllegatoCartaceo(ordPag.isFlagAllegatoCartaceo());
		ordInc.setFlagBeneficiMultiplo(ordPag.isFlagBeneficiMultiplo());
		ordInc.setLoginCancellazione(ordPag.getLoginCancellazione());
		ordInc.setLoginModifica(ordPag.getLoginModifica());
		ordInc.setAnno(ordPag.getAnno());
		ordInc.setNumero(ordPag.getNumero());
		ordInc.setDescrizione(ordPag.getDescrizione());
		ordInc.setDataEmissione(ordPag.getDataEmissione());
		ordInc.setCastellettoCompentenza(ordPag.getCastellettoCompentenza());
		ordInc.setCastellettoEmessi(ordPag.getCastellettoEmessi());
		ordInc.setCastellettoCassa(ordPag.getCastellettoCassa());
		ordInc.setLoginCreazione(ordPag.getLoginCreazione());
		ordInc.setImportoOrdinativo(ordPag.getImportoOrdinativo());
		ordInc.setFlagCopertura(ordPag.isFlagCopertura());
		ordInc.setFlagDaTrasmettere(ordPag.getFlagDaTrasmettere());
		ordInc.setCompetenza(ordPag.getCompetenza());
		ordInc.setStatoOperativoOrdinativo(ordPag.getStatoOperativoOrdinativo());
		ordInc.setCodStatoOperativoOrdinativo(ordPag.getCodStatoOperativoOrdinativo());
		ordInc.setContoTesoreria(ordPag.getContoTesoreria());
		ordInc.setDistinta(ordPag.getDistinta());
		ordInc.setTipoAvviso(ordPag.getTipoAvviso());
		ordInc.setCodiceBollo(ordPag.getCodiceBollo());
		ordInc.setNoteTesoriere(ordPag.getNoteTesoriere());
		ordInc.setTipoAssociazioneEmissione(ordPag.getTipoAssociazioneEmissione());
		ordInc.setSubOrdinativo(ordPag.getSubOrdinativo());
		ordInc.setDatiOrdinativoTrasmesso(ordPag.getDatiOrdinativoTrasmesso());
		ordInc.setDataModifica(ordPag.getDataModifica());
		ordInc.setDataInizioValidita(ordPag.getDataInizioValidita());
		ordInc.setElencoRegolarizzazioneProvvisori(new ArrayList<RegolarizzazioneProvvisorio>());
		ordInc.setElencoRegolarizzazioneProvvisori(ordPag.getElencoRegolarizzazioneProvvisori());
		
		//DICEMBRE 2017, aggiunti gli ordinativi collegati anche per l'incasso:
		ordInc.setElencoOrdinativiCollegati(ordPag.getElencoOrdinativiCollegati());
		//
		
		return ordInc;
	}
	
	protected OrdinativoPagamento travasaOrdinativoIncToPag(OrdinativoIncasso ordInc){
		OrdinativoPagamento ordPag = new OrdinativoPagamento();
		
		
		ordPag.setIdOrdinativo(ordInc.getIdOrdinativo());
		ordPag.setAnnoBilancio(ordInc.getAnnoBilancio());
		ordPag.setDataVariazione(ordInc.getDataVariazione());
		ordPag.setDataSpostamento(ordInc.getDataSpostamento());
		ordPag.setNote(ordInc.getNote());
		ordPag.setFlagAllegatoCartaceo(ordInc.isFlagAllegatoCartaceo());
		ordPag.setFlagBeneficiMultiplo(ordInc.isFlagBeneficiMultiplo());
		ordPag.setLoginCancellazione(ordInc.getLoginCancellazione());
		ordPag.setLoginModifica(ordInc.getLoginModifica());
		ordPag.setAnno(ordInc.getAnno());
		ordPag.setNumero(ordInc.getNumero());
		ordPag.setDescrizione(ordInc.getDescrizione());
		ordPag.setDataEmissione(ordInc.getDataEmissione());
		ordPag.setCastellettoCompentenza(ordInc.getCastellettoCompentenza());
		ordPag.setCastellettoEmessi(ordInc.getCastellettoEmessi());
		ordPag.setCastellettoCassa(ordInc.getCastellettoCassa());
		ordPag.setLoginCreazione(ordInc.getLoginCreazione());
		ordPag.setImportoOrdinativo(ordInc.getImportoOrdinativo());
		ordPag.setFlagCopertura(ordInc.isFlagCopertura());
		ordPag.setCompetenza(ordInc.getCompetenza());
		ordPag.setStatoOperativoOrdinativo(ordInc.getStatoOperativoOrdinativo());
		ordPag.setCodStatoOperativoOrdinativo(ordInc.getCodStatoOperativoOrdinativo());
		ordPag.setContoTesoreria(ordInc.getContoTesoreria());
		ordPag.setDistinta(ordInc.getDistinta());
		ordPag.setTipoAvviso(ordInc.getTipoAvviso());
		ordPag.setCodiceBollo(ordInc.getCodiceBollo());
		ordPag.setNoteTesoriere(ordInc.getNoteTesoriere());
		ordPag.setTipoAssociazioneEmissione(ordInc.getTipoAssociazioneEmissione());
		ordPag.setSubOrdinativo(ordInc.getSubOrdinativo());
		ordPag.setDatiOrdinativoTrasmesso(ordInc.getDatiOrdinativoTrasmesso());
		ordPag.setDataModifica(ordInc.getDataModifica());
		ordPag.setDataInizioValidita(ordInc.getDataInizioValidita());
		ordPag.setElencoRegolarizzazioneProvvisori(new ArrayList<RegolarizzazioneProvvisorio>());
		ordPag.setElencoRegolarizzazioneProvvisori(ordInc.getElencoRegolarizzazioneProvvisori());
		
		//DICEMBRE 2017 - AGGIUNTI ORDINATIVI COLLEGATI DA INCASSO:
		ordPag.setElencoOrdinativiCollegati(ordInc.getElencoOrdinativiCollegati());
		//
		
		return ordPag;
		
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.length(null));
		System.out.println(StringUtils.length(""));
		System.out.println(StringUtils.length("Ciao"));
	}
	
	/**
	 * Salva ordinativo incasso
	 * @return
	 */
	protected String salvaProvvisoriOrdinativoIncasso(){
		
		if(!oggettoDaPopolarePagamento()) {

			boolean aCopertura = model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura();
			// SIAC-6188: ripristinata la scrittura della causale del provvisorio
			//Riporta descrizione dei provvisori in testata per ordinativo di incasso a copertura:
			Errore erroreDescrizione = riportaCausaleProvvisoriInTestata();
			if(erroreDescrizione!=null){
				addErrore(erroreDescrizione);
				return INPUT;
			}
			
			if (aCopertura) {
				// SIAC-6122:  in caso di ordinativo a copertura, senza descrizione, con provvisorio di cassa collegato,
				// impostare come descrizione dell'ordinativo la causale del provvisorio di cassa.
				String descrizioneStep1 = model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione();
				if(StringUtils.length(descrizioneStep1) < 3 && model.getGestioneOrdinativoStep3Model().getListaCoperture() != null && !model.getGestioneOrdinativoStep3Model().getListaCoperture().isEmpty()) {
					for(Iterator<ProvvisorioDiCassa> it = model.getGestioneOrdinativoStep3Model().getListaCoperture().iterator(); it.hasNext() && StringUtils.length(descrizioneStep1) < 3;) {
						ProvvisorioDiCassa provvisorioDiCassa = it.next();
						descrizioneStep1 = provvisorioDiCassa.getCausale();
					}
					if(StringUtils.length(descrizioneStep1) >= 3) {
						model.getGestioneOrdinativoStep1Model().getOrdinativo().setDescrizione(descrizioneStep1);
					}
				}
			}
		}
		
		debug("salvaProvvisoriOrdinativoIncasso", "salvataggio dati");
		
		// se totale da collegare diverso da Zero allora errore
		if(null!=model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare() && model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare().compareTo(BigDecimal.ZERO)!=0){
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizza(false);	
			addErrore(ErroreFin.TOTALE_PROVVISORI_COLLEGATI_NON_COINCIDE_CON_IMPORTO_ORDINATIVO_SUB.getErrore(""));
			return INPUT;
		}
		
		
		InserisceOrdinativoIncasso request = composizioneSalvaIncasso();
			
		List<RegolarizzazioneProvvisorio> rp = new ArrayList<RegolarizzazioneProvvisorio>();
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				
				RegolarizzazioneProvvisorio rego = new RegolarizzazioneProvvisorio();
				rego.setImporto(pc.getImporto());
				rego.setProvvisorioDiCassa(pc);
				
				// popolo la lista di regolarizzazione
				rp.add(rego);
				
			}
		}
		// setto il valore nella request
		request.getOrdinativoIncasso().setElencoRegolarizzazioneProvvisori(rp);
	
		InserisceOrdinativoIncassoResponse response= ordinativoService.inserisceOrdinativoIncasso(request);
		if (response.isFallimento()) {
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizza(false);	
			addErrori(response.getErrori());
			return INPUT;
		} 
		
		
		// vai sulla pagina di consulta

		setNumeroOrdinativoStruts(String.valueOf(response.getOrdinativoIncassoInserito().getNumero()));
		setAnnoOrdinativoStruts(String.valueOf(response.getOrdinativoIncassoInserito().getAnno()));
		
		// messaggio di ok nella pagina consulta
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		// pulisco i dati di sessione
		clearActionData();
				
		return GOTO_CONSULTA;
	}
	
	
	/**
	 * Salva ordinativo pagamento
	 * @return
	 */
	protected String salvaProvvisori(){
		
		debug("salvaProvvisori", "salvataggio dati");
		
		if(model.getGestioneOrdinativoStep3Model().getListaCoperture()==null || model.getGestioneOrdinativoStep3Model().getListaCoperture().size()==0){
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizza(false);	
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("copertura"));
			return INPUT;
		}
		
		
		if(null!=model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare() && model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare().compareTo(BigDecimal.ZERO)!=0){
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizza(false);	
			addErrore(ErroreFin.TOTALE_PROVVISORI_COLLEGATI_NON_COINCIDE_CON_IMPORTO_ORDINATIVO_SUB.getErrore(""));
			return INPUT;
		}
		
		
		InserisceOrdinativoPagamento request = composizioneSalva();
		
		// inserisco le coperture
		
		List<RegolarizzazioneProvvisorio> rp = new ArrayList<RegolarizzazioneProvvisorio>();
		
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				
				RegolarizzazioneProvvisorio rego = new RegolarizzazioneProvvisorio();
				rego.setImporto(pc.getImporto());
				rego.setProvvisorioDiCassa(pc);
				
				// popolo la lista di regolarizzazione
				rp.add(rego);
				
			}
		}
		// setto il valore nella request
		request.getOrdinativoPagamento().setElencoRegolarizzazioneProvvisori(rp);
	
		
		
		InserisceOrdinativoPagamentoResponse response = ordinativoService.inserisceOrdinativoPagamento(request);
		if (response!=null && response.isFallimento()) {
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizza(false);	
			addErrori(response.getErrori());
			return INPUT;
		} 
		
		// vai sulla pagina di consulta

		setNumeroOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoInserito().getNumero()));
		setAnnoOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoInserito().getAnno()));
		
		// messaggio di ok nella pagina consulta
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		// pulisco i dati di sessione
		clearActionData();
				
		return GOTO_CONSULTA;

		
	}
	
	/**
	 * evento di inserimento del provvisiorio
	 * @return
	 * @throws Exception
	 */
	public String inserisciProvvisorio() throws Exception{
		debug("inserisciProvvisorio", "inserisco il provv nella lista");
		
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}

		if(model.getGestioneOrdinativoStep3Model().getProvvisorio().getAnno()==null && model.getGestioneOrdinativoStep3Model().getProvvisorio().getNumero()==null){
			addActionError(SELEZIONARE_PROVV);
			if(OggettoDaPopolareEnum.ORDINATIVO_INCASSO.equals(oggettoDaPopolare)){
				//12 luglio 2017 - cercando di ridurre il codice duplicato ho notato che i metodi 
				// inserisciProvvisorio su GestioneOrdinativoIncassoStep3Action e su GestioneOrdinativoIncassoStep3Action
				//erano praticamente identici se non per il fatto che in incasso aveva questi tre set aggiuntivi:
				setAncoraVisualizzaInserisciProvErrori(true);
				setAncoraVisualizzaInserisciProv(false);
				setAncoraVisualizza(false);
				//li avvolgo dentro questa if in modo che resti tutto equivalente a prima.
			}
			return INPUT;
		}
		
		if(controlloGestioneProvvisorio(OPERAZIONE_INSERIMENTO).equalsIgnoreCase(INPUT)){
			
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizza(false);
			return INPUT;
		}
		
		setAncoraVisualizzaInserisciProv(true);
		setAncoraVisualizzaInserisciProvErrori(false);
		setAncoraVisualizza(false);
		setUidProvvDaRicerca("");
		return SUCCESS;
	}
	
	public String salva() {
		
		//pulisco i flag per i pop up di conferma per evitare interferenze:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		//
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		
		// CONTROLLO TE:
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(!isEmpty(erroriAbilitazione)){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		//

		
		if(oggettoDaPopolarePagamento()){
			//INSERIMENTO ORDINATIVO PAGAMENTO
			InserisceOrdinativoPagamento request = composizioneSalva();
			
			InserisceOrdinativoPagamentoResponse response = ordinativoService.inserisceOrdinativoPagamento(request);
			if (response.isFallimento()) {
				addErrori(response.getErrori());
				return INPUT;
			} 
			
			// vai sulla pagina di consulta
	
			setNumeroOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoInserito().getNumero()));
			setAnnoOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoInserito().getAnno()));
		}else{
			//INSERIMENTO ORDINATIVO INCASSO
			
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso() == null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size() == 0){
				addErrore(ErroreFin.ORDINATIVO_MANCANTE_QUOTE.getErrore("inserimento","ordinativo di incasso"));
				return INPUT;
			}
			
			//Controllo ordinativi inseriti in caso di flag per reintroito checked:
			Errore erroreReintroto = controlliPresenzaOrditiviPerReintroito();
			if(erroreReintroto!=null){
				addErrore(erroreReintroto);
				return INPUT;
			}
			//

			
			InserisceOrdinativoIncasso request = composizioneSalvaIncasso();
			
			
			InserisceOrdinativoIncassoResponse response= ordinativoService.inserisceOrdinativoIncasso(request);
			if (response.isFallimento()) {
				addErrori(response.getErrori());
				return INPUT;
			} 
			
			// vai sulla pagina di consulta
			setNumeroOrdinativoStruts(String.valueOf(response.getOrdinativoIncassoInserito().getNumero()));
			setAnnoOrdinativoStruts(String.valueOf(response.getOrdinativoIncassoInserito().getAnno()));

		}
		
		// messaggio di ok nella pagina consulta
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		// pulisco i dati di sessione
		clearActionData();
		
		return GOTO_CONSULTA;
	}
	
	protected Errore controlliPresenzaOrditiviPerReintroito(){
		if(!sonoInAggiornamentoIncasso()){
			//sono in inserimento
			if(model.getGestioneOrdinativoStep1Model().isFlagPerReintroiti()
					&& isEmpty(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati())){
				return ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ordinativi collegati per reintroito");
			}
		} else {
			//sono in aggiornamento, do errore se per caso li ha eliminati tutti:
			if(model.getGestioneOrdinativoStep1Model().isFlagPerReintroitiOrdinativoInAggiornamento()
					&& isEmpty(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati())){
				return ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ordinativi collegati per reintroito");
			}
		}
		return null;
	}
	
	/**
	 * Compone la descrizione finale dell'ordinativo e ne valuta la correttezza
	 * Ritorna null se tutto ok, altrimenti l'errore riscontrato
	 * @return
	 */
	private Errore riportaCausaleProvvisoriInTestata(){
		Errore errore = null;
		if(!oggettoDaPopolarePagamento()){
			//Riporta descrizione dei provvisori in testata per ordinativo di incasso a copertura:
			
			//Richiamo il metodo "core" che compone la descrizione che risulterebbe tra 
			//quella nello step1 e quella da riportare:
			String descrizioneOrdinativo = componiDescrizioneRisultante();
			
			if(FinStringUtils.isEmpty(descrizioneOrdinativo)){
				errore = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Ordinativo");
			} else if(FinStringUtils.length(descrizioneOrdinativo)<3){
				errore = ErroreFin.CAMPO_LUNGHEZZA_MINIMA_NON_RISPETTATA.getErrore("DESCRIZIONE","tre");
			}
			
			if(errore==null){
				//OK NESSUN ERRORE POSSO SETTARE NEL MODEL LA DESCRIZIONE RISULTANTE:
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setDescrizione(descrizioneOrdinativo);
			}
			
		}
		return errore;
	}
	
	/**
	 * Si occupa di costruire la descrizione finale dell'ordinativo,
	 * si basa sulla descrizione (opzionale) dello step1 concatenandola
	 * con le descrizioni delle coperture per le quali e' stato indicato il flag
	 * riporta in testata
	 * @return
	 */
	private String componiDescrizioneRisultante(){
		String descrizioneOrdinativo = null;
		if(!oggettoDaPopolarePagamento()){
			
			String descrizioneStep1 = model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione();
			
			//Riporta descrizione dei provvisori in testata per ordinativo di incasso a copertura:
			
			List<ProvvisorioDiCassa> listaCoperture = model.getGestioneOrdinativoStep3Model().getListaCoperture();
			StringBuilder sbDescrizioneDaRiportare = new StringBuilder();
			
			if(!FinStringUtils.isEmpty(listaCoperture)){
				//concateno le descrizioni delle quote che vanno riportate:
				boolean first = true;
				for(ProvvisorioDiCassa provvIt : listaCoperture){
					if(Boolean.TRUE.equals(provvIt.getRiportaInTestata())){
						if(!first){
							sbDescrizioneDaRiportare.append(" - ");
						}
						sbDescrizioneDaRiportare.append(provvIt.getCausale().toUpperCase());
						first = false;
					}
				}
				//
			}
			String descrizioneDaRiportare = sbDescrizioneDaRiportare.toString();//da aggiungere a quella dello step1
			
			if(!FinStringUtils.isEmpty(descrizioneStep1) && !FinStringUtils.isEmpty(descrizioneDaRiportare)){
				descrizioneOrdinativo = descrizioneStep1 + " - " + descrizioneDaRiportare;
			} else if(FinStringUtils.isEmpty(descrizioneStep1) && !FinStringUtils.isEmpty(descrizioneDaRiportare)){
				descrizioneOrdinativo = descrizioneDaRiportare;
			} else if(!FinStringUtils.isEmpty(descrizioneStep1) && FinStringUtils.isEmpty(descrizioneDaRiportare)){
				descrizioneOrdinativo = descrizioneStep1;
			}
			
			//va troncata alla massima lunghezza sul db:
			if(FinStringUtils.length(descrizioneOrdinativo)>500){
				descrizioneOrdinativo = descrizioneDaRiportare.substring(0, 500);
			}
			
		}
		return descrizioneOrdinativo;
	}
	
	
	protected CapitoloUscitaGestione convertiCapitoloCustomToCapitolo(CapitoloImpegnoModel capitoloDaConvertire) {
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setAnnoCapitolo(capitoloDaConvertire.getAnno());
		capitolo.setNumeroCapitolo(capitoloDaConvertire.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloDaConvertire.getArticolo());
		capitolo.setNumeroUEB(capitoloDaConvertire.getUeb().intValue());
		capitolo.setUid(capitoloDaConvertire.getUid());
		capitolo.setListaImportiCapitolo(capitoloDaConvertire.getImportiCapitoloUG());
		return capitolo;
	}
	
	protected CapitoloEntrataGestione convertiCapitoloCustomToCapitoloEntrata(CapitoloImpegnoModel capitoloDaConvertire) {
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		capitolo.setAnnoCapitolo(capitoloDaConvertire.getAnno());
		capitolo.setNumeroCapitolo(capitoloDaConvertire.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloDaConvertire.getArticolo());
		capitolo.setNumeroUEB(capitoloDaConvertire.getUeb().intValue());
		capitolo.setUid(capitoloDaConvertire.getUid());
		capitolo.setListaImportiCapitolo(capitoloDaConvertire.getImportiCapitoloEG());
		return capitolo;
	}
	
	
	protected Soggetto convertiSoggettoCustomToSoggetto(SoggettoImpegnoModel soggettoDaConvertire) {
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(soggettoDaConvertire.getCodCreditore());
		return soggetto;
	}
	
	public String proseguiStep() throws Exception {
		return "prosegui";
	}
	
	
	public boolean attivaVideataProvCassa(){
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
			return true;
		}
		return false;
	}
	

	/**
	 * 	elimina la quota riconteggiando la numerazione
	 *  e la sommatoria delle quote
	 * @param daAggiornaQuota
	 * @return
	 */
	protected String eliminaQuotaOrdinativo(boolean daAggiornaQuota){
		debug("eliminaQuotaOrdinativo", " Elimina il "+getNumeroQuotaEliminato());
		if(oggettoDaPopolarePagamento()){
			if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti())){
				for (SubOrdinativoPagamento subOrdinativoPagamento : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()) {
					
					if(sonoUgualiInt(subOrdinativoPagamento.getNumero(), getNumeroQuotaEliminato())){
						if(!daAggiornaQuota){
							//Il chiamante e' l'elimina
							//BISOGNA INCREMENTARE NUOVAMENTE IL DISPONIBILE SULLA LIQUIDAZIONE:
							BigDecimal importo = subOrdinativoPagamento.getImportoAttuale();
							Integer idLiquidazione = subOrdinativoPagamento.getLiquidazione().getIdLiquidazione();
							variaDisponibilitaLiquidazione(idLiquidazione.toString(), importo, false);
							////////////////////////////////////////////////////////////////////////
						}
						
						//SI RIMUOVE LA QUOTA:
						model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().remove(subOrdinativoPagamento);
						//////////////////////////////////////////////////////////////////////
						
						break;
					}
				}
				if(!daAggiornaQuota){
					rinumerazioneQuote(Integer.valueOf(getNumeroQuotaEliminato()));
				}
				
				List<SubOrdinativoPagamento> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
				
				if (elencoQuote.isEmpty()){
					
					model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento("");
					
					
					//quote rimosse tutte, rimuovo anche il siope plus se in inserimento
					
					if(!sonoInAggiornamento() && !daAggiornaQuota){
						pulisciDatiSiopePlusNelModel(model.getGestioneOrdinativoStep2Model());
					}
					
				}
			}
		}else{
			if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso())){
				for (SubOrdinativoIncasso subOrdinativoIncasso : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()) {
					
					if(sonoUgualiInt(subOrdinativoIncasso.getNumero(), getNumeroQuotaEliminato())){
						if(!daAggiornaQuota){
							//Il chiamante e' l'elimina
							//BISOGNA INCREMENTARE NUOVAMENTE IL DISPONIBILE SULLA LIQUIDAZIONE:
							BigDecimal importo = subOrdinativoIncasso.getImportoAttuale();
							Integer idAccertamento = subOrdinativoIncasso.getAccertamento().getUid();
							variaDisponibilitaIncassoAcc(idAccertamento.toString(), importo, false);
							////////////////////////////////////////////////////////////////////////
						}
						
						//SI RIMUOVE LA QUOTA:
						model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().remove(subOrdinativoIncasso);
						//////////////////////////////////////////////////////////////////////
						break;
					}
				}
				if(!daAggiornaQuota){
					rinumerazioneQuote(Integer.valueOf(getNumeroQuotaEliminato()));
				}
				
				List<SubOrdinativoIncasso> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
				
				if (elencoQuote.isEmpty()){
					
					model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento("");
					
				}
		}
		}
		return SUCCESS;
	}
	
	/*
	 *  funzione che esegue la rinumerazione delle quote degli ordinativi
	 *  in caso di eliminazione e in caso di aggiornamento e alla fine aggiorna la sommatoria
	 */
	private void rinumerazioneQuote(Integer numeroQuotaVariata){
		if(oggettoDaPopolarePagamento()){
			// ordinativo pagamento
			
			List<SubOrdinativoPagamento> subordsPag = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
			if(!isEmpty(subordsPag)){
				debug("rinumerazioneQuote", " quota da variare "+numeroQuotaVariata);
				for (SubOrdinativoPagamento subOrdinativoPagamento : subordsPag){
					if(subOrdinativoPagamento.getNumero()>numeroQuotaVariata){
						subOrdinativoPagamento.setNumero(subOrdinativoPagamento.getNumero()-1);
					}
				}
			}
			sommatoriaQuoteSubOrdPagamento();
			
		}else{
			// ordinativo incasso
			
			List<SubOrdinativoIncasso> subordsInc = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
			if(!isEmpty(subordsInc)){
				debug("rinumerazioneQuote", " quota da variare "+numeroQuotaVariata);
				for (SubOrdinativoIncasso subOrdinativoIncasso : subordsInc) {
					if(subOrdinativoIncasso.getNumero()>numeroQuotaVariata){
						subOrdinativoIncasso.setNumero(subOrdinativoIncasso.getNumero()-1);
					}
				}
			}
			sommatoriaQuoteSubOrdIncasso();
			
		}
	}
	
	public String forzaInserisciQuotaAccertamento() throws Exception{
		setQuotaIncassoImpOver(true);
		String ritorno= inserisciQuotaAccertamento();
		sommatoriaQuoteSubOrdIncasso();
		return ritorno;
		
	}
	
	
	public String forzaAggiornaQuotaAccertamento() throws Exception{
		setQuotaIncassoImpOver(true);
		String risultato= aggiornaQuotaOrdinativo();
		sommatoriaQuoteSubOrdIncassoPerAggiorna();
		return risultato;
		
	}
	
	
	protected String inserisciQuotaAccertamento() throws Exception {
		

		model.getGestioneOrdinativoStep2Model().setCheckProseguiQuoteIncasso(false);
		List<Errore> listaErrori= new ArrayList<Errore>();
		//LEGGO I DATI LETTI DA MASCHERA WEB:
		String idAccertamentoSelezionato = model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento();
		String importoQuotaInserito = model.getGestioneOrdinativoStep2Model().getImportoQuotaFormattato();
		String descrizioneInserita = model.getGestioneOrdinativoStep2Model().getDescrizioneQuota();
		
		// setto l'oggetto
		Accertamento accertamentoTrovato = null;
		
		if(StringUtils.isEmpty(idAccertamentoSelezionato)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("accertamento"));
			
		}else{

			
			if(isAccertamentoGiaInserito(idAccertamentoSelezionato)){
				listaErrori.add(ErroreFin.ACCERTAMENTO_GIA_PRESENTE.getErrore());
			}

		}
						
		
		if(StringUtils.isEmpty(importoQuotaInserito)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota"));
		}	
		
		if(StringUtils.isEmpty(descrizioneInserita)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione quota"));
		}
		
		
		if(StringUtils.isNotEmpty(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento())){
			
			if(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento().length()!=10){
				
				listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
			}else {
				Date dataEsecuzionePagamento = DateUtility.parse(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento());
				
				if(null==dataEsecuzionePagamento){
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
					
												
				}else{ 
					// la data esecuzione deve essere superiore della data odierna
					if(!DateUtility.checkDateMinoreUguale(DateUtility.getCurrentDate(), model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento())){
						
						listaErrori.add(ErroreFin.DATA_SUCCESSIVA.getErrore("Data scadenza", "alla data corrente"));
					}
					
					
					// Controllo dell'anno, l'anno della dt di esecuzione pagamento non deve essere superiore a quello di esercizio 
					Integer inputAnnoEsecuzionePagamento = DateUtility.getAnno(dataEsecuzionePagamento);
					Integer annoBilancio = Integer.parseInt(sessionHandler.getAnnoEsercizio());
					
					if(inputAnnoEsecuzionePagamento.compareTo(annoBilancio) > 0 ){
						
						listaErrori.add(it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_NON_VALIDA.getErrore(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()));
						
					}
					
				}
			}
		
		}
		
		// estraggo accertamento tramite il radio button
		accertamentoTrovato = estraeAccertamento(idAccertamentoSelezionato);
		
		checkPianoDeiContiDataEsecuzioneIncasso(listaErrori, accertamentoTrovato);
		
		if(!isEmpty(listaErrori)){
			// con errore tengo aperta la finestra quota
			model.setToggleLiquidazioneAperto(true);
			addErrori(listaErrori);
			return INPUT;	
		}
		
		
		if(accertamentoTrovato.getDisponibilitaIncassare().compareTo(convertiImportoToBigDecimal(importoQuotaInserito))<0 && !isQuotaIncassoImpOver()){
			addActionWarning(ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore().getCodice()+":"+ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore("l'inserimento").getDescrizione());
			
		}else{
			//In questo caso devo inserisce uma modifica di importo su acc o subacc
			
		}
		
		if(getActionWarnings().size()==1){	
		       Iterator<String> it = getActionWarnings().iterator();
		       while(it.hasNext()){
		    	   String cicla = it.next();
		    	   if(cicla.equalsIgnoreCase(ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore().getCodice()+":"+ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore("l'inserimento").getDescrizione())){
		    		   model.getGestioneOrdinativoStep2Model().setCheckProseguiQuoteIncasso(true);
		    		   return INPUT;
		    	   }
		    	   
		       }
	       }
		
		model.getGestioneOrdinativoStep2Model().setImportoQuota(convertiImportoToBigDecimal(importoQuotaInserito));
		
		SubOrdinativoIncasso suboOrdinativoIncasso = new SubOrdinativoIncasso();
		// descrizione
		suboOrdinativoIncasso.setDescrizione(descrizioneInserita);
		suboOrdinativoIncasso.setImportoAttuale(convertiImportoToBigDecimal(importoQuotaInserito));
		
		// data esecuzione pagamento
		if(null!=model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()){
			
			suboOrdinativoIncasso.setDataScadenza(DateUtility.parse(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()));
		}
		
		BigDecimal tmp= BigDecimal.ZERO;
		
		// accertamento
		if(accertamentoTrovato instanceof SubAccertamento){
			// subaccertamento
			suboOrdinativoIncasso.setAccertamento(((SubAccertamento)accertamentoTrovato));
			if(isQuotaIncassoImpOver()){
				ModificaMovimentoGestioneEntrata mmge= new ModificaMovimentoGestioneEntrata();
				//ma cha cavolo devo mettere?!
				mmge.setTipoMovimento(Constanti.MODIFICA_TIPO_SAC);
				mmge.setImportoOld(accertamentoTrovato.getImportoAttuale());
				tmp= convertiImportoToBigDecimal(importoQuotaInserito).subtract(accertamentoTrovato.getDisponibilitaIncassare());
				mmge.setImportoNew(tmp);
				
				mmge.setDescrizioneModificaMovimentoGestione(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
				mmge.setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
				mmge.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
				
				if(suboOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata()== null){
					suboOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
				}
				
				//Setto a new la lista delle modifiche onde evitare che ci siano piu' valori al servizio
				suboOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
				
				suboOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(mmge);
			}
		}else{
			// accertamento
			suboOrdinativoIncasso.setAccertamento(((Accertamento)accertamentoTrovato));
			if(isQuotaIncassoImpOver()){
				
				ModificaMovimentoGestioneEntrata mmge= new ModificaMovimentoGestioneEntrata();
				//ma cha cavolo devo mettere?!
				mmge.setTipoMovimento(Constanti.MODIFICA_TIPO_ACC);
				mmge.setImportoOld(accertamentoTrovato.getImportoAttuale());

				tmp= convertiImportoToBigDecimal(importoQuotaInserito).subtract(accertamentoTrovato.getDisponibilitaIncassare());
				mmge.setImportoNew(tmp);
				
				mmge.setDescrizioneModificaMovimentoGestione(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
				mmge.setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
				mmge.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
				
				if(suboOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata()== null){
					suboOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
				}
				
				//Setto a new la lista delle modifiche onde evitare che ci siano piu' valori al servizio
				suboOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
				
				suboOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(mmge);
				
			}
			
		}
		
		
		// numerazione
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()==null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size()==0){
			
			// setto la prima quota a con ID = 1
			suboOrdinativoIncasso.setNumero(1);
		}else if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size()>=0){
			
			// setto il numero come il size + 1
			suboOrdinativoIncasso.setNumero(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size()+1);
			
		}
		model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().add(suboOrdinativoIncasso);
		
		// chiudo la finestra quota
		model.setToggleLiquidazioneAperto(false);
		
		
		// pulisco quota
		pulisciQuotaAccertamento();
		
		variaDisponibilitaIncassoAcc(idAccertamentoSelezionato, convertiImportoToBigDecimal(importoQuotaInserito), true); 
		
		List<SubOrdinativoIncasso> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
		
		if(!isEmpty(elencoQuote)){
			
			model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento(convertDateToString(elencoQuote.get(0).getDataScadenza()));
			
		}
		
		return SUCCESS;
	}
	
	protected String inserisciQuota() throws Exception {
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		// setto l'oggetto
		Liquidazione liquidazioneTrovata = null;
		
		//LEGGO I DATI LETTI DA MASCHERA WEB:
		String idLiquidazioneSelezionato = model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione();
		String importoQuotaInserito = model.getGestioneOrdinativoStep2Model().getImportoQuotaFormattato();
		String descrizioneInserita = model.getGestioneOrdinativoStep2Model().getDescrizioneQuota();
		
		if(StringUtils.isEmpty(idLiquidazioneSelezionato)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("liquidazione"));
		}else{
			
			// estraggo la liquidazione tramite il radio butto
			liquidazioneTrovata = estraeLiquidazione(model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione());
			if(isLiquidazioneGiaInserita(idLiquidazioneSelezionato)){
				listaErrori.add(ErroreFin.LIQUIDAZIONE_GIA_PRESENTE.getErrore());
			}
			
			//il siope plus deve essere uguale a quello delle quote gia' immesse:
			if(isSiopePlusDiverso(liquidazioneTrovata)){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Siope+", " (La liquidazione ha dati SIOPE + diversi da quelli delle altre quote)"));
			}
		}
		
		
		
		if(StringUtils.isEmpty(descrizioneInserita)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione quota"));
		}
		
		if(StringUtils.isEmpty(importoQuotaInserito)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota"));
		}else{
			
			if(StringUtils.isNotEmpty(model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione()) 
					&& convertiImportoToBigDecimal(importoQuotaInserito).compareTo(liquidazioneTrovata.getDisponibilitaPagare())>0){
				// se selezionata la liquidazione da elenco devo andare
				// a verificare le disponibilita a pagare
				listaErrori.add(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
			}
			
			
			// Dalla sommatoria totale tolgo l'importo iniziale della quota
		    BigDecimal sommatoria = BigDecimal.ZERO;
		    
		    if(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote()!=null){
		    	sommatoria = model.getGestioneOrdinativoStep2Model().getSommatoriaQuote();
		    }
		
		    //aggiungo alla sommatoria il nuovo importo da me scelto
		    sommatoria= sommatoria.add(convertiImportoToBigDecimal(importoQuotaInserito));
		  
		    Map<Integer, BigDecimal> mappaCassa= ritornaMappaAnniDisponibilitaCassa();
		    BigDecimal bd = mappaCassa.get(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		    
		    //controllo con la disponibilita' di cassa del capitolo
		    if(sommatoria.compareTo(bd)>0){
		    	listaErrori.add(ErroreFin.DISPONIBILITA_DI_CASSA_INSUFFICIENTE.getErrore(""));
		    }
			
		}
		
		if(StringUtils.isNotEmpty(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento())){
			
			if(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento().length()!=10){
				listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
			}else {
				Date dataEsecuzionePagamento = DateUtility.parse(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento());
				
				if(null==dataEsecuzionePagamento){
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
				}else{ 
					// la data esecuzione deve essere superiore della data odierna
					if(DateUtility.checkDateMinoreUguale(DateUtility.formatDate(dataEsecuzionePagamento),DateUtility.formatDate(new Date(System.currentTimeMillis())))){
						listaErrori.add(ErroreFin.CAMPO_DATA_NON_ACCETTABILE.getErrore("Data esecuzione pagamento"));
					}
					
					// Controllo dell'anno, l'anno della dt di esecuzione pagamento non deve essere superiore a quello di esercizio 
					Integer inputAnnoEsecuzionePagamento = DateUtility.getAnno(dataEsecuzionePagamento);
					Integer annoBilancio = Integer.parseInt(sessionHandler.getAnnoEsercizio());
					
					if(inputAnnoEsecuzionePagamento.compareTo(annoBilancio) > 0 ){
						listaErrori.add(it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_NON_VALIDA.getErrore(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()));
					}
				}
			}
		}
		
		checkPianoDeiContiDataEsecuzionePagamento(listaErrori, liquidazioneTrovata);
		
		if(!isEmpty(listaErrori)){
			// con errore tengo aperta la finestra quota
			model.setToggleLiquidazioneAperto(true);
			addErrori(listaErrori);
			return INPUT;	
		}
		
		model.getGestioneOrdinativoStep2Model().setImportoQuota(convertiImportoToBigDecimal(importoQuotaInserito));
		
		SubOrdinativoPagamento suboOrdinativoPagamento = new SubOrdinativoPagamento();
		suboOrdinativoPagamento.setImportoIniziale(convertiImportoToBigDecimal(importoQuotaInserito));
		suboOrdinativoPagamento.setImportoAttuale(convertiImportoToBigDecimal(importoQuotaInserito));
		suboOrdinativoPagamento.setDescrizione(descrizioneInserita);
		
		if(null!=model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()){
			
			suboOrdinativoPagamento.setDataEsecuzionePagamento(DateUtility.parse(model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento()));
		}
		
		//impostiamo i dati siope plus della quota inserita (che sono stati accettati solo se uguali a quelli delle altre quote gia' presenti):
		impostaDatiSiopePlusNelModel(liquidazioneTrovata, model.getGestioneOrdinativoStep2Model());
		//
		
		suboOrdinativoPagamento.setLiquidazione(liquidazioneTrovata);
		
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()==null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()==0){
			
			// setto la prima quota a con ID = 1
			suboOrdinativoPagamento.setNumero(1);
		}else if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()>=0){
			
			// setto il numero come il size + 1
		    suboOrdinativoPagamento.setNumero(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()+1);
			
		}
		
		model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().add(suboOrdinativoPagamento);
		
		// chiudo la finestra quota
		model.setToggleLiquidazioneAperto(false);
		
		// pulisco i campi
		pulisciQuota();
		
		//devo sottrarre l'importo digitato alla disponibilita sulla liquidazione selezionata:
		variaDisponibilitaLiquidazione(idLiquidazioneSelezionato, convertiImportoToBigDecimal(importoQuotaInserito), true);
		
		
		List<SubOrdinativoPagamento> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
		
		if(!isEmpty(elencoQuote)){
			
			model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento(convertDateToString(elencoQuote.get(0).getDataEsecuzionePagamento()));
			
		}
		
		return SUCCESS;
	}

	private void checkPianoDeiContiDataEsecuzionePagamento(List<Errore> listaErrori,	Liquidazione liquidazioneTrovata) {
				
				List<SubOrdinativoPagamento> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
				
				String dataEsecuzioneDaInserire = model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento();
				
				String pdcDiRiferimento = null;
				String dataEsecuzioneDiRiferimento = null;
				
				if(!elencoQuote.isEmpty()){
					
					 pdcDiRiferimento = elencoQuote.get(0).getLiquidazione().getCodPdc();
					 
					 dataEsecuzioneDiRiferimento = convertDateToString(elencoQuote.get(0).getDataEsecuzionePagamento());
					 
					 if(liquidazioneTrovata != null){
					
						if(!pdcDiRiferimento.equals(liquidazioneTrovata.getCodPdc())){
							
							listaErrori.add(ErroreFin.PDC_INCOMPATIBILE.getErrore(""));
							
						}
					 }
						
					 if(!dataEsecuzioneDiRiferimento.equals(dataEsecuzioneDaInserire)){
							
						listaErrori.add(ErroreFin.DATA_NON_OMOGENEA.getErrore(""));
							
					 }
					 
				}									

	}
	
	private void checkPianoDeiContiDataEsecuzioneIncasso(List<Errore> listaErrori,	Accertamento accertamentoTrovato) {

		List<SubOrdinativoIncasso> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();

		String dataEsecuzioneDaInserire = model.getGestioneOrdinativoStep2Model().getDataEsecuzionePagamento();

		String pdcDiRiferimento = null;
		String dataEsecuzioneDiRiferimento = null;

		if(!elencoQuote.isEmpty()){

			pdcDiRiferimento = elencoQuote.get(0).getAccertamento().getCodPdc();

			dataEsecuzioneDiRiferimento = convertDateToString(elencoQuote.get(0).getDataScadenza());

			if(accertamentoTrovato != null){

				if(!pdcDiRiferimento.equals(accertamentoTrovato.getCodPdc())){

					listaErrori.add(ErroreFin.PDC_INCOMPATIBILE.getErrore(""));

				}
			}

			if(!dataEsecuzioneDiRiferimento.equals(dataEsecuzioneDaInserire)){

				listaErrori.add(ErroreFin.DATA_NON_OMOGENEA.getErrore(""));

			}

		}									

}
	
	private SubOrdinativoPagamento estraiSubOrdinativoPagamentoByLiquidazione(String idLiquidazioneDaCercare){
		SubOrdinativoPagamento trovato = null;
		if(model != null && model.getGestioneOrdinativoStep2Model()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null){
			List<SubOrdinativoPagamento> listaQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
			if(!isEmpty(listaQuote)){
				for(SubOrdinativoPagamento it : listaQuote){
					if(it!=null && it.getLiquidazione()!=null && it.getLiquidazione().getIdLiquidazione()!=null){
						if(it.getLiquidazione().getIdLiquidazione().intValue() == Integer.valueOf(idLiquidazioneDaCercare).intValue()){
							trovato = clone(it);
							break;
						}
					}
				}
			}
		}
		return trovato;
	}

	/**
	 * sottrare/aggiunge l'importo indicato all'accertamento in lista, restituisce il risultato
	 * @param idAccDaCercare
	 * @param importo
	 * @param sottrai
	 * @return
	 */
	private BigDecimal variaDisponibilitaIncassoAcc(String idAccDaCercare,BigDecimal importo,boolean sottrai) {
		Accertamento accertamentoTrovato = getAccertamentoDaLista(idAccDaCercare);
		Accertamento accertamentoTrovatoOriginale = getAccertamentoDaListaOriginale(idAccDaCercare);
		BigDecimal dispAggiornata = null;
		if(accertamentoTrovato!=null){
			BigDecimal dispAPagare = accertamentoTrovato.getDisponibilitaIncassare();
			BigDecimal deltaImporto =importo.subtract(dispAPagare);
			
			BigDecimal importoDaSovrascrivere= accertamentoTrovato.getImportoAttuale().add(deltaImporto);
			
			BigDecimal importoAcc =accertamentoTrovato.getImportoAttuale();
			
			BigDecimal importoAttualeAccOriginale=  (accertamentoTrovatoOriginale!=null && accertamentoTrovatoOriginale.getImportoAttuale()!=null) ? accertamentoTrovatoOriginale.getImportoAttuale() : BigDecimal.ZERO;
			
			BigDecimal scarto= importoAcc.subtract(importoAttualeAccOriginale);
			
			if(sottrai){
				//Se si supera la disponibilita' ad incassare dell'accertamento,la si porta a 0
				if(!isQuotaIncassoImpOver()){
					dispAggiornata = dispAPagare.subtract(importo);
				}else{
					dispAggiornata= BigDecimal.ZERO;
					accertamentoTrovato.setImportoAttuale(importoDaSovrascrivere);
				}
			} else {
				dispAggiornata = dispAPagare.add(importo.subtract(scarto));
				accertamentoTrovato.setImportoAttuale(importoAcc.subtract(scarto));
			}
			accertamentoTrovato.setDisponibilitaIncassare(dispAggiornata);
		}
		return dispAggiornata;
	}
	
	/**
	 * sottrare/aggiunge l'importo indicato alla liquidazione in lista, restituisce il risultato
	 * @param idLiquidazioneDaCercare
	 * @param importo
	 * @param sottrai
	 * @return
	 */
	private BigDecimal variaDisponibilitaLiquidazione(String idLiquidazioneDaCercare,BigDecimal importo,boolean sottrai) {
		Liquidazione liquidazioneTrovato = getLiquidazioneDaLista(idLiquidazioneDaCercare);
		BigDecimal dispAggiornata = null;
		if(liquidazioneTrovato!=null){
			BigDecimal dispAPagare = liquidazioneTrovato.getDisponibilitaPagare();
			if(sottrai){
				dispAggiornata = dispAPagare.subtract(importo);
			} else {
				dispAggiornata = dispAPagare.add(importo);
			}
			liquidazioneTrovato.setDisponibilitaPagare(dispAggiornata);
		}
		return dispAggiornata;
	}
	
	/**
	 * ritorna una copia della liquidazione dalla lista con l'identificativo indicato
	 * @param idLiquidazioneDaCercare
	 * @return
	 */
	private Liquidazione estraeLiquidazione(String idLiquidazioneDaCercare) {
		Liquidazione liquidazioneTrovata = null;
		Liquidazione daLista = getLiquidazioneDaLista(idLiquidazioneDaCercare);
		if(daLista!=null){
			liquidazioneTrovata = clone(daLista);
		}
		return liquidazioneTrovata;
	}
	
	
	/**
	 * ritorna una copia del accertamento dalla lista con l'identificativo indicato
	 * @param idAccertamentoDaCercare
	 * @return
	 */
	private Accertamento estraeAccertamento(String idAccertamentoDaCercare) {
		Accertamento accertamentoTrovato = null;
		Accertamento daLista = getAccertamentoDaLista(idAccertamentoDaCercare);
		if(daLista!=null){
			accertamentoTrovato = clone(daLista);
		}
		return accertamentoTrovato;
	}
	
	/**
	 * accede al accertamento nella lista con l'identificativo indicato
	 * @param idAccertamentoDaCercare
	 * @return
	 */
	private Accertamento getAccertamentoDaLista(String idAccertamentoDaCercare) {
		return FinUtility.getById(model.getGestioneOrdinativoStep2Model().getListaAccertamento(), idAccertamentoDaCercare);
	}
	
	private Accertamento getAccertamentoDaListaOriginale(String idAccertamentoDaCercare) {
		return FinUtility.getById(model.getGestioneOrdinativoStep2Model().getListaAccertamentoOriginale(), idAccertamentoDaCercare);
	}
	
	/**
	 * accede alla liquidazione nella lista con l'identificativo indicato
	 * @param idLiquidazioneDaCercare
	 * @return
	 */
	private Liquidazione getLiquidazioneDaLista(String idLiquidazioneDaCercare) {
		Iterator<Liquidazione> itLiquidazione =  model.getGestioneOrdinativoStep2Model().getListaLiquidazioni().iterator();
		Liquidazione liquidazioneTrovata = null;
		while(itLiquidazione.hasNext()){
			Liquidazione liquidazione = itLiquidazione.next();
			if(liquidazione.getIdLiquidazione().intValue() == Integer.valueOf(idLiquidazioneDaCercare).intValue()){
				liquidazioneTrovata = liquidazione;
				break;
				
			}
		}
		return liquidazioneTrovata;
	}
	
	private boolean isLiquidazioneGiaInserita(String idLiquidazioneDaCercare){
		List<SubOrdinativoPagamento> listaSubOrd = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
		boolean presente = false;
		if(!isEmpty(listaSubOrd)){
			for(SubOrdinativoPagamento subit : listaSubOrd){
				if(subit!=null){
					Liquidazione liqit = subit.getLiquidazione();
					if(liqit!=null && sonoUgualiInt(liqit.getIdLiquidazione(), idLiquidazioneDaCercare)){
						presente = true;
						break;
					}
				}
			}
		}
		return presente;
	}
	
	/**
	 * verifica che il siope plus sia lo stesso di quello delle eventuali quote gia
	 * immesse
	 * @param idLiquidazioneDaCercare
	 * @param nuovaLiquidazione
	 * @return
	 */
	private boolean isSiopePlusDiverso(Liquidazione nuovaLiquidazione){
		
		boolean siopeDiverso = false;
		
		List<SubOrdinativoPagamento> listaSubOrd = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
		
		if(!isEmpty(listaSubOrd)){
			
			SubOrdinativoPagamento primoElemento = FinUtility.getFirst(listaSubOrd);
			Liquidazione liqConfronto = primoElemento.getLiquidazione();
			
			String cigConfronto = liqConfronto.getCig();
			String codiceAssenzaConfronto = codiceMotivazioneAssenzaCig(liqConfronto.getSiopeAssenzaMotivazione());
			String codiceDebitoConfronto = codiceTipoDebito(liqConfronto.getSiopeTipoDebito());
			
			if(!FinStringUtils.sonoUguali(cigConfronto, nuovaLiquidazione.getCig())){
				return true;
			}
			
			String codiceAssenza = codiceMotivazioneAssenzaCig(nuovaLiquidazione.getSiopeAssenzaMotivazione()); 
			
			
			if(!FinStringUtils.sonoUguali(codiceAssenza, codiceAssenzaConfronto)){
				return true;
			}
			
			String codiceDebito = codiceTipoDebito(nuovaLiquidazione.getSiopeTipoDebito());
			
			if(!FinStringUtils.sonoUguali(codiceDebito, codiceDebitoConfronto)){
				return true;
			}
			
		}
		return siopeDiverso;
	}
	
	private boolean isAccertamentoGiaInserito(String idLiAccertamentoDaCercare){
		List<SubOrdinativoIncasso> listaSubOrd = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
		boolean presente = false;
		for(SubOrdinativoIncasso subit : listaSubOrd){
			if(subit!=null){
				if(subit.getAccertamento() instanceof SubAccertamento){
					//SUBACCERTAMENTO
					if(subit.getAccertamento().getUid()== Integer.parseInt(idLiAccertamentoDaCercare)){
						presente= true;
						break;
					}
				} else if(subit.getAccertamento() instanceof Accertamento){
					//ACCERTAMENTO
					if(subit.getAccertamento().getUid()== Integer.parseInt(idLiAccertamentoDaCercare)){
						presente= true;
						break;
					}
							
					
				}
			}
		}
		return presente;
	}
	
	
	//METODI LISTA PROVVISORI:
	
	/**
	 * accede al provvisorio nella lista con l'identificativo indicato
	 * @param idProvvisorioDaCercare
	 * @return
	 */
	protected ProvvisorioDiCassa getProvvisorioDaLista(String idProvvisorioDaCercare) {
		ProvvisorioDiCassa finded = null;
		List<ProvvisorioDiCassa> list =  model.getGestioneOrdinativoStep3Model().getListaProvvisori();
		if(!isEmpty(list)){
			for(ProvvisorioDiCassa obj: list){
				if(sonoUgualiInt(obj.getIdProvvisorioDiCassa(), idProvvisorioDaCercare)){
					finded = obj;
					break;
				}
			}
		}
		return finded;
	}
	
	/**
	 * ritorna una copia del provvisorio dalla lista con l'identificativo indicato
	 * @param idProvvisorioDaCercare
	 * @return
	 */
	protected ProvvisorioDiCassa estraiProvvisorioDaLista(String idProvvisorioDaCercare) {
		ProvvisorioDiCassa finded = null;
		ProvvisorioDiCassa daLista = getProvvisorioDaLista(idProvvisorioDaCercare);
		if(daLista!=null){
			finded = clone(daLista);
		}
		return finded;
	}
	
	/**
	 * sottrare/aggiunge l'importo indicato alla liquidazione in lista, restituisce il risultato
	 * @param idProvvisorioDaCercare
	 * @param importo
	 * @param sottrai
	 * @return
	 */
	protected BigDecimal variaImportoDaRegolarizzare(String idProvvisorioDaCercare,BigDecimal importo,boolean sottrai) {
		ProvvisorioDiCassa provvisorioTrovato = null;
		if(sonoInAggiornamentoIncasso()){
			if(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getUid()!= 0){
				provvisorioTrovato = model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio();
			}else{
				provvisorioTrovato =  getProvvisorioDaLista(idProvvisorioDaCercare);
			}
			
		}else{
			provvisorioTrovato =  getProvvisorioDaLista(idProvvisorioDaCercare);
		}
		 
		
		BigDecimal valoreAggiornato = null;
		if(provvisorioTrovato!=null){
			BigDecimal dispAPagare = provvisorioTrovato.getImportoDaRegolarizzare();
			if(sottrai){
				valoreAggiornato = dispAPagare.subtract(importo);
			} else {
				valoreAggiornato = dispAPagare.add(importo);
			}
			provvisorioTrovato.setImportoDaRegolarizzare(valoreAggiornato);
		}
		return valoreAggiornato;
	}
	
	/**
	 * estrea una copia della copertura voluta dalla lista coperture inserite momentaneamente nella action
	 * @param idProvvisorioDaCercare
	 * @return
	 */
	protected ProvvisorioDiCassa estraiCoperturaInserita(String idProvvisorioDaCercare){
		ProvvisorioDiCassa trovato = null;
		if(model != null && model.getGestioneOrdinativoStep3Model()!=null && model.getGestioneOrdinativoStep3Model().getListaCoperture()!=null){
			List<ProvvisorioDiCassa> listaCoperture = model.getGestioneOrdinativoStep3Model().getListaCoperture();
			if(!isEmpty(listaCoperture)){
				for(ProvvisorioDiCassa it : listaCoperture){
					if(it!=null && it.getIdProvvisorioDiCassa()!=null){
						if(it.getIdProvvisorioDiCassa().intValue() == Integer.valueOf(idProvvisorioDaCercare).intValue()){
							trovato = clone(it);
							break;
						}
					}
				}
			}
		}
		return trovato;
	}
	
	/**
	 * Da utilizzare quando vengono ricaricati i provvisori dal back-end e si deve re impostare l'importo da regolarizzare
	 * in funzione del fatto che possono gia' essere presenti coperture inserite in sessione
	 */
	protected void reimpostaImportoDaRegolarizzare(){
		Iterator<ProvvisorioDiCassa> itLiquidazione =  model.getGestioneOrdinativoStep3Model().getListaProvvisori().iterator();
		while(itLiquidazione.hasNext()){
			ProvvisorioDiCassa provvisorioDiCassa = itLiquidazione.next();
			if(provvisorioDiCassa.getIdProvvisorioDiCassa()!=null){
				ProvvisorioDiCassa quotaGiaInserita = estraiCoperturaInserita(provvisorioDiCassa.getIdProvvisorioDiCassa().toString());
				if(quotaGiaInserita!=null){
					BigDecimal importoQuota = quotaGiaInserita.getImporto();
					variaImportoDaRegolarizzare(provvisorioDiCassa.getIdProvvisorioDiCassa().toString(), importoQuota, true);
				}
			}
		}
	}
	
	private void pulisciQuota(){
		
		model.getGestioneOrdinativoStep2Model().setDescrizioneQuota("");
		model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato("");
		model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento("");
		model.getGestioneOrdinativoStep2Model().setRadioIdLiquidazione("");
		
	}
	
	private void pulisciQuotaAccertamento(){
		
		model.getGestioneOrdinativoStep2Model().setDescrizioneQuota("");
		model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato("");
		model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento("");
		model.getGestioneOrdinativoStep2Model().setRadioIdAccertamento("");
		
	}
	
	protected String dettaglioAggiornaQuota(){
		debug("dettaglioAggiornaQuota", " sono qui:  "+getNumeroPerDettaglio());
		
		if(oggettoDaPopolarePagamento()){
		
			boolean editaDataEsecuzionePagamento = false;
			
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null){
				
				int count = 0;
				for (SubOrdinativoPagamento subOrdinativoPagamento : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()) {
					
					
					if(subOrdinativoPagamento.getNumero() == Integer.valueOf(getNumeroPerDettaglio())){
						
						if(count == 0){
							editaDataEsecuzionePagamento = true;
						}
						
						// setto i valori
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDescrizione(subOrdinativoPagamento.getDescrizione());
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setImportoQuotaFormattato(convertiBigDecimalToImporto(subOrdinativoPagamento.getImportoAttuale()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDataEsecuzionePagamento(convertDateToString(subOrdinativoPagamento.getDataEsecuzionePagamento()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setNumeroQuota(String.valueOf(subOrdinativoPagamento.getNumero()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setuIdQuota(String.valueOf(subOrdinativoPagamento.getUid()));
						model.getGestioneOrdinativoStep2Model().setImportoQuotaPrimaUpdate(clone(subOrdinativoPagamento.getImportoAttuale()));
						
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setEditaDataEsecuzionePagamento(editaDataEsecuzionePagamento);
						
						break;
					}
					
					count++;
				}
				
			}
		}else{
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!=null){
				
				for (SubOrdinativoIncasso subOrdinativoIncasso : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()) {
					
					if(sonoUgualiInt(subOrdinativoIncasso.getNumero(), getNumeroPerDettaglio())){
						// setto i valori
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDescrizione(subOrdinativoIncasso.getDescrizione());
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setImportoQuotaFormattato(convertiBigDecimalToImporto(subOrdinativoIncasso.getImportoAttuale()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDataScadenzaSubOrd(convertDateToString(subOrdinativoIncasso.getDataScadenza()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setNumeroQuota(String.valueOf(subOrdinativoIncasso.getNumero()));
						model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setuIdQuota(String.valueOf(subOrdinativoIncasso.getUid()));
						model.getGestioneOrdinativoStep2Model().setImportoQuotaPrimaUpdate(clone(subOrdinativoIncasso.getImportoAttuale()));
						break;
					}
					
				}
				
			}
			
		}
	
		return DETTAGLIO_AGGIORNA_QUOTA;	
	}
	
	protected String dettaglioConsultaQuota(){
		for (SubOrdinativoIncasso subOrdinativoIncasso : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()) {
			
			if(sonoUgualiInt(subOrdinativoIncasso.getNumero(), getNumeroPerDettaglio())){
				// setto i valori
				model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDescrizione(subOrdinativoIncasso.getDescrizione());
				model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setImportoQuotaFormattato(convertiBigDecimalToImporto(subOrdinativoIncasso.getImportoAttuale()));
				model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setDataScadenzaSubOrd(convertDateToString(subOrdinativoIncasso.getDataScadenza()));
				model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setNumeroQuota(String.valueOf(subOrdinativoIncasso.getNumero()));
				model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setuIdQuota(String.valueOf(subOrdinativoIncasso.getUid()));
				if(subOrdinativoIncasso.getAccertamento() instanceof SubAccertamento){
					((SubAccertamento) subOrdinativoIncasso.getAccertamento()).getAnnoAccertamentoPadre();
					model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setAccertamento(String.valueOf(((SubAccertamento) subOrdinativoIncasso.getAccertamento()).getAnnoAccertamentoPadre() +"/"+((SubAccertamento) subOrdinativoIncasso.getAccertamento()).getNumeroAccertamentoPadre()+"-"+((SubAccertamento) subOrdinativoIncasso.getAccertamento()).getNumero()));

				}else{
					model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().setAccertamento(String.valueOf(subOrdinativoIncasso.getAccertamento().getAnnoMovimento()) +"/"+subOrdinativoIncasso.getAccertamento().getNumero().toString());


				}
				model.getGestioneOrdinativoStep2Model().setImportoQuotaPrimaUpdate(clone(subOrdinativoIncasso.getImportoAttuale()));
				break;
			}
			
		}
		
		return DETTAGLIO_CONSULTA_QUOTA;
	}

	protected String dettaglioAggiornaCoperture(){
		
		
		 debug("dettaglioAggiornaCoperture", "numero:  "+getNumeroPerDettaglioCopertura());
		 if(model.getGestioneOrdinativoStep3Model().getListaCoperture()!=null){
			 
			 for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				 if(pc.getIdProvvisorioDiCassa().compareTo(new BigInteger(getNumeroPerDettaglioCopertura()))==0){
					 
					boolean settaImportoUpdate = false;
					if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
						 
						 if(sonoInAggiornamento()){
							 settaImportoUpdate = true;
						 }
						 
					 }else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
					 	if(sonoInAggiornamentoIncasso()){
					 		 settaImportoUpdate = true;
					 	}
					 	
					 	// gestione riportaInTestataPageAggiornaCopertura
					 	model.getGestioneOrdinativoStep3Model().setRiportaInTestataPageAggiornaProvvisorio(pc.getRiportaInTestata());
					 	
					 }
					
					 if(settaImportoUpdate){
						BigDecimal bdRego = cercaImportoRegolarizzareDaProvvisorio(pc);
						 pc.setImportoDaRegolarizzare(bdRego);
						 pc.setImportoDaRegolarizzareFormatString(convertiBigDecimalToImporto(bdRego));
					 }
					 
					 // serve a ripulire il campo per digitazioni precedenti 
					 pc.setImportoFormatString(pc.getImporto().toString());

					 model.getGestioneOrdinativoStep3Model().setVisualizzaProvvisorio(pc);
					 model.getGestioneOrdinativoStep3Model().setImportoCoperturaPrimaUpdate(clone(pc.getImporto()));
					
				 }
				
			}
			 
		 }

		 return DETTAGLIO_AGGIORNA_COPERTURE;
	}
	
	private BigDecimal cercaImportoRegolarizzareDaProvvisorio(ProvvisorioDiCassa pc){
		BigDecimal importoFinale = BigDecimal.ZERO;
		RicercaProvvisoriDiCassa rpc = new RicercaProvvisoriDiCassa();
	
		rpc.setEnte(sessionHandler.getEnte());
		rpc.setNumPagina(0);
		rpc.setBilancio(sessionHandler.getBilancio());
		rpc.setRichiedente(sessionHandler.getRichiedente());
		ParametroRicercaProvvisorio prp = new ParametroRicercaProvvisorio();
		prp.setNumeroDa(pc.getNumero()); // numero da
		prp.setNumeroA(pc.getNumero());   // numero a
		prp.setAnnoDa(pc.getAnno()); // anno Da
		prp.setAnnoA(pc.getAnno()); 	// anno A
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
			prp.setTipoProvvisorio(TipoProvvisorioDiCassa.S);
		}else{
			prp.setTipoProvvisorio(TipoProvvisorioDiCassa.E);
		}
	
		// 
		prp.setFlagAnnullato(Constanti.FALSE);
		rpc.setParametroRicercaProvvisorio(prp);
		RicercaProvvisoriDiCassaResponse response =  provvisorioService.ricercaProvvisoriDiCassa(rpc);
		if(isFallimento(response)){
			addErrori(response.getErrori());
			return importoFinale;
		}
		
		// valorizzo l'importo da regolarizzare
		if(!isEmpty(response.getElencoProvvisoriDiCassa())){
			// setto il valore e prendo il primo e unico elemento della lista
			// visto che la ricerca dara' sempre risultato con size = 1
			importoFinale = response.getElencoProvvisoriDiCassa().get(0).getImportoDaRegolarizzare();
		}
		
		return importoFinale;
	}
	
	protected String aggiornaQuotaOrdinativo() throws Exception{
		List<Errore> listaErrori= new ArrayList<Errore>();
		String numeroQuota = "";
		String descrizioneQuota ="";
		String importoQuotaFormattato = "";
		String dataEsecuzionePagamentoString = "";
		String dataScadenzaSubOrdString = "";
		String uIdQuota = "";
		BigDecimal nuovoImportoQuota = null;
		BigDecimal importoPrimaDiModifica =  model.getGestioneOrdinativoStep2Model().getImportoQuotaPrimaUpdate();
		//setto a false la variabile per la popup di conferma
		model.getGestioneOrdinativoStep2Model().setCheckAggiornaQuoteIncasso(false);
		if(oggettoDaPopolarePagamento()){
			//LEGGO I DATI RELATIVI ALLA QUOTA IN MODIFICA:
			 numeroQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getNumeroQuota();
			 descrizioneQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDescrizione();
			 importoQuotaFormattato = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getImportoQuotaFormattato();
			 dataEsecuzionePagamentoString = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDataEsecuzionePagamento();
			 dataScadenzaSubOrdString = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDataScadenzaSubOrd();
			 uIdQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getuIdQuota();
			////////////
		}else if(isQuotaIncassoImpOver()){
			 numeroQuota = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getNumeroQuota();
			 descrizioneQuota = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getDescrizione();
			 importoQuotaFormattato = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getImportoQuotaFormattato();
			 dataEsecuzionePagamentoString = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getDataEsecuzionePagamento();
			 dataScadenzaSubOrdString = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getDataScadenzaSubOrd();
			 uIdQuota = model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().getuIdQuota();
		}else{
			 numeroQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getNumeroQuota();
			 descrizioneQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDescrizione();
			 importoQuotaFormattato = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getImportoQuotaFormattato();
			 dataEsecuzionePagamentoString = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDataEsecuzionePagamento();
			 dataScadenzaSubOrdString = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getDataScadenzaSubOrd();
			 uIdQuota = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getuIdQuota();
		}
		
		if(FinStringUtils.isEmpty(descrizioneQuota)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione quota"));
		}
		
		if(FinStringUtils.isEmpty(importoQuotaFormattato)){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota"));
		} else{

			//Convertiamo in bigdecimal l'importo:
			 nuovoImportoQuota = convertiImportoToBigDecimal(importoQuotaFormattato);

			if(nuovoImportoQuota.compareTo(BigDecimal.ZERO)<=0){
				listaErrori.add(ErroreFin.IMPORTO_ERRATO_SUBIMPEGNO.getErrore(""));
			}

		}
		
		if(!isEmpty(listaErrori)){
			addErrori(listaErrori);
			return INPUT;	
		}
		
		if(oggettoDaPopolarePagamento()){
			
			if(StringUtils.isNotEmpty(dataEsecuzionePagamentoString)){
				
				if(dataEsecuzionePagamentoString.length()!=10){
					
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
					
				}else {
				
					Date dataEsecuzionePagamento = DateUtility.parse(dataEsecuzionePagamentoString);
					
					if(null==dataEsecuzionePagamento){
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data esecuzione pagamento","dd/mm/yyyy"));
					} else {
						
						Timestamp now=  new Timestamp(System.currentTimeMillis());
						
						if(DateUtility.convertiDataInTimeStamp(dataEsecuzionePagamento).before(now)){
							listaErrori.add(ErroreFin.CAMPO_DATA_NON_ACCETTABILE.getErrore("Data esecuzione pagamento"));
						}
							
						
						// Controllo dell'anno, l'anno della dt di esecuzione pagamento non deve essere superiore a quello di esercizio 
						Integer inputAnnoEsecuzionePagamento = DateUtility.getAnno(dataEsecuzionePagamento);
						Integer annoBilancio = Integer.parseInt(sessionHandler.getAnnoEsercizio());
						
						if(inputAnnoEsecuzionePagamento.compareTo(annoBilancio) > 0 ){
							
							listaErrori.add(it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_NON_VALIDA.getErrore(dataEsecuzionePagamentoString));
							
						}
						
					}
					
				}
				
			}
		
		}else{
			
			// aggiorna quota ordinativo incasso
			if(StringUtils.isNotEmpty(dataScadenzaSubOrdString)){
				
				if(dataScadenzaSubOrdString.length()!=10){
					
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data scadenza ordinativo","dd/mm/yyyy"));
				}else {
					
						Date dataScadenzaQuotaIncasso = DateUtility.parse(dataScadenzaSubOrdString);
						if(null == dataScadenzaQuotaIncasso){
							//DATA FORMALMENTE NON VALIDA
							listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data scadenza ordinativo","dd/mm/yyyy"));
						} else {
							//DATA FORMALMENTE VALIDA
							if(sonoInAggiornamentoIncasso()){

								if(null!=model.getGestioneOrdinativoStep1Model().getOrdinativo().getDataEmissione()){
									
									String dataEm = DateUtility.convertiDataInGgMmYyyy(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDataEmissione());

									if(!DateUtility.checkDateMinoreUguale(dataEm, dataScadenzaSubOrdString)){
										
										listaErrori.add(ErroreFin.CAMPO_NON_ACCETTABILE.getErrore("Data scadenza quota ordinativo"));
									}
									
									// RM : ho verificato che l'aggiorna quota ordinativo passa da questo blocco di sonoInAggiornamentoIncasso
									// Controllo dell'anno, l'anno della dt di esecuzione pagamento non deve essere superiore a quello di esercizio 
									Integer inputAnnoDtScadenzaQuotaIncasso = DateUtility.getAnno(dataScadenzaQuotaIncasso);
									Integer annoBilancio = Integer.parseInt(sessionHandler.getAnnoEsercizio());
									
									if(inputAnnoDtScadenzaQuotaIncasso.compareTo(annoBilancio) > 0 ){
										
										listaErrori.add(it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_NON_VALIDA.getErrore(dataScadenzaSubOrdString));
										
									}
								}
							}else{
								
								if(!DateUtility.checkDateMinoreUguale(DateUtility.getCurrentDate(), dataScadenzaSubOrdString)){
									listaErrori.add(ErroreFin.CAMPO_NON_ACCETTABILE.getErrore("Data scadenza quota ordinativo"));
								}
							
							}
						}
				}
			}
		}
		
		if(!isEmpty(listaErrori)){
			addErrori(listaErrori);
			return INPUT;	
		}

		if(oggettoDaPopolarePagamento()){
			Liquidazione liquidazioneAssociata = null;
			SubOrdinativoPagamento subOrdinativoAttuale= null;
			// recupero la liquidazione associata al subordinativo
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null){
				
				for (SubOrdinativoPagamento subOrdinativoPagamento : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()) {
					if(sonoUgualiInt(subOrdinativoPagamento.getNumero(), numeroQuota)){
							if(!model.isSonoInAggiornamento()){
								// sono in iserimento
								liquidazioneAssociata = estraeLiquidazione(subOrdinativoPagamento.getLiquidazione().getIdLiquidazione().toString());
								subOrdinativoAttuale= subOrdinativoPagamento;
							}else{
								// sono in aggiornamento
								liquidazioneAssociata = subOrdinativoPagamento.getLiquidazione();
								subOrdinativoAttuale= subOrdinativoPagamento;
							}
						break;
					}
				}
				
			}
			
			if(nuovoImportoQuota.compareTo(liquidazioneAssociata.getDisponibilitaPagare().add(subOrdinativoAttuale.getImportoAttuale()))>0){
				addErrore(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore(""));
				return INPUT;
			}
			//Risetto la disponibilita' della liquidazione
			liquidazioneAssociata.setDisponibilitaPagare(liquidazioneAssociata.getDisponibilitaPagare().add(subOrdinativoAttuale.getImportoAttuale().subtract(nuovoImportoQuota)));
			
			//Dalla sommatoria totale tolgo l'importo iniziale della quota
			BigDecimal sommatoria = model.getGestioneOrdinativoStep2Model().getSommatoriaQuote();
			sommatoria= sommatoria.subtract(importoPrimaDiModifica);
			
			//aggiungo alla sommatoria il nuovo importo da me scelto
			sommatoria= sommatoria.add(nuovoImportoQuota);
	
			Map<Integer, BigDecimal> mappaCassa= ritornaMappaAnniDisponibilitaCassa();
			BigDecimal bd = mappaCassa.get(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
			
			
			if(nuovoImportoQuota.compareTo(importoPrimaDiModifica)>0){
				// calcolo il delta prima di verificare la disponbilità di cassa del capitolo
				BigDecimal deltaImportoQuotaInAggiornamento = nuovoImportoQuota.subtract(importoPrimaDiModifica);
				
				if(deltaImportoQuotaInAggiornamento.compareTo(bd)>0){
					addErrore(ErroreFin.DISPONIBILITA_DI_CASSA_INSUFFICIENTE.getErrore(""));
					return INPUT;
				}
				
			}
			
			
			// prima elimino la riga selezionata dal tasto aggiorna
			setNumeroQuotaEliminato(numeroQuota);
			eliminaQuotaOrdinativo(true);
			
			//aggiorno il disponibile a pagare sulla liquidazione:
			BigDecimal deltaImporto = nuovoImportoQuota.subtract(importoPrimaDiModifica);
			variaDisponibilitaLiquidazione(liquidazioneAssociata.getIdLiquidazione().toString(), deltaImporto, true); 
			////////////////////////////
			
			// travaso i dati nel model
			SubOrdinativoPagamento suboOrdinativoPagamento = new SubOrdinativoPagamento();
			
			// paolo jira-1427
			suboOrdinativoPagamento.setNumero(Integer.valueOf(numeroQuota));
			
			model.getGestioneOrdinativoStep2Model().setImportoQuota(nuovoImportoQuota);
			suboOrdinativoPagamento.setImportoAttuale(nuovoImportoQuota);
			suboOrdinativoPagamento.setDescrizione(descrizioneQuota);
			
			
			if(null!=dataEsecuzionePagamentoString){
				
				suboOrdinativoPagamento.setDataEsecuzionePagamento(DateUtility.parse(dataEsecuzionePagamentoString));
			}
			
			suboOrdinativoPagamento.setLiquidazione(liquidazioneAssociata);
			
	
			if(null!=uIdQuota){
				suboOrdinativoPagamento.setUid(Integer.parseInt(uIdQuota));
			}
			
			model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().add(suboOrdinativoPagamento);
			
			if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti())){
				
				Collections.sort(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti(), new Comparator<SubOrdinativoPagamento>() {

					@Override
					public int compare(SubOrdinativoPagamento o1,
							SubOrdinativoPagamento o2) {
						
						
						return o1.getNumero().compareTo(o2.getNumero());
					}
					
				});
			}
			

		}else{
			Accertamento accertamentoAssociato= new Accertamento();
			SubOrdinativoIncasso subOrdinativoAttuale= null;
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!=null){
				
				for (SubOrdinativoIncasso subOrdinativIncasso : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()) {
					if(subOrdinativIncasso.getNumero() == Integer.valueOf(numeroQuota)){
						subOrdinativoAttuale= subOrdinativIncasso;
							if(!model.isSonoInAggiornamentoIncasso()){
								// sono in iserimento
								accertamentoAssociato = estraeAccertamento(String.valueOf(subOrdinativIncasso.getAccertamento().getUid()));
							}else{
								//TODO  da gestire l'aggiornamento dell'incasso
								// sono in aggiornamento
								accertamentoAssociato = subOrdinativIncasso.getAccertamento();
								
								if(accertamentoAssociato instanceof SubAccertamento){
									// accertamento
									RicercaAccertamentoPerChiaveOttimizzato rac = new RicercaAccertamentoPerChiaveOttimizzato();
									rac.setEnte(sessionHandler.getEnte());
									rac.setRichiedente(sessionHandler.getRichiedente());
									RicercaAccertamentoK k = new RicercaAccertamentoK();
									
									SubAccertamento sub =(SubAccertamento)accertamentoAssociato;
									
									k.setAnnoAccertamento(sub.getAnnoAccertamentoPadre());
									k.setNumeroAccertamento(sub.getNumeroAccertamentoPadre());
									k.setAnnoEsercizio(Integer.parseInt(sessionHandler.getAnnoEsercizio()));

									rac.setpRicercaAccertamentoK(k);
									RicercaAccertamentoPerChiaveOttimizzatoResponse rp =  movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rac);
									
									if(rp!=null && rp.isFallimento()){
										
										addErrori(rp.getErrori());
										return INPUT;
									}
									accertamentoAssociato= rp.getAccertamento().getElencoSubAccertamenti().get(0);
									
								}else{
									// accertamento
									RicercaAccertamentoPerChiaveOttimizzato rac = new RicercaAccertamentoPerChiaveOttimizzato();
									rac.setEnte(sessionHandler.getEnte());
									rac.setRichiedente(sessionHandler.getRichiedente());
									RicercaAccertamentoK k = new RicercaAccertamentoK();
									
									k.setAnnoAccertamento(accertamentoAssociato.getAnnoMovimento());
									k.setNumeroAccertamento(accertamentoAssociato.getNumero());
									k.setAnnoEsercizio(Integer.parseInt(sessionHandler.getAnnoEsercizio()));

									rac.setpRicercaAccertamentoK(k);
									
									RicercaAccertamentoPerChiaveOttimizzatoResponse rp =  movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rac);
									
									if(isFallimento(rp)){
										addErrori(rp.getErrori());
										return INPUT;
									}
									
									accertamentoAssociato = rp.getAccertamento();
									
								}
								
							}
						break;
					}
				}
				
				
			}
			
			if(nuovoImportoQuota.compareTo(accertamentoAssociato.getDisponibilitaIncassare().add(subOrdinativoAttuale.getImportoAttuale()))>0 && !isQuotaIncassoImpOver()){
				addActionWarning(ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore().getCodice()+":"+ErroreCore.INSERIMENTO_QUOTA_CON_CONFERMA.getErrore("l'aggiornamento").getDescrizione());
				//TIRO FUORI LA POPUP
				 model.getGestioneOrdinativoStep2Model().setCheckAggiornaQuoteIncasso(true);
				 //copio i dati attuali cambiati in un oggetto temporaneo
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setNumeroQuota(numeroQuota);
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setDescrizione(descrizioneQuota);
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setImportoQuotaFormattato(importoQuotaFormattato);
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setDataEsecuzionePagamento(dataEsecuzionePagamentoString);
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setDataScadenzaSubOrd(dataScadenzaSubOrdString);
						model.getGestioneOrdinativoStep2Model().getCopiaDettaglioQuotaOrdinativoModel().setuIdQuota(uIdQuota);
				return INPUT;
			}
			
			// prima elimino la riga selezionata dal tasto aggiorna
			setNumeroQuotaEliminato(numeroQuota);
			eliminaQuotaOrdinativo(true);
			
			// travaso i dati nel model
			SubOrdinativoIncasso subOrdinativoIncasso = new SubOrdinativoIncasso();
			
			
			
			model.getGestioneOrdinativoStep2Model().setImportoQuota(nuovoImportoQuota);
			subOrdinativoIncasso.setImportoAttuale(nuovoImportoQuota);
			subOrdinativoIncasso.setDescrizione(descrizioneQuota);
			// paolo jira-1427
			subOrdinativoIncasso.setNumero(Integer.valueOf(numeroQuota));
			
			if(null!=dataScadenzaSubOrdString){
				
				subOrdinativoIncasso.setDataScadenza(DateUtility.parse(dataScadenzaSubOrdString));
			}
			BigDecimal tmp= BigDecimal.ZERO;
			
			if(accertamentoAssociato instanceof SubAccertamento){
				// subaccertamento
				subOrdinativoIncasso.setAccertamento(((SubAccertamento)accertamentoAssociato));
				if(isQuotaIncassoImpOver()){
					ModificaMovimentoGestioneEntrata mmge= new ModificaMovimentoGestioneEntrata();
					//ma cha cavolo devo mettere?!
					mmge.setTipoMovimento(Constanti.MODIFICA_TIPO_SAC);
					mmge.setImportoOld(accertamentoAssociato.getImportoAttuale());
					tmp= convertiImportoToBigDecimal(importoQuotaFormattato).subtract(accertamentoAssociato.getDisponibilitaIncassare().add(accertamentoAssociato.getImportoAttuale()));
					mmge.setImportoNew(tmp);
					
					mmge.setDescrizioneModificaMovimentoGestione(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
					mmge.setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
					mmge.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
					
					if(subOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata()== null){
						subOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
					}
					
					//Setto a new la lista delle modifiche onde evitare che ci siano piu' valori al servizio
					subOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
					
					subOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(mmge);
				}
			}else{
				// accertamento
				subOrdinativoIncasso.setAccertamento(((Accertamento)accertamentoAssociato));
				if(isQuotaIncassoImpOver()){
					
					ModificaMovimentoGestioneEntrata mmge= new ModificaMovimentoGestioneEntrata();
					//ma cha cavolo devo mettere?!
					mmge.setTipoMovimento(Constanti.MODIFICA_TIPO_ACC);
					//TODO controllare che l'importo old sia giusto
					mmge.setImportoOld(accertamentoAssociato.getImportoAttuale());

					tmp= convertiImportoToBigDecimal(importoQuotaFormattato).subtract(accertamentoAssociato.getDisponibilitaIncassare().add(accertamentoAssociato.getImportoAttuale()));
					mmge.setImportoNew(tmp);
					
					mmge.setDescrizioneModificaMovimentoGestione(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
					mmge.setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
					mmge.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
					
					if(subOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata()== null){
						subOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
					}
					
					//Setto a new la lista delle modifiche onde evitare che ci siano piu' valori al servizio
					subOrdinativoIncasso.getAccertamento().setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
					
					subOrdinativoIncasso.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(mmge);
					
				}
				
			}
		
			if(null!=uIdQuota){
				subOrdinativoIncasso.setUid(Integer.parseInt(uIdQuota));
			}
			
			model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().add(subOrdinativoIncasso);
			
			if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso())){
				
				Collections.sort(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso(), new Comparator<SubOrdinativoIncasso>() {

					@Override
					public int compare(SubOrdinativoIncasso o1,	SubOrdinativoIncasso o2) {
						
						return o1.getNumero().compareTo(o2.getNumero());
					}
					
				});
			}
			
			
			//aggiorno il disponibile a INCASSARE sull'accertamento
			BigDecimal deltaImporto = nuovoImportoQuota.subtract(importoPrimaDiModifica);
			variaDisponibilitaIncassoAcc(String.valueOf(accertamentoAssociato.getUid()), deltaImporto, true); 
			////////////////////////////
			
		}
		
		
		if(oggettoDaPopolarePagamento()){
			
			if(model.isSonoInAggiornamento()){
				// sommo le righe
				sommatoriaQuoteSubOrdPagamentoPerAggiorna();
								
				List<SubOrdinativoPagamento> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
				
				for (int i =1 ; i < model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size() ; i++) {
					
					// CR 4198 Mappo la data del primo ordinativo in tutte le altre quote! 
					model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().get(i).setDataEsecuzionePagamento(elencoQuote.get(0).getDataEsecuzionePagamento());
					
				}
				
			}else{
				// sommo le righe
				sommatoriaQuoteSubOrdPagamento();
				
				List<SubOrdinativoPagamento> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti();
				
				if (elencoQuote.size() == 1){
					//Questa condizione prepopola la data esecuzione, uguale alle date esecuzioni inserite precedentemente
					model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento(convertDateToString(elencoQuote.get(0).getDataEsecuzionePagamento()));
					
				}
				
				
			}
		}else{
			if(model.isSonoInAggiornamentoIncasso()){
				sommatoriaQuoteSubOrdIncassoPerAggiorna();
			}else{
				sommatoriaQuoteSubOrdIncasso();

				List<SubOrdinativoIncasso> elencoQuote = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
				
				if (elencoQuote.size() == 1){
					//Questa condizione prepopola la data esecuzione, uguale alle date esecuzioni inserite precedentemente
					model.getGestioneOrdinativoStep2Model().setDataEsecuzionePagamento(convertDateToString(elencoQuote.get(0).getDataScadenza()));
					
				}
			}
			//risetto l'oggetto per la popup dell'aggiorna quota, come nuovo
			model.getGestioneOrdinativoStep2Model().setCopiaDettaglioQuotaOrdinativoModel(new DettaglioQuotaOrdinativoModel());
		}
		return SUCCESS;
	}
	
	protected Map<Integer, BigDecimal> ritornaMappaAnniDisponibilitaCassa(){
			
			Map<Integer, BigDecimal> mappa = new HashMap<Integer, BigDecimal>();
			
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){	    	
				for(int i =0;i<3;i++){
		    		if(i==0){
		    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitolo().get(i).getAnnoCompetenza(), model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloUG().get(i).getDisponibilitaPagare());
		    		}
		    		if(i==1){
		    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitolo().get(i).getAnnoCompetenza(), model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloUG().get(i).getDisponibilitaPagare());
		
		    		}
		    		
		    		if(i==2){
		    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitolo().get(i).getAnnoCompetenza(), model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloUG().get(i).getDisponibilitaPagare());
		    		}
				}
			}else{
			
				// TODO
			}
			
			return mappa;
			
	}
	
	
	protected void caricaLiquidazioniOrdinativo() {
		model.getGestioneOrdinativoStep2Model().setListaLiquidazioni(new ArrayList<Liquidazione>());
		if (model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione() != null && model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione() != null) {
			RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
			RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK(); 
			Liquidazione liq = new Liquidazione();
			liq.setAnnoLiquidazione(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione());
			liq.setNumeroLiquidazione(new BigDecimal(model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()));
			ricercaLiquidazioneK.setLiquidazione(liq);
			ricercaLiquidazioneK.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
			ricercaLiquidazioneK.setBilancio(sessionHandler.getBilancio());
			ricercaLiquidazioneK.setTipoRicerca(Constanti.TIPO_RICERCA_DA_ORDINATIVO);
			
			req.setEnte(sessionHandler.getAccount().getEnte());
			req.setRichiedente(sessionHandler.getRichiedente());
			req.setDataOra(new Date());
			req.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
			
			
			
			RicercaLiquidazionePerChiaveResponse response = liquidazioneService.ricercaLiquidazionePerChiave(req);
			if (response != null && response.getLiquidazione() != null && !response.isFallimento()) {
				if (response.getLiquidazione().getCapitoloUscitaGestione() != null) {
					
					boolean stessoCapitolo = sonoLoStessoCapitolo(response.getLiquidazione().getCapitoloUscitaGestione(),  model.getGestioneOrdinativoStep1Model().getCapitolo());
					
					if (stessoCapitolo){
						
						controlliPerLiquidazioniInOrdinativo(response.getLiquidazione(),true);
						
						// se la liquidazione va bene per essere visualizzata allora
						// proseguo con le preselect della pagina step2
						if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaLiquidazioni())){
							// preseleziono l'unica liquidazione trovata
							model.getGestioneOrdinativoStep2Model().setRadioIdLiquidazione(String.valueOf(response.getLiquidazione().getIdLiquidazione()));
							
							// preseleziono l'importo con il disponibile a pagare se >0
							if(null!=response.getLiquidazione().getDisponibilitaPagare() && response.getLiquidazione().getDisponibilitaPagare().compareTo(BigDecimal.ZERO) > 0){
								model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato(convertiBigDecimalToImporto(response.getLiquidazione().getDisponibilitaPagare()));
							}
							
							// lancio la routine per caricare i dati della TE a partire dalla liquidazione preselezionata appena sopra
							ricaricaTEByIdLiquidazione();
						} else {
							//TE SUPPORT:
							 codiceSiopeChangedInternal(response.getLiquidazione().getCodSiope());
							//
						}
						
						//SIOPE PLUS
						impostaDatiSiopePlusNelModel(response.getLiquidazione(), model.getGestioneOrdinativoStep2Model());
						//
						
					} else {
						addErrore(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("la liquidazione " + model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione() + "/" + model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()
								+ " appartiene al capitolo " + response.getLiquidazione().getCapitoloUscitaGestione().getNumeroCapitolo() + "/" + response.getLiquidazione().getCapitoloUscitaGestione().getNumeroArticolo()
								+ "/" + response.getLiquidazione().getCapitoloUscitaGestione().getNumeroUEB()));
					}
				}
			} else if(response != null && response.getLiquidazione() == null && !response.isFallimento()) {
				//liquidazione inesistente
				addErrore(ErroreFin.LIQUIDAZIONE_INESISTENTE.getErrore());
			} else if(response!=null) {
				
				if(response.getErrori()!=null && response.getErrori().size()==1){
					// se c'e' un errore e l'errore e' di tipo COR_ERR_0001
					// allora non lo devo visualizzare
					if(!response.getErrori().get(0).getCodice().equals("COR_ERR_0001")){
						addErrori(response);
					}
					
				}else{
					addErrori(response);
				}
			}
		} else 	{

			if(!sonoInAggiornamento()){
				RicercaLiquidazioni request = new RicercaLiquidazioni();
				request.setEnte(sessionHandler.getEnte());
				request.setRichiedente(sessionHandler.getRichiedente());
				ParametroRicercaLiquidazione parametroRicercaLiquidazione = new ParametroRicercaLiquidazione();
				parametroRicercaLiquidazione.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
				if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
					parametroRicercaLiquidazione.setAnnoCapitolo(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno());
					parametroRicercaLiquidazione.setNumeroCapitolo(new BigDecimal(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo()));
					parametroRicercaLiquidazione.setNumeroArticolo(new BigDecimal(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo()));
					parametroRicercaLiquidazione.setNumeroUEB((model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb()==null? 0 : model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb().intValue()));
				}
				if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
					parametroRicercaLiquidazione.setAnnoProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento());
					parametroRicercaLiquidazione.setNumeroProvvedimento(new BigDecimal(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento()));
					parametroRicercaLiquidazione.setTipoProvvedimento(String.valueOf(model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento()));
				}
				if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {
					parametroRicercaLiquidazione.setCodiceCreditore(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
				}
				
				// mi serve per non leggere le liquidzioni legate a documenti
				parametroRicercaLiquidazione.setTipoRicerca(Constanti.TIPO_RICERCA_DA_ORDINATIVO);
				
				// anno bilancio
				parametroRicercaLiquidazione.setAnnoBilancio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
				
				request.setParametroRicercaLiquidazione(parametroRicercaLiquidazione);
				RicercaLiquidazioniResponse response = liquidazioneService.ricercaLiquidazioni(request);
				
				if(!isFallimento(response) && !isEmpty(response.getElencoLiquidazioni())){
					for (Liquidazione currentLiquidazione : response.getElencoLiquidazioni()) {
						controlliPerLiquidazioniInOrdinativo(currentLiquidazione,false);
					}
					reimpostaDisponibilitaLiquidazioni();
				} else {
					addErrori(response);
				}
			}
		}
	}
	
	/**
	 * imposta i dati della liquidazione indicata, 
	 * serve per attingere ai dati siope plus per
	 * l'ordinativo di pagamento che si intende creare
	 * 
	 * @param liqDaCuiLeggere
	 * @param modelInCuiImpostare
	 */
	protected void impostaDatiSiopePlusNelModel(Liquidazione liqDaCuiLeggere,GestioneOrdinativoStep2Model modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(codiceMotivazioneAssenzaCig(liqDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//CIG
		modelInCuiImpostare.setCig(liqDaCuiLeggere.getCig());
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(liqDaCuiLeggere.getSiopeTipoDebito()));
	}
	
	protected void pulisciDatiSiopePlusNelModel(GestioneOrdinativoStep2Model modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(null);
		
		//CIG
		modelInCuiImpostare.setCig(null);
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(null);
	}
	
	protected void caricaAccertamentoOrdinativo() {
		if(!model.isAccertamentoTrovato()){
			model.getGestioneOrdinativoStep2Model().setListaAccertamento(new ArrayList<Accertamento>());
			model.getGestioneOrdinativoStep2Model().setListaAccertamentoOriginale(new ArrayList<Accertamento>());
			model.getGestioneOrdinativoStep2Model().setResultSize(0);
		}
		

		
		if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento() != null && 
				model.getGestioneOrdinativoStep1Model().getNumeroAccertamento() != null &&
				!model.isAccertamentoTrovato()) {
    		RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();

    		rap.setEnte(sessionHandler.getEnte());
    		rap.setRichiedente(sessionHandler.getRichiedente());
    		RicercaAccertamentoK k = new RicercaAccertamentoK();
    		k.setAnnoEsercizio(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
    		k.setAnnoAccertamento(model.getGestioneOrdinativoStep1Model().getAnnoAccertamento());
    		k.setNumeroAccertamento(new BigDecimal(model.getGestioneOrdinativoStep1Model().getNumeroAccertamento()));
    		rap.setpRicercaAccertamentoK(k);
    		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rap);
			if (!isFallimento(response) && response.getAccertamento() != null){
			
				if (response.getAccertamento().getCapitoloEntrataGestione() != null) {
					
					CapitoloEntrataGestione capitoloAcc = response.getAccertamento().getCapitoloEntrataGestione();
					CapitoloImpegnoModel capitoloModel = model.getGestioneOrdinativoStep1Model().getCapitolo();
					
					/**
					 * Questo sarebbe la nuova condizione tecnicamente corretta ma non fa funzionare il tutto..indagare con laura 
					 * 
					boolean almenoUnoDiverso = !sonoUgualiInt(capitoloAcc.getAnnoCapitolo(), capitoloModel.getAnno())
					|| !sonoUgualiInt(capitoloAcc.getNumeroCapitolo(),capitoloModel.getNumCapitolo())
					|| !sonoUgualiInt(capitoloAcc.getNumeroArticolo(),capitoloModel.getArticolo())
					|| !sonoUgualiInt(capitoloAcc.getNumeroUEB(),capitoloModel.getUeb().intValue());
					*/
					
					//Questo e' il vecchio controllo tecnicamento sbagliato ma che non mi fa atterrare sull'errore:
					//COR_ERR_0029 - Esiste un'incongruenza tra i parametri di input: l'accertamento .. appartiene al capitolo ..
					boolean almenoUnoDiverso = 
							capitoloAcc.getAnnoCapitolo() != capitoloModel.getAnno()
							|| capitoloAcc.getNumeroCapitolo() != capitoloModel.getNumCapitolo()
							|| capitoloAcc.getNumeroArticolo() != capitoloModel.getArticolo()
							|| capitoloAcc.getNumeroUEB() != capitoloModel.getUeb().intValue();
					
					if (almenoUnoDiverso) {
						//almeno uno diverso non sono lo stesso capitolo
						
						controlliPerAccertamentoInOrdinativo(response.getAccertamento());
						
						if(model.getGestioneOrdinativoStep2Model().getListaAccertamento()==null || model.getGestioneOrdinativoStep2Model().getListaAccertamento().size()==0){
							addPersistentActionWarning(ErroreFin.CRU_WAR_1003.getErrore().getCodice()+" : "+ErroreFin.CRU_WAR_1003.getErrore(" accertamenti o subaccertamenti").getDescrizione());
						}
						
						model.getGestioneOrdinativoStep2Model().setResultSize(model.getGestioneOrdinativoStep2Model().getListaAccertamento().size());
						ricaricaTEByIdAccertamento();
						
					} else {
						//nessuno diverso,  sono lo stesso capitolo
						addErrore(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("l'accertamento " + model.getGestioneOrdinativoStep1Model().getAnnoAccertamento() + "/" + model.getGestioneOrdinativoStep1Model().getNumeroAccertamento()
								+ " appartiene al capitolo " + response.getAccertamento().getCapitoloEntrataGestione().getNumeroCapitolo() + "/" + response.getAccertamento().getCapitoloEntrataGestione().getNumeroArticolo()
								+ "/" + response.getAccertamento().getCapitoloEntrataGestione().getNumeroUEB()));
					}
				}
				
				//JIRA  SIAC-4116 introdotto controllo:
				boolean subCompilatoMaNonPresente = subCompilatoMaNonPresente(response.getAccertamento());
				if(subCompilatoMaNonPresente){
					addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento",model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()));
				}
				//
				
				//setto il codice del pdc:
				settaPdcDaAccertamentoCercato(response.getAccertamento(), estraiSubCompilato(response.getAccertamento()));;
				//
				
				
			}else if(response==null || response.getAccertamento()==null){
				if(model.getGestioneOrdinativoStep2Model().getListaAccertamento()==null || model.getGestioneOrdinativoStep2Model().getListaAccertamento().size()==0){
					addPersistentActionWarning(ErroreFin.CRU_WAR_1003.getErrore().getCodice()+" : "+ErroreFin.CRU_WAR_1003.getErrore(" accertamenti o subaccertamenti").getDescrizione());
				}
			}
		}
		
	}
	
	protected void settaPdcDaAccertamentoCercato(Accertamento accertamento, SubAccertamento subAcc){
		if(subAcc!=null ){
			//setto il codice del piano dei conti a partire da quello del sub accertamento:
			model.getGestioneOrdinativoStep1Model().setCodPdcAccertamentoCercato(subAcc.getCodPdc());
			//
		}else{
			//setto il codice del piano dei conti a partire da quello dell'accertamento:
			model.getGestioneOrdinativoStep1Model().setCodPdcAccertamentoCercato(accertamento.getCodPdc());
			//
		}
	}
	
	
	protected boolean subCompilatoMaNonPresente(Accertamento accertamento){
		
		boolean subCompilatoMaNonPresente = false;
		
		if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null && model.getGestioneOrdinativoStep1Model().getNumeroSubAcc().intValue()>0){
			//SUB COMPILATO, MI ASPETTO CHE CI SIA TRA QUELLI DELL'ACCERTAMENTO:
			boolean subIndicatoPresente = false;
			if(accertamento.getElencoSubAccertamenti()!=null && !accertamento.getElencoSubAccertamenti().isEmpty()){
				for (SubAccertamento sub : accertamento.getElencoSubAccertamenti()) {
					BigInteger numerosub = sub.getNumero().toBigInteger();
					if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null){
						if(numerosub.equals(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc())){
							//ok trovato corrisponde!
							subIndicatoPresente = true;
							break;
						}
					}
				}
			}
			
			if(!subIndicatoPresente){
				subCompilatoMaNonPresente = true;
			}
			
		}
		
		return subCompilatoMaNonPresente;
	}
	
	
	protected SubAccertamento estraiSubCompilato(Accertamento accertamento){
		SubAccertamento subCompilato = null;
		if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null && model.getGestioneOrdinativoStep1Model().getNumeroSubAcc().intValue()>0){
			//SUB COMPILATO, MI ASPETTO CHE CI SIA TRA QUELLI DELL'ACCERTAMENTO:
			if(accertamento.getElencoSubAccertamenti()!=null && !accertamento.getElencoSubAccertamenti().isEmpty()){
				for (SubAccertamento sub : accertamento.getElencoSubAccertamenti()) {
					BigInteger numerosub = sub.getNumero().toBigInteger();
					if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null){
						if(numerosub.equals(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc())){
							//ok trovato corrisponde!
							subCompilato = sub;
							break;
						}
					}
				}
			}
		}
		return subCompilato;
	}
	
	/**
	 * Controlla la validita della liquidazione selezionata dal punto di vista dei dati
	 * del siope plus
	 * 
	 * riferimentoInErrore: per avere o meno nell'errore anche l'anno e il numero della liquidazione
	 * 
	 * @param liquidazione
	 * @param riferimentoInErrore
	 * @return
	 */
	protected boolean verificaDatiSiopePlusLiqSelezionata(Liquidazione liquidazione, boolean riferimentoInErrore,boolean lanciaErrore){
		
		boolean tuttoOk = true;
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		String riferimentoLiquidazione = liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione();
		
		//CONTROLLI CHE SIA ACCETTABILE RISPETTO AL SIOPE PLUS:
		String codiceTipoDebito = null;
		if(liquidazione.getSiopeTipoDebito()==null || isEmpty(liquidazione.getSiopeTipoDebito().getCodice())){
			//manca il tipo debito 
			if(!riferimentoInErrore){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione ha dati SIOPE + incongruenti: 'Tipo Debito')"));
			} else {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione "+riferimentoLiquidazione+" ha dati SIOPE + incongruenti: 'Tipo Debito')"));
			}
		} else {
			codiceTipoDebito = liquidazione.getSiopeTipoDebito().getCodice();
		}
		
	   String assenzaCigCode = codiceMotivazioneAssenzaCig(liquidazione.getSiopeAssenzaMotivazione());
	   String cigLiq = liquidazione.getCig();
	   if(FinStringUtils.entrambiValorizzati(cigLiq,assenzaCigCode)){
	    	//CIG e Motivazione assenza CIG non possono coesistere
		   if(!riferimentoInErrore){
			   listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione ha dati SIOPE + incongruenti: 'CIG')"));
		   } else {
			   listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione "+riferimentoLiquidazione+" ha dati SIOPE + incongruenti: 'CIG')"));
		   }
	    	
	    	//metto else if perche' propongono lo stesso identico messaggio di errore e
	    	//non vorrei che ne presentasse due uguali, togliere l'else se 
	    	//dovesse cambiare uno di questi due messaggi uguali
	    } else if(Constanti.SIOPE_CODE_COMMERCIALE.equals(codiceTipoDebito) && FinStringUtils.entrambiVuoti(cigLiq,assenzaCigCode)){
	    	//SE "Tipo SIOPE" = Commerciale
	        //CIG o, in alternativa, Motivazione assenza CIG sono obbligatori 
	    	if(!riferimentoInErrore){
	    		listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione ha dati SIOPE + incongruenti: 'CIG')"));
	    	} else {
	    		listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Liquidazione", " (La liquidazione "+riferimentoLiquidazione+" ha dati SIOPE + incongruenti: 'CIG')"));
			}
	    }
	   
	    if(!isEmpty(listaErrori)){
	    	if(lanciaErrore){
	    		addErrori(listaErrori);
	    	}
			tuttoOk = false;
		}
	    
	    return tuttoOk;
	}
	
	/**
	 * effettua i controlli di una liquidazione appena caricata dal back-end
	 * @param liquidazione
	 * @param lanciaErrore
	 */
	private void controlliPerLiquidazioniInOrdinativo(Liquidazione liquidazione,boolean lanciaErrore) {
		if (liquidazione.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.VALIDO)) {
			if (liquidazione.getDisponibilitaPagare().compareTo(BigDecimal.ZERO) > 0) {
				
				
				//verifiche siope plus:
				boolean tuttoOkSiopePlus = verificaDatiSiopePlusLiqSelezionata(liquidazione,true,lanciaErrore);
				if(tuttoOkSiopePlus){
					
					
					// filtro sulla Sede e sulla MDP
					if(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()!=0){
						// se ho selezionato una sede devo vedere che sia la stessa della liquidazione
						if(liquidazione.getSedeSecondariaSoggetto()!=null){
							if(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()==liquidazione.getSedeSecondariaSoggetto().intValue()){
								
								// anche sulla stessa MDP
								
								if(liquidazione.getModalitaPagamentoSoggetto()!= null && liquidazione.getModalitaPagamentoSoggetto().getUid()!=0){
									
									if(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()==liquidazione.getModalitaPagamentoSoggetto().getUid()){
										
										model.getGestioneOrdinativoStep2Model().getListaLiquidazioni().add(liquidazione);
									}
									
								}
							}
						}
						
					}else{
						// stessa MDP
						
						if(liquidazione.getModalitaPagamentoSoggetto()!=null && liquidazione.getModalitaPagamentoSoggetto().getUid()!=0){
							
							if(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()==liquidazione.getModalitaPagamentoSoggetto().getUid()){
								
								model.getGestioneOrdinativoStep2Model().getListaLiquidazioni().add(liquidazione);
							}
							
						}
					}
					
					
				}
				//
			}
		} else {
			if(lanciaErrore){
				addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("la liquidazione " + liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione(), "stato = " + liquidazione.getStatoOperativoLiquidazione().name()));
			}
		}
	}
	
	
	private void controlliPerAccertamentoInOrdinativo(Accertamento accertamento) {
		
		List<Accertamento> listaAccDaPresentare = new ArrayList<Accertamento>();
		
		boolean accerInStatoDefinitivoConSubAnnullati = accertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO);
					
		// verifico prima di tutto se ci sono i subaccertamenti ma solo se l'accertamento non e' DEFINITIVO
		if(!accerInStatoDefinitivoConSubAnnullati && !isEmpty(accertamento.getElencoSubAccertamenti())){
			
			for (SubAccertamento itSubAcc : accertamento.getElencoSubAccertamenti()) {
								
				if(itSubAcc.getAnnoMovimento()<=Integer.valueOf(sessionHandler.getAnnoEsercizio())){
					
					if(!itSubAcc.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_ANNULLATO)
							&& !itSubAcc.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_PROVVISORIO)){
						
						if(itSubAcc.getDisponibilitaIncassare()!=null && itSubAcc.getDisponibilitaIncassare().compareTo(BigDecimal.ZERO)>0){

								
								if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null){
									
									// se nella prima pagina specifico il sub allora devo tirare fuori il
									// sub preciso richiesto									 
									if(new BigDecimal(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc().longValue()).compareTo(new BigDecimal(itSubAcc.getNumero().longValue()))==0){
										listaAccDaPresentare.add(itSubAcc);	
										
										// imposto importo e radio
										model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato(convertiBigDecimalToImporto(itSubAcc.getDisponibilitaIncassare()));
										model.getGestioneOrdinativoStep2Model().setRadioIdAccertamento(String.valueOf(itSubAcc.getUid()));
										
									}
									
								}else{
									// se non e' specificato nulla tiro fuori i sub
									listaAccDaPresentare.add(itSubAcc);
								}
						}else{
							
							// Rilancio warning!!!
							addPersistentActionWarning(ErroreFin.DISPONIBILITA_ACCERTAMENTO_INSUFFICIENTE.getErrore().getCodice()+" : "+ErroreFin.DISPONIBILITA_ACCERTAMENTO_INSUFFICIENTE.getErrore().getDescrizione());
						}
					}
				}
			}
		} else {
				
			// setto l'accertamento
			if(accertamento.getAnnoMovimento()<=Integer.valueOf(sessionHandler.getAnnoEsercizio())){
				if (!accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_ANNULLATO)
						&& !accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_PROVVISORIO)) {
					
					listaAccDaPresentare.add(accertamento);
					
				}
	
			}
		}
		
		if(!isEmpty(listaAccDaPresentare)){
			// aggiungo tutti gli acc e i sub acc
			model.getGestioneOrdinativoStep2Model().getListaAccertamento().addAll(listaAccDaPresentare);
		}
	}
	
	/**
	 * Da utilizzare quando vengono ricaricate le liquidazioni dal back-end e si deve re impostare la disponibilita' a liquidare
	 * in funzione del fatto che possono gia' essere presenti quote inserite in sessione
	 */
	private void reimpostaDisponibilitaLiquidazioni() {
		Iterator<Liquidazione> itLiquidazione =  model.getGestioneOrdinativoStep2Model().getListaLiquidazioni().iterator();
		while(itLiquidazione.hasNext()){
			Liquidazione liquidazione = itLiquidazione.next();
			if(liquidazione.getIdLiquidazione()!=null){
				SubOrdinativoPagamento quotaGiaInserita = estraiSubOrdinativoPagamentoByLiquidazione(liquidazione.getIdLiquidazione().toString());
				if(quotaGiaInserita!=null){
					BigDecimal importoQuota = quotaGiaInserita.getImportoIniziale();
					variaDisponibilitaLiquidazione(liquidazione.getIdLiquidazione().toString(), importoQuota, true);
				}
			}
		}
	}
	
	public String cercaProvvisorio(){
		
		// chiamata a servizio
		setToggleRicercaProvvAperto(true);
		
		// quando ricerco e quando seleziono il radio del provvisorio trovato, devo resettare il valore del riporta in testata della pagina 
		model.getGestioneOrdinativoStep3Model().setRiportaInTestataPageInserisciProvvisorio(Boolean.FALSE);
		
		
		RicercaProvvisoriDiCassa rpc = new RicercaProvvisoriDiCassa();
	
		rpc.setEnte(sessionHandler.getEnte());
		rpc.setNumPagina(0);
		rpc.setBilancio(sessionHandler.getBilancio());
		rpc.setRichiedente(sessionHandler.getRichiedente());
		ParametroRicercaProvvisorio prp = new ParametroRicercaProvvisorio();
		
		// attenzione fake per farlo funzionare
		prp.setNumeroDa(model.getRicercaProvvisorioModel().getNumeroProvvisorioDa()); // numero da
		prp.setNumeroA(model.getRicercaProvvisorioModel().getNumeroProvvisorioA());   // numero a
		
		prp.setAnnoDa(model.getRicercaProvvisorioModel().getAnnoProvvisorioDa()); // anno Da
		prp.setAnnoA(model.getRicercaProvvisorioModel().getAnnoProvvisorioA());   // anno a

		prp.setImportoDa(model.getRicercaProvvisorioModel().getImportoDa()); // importo Da
		prp.setImportoA(model.getRicercaProvvisorioModel().getImportoA());   // importo a
		
		//SIAC6352
		prp.setDescCausale(model.getRicercaProvvisorioModel().getDescCausale());   
		prp.setDenominazioneSoggetto(model.getRicercaProvvisorioModel().getDenominazioneSoggetto());
		prp.setContoTesoriere(model.getRicercaProvvisorioModel().getContoTesoriere());
		
		
		
		if(null!=model.getRicercaProvvisorioModel().getAnnoProvvisorioDa() && model.getRicercaProvvisorioModel().getAnnoProvvisorioDa()<1900){
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("anno da", "deve essere maggiore di 1900"));
		}
		
		if(null!=model.getRicercaProvvisorioModel().getAnnoProvvisorioA() && model.getRicercaProvvisorioModel().getAnnoProvvisorioA()<1900){
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("anno a", "deve essere maggiore di 1900"));
		}
		
		// da inserire le date
		String dataInizioEmissione = model.getRicercaProvvisorioModel().getDataInizioEmissione();
		
		if(StringUtils.isNotEmpty(dataInizioEmissione) && dataInizioEmissione!=null){
				
			if(dataInizioEmissione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			} else if(!DateUtility.isDate(dataInizioEmissione, "dd/MM/yyyy")){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
				if(!oggettoDaPopolarePagamento()){
					// verifico che le date siano nel anno esercizio
					if(!sessionHandler.getAnnoEsercizio().equals(dataInizioEmissione.substring(6))){
						
						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
					}
							
				}
			    // ordinativo pagamento nessun controllo su anno esercizio
				prp.setDataInizioEmissione(DateUtility.parseData(dataInizioEmissione));
				
			}
				
		}	
		
		String dataFineEmissione = model.getRicercaProvvisorioModel().getDataFineEmissione();
		
		if(StringUtils.isNotEmpty(dataFineEmissione) && dataFineEmissione!=null) {
			if(dataFineEmissione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else {
				if(!DateUtility.isDate(dataFineEmissione, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
				}else{
					
					// in caso di ordinativo incasso
					if(!oggettoDaPopolarePagamento()){
						// verifico che le date siano nel anno esercizio
						if(!sessionHandler.getAnnoEsercizio().equals(dataFineEmissione.substring(6))){
							
							addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
						}
								
					}
				    //ordinativo pagamento nessun controllo su anno esercizio
					prp.setDataFineEmissione(DateUtility.parseData(dataFineEmissione));
				}
				
			}
		}
		
		
		String dataInizioTrasmissione = model.getRicercaProvvisorioModel().getDataInizioTrasmissione();
		
		if(StringUtils.isNotEmpty(dataInizioTrasmissione) && dataInizioTrasmissione!=null){
			if(dataInizioTrasmissione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataInizioTrasmissione, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
				if(!oggettoDaPopolarePagamento()){
					// verifico che le date siano nel anno esercizio
					if(!sessionHandler.getAnnoEsercizio().equals(dataInizioTrasmissione.substring(6))){
						
						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
					}
							
				}
			    // ordinativo pagamento nessun controllo su anno esercizio
				prp.setDataInizioTrasmissione(DateUtility.parseData(dataInizioTrasmissione));
				
			}
		}	
		
		String dataFineTrasmissione = model.getRicercaProvvisorioModel().getDataFineTrasmissione();
		
		if(StringUtils.isNotEmpty(dataFineTrasmissione) && dataFineTrasmissione!=null){
			if(dataFineTrasmissione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataFineTrasmissione, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
				if(!oggettoDaPopolarePagamento()){
					// verifico che le date siano nel anno esercizio
					if(!sessionHandler.getAnnoEsercizio().equals(dataFineTrasmissione.substring(6))){
						
						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
					}
							
				} 
				prp.setDataFineTrasmissione(DateUtility.parseData(dataFineTrasmissione));
			}
		}	
			
		
		String dataInizioInvioServizio = model.getRicercaProvvisorioModel().getDataInizioInvioServizio();
		
		if(StringUtils.isNotEmpty(dataInizioInvioServizio) && dataInizioInvioServizio!=null){
			if(dataInizioInvioServizio.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataInizioInvioServizio, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
//				if(!oggettoDaPopolarePagamento()){
//					// verifico che le date siano nel anno esercizio
//					if(!sessionHandler.getAnnoEsercizio().equals(dataInizioInvioServizio.substring(6))){
//						
//						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
//					}
//							
//				}
			    // ordinativo pagamento nessun controllo su anno esercizio
				prp.setDataInizioInvioServizio(DateUtility.parseData(dataInizioInvioServizio));
				
			}
		}	
		
		String dataFineInvioServizio = model.getRicercaProvvisorioModel().getDataFineInvioServizio();
		
		if(StringUtils.isNotEmpty(dataFineInvioServizio) && dataFineInvioServizio!=null){
			if(dataFineInvioServizio.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataFineInvioServizio, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
//				if(!oggettoDaPopolarePagamento()){
//					// verifico che le date siano nel anno esercizio
//					if(!sessionHandler.getAnnoEsercizio().equals(dataFineInvioServizio.substring(6))){
//						
//						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
//					}
//							
//				} 
				
				prp.setDataFineInvioServizio(DateUtility.parseData(dataFineInvioServizio));
			}
		}	
			
				
		
		String dataInizioRifiutoErrataAttribuzione = model.getRicercaProvvisorioModel().getDataInizioRifiutoErrataAttribuzione();
		
		if(StringUtils.isNotEmpty(dataInizioRifiutoErrataAttribuzione) && dataInizioRifiutoErrataAttribuzione!=null){
			if(dataInizioRifiutoErrataAttribuzione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataInizioRifiutoErrataAttribuzione, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
//				if(!oggettoDaPopolarePagamento()){
//					// verifico che le date siano nel anno esercizio
//					if(!sessionHandler.getAnnoEsercizio().equals(dataInizioRifiutoErrataAttribuzione.substring(6))){
//						
//						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
//					}
//							
//				}
			    // ordinativo pagamento nessun controllo su anno esercizio
				prp.setDataInizioRifiutoErrataAttribuzione(DateUtility.parseData(dataInizioRifiutoErrataAttribuzione));
				
			}
		}	
		
		String dataFineRifiutoErrataAttribuzione = model.getRicercaProvvisorioModel().getDataFineRifiutoErrataAttribuzione();
		
		if(StringUtils.isNotEmpty(dataFineRifiutoErrataAttribuzione) && dataFineRifiutoErrataAttribuzione!=null){
			if(dataFineRifiutoErrataAttribuzione.length() != 10){
				addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}	else if(!DateUtility.isDate(dataFineRifiutoErrataAttribuzione, "dd/MM/yyyy")){
					addActionError(ERRORE_FORMATO_DATA_POP_UP);
			}else{
				// in caso di ordinativo incasso
//				if(!oggettoDaPopolarePagamento()){
//					// verifico che le date siano nel anno esercizio
//					if(!sessionHandler.getAnnoEsercizio().equals(dataFineRifiutoErrataAttribuzione.substring(6))){
//						
//						addActionError(ERRORE_DATA_NON_IN_ESERCIZIO);
//					}
//							
//				} 
				prp.setDataFineRifiutoErrataAttribuzione(DateUtility.parseData(dataFineRifiutoErrataAttribuzione));
			}
		}	
			
				
		
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
			prp.setTipoProvvisorio(TipoProvvisorioDiCassa.S);
		}else{
			prp.setTipoProvvisorio(TipoProvvisorioDiCassa.E);
		}
		
		// 
		prp.setFlagAnnullato(Constanti.FALSE);
		rpc.setParametroRicercaProvvisorio(prp);
		
		// se ci sono errori nella maschere non lancio la ricerca
		if(hasActionErrors()){
			model.getGestioneOrdinativoStep3Model().setListaProvvisori(new ArrayList<ProvvisorioDiCassa>());
			return SUCCESS;
		}
		
		rpc.setNumPagina(1);
		
		int maxAmmissibili = 50;
		
		rpc.setNumRisultatiPerPagina(maxAmmissibili);
		
		RicercaProvvisoriDiCassaResponse response =  provvisorioService.ricercaProvvisoriDiCassa(rpc);
		
		model.getGestioneOrdinativoStep3Model().setProvvisorio(new ProvvisorioDiCassa());
		
		if(isFallimento(response)){
			//errore
			if(response.getErrori()!=null){
				addActionError(response.getErrori().get(0).getCodice() + " " +response.getErrori().get(0).getDescrizione());
				// rimetto a vuoto la lista
				model.getGestioneOrdinativoStep3Model().setListaProvvisori(new ArrayList<ProvvisorioDiCassa>());
			}
			
		}else{
			//non fallimento, response qui non puo essere null
			
			if(response.getNumRisultati()>maxAmmissibili){
				addActionWarning("Attenzione ricerca troppo estesa trovati: " + response.getNumRisultati() + " risultati. Presentati solo i primi " +maxAmmissibili + ".");
			}
			
			if(!isEmpty(response.getElencoProvvisoriDiCassa())){
		
				model.getGestioneOrdinativoStep3Model().setListaProvvisori(response.getElencoProvvisoriDiCassa());
				
				if(oggettoDaPopolarePagamento()){
					// se la ricerca e' ok allora pulisco i campi
					model.setRicercaProvvisorioModel(new RicercaProvvisorioModel());
				
					reimpostaImportoDaRegolarizzare();
				}else{
					// in caso di incasso pulisco oggetto ma ripristino i valori di default
					model.setRicercaProvvisorioModel(new RicercaProvvisorioModel());
					model.getRicercaProvvisorioModel().setAnnoProvvisorioDa(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
					model.getRicercaProvvisorioModel().setAnnoProvvisorioA(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
				}
			}else{
				
				addActionError("Non sono stati trovati risultati");
				model.getGestioneOrdinativoStep3Model().setListaProvvisori(new ArrayList<ProvvisorioDiCassa>());
			}
			
		}
	
		setAncoraVisualizza(true);
		setAncoraVisualizzaInserisciProvErrori(false);
		setAncoraVisualizzaInserisciProv(false);
		setClearStatus(true);
		setUidProvvDaRicerca("");
		return SUCCESS;
	}
	
	
	//RICERCA ORDINATIVO PER CHIAVE
	protected RicercaOrdinativoPagamentoPerChiave convertiModelPerChiamataServizioRicercaOrdinativoPerChiave() {
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaOrdinativoPagamentoPerChiave parametroRicercaPerChiave = new RicercaOrdinativoPagamentoPerChiave();
		RicercaOrdinativoPagamentoK ordinativoDaCercare = new RicercaOrdinativoPagamentoK();
		Integer numOrdinativo = model.getGestioneOrdinativoStep1Model().getOrdinativo().getNumero();
		
		ordinativoDaCercare.setOrdinativoPagamento(new OrdinativoPagamento());
		ordinativoDaCercare.getOrdinativoPagamento().setNumero(numOrdinativo);
		ordinativoDaCercare.getOrdinativoPagamento().setAnno( model.getGestioneOrdinativoStep1Model().getOrdinativo().getAnno());
		ordinativoDaCercare.setBilancio(sessionHandler.getBilancio());
		parametroRicercaPerChiave.setDataOra(new Date());
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaOrdinativoPagamentoK(ordinativoDaCercare);
		
		return parametroRicercaPerChiave;
	}
	
	//RICERCA ORDINATIVO INCASSO PER CHIAVE
	protected RicercaOrdinativoIncassoPerChiave convertiModelPerChiamataServizioRicercaOrdinativoIncassoPerChiave() {
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaOrdinativoIncassoPerChiave parametroRicercaPerChiave = new RicercaOrdinativoIncassoPerChiave();
		RicercaOrdinativoIncassoK ordinativoDaCercare = new RicercaOrdinativoIncassoK();
		Integer numOrdinativo = model.getGestioneOrdinativoStep1Model().getOrdinativo().getNumero();
		
		ordinativoDaCercare.setOrdinativoIncasso(new OrdinativoIncasso());
		ordinativoDaCercare.getOrdinativoIncasso().setNumero(numOrdinativo);
		ordinativoDaCercare.getOrdinativoIncasso().setAnno( model.getGestioneOrdinativoStep1Model().getOrdinativo().getAnno());
		ordinativoDaCercare.setBilancio(sessionHandler.getBilancio());
		parametroRicercaPerChiave.setDataOra(new Date());
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaOrdinativoIncassoK(ordinativoDaCercare);
		
		return parametroRicercaPerChiave;
	}
	
	/*
	 *  Elimina il provvedimento
	 */
	public String eliminaProvvisorio(){
		debug("eliminaProvvisorio", " Elimina il "+getIdCoperturaDaEliminare());
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				if (pc.getIdProvvisorioDiCassa().compareTo(new BigInteger(getIdCoperturaDaEliminare()))==0) {
					//BISOGNA INCREMENTARE NUOVAMENTE L'IMPORTO DA REGOLARIZZARE:
					BigDecimal importo = pc.getImporto();
					variaImportoDaRegolarizzare(pc.getIdProvvisorioDiCassa().toString(), importo, false);
					////////////////////////////////////////////////////////////////////////
					model.getGestioneOrdinativoStep3Model().getListaCoperture().remove(pc);
					break;
				}
			}
		}
		
		calcolaTotaliFooter();

		return SUCCESS;
	}

	
	protected void caricaDatiProvvedimento(){
		//TRAVASIAMO I DATI DALL'OGGETTO ATTO AMMINISTRATIVO AL MODEL DELLA PAGINA
		impostaProvvNelModel(provvedimentoOrdinativo, model.getGestioneOrdinativoStep1Model().getProvvedimento());
		//MARCHIAMO CHE C'E' UN PROVVEDIMENTO SELEZIONATO
		model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(true);			
	}
			
			
			
	protected void caricaDatiSoggettoOrdinativo(){	
		
		//richiamo il metodo centralizzato:
		caricaDatiSoggettoOrdinativo(model.getGestioneOrdinativoStep1Model().getSoggetto(),soggettoOrdinativo, classeOrdinativo);
		//
		
		//imposto il flag selezionato a true:
		model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(true);
		
	}
			
	protected void caricaDatiCapitolo(){
		//metodo core:
		caricaDatiCapitolo(capitoloUscitaTrovato, capitoloEntrataTrovato, model.getGestioneOrdinativoStep1Model().getCapitolo());
		
		//seleziono il capitolo per la visualizazzione dei dettagli
		setCapitoloSelezionato(model.getGestioneOrdinativoStep1Model().getCapitolo());
	}	
	
	
	/**
	 * azione del btn seleziona provvisorio 
	 * @return
	 */
	protected String selezionaProvvisorio(String uidPDaRicerca){
			
			debug("selezionaProvvisorio", "seleziono il "+uidPDaRicerca);
			
			if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaProvvisori())){
				
				for (ProvvisorioDiCassa iterProvv : model.getGestioneOrdinativoStep3Model().getListaProvvisori()) {
					
					if(iterProvv.getUid() == Integer.valueOf(uidPDaRicerca)){
						
						debug("selezionaProvvisorio", "trovato "+uidPDaRicerca);
						
						calcolaTotaliFooter();
						
						ProvvisorioDiCassa pdc = iterProvv;
						// setto l'importo formato stringa
						if(pdc.getImporto()!=null){
							
							BigDecimal minimoBD = FinUtility.minTraBD(pdc.getImportoDaRegolarizzare(), model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare());
							
							pdc.setImportoFormatString(convertiBigDecimalToImporto(minimoBD));
						}
						
						model.getGestioneOrdinativoStep3Model().setProvvisorio(clone(iterProvv));
						break;
					}
					
				}
			}
			
			
			return "aggiornaRegolaProvv";
	}
	
	public String annullaInserimentoQuota(){
		if(oggettoDaPopolarePagamento()){
			//ORDINATIVO PAGAMENTO
			if(isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti())){
				//se e' vuota
				pulisciTransazioneElementare();
				teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO.toString());
				caricaListeBil(WebAppConstants.CAP_UG);
			}
		}else{
			//ORDINATIVO INCASSO
			if(isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso())){
				//se e' vuota
				pulisciTransazioneElementare();
				teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO.toString());
				caricaListeBil(WebAppConstants.CAP_EG);
			}
		}
		return "refreshTE";
	}
	
	
	
	protected void calcolaTotaliFooter(){
		BigDecimal totPagato =  BigDecimal.ZERO;
		
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for(ProvvisorioDiCassa itProvv : model.getGestioneOrdinativoStep3Model().getListaCoperture()){
				totPagato = totPagato.add(itProvv.getImporto());
			}
			
		}
		model.getGestioneOrdinativoStep3Model().setTotalizzatorePagato(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote());
		model.getGestioneOrdinativoStep3Model().setTotalizzatoreOrdinativo(totPagato);
		// da collegare
		model.getGestioneOrdinativoStep3Model().setTotalizzatoreDaCollegare(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote().subtract(model.getGestioneOrdinativoStep3Model().getTotalizzatoreOrdinativo()));

	}
	
	/**
	 * Si occupa di interpretare il risultato di un'invocazione di ricerca ord per chiave service
	 * rispetto al soggetto alla sede e alla modalita' di pagamento
	 * @param response
	 * @return
	 */
	protected SoggSedeModPagOrdinativoDto leggiDatiSoggSedeModPag(RicercaOrdinativoPagamentoPerChiaveResponse response){
		SoggSedeModPagOrdinativoDto info = new SoggSedeModPagOrdinativoDto();
		//RICHIAMO IL METODO CENTRALIZZATO:
		SoggettoSedeModPagInfo infoSsm = EntitaUtils.modalitaPagamentoOrdPag(response.getOrdinativoPagamento());
		//SETTO I DATI:
		info.setModPagSelezionata(infoSsm.getModalitaPagamento());
		info.setSedeSecondariaSelezionata(infoSsm.getSedeSecondaria());
		info.setSoggettoCreditore(infoSsm.getSoggetto());
		return info;
	}
	
	protected RicercaOrdinativoPagamentoPerChiaveResponse caricaDatiOrdinativo(){
	
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setAnno(Integer.parseInt(model.getAnnoOrdinativoInAggiornamento()));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setNumero(Integer.parseInt(model.getNumeroOrdinativoInAggiornamento()));
		
		RicercaOrdinativoPagamentoPerChiaveResponse response= new RicercaOrdinativoPagamentoPerChiaveResponse();
		if(model.isRicaricaDopoAggiornamento()){
			response.setOrdinativoPagamento(model.getOrdinativoPagamentoRicaricatoDopoInsOAgg());
			model.setRicaricaDopoAggiornamento(false);
			model.setOrdinativoPagamentoRicaricatoDopoInsOAgg(null);
		}else{
		
			response= ordinativoService.ricercaOrdinativoPagamentoPerChiave(convertiModelPerChiamataServizioRicercaOrdinativoPerChiave());
		}
		
		if(response.getOrdinativoPagamento() != null){
			model.getGestioneOrdinativoStep1Model().setOrdinativo(response.getOrdinativoPagamento());
			
			//Controllo i 3 flag
			if(response.getOrdinativoPagamento().isFlagAllegatoCartaceo()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(true);
			}	
			
			if(response.getOrdinativoPagamento().isFlagBeneficiMultiplo()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(true);
			}
			if(response.getOrdinativoPagamento().isFlagCopertura()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(true);
			}
			
			if(response.getOrdinativoPagamento().isDaTrasmettere()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagDaTrasmettere(true);
			}
			
			
			setProvvedimentoOrdinativo(response.getOrdinativoPagamento().getAttoAmministrativo());
			setSoggettoOrdinativo(response.getOrdinativoPagamento().getSoggetto());
			caricaDatiSoggettoOrdinativo();
			setCapitoloUscitaTrovato(response.getOrdinativoPagamento().getCapitoloUscitaGestione());
			
			caricaDatiCapitolo();
			caricaDatiProvvedimento();

			//Carico le lista delle sedi e mod pag
			caricaListeCreditore(response.getOrdinativoPagamento().getSoggetto().getCodiceSoggetto());
			
			SoggSedeModPagOrdinativoDto infoModPag = leggiDatiSoggSedeModPag(response);
			   
			if(infoModPag.getModPagSelezionata()!= null){
			    model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(infoModPag.getModPagSelezionata().getUid());
			    model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(infoModPag.getModPagSelezionata());
			 }
			   
			 if(infoModPag.getSedeSecondariaSelezionata()!= null){
			    model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(infoModPag.getSedeSecondariaSelezionata().getUid());
			    model.getGestioneOrdinativoStep1Model().setSedeSelezionata(infoModPag.getSedeSecondariaSelezionata());
			 }
			
			// Se esistono, setto le quote ottenute
			if(response.getOrdinativoPagamento().getElencoSubOrdinativiDiPagamento()!= null){
				model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiPagamenti(response.getOrdinativoPagamento().getElencoSubOrdinativiDiPagamento());
				
				if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti())){
					
					Collections.sort(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti(), new Comparator<SubOrdinativoPagamento>() {

						@Override
						public int compare(SubOrdinativoPagamento o1,
								SubOrdinativoPagamento o2) {
							return o1.getNumero().compareTo(o2.getNumero());
						}
						
					});
				}
			}
			
			List<ProvvisorioDiCassa> cop = new ArrayList<ProvvisorioDiCassa>();

			if(!isEmpty(response.getOrdinativoPagamento().getElencoRegolarizzazioneProvvisori())){
				
				for (RegolarizzazioneProvvisorio rp : response.getOrdinativoPagamento().getElencoRegolarizzazioneProvvisori()) {
					
					ProvvisorioDiCassa prov = rp.getProvvisorioDiCassa();
					prov.setIdRegolarizzazione(rp.getIdRegolarizzazioneProvvisorio());
					prov.setImporto(rp.getImporto());
					if(null!=rp.getImporto()){
						prov.setImportoFormatString(convertiBigDecimalToImporto(rp.getImporto()));
					}
					// popolo la lista di provvisori
					cop.add(prov);
					
				}
			}
			// setto il valore nello step 3
			model.getGestioneOrdinativoStep3Model().setListaCoperture(cop);
			
			
			// pulisco la transazione 
			pulisciTransazioneElementare();
			
			// RICARICO ORDINATIVO_TIPO_PAGAMENTO
			// Jira - 1357 in caso di errore di caricamento dei dati
			// dei classificatori non viene segnalato alcun errore
			// ma carica la pagina, al massimo non verranno visualizzate le combo relative
			caricaListeFinOrdinativo(TIPO_ORDINATIVO_PAGAMENTO_P);
				
			
			if(response.getOrdinativoPagamento().getCodMissione()!=null){
				teSupport.setMissioneSelezionata(response.getOrdinativoPagamento().getCodMissione());
			}
			
			if(response.getOrdinativoPagamento().getCodProgramma()!=null){
				teSupport.setProgrammaSelezionato(response.getOrdinativoPagamento().getCodProgramma());
			}

			// MACRO AGGREGATO
			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getIdMacroAggregato()!=null){
				
				teSupport.setIdMacroAggregato(model.getGestioneOrdinativoStep1Model().getCapitolo().getIdMacroAggregato());
				
			}
			
			//albero PDC
			if(response.getOrdinativoPagamento().getCodPdc()!=null){
				
				teSupport.getPianoDeiConti().setCodice(response.getOrdinativoPagamento().getCodPdc());
			}
			
			if(response.getOrdinativoPagamento().getDescPdc() != null){
				teSupport.getPianoDeiConti().setDescrizione(response.getOrdinativoPagamento().getDescPdc());
			}
			
			if(response.getOrdinativoPagamento().getIdPdc() != null){
				teSupport.getPianoDeiConti().setUid(response.getOrdinativoPagamento().getIdPdc());
			}
			
			if(response.getOrdinativoPagamento().getCodCofog()!=null){
				teSupport.setCofogSelezionato(response.getOrdinativoPagamento().getCodCofog());
			}
			
			if(response.getOrdinativoPagamento().getCodTransazioneEuropeaSpesa()!=null){
				teSupport.setTransazioneEuropeaSelezionato(response.getOrdinativoPagamento().getCodTransazioneEuropeaSpesa());
			}
			
			if(response.getOrdinativoPagamento().getCodRicorrenteSpesa()!=null){
				teSupport.setRicorrenteSpesaSelezionato(response.getOrdinativoPagamento().getCodRicorrenteSpesa());
			}
			
			if(response.getOrdinativoPagamento().getCodCapitoloSanitarioSpesa()!=null){
				teSupport.setPerimetroSanitarioSpesaSelezionato(response.getOrdinativoPagamento().getCodCapitoloSanitarioSpesa());
			}
			
			if(response.getOrdinativoPagamento().getCodPrgPolReg()!=null){
				teSupport.setPoliticaRegionaleSelezionato(response.getOrdinativoPagamento().getCodPrgPolReg());
			}
			
			if(response.getOrdinativoPagamento().getCodClassGen11()!=null){
				teSupport.setClassGenSelezionato1(response.getOrdinativoPagamento().getCodClassGen11());
			}
			
			if(response.getOrdinativoPagamento().getCodClassGen12()!=null){
				teSupport.setClassGenSelezionato2(response.getOrdinativoPagamento().getCodClassGen12());
			}
			
			if(response.getOrdinativoPagamento().getCodClassGen13()!=null){
				teSupport.setClassGenSelezionato3(response.getOrdinativoPagamento().getCodClassGen13());
			}
			
			if(response.getOrdinativoPagamento().getCodClassGen14()!=null){
				teSupport.setClassGenSelezionato4(response.getOrdinativoPagamento().getCodClassGen14());
			}
			
			if(response.getOrdinativoPagamento().getCodClassGen15()!=null){
				teSupport.setClassGenSelezionato5(response.getOrdinativoPagamento().getCodClassGen15());
			}
			
			if(response.getOrdinativoPagamento().getClassificatoreStipendi()!=null){
				teSupport.setUidClassStipendiSelezionato(response.getOrdinativoPagamento().getClassificatoreStipendi().getUid());
			}
			
			// Copio la mia transazione elementare dsa poter utilizzare poi in seconda e terza pagina
			model.setTransazioneElementareOrdinativoCache(clonaTransazioneElementare());
			
			// CR SIAC-2843
			// scorro i collegati per calcolare il totale importo
			OrdinativoPagamento op = response.getOrdinativoPagamento();
			// jira-3927: manca il controllo, per l'attivazione del bottone inserisci, sullo stato dell'ordinativo di pagamento:
			// si attiva solo con lo stato INSERITO
			// se è legato a Documento deve essere un ALG 
			
			boolean statoOrdinativoInserito = op.getCodStatoOperativoOrdinativo().equalsIgnoreCase(Constanti.statoOperativoOrdinativoEnumToString(op.getStatoOperativoOrdinativo()));
			boolean senzaDocOTipoDocumentoALG = true; 
			
			//SOMMA E SETTA LA SOMMA DEGLI ORDINATIVI COLLEGATI:
			sommaOrdinativiCollegati(op);
			//
						
			model.getGestioneOrdinativoStep1Model().setOrdinativo(response.getOrdinativoPagamento());
			
			// controllo il tipo di associazione 
			
			
			/*
			 * 
			 * 
			//SETTEMBRE 2017 SIAC-5282 in seguito a nuove regole  questo codice non dovrebbe piu' servire
			//per ora lo tengo committato quando poi e' confermata che funziona come voluto 
			//verra' rimosso
			
			if(op.getElencoSubOrdinativiDiPagamento()!=null && !op.getElencoSubOrdinativiDiPagamento().isEmpty()){
				for (SubOrdinativoPagamento subop : op.getElencoSubOrdinativiDiPagamento()) {
					if(subop.getSubDocumentoSpesa()!=null && subop.getSubDocumentoSpesa().getDocumento()!=null){
						if(!subop.getSubDocumentoSpesa().getDocumento().getTipoDocumento().getCodice().equals("ALG")){
							senzaDocOTipoDocumentoALG = false;
							break;
						}
					}
				}
			}
			*/
			
			//settiamo l'abilitazione a collegare nuovi ordinativi:
			if(statoOrdinativoInserito && senzaDocOTipoDocumentoALG ){
				model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(true);
			} else {
				model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(false);		
			}
					
			
		}
		model.setForceReloadAgiornamentoOrdinativo(false);
		return response;	
	}
	
	/**
	 * Somma e setta la somma degli ordinativi collegati
	 * @param op
	 */
	private void sommaOrdinativiCollegati(Ordinativo op){
		if(op.getElencoOrdinativiCollegati()!=null && !op.getElencoOrdinativiCollegati().isEmpty()){
			BigDecimal calcoloImportoCollegati = BigDecimal.ZERO;
			for (Ordinativo oi : op.getElencoOrdinativiCollegati()) {
				calcoloImportoCollegati = calcoloImportoCollegati.add(oi.getImportoOrdinativo());
				model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(calcoloImportoCollegati);
			}
		}
	}
	
	/**
	 * SETTEMBRE 2017 SIAC-5282 --> regole per nascondere l'azione 
	 * elimina nella tabella degli ordinativi di incasso collegati ad un ordinativo di pagamento
	 * all'interno del caso d'uso aggiorna ordinativo di pagamento
	 * @param impUid
	 * @return
	 */
	public boolean isAbilitatoEliminaCollegamentoOrdinativo(Integer uid){
		
		boolean abilitato = false;
		
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo()!=null){
			//ordinativo diverso da null
			
			StatoOperativoOrdinativo statoOperativo = model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo();
			if(!statoOperativo.equals(StatoOperativoOrdinativo.INSERITO)){
				//per stato diverso da INSERITO (ovvero per TRASMESSO, QUIETANZATO o FIRMATO)
				//il bottone elimina e' sempre disabilitato
				return false;
			}
			
			if(!ordPagHaDoc()){
				//siamo nel caso di sub ordinativo senza doc
				return true;
			}
			
			//lista ordinativi incasso in datatable:
			List<Ordinativo> ordinatiCollegati = model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati();
			
			//cerco l'ordinativo di incasso iternato:
			Ordinativo ordinativoIterato = FinUtility.getById(ordinatiCollegati, uid);
			
			if(ordinativoIterato!=null && TipoAssociazioneEmissione.SUB_ORD.equals(ordinativoIterato.getTipoAssociazioneEmissione())){
				//caso di ordinativo con doc --> elimina abilitato 
				//solo per le relazioni di tipo SUB-SUBORDINATO
				return true;
			}
			
		}
		
		//ritorno l'esisto:
		return abilitato;
	}
	
	/**
	 * per gli ordinativi di pagamento visti dalla maschera di inserimento/aggiorna ordinativo di incasso
	 * @param uid
	 * @return
	 */
	public boolean isAbilitatoEliminaCollegamentoOrdinativoPagamento(Integer uid){
		boolean abilitato = true;
		//ritorno l'esisto:
		return abilitato;
	}
	
	/**
	 * ritorna true se l'ordinativo di pagamento ha collegamenti con documenti di spesa
	 * false altrimenti
	 * @return
	 */
	private boolean ordPagHaDoc(){
		boolean ordPagHaDoc = false;
		//leggo l'ordinativo dal model:
		OrdinativoPagamento ordPag = model.getGestioneOrdinativoStep1Model().getOrdinativo();
		if(ordPag!=null){
			//ok e' diverso da null
			if(!isEmpty(ordPag.getElencoSubOrdinativiDiPagamento())){
				//ciclo sui subordinativi
				for (SubOrdinativoPagamento subop : ordPag.getElencoSubOrdinativiDiPagamento()) {
					if(subop.getSubDocumentoSpesa()!=null && subop.getSubDocumentoSpesa().getDocumento()!=null){
						//trovao un sub ordinativo collegato ad un sub documento di spesa
						ordPagHaDoc = true;
						break;
					}
				}
			}
		}
		//ritorno l'esito:
		return ordPagHaDoc;
	}
	
	protected RicercaOrdinativoIncassoPerChiaveResponse caricaOrdinativoIncassoPerRipeti(String anno, String numero){
		
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setAnno(Integer.parseInt(anno));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setNumero(Integer.parseInt(numero));
		
		RicercaOrdinativoIncassoPerChiave req = convertiModelPerChiamataServizioRicercaOrdinativoIncassoPerChiave();
		RicercaOrdinativoIncassoPerChiaveResponse response= ordinativoService.ricercaOrdinativoIncassoPerChiave(req );
		
		if(!isFallimento(response)){
			OrdinativoIncasso ordinativoCaricato = response.getOrdinativoIncasso();
			if(ordinativoCaricato!=null){
				
				
				//1. Capitolo
				setCapitoloEntrataTrovato(ordinativoCaricato.getCapitoloEntrataGestione());
				caricaDatiCapitolo();
				
				//2. Soggetto con eventuale sede secondaria
				setSoggettoOrdinativo(ordinativoCaricato.getSoggetto());
				caricaDatiSoggettoOrdinativo();
				
				//3. Accertamento 
				//   SE esiste solo una quota ordinativo --> Anno, numero Accertamento e eventuale subaccertamento
				//   SE ci sono piu' quote il campo Accertamento rimane vuoto
				impostaDefaultAccertamentoPerRipetiOrdinativoIncasso(ordinativoCaricato);
				
				//	Descrizione
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setDescrizione(ordinativoCaricato.getDescrizione());
				
				//	Provvedimento
				setProvvedimentoOrdinativo(response.getOrdinativoIncasso().getAttoAmministrativo());
				caricaDatiProvvedimento();
			}
		}
	
		//Li riazzero, servivano solo per pilotare alcuni popolamenti presi dell'aggiorna:
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setAnno(null);
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setNumero(null);
		
		return response;
	}
	
	private void impostaDefaultAccertamentoPerRipetiOrdinativoIncasso(OrdinativoIncasso ordinativoCaricato){
		List<SubOrdinativoIncasso> elencoSubOrdinativi = ordinativoCaricato.getElencoSubOrdinativiDiIncasso();
		if(!isEmpty(elencoSubOrdinativi) && elencoSubOrdinativi.size()==1){
			//UNA SOLA QUOTA
			SubOrdinativoIncasso quotaUnica = FinUtility.getFirst(elencoSubOrdinativi);
			if(quotaUnica!=null){
				Accertamento accertamento = quotaUnica.getAccertamento();
				if(accertamento!=null){
					int annoMovimento = accertamento.getAnnoMovimento();
					BigDecimal numeroMovimento = accertamento.getNumero();
					if(annoMovimento >0 && numeroMovimento !=null){
						model.getGestioneOrdinativoStep1Model().setAnnoAccertamento(annoMovimento);
						model.getGestioneOrdinativoStep1Model().setNumeroAccertamento(numeroMovimento.toBigInteger());
						SubAccertamento sub = FinUtility.getFirst(accertamento.getElencoSubAccertamenti());
						if(sub!=null){
							BigDecimal numeroSub = sub.getNumero();
							if(numeroSub!=null){
								model.getGestioneOrdinativoStep1Model().setNumeroSubAcc(numeroSub.toBigInteger());
							}
						}
					}
				}
			}
		}
	}
	
	protected RicercaOrdinativoIncassoPerChiaveResponse caricaOrdinativoIncasso(){

		model.getGestioneOrdinativoStep1Model().getOrdinativo().setAnno(Integer.parseInt(model.getAnnoOrdinativoInAggiornamento()));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setNumero(Integer.parseInt(model.getNumeroOrdinativoInAggiornamento()));
		
		RicercaOrdinativoIncassoPerChiaveResponse response= new RicercaOrdinativoIncassoPerChiaveResponse();
		if(model.isRicaricaDopoAggiornamento()){
			
			response.setOrdinativoIncasso(model.getOrdinativoIncassoRicaricatoDopoInsOAgg());
			model.setRicaricaDopoAggiornamento(false);
			model.setOrdinativoIncassoRicaricatoDopoInsOAgg(null);
			
		}else{
		
			response = ordinativoService.ricercaOrdinativoIncassoPerChiave(convertiModelPerChiamataServizioRicercaOrdinativoIncassoPerChiave());
		}
		
		
		
		if(response.getOrdinativoIncasso() != null){
			model.getGestioneOrdinativoStep1Model().setOrdinativo(travasaOrdinativoIncToPag(response.getOrdinativoIncasso()));
			

			//Controllo i 3 flag
			if(response.getOrdinativoIncasso().isFlagAllegatoCartaceo()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(true);
			}	
			
			if(response.getOrdinativoIncasso().isFlagBeneficiMultiplo()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(true);
			}
			if(response.getOrdinativoIncasso().isFlagCopertura()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(true);
			}
			
			if(response.getOrdinativoIncasso().isDaTrasmettere()){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagDaTrasmettere(true);
			}
			
			setProvvedimentoOrdinativo(response.getOrdinativoIncasso().getAttoAmministrativo());
			setSoggettoOrdinativo(response.getOrdinativoIncasso().getSoggetto());
			caricaDatiSoggettoOrdinativo();
			setCapitoloEntrataTrovato(response.getOrdinativoIncasso().getCapitoloEntrataGestione());

			caricaDatiCapitolo();
			caricaDatiProvvedimento();

			//Carico le lista delle sedi e mod pag
			caricaListeCreditore(response.getOrdinativoIncasso().getSoggetto().getCodiceSoggetto());
			   
			if(!isEmpty(response.getOrdinativoIncasso().getSoggetto().getSediSecondarie()) && response.getOrdinativoIncasso().getSoggetto().getSediSecondarie().get(0)!= null){
		    	model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(response.getOrdinativoIncasso().getSoggetto().getSediSecondarie().get(0).getUid());
		    	model.getGestioneOrdinativoStep1Model().setSedeSelezionata(response.getOrdinativoIncasso().getSoggetto().getSediSecondarie().get(0));
		    }
			
			// Se esistono, setto le quote ottenute
			if(response.getOrdinativoIncasso().getElencoSubOrdinativiDiIncasso()!= null){
				
				for(SubOrdinativoIncasso sub: response.getOrdinativoIncasso().getElencoSubOrdinativiDiIncasso()){
					
					if(!isEmpty(sub.getAccertamento().getElencoSubAccertamenti())){
						
						Integer tempAnno= sub.getAccertamento().getAnnoMovimento();
						BigDecimal numeroAnno= sub.getAccertamento().getNumero();
						sub.getAccertamento().getElencoSubAccertamenti().get(0).setAnnoAccertamentoPadre(tempAnno);
						sub.getAccertamento().getElencoSubAccertamenti().get(0).setNumeroAccertamentoPadre(numeroAnno);
						sub.setAccertamento(sub.getAccertamento().getElencoSubAccertamenti().get(0));
						
					}
				}
				model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiIncasso(response.getOrdinativoIncasso().getElencoSubOrdinativiDiIncasso());
				
				if(!isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso())){
					
					Collections.sort(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso(), new Comparator<SubOrdinativoIncasso>() {

						@Override
						public int compare(SubOrdinativoIncasso o1,
								SubOrdinativoIncasso o2) {
							
							
							return o1.getNumero().compareTo(o2.getNumero());
						}
						
					});
				}
				
				
			}
			
			
			List<ProvvisorioDiCassa> cop = new ArrayList<ProvvisorioDiCassa>();

			if(!isEmpty(response.getOrdinativoIncasso().getElencoRegolarizzazioneProvvisori())){
				
				for (RegolarizzazioneProvvisorio rp : response.getOrdinativoIncasso().getElencoRegolarizzazioneProvvisori()) {
					
					ProvvisorioDiCassa prov = rp.getProvvisorioDiCassa();
					prov.setImporto(rp.getImporto());
					prov.setIdRegolarizzazione(rp.getIdRegolarizzazioneProvvisorio());
					if(null!=rp.getImporto()){
						prov.setImportoFormatString(convertiBigDecimalToImporto(rp.getImporto()));
					}
					// popolo la lista di provvisori
					cop.add(prov);
					
				}
			}
			// setto il valore nello step 3
			model.getGestioneOrdinativoStep3Model().setListaCoperture(cop);
			
			// pulisco la transazione 
			pulisciTransazioneElementare();
			
			// RICARICO ORDINATIVO_TIPO_PAGAMENTO
			// Jira - 1357 in caso di errore di caricamento dei dati
			// dei classificatori non viene segnalato alcun errore
			// ma carica la pagina, al massimo non verranno visualizzate le combo relative
			caricaListeFinOrdinativo(TIPO_ORDINATIVO_INCASSO_I);
			
			if(response.getOrdinativoIncasso().getCodMissione()!=null){
				teSupport.setMissioneSelezionata(response.getOrdinativoIncasso().getCodMissione());
			}
			
			if(response.getOrdinativoIncasso().getCodProgramma()!=null){
				teSupport.setProgrammaSelezionato(response.getOrdinativoIncasso().getCodProgramma());
			}

			// MACRO AGGREGATO
			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getIdMacroAggregato()!=null){
				teSupport.setIdMacroAggregato(model.getGestioneOrdinativoStep1Model().getCapitolo().getIdMacroAggregato());
			}
			
			//albero PDC
			if(response.getOrdinativoIncasso().getCodPdc()!=null){
				teSupport.getPianoDeiConti().setCodice(response.getOrdinativoIncasso().getCodPdc());
			}
			
			if(response.getOrdinativoIncasso().getDescPdc() != null){
				teSupport.getPianoDeiConti().setDescrizione(response.getOrdinativoIncasso().getDescPdc());
			}
			
			if(response.getOrdinativoIncasso().getIdPdc() != null){
				teSupport.getPianoDeiConti().setUid(response.getOrdinativoIncasso().getIdPdc());
			}
			
			if(response.getOrdinativoIncasso().getCodCofog()!=null){
				teSupport.setCofogSelezionato(response.getOrdinativoIncasso().getCodCofog());
			}
			
			if(response.getOrdinativoIncasso().getCodTransazioneEuropeaSpesa()!=null){
				teSupport.setTransazioneEuropeaSelezionato(response.getOrdinativoIncasso().getCodTransazioneEuropeaSpesa());
			}
			
			if(response.getOrdinativoIncasso().getCodRicorrenteSpesa()!=null){
				teSupport.setRicorrenteEntrataSelezionato(response.getOrdinativoIncasso().getCodRicorrenteSpesa());
			}
			
			if(response.getOrdinativoIncasso().getCodCapitoloSanitarioSpesa()!=null){
				teSupport.setPerimetroSanitarioEntrataSelezionato(response.getOrdinativoIncasso().getCodCapitoloSanitarioSpesa());
			}
			//TODO   quali classificatori carico??!?
			if(response.getOrdinativoIncasso().getCodPrgPolReg()!=null){
				teSupport.setPoliticaRegionaleSelezionato(response.getOrdinativoIncasso().getCodPrgPolReg());
			}
			
			if(response.getOrdinativoIncasso().getCodClassGen16()!=null){
				teSupport.setClassGenSelezionato1(response.getOrdinativoIncasso().getCodClassGen16());
			}
			
			if(response.getOrdinativoIncasso().getCodClassGen17()!=null){
				teSupport.setClassGenSelezionato2(response.getOrdinativoIncasso().getCodClassGen17());
			}
			
			if(response.getOrdinativoIncasso().getCodClassGen18()!=null){
				teSupport.setClassGenSelezionato3(response.getOrdinativoIncasso().getCodClassGen18());
			}
			
			if(response.getOrdinativoIncasso().getCodClassGen19()!=null){
				teSupport.setClassGenSelezionato4(response.getOrdinativoIncasso().getCodClassGen19());
			}
			
			if(response.getOrdinativoIncasso().getCodClassGen20()!=null){
				teSupport.setClassGenSelezionato5(response.getOrdinativoIncasso().getCodClassGen20());
			}
			
			if(response.getOrdinativoIncasso().getClassificatoreStipendi()!=null){
				teSupport.setUidClassStipendiSelezionato(response.getOrdinativoIncasso().getClassificatoreStipendi().getUid());
			}
			
			//Copio la mia transazione elementare dsa poter utilizzare poi in seconda e terza pagina
			model.setTransazioneElementareOrdinativoCache(clonaTransazioneElementare());
			
			//setto la causale nel model:
			impostaCausaleEntrataNelModel(response.getOrdinativoIncasso());
			
			//SOMMA E SETTA LA SOMMA DEGLI ORDINATIVI COLLEGATI:
			sommaOrdinativiCollegati(response.getOrdinativoIncasso());
			//
			
			//IMPOSTO A TRUE LA POSSIBILITA' DI COLLEGARE ORDINATIVI:
			//(se necessario implementare qui il set a false in caso non sia consentito)
			model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(true);
			//
			
			//SE CI SONO ORDINATIVI COLLEGATI, IMPOSTO IL FLAG PER REINTROITI:
			if(!isEmpty(response.getOrdinativoIncasso().getElencoOrdinativiCollegati())){
				model.getGestioneOrdinativoStep1Model().setFlagPerReintroiti(true);
				model.getGestioneOrdinativoStep1Model().setFlagPerReintroitiOrdinativoInAggiornamento(true);
			}
			//
			
		}
		model.setForceReloadAgiornamentoOrdinativoIncasso(false);
		return response;
	}
	
	public String aggiornaAvviso(){
		return "aggiornaAvviso";
	}
	
	protected void caricaListeCreditore(String codiceSoggetto){	
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(convertiModelPerChiamataServizioRicercaPerChiave(codiceSoggetto));
		if(response!=null){
			List<ModalitaPagamentoSoggetto> listMod= new ArrayList<ModalitaPagamentoSoggetto>();
			if(oggettoDaPopolarePagamento()){
				 listMod= FinUtility.filtraValidi(response.getListaModalitaPagamentoSoggetto());
				 listMod = FinUtility.soloMDPNonScadute(listMod);
			}
			
			List<SedeSecondariaSoggetto> listSedi= new ArrayList<SedeSecondariaSoggetto>();
			if(response.getListaSecondariaSoggetto()!= null){
				for(SedeSecondariaSoggetto sedeSec:response.getListaSecondariaSoggetto()){
					if(sedeSec.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(Constanti.STATO_VALIDO) ){
							listSedi.add(sedeSec);
					}
				}
			}
			
			// msg da inserire nel popover
			if(response.getSoggetto()!=null){
				if(response.getSoggetto().isAvviso()){
					
					model.getGestioneOrdinativoStep1Model().setAvvisoFrase(WebAppConstants.MSG_SI);
				}else {
					model.getGestioneOrdinativoStep1Model().setAvvisoFrase(WebAppConstants.MSG_NO);
				}
				
				
			}
			if(oggettoDaPopolarePagamento()){
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamento(listMod);
				model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(listMod);
				model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(null);
				model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(0);
			}
			model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(listSedi);
			model.getGestioneOrdinativoStep1Model().setListaSediSecondarieVisualizza(listSedi);
		}
	}
	
	protected RicercaSoggettoPerChiave convertiModelPerChiamataServizioRicercaPerChiave(String codiceSoggetto) {
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		ricercaSoggettoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggettoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		parametroRicercaSoggettoK.setIncludeModif(false);
		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroRicercaSoggettoK);
		return ricercaSoggettoPerChiave;
	}
	
	private AggiornaOrdinativoPagamento composizioneAggiorna(){
		AggiornaOrdinativoPagamento request = new AggiornaOrdinativoPagamento();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		request.setOrdinativoPagamento(model.getGestioneOrdinativoStep1Model().getOrdinativo());
		if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
			request.getOrdinativoPagamento().setCapitoloUscitaGestione(convertiCapitoloCustomToCapitolo(model.getGestioneOrdinativoStep1Model().getCapitolo()));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
			request.getOrdinativoPagamento().setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
		}
		if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {
			request.getOrdinativoPagamento().setSoggetto(convertiSoggettoCustomToSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto()));
			request.getOrdinativoPagamento().getSoggetto().setElencoModalitaPagamento(new ArrayList<ModalitaPagamentoSoggetto>());
			request.getOrdinativoPagamento().getSoggetto().getElencoModalitaPagamento().add(model.getGestioneOrdinativoStep1Model().getModpagamentoSelezionata());
		}
		
		// setto i flag
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo()!=null){
		    
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagAllegatoCartaceo()){
				request.getOrdinativoPagamento().setFlagAllegatoCartaceo(true);
			}
			
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagBeneficiMultiplo()){
				request.getOrdinativoPagamento().setFlagBeneficiMultiplo(true);
			}
			
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
				request.getOrdinativoPagamento().setFlagCopertura(true);
			}
			
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isDaTrasmettere()){
				request.getOrdinativoPagamento().setFlagDaTrasmettere(true);
			}
			
		}
		
		List<RegolarizzazioneProvvisorio> rp = new ArrayList<RegolarizzazioneProvvisorio>();
		//Converto i provvisori in regolarizzatori 
		if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
			
			for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
				
				RegolarizzazioneProvvisorio rego = new RegolarizzazioneProvvisorio();
				rego.setIdRegolarizzazioneProvvisorio(pc.getIdRegolarizzazione());
				rego.setImporto(pc.getImporto());
				rego.setProvvisorioDiCassa(pc);
				
				// popolo la lista di regolarizzazione
				rp.add(rego);
				
			}
		}
		//Carico i regolarizzatori
		request.getOrdinativoPagamento().setElencoRegolarizzazioneProvvisori(rp);
		
		//QUOTE
		request.getOrdinativoPagamento().setElencoSubOrdinativiDiPagamento(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti());
		
		// copio i dati di transazione ELEMENTARE
		request.setOrdinativoPagamento(copiaTE(request.getOrdinativoPagamento()));
		
		impostaClassificatoreStipendi(model.getGestioneOrdinativoStep2Model(), request.getOrdinativoPagamento());
		
		return request;
	}
	
	protected String aggiornaOrdinativo(){
		
		
		if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_insMan)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		
		if(controlloStatoBilancio(Integer.parseInt(sessionHandler.getAnnoEsercizio()),"Gestione","Ordinativo")){
			return INPUT;
		}
		
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo()== null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() == null || "".equals(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Creditore"));
		}
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Ordinativo"));
		}else if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione().length()<3){
			listaErrori.add(ErroreFin.CAMPO_LUNGHEZZA_MINIMA_NON_RISPETTATA.getErrore("DESCRIZIONE","tre"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
			
		}else if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento()<=1900){
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Anno provvedimento"," > 1900"));
		}
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			
		}else if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento().intValue()<=0){
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Numero provvedimento"," > 0"));
		}
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Bollo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Commissione"));
		}
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Tesoriere"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione()!= null && model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Liquidazione"));
		}else if(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione()== null && model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()!= null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Liquidazione"));

		}
		
		// radio button su modalita pagamento
		if(model.getGestioneOrdinativoStep1Model().isSoggettoSelezionato() &&  model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()==0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modalita' pagamento"));
			
		}else{
			for(ModalitaPagamentoSoggetto modPag : model.getGestioneOrdinativoStep1Model().getListaModalitaPagamentoVisualizza()){
				if(modPag.getUid()==model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()){
					if(modPag.getTipoAccredito()!= null && modPag.getTipoAccredito().equals(TipoAccredito.CSC)){
						listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
					}
				}
			}
		}
		
		
		if(listaErrori.isEmpty()){
			
			if(!controllaCongruenzaCopertura()){

				addActionError(ERRORE_CONGRUENZA_COPERTURE);
				return INPUT;

			}

			AggiornaOrdinativoPagamento request= composizioneAggiorna();
			
			AggiornaOrdinativoPagamentoResponse response= ordinativoService.aggiornaOrdinativoPagamento(request);
			if(response.isFallimento()){
				addErrori(response.getErrori());
				return INPUT;
			}
			
//			/*********************PARTE PER EVITARE LA MEGA QUERY ****************/
		
			if(response.getOrdinativoPagamentoAggiornato()!= null){
				model.setOrdinativoPagamentoRicaricatoDopoInsOAgg(response.getOrdinativoPagamentoAggiornato());
				model.setRicaricaDopoAggiornamento(true);
			}
//			/********************* *****************/
			
			
			
			
			
			// vai sulla pagina di consulta

			setNumeroOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoAggiornato().getNumero()));
			setAnnoOrdinativoStruts(String.valueOf(response.getOrdinativoPagamentoAggiornato().getAnno()));
			
			// messaggio di ok nella pagina consulta
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
					                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			// pulisco i dati di sessione
			model.setSonoInAggiornamento(true);
			model.setForceReloadAgiornamentoOrdinativo(true);
			
			return RETURN_AGGIORNA;
		}else{
			addErrori(listaErrori);
			return INPUT;
		}
		
	}

	protected boolean controllaCongruenzaCopertura() {
		if(controlloQuote()){
			
			BigDecimal totaleCoperture = BigDecimal.ZERO;
			if(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote()!=null){
				
				if(model.getGestioneOrdinativoStep3Model().getListaCoperture()!=null){
					
					for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
						
						totaleCoperture = totaleCoperture.add(pc.getImporto());
						
					}
					
				}
				
				if(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote().compareTo(totaleCoperture)!=0){
					// verifico che le somme siano uguali

					return false;
				}
				
			}
			
		}
		
		return true;
	}
	
	public boolean controlloQuote(){
		boolean controllo=false;
		if(model.isSonoInAggiornamentoIncasso()){
			controllo = model.getGestioneOrdinativoStep1Model().getOrdinativo()!=null
					&& model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura() 
					&& !isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso());
		}else{
			controllo = model.getGestioneOrdinativoStep1Model().getOrdinativo() != null
					&& model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()
					&& !isEmpty(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti());
		}
		return controllo;
	}
	
	public boolean checkDisabilitaDescOrd(){
		if(oggettoDaPopolarePagamento()){
			//ORDINATIVO PAGAMENTO
			if(model.isSonoInAggiornamento()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}else{
			//ORDINTIVO INCASSO
			if(model.isSonoInAggiornamentoIncasso()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_VARORDINC)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}
		
	}
	
	
	public boolean checkDisabilitaNoteTesoriere(){
		if(oggettoDaPopolarePagamento()){
			//ORDINATIVO PAGAMENTO
			if(model.isSonoInAggiornamento()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}else{
			//ORDINTIVO INCASSO
			if(model.isSonoInAggiornamentoIncasso()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_VARORDINC)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}
		
	}
	
	public boolean checkDisabilitaBeneficiariMultipli(){
		if(oggettoDaPopolarePagamento()){
			//ORDINATIVO PAGAMENTO
			if(model.isSonoInAggiornamento()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}else{
			//ORDINTIVO INCASSO
			if(model.isSonoInAggiornamentoIncasso()){
				//AGGIORNAMENTO
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_VARORDINC)){
						return true;
					}
				}
			}else{
				//INSERIMENTO
				return false;
			}
			
			return false;
		}
		
	}
	
	
	public String aggiornaOrdinativoIncasso(){
		//Controllo se e' abilitata l'azione
		if(!isAzioneAbilitata(CodiciOperazioni.OP_ENT_AGGORDINC)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		//Controllo lo stato del bilancio
		if(controlloStatoBilancio(Integer.parseInt(sessionHandler.getAnnoEsercizio()),"Gestione","Ordinativo")){
			return INPUT;
		}
		
		
		List<Errore> listaErrori= new ArrayList<Errore>();

		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Ordinativo"));
		} else if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione().length()<3){
			listaErrori.add(ErroreFin.CAMPO_LUNGHEZZA_MINIMA_NON_RISPETTATA.getErrore("DESCRIZIONE","tre"));
		}

		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Bollo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Tesoriere"));
		}
		
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso() == null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size() == 0){
			listaErrori.add(ErroreFin.ORDINATIVO_MANCANTE_QUOTE.getErrore("inserimento","ordinativo di incasso"));
		}
		
		if(!listaErrori.isEmpty()){
			addErrori(listaErrori);
			return INPUT;
		}
		
		// verifico che ci sia congruenza tra totale quote e totale coperture

		if(!controllaCongruenzaCopertura()){

			addActionError(ERRORE_CONGRUENZA_COPERTURE);
			return INPUT;

		}
		
		
		AggiornaOrdinativoIncasso request = composizioneAggiornaIncasso();
		
		
		AggiornaOrdinativoIncassoResponse response= ordinativoService.aggiornaOrdinativoIncasso(request);
		if (response.isFallimento()) {
			addErrori(response.getErrori());
			return INPUT;
		} 
		
//		/*********************PARTE PER EVITARE LA MEGA QUERY ****************/
		
		if(response.getOrdinativoIncassoAggiornato()!= null){
			model.setOrdinativoIncassoRicaricatoDopoInsOAgg(response.getOrdinativoIncassoAggiornato());
			model.setRicaricaDopoAggiornamento(true);
		}
//		/********************* *****************/
//		
		
		// messaggio di ok nella pagina consulta
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		// pulisco i dati di sessione
		model.setSonoInAggiornamentoIncasso(true);
		model.setForceReloadAgiornamentoOrdinativoIncasso(true);
		
		return RETURN_AGGIORNA;
		
	}
	

	/**
	 * gestisce ajax della bottoniera dei provvisori della terza pagina 
	 * @return String
	 */
	public String aggiornaBottoniera(){

		return "aggiornaBottoniera";
	}
	
	
	/**
	 *  dato anno e numero provv va a vedere se nel corrispondente oggetto
	 *  del model vi e' gia' un provvisorio identificato dai 2 integer
	 * 
	 * @param anno
	 * @param numero
	 * @return boolean
	 */
	 protected boolean coperturaGiaPresente(Integer anno, Integer numero){
	    	boolean ris = false;
	    	if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
	    		for (ProvvisorioDiCassa pc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
	    			if(sonoUgualiInt(pc.getAnno(), anno) && sonoUgualiInt(pc.getNumero(), numero)){
	    				ris= true;
	    				break;
	    			}
				}	
	    	}
	    	return ris;
	    }
	
	
	public boolean checkDisabilita(){
		if(oggettoDaPopolarePagamento()){
			//ORDINATIVO PAGAMENTO
	    	if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.TRASMESSO) && isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
				return true;
			}
	    	
	    	if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO) && isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
				return true;
			}
			
			return false;
		}else{
			//ORDINATIVO INCASSO
			if(model.isSonoInAggiornamentoIncasso()){
	    		//AGGIORNAMENTO
	    		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.TRASMESSO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_VARORDINC)){
						return true;
					}
	    		}
	    		
	    		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)){
					if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_VARORDINC)){
						return true;
					}
	    		}
	    		
	    		
	    	}else{
	    		//INSERIMENTO
	    		return false;
	    	}
	    		
	    		return false;
		}
	
	}

	
	
	// CR-1911 controllo abilitazione campi doc: 
	// BIL--SIAC-FIN-CDU-061-V04 - SPEC052 Aggiorna Ordinativo di Pagamento
	// Tabella 3 Abilitazione campi Maschera in funzione di Stato e esistenza Documenti
	public boolean isCampoAbilitatoInAggiornamento(String nomeCampo) {

		
		Boolean abilitato = Boolean.TRUE;
		
		if(model.isSonoInAggiornamento()){
			
			String statoOrdinativo = model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().toString();
			Boolean conDocumento = Boolean.FALSE;
			
			
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoSubOrdinativiDiPagamento().get(0).getSubDocumentoSpesa()!=null){
				conDocumento = Boolean.TRUE;
			}
			
			if ((isAzioneAbilitata(CodiciOperazioni.OP_SPE_aggMan) && statoOrdinativo.equalsIgnoreCase(StatoOperativoOrdinativo.INSERITO.toString())) 
					||  (isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan) && statoOrdinativo.equalsIgnoreCase(StatoOperativoOrdinativo.TRASMESSO.toString()))){
				if(conDocumento && equalsIgnoreCaseSuAlmenoUno(nomeCampo, FIELD_BENEFICIARI_MULTIPLI,FIELD_A_COPERTURA,FIELD_IMPQUOTA,TAB_PROVVISORI)){
					return Boolean.FALSE;
				}
			}
	
			if (isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan) && (statoOrdinativo.equalsIgnoreCase(StatoOperativoOrdinativo.QUIETANZATO.toString())||
				statoOrdinativo.equalsIgnoreCase(StatoOperativoOrdinativo.FIRMATO.toString()))) {
				if(equalsIgnoreCaseSuAlmenoUno(nomeCampo, FIELD_BENEFICIARI_MULTIPLI,FIELD_A_COPERTURA,FIELD_IMPQUOTA,TAB_PROVVISORI,FIELD_TESORIERE)){
					return Boolean.FALSE;
				}
			}
				
			if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan) && statoOrdinativo.equalsIgnoreCase(StatoOperativoOrdinativo.TRASMESSO.toString())) {
				if (!conDocumento){
					if(equalsIgnoreCaseSuAlmenoUno(nomeCampo, FIELD_A_COPERTURA,FIELD_IMPQUOTA,TAB_PROVVISORI)){
						return Boolean.FALSE;
					}
				}
			}
		}
		return abilitato;

	}
	
	public boolean isAttivaBtnSalva() {
		return attivaBtnSalva;
	}
	
	public boolean sonoInAggiornamento(){
		
		return model.isSonoInAggiornamento();
	}
	
	public boolean sonoInAggiornamentoIncasso(){
		
		return model.isSonoInAggiornamentoIncasso();
	}
	
	
	public boolean sonoInAggiornamentoOR(){
		if(model.isSonoInAggiornamento() || model.isSonoInAggiornamentoIncasso()){
			return true;
		}
		return false;
	}
	
	
	@Override
	public String selezionaCapitolo(){
		
		
		if(oggettoDaPopolarePagamento()){
			model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiPagamenti(new ArrayList<SubOrdinativoPagamento>());
		}else{
			model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiIncasso(new ArrayList<SubOrdinativoIncasso>());
		}
		
		return super.selezionaCapitolo();
	}
	
	@Override
	public String selezionaProvvedimento(){
		
		if(oggettoDaPopolarePagamento()){
			model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiPagamenti(new ArrayList<SubOrdinativoPagamento>());
		}else{
			model.getGestioneOrdinativoStep2Model().setListaSubOrdinativiIncasso(new ArrayList<SubOrdinativoIncasso>());
		}
		
		return super.selezionaProvvedimento();
	}
	
	/**
	 * Per verificare se il codice corrisponde ad un siope esistente
	 * @param cod
	 */
	protected void codiceSiopeChangedInternal(String cod){
		TipiLista tipoList = null;
		if(OggettoDaPopolareEnum.ACCERTAMENTO.toString().equals(teSupport.getOggettoAbilitaTE()) && oggettoDaPopolare==null){
			//SIAMO NELLA ACTION: NuovoAccertamentoOrdinativoAction
			tipoList = TipiLista.TIPO_SIOPE_ENTRATA_I;
		} else if(oggettoDaPopolarePagamento()){
			tipoList = TipiLista.TIPO_SIOPE_SPESA_I;
		} else {
			tipoList = TipiLista.TIPO_SIOPE_ENTRATA_I;
		}
		//richiamo il metodo centralizzato 
		//passandogli la tipologia di lista riguardante 
		//il particolare oggettoDaPopolare:
		codiceSiopeChangedInternal(cod, tipoList);
		
	}
	
	/**
	 * Setta il siope trovato nel model
	 */
	@Override
	protected void setSiopeSpesaSelezionato(SiopeSpesa siopeTrovato,boolean trovato) {
		teSupport.setSiopeSpesaCod(siopeTrovato.getCodice());
		teSupport.setSiopeSpesa(siopeTrovato);
		teSupport.setTrovatoSiopeSpesa(trovato);
	}
	

	public void setAttivaBtnSalva(boolean attivaBtnSalva) {
		this.attivaBtnSalva = attivaBtnSalva;
	}

	public String getNumeroQuotaEliminato() {
		return numeroQuotaEliminato;
	}

	public void setNumeroQuotaEliminato(String numeroQuotaEliminato) {
		this.numeroQuotaEliminato = numeroQuotaEliminato;
	}

	public String getNumeroPerDettaglio() {
		return numeroPerDettaglio;
	}

	public void setNumeroPerDettaglio(String numeroPerDettaglio) {
		this.numeroPerDettaglio = numeroPerDettaglio;
	}
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
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
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}
	

	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public boolean isToggleRicercaProvvAperto() {
		return toggleRicercaProvvAperto;
	}

	public void setToggleRicercaProvvAperto(boolean toggleRicercaProvvAperto) {
		this.toggleRicercaProvvAperto = toggleRicercaProvvAperto;
	}

	public String getNumeroOrdinativo() {
		return numeroOrdinativo;
	}

	public void setNumeroOrdinativo(String numeroOrdinativo) {
		this.numeroOrdinativo = numeroOrdinativo;
	}

	public String getAnnoOrdinativo() {
		return annoOrdinativo;
	}

	public void setAnnoOrdinativo(String annoOrdinativo) {
		this.annoOrdinativo = annoOrdinativo;
	}

	public String getNumeroOrdinativoStruts() {
		return numeroOrdinativoStruts;
	}

	public void setNumeroOrdinativoStruts(String numeroOrdinativoStruts) {
		this.numeroOrdinativoStruts = numeroOrdinativoStruts;
	}

	public String getAnnoOrdinativoStruts() {
		return annoOrdinativoStruts;
	}

	public void setAnnoOrdinativoStruts(String annoOrdinativoStruts) {
		this.annoOrdinativoStruts = annoOrdinativoStruts;
	}

	public AttoAmministrativo getProvvedimentoOrdinativo() {
		return provvedimentoOrdinativo;
	}

	public void setProvvedimentoOrdinativo(
			AttoAmministrativo provvedimentoOrdinativo) {
		this.provvedimentoOrdinativo = provvedimentoOrdinativo;
	}

	public Soggetto getSoggettoOrdinativo() {
		return soggettoOrdinativo;
	}

	public void setSoggettoOrdinativo(Soggetto soggettoOrdinativo) {
		this.soggettoOrdinativo = soggettoOrdinativo;
	}

	public ClasseSoggetto getClasseOrdinativo() {
		return classeOrdinativo;
	}

	public void setClasseOrdinativo(ClasseSoggetto classeOrdinativo) {
		this.classeOrdinativo = classeOrdinativo;
	}

	public CapitoloUscitaGestione getCapitoloUscitaTrovato() {
		return capitoloUscitaTrovato;
	}

	public void setCapitoloUscitaTrovato(
			CapitoloUscitaGestione capitoloUscitaTrovato) {
		this.capitoloUscitaTrovato = capitoloUscitaTrovato;
	}

	public CapitoloEntrataGestione getCapitoloEntrataTrovato() {
		return capitoloEntrataTrovato;
	}

	public void setCapitoloEntrataTrovato(
			CapitoloEntrataGestione capitoloEntrataTrovato) {
		this.capitoloEntrataTrovato = capitoloEntrataTrovato;
	}

	public boolean isIdSedeCreditoreChecked() {
		return idSedeCreditoreChecked;
	}

	public void setIdSedeCreditoreChecked(boolean idSedeCreditoreChecked) {
		this.idSedeCreditoreChecked = idSedeCreditoreChecked;
	}

	public String getIdCoperturaDaEliminare() {
		return idCoperturaDaEliminare;
	}

	public void setIdCoperturaDaEliminare(String idCoperturaDaEliminare) {
		this.idCoperturaDaEliminare = idCoperturaDaEliminare;
	}

	public boolean isQuotaIncassoImpOver() {
		return quotaIncassoImpOver;
	}

	public void setQuotaIncassoImpOver(boolean quotaIncassoImpOver) {
		this.quotaIncassoImpOver = quotaIncassoImpOver;
	}

	public boolean isClearStatus() {
		return clearStatus;
	}

	public void setClearStatus(boolean clearStatus) {
		this.clearStatus = clearStatus;
	}

	public String getUidProvvDaRicerca() {
		return uidProvvDaRicerca;
	}

	public void setUidProvvDaRicerca(String uidProvvDaRicerca) {
		this.uidProvvDaRicerca = uidProvvDaRicerca;
	}

	public boolean isAncoraVisualizza() {
		return ancoraVisualizza;
	}

	public void setAncoraVisualizza(boolean ancoraVisualizza) {
		this.ancoraVisualizza = ancoraVisualizza;
	}

	public boolean isAncoraVisualizzaInserisciProv() {
		return ancoraVisualizzaInserisciProv;
	}

	public void setAncoraVisualizzaInserisciProv(
			boolean ancoraVisualizzaInserisciProv) {
		this.ancoraVisualizzaInserisciProv = ancoraVisualizzaInserisciProv;
	}

	public boolean isAncoraVisualizzaInserisciProvErrori() {
		return ancoraVisualizzaInserisciProvErrori;
	}

	public void setAncoraVisualizzaInserisciProvErrori(
			boolean ancoraVisualizzaInserisciProvErrori) {
		this.ancoraVisualizzaInserisciProvErrori = ancoraVisualizzaInserisciProvErrori;
	}

	/**
	 * @return the disabilitaBtnAzioni
	 */
	public boolean getDisabilitaBtnAzioni() {
		return disabilitaBtnAzioni;
	}

	/**
	 * @param disabilitaBtnAzioni the disabilitaBtnAzioni to set
	 */
	public void setDisabilitaBtnAzioni(boolean disabilitaBtnAzioni) {
		this.disabilitaBtnAzioni = disabilitaBtnAzioni;
	}

	
    public boolean controlloGestioneProvvisorio(String importoStringa, ProvvisorioDiCassa pc, String operazione){
		
		ProvvisorioDiCassa pdcClone = new ProvvisorioDiCassa();
		if(pc!=null){
			
			calcolaTotaliFooter();
			BigDecimal importoBd = BigDecimal.ZERO;
			
			if(null!=pc.getImportoFormatString() && !pc.getImportoFormatString().trim().equals("")){
				importoBd = convertiImportoToBigDecimal(pc.getImportoFormatString());
			}
			
			
			if(importoBd.compareTo(BigDecimal.ZERO)==0){
				addActionError("Importo deve essere maggiore di zero");
			}
			
			BigDecimal importoPrimaUpdate = model.getGestioneOrdinativoStep3Model().getImportoCoperturaPrimaUpdate();
			BigDecimal deltaUpdate = BigDecimal.ZERO;
			boolean aggiornatoDiminuito = false;
			if(importoPrimaUpdate!=null){
				if(importoBd.compareTo(importoPrimaUpdate)<0){
					aggiornatoDiminuito = true;
				}
				deltaUpdate = importoBd.subtract(importoPrimaUpdate);
			} else {
				deltaUpdate = importoBd;
			}
			
			// TotalizzatoreDaCollegare: è l'importo = TOTORD - TOTALEREG, cioè quanto dell’ordinativo è ancora da collegare ai provvisori
			BigDecimal importoPiuBasso = FinUtility.minTraBD(pc.getImportoDaRegolarizzare(), model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare());
			
			// Qui controllo  che la quota ordinativo abbia la capienza per regolarizzare!
			
			// la regola è:
			// IMPORTOCOLLEGATO <= min (IMPDAREG ; DACOLLEGARE)
			// IMPDAREG è quello etichettato sul provvisorio come 'da regolarizzare'
			// DACOLLEGARE è TOTORD - TOTALEREG cioè quanto dell’ordinativo è ancora da collegare ai provvisori 
			// IMPORTOCOLLEGATO è quanto dell'importo dell'ordinativo sto collegando al provvisorio
			
			if(deltaUpdate.compareTo(importoPiuBasso)>0 && !aggiornatoDiminuito){
				
				addActionError(ERRORE_IMPORTO_REGOLARIZZATO);
				return false;
				
			}
			
			// conversione importo
			pdcClone = clone(pc);
			pdcClone.setImporto(importoBd);
		}
		
		if(!hasActionErrors()){
			
			if(operazione.equals(OPERAZIONE_INSERIMENTO)){
				
				if(coperturaGiaPresente(pdcClone.getAnno(), pdcClone.getNumero())){
					addActionError("Provvisorio gia' utilizzato in un altra copertura");
					return false;
				}
				
				model.getGestioneOrdinativoStep3Model().getListaCoperture().add(pdcClone);
				calcolaTotaliFooter();
				model.getGestioneOrdinativoStep3Model().setProvvisorio(null);
				
				variaImportoDaRegolarizzare(pdcClone.getIdProvvisorioDiCassa().toString(), pdcClone.getImporto(), true);
				
			}else if(operazione.equals(OPERAZIONE_MODIFICA)){
			
				   if(model.getGestioneOrdinativoStep3Model().getListaCoperture()!=null){
					   
					   for (ProvvisorioDiCassa pcc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
						   if(pcc.getIdProvvisorioDiCassa().compareTo(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getIdProvvisorioDiCassa())==0){

							   // rimuovo l'elemento selezionato
							   model.getGestioneOrdinativoStep3Model().getListaCoperture().remove(pcc);
							   break;
						   }
						
					   }
					   
					   // lo riaggiungo
					   if(model.getGestioneOrdinativoStep3Model().getListaCoperture()==null){
						   model.getGestioneOrdinativoStep3Model().setListaCoperture(new ArrayList<ProvvisorioDiCassa>());
					   }
					   
					   model.getGestioneOrdinativoStep3Model().getListaCoperture().add(pdcClone);
					   
					   BigDecimal importoPrimaDiModifica = model.getGestioneOrdinativoStep3Model().getImportoCoperturaPrimaUpdate();
					   BigDecimal deltaImporto = pdcClone.getImporto().subtract(importoPrimaDiModifica);
					   variaImportoDaRegolarizzare(pdcClone.getIdProvvisorioDiCassa().toString(), deltaImporto, true);
					   
					   calcolaTotaliFooter();
				   }
			}

			return true;
		}

        return false;
		
	}
    
       
    
	public String controlloGestioneProvvisorio(String operazione){
		
		ProvvisorioDiCassa pdcClone = new ProvvisorioDiCassa();
		
		if(null!=model.getGestioneOrdinativoStep3Model().getProvvisorio()){
			
			BigDecimal importoBd = BigDecimal.ZERO;
			
			BigDecimal importoPrimaUpdate = model.getGestioneOrdinativoStep3Model().getImportoCoperturaPrimaUpdate();
			BigDecimal deltaUpdate = BigDecimal.ZERO;
			boolean aggiornatoDiminuito = false;
			if(importoPrimaUpdate!=null){
				if(importoBd.compareTo(importoPrimaUpdate)<0){
					aggiornatoDiminuito = true;
				}
				deltaUpdate = importoBd.subtract(importoPrimaUpdate);
			} else {
				
					if(operazione.equals(OPERAZIONE_INSERIMENTO)){
						
						deltaUpdate = convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoFormatString());
						if(deltaUpdate.compareTo(BigDecimal.ZERO)==0){
								addActionError(ERRORE_IMPORTO_REGOLARIZZATO + " L'importo deve essere maggiore di zero");
								return INPUT;
						}
						 
					}else{
						//dal btn aggiorna quota
						deltaUpdate = importoBd;	
					}
				
			}
			
			BigDecimal importoDaRegolarizzare = BigDecimal.ZERO;
			
			if((sonoInAggiornamentoIncasso() || !sonoInAggiornamentoIncasso()) && operazione.equals(OPERAZIONE_MODIFICA)){
				// in caso di aggiornamento modifico il dato da popup
				importoDaRegolarizzare = model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoDaRegolarizzare();
			}else {
				 // tutti gli altri casi
				importoDaRegolarizzare = model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoDaRegolarizzare();
			}
				
			
			BigDecimal importoPiuBasso = FinUtility.minTraBD(importoDaRegolarizzare, model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare());
			
			//
			if(operazione.equals(OPERAZIONE_INSERIMENTO)){
				
				BigDecimal totPagato =  BigDecimal.ZERO;
				
				if(!isEmpty(model.getGestioneOrdinativoStep3Model().getListaCoperture())){
					
					for(ProvvisorioDiCassa itProvv : model.getGestioneOrdinativoStep3Model().getListaCoperture()){
						totPagato = totPagato.add(itProvv.getImporto());
					}
					
				}
				totPagato= totPagato.add(convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoFormatString()));
				if(model.getGestioneOrdinativoStep2Model().getSommatoriaQuote().subtract(totPagato).compareTo(BigDecimal.ZERO)<0){
					addActionError(ERRORE_IMPORTO_REGOLARIZZATO_MAGGIORE_DEL_REGOLARIZZABILE_QUOTA);
					return INPUT;
				}
				
				
			}else{
				
				debug("importoPiuBasso", importoPiuBasso);
				
				if(deltaUpdate.compareTo(importoPiuBasso)>0 && !aggiornatoDiminuito){
					
					addActionError(ERRORE_IMPORTO_REGOLARIZZATO);
					return INPUT;
				}
			}
			
			if(operazione.equals(OPERAZIONE_INSERIMENTO)){
				if(null!=model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoFormatString() && !model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoFormatString().trim().equals("")){
					importoBd = convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoFormatString());
				}
				//setto oggettone
				pdcClone = clone(model.getGestioneOrdinativoStep3Model().getProvvisorio());
				
			}else if(operazione.equals(OPERAZIONE_MODIFICA)){
				
					if(null!=model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoFormatString() && !model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoFormatString().trim().equals("")){
						
						importoBd = convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoFormatString());
						
						if(deltaUpdate.compareTo(BigDecimal.ZERO)>0){
						
							addActionError("ERRORE_IMPORTO_REGOLARIZZATO");
							return INPUT;
						}
					}
					//setto oggettone
					pdcClone = clone(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio());
			}
			
			if (importoDaRegolarizzare.compareTo(importoBd) < 0){
				addActionError(ERRORE_IMPORTO_REGOLARIZZATO_SUL_REGOLARIZZABILE_PROVVISORIO_DI_CASSA);
				return INPUT;
			}
			
			// importo
			pdcClone.setImporto(importoBd);
			
		}
		
		if(operazione.equals(OPERAZIONE_INSERIMENTO)){
			
			if(coperturaGiaPresente(pdcClone.getAnno(), pdcClone.getNumero())){
				addActionError("Provvisorio gia' utilizzato in un altra copertura");
				return INPUT;
			}
			
			// aggiungo gestione del riporta in testata
			if(model.getGestioneOrdinativoStep3Model().getRiportaInTestataPageInserisciProvvisorio()!=null){
				
				if(model.getGestioneOrdinativoStep3Model().getRiportaInTestataPageInserisciProvvisorio()){
					pdcClone.setRiportaInTestata(Boolean.TRUE);
				}else{
					pdcClone.setRiportaInTestata(Boolean.FALSE);
				}
				
			}
			
			model.getGestioneOrdinativoStep3Model().getListaCoperture().add(pdcClone);
			calcolaTotaliFooter();
			variaImportoDaRegolarizzare(pdcClone.getIdProvvisorioDiCassa().toString(), pdcClone.getImporto(), true);
			
		}else if(operazione.equals(OPERAZIONE_MODIFICA)){
			
			if(model.getGestioneOrdinativoStep3Model().getListaCoperture()!=null){
				   
				   if(!sonoInAggiornamentoIncasso()){
					   
					   // sono in inserimento ordinativo
					  
					   for (ProvvisorioDiCassa pcc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
						   
						   if(operazione.equals(OPERAZIONE_INSERIMENTO)){
							   
							   
							   if(pcc.getIdProvvisorioDiCassa().compareTo(model.getGestioneOrdinativoStep3Model().getProvvisorio().getIdProvvisorioDiCassa())==0){
									
								   // rimuovo l'elemento selezionato
								   model.getGestioneOrdinativoStep3Model().getListaCoperture().remove(pcc);
								   break;
							   }
							   
							   
						   }else {
							   // aggiornamento
							   
							   if(pcc.getIdProvvisorioDiCassa().compareTo(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getIdProvvisorioDiCassa())==0){
									
								   // rimuovo l'elemento selezionato
								   model.getGestioneOrdinativoStep3Model().getListaCoperture().remove(pcc);
								   break;
							   }
						   }	   
					   }

				   }else{
					   // sono in aggiornamento ordinativo
					   for (ProvvisorioDiCassa pcc : model.getGestioneOrdinativoStep3Model().getListaCoperture()) {
						   if(pcc.getIdProvvisorioDiCassa().compareTo(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getIdProvvisorioDiCassa())==0){
							   // rimuovo l'elemento selezionato
							   model.getGestioneOrdinativoStep3Model().getListaCoperture().remove(pcc);
							   break;
						   }
					   }
				   }
				   
				   // lo riaggiungo
				   if(model.getGestioneOrdinativoStep3Model().getListaCoperture()==null){
					   model.getGestioneOrdinativoStep3Model().setListaCoperture(new ArrayList<ProvvisorioDiCassa>());
				   }
				   
				   BigDecimal importoPrimaDiModifica = BigDecimal.ZERO;
				   if(null!=model.getGestioneOrdinativoStep3Model().getImportoCoperturaPrimaUpdate()){
					   importoPrimaDiModifica = model.getGestioneOrdinativoStep3Model().getImportoCoperturaPrimaUpdate();
				   }
				   
				   BigDecimal deltaImporto = null;  
				   
				   	   
				   // aggiungo gestione del riporta in testata in aggiorna provvisorio
				   if(model.getGestioneOrdinativoStep3Model().getHiddenRiportaInTestataPageAggiornaProvvisorio()!=null){
					
						if(model.getGestioneOrdinativoStep3Model().getHiddenRiportaInTestataPageAggiornaProvvisorio()){
							pdcClone.setRiportaInTestata(Boolean.TRUE);
						}else{
							pdcClone.setRiportaInTestata(Boolean.FALSE);
						}
						
						model.getGestioneOrdinativoStep3Model().setRiportaInTestataPageAggiornaProvvisorio(model.getGestioneOrdinativoStep3Model().getHiddenRiportaInTestataPageAggiornaProvvisorio());
				   }

				   if(!sonoInAggiornamentoIncasso()){
					   deltaImporto = pdcClone.getImporto().subtract(importoPrimaDiModifica);
					   variaImportoDaRegolarizzare(pdcClone.getIdProvvisorioDiCassa().toString(), deltaImporto, true);
					   model.getGestioneOrdinativoStep3Model().getListaCoperture().add(pdcClone);
				   }else{
					   deltaImporto = model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImporto().subtract(convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoFormatString()));
					   variaImportoDaRegolarizzare(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getIdProvvisorioDiCassa().toString(), deltaImporto, true);
					   model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().setImporto(convertiImportoToBigDecimal(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio().getImportoFormatString()));
					   model.getGestioneOrdinativoStep3Model().getListaCoperture().add(model.getGestioneOrdinativoStep3Model().getVisualizzaProvvisorio());
				   
				   }
				   // calcola i totali nella tabella
				   calcolaTotaliFooter();
			   }
		}	
		return SUCCESS;
	
	}
	
	protected void caricaListaMotivazioniAssenzaCig() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.MOTIVAZIONE_ASSENZA_CIG);
		model.getGestioneOrdinativoStep2Model().setListaMotivazioniAssenzaCig((List<CodificaFin>)mappaListe.get(TipiLista.MOTIVAZIONE_ASSENZA_CIG));
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
	
	
	/**
	 * Implementa i controlli sul campo descrizione al prosegui da step 1
	 * @param listaErrori
	 */
	protected void controlloDescrizioneProseguiStep1(List<Errore> listaErrori){
		boolean aCopertura = model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura();
		//se ordinativo a copertura nessun controllo sulla descrizione
		if(!aCopertura){
			if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Ordinativo"));
			}else{
				if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione().length()<3){
					listaErrori.add(ErroreFin.CAMPO_LUNGHEZZA_MINIMA_NON_RISPETTATA.getErrore("DESCRIZIONE","tre"));
				} else {
					//OK
					model.getGestioneOrdinativoStep2Model().setDescrizioneQuota(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDescrizione());
				}
			}
		}
	}

}
