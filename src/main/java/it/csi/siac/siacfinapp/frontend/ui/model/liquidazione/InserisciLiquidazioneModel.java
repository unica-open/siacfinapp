/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.liquidazione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.LiquidazioneModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class InserisciLiquidazioneModel extends LiquidazioneModel {

	private static final long serialVersionUID = 1L;
	
	//flag vari:
	private boolean hasCodiceSoggetto = false;
	private boolean ricercaImpegnoStep1Abilitata = true;
	
	//lista ordinativi:
	private List<OrdinativoPagamento> listaOrdinativi;
	
	//check per warning:
	private boolean checkWarningsLiquidazioneStep2 = false;
	
	//step
	private String step;
	
	//request per inserimento
	private transient InserisceLiquidazione request = null;
	
	//descrizione
	private String descrizioneLiquidazione;
	
	//importo
	private String importoLiquidazione;
	
	//cig e cup
	private String cig;
	private String cup;
	
	private String distinta;
	private String contoTesoriere;
	
	//Emissione ordinativo: manuale/automatico
	private String tipoConvalida;
	private List<CodificaFin> tipoConvalidaList = new ArrayList<CodificaFin>();
	
	//Tipo debito siope:
	private List<String> scelteTipoDebitoSiope = new ArrayList<String>();
	private String tipoDebitoSiope;
	
	
	//motivazione assenza cig:
	private String motivazioneAssenzaCig;
	
	//lista motivazioni assenza cig:
	public List<CodificaFin> listaMotivazioniAssenzaCig;
	//SIAC-5333
	private boolean saltaInserimentoPrimaNota = false;
	private boolean richiediConfermaUtente = false;
	private int uidDaCompletare;

	private boolean salvataConValidaOra = false;
	
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
	public String getTipoConvalida() {
		return tipoConvalida;
	}
	public void setTipoConvalida(String tipoConvalida) {
		this.tipoConvalida = tipoConvalida;
	}
	public boolean isHasCodiceSoggetto() {
		return hasCodiceSoggetto;
	}
	public void setHasCodiceSoggetto(boolean hasCodiceSoggetto) {
		this.hasCodiceSoggetto = hasCodiceSoggetto;
	}
	public void setDistinta(String distinta) {
		this.distinta = distinta;
	}
	public void setContoTesoriere(String contoTesoriere) {
		this.contoTesoriere = contoTesoriere;
	}
	public String getDistinta() {
		return distinta;
	}
	public String getContoTesoriere() {
		return contoTesoriere;
	}
	public List<CodificaFin> getTipoConvalidaList() {
		return tipoConvalidaList;
	}
	public void setTipoConvalidaList(List<CodificaFin> tipoConvalidaList) {
		this.tipoConvalidaList = tipoConvalidaList;
	}
	public boolean isRicercaImpegnoStep1Abilitata() {
		return ricercaImpegnoStep1Abilitata;
	}
	public void setRicercaImpegnoStep1Abilitata(boolean ricercaImpegnoStep1Abilitata) {
		this.ricercaImpegnoStep1Abilitata = ricercaImpegnoStep1Abilitata;
	}
	public boolean isCheckWarningsLiquidazioneStep2() {
		return checkWarningsLiquidazioneStep2;
	}
	public void setCheckWarningsLiquidazioneStep2(
			boolean checkWarningsLiquidazioneStep2) {
		this.checkWarningsLiquidazioneStep2 = checkWarningsLiquidazioneStep2;
	}
	public InserisceLiquidazione getRequest() {
		return request;
	}
	public void setRequest(InserisceLiquidazione request) {
		this.request = request;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public List<OrdinativoPagamento> getListaOrdinativi() {
		return listaOrdinativi;
	}
	public void setListaOrdinativi(List<OrdinativoPagamento> listaOrdinativi) {
		this.listaOrdinativi = listaOrdinativi;
	}
	public List<String> getScelteTipoDebitoSiope() {
		return scelteTipoDebitoSiope;
	}
	public void setScelteTipoDebitoSiope(List<String> scelteTipoDebitoSiope) {
		this.scelteTipoDebitoSiope = scelteTipoDebitoSiope;
	}
	public String getTipoDebitoSiope() {
		return tipoDebitoSiope;
	}
	public void setTipoDebitoSiope(String tipoDebitoSiope) {
		this.tipoDebitoSiope = tipoDebitoSiope;
	}
	public String getMotivazioneAssenzaCig() {
		return motivazioneAssenzaCig;
	}
	public void setMotivazioneAssenzaCig(String motivazioneAssenzaCig) {
		this.motivazioneAssenzaCig = motivazioneAssenzaCig;
	}
	public List<CodificaFin> getListaMotivazioniAssenzaCig() {
		return listaMotivazioniAssenzaCig;
	}
	public void setListaMotivazioniAssenzaCig(List<CodificaFin> listaMotivazioniAssenzaCig) {
		this.listaMotivazioniAssenzaCig = listaMotivazioniAssenzaCig;
	}
	
	
	/**
	 * @return the saltaInserimentoPrimaNota
	 */
	public boolean isSaltaInserimentoPrimaNota() {
		return saltaInserimentoPrimaNota;
	}
	/**
	 * @param saltaInserimentoPrimaNota the saltaInserimentoPrimaNota to set
	 */
	public void setSaltaInserimentoPrimaNota(boolean saltaInserimentoPrimaNota) {
		this.saltaInserimentoPrimaNota = saltaInserimentoPrimaNota;
	}
	
	
	/**
	 * @return the richiediConfermaUtente
	 */
	public boolean isRichiediConfermaUtente() {
		return richiediConfermaUtente;
	}
	/**
	 * @param richiediConfermaUtente the richiediConfermaUtente to set
	 */
	public void setRichiediConfermaUtente(boolean richiediConfermaUtente) {
		this.richiediConfermaUtente = richiediConfermaUtente;
	}
	/**
	 * @return the uidRegistrazione
	 */
	public int getUidDaCompletare() {
		return uidDaCompletare;
	}
	
	/**
	 * Sets the uid da completare.
	 *
	 * @param uidDaCompletare the new uid da completare
	 */
	public void setUidDaCompletare(int uidDaCompletare) {
		this.uidDaCompletare = uidDaCompletare;
	}
	/**
	 * Controlla se l'ente sia o meno abilitato gestione prima nota da finanziaria.
	 *
	 * @return true, if is ente abilitato gestione prima nota da finanziaria false altrimenti
	 */
	public boolean isEnteAbilitatoGestionePrimaNotaDaFinanziaria() {
		return "TRUE".equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
	}
	public boolean isSalvataConValidaOra() {
		return salvataConValidaOra;
	}
	public void setSalvataConValidaOra(boolean salvataConValidaOra) {
		this.salvataConValidaOra = salvataConValidaOra;
	}
}
