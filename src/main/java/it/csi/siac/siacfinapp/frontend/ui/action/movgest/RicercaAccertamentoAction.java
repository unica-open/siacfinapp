/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaAccertamentoAction extends WizardRicercaMovGestAction {

	
	private static final String SI = "Si";
	private static final String NO = "No";
	private static final String TUTTI = "Tutti";

	private static final long serialVersionUID = -5992797230767370649L;

	/**
	 * per verificare l'abilitazione all'inserimento di un provvedimento.
	 * 
	 */
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		//ritoriniamo false, in questo scenario non e' previsto
		return false;
	}
	
	/**
	 * Costruttore di classe
	 */
	public RicercaAccertamentoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
	@Override
	public boolean sonoInInserimento() {
		// ritorna true 
		// utilizza la striscia jsp di inserimento con i
		// textfield editabili
		return super.sonoInInserimento();
	}
	
	/**
	 * prepare di classe
	 */
	@Override
	public void prepare() throws Exception {
		//info per debug:
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca Accertamento");
		
		try{
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				//ricarico la lista tipi:
				caricaTipiProvvedimenti();
			}
			//liste varie:
			caricaListePerRicercaSoggetto();
			caricaListeRicercaImpegno();
			//lista da riaccertameno:
			List<String> listaDaRiaccertamento = new ArrayList<String>();
			listaDaRiaccertamento.add(SI);
			listaDaRiaccertamento.add(NO);
			model.getStep1Model().setDaRiaccertamento(listaDaRiaccertamento);
			//lista competenze:
			List<String> competenzeListApp = new ArrayList<String>();
			competenzeListApp.add("Tutti");
			competenzeListApp.add("Residui");
			competenzeListApp.add("Correnti");
			competenzeListApp.add("Futuri");
			competenzeListApp.add("Residui ROR");
			setCompetenzeList(competenzeListApp);
			model.getRicercaModel().setAnnoMovimento(sessionHandler.getAnnoEsercizio()); // qui è l'accertamento
		}catch(Exception e){
			log.debug("prepara", e.getMessage());
		}
		
		//SIAC-6997
		List<String> listaDaReanno = new ArrayList<String>();
		listaDaReanno.add(SI);
		listaDaReanno.add(NO);
		model.getStep1Model().setDaReanno(listaDaReanno);

	}
	
	/**
	 * metodo che disabilita il caricamento degli alberi inutili per questo scenario
	 */
	private void azzeraRicaricheAlberi(){
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		//questo serve nelle ricerca guidate
		teSupport.setRicaricaStrutturaAmministrativa(true);
		teSupport.setRicaricaSiopeSpesa(false);
	}
	
	/**
	 * execute di classe
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");

		
		//SIAC-6997
		model.setRicercaTipoROR(false);
		//SIAC-7704
		model.setSkipControlloBloccoRagioneria(true);
//		if(
//			(sessionHandler.getAzioneRichiesta()!= null && sessionHandler.getAzioneRichiesta().getAzione()!= null && sessionHandler.getAzioneRichiesta().getAzione().getNome()!= null
//			&& (sessionHandler.getAzioneRichiesta().getAzione().getNome().equals(AzioniConsentite.LEGGI_ACC_ROR_DECENTRATO.getNomeAzione())||
//			sessionHandler.getAzioneRichiesta().getAzione().getNome().equals(AzioniConsentite.LEGGI_ACC_ROR.getNomeAzione()))) //ROR DECENTRATO ACTION
//			){
//			//AZIONE RICERCA ROR
//			if(CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(sessionHandler.getFaseBilancio())){ // PREDISPOSIZIONE CONSUNTIVO)
//					model.setRicercaTipoROR(true);
//				}
//				else{
//					return "erroreRicercaFase";
//				}
//		}
		
		
		setAzioniDecToCheck(AzioneConsentitaEnum.OP_ENT_gestAccROR.getNomeAzione()+ ","+AzioneConsentitaEnum.OP_ENT_gestAccRORdecentrato.getNomeAzione()); //SOLO PER AZIONI ROR
		if( checkAzioniDec() 
				&& CostantiFin.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(sessionHandler.getFaseBilancio())	){ //SOLO PER PREDISPOSIZIONE CONSUNTIVO  
			model.setRicercaTipoROR(true);
		}
		
		
		
		
		resetPageNumberTableId("ricercaAccertamentoID");
		
		//Pulisco i campi
		pulisciCampi();
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		azzeraRicaricheAlberi();
		
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}

		model.getStep1Model().setRiaccertato(NO);
		model.getRicercaModel().setCompetenze(TUTTI);
		model.getRicercaModel().setPianoDeiConti(null);
		model.getStep1Model().setDaRicerca(true);
		
		//jira 820
		if (caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}
		
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		azzeraRicaricheAlberi();
		
		//SIAC-6997
		model.getStep1Model().setReanno(NO);
		
		return SUCCESS;
	}
	
	/**
	 * cerca l'accertamento
	 * @return
	 */
	public String ricercaAccertamento(){
		//info per debug:
		setMethodName("ricercaAccertamento");
		
		//istanzio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean annoExist=false;
		boolean numeroExist=false;
		
		//verifichiamo l'anno indicato:
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoMovimento())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoMovimento())){
				//non e' numerico
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento deve essere numerico"));
			} else {
				if(model.getRicercaModel().getAnnoMovimento().length() != 4){
					//formato non valido
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento : yyyy"));
				} else {
					annoExist = true;
				}
			}
		}
		
		//verifichiamo il numero indicato:
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpegno())){
				//formato non valido
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Accertamento deve essere numerico"));
			} else {
				numeroExist = true;
			}
		}

		if(!annoExist && numeroExist){
			//errore per numero indicato ma anno assente
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento "));
		}
		
		//numero acc origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpOrigine())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Accertamento Origine deve essere numerico"));
			}
		}
		
		//anno acc origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoImpOrigine())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento Origine deve essere numerico"));
			} else {
				if(model.getRicercaModel().getAnnoImpOrigine().length() != 4){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento Origine : yyyy"));
				}
			}
		}
		
		EsistenzaProgettoResponse esistenzaResp = new EsistenzaProgettoResponse();
	    // verifico l'esistenza del PROGETTO
	    if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
	    	// verifica PROGETTO
			// metodo che verifica il progetto
			esistenzaResp = esistenzaProgetto();
			if(esistenzaResp.getErrori()!=null && !esistenzaResp.getErrori().isEmpty()){
				listaErrori.addAll(esistenzaResp.getErrori());
			}
	    }
		
		
	    //ACCERTAMENTO RIACCERTATO:
		if (NO.equalsIgnoreCase(model.getStep1Model().getRiaccertato())) {
			model.getStep1Model().setAnnoImpRiacc(null);
	    	model.getStep1Model().setNumImpRiacc(null);
		}else{
			if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
				if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpRiacc())){
					//non e' numerico
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Accertamento Riaccertato deve essere numerico"));
				}
			}
			if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
				if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoImpRiacc())){
					//formato non valido
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento Riaccertato deve essere numerico"));
				} else {
					if(model.getRicercaModel().getAnnoImpRiacc().length() != 4){
						//formato non valido
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Accertamento Riaccertato : yyyy"));
					}
	
				}
			}
			
			//Se esiste anno imp origine deve esistere numero imp origine e viceversa
			if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine()) && StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Accertamento Origine che Numero Accertamento Origine"));
			}
			if(StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine()) && !StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Accertamento Origine che Numero Accertamento Origine"));
			}
		}
		

		//Condizioni ricerca - Provvedimento
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()!=null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue()>0 ){
			if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento()==null && 
					model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()==null){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero o Tipo Provvedimento obbligatorio con Anno Provvedimento"));
			}
		}

		if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento()!=null || 
				model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()!=null){
			if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()==null || model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue()==0 ){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento obbligatorio con Numero o Tipo Provvedimento"));
			}
		}
		
		//Jira 1907/1908, ricerco acertamenti anche x uid sac
		if(!StringUtils.isEmpty(strutturaSelezionataSuPagina)){
			model.getStep1Model().getProvvedimento().setUidStrutturaAmm(Integer.parseInt(strutturaSelezionataSuPagina));
		}

		//VALIDO
		if(listaErrori.isEmpty()){
			//Controllo il tipo di competenza da ricercare
			if(StringUtils.isEmpty(model.getRicercaModel().getCompetenze())){
				model.getRicercaModel().setCompetenzaTutti(true);
			} else {
				if(model.getRicercaModel().getCompetenze().equalsIgnoreCase("Correnti")){
					model.getRicercaModel().setCompetenzaCorrente(true);
				}
				if(model.getRicercaModel().getCompetenze().equalsIgnoreCase("Futuri")){
					model.getRicercaModel().setCompetenzaFuturi(true);
				}
				if(model.getRicercaModel().getCompetenze().equalsIgnoreCase("Residui")){
					model.getRicercaModel().setCompetenzaCompetenza(true);
				}
				if(model.getRicercaModel().getCompetenze().equalsIgnoreCase("Tutti")){
					model.getRicercaModel().setCompetenzaTutti(true);
				}
				//SIAC-6997
				if(model.getRicercaModel().getCompetenze().equalsIgnoreCase("Residui ROR")){
					model.getRicercaModel().setCompetenzaResiduiRor(true);
				}
			}


			return GOTO_ELENCO_ACCERTAMENTI;
		} else {
			addErrori(listaErrori);
			return INPUT;
		}

	}
	
	/**
	 * Conferma Piano Dei Conti
	 */
	public String confermaPdc() {
		model.getRicercaModel().setPianoDeiConti(teSupport.getPianoDeiConti());
		return SUCCESS;
	}
	
}
