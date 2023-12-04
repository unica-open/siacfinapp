/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.carta;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class RicercaCartaModel extends GenericPopupModel{
	
	private static final long serialVersionUID = 1L;
	
	//model del capitolo
	private CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
	//model del soggetto
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//LIQUIDAZIONE IMPEGNO
	private ImpegnoLiquidazioneModel impegno = new ImpegnoLiquidazioneModel();
	
	//DATI CARTA:
	private String numeroCartaDa,numeroCartaA,dataScadenzaCartaDa,dataScadenzaCartaA,statoCarta,descrizioneCarta;	
	private List<CodificaFin> listaStatoCartaContabile = new ArrayList<CodificaFin>();
	
	//FLAG ELEMENTI SELEZIONATI:
	private boolean capitoloSelezionato= false, provvedimentoSelezionato=false;
	private boolean toggleImputazioneContabiliAperto = false;
	private boolean soggettoSelezionato = false;
	
	//LISTA CARTE CONTABILI:
	private List<CartaContabile> elencoCarteContabili = new ArrayList<CartaContabile>();
	
	//PAGINZIONE:
	private int resultSize,premutoPaginazione;
	
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
	public ImpegnoLiquidazioneModel getImpegno() {
		return impegno;
	}
	public void setImpegno(ImpegnoLiquidazioneModel impegno) {
		this.impegno = impegno;
	}
	public String getNumeroCartaDa() {
		return numeroCartaDa;
	}
	public void setNumeroCartaDa(String numeroCartaDa) {
		this.numeroCartaDa = numeroCartaDa;
	}
	public String getNumeroCartaA() {
		return numeroCartaA;
	}
	public void setNumeroCartaA(String numeroCartaA) {
		this.numeroCartaA = numeroCartaA;
	}
	public String getDataScadenzaCartaDa() {
		return dataScadenzaCartaDa;
	}
	public void setDataScadenzaCartaDa(String dataScadenzaCartaDa) {
		this.dataScadenzaCartaDa = dataScadenzaCartaDa;
	}
	public String getDataScadenzaCartaA() {
		return dataScadenzaCartaA;
	}
	public void setDataScadenzaCartaA(String dataScadenzaCartaA) {
		this.dataScadenzaCartaA = dataScadenzaCartaA;
	}
	public String getStatoCarta() {
		return statoCarta;
	}
	public void setStatoCarta(String statoCarta) {
		this.statoCarta = statoCarta;
	}
	public String getDescrizioneCarta() {
		return descrizioneCarta;
	}
	public void setDescrizioneCarta(String descrizioneCarta) {
		this.descrizioneCarta = descrizioneCarta;
	}
	public List<CodificaFin> getListaStatoCartaContabile() {
		return listaStatoCartaContabile;
	}
	public void setListaStatoCartaContabile(List<CodificaFin> listaStatoCartaContabile) {
		this.listaStatoCartaContabile = listaStatoCartaContabile;
	}
	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}
	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}
	public boolean isToggleImputazioneContabiliAperto() {
		return toggleImputazioneContabiliAperto;
	}
	public void setToggleImputazioneContabiliAperto(boolean toggleImputazioneContabiliAperto) {
		this.toggleImputazioneContabiliAperto = toggleImputazioneContabiliAperto;
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
	public List<CartaContabile> getElencoCarteContabili() {
		return elencoCarteContabili;
	}
	public void setElencoCarteContabili(List<CartaContabile> elencoCarteContabili) {
		this.elencoCarteContabili = elencoCarteContabili;
	}
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	public int getPremutoPaginazione() {
		return premutoPaginazione;
	}
	public void setPremutoPaginazione(int premutoPaginazione) {
		this.premutoPaginazione = premutoPaginazione;
	}


}
