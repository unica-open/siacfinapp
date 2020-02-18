/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GestisciImpegnoStep1Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//model del capitolo
	private CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
	//model del soggetto
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//support per provvedimento:
	private ProvvedimentoImpegnoModel provvedimentoSupport = new ProvvedimentoImpegnoModel();
	
	//progetto model:
	private ProgettoImpegnoModel progettoImpegno = new ProgettoImpegnoModel();
	
	//soggetto
	private Soggetto soggettoImpegno = new Soggetto();
	
	//capitoli:
	private CapitoloUscitaGestione capitoloImpegno = new CapitoloUscitaGestione();
	private CapitoloEntrataGestione capitoloAccertamento = new CapitoloEntrataGestione();
	
	
	//altri dati:
	private Integer annoImpegno, annoFinanziamento, numeroFinanziamento, annoImpRiacc, numeroPluriennali, 
					annoImpOrigine, numImpOrigine, uid, numeroImpegno, numImpRiacc, annoAccertamentoVincolo, numeroAccertamentoVincolo;
	private String oggettoImpegno, stato, cig, cup, progetto,riaccertato,pluriennale, tipoImpegno, titolo , statoOperativo, 
				   descrizioneStatoOperativoMovimento; 
	
	private String attivitaPrevista;
	private String cronoprogramma;

	private Integer idSpesaCronoprogramma;
	private Integer idCronoprogramma;
	
	
	private String soggettoDurc;
	
	//check vari:
	private boolean checkproseguiRiacc,checkproseguiOrigin,checkproseguiMovimentoSpesa;
	
	//data stato operativo:
	private Date dataStatoOperativoMovimento;
	
	private String flagFattura;
	private String flagCorrispettivo;
	
	//flag attiva gsa:
	private String flagAttivaGsa;
	private List<String> listflagAttivaGsa = new ArrayList<String>();
	
	private boolean daRicerca, operazioneInserimento, apriTabVincoli, inserisciVincoloBtn, parereFinanziario;
	
	//vincoli:
	private List<VincoloImpegno> listaVincoliImpegno;
	private DettaglioVincoloModel dettaglioVincolo;
	
	 //importi:
	private BigDecimal importoImpegno, importoImpegnoMod , importoImpegnoIniziale, importoVincolo, totaleImportoVincoli, totaleImportoDaCollegare;	
	private String scadenza , scadenzaOld, importoFormattato, importoVincoloFormattato, importoInModificaFormattato;
	
	private BigDecimal disponibileLiquidareDaFunction;
	
	private String importoUtilizzabileFormattato;
	private BigDecimal disponibilitaUtilizzare;
	
	//da riaccertamento:
	private List<String> daRiaccertamento = new ArrayList<String>();
	//pluriennale:
	private List<String> implPluriennale = new ArrayList<String>();	
	
	//selezionati:
	private boolean soggettoSelezionato = false, capitoloSelezionato = false, provvedimentoSelezionato = false;
	private boolean progettoSelezionato = false;
	
	// modale della ricerca accertamento
	private AccertamentoRicercaModel accertamentoRicerca = new AccertamentoRicercaModel();
	private List<Accertamento> listaAccPerVincoli;
	private boolean trovatiAccPerVincolo, portaAdAltezzaVincoli;
	private Accertamento accertamentoPerVincolo;
	
	//PER AVANZO VINCOLO (FPV / Avanzo):
	private String tipoVincolo;//Accertamento oppure Avanzovincolo
	private String avanzoVincoloSelezionato;
	private List<String> sceltaAccertamentoAvanzoList = new ArrayList<String>();
	private String importoAvanzoVincoloFormattato;
	
	//parere finanziario:
	private Date parereFinanziarioDataModifica;
	private String parereFinanziarioLoginOperazione;
	
	//alert
	private String alertDiConfermaModificaProvvedimento;
	private String alertMovimentoCollegatoAOrdinativiOLiquidazioni;
	
	private String messaggioDiConfermaModificaVincoli;

	//Prenotazione:
	private List<String> daPrenotazione = new ArrayList<String>();
	private String prenotazione;
	private Boolean prenotazioneLiquidabile = Boolean.FALSE;
	private Boolean hiddenPerPrenotazioneLiquidabile;
	
	//Di cassa economale:
	private List<String> diCassaEconomale = new ArrayList<String>();
	private String cassaEconomale;
	
	//Di cassa economale:
	private List<String> scelteFrazionabile = new ArrayList<String>();
	private String frazionabile;
	private Integer annoScritturaEconomicoPatrimoniale ;
	
	
	//Tipo debito siope:
	private List<String> scelteTipoDebitoSiope = new ArrayList<String>();
	private String tipoDebitoSiope;
	
	
	//motivazione assenza cig:
	private String motivazioneAssenzaCig;
	
	
	private Boolean imprtoVincoliModificato = Boolean.FALSE ;
	
