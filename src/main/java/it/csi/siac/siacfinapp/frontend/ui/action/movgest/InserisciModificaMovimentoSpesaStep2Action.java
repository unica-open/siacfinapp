/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.RespectBinding;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinActionUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessivi;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessiviResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaStep2Action extends ActionKeyAggiornaImpegno {

	private static final long serialVersionUID = 379322901507966005L;

	public int campoPerPulire;
	
	
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Importo - Impegni pluriennali");
		
		//imposto il tipo di oggetto trattato:
		oggettoDaPopolare= OggettoDaPopolareEnum.IMPEGNO;
	}

	@Override
	public String execute() throws Exception {
		setMethodName("execute");
		
		//1. popolo i plurienalli nel model:
		popolaPluriennaliNelModel();
		
		//2. Setto l'importo formattato:
		model.getMovimentoSpesaModel().setImportoTotFormatted(convertiBigDecimalToImporto(model.getMovimentoSpesaModel().getImportoTot()));
		
		return SUCCESS;
	}
	
	public String salvaImpegniPluriennali(){
		setMethodName("salvaImpegniPluriennali");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		BigDecimal totaleImporti = new BigDecimal(0);
		List<ImpegniPluriennaliModel> listadefinitiva = new ArrayList<ImpegniPluriennaliModel>();
		
		if(model.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && model.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : model.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				
				//converto l'importo:
				BigDecimal importoBD=FinActionUtils.convertiImportiPluriennaleString(currentImpegnoPluriennale);
				
				//non deve essere minore del 1900:
				if (currentImpegnoPluriennale.getAnnoImpPluriennale()<=1900) {
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno",currentImpegnoPluriennale.getAnnoImpPluriennale(),">1900"));
				}

				//Controlli date
				listaErrori = ValidationUtils.validaDataScadenzaPluriennale(currentImpegnoPluriennale);
				
				//SIAC-6990
				//Controllo Importi
				totaleImporti = totaleImporti.add(validaImporto(currentImpegnoPluriennale, listadefinitiva, listaErrori));
				
				//Controllo disponibilita' capitolo pluriennali
				if (importoBD.compareTo(BigDecimal.ZERO)==1 && listaErrori.isEmpty()) {
					try {
						if (controlloImportiImpegniPlur(currentImpegnoPluriennale.getAnnoImpPluriennale(), importoBD)) {
							listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNI_PLUR.getErrore("Inserimento", String.valueOf(currentImpegnoPluriennale.getAnnoImpPluriennale())));
						}
					} catch (Exception e) {
						listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNI_PLUR.getErrore("Inserimento", String.valueOf(currentImpegnoPluriennale.getAnnoImpPluriennale())));
					}
				}

			}
			
		}
		
		//controllo distribuzione importi:
		listaErrori = controlloDistribuzioneImporti(totaleImporti, listaErrori);
		
		//verifichiamo la presenza di errori:
		if(listaErrori.isEmpty()){
			boolean fallimento = false;
			
			//gestione dei pluriennali:
			List<Impegno> listaPluriennali = componiListaPluriennali(listadefinitiva);
			
			List<Messaggio> listMess = null;
			
			if(listaPluriennali != null && listaPluriennali.size() > 0){
				// ci sono pluriennali

				//SIAC-6990
				//si sospende il precedente metodo di 
				//invocazione del servizio di inserimento:
//				InserisceImpegniResponse response = invocazioneServizioInserimento();
				
				//analizziamo la response:
//				fallimento = analizzaResponse(response);
				//
				
				//SIAC-6990
				InserisceImpegni inserisceImp = convertiModelCustomPerModificaSpesaInRequest(model.getImpegnoInAggiornamento(), model);
				
				ModificaImportoImpegnoSuAnniSuccessivi modificaImportoImpegnoSuAnniSuccessivi = new ModificaImportoImpegnoSuAnniSuccessivi();
				modificaImportoImpegnoSuAnniSuccessivi.setAggiornaImpegnoPadre(model.getImpegnoRequestStep1());
				modificaImportoImpegnoSuAnniSuccessivi.setRichiedente(model.getRichiedente());
				modificaImportoImpegnoSuAnniSuccessivi.setInserisciImpegniRequest(inserisceImp);
				modificaImportoImpegnoSuAnniSuccessivi.setAnnoBilancio(model.getAnnoImpegno());
				
				ModificaImportoImpegnoSuAnniSuccessiviResponse response = 
						movimentoGestionService.modificaImportoImpegnoSuAnniSuccessivi(modificaImportoImpegnoSuAnniSuccessivi);
				
				if(response.isFallimento() || response.getErrori().size() > 0) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response.getErrori());
					
					fallimento = true;
					listaErrori = response.getErrori();
				} else {
					listMess = response.getListaMessaggi();
				}
				//
			}
			
			if(fallimento) {
				addErrori(listaErrori);
				return INPUT;
			}
			
			//SIAC-6990
			if(listMess != null) {
				addMessaggi(listMess);
				return SUCCESS;
			}
			
			return GOTO_ELENCO_MODIFICHE;
		
		} else {
			// ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
	}
	
	/**
	 * Semplice metodo per mettere a fattore comune 
	 * il codice per invocare il servizio tra i metodi:
	 * 
	 * salvaImpegniPluriennali
	 * e 
	 * salva
	 * 
	 * @return
	 */
	private InserisceImpegniResponse invocazioneServizioInserimento(){
		ImportiCapitoloUG importiCapitoloUGApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG();
		
		model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().setImportiCapitoloUG(null);
		model.getImpegnoInAggiornamento().setSoggetto(null);
		
		//Non veniva popolato il componente con i dati della trans. elemen.
		popolaStrutturaTransazioneElementare();				
		InserisceImpegniResponse response = inserisceImpegoCustom(model.getImpegnoInAggiornamento(), model);
		
		model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().setImportiCapitoloUG(importiCapitoloUGApp);
		
		return response;
	}
	
	/**
	 * Analizza la response del servizio 
	 * e visualizza gli opportuni messaggi
	 * di errore o esito positivo.
	 * @param response
	 * @return
	 */
	private boolean analizzaResponse(InserisceImpegniResponse response){
		log.debug("analizzaResponse", JAXBUtility.marshall(response));
		
		boolean fallimento = false;
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			
			fallimento = true;
		}  else {
			List<Impegno> accList = response.getElencoImpegniInseriti();
			
			//SIAC-6990
			log.debug("accList", accList);
			
			if(accList != null && accList.size() > 0){
				int i = 0;
				for(Impegno app : accList){
					if(i != 0){
						addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Impegno, anno = " + app.getAnnoMovimento() +  ", numero= "+ app.getNumero() +" )");
					}
					i++;
				}
			}
		}
		
		return fallimento;
	}
	
	public String salva(){
		setMethodName("salva");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		BigDecimal totaleImporti = new BigDecimal(0);
		List<ImpegniPluriennaliModel> listadefinitiva = new ArrayList<ImpegniPluriennaliModel>();
		
		if(model.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && model.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : model.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				
				//non deve essere minore del 1900:
				if (currentImpegnoPluriennale.getAnnoImpPluriennale()<=1900) {
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno = "+currentImpegnoPluriennale.getAnnoImpPluriennale(),">1900"));
				}
				
				//Controlli date
				listaErrori = ValidationUtils.validaDataScadenzaPluriennale(currentImpegnoPluriennale);
				
				//SIAC-6990
				//Controllo Importi
				totaleImporti = totaleImporti.add(validaImporto(currentImpegnoPluriennale, listadefinitiva, listaErrori));
			}
			
		}
		
		//controllo distribuzione importi:
		listaErrori = controlloDistribuzioneImporti(totaleImporti, listaErrori);
		
		//verifichiamo la presenza di errori:
		if(listaErrori.isEmpty()){
		
			AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegni(true,true);
			
			if (requestAggiorna.getImpegno().getProgetto() != null) {
				Progetto progetto = requestAggiorna.getImpegno().getProgetto();
				
				progetto.setTipoProgetto(TipoProgetto.GESTIONE);
				progetto.setBilancio(sessionHandler.getBilancio());

				requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
				requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
			}
			
			// APRILE 2016
			// l'aggiorna impegno non richiama piu' ricercaImpegnoPerChiave 
			// (benche' ottimizzato) e' troppo rischioso perche' l'aggiorna impegno
			// ha richiesto un po' di tempo e qui il timeout e' quasi assicurato
			// con troppi sub...conviene spezzare il caricamento
			// e rieffettuarlo dalla action per evitare timeout
			
			//Setto gli impegni pluriennali
			
			//flag per non fare salvare sul db il primo record dato che sto in modifica
			
			boolean fallimento = false;
			
			//gestione dei pluriennali:
			List<Impegno> listaPluriennali = componiListaPluriennali(listadefinitiva);
			
			//SIAC-6990
			//sposto la modifica, in questo caso se non ci fossero pluriennali
			//eseguo l'aggiornamento
			if(!(listaPluriennali != null && listaPluriennali.size() > 0)) {
				//Inserisco La modifica di spesa
				AggiornaImpegnoResponse responseModificaMovimentoSpesa = movimentoGestionService.aggiornaImpegno(requestAggiorna);
				if(responseModificaMovimentoSpesa.isFallimento() || (responseModificaMovimentoSpesa.getErrori() != null && responseModificaMovimentoSpesa.getErrori().size() > 0)){
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, responseModificaMovimentoSpesa);
					return INPUT;
				}
				//
			}
			
			//SIAC-6990
			List<Messaggio> listMess = null;
			
			if(listaPluriennali != null && listaPluriennali.size() > 0){
				// ci sono pluriennali
				
				////SIAC-6990
				//si sospende il precedente metodo di 
//				//invocazione del servizio di inserimento:
//				InserisceImpegniResponse response = invocazioneServizioInserimento();

//				//analizziamo la response:
//				fallimento = analizzaResponse(response);

				
				InserisceImpegni inserisceImp = convertiModelCustomPerModificaSpesaInRequest(model.getImpegnoInAggiornamento(), model);
				
				ModificaImportoImpegnoSuAnniSuccessivi modificaImportoImpegnoSuAnniSuccessivi = new ModificaImportoImpegnoSuAnniSuccessivi();
				modificaImportoImpegnoSuAnniSuccessivi.setAggiornaImpegnoPadre(model.getImpegnoRequestStep1());
				modificaImportoImpegnoSuAnniSuccessivi.setRichiedente(model.getRichiedente());
				modificaImportoImpegnoSuAnniSuccessivi.setInserisciImpegniRequest(inserisceImp);
				
				ModificaImportoImpegnoSuAnniSuccessiviResponse response = 
						movimentoGestionService.modificaImportoImpegnoSuAnniSuccessivi(modificaImportoImpegnoSuAnniSuccessivi);
				
				if(response.isFallimento() || response.getErrori().size() > 0) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					
					fallimento = true;
					listaErrori = response.getErrori();
				} else {
					listMess = response.getListaMessaggi();
				}
				////
				
			}

			
			if(fallimento) {
				
				return INPUT;
			}
			
			//SIAC-6990
			if(listMess != null) {
				addMessaggi(listMess);
			}
			
