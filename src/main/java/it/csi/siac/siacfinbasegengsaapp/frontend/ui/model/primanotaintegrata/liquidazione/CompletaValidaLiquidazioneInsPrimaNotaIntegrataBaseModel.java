/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione;



/**
 * Completamento e validazione della prima nota integrata sulla liquidazione.
 */
public abstract class CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel extends GestioneLiquidazionePrimaNotaIntegrataBaseModel{

	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1306011990899514978L;

	/** Costruttore vuoto di default */
	public CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isValidazione() {
		return true;
	}

	@Override
	public boolean isFromRegistrazione() {
		return true;
	}

	@Override
	public String getBaseUrl() {
		return "completaValidaLiquidazioneInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}



}


