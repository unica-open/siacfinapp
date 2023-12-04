/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.io.Serializable;

import it.csi.siac.siaccorser.model.TipoClassificatore;

public class GestoreDatiAlberoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//uid
	private Integer uid, uidPadre;
	
	//id, pId, name, codice, tipo
	private String id, pId, name, codice, tipo;
	
	//flag per checked e open
	private boolean checked = false, open = false;
	
	//tipo classificatore
	private TipoClassificatore tipoClassificatore;
	
	public TipoClassificatore getTipoClassificatore() {
		return tipoClassificatore;
	}
	public void setTipoClassificatore(TipoClassificatore tipoClassificatore) {
		this.tipoClassificatore = tipoClassificatore;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getUidPadre() {
		return this.uidPadre;
	}
	public void setUidPadre(Integer uidPadre) {
		this.uidPadre = uidPadre;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
