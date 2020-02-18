/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class ReintroitoOrdinativoStep2Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	//per pilotare il pop up che chiede conferma con warning sui soggetti:
	private boolean checkWarningSoggetti;
	
	//per pilotare il pop up che chiede conferma con warning sul piano dei conti:
	private boolean checkWarningPianoDeiConti;
	
	//per capire da quale riga parte una compilazione guidata:
	private String riferimentoRiga;
	
	//prima riga fissa per il netto:
	private RigaDiReintroitoOrdinativoModel netto = new RigaDiReintroitoOrdinativoModel();
	
	//seconda riga fissa per il riepilogo ritenute:
	private RigaDiReintroitoOrdinativoModel ritenute = new RigaDiReintroitoOrdinativoModel();;
	
	//n righe variabili per gli ordinativi collegati:
	private List<RigaDiReintroitoOrdinativoModel> listOrdInc;

	public RigaDiReintroitoOrdinativoModel getNetto() {
		return netto;
	}

	public void setNetto(RigaDiReintroitoOrdinativoModel netto) {
		this.netto = netto;
	}

	public RigaDiReintroitoOrdinativoModel getRitenute() {
		return ritenute;
	}

	public void setRitenute(RigaDiReintroitoOrdinativoModel ritenute) {
		this.ritenute = ritenute;
	}

	public List<RigaDiReintroitoOrdinativoModel> getListOrdInc() {
		return listOrdInc;
	}

	public void setListOrdInc(List<RigaDiReintroitoOrdinativoModel> listOrdInc) {
		this.listOrdInc = listOrdInc;
	}

	public String getRiferimentoRiga() {
		return riferimentoRiga;
	}

	public void setRiferimentoRiga(String riferimentoRiga) {
		this.riferimentoRiga = riferimentoRiga;
	}

	public boolean isCheckWarningSoggetti() {
		return checkWarningSoggetti;
	}

	public void setCheckWarningSoggetti(boolean checkWarningSoggetti) {
		this.checkWarningSoggetti = checkWarningSoggetti;
	}

	public boolean isCheckWarningPianoDeiConti() {
		return checkWarningPianoDeiConti;
	}

	public void setCheckWarningPianoDeiConti(boolean checkWarningPianoDeiConti) {
		this.checkWarningPianoDeiConti = checkWarningPianoDeiConti;
	}
	
	
}
