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
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoMovimentoSpesaAction extends ActionKeyAggiornaImpegno {

	
	protected boolean ricaricaDopoInserimento = false;
	
	public ElencoMovimentoSpesaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	private static final long serialVersionUID = 9176837164844056485L;

	private boolean successInsert = false;
	private boolean opeazione = false;
	
	private String uidPerDettaglioModImpegno;
	private String uidModDaAnnullare;
	private String numeroMovDaAnnullare;
	
	//SIAC-7349 Inizio  SR180 MC 08/04/2020
	List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata ;
	//SIAC-7349 Fine  SR180 MC 08/04/2020
	
	/**
	 * Metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//SIAC-8034
		resetPageNumberTableId("ricercaSubImpegniID");
		
		//setto il titolo:
		super.model.setTitolo("Movimento Spesa");
	}
	
	/**
	 * Metodo execute della action
	 */
	@Override
	public String execute() throws Exception {
		
		
		//APRILE 2016. Ottimizzazioni ricerca impegno faccio
		//richiamare sempre il servizio dicendogli di caricare solo le
		//modifiche, che prima ho evitato di fargli caricare:
		forceReload = true;
		//
		
		//carichiamo le labels:
		caricaLabelsAggiorna(1);
		
		if(forceReload || ricaricaDopoInserimento){
			//ricarichiamo completamente i dati
			caricaDatiTotali(null);
		}else {
			//ci basiamo su dati gia caricati
			
			if(model.getMovimentoSpesaModel().getImpegno()==null && model.getImpegnoInAggiornamento()!=null){
				model.getMovimentoSpesaModel().setImpegno(model.getImpegnoInAggiornamento());
				
				//componiamo una lista modifiche comprendente sia quelle del movimento che quelle dei suoi sub:
				List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
				
				//aggiungo quelle del movimento:
				if(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa() != null && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().size() > 0){
					listaModifiche.addAll(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa());
				}
	
				//aggiungo quelle dei sub:
				if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0){
					for(SubImpegno sub : model.getListaSubimpegni()){
						if(sub.getListaModificheMovimentoGestioneSpesa() != null && sub.getListaModificheMovimentoGestioneSpesa().size() > 0){					
							listaModifiche.addAll(sub.getListaModificheMovimentoGestioneSpesa());
						}				 
					}
				}
				
				//ordiniamo la lista:
				Collections.sort(listaModifiche, new NumComparator());
				
				//carico l'atto amministrativo delle modifiche
				List<ModificaMovimentoGestioneSpesa> appList = new ArrayList<ModificaMovimentoGestioneSpesa>();
				//ciclo sulla lista di modifiche appena composta e arricchisco
				//ognuna di esse con il dato dell'atto amministrativo
				for(ModificaMovimentoGestioneSpesa app : listaModifiche){
					if(app.getAttoAmministrativo() != null){
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
				
				//app list e' uguale a listaModifiche con l'arricchimento dell'atto amministrativo,
				//travado quindi appList in listaModifiche:
				listaModifiche = appList;
				
				
				
				//setto la lista modifiche nel model:
				model.getMovimentoSpesaModel().setListaModifiche(listaModifiche);
			}	

		}
		//SIAC-7349 MR CONT-228 05/06/2020: fix disponibilita impegnare componente per annulla modifica
		uploadDisponibilitaPerSingolaModifica();
		
		//ok	
		return SUCCESS;
	}
	
	
	

	/**
	 * Metodo che ricarica i dati
	 * @param impegnoFresco
	 */
	private void caricaDatiTotali(Impegno impegnoFresco){
	
		List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
		
		// Raffa:
		// Era commentato! Ricarica impegnoInAggiornamento 
		// che risulta essere null dopo l'annulla della modifica importo
		RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
		BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
		
		//setto i dati di chiave dell'impegno:
		impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		impegnoDaCercare.setNumeroImpegno(numeroImpegno);
		impegnoDaCercare.setAnnoImpegno(model.getStep1Model().getAnnoImpegno());
				
		//richiedente:
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		//ente:
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);
		
		//non carichiamo i sub:
		parametroRicercaPerChiave.setCaricaSub(false);
		
		//imposto i dati opzionali:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaElencoModificheMovGest(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		
		//PER SIAC-5785 - serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
		//il cup viene gratis assieme al cig (sono attr entrambi vedi funzonamento servizio)
		//quindi tanto vale farlo caricare:
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//
		
		parametroRicercaPerChiave.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
			
		//richiamo il servizio di ricerca:
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(parametroRicercaPerChiave);
		
		//analizzo la risposta del servizio:
		if(!respRk.isFallimento() && respRk.getImpegno()!= null){
			impegnoFresco = respRk.getImpegno();
		}
		
		// .........era commentato sino a qui!

		
		//SETTO L'IMPEGNO NEL MODEL:
		if(impegnoFresco!=null){
			model.setImpegnoRicaricatoDopoInsOAgg(impegnoFresco);
		}
		if(model.getImpegnoRicaricatoDopoInsOAgg()!= null){
			model.setImpegnoInAggiornamento(model.getImpegnoRicaricatoDopoInsOAgg());
			caricaDati(true);
			model.setImpegnoRicaricatoDopoInsOAgg(null);
		}
		
		//APRILE 2016 - ottimizzazioni sub
		List<SubImpegno> elencoSubImpegni = null;
		if(respRk!=null){
			elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
		}
		
		model.getStep1Model().setImportoImpegno(model.getImpegnoInAggiornamento().getImportoAttuale());
		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(model.getImpegnoInAggiornamento().getImportoAttuale()));
		model.setTotaleSubImpegno(model.getImpegnoInAggiornamento().getTotaleSubImpegniBigDecimal());
		model.setDisponibilitaSubImpegnare(model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare());
		
		if (model.getImpegnoInAggiornamento() != null && elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
			model.setListaSubimpegni(elencoSubImpegni);
		}else model.setListaSubimpegni(new ArrayList<SubImpegno>());		
		
		
		//componiamo una lista modifiche comprendente sia quelle del movimento che quelle dei suoi sub:
		
		//aggiungo quelle del movimento:
		if(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa() != null && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().size() > 0){
			listaModifiche.addAll(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa());
		}

		//aggiungo quelle dei sub:
		if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0){
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(sub.getListaModificheMovimentoGestioneSpesa() != null && sub.getListaModificheMovimentoGestioneSpesa().size() > 0){					
					listaModifiche.addAll(sub.getListaModificheMovimentoGestioneSpesa());
				}				 
			}
		}
		
		//ordiniamo la lista:
		Collections.sort(listaModifiche, new NumComparator());

		//settiamo le modifiche nel model:
		model.getMovimentoSpesaModel().setListaModifiche(listaModifiche);
		model.getMovimentoSpesaModel().setImpegno(model.getImpegnoInAggiornamento());

		//creo il model cache:
		creaMovGestModelCache();
		
		//ripulisco i model di step1:
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		model.setImpegnoRicaricatoDopoInsOAgg(new Impegno());
		
	}
	
	/**
	 * Metodo per annullare un movimento spesa
	 * @return
	 */
	public String annullaMovGestSpesa(){
		
		//informazioni per debug:
		setMethodName("annullaMovGestSpesa");
		debug(methodName, " ----> "+getUidModDaAnnullare());
		debug(methodName, " ----> "+getNumeroMovDaAnnullare());
		
		//1. Individuaimo l'uid del movimento da annullare:
		String uidDaAnnullare = getUidModDaAnnullare();
		model.getErrori().clear();
		
		
		
		//2. Componiamo la request per il servizio:
		AnnullaMovimentoSpesa requestAnnulla = convertiModelPerChiamataServizioAnnulla(uidDaAnnullare);
		requestAnnulla.setVerificaImportiDopoAnnullamentoModifica(true);
		
		//SIAC-7349 - MR - Start - 05/06/2020 Fix per annulla modifica con disponibilita impegnare della componente
		//3. Setto la disponibilita ad impegnare della componente
		requestAnnulla.getImpegno().setDisponibilitaImpegnareComponente(model.getDisponibilitaComponentePerAnnullaModifiche());
		
		//SIAC-8611: verifico se posso effettuare l'annullamento: l'annullamento non deve rendere inconsistenti gli importi					
		BigDecimal importoModificaDaAnnullare = new BigDecimal(0);
		int signumMod = 0;
		
		// task-10
		
			
		boolean residuo = (model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getBilancio().getAnno())?true:false; 
		String statoProvvedimentoModifica ="";	
		
		if(model.getNumeroSubImpegnoMod() == null || model.getNumeroSubImpegnoMod().equals("0")) {
			// sono sull'impegno recupero la mia modifica
			for (ModificaMovimentoGestioneSpesa modifica: model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()) {
				if(new Integer(modifica.getUid()).equals(new Integer(uidDaAnnullare))) {
					signumMod=modifica.getImportoOld().signum();
					importoModificaDaAnnullare = modifica.getImportoOld().negate();
					//task-63
					statoProvvedimentoModifica = modifica.getAttoAmministrativo().getStatoOperativo();
				}			
			}
			
			BigDecimal minImportoCalcolatoMod = model.getDisponibilitaImpegnoModifica()!= null? model.getDisponibilitaImpegnoModifica().negate():new BigDecimal(0);
			BigDecimal maxImportoCalcolatoMod = model.getDisponibilitaComponentePerAnnullaModifiche()!= null? model.getDisponibilitaComponentePerAnnullaModifiche():new BigDecimal(0);
			
			//task-63: se statoProvvedimentoModifica è PROVVISORIO NON FACCIO I CONTROLLI SU DISPONBILITA'
			if(!StatoOperativoAtti.PROVVISORIO.name().equals(statoProvvedimentoModifica)) {
				if(residuo) {
					// nel caso del residuo: non deve fare i controlli sul capitolo ma solo quello su importo impegno attualizzato
					//> minImportoCalcolatoMod --> DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA
					if(!(importoModificaDaAnnullare.compareTo(minImportoCalcolatoMod) >= 0)) {
						addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA.getErrore(getNumeroMovDaAnnullare()));
					}
				} else {
					//importo modifica (negato) deve essere compresa tra importo minimo (importo impegno attualizzato) e importo massimo (disp. capitolo)
					if(!(importoModificaDaAnnullare.compareTo(minImportoCalcolatoMod) >= 0 
							&& importoModificaDaAnnullare.compareTo(maxImportoCalcolatoMod) <= 0)) {
						if (signumMod < 0) {
							addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODNEGATIVA.getErrore(getNumeroMovDaAnnullare()));
						} else {
							addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA.getErrore(getNumeroMovDaAnnullare()));
						}	
					}
				} 
			}	
		} else {
			// sono sul sub recupero la mia modifica, minimo e massimo
			BigDecimal minImportoSubCalcolatoMod = new BigDecimal(0);
			BigDecimal maxImportoSubCalcolatoMod = new BigDecimal(0);
			
			for (SubImpegno sub : model.getListaSubimpegni()) {
				if ((sub.getNumeroBigDecimal().toString()).equals(model.getNumeroSubImpegnoMod())) {
					for (ModificaMovimentoGestioneSpesa modifica: sub.getListaModificheMovimentoGestioneSpesa()) {
						if(new Integer(modifica.getUid()).equals(new Integer(uidDaAnnullare))) {
							signumMod=modifica.getImportoOld().signum();
							importoModificaDaAnnullare = modifica.getImportoOld().negate();
							minImportoSubCalcolatoMod =  sub.getDisponibilitaImpegnoModifica().negate();							
							//task-63
							statoProvvedimentoModifica = modifica.getAttoAmministrativo().getStatoOperativo();
						}			
					}
				}
			}
			
			maxImportoSubCalcolatoMod = model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare();
			
			//task-63: se statoProvvedimentoModifica è PROVVISORIO NON FACCIO I CONTROLLI SU DISPONBILITA'
			if(!StatoOperativoAtti.PROVVISORIO.name().equals(statoProvvedimentoModifica)) {
				if(residuo) {
					// nel caso del residuo: non deve fare i controlli sul capitolo ma solo quelli sull'importo impegno attualizzato
					//> minImportoCalcolatoMod --> DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA
					if(!(importoModificaDaAnnullare.compareTo(minImportoSubCalcolatoMod) >= 0)) {
						addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA.getErrore(getNumeroMovDaAnnullare()));
					}
				} else {
					//importo modifica (negato) deve essere compresa tra importo minimo e importo massimo (presi dall'oggetto)
					if(!(importoModificaDaAnnullare.compareTo(minImportoSubCalcolatoMod) >= 0 
							&& importoModificaDaAnnullare.compareTo(maxImportoSubCalcolatoMod) <= 0)) {
						if (signumMod < 0) {
							addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODNEGATIVA.getErrore(getNumeroMovDaAnnullare()));
						} else {
							addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODPOSITIVA.getErrore(getNumeroMovDaAnnullare()));
						}
					}
				}
			} else {
				//task-63: se lo stato è provvisorio e solo se la modifica (del sub, non vale per impegno) è negativa 
				//         aggiungo un controllo per non sfondare il capitolo
				if (signumMod < 0) {
					if(!(importoModificaDaAnnullare.compareTo(maxImportoSubCalcolatoMod) <= 0)) {
						addErrore(ErroreFin.DISPONIBILITA_INCONGRUENTE_IMPEGNO_MODNEGATIVA.getErrore(getNumeroMovDaAnnullare()));
					}
				}
			}
		}
		
		if(hasErrori()){
			return INPUT;
		}
		
		//3. Invochiamo il servizio:
		AnnullaMovimentoSpesaResponse response = movimentoGestioneFinService.annullaMovimentoSpesa(requestAnnulla);
		
		//controllo errori nella response:
		if(isFallimento(response)){
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		//4. Ricarichiamo i dati:
		caricaDatiTotali(response.getImpegno());
		
		return SUCCESS;
	}
	
	/**
	 * Metodo che compone la request per richiamare il servizio di annullamento
	 * @param uid
	 * @return
	 */
	private AnnullaMovimentoSpesa convertiModelPerChiamataServizioAnnulla(String uid){
		
		AnnullaMovimentoSpesa request = new AnnullaMovimentoSpesa();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());
		
		// setto impegno
		Impegno impegno = new Impegno();
		impegno.setUid(model.getMovimentoSpesaModel().getImpegno().getUid());
		
		// setto uid movimento spesa
		ModificaMovimentoGestioneSpesa mgs = new ModificaMovimentoGestioneSpesa();
		List<ModificaMovimentoGestioneSpesa> listaMGP = new ArrayList<ModificaMovimentoGestioneSpesa>();
		
		//setto l'uid:
		mgs.setUid(Integer.valueOf(uid));
		
		//la lista avra' un solo elemento, quello da annullare:
		listaMGP.add(mgs);
		
		impegno.setListaModificheMovimentoGestioneSpesa(listaMGP);
		
		request.setImpegno(impegno);

		return request;
	}
	
	/**
	 * Metodo per il dettaglio
	 * @return
	 */
	public String dettaglioModPopUp(){
		
		//setto le informazioni di debug:
		setMethodName("dettaglioModPopUp");
		debug(methodName, " ----> "+getUidPerDettaglioModImpegno());

		//individuo il selezionato:
		int selezionato = Integer.valueOf(getUidPerDettaglioModImpegno());
		
		//lo carico dalla lista:
		ModificaMovimentoGestioneSpesa modifica = model.getMovimentoSpesaModel().getListaModifiche().get(selezionato);
		
		//setto i dati nel model di consultazione:
		
		ModificaConsulta mc = new ModificaConsulta();
		mc.setTipoMovimento(MovimentoConsulta.IMPEGNO);
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
	    
	    //SIAC-8834
	    mc.setImpegnoAssociato(modifica.getImpegno());
	    
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

		String descImpegno = model.getImpegnoInAggiornamento().getAnnoMovimento() + "/" + model.getImpegnoInAggiornamento().getNumeroBigDecimal() + 
			" - " + ((model.getImpegnoInAggiornamento().getDescrizione() != null)? model.getImpegnoInAggiornamento().getDescrizione() : "") +
			" - " + convertiBigDecimalToImporto(model.getImpegnoInAggiornamento().getImportoAttuale()) + 
			" (" +  model.getImpegnoInAggiornamento().getDescrizioneStatoOperativoMovimentoGestioneSpesa() + " dal " + convertDateToString(model.getImpegnoInAggiornamento().getDataStatoOperativoMovimentoGestioneSpesa()) + ")";	
		mc.setDescMain(descImpegno);
	    
	    if (modifica.getNumeroSubImpegno() != null) {
	    	SubImpegno sImp = getSubImpegno(modifica.getNumeroSubImpegno(), model.getListaSubimpegni());
		    if (sImp != null) {
				String descSub = sImp.getNumeroBigDecimal() +
					" (" +  sImp.getDescrizioneStatoOperativoMovimentoGestioneSpesa() + " dal " + convertDateToString(sImp.getDataStatoOperativoMovimentoGestioneSpesa()) + ")";	
				mc.setDescSub(descSub);
		    }
	    }
		
		model.setModificaDettaglio(mc);
		
		return "dettaglioModPopUp";
	}
	
	/**
	 * Cerca nell'elenco indicato il sub con il numero ricercato
	 * @param numero
	 * @param elencoSubImp
	 * @return
	 */
	private SubImpegno getSubImpegno (Integer numero, List<SubImpegno> elencoSubImp) {
		if (elencoSubImp != null) for (SubImpegno subImpegno : elencoSubImp) {
			if (subImpegno.getNumeroBigDecimal().intValue() == numero) return subImpegno;
		}
		return null;
	}
	
	public String indietro(){
		setMethodName("indietro");
		return "gotoAggiornaImpegnoStep1";
	}

	public boolean isSuccessInsert() {
		return successInsert;
	}


	public void setSuccessInsert(boolean successInsert) {
		this.successInsert = successInsert;
	}


	public String getUidPerDettaglioModImpegno() {
		return uidPerDettaglioModImpegno;
	}


	public void setUidPerDettaglioModImpegno(String uidPerDettaglioModImpegno) {
		this.uidPerDettaglioModImpegno = uidPerDettaglioModImpegno;
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


	public boolean isOpeazione() {
		return opeazione;
	}


	public void setOpeazione(boolean opeazione) {
		this.opeazione = opeazione;
	}


	public boolean isRicaricaDopoInserimento() {
		return ricaricaDopoInserimento;
	}


	public void setRicaricaDopoInserimento(boolean ricaricaDopoInserimento) {
		this.ricaricaDopoInserimento = ricaricaDopoInserimento;
	}

	/**
	 * @return the listaModificheSpeseCollegata
	 */
	public List<ModificaMovimentoGestioneSpesaCollegata> getListaModificheSpeseCollegata() {
		return listaModificheSpeseCollegata;
	}

	/**
	 * @param listaModificheSpeseCollegata the listaModificheSpeseCollegata to set
	 */
	public void setListaModificheSpeseCollegata(
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata) {
		this.listaModificheSpeseCollegata = listaModificheSpeseCollegata;
	}
	
	//SIAC-7349 - MR - Start - 05/06/2020: Metodo per settare la disponibilita componente ad ogni modifica da annullare
	private void uploadDisponibilitaPerSingolaModifica() {				
		if(model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
			//SIAC-7349
			model.setDisponibilitaComponentePerAnnullaModifiche(BigDecimal.ZERO);
		} else if(model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()){		
			model.setDisponibilitaComponentePerAnnullaModifiche(BigDecimal.ZERO);
		} else {
			if(model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() ){							
				model.setDisponibilitaComponentePerAnnullaModifiche(getDisponibilitaModifica(0));				
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +1) ){
				model.setDisponibilitaComponentePerAnnullaModifiche(getDisponibilitaModifica(1));
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +2) ){
							//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				model.setDisponibilitaComponentePerAnnullaModifiche(getDisponibilitaModifica(2));

			}
		}
			
	}
	
	private BigDecimal getDisponibilitaModifica(int i) {

		if(model.getImportiComponentiCapitolo() == null || model.getImportiComponentiCapitolo().isEmpty()){
			return BigDecimal.ZERO;	
		}else{
			if(i==0){				
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}				
			}else if(i==1){
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}
			}else if (i==2){
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}
			}			
		}			
		return BigDecimal.ZERO;	
	}
		//SIAC-7349 - End


	
}

/**
 * classe di ordinamento della lista modifiche
 * @author 
 *
 */
class NumComparator implements Comparator<ModificaMovimentoGestioneSpesa> {
    @Override
    public int compare(ModificaMovimentoGestioneSpesa objToCampareUno, ModificaMovimentoGestioneSpesa objToCampareDue) {
		if(objToCampareUno!=null && objToCampareDue!=null){
	    	return objToCampareDue.getNumeroModificaMovimentoGestione() < objToCampareUno.getNumeroModificaMovimentoGestione() ? -1 : objToCampareUno.getNumeroModificaMovimentoGestione() ==  objToCampareDue.getNumeroModificaMovimentoGestione() ? 0 : 1;
		} else {
            return -1;
        }
    }
}
