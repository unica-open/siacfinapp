/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSubaccertamentoAction extends AggiornaSubGenericAction {

	private static final long serialVersionUID = 1L;
	private static final String SI = null;

	public AggiornaSubaccertamentoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBACCERTAMENTO;
	}
	
	@Override
	public String getActionKey() {
		return "aggiornaAccertamento";
	}
	
	/**
	 * Metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		model.setTitolo("Aggiorna Subaccertamento");
	}
	
	/**
	 * Metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		//setto il flag prosegui con warning a false:
		model.setProseguiConWarningControlloSoggetti(false);
		//valuto se ricaricare i contenuti:
		if(ricaricaDopoInserimento || forceReload){
			ricaricaSubAccertamenti(false);
		}
		//caricamento delle label:
		caricaLabelsAggiornaSub();
		//SIAC-6032
		leggiEventualiErroriEWarningAzionePrecedente(true);
	    return SUCCESS;
	}   
	
	/**
	 * Metodo per l'evento dettaglio sub
	 * @return
	 */
	public String dettaglioSubPopUp(){
		setMethodName("dettaglioSubPopUp");
		//individuiamo il sub in questione:
		SubAccertamento sub = model.getListaSubaccertamenti().get(Integer.valueOf(getUidPerDettaglioSub()));

		//settiamone i dati nel model:
		MovimentoConsulta mc = new MovimentoConsulta();
		mc.setTipoMovimento(MovimentoConsulta.ACCERTAMENTO);
		// movimento
		mc.setAnno(String.valueOf(sub.getAnnoMovimento()));
		mc.setNumero(String.valueOf(sub.getNumero()));
//	    mc.setBloccoRagioneria( ((sub.getAttoAmministrativo().getBloccoRagioneria()==true ? "SI" : (sub.getBloccoRagioneria()==false) ? "NO" : "N/A")));
	    
	    
	    
		mc.setDescrizione(sub.getDescrizione());
	    mc.setImporto(sub.getImportoAttuale());
	    mc.setImportoIniziale(sub.getImportoIniziale());
	    if (sub.getUtenteCreazione() != null) 	mc.setUtenteCreazione(sub.getUtenteCreazione());
	    if (sub.getUtenteModifica() != null) 	mc.setUtenteModifica(sub.getUtenteModifica());
	    mc.setDataInserimento(sub.getDataEmissioneSupport());
	    mc.setDataModifica(sub.getDataModifica());
	    mc.setStatoOperativo(sub.getDescrizioneStatoOperativoMovimentoGestioneEntrata());
	    mc.setDataStatoOperativo(sub.getDataStatoOperativoMovimentoGestioneEntrata());
	    if (sub.getTipoImpegno() != null)		mc.setTipo(sub.getTipoImpegno().getDescrizione());
	    if (sub.getDataScadenza() != null)	    mc.setDataScadenza(sub.getDataScadenza());
        if (sub.getProgetto() != null)			mc.setProgetto(sub.getProgetto().getCodice());

        // descrizione impegno del subaccertamento
        Accertamento accertamento = model.getAccertamentoInAggiornamento();
        if(accertamento.getDescrizione()== null){
        	accertamento.setDescrizione("");
        }
        
        //compongo la descrizione:
		String descAccertamento = accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + 
			" - " + accertamento.getDescrizione() +
			" - " + convertiBigDecimalToImporto(accertamento.getImportoAttuale()) + 
			" (" +  accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata() + " dal " + convertDateToString(accertamento.getDataStatoOperativoMovimentoGestioneEntrata()) + ")";	
		mc.setDescSuper(descAccertamento);

	    // disponibilita
	    mc.setDisponibilitaSub(sub.getDisponibilitaSubAccertare());
	    mc.setTotaleSub(sub.getTotaleSubAccertamenti());
	    
	    // provvedimento
	    if (sub.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(sub.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(sub.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(sub.getAttoAmministrativo().getOggetto());
	    	if (sub.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(sub.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (sub.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(sub.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	//6929
	    	if  (sub.getAttoAmministrativo().getBloccoRagioneria()!= null ) {
	    		mc.getProvvedimento().setBloccoRagioneria(((sub.getAttoAmministrativo().getBloccoRagioneria()==true) ? "SI" : "NO" ));
	    	}else {
	    		mc.getProvvedimento().setBloccoRagioneria("N/A");
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
        if (sub.isFlagDaRiaccertamento()){
        	mc.setDaRiaccertamento(WebAppConstants.MSG_SI + " " + sub.getAnnoRiaccertato() + " / " + sub.getNumeroRiaccertato());
        } else {
        	mc.setDaRiaccertamento(WebAppConstants.MSG_NO);
        }
        	
        
        // disponibilita
	    mc.setDisponibilitaIncassare(sub.getDisponibilitaIncassare());
		
	    //setto l'oggetto appena popolato nel model:
		model.setSubDettaglio(mc);

		return "dettaglioSubPopUp";
	}
	
	/**
	 * Metodo per l'annullamento di un sub accertamento
	 * @return
	 */
	public String annullaSubAccertamento(){
		//informazioni per debug:
		setMethodName("annullaSubAccertamento");
		debug(methodName, "uid da annullare--> "+getUidSubDaAnnullare());
		
		//compongo la request per il servizio di annulla:
		AnnullaMovimentoEntrata reqAnnulla = convertiModelPerChiamataServizioAnnulla(getUidSubDaAnnullare());
		
		//richiamo il servizio di annulla:
		AnnullaMovimentoEntrataResponse response = movimentoGestionService.annullaMovimentoEntrata(reqAnnulla);

		//analizzo la risposta del servizio:
		if(!response.isFallimento()){
			// RICARICO L'ACCERTAMENTO EVITANDO LA MEGA QUERY
			if(response.getAccertamento()!= null){
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
			}
			
			//richiamo il metodo di caricamento passandogli flagPostAggiorna a true:
			ricaricaSubAccertamenti(true);
			
			//setto il messaggio di esito positivo:
			addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		}else{
			//presenza errori
			addErrori(response.getErrori());
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Metodo che determina se un sub accertamento e' annullabile
	 * 
	 * @param stato
	 * @return
	 */
	public boolean isAnnullaAbilitatoAccertamento(String stato){
		
		//solo per il subaccertamento e' Annullabile anche da stato provvisorio
		
		//questo primo controllo riguarda logiche comuni per
		//sub accertamenti e sub impegni:
		boolean abilitatazioniComuniImpEAcc = super.isAnnullaAbilitato(stato);
		if(!abilitatazioniComuniImpEAcc){
			return false;
		}
		
		//controllo fase bilancio:
		boolean abilita = isFaseBilancioAbilitata();
		if(!abilita){
			return false;
		}
		
		//SIAC-4949 IMPEGNI e ACCERTAMENTI Aggiornamento per decentrati CR 912
		// quando l'utente ha il'azione DecentratoP deve essere disabilitata l'azione AGGIORNA e ANNULLA 
		//dei subimpegni/subaccertamenti definitivi 
		if(stato.equals("D") && isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentratoP)){
			return false;
		}
		//
			
		return true;
	}
	
	/**
	 * Metodo che determina se un sub accertamento e' aggiornabile
	 * @param stato
	 * @return
	 */
	public boolean isAggiornaAbilitatoAccertamento(String stato){
		
		//questo primo controllo riguarda logiche comuni per
		//sub accertamenti e sub impegni:
		boolean abilitatazioniComuniImpEAcc = super.isAggiornaAbilitato(stato);
		if(!abilitatazioniComuniImpEAcc){
			return false;
		}
		
		//SIAC-4949 IMPEGNI e ACCERTAMENTI Aggiornamento per decentrati CR 912
		// quando l'utente ha il'azione DecentratoP deve essere disabilitata l'azione AGGIORNA e ANNULLA 
		//dei subimpegni/subaccertamenti definitivi 
		if(stato.equals("D") && isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentratoP)){
			return false;
		}
		//
			
		return true;
	}
	
	/**
	 * Metodo che determina se un sub accertamento e' eliminabile
	 * @param stato
	 * @return
	 */
	public boolean isEliminaAbilitatoAccertamento(String stato){
		
		//controllo stato:
		boolean abilita = true;
		if(stato.equals("A")){
		     return false;
		}
		if(stato.equals("D")){
	     	 return false;
		}
		
		//controllo fase bilancio:
		abilita = isFaseBilancioAbilitata();
		
	    return abilita;
	}
	
	/**
	 * Ricarica i dati dei sub
	 * @param flagPostAggiorna
	 */
	private void ricaricaSubAccertamenti(boolean flagPostAggiorna) {
		if(model.getAccertamentoRicaricatoDopoInsOAgg()!= null){
			
			//travaso alcuni dati:
			model.setListaSubaccertamenti(model.getAccertamentoRicaricatoDopoInsOAgg().getElencoSubAccertamenti());
			model.setDisponibilitaSubImpegnare(model.getAccertamentoRicaricatoDopoInsOAgg().getDisponibilitaSubAccertare());
			model.setTotaleSubImpegno(model.getAccertamentoRicaricatoDopoInsOAgg().getTotaleSubAccertamenti());
			model.setAccertamentoInAggiornamento(model.getAccertamentoRicaricatoDopoInsOAgg());
			setAccertamentoToUpdate(model.getAccertamentoRicaricatoDopoInsOAgg());
			
			//flagPostAggiorna serve ad indicare al metodo caricaDatiAccertamento
			//se deve ricaricare i dati da servizio oppure basarsi su quelli presenti nel model:
			caricaDatiAccertamento(flagPostAggiorna);
			
			//setto a null AccertamentoRicaricatoDopoInsOAgg
			model.setAccertamentoRicaricatoDopoInsOAgg(null);
		}
	}
	
	/**
	 * Metodo per eliminare un sub accertamento
	 * @return
	 */
	public String eliminaSubAccertamento(){
		//informazioni per debug:
		setMethodName("eliminaSubAccertamento");
		debug(methodName, "uid da annullare--> "+getUidSubDaEliminare());
		
		if(null!=model.getListaSubaccertamenti() && !model.getListaSubaccertamenti().isEmpty()){
			
			//itero la lista dei sub
			for (int i = 0; i < model.getListaSubaccertamenti().size(); i++) {
				SubAccertamento sub = model.getListaSubaccertamenti().get(i);
				//se trovo il sub in questione:
				if(sub.getUid() == Integer.valueOf(getUidSubDaEliminare())){
					//lo rimuovo
					model.getListaSubaccertamenti().remove(i);
				} 	 
			}
			//setto la nuova lista con il sub rimosso:
			model.setListaSubaccertamenti(model.getListaSubaccertamenti());
			
			//Fix per SIOPE JIRA  SIAC-3913:
			AggiornaAccertamento requestAggiorna = convertiModelPerChiamataServizioAggiornaAccertamenti(true);
			requestAggiorna.getAccertamento().setCodSiope(model.getAccertamentoInAggiornamento().getCodSiope());
			//
			
			//chiamo il servizio di aggiorna che non avendo piu' quel sub in lista lo eliminera':
			AggiornaAccertamentoResponse response =	movimentoGestionService.aggiornaAccertamento(requestAggiorna);
			
			//analizzo l'esito del servizio:
			if(!response.isFallimento() && response.getAccertamento()!= null ){
				//Ottimizzazione richiamo ai servizi
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
			}else{
				addErrori(response.getErrori());
				return INPUT;
			}
		}
		
		//ricarico i dati:
		ricaricaSubAccertamenti(true);
		return SUCCESS;
	}
	
	/**
	 * Si occupa di predisporre la request per la chiamata al servizio di annulla movimento
	 * @param uid
	 * @return
	 */
	private AnnullaMovimentoEntrata convertiModelPerChiamataServizioAnnulla(String uid){
		//istanzio la request:
		AnnullaMovimentoEntrata request = new AnnullaMovimentoEntrata();
		
		//setto i dati neessari:
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());
		
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(model.getAccertamentoInAggiornamento().getAnnoMovimento());
		accertamento.setNumero(model.getAccertamentoInAggiornamento().getNumero());
		
		SubAccertamento subAccertamentoIntero = FinUtility.getById(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(), Integer.valueOf(uid));
		
		List<SubAccertamento> subAccertamentos = new ArrayList<SubAccertamento>();
		
		subAccertamentos.add(subAccertamentoIntero);
		accertamento.setElencoSubAccertamenti(subAccertamentos);
		request.setAccertamento(accertamento);
		return request;
	}

}