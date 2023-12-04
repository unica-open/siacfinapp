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

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinActionUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaAccStep2Action extends ActionKeyAggiornaAccertamento {

	private static final long serialVersionUID = -8026818682722777552L;

	public int campoPerPulire;
	public boolean checkProseguiPlurAccertamento;
	public boolean fromForza;
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Importo - Accertamenti pluriennali");
		
		//setto il tipo di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
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
	
	public String salvaAccertamentiPluriennali(){
		setMethodName("salva");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		BigDecimal totaleImporti = new BigDecimal(0);
		List<ImpegniPluriennaliModel> listadefinitiva = new ArrayList<ImpegniPluriennaliModel>();
		
		if(model.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && model.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : model.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				
				//converto l'importo:
				BigDecimal importoBD=FinActionUtils.convertiImportiPluriennaleString(currentImpegnoPluriennale);
				
				//Controlli date
				listaErrori = ValidationUtils.validaDataScadenzaPluriennale(currentImpegnoPluriennale);
				
				//Controllo Importi
				totaleImporti = validaImporto(currentImpegnoPluriennale, listadefinitiva, listaErrori);
				
				//Controllo disponibilita' capitolo pluriennali
				//TODO - Controllo disponibilita' capitolo pluriennali
				if (importoBD.compareTo(BigDecimal.ZERO)==1) {
					if (controlloImportiAccertamentiPlur(currentImpegnoPluriennale.getAnnoImpPluriennale(), importoBD) && !isFromForza()) {
				    	addActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getCodice()+" : "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getDescrizione());
				    	setCheckProseguiPlurAccertamento(true);
				    	return INPUT;
					}
				}
			}
		}
		
		//controllo distribuzione importi:
		listaErrori = controlloDistribuzioneImporti(totaleImporti, listaErrori);
		
		//verifichiamo la presenza di errori:
		if(listaErrori.isEmpty()){

			model.getStep1Model().setRiaccertato("si");

			boolean fallimento = false;

			//gestione dei pluriennali:
			List<Accertamento> listaPluriennali = componiListaPluriennali(listadefinitiva);
			
			if(listaPluriennali != null && listaPluriennali.size() > 0){
				// ci sono pluriennali
				
				//invocazione del servizio di inserimento:
				InserisceAccertamentiResponse response = invocazioneServizioInserimento();

				//analizziamo la response:
				fallimento = analizzaResponse(response);
			}
			
			if(fallimento) {
				setFromForza(false);
				return INPUT;
			}
			
			setFromForza(false);
			return GOTO_ELENCO_MODIFICHE;
			
		} else {
			// ci sono errori
			addErrori(listaErrori);
			setFromForza(false);
			return INPUT;
		}
	}
	
	/**
	 * Metodo di comodo per comporre la lista di eventuali 
	 * pluriennali da passare al servizio.
	 * @param listadefinitiva
	 * @return
	 */
	private List<Accertamento> componiListaPluriennali(List<ImpegniPluriennaliModel> listadefinitiva){
		List<Accertamento> listaPluriennali = new ArrayList<Accertamento>();
		if(listadefinitiva != null && listadefinitiva.size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : listadefinitiva) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale() != null && currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO) > 0) {
					listaPluriennali.add(popolaAccertamento(model, currentImpegnoPluriennale, creaOggettoBilancio()));
				}
			}
		}
		return listaPluriennali;
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
	private InserisceAccertamentiResponse invocazioneServizioInserimento(){
		
		ImportiCapitoloEG importiCapitoloEGApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG();
		
		model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().setImportiCapitoloEG(null);
		model.getAccertamentoInAggiornamento().setSoggetto(null);
		
		//Non veniva popolto il componente con i dati della trans. elemen.
		popolaStrutturaTransazioneElementareAcc();
		InserisceAccertamentiResponse response = inserisceAccertamentoCustom(model.getAccertamentoInAggiornamento(), model);
		
		model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().setImportiCapitoloEG(importiCapitoloEGApp);
		
		return response;
	}
	
	/**
	 * Analizza la response del servizio 
	 * e visualizza gli opportuni messaggi
	 * di errore o esito positivo.
	 * @param response
	 * @return
	 */
	private boolean analizzaResponse(InserisceAccertamentiResponse response){
		
		boolean fallimento = false;
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			
			//ci sono errori
			
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			
			fallimento = true;
		} else {
			
			//tutto ok
			
			List<Accertamento> accList = response.getElencoAccertamentiInseriti();
			if(accList != null && accList.size() > 0){
				int i = 0;
				for(Accertamento app : accList){
					if(i != 0){
						addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Accertamento, anno = " + app.getAnnoMovimento() +  ", numero= "+ app.getNumeroBigDecimal() +" )");
					}
					i++;
				}
			}

		}
		
		//ritorniamo il risultato dell'analisi:
		return fallimento;
	}
	
	public String forzaSalvaPluriennaleAccertamento(){
		setFromForza(true);
		return salvaAccertamentiPluriennali();
	}

	
	public String salva(){
		setMethodName("salva");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		BigDecimal totaleImporti = new BigDecimal(0);
		List<ImpegniPluriennaliModel> listadefinitiva = new ArrayList<ImpegniPluriennaliModel>();
		
		if(model.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && model.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0){
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : model.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				
				//Controlli date
				listaErrori = ValidationUtils.validaDataScadenzaPluriennale(currentImpegnoPluriennale);
				
				//Controllo Importi
				totaleImporti = validaImporto(currentImpegnoPluriennale, listadefinitiva, listaErrori);
			}
			
		}
		
		//controllo distribuzione importi:
		listaErrori = controlloDistribuzioneImporti(totaleImporti, listaErrori);
		
		//verifichiamo la presenza di errori:
		if(listaErrori.isEmpty()){
		
			model.getStep1Model().setRiaccertato("si");
			
			AggiornaAccertamentoResponse responseModificaMovimentoEntrata = movimentoGestioneFinService.aggiornaAccertamento(convertiModelPerChiamataServizioAggiornaAccertamenti(true));
			
			if(responseModificaMovimentoEntrata.isFallimento() || (responseModificaMovimentoEntrata.getErrori() != null && responseModificaMovimentoEntrata.getErrori().size() > 0)){
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, responseModificaMovimentoEntrata);
				return INPUT;
			}
			
			
			//Ottimizzazione richiamo ai servizi
			model.setAccertamentoRicaricatoDopoInsOAgg(responseModificaMovimentoEntrata.getAccertamento());
			
			List<ModificaMovimentoGestioneEntrata> nuovaListaModificheImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
			nuovaListaModificheImporti = responseModificaMovimentoEntrata.getAccertamento().getListaModificheMovimentoGestioneEntrata();
			model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(nuovaListaModificheImporti);
			
			List<SubAccertamento> subDefinitivoList = new ArrayList<SubAccertamento>();
			if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size()> 0){
				for(SubAccertamento subSession : model.getListaSubaccertamenti()){
					int idSubSessione = subSession.getUid();

					for(SubAccertamento subResponse : responseModificaMovimentoEntrata.getAccertamento().getSubAccertamenti()){
						int idSubResponse = subResponse.getUid();
						if(idSubSessione == idSubResponse){
							List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSubImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
							nuovaListaModificheSubImporti = subResponse.getListaModificheMovimentoGestioneEntrata();
							subSession.setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSubImporti);
							subDefinitivoList.add(subSession);
						}
					}
				}
			}
			
			model.setListaSubaccertamenti(subDefinitivoList);
			
			boolean fallimento = false;
		
			//gestione dei pluriennali:
			List<Accertamento> listaPluriennali = componiListaPluriennali(listadefinitiva);
			
			if(listaPluriennali != null && listaPluriennali.size() > 0){
				// ci sono pluriennali
				
				//invocazione del servizio di inserimento:
				InserisceAccertamentiResponse response = invocazioneServizioInserimento();
				
				//analizziamo la response:
				fallimento = analizzaResponse(response);
			}
			
			if(fallimento) {
				return INPUT;
			}
			
			return GOTO_ELENCO_MODIFICHE;
		} else {
			// ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
		
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
							model.getMovimentoSpesaModel().getEntrata().setImportoNew(convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString()));
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
	
	//Funzione per il controllo della disponibilita' del capitolo sugli importi degli accertamenti pluriennali
	private boolean controlloImportiAccertamentiPlur(int annoAccertamentoPlur, BigDecimal importoPlur){
		boolean erroreImporti = false;
		Integer annoEsercio = sessionHandler.getAnnoBilancio();
		Integer annoAccertamentoPlurInt=Integer.valueOf(annoAccertamentoPlur);
		
	    if (annoAccertamentoPlurInt != null && annoAccertamentoPlurInt.compareTo(annoEsercio + 2) <= 0) {
	    	
	    	// ricavo la mappa con gli anni/valori
	    	Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilitaAccertamenti();
	    	
	    	// verifico le disponibilita
	    	BigDecimal bd =(BigDecimal) mappaAnniValori.get(annoAccertamentoPlurInt);
    		if (importoPlur.compareTo(bd) > 0) {
    			 erroreImporti = true;
    		}
    		 
	    }
	    return erroreImporti;
	}
	
	//Controllo disponibilita' del capitolo entrata
	private Map<Integer, BigDecimal> ritornaMappaAnniDisponibilitaAccertamenti(){

		Map<Integer, BigDecimal> mappa = new HashMap<Integer, BigDecimal>();

		for(int i =0;i<3;i++){
			if(i==0){
				mappa.put(model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getAnnoCompetenza()+i, model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno1());
			}
			if(i==1){
				mappa.put(model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getAnnoCompetenza()+i, model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno2());
			}
			if(i==2){
				mappa.put(model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getAnnoCompetenza()+i, model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno3());
			}
		}
		return mappa;

	}

	public boolean isCheckProseguiPlurAccertamento() {
		return checkProseguiPlurAccertamento;
	}

	public void setCheckProseguiPlurAccertamento(
			boolean checkProseguiPlurAccertamento) {
		this.checkProseguiPlurAccertamento = checkProseguiPlurAccertamento;
	}

	public boolean isFromForza() {
		return fromForza;
	}

	public void setFromForza(boolean fromForza) {
		this.fromForza = fromForza;
	}

}
