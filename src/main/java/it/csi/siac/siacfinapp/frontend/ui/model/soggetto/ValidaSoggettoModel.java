/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class ValidaSoggettoModel extends GenericoDettaglioSoggettoModel {

	private static final long serialVersionUID = 3127899219385898149L;

	//dettaglio soggetto mod
	private Soggetto dettaglioSoggettoMod;
	
	public ValidaSoggettoModel(){
		//rimando al costrutture della super classe:
		super();
		//e setto il titolo:
		setTitolo("Valida Soggetto");
	}


	public Soggetto getDettaglioSoggettoMod() {
		return dettaglioSoggettoMod;
	}

	public void setDettaglioSoggettoMod(Soggetto dettaglioSoggettoMod) {
		this.dettaglioSoggettoMod = dettaglioSoggettoMod;
	}
	
	public String getClassiFormattateMod() {
		if (dettaglioSoggettoMod != null){
			return getClassiFormattate(dettaglioSoggettoMod.getElencoClass());
		} else {
			//nulla in elenco
			return " ";
		}
	}
	
	public String getOneriFormattatiMod() {
		if (dettaglioSoggettoMod != null) {
			return getOneriFormattati(dettaglioSoggettoMod.getElencoOneri());
		} else {
			//nulla in elenco
			return " ";
		}
	}

}
