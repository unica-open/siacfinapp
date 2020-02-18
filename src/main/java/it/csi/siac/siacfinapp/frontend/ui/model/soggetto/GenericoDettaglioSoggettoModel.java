/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Onere;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GenericoDettaglioSoggettoModel extends SoggettoModel {

	
	private static final long serialVersionUID = 1L;

	//dettaglio soggetto
	private Soggetto dettaglioSoggetto;
	
	public GenericoDettaglioSoggettoModel(){
		super();
	}
	
	public Soggetto getDettaglioSoggetto() {
		return dettaglioSoggetto;
	}

	public void setDettaglioSoggetto(Soggetto dettaglioSoggetto) {
		this.dettaglioSoggetto = dettaglioSoggetto;
	}

	public String getOneriFormattati() {
		if (dettaglioSoggetto != null) {
			return getOneriFormattati(dettaglioSoggetto.getElencoOneri());
		} else {
			//nulla in elenco
			return " ";
		}
	}
	
	protected String getOneriFormattati(List<Onere> lista) {
		if (lista != null && lista.size() > 0) {
			//ci sono oneri nell'elenco
			String support = "";
			for(int i=0;i<lista.size();i++){
				//ciclo sugli oneri in elenco
				if(i==lista.size()-1){
					support += lista.get(i).getOnereDesc();
				}else{
					support += lista.get(i).getOnereDesc()+" ,";
				}
			}
			//ritorno la stringa con la concatenazione delle descrizioni:
			return support;
		} else {
			//nulla in elenco
			return " ";
		}
	}
	
	public String getClassiFormattate() {
		if (dettaglioSoggetto != null){
			return getClassiFormattate(dettaglioSoggetto.getElencoClass());
		} else {
			//nulla in elenco
			return " ";
		}
	}
	
	protected String getClassiFormattate(List<ClassificazioneSoggetto> lista) {
		if (lista != null && lista.size() > 0) {
			//ci sono classi nell'elenco
			String support = "";
			for(int i=0;i<lista.size();i++){
				//ciclo suglle classi in elenco
				if(i==lista.size()-1){
					support += lista.get(i).getSoggettoClasseDesc();
				}else{
					support += lista.get(i).getSoggettoClasseDesc()+" ,";
				}
			}
			//ritorno la stringa con la concatenazione delle descrizioni:
			return support;
		} else {
			//nulla in elenco
			return " ";
		}
	}

}
