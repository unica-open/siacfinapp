/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
/**
 * usata per la cache dei model
 * 
 * @author 
 *
 */
public class TTLCache implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<? extends CodificaFin> list;
	private long birth;
	public TTLCache(List<? extends CodificaFin> list, long birth) {
		this.list = list;
		this.birth = birth;
	}
	public List<? extends CodificaFin> getList() {
		return list;
	}
	public long getBirth() {
		return birth;
	}
}