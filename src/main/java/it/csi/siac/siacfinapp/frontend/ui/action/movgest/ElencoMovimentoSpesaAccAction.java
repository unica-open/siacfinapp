/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoMovimentoSpesaAccAction extends BaseRorEntrata {

	private static final long serialVersionUID = -2541634052439907448L;

	private String uidPerDettaglioModAccertamento;
	
	private String uidModDaAnnullare;
	private String numeroMovDaAnnullare;
	
	//SIAC-7349 Inizio  SR180 MC 08/04/2020
	private String numeroMovCollegatiDaAnnullare;
	List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata ;
	//SIAC-7349 Fine  SR180 MC 08/04/2020
	
	protected boolean ricaricaDopoInserimento = false;
	//SIAC-8553
	private boolean skipControlloDiponibilitaCapitoloAnnullamentoModifica = false;
	
	public ElencoMovimentoSpesaAccAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//SIAC-7349 Inizio  SR180 MC 08/04/2020
		setListaModificheSpeseCollegata(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneSpesaCollegata());
		//SIAC-7349 Fine SR180 MC 08/04/2020
		
		//setto il titolo:
		super.model.setTitolo("Movimento Entrata");
		
	}
	
	/**
	 * Metodo che ricarica i dati
	 * @param accertamentoFresco
	 */
	private void caricaDatiTotali(Accertamento accertamentoFresco){
		List<ModificaMovimentoGestioneEntrata> listaModificheEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();

		// Scommento per risolvere Jira ????
		RicercaAccertamentoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoK accertamentoDaCercare = new RicercaAccertamentoK();
		BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
		
		//setto i dati di chiave dell'accertamento:
		accertamentoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		accertamentoDaCercare.setNumeroAccertamento(numeroImpegno);
		accertamentoDaCercare.setAnnoAccertamento(model.getStep1Model().getAnnoImpegno());
				
		//RICHIEDENTE
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		
		//ENTE
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaAccertamentoK(accertamentoDaCercare);
		
		//Richiamo il servizio di ricerca:
		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(parametroRicercaPerChiave);
		
		//analizzo la risposta del servizio:
		if(!respRk.isFallimento() && respRk.getAccertamento()!= null){
			//ok non ci sono errori
			accertamentoFresco = respRk.getAccertamento();
			
		}
		
		//SIAC-8065 loggo gli errori 
		if(isFallimento(respRk)) {
			debug("Errore nel servizio di ricerca dell'accertamento");
			addErrori(respRk);
			return;
		}
		
		if(null!=accertamentoFresco){
			// setto il model con il fresco
			model.setAccertamentoRicaricatoDopoInsOAgg(accertamentoFresco);
		}
		
		if(model.getAccertamentoRicaricatoDopoInsOAgg()!= null){
			model.setAccertamentoInAggiornamento(model.getAccertamentoRicaricatoDopoInsOAgg());
			caricaDatiAccertamento(true);
			model.setAccertamentoRicaricatoDopoInsOAgg(null);
		}

		model.getStep1Model().setImportoImpegno(model.getAccertamentoInAggiornamento().getImportoAttuale());
		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(model.getAccertamentoInAggiornamento().getImportoAttuale()));
		model.setTotaleSubImpegno(model.getAccertamentoInAggiornamento().getTotaleSubAccertamentiBigDecimal());
		model.setDisponibilitaSubImpegnare(model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare());

		
		model.setAccertamentoInAggiornamento(model.getAccertamentoInAggiornamento());
		if (model.getAccertamentoInAggiornamento() != null && model.getAccertamentoInAggiornamento().getElencoSubAccertamenti() != null && model.getAccertamentoInAggiornamento().getElencoSubAccertamenti().size() > 0) {
			model.setListaSubaccertamenti(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti());	//in attesa dei sub-accertamenti
		}else {
			model.setListaSubaccertamenti(new ArrayList<SubAccertamento>());	//in attesa dei sub-accertamenti
		}

		
		if(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata() != null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata().size() > 0){
			listaModificheEntrata.addAll(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata());
		}
		
		if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0){
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(sub.getListaModificheMovimentoGestioneEntrata() != null && sub.getListaModificheMovimentoGestioneEntrata().size() > 0){					
					listaModificheEntrata.addAll(sub.getListaModificheMovimentoGestioneEntrata());
				}
			}
		}
		
		listaModificheEntrata = preparaEOrdinaPerModel(listaModificheEntrata);
			
		model.getMovimentoSpesaModel().setListaModificheEntrata(listaModificheEntrata);
		model.getMovimentoSpesaModel().setAccertamento(model.getAccertamentoInAggiornamento());

		creaMovGestModelCache();
		
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		model.setAccertamentoRicaricatoDopoInsOAgg(new Accertamento());
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	public String execute() throws Exception {
		
		caricaLabelsAggiorna(1);
		if(forceReload || ricaricaDopoInserimento){
			caricaDatiTotali(null);
			//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
			if(model.hasErrori()) {
				return INPUT;
			}
		}else{
			if(model.getMovimentoSpesaModel().getAccertamento()==null && model.getAccertamentoInAggiornamento()!=null){
				model.getMovimentoSpesaModel().setAccertamento(model.getAccertamentoInAggiornamento());
			
				//vado a comporre una lista di modifiche comprendente quelle dell'accertamento piu' quelle dei suoi sub:
				List<ModificaMovimentoGestioneEntrata> listaModificheEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();
				
				//modifiche dell'accertamento:
				if(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata() != null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata().size() > 0){
					listaModificheEntrata.addAll(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata());
				}
				
				//modifiche dei suoi sub:
				if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0){
					for(SubAccertamento sub : model.getListaSubaccertamenti()){
						if(sub.getListaModificheMovimentoGestioneEntrata() != null && sub.getListaModificheMovimentoGestioneEntrata().size() > 0){					
							listaModificheEntrata.addAll(sub.getListaModificheMovimentoGestioneEntrata());
						}
					}
				}
				
				//completa la lista ordinandola e settando gli atti amministrativi:
				listaModificheEntrata = preparaEOrdinaPerModel(listaModificheEntrata);
				
				//setto la lista completata nel model:
				model.getMovimentoSpesaModel().setListaModificheEntrata(listaModificheEntrata);
			}	
		}	
		
		//SIAC-8553
		model.setProseguiConWarningModificaPositivaAccertamento(false);
		
		return SUCCESS;
	}
	
	/**
	 * Data una lista di modifiche la ordina per numero e la veste con i dati degli atti amministrativi
	 * @param listaModificheEntrata
	 * @return
	 */
	protected List<ModificaMovimentoGestioneEntrata> preparaEOrdinaPerModel(List<ModificaMovimentoGestioneEntrata> listaModificheEntrata ){
		List<ModificaMovimentoGestioneEntrata> appList = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		if(listaModificheEntrata!=null && listaModificheEntrata.size()>0){
			
			//ordinamento:
			Collections.sort(listaModificheEntrata, new NumComparatorAcc());
			
			//atto amministrativo:
			for(ModificaMovimentoGestioneEntrata app : listaModificheEntrata){
				if(app.getAttoAmministrativo() != null){
					
					if(app.isRiepilogoAutomatiche()){
						//Modifica fittizia per riepilogo automatiche:
						appList.add(app);
					}else {
						//Codice classico per modifiche normali:
						if(app.getAttoAmministrativo().getUid() != 0){
							AttoAmministrativo atto = new AttoAmministrativo();
							atto = ricercaAttoAmministrativo(app.getAttoAmministrativo().getUid());
							app.setAttoAmministrativo(atto);
							appList.add(app);
						} else {
							Integer uidApp = ((MovimentoGestione)app).getAttoAmministrativo().getUid();
							if(uidApp != 0){
								AttoAmministrativo atto = new AttoAmministrativo();
								atto = ricercaAttoAmministrativo(uidApp);
								app.setAttoAmministrativo(atto);
								appList.add(app);
							}
						}
					}
				}
			}						
			
			listaModificheEntrata = appList;
			
			//ordinamento delle automatiche:
			listaModificheEntrata = riordinaAutomatiche(listaModificheEntrata);
			
		}
		
		return listaModificheEntrata;
	}
	
	/**
	 * data una lista di modifiche sposta ad inizio della lista quelle di riepilogo automatiche, a seguire le altre
	 * @param listaModificheEntrata
	 * @return
	 */
	protected List<ModificaMovimentoGestioneEntrata> riordinaAutomatiche(List<ModificaMovimentoGestioneEntrata> listaModificheEntrata){
		//istanzio la lista tutte che sara' poi restituita al chiamante:
		List<ModificaMovimentoGestioneEntrata> tutte = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		//istanzio due liste di appoggio, in una andro' a inserire quelle di riepilogo automatiche:
		List<ModificaMovimentoGestioneEntrata> automatiche = new ArrayList<ModificaMovimentoGestioneEntrata>();
		//nell'altra quelle normali:
		List<ModificaMovimentoGestioneEntrata> normali = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		//itero le modifiche, e quando trovo una modifica di tipo riepilogo automatico la
		//inserisco nella lista automatiche, e inserisco le normali nella lista normali:
		for(ModificaMovimentoGestioneEntrata it: listaModificheEntrata){
			if(it!=null){
				if(it.isRiepilogoAutomatiche()){
					automatiche.add(clone(it));
				} else {
					normali.add(clone(it));
				}
			}
		}
		//aggiungo prima le automatiche:
		tutte.addAll(automatiche);
		//e poi le normali:
		tutte.addAll(normali);
		
		//ritorno tutte che si trova ad avere le automatiche in testa:
		return tutte;
	}
	
	/**
	 * per il dettaglio poo up
	 * @return
	 */
	public String dettaglioModPopUp(){
		setMethodName("dettaglioModPopUp");
		debug(methodName, " ----> "+getUidPerDettaglioModAccertamento());

		//individuo l'indice di quella selezionata:
		int selezionata = Integer.valueOf(getUidPerDettaglioModAccertamento());
		
		//pesco la modifica selezionata dalla lista:
		ModificaMovimentoGestioneEntrata modifica = model.getMovimentoSpesaModel().getListaModificheEntrata().get(selezionata);
		
		ModificaConsulta mc = new ModificaConsulta();
		mc.setTipoMovimento(MovimentoConsulta.ACCERTAMENTO);
		// modifica
		mc.setNumero(String.valueOf(modifica.getNumeroModificaMovimentoGestione()));
		mc.setDescrizione(modifica.getDescrizioneModificaMovimentoGestione());
	    mc.setImporto(modifica.getImportoOld());
	    mc.setMotivo(modifica.getTipoMovimentoDesc());
	    mc.setUtenteCreazione(modifica.getUtenteCreazione());
	    mc.setUtenteModifica(modifica.getUtenteModifica());
	    mc.setDataInserimento(modifica.getDataEmissione());
	    mc.setDataModifica(modifica.getDataModifica());
	    mc.setStatoOperativo(modifica.getStatoOperativoModificaMovimentoGestione().name());
	    //SIAC-8118
	    mc.setDataStatoOperativo(modifica.getDataFromStatoOperativo());
	    //SIAC-7349 Inizio  SR180 MC 08/04/2020
	    mc.setListaModificheMovimentoGestioneSpesaCollegata(modifica.getListaModificheMovimentoGestioneSpesaCollegata());
	    //SIAC-7349 Fine  SR180 MC 08/04/2020
	    
	    //Reimputazione:
	    mc = settaDatiReimputazioneInModificaConsulta(modifica, mc);
	    
	    // provvedimento
	    if (modifica.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(modifica.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(modifica.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(modifica.getAttoAmministrativo().getOggetto());
	    	//siac 6929
	    	Boolean valoreBloccoD = modifica.getAttoAmministrativo().getBloccoRagioneria();
	    	String valore = (valoreBloccoD == null) ? null : String.valueOf(valoreBloccoD);
	    	
	    	mc.getProvvedimento().setBloccoRagioneria(valore);
	    	
	    	if (modifica.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(modifica.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (modifica.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(modifica.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	mc.getProvvedimento().setStato(modifica.getAttoAmministrativo().getStatoOperativo());
	    }
	    
	    boolean daSoggetto = isValorizzato(modifica.getSoggettoOldMovimentoGestione());
	    boolean aSoggetto = isValorizzato(modifica.getSoggettoNewMovimentoGestione());
	    
	    boolean diImporto = isDiImporto(modifica);
	    
	    //Controllo sui soggetti
	    if(!diImporto){
		    if(!daSoggetto && aSoggetto){
		    	
		    	//da classe a soggetto
		    	
		    	//Classe Precedente 
		    	mc.getSoggettoPrec().setClasseSoggettoCodice(modifica.getClasseSoggettoOldMovimentoGestione().getCodice());
		    	mc.getSoggettoPrec().setClasseSoggettoDescrizione(modifica.getClasseSoggettoOldMovimentoGestione().getDescrizione());
		    	//Soggetto Attuale
		    	mc.getSoggettoAttuale().setCodice(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
			    mc.getSoggettoAttuale().setDenominazione(modifica.getSoggettoNewMovimentoGestione().getDenominazione());
			    mc.getSoggettoAttuale().setCodiceFiscale(modifica.getSoggettoNewMovimentoGestione().getCodiceFiscale());
			    mc.getSoggettoAttuale().setPartitaIva(modifica.getSoggettoNewMovimentoGestione().getPartitaIva());
		    	
		    	
		    } else if(daSoggetto && !aSoggetto){
	    		//da soggetto a classe
	    	
	    		//Soggetto Precedente
	    		mc.getSoggettoPrec().setCodice(modifica.getSoggettoOldMovimentoGestione().getCodiceSoggetto());
			    mc.getSoggettoPrec().setDenominazione(modifica.getSoggettoOldMovimentoGestione().getDenominazione());
			    mc.getSoggettoPrec().setCodiceFiscale(modifica.getSoggettoOldMovimentoGestione().getCodiceFiscale());
			    mc.getSoggettoPrec().setPartitaIva(modifica.getSoggettoOldMovimentoGestione().getPartitaIva());
	    	
	    	
			    //Classe Attuale
			    mc.getSoggettoAttuale().setClasseSoggettoCodice(modifica.getClasseSoggettoNewMovimentoGestione().getCodice());
		    	mc.getSoggettoAttuale().setClasseSoggettoDescrizione(modifica.getClasseSoggettoNewMovimentoGestione().getDescrizione());
				    
		    } else if(!daSoggetto && !aSoggetto){
		    	//da classe a classe
		    	
		    	//Classe Precedente
		    	mc.getSoggettoPrec().setClasseSoggettoCodice(modifica.getClasseSoggettoOldMovimentoGestione().getCodice());
		    	mc.getSoggettoPrec().setClasseSoggettoDescrizione(modifica.getClasseSoggettoOldMovimentoGestione().getDescrizione());
		    	
		    	//Classe Attuale
		    	mc.getSoggettoAttuale().setClasseSoggettoCodice(modifica.getClasseSoggettoNewMovimentoGestione().getCodice());
		    	mc.getSoggettoAttuale().setClasseSoggettoDescrizione(modifica.getClasseSoggettoNewMovimentoGestione().getDescrizione());
		    	
		    } else if(daSoggetto && aSoggetto){
		    	//da soggetto a soggetto
		    	
	    		//Soggetto Precedente
		    	mc.getSoggettoPrec().setCodice(modifica.getSoggettoOldMovimentoGestione().getCodiceSoggetto());
			    mc.getSoggettoPrec().setDenominazione(modifica.getSoggettoOldMovimentoGestione().getDenominazione());
			    mc.getSoggettoPrec().setCodiceFiscale(modifica.getSoggettoOldMovimentoGestione().getCodiceFiscale());
			    mc.getSoggettoPrec().setPartitaIva(modifica.getSoggettoOldMovimentoGestione().getPartitaIva());
			    
			    //Soggetto Attuale
			    mc.getSoggettoAttuale().setCodice(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
			    mc.getSoggettoAttuale().setDenominazione(modifica.getSoggettoNewMovimentoGestione().getDenominazione());
			    mc.getSoggettoAttuale().setCodiceFiscale(modifica.getSoggettoNewMovimentoGestione().getCodiceFiscale());
			    mc.getSoggettoAttuale().setPartitaIva(modifica.getSoggettoNewMovimentoGestione().getPartitaIva());
		    	
		    }
	    }
	    
		String descAccertamento = model.getAccertamentoInAggiornamento().getAnnoMovimento() + "/" + model.getAccertamentoInAggiornamento().getNumeroBigDecimal() + 
			" - " + ((model.getAccertamentoInAggiornamento().getDescrizione() != null)? model.getAccertamentoInAggiornamento().getDescrizione() : "") +
			" - " + convertiBigDecimalToImporto(model.getAccertamentoInAggiornamento().getImportoAttuale()) + 
			" (" +  model.getAccertamentoInAggiornamento().getDescrizioneStatoOperativoMovimentoGestioneEntrata() + " dal " + convertDateToString(model.getAccertamentoInAggiornamento().getDataStatoOperativoMovimentoGestioneEntrata()) + ")";	
		mc.setDescMain(descAccertamento);
	    
	    if (modifica.getNumeroSubAccertamento() != null) {
	    	SubAccertamento sAcc = getSubAccertamento(modifica.getNumeroSubAccertamento(), model.getListaSubaccertamenti());
		    if (sAcc != null) {
				String descSub = sAcc.getNumeroBigDecimal() + " (" +  sAcc.getDescrizioneStatoOperativoMovimentoGestioneEntrata() + " dal " + convertDateToString(sAcc.getDataStatoOperativoMovimentoGestioneEntrata()) + ")";	
				mc.setDescSub(descSub);
		    }
	    }
		
	    //setto nel model:
		model.setModificaDettaglio(mc);
		
		return "dettaglioModPopUp";
	}
	
	private SubAccertamento getSubAccertamento (Integer numero, List<SubAccertamento> elencoSubAcc) {
		if (elencoSubAcc != null) for (SubAccertamento subAccertamento : elencoSubAcc) {
			if (subAccertamento.getNumeroBigDecimal().intValue() == numero) return subAccertamento;
		}
		return null;
	}
	
	/**
	 * Annullamento di un movimento spesa
	 * @return
	 */
	public String annullaMovGestSpesa(){
		//dati per degub:
		setMethodName("annullaMovGestSpesa");
		
		//individuo l'uid da annullare
		String uidDaAnnullare = getUidModDaAnnullare();
		
		//SIAC-8553
		List<Errore> listaWarning = new ArrayList<Errore>();
		
		//issue-11: su accertamento residuo non deve controllare il tetto massimo
		boolean residuo = model.getAccertamentoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio();
		
		//issue-12: non dare il warning se il capitolo ha disponibilità negativa e la modifica che sto annullando è positiva
		int signumMod = 0;
		for (ModificaMovimentoGestioneEntrata modifica: model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
			if(new Integer(modifica.getUid()).equals(new Integer(uidDaAnnullare))) {
				signumMod=modifica.getImportoOld().signum();
			}			
		}
		boolean	disponibCapitoloNegativa = model.getAccertamentoInAggiornamento().getAnnoMovimento() == sessionHandler.getAnnoBilancio() &&
										   model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitolo().getDisponibilitaAccertareAnno1().signum() == -1 && 
										   signumMod != -1?true:false;
		
		if (!residuo && !disponibCapitoloNegativa) {
			controlloSuDiponibilitaPreAnnullamento(uidDaAnnullare, listaWarning);
		}
			
		if(!listaWarning.isEmpty()) {
			addWarnings(listaWarning, true);
			return INPUT;
		}
		
		//compongo la request per il servizio:
		AnnullaMovimentoEntrata reqAnnulla = convertiModelPerChiamataServizioAnnullaSubAcc(uidDaAnnullare);
		reqAnnulla.setVerificaImportiDopoAnnullamentoModifica(true);
		
		// chiamata al servizio di annullamento:
		AnnullaMovimentoEntrataResponse response = movimentoGestioneFinService.annullaMovimentoEntrata(reqAnnulla);
		
		//controllo errori nella response:
		if(isFallimento(response)){
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		//ricarico i dati:
		caricaDatiTotali(response.getAccertamento());
		//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
		if(model.hasErrori()) {
			return INPUT;
		}
		
		return SUCCESS;
		
	}
	
	//SIAC-8553
	private void controlloSuDiponibilitaPreAnnullamento(String uidDaAnnullare, List<Errore> listaWarning) {
		
		ModificaMovimentoGestioneEntrata modifica = filtraListaModificheMovgestEntrata(
				model.getAccertamentoInAggiornamento(), 
				NumberUtil.safeParseStringToInteger(uidDaAnnullare)
		);
		
		if(modifica != null && modifica.getImportoOld() != null) {
			
			if(controlloImportoModificaSuDisponibiltaAccertareCapitolo(modifica.getImportoOld().negate(), listaWarning)) {
				model.setProseguiConWarningModificaPositivaAccertamento(true);
			}
		}
	}
	
	public String indietro(){
		setMethodName("indietro");
		return "gotoAggiornaImpegnoStep1";
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getUidPerDettaglioModAccertamento() {
		return uidPerDettaglioModAccertamento;
	}

	public void setUidPerDettaglioModAccertamento(String uidPerDettaglioModAccertamento) {
		this.uidPerDettaglioModAccertamento = uidPerDettaglioModAccertamento;
	}

	public String getUidModDaAnnullare() {
		return uidModDaAnnullare;
	}

	public void setUidModDaAnnullare(String uidModDaAnnullare) {
		this.uidModDaAnnullare = uidModDaAnnullare;
	}

	public String getNumeroMovDaAnnullare() {
		return numeroMovDaAnnullare;
	}

	public void setNumeroMovDaAnnullare(String numeroMovDaAnnullare) {
		this.numeroMovDaAnnullare = numeroMovDaAnnullare;
	}


	//SIAC-7349 Inizio  SR180 MC 08/04/2020
	
	
	/**
	 * @return the listaModificheSpeseCollegata
	 */
	public List<ModificaMovimentoGestioneSpesaCollegata> getListaModificheSpeseCollegata() {
		return listaModificheSpeseCollegata;
	}

	/**
	 * @return the numeroMovCollegatiDaAnnullare
	 */
	public String getNumeroMovCollegatiDaAnnullare() {
		return numeroMovCollegatiDaAnnullare;
	}

	/**
	 * @param numeroMovCollegatiDaAnnullare the numeroMovCollegatiDaAnnullare to set
	 */
	public void setNumeroMovCollegatiDaAnnullare(String numeroMovCollegatiDaAnnullare) {
		this.numeroMovCollegatiDaAnnullare = numeroMovCollegatiDaAnnullare;
	}

	/**
	 * @param listaModificheSpeseCollegata the listaModificheSpeseCollegata to set
	 */
	public void setListaModificheSpeseCollegata(
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata) {
		this.listaModificheSpeseCollegata = listaModificheSpeseCollegata;
	}
	//SIAC-7349 Fine  SR180 MC 08/04/2020

	/**
	 * @return the skipControlloDiponibilitaCapitoloAnnullamentoModifica
	 */
	public boolean isSkipControlloDiponibilitaCapitoloAnnullamentoModifica() {
		return skipControlloDiponibilitaCapitoloAnnullamentoModifica;
	}

	/**
	 * @param skipControlloDiponibilitaCapitoloAnnullamentoModifica the skipControlloDiponibilitaCapitoloAnnullamentoModifica to set
	 */
	public void setSkipControlloDiponibilitaCapitoloAnnullamentoModifica(boolean skipControlloDiponibilitaCapitoloAnnullamentoModifica) {
		this.skipControlloDiponibilitaCapitoloAnnullamentoModifica = skipControlloDiponibilitaCapitoloAnnullamentoModifica;
	}

}

//Classe per ordinamento della lista
class NumComparatorAcc implements Comparator<ModificaMovimentoGestioneEntrata> {
  @Override
  public int compare(ModificaMovimentoGestioneEntrata objToCampareUno, ModificaMovimentoGestioneEntrata objToCampareDue) {
		
		if(objToCampareUno!=null && objToCampareDue!=null){
		    	return objToCampareDue.getNumeroModificaMovimentoGestione() < objToCampareUno.getNumeroModificaMovimentoGestione() ? -1 : objToCampareUno.getNumeroModificaMovimentoGestione() ==  objToCampareDue.getNumeroModificaMovimentoGestione() ? 0 : 1;
		} else {
          return -1;
      }
  }
}

