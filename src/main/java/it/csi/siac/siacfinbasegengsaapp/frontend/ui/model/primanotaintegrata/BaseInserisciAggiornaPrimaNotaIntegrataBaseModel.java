/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinBaseHelper;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommon.util.date.DateConverter;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.GenericContabilitaGeneraleModel;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;
/**
 * Classe base di model per l'inserimento e l'aggiornamento della PRIMA NOTA Integrata EP.
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/04/2015
 * @version 1.1.0 - 12/10/2015
 * @param <E> la tipizzazione dell'entita
 *
 */
public abstract class BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<E extends Entita> extends GenericContabilitaGeneraleModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = 1990797233383432910L;
	private Integer uidPrimaNota;
	private RegistrazioneMovFin registrazioneMovFin;
	private PrimaNota primaNota;
	private List<MovimentoEP> listaMovimentoEP = new ArrayList<MovimentoEP>();
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	// Differenza totali dare avere
	private BigDecimal daRegistrare = BigDecimal.ZERO;
	private String descrMovimentoFinanziario;
	private String descrizioneProposta;

	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	
	private Evento evento;
	private TipoEvento tipoEvento;
	private CausaleEP causaleEP;
	
	private Soggetto soggettoMovimentoFIN;
	
	// STEP 2
	private List<Conto> listaConto = new ArrayList<Conto>();
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	
	
	private boolean contiCausale = false;
	private boolean importoFromProposto = false;
	// da modale
	private Integer indiceConto;
	private BigDecimal importo;
	private OperazioneSegnoConto operazioneSegnoConto;
	// da collapse
	private Conto conto;
	private BigDecimal importoCollapse;
	private OperazioneSegnoConto operazioneSegnoContoCollapse;
	
	// modale compilazione guidata conto
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	
	// per gestione classe di conciliazione, dove presente
	private ClassificatoreGerarchico classificatoreGerarchico;
	private Conto contoDaSostituire;
	
	// SIAC-4805
	private List<Conto> listaContoDaClasseConciliazione = new ArrayList<Conto>();
	
	// SIAC-5336: presente sulla superclasse per evitare di duplicare il campo nei model. Utilizzato solo nei [...]Valida[...]GSA[...]
	private ClassificatoreGSA classificatoreGSA;
	//SIAC-5333
	private String campoTitoloPagina = "";
	
	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}

	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}

	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return registrazioneMovFin;
	}

	/**
	 * @param registrazioneMovFin the registrazioneMovFin to set
	 */
	public void setRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
	}

	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the listaMovimentoEP
	 */
	public List<MovimentoEP> getListaMovimentoEP() {
		return listaMovimentoEP;
	}

	/**
	 * @param listaMovimentoEP the listaMovimentoEP to set
	 */
	public void setListaMovimentoEP(List<MovimentoEP> listaMovimentoEP) {
		this.listaMovimentoEP = listaMovimentoEP;
	}

	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return totaleDare;
	}

	/**
	 * @param totaleDare the totaleDare to set
	 */
	public void setTotaleDare(BigDecimal totaleDare) {
		this.totaleDare = totaleDare != null ? totaleDare : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return totaleAvere;
	}

	/**
	 * @param totaleAvere the totaleAvere to set
	 */
	public void setTotaleAvere(BigDecimal totaleAvere) {
		this.totaleAvere = totaleAvere != null ? totaleAvere : BigDecimal.ZERO;
	}

	/**
	 * @return the daRegistrare
	 */
	public BigDecimal getDaRegistrare() {
		return daRegistrare;
	}

	/**
	 * @param daRegistrare the daRegistrare to set
	 */
	public void setDaRegistrare(BigDecimal daRegistrare) {
		this.daRegistrare = daRegistrare != null ? daRegistrare : BigDecimal.ZERO;
	}

	/**
	 * @return the descrMovimentoFinanziario
	 */
	public String getDescrMovimentoFinanziario() {
		return descrMovimentoFinanziario;
	}

	/**
	 * @param descrMovimentoFinanziario the descrMovimentoFinanziario to set
	 */
	public void setDescrMovimentoFinanziario(String descrMovimentoFinanziario) {
		this.descrMovimentoFinanziario = descrMovimentoFinanziario;
	}

	/**
	 * @return the descrizioneProposta
	 */
	public String getDescrizioneProposta() {
		return descrizioneProposta;
	}

	/**
	 * @param descrizioneProposta the descrizioneProposta to set
	 */
	public void setDescrizioneProposta(String descrizioneProposta) {
		this.descrizioneProposta = descrizioneProposta;
	}

	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}

	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP != null ? listaCausaleEP : new ArrayList<CausaleEP>();
	}


	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}


	/**
	 * @param causaleEP the causaleEp to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}

	/**
	 * @return the soggettoMovimentoFIN
	 */
	public final Soggetto getSoggettoMovimentoFIN() {
		return soggettoMovimentoFIN;
	}

	/**
	 * @param soggettoMovimentoFIN the soggettoMovimentoFIN to set
	 */
	public final void setSoggettoMovimentoFIN(Soggetto soggettoMovimentoFIN) {
		this.soggettoMovimentoFIN = soggettoMovimentoFIN;
	}

	/**
	 * @return the indiceConto
	 */
	public Integer getIndiceConto() {
		return indiceConto;
	}

	/**
	 * @param indiceConto the indiceConto to set
	 */
	public void setIndiceConto(Integer indiceConto) {
		this.indiceConto = indiceConto;
	}

	/**
	 * @return the listaConto
	 */
	public List<Conto> getListaConto() {
		return listaConto;
	}

	/**
	 * @param listaConto the listaConto to set
	 */
	public void setListaConto(List<Conto> listaConto) {
		this.listaConto = listaConto != null ? listaConto : new ArrayList<Conto>();
	}

	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}

	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

	/**
	 * @return the listaMovimentoDettaglio
	 */
	public List<MovimentoDettaglio> getListaMovimentoDettaglio() {
		return listaMovimentoDettaglio;
	}

	/**
	 * @param listaMovimentoDettaglio the listaMovimentoDettaglio to set
	 */
	public void setListaMovimentoDettaglio(List<MovimentoDettaglio> listaMovimentoDettaglio) {
		this.listaMovimentoDettaglio = listaMovimentoDettaglio != null ? listaMovimentoDettaglio : new ArrayList<MovimentoDettaglio>();
	}

	/**
	 * @return the listaElementoScrittura
	 */
	public List<ElementoScritturaPrimaNotaIntegrata> getListaElementoScrittura() {
		return listaElementoScrittura;
	}

	/**
	 * @param listaElementoScrittura the listaElementoScrittura to set
	 */
	public void setListaElementoScrittura(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura) {
		this.listaElementoScrittura = listaElementoScrittura != null ? listaElementoScrittura : new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	}

	
	/**
	 * @return the listaElementoScritturaPerElaborazione
	 */
	public List<ElementoScritturaPrimaNotaIntegrata> getListaElementoScritturaPerElaborazione() {
		return listaElementoScritturaPerElaborazione;
	}

	/**
	 * @param listaElementoScritturaPerElaborazione the listaElementoScritturaPerElaborazione to set
	 */
	public void setListaElementoScritturaPerElaborazione(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione) {
		this.listaElementoScritturaPerElaborazione = listaElementoScritturaPerElaborazione != null ? listaElementoScritturaPerElaborazione : new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	}
	
	/**
	 * @return the contiCausale
	 */
	public boolean isContiCausale() {
		return contiCausale;
	}

	/**
	 * @param contiCausale the contiCausale to set
	 */
	public void setContiCausale(boolean contiCausale) {
		this.contiCausale = contiCausale;
	}

	/**
	 * @return the importoFromProposto
	 */
	public boolean isImportoFromProposto() {
		return importoFromProposto;
	}

	/**
	 * @param importoFromProposto the importoFromProposto to set
	 */
	public void setImportoFromProposto(boolean importoFromProposto) {
		this.importoFromProposto = importoFromProposto;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @return the operazioneSegnoConto
	 */
	public OperazioneSegnoConto getOperazioneSegnoConto() {
		return operazioneSegnoConto;
	}

	/**
	 * @param operazioneSegnoConto the operazioneSegnoConto to set
	 */
	public void setOperazioneSegnoConto(OperazioneSegnoConto operazioneSegnoConto) {
		this.operazioneSegnoConto = operazioneSegnoConto;
	}

	/**
	 * @return the importoCollapse
	 */
	public BigDecimal getImportoCollapse() {
		return importoCollapse;
	}

	/**
	 * @param importoCollapse the importoCollapse to set
	 */
	public void setImportoCollapse(BigDecimal importoCollapse) {
		this.importoCollapse = importoCollapse;
	}

	/**
	 * @return the operazioneSegnoContoCollapse
	 */
	public OperazioneSegnoConto getOperazioneSegnoContoCollapse() {
		return operazioneSegnoContoCollapse;
	}

	/**
	 * @param operazioneSegnoContoCollapse the operazioneSegnoContoCollapse to set
	 */
	public void setOperazioneSegnoContoCollapse(
			OperazioneSegnoConto operazioneSegnoContoCollapse) {
		this.operazioneSegnoContoCollapse = operazioneSegnoContoCollapse;
	}

	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi != null ? listaClassi : new ArrayList<ClassePiano>();
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}
	
	/**
	 * @return the contoDaSostituire
	 */
	public Conto getContoDaSostituire() {
		return contoDaSostituire;
	}

	/**
	 * @param contoDaSostituire the contoDaSostituire to set
	 */
	public void setContoDaSostituire(Conto contoDaSostituire) {
		this.contoDaSostituire = contoDaSostituire;
	}
	
	/**
	 * @return the classificatoreGerarchico
	 */
	public ClassificatoreGerarchico getClassificatoreGerarchico() {
		return classificatoreGerarchico;
	}

	/**
	 * @param classificatoreGerarchico the classificatoreGerarchico to set
	 */
	public void setClassificatoreGerarchico(
			ClassificatoreGerarchico classificatoreGerarchico) {
		this.classificatoreGerarchico = classificatoreGerarchico;
	}
	
	/**
	 * Gets the lista conto da classe conciliazione.
	 *
	 * @return the lista conto da classe conciliazione
	 */
	public List<Conto> getListaContoDaClasseConciliazione() {
		return listaContoDaClasseConciliazione;
	}

	/**
	 * Sets the lista conto da classe conciliazione.
	 *
	 * @param listaContoDaClasseConciliazione the new lista conto da classe conciliazione
	 */
	public void setListaContoDaClasseConciliazione(List<Conto> listaContoDaClasseConciliazione) {
		this.listaContoDaClasseConciliazione = listaContoDaClasseConciliazione;
	}

