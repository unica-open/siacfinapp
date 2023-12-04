/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.converter;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

public class IntegerConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		if(values.length != 1) {
			// Nel caso in cui non vi sia esattamente un parametro da convertire, si effettui il fallback
			return super.performFallbackConversion(context, values, clazz);
		}
		
		LogWebUtil log = new LogWebUtil(getClass());
		final String methodName = "convertFromString";
		
		String str = values[0];
		
		Object result = null;
		try {
			
			if(StringUtils.isNotEmpty(str)) {
				result = Integer.parseInt(str.trim());
			}
			
		} catch (NumberFormatException e) {
			log.info(methodName, "Conversione in java.lang.Integer fallita causa NumberFormatException: " + e.getMessage());
			result = super.performFallbackConversion(context, values, clazz);
		} catch (NullPointerException e) {
			log.info(methodName, "Conversione in java.lang.Integer fallita causa NullPointerException: " + e.getMessage());
			result = super.performFallbackConversion(context, values, clazz);
		}
		// Mangio l'eccezione e continuo
		return result;
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		int i = Integer.getInteger((String) arg1);
		return Integer.toString(i);
	}
	
}
