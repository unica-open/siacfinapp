/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class ModalitaPagamentoSoggettoModel extends SoggettoModel {


	private static final long serialVersionUID = 7428497723299760614L;

	//sedi secondarie
	private List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	
	//dettaglio ricercasoggetto
	private List<Soggetto> dettaglioRicercaSoggetto = new ArrayList<Soggetto>();
	
	//modalita pagamento cessione
	private List<ModalitaPagamentoSoggetto> modalitaPagamentoCessione = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//modalita pagamento cessione aggiorna
	private List<ModalitaPagamentoSoggetto> modalitaPagamentoCessioneAggiorna = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//dettaglio soggetto cessione
	private Soggetto dettaglioSoggettoCessione = new Soggetto();
	
	//sedisecondaria soggettocessione
	private List<SedeSecondariaSoggetto> sedisecondariaSoggettoCessione = new ArrayList<SedeSecondariaSoggetto>();
	
	//sedisecondaria soggetto cessione aggiorna
	private List<SedeSecondariaSoggetto> sedisecondariaSoggettoCessioneAggiorna = new ArrayList<SedeSecondariaSoggetto>();
	
	//soggetti ricercati
	private List<Soggetto> soggettiRicercati = new ArrayList<Soggetto>();
	
	//soggetti ricercati aggiorna
	private List<Soggetto> soggettiRicercatiAggiorna = new ArrayList<Soggetto>();
	
	//dettaglio soggetto cessione aggiorna
	private Soggetto dettaglioSoggettoCessioneAggiorna = new Soggetto();
	
	//code mdp selected
	private String codeMdpSelected;
	
	//modalita pagamento soggetto to insert
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToInsert;
	
	//modalitapagamento soggetto to update
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToUpdate;
	
	private List<String> associataList = new ArrayList<String>();  
	
	//Paramentri per la ricerca soggetto mod pagamento
	private Integer codice;
	private String codiceFiscale;
	private String partitaIva;
	private String denominazione;
	private Integer codiceAggiorna;
	private String codiceFiscaleAggiorna;
	private String partitaIvaAggiorna;
	private String denominazioneAggiorna;

	//Paramentri di salvataggio per il form di inserimento
	private String idTipoAccredito;
	private String denominazioneAssociatoA; 
	
	private String idAccreditoTipoSelected;
	private String tipoind;

	
	public ModalitaPagamentoSoggettoModel(){
		super();
		setTitolo("Modalita Pagamento Soggetto");
	}
	
	//GENERATED GETTER & SETTER
	
	public List<Soggetto> getDettaglioRicercaSoggetto() {
		return dettaglioRicercaSoggetto;
	}

	public void setDettaglioRicercaSoggetto(List<Soggetto> dettaglioRicercaSoggetto) {
		this.dettaglioRicercaSoggetto = dettaglioRicercaSoggetto;
	}

	public List<SedeSecondariaSoggetto> getSediSecondarie() {
		return sediSecondarie;
	}

	public void setSediSecondarie(List<SedeSecondariaSoggetto> sediSecondarie) {
		this.sediSecondarie = sediSecondarie;
	}

	public Integer getCodice() {
		return codice;
	}

	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public List<ModalitaPagamentoSoggetto> getModalitaPagamentoCessione() {
		return modalitaPagamentoCessione;
	}
	
	public void setModalitaPagamentoCessione(List<ModalitaPagamentoSoggetto> modalitaPagamentoCessione) {
		this.modalitaPagamentoCessione = modalitaPagamentoCessione;
	}

	public Soggetto getDettaglioSoggettoCessione() {
		return dettaglioSoggettoCessione;
	}

	public void setDettaglioSoggettoCessione(Soggetto dettaglioSoggettoCessione) {
		this.dettaglioSoggettoCessione = dettaglioSoggettoCessione;
	}

	public List<Soggetto> getSoggettiRicercati() {
		return soggettiRicercati;
	}

	public void setSoggettiRicercati(List<Soggetto> soggettiRicercati) {
		this.soggettiRicercati = soggettiRicercati;
	}

	public List<String> getAssociataList() {
		return associataList;
	}
	
	public void setAssociataList(List<String> associataList) {
		this.associataList = associataList;
	}

	public String getIdTipoAccredito() {
		return idTipoAccredito;
	}

	public void setIdTipoAccredito(String idTipoAccredito) {
		this.idTipoAccredito = idTipoAccredito;
	}
	
	public String getDenominazioneAssociatoA() {
		return denominazioneAssociatoA;
	}

	public void setDenominazioneAssociatoA(String denominazioneAssociatoA) {
		this.denominazioneAssociatoA = denominazioneAssociatoA;
	}
	
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoToInsert() {
		return modalitaPagamentoSoggettoToInsert;
	}

	public void setModalitaPagamentoSoggettoToInsert(ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToInsert) {
		this.modalitaPagamentoSoggettoToInsert = modalitaPagamentoSoggettoToInsert;
	}

	public String getIdAccreditoTipoSelected() {
		return idAccreditoTipoSelected;
	}

	public void setIdAccreditoTipoSelected(String idAccreditoTipoSelected) {
		this.idAccreditoTipoSelected = idAccreditoTipoSelected;
	}

	public String getTipoind() {
		return tipoind;
	}

	public void setTipoind(String tipoind) {
		this.tipoind = tipoind;
	}

	public List<Soggetto> getSoggettiRicercatiAggiorna() {
		return soggettiRicercatiAggiorna;
	}

	public void setSoggettiRicercatiAggiorna(List<Soggetto> soggettiRicercatiAggiorna) {
		this.soggettiRicercatiAggiorna = soggettiRicercatiAggiorna;
	}

	public Soggetto getDettaglioSoggettoCessioneAggiorna() {
		return dettaglioSoggettoCessioneAggiorna;
	}

	public void setDettaglioSoggettoCessioneAggiorna(Soggetto dettaglioSoggettoCessioneAggiorna) {
		this.dettaglioSoggettoCessioneAggiorna = dettaglioSoggettoCessioneAggiorna;
	}

	public Integer getCodiceAggiorna() {
		return codiceAggiorna;
	}

	public void setCodiceAggiorna(Integer codiceAggiorna) {
		this.codiceAggiorna = codiceAggiorna;
	}

	public String getCodiceFiscaleAggiorna() {
		return codiceFiscaleAggiorna;
	}

	public void setCodiceFiscaleAggiorna(String codiceFiscaleAggiorna) {
		this.codiceFiscaleAggiorna = codiceFiscaleAggiorna;
	}

	public String getPartitaIvaAggiorna() {
		return partitaIvaAggiorna;
	}

	public void setPartitaIvaAggiorna(String partitaIvaAggiorna) {
		this.partitaIvaAggiorna = partitaIvaAggiorna;
	}

	public String getDenominazioneAggiorna() {
		return denominazioneAggiorna;
	}

	public void setDenominazioneAggiorna(String denominazioneAggiorna) {
		this.denominazioneAggiorna = denominazioneAggiorna;
	}
	
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoToUpdate() {
		return modalitaPagamentoSoggettoToUpdate;
	}

	public void setModalitaPagamentoSoggettoToUpdate(ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToUpdate) {
		this.modalitaPagamentoSoggettoToUpdate = modalitaPagamentoSoggettoToUpdate;
	}

	public List<SedeSecondariaSoggetto> getSedisecondariaSoggettoCessione() {
		return sedisecondariaSoggettoCessione;
	}

	public void setSedisecondariaSoggettoCessione(List<SedeSecondariaSoggetto> sedisecondariaSoggettoCessione) {
		this.sedisecondariaSoggettoCessione = sedisecondariaSoggettoCessione;
	}

	public List<ModalitaPagamentoSoggetto> getModalitaPagamentoCessioneAggiorna() {
		return modalitaPagamentoCessioneAggiorna;
	}

	public void setModalitaPagamentoCessioneAggiorna(List<ModalitaPagamentoSoggetto> modalitaPagamentoCessioneAggiorna) {
		this.modalitaPagamentoCessioneAggiorna = modalitaPagamentoCessioneAggiorna;
	}
	
	public List<SedeSecondariaSoggetto> getSedisecondariaSoggettoCessioneAggiorna() {
		return sedisecondariaSoggettoCessioneAggiorna;
	}

	public void setSedisecondariaSoggettoCessioneAggiorna(List<SedeSecondariaSoggetto> sedisecondariaSoggettoCessioneAggiorna) {
		this.sedisecondariaSoggettoCessioneAggiorna = sedisecondariaSoggettoCessioneAggiorna;
	}

	public String getCodeMdpSelected() {
		return codeMdpSelected;
	}

	public void setCodeMdpSelected(String codeMdpSelected) {
		this.codeMdpSelected = codeMdpSelected;
	}	
	
	
}
