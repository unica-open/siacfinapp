/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * The Class GestioneOrdinativoModel.
 */
public class GestioneOrdinativoModel extends GenericPopupModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gestione ordinativo step 1 model. */
	//MODEL DELLO STEP 1:
	private GestioneOrdinativoStep1Model gestioneOrdinativoStep1Model = new GestioneOrdinativoStep1Model();
	
	/** The gestione ordinativo step 2 model. */
	//MODEL DELLO STEP 2:
	private GestioneOrdinativoStep2Model gestioneOrdinativoStep2Model = new GestioneOrdinativoStep2Model();
	
	/** The gestione ordinativo step 3 model. */
	//MODEL DELLO STEP 3:
	private GestioneOrdinativoStep3Model gestioneOrdinativoStep3Model = new GestioneOrdinativoStep3Model();

	/** The nuova liquidazione model. */
	//MODEL PER LA NUOVA LIQUIDAZIONE:
	private NuovaLiquidazioneModel nuovaLiquidazioneModel = new NuovaLiquidazioneModel();
	
	/** The nuovo accertamento model. */
	//MODEL PER IL NUOVO ACCERTAMENTO:
	private NuovoAccertamentoModel nuovoAccertamentoModel = new NuovoAccertamentoModel();

	/** The transazione elementare ordinativo cache. */
	//DATI TRANSAZIONE ELEMENTARE CENTRALIZZATI:
	private GestoreTransazioneElementareModel transazioneElementareOrdinativoCache = new GestoreTransazioneElementareModel();

	/** The liquidazione trovata. */
	//FLAG PER GESTIRE COMPORTAMNTI VARI:
	private boolean sonoInAggiornamento = false,
			forceReloadAgiornamentoOrdinativo = true,
			sonoInAggiornamentoIncasso = false,
			forceReloadAgiornamentoOrdinativoIncasso = true,
			accertamentoTrovato = false,
			liquidazioneTrovata = false;

	/** The ricarica dopo aggiornamento. */
	private boolean toggleLiquidazioneAperto = false,
			ricaricaDopoAggiornamento = false;
	
	/** The numero ordinativo in aggiornamento. */
	//ANNO E NUMERO ORDINATIVO IN AGGIORNAMENTO:
	private String annoOrdinativoInAggiornamento,
			numeroOrdinativoInAggiornamento;
	
	/** The prosegui con warning. */
	//GESTIONE DEL PROSEGUI CON WARNING:
	public boolean proseguiConWarning;
	
	/** The prosegui con warning reintroito pdc. */
	//GESTIONE DEL PROSEGUI CON WARNING per piano dei conti in caso di reintroito:
	public boolean proseguiConWarningReintroitoPdc;

	/** The ordinativo pagamento ricaricato dopo ins O agg. */
	// OGGETTI MESSI IN SESSIONE PER EVITARE RICHIAMO MEGA QUERY
	private OrdinativoPagamento ordinativoPagamentoRicaricatoDopoInsOAgg = null;
	
	/** The ordinativo incasso ricaricato dopo ins O agg. */
	private OrdinativoIncasso ordinativoIncassoRicaricatoDopoInsOAgg = null;
	
	/** ************************* GESTIONE DINAMICA ****************************. */
	private Map<String, String> labels = new HashMap<String, String>();
	
	/** *************************************************************************. */
	
	//lista delle tipologie di finanziamento:
	private List<TipoFinanziamento> listaTipoFinanziamento = new ArrayList<TipoFinanziamento>();

	/** The ricerca provvisorio model. */
	//model per la ricerca dei provvisori:
	private RicercaProvvisorioModel ricercaProvvisorioModel = new RicercaProvvisorioModel();
	
	/** The salta controllo soggetto. */
	//per il controllo sul spoggetto
	private boolean saltaControlloSoggetto;
	
	/** The chiedi conferma collegamento ordinativo. */
	//necessita' conferma soggetto
	private boolean chiediConfermaCollegamentoOrdinativo;
	
	//SIAC-6352
	private ContoTesoreria contoTesoreria;
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();

	//GETTER E SETTER:

	/**
	 * Gets the ordinativo pagamento ricaricato dopo ins O agg.
	 *
	 * @return the ordinativo pagamento ricaricato dopo ins O agg
	 */
	public OrdinativoPagamento getOrdinativoPagamentoRicaricatoDopoInsOAgg() {
		return ordinativoPagamentoRicaricatoDopoInsOAgg;
	}

	/**
	 * Sets the ordinativo pagamento ricaricato dopo ins O agg.
	 *
	 * @param ordinativoPagamentoRicaricatoDopoInsOAgg the new ordinativo pagamento ricaricato dopo ins O agg
	 */
	public void setOrdinativoPagamentoRicaricatoDopoInsOAgg(OrdinativoPagamento ordinativoPagamentoRicaricatoDopoInsOAgg) {
		this.ordinativoPagamentoRicaricatoDopoInsOAgg = ordinativoPagamentoRicaricatoDopoInsOAgg;
	}

	/**
	 * Gets the ordinativo incasso ricaricato dopo ins O agg.
	 *
	 * @return the ordinativo incasso ricaricato dopo ins O agg
	 */
	public OrdinativoIncasso getOrdinativoIncassoRicaricatoDopoInsOAgg() {
		return ordinativoIncassoRicaricatoDopoInsOAgg;
	}

	/**
	 * Sets the ordinativo incasso ricaricato dopo ins O agg.
	 *
	 * @param ordinativoIncassoRicaricatoDopoInsOAgg the new ordinativo incasso ricaricato dopo ins O agg
	 */
	public void setOrdinativoIncassoRicaricatoDopoInsOAgg(OrdinativoIncasso ordinativoIncassoRicaricatoDopoInsOAgg) {
		this.ordinativoIncassoRicaricatoDopoInsOAgg = ordinativoIncassoRicaricatoDopoInsOAgg;
	}

	/**
	 * Checks if is ricarica dopo aggiornamento.
	 *
	 * @return true, if is ricarica dopo aggiornamento
	 */
	public boolean isRicaricaDopoAggiornamento() {
		return ricaricaDopoAggiornamento;
	}

	/**
	 * Sets the ricarica dopo aggiornamento.
	 *
	 * @param ricaricaDopoAggiornamento the new ricarica dopo aggiornamento
	 */
	public void setRicaricaDopoAggiornamento(boolean ricaricaDopoAggiornamento) {
		this.ricaricaDopoAggiornamento = ricaricaDopoAggiornamento;
	}

	/**
	 * Checks if is abilitato.
	 *
	 * @return true, if is abilitato
	 */
	public boolean isAbilitato() {
		if (isSonoInAggiornamento()) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the gestione ordinativo step 1 model.
	 *
	 * @return the gestione ordinativo step 1 model
	 */
	public GestioneOrdinativoStep1Model getGestioneOrdinativoStep1Model() {
		return gestioneOrdinativoStep1Model;
	}

	/**
	 * Sets the gestione ordinativo step 1 model.
	 *
	 * @param gestioneOrdinativoStep1Model the new gestione ordinativo step 1 model
	 */
	public void setGestioneOrdinativoStep1Model(GestioneOrdinativoStep1Model gestioneOrdinativoStep1Model) {
		this.gestioneOrdinativoStep1Model = gestioneOrdinativoStep1Model;
	}

	/**
	 * Gets the labels.
	 *
	 * @return the labels
	 */
	public Map<String, String> getLabels() {
		return labels;
	}

	/**
	 * Sets the labels.
	 *
	 * @param labels the labels
	 */
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel#getListaTipoFinanziamento()
	 */
	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel#setListaTipoFinanziamento(java.util.List)
	 */
	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}

	/**
	 * Gets the gestione ordinativo step 2 model.
	 *
	 * @return the gestione ordinativo step 2 model
	 */
	public GestioneOrdinativoStep2Model getGestioneOrdinativoStep2Model() {
		return gestioneOrdinativoStep2Model;
	}

	/**
	 * Sets the gestione ordinativo step 2 model.
	 *
	 * @param gestioneOrdinativoStep2Model the new gestione ordinativo step 2 model
	 */
	public void setGestioneOrdinativoStep2Model(GestioneOrdinativoStep2Model gestioneOrdinativoStep2Model) {
		this.gestioneOrdinativoStep2Model = gestioneOrdinativoStep2Model;
	}

	/**
	 * Checks if is toggle liquidazione aperto.
	 *
	 * @return true, if is toggle liquidazione aperto
	 */
	public boolean isToggleLiquidazioneAperto() {
		return toggleLiquidazioneAperto;
	}

	/**
	 * Sets the toggle liquidazione aperto.
	 *
	 * @param toggleLiquidazioneAperto the new toggle liquidazione aperto
	 */
	public void setToggleLiquidazioneAperto(boolean toggleLiquidazioneAperto) {
		this.toggleLiquidazioneAperto = toggleLiquidazioneAperto;
	}

	/**
	 * Gets the nuova liquidazione model.
	 *
	 * @return the nuova liquidazione model
	 */
	public NuovaLiquidazioneModel getNuovaLiquidazioneModel() {
		return nuovaLiquidazioneModel;
	}

	/**
	 * Sets the nuova liquidazione model.
	 *
	 * @param nuovaLiquidazioneModel the new nuova liquidazione model
	 */
	public void setNuovaLiquidazioneModel(NuovaLiquidazioneModel nuovaLiquidazioneModel) {
		this.nuovaLiquidazioneModel = nuovaLiquidazioneModel;
	}

	/**
	 * Gets the gestione ordinativo step 3 model.
	 *
	 * @return the gestione ordinativo step 3 model
	 */
	public GestioneOrdinativoStep3Model getGestioneOrdinativoStep3Model() {
		return gestioneOrdinativoStep3Model;
	}

	/**
	 * Sets the gestione ordinativo step 3 model.
	 *
	 * @param gestioneOrdinativoStep3Model the new gestione ordinativo step 3 model
	 */
	public void setGestioneOrdinativoStep3Model(GestioneOrdinativoStep3Model gestioneOrdinativoStep3Model) {
		this.gestioneOrdinativoStep3Model = gestioneOrdinativoStep3Model;
	}

	/**
	 * Gets the ricerca provvisorio model.
	 *
	 * @return the ricerca provvisorio model
	 */
	public RicercaProvvisorioModel getRicercaProvvisorioModel() {
		return ricercaProvvisorioModel;
	}

	/**
	 * Sets the ricerca provvisorio model.
	 *
	 * @param ricercaProvvisorioModel the new ricerca provvisorio model
	 */
	public void setRicercaProvvisorioModel(RicercaProvvisorioModel ricercaProvvisorioModel) {
		this.ricercaProvvisorioModel = ricercaProvvisorioModel;
	}

	/**
	 * Checks if is sono in aggiornamento.
	 *
	 * @return true, if is sono in aggiornamento
	 */
	public boolean isSonoInAggiornamento() {
		return sonoInAggiornamento;
	}

	/**
	 * Sets the sono in aggiornamento.
	 *
	 * @param sonoInAggiornamento the new sono in aggiornamento
	 */
	public void setSonoInAggiornamento(boolean sonoInAggiornamento) {
		this.sonoInAggiornamento = sonoInAggiornamento;
	}

	/**
	 * Checks if is force reload agiornamento ordinativo.
	 *
	 * @return true, if is force reload agiornamento ordinativo
	 */
	public boolean isForceReloadAgiornamentoOrdinativo() {
		return forceReloadAgiornamentoOrdinativo;
	}

	/**
	 * Sets the force reload agiornamento ordinativo.
	 *
	 * @param forceReloadAgiornamentoOrdinativo the new force reload agiornamento ordinativo
	 */
	public void setForceReloadAgiornamentoOrdinativo(boolean forceReloadAgiornamentoOrdinativo) {
		this.forceReloadAgiornamentoOrdinativo = forceReloadAgiornamentoOrdinativo;
	}

	/**
	 * Gets the transazione elementare ordinativo cache.
	 *
	 * @return the transazione elementare ordinativo cache
	 */
	public GestoreTransazioneElementareModel getTransazioneElementareOrdinativoCache() {
		return transazioneElementareOrdinativoCache;
	}

	/**
	 * Sets the transazione elementare ordinativo cache.
	 *
	 * @param transazioneElementareOrdinativoCache the new transazione elementare ordinativo cache
	 */
	public void setTransazioneElementareOrdinativoCache(GestoreTransazioneElementareModel transazioneElementareOrdinativoCache) {
		this.transazioneElementareOrdinativoCache = transazioneElementareOrdinativoCache;
	}

	/**
	 * Gets the anno ordinativo in aggiornamento.
	 *
	 * @return the anno ordinativo in aggiornamento
	 */
	public String getAnnoOrdinativoInAggiornamento() {
		return annoOrdinativoInAggiornamento;
	}

	/**
	 * Sets the anno ordinativo in aggiornamento.
	 *
	 * @param annoOrdinativoInAggiornamento the new anno ordinativo in aggiornamento
	 */
	public void setAnnoOrdinativoInAggiornamento(String annoOrdinativoInAggiornamento) {
		this.annoOrdinativoInAggiornamento = annoOrdinativoInAggiornamento;
	}

	/**
	 * Gets the numero ordinativo in aggiornamento.
	 *
	 * @return the numero ordinativo in aggiornamento
	 */
	public String getNumeroOrdinativoInAggiornamento() {
		return numeroOrdinativoInAggiornamento;
	}

	/**
	 * Sets the numero ordinativo in aggiornamento.
	 *
	 * @param numeroOrdinativoInAggiornamento the new numero ordinativo in aggiornamento
	 */
	public void setNumeroOrdinativoInAggiornamento(
			String numeroOrdinativoInAggiornamento) {
		this.numeroOrdinativoInAggiornamento = numeroOrdinativoInAggiornamento;
	}

	/**
	 * Gets the nuovo accertamento model.
	 *
	 * @return the nuovo accertamento model
	 */
	public NuovoAccertamentoModel getNuovoAccertamentoModel() {
		return nuovoAccertamentoModel;
	}

	/**
	 * Sets the nuovo accertamento model.
	 *
	 * @param nuovoAccertamentoModel the new nuovo accertamento model
	 */
	public void setNuovoAccertamentoModel(
			NuovoAccertamentoModel nuovoAccertamentoModel) {
		this.nuovoAccertamentoModel = nuovoAccertamentoModel;
	}

	/**
	 * Checks if is sono in aggiornamento incasso.
	 *
	 * @return true, if is sono in aggiornamento incasso
	 */
	public boolean isSonoInAggiornamentoIncasso() {
		return sonoInAggiornamentoIncasso;
	}

	/**
	 * Sets the sono in aggiornamento incasso.
	 *
	 * @param sonoInAggiornamentoIncasso the new sono in aggiornamento incasso
	 */
	public void setSonoInAggiornamentoIncasso(boolean sonoInAggiornamentoIncasso) {
		this.sonoInAggiornamentoIncasso = sonoInAggiornamentoIncasso;
	}

	/**
	 * Checks if is force reload agiornamento ordinativo incasso.
	 *
	 * @return true, if is force reload agiornamento ordinativo incasso
	 */
	public boolean isForceReloadAgiornamentoOrdinativoIncasso() {
		return forceReloadAgiornamentoOrdinativoIncasso;
	}

	/**
	 * Sets the force reload agiornamento ordinativo incasso.
	 *
	 * @param forceReloadAgiornamentoOrdinativoIncasso the new force reload agiornamento ordinativo incasso
	 */
	public void setForceReloadAgiornamentoOrdinativoIncasso(
			boolean forceReloadAgiornamentoOrdinativoIncasso) {
		this.forceReloadAgiornamentoOrdinativoIncasso = forceReloadAgiornamentoOrdinativoIncasso;
	}

	/**
	 * Checks if is liquidazione trovata.
	 *
	 * @return the liquidazioneTrovata
	 */
	public boolean isLiquidazioneTrovata() {
		return liquidazioneTrovata;
	}

	/**
	 * Sets the liquidazione trovata.
	 *
	 * @param liquidazioneTrovata the liquidazioneTrovata to set
	 */
	public void setLiquidazioneTrovata(boolean liquidazioneTrovata) {
		this.liquidazioneTrovata = liquidazioneTrovata;
	}

	/**
	 * Checks if is accertamento trovato.
	 *
	 * @return the accertamentoTrovato
	 */
	public boolean isAccertamentoTrovato() {
		return accertamentoTrovato;
	}

	/**
	 * Sets the accertamento trovato.
	 *
	 * @param accertamentoTrovato the accertamentoTrovato to set
	 */
	public void setAccertamentoTrovato(boolean accertamentoTrovato) {
		this.accertamentoTrovato = accertamentoTrovato;
	}

	/**
	 * Checks if is prosegui con warning.
	 *
	 * @return the proseguiConWarning
	 */
	public boolean isProseguiConWarning() {
		return proseguiConWarning;
	}

	/**
	 * Sets the prosegui con warning.
	 *
	 * @param proseguiConWarning the proseguiConWarning to set
	 */
	public void setProseguiConWarning(boolean proseguiConWarning) {
		this.proseguiConWarning = proseguiConWarning;
	}

	/**
	 * Checks if is prosegui con warning reintroito pdc.
	 *
	 * @return true, if is prosegui con warning reintroito pdc
	 */
	public boolean isProseguiConWarningReintroitoPdc() {
		return proseguiConWarningReintroitoPdc;
	}

	/**
	 * Sets the prosegui con warning reintroito pdc.
	 *
	 * @param proseguiConWarningReintroitoPdc the new prosegui con warning reintroito pdc
	 */
	public void setProseguiConWarningReintroitoPdc(boolean proseguiConWarningReintroitoPdc) {
		this.proseguiConWarningReintroitoPdc = proseguiConWarningReintroitoPdc;
	}

	/**
	 * Checks if is salta controllo soggetto.
	 *
	 * @return the saltaControlloSoggetto
	 */
	public boolean isSaltaControlloSoggetto() {
		return saltaControlloSoggetto;
	}

	/**
	 * Sets the salta controllo soggetto.
	 *
	 * @param saltaControlloSoggetto the saltaControlloSoggetto to set
	 */
	public void setSaltaControlloSoggetto(boolean saltaControlloSoggetto) {
		this.saltaControlloSoggetto = saltaControlloSoggetto;
	}

	/**
	 * Checks if is chiedi conferma collegamento ordinativo.
	 *
	 * @return the chiediConfermaCollegamentoOrdinativo
	 */
	public boolean isChiediConfermaCollegamentoOrdinativo() {
		return chiediConfermaCollegamentoOrdinativo;
	}

	/**
	 * Sets the chiedi conferma collegamento ordinativo.
	 *
	 * @param chiediConfermaCollegamentoOrdinativo the chiediConfermaCollegamentoOrdinativo to set
	 */
	public void setChiediConfermaCollegamentoOrdinativo(boolean chiediConfermaCollegamentoOrdinativo) {
		this.chiediConfermaCollegamentoOrdinativo = chiediConfermaCollegamentoOrdinativo;
	}

	/**
 * Creazione della request per la lettura dei conti di tesoreria
 * @return la request creata
 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = new LeggiContiTesoreria();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}

	/**
	 * @return the contoTesoreria
	 */
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}

	/**
	 * @param contoTesoreria the contoTesoreria to set
	 */
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
	}

	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}

	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria;
	}

	

}
