/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ModalitaPagamentoSoggettoAction;
import it.csi.siac.siacfinser.CostantiFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ModalitaPagamentoSoggettoCecAction extends ModalitaPagamentoSoggettoAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6002079322682672124L;
	
	@Override
	protected String getcodiceAmbito() {
		//ritorniamo la codifica 
		//dell'ambito, cec in questo caso
		return CostantiFin.AMBITO_CEC;
	}

}
