/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.carta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class RicercaQuotaSpesaModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ente bilancio richiedente
	private Ente ente;
	private Bilancio bilancio;
	private Richiedente richiedente;
	
	//dati elenco
	private Integer uidElenco;
	private Integer annoElenco;
	private Integer numeroElenco;
	
	//provvisorio
	private Integer annoProvvisorio;
	private Integer numeroProvvisorio;
	private Date dataProvvisorio;
	
	//documento
	private TipoDocumento tipoDocumento;
	private Integer annoDocumento; 
	private String numeroDocumento;
	private Date dataEmissioneDocumento;
	
	//quota
	private Integer numeroQuota;	
	
	//movimento
	private BigDecimal numeroMovimento;
	private Integer annoMovimento; 
	
	//soggetto
	private Soggetto soggetto;
	
	//provvedimento
	private Integer uidProvvedimento;
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private TipoAtto tipoAtto;
	
	//SAC
	private StrutturaAmministrativoContabile struttAmmContabile;
	
	//stati doc
	private List<StatoOperativoDocumento> statiOperativoDocumento = new ArrayList<StatoOperativoDocumento>();
	
	//Vari flag
	private Boolean collegatoAMovimentoDelloStessoBilancio;
	private Boolean associatoAProvvedimentoOAdElenco;
	private Boolean importoDaPagareZero;
	private Boolean rilevatiIvaConRegistrazioneONonRilevantiIva;

	private ParametriPaginazione parametriPaginazione;

	public Ente getEnte() {
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public Bilancio getBilancio() {
		return bilancio;
	}

	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	public Integer getUidElenco() {
		return uidElenco;
	}

	public void setUidElenco(Integer uidElenco) {
		this.uidElenco = uidElenco;
	}

	public Integer getAnnoElenco() {
		return annoElenco;
	}

	public void setAnnoElenco(Integer annoElenco) {
		this.annoElenco = annoElenco;
	}

	public Integer getNumeroElenco() {
		return numeroElenco;
	}

	public void setNumeroElenco(Integer numeroElenco) {
		this.numeroElenco = numeroElenco;
	}

	public Integer getAnnoProvvisorio() {
		return annoProvvisorio;
	}

	public void setAnnoProvvisorio(Integer annoProvvisorio) {
		this.annoProvvisorio = annoProvvisorio;
	}

	public Integer getNumeroProvvisorio() {
		return numeroProvvisorio;
	}

	public void setNumeroProvvisorio(Integer numeroProvvisorio) {
		this.numeroProvvisorio = numeroProvvisorio;
	}

	public Date getDataProvvisorio() {
		return dataProvvisorio;
	}

	public void setDataProvvisorio(Date dataProvvisorio) {
		this.dataProvvisorio = dataProvvisorio;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getAnnoDocumento() {
		return annoDocumento;
	}

	public void setAnnoDocumento(Integer annoDocumento) {
		this.annoDocumento = annoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getDataEmissioneDocumento() {
		return dataEmissioneDocumento;
	}

	public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
		this.dataEmissioneDocumento = dataEmissioneDocumento;
	}

	public Integer getNumeroQuota() {
		return numeroQuota;
	}

	public void setNumeroQuota(Integer numeroQuota) {
		this.numeroQuota = numeroQuota;
	}

	public BigDecimal getNumeroMovimento() {
		return numeroMovimento;
	}

	public void setNumeroMovimento(BigDecimal numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	public Soggetto getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}

	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public StrutturaAmministrativoContabile getStruttAmmContabile() {
		return struttAmmContabile;
	}

	public void setStruttAmmContabile(
			StrutturaAmministrativoContabile struttAmmContabile) {
		this.struttAmmContabile = struttAmmContabile;
	}

	public List<StatoOperativoDocumento> getStatiOperativoDocumento() {
		return statiOperativoDocumento;
	}

	public void setStatiOperativoDocumento(
			List<StatoOperativoDocumento> statiOperativoDocumento) {
		this.statiOperativoDocumento = statiOperativoDocumento;
	}

	public Boolean getCollegatoAMovimentoDelloStessoBilancio() {
		return collegatoAMovimentoDelloStessoBilancio;
	}

	public void setCollegatoAMovimentoDelloStessoBilancio(
			Boolean collegatoAMovimentoDelloStessoBilancio) {
		this.collegatoAMovimentoDelloStessoBilancio = collegatoAMovimentoDelloStessoBilancio;
	}

	public Boolean getAssociatoAProvvedimentoOAdElenco() {
		return associatoAProvvedimentoOAdElenco;
	}

	public void setAssociatoAProvvedimentoOAdElenco(
			Boolean associatoAProvvedimentoOAdElenco) {
		this.associatoAProvvedimentoOAdElenco = associatoAProvvedimentoOAdElenco;
	}

	public Boolean getImportoDaPagareZero() {
		return importoDaPagareZero;
	}

	public void setImportoDaPagareZero(Boolean importoDaPagareZero) {
		this.importoDaPagareZero = importoDaPagareZero;
	}

	public Boolean getRilevatiIvaConRegistrazioneONonRilevantiIva() {
		return rilevatiIvaConRegistrazioneONonRilevantiIva;
	}

	public void setRilevatiIvaConRegistrazioneONonRilevantiIva(
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva) {
		this.rilevatiIvaConRegistrazioneONonRilevantiIva = rilevatiIvaConRegistrazioneONonRilevantiIva;
	}

	public ParametriPaginazione getParametriPaginazione() {
		return parametriPaginazione;
	}

	public void setParametriPaginazione(ParametriPaginazione parametriPaginazione) {
		this.parametriPaginazione = parametriPaginazione;
	}

	public Richiedente getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}
	
}
