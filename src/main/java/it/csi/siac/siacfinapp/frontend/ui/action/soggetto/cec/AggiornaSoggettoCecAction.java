/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.AggiornaSoggettoAction;
import it.csi.siac.siacfinser.Constanti;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSoggettoCecAction extends AggiornaSoggettoAction {

	private static final long serialVersionUID = -3398243443253410964L;

	@Override
	protected String getCodificaAmbito() {
		//settiamo l'ambito
		//cec in questo caso
		return Constanti.AMBITO_CEC;
	}
	
	@Override
	protected void checkCampiFEL(List<Errore> listaErrori) {
		//SIAC-6841
	}
	
}
