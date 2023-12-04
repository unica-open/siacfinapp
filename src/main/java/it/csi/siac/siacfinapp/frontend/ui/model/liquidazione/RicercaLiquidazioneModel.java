/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.dispatcher.Parameter;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class RicercaLiquidazioneModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;

	//model del capitolo
	private CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
	//model del soggetto
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//liquidazione
	private ImpegnoLiquidazioneModel impegno = new ImpegnoLiquidazioneModel();
	
	//anno liquidazione consulta
	private Integer annoLiquidazioneConsulta;
	
	//numero Liquidazione Consulta
	private BigDecimal numeroLiquidazioneConsulta;
	
	//atto amministrativo
	private AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
	
	//soggetto consulta
	private Soggetto soggettoConsulta = new Soggetto();
	
	//capitolo consulta
	private CapitoloUscitaGestione capitoloConsulta = new CapitoloUscitaGestione();
	
	//impegno consulta
	private Impegno impegnoConsulta = new Impegno();
	
	//sub impegno consulta
	private SubImpegno subImpegnoConsulta = new SubImpegno();
	
	//mdp consulta
	private ModalitaPagamentoSoggetto mdpConsulta = new ModalitaPagamentoSoggetto();

	//liquidazione consulta
	private Liquidazione liquidazioneConsulta = new Liquidazione();
	
	//atto amministrativo consulta
	private AttoAmministrativo attoAmministrativoConsulta = new AttoAmministrativo();
	
	//fine consulta
	
	//flag di selezione
	private boolean soggettoSelezionato = false, provvedimentoSelezionato = false , capitoloSelezionato = false;
	
	//SIAC-5253 nuovo campo per escludere i movimenti annullati:
	private Boolean escludiAnnullati = Boolean.FALSE;
	private Boolean hiddenPerEscludiAnnullati;
	
	//campi vari
	private Integer annoLiquidazione;
	private BigDecimal numeroLiquidazione;
	private String numeroLiquidazioneString;
	private String descrizioneLiquidazione; 
	private String importoLiquidazione;
	private BigDecimal importoLiquidazioneBigDecimal;
	private String cigLiquidazione;
	private String cupLiquidazione;
	private List<Liquidazione> elencoLiquidazioni = new ArrayList<Liquidazione>();
	private StatoOperativoLiquidazione statoOperativoLiquidazione;
	private Date dataCreazione,dataModifica,dataAnnullamento;
	private String transazioneElementare;
		
	//paginazione
	private int resultSize;
	private int numeroPaginaElenco;
	
	//da annulla
	private boolean fromAnnulla; 
	
	//lista tipo finanziamento
	private List<TipoFinanziamento> listaTipoFinanziamento;
	
	
	//SIOPE PLUS (sono i dati per il consulta liquidazione)
	//motivazione assenza cig
	private String motivazioneAssenzaCig;
	
	//tipo debito siope
	private String tipoDebitoSiope;
	//ENDO SIOPE PLUS
	
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
	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloImpegnoModel capitolo) {
		this.capitolo = capitolo;
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
	public List<Liquidazione> getElencoLiquidazioni() {
		return elencoLiquidazioni;
	}
	public void setElencoLiquidazioni(List<Liquidazione> elencoLiquidazioni) {
		this.elencoLiquidazioni = elencoLiquidazioni;
	}
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	public Integer getAnnoLiquidazione() {
		return annoLiquidazione;
	}
	public void setAnnoLiquidazione(Integer annoLiquidazione) {
		this.annoLiquidazione = annoLiquidazione;
	}
	public BigDecimal getNumeroLiquidazione() {
		return numeroLiquidazione;
	}
	public void setNumeroLiquidazione(BigDecimal numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}

	public String getDescrizioneLiquidazione() {
		return descrizioneLiquidazione;
	}
	public void setDescrizioneLiquidazione(String descrizioneLiquidazione) {
		this.descrizioneLiquidazione = descrizioneLiquidazione;
	}
	public String getImportoLiquidazione() {
		return importoLiquidazione;
	}
	public void setImportoLiquidazione(String importoLiquidazione) {
		this.importoLiquidazione = importoLiquidazione;
	}
	public StatoOperativoLiquidazione getStatoOperativoLiquidazione() {
		return statoOperativoLiquidazione;
	}
	public void setStatoOperativoLiquidazione(StatoOperativoLiquidazione statoOperativoLiquidazione) {
		this.statoOperativoLiquidazione = statoOperativoLiquidazione;
	}
	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}
	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}
	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}
	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}
	public ImpegnoLiquidazioneModel getImpegno() {
		return impegno;
	}
	public void setImpegno(ImpegnoLiquidazioneModel impegno) {
		this.impegno = impegno;
	}
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}
	public Soggetto getSoggettoConsulta() {
		return soggettoConsulta;
	}
	public void setSoggettoConsulta(Soggetto soggettoConsulta) {
		this.soggettoConsulta = soggettoConsulta;
	}
	public CapitoloUscitaGestione getCapitoloConsulta() {
		return capitoloConsulta;
	}
	public void setCapitoloConsulta(CapitoloUscitaGestione capitoloConsulta) {
		this.capitoloConsulta = capitoloConsulta;
	}
	public Impegno getImpegnoConsulta() {
		return impegnoConsulta;
	}
	public void setImpegnoConsulta(Impegno impegnoConsulta) {
		this.impegnoConsulta = impegnoConsulta;
	}
	public String getCigLiquidazione() {
		return cigLiquidazione;
	}
	public void setCigLiquidazione(String cigLiquidazione) {
		this.cigLiquidazione = cigLiquidazione;
	}
	public String getCupLiquidazione() {
		return cupLiquidazione;
	}
	public void setCupLiquidazione(String cupLiquidazione) {
		this.cupLiquidazione = cupLiquidazione;
	}
	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}
	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Date getDataModifica() {
		return dataModifica;
	}
	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}
	public String getTransazioneElementare() {
		return transazioneElementare;
	}
	public void setTransazioneElementare(String transazioneELementare) {
		this.transazioneElementare = transazioneELementare;
	}
	public SubImpegno getSubImpegnoConsulta() {
		return subImpegnoConsulta;
	}
	public void setSubImpegnoConsulta(SubImpegno subImpegnoConsulta) {
		this.subImpegnoConsulta = subImpegnoConsulta;
	}
	public ModalitaPagamentoSoggetto getMdpConsulta() {
		return mdpConsulta;
	}
	public void setMdpConsulta(ModalitaPagamentoSoggetto mdpConsulta) {
		this.mdpConsulta = mdpConsulta;
	}
	public BigDecimal getImportoLiquidazioneBigDecimal() {
		return importoLiquidazioneBigDecimal;
	}
	public void setImportoLiquidazioneBigDecimal(
			BigDecimal importoLiquidazioneBigDecimal) {
		this.importoLiquidazioneBigDecimal = importoLiquidazioneBigDecimal;
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
	public Liquidazione getLiquidazioneConsulta() {
		return liquidazioneConsulta;
	}
	public void setLiquidazioneConsulta(Liquidazione liquidazioneConsulta) {
		this.liquidazioneConsulta = liquidazioneConsulta;
	}
	public AttoAmministrativo getAttoAmministrativoConsulta() {
		return attoAmministrativoConsulta;
	}
	public void setAttoAmministrativoConsulta(AttoAmministrativo attoAmministrativoConsulta) {
		this.attoAmministrativoConsulta = attoAmministrativoConsulta;
	}
	public String getNumeroLiquidazioneString() {
		return numeroLiquidazioneString;
	}
	public void setNumeroLiquidazioneString(String numeroLiquidazioneString) {
		this.numeroLiquidazioneString = numeroLiquidazioneString;
	}

	public int getNumeroPaginaElenco() {
		return numeroPaginaElenco;
	}
	public void setNumeroPaginaElenco(int numeroPaginaElenco) {
		this.numeroPaginaElenco = numeroPaginaElenco;
	}
	public boolean isFromAnnulla() {
		return fromAnnulla;
	}
	public void setFromAnnulla(boolean fromAnnulla) {
		this.fromAnnulla = fromAnnulla;
	}
	public Boolean getEscludiAnnullati() {
		return escludiAnnullati;
	}
	public void setEscludiAnnullati(Boolean escludiAnnullati) {
		this.escludiAnnullati = escludiAnnullati;
	}
	public Boolean getHiddenPerEscludiAnnullati() {
		return hiddenPerEscludiAnnullati;
	}
	public void setHiddenPerEscludiAnnullati(Boolean hiddenPerEscludiAnnullati) {
		this.hiddenPerEscludiAnnullati = hiddenPerEscludiAnnullati;
	}
	public String getMotivazioneAssenzaCig() {
		return motivazioneAssenzaCig;
	}
	public void setMotivazioneAssenzaCig(String motivazioneAssenzaCig) {
		this.motivazioneAssenzaCig = motivazioneAssenzaCig;
	}
	public String getTipoDebitoSiope() {
		return tipoDebitoSiope;
	}
	public void setTipoDebitoSiope(String tipoDebitoSiope) {
		this.tipoDebitoSiope = tipoDebitoSiope;
	}
}
