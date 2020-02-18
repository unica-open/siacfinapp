/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.subaccertamento;


import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.GenericContabilitaGeneraleAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento.GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector.CausaleEPFINSelector;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subaccertamento. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBACCERTAMENTO_FIN)
public class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINAction extends GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction<CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6431532668393724639L;

	@Override
	@BreadCrumb(GenericContabilitaGeneraleAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}
	
	@Override
	protected FinSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return FinSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GEN;
	}
	
	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPFINSelector(ottieniElementoPianoDeiContiDaMovimento(), ottieniSoggettoDaMovimento());
	}
}

