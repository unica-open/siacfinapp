/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegnoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSubimpegnoAction extends AggiornaSubGenericAction {

	private static final long serialVersionUID = 1L;
	
	public boolean ricaricaPagina;
	public boolean status;
	
	public AggiornaSubimpegnoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBIMPEGNO;
	}
	
	@Override
	public String getActionKey() {
		return "aggiornaImpegno";
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		model.setTitolo("Aggiorna Subimpegno");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//SIAC-6032
		leggiEventualiErroriEWarningAzionePrecedente(true);
		if(ricaricaPagina){
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
			return reload();
		}

		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUBIMPEGNI, new ArrayList<SubImpegno>());
		model.setResultSize(0);

		RicercaSubImpegniDiUnImpegnoResponse respoSub = executeRicercaSubImpegni();
		if(respoSub == null){
			//errori
			return INPUT;
		}
		
		setStatus(true);
		
		caricaDati(true);
		
		//APRILE 2016 -ATTENZIONE il caricaDati pulisce la listaSubImpegni che va ripristinata:
		if(respoSub!=null){
			//PAGINA
			List<SubImpegno> paginaSubImpegni = (respoSub.getElencoSubImpegni()!=null? respoSub.getElencoSubImpegni() : new ArrayList<SubImpegno>());
			model.setListaSubimpegni(paginaSubImpegni);
			//TUTTI CON DATI MINIMI:
			model.getImpegnoInAggiornamento().setElencoSubImpegni(respoSub.getElencoSubImpegniTuttiConSoloGliIds());
			model.setListaTuttiSubimpegniDatiMinimi(respoSub.getElencoSubImpegniTuttiConSoloGliIds());
		}
		//
		
		caricaLabelsAggiornaSub();
	
	    return SUCCESS;
	}   
	
	protected RicercaSubImpegniDiUnImpegnoResponse executeRicercaSubImpegni() {

		RicercaSubImpegniDiUnImpegno ricercaSubImpegniDiUnImpegno = convertiModelPerChiamataServizioRicercaSubImpegni();
		RicercaSubImpegniDiUnImpegnoResponse respoSub = movimentoGestioneFinService.ricercaSubImpegniDiUnImpegno(ricercaSubImpegniDiUnImpegno );
		
		if(respoSub.isFallimento()){
			addErrori(methodName, respoSub);
			return null;
		}
		
		List<SubImpegno> listaCompleta = (respoSub.getElencoSubImpegni()!=null? respoSub.getElencoSubImpegni() : new ArrayList<SubImpegno>());
		
		//Metto in sessione la lista ricevuta
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUBIMPEGNI, listaCompleta);
		model.setListaSubimpegni(listaCompleta);
		//per retro-compatibilita' va popolata anche:
		model.setImpegnoRicaricatoDopoInsOAgg(respoSub.getImpegno());
		model.setImpegnoInAggiornamento(respoSub.getImpegno());
		model.getImpegnoInAggiornamento().setElencoSubImpegni(listaCompleta);
		
		model.setResultSize(respoSub.getNumRisultati());
		
		return respoSub;
		    
	}
	
	
	public String reload() {
		setMethodName("reload");
		
		debug(methodName, "entro nel reload ");
		
		RicercaSubImpegniDiUnImpegno ricercaSubImpegniDiUnImpegno = convertiModelPerChiamataServizioRicercaSubImpegni();
		RicercaSubImpegniDiUnImpegnoResponse respoSub = movimentoGestioneFinService.ricercaSubImpegniDiUnImpegno(ricercaSubImpegniDiUnImpegno );
		
		//Controllo che il servizio non restituisca errori
		if(respoSub.isFallimento()) {
			addErrori(methodName, respoSub);
			return INPUT;
		}
		//Metto in sessione la lista ricevuta per utilizzarla in Elenco Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUBIMPEGNI, respoSub.getElencoSubImpegni());
		model.setListaSubimpegni(respoSub.getElencoSubImpegni());
		//per retro-compatibilita' va popolata anche:
		model.setImpegnoRicaricatoDopoInsOAgg(respoSub.getImpegno());
		model.setImpegnoInAggiornamento(respoSub.getImpegno());
		model.getImpegnoInAggiornamento().setElencoSubImpegni(respoSub.getElencoSubImpegni());
		
		
		return SUCCESS;
		
	}	
	
	
	private RicercaSubImpegniDiUnImpegno convertiModelPerChiamataServizioRicercaSubImpegni(){
		RicercaSubImpegniDiUnImpegno ricercaSubImpegniDiUnImpegno = new RicercaSubImpegniDiUnImpegno();
		ricercaSubImpegniDiUnImpegno.setEnte(sessionHandler.getEnte());
		ricercaSubImpegniDiUnImpegno.setRichiedente(sessionHandler.getRichiedente());
		
		RicercaSubImpegniDiUnImpegno riImpChiave = new RicercaSubImpegniDiUnImpegno();
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		
		pRicercaImpegnoK.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		pRicercaImpegnoK.setAnnoImpegno(model.getImpegnoInAggiornamento().getAnnoMovimento());
		pRicercaImpegnoK.setCaricaDatiUlteriori(true);
		pRicercaImpegnoK.setCaricaSediEModalitaPagamento(true);
		pRicercaImpegnoK.setNumeroImpegno(model.getImpegnoInAggiornamento().getNumeroBigDecimal());
		
		riImpChiave.setpRicercaImpegnoK(pRicercaImpegnoK);
		ricercaSubImpegniDiUnImpegno.setpRicercaImpegnoK(riImpChiave.getpRicercaImpegnoK());
		
		//SCEGLIAMO PAGINAZIONE SU DATI MINIMI PER MIGLIORARE LE PERFORMANCE (cosi evitiamo di caricare proprio TUTTO per l'intera pagina di 10 sub impegni) :
		ricercaSubImpegniDiUnImpegno.setPaginazioneSuDatiMinimi(true);

		//ALCUNI DATI della paginazione su dati minimi DEVO ESSERE ESPLICITAMENTE RICHIESTI:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		ricercaSubImpegniDiUnImpegno.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		//
		
		addNumAndPageSize(ricercaSubImpegniDiUnImpegno, "ricercaSubImpegniID");
		
		return ricercaSubImpegniDiUnImpegno;
	}
	
	public String dettaglioSubPopUp(){
		setMethodName("dettaglioSubPopUp");
		SubImpegno sub = model.getListaSubimpegni().get(Integer.valueOf(getUidPerDettaglioSub()));
	
		MovimentoConsulta mc = new MovimentoConsulta();
		mc.setTipoMovimento(MovimentoConsulta.IMPEGNO);
		// movimento
		mc.setAnno(String.valueOf(sub.getAnnoMovimento()));
		mc.setNumero(String.valueOf(sub.getNumeroBigDecimal()));
		mc.setDescrizione(sub.getDescrizione());
	    mc.setImporto(sub.getImportoAttuale());
	    mc.setImportoIniziale(sub.getImportoIniziale());
	    if (sub.getUtenteCreazione() != null) 	mc.setUtenteCreazione(sub.getUtenteCreazione());
	    if (sub.getUtenteModifica() != null) 	mc.setUtenteModifica(sub.getUtenteModifica());
	    mc.setDataInserimento(sub.getDataEmissione());
	    mc.setDataModifica(sub.getDataModifica());
	    mc.setStatoOperativo(sub.getDescrizioneStatoOperativoMovimentoGestioneSpesa());
	    mc.setDataStatoOperativo(sub.getDataStatoOperativoMovimentoGestioneSpesa());
	    if (sub.getTipoImpegno() != null)		mc.setTipo(sub.getTipoImpegno().getDescrizione());
	    if (sub.getDataScadenza() != null)	    mc.setDataScadenza(sub.getDataScadenza());
	    mc.setCig(sub.getCig());
	    mc.setCup(sub.getCup());
	    if (sub.getProgetto() != null)			mc.setProgetto(sub.getProgetto().getCodice());
	
	    // descrizione impegno del subimpegno
	    Impegno impegno = model.getImpegnoInAggiornamento();
		String descImpegno = impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal() + 
			" - " + impegno.getDescrizione() +
			" - " + convertiBigDecimalToImporto(impegno.getImportoAttuale()) + 
			" (" +  impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa() + " dal " + convertDateToString(impegno.getDataStatoOperativoMovimentoGestioneSpesa()) + ")";	
		mc.setDescSuper(descImpegno);
	
	    // disponibilita
	    mc.setDisponibilitaSub(sub.getDisponibilitaSubimpegnare());
	    mc.setTotaleSub(sub.getTotaleSubImpegniBigDecimal());
	    
	    // provvedimento
	    if (sub.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(sub.getAttoAmministrativo().getAnno()));
	    	//6929

	    	if  (sub.getAttoAmministrativo().getBloccoRagioneria()!= null ) {
	    		mc.getProvvedimento().setBloccoRagioneria(((sub.getAttoAmministrativo().getBloccoRagioneria()==true) ? "SI" : "NO" ));
	    	}else {
	    		mc.getProvvedimento().setBloccoRagioneria("N/A");
	    	}
	    
	    
	    	
	    	mc.getProvvedimento().setNumero(String.valueOf(sub.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(sub.getAttoAmministrativo().getOggetto());
	    	if (sub.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(sub.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (sub.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(sub.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	mc.getProvvedimento().setStato(sub.getAttoAmministrativo().getStatoOperativo());
	    }
	    
	    // soggetto
	    if ((sub.getSoggetto() != null) && (sub.getSoggetto().getUid() != 0)) {
		    mc.getSoggetto().setCodice(sub.getSoggetto().getCodiceSoggetto());
		    mc.getSoggetto().setDenominazione(sub.getSoggetto().getDenominazione());
		    mc.getSoggetto().setCodiceFiscale(sub.getSoggetto().getCodiceFiscale());
		    mc.getSoggetto().setPartitaIva(sub.getSoggetto().getPartitaIva());
	    } else if (sub.getClasseSoggetto() != null) {
	    	mc.getSoggetto().setClasseSoggettoCodice(sub.getClasseSoggetto().getCodice());
	    	mc.getSoggetto().setClasseSoggettoDescrizione(sub.getClasseSoggetto().getDescrizione());
	    }
	    
	    // riaccertamento
	    if (sub.isFlagDaRiaccertamento()) 
	    	mc.setDaRiaccertamento(WebAppConstants.MSG_SI + " " + sub.getAnnoRiaccertato() + " / " + sub.getNumeroRiaccertato());
	    else
	    	mc.setDaRiaccertamento(WebAppConstants.MSG_NO);

	    
	    if (sub.isFlagSoggettoDurc()) 
	    	mc.setSoggettoDurc(WebAppConstants.MSG_SI);
	    else
	    	mc.setSoggettoDurc(WebAppConstants.MSG_NO);
	    
	    // disponibilita
	    mc.setDisponibilitaLiquidare(sub.getDisponibilitaLiquidare());
	    mc.setDisponibilitaPagare(sub.getDisponibilitaPagare());
	    mc.setDisponibilitaFinanziare(sub.getDisponibilitaFinanziare());
		
	    //SIOPE PLUS:
	    impostaDatiSiopePlusNelModelDiConsultazione(sub, mc);
	    //
	    
		model.setSubDettaglio(mc);
	
		return "dettaglioSubPopUp";
	}

	public String annullaSubImpegno(){
		setMethodName("annullaSubImpegno");
		AnnullaMovimentoSpesaResponse response = movimentoGestioneFinService.annullaMovimentoSpesa(convertiModelPerChiamataServizioAnnulla(getUidSubDaAnnullare()));
		
		if(!response.isFallimento()){
			
			if(response.getImpegno()!= null){
				//MODIFICA PER EVITARE MEGA QUERY
				model.setImpegnoRicaricatoDopoInsOAgg(response.getImpegno());
			}
			//OTTOBRE 2017 correggendo la JIRA SIAC-5339 mi riconduco a
			//quanto gia' fatto dopo l'azione elimina per le ottimizzazioni aprile 2016:
			//OTTIMIZZAZIONI APRILE 2016, il servizio aggiorna non restituisce piu' l'impegno per non rischiare timeout a caricarlo...
			forceReload=true;
			model.setImpegnoRicaricatoDopoInsOAgg(null);
			ricaricaSubImpegni();
			
			
			addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
	                + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			
			//SIAC-8034
			resetPageNumberTableId("ricercaSubImpegniID");
			
			return GOTO_AGGIORNA_SUBIMPEGNO;
		}else {
			//SIAC-7853
			addErrori(response);
		}
		return SUCCESS;
	}
	
	
	private AnnullaMovimentoSpesa convertiModelPerChiamataServizioAnnulla(String uid){
		AnnullaMovimentoSpesa request = new AnnullaMovimentoSpesa();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(model.getImpegnoInAggiornamento().getAnnoMovimento());
		impegno.setNumeroBigDecimal(model.getImpegnoInAggiornamento().getNumeroBigDecimal());
		impegno.setUid(model.getImpegnoInAggiornamento().getUid());
		
		SubImpegno subImpegnoIntero = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(), Integer.valueOf(uid));
		List<SubImpegno> listaSubImpegnos = new ArrayList<SubImpegno>();
		
		listaSubImpegnos.add(subImpegnoIntero);
		impegno.setElencoSubImpegni(listaSubImpegnos);
		request.setImpegno(impegno);
		return request;
	}
	
	public String eliminaSubImpegno(){
		setMethodName("eliminaSubImpegno");
		if(null!=model.getListaSubimpegni() &&
		   !model.getListaSubimpegni().isEmpty()){
			
			List<SubImpegno> elencoSubImpegniDaEliminare = new ArrayList<SubImpegno>();
			
			for (int i = 0; i < model.getListaSubimpegni().size(); i++) {
				SubImpegno sub = model.getListaSubimpegni().get(i);
				if(sub.getUid() == Integer.valueOf(getUidSubDaEliminare())){
					elencoSubImpegniDaEliminare.add(sub);
				} 	 
			}
			
			//NUOVO FUNZIONAMENTO, NON DEVO PASSARE GLI ALTRI SUB:
			model.setListaSubimpegni(new ArrayList<SubImpegno>());
			
			AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegni(true,false);
			
			//Fix per SIOPE JIRA  SIAC-3913:
			requestAggiorna.getImpegno().setCodSiope(model.getImpegnoInAggiornamento().getCodSiope());
			//
			
			//FEB-MARZO 2016, NUOVO FUNZIONAMENTO. PER LA NUOVA PAGINAZIONE DEI SUB, quelli da eliminare vanno indicati
			//esplicitamente in una lista a parte:
			requestAggiorna.setElencoSubImpegniDaEliminare(elencoSubImpegniDaEliminare);
			//
			
			if (requestAggiorna.getImpegno().getProgetto() != null) {
				Progetto progetto = requestAggiorna.getImpegno().getProgetto();
				
				progetto.setTipoProgetto(TipoProgetto.GESTIONE);
				progetto.setBilancio(sessionHandler.getBilancio());

				requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
				requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
			}
			

			AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna); 
			
			
			if(!response.isFallimento() && response.getImpegno()!= null){
				//OTTIMIZZAZIONI APRILE 2016, il servizio aggiorna non restituisce piu' l'impegno per non rischiare timeout a caricarlo...
				forceReload=true;
				model.setImpegnoRicaricatoDopoInsOAgg(null);
				
				//SIAC-8034
				resetPageNumberTableId("ricercaSubImpegniID");
				
				return GOTO_AGGIORNA_SUBIMPEGNO;
			}else{
				addErrori(response.getErrori());
				return INPUT;
			}
		}
		ricaricaSubImpegni();
		return SUCCESS;
	}
	
	private void ricaricaSubImpegni(){
		if(model.getImpegnoRicaricatoDopoInsOAgg()!= null){
			model.setListaSubimpegni(model.getImpegnoRicaricatoDopoInsOAgg().getElencoSubImpegni());
			model.setDisponibilitaSubImpegnare(model.getImpegnoRicaricatoDopoInsOAgg().getDisponibilitaSubimpegnare());
			model.setTotaleSubImpegno(model.getImpegnoRicaricatoDopoInsOAgg().getTotaleSubImpegniBigDecimal());
			model.setImpegnoInAggiornamento(model.getImpegnoRicaricatoDopoInsOAgg());
			setImpegnoToUpdate(model.getImpegnoRicaricatoDopoInsOAgg());
			caricaDati(true);
			model.setImpegnoRicaricatoDopoInsOAgg(null);
		}
	}
	
	public boolean isAggiornaAbilitatoImpegno(String stato){
		boolean abilitatazioniComuniImpEAcc = super.isAggiornaAbilitato(stato);
		
		if(!abilitatazioniComuniImpEAcc){
			return false;
		}
		
		//SIAC-4949 IMPEGNI e ACCERTAMENTI Aggiornamento per decentrati CR 912
		// quando l'utente ha il'azione DecentratoP deve essere disabilitata l'azione AGGIORNA e ANNULLA 
		//dei subimpegni/subaccertamenti definitivi 
		if(stato.equals("D") && isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP)){
			return false;
		}
		//
			
		return true;
	}
	
	// JIra 1872
	// la disponibilita' a liquidare e' stata sostituita con la disponiblita' in modifica, questo  
	// perche' nell'algoritmo del calcolo disponibilita' a liquidare (lato ser) anche per i sub (come per l'impegno) 
	// quella a liquidare veniva abbattuta (impostata a 0) nel caso in cui 
	// la disponiblita' a vincolare fosse > 0 e ci fossero trasferimenti vincolati
	public boolean isAnnullaAbilitatoImpegno(String stato,BigDecimal disponibilitaImpegnoModifica, BigDecimal importoIniziale){
		
		boolean abilitatazioniComuniImpEAcc = super.isAnnullaAbilitato(stato);
		
		if(!abilitatazioniComuniImpEAcc){
			return false;
		}
		
		boolean abilita = isFaseBilancioAbilitata();
		
		if(abilita == true){
			if(disponibilitaImpegnoModifica.compareTo(importoIniziale)==0){
				abilita= true;
			}else{
				return false;
			}
		}
		
		//SIAC-4949 IMPEGNI e ACCERTAMENTI Aggiornamento per decentrati CR 912
		// quando l'utente ha il'azione DecentratoP deve essere disabilitata l'azione AGGIORNA e ANNULLA 
		//dei subimpegni/subaccertamenti definitivi 
		if(stato.equals("D") && isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP)){
			return false;
		}
		//
		
	    return abilita;
	}

	public boolean isRicaricaPagina() {
		return ricaricaPagina;
	}

	public void setRicaricaPagina(boolean ricaricaPagina) {
		this.ricaricaPagina = ricaricaPagina;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	//SIAC-7667
	protected boolean isPerimetroSanitarioCongruenteConGsa(CapitoloImpegnoModel capitolo) {
		return capitolo.getCodicePerimetroSanitarioSpesa() != null && CODICE_PERIMETRO_SANITARIO_SPESA_GSA.equals(capitolo.getCodicePerimetroSanitarioSpesa());
	}

}