//			forceReload = true;
			model.setImpegnoRicaricatoDopoInsOAgg(null);
			
			//SIAC-6990
			if(hasActionMessages()) {
				return SUCCESS;
			}
			
			return GOTO_ELENCO_MODIFICHE;
			
		} else {
			// ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
		
	}
	
	/**
	 * Metodo di comodo per comporre la lista di eventuali 
	 * pluriennali da passare al servizio.
	 * @param listadefinitiva
	 * @return
	 */
	private List<Impegno> componiListaPluriennali(List<ImpegniPluriennaliModel> listadefinitiva){
		List<Impegno> listaPluriennali = new ArrayList<Impegno>();
		if(listadefinitiva != null && listadefinitiva.size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : listadefinitiva) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale() != null && currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO) > 0) {
					listaPluriennali.add(popolaImpegno(model, currentImpegnoPluriennale, creaOggettoBilancio(),true));
				}
			}
		}
		return listaPluriennali;
	}
	
	/**
	 * Per validare l'importo 
	 * e calcolare la somma degli importi tra i pluriennali
	 * 
	 * @param currentImpegnoPluriennale
	 * @param listadefinitiva
	 * @param listaErrori
	 * @return
	 */
	private BigDecimal validaImporto(ImpegniPluriennaliModel currentImpegnoPluriennale, List<ImpegniPluriennaliModel> listadefinitiva,List<Errore> listaErrori){
		
		BigDecimal totaleImporti = new BigDecimal(0);
		
		if(!StringUtils.isEmpty(currentImpegnoPluriennale.getImportoImpPluriennaleString())){
			if(currentImpegnoPluriennale.getImportoImpPluriennaleString().split(",").length <= 2){
				if(currentImpegnoPluriennale.getImportoImpPluriennaleString().split(".").length <= 2){
					BigDecimal impApp = convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString());
					if (impApp.compareTo(BigDecimal.ZERO)==1) {
						totaleImporti = totaleImporti.add(impApp);
						currentImpegnoPluriennale.setImportoImpPluriennale(convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString()));
						
						if(currentImpegnoPluriennale.getAnnoImpPluriennale() == model.getMovimentoSpesaModel().getAnnoImpegno()){
							model.getMovimentoSpesaModel().getSpesa().setImportoNew(convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString()));
						} else {
							listadefinitiva.add(currentImpegnoPluriennale);
						}
					}else if(impApp.compareTo(BigDecimal.ZERO)==-1){
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("Importo Anno " +currentImpegnoPluriennale.getAnnoImpPluriennale() + " negativo"));
					}
					
					
				} else {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo Anno " +currentImpegnoPluriennale.getAnnoImpPluriennale() + " : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));	
				}
			} else {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo Anno " +currentImpegnoPluriennale.getAnnoImpPluriennale() + " : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			}
		} 
		
		return totaleImporti;
		
	}
	
	
	public String pulisciCampo() {
		setMethodName("pulisciCampo");
		for(ImpegniPluriennaliModel i: model.getMovimentoSpesaModel().getListaImpegniPluriennali()){			
			if(i.getAnnoImpPluriennale() == getCampoPerPulire()){
				i.setAnnoImpPluriennale(0);
				i.setDataScadenzaImpPluriennale(null);
				i.setImportoImpPluriennale(BigDecimal.ZERO);
				return SUCCESS;
			}		
		}
		/* AGGIUNGERE ERRORE GIUSTO */
		addErrore(methodName, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("ERRORE") );
		return INPUT;
	}

	public int getCampoPerPulire() {
		return campoPerPulire;
	}

	public void setCampoPerPulire(int campoPerPulire) {
		this.campoPerPulire = campoPerPulire;
	}
	
	
	public String indietro(){
		setMethodName("indietro");
		return GOTO_ELENCO_MODIFICHE;
	}

	
	//Funzione per il controllo della disponibilita' del capitolo sugli importi degli impegni pluriennali
	private boolean controlloImportiImpegniPlur(int annoImpegnoPlur, BigDecimal importoPlur){
		boolean erroreImporti = false;
		Integer annoEsercio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
		Integer annoImpegnoPlurInt=Integer.valueOf(annoImpegnoPlur);
		
	    if (annoImpegnoPlurInt != null && annoImpegnoPlurInt.compareTo(annoEsercio + 2) <= 0) {
	    	
	    	// ricavo la mappa con gli anni/valori
	    	Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilitaImpegni();
	    	
	    	// verifico le disponibilita
	    	BigDecimal bd =(BigDecimal) mappaAnniValori.get(annoImpegnoPlurInt);
    		if (importoPlur.compareTo(bd) > 0) {
    			 erroreImporti = true;
    		}
    		 
	    }
	    return erroreImporti;
	}
	
	//Controllo disponibilita' del capitolo uscita
	private Map<Integer, BigDecimal> ritornaMappaAnniDisponibilitaImpegni(){

		Map<Integer, BigDecimal> mappa = new HashMap<Integer, BigDecimal>();

		for(int i =0;i<3;i++){
			if(i==0){
				mappa.put(model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getAnnoCompetenza()+i, model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno1());
			}
			if(i==1){
				mappa.put(model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getAnnoCompetenza()+i, model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno2());

			}

			if(i==2){
				mappa.put(model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getAnnoCompetenza()+i, model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitoloUG().getDisponibilitaImpegnareAnno2());
			}
		}
		return mappa;
	}
	
}
