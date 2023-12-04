/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.subaccertamento;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento.GestioneSubAccertamentoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento.GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subaccertamento. Modulo FIN
 * 
 * @author elisa
 * @version 1.0.0 - 13-03-2018
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBACCERTAMENTO_FIN)
public class CompletaValidaSubAccertamentoInsContoPrimaNotaIntegrataFINAction extends GestioneSubAccertamentoInsContoPrimaNotaIntegrataBaseAction <CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4432362484965013154L;



	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}

