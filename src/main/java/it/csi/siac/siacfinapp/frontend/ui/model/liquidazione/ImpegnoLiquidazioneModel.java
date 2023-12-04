/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.liquidazione;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class ImpegnoLiquidazioneModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//anno
	private Integer annoImpegno;
	
	//numero
	private Integer numeroImpegno;
	
	//numero sub
	private Integer numeroSub;
	
	//uid
	private Integer uid;
	
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public Integer getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(Integer numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public Integer getNumeroSub() {
		return numeroSub;
	}
	public void setNumeroSub(Integer numeroSub) {
		this.numeroSub = numeroSub;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
}
