/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class RicercaElencoSoggettoModel extends GenericoRicercaSoggettoModel {

	private static final long serialVersionUID = 1L;
	
	//Codice fiscale estero
	private String codiceFiscaleEstero;
	
	//dettagli soggetto
	private Soggetto dettaglioSoggetto;
	
	//modalita pagamento
	private List<ModalitaPagamentoSoggetto> modalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//sedi secondarie
	private List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	
	private int resultSize;
	
	public RicercaElencoSoggettoModel(){
		//richiamo il costruttore della super classe
		super();
	}
	
	public Soggetto getDettaglioSoggetto() {
		return dettaglioSoggetto;
	}
	
	public void setDettaglioSoggetto(Soggetto dettaglioSoggetto) {
		this.dettaglioSoggetto = dettaglioSoggetto;
	}
	
	public List<ModalitaPagamentoSoggetto> getModalitaPagamento() {
		return modalitaPagamento;
	}
	
	public void setModalitaPagamento(List<ModalitaPagamentoSoggetto> modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}
	
	public List<SedeSecondariaSoggetto> getSediSecondarie() {
		return sediSecondarie;
	}
	
	public void setSediSecondarie(List<SedeSecondariaSoggetto> sediSecondarie) {
		this.sediSecondarie = sediSecondarie;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public String getCodiceFiscaleEstero() {
		return codiceFiscaleEstero;
	}

	public void setCodiceFiscaleEstero(String codiceFiscaleEstero) {
		this.codiceFiscaleEstero = codiceFiscaleEstero;
	}
	
}
