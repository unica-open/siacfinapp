/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * Classe di conversione per i {@link BigDecimal} da e verso {@link String}.
 * 
 * @author Marchino Alessandro
 *
 */
@SuppressWarnings("rawtypes")
public class BigDecimalConverter extends StrutsTypeConverter {

	// Formattatore per i decimali	
	private DecimalFormat df;
	
	/** Costruttore vuoto di default */
	public BigDecimalConverter() {
		// Inizializzo il decimalFormat
		// formattato con locale=Italy
		df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
	}
	
	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		if(values.length != 1) {
			// Nel caso in cui non vi sia esattamente un parametro da convertire, si effettui il fallback
			super.performFallbackConversion(context, values, clazz);
		}
		
		String str = values[0];
		
		BigDecimal result = null;
		try {
			if(str.contains(",")) {
				// Sono nel caso di una formulazione numerica con locale italiano
				result = (BigDecimal) df.parse(str);
			} else {
				// Non ho un locale ben definito (probabilmente sono in un locale inglese). Converto in maniera standard
				result = new BigDecimal(str);
			}
		} catch (ParseException e) {
			throw new TypeConversionException("Conversione in BigDecimal fallita", e);
		} catch (NumberFormatException e) {
			throw new TypeConversionException("Conversione in BigDecimal fallita", e);
		}
		return result;
	}

	@Override
	public String convertToString(Map context, Object value) {
		try {
			return df.format(value);
		} catch(IllegalArgumentException e) {
			/* Non ho grosse necessità di implementare il catch: va bene anche il caso in cui non possa fare nulla */
			return null;
		}
	}
	
}
