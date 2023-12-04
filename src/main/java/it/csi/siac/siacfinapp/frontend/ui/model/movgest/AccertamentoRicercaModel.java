/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

/**
 * utilizzato per il pop up di ricerca accertamento in inserisci e gestione impegno, 
 * per tutta la questione dei vincoli
 * 
 * @author 
 *
 */
public class AccertamentoRicercaModel extends GenericFinModel{

	private static final long serialVersionUID = 1L;
	
	private String annoCapitolo, numeroCapitolo, numeroArticolo, ueb, annoAccertamento, numeroAccertamento;

	public String getAnnoCapitolo() {
		return annoCapitolo;
	}

	public void setAnnoCapitolo(String annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	public String getNumeroCapitolo() {
		return numeroCapitolo;
	}

	public void setNumeroCapitolo(String numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	public String getNumeroArticolo() {
		return numeroArticolo;
	}

	public void setNumeroArticolo(String numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	public String getUeb() {
		return ueb;
	}

	public void setUeb(String ueb) {
		this.ueb = ueb;
	}

	public String getAnnoAccertamento() {
		return annoAccertamento;
	}

	public void setAnnoAccertamento(String annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}

	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}

	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	
}
