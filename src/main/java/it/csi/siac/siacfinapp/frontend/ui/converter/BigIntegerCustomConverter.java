/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.converter;

import java.math.BigInteger;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class BigIntegerCustomConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class clazz) {
		//ritorna null se nullo o stringa vuota, altrimenti
		//prende l'elemento in posizione 0 e lo usa per costruire un bigdecimal
		if (values == null || values[0].equals("")) return null;
		return new BigInteger(values[0]);
	}

	@Override
	public String convertToString(Map context, Object value) {
		return value != null ? value.toString() : "";
	}

}
