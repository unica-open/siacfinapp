/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfinser.model.Impegno;

public class MovimentoConsulta extends CommonConsulta {

	private static final long serialVersionUID = 1L;
	
	//id movimento:
	private Integer idMovimento;
	
	// SIAC-6997 aggiunti campi daReanno e strutturaCompetente
	
	//dati principali:
	private String 		anno, numero, descrizione, progetto, cig, cup, daRiaccertamento, daReanno, strutturaCompetente, soggettoDurc, classificazione, codificaTransazioneElementare;
	private Date 		dataScadenza;
	private BigDecimal	importoIniziale, disponibilitaSub, totaleSub;
	private String		descSuper; 
	
	//tipo
	private String tipo;
	
	//tipo code:
	private String tipoCode;
	
	//parere finanziario:
	private String parereFinanziario ;
	private Date parereFinanziarioDataModifica;
	private String parereFinanziarioLoginOperazione;
	
	//disponibilita:
	private BigDecimal	disponibilitaLiquidare,disponibilitaPagare,disponibilitaFinanziare, disponibilitaIncassare, disponibilitaVincolare;
	
	private BigDecimal disponibilitaUtilizzare;
	
	//model del capitolo:
	private final CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();

	//provvedimento:
	private Provvedimento provvedimento = new Provvedimento();

	//soggetto:
	private  Soggetto soggetto = new Soggetto();
	
	private String flagFattura;
	private String flagCorrispettivo;
	
	//flag attiva gsa:
	private String flagAttivaGsa;
	
	//impegno senza disponibilita fondi:
	private boolean impegnoSDF;
	
	//FRAZIONABILE:
	private boolean frazionabile;
	private boolean frazionabileValorizzato;
	private String frazionabileString;
	//
	
	//PRENOTATO:
	private boolean prenotato;
	private String prenotatoString;
	//
	
	//anno scrittura economico patrimoniale
	private String annoScritturaEconomicoPatrimoniale;
	
	
	//SIOPE PLUS:
	//motivazione assenza cig
	private String motivazioneAssenzaCig;
	
	//tipo debito siope
	private String tipoDebitoSiope;
	
	// SIAC-6247 & SIAC-6268 
	// Classificatori
	private String titoloGiuridico;
	private String tipoTracciabilita;
	private String voceTracciabilita;
	private String classificazioneFSC;
	
	// SIAC-6695
	// Impegno
	private String motivazioneDisponibilitaLiquidare;
	private String motivazioneDisponibilitaPagare;
	private String motivazioneDisponibilitaFinanziare;
	private String motivazioneDisponibilitaSubImpegnare;
	private String motivazioneDisponibilitaImpegnoModifica;
	private String motivazioneDisponibilitaVincolare;
	// Accertamento
	private String motivazioneDisponibilitaIncassare;
	private String motivazioneDisponibilitaSubAccertare;
	private String motivazioneDisponibilitaUtilizzare;
	
	//SIAC-6929
	private String bloccoRagioneria;

	//SIAC-7349
	private String descrizioneComponente;
	
	//SIAC-6865
	private boolean aggiudicazioneDaPrenotazione;
	private Impegno impegnoPrenotazioneOrigine;
	private AttoAmministrativo provvedimentoAggiudicazione;
	
	//SIAC-8178
	private String codiceVerbale;

	public String getCodiceVerbale() {
			return codiceVerbale;
	}

	public void setCodiceVerbale(String codiceVerbale) {
			this.codiceVerbale = codiceVerbale;
	}


	public String getDescrizioneComponente() {
		return descrizioneComponente;
	}

	public void setDescrizioneComponente(String descrizioneComponente) {
		this.descrizioneComponente = descrizioneComponente;
	}

	public String getBloccoRagioneria() {
		return bloccoRagioneria;
	}

	public void setBloccoRagioneria(String bloccoRagioneria) {
		this.bloccoRagioneria = bloccoRagioneria;
	}

	// GETTE E SETTER:
	public String getAnno() {
		return anno;
	}

	/**
	 * @return the idMovimento
	 */
	public Integer getIdMovimento() {
		return idMovimento;
	}

