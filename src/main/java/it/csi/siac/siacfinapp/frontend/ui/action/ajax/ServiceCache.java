/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ajax;

import java.io.Serializable;

public class ServiceCache <R extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//generico oggetto in cache
	private R value;
	
	//riferimento temporale di immissione in cache
	private long birth;
	
	public ServiceCache(R value, long birth) {
		this.value = value;
		this.birth = birth;
	}
	
	public R getValue() {
		return value;
	}
	
	public long getBirth() {
		return birth;
	}
}