/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaImpegnoAction extends WizardRicercaMovGestAction {
	
	
	public RicercaImpegnoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
		
	private static final String SI = "Si";
	private static final String NO = "No";
	private static final String TUTTI = "Tutti";
	
	
	private static final long serialVersionUID = 8169945661751620020L;
	
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return false;
	}

	@Override
	public boolean sonoInInserimento() {
		// ritorna true 
		// utilizza la striscia jsp di inserimento con i
		// textfield editabili
		return super.sonoInInserimento();
	}
	
	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca Impegno");

		try{
			//tipi provvedimenti:
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			if(model.getListaTipiAmbito()==null || model.getListaTipiAmbito().isEmpty()){
				caricaListaAmbiti();
			}
			
			//liste per ricerca soggetto:
			caricaListePerRicercaSoggetto();
			
			//liste per ricerca impegno:
			caricaListeRicercaImpegno();
			
			//lista da riaccertamento:
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
			setCompetenzeList(competenzeListApp);
			
			//setto l'anno esercizio come defualt di ricerca:
			model.getRicercaModel().setAnnoMovimento(sessionHandler.getAnnoEsercizio());
		}catch(Exception e){
			log.debug("prepara", e.getMessage());
		}	

	}

	
	private void caricaListaAmbiti() {
		
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);

		model.setListaTipiAmbito(response.getTipiAmbito());
	}
	
	
	/**
	 * Metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");

		resetPageNumberTableId("ricercaImpegnoID");  
		
		//Pulisco i campi
		pulisciCampi();
		
		//transazione elementare:
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//anno capitolo default:
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}

		//DEFAULT RIACCERTATO NO
		model.getStep1Model().setRiaccertato(NO);

		// durc
		model.getStep1Model().setSoggettoDurc(NO);
		
		//DEFAULT COMPETENZE TUTTI
		model.getRicercaModel().setCompetenze(TUTTI);
		
		//setto il flag da ricerca a true
		model.getStep1Model().setDaRicerca(true);
		
		//carica lista tipo finanziamento:
		if(caricaListaTipoFinanziamento()){
	   		return INPUT;
	   	}

		return SUCCESS;
	}   

	/**
	 * metodo di ricerca impegni
	 * @return
	 */
	public String ricercaImpegni(){
		setMethodName("ricercaImpegni");
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean annoExist=false;
		boolean numeroExist=false;
		
		//anno movimento
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoMovimento())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoMovimento())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno deve essere numerico"));
			} else {
				if(model.getRicercaModel().getAnnoMovimento().length() != 4){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno : yyyy"));
				} else {
					annoExist = true;
				}
			}
		}
		
		//numero movimento
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpegno())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Impegno deve essere numerico"));
			} else {
				numeroExist = true;
			}
		}

		if(!annoExist && numeroExist){
			//errore
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno "));
		}		
		
		//Controllo formato su ANNO E NUMERO Impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpOrigine())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Impegno Origine deve essere numerico"));
			}
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoImpOrigine())){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno Origine deve essere numerico"));
			} else {
				if(model.getRicercaModel().getAnnoImpOrigine().length() != 4){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno Origine : yyyy"));
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
		
		//Se esiste anno impegno origine deve esistere numero impegno origine e viceversa
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine()) && StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Impegno Origine che Numero Impegno Origine"));
		}
		if(StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine()) && !StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Impegno Origine che Numero Impegno Origine"));
		}
		
		
		//Controllo su RIACCERTATO
		if (NO.equalsIgnoreCase(model.getStep1Model().getRiaccertato())) {
	    	model.getStep1Model().setAnnoImpRiacc(null);
	    	model.getStep1Model().setNumImpRiacc(null);
		}else{
			
			//controllo formato numero riaccertato
			if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
				if(!NumberUtils.isNumber(model.getRicercaModel().getNumeroImpRiacc())){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Impegno Riaccertato deve essere numerico"));
				}
			}
			
			//controllo fornato anno riaccertato
			if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
				if(!NumberUtils.isNumber(model.getRicercaModel().getAnnoImpRiacc())){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno Riaccertato deve essere numerico"));
				} else if(model.getRicercaModel().getAnnoImpRiacc().length() != 4){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno Riaccertato : yyyy"));
				}
			}
			
			if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc()) && StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Impegno Riaccertato che Numero Impegno Riaccertato"));
			}
			if(StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc()) && !StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Devono essere presenti sia Anno Impegno Riaccertato che Numero Impegno Riaccertato"));
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
		
		//Jira 1907/1908, ricerco impegni anche x uid sac provvedimento
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
			}

			return GOTO_ELENCO_IMPEGNI;
		} else {
			addErrori(listaErrori);
			return INPUT;
		}

	}
	
	/**
	 * Gestione pulsante annulla
	 * @return
	 */
	public String annulla(){
		setMethodName("annulla");
		// pulisci i campi della ricerca
		pulisciCampi();
		return SUCCESS;
	}
	
	/**
	 * click su conferma PDC
	 */
	public String confermaPdc() {
		return SUCCESS;
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
	
	
	
}