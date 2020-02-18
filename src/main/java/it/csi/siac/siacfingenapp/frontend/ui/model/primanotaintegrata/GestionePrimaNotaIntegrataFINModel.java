/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.GestionePrimaNotaIntegrataBaseModel;

/**
 * Model per la gestione della prima nota integrata. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - gestione GEN/GSA
 */
public class GestionePrimaNotaIntegrataFINModel extends GestionePrimaNotaIntegrataBaseModel {

	

	/** Per la serializzazione */
	private static final long serialVersionUID = 6550555491059358236L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
