/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class GestioneClassiModel extends SoggettoModel {

	private static final long serialVersionUID = 1L;
	
	//variabili per la selezione di una classe che si vuole gestire:
	
	//id classe
	private Integer idClasse;
	
	//lista classe
	private List<CodificaFin> listaClasse = new ArrayList<CodificaFin>();
	
	//total size elenco classi
	private int	totalSizeElencoClassi;
	
	public int getTotalSizeElencoClassi() {
		return totalSizeElencoClassi;
	}
	
	public void setTotalSizeElencoClassi(int totalSizeElencoClassi) {
		this.totalSizeElencoClassi = totalSizeElencoClassi;
	}
	
	public Integer getIdClasse() {
		return idClasse;
	}
	
	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}
		
	public List<CodificaFin> getListaClasse() {
		return listaClasse;
	}
	public void setListaClasse(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasse = listaClasseSoggetto;
	}

	
}
