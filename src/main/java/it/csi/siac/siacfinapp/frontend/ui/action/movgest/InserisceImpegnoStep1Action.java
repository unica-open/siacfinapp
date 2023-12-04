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

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisceImpegnoStep1Action extends ActionKeyInserisceImpegno {

	private static final long serialVersionUID = 1L;
	
	private String ripetereMovimento;
	
	//SIAC-6997
	private boolean ricaricaStrutturaAmministrativa = true;
	
	
	public InserisceImpegnoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	public boolean isRicaricaStrutturaAmministrativa() {
		return ricaricaStrutturaAmministrativa;
	}

	public void setRicaricaStrutturaAmministrativa(boolean ricaricaStrutturaAmministrativa) {
		this.ricaricaStrutturaAmministrativa = ricaricaStrutturaAmministrativa;
	}
	
	/**
	 * settembre 2017 SIAC-5288
	 * @return
	 */
	public boolean isAbilitatoInserimentoVincolo(){
		return true;
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
		
		/*SIAC-6929
		 * Abilitiamo il controllo sul blocco ragioneria
		 */
		model.setControlloBloccoRagioneriaAbilitato(true);
		
		this.model.setOperazioneAggiorna(false);
		try{
			sessionHandler.getBilancio().getStato();
			
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().isEmpty()){
				caricaTipiProvvedimenti();
			}
			
			if(model.getListaTipiAmbito()==null || model.getListaTipiAmbito().isEmpty()){
				caricaListaAmbiti();
			}
			
			caricaStatiOperativiAtti();
			
			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();
			
		    model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
		    model.getStep1Model().setImplPluriennale(buildListaSiNo());
		    model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());
		    //SIAC-6997
		    model.getStep1Model().setDaReanno(buildListaSiNo());
		    model.getStep1Model().setDaPrenotazione(buildListaSiNo());
		    model.getStep1Model().setDiCassaEconomale(buildListaSiNo());
		    
		    model.getStep1Model().setScelteFrazionabile(buildListaFrazionabile());
		    model.getStep1Model().setFrazionabile(WebAppConstants.FRAZIONABILE);
		    
		    //tipo debito siope:
		    model.getStep1Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());
		    model.getStep1Model().setTipoDebitoSiope(WebAppConstants.COMMERCIALE_CON_FATTURE);
		    
		    // il campo anno ricerca per accertamento
		    model.getStep1Model().getAccertamentoRicerca().setAnnoCapitolo(sessionHandler.getAnnoEsercizio());
			
		    // non agisce l'ancora dei vincoli 
		    model.getStep1Model().setPortaAdAltezzaVincoli(false);
		    
			//SIAC-6997
			recuperaDescrizioneStrutturaCompetente();
		    
		} catch(Exception e) {
			log.debug("prepare", e.getMessage());
		}
		
		//setto il titolo:
		this.model.setTitolo("Inserisce Impegno - Dati Impegno");
	}
	
	private void caricaListaAmbiti() {
		
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(sessionHandler.getAnnoBilancio());
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);

		model.setListaTipiAmbito(response.getTipiAmbito());
	}
	
	
	public boolean isVisibileFlagFrazionabile(){
		//In inserimento il frazionabile compare solo se il bilancio . in stato "Esercizio Provvisoro"
		return isBilancioAttualeInEsercizioProvvisorio();
	}
	
	
	public boolean isImpegnoPlurAbilitato(){
		return isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoPluriennale);
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		//SIAC-7349
		Integer componenteUidFromRipeti=null;
		//FIX PER JIRA  SIAC-2709 caso in cui si crea una situazione incoerente cliccando il filo d'arianna:
		if(ripetereMovimento()){
			GestisciMovGestModel mvDaRiperte = (GestisciMovGestModel) sessionHandler.getParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE);
			if(mvDaRiperte==null){
				setRipetereMovimento("false");
				forceReload = true;
			}else{
				if(mvDaRiperte.getStep1Model()!= null && mvDaRiperte.getStep1Model().getCapitolo()!= null){
					componenteUidFromRipeti = mvDaRiperte.getStep1Model().getCapitolo().getComponenteBilancioUid();
				}
				
			}
		}
		//
		
		// precondizioni di ingresso
		
		settaBilancioInPredispConsuntivoNelModel();
		
		if(isAbilitatoGestisciImpegnoDec()){
			// decentrato
			setAzioniDecToCheck(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentrato.getNomeAzione());
			if(!checkAzioniDec()){
			   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
			}
		}else if(isAbilitatoGestisciImpegno()){
			    // master
			setAzioniToCheck(AzioneConsentitaEnum.OP_SPE_gestisciImpegno.getNomeAzione());
			if(!checkAzioni()){
				   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
				}
		}else{
			 addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
		}
		
		
		// verifico lo stato di bilancio
		// nel caso genero errore non appena si atterra sulla pagina
		controlloStatoBilancio(sessionHandler.getAnnoBilancio(), "INSERIMENTO", "IMPEGNO");
		
		//jira 1846
		if(forceReload || (model.getStep1Model().getCapitolo() != null && model.getStep1Model().getCapitolo().getAnno() == null)){
			
			//CARICAMENTO AVANZOVINCOLI:
			caricaListaAvanzovincolo();
			initSceltaAccertamentoAvanzoList();
			//
			
			if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
				model.getStep1Model().setAnnoImpegno(sessionHandler.getAnnoBilancio());
				model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				//anomalia numero 16
			}

			model.getStep1Model().setRiaccertato(setNoIfNull(model.getStep1Model().getRiaccertato()));
			//SIAC-6997
			model.getStep1Model().setReanno(setNoIfNull(model.getStep1Model().getReanno()));
			model.getStep1Model().setSoggettoDurc(setNoIfNull(model.getStep1Model().getSoggettoDurc()));
			model.getStep1Model().setPluriennale(setNoIfNull(model.getStep1Model().getPluriennale()));
			model.getStep1Model().setFlagAttivaGsa(setNoIfNull(model.getStep1Model().getFlagAttivaGsa()));
			
			model.getStep1Model().setPrenotazione(setNoIfNull(model.getStep1Model().getPrenotazione()));
			model.getStep1Model().setCassaEconomale(setNoIfNull(model.getStep1Model().getCassaEconomale()));
			
			model.getStep1Model().setFrazionabile(setFrazionabileIfNull(model.getStep1Model().getFrazionabile()));
			
			if(model.getStep1Model().getStato() == null){
				model.getStep1Model().setStato(CostantiFin.STATO_PROVVISORIO);
			}
			
			//SIAC-6997
			recuperaDescrizioneStrutturaCompetente();
