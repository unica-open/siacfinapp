/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ajax;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreDatiAlberoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;



/**
 * Classe di Model per le chiamate AJAX.
 * La presente classe serve ad immagazzinare i dati passati da <em>Struts2</em> via chiamata di tipo AJAX, e
 * ad immagazzinare i risultati dei WebServices che serviranno come risposta alla chiamata asincrona.
 * 
 * @author paolos
 *
 *
 */
public class AjaxModel extends GenericFinModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723772573344585168L;
	
	
	private String comuneDesc;

	private List<ComuneNascita> listaComuni;
	private List<CodificaExtFin> listaSedimi;
	
	// CR 2023 listaContiEconomici, si elimina //SIAC-7477 Aggiunta listaStrutturaAmministrativeCompetente
	private List<GestoreDatiAlberoModel> listaPianoDeiConti, listaStrutturaAmministrative,listaStrutturaAmministrativeCompetente, listaSiopeSpesa, listaPianoDeiContiCompleto;

	//SIAC-7349
	private List<ImportiCapitoloPerComponente> listaComponentiBilancio = new ArrayList<ImportiCapitoloPerComponente>();
	private Integer uidCapitolo;
	private List<ImportiCapitoloPerComponente> listaComponentiBilancioCompleta = new ArrayList<ImportiCapitoloPerComponente>();

	/**
	 * @return the listaComponentiBilancioCompleta
	 */
	public List<ImportiCapitoloPerComponente> getListaComponentiBilancioCompleta() {
		return listaComponentiBilancioCompleta;
	}

	/**
	 * @param listaComponentiBilancioCompleta the listaComponentiBilancioCompleta to set
	 */
	public void setListaComponentiBilancioCompleta(List<ImportiCapitoloPerComponente> listaComponentiBilancioCompleta) {
		this.listaComponentiBilancioCompleta = listaComponentiBilancioCompleta;
	}

	/**
	 * @return the listaComponentiBilancio
	 */
	public List<ImportiCapitoloPerComponente> getListaComponentiBilancio() {
		return listaComponentiBilancio;
	}

	/**
	 * @param listaComponentiBilancio the listaComponentiBilancio to set
	 */
	public void setListaComponentiBilancio(List<ImportiCapitoloPerComponente> listaComponentiBilancio) {
		this.listaComponentiBilancio = listaComponentiBilancio;
	}
	
	public String getComuneDesc() {
		return comuneDesc;
	}

	public void setComuneDesc(String comuneDesc) {
		this.comuneDesc = comuneDesc;
	}

	public List<ComuneNascita> getListaComuni() {
		return listaComuni;
	}

	public void setListaComuni(List<ComuneNascita> listaComuni) {
		this.listaComuni = listaComuni;
	}

	public List<CodificaExtFin> getListaSedimi() {
		return listaSedimi;
	}

	public void setListaSedimi(List<CodificaExtFin> listaSedimi) {
		this.listaSedimi = listaSedimi;
	}

	public List<GestoreDatiAlberoModel> getListaPianoDeiConti() {
		return listaPianoDeiConti;
	}

	public void setListaPianoDeiConti(List<GestoreDatiAlberoModel> listaPianoDeiConti) {
		this.listaPianoDeiConti = listaPianoDeiConti;
	}

	public List<GestoreDatiAlberoModel> getListaStrutturaAmministrative() {
		return listaStrutturaAmministrative;
	}

	
	/**
	 * @return the listaStrutturaAmministrativeCompetente
	 */
	public List<GestoreDatiAlberoModel> getListaStrutturaAmministrativeCompetente() {
		return listaStrutturaAmministrativeCompetente;
	}

	/**
	 * @param listaStrutturaAmministrativeCompetente the listaStrutturaAmministrativeCompetente to set
	 */
	public void setListaStrutturaAmministrativeCompetente(
			List<GestoreDatiAlberoModel> listaStrutturaAmministrativeCompetente) {
		this.listaStrutturaAmministrativeCompetente = listaStrutturaAmministrativeCompetente;
	}

	public void setListaStrutturaAmministrative(
			List<GestoreDatiAlberoModel> listaStrutturaAmministrative) {
		this.listaStrutturaAmministrative = listaStrutturaAmministrative;
	}

	public List<GestoreDatiAlberoModel> getListaSiopeSpesa() {
		return listaSiopeSpesa;
	}

	public void setListaSiopeSpesa(List<GestoreDatiAlberoModel> listaSiopeSpesa) {
		this.listaSiopeSpesa = listaSiopeSpesa;
	}

	public List<GestoreDatiAlberoModel> getListaPianoDeiContiCompleto() {
		return listaPianoDeiContiCompleto;
	}

	public void setListaPianoDeiContiCompleto(
			List<GestoreDatiAlberoModel> listaPianoDeiContiCompleto) {
		this.listaPianoDeiContiCompleto = listaPianoDeiContiCompleto;
	}

	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return uidCapitolo;
	}

	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}

	
	
}
