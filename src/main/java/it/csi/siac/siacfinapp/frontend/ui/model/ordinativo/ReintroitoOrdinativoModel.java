/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;

public class ReintroitoOrdinativoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;
	
	private boolean elaborazioneAvviata;
	
	private boolean datiInizializzatiPrepareStep1;
	private boolean datiInizializzatiExecuteStep1;
	
	//MODEL DELLO STEP 1:
	private ReintroitoOrdinativoStep1Model reintroitoOrdinativoStep1Model = new ReintroitoOrdinativoStep1Model();
	
	//MODEL DELLO STEP 2:
	private ReintroitoOrdinativoStep2Model reintroitoOrdinativoStep2Model = new ReintroitoOrdinativoStep2Model();

	public ReintroitoOrdinativoStep1Model getReintroitoOrdinativoStep1Model() {
		return reintroitoOrdinativoStep1Model;
	}

	public void setReintroitoOrdinativoStep1Model(ReintroitoOrdinativoStep1Model reintroitoOrdinativoStep1Model) {
		this.reintroitoOrdinativoStep1Model = reintroitoOrdinativoStep1Model;
	}

	public ReintroitoOrdinativoStep2Model getReintroitoOrdinativoStep2Model() {
		return reintroitoOrdinativoStep2Model;
	}

	public void setReintroitoOrdinativoStep2Model(ReintroitoOrdinativoStep2Model reintroitoOrdinativoStep2Model) {
		this.reintroitoOrdinativoStep2Model = reintroitoOrdinativoStep2Model;
	}

	public boolean isElaborazioneAvviata() {
		return elaborazioneAvviata;
	}

	public void setElaborazioneAvviata(boolean elaborazioneAvviata) {
		this.elaborazioneAvviata = elaborazioneAvviata;
	}

	public boolean isDatiInizializzatiPrepareStep1() {
		return datiInizializzatiPrepareStep1;
	}

	public void setDatiInizializzatiPrepareStep1(boolean datiInizializzatiPrepareStep1) {
		this.datiInizializzatiPrepareStep1 = datiInizializzatiPrepareStep1;
	}

	public boolean isDatiInizializzatiExecuteStep1() {
		return datiInizializzatiExecuteStep1;
	}

	public void setDatiInizializzatiExecuteStep1(boolean datiInizializzatiExecuteStep1) {
		this.datiInizializzatiExecuteStep1 = datiInizializzatiExecuteStep1;
	}

}