	/**
	 * @param idMovimento the idMovimento to set
	 */
	public void setIdMovimento(Integer idMovimento) {
		this.idMovimento = idMovimento;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getProgetto() {
		return progetto;
	}

	public void setProgetto(String progetto) {
		this.progetto = progetto;
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

	public String getDaRiaccertamento() {
		return daRiaccertamento;
	}

	public void setDaRiaccertamento(String daRiaccertamento) {
		this.daRiaccertamento = daRiaccertamento;
	}

	// inizio SIAC-6997
	/**
	 * @return the daREANNO
	 */
	public String getDaReanno() {
		return daReanno;
	}

	/**
	 * @param daREANNO the daREANNO to set
	 */
	public void setDaReanno(String daReanno) {
		this.daReanno = daReanno;
	}
	
	/**
	 * @return the strutturaCompetente
	 */
	public String getStrutturaCompetente() {
		return strutturaCompetente;
	}

	/**
	 * @param strutturaCompetente the strutturaCompetente to set
	 */
	public void setStrutturaCompetente(String strutturaCompetente) {
		this.strutturaCompetente = strutturaCompetente;
	}
	
	// fine SIAC-6997

	public String getClassificazione() {
		return classificazione;
	}

	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public BigDecimal getImportoIniziale() {
		return importoIniziale;
	}

	public void setImportoIniziale(BigDecimal importoIniziale) {
		this.importoIniziale = importoIniziale;
	}

	public BigDecimal getDisponibilitaSub() {
		return disponibilitaSub;
	}

	public void setDisponibilitaSub(BigDecimal disponibilitaSub) {
		this.disponibilitaSub = disponibilitaSub;
	}

	public BigDecimal getTotaleSub() {
		return totaleSub;
	}

	public void setTotaleSub(BigDecimal totaleSub) {
		this.totaleSub = totaleSub;
	}

	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}

	public Provvedimento getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(Provvedimento provvedimento) {
		this.provvedimento = provvedimento;
	}

	public Soggetto getSoggetto() {
		return soggetto;
	}
	
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	public String getDescSuper() {
		return descSuper;
	}

	public void setDescSuper(String descSuper) {
		this.descSuper = descSuper;
	}

	public String getCodificaTransazioneElementare() {
		return codificaTransazioneElementare;
	}

	public void setCodificaTransazioneElementare(String codificaTransazioneElementare) {
		this.codificaTransazioneElementare = codificaTransazioneElementare;
	}

	public BigDecimal getDisponibilitaLiquidare() {
		return disponibilitaLiquidare;
	}

	public void setDisponibilitaLiquidare(BigDecimal disponibilitaLiquidare) {
		this.disponibilitaLiquidare = disponibilitaLiquidare;
	}

	public BigDecimal getDisponibilitaPagare() {
		return disponibilitaPagare;
	}

	public void setDisponibilitaPagare(BigDecimal disponibilitaPagare) {
		this.disponibilitaPagare = disponibilitaPagare;
	}

	public BigDecimal getDisponibilitaFinanziare() {
		return disponibilitaFinanziare;
	}

	public void setDisponibilitaFinanziare(BigDecimal disponibilitaFinanziare) {
		this.disponibilitaFinanziare = disponibilitaFinanziare;
	}

	public BigDecimal getDisponibilitaIncassare() {
		return disponibilitaIncassare;
	}

	public void setDisponibilitaIncassare(BigDecimal disponibilitaIncassare) {
		this.disponibilitaIncassare = disponibilitaIncassare;
	}

	public BigDecimal getDisponibilitaVincolare() {
		return disponibilitaVincolare;
	}

	public void setDisponibilitaVincolare(BigDecimal disponibilitaVincolare) {
		this.disponibilitaVincolare = disponibilitaVincolare;
	}

	/**
	 * @return the parereFinanziario
	 */
	public String getParereFinanziario() {
		return parereFinanziario;
	}

	/**
	 * @param parereFinanziario the parereFinanziario to set
	 */
	public void setParereFinanziario(String parereFinanziario) {
		this.parereFinanziario = parereFinanziario;
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

	public Date getParereFinanziarioDataModifica() {
		return parereFinanziarioDataModifica;
	}

	public void setParereFinanziarioDataModifica(Date parereFinanziarioDataModifica) {
		this.parereFinanziarioDataModifica = parereFinanziarioDataModifica;
	}

	public String getParereFinanziarioLoginOperazione() {
		return parereFinanziarioLoginOperazione;
	}

	public void setParereFinanziarioLoginOperazione(
			String parereFinanziarioLoginOperazione) {
		this.parereFinanziarioLoginOperazione = parereFinanziarioLoginOperazione;
	}

	public boolean isImpegnoSDF() {
		return impegnoSDF;
	}

	public void setImpegnoSDF(boolean impegnoSDF) {
		this.impegnoSDF = impegnoSDF;
	}

	public boolean isFrazionabile() {
		return frazionabile;
	}

	public void setFrazionabile(boolean frazionabile) {
		this.frazionabile = frazionabile;
	}

	public boolean isFrazionabileValorizzato() {
		return frazionabileValorizzato;
	}

	public void setFrazionabileValorizzato(boolean frazionabileValorizzato) {
		this.frazionabileValorizzato = frazionabileValorizzato;
	}

	public String getFrazionabileString() {
		return frazionabileString;
	}

	public void setFrazionabileString(String frazionabileString) {
		this.frazionabileString = frazionabileString;
	}

	public boolean isPrenotato() {
		return prenotato;
	}

	public void setPrenotato(boolean prenotato) {
		this.prenotato = prenotato;
	}

	public String getPrenotatoString() {
		return prenotatoString;
	}

	public void setPrenotatoString(String prenotatoString) {
		this.prenotatoString = prenotatoString;
	}

	public String getTipoCode() {
		return tipoCode;
	}

	public void setTipoCode(String tipoCode) {
		this.tipoCode = tipoCode;
	}

	public BigDecimal getDisponibilitaUtilizzare() {
		return disponibilitaUtilizzare;
	}

	public void setDisponibilitaUtilizzare(BigDecimal disponibilitaUtilizzare) {
		this.disponibilitaUtilizzare = disponibilitaUtilizzare;
	}

	public String getAnnoScritturaEconomicoPatrimoniale() {
		return annoScritturaEconomicoPatrimoniale;
	}

	public void setAnnoScritturaEconomicoPatrimoniale(String annoScritturaEconomicoPatrimoniale) {
		this.annoScritturaEconomicoPatrimoniale = annoScritturaEconomicoPatrimoniale;
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

	public String getTitoloGiuridico() {
		return titoloGiuridico;
	}

	public void setTitoloGiuridico(String titoloGiuridico) {
		this.titoloGiuridico = titoloGiuridico;
	}

	public String getTipoTracciabilita() {
		return tipoTracciabilita;
	}

	public void setTipoTracciabilita(String tipoTracciabilita) {
		this.tipoTracciabilita = tipoTracciabilita;
	}

	public String getVoceTracciabilita() {
		return voceTracciabilita;
	}

	public void setVoceTracciabilita(String voceTracciabilita) {
		this.voceTracciabilita = voceTracciabilita;
	}

	public String getClassificazioneFSC() {
		return classificazioneFSC;
	}

	public void setClassificazioneFSC(String classificazioneFSC) {
		this.classificazioneFSC = classificazioneFSC;
	}

	public String getSoggettoDurc() {
		return soggettoDurc;
	}

	public void setSoggettoDurc(String soggettoDurc) {
		this.soggettoDurc = soggettoDurc;
	}

	/**
	 * @return the motivazioneDisponibilitaLiquidare
	 */
	public String getMotivazioneDisponibilitaLiquidare() {
		return this.motivazioneDisponibilitaLiquidare;
	}

	/**
	 * @param motivazioneDisponibilitaLiquidare the motivazioneDisponibilitaLiquidare to set
	 */
	public void setMotivazioneDisponibilitaLiquidare(String motivazioneDisponibilitaLiquidare) {
		this.motivazioneDisponibilitaLiquidare = motivazioneDisponibilitaLiquidare;
	}

	/**
	 * @return the motivazioneDisponibilitaPagare
	 */
	public String getMotivazioneDisponibilitaPagare() {
		return this.motivazioneDisponibilitaPagare;
	}

	/**
	 * @param motivazioneDisponibilitaPagare the motivazioneDisponibilitaPagare to set
	 */
	public void setMotivazioneDisponibilitaPagare(String motivazioneDisponibilitaPagare) {
		this.motivazioneDisponibilitaPagare = motivazioneDisponibilitaPagare;
	}

	/**
	 * @return the motivazioneDisponibilitaFinanziare
	 */
	public String getMotivazioneDisponibilitaFinanziare() {
		return this.motivazioneDisponibilitaFinanziare;
	}

	/**
	 * @param motivazioneDisponibilitaFinanziare the motivazioneDisponibilitaFinanziare to set
	 */
	public void setMotivazioneDisponibilitaFinanziare(String motivazioneDisponibilitaFinanziare) {
		this.motivazioneDisponibilitaFinanziare = motivazioneDisponibilitaFinanziare;
	}

	/**
	 * @return the motivazioneDisponibilitaSubImpegnare
	 */
	public String getMotivazioneDisponibilitaSubImpegnare() {
		return this.motivazioneDisponibilitaSubImpegnare;
	}

	/**
	 * @param motivazioneDisponibilitaSubImpegnare the motivazioneDisponibilitaSubImpegnare to set
	 */
	public void setMotivazioneDisponibilitaSubImpegnare(String motivazioneDisponibilitaSubImpegnare) {
		this.motivazioneDisponibilitaSubImpegnare = motivazioneDisponibilitaSubImpegnare;
	}

	/**
	 * @return the motivazioneDisponibilitaImpegnoModifica
	 */
	public String getMotivazioneDisponibilitaImpegnoModifica() {
		return this.motivazioneDisponibilitaImpegnoModifica;
	}

	/**
	 * @param motivazioneDisponibilitaImpegnoModifica the motivazioneDisponibilitaImpegnoModifica to set
	 */
	public void setMotivazioneDisponibilitaImpegnoModifica(String motivazioneDisponibilitaImpegnoModifica) {
		this.motivazioneDisponibilitaImpegnoModifica = motivazioneDisponibilitaImpegnoModifica;
	}

	/**
	 * @return the motivazioneDisponibilitaVincolare
	 */
	public String getMotivazioneDisponibilitaVincolare() {
		return this.motivazioneDisponibilitaVincolare;
	}

	/**
	 * @param motivazioneDisponibilitaVincolare the motivazioneDisponibilitaVincolare to set
	 */
	public void setMotivazioneDisponibilitaVincolare(String motivazioneDisponibilitaVincolare) {
		this.motivazioneDisponibilitaVincolare = motivazioneDisponibilitaVincolare;
	}

	/**
	 * @return the motivazioneDisponibilitaIncassare
	 */
	public String getMotivazioneDisponibilitaIncassare() {
		return this.motivazioneDisponibilitaIncassare;
	}

	/**
	 * @param motivazioneDisponibilitaIncassare the motivazioneDisponibilitaIncassare to set
	 */
	public void setMotivazioneDisponibilitaIncassare(String motivazioneDisponibilitaIncassare) {
		this.motivazioneDisponibilitaIncassare = motivazioneDisponibilitaIncassare;
	}

	/**
	 * @return the motivazioneDisponibilitaSubAccertare
	 */
	public String getMotivazioneDisponibilitaSubAccertare() {
		return this.motivazioneDisponibilitaSubAccertare;
	}

	/**
	 * @param motivazioneDisponibilitaSubAccertare the motivazioneDisponibilitaSubAccertare to set
	 */
	public void setMotivazioneDisponibilitaSubAccertare(String motivazioneDisponibilitaSubAccertare) {
		this.motivazioneDisponibilitaSubAccertare = motivazioneDisponibilitaSubAccertare;
	}

	/**
	 * @return the motivazioneDisponibilitaUtilizzare
	 */
	public String getMotivazioneDisponibilitaUtilizzare() {
		return this.motivazioneDisponibilitaUtilizzare;
	}

	/**
	 * @param motivazioneDisponibilitaUtilizzare the motivazioneDisponibilitaUtilizzare to set
	 */
	public void setMotivazioneDisponibilitaUtilizzare(String motivazioneDisponibilitaUtilizzare) {
		this.motivazioneDisponibilitaUtilizzare = motivazioneDisponibilitaUtilizzare;
	}

	public String getFlagCorrispettivo() {
		return flagCorrispettivo;
	}

	public void setFlagCorrispettivo(String flagCorrispettivo) {
		this.flagCorrispettivo = flagCorrispettivo;
	}

	/**
	 * @return the aggiudicazioneDaPrenotazione
	 */
	public boolean isAggiudicazioneDaPrenotazione() {
		return aggiudicazioneDaPrenotazione;
	}

	/**
	 * @param aggiudicazioneDaPrenotazione the aggiudicazioneDaPrenotazione to set
	 */
	public void setAggiudicazioneDaPrenotazione(boolean aggiudicazioneDaPrenotazione) {
		this.aggiudicazioneDaPrenotazione = aggiudicazioneDaPrenotazione;
	}

	
	/**
	 * @return the provvedimentoAggiudicazione
	 */
	public AttoAmministrativo getProvvedimentoAggiudicazione() {
		return provvedimentoAggiudicazione;
	}

	/**
	 * @param provvedimentoAggiudicazione the provvedimentoAggiudicazione to set
	 */
	public void setProvvedimentoAggiudicazione(AttoAmministrativo provvedimentoAggiudicazione) {
		this.provvedimentoAggiudicazione = provvedimentoAggiudicazione;
	}

	/**
	 * @return the impegnoPrenotazioneOrigine
	 */
	public Impegno getImpegnoPrenotazioneOrigine() {
		return impegnoPrenotazioneOrigine;
	}

	/**
	 * @param impegnoPrenotazioneOrigine the impegnoPrenotazioneOrigine to set
	 */
	public void setImpegnoPrenotazioneOrigine(Impegno impegnoPrenotazioneOrigine) {
		this.impegnoPrenotazioneOrigine = impegnoPrenotazioneOrigine;
	}
	
	/**
	 * Gets the numero prenotazione origine.
	 *
	 * @return the numero prenotazione origine
	 */
	public String getNumeroPrenotazioneOrigine() {
		return getImpegnoPrenotazioneOrigine() != null && getImpegnoPrenotazioneOrigine().getNumeroBigDecimal() != null? 
				Integer.valueOf(getImpegnoPrenotazioneOrigine().getNumeroBigDecimal().intValue()).toString() : "";   
	}
	
	
	
}
