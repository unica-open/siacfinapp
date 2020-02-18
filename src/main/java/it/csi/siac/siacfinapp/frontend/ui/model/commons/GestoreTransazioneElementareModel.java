/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class GestoreTransazioneElementareModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//MISSIONESELEZIONATA, PROGRAMMASELEZIONATO, COFOGSELEZIONATO, TRANSAZIONEEUROPEASELEZIONATO
	private String missioneSelezionata, programmaSelezionato, cofogSelezionato, transazioneEuropeaSelezionato;
	
	//RICORRENTESPESASELEZIONATO, PERIMETROSANITARIOSPESASELEZIONATO, POLITICAREGIONALESELEZIONATO, CUP
	private String ricorrenteSpesaSelezionato, perimetroSanitarioSpesaSelezionato, politicaRegionaleSelezionato, cup;
	
	//CLASSIFICATORI GENERICI 1 - 5
	private String classGenSelezionato1, classGenSelezionato2, classGenSelezionato3, classGenSelezionato4, classGenSelezionato5;
	
	//
	private int uidClassStipendiSelezionato;
	               
	//RICORRENTEENTRATASELEZIONATO, PERIMETROSANITARIOENTRATASELEZIONATO
	private String ricorrenteEntrataSelezionato,perimetroSanitarioEntrataSelezionato;
	
	//questi sono per accertamento
	private String classGenSelezionato6, classGenSelezionato7, classGenSelezionato8, classGenSelezionato9, classGenSelezionato10;
	
	//questi sono per ordinativo pagamento
	private String classGenSelezionato11, classGenSelezionato12, classGenSelezionato13, classGenSelezionato14, classGenSelezionato15;
	
	
	//IDMACROAGGREGATO, IDPIANODEICONTIPADREPERAGGIORNA
	private Integer idMacroAggregato, idPianoDeiContiPadrePerAggiorna = null;
	
	//ElementoPianoDeiConti
    private ElementoPianoDeiConti pianoDeiConti = new ElementoPianoDeiConti();
    
    // CR 2023 eliminato conto economico
    
    //GESTIONE SIOPE SPESA:
    private SiopeSpesa siopeSpesa = new SiopeSpesa();
    private String siopeSpesaCod;
    private boolean trovatoSiopeSpesa;
    //
    
    //per pilotare abilitazione
    private String oggettoAbilitaTE;
    
    ///VARIABILI D'APPOGGIO PER GESTIRE IL CARICAMENTO O MENO DEI VARI ALBERI/DATI IN AJAXACTION.JAVA:
    private boolean ricaricaAlberoPianoDeiConti = true;
	private boolean ricaricaStrutturaAmministrativa = true;
	private String struttAmmOriginale;
	private boolean ricaricaSiopeSpesa = true;
	///////////////////////////////////////////////////////////////
    
	//dati uscita impegno
	boolean datiUscitaImpegno = true;
    
    //LISTE DATI TRANSAZIONE
	private List<Missione> listaMissione = new ArrayList<Missione>();
    private List<Programma> listaProgramma = new ArrayList<Programma>();
    private List<TransazioneUnioneEuropeaSpesa> listaTransazioneEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
    private List<ClassificazioneCofog> listaCofog = new ArrayList<ClassificazioneCofog>();
    private List<RicorrenteSpesa> listaRicorrenteSpesa = new ArrayList<RicorrenteSpesa>();
    private List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa = new ArrayList<PerimetroSanitarioSpesa>();
    private List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie = new ArrayList<PoliticheRegionaliUnitarie>();
    
    //lista tipo finanziamento
    private List<TipoFinanziamento> listaTipoFinanziamento = new ArrayList<TipoFinanziamento>();
    
    //classificatori generici 11 - 15
    private List<ClassificatoreGenerico> listaClassificatoriGen11 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen12 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen13 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen14 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen15 = new ArrayList<ClassificatoreGenerico>();
    
    //classificatori generici 16 - 20
    private List<ClassificatoreGenerico> listaClassificatoriGen16 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen17 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen18 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen19 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen20 = new ArrayList<ClassificatoreGenerico>();
    
    //classificatori generici 21 - 25
    private List<ClassificatoreGenerico> listaClassificatoriGen21 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen22 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen23 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen24 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen25 = new ArrayList<ClassificatoreGenerico>();
    
    //classificatori generici 26 - 30
    private List<ClassificatoreGenerico> listaClassificatoriGen26 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen27 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen28 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen29 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen30 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreStipendi> listaClassificatoriStipendi = new ArrayList<ClassificatoreStipendi>();
    
    
    //lista transazione europea entrata
    private List<TransazioneUnioneEuropeaEntrata> listaTransazioneEuropeaEntrata = new ArrayList<TransazioneUnioneEuropeaEntrata>();
    
    //lista ricorrente entrata
    private List<RicorrenteEntrata> listaRicorrenteEntrata = new ArrayList<RicorrenteEntrata>();
    
    //lista perimetro sanitario entrata
    private List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata = new ArrayList<PerimetroSanitarioEntrata>();
  
    //FINE LISTE DATI TRANSAZIONE
    
    //GETTER E SETTER:

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

	public void setTransazioneEuropeaSelezionato(String transazioneEuropeaSelezionato) {
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

	public void setPerimetroSanitarioSpesaSelezionato(String perimetroSanitarioSpesaSelezionato) {
		this.perimetroSanitarioSpesaSelezionato = perimetroSanitarioSpesaSelezionato;
	}

	public String getPoliticaRegionaleSelezionato() {
		return politicaRegionaleSelezionato;
	}

	public void setPoliticaRegionaleSelezionato(String politicaRegionaleSelezionato) {
		this.politicaRegionaleSelezionato = politicaRegionaleSelezionato;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
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

	public ElementoPianoDeiConti getPianoDeiConti() {
		return pianoDeiConti;
	}

	public void setPianoDeiConti(ElementoPianoDeiConti pianoDeiConti) {
		this.pianoDeiConti = pianoDeiConti;
	}

	//CR 2023 eliminato conto economico

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

	public Integer getIdMacroAggregato() {
		return idMacroAggregato;
	}

	public void setIdMacroAggregato(Integer idMacroAggregato) {
		this.idMacroAggregato = idMacroAggregato;
	}

	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}
	
	public List<TransazioneUnioneEuropeaEntrata> getListaTransazioneEuropeaEntrata() {
		return listaTransazioneEuropeaEntrata;
	}

	public void setListaTransazioneEuropeaEntrata(List<TransazioneUnioneEuropeaEntrata> listaTransazioneEuropeaEntrata) {
		this.listaTransazioneEuropeaEntrata = listaTransazioneEuropeaEntrata;
	}

	public List<RicorrenteEntrata> getListaRicorrenteEntrata() {
		return listaRicorrenteEntrata;
	}

	public void setListaRicorrenteEntrata(List<RicorrenteEntrata> listaRicorrenteEntrata) {
		this.listaRicorrenteEntrata = listaRicorrenteEntrata;
	}

	public String getRicorrenteEntrataSelezionato() {
		return ricorrenteEntrataSelezionato;
	}

	public void setRicorrenteEntrataSelezionato(String ricorrenteEntrataSelezionato) {
		this.ricorrenteEntrataSelezionato = ricorrenteEntrataSelezionato;
	}

	public List<PerimetroSanitarioEntrata> getListaPerimetroSanitarioEntrata() {
		return listaPerimetroSanitarioEntrata;
	}

	public void setListaPerimetroSanitarioEntrata(List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata) {
		this.listaPerimetroSanitarioEntrata = listaPerimetroSanitarioEntrata;
	}

	public String getPerimetroSanitarioEntrataSelezionato() {
		return perimetroSanitarioEntrataSelezionato;
	}

	public void setPerimetroSanitarioEntrataSelezionato(String perimetroSanitarioEntrataSelezionato) {
		this.perimetroSanitarioEntrataSelezionato = perimetroSanitarioEntrataSelezionato;
	}

	public String getOggettoAbilitaTE() {
		return oggettoAbilitaTE;
	}

	public void setOggettoAbilitaTE(String oggettoAbilitaTE) {
		this.oggettoAbilitaTE = oggettoAbilitaTE;
	}

	public Integer getIdPianoDeiContiPadrePerAggiorna() {
		return idPianoDeiContiPadrePerAggiorna;
	}

	public void setIdPianoDeiContiPadrePerAggiorna(Integer idPianoDeiContiPadrePerAggiorna) {
		this.idPianoDeiContiPadrePerAggiorna = idPianoDeiContiPadrePerAggiorna;
	}
	
	public String getClassGenSelezionato6() {
		return classGenSelezionato6;
	}

	public void setClassGenSelezionato6(String classGenSelezionato6) {
		this.classGenSelezionato6 = classGenSelezionato6;
	}

	public String getClassGenSelezionato7() {
		return classGenSelezionato7;
	}

	public void setClassGenSelezionato7(String classGenSelezionato7) {
		this.classGenSelezionato7 = classGenSelezionato7;
	}

	public String getClassGenSelezionato8() {
		return classGenSelezionato8;
	}

	public void setClassGenSelezionato8(String classGenSelezionato8) {
		this.classGenSelezionato8 = classGenSelezionato8;
	}

	public String getClassGenSelezionato9() {
		return classGenSelezionato9;
	}

	public void setClassGenSelezionato9(String classGenSelezionato9) {
		this.classGenSelezionato9 = classGenSelezionato9;
	}

	public String getClassGenSelezionato10() {
		return classGenSelezionato10;
	}

	public void setClassGenSelezionato10(String classGenSelezionato10) {
		this.classGenSelezionato10 = classGenSelezionato10;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen16() {
		return listaClassificatoriGen16;
	}

	public void setListaClassificatoriGen16(List<ClassificatoreGenerico> listaClassificatoriGen16) {
		this.listaClassificatoriGen16 = listaClassificatoriGen16;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen17() {
		return listaClassificatoriGen17;
	}

	public void setListaClassificatoriGen17(List<ClassificatoreGenerico> listaClassificatoriGen17) {
		this.listaClassificatoriGen17 = listaClassificatoriGen17;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen18() {
		return listaClassificatoriGen18;
	}

	public void setListaClassificatoriGen18(List<ClassificatoreGenerico> listaClassificatoriGen18) {
		this.listaClassificatoriGen18 = listaClassificatoriGen18;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen19() {
		return listaClassificatoriGen19;
	}

	public void setListaClassificatoriGen19(List<ClassificatoreGenerico> listaClassificatoriGen19) {
		this.listaClassificatoriGen19 = listaClassificatoriGen19;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen20() {
		return listaClassificatoriGen20;
	}

	public void setListaClassificatoriGen20(List<ClassificatoreGenerico> listaClassificatoriGen20) {
		this.listaClassificatoriGen20 = listaClassificatoriGen20;
	}

	public String getClassGenSelezionato11() {
		return classGenSelezionato11;
	}

	public void setClassGenSelezionato11(String classGenSelezionato11) {
		this.classGenSelezionato11 = classGenSelezionato11;
	}

	public String getClassGenSelezionato12() {
		return classGenSelezionato12;
	}

	public void setClassGenSelezionato12(String classGenSelezionato12) {
		this.classGenSelezionato12 = classGenSelezionato12;
	}

	public String getClassGenSelezionato13() {
		return classGenSelezionato13;
	}

	public void setClassGenSelezionato13(String classGenSelezionato13) {
		this.classGenSelezionato13 = classGenSelezionato13;
	}

	public String getClassGenSelezionato14() {
		return classGenSelezionato14;
	}

	public void setClassGenSelezionato14(String classGenSelezionato14) {
		this.classGenSelezionato14 = classGenSelezionato14;
	}

	public String getClassGenSelezionato15() {
		return classGenSelezionato15;
	}

	public void setClassGenSelezionato15(String classGenSelezionato15) {
		this.classGenSelezionato15 = classGenSelezionato15;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen21() {
		return listaClassificatoriGen21;
	}

	public void setListaClassificatoriGen21(List<ClassificatoreGenerico> listaClassificatoriGen21) {
		this.listaClassificatoriGen21 = listaClassificatoriGen21;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen22() {
		return listaClassificatoriGen22;
	}

	public void setListaClassificatoriGen22(List<ClassificatoreGenerico> listaClassificatoriGen22) {
		this.listaClassificatoriGen22 = listaClassificatoriGen22;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen23() {
		return listaClassificatoriGen23;
	}

	public void setListaClassificatoriGen23(List<ClassificatoreGenerico> listaClassificatoriGen23) {
		this.listaClassificatoriGen23 = listaClassificatoriGen23;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen24() {
		return listaClassificatoriGen24;
	}

	public void setListaClassificatoriGen24(List<ClassificatoreGenerico> listaClassificatoriGen24) {
		this.listaClassificatoriGen24 = listaClassificatoriGen24;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen25() {
		return listaClassificatoriGen25;
	}

	public void setListaClassificatoriGen25(List<ClassificatoreGenerico> listaClassificatoriGen25) {
		this.listaClassificatoriGen25 = listaClassificatoriGen25;
	}

	public void setDatiUscitaImpegno(boolean datiUscitaImpegno) {
		this.datiUscitaImpegno = datiUscitaImpegno;
	}

	public boolean isDatiUscitaImpegno() {
		return datiUscitaImpegno;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen26() {
		return listaClassificatoriGen26;
	}

	public void setListaClassificatoriGen26(List<ClassificatoreGenerico> listaClassificatoriGen26) {
		this.listaClassificatoriGen26 = listaClassificatoriGen26;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen27() {
		return listaClassificatoriGen27;
	}

	public void setListaClassificatoriGen27(List<ClassificatoreGenerico> listaClassificatoriGen27) {
		this.listaClassificatoriGen27 = listaClassificatoriGen27;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen28() {
		return listaClassificatoriGen28;
	}

	public void setListaClassificatoriGen28(List<ClassificatoreGenerico> listaClassificatoriGen28) {
		this.listaClassificatoriGen28 = listaClassificatoriGen28;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen29() {
		return listaClassificatoriGen29;
	}

	public void setListaClassificatoriGen29(List<ClassificatoreGenerico> listaClassificatoriGen29) {
		this.listaClassificatoriGen29 = listaClassificatoriGen29;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen30() {
		return listaClassificatoriGen30;
	}

	public void setListaClassificatoriGen30(List<ClassificatoreGenerico> listaClassificatoriGen30) {
		this.listaClassificatoriGen30 = listaClassificatoriGen30;
	}
	
	/**
	 * @return the listaClassificatoriStipendi
	 */
	public List<ClassificatoreStipendi> getListaClassificatoriStipendi() {
		return listaClassificatoriStipendi;
	}

	/**
	 * @param listaClassificatoriStipendi the listaClassificatoriStipendi to set
	 */
	public void setListaClassificatoriStipendi(List<ClassificatoreStipendi> listaClassificatoriStipendi) {
		this.listaClassificatoriStipendi = listaClassificatoriStipendi;
	}

	public boolean isRicaricaStrutturaAmministrativa() {
		return ricaricaStrutturaAmministrativa;
	}

	public void setRicaricaStrutturaAmministrativa(boolean ricaricaStrutturaAmministrativa) {
		this.ricaricaStrutturaAmministrativa = ricaricaStrutturaAmministrativa;
	}

	public boolean isRicaricaSiopeSpesa() {
		return ricaricaSiopeSpesa;
	}

	public void setRicaricaSiopeSpesa(boolean ricaricaSiopeSpesa) {
		this.ricaricaSiopeSpesa = ricaricaSiopeSpesa;
	}

	public String getSiopeSpesaCod() {
		return siopeSpesaCod;
	}

	public void setSiopeSpesaCod(String siopeSpesaCod) {
		this.siopeSpesaCod = siopeSpesaCod;
	}

	public boolean isTrovatoSiopeSpesa() {
		return trovatoSiopeSpesa;
	}

	public void setTrovatoSiopeSpesa(boolean trovatoSiopeSpesa) {
		this.trovatoSiopeSpesa = trovatoSiopeSpesa;
	}

	public String getStruttAmmOriginale() {
		return struttAmmOriginale;
	}

	public void setStruttAmmOriginale(String struttAmmOriginale) {
		this.struttAmmOriginale = struttAmmOriginale;
	}

	/**
	 * @return the classStipendiSelezionato
	 */
	public int getUidClassStipendiSelezionato() {
		return uidClassStipendiSelezionato;
	}

	/**
	 * @param classStipendiSelezionato the classStipendiSelezionato to set
	 */
	public void setUidClassStipendiSelezionato(int uidClassStipendiSelezionato) {
		this.uidClassStipendiSelezionato = uidClassStipendiSelezionato;
	}
	
	
	
}