//	SIAC-5943
	private boolean passaggioAStatoDefinitivo = false; 
	
	//SIAC-6702
	private boolean saltaControlloLegamiStoricizzati;
	

	/**
	 * @return the alertMovimentoCollegatoAOrdinativiOLiquidazioni
	 */
	public String getAlertMovimentoCollegatoAOrdinativiOLiquidazioni() {
		return alertMovimentoCollegatoAOrdinativiOLiquidazioni;
	}

	/**
	 * @param alertMovimentoCollegatoAOrdinativiOLiquidazioni the alertMovimentoCollegatoAOrdinativiOLiquidazioni to set
	 */
	public void setAlertMovimentoCollegatoAOrdinativiOLiquidazioni(String alertMovimentoCollegatoAOrdinativiOLiquidazioni) {
		this.alertMovimentoCollegatoAOrdinativiOLiquidazioni = alertMovimentoCollegatoAOrdinativiOLiquidazioni;
	}

	public String getAlertDiConfermaModificaProvvedimento() {
		return alertDiConfermaModificaProvvedimento;
	}
	
	public void setAlertDiConfermaModificaProvvedimento(String alertDiConfermaModificaProvvedimento) {
		this.alertDiConfermaModificaProvvedimento = alertDiConfermaModificaProvvedimento;
	}
	/**
	 * @return the parereFinanziario
	 */
	public boolean isParereFinanziario() {
		return parereFinanziario;
	}
	/**
	 * @param parereFinanziario the parereFinanziario to set
	 */
	public void setParereFinanziario(boolean parereFinanziario) {
		this.parereFinanziario = parereFinanziario;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public List<String> getImplPluriennale() {
		return implPluriennale;
	}
	public void setImplPluriennale(List<String> implPluriennale) {
		this.implPluriennale = implPluriennale;
	}
	public List<String> getDaRiaccertamento() {
		return daRiaccertamento;
	}
	public void setDaRiaccertamento(List<String> daRiaccertamento) {
		this.daRiaccertamento = daRiaccertamento;
	}
	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloImpegnoModel capitolo) {
		this.capitolo = capitolo;
	}
	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public Integer getAnnoFinanziamento() {
		return annoFinanziamento;
	}
	public void setAnnoFinanziamento(Integer annoFinanziamento) {
		this.annoFinanziamento = annoFinanziamento;
	}
	public Integer getNumeroFinanziamento() {
		return numeroFinanziamento;
	}
	public void setNumeroFinanziamento(Integer numeroFinanziamento) {
		this.numeroFinanziamento = numeroFinanziamento;
	}
	public Integer getAnnoImpRiacc() {
		return annoImpRiacc;
	}
	public void setAnnoImpRiacc(Integer annoImpRiacc) {
		this.annoImpRiacc = annoImpRiacc;
	}
	public Integer getNumImpRiacc() {
		return numImpRiacc;
	}
	public void setNumImpRiacc(Integer numImpRiacc) {
		this.numImpRiacc = numImpRiacc;
	}
	public Integer getNumeroPluriennali() {
		return numeroPluriennali;
	}
	public void setNumeroPluriennali(Integer numeroPluriennali) {
		this.numeroPluriennali = numeroPluriennali;
	}
	public Integer getAnnoImpOrigine() {
		return annoImpOrigine;
	}
	public void setAnnoImpOrigine(Integer annoImpOrigine) {
		this.annoImpOrigine = annoImpOrigine;
	}
	public Integer getNumImpOrigine() {
		return numImpOrigine;
	}
	public void setNumImpOrigine(Integer numImpOrigine) {
		this.numImpOrigine = numImpOrigine;
	}
	public String getOggettoImpegno() {
		return oggettoImpegno;
	}
	public void setOggettoImpegno(String oggettoImpegno) {
		this.oggettoImpegno = oggettoImpegno;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getCig() {
		return cig;
	}
	public void setCig(String cig) {
		this.cig = cig;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getProgetto() {
		return progetto;
	}
	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}
	public String getTipoImpegno() {
		return tipoImpegno;
	}
	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}
	public BigDecimal getImportoImpegno() {
		return importoImpegno;
	}
	public void setImportoImpegno(BigDecimal importoImpegno) {
		this.importoImpegno = importoImpegno;
	}
	
	public String getRiaccertato() {
		return riaccertato;
	}
	public void setRiaccertato(String riaccertato) {
		this.riaccertato = riaccertato;
	}
	
	public String getPluriennale() {
		return pluriennale;
	}
	public void setPluriennale(String pluriennale) {
		this.pluriennale = pluriennale;
	}
	public BigDecimal getImportoImpegnoMod() {
		return importoImpegnoMod;
	}
	public void setImportoImpegnoMod(BigDecimal importoImpegnoMod) {
		this.importoImpegnoMod = importoImpegnoMod;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(Integer numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public Soggetto getSoggettoImpegno() {
		return soggettoImpegno;
	}
	public void setSoggettoImpegno(Soggetto soggettoImpegno) {
		this.soggettoImpegno = soggettoImpegno;
	}
	public CapitoloUscitaGestione getCapitoloImpegno() {
		return capitoloImpegno;
	}
	public void setCapitoloImpegno(CapitoloUscitaGestione capitoloImpegno) {
		this.capitoloImpegno = capitoloImpegno;
	}
	public String getScadenza() {
		return scadenza;
	}
	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}
	public String getScadenzaOld() {
		return scadenzaOld;
	}
	public void setScadenzaOld(String scadenzaOld) {
		this.scadenzaOld = scadenzaOld;
	}
	public String getImportoFormattato() {
		return importoFormattato;
	}
	public void setImportoFormattato(String importoFormattato) {
		this.importoFormattato = importoFormattato;
	}
	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}
	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}
	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}
	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}
	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}
	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}
	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}
	
	public BigDecimal getImportoImpegnoIniziale() {
		return importoImpegnoIniziale;
	}
	public void setImportoImpegnoIniziale(BigDecimal importoImpegnoIniziale) {
		this.importoImpegnoIniziale = importoImpegnoIniziale;
	}
	public String getStatoOperativo() {
		return statoOperativo;
	}
	public void setStatoOperativo(String statoOperativo) {
		this.statoOperativo = statoOperativo;
	}
	public String getDescrizioneStatoOperativoMovimento() {
		return descrizioneStatoOperativoMovimento;
	}
	public void setDescrizioneStatoOperativoMovimento(String descrizioneStatoOperativoMovimento) {
		this.descrizioneStatoOperativoMovimento = descrizioneStatoOperativoMovimento;
	}
	public Date getDataStatoOperativoMovimento() {
		return dataStatoOperativoMovimento;
	}
	public void setDataStatoOperativoMovimento(Date dataStatoOperativoMovimento) {
		this.dataStatoOperativoMovimento = dataStatoOperativoMovimento;
	}
	public CapitoloEntrataGestione getCapitoloAccertamento() {
		return capitoloAccertamento;
	}
	public void setCapitoloAccertamento(CapitoloEntrataGestione capitoloAccertamento) {
		this.capitoloAccertamento = capitoloAccertamento;
	}
	public boolean getDaRicerca() {
		return daRicerca;
	}
	public void setDaRicerca(boolean daRicerca) {
		this.daRicerca = daRicerca;
	}
	public boolean isCheckproseguiRiacc() {
		return checkproseguiRiacc;
	}
	public void setCheckproseguiRiacc(boolean checkproseguiRiacc) {
		this.checkproseguiRiacc = checkproseguiRiacc;
	}
	public boolean isCheckproseguiOrigin() {
		return checkproseguiOrigin;
	}
	public void setCheckproseguiOrigin(boolean checkproseguiOrigin) {
		this.checkproseguiOrigin = checkproseguiOrigin;
	}
	public boolean isCheckproseguiMovimentoSpesa() {
		return checkproseguiMovimentoSpesa;
	}
	public void setCheckproseguiMovimentoSpesa(boolean checkproseguiMovimentoSpesa) {
		this.checkproseguiMovimentoSpesa = checkproseguiMovimentoSpesa;
	}
	public boolean isOperazioneInserimento() {
		return operazioneInserimento;
	}
	public void setOperazioneInserimento(boolean operazioneInserimento) {
		this.operazioneInserimento = operazioneInserimento;
	}
	
	public boolean isApriTabVincoli() {
		return apriTabVincoli;
	}
	public void setApriTabVincoli(boolean apriTabVincoli) {
		this.apriTabVincoli = apriTabVincoli;
	}
	public Integer getAnnoAccertamentoVincolo() {
		return annoAccertamentoVincolo;
	}
	public void setAnnoAccertamentoVincolo(Integer annoAccertamentoVincolo) {
		this.annoAccertamentoVincolo = annoAccertamentoVincolo;
	}
	public Integer getNumeroAccertamentoVincolo() {
		return numeroAccertamentoVincolo;
	}
	public void setNumeroAccertamentoVincolo(Integer numeroAccertamentoVincolo) {
		this.numeroAccertamentoVincolo = numeroAccertamentoVincolo;
	}
	public String getImportoVincoloFormattato() {
		return importoVincoloFormattato;
	}
	public void setImportoVincoloFormattato(String importoVincoloFormattato) {
		this.importoVincoloFormattato = importoVincoloFormattato;
	}
	public BigDecimal getImportoVincolo() {
		return importoVincolo;
	}
	public void setImportoVincolo(BigDecimal importoVincolo) {
		this.importoVincolo = importoVincolo;
	}
	public boolean isInserisciVincoloBtn() {
		return inserisciVincoloBtn;
	}
	public void setInserisciVincoloBtn(boolean inserisciVincoloBtn) {
		this.inserisciVincoloBtn = inserisciVincoloBtn;
	}
	public List<VincoloImpegno> getListaVincoliImpegno() {
		return listaVincoliImpegno;
	}
	public void setListaVincoliImpegno(List<VincoloImpegno> listaVincoliImpegno) {
		this.listaVincoliImpegno = listaVincoliImpegno;
	}
	public BigDecimal getTotaleImportoVincoli() {
		return totaleImportoVincoli;
	}
	public void setTotaleImportoVincoli(BigDecimal totaleImportoVincoli) {
		this.totaleImportoVincoli = totaleImportoVincoli;
	}
	public BigDecimal getTotaleImportoDaCollegare() {
		return totaleImportoDaCollegare;
	}
	public void setTotaleImportoDaCollegare(BigDecimal totaleImportoDaCollegare) {
		this.totaleImportoDaCollegare = totaleImportoDaCollegare;
	}
	public DettaglioVincoloModel getDettaglioVincolo() {
		return dettaglioVincolo;
	}
	public void setDettaglioVincolo(DettaglioVincoloModel dettaglioVincolo) {
		this.dettaglioVincolo = dettaglioVincolo;
	}
	public AccertamentoRicercaModel getAccertamentoRicerca() {
		return accertamentoRicerca;
	}
	public void setAccertamentoRicerca(AccertamentoRicercaModel accertamentoRicerca) {
		this.accertamentoRicerca = accertamentoRicerca;
	}
	public List<Accertamento> getListaAccPerVincoli() {
		return listaAccPerVincoli;
	}
	public void setListaAccPerVincoli(List<Accertamento> listaAccPerVincoli) {
		this.listaAccPerVincoli = listaAccPerVincoli;
	}
	public boolean isTrovatiAccPerVincolo() {
		return trovatiAccPerVincolo;
	}
	public void setTrovatiAccPerVincolo(boolean trovatiAccPerVincolo) {
		this.trovatiAccPerVincolo = trovatiAccPerVincolo;
	}
	public Accertamento getAccertamentoPerVincolo() {
		return accertamentoPerVincolo;
	}
	public void setAccertamentoPerVincolo(Accertamento accertamentoPerVincolo) {
		this.accertamentoPerVincolo = accertamentoPerVincolo;
	}
	public boolean isPortaAdAltezzaVincoli() {
		return portaAdAltezzaVincoli;
	}
	public void setPortaAdAltezzaVincoli(boolean portaAdAltezzaVincoli) {
		this.portaAdAltezzaVincoli = portaAdAltezzaVincoli;
	}
	public String getImportoInModificaFormattato() {
		return importoInModificaFormattato;
	}
	public void setImportoInModificaFormattato(String importoInModificaFormattato) {
		this.importoInModificaFormattato = importoInModificaFormattato;
	}
	public ProgettoImpegnoModel getProgettoImpegno() {
		return progettoImpegno;
	}
	public void setProgettoImpegno(ProgettoImpegnoModel progettoImpegno) {
		this.progettoImpegno = progettoImpegno;
	}
	public boolean isProgettoSelezionato() {
		return progettoSelezionato;
	}
	public void setProgettoSelezionato(boolean progettoSelezionato) {
		this.progettoSelezionato = progettoSelezionato;
	}
	public ProvvedimentoImpegnoModel getProvvedimentoSupport() {
		return provvedimentoSupport;
	}
	public void setProvvedimentoSupport(ProvvedimentoImpegnoModel provvedimentoSupport) {
		this.provvedimentoSupport = provvedimentoSupport;
	}
	public Date getParereFinanziarioDataModifica() {
		return parereFinanziarioDataModifica;
	}
	public void setParereFinanziarioDataModifica(Date parereFinanziarioDataModifica) {
		this.parereFinanziarioDataModifica = parereFinanziarioDataModifica;
	}
	public String getParereFinanziarioLoginOperazione() {
		return parereFinanziarioLoginOperazione;
	}
	public void setParereFinanziarioLoginOperazione(String parereFinanziarioLoginOperazione) {
		this.parereFinanziarioLoginOperazione = parereFinanziarioLoginOperazione;
	}
	public String getFlagFattura() {
		return flagFattura;
	}
	public void setFlagFattura(String flagFattura) {
		this.flagFattura = flagFattura;
	}

	public String getFlagAttivaGsa() {
		return flagAttivaGsa;
	}
	public void setFlagAttivaGsa(String flagAttivaGsa) {
		this.flagAttivaGsa = flagAttivaGsa;
	}
	public List<String> getListflagAttivaGsa() {
		return listflagAttivaGsa;
	}
	public void setListflagAttivaGsa(List<String> listflagAttivaGsa) {
		this.listflagAttivaGsa = listflagAttivaGsa;
	}
	public String getImportoUtilizzabileFormattato() {
		return importoUtilizzabileFormattato;
	}
	public void setImportoUtilizzabileFormattato(String importoUtilizzabileFormattato) {
		this.importoUtilizzabileFormattato = importoUtilizzabileFormattato;
	}
	public BigDecimal getDisponibilitaUtilizzare() {
		return disponibilitaUtilizzare;
	}
	public void setDisponibilitaUtilizzare(BigDecimal disponibilitaUtilizzare) {
		this.disponibilitaUtilizzare = disponibilitaUtilizzare;
	}

	public List<String> getDaPrenotazione() {
		return daPrenotazione;
	}

	public void setDaPrenotazione(List<String> daPrenotazione) {
		this.daPrenotazione = daPrenotazione;
	}

	public String getPrenotazione() {
		return prenotazione;
	}

	public void setPrenotazione(String prenotazione) {
		this.prenotazione = prenotazione;
	}

	public List<String> getDiCassaEconomale() {
		return diCassaEconomale;
	}

	public void setDiCassaEconomale(List<String> diCassaEconomale) {
		this.diCassaEconomale = diCassaEconomale;
	}

	public String getCassaEconomale() {
		return cassaEconomale;
	}

	public void setCassaEconomale(String cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}

	public List<String> getScelteFrazionabile() {
		return scelteFrazionabile;
	}

	public void setScelteFrazionabile(List<String> scelteFrazionabile) {
		this.scelteFrazionabile = scelteFrazionabile;
	}

	public String getFrazionabile() {
		return frazionabile;
	}

	public void setFrazionabile(String frazionabile) {
		this.frazionabile = frazionabile;
	}

	public String getTipoVincolo() {
		return tipoVincolo;
	}

	public void setTipoVincolo(String tipoVincolo) {
		this.tipoVincolo = tipoVincolo;
	}

	public String getAvanzoVincoloSelezionato() {
		return avanzoVincoloSelezionato;
	}

	public void setAvanzoVincoloSelezionato(String avanzoVincoloSelezionato) {
		this.avanzoVincoloSelezionato = avanzoVincoloSelezionato;
	}

	public List<String> getSceltaAccertamentoAvanzoList() {
		return sceltaAccertamentoAvanzoList;
	}

	public void setSceltaAccertamentoAvanzoList(
			List<String> sceltaAccertamentoAvanzoList) {
		this.sceltaAccertamentoAvanzoList = sceltaAccertamentoAvanzoList;
	}

	public String getImportoAvanzoVincoloFormattato() {
		return importoAvanzoVincoloFormattato;
	}

	public void setImportoAvanzoVincoloFormattato(String importoAvanzoVincoloFormattato) {
		this.importoAvanzoVincoloFormattato = importoAvanzoVincoloFormattato;
	}

	public Boolean getPrenotazioneLiquidabile() {
		return prenotazioneLiquidabile;
	}

	public void setPrenotazioneLiquidabile(Boolean prenotazioneLiquidabile) {
		this.prenotazioneLiquidabile = prenotazioneLiquidabile;
	}

	public Boolean getHiddenPerPrenotazioneLiquidabile() {
		return hiddenPerPrenotazioneLiquidabile;
	}

	public void setHiddenPerPrenotazioneLiquidabile(Boolean hiddenPerPrenotazioneLiquidabile) {
		this.hiddenPerPrenotazioneLiquidabile = hiddenPerPrenotazioneLiquidabile;
	}

	/**
	 * @return the annoScritturaEconomicoPatrimoniale
	 */
	public Integer getAnnoScritturaEconomicoPatrimoniale() {
		return annoScritturaEconomicoPatrimoniale;
	}

	/**
	 * @param annoScritturaEconomicoPatrimoniale the annoScritturaEconomicoPatrimoniale to set
	 */
	public void setAnnoScritturaEconomicoPatrimoniale(Integer annoScritturaEconomicoPatrimoniale) {
		this.annoScritturaEconomicoPatrimoniale = annoScritturaEconomicoPatrimoniale;
	}


	public String getTipoDebitoSiope() {
		return tipoDebitoSiope;
	}

	public void setTipoDebitoSiope(String tipoDebitoSiope) {
		this.tipoDebitoSiope = tipoDebitoSiope;
	}

	public List<String> getScelteTipoDebitoSiope() {
		return scelteTipoDebitoSiope;
	}

	public void setScelteTipoDebitoSiope(List<String> scelteTipoDebitoSiope) {
		this.scelteTipoDebitoSiope = scelteTipoDebitoSiope;
	}

	public String getMotivazioneAssenzaCig() {
		return motivazioneAssenzaCig;
	}

	public void setMotivazioneAssenzaCig(String motivazioneAssenzaCig) {
		this.motivazioneAssenzaCig = motivazioneAssenzaCig;
	}

	/**
	 * @return the messaggioDiConfermaModificaVincoli
	 */
	public String getMessaggioDiConfermaModificaVincoli() {
		return messaggioDiConfermaModificaVincoli;
	}

	/**
	 * @param messaggioDiConfermaModificaVincoli the messaggioDiConfermaModificaVincoli to set
	 */
	public void setMessaggioDiConfermaModificaVincoli(String messaggioDiConfermaModificaVincoli) {
		this.messaggioDiConfermaModificaVincoli = messaggioDiConfermaModificaVincoli;
	}

	/**
	 * @return the imprtoVincoliModificato
	 */
	public Boolean getImprtoVincoliModificato() {
		return imprtoVincoliModificato;
	}

	/**
	 * @param imprtoVincoliModificato the imprtoVincoliModificato to set
	 */
	public void setImprtoVincoliModificato(Boolean imprtoVincoliModificato) {
		this.imprtoVincoliModificato = imprtoVincoliModificato;
	}
	
	

	public BigDecimal getDisponibileLiquidareDaFunction() {
		return disponibileLiquidareDaFunction;
	}

	public void setDisponibileLiquidareDaFunction(
			BigDecimal disponibileLiquidareDaFunction) {
		this.disponibileLiquidareDaFunction = disponibileLiquidareDaFunction;
	}

	/**
	 * @return the passaggioAStatoDefinitivo
	 */
	public boolean isPassaggioAStatoDefinitivo() {
		return passaggioAStatoDefinitivo;
	}

	/**
	 * @param passaggioAStatoDefinitivo the passaggioAStatoDefinitivo to set
	 */
	public void setPassaggioAStatoDefinitivo(boolean passaggioAStatoDefinitivo) {
		this.passaggioAStatoDefinitivo = passaggioAStatoDefinitivo;
	}

	/**
	 * @return the saltaControlloLegamiStoricizzati
	 */
	public boolean isSaltaControlloLegamiStoricizzati() {
		return saltaControlloLegamiStoricizzati;
	}

	/**
	 * @param saltaControlloLegamiStoricizzati the saltaControlloLegamiStoricizzati to set
	 */
	public void setSaltaControlloLegamiStoricizzati(boolean saltaControlloLegamiStoricizzati) {
		this.saltaControlloLegamiStoricizzati = saltaControlloLegamiStoricizzati;
	}

	public String getSoggettoDurc() {
		return soggettoDurc;
	}

	public void setSoggettoDurc(String soggettoDurc) {
		this.soggettoDurc = soggettoDurc;
	}

	public String getAttivitaPrevista() {
		return attivitaPrevista;
	}

	public void setAttivitaPrevista(String attivitaPrevista) {
		this.attivitaPrevista = attivitaPrevista;
	}

	public String getCronoprogramma() {
		return cronoprogramma;
	}

	public void setCronoprogramma(String cronoprogramma) {
		this.cronoprogramma = cronoprogramma;
	}

	public Integer getIdSpesaCronoprogramma() {
		return idSpesaCronoprogramma;
	}

	public void setIdSpesaCronoprogramma(Integer idSpesaCronoprogramma) {
		this.idSpesaCronoprogramma = idSpesaCronoprogramma;
	}

	public Integer getIdCronoprogramma() {
		return idCronoprogramma;
	}

	public void setIdCronoprogramma(Integer idCronoprogramma) {
		this.idCronoprogramma = idCronoprogramma;
	}

	public String getFlagCorrispettivo() {
		return flagCorrispettivo;
	}

	public void setFlagCorrispettivo(String flagCorrispettivo) {
		this.flagCorrispettivo = flagCorrispettivo;
	}

	
	
	
}
