/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.TransazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class LiquidazioneModel extends GenericPopupModel {
	
	private static final long serialVersionUID = 1L;
	
	//impegno
	private Impegno impegno;		
	
	//subdocumentoSpesa
	private SubdocumentoSpesa subdocumentoSpesa;
	
	//anno e numero liq consulta:
	private Integer annoLiquidazioneConsulta;
	private BigDecimal numeroLiquidazioneConsulta;
	
	//flag di lock:
	private Boolean lockProvvedimento = false;
	private Boolean lockImportoLiquidazione = false;
	private Boolean lockTipoConvalida = false;
	
	//campi scheda:
	private Integer annoLiquidazioneScheda;
	private BigDecimal numeroLiquidazioneScheda;
	private BigDecimal importoLiquidazioneScheda;
	private String descrizioneLiquidazioneScheda;
	private String statoLiquidazioneScheda;
	
	//ServizioAggiorna
	private BigDecimal importoLiquidazioneDaAggiornare;
	private BigDecimal disponibilePagareLiquidazioneDaAggiornare;
	private String statoOperativoAttoAmmDaAggiornare;
	private StatoOperativoLiquidazione statoOperativoLiquidazioneDaAggiornare;
	private String liqAutomaticaDaAggiornare;
	private String liqManualeDaAggiornare;
	private Integer annoAttoAmmDaAggiornare;
	private Integer numeroAttoAmmDaAggiornare;
	private String codiceTipoAttoAmmDaAggiornare;
	
	//provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//soggetto impegno
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	
	//capitolo
	private CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
	
	//flag di selezione:
	private boolean soggettoSelezionato = false, provvedimentoSelezionato = false, capitoloSelezionato = false, impegnoSelezionato = false;

	//mod pag sel
	private int radioModPagSelezionato;
	//sede sec sel
	private int radioSediSecondarieSoggettoSelezionato;
	
	//classe imp
	private String classeImpegno;
	
	private SedeSecondariaSoggetto sedeSelezionata = null;
	private ModalitaPagamentoSoggetto modpagamentoSelezionata = null;
	
	//liste:
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();	
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoOrigine = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<SedeSecondariaSoggetto>  listaSediSecondarieSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	
	
	private List<CodificaFin> listContoTesoreria = new ArrayList<CodificaFin>();
	private List<CodificaFin> listDistinta = new ArrayList<CodificaFin>();
	
	//LISTE DATI TRANSAZIONE
	private List<Missione> listaMissione = new ArrayList<Missione>();
    private List<Programma> listaProgramma = new ArrayList<Programma>();
    private List<TransazioneUnioneEuropeaSpesa> listaTransazioneEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
    private List<ClassificazioneCofog> listaCofog = new ArrayList<ClassificazioneCofog>();
    private List<TransazioneUnioneEuropeaSpesa> listaTransEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
    private List<RicorrenteSpesa> listaRicorrenteSpesa = new ArrayList<RicorrenteSpesa>();
    private List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa = new ArrayList<PerimetroSanitarioSpesa>();
    private List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie = new ArrayList<PoliticheRegionaliUnitarie>();
    private List<TipoFinanziamento> listaTipoFinanziamento = new ArrayList<TipoFinanziamento>();
    //FINE LISTE DATI TRANSAZIONE
    
    TransazioneModel trxModel = new TransazioneModel();
    
    //IMPEGNO PER SERVIZIO
    private Impegno impegnoPerServizio;
    
	//per indietro
	private Boolean flagIndietro;

    
    //GETTER E SETTER:
    
	public Impegno getImpegno() {
		return impegno;
	}

	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}

	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}

	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}

	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}

	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}

	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}

	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto;
	}

	public List<SedeSecondariaSoggetto> getListaSediSecondarieSoggetto() {
		return listaSediSecondarieSoggetto;
	}

	public void setListaSediSecondarieSoggetto(List<SedeSecondariaSoggetto> listaSediSecondarieSoggetto) {
		this.listaSediSecondarieSoggetto = listaSediSecondarieSoggetto;
	}

	public int getRadioModPagSelezionato() {
		return radioModPagSelezionato;
	}

	public void setRadioModPagSelezionato(int radioModPagSelezionato) {
		this.radioModPagSelezionato = radioModPagSelezionato;
	}

	public int getRadioSediSecondarieSoggettoSelezionato() {
		return radioSediSecondarieSoggettoSelezionato;
	}

	public void setRadioSediSecondarieSoggettoSelezionato(int radioSediSecondarieSoggettoSelezionato) {
		this.radioSediSecondarieSoggettoSelezionato = radioSediSecondarieSoggettoSelezionato;
	}

	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}

	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}

	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}

	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoOrigine() {
		return listaModalitaPagamentoSoggettoOrigine;
	}

	public void setListaModalitaPagamentoSoggettoOrigine(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoOrigine) {
		this.listaModalitaPagamentoSoggettoOrigine = listaModalitaPagamentoSoggettoOrigine;
	}

	public SedeSecondariaSoggetto getSedeSelezionata() {
		return sedeSelezionata;
	}

	public void setSedeSelezionata(SedeSecondariaSoggetto sedeSelezionata) {
		this.sedeSelezionata = sedeSelezionata;
	}

	public ModalitaPagamentoSoggetto getModpagamentoSelezionata() {
		return modpagamentoSelezionata;
	}

	public void setModpagamentoSelezionata(ModalitaPagamentoSoggetto modpagamentoSelezionata) {
		this.modpagamentoSelezionata = modpagamentoSelezionata;
	}

	public String getClasseImpegno() {
		return classeImpegno;
	}

	public void setClasseImpegno(String classeImpegno) {
		this.classeImpegno = classeImpegno;
	}

	public List<Missione> getListaMissione() {
		return listaMissione;
	}

	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione;
	}

	public List<Programma> getListaProgramma() {
		return listaProgramma;
	}

	public void setListaProgramma(List<Programma> listaProgramma) {
		this.listaProgramma = listaProgramma;
	}

	public List<TransazioneUnioneEuropeaSpesa> getListaTransazioneEuropeaSpesa() {
		return listaTransazioneEuropeaSpesa;
	}

	public void setListaTransazioneEuropeaSpesa(List<TransazioneUnioneEuropeaSpesa> listaTransazioneEuropeaSpesa) {
		this.listaTransazioneEuropeaSpesa = listaTransazioneEuropeaSpesa;
	}

	public List<ClassificazioneCofog> getListaCofog() {
		return listaCofog;
	}

	public void setListaCofog(List<ClassificazioneCofog> listaCofog) {
		this.listaCofog = listaCofog;
	}

	public List<TransazioneUnioneEuropeaSpesa> getListaTransEuropeaSpesa() {
		return listaTransEuropeaSpesa;
	}

	public void setListaTransEuropeaSpesa(List<TransazioneUnioneEuropeaSpesa> listaTransEuropeaSpesa) {
		this.listaTransEuropeaSpesa = listaTransEuropeaSpesa;
	}

	public List<RicorrenteSpesa> getListaRicorrenteSpesa() {
		return listaRicorrenteSpesa;
	}

	public void setListaRicorrenteSpesa(List<RicorrenteSpesa> listaRicorrenteSpesa) {
		this.listaRicorrenteSpesa = listaRicorrenteSpesa;
	}

	public List<PerimetroSanitarioSpesa> getListaPerimetroSanitarioSpesa() {
		return listaPerimetroSanitarioSpesa;
	}

	public void setListaPerimetroSanitarioSpesa(List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa) {
		this.listaPerimetroSanitarioSpesa = listaPerimetroSanitarioSpesa;
	}

	public List<PoliticheRegionaliUnitarie> getListaPoliticheRegionaliUnitarie() {
		return listaPoliticheRegionaliUnitarie;
	}

	public void setListaPoliticheRegionaliUnitarie(List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie) {
		this.listaPoliticheRegionaliUnitarie = listaPoliticheRegionaliUnitarie;
	}

	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}

	public TransazioneModel getTrxModel() {
		return trxModel;
	}

	public void setTrxModel(TransazioneModel trxModel) {
		this.trxModel = trxModel;
	}

	public List<CodificaFin> getListContoTesoreria() {
		return listContoTesoreria;
	}

	public void setListContoTesoreria(List<CodificaFin> listContoTesoreria) {
		this.listContoTesoreria = listContoTesoreria;
	}

	public List<CodificaFin> getListDistinta() {
		return listDistinta;
	}

	public void setListDistinta(List<CodificaFin> listDistinta) {
		this.listDistinta = listDistinta;
	}

	public Impegno getImpegnoPerServizio() {
		return impegnoPerServizio;
	}

	public void setImpegnoPerServizio(Impegno impegnoPerServizio) {
		this.impegnoPerServizio = impegnoPerServizio;
	}

	public Integer getAnnoLiquidazioneConsulta() {
		return annoLiquidazioneConsulta;
	}

	public void setAnnoLiquidazioneConsulta(Integer annoLiquidazioneConsulta) {
		this.annoLiquidazioneConsulta = annoLiquidazioneConsulta;
	}

	public BigDecimal getNumeroLiquidazioneConsulta() {
		return numeroLiquidazioneConsulta;
	}

	public void setNumeroLiquidazioneConsulta(BigDecimal numeroLiquidazioneConsulta) {
		this.numeroLiquidazioneConsulta = numeroLiquidazioneConsulta;
	}

	public Boolean getLockProvvedimento() {
		return lockProvvedimento;
	}

	public void setLockProvvedimento(Boolean lockProvvedimento) {
		this.lockProvvedimento = lockProvvedimento;
	}

	public Boolean getLockImportoLiquidazione() {
		return lockImportoLiquidazione;
	}

	public void setLockImportoLiquidazione(Boolean lockImportoLiquidazione) {
		this.lockImportoLiquidazione = lockImportoLiquidazione;
	}

	public Boolean getLockTipoConvalida() {
		return lockTipoConvalida;
	}

	public void setLockTipoConvalida(Boolean lockTipoConvalida) {
		this.lockTipoConvalida = lockTipoConvalida;
	}

	public Integer getAnnoLiquidazioneScheda() {
		return annoLiquidazioneScheda;
	}

	public void setAnnoLiquidazioneScheda(Integer annoLiquidazioneScheda) {
		this.annoLiquidazioneScheda = annoLiquidazioneScheda;
	}

	public BigDecimal getNumeroLiquidazioneScheda() {
		return numeroLiquidazioneScheda;
	}

	public void setNumeroLiquidazioneScheda(BigDecimal numeroLiquidazioneScheda) {
		this.numeroLiquidazioneScheda = numeroLiquidazioneScheda;
	}

	public BigDecimal getImportoLiquidazioneScheda() {
		return importoLiquidazioneScheda;
	}

	public void setImportoLiquidazioneScheda(BigDecimal importoLiquidazioneScheda) {
		this.importoLiquidazioneScheda = importoLiquidazioneScheda;
	}

	public BigDecimal getImportoLiquidazioneDaAggiornare() {
		return importoLiquidazioneDaAggiornare;
	}

	public void setImportoLiquidazioneDaAggiornare(BigDecimal importoLiquidazioneDaAggiornare) {
		this.importoLiquidazioneDaAggiornare = importoLiquidazioneDaAggiornare;
	}

	public String getStatoOperativoAttoAmmDaAggiornare() {
		return statoOperativoAttoAmmDaAggiornare;
	}

	public void setStatoOperativoAttoAmmDaAggiornare(String statoOperativoAttoAmmDaAggiornare) {
		this.statoOperativoAttoAmmDaAggiornare = statoOperativoAttoAmmDaAggiornare;
	}

	public StatoOperativoLiquidazione getStatoOperativoLiquidazioneDaAggiornare() {
		return statoOperativoLiquidazioneDaAggiornare;
	}

	public void setStatoOperativoLiquidazioneDaAggiornare(StatoOperativoLiquidazione statoOperativoLiquidazioneDaAggiornare) {
		this.statoOperativoLiquidazioneDaAggiornare = statoOperativoLiquidazioneDaAggiornare;
	}

	public String getLiqAutomaticaDaAggiornare() {
		return liqAutomaticaDaAggiornare;
	}

	public void setLiqAutomaticaDaAggiornare(String liqAutomaticaDaAggiornare) {
		this.liqAutomaticaDaAggiornare = liqAutomaticaDaAggiornare;
	}

	public String getLiqManualeDaAggiornare() {
		return liqManualeDaAggiornare;
	}

	public void setLiqManualeDaAggiornare(String liqManualeDaAggiornare) {
		this.liqManualeDaAggiornare = liqManualeDaAggiornare;
	}

	public Integer getAnnoAttoAmmDaAggiornare() {
		return annoAttoAmmDaAggiornare;
	}

	public void setAnnoAttoAmmDaAggiornare(Integer annoAttoAmmDaAggiornare) {
		this.annoAttoAmmDaAggiornare = annoAttoAmmDaAggiornare;
	}

	public Integer getNumeroAttoAmmDaAggiornare() {
		return numeroAttoAmmDaAggiornare;
	}

	public void setNumeroAttoAmmDaAggiornare(Integer numeroAttoAmmDaAggiornare) {
		this.numeroAttoAmmDaAggiornare = numeroAttoAmmDaAggiornare;
	}

	public String getCodiceTipoAttoAmmDaAggiornare() {
		return codiceTipoAttoAmmDaAggiornare;
	}

	public void setCodiceTipoAttoAmmDaAggiornare(String codiceTipoAttoAmmDaAggiornare) {
		this.codiceTipoAttoAmmDaAggiornare = codiceTipoAttoAmmDaAggiornare;
	}

	public String getDescrizioneLiquidazioneScheda() {
		return descrizioneLiquidazioneScheda;
	}

	public void setDescrizioneLiquidazioneScheda(String descrizioneLiquidazioneScheda) {
		this.descrizioneLiquidazioneScheda = descrizioneLiquidazioneScheda;
	}

	public String getStatoLiquidazioneScheda() {
		return statoLiquidazioneScheda;
	}

	public void setStatoLiquidazioneScheda(String statoLiquidazioneScheda) {
		this.statoLiquidazioneScheda = statoLiquidazioneScheda;
	}

	/**
	 * @return the impegnoSelezionato
	 */
	public boolean isImpegnoSelezionato() {
		return impegnoSelezionato;
	}

	/**
	 * @param impegnoSelezionato the impegnoSelezionato to set
	 */
	public void setImpegnoSelezionato(boolean impegnoSelezionato) {
		this.impegnoSelezionato = impegnoSelezionato;
	}

	public BigDecimal getDisponibilePagareLiquidazioneDaAggiornare() {
		return disponibilePagareLiquidazioneDaAggiornare;
	}

	public void setDisponibilePagareLiquidazioneDaAggiornare(BigDecimal disponibilePagareLiquidazioneDaAggiornare) {
		this.disponibilePagareLiquidazioneDaAggiornare = disponibilePagareLiquidazioneDaAggiornare;
	}

	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}

	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
	}

	public Boolean getFlagIndietro() {
		return flagIndietro;
	}

	public void setFlagIndietro(Boolean flagIndietro) {
		this.flagIndietro = flagIndietro;
	}
	
}
