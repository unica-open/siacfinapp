/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.impegno;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.GenericContabilitaGeneraleAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.annotation.PutModelInSession;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.impegno.GestioneImpegnoInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector.CausaleEPFINSelector;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.impegno.CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, impegno. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2015
 * @version 1.1.0 - 13/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneImpegnoInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_IMPEGNO_FIN)
public class CompletaValidaImpegnoInsPrimaNotaIntegrataFINAction extends GestioneImpegnoInsPrimaNotaIntegrataBaseAction<CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel> {
	/** Per la serializzazione */
	private static final long serialVersionUID = 7950931957094126057L;



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
