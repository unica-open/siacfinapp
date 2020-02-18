/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale;

public class OmocodiaUtil {
	
	private static final int[] OMOCODIA_POS = new int[] { 6, 7, 9, 10, 12, 13, 14 };
	private static String OMOCODIA_MAPPING = "LMNPQRSTUV";

	public boolean isOmocodo(String codiceFiscale) {
		//string builder inizializzato con il codice fiscale maiuscolo:
		StringBuilder cf = new StringBuilder(codiceFiscale.toUpperCase());

		for (int i : OMOCODIA_POS) {
			char c = cf.charAt(i);

			if (c >= 'A' && c <= 'Z')
				return true;
		}

		return false;
	}

	public String convertToStandard(String codiceFiscale) {
		if (!isOmocodo(codiceFiscale))
			return codiceFiscale;

		StringBuilder cf = new StringBuilder(codiceFiscale);

		for (int i : OMOCODIA_POS) {
			int idx = OMOCODIA_MAPPING.indexOf(cf.charAt(i));

			if (idx != -1)
				cf.setCharAt(i, (char) ((int) '0' + idx));
		}

		cf.setCharAt(15, calcolaCodiceControllo(cf.toString()));
		
		return cf.toString();
	}
	
	private char calcolaCodiceControllo(String string) {
		//per determinare il carattere di controllo
		int sum = 0;

		for (int i = 0; i < 15; i++) {
			char c = string.charAt(i);

			int x = (Character.isDigit(c) ? Character.getNumericValue(c) : new String(
					new char[] { c }).getBytes()[0] - 65);

			sum += ((i + 1) % 2 == 0 ? x : new int[] { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18,
					20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 }[x]);
		}

		return (char) ((sum % 26) + 65);
	}

	
}
