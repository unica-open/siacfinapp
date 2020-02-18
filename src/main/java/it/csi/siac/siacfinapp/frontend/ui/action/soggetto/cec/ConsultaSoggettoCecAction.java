/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ConsultaSoggettoAction;
import it.csi.siac.siacfinser.Constanti;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaSoggettoCecAction extends ConsultaSoggettoAction {

	private static final long serialVersionUID = -4777943029428535479L;
	
	@Override
	protected String getCodificaAmbito() {
		//settiamo l'ambito
		//in questo caso cec:
		return Constanti.AMBITO_CEC;
	}

}
