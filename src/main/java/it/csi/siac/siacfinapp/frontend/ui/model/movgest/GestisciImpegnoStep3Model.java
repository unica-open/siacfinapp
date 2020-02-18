/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;



public class GestisciImpegnoStep3Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	
	private List<ImpegniPluriennaliModel> listaImpegniPluriennali = new ArrayList<ImpegniPluriennaliModel>();
	private Integer numeroMovimentoInseritoInStep2;
	private Integer annoMovimentoInseritoInStep2;
	
	
	private boolean checkproseguiPlurAcc;
	


	/**
	 * @return the numeroMovimentoInseritoInStep2
	 */
	public Integer getNumeroMovimentoInseritoInStep2() {
		return numeroMovimentoInseritoInStep2;
	}


	/**
	 * @param numeroMovimentoInseritoInStep2 the numeroMovimentoInseritoInStep2 to set
	 */
	public void setNumeroMovimentoInseritoInStep2(
			Integer numeroMovimentoInseritoInStep2) {
		this.numeroMovimentoInseritoInStep2 = numeroMovimentoInseritoInStep2;
	}


	/**
	 * @return the annoMovimentoInseritoInStep2
	 */
	public Integer getAnnoMovimentoInseritoInStep2() {
		return annoMovimentoInseritoInStep2;
	}


	/**
	 * @param annoMovimentoInseritoInStep2 the annoMovimentoInseritoInStep2 to set
	 */
	public void setAnnoMovimentoInseritoInStep2(Integer annoMovimentoInseritoInStep2) {
		this.annoMovimentoInseritoInStep2 = annoMovimentoInseritoInStep2;
	}


	public List<ImpegniPluriennaliModel> getListaImpegniPluriennali() {
		return listaImpegniPluriennali;
	}


	public void setListaImpegniPluriennali(
			List<ImpegniPluriennaliModel> listaImpegniPluriennali) {
		this.listaImpegniPluriennali = listaImpegniPluriennali;
	}


	public boolean isCheckproseguiPlurAcc() {
		return checkproseguiPlurAcc;
	}


	public void setCheckproseguiPlurAcc(boolean checkproseguiPlurAcc) {
		this.checkproseguiPlurAcc = checkproseguiPlurAcc;
	}

 
	
	
	
}
