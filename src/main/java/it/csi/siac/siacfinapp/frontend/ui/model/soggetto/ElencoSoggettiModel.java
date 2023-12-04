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

public class ElencoSoggettiModel extends SoggettoModel{

	private static final long serialVersionUID = 3127899219385898149L;

	//dettaglio soggetto:
	private Soggetto dettaglioSoggetto;
	
	//lista modalita pagamento:
	private List<ModalitaPagamentoSoggetto> modalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//lista sedi secondarie:
	private List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	
	public ElencoSoggettiModel(){
		super();
		setTitolo("Elenco Soggetti");
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

}
