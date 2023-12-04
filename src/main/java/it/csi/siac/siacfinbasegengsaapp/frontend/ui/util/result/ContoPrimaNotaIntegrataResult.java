/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.result;


/**
 * Risultato per i conti della prima nota integrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/05/2015
 * @author Elisa Chiari
 * @version 2.0.0 - 15/03/2017
 *
 */
public class ContoPrimaNotaIntegrataResult extends CustomJSONResult {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8361281885205372584L;
	/** Propriet&agrave; da includere nel JSON creato */
	private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaElementoScritturaPerElaborazione.*, totaleDare.*, totaleAvere.*, daRegistrare.*, contiCausale.*, classeDiConciliazione.*, conciliazioneDaSostituire ";

	/** Empty default constructor */
	public ContoPrimaNotaIntegrataResult() {
		super();
		setIncludeProperties(INCLUDE_PROPERTIES);
	}
}
