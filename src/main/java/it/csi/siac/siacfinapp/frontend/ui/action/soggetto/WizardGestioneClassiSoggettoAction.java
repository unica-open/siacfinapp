/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.GestioneClassiSoggettoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


public class WizardGestioneClassiSoggettoAction extends GenericPopupAction<GestioneClassiSoggettoModel> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected final static String GOTO_GESTIONE_CLASSI_SOGGETTO = "gotoGestioneClassiSoggetto"; 
	
	protected boolean daSelezioneClasse= false;
	
	protected boolean pulisciSoggettoSelezionatoDaAggiungere = false;
	
	protected String methodName;
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String getActionKey() {
		return "gestioneClassiSoggetto";
	}

	@BreadCrumb("%{model.titolo}")
	public String doExecute() throws Exception {
		return execute();
	}
	
	public void pulisciRicercaSoggettoDaAggiungere() {
		//puliamo i campi nel model:
		model.setListaRicercaSoggetto(new ArrayList<Soggetto>());
		model.setSoggettoTrovato(false);
		model.setSoggettoSelezionatoDaAggiungere(new SoggettoImpegnoModel());
	}
	
	@SuppressWarnings("unchecked")
	protected void caricaListeRicercaSoggetto(){
		//carico le liste
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	}

	protected String getCodiceAmbito() {
		return null;
	}

	public boolean isDaSelezioneClasse() {
		return daSelezioneClasse;
	}

	public void setDaSelezioneClasse(boolean daSelezioneClasse) {
		this.daSelezioneClasse = daSelezioneClasse;
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		//setting del soggetto selezionato
		model.setSoggettoSelezionatoDaAggiungere(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);
	}
	
	@Override
	public String listaClasseSoggettoChanged() {
		//listaClasseSoggettoChanged
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return null;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		//setCapitoloSelezionato
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
	}

	@Override
	protected void setErroreCapitolo() {
		//setErroreCapitolo
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		//setProvvedimentoSelezionato
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
	}

	@Override
	public boolean missioneProgrammaAttivi() {
		//missioneProgrammaAttivi
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		//cofogAttivo
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public boolean cupAttivo() {
		//cupAttivo
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		//programmaPoliticoRegionaleUnitarioAttivo
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		//transazioneElementareAttiva
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		//altriClassificatoriAttivi
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	@Override
	public String confermaPdc() {
		//confermaPdc
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return null;
	}

	@Override
	public String confermaSiope() {
		//confermaSiope
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		//datiUscitaImpegno
		//non faccio nulla di particolare, serve per fare l'ovveride obbligatorio
		return false;
	}

	public boolean isPulisciSoggettoSelezionatoDaAggiungere() {
		return pulisciSoggettoSelezionatoDaAggiungere;
	}

	public void setPulisciSoggettoSelezionatoDaAggiungere(boolean pulisciSoggettoSelezionatoDaAggiungere) {
		this.pulisciSoggettoSelezionatoDaAggiungere = pulisciSoggettoSelezionatoDaAggiungere;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}
	
}