//	/**
//	 * @return the consultazioneHelper
//	 */
//	public H getConsultazioneHelper() {
//		return this.consultazioneHelper;
//	}
//
//	/**
//	 * @param consultazioneHelper the consultazioneHelper to set
//	 */
//	public void setConsultazioneHelper(H consultazioneHelper) {
//		this.consultazioneHelper = consultazioneHelper;
//	}

	/**
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSA() {
		return this.classificatoreGSA;
	}

	/**
	 * @param classificatoreGSA the classificatoreGSA to set
	 */
	public void setClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}

	/**
	 * Gets the campo titolo pagina.
	 *
	 * @return the campoTitoloPagina
	 */
	public String getCampoTitoloPagina() {
		return campoTitoloPagina;
	}

	/**
	 * Sets the campo titolo pagina.
	 *
	 * @param campoTitoloPagina the campoTitoloPagina to set
	 */
	public void setCampoTitoloPagina(String campoTitoloPagina) {
		this.campoTitoloPagina = campoTitoloPagina;
	}

	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}

	/**
	 * @return the aggiornamento
	 */
	public abstract boolean isAggiornamento();
	
	/**
	 * @return the validazione flag
	 */
	public abstract boolean isValidazione();
	
	/**
	 * @return the isFromRegistrazione
	 */
	public abstract boolean isFromRegistrazione();

	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the consultazioneSubpath
	 */
	public abstract String getConsultazioneSubpath();

	/**
	 * @return the urlAnnullaStep1
	 */
	public String getUrlAnnullaStep1(){
		return getBaseUrl() + "_annullaStep1.do";
	}
	
	/**
	 * @return the urlAnnullaStep2
	 */
	public String getUrlAnnullaStep2(){
		return getBaseUrl() + "_annullaStep2.do";
	}
	
	/**
	 * @return the urlStep1
	 */
	public String getUrlStep1() {
		return getBaseUrl() + "_completeStep1";
	}
	
	/**
	 * @return the urlBackToStep1
	 */
	public String getUrlBackToStep1() {
		return getBaseUrl() + "_backToStep1";
	}
	
	/**
	 * @return the urlStep2
	 */
	public String getUrlStep2() {
		return getBaseUrl() + "_completeStep2";
	}
	
	/**
	 * @return the urlStep3
	 */
	public String getUrlStep3() {
		return getBaseUrl() + "_completeStep3";
	}
	
	/**
	 * @return the urlStep3
	 */
	public String getUrlSalva() {
		return getBaseUrl() + "_completeSalva";
	}

	/**
	 * @return the descrRichiesta
	 */
	public String getDescrRichiesta() {
		if(getRegistrazioneMovFin() == null || getRegistrazioneMovFin().getEvento() == null || getRegistrazioneMovFin().getEvento().getTipoCollegamento() == null) {
			return "";
		}
		return new StringBuilder("Richiesta: ")
			.append("<span>")
			// Data della registrazione, nel formato DD/MM/YYYY
			.append(DateConverter.formatDateAsString(getRegistrazioneMovFin().getDataRegistrazione()))
			.append("</span>")
			.append(" - ")
			.append("<span>")
			// Collegamento
			.append(getRegistrazioneMovFin().getEvento().getTipoCollegamento().getDescrizione())
			.append("</span>")
			.append(" - ")
			.append("<span>")
			// Evento
			.append(getRegistrazioneMovFin().getEvento().getDescrizione())
			.append("</span>")
			.toString();
	}
	
	/**
	 * @return the stringaRiepiloCausaleEPStep1
	 */
	public String getStringaRiepiloCausaleEPStep1() {
		return computaStringaCausaleEPPrimaNota();
	}

	/**
	 * @return the stringaRiepiloDescrizioneStep1
	 */
	public String getStringaRiepiloDescrizioneStep1() {
		return computaStringaDescrizionePrimaNota();
	}
	
	/**
	 * @return l'intestazione dei dati della richiesta
	 */
	public String getIntestazioneRichiesta () {
		return new StringBuilder()
			.append ("Richiesta: ")
			.append(getDescrRichiesta())
			.toString();
	}
	
	/**
	 * @return l'intestazione dei dati del movimento finanziario
	 */
	public String getIntestazioneMovimentoFinanziario (){
		return new StringBuilder()
			.append("Movimento finanziario ")
			.append(getDescrMovimentoFinanziario())
			.toString();
		
	}
	
	/**
	 * Calcolo della stringa del riepilogo causale
	 * 
	 * @return la stringa del numero richiesta
	 */
	protected String computaStringaCausaleEPPrimaNota() {
		if(getCausaleEP() == null || getPrimaNota() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Causale: ")
			.append(causaleEP.getCodice())
			.append(" - registrata il ")
			.append(DateConverter.formatDateAsString(primaNota.getDataRegistrazione()))
			.toString();
	}
	
	/**
	 * Calcolo della stringa della data richiesta.
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaDescrizionePrimaNota () {
		if(getPrimaNota() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Descrizione ")
			.append(primaNota.getDescrizione())
			.toString();
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareCausale creaRequestRicercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale request = creaRequest(RicercaSinteticaModulareCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		causEpPerRequest.setTipoCausale(TipoCausale.Integrata);
		if(Ambito.AMBITO_FIN.equals(getAmbito())) {
			causEpPerRequest.setElementoPianoDeiConti(getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato());
		}
		causEpPerRequest.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		causEpPerRequest.setAmbito(getAmbito());
		causEpPerRequest.setEventi(Arrays.asList(getEvento()));
		
		request.setCausaleEP(causEpPerRequest);
		request.setBilancio(getBilancio());
		request.setTipoEvento(getTipoEvento());
		request.setParametriPaginazione(creaParametriPaginazione(100));
		request.setCausaleEPModelDetails(CausaleEPModelDetail.Classif, CausaleEPModelDetail.Conto, CausaleEPModelDetail.Soggetto);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param contoDaCercare il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto contoDaCercare) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		
		contoDaCercare.setAmbito(getAmbito());
		
		request.setBilancio(getBilancio());
		request.setConto(contoDaCercare);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioConto}.
	 * @param contoDaCercare il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioConto creaRequestRicercaDettaglioConto(Conto contoDaCercare) {
		RicercaDettaglioConto request = creaRequest(RicercaDettaglioConto.class);
		
		contoDaCercare.setAmbito(getAmbito());
		
		request.setBilancio(getBilancio());
		request.setConto(contoDaCercare);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNota}.
	 *
	 * @return la request creata
	 */
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota request = creaRequest(ValidaPrimaNota.class);
		
		PrimaNota pn = new PrimaNota();
		pn.setUid(getPrimaNota().getUid());
		pn.setAmbito(getAmbito());
		
		request.setPrimaNota(pn);

		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RegistraPrimaNotaIntegrata}.
	 * 
	 * @return la request creata
	 */
	public RegistraPrimaNotaIntegrata creaRequestRegistraPrimaNotaIntegrata (){
		RegistraPrimaNotaIntegrata request = creaRequest(RegistraPrimaNotaIntegrata.class);
		
		getPrimaNota().setBilancio(getBilancio());
		getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		/* imposto la lista dei movimenti*/
		getPrimaNota().setListaMovimentiEP(ottieniListaMovimentiEPConAmbito());
		getPrimaNota().setAmbito(getAmbito());
		
		request.setPrimaNota(getPrimaNota());
		request.setIsAggiornamento(Boolean.valueOf(isAggiornamento()));
		request.setIsDaValidare(Boolean.valueOf(isValidazione()));
		// SIAC-5336
		if(isValidazione() && Ambito.AMBITO_GSA.equals(getAmbito())) {
			// Aggiunto solo per GSA
			request.setClassificatoreGSA(getClassificatoreGSA() != null && getClassificatoreGSA().getUid() != 0? getClassificatoreGSA() : null);
		}
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RegistraPrimaNotaIntegrata} per il controllo di eseguibilit&agrave;.
	 * 
	 * @return la request creata
	 */
	public RegistraPrimaNotaIntegrata creaRequestRegistraPrimaNotaIntegrataPerCheckEsecuzione (){
		RegistraPrimaNotaIntegrata request = creaRequest(RegistraPrimaNotaIntegrata.class);
		
		MovimentoEP mep = new MovimentoEP();
		mep.setRegistrazioneMovFin(getRegistrazioneMovFin());
		
		List<MovimentoEP> lmep = new ArrayList<MovimentoEP>();
		lmep.add(mep);
		
		PrimaNota pn = new PrimaNota();
		pn.setListaMovimentiEP(lmep);
		
		request.setPrimaNota(pn);
		request.setCheckOnlyElaborazioneAttiva(Boolean.TRUE);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNotaIntegrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNotaIntegrata creaRequestRicercaDettaglioPrimaNotaIntegrata() {
		return creaRequest(RicercaDettaglioPrimaNotaIntegrata.class);
	}
	
	/**
	 * Impostazione dei dati nel model.
	 */
	public void impostaDatiNelModel() {
		if (getPrimaNota() == null) {
			setPrimaNota(new PrimaNota());
		}
		getPrimaNota().setDataRegistrazione(new Date());
	}
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		String suffix = "_" + getAmbito().getSuffix();
		return creaRequestRicercaCodifiche("ClassePiano" + suffix);
	}

	/**
	 * Ottiene la lista dei movimenti EP con l'ambito correttamente popolato.
	 * 
	 * @return la lista dei movimenti
	 */
	private List<MovimentoEP> ottieniListaMovimentiEPConAmbito() {
		for(MovimentoEP mep : getListaMovimentoEP()) {
			mep.setAmbito(getAmbito());
		}
		return getListaMovimentoEP();
	}
	
}
