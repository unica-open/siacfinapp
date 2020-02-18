/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.SalvaSoggettoAction;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.SalvaSoggettoModel;



@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SalvaSoggettoCecAction extends SalvaSoggettoAction<SalvaSoggettoModel> {

	private static final long serialVersionUID = 7552296382272005111L;
	
	//non ci sono campi
	//bastano quelli della super classe
}
