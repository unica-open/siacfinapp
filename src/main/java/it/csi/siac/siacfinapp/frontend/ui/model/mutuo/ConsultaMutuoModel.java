/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.mutuo;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;


public class ConsultaMutuoModel extends GenericFinModel {
	
	private static final long serialVersionUID = 1L;
	
	private Mutuo mutuo;
	private String activeTab = "mutuo";

	/* **************************************************************************** */
	/*   Getter / Setter															*/
	/* **************************************************************************** */
	
	public Mutuo getMutuo() {
		return mutuo;
	}

	public void setMutuo(Mutuo mutuo) {
		this.mutuo = mutuo;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}
	
}
