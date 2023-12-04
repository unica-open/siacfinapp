/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class ConsultaSoggettoModel extends GenericoDettaglioSoggettoModel {

	private static final long serialVersionUID = 916728761599239485L;
	
	//lista delle modalita pagamento
	private List<ModalitaPagamentoSoggetto> modalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//lista delle sedi secondarie
	private List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	
	public ConsultaSoggettoModel(){
		//chiamo il costruttore della super classe:
		super();
	}
	
	//GETTER E SETTER:
	
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

}
