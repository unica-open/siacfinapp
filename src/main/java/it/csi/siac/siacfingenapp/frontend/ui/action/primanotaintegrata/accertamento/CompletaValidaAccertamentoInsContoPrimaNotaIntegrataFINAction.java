/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.accertamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.accertamento.GestioneAccertamentoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.accertamento.GestioneAccertamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.accertamento.CompletaValidaAccertamentoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, accertamento. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2015
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneAccertamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ACCERTAMENTO_FIN)
public class CompletaValidaAccertamentoInsContoPrimaNotaIntegrataFINAction extends GestioneAccertamentoInsContoPrimaNotaIntegrataBaseAction <CompletaValidaAccertamentoInsPrimaNotaIntegrataFINModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = -5032324859868869860L;

	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}

