/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.displaytag;

import java.math.BigDecimal;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;

/**
 * utiilzzato nel tag display delle jsp e serve a formattare 
 * i numeri interi che altrimenti riporterebbero gli zeri dopo la virgola
 * @author 
 *
 */
public class ConverterNumeroIntero implements DisplaytagColumnDecorator {

	private static final LogUtil LOG = new LogUtil(ConverterNumeroIntero.class);
	
	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		try {
			if (columnValue != null) {
				String trimmed = columnValue.toString().trim();
				if(trimmed.contains(",")){
					trimmed = trimmed.replace(",", ".");
				}
				if(FinUtility.isNumeroIntero(trimmed)){
					Integer intnumber = Integer.parseInt(trimmed);
					columnValue = intnumber.toString();
				} else if (FinUtility.isNumero(trimmed)){
					BigDecimal numeroBD = new BigDecimal(trimmed);
					Integer intnumber = numeroBD.intValue();
					columnValue = intnumber.toString();
				}
			}
		} catch (Exception nfe) {
			LOG.error("Exception", nfe.getMessage(), nfe);
		}
		return columnValue;
	}
}