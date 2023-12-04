/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.RicercaSoggettoAction;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaSoggettoCecAction extends RicercaSoggettoAction{

	private static final long serialVersionUID = 8762696047696000567L;

	@Override
	protected void caricaListeRicercaSoggetto() {
		
		//invoco il carica liste della super classe:
		super.caricaListeRicercaSoggetto();
				
		ArrayList<CodificaFin> tmp = new ArrayList<CodificaFin>();
		
		//ciclo su lista stato
		for (CodificaFin statoSoggetto : model.getListaStatoOperativoSoggetto()){
			if ("VALIDO".equals(statoSoggetto.getCodice()) || "ANNULLATO".equals(statoSoggetto.getCodice())){
				//se valido o annullato
				tmp.add(statoSoggetto);
			}
		}
		
		model.setListaStatoOperativoSoggetto(tmp);
	}

}
