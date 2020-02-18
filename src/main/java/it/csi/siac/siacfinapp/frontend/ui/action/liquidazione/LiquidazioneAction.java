/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.LiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.InserisciLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public abstract class LiquidazioneAction<M extends LiquidazioneModel> extends GenericPopupAction<M> {
	
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Verifica se per l'ente 
	 * e' consentito l'aggiornamento delle liquidazioni
	 * @return
	 */
	public boolean isEnteAbilitatoAggiornaImportoLiq(){
		return Constanti.AGGIORNA_IMPORTO_LIQ.equals(sessionHandler.getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.AGGIORNA_IMPORTO_LIQ));
	}
	
	/**
	 * Ricerca dell'impegno tramite compilazione guidata
	 * @return
	 */
	public String ricercaImpegnoCompilazioneGuidata(){		
		
		//puliamo i dati nel model:
		pulisciListeeSchedaPopup();
		
		//marchiamo che siamo passati dal pop up:
		model.setInPopup(true);
		
		//istanzio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//leggo anno e numero impegno dalla pagina:
		HttpServletRequest request = ServletActionContext.getRequest();		
		String annoimpegno = request.getParameter("anno");
		String numeroimpegno = request.getParameter("numero");
		
		//Controlli su anno e numero:
		listaErrori = controllaAnnoENumeroPerRicercaImpegnoCompilazioneGuidata(annoimpegno, numeroimpegno,listaErrori);
		
		if (!listaErrori.isEmpty()) {
			// ci sono errori
			addErrori(listaErrori);
			return "refreshPopupModalImpegno";
		}
		
		//compongo la request per la ricerca dell'impegno:
		RicercaImpegnoPerChiaveOttimizzato rip = builRequestPerRicercaImpegnoCompilazioneGuidata(annoimpegno, numeroimpegno);
		
		//invoco il servizio di ricerca:
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);

		//analizzo la response del servizio:
		if (respRk != null && respRk.getImpegno() != null) {
			Impegno impegno = respRk.getImpegno();
			
			//Sempre in seguito alle ottimizzazioni uso la lista sub impegni minimale:
			impegno.setElencoSubImpegni(respRk.getElencoSubImpegniTuttiConSoloGliIds());
			//
			
			//FIX PER JIRA SIAC-3481 (non settava il capitolo):
			if (impegno.getCapitoloUscitaGestione() != null) {
				model.setCapitolo(impegno.getCapitoloUscitaGestione());
			}
			//
			
				if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D") || impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("N")){
				//metto qui le info per la tabella impegno da visualizzaree sempre
					
					//setting di capitolo, provvedimento e soggetto:
					settaCapitoloProvvedimentoSoggettoPopUp(impegno);
					//
					
					model.setImpegnoPopup(impegno);
					model.setDescrizioneImpegnoPopup(impegno.getDescrizione());
					model.setHasImpegnoSelezionatoPopup(true);
					model.setHasImpegnoSelezionatoXPopup(true);
					List<VoceMutuo> listaVociMutuo = new ArrayList<VoceMutuo>();
					if(impegno!=null && impegno.getListaVociMutuo()!=null && impegno.getListaVociMutuo().size()>0){
						listaVociMutuo = impegno.getListaVociMutuo();
						model.setHasMutui(true);
					}
					model.setListaVociMutuo(listaVociMutuo);
				
				
				//verifico se presenti subimpegni, in questo caso popolo la tabella
				List<Impegno> listaTabellaImpegni = new ArrayList<Impegno>();		
				
				List<SubImpegno> elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
				
				if (elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
					
					// jira 3190
					// prendo solo i sub che NON sono annullati
					for(int i = 0; i < elencoSubImpegni.size(); i++){
						SubImpegno subImp = clone(elencoSubImpegni.get(i));
						if(!subImp.getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_ANNULLATO)){
							
							// caso in cui il subimpegnp NON ha capitolo, gli imposto quello dell'impegno:
							if(subImp.getCapitoloUscitaGestione()==null){
								subImp.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
							}
							listaTabellaImpegni.add((Impegno)subImp);
						}
					}
					
					if(!listaTabellaImpegni.isEmpty()){
						model.setIsnSubImpegno(true);
						model.setImpegno(impegno);
					}else{
						listaTabellaImpegni.add(impegno);
						
						model.setImpegno(impegno);
						
						model.setnSubImpegno(null);
						model.setIsnSubImpegno(false);
					}
				}else{
					listaTabellaImpegni.add(impegno);
					
					model.setImpegno(impegno);
					
					//agiorno nsubImpegno e nisSubImpegno variabile utilita' popup
					model.setnSubImpegno(null);
					model.setIsnSubImpegno(false);
    				//ISSUE R35
				}			
				model.setListaImpegniCompGuidata(listaTabellaImpegni);		
				model.setImpegnoSelezionato(true);
			}else{
				//ISSUE R35
				addErrore(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("Impegno in stato: "+ impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()));
			}			
		}else{
			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));	
			if (!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				model.setInPopup(true);
				return "refreshPopupModalImpegno";
			}
		}
		return "refreshPopupModalImpegno";
	}
	
	/**
	 * Controlla la validita' dei dati immessi in anno e numero impegno
	 * @param annoimpegno
	 * @param numeroimpegno
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> controllaAnnoENumeroPerRicercaImpegnoCompilazioneGuidata(String annoimpegno,String numeroimpegno, List<Errore> listaErrori){
		if (annoimpegno != null && !annoimpegno.isEmpty()) {
			try {
				Integer anno = Integer.valueOf(annoimpegno);
				model.setnAnno(anno);
				if (anno <= 1900) {
					//anno troppo antico
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno", annoimpegno, ">1900"));
				}
			} catch (NumberFormatException e) {
				//valore non valido
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", annoimpegno));
			}
		} else {
			//dato omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Anno "));
			model.setnAnno(null);
		}
		
		if (numeroimpegno != null && !numeroimpegno.isEmpty()) {
			try {
			model.setnImpegno(Integer.valueOf(numeroimpegno));
			} catch (NumberFormatException e) {
				//non e' un numero
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", numeroimpegno));
			}

		} else {
			//dato omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Numero "));
			model.setnImpegno(null);
		}
		return listaErrori;
	}

	/**
	 * Costruisce la request per la ricerca 
	 * dell'impegno da compilazione guidata
	 * @param annoimpegno
	 * @param numeroimpegno
	 * @return
	 */
	private RicercaImpegnoPerChiaveOttimizzato builRequestPerRicercaImpegnoCompilazioneGuidata(String annoimpegno,String numeroimpegno){
		
		//istanzio la request:
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			
		//popoliamo la request:
		
		//ente
		rip.setEnte(sessionHandler.getEnte());
		
		//richiedente
		
		rip.setRichiedente(sessionHandler.getRichiedente());
		
		//chiave di ricerca dell'impegno:
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(annoimpegno!=null){
			//anno
			k.setAnnoImpegno(new Integer(annoimpegno));
		}
		if(numeroimpegno!=null){
			//numero
			k.setNumeroImpegno(new BigDecimal(numeroimpegno));
		}
		rip.setpRicercaImpegnoK(k);
		
		//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		rip.setCaricaSub(false);
		//
		
		//Dati opzionali necessari:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		return rip;
	}
	
	
	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}
	
	@Override
	public String selezionaSoggetto() {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(StringUtils.isEmpty(getRadioCodiceSoggetto())){
			if (model.getListaRicercaSoggetto() != null && model.getListaRicercaSoggetto().size() > 0 && !model.isSoggettoTrovato()) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
				addErrori(listaErrori);
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
			}
			return INPUT;
		} else {
			//soggetto selezionato, devo individuarlo:
			for(Soggetto s: model.getListaRicercaSoggetto()){
				if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
					popolaSoggetto(s);
				}
			}
		}
		return SUCCESS;
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		caricaListeCreditore(soggettoImpegnoModel.getCodCreditore());
		model.setSoggettoSelezionato(true);
		model.setClasseImpegno(soggettoImpegnoModel.getClasse());
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);	
		model.setLockProvvedimento(true);
	}	

	//questo deve essere completato con popup, quindi spostato in GenericPopupAction
	protected void setImpegnoSelezionato(){
	}

	/**
	 * Setta il capitolo selezionato nel model
	 */
	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		model.setDatoPerVisualizza(supportCapitolo);
		model.setListaRicercaCapitolo(null);
		model.setCapitoloTrovato(false);
	}

	@Override
	protected void setErroreCapitolo() {
		// Auto-generated method stub
	}
	
	/**
	 * Effettua il caricamento delle liste creditore (mod pag e sedi secondarie)
	 * @param codiceSoggetto
	 */
	protected void caricaListeCreditore(String codiceSoggetto){	
		//invochiamo il servizio di ricerca soggetto:
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(convertiModelPerChiamataServizioRicercaPerChiave(codiceSoggetto));
		if(response!=null){
			//leggo i dati ottenuti:
			ArrayList<ModalitaPagamentoSoggetto> listMod= FinUtility.filtraValidi(response.getListaModalitaPagamentoSoggetto());
			model.setListaModalitaPagamentoSoggetto(listMod);
			model.setListaModalitaPagamentoSoggettoOrigine(listMod);
			model.setListaSediSecondarieSoggetto(response.getListaSecondariaSoggetto());
		}
	}
	
	/**
	 * Carica le liste creditore
	 * 
	 * @param soggetto
	 * @param codiceSoggetto
	 */
	protected void caricaListeCreditore(Soggetto soggetto, String codiceSoggetto){	
		if(soggetto!=null && soggetto.getUid() > 0){
			//abbiamo l'uid
			
			//componiamo i dati per il model:
			ArrayList<ModalitaPagamentoSoggetto> listMod= FinUtility.filtraValidi(soggetto.getModalitaPagamentoList());
			listMod = FinUtility.soloMDPNonScadute(listMod);
			model.setListaModalitaPagamentoSoggetto(listMod);
			model.setListaModalitaPagamentoSoggettoOrigine(listMod);
			model.setListaSediSecondarieSoggetto(soggetto.getSediSecondarie());
			
			//log per diagnostica:
			if(log.isDebugEnabled()){
				//log delle mod pag
				for (ModalitaPagamentoSoggetto modalitaPagamentoSoggetto : listMod) {
					System.out.println("modalitaPagamentoSoggetto.uid: " + modalitaPagamentoSoggetto.getUid());
					System.out.println("modalitaPagamentoSoggetto.descrizione: " + modalitaPagamentoSoggetto.getDescrizione());
				}
				//log delle sedi secondarie
				if(soggetto.getSediSecondarie()!=null){
					for (SedeSecondariaSoggetto sede : soggetto.getSediSecondarie()) {
						System.out.println("sede.uid: " + sede.getUid());
						System.out.println("sede.codie-denominazione: " + sede.getCodiceSedeSecondaria() + " - " + sede.getDenominazione());
					}
				}
			}
			
		}else{
			//abbiamo il codice,
			//richiamo la ricerca:
			caricaListeCreditore(codiceSoggetto);
		}
	}
	
	/**
	 * Costruisce la request per il servizio di ricerca soggetto 
	 * per chiave
	 * @param codiceSoggetto
	 * @return
	 */
	protected RicercaSoggettoPerChiave convertiModelPerChiamataServizioRicercaPerChiave(String codiceSoggetto) {
		//istanzio la request:
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		//setto i parametri:
		ricercaSoggettoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggettoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		parametroRicercaSoggettoK.setIncludeModif(false);
		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroRicercaSoggettoK);
		//ritorno la request al chiamante:
		return ricercaSoggettoPerChiave;
	}
	
	/**
	 * Per verificare se il codice corrisponde ad un siope esistente
	 * @param cod
	 */
	@Override
	protected void codiceSiopeChangedInternal(String cod){
		//richiamo il metodo centralizzato 
		//passandogli la tipologia di lista riguardante la liquidazione:
		codiceSiopeChangedInternal(cod, TipiLista.TIPO_SIOPE_SPESA_I);
	}
	
	/**
	 * Setta il siope trovato nel model
	 */
	@Override
	protected void setSiopeSpesaSelezionato(SiopeSpesa siopeTrovato,boolean trovato) {
		teSupport.setSiopeSpesaCod(siopeTrovato.getCodice());
		teSupport.setSiopeSpesa(siopeTrovato);
		teSupport.setTrovatoSiopeSpesa(trovato);
	}
	
	/**
	 * imposta i dati del sub o impegno indicato, 
	 * serve per mettere i default alla creazione di una liquidazione
	 * ereditando tali default dal sub o imp
	 * @param impegoOSubDaCuiLeggere
	 * @param modelInCuiImpostare
	 */
	protected void impostaDatiSiopePlusNelModel(Impegno impegoOSubDaCuiLeggere,InserisciLiquidazioneModel modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(codiceMotivazioneAssenzaCig(impegoOSubDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(impegoOSubDaCuiLeggere.getSiopeTipoDebito()));
	}
	
	/**
	 * imposta i dati della liquidazione indicata, 
	 * serve per mettere i default nello scenario 
	 * di aggiornamento di una liquidazione
	 * 
	 * @param liqDaCuiLeggere
	 * @param modelInCuiImpostare
	 */
	protected void impostaDatiSiopePlusNelModel(Liquidazione liqDaCuiLeggere,InserisciLiquidazioneModel modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(codiceMotivazioneAssenzaCig(liqDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(liqDaCuiLeggere.getSiopeTipoDebito()));
	}
	
	/**
	 * Messi a fattor comune i controlli di formato su CIG, CUP e nuovi campi SIOPE PLUS
	 * 
	 * Fa l'addError degli eventuli errori
	 * 
	 * @param step1Model
	 */
	protected void controlliCigCupESiopePlus(InserisciLiquidazioneModel modelLiq){
		
		//instazio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//metodo core che si basa sulla lista errori:
		controlliCigCupESiopePlus(modelLiq, listaErrori);
		
		//aggiungo gli errori in pagina:
		if(!isEmpty(listaErrori)){
			addErrori(listaErrori);
		}
	}
	
	/**
	 * Messi a fattor comune i controlli di formato su CIG, CUP e nuovi campi SIOPE PLUS
	 * 
	 * Aggiunge gli eventuali errori a listaErrori
	 * @param step1Model
	 * @param listaErrori
	 */
	protected void controlliCigCupESiopePlus(InserisciLiquidazioneModel modelLiq, List<Errore> listaErrori ){
		// controllo di congruenza di CIG e CUP
	    if(modelLiq.getCig()!=null && StringUtils.isNotEmpty(modelLiq.getCig())){
	    	// controllo di congruenza CIG
	    	if(!FinUtility.cigValido(modelLiq.getCig())){
	    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("CIG"));
	    	}
	    }
	    if(modelLiq.getCup()!=null && StringUtils.isNotEmpty(modelLiq.getCup())){
	    	// controllo di congruenza CIG
	    	if(!FinUtility.cupValido(modelLiq.getCup())){
	    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("CUP"));
	    	}
	    } 
	    
	    //SIOPE PLUS:
	    if(FinStringUtils.entrambiValorizzati(modelLiq.getCig(),modelLiq.getMotivazioneAssenzaCig())){
	    	//CIG e Motivazione assenza CIG non possono coesistere
	    	listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", " (Compilare un solo campo tra CIG e Motivazione assenza CIG)"));
	    }
	    
	    if(isTipoCommerciale(modelLiq.getTipoDebitoSiope())
	    		&& FinStringUtils.entrambiVuoti(modelLiq.getCig(),modelLiq.getMotivazioneAssenzaCig())){
	    	//SE "Tipo SIOPE" = Commerciale
	        //CIG o, in alternativa, Motivazione assenza CIG sono obbligatori 
	    	listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo debito siope", " (Per tipo debito Commerciale occorre indicare il CIG oppure la Motivazione assenza CIG )"));
	    }
	    
	    //Motivazione assenza cig "Da definire in fase di liquidazione" non valida per la liquidazione:
	    if(Constanti.ASSENZA_CIG_DA_DEFINIRE_IN_FASE_DI_LIQUIDAZIONE.equalsIgnoreCase(modelLiq.getMotivazioneAssenzaCig())){
	    	//SIAC-5526
	    	listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("CIG", "Cig da definire in fase di liquidazione non accettabile"));
	    }
	    
	    //Motivazione assenza cig "CIG in corso di definizione" non valida per la liquidazione:
	    if(Constanti.ASSENZA_CIG_CODE_IN_CORSO_DI_DEFINIZIONE.equalsIgnoreCase(modelLiq.getMotivazioneAssenzaCig())){
	    	//SIAC-5543
	    	listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("CIG", "Cig in corso di definizione non accettabile"));
	    }
	}
	
	/**
	 * prende i dati in InserisciLiquidazioneModel e li travasa nell'oggetto liquidazione che
	 * sta per essere usato in inserimento o aggiornamento service
	 * @param datiImpegno
	 * @param impegno
	 */
	protected void impostaDatiSiopePlusPerInserisciAggiorna(InserisciLiquidazioneModel modelDaCuiLeggere, Liquidazione liquidazione){
		//---------- SIOPE PLUS ----------
		
		//SIOPE TIPO DEBITO:
		SiopeTipoDebito siopeTipoDebito = siopeTipoDebitoDaSceltaNelModel(modelDaCuiLeggere.getTipoDebitoSiope());
		liquidazione.setSiopeTipoDebito(siopeTipoDebito);
		
		//SIOPE MOTIVAZIONE ASSENZA CIG:
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = siopeAssenzaMotivazioneDaSceltaNelModel(modelDaCuiLeggere.getMotivazioneAssenzaCig());
		liquidazione.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		//---------- FINE SIOPE PLUS ----------
	}
	
}
