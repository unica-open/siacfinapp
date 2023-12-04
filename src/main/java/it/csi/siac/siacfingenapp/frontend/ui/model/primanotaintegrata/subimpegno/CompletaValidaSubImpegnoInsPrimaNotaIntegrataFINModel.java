/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.subimpegno;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel;


/**
 * Completamento e validazione della prima nota integrata sul subimpegno. Modulo FIN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 */
public class CompletaValidaSubImpegnoInsPrimaNotaIntegrataFINModel extends CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel{


	/** Per la serializzazione */
	private static final long serialVersionUID = 5941931534673984312L;

	/** Costruttore vuoto di default */
	public CompletaValidaSubImpegnoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
