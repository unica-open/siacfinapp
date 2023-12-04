/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.ElementoContoEconomico;

public class GestisciImpegnoStep2Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	private String missioneSelezionata, programmaSelezionato, cofogSelezionato, transazioneEuropeaSelezionato, 
	               ricorrenteSpesaSelezionato, perimetroSanitarioSpesaSelezionato, politicaRegionaleSelezionato, cup;
	
	private String classGenSelezionato1, classGenSelezionato2, classGenSelezionato3, classGenSelezionato4, classGenSelezionato5;
	
	
//	Programma
//	Piano conti Finanziario  --> pianoDeiConti
//	Conto Economico
//	COFOG
//	Codifica Transazione Europea
//	CUP
//	Ricorrente
//	SIOPE
//	ASL --> perimetroSanitarioSpesaSelezionato
//	Programma Pol. Reg. Unitaria --> politicaRegionaleSelezionato
	
	
    private ElementoPianoDeiConti pianoDeiConti = new ElementoPianoDeiConti();
    private ElementoContoEconomico contoEconomico = new ElementoContoEconomico();
    private SiopeSpesa siopeSpesa = new SiopeSpesa();
    
    private boolean ricaricaAlberoPianoDeiConti = true;

	public String getMissioneSelezionata() {
		return missioneSelezionata;
	}


	public void setMissioneSelezionata(String missioneSelezionata) {
		this.missioneSelezionata = missioneSelezionata;
	}


	public String getProgrammaSelezionato() {
		return programmaSelezionato;
	}


	public void setProgrammaSelezionato(String programmaSelezionato) {
		this.programmaSelezionato = programmaSelezionato;
	}


	public String getCofogSelezionato() {
		return cofogSelezionato;
	}


	public void setCofogSelezionato(String cofogSelezionato) {
		this.cofogSelezionato = cofogSelezionato;
	}


	public String getTransazioneEuropeaSelezionato() {
		return transazioneEuropeaSelezionato;
	}


	public void setTransazioneEuropeaSelezionato(
			String transazioneEuropeaSelezionato) {
		this.transazioneEuropeaSelezionato = transazioneEuropeaSelezionato;
	}


	public String getRicorrenteSpesaSelezionato() {
		return ricorrenteSpesaSelezionato;
	}


	public void setRicorrenteSpesaSelezionato(String ricorrenteSpesaSelezionato) {
		this.ricorrenteSpesaSelezionato = ricorrenteSpesaSelezionato;
	}


	public String getPerimetroSanitarioSpesaSelezionato() {
		return perimetroSanitarioSpesaSelezionato;
	}


	public void setPerimetroSanitarioSpesaSelezionato(
			String perimetroSanitarioSpesaSelezionato) {
		this.perimetroSanitarioSpesaSelezionato = perimetroSanitarioSpesaSelezionato;
	}


	public String getPoliticaRegionaleSelezionato() {
		return politicaRegionaleSelezionato;
	}


	public void setPoliticaRegionaleSelezionato(String politicaRegionaleSelezionato) {
		this.politicaRegionaleSelezionato = politicaRegionaleSelezionato;
	}


	public String getClassGenSelezionato1() {
		return classGenSelezionato1;
	}


	public void setClassGenSelezionato1(String classGenSelezionato1) {
		this.classGenSelezionato1 = classGenSelezionato1;
	}


	public String getClassGenSelezionato2() {
		return classGenSelezionato2;
	}


	public void setClassGenSelezionato2(String classGenSelezionato2) {
		this.classGenSelezionato2 = classGenSelezionato2;
	}


	public String getClassGenSelezionato3() {
		return classGenSelezionato3;
	}


	public void setClassGenSelezionato3(String classGenSelezionato3) {
		this.classGenSelezionato3 = classGenSelezionato3;
	}


	public String getClassGenSelezionato4() {
		return classGenSelezionato4;
	}


	public void setClassGenSelezionato4(String classGenSelezionato4) {
		this.classGenSelezionato4 = classGenSelezionato4;
	}


	public String getClassGenSelezionato5() {
		return classGenSelezionato5;
	}


	public void setClassGenSelezionato5(String classGenSelezionato5) {
		this.classGenSelezionato5 = classGenSelezionato5;
	}


	public String getCup() {
		return cup;
	}


	public void setCup(String cup) {
		this.cup = cup;
	}

	
	public ElementoPianoDeiConti getPianoDeiConti() {
		return pianoDeiConti;
	}


	public void setPianoDeiConti(ElementoPianoDeiConti pianoDeiConti) {
		this.pianoDeiConti = pianoDeiConti;
	}


	public ElementoContoEconomico getContoEconomico() {
		return contoEconomico;
	}


	public void setContoEconomico(ElementoContoEconomico contoEconomico) {
		this.contoEconomico = contoEconomico;
	}

	public SiopeSpesa getSiopeSpesa() {
		return siopeSpesa;
	}

	public void setSiopeSpesa(SiopeSpesa siopeSpesa) {
		this.siopeSpesa = siopeSpesa;
	}

	public boolean isRicaricaAlberoPianoDeiConti() {
		return ricaricaAlberoPianoDeiConti;
	}

	public void setRicaricaAlberoPianoDeiConti(boolean ricaricaAlberoPianoDeiConti) {
		this.ricaricaAlberoPianoDeiConti = ricaricaAlberoPianoDeiConti;
	}
	
}
