/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaCartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCartaAction extends ActionKeyRicercaCartaAction {

	private static final long serialVersionUID = 1L;
	
	
	public RicercaCartaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca Carta Contabile ");
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		// caricamento dati nelle combo
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.STATO_OP_CARTA_CONTABILE);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
		model.setListaStatoCartaContabile((List<CodificaFin>)mappaListe.get(TipiLista.STATO_OP_CARTA_CONTABILE));

	}
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
		
	   	//abilitazione o meno alla funzione
	   	if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_ricCarta)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * qui deve essere sempre abilitato il tasto di compilazione guidata
	 * 
	 */
	public boolean disabilitaCompilazioneGuidataPerCollega(){
		// la compilazione guidata e' sempre attiva
		return true;
	}
	
	/**
	 * click sul tasto cerca, che controlla i dati e poi lancera' la funzione di ricerca carta
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String cerca() throws Exception{
		List<Errore> listaErrori= new ArrayList<Errore>();
		boolean noInputData=true;

		// al click di cerca riazzero l'elenco delle righe
		model.setElencoCarteContabili(new ArrayList<CartaContabile>());
		sessionHandler.setParametro(FinSessionParameter.RIGHE_DA_REGOLARIZZARE, new ArrayList<CartaContabile>());
		model.setResultSize(0);
		
		//Condizioni ricerca - Carta
		if (!FinStringUtils.isEmpty(model.getNumeroCartaDa()) || !FinStringUtils.isEmpty(model.getNumeroCartaA())) {
		
			noInputData=false;
		
		}
		
		// verifico la completezza dei numeri 
		if (!FinStringUtils.isEmpty(model.getNumeroCartaDa()) && FinStringUtils.isEmpty(model.getNumeroCartaA())) {
			noInputData=false;          
			model.setNumeroCartaA(model.getNumeroCartaDa());
			
		}else if (FinStringUtils.isEmpty(model.getNumeroCartaDa()) && !FinStringUtils.isEmpty(model.getNumeroCartaA())) {
			   noInputData=false;
			   model.setNumeroCartaDa(model.getNumeroCartaA());
		}

		
		// valore non vuoto della data di scadenza
		if (!FinStringUtils.isEmpty(model.getDataScadenzaCartaDa()) || !FinStringUtils.isEmpty(model.getDataScadenzaCartaA())) {
			noInputData=false;
		}
		
		
		//Condizioni ricerca - Da-A numOrd
		if (model.getNumeroCartaDa()!=null && !model.getNumeroCartaDa().trim().equals("") && model.getNumeroCartaA()!=null && !model.getNumeroCartaA().trim().equals("")) {
			
			if(Integer.valueOf(model.getNumeroCartaDa())>Integer.valueOf(model.getNumeroCartaA())){
				listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Numero Carta Da/Numero Carta A","(Numero Carta Da deve essere minore di Numero Carta A)"));
			}
			
		}
			
		if (model.getNumeroCartaDa()==null && model.getNumeroCartaA()!=null) {
			listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Numero Carta Da/Numero Carta A","(Non e' possibile inserire Numero Carta A senza Numero Carta Da)"));
		}
		
		// stato
		if(!FinStringUtils.isEmpty(model.getStatoCarta())){
			
			noInputData=false;
		}
		
		// descrizione
		if(FinStringUtils.isEmpty(model.getDescrizioneCarta())){
			
		}else if(!(model.getDescrizioneCarta().length()>2)){
			    noInputData = false;
				listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("descrizione carta","(inserire almeno 3 caratteri)"));
		}else{
			// lunghezza giusta quindi nessun errore
			noInputData = false;
		}
		
		
		//Condizioni ricerca - Da-A dataOrd
		if (!FinStringUtils.isEmpty(model.getDataScadenzaCartaDa()) || !FinStringUtils.isEmpty(model.getDataScadenzaCartaA())) {

			DateFormat df=null;
			Date parsedTimeDa=null;
			Date parsedTimeA=null;
			// date che siano formattate correttamente e che non siano inseriti numeri a caso
			if (!FinStringUtils.isEmpty(model.getDataScadenzaCartaDa())) {
				if (model.getDataScadenzaCartaDa().length() == 8) {
					df = new SimpleDateFormat("dd/MM/yy");
				} else {
					df = new SimpleDateFormat("dd/MM/yyyy");
				}
				
				try {
					// parse della data e in caso di eccezione errore !!
					parsedTimeDa=df.parse(model.getDataScadenzaCartaDa());
				} catch (ParseException e) {
					listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Data Scadenza Da",""));				}
			}

			// coerenza date inserite e corretta formattazione
			if (!FinStringUtils.isEmpty(model.getDataScadenzaCartaA())) {
				if (model.getDataScadenzaCartaA().length() == 8) {
					df = new SimpleDateFormat("dd/MM/yy");
				} else {
					df = new SimpleDateFormat("dd/MM/yyyy");
				}
				
				try {
					parsedTimeA=df.parse(model.getDataScadenzaCartaA());
				} catch (ParseException e) {
					listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Data Scadenza A",""));				
				}
			}

			// controllo sulle date scadenza secondo cdu
			if (parsedTimeDa==null && parsedTimeA!=null) {
				listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Data Scadenza Da/Data Scadenza A","(Non e' possibile inserire Data Scadenza A senza Data Scadenza Da)"));
			}
			
			if (parsedTimeDa!=null && parsedTimeA!=null && parsedTimeDa.after(parsedTimeA)) {
				listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Data Scadenza Da/Data Scadenza A","(Data Scadenza Da deve essere minore di Data Scadenza A)"));
			}

		}
		
		// blocco di ricerca per impegno
		if(model.getImpegno().getAnnoImpegno()!=null && model.getImpegno().getNumeroImpegno()==null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno"));
		
		}else if(model.getImpegno().getAnnoImpegno()==null && model.getImpegno().getNumeroImpegno()!=null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno"));

		}else if(model.getImpegno().getAnnoImpegno()==null && model.getImpegno().getNumeroImpegno()==null && model.getImpegno().getNumeroSub()!=null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno"));
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno"));
		}
		
		// blocco di ricerca per provvedimento
		//Criterio provvedimento esistente
		if (model.getProvvedimento()!=null) {
			// sotto caso tutto !=null
			if (model.getProvvedimento().getAnnoProvvedimento()!=null || model.getProvvedimento().getNumeroProvvedimento()!=null || model.getProvvedimento().getIdTipoProvvedimento()!=null) {
				noInputData=false;
			}

			if(model.getProvvedimento().getAnnoProvvedimento()!=null && model.getProvvedimento().getAnnoProvvedimento().intValue()>0 ){
				if(model.getProvvedimento().getNumeroProvvedimento()==null && model.getProvvedimento().getIdTipoProvvedimento()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero o Tipo Provvedimento obbligatorio con Anno Provvedimento"));
				}
			}

			if(model.getProvvedimento().getNumeroProvvedimento()!=null || model.getProvvedimento().getIdTipoProvvedimento()!=null){
				if(model.getProvvedimento().getAnnoProvvedimento()==null || model.getProvvedimento().getAnnoProvvedimento().intValue()==0 ){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento obbligatorio con Numero o Tipo Provvedimento"));
				}
			}
		}
		
		//Condizioni ricerca - Capitolo
		if (model.getCapitolo().getNumCapitolo()!=null || model.getCapitolo().getArticolo()!=null || model.getCapitolo().getUeb()!=null) {
			noInputData=false;
		}
		
		
		//Condizioni ricerca - Impegno
		if (model.getAnnoImpegno()!= null || model.getNumeroImpegno()!= null || model.getNumeroSub()!= null) {
			noInputData = false;
		}
		
		//Criterio soggetto esistente
		if (!FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())) {
			noInputData=false;
		}
		
		
		// criterio sulla ricerca provvedimento 
		// valida se : anno e numero Oppure Anno e Tipo Oppure tutti e 3 
		
		
		// se true allora non ha inserito alcun criterio e quindi blocca con errore
		if(noInputData){
			listaErrori.add(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}
		
		
		if(!listaErrori.isEmpty()){
			addErrori(listaErrori);
			return INPUT;
		}
		
		// gestione dei parametri per session, dato che quando si passa in altre funzioni
		// si eredita un'altra action
		//
		// levo da sessione eventuali parametri lanciati in ricerche precedenti
		sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_CARTA, new ParametroRicercaCartaContabile());
		
		
		return "cerca";
		
	}
	
	
	public String pulisciRicercaImpegno() {

		// funzione che pulisce i campi
		model.setIsnSubImpegno(false);
		model.setHasImpegnoSelezionatoPopup(false);
		
		return "refreshPopupModalImpegno";
	}
	
	/**
	 *  ricerca guidata di impegno
	 */
	public String ricercaGuidataImpegno(){		
		// pulisci eventuali dati sporchi da precedenti ricerche
		pulisciListeeSchedaPopup();
		model.setInPopup(true);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		HttpServletRequest request = ServletActionContext.getRequest();		
		
		String annoimpegno = request.getParameter("anno");
		
		// catturo i paramwentri con cui poi lancio la ricerca
		if (annoimpegno != null && !annoimpegno.isEmpty()) {
			try {
				// verifica su anno
				Integer anno = Integer.valueOf(annoimpegno);
				model.setnAnno(anno);
				if (anno <= 1900) {
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno", annoimpegno, ">1900"));
				}
			} catch (NumberFormatException e) {
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", annoimpegno));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Anno "));
			model.setnAnno(null);
		}
		
		// blocco numero
		String numeroimpegno = request.getParameter("numero");
		if (numeroimpegno != null && !numeroimpegno.isEmpty()) {
			try {
			model.setnImpegno(Integer.valueOf(numeroimpegno));
			} catch (NumberFormatException e) {
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", numeroimpegno));
			}

		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Numero "));
			model.setnImpegno(null);
		}
		
		if (!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return "refreshPopupModalImpegno";
		}
		
		// lancio la ricerca impegno
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		rip.setEnte(sessionHandler.getEnte());
		rip.setRichiedente(sessionHandler.getRichiedente());
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(annoimpegno!=null)
			k.setAnnoImpegno(new Integer(annoimpegno));
		if(numeroimpegno!=null)
			k.setNumeroImpegno(new BigDecimal(numeroimpegno));
		rip.setpRicercaImpegnoK(k);
		
		//Richiedo alcuni dati opzionali:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds );
		
		//esclude il caricamento di tutti i sub con i dati pesanti:
		rip.setCaricaSub(false);
		//
		
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		
		
		if (respRk != null && respRk.getImpegno() != null) {
			Impegno impegno = respRk.getImpegno();
				if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D") || impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("N")){
				//metto qui le info per la tabella impegno da visualizzaree sempre
					
					//setting di capitolo, provvedimento e soggetto:
					settaCapitoloProvvedimentoSoggettoPopUp(impegno);
					//
					
					model.setImpegnoPopup(impegno);
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
				if (respRk.getElencoSubImpegniTuttiConSoloGliIds() != null && respRk.getElencoSubImpegniTuttiConSoloGliIds().size() > 0) {
					for(int i = 0; i < respRk.getElencoSubImpegniTuttiConSoloGliIds().size(); i++){
						SubImpegno subImp = clone(respRk.getElencoSubImpegniTuttiConSoloGliIds().get(i));
						//il subimpegnp NON ha capitolo quindi uso quello dell'impegno:
						if(subImp.getCapitoloUscitaGestione()==null){
							subImp.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
						}
						listaTabellaImpegni.add((Impegno)subImp);
						//setto isnSubImpegno variabile utilita' popup
						model.setIsnSubImpegno(true);
					}
				}else{
					listaTabellaImpegni.add(impegno);
					//agiorno nsubImpegno e nisSubImpegno variabile utilita' popup
					model.setnSubImpegno(null);
					model.setIsnSubImpegno(false);
				}			
				model.setListaImpegniCompGuidata(listaTabellaImpegni);			
			}
			else{
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
	
	
	
	

	public String annulla(){
		// click del tasto annulla che pulisce tutti i campi della maschera di ricerca
		model.setNumeroCartaDa("");
		model.setNumeroCartaA("");
		model.setDataScadenzaCartaDa("");
		model.setDataScadenzaCartaA("");
		model.setDescrizioneCarta("");
		
		ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
		model.setProvvedimento(provvedimento);
		
		model.getSoggetto().setCodCreditore("");
		
		CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
		model.setCapitolo(capitolo);
		model.getCapitolo().setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		
		model.setAnnoImpegno(null);
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		
		model.setCapitoloSelezionato(false);
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setProvvedimentoSelezionato(false);
		model.setSoggettoSelezionato(false);
		
		return SUCCESS;
	}
	
}
