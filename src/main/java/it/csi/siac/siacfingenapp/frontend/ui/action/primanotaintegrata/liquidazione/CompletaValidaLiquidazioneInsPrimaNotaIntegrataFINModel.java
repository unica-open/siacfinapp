/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfingenapp.frontend.ui.action.primanotaintegrata.liquidazione;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione.CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel;


/**
 * Completamento e validazione della prima nota integrata sulla liquidazione.
 */
public class CompletaValidaLiquidazioneInsPrimaNotaIntegrataFINModel extends CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166240681408158786L;
	
	/** Costruttore vuoto di default */
	public CompletaValidaLiquidazioneInsPrimaNotaIntegrataFINModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}


