/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.CartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaEstera;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class WizardGestioneCartaAction extends GenericPopupAction<GestioneCartaModel> {

	private static final long serialVersionUID = 1L;
	
	protected static final String MODPAGAMENTO = "modpagamento";
	protected static final String SEDISECONDARIE = "sedisecondarie";
	private static final String VAI_STEP2 = "vaiStep2";	
	protected static final String GOTO_CONSULTA = "gotoConsulta";
	protected static final String RELOAD_GCS1 = "reloadGestioneCartaStep1";

	private boolean idSedeCreditoreChecked;
	private String numeroCartaStruts;
	private String annoCartaStruts;
	private Boolean	fromInserisciStruts;
	
	
	@Autowired
	protected transient CartaContabileService cartaContabileService;
	
	public List<CartaEstera.TipoPagamento> getListaModalita() {
		return  Arrays.asList(CartaEstera.TipoPagamento.values());
	}
	
	/**
	 * qui deve essere sempre abilitato il tasto di compilazione guidata
	 * 
	 */
	public boolean disabilitaCompilazioneGuidataPerCollega(){
		return true;
	}
	
	/**
	 * Metodo che controlla i campi di testata
	 * @return
	 */
	public List<Errore> controlloCampiTestataCarta() {
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//CONTROLLI PROVVEDIMENTO:
		
		if (model.getProvvedimento().getAnnoProvvedimento()==null) {
			//anno mancante
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		}else if (model.getProvvedimento().getAnnoProvvedimento().intValue()<=1900) {
			//anno non valido
			listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Provvedimento = "+model.getProvvedimento().getAnnoProvvedimento(),">1900"));
		}
		
		if (model.getProvvedimento().getNumeroProvvedimento()==null) {
			//numero mancante
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
		}else if (model.getProvvedimento().getNumeroProvvedimento().compareTo(BigInteger.ZERO)==0) {
			//numero non valido
			listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Numero Provvedimento = "+model.getProvvedimento().getNumeroProvvedimento(),">0"));
		}

		if (model.getProvvedimento().getIdTipoProvvedimento() == null) {
			//tipo provvedimento
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}							

		//Controllo Causale
		if (FinStringUtils.isEmpty(model.getCausaleCarta())) {
			//descrizione carta
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Causale"));
		}


		//DATA VALUTA CARTA:
		//SIAC-6076 rimossa data esecuzione pagamento
		/*if (!FinStringUtils.isEmpty(model.getDataValutaCarta())) {
			DateFormat df=null;
			Date parsedDate=null;
			if (model.getDataValutaCarta().length() == 8) {
				df = new SimpleDateFormat("dd/MM/yy");
			} else {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}
			
			try {
				parsedDate=df.parse(model.getDataValutaCarta());
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(parsedDate);
				cal.get(Calendar.YEAR);

				if (cal.get(Calendar.YEAR)!=annoEsercizio) {
					//anno esercizio non valido
					listaErrori.add(ErroreFin.DATE_INCONGRUENTI.getErrore(" : data pagamento deve essere compresa nell'anno esercizio.",""));				
				}
			} catch (ParseException e) {
				//valore non valido
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data esecuzione pagamento",""));				
			}
		}else{
			//data assente
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data esecuzione pagamento"));
		}*/
		

		//DATA SCADENZA CARTA:
		// RM : (CR 648) jira 4200 aggiunto controllo obbligatorieta'
		if (!FinStringUtils.isEmpty(model.getDataScadenzaCarta())) {
			DateFormat df=null;
			Date parsedDate=null;
			if (model.getDataScadenzaCarta().length() == 8) {
				df = new SimpleDateFormat("dd/MM/yy");
			} else {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}
			
			try {
				parsedDate=df.parse(model.getDataScadenzaCarta());
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(parsedDate);
				
				cal.setTime(new Date());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				
				df.format(parsedDate).equals(df.format(new Date()));
				
				/*SIAC-606	Togliere i controlli sulla data di scadenza (rispetto alla data del giorno e dell'anno di bilancio)
				if (parsedDateYear!=annoEsercizio || parsedDate.before(now)) {
					//anno esercizio non valido
					listaErrori.add(ErroreFin.DATE_INCONGRUENTI.getErrore(" : data scadenza  deve essere compresa nell'anno esercizio e maggiore o uguale alla data corrente.",""));				
				}*/
			} catch (ParseException e) {
				//valore non valido
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza",""));				
			}
		}else{
			//data assente
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data Scadenza"));
		}
		
		//Controlli Valuta Estera
		if (model.isOptionsPagamentoEstero()) {
			//Controllo Valuta estera
			if (FinStringUtils.isEmpty(model.getCodiceDivisa())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Valuta estera"));
			} else if (model.isAggiornamento() && !model.getCodiceDivisa().equalsIgnoreCase(model.getCodiceDivisaCaricato())) {
		    	addPersistentActionWarning(ErroreFin.OPERAZIONE_EFFETTUATA.getErrore().getCodice()+" : "+ErroreFin.OPERAZIONE_EFFETTUATA.getErrore("modifica valuta estera","Modificare gli importi delle righe.").getDescrizione());
			}
			
			//Controllo Causale del Pagamento
			if (FinStringUtils.isEmpty(model.getCausalePagamentoEstero())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Causale del Pagamento"));
			}
			
			//Controllo Bonifico Assegno Altro
			if (FinStringUtils.isEmpty(model.getModalitaPagamento())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipologia"));
			}
			
			//Controllo spese e commissioni
			if (FinStringUtils.isEmpty(model.getSpeseECommissioni())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Spese e commissioni"));
			}
		}					
		
		return listaErrori;
	}
	
	/**
	 * Salvataggio della carta.
	 * 
	 * Gestisce sia l'inserimento che l'aggiornamento.
	 * 
	 * @return
	 */
	public String salvaCarta(){
		
		//ABILITAZIONE:
		if (model.isAggiornamento()) {
			
			//IN AGGIORNAMENTO DEVO ESSERE ABILITATO A OP_SPE_AGGCARTA
			
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		} else {
			
			//IN INSERIMENTO DEVO ESSERE ABILITATO A OP_SPE_INSCARTA
			
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_INSCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}
		
		//controllo stato bilancio:
		if(controlloStatoBilancio(sessionHandler.getAnnoBilancio(),"Gestione","Carta Contabile")){
			//esito negativo, l'errore e' gia' stato settato dentro al metodo controlloStatoBilancio
			return INPUT;
		}
		
		List<Errore> listaErroriSulleRighe = controlliSulleRighe();
		
		//messo cosi' per non modificare troppo il comportamento
		if(listaErroriSulleRighe != null && !listaErroriSulleRighe.isEmpty()){
			//ci sono errori
			addErrori(listaErroriSulleRighe);
			return INPUT;
		}
		
		//Verifiche sui campi testata:
		List<Errore> listaErrori=controlloCampiTestataCarta();
		
		
		//controllo esistenza provvedimento valido
		boolean erroriProvvedimento = controlloServizioProvvedimento(model.getProvvedimento(), model.isProvvedimentoSelezionato());
		
		if(!listaErrori.isEmpty() || erroriProvvedimento){
			//ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		if (!model.isAggiornamento()) {
			
			//INSERIMENTO

			//compongo la request per il servizio di inserimento
			InserisceCartaContabile inserisceCartaContabileRequest = composizioneSalvaCartaContabile();

			//invoco il servizio di inserimento:
			InserisceCartaContabileResponse response = cartaContabileService.inserisceCartaContabile(inserisceCartaContabileRequest);
			
			//analizzo la risposta del servizio:
			if (response.isFallimento()) {
				//ci sono errori
				addErrori(response.getErrori());
				return INPUT;
			}else{
				//tutto ok
				addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			}

			if (response.getCartaContabile()!=null) {
				setNumeroCartaStruts(String.valueOf(response.getCartaContabile().getNumero()));
				setFromInserisciStruts(true);
			}
			
		} else {
			
			//AGGIORNAMENTO
			
			//compongo la request per il servizio di aggiornamento
			AggiornaCartaContabile aggiornaCartaContabileRequest = composizioneAggiornaCartaContabile();

			//invoco il servizio di aggiornamento:
			AggiornaCartaContabileResponse response = cartaContabileService.aggiornaCartaContabile(aggiornaCartaContabileRequest);
			
			//analizzo la risposta del servizio:
			if (response.isFallimento()) {
				//ci sono errori
				addErrori(response.getErrori());
				return INPUT;
			}else{
				//tutto ok
				addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			}

			if (response.getCartaContabile()!=null) {
				setNumeroCartaStruts(String.valueOf(response.getCartaContabile().getNumero()));
			}
		}
		
		if (model.isAggiornamento()) {
			setNumeroCartaStruts(model.getNumeroCartaInAggiornamento());
			setAnnoCartaStruts(model.getAnnoCartaInAggiornamento());
			return RELOAD_GCS1;
		}
		
		return GOTO_CONSULTA;
	}

	/**
	 * 
	 */
	private List<Errore> controlliSulleRighe() {
		List<Errore> errori = new ArrayList<Errore>();
		List<PreDocumentoCarta> listaRighe = model.getListaRighe();
		//Controllo presenza righe
		if (listaRighe==null || listaRighe.isEmpty()) {
			//righe assenti
			errori.add(ErroreFin.ORDINATIVO_MANCANTE_DI_QUOTE.getErrore("inserimento","Carta"));
			return errori;
		}
		//SIAC-6173
		for (PreDocumentoCarta preDocumentoCarta : listaRighe) {
			if(preDocumentoCarta.getContoTesoreria() == null || StringUtils.isEmpty(preDocumentoCarta.getContoTesoreria().getCodice())) {
				errori.add(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto tesoreria della riga"));
				//esco, mi basta che ce ne sia uno solo senza conto tesoreria
				return errori;
			}
		}
		
		return errori;
	}
	
	
	/**
	 * Compone la request per l'inserimento
	 * @return
	 */
	private InserisceCartaContabile composizioneSalvaCartaContabile(){

		InserisceCartaContabile request = new InserisceCartaContabile();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		
		CartaContabile cartaContabileDaInserire=new CartaContabile();
		
		// atto amministrativo
		if (model.getProvvedimento()!=null && model.getProvvedimento().getNumeroProvvedimento()!=null &&
				model.getProvvedimento().getAnnoProvvedimento()!=null && model.getProvvedimento().getIdTipoProvvedimento()!=null) {
			AttoAmministrativo attoAmministrativo = popolaProvvedimento(model.getProvvedimento());
			cartaContabileDaInserire.setAttoAmministrativo(attoAmministrativo);
		}
		
		//Carta contabile
		cartaContabileDaInserire.setCausale(model.getCausaleCarta());
		cartaContabileDaInserire.setMotivoUrgenza(model.getMotivoUrgenzaCarta());
		cartaContabileDaInserire.setNote(model.getNoteCarta());
		cartaContabileDaInserire.setOggetto(model.getDescrizioneCarta());
		cartaContabileDaInserire.setImporto(sommaImportiRighe());
		cartaContabileDaInserire.setFirma1(model.getFirma1());
		cartaContabileDaInserire.setFirma2(model.getFirma2());
		
	    if(!FinStringUtils.isEmpty(model.getDataValutaCarta())){
	    	cartaContabileDaInserire.setDataEsecuzionePagamento(DateUtility.parse(model.getDataValutaCarta()));
	    }

	    if(!FinStringUtils.isEmpty(model.getDataScadenzaCarta())){
	    	cartaContabileDaInserire.setDataScadenza(DateUtility.parse(model.getDataScadenzaCarta()));
	    }
		
		//Carta estera
		if (model.isOptionsPagamentoEstero()) {
			CartaEstera cartaEsteraDaInserire=new CartaEstera();

			CodificaFin valuta=new CodificaFin();
			valuta.setCodice(model.getCodiceDivisa());
			cartaEsteraDaInserire.setValuta(valuta);
			
			cartaEsteraDaInserire.setCausalePagamento(model.getCausalePagamentoEstero());
			CodificaFin commissione=new CodificaFin();
			commissione.setCodice(model.getSpeseECommissioni());
			
			cartaEsteraDaInserire.setCommissioniEstero(commissione);
			
			// Modalita' di pagamento  - Tipo
			cartaEsteraDaInserire.setTipoPagamento(CartaEstera.TipoPagamento.valueOf(model.getModalitaPagamento()));
			
			// Modalita' di consegna
			
			//Istruzioni pagamento
			cartaEsteraDaInserire.setIstruzioni(model.getIstrPartPagamentoEstero());
			// Esecutore diverso da Titolare
			if (model.isCheckEsecutoreTitolare()) {
				cartaEsteraDaInserire.setDiversoTitolare(CostantiFin.TRUE);
			} else {
				cartaEsteraDaInserire.setDiversoTitolare(CostantiFin.FALSE);
			}
			
			cartaContabileDaInserire.setCartaEstera(cartaEsteraDaInserire);
		}
		
		//Righe
		if (model.getListaRighe()!=null) {
			cartaContabileDaInserire.setListaPreDocumentiCarta(model.getListaRighe());
		}
		
		
		request.setCartaContabile(cartaContabileDaInserire);
		
		return request;
	}	

	/**
	 * Compone la request per l'aggiornamento
	 * @return
	 */
	private AggiornaCartaContabile composizioneAggiornaCartaContabile(){
		AggiornaCartaContabile request = new AggiornaCartaContabile();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(sessionHandler.getBilancio());
		
		
		CartaContabile cartaContabileDaAggiornare=new CartaContabile();
		
		// atto amministrativo
		if (model.getProvvedimento()!=null && model.getProvvedimento().getNumeroProvvedimento()!=null &&
				model.getProvvedimento().getAnnoProvvedimento()!=null && model.getProvvedimento().getIdTipoProvvedimento()!=null) {
			AttoAmministrativo attoAmministrativo = popolaProvvedimento(model.getProvvedimento());
			cartaContabileDaAggiornare.setAttoAmministrativo(attoAmministrativo);
		}
		
		//Carta contabile
		cartaContabileDaAggiornare.setNumRegistrazione(model.getNumeroRegistrazione());
		cartaContabileDaAggiornare.setNumero(new Integer(model.getNumeroCartaInAggiornamento()));
		cartaContabileDaAggiornare.setCausale(model.getCausaleCarta());
		cartaContabileDaAggiornare.setMotivoUrgenza(model.getMotivoUrgenzaCarta());
		cartaContabileDaAggiornare.setNote(model.getNoteCarta());
		cartaContabileDaAggiornare.setOggetto(model.getDescrizioneCarta());
		cartaContabileDaAggiornare.setImporto(sommaImportiRighe());
		cartaContabileDaAggiornare.setFirma1(model.getFirma1());
		cartaContabileDaAggiornare.setFirma2(model.getFirma2());
		
	    if(!FinStringUtils.isEmpty(model.getDataValutaCarta())){
	    	cartaContabileDaAggiornare.setDataEsecuzionePagamento(DateUtility.parse(model.getDataValutaCarta()));
	    }

	    if(!FinStringUtils.isEmpty(model.getDataScadenzaCarta())){
	    	cartaContabileDaAggiornare.setDataScadenza(DateUtility.parse(model.getDataScadenzaCarta()));
	    }
		
		//Carta estera
		if (model.isOptionsPagamentoEstero()) {
			CartaEstera cartaEsteraDaInserire=new CartaEstera();

			CodificaFin valuta=new CodificaFin();
			valuta.setCodice(model.getCodiceDivisa());
			cartaEsteraDaInserire.setValuta(valuta);
			
			cartaEsteraDaInserire.setCausalePagamento(model.getCausalePagamentoEstero());
			CodificaFin commissione=new CodificaFin();
			commissione.setCodice(model.getSpeseECommissioni());
			
			cartaEsteraDaInserire.setCommissioniEstero(commissione);
			
			// Modalita' di pagamento  - Tipo
			cartaEsteraDaInserire.setTipoPagamento(CartaEstera.TipoPagamento.valueOf(model.getModalitaPagamento()));
			
			// Modalita' di consegna
			
			//Istruzioni pagamento
			cartaEsteraDaInserire.setIstruzioni(model.getIstrPartPagamentoEstero());
			// Esecutore diverso da Titolare
			if (model.isCheckEsecutoreTitolare()) {
				cartaEsteraDaInserire.setDiversoTitolare(CostantiFin.TRUE);
			} else {
				cartaEsteraDaInserire.setDiversoTitolare(CostantiFin.FALSE);
			}
			
			cartaContabileDaAggiornare.setCartaEstera(cartaEsteraDaInserire);
		}
		
		//Righe
		if (model.getListaRighe()!=null) {
			cartaContabileDaAggiornare.setListaPreDocumentiCarta(model.getListaRighe());
		}
		
		
		request.setCartaContabile(cartaContabileDaAggiornare);

		
		return request;
	}

	/**
	 * pulisce i campi riga nel model
	 */
	public void pulisciCampiRiga(){
		// pulisci campi della form
		model.setSoggetto(new SoggettoImpegnoModel());
		model.setImpegno(new ImpegnoLiquidazioneModel());
		model.setListaSediSecondarie(new ArrayList<SedeSecondariaSoggetto>());
		model.setListaModalitaPagamento(new ArrayList<ModalitaPagamentoSoggetto>());
		model.setListaSediSecondarieVisualizza(new ArrayList<SedeSecondariaSoggetto>());
		model.setListaModalitaPagamentoVisualizza(new ArrayList<ModalitaPagamentoSoggetto>());
		model.setSedeSelezionata(null);
		model.setModpagamentoSelezionata(null);
		model.setRadioModPagSelezionato(0);
		model.setRadioSediSecondarieSoggettoSelezionato(0);
		model.setSoggettoSelezionato(false);
		model.setAvvisoFrase(null);
		model.setDataEsecuzioneRiga(null);
		model.setImportoRiga(null);
		model.setImportoEsteroRiga(null);
		model.setDescrizioneRiga(null);
		model.setNoteRiga(null);
		model.setContoTesoriereRiga(null);
		model.setAnnoImpegno(null);
		model.setNumeroSub(null);
		model.setNumeroImpegno(null);
		model.setHasImpegnoSelezionatoXPopup(false);
		
		model.setRigaInModifica(null);		
		model.setListaSubDocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setDescrizioneImpegnoPopup(null);
	}
	
	protected void impostaDefaultContoTesoriere(boolean inAggiornamento){
		// SIAC-5933 - SIAC-6029
		if(!inAggiornamento && !isEmpty(model.getListaContoTesoriereRiga())){
			for(CodificaFin it: model.getListaContoTesoriereRiga()){
				if(it.getCodice().startsWith("0000100")){
					if(model.getContoTesoriereRiga()==null){
						//se e' null sono appena entrato nel caso d'uso di inserimento
						model.setContoTesoriereRiga(it.getCodice());
					}
				}
			}
		}
		//
	}
	
	protected void impostaDefaultDescrizioneRiga(boolean inAggiornamento){
		if(!inAggiornamento){
			model.setDescrizioneRiga(model.getCausaleCarta());
		}
		//
	}
	
	/**
	 * Inserimento di una riga
	 * @return
	 */
	public String inserisciRiga(){
		
		// controllo campi
		List<Errore> listaErrori = controllaCampiRiga(true);
		
		if(listaErrori!=null && listaErrori.size()>0){
			//ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		// richiamo metodo di mappatura model-entity
		PreDocumentoCarta nuovaRiga = popolaRigaDaModel();
		
		//Aggiungiamo la riga nel model:
		inserisciRigaInternal(nuovaRiga);
		
		//puliamoi campi nel model:
		pulisciCampiRiga();
		
		return VAI_STEP2;
	}
	
	/**
	 * Aggiunge all'elenco nel model la riga indicata
	 * @param nuovaRiga
	 */
	public void inserisciRigaInternal(PreDocumentoCarta nuovaRiga){
		
		if(model.getListaRighe()!=null && model.getListaRighe().size()>0){			
			//associo il numero max+1 alla nuova riga
			int numeratore = 0;
			for(PreDocumentoCarta riga : model.getListaRighe()){
				if(riga.getNumero()!=null && riga.getNumero()>numeratore){
					numeratore = riga.getNumero();
				}
			}
			
			nuovaRiga.setNumero(numeratore+1);
			model.getListaRighe().add(nuovaRiga);
			
		}else{
			//primo elemento della lista righe
			nuovaRiga.setNumero(1);
			List<PreDocumentoCarta> listaRighe = new ArrayList<PreDocumentoCarta>();
			listaRighe.add(nuovaRiga);
			model.setListaRighe(listaRighe);
		}
	}
	
	/**
	 * Metodo per l'aggiornamento di una riga
	 * @return
	 */
	public String aggiornaRiga(){
		
		// controllo campi
		List<Errore> listaErrori = controllaCampiRiga(true);
		
		if(listaErrori!=null && listaErrori.size()>0){
			//ci sono errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		//richiamo metodo di mappatura model-entity
		PreDocumentoCarta nuovaRiga = popolaRigaDaModel();
							
		//associo il numero alla riga selezionata
		nuovaRiga.setNumero(model.getRigaInModifica().getNumero());
		
		//compongo la lista con tutte le righe:
		List<PreDocumentoCarta> listaRigheAggiornata = new ArrayList<PreDocumentoCarta>();
		
		for(PreDocumentoCarta rigaIterata : model.getListaRighe()){
			if(rigaIterata.getNumero().intValue()==nuovaRiga.getNumero().intValue()){
				listaRigheAggiornata.add(nuovaRiga);
			} else {
				listaRigheAggiornata.add(rigaIterata);
			}			
		}		
		
		//setto la lista nel model:
		model.setListaRighe(listaRigheAggiornata);
		
		//pulisco i campi riga:
		pulisciCampiRiga();
		
		//vado allo step 2:
		return VAI_STEP2;
	}
	
	/**
	 * Aggiornamento da modale di una riga
	 * @return
	 */
	public String aggiornaRigaDaModale(){
		
		//carichiamo i dati della riga:
		caricaDatiRiga(model.getRigaInModifica().getNumero().toString(), true);
		
		// controllo campi
		List<Errore> listaErrori = controllaCampiRiga(false);
		
		if(listaErrori!=null && listaErrori.size()>0){
			addErrori(listaErrori);
			return INPUT;
		}
		
		//richiamo metodo di mappatura model-entity dal model della modale
		PreDocumentoCarta nuovaRiga = popolaRigaDaModelXmodale();
							
		//associo il numero alla riga selezionata
		nuovaRiga.setNumero(model.getRigaInModifica().getNumero());
				
		List<PreDocumentoCarta> listaRigheAggiornata = new ArrayList<PreDocumentoCarta>();
		
		for(PreDocumentoCarta rigaIterata : model.getListaRighe()){
			if(rigaIterata.getNumero().intValue()==nuovaRiga.getNumero().intValue()){
				listaRigheAggiornata.add(nuovaRiga);
			} else {
				listaRigheAggiornata.add(rigaIterata);
			}			
		}		
		
		//settiamo la lista nel model:
		model.setListaRighe(listaRigheAggiornata);
		
		//puliamo i campi riga:
		pulisciCampiRiga();
					
		return VAI_STEP2;
	}
	
	/**
	 * Elimina una riga
	 *
	 * @param numeroRigaSelezionata
	 * @return
	 */
	public String eliminaRiga(String numeroRigaSelezionata){
		
		List<PreDocumentoCarta> listaRigheAggiornata = new ArrayList<PreDocumentoCarta>();
		
		//costruisco una lista di righe con tutte tranne quella che si intende eliminare:
		for(PreDocumentoCarta rigaIterata : model.getListaRighe()){
			if(rigaIterata.getNumero().intValue()==Integer.valueOf(numeroRigaSelezionata)){
				//non copio la riga da eliminare nella nuova lista righe
			} else {
				listaRigheAggiornata.add(rigaIterata);
			}			
		}		
		
		//setto la lista nel mode:
		model.setListaRighe(listaRigheAggiornata);
		
		//pulisco i campi
		pulisciCampiRiga();
		
		
		//vado allo step 2:
		return VAI_STEP2;
	}
	
	public String indietroRiga(){								
		pulisciCampiRiga();
		
		return VAI_STEP2;
	}
	
	public String annullaInserisciRiga(){										
		return "vaiInserisciRigaDaMovimento";
	}		
		
	/**
	 * Controlla eventuali errori nei campi riga
	 * 
	 * @param flagDaInserimento
	 * @return
	 */
	private List<Errore> controllaCampiRiga(boolean flagDaInserimento){
		
		List<Errore> listaErrori = new ArrayList<Errore>();			
		
		//controllo se specificato anno impegno che ci sia il numero impegno
		if(model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0 && (model.getNumeroImpegno()==null || model.getNumeroImpegno()==0)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno"));
		}
		
		//controllo se specificato numero impegno che ci sia l'anno impegno
		if(model.getNumeroImpegno()!=null && model.getNumeroImpegno()!=0 && (model.getAnnoImpegno()==null || model.getAnnoImpegno()==0)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno"));
		}	
		
		//controllo annoImpegno <= anno esercizio				
		if(model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0){
			if(model.getAnnoImpegno() > sessionHandler.getAnnoBilancio()){
				listaErrori.add(ErroreFin.ANNO_MOVIMENTO_NON_VALIDO.getErrore());	
			}		
		}
		
		//Controllo DataEsecuzioneRiga
		/* SIAC-6076 tolgo data esecuzione pagamento 
		int annoEsercizio= sessionHandler.getAnnoBilancio();
		if (!FinStringUtils.isEmpty(model.getDataEsecuzioneRiga())) {
			DateFormat df=null;
			Date parsedDate=null;
			if (model.getDataEsecuzioneRiga().length() == 8) {
				df = new SimpleDateFormat("dd/MM/yy");
			} else {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}

			try {
				parsedDate=df.parse(model.getDataEsecuzioneRiga());

				Calendar cal = Calendar.getInstance();
				cal.setTime(parsedDate);

				Date now=new Date();
				Calendar calNow = Calendar.getInstance();
				calNow.setTime(now);
				calNow.set(Calendar.HOUR_OF_DAY, 0);
				calNow.set(Calendar.MINUTE, 0);
				calNow.set(Calendar.SECOND, 0);
				calNow.set(Calendar.MILLISECOND, 0);

				if (cal.get(Calendar.YEAR)!=annoEsercizio) {
					listaErrori.add(ErroreFin.DATE_INCONGRUENTI.getErrore(": data esecuzione pagamento deve essere compresa nell'anno esercizio e maggiore o uguale alla data corrente.",""));				
				} else if (calNow.after(cal)) {
					listaErrori.add(ErroreFin.DATE_INCONGRUENTI.getErrore(": data esecuzione pagamento deve essere compresa nell'anno esercizio e maggiore o uguale alla data corrente.",""));				
				}

			} catch (ParseException e) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data esecuzione pagamento",""));				
			}
		}else{
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data esecuzione pagamento"));
		}	*/

		
		//controllo stato dell'impegno/subimpegno definitivo o definitivo non liquidabile ---> verificare valore impegno da compilazione guidata per evitare di nuovo il controllo sullo stato dell'impegno
		if(model.getImpegno()==null || (model.getImpegno()!=null && this.model.getNumeroImpegno()!=null) && flagDaInserimento){
		
			
			RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			rip.setEnte(sessionHandler.getEnte());
			rip.setRichiedente(sessionHandler.getRichiedente());
			RicercaImpegnoK k = new RicercaImpegnoK();
			k.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			k.setAnnoImpegno(this.model.getAnnoImpegno());
			k.setNumeroImpegno(new BigDecimal(this.model.getNumeroImpegno()));
			
			
			//MARZO 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
			boolean indicatoSub = false;
			if(isCompilatoNumeroSubImpegno()){
				//Selezionato un SUB 
				indicatoSub = true;
				rip.setCaricaSub(true);
				k.setNumeroSubDaCercare(new BigDecimal(model.getNumeroSub()));
			} else {
				//Non selezionato un SUB
				indicatoSub = false;
				k.setNumeroSubDaCercare(null);
				rip.setCaricaSub(false);
			}
			//
			rip.setpRicercaImpegnoK(k);
			
			RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(rip);
			boolean corrispondenzaImpegnoSubImpegno = false;
			
			if (respRk != null && respRk.getImpegno() != null) {
				Impegno impegno = respRk.getImpegno();
				
				String stato = null;
				boolean  validato = impegno.isValidato();
				if (impegno != null) {
					

					
					//se impegno ha sumbimpegno riga deve essere collegata a un sub
					
					List<SubImpegno> elencoSubImpegni = null;
					if(indicatoSub){
						//Se ho indicato un sub avro' l'elenco COMPLETO DI TUTTE LE INFO con il SOLO sub Esplicitamente RICHIESTO
						elencoSubImpegni = impegno.getElencoSubImpegni();
					} else {
						//Se NON ho indicato un sub avro' solo l'elenco con LE INFO MINIME per tutti i sub
						//ma non e' un problema perche' tanto verra' lanciato l'errore che se NON INDICO UN SUB e CI SONO DEI SUB 
						//DOVEVA essere obbligatorio indicarne uno
						elencoSubImpegni =  respRk.getElencoSubImpegniTuttiConSoloGliIds();
					}
					
					
					if (elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
						//Esistono sub
						if (!isCompilatoNumeroSubImpegno()) {
							//IN CASO DI IMPEGNO CON DEI SUB DEVE ESSERE SELEZIONATO UN SUBIMPEGNO
							listaErrori.add(ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
						} else {
							//se impegno ha sumbimpegno riga deve essere collegata a un sub
							for (int i = 0; i < elencoSubImpegni.size(); i++) {
								BigDecimal numeroSubImpegno = elencoSubImpegni.get(i).getNumeroBigDecimal();
								if (numeroSubImpegno.intValueExact() == model.getNumeroSub().intValue()) {
									
									corrispondenzaImpegnoSubImpegno = true;								
	
									stato = elencoSubImpegni.get(i).getDescrizioneStatoOperativoMovimentoGestioneSpesa();
									validato = elencoSubImpegni.get(i).isValidato();
	
									if (stato != null && !stato.equals("DEFINITIVO")
											&& !stato.equals("DEFINITIVO NON LIQUIDABILE")) {
										listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("STATO IMPEGNO = "+ stato));
									}
	
									 if(!validato){
										 listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("impegno NON VALIDATO"));
									 }
	
									 
									 //controllo coerenza soggetto Maschera con soggetto SubImpegno
									 if( model.getSoggetto() != null && 
											 !FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore()) &&
											 elencoSubImpegni.get(i).getSoggetto().getCodiceSoggetto().compareTo(model.getSoggetto().getCodCreditore())!=0){
										 
										 listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore());
									 }else {
										 
										 if( model.getSoggetto() == null || FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())){												 
											 //se e' stato selezionato il sub ma non il soggetto in maschera imposto il soggetto del sub nel model e la sua prima mdp
											 SoggettoImpegnoModel sogg = new SoggettoImpegnoModel();
											 sogg.setCodCreditore(elencoSubImpegni.get(i).getSoggetto().getCodiceSoggetto());
											 sogg.setDenominazione(elencoSubImpegni.get(i).getSoggetto().getDenominazione());
											 sogg.setStato(elencoSubImpegni.get(i).getSoggetto().getStatoOperativo());
											 model.setSoggetto(sogg);										 
											 
											//setto come mdp di default la prima valida in ordine di priorita'
											if(elencoSubImpegni.get(i).getSoggetto().getElencoModalitaPagamento()!=null && elencoSubImpegni.get(i).getSoggetto().getElencoModalitaPagamento().size()>0){
												caricaMdpDiDefault(elencoSubImpegni.get(i).getSoggetto().getElencoModalitaPagamento());
											}
										 }	
									}															 
									break;
								}										
							}
						}
						if (!corrispondenzaImpegnoSubImpegno) {
							if(listaErrori==null || listaErrori.size()==0){
								listaErrori.add(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
							}	
						}
					} else {					
						//Non ci sono sub
						if (model.getNumeroSub() != null && !model.getNumeroSub().equals(0)) {
							if(listaErrori==null || listaErrori.size()==0){
								listaErrori.add(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
							}	
						}
						
						stato = impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa();
						String codiceStato = impegno.getStatoOperativoMovimentoGestioneSpesa();
						validato = impegno.isValidato();
	
						if (stato != null && !stato.equals("DEFINITIVO") && !stato.equals("DEFINITIVO NON LIQUIDABILE")) {
							listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("STATO IMPEGNO = " + stato));
						}
						
						//controllo la classe soggetto dell'impegno con la classe del soggetto in maschera
						if(stato.equals("DEFINITIVO")){							
							if(impegno.getSoggetto()!=null && (impegno.getSoggetto().getCodiceSoggetto()==null || FinStringUtils.isEmpty(impegno.getSoggetto().getCodiceSoggetto()))){								
								
								if(impegno.getClasseSoggetto()!=null && impegno.getClasseSoggetto().getCodice()!=null && !FinStringUtils.isEmpty(impegno.getClasseSoggetto().getCodice())){									
									
									if(model.getSoggetto()!=null && model.getSoggetto().getCodCreditore()!=null && !FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore()) 
											&& model.getListaClassificazioneSoggetto()!=null && model.getListaClassificazioneSoggetto().size()>0){
																			
										boolean flagClasseCorrispondente = false;
										for(ClassificazioneSoggetto classe : model.getListaClassificazioneSoggetto()){
											if(classe.getSoggettoClasseCode().compareTo(impegno.getClasseSoggetto().getCodice())==0){
												flagClasseCorrispondente = true;
											}
										}
										
										if(!flagClasseCorrispondente){
											listaErrori.add(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore());
										}
									}
								}																							
							}
						}
						
						if(!validato){
							listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("impegno NON VALIDATO"));
						}											

						//stato impegno definitivo e COD CREDITORE presente
						if (stato != null && codiceStato.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO) && 
							 model.getSoggetto() != null && 
							 model.getSoggetto().getCodCreditore()!=null) {


							 //controllo coerenza soggetto Impegno con il soggetto espresso in maschera 
							 if (impegno.getSoggetto()!=null && impegno.getSoggetto().getCodiceSoggetto()!=null && !FinStringUtils.isEmpty(impegno.getSoggetto().getCodiceSoggetto())
									 && model.getSoggetto().getCodCreditore()!=null && !FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())
									 &&	 impegno.getSoggetto().getCodiceSoggetto().compareTo(model.getSoggetto().getCodCreditore())!=0) {
								 listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
							 } else if (impegno.getSoggetto()!=null && impegno.getSoggetto().getCodiceSoggetto()!=null) {
								if ( model.getSoggetto() == null ||
										 model.getSoggetto().getCodCreditore()==null || FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())) {

									//imposto soggetto dell'impegno in soggetto della riga se non e' stato specificato in maschera
									SoggettoImpegnoModel sogg = new SoggettoImpegnoModel();
									sogg.setCodCreditore(impegno.getSoggetto().getCodiceSoggetto());
									sogg.setDenominazione(impegno.getSoggetto().getDenominazione());
									sogg.setStato(impegno.getSoggetto().getStatoOperativo());
									model.setSoggetto(sogg);									
									 
									//setto come mdp di default la prima valida in ordine di priorita'
									if(impegno.getSoggetto().getElencoModalitaPagamento()!=null && impegno.getSoggetto().getElencoModalitaPagamento().size()>0){
										caricaMdpDiDefault(impegno.getSoggetto().getElencoModalitaPagamento());
									}																		
								}
							}
						 }
					}	
				}				
			} else {
				listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));return listaErrori;
			}
		}					
		
		//controllo esistenza del soggetto espresso		
		if (model.getSoggetto() == null || model.getSoggetto().getCodCreditore()==null || FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto :Codice "));
		}
		
		//controllo stato valido o sospeso del soggetto
		if (model.getSoggetto() != null	&& model.getSoggetto().getStato() != null	&& !(model.getSoggetto().getStato()==StatoOperativoAnagrafica.VALIDO) && !(model.getSoggetto().getStato()==StatoOperativoAnagrafica.SOSPESO)) {
			listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		}
		
		//controllo obbligatorieta modpag
		ModalitaPagamentoSoggetto mps = null;
		SedeSecondariaSoggetto sss = null;
		if(model.getListaModalitaPagamento()!=null && model.getListaModalitaPagamento().size()>0){
			for(int i=0; i<model.getListaModalitaPagamento().size(); i++){
				if(model.getListaModalitaPagamento().get(i).getUid() == (model.getRadioModPagSelezionato())){
					mps = model.getListaModalitaPagamento().get(i);
					break;
				}
			}
		}
		if(mps==null && flagDaInserimento){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("modalita di pagamento"));
		}
		if(model.getListaSediSecondarie()!=null && !model.getListaSediSecondarie().isEmpty()){
			
			for(int i=0; i<model.getListaSediSecondarie().size(); i++){
				if(model.getListaSediSecondarie().get(i).getUid() == (model.getRadioSediSecondarieSoggettoSelezionato())){
					sss = model.getListaSediSecondarie().get(i);
					break;
				}
			}
		}
		
		//controllo che la mdp non sia di tipo cessione
		if(mps!=null && mps.getModalitaAccreditoSoggetto()!=null && mps.getModalitaAccreditoSoggetto().getCodice()!=null && mps.getModalitaAccreditoSoggetto().getCodice().equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)){
			listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
		}		
		if(mps!=null && mps.getDescrizioneStatoModalitaPagamento()!=null && (mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("BLOCCATO") || mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("ANNULLATO")|| mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("PROVVISORIO"))){
			listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
		}
		
						
		//controllo obbligatorieta conto tesoriere
		if(model.getContoTesoriereRiga()==null || FinStringUtils.isEmpty(model.getContoTesoriereRiga())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("conto tesoriere"));
		}
		
		
		//controllo se valuta estera true => importo estero> 0
		if(model.isOptionsPagamentoEstero() && 
				(model.getImportoEsteroRiga()==null || !(model.getImportoEsteroRiga().compareTo(new BigDecimal(0))>0))){
			listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("l'importo valuta estera deve essere maggiore di zero"));
		}
		
		//controllo importo > 0 && controllo importo valuta esteara < disponibile
		if (model.getImportoRiga()!= null && model.isOptionsPagamentoEstero()) {			

			
		}else if(model.getImportoRiga()!= null && !(model.getImportoRiga().compareTo(new BigDecimal(0))>0)){
			listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("l'importo della riga deve essere maggiore di zero"));
		} else if(model.getImportoRiga()== null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo riga"));
		}	
		
		
		//controllo obbligatorieta descrizione riga
		if(model.getDescrizioneRiga()==null || FinStringUtils.isEmpty(model.getDescrizioneRiga())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione riga"));
		}
								
		//controllo duplicazione 		
		if(model.getListaRighe()!=null && model.getListaRighe().size()>0 && flagDaInserimento){			
			for(PreDocumentoCarta rigaIterata :model.getListaRighe()){
				//effettuo i controlli di duplicazione della riga attuale con tutte e sole le altre righe
				if(rigaIterata.getNumero()!=null && model.getNumeroRigaSelezionata()!=null &&  !FinStringUtils.isEmpty(model.getNumeroRigaSelezionata())
						&&	rigaIterata.getNumero()==Integer.valueOf(model.getNumeroRigaSelezionata())){
					break;
				}	
					
					
				boolean flagDuplicato=true;
				
				//confronto anno impegno
				if(rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getAnnoMovimento()!=0 && model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0
						&& model.getAnnoImpegno()!=rigaIterata.getImpegno().getAnnoMovimento()){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()==null || rigaIterata.getImpegno().getAnnoMovimento()==0)
						&& (model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0)){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getAnnoMovimento()!=0)
						&& (model.getAnnoImpegno()==null || model.getAnnoImpegno()==0)){
					flagDuplicato=false;
				}
				
				//confronto numero impegno
				if(model.getNumeroImpegno()!=null && rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getNumeroBigDecimal() != null && model.getNumeroImpegno()!=null && model.getNumeroImpegno()!=0
						&& model.getNumeroImpegno() != rigaIterata.getImpegno().getNumeroBigDecimal().intValue()){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()==null || rigaIterata.getImpegno().getNumeroBigDecimal()==null || rigaIterata.getImpegno().getNumeroBigDecimal().intValue()==0)
						&& (model.getNumeroImpegno()!=null && model.getNumeroImpegno()!=0)){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getNumeroBigDecimal()!=null && rigaIterata.getImpegno().getNumeroBigDecimal().intValue()!=0)
						&& (model.getNumeroImpegno()==null || model.getNumeroImpegno()==0)){
					flagDuplicato=false;
				}
				
				//confronto sub impegno
				if(model.getNumeroSub()!=null && rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getElencoSubImpegni() != null && rigaIterata.getImpegno().getElencoSubImpegni().size()>0 
						&& model.getNumeroSub() != rigaIterata.getImpegno().getElencoSubImpegni().get(0).getNumeroBigDecimal().intValue()){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()==null || rigaIterata.getImpegno().getElencoSubImpegni() == null || rigaIterata.getImpegno().getElencoSubImpegni().size()==0 || rigaIterata.getImpegno().getElencoSubImpegni().get(0).getNumeroBigDecimal().intValue()==0)
						&& (model.getNumeroSub()!=null && model.getNumeroSub()!=0)){
					flagDuplicato=false;
				}
				if((rigaIterata.getImpegno()!=null && rigaIterata.getImpegno().getElencoSubImpegni() != null && rigaIterata.getImpegno().getElencoSubImpegni().size()>0 && rigaIterata.getImpegno().getElencoSubImpegni().get(0).getNumeroBigDecimal().intValue()!=0)
						&& (model.getNumeroSub()==null || model.getNumeroSub()==0)){
					flagDuplicato=false;
				}
				
				//confronto soggetto
				if(model.getSoggetto()!=null && model.getSoggetto().getCodCreditore()!=null && rigaIterata.getSoggetto()!=null && rigaIterata.getSoggetto().getCodiceSoggetto()!= null && !FinStringUtils.isEmpty(rigaIterata.getSoggetto().getCodiceSoggetto())){
					if(model.getSoggetto().getCodCreditore().compareToIgnoreCase(rigaIterata.getSoggetto().getCodiceSoggetto())!=0){
						flagDuplicato=false;
					}
				}
				//confronto sede				
				if(sss!=null && rigaIterata.getSoggetto()!=null && rigaIterata.getSoggetto().getSediSecondarie()!=null && rigaIterata.getSoggetto().getSediSecondarie().size()>0 && !FinStringUtils.isEmpty(rigaIterata.getSoggetto().getSediSecondarie().get(0).getCodiceSedeSecondaria())){
					//caso in cui sia selezionata la sede in maschera e ci sia per la riga iterata
					if(sss.getCodiceSedeSecondaria().compareToIgnoreCase(rigaIterata.getSoggetto().getSediSecondarie().get(0).getCodiceSedeSecondaria())!=0){
						flagDuplicato=false;
					}					
				}else if(sss==null && (rigaIterata.getSoggetto().getSediSecondarie()!=null && rigaIterata.getSoggetto().getSediSecondarie().size()>0)){
					//caso in cui non sia stata selezionata una sede ma esiste invece per la riga iterata 
					flagDuplicato=false;
				}else if(sss!=null && (rigaIterata.getSoggetto().getSediSecondarie()==null || rigaIterata.getSoggetto().getSediSecondarie().size()==0)){
					//caso in cui e' stata selezionata una sede ma la riga iterata non ne possiede
					flagDuplicato=false;
				}
				//confronto mdp tra le mpd dei soggetti
				if(mps!=null && rigaIterata.getSoggetto()!=null && rigaIterata.getSoggetto().getElencoModalitaPagamento()!=null && rigaIterata.getSoggetto().getElencoModalitaPagamento().size()>0 && rigaIterata.getSoggetto().getElencoModalitaPagamento().get(0).getModalitaAccreditoSoggetto()!=null && !FinStringUtils.isEmpty(rigaIterata.getSoggetto().getElencoModalitaPagamento().get(0).getModalitaAccreditoSoggetto().getCodice())){
					if(mps.getModalitaAccreditoSoggetto().getCodice().compareToIgnoreCase(rigaIterata.getSoggetto().getElencoModalitaPagamento().get(0).getModalitaAccreditoSoggetto().getCodice())!=0){
						flagDuplicato=false;
					}
				}
				
				if(flagDuplicato && model.getListaRighe()!=null && model.getListaRighe().size()>0){
					listaErrori.add(ErroreFin.RELAZIONE_GIA_PRESENTE.getErrore(""));
				}				
			}
		}
				
		return listaErrori;
	}
	
	private boolean isCompilatoNumeroSubImpegno(){
		boolean compilato = false;
		if(model.getNumeroSub()!=null && model.getNumeroSub().intValue()>0){
			compilato = true;
		}
		return compilato;
	}
	
	
	private PreDocumentoCarta popolaRigaDaModel(){
		
		PreDocumentoCarta riga = new PreDocumentoCarta();
		
		//campi Soggetto
		if(model.getSoggetto()!=null && !FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())){			
			riga.setSoggetto(new Soggetto());
			riga.getSoggetto().setCodiceSoggetto(model.getSoggetto().getCodCreditore());
			
			if(!FinStringUtils.isEmpty(model.getSoggetto().getDenominazione())){
				riga.getSoggetto().setDenominazione(model.getSoggetto().getDenominazione());
			}			
			
			if(model.getSedeSelezionata()!=null){
				List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
				listaSediSecondarie.add(model.getSedeSelezionata());
				riga.getSoggetto().setSediSecondarie(listaSediSecondarie);
			}
			
			if(model.getModpagamentoSelezionata()!=null){
				List<ModalitaPagamentoSoggetto> listaMod=new ArrayList<ModalitaPagamentoSoggetto>();
				listaMod.add(model.getModpagamentoSelezionata());
				riga.getSoggetto().setElencoModalitaPagamento(listaMod);
			}		
		}
		
		//campi impegno
		if(model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0 
				&& model.getNumeroImpegno()!=null && model.getNumeroImpegno()!=0){
			
			riga.setImpegno(new Impegno());
			riga.getImpegno().setNumeroBigDecimal(new BigDecimal(model.getNumeroImpegno()));
			riga.getImpegno().setAnnoMovimento(model.getAnnoImpegno());
			
			if(model.getNumeroSub()!=null && model.getNumeroSub()!=0){
				SubImpegno subImpegno = new SubImpegno();
				List<SubImpegno> listSubImpegni = new ArrayList<SubImpegno>();
				subImpegno.setNumeroBigDecimal(new BigDecimal(model.getNumeroSub()));
				listSubImpegni.add(subImpegno);
				riga.getImpegno().setElencoSubImpegni(listSubImpegni);
			}			
		}
		
		//campi riga		
		if(model.getDataEsecuzioneRiga()!=null){
			riga.setDataEsecuzioneRiga(DateUtility.parse(model.getDataEsecuzioneRiga()));
			riga.setDataDocumento(DateUtility.parse(model.getDataEsecuzioneRiga()));
		}
		if(model.getImportoRiga()!=null){
			riga.setImporto(model.getImportoRiga());
		}
		if(model.getImportoEsteroRiga()!=null){
			riga.setImportoValutaEstera(model.getImportoEsteroRiga());
		}
		if(model.getDescrizioneRiga()!=null){
			riga.setDescrizione(model.getDescrizioneRiga());
		}
		if(model.getNoteRiga()!=null){
			riga.setNote(model.getNoteRiga());
		}
		if(model.getContoTesoriereRiga()!=null && !FinStringUtils.isEmpty(model.getContoTesoriereRiga())){						
			riga.setContoTesoreria(new ContoTesoreria());
			riga.getContoTesoreria().setCodice(model.getContoTesoriereRiga());
		}
		
		//documenti
		if(model.getListaSubDocumentoSpesa()!=null && model.getListaSubDocumentoSpesa().size()>0){
			riga.setListaSubDocumentiSpesaCollegati(model.getListaSubDocumentoSpesa());
		}
		
		
		
		return riga;
	}
	
	private PreDocumentoCarta popolaRigaDaModelXmodale(){
		
		PreDocumentoCarta riga = new PreDocumentoCarta();
		
		//campi Soggetto
		if(model.getSoggetto()!=null && !FinStringUtils.isEmpty(model.getSoggetto().getCodCreditore())){			
			riga.setSoggetto(new Soggetto());
			riga.getSoggetto().setCodiceSoggetto(model.getSoggetto().getCodCreditore());
			
			if(!FinStringUtils.isEmpty(model.getSoggetto().getDenominazione())){
				riga.getSoggetto().setDenominazione(model.getSoggetto().getDenominazione());
			}
			
			if(model.getSedeSelezionata()!=null){
				List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
				listaSediSecondarie.add(model.getSedeSelezionata());
				riga.getSoggetto().setSediSecondarie(listaSediSecondarie);
			}
			
			if(model.getModpagamentoSelezionata()!=null){
				List<ModalitaPagamentoSoggetto> listaMod=new ArrayList<ModalitaPagamentoSoggetto>();
				listaMod.add(model.getModpagamentoSelezionata());
				riga.getSoggetto().setElencoModalitaPagamento(listaMod);
			}		
		}				
		
		//campi impegno
		if(model.getAnnoImpegno()!=null && model.getAnnoImpegno()!=0 
				&& model.getNumeroImpegno()!=null && model.getNumeroImpegno()!=0){
			
			riga.setImpegno(new Impegno());
			riga.getImpegno().setNumeroBigDecimal(new BigDecimal(model.getNumeroImpegno()));
			riga.getImpegno().setAnnoMovimento(model.getAnnoImpegno());
			
			if(model.getNumeroSub()!=null && model.getNumeroSub()!=0){
				SubImpegno subImpegno = new SubImpegno();
				List<SubImpegno> listSubImpegni = new ArrayList<SubImpegno>();
				subImpegno.setNumeroBigDecimal(new BigDecimal(model.getNumeroSub()));
				riga.getImpegno().setElencoSubImpegni(listSubImpegni);
			}			
		}
				
		//campi riga		
		if(model.getRigaInModifica().getDataEsecuzioneRiga()!=null){
			riga.setDataEsecuzioneRiga(model.getRigaInModifica().getDataEsecuzioneRiga());
		}
		if(model.getRigaInModifica().getImporto()!=null){
			riga.setImporto(model.getRigaInModifica().getImporto());
		}
		if(model.getImportoEsteroRiga()!=null){
			riga.setImportoValutaEstera(model.getImportoEsteroRiga());
		}
		if(model.getRigaInModifica().getDescrizione()!=null){
			riga.setDescrizione(model.getRigaInModifica().getDescrizione());
		}
		if(model.getRigaInModifica().getNote()!=null){
			riga.setNote(model.getRigaInModifica().getNote());
		}
		if(model.getRigaInModifica().getContoTesoreria().getCodice()!=null && !FinStringUtils.isEmpty(model.getRigaInModifica().getContoTesoreria().getCodice())){						
			riga.setContoTesoreria(new ContoTesoreria());
			riga.getContoTesoreria().setCodice(model.getRigaInModifica().getContoTesoreria().getCodice());
		}
		
		//SUB DOC SPESA:
		riga.setListaSubDocumentiSpesaCollegati(model.getListaSubDocumentoSpesa());
		//
		
		return riga;
	}
	
	//carica dati riga per aggiorna riga da aggiorna carta
	protected void caricaDatiRiga(String rigaId, boolean isAggiornaRigaModale){

		PreDocumentoCarta rigaSelezionata = new PreDocumentoCarta();
		
		if(model.getListaRighe()!=null && model.getListaRighe().size()>0){
			for(PreDocumentoCarta riga : model.getListaRighe()){
				if(riga.getNumero()==Integer.valueOf(rigaId)){
					rigaSelezionata=riga;
					if(!isAggiornaRigaModale){
						model.setRigaInModifica(riga);
					}
					break;
				}
			}
		}			
			
		//campi riga		
		if(rigaSelezionata.getDataEsecuzioneRiga()!=null){
			model.setDataEsecuzioneRiga(DateUtility.convertiDataInGgMmYyyy(rigaSelezionata.getDataEsecuzioneRiga()));
		}
		if(rigaSelezionata.getImporto()!=null){
			model.setImportoRiga(rigaSelezionata.getImporto());
		}
		if(rigaSelezionata.getImportoValutaEstera()!=null){
			model.setImportoEsteroRiga(rigaSelezionata.getImportoValutaEstera());
		}
		if(rigaSelezionata.getDescrizione()!=null){
			model.setDescrizioneRiga(rigaSelezionata.getDescrizione());
		}
		if(rigaSelezionata.getNote()!=null){
			model.setNoteRiga(rigaSelezionata.getNote());
		}
		if(rigaSelezionata.getContoTesoreria()!=null && rigaSelezionata.getContoTesoreria().getCodice()!=null 
				&& !FinStringUtils.isEmpty(rigaSelezionata.getContoTesoreria().getCodice())){
			model.setContoTesoriereRiga(rigaSelezionata.getContoTesoreria().getCodice());
		}				
		if(rigaSelezionata.getImportoDaRegolarizzare()!=null){
			model.setImportoDaRegolarizzare(rigaSelezionata.getImportoDaRegolarizzare());
		}
		
		
		//START FIX PER  SIAC-4791:
		if(isAggiornaRigaModale){
			
			PreDocumentoCarta datiRigaModale = model.getRigaInModifica();
			
			if(datiRigaModale.getImporto()!=null){
				model.setImportoRiga(datiRigaModale.getImporto());
			}
			if(datiRigaModale.getDescrizione()!=null){
				model.setDescrizioneRiga(datiRigaModale.getDescrizione());
			}
			
			if(datiRigaModale.getContoTesoreria()!=null && datiRigaModale.getContoTesoreria().getCodice()!=null 
					&& !FinStringUtils.isEmpty(datiRigaModale.getContoTesoreria().getCodice())){
				model.setContoTesoriereRiga(datiRigaModale.getContoTesoreria().getCodice());
			}	
			
			if(datiRigaModale.getNote()!=null){
				model.setNoteRiga(datiRigaModale.getNote());
			}
			
			if(datiRigaModale.getDataEsecuzioneRiga()!=null){
				model.setDataEsecuzioneRiga(DateUtility.convertiDataInGgMmYyyy(datiRigaModale.getDataEsecuzioneRiga()));
			}
			
		}
		// END FIX PER  SIAC-4791

		//campi impegno
		if(rigaSelezionata.getImpegno()!=null 
				&& rigaSelezionata.getImpegno().getAnnoMovimento()!=0
				&& rigaSelezionata.getImpegno().getNumeroBigDecimal()!=null){
			model.setAnnoImpegno(rigaSelezionata.getImpegno().getAnnoMovimento());
			model.setNumeroImpegno(rigaSelezionata.getImpegno().getNumeroBigDecimal().intValue());
			model.setHasImpegnoSelezionatoXPopup(true);
			
			if(rigaSelezionata.getImpegno().getDescrizione()!=null && !FinStringUtils.isEmpty(rigaSelezionata.getImpegno().getDescrizione())){
				model.setDescrizioneImpegnoPopup(rigaSelezionata.getImpegno().getDescrizione());
			}	
			
			if(rigaSelezionata.getImpegno().getElencoSubImpegni()!=null
					&& rigaSelezionata.getImpegno().getElencoSubImpegni().size()>0){
				model.setNumeroSub(rigaSelezionata.getImpegno().getElencoSubImpegni().get(0).getNumeroBigDecimal().intValue());
			}
		}
		
		//campi Soggetto
		if(rigaSelezionata.getSoggetto()!=null 
				&& rigaSelezionata.getSoggetto().getCodiceSoggetto()!=null && !FinStringUtils.isEmpty(rigaSelezionata.getSoggetto().getCodiceSoggetto())){
			model.setSoggetto(new SoggettoImpegnoModel());
			model.getSoggetto().setCodCreditore(rigaSelezionata.getSoggetto().getCodiceSoggetto());
			model.setSoggettoSelezionato(true);
			// carico anche l'uid
			model.getSoggetto().setUid(rigaSelezionata.getSoggetto().getUid());
			
			if(!FinStringUtils.isEmpty(rigaSelezionata.getSoggetto().getDenominazione())){
				model.getSoggetto().setDenominazione(rigaSelezionata.getSoggetto().getDenominazione());
			}	
			if(!FinStringUtils.isEmpty(rigaSelezionata.getSoggetto().getCodiceFiscale())){
				model.getSoggetto().setCodfisc(rigaSelezionata.getSoggetto().getCodiceFiscale());
			}	

			if(isAggiornaRigaModale){
				if(rigaSelezionata.getSoggetto()!=null && rigaSelezionata.getSoggetto().getSediSecondarie()!=null && rigaSelezionata.getSoggetto().getSediSecondarie().size()>0){
					model.setSedeSelezionata(rigaSelezionata.getSoggetto().getSediSecondarie().get(0));
				}
				if(rigaSelezionata.getSoggetto().getElencoModalitaPagamento()!=null && rigaSelezionata.getSoggetto().getElencoModalitaPagamento().size()>0){
					model.setModpagamentoSelezionata(rigaSelezionata.getSoggetto().getElencoModalitaPagamento().get(0));
				}		
			} else {				
				//richiamo il soggetto per chiave per caricare la prima volta le liste sedi e mdp in aggiorna riga da aggiorna carta
				ricercaSoggettoPerChiaveRiga();
				
				if(rigaSelezionata.getSedeSecondariaSoggetto()!=null && rigaSelezionata.getSedeSecondariaSoggetto().getModalitaPagamentoSoggettos()!=null && rigaSelezionata.getSedeSecondariaSoggetto().getModalitaPagamentoSoggettos().size()>0){
					model.setSedeSelezionata(rigaSelezionata.getSedeSecondariaSoggetto());
					model.setRadioSediSecondarieSoggettoSelezionato(rigaSelezionata.getSedeSecondariaSoggetto().getUid());
				}
				if(rigaSelezionata.getSoggetto().getElencoModalitaPagamento()!=null && rigaSelezionata.getSoggetto().getElencoModalitaPagamento().size()>0){
					model.setModpagamentoSelezionata(rigaSelezionata.getSoggetto().getElencoModalitaPagamento().get(0));
					model.setRadioModPagSelezionato(model.getModpagamentoSelezionata().getUid());
				}				
			}	
		}
		
		//campi documenti
		if(rigaSelezionata.getListaSubDocumentiSpesaCollegati()!=null && rigaSelezionata.getListaSubDocumentiSpesaCollegati().size()>0){
			model.setListaSubDocumentoSpesa(rigaSelezionata.getListaSubDocumentiSpesaCollegati());
		}
	}
	
	private void caricaMdpDiDefault(List<ModalitaPagamentoSoggetto> elencoModalitaPagamento){		
		List<ModalitaPagamentoSoggetto> listaMdp = new ArrayList<ModalitaPagamentoSoggetto>();			
		
		for(ModalitaPagamentoSoggetto mdp : elencoModalitaPagamento){
			if(mdp.getModalitaAccreditoSoggetto()!=null && mdp.getModalitaAccreditoSoggetto().getCodice()!=null 
					&& !mdp.isTipoCessione()){
				
				if(mdp.getDescrizioneStatoModalitaPagamento()!=null 
						&& !mdp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_BLOCCATO) 
						&& !mdp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)
						&& !mdp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)){
					
					
					if(mdp.getDataFineValidita()!=null){
						Date now=new Date();
						Calendar calNow = Calendar.getInstance();
						calNow.setTime(now);
						calNow.set(Calendar.HOUR_OF_DAY, 0);
						calNow.set(Calendar.MINUTE, 0);
						calNow.set(Calendar.SECOND, 0);
						calNow.set(Calendar.MILLISECOND, 0);
						
						if (!calNow.after(mdp.getDataFineValidita())) {
							listaMdp.add(mdp);
						}														
					} else {
						listaMdp.add(mdp);
					}																																							
				}																																																																									
			}
		}										
		
		if(listaMdp!=null && listaMdp.size()>0){
			model.setRadioModPagSelezionato(listaMdp.get(0).getUid());
			model.setListaModalitaPagamento(listaMdp);
			model.setModpagamentoSelezionata(listaMdp.get(0));
		}
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}
	
	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);		
	}
	
	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}
	
	protected void caricaListeCreditore(String codiceSoggetto){	
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(convertiModelPerChiamataServizioRicercaPerChiave(codiceSoggetto));
		if(response!=null){
			ArrayList<ModalitaPagamentoSoggetto> listMod= FinUtility.filtraValidi(response.getListaModalitaPagamentoSoggetto());
			
			ArrayList<SedeSecondariaSoggetto> listSedi= new ArrayList<SedeSecondariaSoggetto>();
			if(response.getListaSecondariaSoggetto()!= null){
				for(SedeSecondariaSoggetto sedeSec:response.getListaSecondariaSoggetto()){
					if(sedeSec.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_VALIDO) ){
							listSedi.add(sedeSec);
					}
				}
			}
			
			// msg da inserire nel popover
			if(response.getSoggetto()!=null){
				if(response.getSoggetto().isAvviso()){
					
				model.setAvvisoFrase(WebAppConstants.MSG_SI);
				}else {
					model.setAvvisoFrase(WebAppConstants.MSG_NO);
				}						
			}
						
			model.setListaModalitaPagamento(listMod);
			model.setListaModalitaPagamentoVisualizza(listMod);
			model.setModpagamentoSelezionata(null);	
			model.setRadioModPagSelezionato(0);
			
			String denominazioneSoggetto="";
			StatoOperativoAnagrafica StatoOperativoSoggetto=null;
			List<ClassificazioneSoggetto> elencoClassi=null;
			
			if (response.getSoggetto()!=null) {
				denominazioneSoggetto=response.getSoggetto().getDenominazione();
						StatoOperativoSoggetto=response.getSoggetto().getStatoOperativo();
						elencoClassi=response.getSoggetto().getElencoClass();
			}
			
			model.getSoggetto().setDenominazione(denominazioneSoggetto);
			model.getSoggetto().setStato(StatoOperativoSoggetto);
			
			model.setListaSediSecondarie(listSedi);
			model.setListaSediSecondarieVisualizza(listSedi);
			model.setListaClassificazioneSoggetto(elencoClassi);
		}
	}
	
	protected RicercaSoggettoPerChiave convertiModelPerChiamataServizioRicercaPerChiave(String codiceSoggetto) {
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		ricercaSoggettoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggettoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		parametroRicercaSoggettoK.setIncludeModif(false);
		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroRicercaSoggettoK);
		return ricercaSoggettoPerChiave;
	}
	
	protected String selezionaSoggettoRiga() {
		// recupero solo i soggetti validi o sospesi
		String ritorno = super.selezionaSoggettoControlloInSospeso();
		
		if(model.getSoggetto()!=null){
			
			ricercaSoggettoPerChiaveRiga();
			
			model.setModpagamentoSelezionata(null);
			model.setRadioModPagSelezionato(0);
			model.setSedeSelezionata(null);
			model.setRadioSediSecondarieSoggettoSelezionato(0);
			
		}
	
		return ritorno;
	
	}
	
	protected void ricercaSoggettoPerChiaveRiga() {
		RicercaSoggettoPerChiave rsc = new RicercaSoggettoPerChiave();
		rsc.setEnte(sessionHandler.getEnte());
		rsc.setRichiedente(sessionHandler.getRichiedente());
		ParametroRicercaSoggettoK k = new ParametroRicercaSoggettoK();
		
		k.setCodice(model.getSoggetto().getCodCreditore());
		
		rsc.setParametroSoggettoK(k);
		
		RicercaSoggettoPerChiaveResponse response =  soggettoService.ricercaSoggettoPerChiave(rsc);
		
		if(response!=null && !response.isFallimento()){
			
			if(response.getListaSecondariaSoggetto()!=null && response.getListaSecondariaSoggetto().size()>0){
				ArrayList<SedeSecondariaSoggetto> listaSede = new ArrayList<SedeSecondariaSoggetto>();
				for(SedeSecondariaSoggetto sede : response.getListaSecondariaSoggetto()){
					if(sede.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_VALIDO) ){
						listaSede.add(sede);
					}
				}
				model.setListaSediSecondarie(listaSede);
			}else  {
				// setto a vuoto la lista sedi
				model.setListaSediSecondarie(null);
			}
			
			if(response.getListaModalitaPagamentoSoggetto()!=null && response.getListaModalitaPagamentoSoggetto().size()>0){
				
				ArrayList<ModalitaPagamentoSoggetto> listMod= filtraModalitaPagamentoSoggettoPerMascheraWeb(response);
				
				model.setListaModalitaPagamento(listMod);
				model.setListaModalitaPagamentoVisualizza(listMod);
			
			}else{
				model.setListaModalitaPagamento(null);
				model.setListaModalitaPagamentoVisualizza(null);
			}
			
		}
	}
	
	protected String remodpagamento() {
		
		if(idSedeCreditoreChecked){
		
			String cod = String.valueOf(model.getRadioSediSecondarieSoggettoSelezionato());
			
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamento();
			List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarie();
			SedeSecondariaSoggetto sedeSelezionata = null;
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(Integer.parseInt(cod) == listaSediSecondarie.get(i).getUid()){
					sedeSelezionata = listaSediSecondarie.get(i);
					model.setSedeSelezionata(sedeSelezionata);
					break;
				}
			}
			
			if(sedeSelezionata!=null){
				List<ModalitaPagamentoSoggetto> relistaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
				int first = 0;
				if(null!=listaModalitaPagamento && listaModalitaPagamento.size()>0){
					for(int j=0; j<listaModalitaPagamento.size(); j++){				
						if(listaModalitaPagamento.get(j).getAssociatoA().equals(sedeSelezionata.getDenominazione())){
							//Inserisco le modalita' di pagamento solamente se sono in stato valida e se la data scadenza e' > della data attuale
							if(FinUtility.isValida(listaModalitaPagamento.get(j))){
								first = first + 1;
								relistaModalitaPagamento.add(listaModalitaPagamento.get(j));
							}
							if(first==1){	
								model.setRadioModPagSelezionato(listaModalitaPagamento.get(j).getUid());
								model.setModpagamentoSelezionata(listaModalitaPagamento.get(j));
							}
						}
					}
			    } 
				if(relistaModalitaPagamento.size()>0){
					model.setListaModalitaPagamentoVisualizza(relistaModalitaPagamento);
				}else{
					model.setListaModalitaPagamentoVisualizza(null);
				}
			}
		} else {
			model.setListaModalitaPagamentoVisualizza(model.getListaModalitaPagamento());
			model.setModpagamentoSelezionata(null);
			model.setRadioModPagSelezionato(0);
		}
		
		return MODPAGAMENTO;
	}
	
	protected String resede() {
		String cod ="";
		if(0!=model.getRadioModPagSelezionato()){
			cod = String.valueOf(model.getRadioModPagSelezionato());
		}
		
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento =  model.getListaModalitaPagamento();
		ModalitaPagamentoSoggetto modpagamentoSelezionato = null; 
		for(int j=0; j<listaModalitaPagamento.size(); j++){
			if(Integer.parseInt(cod) == listaModalitaPagamento.get(j).getUid()){
				modpagamentoSelezionato = listaModalitaPagamento.get(j);
				model.setModpagamentoSelezionata(listaModalitaPagamento.get(j));
			}
		}
		List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarie();
		if(listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()){
		boolean corrispondenzaSede = false;
		
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(modpagamentoSelezionato.getAssociatoA().equals(listaSediSecondarie.get(i).getDenominazione())){
					if(listaSediSecondarie.get(i).getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_VALIDO) ){
						model.setRadioSediSecondarieSoggettoSelezionato(listaSediSecondarie.get(i).getUid());
						//PROVA
						model.setSedeSelezionata(listaSediSecondarie.get(i));
						corrispondenzaSede = true;
						break;
					}
				}
			}
		
			if(!corrispondenzaSede){
				model.setRadioSediSecondarieSoggettoSelezionato(0);
			}
		}
		return SEDISECONDARIE;
	}
	
	public boolean isIdSedeCreditoreChecked() {
		return idSedeCreditoreChecked;
	}

	public void setIdSedeCreditoreChecked(boolean idSedeCreditoreChecked) {
		this.idSedeCreditoreChecked = idSedeCreditoreChecked;
	}
		
	public String confermaImpegnoCarta(){
		if(model.isHasImpegnoSelezionatoPopup()==true){
			model.setNumeroImpegno(model.getnImpegno());
			model.setAnnoImpegno(model.getnAnno());
			model.setNumeroSub(model.getnSubImpegno());

			model.setnAnno(null);
			model.setnImpegno(null);


			
			//se c'e' un soggetto:
			
			if(model.getImpegnoPopup()!=null && model.getImpegnoPopup().getSoggettoCode()!=null){
				model.setSoggetto(new SoggettoImpegnoModel());
				model.getSoggetto().setDenominazione(model.getImpegnoPopup().getSoggetto().getDenominazione());
				model.getSoggetto().setCodCreditore(model.getImpegnoPopup().getSoggettoCode());
				ricercaSoggettoPerChiaveRiga();
			}
			
		}

		pulisciListeeSchedaPopup();
		return INPUT;
	}
	
	public BigDecimal sommaImportiRighe(){
		BigDecimal somma=BigDecimal.ZERO;
		
		if (model.getListaRighe()!=null && model.getListaRighe().size()>0) {
			for (PreDocumentoCarta riga : model.getListaRighe()) {
				somma=somma.add(riga.getImporto());
			}
		}
		
		return somma;
	}
	
	
	public BigDecimal sommaImportiRigheValutaEstera(){
		BigDecimal somma=BigDecimal.ZERO;
		
		if (model.getListaRighe()!=null && model.getListaRighe().size()>0) {
			for (PreDocumentoCarta riga : model.getListaRighe()) {
				somma=somma.add(riga.getImportoValutaEstera());
			}
		}
		
		return somma;
	}
		
	public String creaTitoloCarta(){
		StringBuffer titolo=new StringBuffer();
		
		titolo.append("Carta ");
		
		if (model.isAggiornamento()) {
			titolo.append(model.getNumeroCartaInAggiornamento());
			titolo.append(" - ");
		}
		
		titolo.append(model.getDescrizioneCarta());
		
		if (model.isAggiornamento()) {
			titolo.append(" (stato ");
			titolo.append(model.getStatoOperativoCarta());
			if (model.getDataStatoOperativoCarta()!=null) {
				titolo.append(" dal ");
				titolo.append(model.getDataStatoOperativoCarta());
			}
			titolo.append(")");
		}
		
		if (model.isOptionsPagamentoEstero()) {
			titolo.append(" - Valuta Estera ");
			titolo.append(model.getCodiceDivisa());
		}

		return titolo.toString();
	}

	/* **************************************************************************** */
	/*  transazione elementare														*/
	/* **************************************************************************** */

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		// MARCO Auto-generated method stub
		
	}

	@Override
	protected void setErroreCapitolo() {
		// MARCO Auto-generated method stub
		
	}


	@Override
	public boolean missioneProgrammaAttivi() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cupAttivo() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		// MARCO Auto-generated method stub
		return false;
	}

	@Override
	public String confermaPdc() {
		// MARCO Auto-generated method stub
		return null;
	}

	@Override
	public String confermaSiope() {
		// MARCO Auto-generated method stub
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		// MARCO Auto-generated method stub
		return false;
	}

	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getNumeroCartaStruts() {
		return numeroCartaStruts;
	}

	public void setNumeroCartaStruts(String numeroCartaStruts) {
		this.numeroCartaStruts = numeroCartaStruts;
	}

	public String getAnnoCartaStruts() {
		return annoCartaStruts;
	}

	public void setAnnoCartaStruts(String annoCartaStruts) {
		this.annoCartaStruts = annoCartaStruts;
	}

	public Boolean getFromInserisciStruts() {
		return fromInserisciStruts;
	}

	public void setFromInserisciStruts(Boolean fromInserisciStruts) {
		this.fromInserisciStruts = fromInserisciStruts;
	}

	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}

}
