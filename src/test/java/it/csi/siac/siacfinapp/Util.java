/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

public class Util {

	public BigDecimal convertiImportoToBigDecimal(String importoFormattato) {

		BigDecimal importoDB = null;

		if(null!=importoFormattato && importoFormattato.contains(".")){
			importoFormattato = importoFormattato.replace(".", "");
			if(StringUtils.isEmpty(importoFormattato)) importoFormattato = "0";
		}
		
		
		if(null!=importoFormattato && importoFormattato.contains(",")){

			importoFormattato = importoFormattato.replace(",", ".");
			if(StringUtils.isEmpty(importoFormattato)) importoFormattato = "0";
		}	
		
//		ROUND_DOWN non arrotonda
//		ROUND_HALF_UP si arrotonda al 3 decimale superiore
		importoDB = new BigDecimal(importoFormattato).setScale(2,BigDecimal.ROUND_DOWN);

		
		return importoDB;
	}
	public String convertiBigDecimalToImporto(BigDecimal importoDB) {

		String importoFormattato = null;

		DecimalFormat df = new DecimalFormat("#,###,##0.00");

		importoFormattato = df.format(importoDB);

		return importoFormattato;
	}
	
	public static void main(String[] args) {
		
		String s = new String("100152352325235");
		Util util = new Util();
		
		util.convertiImportoToBigDecimal(s);
		
		//BigDecimal bd = new BigDecimal("1001");
		
		
//		System.out.println("CONVERSIONE "+util.convertiBigDecimalToImporto(util.convertiImportoToBigDecimal(s)));
		
	}
	
	
}
