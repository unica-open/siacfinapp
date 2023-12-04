/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigInteger;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisceAccertamentoStep1Action extends ActionKeyInserisceAccertamento {

	private static final long serialVersionUID = 1L;
	
	private String ripetereMovimento;
	
	public InserisceAccertamentoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
	@Override
	public boolean sonoInInserimento() {
		// ritorna true
		return super.sonoInInserimento();
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		try{
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			caricaStatiOperativiAtti();
			
			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();
			
		    model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
		    model.getStep1Model().setImplPluriennale(buildListaSiNo());
		    model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());
		    //SIAC-6997
		    model.getStep1Model().setDaReanno(buildListaSiNo());
			recuperaDescrizioneStrutturaCompetente();
		    
		} catch(Exception e) {
			log.debug("prepare", e.getMessage());
		}
		
		//setto il titolo:
		this.model.setTitolo("Inserisce Accertamento - Dati Accertamento");
	}
	
	
	public boolean isAccertamentoPlurAbilitato(){
		return isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoPluriennale);
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//FIX PER JIRA  SIAC-2709 caso in cui si crea una situazione incoerente cliccando il filo d'arianna:
		if(ripetereMovimento()){
			GestisciMovGestModel mvDaRiperte = (GestisciMovGestModel) sessionHandler.getParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE);
			if(mvDaRiperte==null){
				setRipetereMovimento("false");
				forceReload = true;
			}
		}
		//
		
		settaBilancioInPredispConsuntivoNelModel();
		
		if(isAbilitatoGestisciAccertamentoDec()){
			// decentrato
			setAzioniDecToCheck(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentrato.getNomeAzione());
			if(!checkAzioniDec()){
			   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
			}
		}else if(isAbilitatoGestisciAccertamento()){
			    // master
			setAzioniToCheck(AzioneConsentitaEnum.OP_ENT_gestisciAccertamento.getNomeAzione());
			if(!checkAzioni()){
				   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
				}
		}else{
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
		}
		
		// verifico lo stato di bilancio
		// nel caso genero errore non appena si atterra sulla pagina
		controlloStatoBilancio(sessionHandler.getAnnoBilancio(), "INSERIMENTO", "ACCERTAMENTO");
		
			
		if(forceReload){
			
			if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
				model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				if(model.getStep1Model().getAnnoImpegno() == null || "".equalsIgnoreCase(model.getStep1Model().getAnnoImpegno().toString())){
					model.getStep1Model().setAnnoImpegno(sessionHandler.getAnnoBilancio());
				}
				
			}
			
			model.getStep1Model().setRiaccertato( setNoIfNull(model.getStep1Model().getRiaccertato()));
			//SIAC-6997
			model.getStep1Model().setReanno(setNoIfNull(model.getStep1Model().getReanno()));
			model.getStep1Model().setPluriennale(setNoIfNull(model.getStep1Model().getPluriennale()));
			model.getStep1Model().setFlagFattura(setNoIfNull(model.getStep1Model().getFlagFattura()));
			model.getStep1Model().setFlagCorrispettivo(setNoIfNull(model.getStep1Model().getFlagCorrispettivo()));
			model.getStep1Model().setFlagAttivaGsa(setNoIfNull(model.getStep1Model().getFlagAttivaGsa()));
			
			//SIAC-6997
			recuperaDescrizioneStrutturaCompetente();
