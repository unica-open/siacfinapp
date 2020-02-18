/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale;

/**
 * utilizzata per la validazione del CF nella parte di
 * gestione del soggetto
 * @author 
 *
 */
public class ValidaCF {

	
	public String controllaCF(String codiceFiscale) 
	{
		OmocodiaUtil omocodiaUtil = new OmocodiaUtil();
		
		String cf = omocodiaUtil.convertToStandard(codiceFiscale);
				
	    int i, s, c;
	    String cf2;
	    int setdisp[] = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20,
	        11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
	    if( cf.length() == 0 ) {
	    	return "";
	    }
	    if( cf.length() != 16 ){
	        return "La lunghezza del codice fiscale non &egrave;\n"
	        + "corretta: il codice fiscale dovrebbe essere lungo\n"
	        + "esattamente 16 caratteri.";
	    }    
	    cf2 = cf.toUpperCase();
	    for( i=0; i<16; i++ ){
	        c = cf2.charAt(i);
	        if( ! ( c>='0' && c<='9' || c>='A' && c<='Z' ) ){
	        	return "Il codice fiscale contiene dei caratteri non validi:\n"
	    	            + "i soli caratteri validi sono le lettere e le cifre.";
	        }
	            
	    }
	    s = 0;
	    for( i=1; i<=13; i+=2 ){
	        c = cf2.charAt(i);
	        if( c>='0' && c<='9' ){
	        	 s = s + c - '0';
	        }else{
	            s = s + c - 'A';
	        }    
	    }
	    for( i=0; i<=14; i+=2 ){
	        c = cf2.charAt(i);
	        if( c>='0' && c<='9' ){
	        	c = c - '0' + 'A';
	        }
	        s = s + setdisp[c - 'A'];
	    }
	    if( s%26 + 'A' != cf2.charAt(15) ){
	        return "Il codice fiscale non &egrave; corretto:\n"
	        + "il codice di controllo non corrisponde.";
	    }    
	    return "OK";
	}
	
}
