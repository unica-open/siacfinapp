/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.model.primanotaintegrata.subaccertamento;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel;


/**
 * Completamento e validazione della prima nota integrata sul subaccertamento. Modulo FIN
 * 
 * @author elisa
 * @version 1.0.0 - 13-03-2018
 */
public class CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel extends CompletaValidaSubAccertamentoInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4255562282369626459L;

	/** Costruttore vuoto di default */
	public CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
