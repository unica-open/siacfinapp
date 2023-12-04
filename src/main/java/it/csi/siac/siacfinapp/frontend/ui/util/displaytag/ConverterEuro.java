/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.displaytag;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * utiilzzato nel tag display delle jsp e serve a formattare 
 * le cifre nella forma italiana
 * @author 
 *
 */
public class ConverterEuro implements DisplaytagColumnDecorator {

	private static DecimalFormat dFormat = new DecimalFormat("#,###,##0.00");

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		try {
			if (columnValue != null) {
				columnValue = ConverterEuro.conversione(columnValue);
			}
		} catch (Exception nfe) {
			// Nulla da fare
		}
		return columnValue;
	}
	
	public static Object conversione(Object columnValue){
		if (columnValue != null) {
			dFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ITALY);
			dFormat.setParseBigDecimal(true);
			dFormat.setMinimumFractionDigits(2);
			dFormat.setMaximumFractionDigits(2);
			columnValue = dFormat.format(columnValue);
			
		}
		return columnValue;
	}
}