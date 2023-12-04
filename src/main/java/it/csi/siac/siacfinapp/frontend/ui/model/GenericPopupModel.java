/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GenericPopupModel extends GestoreTransazioneElementareModel {

	private static final long serialVersionUID = 1L;
	
	
	/************************* GESTIONE MODAL PROGETTO **************************/
	//progetto ricerca
	private ProgettoImpegnoModel progettoRicerca = new ProgettoImpegnoModel();
	
	//lista ricerca progetto
	private List<Progetto> listaRicercaProgetto = new ArrayList<Progetto>();
	
	//lista ricerca progetto con cronoprogrammi
	private List<Cronoprogramma> listaRicercaProgettoCronoprogrammi = new ArrayList<Cronoprogramma>();
	
	//progetto trovato
	public boolean progettoTrovato = false;
	/****************************************************************************/
	
	
	
	/************************* GESTIONE MODAL SOGGETTO **************************/
	//soggetto ricerca
	private SoggettoImpegnoModel soggettoRicerca = new SoggettoImpegnoModel();
	
	//lista ricerca soggetto
	private List<Soggetto> listaRicercaSoggetto = new ArrayList<Soggetto>();
	
	//soggetto trovato
	public boolean soggettoTrovato = false;
	/****************************************************************************/
	
	/************************* GESTIONE MODAL SOGGETTO DUE **************************/
	//soggetto ricerca
	private SoggettoImpegnoModel soggettoRicercaDue = new SoggettoImpegnoModel();
	
	/****************************************************************************/
	
	
	
	/************************* GESTIONE MODAL CAPITOLO **************************/
	//capitolo ricerca
	private CapitoloImpegnoModel capitoloRicerca = new CapitoloImpegnoModel();
	
	//lista ricerca capitolo
	private List<CapitoloImpegnoModel> listaRicercaCapitolo = new ArrayList<CapitoloImpegnoModel>();
	
	//capitolo trovato
	public boolean capitoloTrovato = false;
	
	//capitolo model trovato da servizio
	private CapitoloImpegnoModel capitoloModelTrovatoDaServizio;
	/****************************************************************************/
	
	
	/************************* GESTIONE MODAL PROVVEDIMENTO *********************/
	//provvedimento ricerca
	private ProvvedimentoImpegnoModel provvedimentoRicerca = new ProvvedimentoImpegnoModel();
	
	//provvedimento trovato
	public boolean provvedimentoTrovato = false;
	
	//lista ricerca provvedimento
	private List<ProvvedimentoImpegnoModel> listaRicercaProvvedimento = new ArrayList<ProvvedimentoImpegnoModel>();
	
	private boolean daCruscotto; //per escludere i provvedimenti con blocco true
	/****************************************************************************/
	
	
	
	//numero provvedimento inserimento da resp
	private BigInteger numeroProvvedimentoInserimentoDaResp;
	
	//provvedimento inserimento
	private ProvvedimentoImpegnoModel provvedimentoInserimento = new ProvvedimentoImpegnoModel();
	
	//provvedimento inserito
	public boolean provvedimentoInserito = false;
	
	
	
	/************************* GESTIONE MODAL IMPEGNO ***************************/
	private ImpegnoLiquidazioneModel impegnoRicerca = new ImpegnoLiquidazioneModel();
	public boolean impegnoTrovato = false;
	private List<ImpegnoLiquidazioneModel> listaRicercaImpegno = new ArrayList<ImpegnoLiquidazioneModel>();
	private boolean impegnoAppenaSelezionatoDaCompGuidata;
	/****************************************************************************/	
	
	
	
	/************************* POPUP COMPILAZIONE AUTOMATICA IMPEGNI **************************/
	private Impegno impegnoPopup;
	private String descrizioneImpegnoPopup,descrizioneTipoImpegnoPopup,importoImpegno;
	private BigDecimal disponibilita;
	private Integer nImpegno, nSubImpegno, nAnno;
	//campi da settare post scelta in popup
	private Integer annoImpegno, numeroImpegno, numeroSub;
	private int radioImpegnoSelezionato, radioSubImpegnoSelezionato;

	private List<Impegno>  listaImpegniCompGuidata = new ArrayList<Impegno>();
	private List<SubImpegno>  listaSubImpegniCompGuidata = new ArrayList<SubImpegno>();

	//utili per visualizzazione scheda impegno in popup
	private ProvvedimentoImpegnoModel provvedimentoPopup = new ProvvedimentoImpegnoModel();
	private SoggettoImpegnoModel soggettoPopup = new SoggettoImpegnoModel();
	private CapitoloUscitaGestione capitoloPopup = new CapitoloUscitaGestione();
	
	
	//NOVEMBRE 2017 NUOVA POSSIBILITA' DI RICERCA ACCERTAMENTI CON POP UP:
	private List<Accertamento>  listaAccertamentiCompGuidata = new ArrayList<Accertamento>();
	private Accertamento accertamentoPopup;
	private CapitoloEntrataGestione capitoloEntrataPopup = new CapitoloEntrataGestione();
	//per qaunto riguardagli altri campi che non hanno un tipo specifico
	//per l'accertamento riuso quelli usati per il popup dell'impegno: numeroImpegno, radioImpegnoSelezionato, ecc 
	//
	
	private Soggetto ultimoSoggettoSelezionatoCorrettamente = new Soggetto();
	
	
	//campi utili popup
	private boolean isnSubImpegno = false, hasImpegnoSelezionatoPopup = false, hasImpegnoSelezionatoXPopup = false, soggettoSelezionatoPopup = false, inPopup = true;	
	/************************* Popup Compilazione Automatica Impegni **************************/
	
	/******** utilizzato nuova liquidazione da ordinativo ******/
	private String cupImpegnoSelezionato;
	private CapitoloUscitaGestione capitoloPopupNuovaLiquidazioneOrdinativo = new CapitoloUscitaGestione();	
	private Impegno impegnoPopUpNuovaLiquidazioneOrdinativo = new Impegno();
	
	
	/************************* Popup Compilazione Automatica Documenti Carta **************************/
	private String numDoc;
	private Integer annoDoc,soggDoc;
	private String codiceTipoDoc;
	private List<DocumentoSpesa> listaRicercaDocumento = new ArrayList<DocumentoSpesa>();
	private List<SubdocumentoSpesa> listaRicercaSubDocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	private List<CodificaFin> listaDocTipoSpesa = new ArrayList<CodificaFin>();
	private boolean hasSubDocumentiSpesa = false;
	private int radioDocumentoSelezionato,radioSubDocumentoSelezionato;
	
	/************************* Popup Compilazione Automatica Documenti Carta **************************/
	
	
	/**
	 * SIAC-6929
	 * Campo per abilitare il controllo del BLOCCO RAGIONERIA
	 * settato solo in inserisci impegno ma mai controllato 
	 * si potrebbe anche togliere
	 */
	private boolean controlloBloccoRagioneriaAbilitato = false;
	/**
	 * SIAC-7399
	 * Parametro da mettere a true solo se non si vuole rendere valido
	 * il controllo sul blocco ragioneria.
	 * Il parametro precedente viene settato in alcuni casi ma non viene
	 * mai effettivamente controllato sulla genericPopupAction 
	 * @return
	 */
	private boolean skipControlloBloccoRagioneria;
	
	
	//SIAC-7349
	private boolean componenteBilancioCapitoloAttivo;
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	private ImportiCapitolo importiAnniSuccessivi;
	private ImportiCapitolo importiResidui;
	private ImportiCapitoloPerComponente competenzaStanziamento;
	private ImportiCapitoloPerComponente competenzaImpegnato;
	private ImportiCapitoloPerComponente residuiEffettivi;
	private ImportiCapitoloPerComponente residuiPresunti;
	private ImportiCapitoloPerComponente cassaStanziato;
	private ImportiCapitoloPerComponente cassaPagato;
	private Integer annoStanziamentoCapitoloInizio;
	private ImportiCapitolo importiEx;
	private ImportiCapitolo importiEx1;
	private ImportiCapitolo importiEx2;
	private ImportiCapitolo importiEx3;
	private ImportiCapitolo importiCapitoloUscita0;
	private ImportiCapitolo importiCapitoloUscita1;
	private ImportiCapitolo importiCapitoloUscita2;
	private Integer componenteBilancioUid;
	private boolean componenteBilancioCapitoloAttivoRicerca;
	
	/**
	 * @return the componenteBilancioUid
	 */
	public Integer getComponenteBilancioUid() {
		return componenteBilancioUid;
	}
	/**
	 * @param componenteBilancioUid the componenteBilancioUid to set
	 */
	public void setComponenteBilancioUid(Integer componenteBilancioUid) {
		this.componenteBilancioUid = componenteBilancioUid;
	}
	public String getCupImpegnoSelezionato() {
		return cupImpegnoSelezionato;
	}
	public void setCupImpegnoSelezionato(String cupImpegnoSelezionato) {
		this.cupImpegnoSelezionato = cupImpegnoSelezionato;
	}
	private CapitoloImpegnoModel datoPerVisualizza = new CapitoloImpegnoModel();
	@SuppressWarnings("rawtypes")
	public List listaTipiProvvedimenti;
	public List<CodificaFin> listaClasseSoggetto;
	
	public List<TipoAmbito> listaTipiAmbito;
	
	@SuppressWarnings("rawtypes")
	public List listaStatiProvvedimenti;
	
	
	public SoggettoImpegnoModel getSoggettoRicerca() {
		return soggettoRicerca;
	}
	public void setSoggettoRicerca(SoggettoImpegnoModel soggettoRicerca) {
		this.soggettoRicerca = soggettoRicerca;
	}
	public List<Soggetto> getListaRicercaSoggetto() {
		return listaRicercaSoggetto;
	}
	public void setListaRicercaSoggetto(List<Soggetto> listaRicercaSoggetto) {
		this.listaRicercaSoggetto = listaRicercaSoggetto;
	}
	public boolean isSoggettoTrovato() {
		return soggettoTrovato;
	}
	public void setSoggettoTrovato(boolean soggettoTrovato) {
		this.soggettoTrovato = soggettoTrovato;
	}
	public CapitoloImpegnoModel getCapitoloRicerca() {
		return capitoloRicerca;
	}
	public void setCapitoloRicerca(CapitoloImpegnoModel capitoloRicerca) {
		this.capitoloRicerca = capitoloRicerca;
	}
	public List<CapitoloImpegnoModel> getListaRicercaCapitolo() {
		return listaRicercaCapitolo;
	}
	public void setListaRicercaCapitolo(List<CapitoloImpegnoModel> listaRicercaCapitolo) {
		this.listaRicercaCapitolo = listaRicercaCapitolo;
	}
	public boolean isCapitoloTrovato() {
		return capitoloTrovato;
	}
	public void setCapitoloTrovato(boolean capitoloTrovato) {
		this.capitoloTrovato = capitoloTrovato;
	}
	public ProvvedimentoImpegnoModel getProvvedimentoRicerca() {
		return provvedimentoRicerca;
	}
	public void setProvvedimentoRicerca(ProvvedimentoImpegnoModel provvedimentoRicerca) {
		this.provvedimentoRicerca = provvedimentoRicerca;
	}
	public boolean isProvvedimentoTrovato() {
		return provvedimentoTrovato;
	}
	public void setProvvedimentoTrovato(boolean provvedimentoTrovato) {
		this.provvedimentoTrovato = provvedimentoTrovato;
	}
	public List<ProvvedimentoImpegnoModel> getListaRicercaProvvedimento() {
		return listaRicercaProvvedimento;
	}
	public void setListaRicercaProvvedimento(
			List<ProvvedimentoImpegnoModel> listaRicercaProvvedimento) {
		this.listaRicercaProvvedimento = listaRicercaProvvedimento;
	}
	public CapitoloImpegnoModel getDatoPerVisualizza() {
		return datoPerVisualizza;
	}
	public void setDatoPerVisualizza(CapitoloImpegnoModel datoPerVisualizza) {
		this.datoPerVisualizza = datoPerVisualizza;
	}
	@SuppressWarnings("rawtypes")
	public List getListaTipiProvvedimenti() {
		return listaTipiProvvedimenti;
	}
	@SuppressWarnings("rawtypes")
	public void setListaTipiProvvedimenti(List listaTipiProvvedimenti) {
		this.listaTipiProvvedimenti = listaTipiProvvedimenti;
	}
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}
	public ImpegnoLiquidazioneModel getImpegnoRicerca() {
		return impegnoRicerca;
	}
	public void setImpegnoRicerca(ImpegnoLiquidazioneModel impegnoRicerca) {
		this.impegnoRicerca = impegnoRicerca;
	}
	public boolean isImpegnoTrovato() {
		return impegnoTrovato;
	}
	public void setImpegnoTrovato(boolean impegnoTrovato) {
		this.impegnoTrovato = impegnoTrovato;
	}
	public List<ImpegnoLiquidazioneModel> getListaRicercaImpegno() {
		return listaRicercaImpegno;
	}
	public void setListaRicercaImpegno(List<ImpegnoLiquidazioneModel> listaRicercaImpegno) {
		this.listaRicercaImpegno = listaRicercaImpegno;
	}
	public boolean isIsnSubImpegno() {
		return isnSubImpegno;
	}
	public void setIsnSubImpegno(boolean isnSubImpegno) {
		this.isnSubImpegno = isnSubImpegno;
	}
	public Impegno getImpegnoPopup() {
		return impegnoPopup;
	}
	public void setImpegnoPopup(Impegno impegnoPopup) {
		this.impegnoPopup = impegnoPopup;
	}

	public Integer getnImpegno() {
		return nImpegno;
	}
	public void setnImpegno(Integer nImpegno) {
		this.nImpegno = nImpegno;
	}
	public Integer getnSubImpegno() {
		return nSubImpegno;
	}
	public void setnSubImpegno(Integer nSubImpegno) {
		this.nSubImpegno = nSubImpegno;
	}
	public Integer getnAnno() {
		return nAnno;
	}
	public void setnAnno(Integer nAnno) {
		this.nAnno = nAnno;
	}
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public Integer getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(Integer numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public Integer getNumeroSub() {
		return numeroSub;
	}
	public void setNumeroSub(Integer numeroSub) {
		this.numeroSub = numeroSub;
	}

	public List<Impegno> getListaImpegniCompGuidata() {
		return listaImpegniCompGuidata;
	}
	public void setListaImpegniCompGuidata(List<Impegno> listaImpegniCompGuidata) {
		this.listaImpegniCompGuidata = listaImpegniCompGuidata;
	}
	public int getRadioImpegnoSelezionato() {
		return radioImpegnoSelezionato;
	}
	public void setRadioImpegnoSelezionato(int radioImpegnoSelezionato) {
		this.radioImpegnoSelezionato = radioImpegnoSelezionato;
	}


	public boolean isHasImpegnoSelezionatoPopup() {
		return hasImpegnoSelezionatoPopup;
	}
	public void setHasImpegnoSelezionatoPopup(boolean hasImpegnoSelezionatoPopup) {
		this.hasImpegnoSelezionatoPopup = hasImpegnoSelezionatoPopup;
	}
	public ProvvedimentoImpegnoModel getProvvedimentoPopup() {
		return provvedimentoPopup;
	}
	public void setProvvedimentoPopup(ProvvedimentoImpegnoModel provvedimentoPopup) {
		this.provvedimentoPopup = provvedimentoPopup;
	}
	public SoggettoImpegnoModel getSoggettoPopup() {
		return soggettoPopup;
	}
	public void setSoggettoPopup(SoggettoImpegnoModel soggettoPopup) {
		this.soggettoPopup = soggettoPopup;
	}
	public CapitoloUscitaGestione getCapitoloPopup() {
		return capitoloPopup;
	}
	public void setCapitoloPopup(CapitoloUscitaGestione capitoloPopup) {
		this.capitoloPopup = capitoloPopup;
	}
	public boolean isSoggettoSelezionatoPopup() {
		return soggettoSelezionatoPopup;
	}
	public void setSoggettoSelezionatoPopup(boolean soggettoSelezionatoPopup) {
		this.soggettoSelezionatoPopup = soggettoSelezionatoPopup;
	}
	public boolean isHasImpegnoSelezionatoXPopup() {
		return hasImpegnoSelezionatoXPopup;
	}
	public void setHasImpegnoSelezionatoXPopup(boolean hasImpegnoSelezionatoXPopup) {
		this.hasImpegnoSelezionatoXPopup = hasImpegnoSelezionatoXPopup;
	}
	public boolean isInPopup() {
		return inPopup;
	}
	public void setInPopup(boolean inPopup) {
		this.inPopup = inPopup;
	}
	public BigDecimal getDisponibilita() {
		return disponibilita;
	}
	public void setDisponibilita(BigDecimal disponibilita) {
		this.disponibilita = disponibilita;
	}
	public CapitoloImpegnoModel getCapitoloModelTrovatoDaServizio() {
		return capitoloModelTrovatoDaServizio;
	}
	public void setCapitoloModelTrovatoDaServizio(CapitoloImpegnoModel capitoloModelTrovatoDaServizio) {
		this.capitoloModelTrovatoDaServizio = capitoloModelTrovatoDaServizio;
	}
	public CapitoloUscitaGestione getCapitoloPopupNuovaLiquidazioneOrdinativo() {
		return capitoloPopupNuovaLiquidazioneOrdinativo;
	}
	public void setCapitoloPopupNuovaLiquidazioneOrdinativo(CapitoloUscitaGestione capitoloPopupNuovaLiquidazioneOrdinativo) {
		this.capitoloPopupNuovaLiquidazioneOrdinativo = capitoloPopupNuovaLiquidazioneOrdinativo;
	}
	public Impegno getImpegnoPopUpNuovaLiquidazioneOrdinativo() {
		return impegnoPopUpNuovaLiquidazioneOrdinativo;
	}
	public void setImpegnoPopUpNuovaLiquidazioneOrdinativo(Impegno impegnoPopUpNuovaLiquidazioneOrdinativo) {
		this.impegnoPopUpNuovaLiquidazioneOrdinativo = impegnoPopUpNuovaLiquidazioneOrdinativo;
	}
	public List<SubImpegno> getListaSubImpegniCompGuidata() {
		return listaSubImpegniCompGuidata;
	}
	public void setListaSubImpegniCompGuidata(List<SubImpegno> listaSubImpegniCompGuidata) {
		this.listaSubImpegniCompGuidata = listaSubImpegniCompGuidata;
	}
	public int getRadioSubImpegnoSelezionato() {
		return radioSubImpegnoSelezionato;
	}
	public void setRadioSubImpegnoSelezionato(int radioSubImpegnoSelezionato) {
		this.radioSubImpegnoSelezionato = radioSubImpegnoSelezionato;
	}
	public String getDescrizioneImpegnoPopup() {
		return descrizioneImpegnoPopup;
	}
	public void setDescrizioneImpegnoPopup(String descrizioneImpegnoPopup) {
		this.descrizioneImpegnoPopup = descrizioneImpegnoPopup;
	}
	public String getDescrizioneTipoImpegnoPopup() {
		return descrizioneTipoImpegnoPopup;
	}
	public void setDescrizioneTipoImpegnoPopup(String descrizioneTipoImpegnoPopup) {
		this.descrizioneTipoImpegnoPopup = descrizioneTipoImpegnoPopup;
	}

	public String getImportoImpegno() {
		return importoImpegno;
	}
	public void setImportoImpegno(String importoImpegno) {
		this.importoImpegno = importoImpegno;
	}
	public Integer getAnnoDoc() {
		return annoDoc;
	}
	public void setAnnoDoc(Integer annoDoc) {
		this.annoDoc = annoDoc;
	}
	public Integer getSoggDoc() {
		return soggDoc;
	}
	public void setSoggDoc(Integer soggDoc) {
		this.soggDoc = soggDoc;
	}
	public List<DocumentoSpesa> getListaRicercaDocumento() {
		return listaRicercaDocumento;
	}
	public void setListaRicercaDocumento(List<DocumentoSpesa> listaRicercaDocumento) {
		this.listaRicercaDocumento = listaRicercaDocumento;
	}
	public int getRadioDocumentoSelezionato() {
		return radioDocumentoSelezionato;
	}
	public void setRadioDocumentoSelezionato(int radioDocumentoSelezionato) {
		this.radioDocumentoSelezionato = radioDocumentoSelezionato;
	}
	public List<CodificaFin> getListaDocTipoSpesa() {
		return listaDocTipoSpesa;
	}
	public void setListaDocTipoSpesa(List<CodificaFin> listaDocTipoSpesa) {
		this.listaDocTipoSpesa = listaDocTipoSpesa;
	}
	public String getCodiceTipoDoc() {
		return codiceTipoDoc;
	}
	public void setCodiceTipoDoc(String codiceTipoDoc) {
		this.codiceTipoDoc = codiceTipoDoc;
	}
	public List<SubdocumentoSpesa> getListaRicercaSubDocumentoSpesa() {
		return listaRicercaSubDocumentoSpesa;
	}
	public void setListaRicercaSubDocumentoSpesa(
			List<SubdocumentoSpesa> listaRicercaSubDocumentoSpesa) {
		this.listaRicercaSubDocumentoSpesa = listaRicercaSubDocumentoSpesa;
	}
	public boolean isHasSubDocumentiSpesa() {
		return hasSubDocumentiSpesa;
	}
	public void setHasSubDocumentiSpesa(boolean hasSubDocumentiSpesa) {
		this.hasSubDocumentiSpesa = hasSubDocumentiSpesa;
	}
	public int getRadioSubDocumentoSelezionato() {
		return radioSubDocumentoSelezionato;
	}
	public void setRadioSubDocumentoSelezionato(int radioSubDocumentoSelezionato) {
		this.radioSubDocumentoSelezionato = radioSubDocumentoSelezionato;
	}
	public Soggetto getUltimoSoggettoSelezionatoCorrettamente() {
		return ultimoSoggettoSelezionatoCorrettamente;
	}
	public void setUltimoSoggettoSelezionatoCorrettamente(Soggetto ultimoSoggettoSelezionatoCorrettamente) {
		this.ultimoSoggettoSelezionatoCorrettamente = ultimoSoggettoSelezionatoCorrettamente;
	}
	public ProgettoImpegnoModel getProgettoRicerca() {
		return progettoRicerca;
	}
	public void setProgettoRicerca(ProgettoImpegnoModel progettoRicerca) {
		this.progettoRicerca = progettoRicerca;
	}
	public List<Progetto> getListaRicercaProgetto() {
		return listaRicercaProgetto;
	}
	public void setListaRicercaProgetto(List<Progetto> listaRicercaProgetto) {
		this.listaRicercaProgetto = listaRicercaProgetto;
	}
	public boolean isProgettoTrovato() {
		return progettoTrovato;
	}
	public void setProgettoTrovato(boolean progettoTrovato) {
		this.progettoTrovato = progettoTrovato;
	}
	public String getNumDoc() {
		return numDoc;
	}
	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}
	public ProvvedimentoImpegnoModel getProvvedimentoInserimento() {
		return provvedimentoInserimento;
	}
	public void setProvvedimentoInserimento(ProvvedimentoImpegnoModel provvedimentoInserimento) {
		this.provvedimentoInserimento = provvedimentoInserimento;
	}
	public boolean isProvvedimentoInserito() {
		return provvedimentoInserito;
	}
	public SoggettoImpegnoModel getSoggettoRicercaDue() {
		return soggettoRicercaDue;
	}
	public void setSoggettoRicercaDue(SoggettoImpegnoModel soggettoRicercaDue) {
		this.soggettoRicercaDue = soggettoRicercaDue;
	}
	public void setProvvedimentoInserito(boolean provvedimentoInserito) {
		this.provvedimentoInserito = provvedimentoInserito;
	}
	public List getListaStatiProvvedimenti() {
		return listaStatiProvvedimenti;
	}
	public void setListaStatiProvvedimenti(List listaStatiProvvedimenti) {
		this.listaStatiProvvedimenti = listaStatiProvvedimenti;
	}
	public BigInteger getNumeroProvvedimentoInserimentoDaResp() {
		return numeroProvvedimentoInserimentoDaResp;
	}
	public void setNumeroProvvedimentoInserimentoDaResp(BigInteger numeroProvvedimentoInserimentoDaResp) {
		this.numeroProvvedimentoInserimentoDaResp = numeroProvvedimentoInserimentoDaResp;
	}
	public boolean isImpegnoAppenaSelezionatoDaCompGuidata() {
		return impegnoAppenaSelezionatoDaCompGuidata;
	}
	public void setImpegnoAppenaSelezionatoDaCompGuidata(boolean impegnoAppenaSelezionatoDaCompGuidata) {
		this.impegnoAppenaSelezionatoDaCompGuidata = impegnoAppenaSelezionatoDaCompGuidata;
	}
	public List<Accertamento> getListaAccertamentiCompGuidata() {
		return listaAccertamentiCompGuidata;
	}
	public void setListaAccertamentiCompGuidata(List<Accertamento> listaAccertamentiCompGuidata) {
		this.listaAccertamentiCompGuidata = listaAccertamentiCompGuidata;
	}
	public Accertamento getAccertamentoPopup() {
		return accertamentoPopup;
	}
	public void setAccertamentoPopup(Accertamento accertamentoPopup) {
		this.accertamentoPopup = accertamentoPopup;
	}
	public CapitoloEntrataGestione getCapitoloEntrataPopup() {
		return capitoloEntrataPopup;
	}
	public void setCapitoloEntrataPopup(CapitoloEntrataGestione capitoloEntrataPopup) {
		this.capitoloEntrataPopup = capitoloEntrataPopup;
	}
	public List<TipoAmbito> getListaTipiAmbito() {
		return listaTipiAmbito;
	}
	public void setListaTipiAmbito(List<TipoAmbito> listaTipiAmbito) {
		this.listaTipiAmbito = listaTipiAmbito;
	}
	
	public RicercaTipiAmbito creaRequestRicercaTipiAmbito() {
		RicercaTipiAmbito request = creaRequest(RicercaTipiAmbito.class);
		request.setEnte(getEnte());
		return request;
	}
	public List<Cronoprogramma> getListaRicercaProgettoCronoprogrammi() {
		return listaRicercaProgettoCronoprogrammi;
	}
	public void setListaRicercaProgettoCronoprogrammi(List<Cronoprogramma> listaRicercaProgettoCronoprogrammi) {
		this.listaRicercaProgettoCronoprogrammi = listaRicercaProgettoCronoprogrammi;
	}
	
	public void moveListaRicercaProgettoToListaRicercaProgettoCronoprogrammi() {
		
		listaRicercaProgettoCronoprogrammi = new ArrayList<Cronoprogramma>();
		
		if(listaRicercaProgetto != null) {
			for (Progetto p : listaRicercaProgetto) {
				for (Cronoprogramma c : p.getCronoprogrammi()) {
					c.setProgetto(p);
					listaRicercaProgettoCronoprogrammi.add(c);
				}
			}
		}
		
		listaRicercaProgetto = null;
	}
	/**
	 * @return the controlloBloccoRagioneriaAbilitato
	 */
	public boolean isControlloBloccoRagioneriaAbilitato() {
		return controlloBloccoRagioneriaAbilitato;
	}
	/**
	 * @param controlloBloccoRagioneriaAbilitato the controlloBloccoRagioneriaAbilitato to set
	 */
	public void setControlloBloccoRagioneriaAbilitato(boolean controlloBloccoRagioneriaAbilitato) {
		this.controlloBloccoRagioneriaAbilitato = controlloBloccoRagioneriaAbilitato;
	}
	/**
	 * @return the skipControlloBloccoRagioneria
	 */
	public boolean isSkipControlloBloccoRagioneria() {
		return skipControlloBloccoRagioneria;
	}
	/**
	 * @param skipControlloBloccoRagioneria the skipControlloBloccoRagioneria to set
	 */
	public void setSkipControlloBloccoRagioneria(boolean skipControlloBloccoRagioneria) {
		this.skipControlloBloccoRagioneria = skipControlloBloccoRagioneria;
	}
	public boolean isDaCruscotto() {
		return daCruscotto;
	}
	public void setDaCruscotto(boolean daCruscotto) {
		this.daCruscotto = daCruscotto;
	}
	/**
	 * @return the componenteBilancioCapitoloAttivo
	 */
	public boolean isComponenteBilancioCapitoloAttivo() {
		return componenteBilancioCapitoloAttivo;
	}
	/**
	 * @param componenteBilancioCapitoloAttivo the componenteBilancioCapitoloAttivo to set
	 */
	public void setComponenteBilancioCapitoloAttivo(boolean componenteBilancioCapitoloAttivo) {
		this.componenteBilancioCapitoloAttivo = componenteBilancioCapitoloAttivo;
	}
	/**
	 * @return the importiComponentiCapitolo
	 */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}
	/**
	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}
	/**
	 * @return the importiAnniSuccessivi
	 */
	public ImportiCapitolo getImportiAnniSuccessivi() {
		return importiAnniSuccessivi;
	}
	/**
	 * @return the importiResidui
	 */
	public ImportiCapitolo getImportiResidui() {
		return importiResidui;
	}
	/**
	 * @param importiAnniSuccessivi the importiAnniSuccessivi to set
	 */
	public void setImportiAnniSuccessivi(ImportiCapitolo importiAnniSuccessivi) {
		this.importiAnniSuccessivi = importiAnniSuccessivi;
	}
	/**
	 * @param importiResidui the importiResidui to set
	 */
	public void setImportiResidui(ImportiCapitolo importiResidui) {
		this.importiResidui = importiResidui;
	}

	/**
	 * @return the competenzaStanziamento
	 */
	public ImportiCapitoloPerComponente getCompetenzaStanziamento() {
		return competenzaStanziamento;
	}
	/**
	 * @return the competenzaImpegnato
	 */
	public ImportiCapitoloPerComponente getCompetenzaImpegnato() {
		return competenzaImpegnato;
	}
	/**
	 * @return the residuiEffettivi
	 */
	public ImportiCapitoloPerComponente getResiduiEffettivi() {
		return residuiEffettivi;
	}
	/**
	 * @return the residuiPresunti
	 */
	public ImportiCapitoloPerComponente getResiduiPresunti() {
		return residuiPresunti;
	}
	/**
	 * @return the cassaStanziato
	 */
	public ImportiCapitoloPerComponente getCassaStanziato() {
		return cassaStanziato;
	}
	/**
	 * @return the cassaPagato
	 */
	public ImportiCapitoloPerComponente getCassaPagato() {
		return cassaPagato;
	}
	/**
	 * @param competenzaStanziamento the competenzaStanziamento to set
	 */
	public void setCompetenzaStanziamento(ImportiCapitoloPerComponente competenzaStanziamento) {
		this.competenzaStanziamento = competenzaStanziamento;
	}
	/**
	 * @param competenzaImpegnato the competenzaImpegnato to set
	 */
	public void setCompetenzaImpegnato(ImportiCapitoloPerComponente competenzaImpegnato) {
		this.competenzaImpegnato = competenzaImpegnato;
	}
	/**
	 * @param residuiEffettivi the residuiEffettivi to set
	 */
	public void setResiduiEffettivi(ImportiCapitoloPerComponente residuiEffettivi) {
		this.residuiEffettivi = residuiEffettivi;
	}
	/**
	 * @param residuiPresunti the residuiPresunti to set
	 */
	public void setResiduiPresunti(ImportiCapitoloPerComponente residuiPresunti) {
		this.residuiPresunti = residuiPresunti;
	}
	/**
	 * @param cassaStanziato the cassaStanziato to set
	 */
	public void setCassaStanziato(ImportiCapitoloPerComponente cassaStanziato) {
		this.cassaStanziato = cassaStanziato;
	}
	/**
	 * @param cassaPagato the cassaPagato to set
	 */
	public void setCassaPagato(ImportiCapitoloPerComponente cassaPagato) {
		this.cassaPagato = cassaPagato;
	}
	
	
	/**
	 * @return the annoStanziamentoCapitoloInizio
	 */
	public Integer getAnnoStanziamentoCapitoloInizio() {
		return annoStanziamentoCapitoloInizio;
	}
	/**
	 * @param annoStanziamentoCapitoloInizio the annoStanziamentoCapitoloInizio to set
	 */
	public void setAnnoStanziamentoCapitoloInizio(Integer annoStanziamentoCapitoloInizio) {
		this.annoStanziamentoCapitoloInizio = annoStanziamentoCapitoloInizio;
	}
	/**
	 * @return the importiCapitoloUscitaPrevisione0
	 */
	/**
	 * @return the importiEx
	 */
	public ImportiCapitolo getImportiEx() {
		return importiEx;
	}
	/**
	 * @return the importiEx1
	 */
	public ImportiCapitolo getImportiEx1() {
		return importiEx1;
	}
	/**
	 * @return the importiEx2
	 */
	public ImportiCapitolo getImportiEx2() {
		return importiEx2;
	}
	/**
	 * @return the importiEx3
	 */
	public ImportiCapitolo getImportiEx3() {
		return importiEx3;
	}
	/**
	 * @param importiEx the importiEx to set
	 */
	public void setImportiEx(ImportiCapitolo importiEx) {
		this.importiEx = importiEx;
	}
	/**
	 * @param importiEx1 the importiEx1 to set
	 */
	public void setImportiEx1(ImportiCapitolo importiEx1) {
		this.importiEx1 = importiEx1;
	}
	/**
	 * @param importiEx2 the importiEx2 to set
	 */
	public void setImportiEx2(ImportiCapitolo importiEx2) {
		this.importiEx2 = importiEx2;
	}
	/**
	 * @param importiEx3 the importiEx3 to set
	 */
	public void setImportiEx3(ImportiCapitolo importiEx3) {
		this.importiEx3 = importiEx3;
	}
	/**
	 * @return the importiCapitoloUscita0
	 */
	public ImportiCapitolo getImportiCapitoloUscita0() {
		return importiCapitoloUscita0;
	}
	/**
	 * @return the importiCapitoloUscita1
	 */
	public ImportiCapitolo getImportiCapitoloUscita1() {
		return importiCapitoloUscita1;
	}
	/**
	 * @return the importiCapitoloUscita2
	 */
	public ImportiCapitolo getImportiCapitoloUscita2() {
		return importiCapitoloUscita2;
	}
	/**
	 * @param importiCapitoloUscita0 the importiCapitoloUscita0 to set
	 */
	public void setImportiCapitoloUscita0(ImportiCapitolo importiCapitoloUscita0) {
		this.importiCapitoloUscita0 = importiCapitoloUscita0;
	}
	/**
	 * @param importiCapitoloUscita1 the importiCapitoloUscita1 to set
	 */
	public void setImportiCapitoloUscita1(ImportiCapitolo importiCapitoloUscita1) {
		this.importiCapitoloUscita1 = importiCapitoloUscita1;
	}
	/**
	 * @param importiCapitoloUscita2 the importiCapitoloUscita2 to set
	 */
	public void setImportiCapitoloUscita2(ImportiCapitolo importiCapitoloUscita2) {
		this.importiCapitoloUscita2 = importiCapitoloUscita2;
	}
	/**
	 * @return the componenteBilancioCapitoloAttivoRicerca
	 */
	public boolean isComponenteBilancioCapitoloAttivoRicerca() {
		return componenteBilancioCapitoloAttivoRicerca;
	}
	/**
	 * @param componenteBilancioCapitoloAttivoRicerca the componenteBilancioCapitoloAttivoRicerca to set
	 */
	public void setComponenteBilancioCapitoloAttivoRicerca(boolean componenteBilancioCapitoloAttivoRicerca) {
		this.componenteBilancioCapitoloAttivoRicerca = componenteBilancioCapitoloAttivoRicerca;
	}
	

	
	
}
