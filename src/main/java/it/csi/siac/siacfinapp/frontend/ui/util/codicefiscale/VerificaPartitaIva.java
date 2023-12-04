/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale;
/**
 * Classe che verifica la correttezza della partita iva secondo i criteri standard
 * 
 * @author paolos
 *
 */
public final class VerificaPartitaIva {

	
	public static String controllaPIVA(String pi)
	{
	    int i, c, s;
	    if( pi.length() == 0 )  return "";
	    if( pi.length() != 11 ){
	        return "La lunghezza della partita IVA non &egrave;\n"
	        + "corretta: la partita IVA dovrebbe essere lunga\n"
	        + "esattamente 11 caratteri.\n";
	    }    
	    for( i=0; i<11; i++ ){
	        if( pi.charAt(i) < '0' || pi.charAt(i) > '9' ){
	            return "La partita IVA contiene dei caratteri non ammessi:\n"
	            + "la partita IVA dovrebbe contenere solo cifre.\n";
	        }    
	    }
	    s = 0;
	    for( i=0; i<=9; i+=2 ){
	        s += pi.charAt(i) - '0';
	    }    
	    for( i=1; i<=9; i+=2 ){
	        c = 2*( pi.charAt(i) - '0' );
	        if( c > 9 ){
	        	c = c - 9;
	        }
	        s += c;
	    }
	    if( ( 10 - s%10 )%10 != pi.charAt(10) - '0' ){
	        return "La partita IVA non &egrave; valida:\n"
	        + "il codice di controllo non corrisponde.";
	    }
	    // se tutto bene restituisce la stringa OK
	    return "OK";
	}
	
}
