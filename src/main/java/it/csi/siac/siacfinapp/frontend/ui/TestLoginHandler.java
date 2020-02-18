/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui;

import java.util.Map;

import it.csi.siac.siaccommonapp.util.login.LoginHandler;
import it.csi.siac.siaccorser.model.Operatore;

/**
 * utilizzata per la login in fase di sviluppo
 * bisogna scommentare il codice fiscale che attualmente si sta utilizzando a sistema
 * @author 
 *
 */
public class TestLoginHandler extends LoginHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7258888102942728764L;

	/**
	 * @return the operatore
	 */
	public Operatore getOperatore(Map<String, Object> session) {
		String codiceFiscale = (String)session.get("it.csi.siac.siaccruapp.login.test.codiceFiscaleOperatore");
		if (codiceFiscale==null) {
//			throw new IdentitaDigitaleNonConformeException("identita");
//			codiceFiscale = "AAAAAA00B77B000F"; // Demo 20
//			codiceFiscale = "AAAAAA00A11B000J"; //Demo 21
			codiceFiscale = "AAAAAA00A11C000K"; //Demo 22
//			codiceFiscale = "AAAAAA00A11D000L"; // Demo 23
//			codiceFiscale = "AAAAAA00A11K000S"; // Demo 30
//			codiceFiscale = "AAAAAA00A11L000T"; // Demo 31
//			codiceFiscale = "AAAAAA00A11E000M";  // Demo 24
		}
		Operatore operatore = new Operatore();
		operatore.setNome("Demo");
		operatore.setCognome(codiceFiscale);
		operatore.setCodiceFiscale(codiceFiscale);
		return operatore;
	}
	
	

}