//			if(model.getStep1Model().getStrutturaSelezionataCompetente() != null && !model.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
//				List<StrutturaAmministrativoContabile> lista = sessionHandler.getAccount().getStruttureAmministrativeContabili();
//				if(lista != null && !lista.isEmpty()){
//					if(lista.size() > 1){
//						//devo filtrare l'elenco per codice
//						for(int j = 0; j < lista.size(); j++){
//							if(lista.get(j).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())) {
//								model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getCodice() +"-"+lista.get(j).getDescrizione());
//								break;
//							}else{
//								if(lista.get(j).getSubStrutture() != null && !lista.get(j).getSubStrutture().isEmpty()){
//									for(int k = 0; k < lista.get(j).getSubStrutture().size(); k++){
//										if(lista.get(j).getSubStrutture().get(k).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())){
//											model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getSubStrutture().get(k).getCodice() +"-"+lista.get(j).getSubStrutture().get(k).getDescrizione());
//											break;
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
			
			if(model.getStep1Model().getStato() == null){
				model.getStep1Model().setStato(MOVGEST_PROVVISORIO);
			}
			
		}
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		if (caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}
		
		caricaLabelsInserisci(1);
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		//CR-2023 si elimina CE
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		if(ripetereMovimento()){
			ripetiAccertamento((GestisciMovGestModel) sessionHandler.getParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE));
			sessionHandler.setParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE, null);
		}
		
		return SUCCESS;
	} 
	
	//SIAC-6997
	/*protected void recuperaDescrizioneStrutturaCompetente() {
			
			
			//istanzio la request per il servizio:
			LeggiStrutturaAmminstrativoContabile request = new LeggiStrutturaAmminstrativoContabile();

			request.setAnno(sessionHandler.getAnnoBilancio());
			request.setIdEnteProprietario(sessionHandler.getEnte().getUid());
			request.setRichiedente(sessionHandler.getRichiedente());

			//invoco il servizio:
			LeggiStrutturaAmminstrativoContabileResponse response = classificatoreService.leggiStrutturaAmminstrativoContabile(request);

			
			List<StrutturaAmministrativoContabile> lista =response.getListaStrutturaAmmContabile();// sessionHandler.getAccount().getStruttureAmministrativeContabili();
			if(lista != null && !lista.isEmpty() && model.getStep1Model().getStrutturaSelezionataCompetente()!=null){

					
						 for(int j = 0; j < lista.size(); j++){
							 if(lista.get(j).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())) {
								 model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getCodice() +"-"+lista.get(j).getDescrizione());
								 break;
							 }else{
								 if(lista.get(j).getSubStrutture() != null && !lista.get(j).getSubStrutture().isEmpty()){
									 for(int k = 0; k < lista.get(j).getSubStrutture().size(); k++){
										 if(lista.get(j).getSubStrutture().get(k).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())){
											 model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getSubStrutture().get(k).getCodice() +"-"+lista.get(j).getSubStrutture().get(k).getDescrizione());
											 break;
										 }
									 }
								 }
							 }
						 }
					 
				 
			}
		}  */		
	
	private boolean ripetereMovimento(){
		if(getRipetereMovimento()!=null && getRipetereMovimento().equals("true")){
			return true;
		} else {
			return false;
		}
	}
	
	private void ripetiAccertamento(GestisciMovGestModel gm){
		Accertamento accertamentoDaRipetere = gm.getAccertamentoInAggiornamento();
		
		model.setAnnoImpegno(accertamentoDaRipetere.getAnnoMovimento());
	
		// CAPITOLO
		CapitoloImpegnoModel cim = new CapitoloImpegnoModel();
		cim.setAnno(accertamentoDaRipetere.getCapitoloEntrataGestione().getAnnoCapitolo());
		cim.setNumCapitolo(accertamentoDaRipetere.getCapitoloEntrataGestione().getNumeroCapitolo());
		cim.setArticolo(accertamentoDaRipetere.getCapitoloEntrataGestione().getNumeroArticolo());
		if(accertamentoDaRipetere.getCapitoloEntrataGestione().getNumeroUEB()!=null){
			cim.setUeb(new BigInteger(accertamentoDaRipetere.getCapitoloEntrataGestione().getNumeroUEB().toString()));
		}
		
		eseguiRicercaCapitolo(cim, false, OggettoDaPopolareEnum.ACCERTAMENTO);
		
		CapitoloImpegnoModel capTrovato = model.getCapitoloModelTrovatoDaServizio();
		
		GestisciImpegnoStep1Model step1Model = model.getStep1Model();
		step1Model.setCapitolo(capTrovato);
		step1Model.setCapitoloSelezionato(true);
		
		// PROVVEDIMENTO
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		if(accertamentoDaRipetere.getAttoAmministrativo()!=null && accertamentoDaRipetere.getAttoAmministrativo().getUid()!=0){
			pim = getProvvedimentoById(accertamentoDaRipetere.getAttoAmministrativo().getUid());
			step1Model.setProvvedimentoSelezionato(true);
			setStrutturaSelezionataSuPagina(String.valueOf(pim.getUidStrutturaAmm()));
		}
		
		
		step1Model.setProvvedimento(pim);
		
		// SOGGETTO
		SoggettoImpegnoModel sim = new SoggettoImpegnoModel();
		if(accertamentoDaRipetere.getSoggetto()!=null && accertamentoDaRipetere.getSoggetto().getUid()!=0){
			step1Model.setSoggettoSelezionato(true);
			
			step1Model.setSoggettoImpegno(accertamentoDaRipetere.getSoggetto());
			sim.setCodCreditore(accertamentoDaRipetere.getSoggetto().getCodiceSoggetto());
			sim.setDenominazione(accertamentoDaRipetere.getSoggetto().getDenominazione());
			sim.setCodfisc(accertamentoDaRipetere.getSoggetto().getCodiceFiscale());
		}
        step1Model.setSoggetto(sim); 
        
        
        // Dati acc
        step1Model.setOggettoImpegno(accertamentoDaRipetere.getDescrizione());
        step1Model.setImportoFormattato(accertamentoDaRipetere.getImportoAttuale().toString());
        if(accertamentoDaRipetere.getDataScadenza()!=null){
        	step1Model.setScadenza(DateUtility.convertiDataInGgMmYyyy(accertamentoDaRipetere.getDataScadenza()));
		}
        if(accertamentoDaRipetere.getProgetto()!=null){
        	step1Model.setProgetto(accertamentoDaRipetere.getProgetto().getCodice());
        }
        step1Model.setCup(accertamentoDaRipetere.getCup());
        // RIACCERTAMENTO
        // probabilmente non ha senso copiare gli stessi dati da un altro movimento
        //Impegno origine
        // probabilmente non ha senso copiare gli stessi dati da un altro movimento
        
        
        step1Model.setRiaccertato(accertamentoDaRipetere.isFlagDaRiaccertamento() ? WebAppConstants.Si : WebAppConstants.No);
        step1Model.setFlagFattura(accertamentoDaRipetere.isFlagFattura() ? WebAppConstants.Si : WebAppConstants.No);
        step1Model.setFlagCorrispettivo(accertamentoDaRipetere.isFlagCorrispettivo() ? WebAppConstants.Si : WebAppConstants.No);
        
        // TRANSAZIONE ELEMENTARE
        
        // PDC
        ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
        pdc.setCodice(accertamentoDaRipetere.getCodPdc());
        pdc.setDescrizione(accertamentoDaRipetere.getCodPdc()+" - "+accertamentoDaRipetere.getDescPdc());
        pdc.setUid(accertamentoDaRipetere.getIdPdc());
        pdc.setTipoClassificatore(step1Model.getCapitolo().getTipoClassificatorePdc());
        // forzo il V livello
        pdc.getTipoClassificatore().setCodice(V_LIVELLO_TIPO_CLASSIFICATORE);
        teSupport.setPianoDeiConti(pdc);
        if(step1Model.getCapitolo().getIdPianoDeiConti() != null){
			teSupport.setIdPianoDeiContiPadrePerAggiorna(step1Model.getCapitolo().getIdPianoDeiConti());
		}
       
        teSupport.setIdMacroAggregato(step1Model.getCapitolo().getIdMacroAggregato());
        
        // COMBO
        teSupport.setTransazioneEuropeaSelezionato(accertamentoDaRipetere.getCodTransazioneEuropeaSpesa());
        teSupport.setRicorrenteEntrataSelezionato(accertamentoDaRipetere.getCodRicorrenteSpesa());
        teSupport.setPerimetroSanitarioEntrataSelezionato(accertamentoDaRipetere.getCodCapitoloSanitarioSpesa());
        
        // Classificatori
        teSupport.setClassGenSelezionato6(accertamentoDaRipetere.getCodClassGen16());
        teSupport.setClassGenSelezionato7(accertamentoDaRipetere.getCodClassGen17());
        teSupport.setClassGenSelezionato8(accertamentoDaRipetere.getCodClassGen18());
        teSupport.setClassGenSelezionato9(accertamentoDaRipetere.getCodClassGen19());
        teSupport.setClassGenSelezionato10(accertamentoDaRipetere.getCodClassGen20());
        
        //SIOPE:
        teSupport.setSiopeSpesaCod(accertamentoDaRipetere.getCodSiope());
        codiceSiopeChangedInternal(accertamentoDaRipetere.getCodSiope());
        
	}
	
	
	// pulisce i campi della pagina
	public String annulla() throws Exception {
		model.getStep1Model().setCapitolo(new CapitoloImpegnoModel());
		model.getStep1Model().setCapitoloSelezionato(false);
		model.getStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
		model.getStep1Model().setProvvedimentoSelezionato(false);
		model.getStep1Model().setSoggetto(new SoggettoImpegnoModel());
		model.getStep1Model().setSoggettoSelezionato(false);
		model.getStep1Model().setAnnoImpegno(null);
		model.getStep1Model().setTipoImpegno("");
		model.getStep1Model().setDescrizioneAnnoBilancio("");
		model.getStep1Model().setOggettoImpegno("");
		model.getStep1Model().setImportoImpegno(null);
		model.getStep1Model().setImportoFormattato("");
		model.getStep1Model().setScadenza("");
		model.getStep1Model().setProgetto("");
		model.getStep1Model().setAnnoFinanziamento(null);
		model.getStep1Model().setNumeroFinanziamento(null);
		model.getStep1Model().setCig("");
		model.getStep1Model().setCup("");
		model.getStep1Model().setAnnoImpOrigine(null);
		model.getStep1Model().setNumeroImpegno(null);
		model.getStep1Model().setNumImpOrigine(null);
		model.getStep1Model().setNumeroPluriennali(null);
		model.getStep1Model().setAnnoImpRiacc(null);
        model.getStep1Model().setNumImpRiacc(null);
		
		ricaricaValoriDefaultStep1();
	    return SUCCESS;
	}

	public String getRipetereMovimento() {
		return ripetereMovimento;
	}

	public void setRipetereMovimento(String ripetereMovimento) {
		this.ripetereMovimento = ripetereMovimento;
	}
	

	@Override
	public String ricercaProgetto() throws Exception {
		super.ricercaProgetto();
		
		//model.moveListaRicercaProgettoToListaRicercaProgettoCronoprogrammi();
		
		return "ricercaProgettoCronop";
	}
	

}
