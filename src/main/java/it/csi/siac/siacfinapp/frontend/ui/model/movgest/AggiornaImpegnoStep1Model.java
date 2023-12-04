/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class AggiornaImpegnoStep1Model extends GenericFinModel{
	
	private static final long serialVersionUID = 1L;
	
	//CAPITOLO
	private CapitoloImpegnoModel capitolo;
	
	//PROVVEDIMENTO
	private ProvvedimentoImpegnoModel provvedimento;
	
	//SOGGETTO
	private SoggettoImpegnoModel soggetto;
	
	//Impegno
	private Impegno impegnoToUpdate;
	
	//dati vari
	private Integer annoImpegno, annoFinanziamento, numeroFinanziamento, annoImpRiacc, numImpRiacc, numeroPluriennali, annoImpOrigine, numImpOrigine;
	private String oggettoImpegno, stato, cig, cup, progetto,riaccertato,pluriennale,scadenza;
	
	//tipo impegno
	private CodificaFin tipoImpegno;
	
	//importo impegno
	private float importoImpegno;	
	
	//da riaccertamento
	private List<String> daRiaccertamento = new ArrayList<String>();
	
	//SIAC-6997
	//da reanno:
	private String reanno;
	private List<String> daReanno = new ArrayList<String>();
	
	 
 
	//impl pluriennale
	private List<String> implPluriennale = new ArrayList<String>();	
	
	//capitolo trovato
	public boolean capitoloTrovato = false;
	
	//soggetto trovato
	public boolean soggettoTrovato = false;
	
	//provvedimento trovato
	public boolean provvedimentoTrovato = false;
		
	
	//GETTER E SETTER:
	
	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloImpegnoModel capitolo) {
		this.capitolo = capitolo;
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
	public String getProgetto() {
		return progetto;
	}
	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
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
	public float getAnnoFinanziamento() {
		return annoFinanziamento;
	}
	public float getNumeroFinanziamento() {
		return numeroFinanziamento;
	}	
	public Impegno getImpegnoToUpdate() {
		return impegnoToUpdate;
	}
	public void setImpegnoToUpdate(Impegno impegnoToUpdate) {
		this.impegnoToUpdate = impegnoToUpdate;
	}
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public String getOggettoImpegno() {
		return oggettoImpegno;
	}
	public void setOggettoImpegno(String oggettoImpegno) {
		this.oggettoImpegno = oggettoImpegno;
	}
	public String getCig() {
		return cig;
	}
	public void setCig(String cig) {
		this.cig = cig;
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
	public CodificaFin getTipoImpegno() {
		return tipoImpegno;
	}
	public void setTipoImpegno(CodificaFin tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}
	public float getImportoImpegno() {
		return importoImpegno;
	}
	public void setImportoImpegno(float importoImpegno) {
		this.importoImpegno = importoImpegno;
	}
	public List<String> getDaRiaccertamento() {
		return daRiaccertamento;
	}
	public void setDaRiaccertamento(List<String> daRiaccertamento) {
		this.daRiaccertamento = daRiaccertamento;
	}
	
	//SIAC-6997
	/**
	 * @return the daReanno
	 */
	public List<String> getDaReanno() {
		return daReanno;
	}
	/**
	 * @param daReanno the daReanno to set
	 */
	public void setDaReanno(List<String> daReanno) {
		this.daReanno = daReanno;
	}
	
	
	/**
	 * @return the reanno
	 */
	public String getReanno() {
		return reanno;
	}
	/**
	 * @param reanno the reanno to set
	 */
	public void setReanno(String reanno) {
		this.reanno = reanno;
	}
	
	public List<String> getImplPluriennale() {
		return implPluriennale;
	}
	public void setImplPluriennale(List<String> implPluriennale) {
		this.implPluriennale = implPluriennale;
	}	
	public Integer getNumeroPluriennali() {
		return numeroPluriennali;
	}
	public void setNumeroPluriennali(Integer numeroPluriennali) {
		this.numeroPluriennali = numeroPluriennali;
	}
	public String getScadenza() {
		return scadenza;
	}
	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}
}
