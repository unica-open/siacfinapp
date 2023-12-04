/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class MovGestModel extends GenericPopupModel {
	
	private static final long serialVersionUID = 1L;
	
	//lista stato operativo movgest
	public List<CodificaFin> listaStatoOperativoMovgest;
	
	//lista tipi impegno
	public List<CodificaFin> listaTipiImpegno;
	
	//lista motivazioni assenza cig:
	public List<CodificaFin> listaMotivazioniAssenzaCig;
	
	//lista avanzovincolo
	public List<Avanzovincolo> listaAvanzovincolo;
	//non uso codifica perche' serve sapere anche l'importo
	
	public List<CodificaFin> getListaStatoOperativoMovgest() {
		return listaStatoOperativoMovgest;
	}

	public void setListaStatoOperativoMovgest(List<CodificaFin> listaStatoOperativoMovgest) {
		this.listaStatoOperativoMovgest = listaStatoOperativoMovgest;
	}

	public List<CodificaFin> getListaTipiImpegno() {
		return listaTipiImpegno;
	}

	public void setListaTipiImpegno(List<CodificaFin> listaTipiImpegno) {
		this.listaTipiImpegno = listaTipiImpegno;
	}

	public List<Avanzovincolo> getListaAvanzovincolo() {
		return listaAvanzovincolo;
	}

	public void setListaAvanzovincolo(List<Avanzovincolo> listaAvanzovincolo) {
		this.listaAvanzovincolo = listaAvanzovincolo;
	}

	public List<CodificaFin> getListaMotivazioniAssenzaCig() {
		return listaMotivazioniAssenzaCig;
	}

	public void setListaMotivazioniAssenzaCig(List<CodificaFin> listaMotivazioniAssenzaCig) {
		this.listaMotivazioniAssenzaCig = listaMotivazioniAssenzaCig;
	}	
	
}
