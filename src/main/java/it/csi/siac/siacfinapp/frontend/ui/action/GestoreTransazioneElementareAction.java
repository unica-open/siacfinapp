/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
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
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.InserisciLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public abstract class GestoreTransazioneElementareAction<M extends GestoreTransazioneElementareModel> extends GenericFinAction<M> {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "GestoreTransazioneElementareAction";
	
	protected static final String IV_LIVELLO = "000";
	
	protected static final String V_LIVELLO_TIPO_CLASSIFICATORE = "PDC_V";
	
	protected OggettoDaPopolareEnum oggettoDaPopolare = null;
	
	
	
	public boolean isLiquidazioneConAzioneImpegnoDecentrato(){
		boolean liquidazioneConAzioneImpegnoDecentrato = false;
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.LIQUIDAZIONE) && 
				model!=null && model instanceof InserisciLiquidazioneModel){
			liquidazioneConAzioneImpegnoDecentrato = abilitatoAzioneGestisciImpegnoDecentratP();
		}
		return liquidazioneConAzioneImpegnoDecentrato;
	}
	
	/**
	 * 
	 * Viene usato dalle jsp per disabilitare i campi che non possono essere aggiornati in caso di liquidazione collegata ad ordinativi di pagamento.
	 * 
	 * In caso di inserimento di una nuova liquidazione non ci sono ovvviamente ordinativi e restituisce false.
	 * 
	 * In caso di transazione elmentare di oggetti diversi da liquidazione restituisce ovviamente false.
	 * 
	 * @return
	 */
	public boolean isPresenzaOrdinativiPerLaLiquidazione(){
		boolean presenzaOrdinativi = false;
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.LIQUIDAZIONE) && 
				model!=null && model instanceof InserisciLiquidazioneModel){
			
			InserisciLiquidazioneModel modelLiq = (InserisciLiquidazioneModel) this.model;
			
			if (modelLiq!=null && modelLiq.getListaOrdinativi()!=null && modelLiq.getListaOrdinativi().size()>0) {
				for (OrdinativoPagamento ordinativoPagamento : modelLiq.getListaOrdinativi()) {
					if (ordinativoPagamento!=null && !ordinativoPagamento.getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.ANNULLATO)) {
						presenzaOrdinativi = true;
						break;
					}
				}
			}
		}
		return presenzaOrdinativi;
	}
	
	/**
	 * Variabile utilizzata per fare il binding con la jsp.
	 * Utilizzata per le situazioni in cui un oggetto possa contenere un subOggetto con una Transazione Elementare
	 */
	public GestoreTransazioneElementareModel teSupport = new GestoreTransazioneElementareModel();
	
	public GestoreTransazioneElementareAction() {
		if (teSupport == null) {
			teSupport = new GestoreTransazioneElementareModel();
		}
	}
	
     public boolean abilitatoGestioneTE(){
    	 // da inserire nella lista delle azioni altrimenti non verra'
    	 // visualizzata mai il blocco della transazione
    	 if(AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_COM_gestisciCFG, sessionHandler.getAzioniConsentite())){
    		 return true; 
    	 }
    	 return false;
     }
     
     /**
      * ritorna TRUE se oggetto di IMPEGNO O LIQUIDAZIONE
      * 
      * @return
      */
     public boolean classificatoriImpegnoLiquidazione(){
    	 
    	 return oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.LIQUIDAZIONE);
     }
	
     /**
      * ritorna TRUE se oggetto di ORDINATIVO PAGAMENTO
      * 
      * @return
      */
     public boolean classificatoriOrdinativoPagamento(){
         return oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO);
     }
     
	/**
	 * Metodo che clona un oggetto di tipo Transazione Elementare
	 * Utilizzato per la gestione delle classi di cache
	 * @return GestoreTransazioneElementareModel
	 */
	public GestoreTransazioneElementareModel clonaTransazioneElementare() {
		GestoreTransazioneElementareModel elementoClonato = new GestoreTransazioneElementareModel();
		elementoClonato.setClassGenSelezionato1(teSupport.getClassGenSelezionato1());
		elementoClonato.setClassGenSelezionato2(teSupport.getClassGenSelezionato2());
		elementoClonato.setClassGenSelezionato3(teSupport.getClassGenSelezionato3());
		elementoClonato.setClassGenSelezionato4(teSupport.getClassGenSelezionato4());
		elementoClonato.setClassGenSelezionato5(teSupport.getClassGenSelezionato5());
		elementoClonato.setCofogSelezionato(teSupport.getCofogSelezionato());
		
		// CR 2023 eliminato conto economico
		
		elementoClonato.setCup(teSupport.getCup());
		elementoClonato.setIdMacroAggregato(teSupport.getIdMacroAggregato());
		// impegno
		elementoClonato.setListaClassificatoriGen11(clone(teSupport.getListaClassificatoriGen11()));
		elementoClonato.setListaClassificatoriGen12(clone(teSupport.getListaClassificatoriGen12()));
		elementoClonato.setListaClassificatoriGen13(clone(teSupport.getListaClassificatoriGen13()));
		elementoClonato.setListaClassificatoriGen14(clone(teSupport.getListaClassificatoriGen14()));
		elementoClonato.setListaClassificatoriGen15(clone(teSupport.getListaClassificatoriGen15()));
		// accertamento
		elementoClonato.setListaClassificatoriGen16(clone(teSupport.getListaClassificatoriGen16()));
		elementoClonato.setListaClassificatoriGen17(clone(teSupport.getListaClassificatoriGen17()));
		elementoClonato.setListaClassificatoriGen18(clone(teSupport.getListaClassificatoriGen18()));
		elementoClonato.setListaClassificatoriGen19(clone(teSupport.getListaClassificatoriGen19()));
		elementoClonato.setListaClassificatoriGen20(clone(teSupport.getListaClassificatoriGen20()));
		// ordinativo pagamento
		elementoClonato.setListaClassificatoriGen21(clone(teSupport.getListaClassificatoriGen21()));
		elementoClonato.setListaClassificatoriGen22(clone(teSupport.getListaClassificatoriGen22()));
		elementoClonato.setListaClassificatoriGen23(clone(teSupport.getListaClassificatoriGen23()));
		elementoClonato.setListaClassificatoriGen24(clone(teSupport.getListaClassificatoriGen24()));
		elementoClonato.setListaClassificatoriGen25(clone(teSupport.getListaClassificatoriGen25()));
		
		elementoClonato.setListaCofog(clone(teSupport.getListaCofog()));
		elementoClonato.setListaMissione(clone(teSupport.getListaMissione()));
		elementoClonato.setListaPerimetroSanitarioSpesa(clone(teSupport.getListaPerimetroSanitarioSpesa()));
		elementoClonato.setListaPoliticheRegionaliUnitarie(clone(teSupport.getListaPoliticheRegionaliUnitarie()));
		elementoClonato.setListaProgramma(clone(teSupport.getListaProgramma()));
		elementoClonato.setListaRicorrenteSpesa(clone(teSupport.getListaRicorrenteSpesa()));
		elementoClonato.setListaTransazioneEuropeaSpesa(clone(teSupport.getListaTransazioneEuropeaSpesa()));
		elementoClonato.setMissioneSelezionata(teSupport.getMissioneSelezionata());
		
		elementoClonato.setPianoDeiConti(clone(teSupport.getPianoDeiConti()));
		elementoClonato.setPoliticaRegionaleSelezionato(teSupport.getPoliticaRegionaleSelezionato());
		elementoClonato.setProgrammaSelezionato(teSupport.getProgrammaSelezionato());
		elementoClonato.setRicaricaAlberoPianoDeiConti(teSupport.isRicaricaAlberoPianoDeiConti());
		elementoClonato.setRicorrenteSpesaSelezionato(teSupport.getRicorrenteSpesaSelezionato());
		elementoClonato.setSiopeSpesa(clone(teSupport.getSiopeSpesa()));
		elementoClonato.setTransazioneEuropeaSelezionato(teSupport.getTransazioneEuropeaSelezionato());
		
		
		// entrata
		elementoClonato.setListaTransazioneEuropeaEntrata(clone(teSupport.getListaTransazioneEuropeaEntrata()));
		elementoClonato.setListaRicorrenteEntrata(clone(teSupport.getListaRicorrenteEntrata()));
		elementoClonato.setRicorrenteEntrataSelezionato(teSupport.getRicorrenteEntrataSelezionato());
		elementoClonato.setListaPerimetroSanitarioEntrata(clone(teSupport.getListaPerimetroSanitarioEntrata()));
		elementoClonato.setPerimetroSanitarioEntrataSelezionato(teSupport.getPerimetroSanitarioEntrataSelezionato());
		elementoClonato.setDatiUscitaImpegno(teSupport.isDatiUscitaImpegno());
		
		return elementoClonato;
	}
	
	/**
	 * Metodo che copia l'oggetto di appoggio sul model di riferimento della Transazione Elementare
	 * Svuota il model support.
	 */
	public void copiaTransazioneElementareSupportSuModel(boolean pulisciTransazione) {
		// impegno
		model.setClassGenSelezionato1(teSupport.getClassGenSelezionato1());
		model.setClassGenSelezionato2(teSupport.getClassGenSelezionato2());
		model.setClassGenSelezionato3(teSupport.getClassGenSelezionato3());
		model.setClassGenSelezionato4(teSupport.getClassGenSelezionato4());
		model.setClassGenSelezionato5(teSupport.getClassGenSelezionato5());
		
		//accertamento
		model.setClassGenSelezionato6(teSupport.getClassGenSelezionato6());
		model.setClassGenSelezionato7(teSupport.getClassGenSelezionato7());
		model.setClassGenSelezionato8(teSupport.getClassGenSelezionato8());
		model.setClassGenSelezionato9(teSupport.getClassGenSelezionato9());
		model.setClassGenSelezionato10(teSupport.getClassGenSelezionato10());
		
		// ordinativo pagamento
		model.setClassGenSelezionato11(teSupport.getClassGenSelezionato11());
		model.setClassGenSelezionato12(teSupport.getClassGenSelezionato12());
		model.setClassGenSelezionato13(teSupport.getClassGenSelezionato13());
		model.setClassGenSelezionato14(teSupport.getClassGenSelezionato14());
		model.setClassGenSelezionato15(teSupport.getClassGenSelezionato15());
		
		model.setCofogSelezionato(teSupport.getCofogSelezionato());
		
		// CR 2023 eliminato conto economico
		
		model.setCup(teSupport.getCup());
		model.setIdMacroAggregato(teSupport.getIdMacroAggregato());
		
		model.setListaClassificatoriGen11(clone(teSupport.getListaClassificatoriGen11()));
		model.setListaClassificatoriGen12(clone(teSupport.getListaClassificatoriGen12()));
		model.setListaClassificatoriGen13(clone(teSupport.getListaClassificatoriGen13()));
		model.setListaClassificatoriGen14(clone(teSupport.getListaClassificatoriGen14()));
		model.setListaClassificatoriGen15(clone(teSupport.getListaClassificatoriGen15()));
		
		model.setListaClassificatoriGen16(clone(teSupport.getListaClassificatoriGen16()));
		model.setListaClassificatoriGen17(clone(teSupport.getListaClassificatoriGen17()));
		model.setListaClassificatoriGen18(clone(teSupport.getListaClassificatoriGen18()));
		model.setListaClassificatoriGen19(clone(teSupport.getListaClassificatoriGen19()));
		model.setListaClassificatoriGen20(clone(teSupport.getListaClassificatoriGen20()));
		
		
		model.setListaClassificatoriGen21(clone(teSupport.getListaClassificatoriGen21()));
		model.setListaClassificatoriGen22(clone(teSupport.getListaClassificatoriGen22()));
		model.setListaClassificatoriGen23(clone(teSupport.getListaClassificatoriGen23()));
		model.setListaClassificatoriGen24(clone(teSupport.getListaClassificatoriGen24()));
		model.setListaClassificatoriGen25(clone(teSupport.getListaClassificatoriGen25()));
		
		model.setListaCofog(clone(teSupport.getListaCofog()));
		model.setListaMissione(clone(teSupport.getListaMissione()));
		model.setListaPerimetroSanitarioSpesa(clone(teSupport.getListaPerimetroSanitarioSpesa()));
		model.setListaPoliticheRegionaliUnitarie(clone(teSupport.getListaPoliticheRegionaliUnitarie()));
		model.setListaProgramma(clone(teSupport.getListaProgramma()));
		model.setListaRicorrenteSpesa(clone(teSupport.getListaRicorrenteSpesa()));
		model.setListaTransazioneEuropeaSpesa(clone(teSupport.getListaTransazioneEuropeaSpesa()));
		model.setMissioneSelezionata(teSupport.getMissioneSelezionata());
		model.setPerimetroSanitarioSpesaSelezionato(teSupport.getPerimetroSanitarioSpesaSelezionato());
		model.setPianoDeiConti(clone(teSupport.getPianoDeiConti()));
		model.setPoliticaRegionaleSelezionato(teSupport.getPoliticaRegionaleSelezionato());
		model.setProgrammaSelezionato(teSupport.getProgrammaSelezionato());
		model.setRicaricaAlberoPianoDeiConti(teSupport.isRicaricaAlberoPianoDeiConti());
		model.setRicorrenteSpesaSelezionato(teSupport.getRicorrenteSpesaSelezionato());
		
		//SIOPE:
		//Il flag isTrovatoSiopeSpesa e' pilotato dalla ricerca del codice siope digitato
		if(teSupport.isTrovatoSiopeSpesa()){
			model.setSiopeSpesa(clone(teSupport.getSiopeSpesa()));
			model.getSiopeSpesa().setCodice(teSupport.getSiopeSpesaCod());
		} else {
			model.setSiopeSpesa(new SiopeSpesa());
		}
		//
		
		model.setTransazioneEuropeaSelezionato(teSupport.getTransazioneEuropeaSelezionato());
		
		// entrata
		model.setListaTransazioneEuropeaEntrata(clone(teSupport.getListaTransazioneEuropeaEntrata()));
		model.setListaRicorrenteEntrata(clone(clone(teSupport.getListaRicorrenteEntrata())));
		model.setRicorrenteEntrataSelezionato(teSupport.getRicorrenteEntrataSelezionato());
		model.setListaPerimetroSanitarioEntrata(clone(teSupport.getListaPerimetroSanitarioEntrata()));
		model.setPerimetroSanitarioEntrataSelezionato(teSupport.getPerimetroSanitarioEntrataSelezionato());
		
		
		model.setDatiUscitaImpegno(teSupport.isDatiUscitaImpegno());
		
		if(pulisciTransazione){
			pulisciTransazioneElementare();
		}
	}
	
	/**
	 * Metodo utilizzato per pulire il model di supporto della Transazione Elementare
	 */
	public void pulisciTransazioneElementare() {
		teSupport = new GestoreTransazioneElementareModel();
	}
	
	/**
	 * Metodo che permette il caricamento delle liste relative alla Transazione Elementare
	 * @param tipoElemBil (USCITA-ENTRATA)
	 * @return boolean
	 */
	public boolean caricaListeBil(String tipoElemBil) {
		LeggiClassificatoriGenericiByTipoElementoBilResponse bilResponse = caricaClassifGenericiFin(tipoElemBil);
		if(bilResponse.isFallimento() || (bilResponse.getErrori() != null && bilResponse.getErrori().size() > 0)) {
			addErrori(CLASS_NAME, bilResponse);
			return true;
		} else {
			if (bilResponse.getClassificatoriPerimetroSanitarioSpesa() != null && bilResponse.getClassificatoriPerimetroSanitarioSpesa().size() > 0) {
				teSupport.setListaPerimetroSanitarioSpesa(bilResponse.getClassificatoriPerimetroSanitarioSpesa());
			} else 
				teSupport.setListaPerimetroSanitarioSpesa(new ArrayList<PerimetroSanitarioSpesa>());
			if (bilResponse.getClassificatoriTransazioneUnioneEuropeaSpesa() != null&& bilResponse.getClassificatoriTransazioneUnioneEuropeaSpesa().size() > 0) {
				teSupport.setListaTransazioneEuropeaSpesa(bilResponse.getClassificatoriTransazioneUnioneEuropeaSpesa());
			} else
				teSupport.setListaTransazioneEuropeaSpesa(new ArrayList<TransazioneUnioneEuropeaSpesa>());
			if (bilResponse.getClassificatoriRicorrenteSpesa() != null && bilResponse.getClassificatoriRicorrenteSpesa().size() > 0) {
				teSupport.setListaRicorrenteSpesa(bilResponse.getClassificatoriRicorrenteSpesa());
			} else
				teSupport.setListaRicorrenteSpesa(new ArrayList<RicorrenteSpesa>());
			if (bilResponse.getClassificatoriPoliticheRegionaliUnitarie() != null && bilResponse.getClassificatoriPoliticheRegionaliUnitarie().size() > 0) {
				teSupport.setListaPoliticheRegionaliUnitarie(bilResponse.getClassificatoriPoliticheRegionaliUnitarie());
			} else
				teSupport.setListaPoliticheRegionaliUnitarie(new ArrayList<PoliticheRegionaliUnitarie>());
			if (bilResponse.getClassificatoriTipoFinanziamento() != null && bilResponse.getClassificatoriTipoFinanziamento().size() > 0) {
				model.setListaTipoFinanziamento(bilResponse.getClassificatoriTipoFinanziamento());
			} else
				model.setListaTipoFinanziamento(new ArrayList<TipoFinanziamento>());
			
			
			// ENTRATA
			if(bilResponse.getClassificatoriTransazioneUnioneEuropeaEntrata()!=null && bilResponse.getClassificatoriTransazioneUnioneEuropeaEntrata().size()>0){
				teSupport.setListaTransazioneEuropeaEntrata(bilResponse.getClassificatoriTransazioneUnioneEuropeaEntrata());
			}else
				teSupport.setListaTransazioneEuropeaEntrata(new ArrayList<TransazioneUnioneEuropeaEntrata>());
			
			
			if(bilResponse.getClassificatoriRicorrenteEntrata() !=null && bilResponse.getClassificatoriRicorrenteEntrata().size()>0){
				teSupport.setListaRicorrenteEntrata(bilResponse.getClassificatoriRicorrenteEntrata());
			}else
				teSupport.setListaRicorrenteEntrata(new ArrayList<RicorrenteEntrata>());
		
			
			if (bilResponse.getClassificatoriPerimetroSanitarioEntrata() != null && bilResponse.getClassificatoriPerimetroSanitarioEntrata().size() > 0) {
				teSupport.setListaPerimetroSanitarioEntrata(bilResponse.getClassificatoriPerimetroSanitarioEntrata());
			} else 
				teSupport.setListaPerimetroSanitarioEntrata(new ArrayList<PerimetroSanitarioEntrata>());
			
		}
		return false;
	}
	
	/**
	 * Metodo che permette il caricamento delle liste relative agli Altri Classificatori
	 * @param tipoMovimento (IMPEGNO - ACCERTAMENTO)
	 * @return boolean
	 */
	protected boolean caricaListeFin(String tipoMovimento) {
		//istanzio la request per il servizio:
		LeggiClassificatoriGenericiByTipoMovimentoGest ll = new LeggiClassificatoriGenericiByTipoMovimentoGest();
		ll.setAnno(sessionHandler.getAnnoBilancio());
		ll.setCodiceTipoMovimentoGestione(tipoMovimento);
		ll.setDataOra(new Date());
		ll.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		ll.setRichiedente(sessionHandler.getRichiedente());
		//invoco il servizio:
		LeggiClassificatoriGenericiByTipoMovimentoGestResponse respClassFin =  classificatoreFinService.leggiClassificatoriGenericiByTipoMovimentoGest(ll);
		if(respClassFin.isFallimento() || (respClassFin.getErrori() != null && respClassFin.getErrori().size() > 0)) {
			debug(CLASS_NAME, "Errore nella risposta del servizio");
			return true;
		}
		if (null != respClassFin.getClassificatoriGenerici11() && respClassFin.getClassificatoriGenerici11().size() > 0) {
			teSupport.setListaClassificatoriGen11(respClassFin.getClassificatoriGenerici11());
		}else{
			teSupport.setListaClassificatoriGen11(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici12() && respClassFin.getClassificatoriGenerici12().size() > 0) {
			teSupport.setListaClassificatoriGen12(respClassFin.getClassificatoriGenerici12());
		}else{
			teSupport.setListaClassificatoriGen12(new ArrayList<ClassificatoreGenerico>());
		}
		if (null != respClassFin.getClassificatoriGenerici13() && respClassFin.getClassificatoriGenerici13().size() > 0) {
			teSupport.setListaClassificatoriGen13(respClassFin.getClassificatoriGenerici13());
		}else{
			teSupport.setListaClassificatoriGen13(new ArrayList<ClassificatoreGenerico>());
		}
		if (null != respClassFin.getClassificatoriGenerici14() && respClassFin.getClassificatoriGenerici14().size() > 0) {
			teSupport.setListaClassificatoriGen14(respClassFin.getClassificatoriGenerici14());		
		}else{
			teSupport.setListaClassificatoriGen14(new ArrayList<ClassificatoreGenerico>());
		}
		if (null != respClassFin.getClassificatoriGenerici15() && respClassFin.getClassificatoriGenerici15().size() > 0) {
			teSupport.setListaClassificatoriGen15(respClassFin.getClassificatoriGenerici15());
		}else{
			teSupport.setListaClassificatoriGen15(new ArrayList<ClassificatoreGenerico>());
		}
		
		// accertamento
		if (null != respClassFin.getClassificatoriGenerici16() && respClassFin.getClassificatoriGenerici16().size() > 0) {
			teSupport.setListaClassificatoriGen16(respClassFin.getClassificatoriGenerici16());
		}else{
			teSupport.setListaClassificatoriGen16(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici17() && respClassFin.getClassificatoriGenerici17().size() > 0) {
			teSupport.setListaClassificatoriGen17(respClassFin.getClassificatoriGenerici17());
		}else{
			teSupport.setListaClassificatoriGen17(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici18() && respClassFin.getClassificatoriGenerici18().size() > 0) {
			teSupport.setListaClassificatoriGen18(respClassFin.getClassificatoriGenerici18());
		}else{
			teSupport.setListaClassificatoriGen18(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici19() && respClassFin.getClassificatoriGenerici19().size() > 0) {
			teSupport.setListaClassificatoriGen19(respClassFin.getClassificatoriGenerici19());
		}else{
			teSupport.setListaClassificatoriGen19(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici20() && respClassFin.getClassificatoriGenerici20().size() > 0) {
			teSupport.setListaClassificatoriGen20(respClassFin.getClassificatoriGenerici20());
		}else{
			teSupport.setListaClassificatoriGen20(new ArrayList<ClassificatoreGenerico>());
		}
		
		
		
		return false;
	}
	
	
	/**
	 * Metodo che permette il caricamento delle liste relative agli Altri Classificatori
	 * @param tipoOrdinativo (IMPEGNO - ACCERTAMENTO)
	 * @return boolean
	 */
	protected boolean caricaListeFinOrdinativo(String tipoOrdinativo) {
		//istanzio la request per il servizio:
		LeggiClassificatoriGenericiByTipoOrdinativoGest ll = new LeggiClassificatoriGenericiByTipoOrdinativoGest();
		ll.setAnno(sessionHandler.getAnnoBilancio());
		ll.setCodiceTipoOrdinativoGestione(tipoOrdinativo);
		ll.setDataOra(new Date());
		ll.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		ll.setRichiedente(sessionHandler.getRichiedente());
		//invoco il servizio:
		LeggiClassificatoriGenericiByTipoOrdinativoGestResponse respClassFin =  classificatoreFinService.leggiClassificatoriGenericiByTipoOrdinativoGest(ll);
		if(respClassFin.isFallimento() || (respClassFin.getErrori() != null && respClassFin.getErrori().size() > 0)) {
			debug(CLASS_NAME, "Errore nella risposta del servizio");
			return true;
		}
		
		
		// ordinativo pagamento
		if (null != respClassFin.getClassificatoriGenerici21() && respClassFin.getClassificatoriGenerici21().size() > 0) {
			teSupport.setListaClassificatoriGen21(respClassFin.getClassificatoriGenerici21());
		}else{
			teSupport.setListaClassificatoriGen21(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici22() && respClassFin.getClassificatoriGenerici22().size() > 0) {
			teSupport.setListaClassificatoriGen22(respClassFin.getClassificatoriGenerici22());
		}else{
			teSupport.setListaClassificatoriGen22(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici23() && respClassFin.getClassificatoriGenerici23().size() > 0) {
			teSupport.setListaClassificatoriGen23(respClassFin.getClassificatoriGenerici23());
		}else{
			teSupport.setListaClassificatoriGen23(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici24() && respClassFin.getClassificatoriGenerici24().size() > 0) {
			teSupport.setListaClassificatoriGen24(respClassFin.getClassificatoriGenerici24());
		}else{
			teSupport.setListaClassificatoriGen24(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici25() && respClassFin.getClassificatoriGenerici25().size() > 0) {
			teSupport.setListaClassificatoriGen25(respClassFin.getClassificatoriGenerici25());
		}else{
			teSupport.setListaClassificatoriGen25(new ArrayList<ClassificatoreGenerico>());
		}
		
		// ordinativo incasso
		
		if (null != respClassFin.getClassificatoriGenerici26() && respClassFin.getClassificatoriGenerici26().size() > 0) {
			teSupport.setListaClassificatoriGen26(respClassFin.getClassificatoriGenerici26());
		}else{
			teSupport.setListaClassificatoriGen26(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici27() && respClassFin.getClassificatoriGenerici27().size() > 0) {
			teSupport.setListaClassificatoriGen27(respClassFin.getClassificatoriGenerici27());
		}else{
			teSupport.setListaClassificatoriGen27(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici28() && respClassFin.getClassificatoriGenerici28().size() > 0) {
			teSupport.setListaClassificatoriGen28(respClassFin.getClassificatoriGenerici28());
		}else{
			teSupport.setListaClassificatoriGen28(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici29() && respClassFin.getClassificatoriGenerici29().size() > 0) {
			teSupport.setListaClassificatoriGen29(respClassFin.getClassificatoriGenerici29());
		}else{
			teSupport.setListaClassificatoriGen29(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriGenerici30() && respClassFin.getClassificatoriGenerici30().size() > 0) {
			teSupport.setListaClassificatoriGen30(respClassFin.getClassificatoriGenerici30());
		}else{
			teSupport.setListaClassificatoriGen30(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriStipendi()&& respClassFin.getClassificatoriStipendi().size() > 0) {
			teSupport.setListaClassificatoriStipendi(respClassFin.getClassificatoriStipendi());
		}else{
			teSupport.setListaClassificatoriStipendi(new ArrayList<ClassificatoreStipendi>());
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo che carica la lista Cofog della Transazione Elementare
	 * @param idProgramma
	 */
	protected void caricaListaCofog(int idProgramma) {
		//istanzio la request per il servizio:
		LeggiClassificatoriByRelazione request = new LeggiClassificatoriByRelazione();
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getEnte());
		request.setIdClassif(idProgramma);
		request.setAnno(sessionHandler.getAnnoBilancio());
		//invoco il servizio:
		LeggiClassificatoriByRelazioneResponse response = classificatoreBilService.leggiClassificatoriByRelazione(request);
		List<ClassificazioneCofog> listaCofog = response.getClassificatoriClassificazioneCofog();
		if (null != listaCofog && listaCofog.size() > 0)
			teSupport.setListaCofog(listaCofog);
		else
			teSupport.setListaCofog(new ArrayList<ClassificazioneCofog>());
	}

	/**
	 * Metodo che carica la Missione della Transazione Elementare
	 * @param capitolo
	 */
	protected void caricaMissione(CapitoloImpegnoModel capitolo) {
		List<Missione> listaMissione = new ArrayList<Missione>();
		Missione missione = new Missione();
		if(null!=capitolo.getCodiceMissione()){
			missione.setCodice(capitolo.getCodiceMissione());
			missione.setDescrizione(capitolo.getCodiceMissione()+" - "+capitolo.getDescrizioneMissione());
			listaMissione.add(missione);
		}
		teSupport.setListaMissione(listaMissione);
		teSupport.setMissioneSelezionata(capitolo.getCodiceMissione());
	}

	/**
	 * Metodo che carica il Programma della Transazione Elementare
	 * @param capitolo
	 */
	protected void caricaProgramma(CapitoloImpegnoModel capitolo) {
		List<Programma> listaProgramma = new ArrayList<Programma>();
		Programma programma = new Programma();
		if(null!=capitolo.getCodiceProgramma()){
			programma.setCodice(capitolo.getCodiceProgramma());
			programma.setDescrizione(capitolo.getCodiceProgramma()+" - "+capitolo.getDescrizioneProgramma());
			listaProgramma.add(programma);
		}
		teSupport.setListaProgramma(listaProgramma);
		teSupport.setProgrammaSelezionato(capitolo.getCodiceProgramma());
	}
	
	/**
	 * Metodo che rende visualizzabili i pannelli relativi a Missione e Programma
	 * @return boolean
	 */
	public abstract boolean missioneProgrammaAttivi();
	/**
	 * Metodo che rende visualizzabili il pannello relativo al Cofog
	 * @return boolean
	 */
	public abstract boolean cofogAttivo();
	/**
	 * Metodo che rende visualizzabili il pannello relativo al Cup
	 * @return boolean
	 */
	public abstract boolean cupAttivo();
	/**
	 * Metodo che rende visualizzabile il pannello relatico al Programma Politico Regionale Unitario
	 * @return boolean
	 */
	public abstract boolean programmaPoliticoRegionaleUnitarioAttivo();
	/**
	 * Metodo che rende visualizzabile tutto il pannello per la gestione della Transazione Elementare
	 * @return boolean
	 */
	public abstract boolean transazioneElementareAttiva();
	/**
	 * Metodo che rende visualizzabile tutto il pannello per la gestione degli Altri Classificatori
	 * @return boolean
	 */
	public abstract boolean altriClassificatoriAttivi();
	/**
	 * Evento che viene scatenato alla selezione di un elemento dell'albero Piano Dei Conti
	 * @return String
	 */
	public abstract String confermaPdc();
	
	
	/**
	 * Evento che viene scatenato alla selezione di un elemento dell'albero Siope
	 * @return String
	 */
	public abstract String confermaSiope();
	
	/**
	 * Metodo che viene invocato all'onchange del campo di input del codice siope al
	 * fine di verificare se il codice corrisponde ad un siope esistente
	 * @return
	 */
	public String codiceSiopeChanged(){
		//leggo il nuovo codice immesso:
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		//richiamo il metodo interno:
		codiceSiopeChangedInternal(cod);
 		return "labelSiope";
	}
	
	protected abstract void codiceSiopeChangedInternal(String cod);
	
	/**
	 * Per verificare se il codice corrisponde ad un siope esistente.
	 * 
	 * Metodo centralizzato.
	 * 
	 * @param cod
	 * @param tipoList
	 */
	protected void codiceSiopeChangedInternal(String cod,TipiLista tipoList){
		
		boolean beccato = false;
		
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(tipoList);
	    List<CodificaExtFin> lista = (List<CodificaExtFin>) mappaListe.get(tipoList);
	    
	    //vediamo se lista ha elementi:
	    if(lista!=null && lista.size()>0){
	    	//cerchiamo il nostro:
	    	for(CodificaExtFin iterato : lista){
	    		if(iterato.getCodice().equals(cod)){
	    			//trovato
	    			beccato = true;
	    			SiopeSpesa siope = new SiopeSpesa();
	    			siope.setCodice(cod);
	    			siope.setDescrizione(iterato.getDescrizione());
	    			popolaSiope(siope,beccato);
	    			break;
	    			
	    		}
	    	}
	    }
		
		if(!beccato){
			//caso non trovato
			SiopeSpesa siope = new SiopeSpesa();
			siope.setCodice(cod);
			popolaSiope(siope,beccato);
		}
		
	}
	
	protected abstract void popolaSiope(SiopeSpesa siopeSpesa,boolean trovato);
	
	/**
	 * Metodo che rende visualizzabile le combo che riguardano o uscita o spesa
	 * @return boolean
	 */
	public abstract boolean datiUscitaImpegno();
	
	
	public void impostaObbligatoriConUnSoloElementoInLista(OggettoDaPopolareEnum oggettoDP){
		
		
		if(oggettoDP.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDP.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
			
			//COFOG:
			impostaCofogSeUnicoInLista();
			//
			
			//TRANSAZIONE EUROPEA:
			impostaTransazioneEuropeaSpesaSeUnicoInLista();
			//
			
			//RICORRENTE:
			impostaRicorrenteSpesaSeUnicoInLista();
			//
			
			
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.ACCERTAMENTO) || oggettoDP.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)){
			
			
			//TRANSAZIONE EUROPEA:
			impostaTransazioneEuropeaEntrataSeUnicoInLista();
			//
			
			//RICORRENTE:
			impostaRicorrenteEntrataSeUnicoInLista();
			//
			
			//PERIMETRO SANITARIO:
			impostaPerimetroSanitarioEntrataSeUnicoInLista();
			
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.LIQUIDAZIONE)){			
			
			//TRANSAZIONE EUROPEA:
			impostaTransazioneEuropeaSpesaSeUnicoInLista();
			//
	
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
			
			
		} else if(oggettoDP.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
			
		}
		
		
	}
	
	private void impostaCofogSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaCofog()) && FinStringUtils.isEmpty(teSupport.getCofogSelezionato())){
			teSupport.setCofogSelezionato(teSupport.getListaCofog().get(0).getCodice());
		}
	}
	
	private void impostaTransazioneEuropeaEntrataSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaTransazioneEuropeaEntrata()) && FinStringUtils.isEmpty(teSupport.getTransazioneEuropeaSelezionato())){
			teSupport.setTransazioneEuropeaSelezionato(teSupport.getListaTransazioneEuropeaEntrata().get(0).getCodice());
		}
	}
	
	private void impostaTransazioneEuropeaSpesaSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaTransazioneEuropeaSpesa()) && FinStringUtils.isEmpty(teSupport.getTransazioneEuropeaSelezionato())){
			teSupport.setTransazioneEuropeaSelezionato(teSupport.getListaTransazioneEuropeaSpesa().get(0).getCodice());
		}
	}
	
	private void impostaRicorrenteSpesaSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaRicorrenteSpesa()) && FinStringUtils.isEmpty(teSupport.getRicorrenteSpesaSelezionato())){
			teSupport.setRicorrenteSpesaSelezionato(teSupport.getListaRicorrenteSpesa().get(0).getCodice());
		}
	}
	
	private void impostaRicorrenteEntrataSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaRicorrenteEntrata()) && FinStringUtils.isEmpty(teSupport.getRicorrenteEntrataSelezionato())){
			teSupport.setRicorrenteEntrataSelezionato(teSupport.getListaRicorrenteEntrata().get(0).getCodice());
		}
	}
	
	private void impostaPerimetroSanitarioEntrataSeUnicoInLista(){
		if(FinUtility.haUnSoloElementoNonNullo(teSupport.getListaPerimetroSanitarioEntrata()) && FinStringUtils.isEmpty(teSupport.getPerimetroSanitarioEntrataSelezionato())){
			teSupport.setPerimetroSanitarioEntrataSelezionato(teSupport.getListaPerimetroSanitarioEntrata().get(0).getCodice());
		}
	}
	
	
	public List<Errore> abilitazioneCampiTE(OggettoDaPopolareEnum oggettoDP){
		
		List<Errore> errori = new ArrayList<Errore>();
		
		if(oggettoDP.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDP.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
			
			// Controlli per il caso di IMPEGNO
			if(null==teSupport.getPianoDeiConti() || teSupport.getPianoDeiConti().getUid()==0){
				errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("P.d.C. finanziario"));
				
			}
			
			if(null==teSupport.getCofogSelezionato() || teSupport.getCofogSelezionato().equals("")){
				errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cofog"));
				
			}

			
			if(null==teSupport.getTransazioneEuropeaSelezionato() || teSupport.getTransazioneEuropeaSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codifica Transazione Europea"));
				 
			 }
			
			if(null==teSupport.getRicorrenteSpesaSelezionato() || teSupport.getRicorrenteSpesaSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ricorrente"));
				 
			 }
			
			if(!teSupport.isTrovatoSiopeSpesa() || StringUtils.isEmpty(teSupport.getSiopeSpesaCod())){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope"));
			 }
			
			
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.ACCERTAMENTO) || oggettoDP.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)){
			
			// Controlli per il caso di ACCERTAMENTO
			if(null==teSupport.getPianoDeiConti() || teSupport.getPianoDeiConti().getUid()==0){
				errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("P.d.C. finanziario"));
				
			}
			
			if(null==teSupport.getTransazioneEuropeaSelezionato() || teSupport.getTransazioneEuropeaSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codifica Transazione Europea"));
				 
			 }
			
			if(null==teSupport.getRicorrenteEntrataSelezionato() || teSupport.getRicorrenteEntrataSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ricorrente"));
				 
			 }
			
			 if(null==teSupport.getPerimetroSanitarioEntrataSelezionato() || teSupport.getPerimetroSanitarioEntrataSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Perimetro sanitario"));
				 
			 }
			 
			 if(!teSupport.isTrovatoSiopeSpesa() || StringUtils.isEmpty(teSupport.getSiopeSpesaCod())){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope"));
			 }
			
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.LIQUIDAZIONE)){			
			
			if(null==teSupport.getTransazioneEuropeaSelezionato() || teSupport.getTransazioneEuropeaSelezionato().equals("")){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codifica Transazione Europea"));
				 
			}
			
 			//CR 2023 eliminato conto economico
			
			if(!teSupport.isTrovatoSiopeSpesa() || StringUtils.isEmpty(teSupport.getSiopeSpesaCod())){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope"));
			 }
	
		}else if(oggettoDP.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
			
			if(!teSupport.isTrovatoSiopeSpesa() || StringUtils.isEmpty(teSupport.getSiopeSpesaCod())){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope"));
			}
			
		} else if(oggettoDP.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
			
			if(!teSupport.isTrovatoSiopeSpesa() || StringUtils.isEmpty(teSupport.getSiopeSpesaCod())){
				 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Siope"));
			}
			
		}
		
		return errori;
		
	}
	
	
	@Override
	public String getActionDataKeys() {
		return "model,teSupport";
	}

	public GestoreTransazioneElementareModel getTeSupport() {
		return teSupport;
	}

	public void setTeSupport(GestoreTransazioneElementareModel teSupport) {
		this.teSupport = teSupport;
	}

}
