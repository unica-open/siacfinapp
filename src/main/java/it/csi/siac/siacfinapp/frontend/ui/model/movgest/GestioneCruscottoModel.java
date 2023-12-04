/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.io.Serializable;

public class GestioneCruscottoModel  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idMotivo;
	private String descrizione;
	private String valoreSintesi;
	
	private String importo;
	private Integer anno;
	private Integer index;
	
	
	
	
	public GestioneCruscottoModel() {

	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public String getIdMotivo() {
		return idMotivo;
	}
	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getImporto() {
		return importo;
	}
	public void setImporto(String importo) {
		this.importo = importo;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getValoreSintesi() {
		return valoreSintesi;
	}
	public void setValoreSintesi(String valoreSintesi) {
		this.valoreSintesi = valoreSintesi;
	}
	
	

}
