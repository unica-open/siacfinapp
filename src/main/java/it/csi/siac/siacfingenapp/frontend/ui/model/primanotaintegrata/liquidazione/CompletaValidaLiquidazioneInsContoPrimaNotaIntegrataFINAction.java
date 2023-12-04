/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.liquidazione;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione.GestioneLiquidazioneInsContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione.GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.liquidazione.CompletaValidaLiquidazioneInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio
 * 
 * @author Paggio Simona
 * @author Valentina
 * @version 1.0.0 - 14/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_LIQUIDAZIONE_FIN)
public class CompletaValidaLiquidazioneInsContoPrimaNotaIntegrataFINAction extends GestioneLiquidazioneInsContoPrimaNotaIntegrataBaseAction<CompletaValidaLiquidazioneInsPrimaNotaIntegrataFINModel>{

	
	/** Per la serializzazione*/
	private static final long serialVersionUID = -4361718583591409887L;

	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}


}

