/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.AggiornaSoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

public class AggiornaSoggettoGenericAction extends SoggettoAction<AggiornaSoggettoModel>{
	private static final long serialVersionUID = 1L;

	// parte per inserimento indirizzi
	protected IndirizzoSoggetto indirizzoSoggetto = new IndirizzoSoggetto();
	
	//gestione id provvisorio per inserimento
	protected final static String PROVVISORIO = "PROVVISORIO_";
	
	//id univoco:
	protected Integer idProvvisorio = 0;
	
	//data di nascita:
	protected String dataNascitaStringa = "";

	@Override
	public String getActionKey() {
		//action key
		return "aggiornaSoggetto";
	}

	@BreadCrumb("%{model.titolo}")
	public String doExecute() throws Exception {
		//setto forece reload a false:
		forceReload = false;
		//rimando alla execute della super classe:
		return execute();
	}
	
	@Override
	public String getActionDataKeys() {
		//action data key
		return "model,idProvvisorio,indirizzoSoggetto,dataNascitaStringa";
	}
	
	//GETTER E SETTER:

	public IndirizzoSoggetto getIndirizzoSoggetto() {
		return indirizzoSoggetto;
	}

	public void setIndirizzoSoggetto(IndirizzoSoggetto indirizzoSoggetto) {
		this.indirizzoSoggetto = indirizzoSoggetto;
	}

	public Integer getIdProvvisorio() {
		return idProvvisorio;
	}

	public void setIdProvvisorio(Integer idProvvisorio) {
		this.idProvvisorio = idProvvisorio;
	}
	
	public String redirectSedi() {
		return "sedi";
	}
	
	public String redirectMdp() {
		return "mdp";
	}
	public String getDataNascitaStringa() {
		return dataNascitaStringa;
	}

	public void setDataNascitaStringa(String dataNascitaStringa) {
		this.dataNascitaStringa = dataNascitaStringa;
	}   

}
