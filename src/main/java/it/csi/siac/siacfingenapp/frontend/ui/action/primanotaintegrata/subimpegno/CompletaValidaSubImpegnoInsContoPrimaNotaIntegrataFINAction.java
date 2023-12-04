/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.subimpegno;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno.GestioneSubImpegnoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno.GestioneSubImpegnoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaValidaSubImpegnoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subimpegno. Modulo FIN
 * 
 * @author elisa
 * @version 1.0.0 - 12-03-2018
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneSubImpegnoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBIMPEGNO_FIN)
public class CompletaValidaSubImpegnoInsContoPrimaNotaIntegrataFINAction extends GestioneSubImpegnoInsContoPrimaNotaIntegrataBaseAction <CompletaValidaSubImpegnoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6699253779483645567L;

	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}

