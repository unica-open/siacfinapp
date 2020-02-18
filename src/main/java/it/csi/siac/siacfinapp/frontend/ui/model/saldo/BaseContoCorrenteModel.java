/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.saldo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.saldo.AddebitoContoCorrente;
import it.csi.siac.siacfinser.model.saldo.VociContoCorrente;

public abstract class BaseContoCorrenteModel extends GenericFinModel {
	
	private static final long serialVersionUID = -5511677007702738234L;

	//voci conto corrente:
	private VociContoCorrente vociContoCorrente;

	//addebito conto corrente:
	private AddebitoContoCorrente addebitoContoCorrente;
	
	public VociContoCorrente getVociContoCorrente() {
		return vociContoCorrente;
	}

	public void setVociContoCorrente(VociContoCorrente vociContoCorrente) {
		this.vociContoCorrente = vociContoCorrente;
	}

	public AddebitoContoCorrente getAddebitoContoCorrente() {
		return addebitoContoCorrente;
	}

	public void setAddebitoContoCorrente(AddebitoContoCorrente addebitoContoCorrente) {
		this.addebitoContoCorrente = addebitoContoCorrente;
	}

	public void insAddebitoContoCorrente() {
		vociContoCorrente = new VociContoCorrente();
		//istanzio una nuova lista:
		List<AddebitoContoCorrente> elencoAddebiti = new ArrayList<AddebitoContoCorrente>();
		//aggiungo un nuovo elemento in lista:
		elencoAddebiti.add(addebitoContoCorrente);
		//aggiungo la lista a vociContoCorrente:
		vociContoCorrente.setElencoAddebiti(elencoAddebiti);		
	}

}