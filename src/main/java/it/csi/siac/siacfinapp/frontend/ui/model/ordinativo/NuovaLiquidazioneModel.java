/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class NuovaLiquidazioneModel extends GenericFinModel{


	private static final long serialVersionUID = 1L;
	
	//CAMPI RELATIVI ALL'IMPEGNO:
	
	//num impegno
	private String numeroImpegno;
	
	//num sub
	private String numeroSub;
	
	//numero mutuo
	private String numeroMutuoPopupString;
	
	//anno imp
	private String annoImpegno;
	
	//CAMPI RELATIVI ALLA LIQUIDAZIONE:
	
	//descrizione liquidazione
	private String descrizioneLiquidazione;
	
	//importo liquidazione
	private String importoLiquidazione;
	
	//cig
	private String cig;
	
	//cup
	private String cup;
	
	//distinta
	private String distinta;
	
	//conto tesoriere
	private String contoTesoriere;
	
	//DATI TRANSAZIONE ORDINATIVO:
	private TransazioneGestioneOrdinativoModel transazione = new TransazioneGestioneOrdinativoModel();
	
	
	//GETTER E SETTE:
	
	public String getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public String getNumeroSub() {
		return numeroSub;
	}
	public void setNumeroSub(String numeroSub) {
		this.numeroSub = numeroSub;
	}
	public String getNumeroMutuoPopupString() {
		return numeroMutuoPopupString;
	}
	public void setNumeroMutuoPopupString(String numeroMutuoPopupString) {
		this.numeroMutuoPopupString = numeroMutuoPopupString;
	}
	public String getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(String annoImpegno) {
		this.annoImpegno = annoImpegno;
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
	public String getDistinta() {
		return distinta;
	}
	public void setDistinta(String distinta) {
		this.distinta = distinta;
	}
	public String getContoTesoriere() {
		return contoTesoriere;
	}
	public void setContoTesoriere(String contoTesoriere) {
		this.contoTesoriere = contoTesoriere;
	}
	public TransazioneGestioneOrdinativoModel getTransazione() {
		return transazione;
	}
	public void setTransazione(TransazioneGestioneOrdinativoModel transazione) {
		this.transazione = transazione;
	}
	
}
