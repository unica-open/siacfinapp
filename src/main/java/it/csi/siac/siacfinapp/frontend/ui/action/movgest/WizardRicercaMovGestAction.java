/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.RicercaImpegnoModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobal;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamento;


public class WizardRicercaMovGestAction extends WizardGenericMovGestAction {

	
	private static final long serialVersionUID = 1L;


	@Override
	public String getActionKey() {
		return "ricercaImpegno";
	}
	
	
	public boolean sonoInInserimento() {
		// ritorna true 
		// utilizza la striscia jsp di inserimento con i
		// textfield editabili
		return true;
	}
	
	private List<String> competenzeList = new ArrayList<String>();
	
	private String radioCodiceProvvedimento;
	
	/**
	 * carica le liste per ricerca impegno
	 */
	protected void caricaListeRicercaImpegno(){
		
		//carica la lista classe soggetto:
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
		
		//carica la lista stati movgest:
		Map<TipiLista, List<? extends CodificaFin>> mappaListaStatoOperativoMovgest = getCodifiche(TipiLista.STATO_MOVGEST);
		
		//setta la lista stati movgest nel model:
		model.setListaStatoOperativoMovgest((List<CodificaFin>)mappaListaStatoOperativoMovgest.get(TipiLista.STATO_MOVGEST));
		
		//setta la lista classe soggetto nel model:
		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
		
				 
	}
	
