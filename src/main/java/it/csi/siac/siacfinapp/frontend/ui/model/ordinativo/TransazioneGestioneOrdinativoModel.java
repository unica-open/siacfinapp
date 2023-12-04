/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep2Model;

public class TransazioneGestioneOrdinativoModel extends GestisciImpegnoStep2Model {

	private static final long serialVersionUID = 1L;

	// LISTE DATI TRANSAZIONE:
	
	//MISSIONE E PROGRAMMA:
	private List<Missione> listaMissione = new ArrayList<Missione>();
    private List<Programma> listaProgramma = new ArrayList<Programma>();
    
    //COFOG:
    private List<ClassificazioneCofog> listaCofog = new ArrayList<ClassificazioneCofog>();
    
    //TRANSAZIONEU NIONE EUROPEA:
    private List<TransazioneUnioneEuropeaSpesa> listaTransazioneEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
    private List<TransazioneUnioneEuropeaSpesa> listaTransEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
    
    //RICORRENTE SPESA:
    private List<RicorrenteSpesa> listaRicorrenteSpesa = new ArrayList<RicorrenteSpesa>();
    
    //PERIMETRO SANITARIO SPESA:
    private List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa = new ArrayList<PerimetroSanitarioSpesa>();
    
    //POLITICHE REGIONALI UNITARIE:
    private List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie = new ArrayList<PoliticheRegionaliUnitarie>();
    
    //CLASSIFICATORI GENERICI:
    private List<ClassificatoreGenerico> listaClassificatoriGen11 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen12 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen13 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen14 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen15 = new ArrayList<ClassificatoreGenerico>();
    
    
    
    //GETTER E SETTER:
    
	public List<Missione> getListaMissione() {
		return listaMissione;
	}
	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione;
	}
	public List<Programma> getListaProgramma() {
		return listaProgramma;
	}
	public void setListaProgramma(List<Programma> listaProgramma) {
		this.listaProgramma = listaProgramma;
	}
	public List<TransazioneUnioneEuropeaSpesa> getListaTransazioneEuropeaSpesa() {
		return listaTransazioneEuropeaSpesa;
	}
	public void setListaTransazioneEuropeaSpesa(List<TransazioneUnioneEuropeaSpesa> listaTransazioneEuropeaSpesa) {
		this.listaTransazioneEuropeaSpesa = listaTransazioneEuropeaSpesa;
	}
	public List<ClassificazioneCofog> getListaCofog() {
		return listaCofog;
	}
	public void setListaCofog(List<ClassificazioneCofog> listaCofog) {
		this.listaCofog = listaCofog;
	}
	public List<TransazioneUnioneEuropeaSpesa> getListaTransEuropeaSpesa() {
		return listaTransEuropeaSpesa;
	}
	public void setListaTransEuropeaSpesa(List<TransazioneUnioneEuropeaSpesa> listaTransEuropeaSpesa) {
		this.listaTransEuropeaSpesa = listaTransEuropeaSpesa;
	}
	public List<RicorrenteSpesa> getListaRicorrenteSpesa() {
		return listaRicorrenteSpesa;
	}
	public void setListaRicorrenteSpesa(List<RicorrenteSpesa> listaRicorrenteSpesa) {
		this.listaRicorrenteSpesa = listaRicorrenteSpesa;
	}
	public List<PerimetroSanitarioSpesa> getListaPerimetroSanitarioSpesa() {
		return listaPerimetroSanitarioSpesa;
	}
	public void setListaPerimetroSanitarioSpesa(List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa) {
		this.listaPerimetroSanitarioSpesa = listaPerimetroSanitarioSpesa;
	}
	public List<PoliticheRegionaliUnitarie> getListaPoliticheRegionaliUnitarie() {
		return listaPoliticheRegionaliUnitarie;
	}
	public void setListaPoliticheRegionaliUnitarie(List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie) {
		this.listaPoliticheRegionaliUnitarie = listaPoliticheRegionaliUnitarie;
	}
	public List<ClassificatoreGenerico> getListaClassificatoriGen11() {
		return listaClassificatoriGen11;
	}
	public void setListaClassificatoriGen11(List<ClassificatoreGenerico> listaClassificatoriGen11) {
		this.listaClassificatoriGen11 = listaClassificatoriGen11;
	}
	public List<ClassificatoreGenerico> getListaClassificatoriGen12() {
		return listaClassificatoriGen12;
	}
	public void setListaClassificatoriGen12(List<ClassificatoreGenerico> listaClassificatoriGen12) {
		this.listaClassificatoriGen12 = listaClassificatoriGen12;
	}
	public List<ClassificatoreGenerico> getListaClassificatoriGen13() {
		return listaClassificatoriGen13;
	}
	public void setListaClassificatoriGen13(List<ClassificatoreGenerico> listaClassificatoriGen13) {
		this.listaClassificatoriGen13 = listaClassificatoriGen13;
	}
	public List<ClassificatoreGenerico> getListaClassificatoriGen14() {
		return listaClassificatoriGen14;
	}
	public void setListaClassificatoriGen14(List<ClassificatoreGenerico> listaClassificatoriGen14) {
		this.listaClassificatoriGen14 = listaClassificatoriGen14;
	}
	public List<ClassificatoreGenerico> getListaClassificatoriGen15() {
		return listaClassificatoriGen15;
	}
	public void setListaClassificatoriGen15(List<ClassificatoreGenerico> listaClassificatoriGen15) {
		this.listaClassificatoriGen15 = listaClassificatoriGen15;
	}
    
}