//			if(model.getStep1Model().getStrutturaSelezionataCompetente() != null && !model.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
//				List<StrutturaAmministrativoContabile> lista = sessionHandler.getAccount().getStruttureAmministrativeContabili();
//				if(lista != null && !lista.isEmpty()){
//					 if(lista.size() > 1){
//						//devo filtrare l'elenco per codice
//						for(int j = 0; j < lista.size(); j++){
//								//System.out.println("SAC(in sessione) n." + j + " e uid="+lista.get(j).getUid() + " e cod+descrizione="+lista.get(j).getCodice() + "-" + lista.get(j).getDescrizione());
//							 if(lista.get(j).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())) {
//								 model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getCodice() +"-"+lista.get(j).getDescrizione());
//								 break;
//							 }else{
//								 if(lista.get(j).getSubStrutture() != null && !lista.get(j).getSubStrutture().isEmpty()){
//									 for(int k = 0; k < lista.get(j).getSubStrutture().size(); k++){
//										 if(lista.get(j).getSubStrutture().get(k).getUid() == Integer.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())){
//											 model.getStep1Model().setStrutturaSelezionataCompetenteDesc(lista.get(j).getSubStrutture().get(k).getCodice() +"-"+lista.get(j).getSubStrutture().get(k).getDescrizione());
//											 break;
//										 }
//									 }
//								 }
//							 }
//						}
//					 }
//				}
//			}
			
		}
		
		if(teSupport == null){
			pulisciTransazioneElementare();
		}
		
		if(caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		}
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getStep1Model().getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getStep1Model().getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//Descrizione dell'impegno:
		model.getStep1Model().setTipoImpegno(model.getListaTipiImpegno().get(0).getDescrizione());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//Label di inserisci:
		caricaLabelsInserisci(1);
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		if(ripetereMovimento()){
			ripetiImpegno((GestisciMovGestModel) sessionHandler.getParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE));
			sessionHandler.setParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE, null);
		}
		
		/*
		 * SIAC-7349
		 * Setting dell abilitazione per 
		 * il funzionamento con le componenti di bilancio
		 * del capitolo
		 */
		model.setComponenteBilancioCapitoloAttivo(true);
		//SE PRESENTE UID MA ASSENTE LA LISTA IL CARICAMENTO AVVIENE LATO AJAX. BISOGNA RISETTARE LA LISTA.
    	if(model.getStep1Model().getCapitolo()!= null && model.getStep1Model().getCapitolo().getListaComponentiBilancio()== null
    			|| model.getStep1Model().getCapitolo().getListaComponentiBilancio().isEmpty() 
    			){
    		
    		//model.getStep1Model().setListaComponentiCapitoloFromAjax();
    		model.getStep1Model().getCapitolo().setComponenteBilancioUid(model.getStep1Model().getComponenteImpegnoCapitoloUid());
    		model.getStep1Model().getCapitolo().setComponenteBilancioUidReturn(model.getStep1Model().getComponenteImpegnoCapitoloUid());
    		//DA RIPETI componenteUidFromRipeti
    		if(model.getStep1Model().getComponenteImpegnoCapitoloUid()== null  ){
    			model.getStep1Model().getCapitolo().setComponenteBilancioUid(componenteUidFromRipeti);
        		model.getStep1Model().getCapitolo().setComponenteBilancioUidReturn(componenteUidFromRipeti);
    		}
    		
    		
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
	
	private void ripetiImpegno(GestisciMovGestModel gm){
		if(gm!=null && gm.getImpegnoInAggiornamento()!=null){
			
			Impegno impegnoDaRipetere = gm.getImpegnoInAggiornamento();
			
			model.setAnnoImpegno(impegnoDaRipetere.getAnnoMovimento());
		
			// CAPITOLO
			CapitoloImpegnoModel cim = new CapitoloImpegnoModel();
			cim.setAnno(impegnoDaRipetere.getCapitoloUscitaGestione().getAnnoCapitolo());
			cim.setNumCapitolo(impegnoDaRipetere.getCapitoloUscitaGestione().getNumeroCapitolo());
			cim.setArticolo(impegnoDaRipetere.getCapitoloUscitaGestione().getNumeroArticolo());
			if(impegnoDaRipetere.getCapitoloUscitaGestione().getNumeroUEB()!=null){
				cim.setUeb(new BigInteger(impegnoDaRipetere.getCapitoloUscitaGestione().getNumeroUEB().toString()));
			}
			
			eseguiRicercaCapitolo(cim, false, OggettoDaPopolareEnum.IMPEGNO);
			
			CapitoloImpegnoModel capTrovato = model.getCapitoloModelTrovatoDaServizio();
			
			model.getStep1Model().setCapitolo(capTrovato);
			model.getStep1Model().setCapitoloSelezionato(true);
			
			// PROVVEDIMENTO
			ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
			if(impegnoDaRipetere.getAttoAmministrativo()!=null && impegnoDaRipetere.getAttoAmministrativo().getUid()!=0){
				pim = getProvvedimentoById(impegnoDaRipetere.getAttoAmministrativo().getUid());
				model.getStep1Model().setProvvedimentoSelezionato(true);
				setStrutturaSelezionataSuPagina(String.valueOf(pim.getUidStrutturaAmm()));
			}
			
			model.getStep1Model().setProvvedimento(pim);
			
			// SOGGETTO
			SoggettoImpegnoModel sim = new SoggettoImpegnoModel();
			if(impegnoDaRipetere.getSoggetto()!=null && impegnoDaRipetere.getSoggetto().getUid()!=0){
				//IMPEGNO CON SOGGETTO
				model.getStep1Model().setSoggettoSelezionato(true);
				model.getStep1Model().setSoggettoImpegno(impegnoDaRipetere.getSoggetto());
				sim.setCodCreditore(impegnoDaRipetere.getSoggetto().getCodiceSoggetto());
				sim.setDenominazione(impegnoDaRipetere.getSoggetto().getDenominazione());
				sim.setCodfisc(impegnoDaRipetere.getSoggetto().getCodiceFiscale());
			} else if(impegnoDaRipetere.getClasseSoggetto()!=null && !isEmpty(impegnoDaRipetere.getClasseSoggetto().getCodice())){
				//IMPEGNO CON CLASSE SOGGETTO
				model.getStep1Model().setSoggettoSelezionato(false);
				model.getStep1Model().setSoggettoImpegno(impegnoDaRipetere.getSoggetto());
				String codeClasse = impegnoDaRipetere.getClasseSoggetto().getCodice();
				sim.setIdClasse(Integer.toString(FinUtility.getUidByCode(model.getListaClasseSoggetto(),codeClasse )));
				sim.setClasse(codeClasse);
			}
	        model.getStep1Model().setSoggetto(sim);
	        
	        // Dati impegno
	        model.getStep1Model().setTipoImpegno(impegnoDaRipetere.getTipoImpegno().getCodice());
	        model.getStep1Model().setOggettoImpegno(impegnoDaRipetere.getDescrizione());
	        model.getStep1Model().setImportoFormattato(impegnoDaRipetere.getImportoAttuale().toString());
	        if(impegnoDaRipetere.getDataScadenza()!=null){
	        	model.getStep1Model().setScadenza(DateUtility.convertiDataInGgMmYyyy(impegnoDaRipetere.getDataScadenza()));
			}
	        if(impegnoDaRipetere.getProgetto()!=null){
	        	model.getStep1Model().setProgetto(impegnoDaRipetere.getProgetto().getCodice());
	        }
	        model.getStep1Model().setCig(impegnoDaRipetere.getCig());
	        model.getStep1Model().setCup(impegnoDaRipetere.getCup());
	        
	        //SIOPE PLUS:
	        impostaDatiSiopePlusNelModel(impegnoDaRipetere, model.getStep1Model());
	        
	        // RIACCERTAMENTO
	        // probabilmente non ha senso copiare gli stessi dati da un altro movimento
	        //Impegno origine
	        // probabilmente non ha senso copiare gli stessi dati da un altro movimento
	        
	        // TRANSAZIONE ELEMENTARE
	        
	        // PDC
	        ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
	        pdc.setCodice(impegnoDaRipetere.getCodPdc());
	        pdc.setDescrizione(impegnoDaRipetere.getCodPdc()+" - "+impegnoDaRipetere.getDescPdc());
	        pdc.setUid(impegnoDaRipetere.getIdPdc());
	        pdc.setTipoClassificatore(model.getStep1Model().getCapitolo().getTipoClassificatorePdc());
	        // forzo il V livello
	        pdc.getTipoClassificatore().setCodice(V_LIVELLO_TIPO_CLASSIFICATORE);
	        teSupport.setPianoDeiConti(pdc);
	        if(model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null){
				teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
			}
	       
	        teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());
	        
	        //COMBO
	        teSupport.setCup(impegnoDaRipetere.getCup());
	        teSupport.setCofogSelezionato(impegnoDaRipetere.getCodCofog());
	        teSupport.setTransazioneEuropeaSelezionato(impegnoDaRipetere.getCodTransazioneEuropeaSpesa());
	        teSupport.setRicorrenteSpesaSelezionato(impegnoDaRipetere.getCodRicorrenteSpesa());
	        teSupport.setPerimetroSanitarioSpesaSelezionato(impegnoDaRipetere.getCodCapitoloSanitarioSpesa());
	        teSupport.setPoliticaRegionaleSelezionato(impegnoDaRipetere.getCodPrgPolReg());
	        
	        // Classificatori
	        teSupport.setClassGenSelezionato1(impegnoDaRipetere.getCodClassGen11());
	        teSupport.setClassGenSelezionato2(impegnoDaRipetere.getCodClassGen12());
	        teSupport.setClassGenSelezionato3(impegnoDaRipetere.getCodClassGen13());
	        teSupport.setClassGenSelezionato4(impegnoDaRipetere.getCodClassGen14());
	        teSupport.setClassGenSelezionato5(impegnoDaRipetere.getCodClassGen15());
	        
	        //SIOPE:
	        teSupport.setSiopeSpesaCod(impegnoDaRipetere.getCodSiope());
	        codiceSiopeChangedInternal(impegnoDaRipetere.getCodSiope());
			
		}
	}
	
	// tasto annulla che pulisce i dati della pagina
	public String annulla() throws Exception {
		GestisciImpegnoStep1Model step1Model = model.getStep1Model();
		
		step1Model.setCapitolo(new CapitoloImpegnoModel());
		step1Model.setCapitoloSelezionato(false);
		step1Model.setProvvedimento(new ProvvedimentoImpegnoModel());
		step1Model.setProvvedimentoSelezionato(false);
		step1Model.setSoggetto(new SoggettoImpegnoModel());
		step1Model.setSoggettoSelezionato(false);
		step1Model.setAnnoImpegno(null);
		step1Model.setTipoImpegno("");
		step1Model.setDescrizioneAnnoBilancio("");
		step1Model.setOggettoImpegno("");
		step1Model.setImportoImpegno(null);
		step1Model.setImportoFormattato("");
		step1Model.setScadenza("");
		step1Model.setProgetto("");
		step1Model.setAnnoFinanziamento(null);
		step1Model.setNumeroFinanziamento(null);
		step1Model.setCig("");
		step1Model.setCup("");
		step1Model.setAnnoImpOrigine(null);
		step1Model.setNumeroImpegno(null);
		step1Model.setNumImpOrigine(null);
		step1Model.setNumeroPluriennali(null);
		step1Model.setAnnoImpRiacc(null);
        step1Model.setNumImpRiacc(null);
        
        step1Model.setCronoprogramma(null);
        step1Model.setIdCronoprogramma(null);
        step1Model.setIdSpesaCronoprogramma(null);
        //SIAC-7349
        step1Model.setComponenteImpegnoCapitoloUid(null);
        
		ricaricaValoriDefaultStep1();
		// levo le righe dei vincoli
		step1Model.setListaVincoliImpegno(null);
		step1Model.setTotaleImportoDaCollegare(null);
		step1Model.setTotaleImportoVincoli(null);
		// chiudo il tab vincoli
        this.annullaValoriVincolo();
	    return SUCCESS;
	}

	public String getRipetereMovimento() {
		return ripetereMovimento;
	}


	public void setRipetereMovimento(String ripetereMovimento) {
		this.ripetereMovimento = ripetereMovimento;
	}
	
    // SIAC-6338
	@Override
	protected String controlliServiziPerProsegui(OggettoDaPopolareEnum oggettoDaPopolare) {
		String ret = super.controlliServiziPerProsegui(oggettoDaPopolare);
		
		if (oggettoDaPopolareImpegno() && model.getStep1Model().getCapitolo() != null) {
			teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
		}
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction#convertiModelPerChiamataServizioRicercaProgetto(it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel)
	 */
	@Override
	public RicercaSinteticaProgetto convertiModelPerChiamataServizioRicercaProgetto(ProgettoImpegnoModel criteriRicercaProgetto) {
		RicercaSinteticaProgetto ricercaSinteticaProgetto = super.convertiModelPerChiamataServizioRicercaProgetto(criteriRicercaProgetto);
		
		ricercaSinteticaProgetto.setProgettoModelDetails(ProgettoModelDetail.Cronoprogrammi);
		
		return ricercaSinteticaProgetto;
	}
	
	@Override
	public String ricercaProgetto() throws Exception {
		super.ricercaProgetto();
		
		model.moveListaRicercaProgettoToListaRicercaProgettoCronoprogrammi();
		
		return "ricercaProgettoCronop";
	}
	
	public String ricercaCronop() throws Exception {
		
		RicercaDeiCronoprogrammiCollegatiAlProvvedimento req = model.creaRequestRicercaCronoprogramma();
		
		req.setNumeroAttoAmm(model.getStep1Model().getProvvedimento().getNumeroProvvedimento());
		req.setAnnoAttoAmm(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
		req.setIdTipoProvvedimento(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento());
		req.setIdStrutturaAmministrativoContabile(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());

		RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse res = progettoService.ricercaDeiCronoprogrammiCollegatiAlProvvedimento(req);
		
		model.setListaRicercaCronop(res.getCronoprogrammi());
		
		return "ricercaCronop";
	}

	
	
	

}