	/**
	 * Costruisce la request per invocare il servizio di ricerca
	 * @return
	 */
	protected RicercaImpegniGlobal convertiModelPerChiamataServizioRicercaImpegni() {
		
		//istanzio la request:
		RicercaImpegniGlobal ricercaImpegni = new RicercaImpegniGlobal();
		
		//richiedente:
		ricercaImpegni.setRichiedente(sessionHandler.getRichiedente());
		
		//ente
		ricercaImpegni.setEnte(sessionHandler.getAccount().getEnte());
		
		//richiedente
		ParametroRicercaImpegno parametroRicercaImpegno = new ParametroRicercaImpegno();
		
		//anno esercizio
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoEsercizio())){
			parametroRicercaImpegno.setAnnoEsercizio(Integer.valueOf(model.getRicercaModel().getAnnoEsercizio()));
		}else{
			parametroRicercaImpegno.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		}
		
		// anno bilancio e anno esercizio sono la stessa cosa
		parametroRicercaImpegno.setAnnoBilancio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//numero impegno
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			parametroRicercaImpegno.setNumeroImpegno(Integer.valueOf(model.getRicercaModel().getNumeroImpegno()));
		}
		
		//anno impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			parametroRicercaImpegno.setAnnoImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getAnnoImpOrigine()));
		}
		
		//numero impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			parametroRicercaImpegno.setNumeroImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getNumeroImpOrigine()));
		}
		
		//riaccertato
		if(!StringUtils.isEmpty(model.getStep1Model().getRiaccertato())){
			parametroRicercaImpegno.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());	
		}
		
		//anno impegno riaccertatato
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
			parametroRicercaImpegno.setAnnoImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getAnnoImpRiacc()));
		}
		
		//numero impegno riaccertato
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
			parametroRicercaImpegno.setNumeroImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getNumeroImpRiacc()));
		}
		
		//cig
		if(!StringUtils.isEmpty(model.getRicercaModel().getCig())){
			parametroRicercaImpegno.setCig(model.getRicercaModel().getCig());
		}
		
		//cup
		if(!StringUtils.isEmpty(model.getRicercaModel().getCup())){
			parametroRicercaImpegno.setCup(model.getRicercaModel().getCup());
		}
		
		//competenze
		if(!StringUtils.isEmpty(model.getRicercaModel().getCompetenze())){
			parametroRicercaImpegno.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
			parametroRicercaImpegno.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
			parametroRicercaImpegno.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());
		}
		
		//progetto
		if(!StringUtils.isEmpty(model.getRicercaModel().getProgetto())){
			parametroRicercaImpegno.setProgetto(model.getRicercaModel().getProgetto());
		}
		
		//soggetto
		if(model.getStep1Model().getSoggetto() != null){
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
				parametroRicercaImpegno.setCodiceCreditore(model.getStep1Model().getSoggetto().getCodCreditore());
			}
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getIdClasse())){
				//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
				inserisciDescClasseSoggetto();
				parametroRicercaImpegno.setCodiceClasseSoggetto(model.getStep1Model().getSoggetto().getClasse());
			}
		}

		//Gestisco Capitolo	//jira 1337 ricerca capitoli con 0
		if(model.getStep1Model().getCapitolo() != null){
			if(model.getStep1Model().getCapitolo().getUid() != null){
				parametroRicercaImpegno.setUidCapitolo(model.getStep1Model().getCapitolo().getUid());
			} else {
				if(model.getStep1Model().getCapitolo().getNumCapitolo() != null){
					parametroRicercaImpegno.setNumeroCapitolo(model.getStep1Model().getCapitolo().getNumCapitolo());
				}
				
				if(model.getStep1Model().getCapitolo().getArticolo() != null){
					parametroRicercaImpegno.setNumeroArticolo(model.getStep1Model().getCapitolo().getArticolo());
				}
				
				if(model.getStep1Model().getCapitolo().getUeb() != null){
					parametroRicercaImpegno.setNumeroUEB(model.getStep1Model().getCapitolo().getUeb().intValue());
				}
				
			}
		}
		
		//Gestisco Provvedimento
		if(model.getStep1Model().getProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getUid() != null && model.getStep1Model().getProvvedimento().getUid() != 0){
				parametroRicercaImpegno.setUidProvvedimento(model.getStep1Model().getProvvedimento().getUid());
			} else {
				if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						parametroRicercaImpegno.setAnnoProvvedimento(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					parametroRicercaImpegno.setNumeroProvvedimento(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
				}
				
				if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null && model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != 0){
					parametroRicercaImpegno.setTipoProvvedimento(String.valueOf(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()));
				}
				
				if(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null){
					
					model.getStep1Model().getProvvedimento().getUidStrutturaAmm();
					parametroRicercaImpegno.setStrutturaAmministrativoContabileDelProvvedimento(String.valueOf(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()));
				}
				
				
			}
		}
		
		//elemento piano dei conti
		if(model.getRicercaModel().getPianoDeiConti()!=null && !StringUtils.isEmpty(model.getRicercaModel().getPianoDeiConti().getCodice())){
			parametroRicercaImpegno.setElementoPianoDeiConti(model.getRicercaModel().getPianoDeiConti().getCodice());
		}
		
		//stato
		if(!StringUtils.isEmpty(model.getRicercaModel().getIdStatoOperativoMovgest())){
			parametroRicercaImpegno.setStato(model.getRicercaModel().getIdStatoOperativoMovgest());
		}
		
		//flag da riaccertamento
		parametroRicercaImpegno.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());
		
		//durc
		parametroRicercaImpegno.setFlagSoggettoDurc(model.getStep1Model().getSoggettoDurc());
		
		//competenza
		parametroRicercaImpegno.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
		
		//competenza corrente
		parametroRicercaImpegno.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
		
		//competenza futuri
		parametroRicercaImpegno.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());
		
		//setto il parametro di ricerca nella request:
		ricercaImpegni.setParametroRicercaImpegno(parametroRicercaImpegno);
		
		//dati di paginazione:
		addNumAndPageSize(ricercaImpegni, "ricercaImpegnoID");
		
		//ritorno la request al chiamante:
		return ricercaImpegni;
	}

	
	// CR - 1907 converto per preparare la reqeust del servizio RicercaSinteticaImpegniSubimpegni
	protected RicercaSinteticaImpegniSubImpegni convertiModelPerChiamataServizioRicercaImpegniSubImpegni() {
		
		RicercaSinteticaImpegniSubImpegni ricercaImpegniSubImpegni = new RicercaSinteticaImpegniSubImpegni();
		ricercaImpegniSubImpegni.setRichiedente(sessionHandler.getRichiedente());
		ricercaImpegniSubImpegni.setEnte(sessionHandler.getAccount().getEnte());
		
		ParametroRicercaImpSub paramRicercaImpSub = new ParametroRicercaImpSub();
		
		// Rm jira 2060: é fisso ed è l'anno di bilancio!
		paramRicercaImpSub.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		// Rm jira 2060: l'anno movimento è inputato dall'utente:
		// se non viene passato si considera la competenza, azzero l'anno per evitare il nullpointer
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoMovimento())){
			paramRicercaImpSub.setAnnoImpegno(Integer.valueOf(model.getRicercaModel().getAnnoMovimento()));
			
			paramRicercaImpSub.setCompetenzaCompetenza(false);
			paramRicercaImpSub.setCompetenzaCorrente(false);
			paramRicercaImpSub.setCompetenzaFuturi(false);
			
		}else{
			if(model.getRicercaModel().isCompetenzaTutti()||
					model.getRicercaModel().isCompetenzaFuturi()||
					model.getRicercaModel().isCompetenzaCompetenza()||
					model.getRicercaModel().isCompetenzaCorrente()){
				paramRicercaImpSub.setAnnoImpegno(0);
				
				paramRicercaImpSub.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
				paramRicercaImpSub.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
				paramRicercaImpSub.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());
			}
		}
		
		//numero impegno
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			paramRicercaImpSub.setNumeroImpegno(Integer.valueOf(model.getRicercaModel().getNumeroImpegno()));
		}
		
		//anno impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			paramRicercaImpSub.setAnnoImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getAnnoImpOrigine()));
		}
		
		//numero impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			paramRicercaImpSub.setNumeroImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getNumeroImpOrigine()));
		}
		
		//flag da riaccertamento
		if(!StringUtils.isEmpty(model.getStep1Model().getRiaccertato())){
			paramRicercaImpSub.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());	
		}
		
		//anno impegno riaccertato
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
			paramRicercaImpSub.setAnnoImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getAnnoImpRiacc()));
		}
		
		//numero impegno riaccertato
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
			paramRicercaImpSub.setNumeroImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getNumeroImpRiacc()));
		}
		
		//cig
		if(!StringUtils.isEmpty(model.getRicercaModel().getCig())){
			paramRicercaImpSub.setCig(model.getRicercaModel().getCig());
		}
		
		//cup
		if(!StringUtils.isEmpty(model.getRicercaModel().getCup())){
			paramRicercaImpSub.setCup(model.getRicercaModel().getCup());
		}
		
		//Progetto
		//SIAC-7144
		if(!StringUtils.isEmpty(model.getStep1Model().getProgetto())){
			paramRicercaImpSub.setProgetto(model.getStep1Model().getProgetto());
		}

		//CODICE PROGETTO:
		if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
			paramRicercaImpSub.setCodiceProgetto(model.getStep1Model().getProgetto());
		}
		
		//SIAC-7042
		//cronoprogramma
		if(!StringUtils.isEmpty(model.getStep1Model().getCronoprogramma()) && model.getStep1Model().getIdCronoprogramma() != null){
			paramRicercaImpSub.setUidCronoprogramma(model.getStep1Model().getIdCronoprogramma());
		}
		
		//soggetto
		if(model.getStep1Model().getSoggetto() != null){
			
			// Jira 2060: silvia mi conferma che se imposto il cod. soggetto non considero la classe! 
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
				paramRicercaImpSub.setCodiceCreditore(model.getStep1Model().getSoggetto().getCodCreditore());
			}else{
				
				if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getIdClasse())){
					inserisciDescClasseSoggetto();
					//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
					paramRicercaImpSub.setCodiceClasseSoggetto(model.getStep1Model().getSoggetto().getClasse());
				}
			}
		}

		//Gestisco Capitolo	//jira 1337 ricerca capitoli con 0
		if(model.getStep1Model().getCapitolo() != null){
			if(model.getStep1Model().getCapitolo().getUid() != null){
				paramRicercaImpSub.setUidCapitolo(model.getStep1Model().getCapitolo().getUid());
			} else {
				if(model.getStep1Model().getCapitolo().getNumCapitolo() != null){
					paramRicercaImpSub.setNumeroCapitolo(model.getStep1Model().getCapitolo().getNumCapitolo());
				}
				
				if(model.getStep1Model().getCapitolo().getArticolo() != null){
					paramRicercaImpSub.setNumeroArticolo(model.getStep1Model().getCapitolo().getArticolo());
				}
				
				if(model.getStep1Model().getCapitolo().getUeb() != null){
					paramRicercaImpSub.setNumeroUEB(model.getStep1Model().getCapitolo().getUeb().intValue());
				}
				
			}
		}
		
		//Gestisco Provvedimento
		if(model.getStep1Model().getProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getUid() != null && model.getStep1Model().getProvvedimento().getUid() != 0){
				paramRicercaImpSub.setUidProvvedimento(model.getStep1Model().getProvvedimento().getUid());
			} else {
				if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						paramRicercaImpSub.setAnnoProvvedimento(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					paramRicercaImpSub.setNumeroProvvedimento(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
				}
				
				if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null && model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != 0){
					
					//passo al server il tipo provvedimento completo non solo di uid
					List<TipoAtto> listaTipoProvvedimento =  model.getListaTipiProvvedimenti();
					for (TipoAtto tipoAtto : listaTipoProvvedimento) {
						if(tipoAtto.getUid() == model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()){
							paramRicercaImpSub.setTipoProvvedimento(tipoAtto);
							break;
						}
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null){
					paramRicercaImpSub.setUidStrutturaAmministrativoContabile(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());
				}
				
			}
		}

		//stato
		if(!StringUtils.isEmpty(model.getRicercaModel().getIdStatoOperativoMovgest())){
			paramRicercaImpSub.setStato(model.getRicercaModel().getIdStatoOperativoMovgest());
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.getRicercaModel().setEscludiAnnullati(model.getRicercaModel().getHiddenPerEscludiAnnullati());
		if(model.getRicercaModel().getEscludiAnnullati()!=null && model.getRicercaModel().getEscludiAnnullati()==true){
			List<String> statiDaEscludere = new ArrayList<String>();
			statiDaEscludere.add("A");
			paramRicercaImpSub.setStatiDaEscludere(statiDaEscludere);
		}
		//
		
		//flag da riaccertamento
		paramRicercaImpSub.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());

		//flag da riaccertamento
		paramRicercaImpSub.setFlagSoggettoDurc(model.getStep1Model().getSoggettoDurc());

		//ricerca da impegno
		paramRicercaImpSub.setIsRicercaDaImpegno(true);
		
		//setto il parametro nella request:
		ricercaImpegniSubImpegni.setParametroRicercaImpSub(paramRicercaImpSub);
		
		//dati per la paginazione dei risultati:
		addNumAndPageSize(ricercaImpegniSubImpegni, "ricercaImpegnoID");
		
		
		//ritorno la request al chiamante:
		return ricercaImpegniSubImpegni;
	}
	
	
	protected RicercaAccertamenti convertiModelPerChiamataServizioRicercaAccertamenti() {
	
		//istanzio la request:
		RicercaAccertamenti ricercaAccertamenti = new RicercaAccertamenti();
		RicercaAccertamento parametro = new RicercaAccertamento();
		
		//richiedente
		ricercaAccertamenti.setRichiedente(sessionHandler.getRichiedente());
		
		//anno bilancio
		parametro.setAnnoBilancio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//anno esercizio
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoEsercizio())){
			parametro.setAnnoEsercizio(Integer.valueOf(model.getRicercaModel().getAnnoEsercizio()));
		}
		
		//numero impegno
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			parametro.setNumeroImpegno(Integer.valueOf(model.getRicercaModel().getNumeroImpegno()));
		}
		
		//anno impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			parametro.setAnnoImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getAnnoImpOrigine()));
		}
		
		//numero impegno origine
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			parametro.setNumeroImpegnoOrigine(Integer.valueOf(model.getRicercaModel().getNumeroImpOrigine()));
		}
		
		//flag da riaccertamento
		if(!StringUtils.isEmpty(model.getStep1Model().getRiaccertato())){
			parametro.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());	
		}
		
		//anno impegno riaccertato
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
			parametro.setAnnoImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getAnnoImpRiacc()));
		}
		
		//numero impegno riaccertato
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
			parametro.setNumeroImpegnoRiaccertato(Integer.valueOf(model.getRicercaModel().getNumeroImpRiacc()));
		}
		
		//cig
		if(!StringUtils.isEmpty(model.getRicercaModel().getCig())){
			parametro.setCig(model.getRicercaModel().getCig());
		}
		
		//cup
		if(!StringUtils.isEmpty(model.getRicercaModel().getCup())){
			parametro.setCup(model.getRicercaModel().getCup());
		}
		
		//competenze
		if(!StringUtils.isEmpty(model.getRicercaModel().getCompetenze())){
			parametro.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
			parametro.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
			parametro.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());
		}
		
		
		//progetto
		if(!StringUtils.isEmpty(model.getRicercaModel().getProgetto())){
			parametro.setProgetto(model.getRicercaModel().getProgetto());
		}
		
		//Gestiscio Soggetto
		if(model.getStep1Model().getSoggetto() != null){
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
				parametro.setCodiceCreditore(model.getStep1Model().getSoggetto().getCodCreditore());
			}
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getIdClasse())){
				//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
				inserisciDescClasseSoggetto();
				parametro.setCodiceClasseSoggetto(model.getStep1Model().getSoggetto().getClasse());
			}
		}
		
		//Gestisco Capitolo	//jira 1338 ricerca capitolo con 0
		if(model.getStep1Model().getCapitolo() != null){
			if(model.getStep1Model().getCapitolo().getUid() != null){
				parametro.setUidCapitolo(model.getStep1Model().getCapitolo().getUid());
			} else {
				if(model.getStep1Model().getCapitolo().getNumCapitolo() != null){
					parametro.setNumeroCapitolo(model.getStep1Model().getCapitolo().getNumCapitolo());
				}
				
				if(model.getStep1Model().getCapitolo().getArticolo() != null){
					parametro.setNumeroArticolo(model.getStep1Model().getCapitolo().getArticolo());
				}
				
				if(model.getStep1Model().getCapitolo().getUeb() != null){
					parametro.setNumeroUEB(model.getStep1Model().getCapitolo().getUeb().intValue());
				}
				
			}
		}
		
		//Gestisco Provvedimento
		if(model.getStep1Model().getProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getUid() != null && model.getStep1Model().getProvvedimento().getUid() != 0){
				parametro.setUidProvvedimento(model.getStep1Model().getProvvedimento().getUid());
			} else {
				if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						parametro.setAnnoProvvedimento(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					parametro.setNumeroProvvedimento(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
				}
				
				if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null && model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != 0){
					parametro.setTipoProvvedimento(String.valueOf(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()));
				}
				
				
				if(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null){
					model.getStep1Model().getProvvedimento().getUidStrutturaAmm();
					parametro.setStrutturaAmministrativoContabileDelProvvedimento(String.valueOf(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()));
				}
				
			}
		}
		
		//elemento piano dei conti
		if(model.getRicercaModel().getPianoDeiConti()!=null && !StringUtils.isEmpty(model.getRicercaModel().getPianoDeiConti().getCodice())){
			parametro.setElementoPianoDeiConti(model.getRicercaModel().getPianoDeiConti().getCodice());
		}
		
		//stato
		if(!StringUtils.isEmpty(model.getRicercaModel().getIdStatoOperativoMovgest())){
			parametro.setStato(model.getRicercaModel().getIdStatoOperativoMovgest());
		}
		
		//flag da riaccertamento
		parametro.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());
		
		//competenza
		parametro.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
		
		//competenza corrente
		parametro.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
		
		//competenza futuri
		parametro.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());

		//setto il parametro nella request:
		ricercaAccertamenti.setpRicercaAccertamento(parametro);
		
		//dati per la paginazione:
		addNumAndPageSize(ricercaAccertamenti, "ricercaAccertamentoID");
		
		//ritorno la request al chiamante:
		return ricercaAccertamenti;
	}
	
	
	
	/**
	 * Esegue i controlli di validità dei parametri di ricerca , 
	 * e imposta la request
	 * @return
	 */
	protected RicercaSinteticaAccertamentiSubAccertamenti convertiModelPerChiamataServizioRicercaAccertamentiSub() {
		RicercaSinteticaAccertamentiSubAccertamenti ricercaAccertamenti = new RicercaSinteticaAccertamentiSubAccertamenti();
		ricercaAccertamenti.setRichiedente(sessionHandler.getRichiedente());
		ricercaAccertamenti.setEnte(sessionHandler.getAccount().getEnte());
		
		ParametroRicercaAccSubAcc parametro = new ParametroRicercaAccSubAcc();
		
		// anno esercizio = anno bilancio
		parametro.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		// Rm jira 2060: l'anno movimento è inputato dall'utente:
		// se non viene passato si considera la competenza, azzero l'anno per evitare il nullpointer
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoMovimento())){
			parametro.setAnnoAccertamento(Integer.valueOf(model.getRicercaModel().getAnnoMovimento()));
			
			parametro.setCompetenzaCompetenza(false);
			parametro.setCompetenzaCorrente(false);
			parametro.setCompetenzaFuturi(false);
		}else{
			if(model.getRicercaModel().isCompetenzaTutti()||
					model.getRicercaModel().isCompetenzaFuturi()||
					model.getRicercaModel().isCompetenzaCompetenza()||
					model.getRicercaModel().isCompetenzaCorrente()){
				parametro.setAnnoAccertamento(0);
				
				parametro.setCompetenzaCompetenza(model.getRicercaModel().isCompetenzaCompetenza());
				parametro.setCompetenzaCorrente(model.getRicercaModel().isCompetenzaCorrente());
				parametro.setCompetenzaFuturi(model.getRicercaModel().isCompetenzaFuturi());
				
			}
		}
		
		//PROGETTO:
		if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
			parametro.setCodiceProgetto(model.getStep1Model().getProgetto());
		}
		
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpegno())){
			parametro.setNumeroAccertamento(Integer.valueOf(model.getRicercaModel().getNumeroImpegno()));// in realtà è accertamento
		}
		
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpOrigine())){
			parametro.setAnnoAccertamentoOrigine(Integer.valueOf(model.getRicercaModel().getAnnoImpOrigine()));
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpOrigine())){
			parametro.setNumeroAccertamentoOrigine(Integer.valueOf(model.getRicercaModel().getNumeroImpOrigine()));
		}
		if(!StringUtils.isEmpty(model.getStep1Model().getRiaccertato())){
			parametro.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());	
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getAnnoImpRiacc())){
			parametro.setAnnoAccertamentoRiaccertato(Integer.valueOf(model.getRicercaModel().getAnnoImpRiacc()));
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getNumeroImpRiacc())){
			parametro.setNumeroAccertamentoRiaccertato(Integer.valueOf(model.getRicercaModel().getNumeroImpRiacc()));
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getCig())){
			parametro.setCig(model.getRicercaModel().getCig());
		}
		if(!StringUtils.isEmpty(model.getRicercaModel().getCup())){
			parametro.setCup(model.getRicercaModel().getCup());
		}
		
				
		//Gestiscio Soggetto
		if(model.getStep1Model().getSoggetto() != null){
			if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
				parametro.setCodiceDebitore(model.getStep1Model().getSoggetto().getCodCreditore());
			}else{
				if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getIdClasse())){
					//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
					inserisciDescClasseSoggetto();
					parametro.setCodiceClasseSoggetto(model.getStep1Model().getSoggetto().getClasse());
				}
			}
		}
		
		//Gestisco Capitolo	//jira 1338 ricerca capitolo con 0
		if(model.getStep1Model().getCapitolo() != null){
			if(model.getStep1Model().getCapitolo().getUid() != null){
				parametro.setUidCapitolo(model.getStep1Model().getCapitolo().getUid());
			} else {
				if(model.getStep1Model().getCapitolo().getNumCapitolo() != null){
					parametro.setNumeroCapitolo(model.getStep1Model().getCapitolo().getNumCapitolo());
				}
				
				if(model.getStep1Model().getCapitolo().getArticolo() != null){
					parametro.setNumeroArticolo(model.getStep1Model().getCapitolo().getArticolo());
				}
				
				if(model.getStep1Model().getCapitolo().getUeb() != null){
					parametro.setNumeroUEB(model.getStep1Model().getCapitolo().getUeb().intValue());
				}
				
			}
		}
		
		//Gestisco Provvedimento
		if(model.getStep1Model().getProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getUid() != null && model.getStep1Model().getProvvedimento().getUid() != 0){
				parametro.setUidProvvedimento(model.getStep1Model().getProvvedimento().getUid());
			} else {
				if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						parametro.setAnnoProvvedimento(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					parametro.setNumeroProvvedimento(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
				}
				
				if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null && model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != 0){
					
					//passo al server il tipo provvedimento completo non solo di uid
					List<TipoAtto> listaTipoProvvedimento =  model.getListaTipiProvvedimenti();
					for (TipoAtto tipoAtto : listaTipoProvvedimento) {
						if(tipoAtto.getUid() == model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()){
							parametro.setTipoProvvedimento(tipoAtto);
							break;
						}
					}
				}
				
				if(model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null){
					parametro.setUidStrutturaAmministrativoContabile(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());
				}
				
			}
		}
		
		// Stato		
		if(!StringUtils.isEmpty(model.getRicercaModel().getIdStatoOperativoMovgest())){
			parametro.setStato(model.getRicercaModel().getIdStatoOperativoMovgest());
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.getRicercaModel().setEscludiAnnullati(model.getRicercaModel().getHiddenPerEscludiAnnullati());
		if(model.getRicercaModel().getEscludiAnnullati()!=null && model.getRicercaModel().getEscludiAnnullati()==true){
			List<String> statiDaEscludere = new ArrayList<String>();
			statiDaEscludere.add("A");
			parametro.setStatiDaEscludere(statiDaEscludere);
		}
		//
		
		parametro.setFlagDaRiaccertamento(model.getStep1Model().getRiaccertato());
		
		parametro.setIsRicercaDaAccertamento(true);
		ricercaAccertamenti.setParametroRicercaAccSubAcc(parametro);
		addNumAndPageSize(ricercaAccertamenti, "ricercaAccertamentoID");
		
		return ricercaAccertamenti;
	}
	
	protected void pulisciCampi(){
		model.setRicercaModel(new RicercaImpegnoModel());
		model.setStep1Model(new GestisciImpegnoStep1Model());
		
		List<String> listaDaRiaccertamento = new ArrayList<String>();
		listaDaRiaccertamento.add("Si");
		listaDaRiaccertamento.add("No");
		model.getStep1Model().setDaRiaccertamento(listaDaRiaccertamento);
		model.getRicercaModel().setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
		model.getRicercaModel().setAnnoMovimento(sessionHandler.getAnnoEsercizio());
		if(teSupport!=null){
			teSupport=null;
		}
	
	}
	
	/**
	 * Si occupa di effettuare la ricerca degli accertamenti
	 * @return
	 */
	protected String executeRicercaAccertamenti() {

		List<Accertamento> listaCompleta = new ArrayList<Accertamento>();
	
		/*
		 *  INIZIO - STOPWATCH
		 */
		
	    StopWatch stopwatch = new StopWatch("stopWatchCategory");
		stopwatch.start();
		
		// CR- 1908 RM Sostiuire la ricerca accertamenti 
		//con la ricerca accertamentiSubAccertamenti,
		// cosi come si e' x i mutui
		RicercaSinteticaAccertamentiSubAccertamentiResponse response = movimentoGestionService.ricercaSinteticaAccertamentiSubAccertamenti(convertiModelPerChiamataServizioRicercaAccertamentiSub());

		stopwatch.stop();
		stopWatchLogger.info(this.getClass().getName(), methodName,  stopwatch.getTotalTimeMillis());
		
		/*
		 *  FINE - STOPWATCH
		 */
		
		if(response.isFallimento()){
			addErrori(methodName, response);
			return INPUT;
		}
		
		
		//Metto in sessione la lista ricevuta 
		listaCompleta = (response.getListaAccertamenti()!=null? response.getListaAccertamenti() : new ArrayList<Accertamento>());
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI, listaCompleta);
		model.setResultSize(response.getNumRisultati());
		
		return GOTO_ELENCO_ACCERTAMENTI;
		    
	}
	
	/**
	 * Si occupa di effettuare la ricerca degli impegni
	 * @return
	 */
	protected String executeRicercaImpegni() {

		List<Impegno> listaCompleta = new ArrayList<Impegno>();
	
		/*
		 *  INIZIO - STOPWATCH
		 */
		//
	    StopWatch stopwatch = new StopWatch("stopWatchCategory");
		stopwatch.start();
		
		// CR- 1907 Sostiuire la ricerca impegni con la ricerca impegni subimpegni, cosi come si fa x i mutui
		RicercaSinteticaImpegniSubimpegniResponse response = movimentoGestionService.ricercaSinteticaImpegniSubimpegni(convertiModelPerChiamataServizioRicercaImpegniSubImpegni());
		
		stopwatch.stop();
		stopWatchLogger.info(this.getClass().getName(), "executeRicercaImpegniSubImpegni",  stopwatch.getTotalTimeMillis());
		
		/*
		 *  FINE - STOPWATCH
		 */
		
		if(response.isFallimento()){
			addErrori(methodName, response);
			return INPUT;
		}
		
		listaCompleta = (response.getListaImpegni()!=null? response.getListaImpegni() : new ArrayList<Impegno>());
		
		//Metto in sessione la lista ricevuta
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI, listaCompleta);
		model.setResultSize(response.getNumRisultati());
		
		return GOTO_ELENCO_IMPEGNI;
		    
	}
	
	
	
	protected CapitoloEntrataGestione ricercaCapitoloEntrata(Integer uidCapitoloEntrata){
		
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		boolean cache = false;
		
		//Controllo che non sia presente nella lista temporanea dei CapitoliUscita nella model
		for(CapitoloEntrataGestione app : model.getCapitoliEntrata()){
			if(app.getUid() == uidCapitoloEntrata){
				capitolo = app;
				cache = true;
			}
		}
		
		if(!cache){
			RicercaDettaglioCapitoloEntrataGestione ricercaDettaglio = new RicercaDettaglioCapitoloEntrataGestione();
			RicercaDettaglioCapitoloEGest dettaglio = new RicercaDettaglioCapitoloEGest();
			
			dettaglio.setChiaveCapitolo(uidCapitoloEntrata);
			ricercaDettaglio.setRicercaDettaglioCapitoloEGest(dettaglio);
			ricercaDettaglio.setEnte(sessionHandler.getEnte());
			ricercaDettaglio.setRichiedente(sessionHandler.getRichiedente());
			
			RicercaDettaglioCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(ricercaDettaglio);
			
			if(response.getCapitoloEntrataGestione() != null){
				capitolo = response.getCapitoloEntrataGestione();
				
				//Inserisco nella cache
				model.getCapitoliEntrata().add(response.getCapitoloEntrataGestione());
			}
			
		}
		
		return capitolo;
	}
	
	
	protected CapitoloUscitaGestione ricercaCapitoloUscita(Integer uidCapitoloUscita){
		
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		boolean cache = false;
		
		//Controllo che non sia presente nella lista temporanea dei CapitoliUscita nella model
		for(CapitoloUscitaGestione app : model.getCapitoliUscita()){
			if(app.getUid() == uidCapitoloUscita){
				capitolo = app;
				cache = true;
			}
		}
		
		if(!cache){
			RicercaDettaglioCapitoloUscitaGestione ricercaDettaglio = new RicercaDettaglioCapitoloUscitaGestione();
			RicercaDettaglioCapitoloUGest dettaglio = new RicercaDettaglioCapitoloUGest();
			
			dettaglio.setChiaveCapitolo(uidCapitoloUscita);
			ricercaDettaglio.setRicercaDettaglioCapitoloUGest(dettaglio);
			ricercaDettaglio.setEnte(sessionHandler.getEnte());
			ricercaDettaglio.setRichiedente(sessionHandler.getRichiedente());
			
			RicercaDettaglioCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglio);
			
			if(response.getCapitoloUscita() != null){
				capitolo = response.getCapitoloUscita();
				
				//Inserisco nella cache
				model.getCapitoliUscita().add(response.getCapitoloUscita());
			}
			
		}
		
		return capitolo;
	}
	
	
	
	protected AttoAmministrativo ricercaAttoAmministrativo(Integer uidAtto){
		
		AttoAmministrativo atto = new AttoAmministrativo();
		boolean cache = false;
		
		for(AttoAmministrativo app : model.getAtti()){
			if(app.getUid() == uidAtto){
				atto = app;
				cache = true;
			}
		}
		
		if(!cache){
			RicercaProvvedimento provvedimento = new RicercaProvvedimento();
			RicercaAtti atti = new RicercaAtti();
			
			atti.setUid(uidAtto);
			provvedimento.setRicercaAtti(atti);
			provvedimento.setEnte(sessionHandler.getEnte());
			provvedimento.setRichiedente(sessionHandler.getRichiedente());
			
			RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(provvedimento);
			
			if(response.getListaAttiAmministrativi() != null && response.getListaAttiAmministrativi().size() > 0){
				atto = response.getListaAttiAmministrativi().get(0);
			
				//inserisco nella cache
				model.getAtti().add(response.getListaAttiAmministrativi().get(0));
			}
			
		}
		
		return atto;
	}


	public List<String> getCompetenzeList() {
		return competenzeList;
	}

	public void setCompetenzeList(List<String> competenzeList) {
		this.competenzeList = competenzeList;
	}


	public String getRadioCodiceProvvedimento() {
		return radioCodiceProvvedimento;
	}


	public void setRadioCodiceProvvedimento(String radioCodiceProvvedimento) {
		this.radioCodiceProvvedimento = radioCodiceProvvedimento;
	}

}
