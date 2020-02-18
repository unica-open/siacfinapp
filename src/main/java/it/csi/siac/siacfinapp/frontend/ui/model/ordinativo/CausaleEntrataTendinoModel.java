/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.util.List;

import it.csi.siac.siaccommonapp.model.GenericModel;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.TipoCausale;

public class CausaleEntrataTendinoModel extends GenericModel {

	private static final long serialVersionUID = 1L;

	//lista tipi causale:
	private List<TipoCausale> listaTipiCausale;
	
	//codice tipo causale:
	private String codiceTipoCausale;

	//lista causali:
	private List<CausaleEntrata> listaCausali;
	
	//codice causale:
	private String codiceCausale;
	
	
	//GETTER E SETTER:
	
	public List<TipoCausale> getListaTipiCausale() {
		return listaTipiCausale;
	}
	public void setListaTipiCausale(List<TipoCausale> listaTipiCausale) {
		this.listaTipiCausale = listaTipiCausale;
	}
	public String getCodiceTipoCausale() {
		return codiceTipoCausale;
	}
	public void setCodiceTipoCausale(String codiceTipoCausale) {
		this.codiceTipoCausale = codiceTipoCausale;
	}
	public List<CausaleEntrata> getListaCausali() {
		return listaCausali;
	}
	public void setListaCausali(List<CausaleEntrata> listaCausali) {
		this.listaCausali = listaCausali;
	}
	public String getCodiceCausale() {
		return codiceCausale;
	}
	public void setCodiceCausale(String codiceCausale) {
		this.codiceCausale = codiceCausale;
	}
	
}
