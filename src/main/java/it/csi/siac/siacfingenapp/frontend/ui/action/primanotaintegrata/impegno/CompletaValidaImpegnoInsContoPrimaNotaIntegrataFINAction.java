/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.impegno;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.impegno.GestioneImpegnoInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.impegno.GestioneImpegnoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.impegno.CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, impegno. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2015
 * @version 1.1.0 - 13/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneImpegnoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_IMPEGNO_FIN)
public class CompletaValidaImpegnoInsContoPrimaNotaIntegrataFINAction extends GestioneImpegnoInsContoPrimaNotaIntegrataBaseAction <CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3594580568404362452L;

	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}

}
