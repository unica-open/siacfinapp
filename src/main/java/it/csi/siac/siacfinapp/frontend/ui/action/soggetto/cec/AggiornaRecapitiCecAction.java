/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.AggiornaRecapitiAction;
import it.csi.siac.siacfinser.Constanti;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaRecapitiCecAction extends AggiornaRecapitiAction {

	private static final long serialVersionUID = -8401163675864793711L;

	@Override
	protected String getCodificaAmbito() {
		//settiamo l'ambito
		//cec in questo caso
		return Constanti.AMBITO_CEC;
	}
	
}
