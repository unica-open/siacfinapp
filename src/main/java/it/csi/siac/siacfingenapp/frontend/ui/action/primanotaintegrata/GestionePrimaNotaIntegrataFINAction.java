/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.GestionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataFINModel;

/**
 * Classe di action per la gestione della prima nota integrata. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestionePrimaNotaIntegrataFINAction extends GestionePrimaNotaIntegrataBaseAction<GestionePrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7391211646801700091L;

}
