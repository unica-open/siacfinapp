/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.DettaglioDocumentoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.EntitaUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.SoggettoSedeModPagInfo;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ReintroitoOrdinativoPagamentoStep1Action extends ActionKeyReintroitoOrdinativoPagamentoAction{

	private static final long serialVersionUID = 1L;
	
	public ReintroitoOrdinativoPagamentoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
	}

	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		
		if(model == null || !model.isDatiInizializzatiPrepareStep1()){
			//invoco il prepare della super classe:
			super.prepare();
		}
		
		//setto il titolo:
		this.model.setTitolo("Gestione Reintroiti - Scelta ordinativo");
		if(!model.isDatiInizializzatiPrepareStep1()){
			
			//carico le liste ricerca soggetto:
			caricaListePerRicercaSoggetto();
			
			//lista tipi provvedimento:
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			//transazione elementare:
			if(teSupport==null){
				pulisciTransazioneElementare();
			}
			
			//Scelta Provvedimento da usare:
		    model.getReintroitoOrdinativoStep1Model().setScelteProvvedimentoDaUsare(buildListaScelteProvvedimentoDaUsare());
		    model.getReintroitoOrdinativoStep1Model().setProvvedimentoDaUsare(WebAppConstants.SCELTA_PROVVEDIMENTO_UNICO);
		    
		    //setto l'anno in sola lettura:
		    model.getReintroitoOrdinativoStep1Model().setAnnoOrdinativoPagamento(new Integer(sessionHandler.getAnnoEsercizio()));
			//
		    
		    //SETTO I DATI COME INIZIALIZZATI:
		    model.setDatiInizializzatiPrepareStep1(true);
		    //
			
		}
	    
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		
		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_REINTROITO_ORD_PAG)){
			//non e' abilitato
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
		}
			
		//transazione elementare:
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		
		//setto il tipo di oggetto trattato:
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO.toString());
		
		
		//liste bil solo se non ancora inizializzato:
		if(!model.isDatiInizializzatiExecuteStep1()){
			//CARICO LE LISTE BIL
			if (caricaListeBil(WebAppConstants.CAP_UG)) {
				return INPUT;
			}
		}
		
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		
		//SETTO CHE I DATI SONO STATI INIZIALIZZATI:
		model.setDatiInizializzatiExecuteStep1(true);
		//
		
		return SUCCESS;
	}	
	
	/**
	 * Controlli da effettuare alla pressione del tasto prosegui 
	 * verso lo step 2
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean controlliPerProsegui(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//controllo che anno e numero siano presenti:
		
		if (!isOrdinativoPresente()){
			//ordinativo non indicato
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Indicare un ordinativo di pagamento"));
		}
		
		if (provvedimentoUnico() && !model.getReintroitoOrdinativoStep1Model().isProvvedimentoSelezionato()){
			//in caso di provvedimento unico il provvedimento va ricercato
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento non indicato"));
		}
			
		return checkAndAddErrors(listaErrori);
	}
	
	/**
	 * Controlla se i campi del provvedimento sono compilati avendo selezionato provvedimento unico
	 * @return
	 */
	private boolean compilatiCampiProvvedimentoPerProvvedientoUnico(){
		List<Errore> listaErrori= new ArrayList<Errore>();
		if (provvedimentoUnico()){
			ProvvedimentoImpegnoModel provvedimento = model.getReintroitoOrdinativoStep1Model().getProvvedimento();
			if(provvedimento!=null){
				
				if(FinUtility.isNullOrZero(provvedimento.getAnnoProvvedimento())){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno provvedimento"));
				}
				
				if(FinUtility.isNullOrZero(provvedimento.getIdTipoProvvedimento())){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo provvedimento"));
				}
				
				if(FinUtility.isNullOrZero(provvedimento.getNumeroProvvedimento())){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero provvedimento"));
				}
				
			}
		}
		return checkAndAddErrors(listaErrori);
	}
	
	/**
	 * Controlli formali sui dati immessi prima di invocare la ricerca
	 * dell'ordinativo di pagamento.
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean controlliFormaliPerCercaOrdinativoPagamento(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//controllo che anno e numero siano presenti:
		
		if (model.getReintroitoOrdinativoStep1Model().getAnnoOrdinativoPagamento()== null){
			//numero omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Ordinativo"));
		}
		
		if (model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento()== null){
			//numero omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Ordinativo"));
		}
			
		return checkAndAddErrors(listaErrori);
	}
	
	/**
	 * Controlli di merito sull'ordinativo appena ottenuto dalla ricerca.
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * @param ordinativoTrovato
	 * @return
	 */
	private boolean controlliDiMeritoOrdinativoPagamentoCercato(OrdinativoPagamento ordinativoTrovato){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//1. esistenza ordinativo
		if(ordinativoTrovato==null || ordinativoTrovato.getNumero()==null){
			//l'ordinativo non esiste
			listaErrori.add(ErroreFin.ORDINATIVO_INESISTENTE.getErrore());
		}
		
		//L'ordinativo deve essere in stato Q
		StatoOperativoOrdinativo stato = ordinativoTrovato.getStatoOperativoOrdinativo();
		if(!StatoOperativoOrdinativo.QUIETANZATO.equals(stato)){
			//errore non e' quietanziato
			listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Ordinativo","diverso da quietanzato"));
		}
		
		//e non deve essere gia' stato reintroitato quindi non deve avere la DATA SPOSTAMENTO
		if(ordinativoTrovato.getDataSpostamento()!=null){
			//errore non e' quietanziato
			String msg = "reintroitato in data: " + FinUtility.formatDataDdMmYy(ordinativoTrovato.getDataSpostamento());
			listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Ordinativo",msg));
		}
			
		return checkAndAddErrors(listaErrori);
	}
	
	/**
	 * Compone la requeste per ricerca l'ordinativo di pagamento col numero indicato
	 * @return
	 */
	private RicercaOrdinativoPagamentoPerChiave componiRequestPerRicercaOrdinativoPagamento(){
		RicercaOrdinativoPagamentoPerChiave req = new RicercaOrdinativoPagamentoPerChiave();
		
		req.setDataOra(new Date());
		req.setEnte(sessionHandler.getAccount().getEnte());
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		pRicercaOrdinativoPagamentoK.setBilancio(sessionHandler.getBilancio());
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setNumero(model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento().intValue());
		
		//ma l'anno si puo indicare
		ordinativoPagamento.setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		//ordinativoPagamento.setAnno(model.getReintroitoOrdinativoStep1Model().getAnnoOrdinativoPagamento().intValue());
		
		ordinativoPagamento.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		pRicercaOrdinativoPagamentoK.setBilancio(sessionHandler.getBilancio());
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		req.setRichiedente(sessionHandler.getRichiedente());
		
		return req;
	}
	
	/**
	 * 
	 * Esegue la ricerca dell'ordinativo indicato.
	 * 
	 * Aggiunge gli eventuali errori in sessione e ritorna:
	 * 
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean cercaOrdinativoPagamentoRoutine(){
		
		//1. CONTROLLI FORMALI:
		if(!controlliFormaliPerCercaOrdinativoPagamento()){
			return false;
		}
		
		//2. COMPONGO LA REQUEST:
		RicercaOrdinativoPagamentoPerChiave req = componiRequestPerRicercaOrdinativoPagamento();
		
		//3. RICHIAMO IL SERVIZIO:
		RicercaOrdinativoPagamentoPerChiaveResponse response = ordinativoService.ricercaOrdinativoPagamentoPerChiave(req);
			
		//4. ANALIZZO LA RISPOSTA DEL SERVIZIO:
		if(isFallimento(response)){
			//ci sono errori:
			addErrori(response);
			return false;
		}
		
		//5. OK LA CHIAMATA E' ANDATA BENE, PROCEDIAMO CON ULTARIORI CONTROLLI:
		OrdinativoPagamento ordinativoTrovato = response.getOrdinativoPagamento();
		if(!controlliDiMeritoOrdinativoPagamentoCercato(ordinativoTrovato)){
			//l'ordinativo non va bene
			return false;
		}
		
		//6. L'ORDINATIVO E' ACCETTABILE (altrimenti saremmo gia' usciti con false)
		//AGGIUNGIAMOLO AL MODEL:
		impostaOrdinativoAppenaCaricatoNelModel(ordinativoTrovato);
		
		//7. ABILITIAMO IL PULSANTE ESEGUI ALLO STEP2:
		model.setElaborazioneAvviata(false);
		//
		
		return true;
	}
	
	/**
	 * metodo che gestisce il pulsante CERCA relativamente al numero
	 * di ordinativo di pagamento indicato
	 * @return
	 */
	public String cercaOrdinativoPagamento(){
		
		//CHIAMO IL METODO CHE GESTISTE LA RICERCA E GLI EVENTUALI ERRORI:
		if(!cercaOrdinativoPagamentoRoutine()){
			return INPUT;
		}
		//
		
		return SUCCESS;
	}
	
	/**
	 * Setta nel model i dati dell'ordinativo appena individuato tramite il pulsante cerca
	 * @param ordinativoTrovato
	 */
	private void impostaOrdinativoAppenaCaricatoNelModel(OrdinativoPagamento ordinativoTrovato){
		
		//setto l'oggetto principale:
		model.getReintroitoOrdinativoStep1Model().setOrdinativoDaReintroitare(ordinativoTrovato);
		 
		//calcolo l'importo netto:
		BigDecimal importoNetto = calcolaImportoNettoPerReintroito(ordinativoTrovato);
		model.getReintroitoOrdinativoStep1Model().setImportoNetto(importoNetto);
		 
		 
		//TRAVASIAMO I DATI DALL'OGGETTO ATTO AMMINISTRATIVO AL MODEL DELLA PAGINA
		impostaProvvNelModel(ordinativoTrovato.getAttoAmministrativo(), model.getReintroitoOrdinativoStep1Model().getProvvedimentoOrdinativoDaReintroitare());
		//
		
		//setto i dati del soggetto:
		Soggetto soggettoOrdinativo = ordinativoTrovato.getSoggetto();
		caricaDatiSoggettoOrdinativo(model.getReintroitoOrdinativoStep1Model().getSoggettoOrdinativoDaReintroitare(),soggettoOrdinativo, null);
		//
		
		//Dati del documento:
		List<DettaglioDocumentoModel> documentiCollegati = new ArrayList<DettaglioDocumentoModel>();
		BigDecimal sommaDoc = null;
		List<DocumentoSpesa> docs = FinUtility.distintiDocumentiDellOrdinativo(ordinativoTrovato);
		if(!isEmpty(docs)){
			sommaDoc = BigDecimal.ZERO;
			for(DocumentoSpesa docIt: docs){
				documentiCollegati.add(FinUtility.documentoSpesaToDettaglioModel(docIt));
				sommaDoc = sommaDoc.add(docIt.getImporto());
			}
		}
		model.getReintroitoOrdinativoStep1Model().setDocumentiCollegati(documentiCollegati);
		model.getReintroitoOrdinativoStep1Model().setImportoDocumentiCollegati(sommaDoc);
		
		//Modalita pagamento:
		SoggettoSedeModPagInfo modPag = EntitaUtils.modalitaPagamentoOrdPag(ordinativoTrovato);
		if(modPag!=null && modPag.getModalitaPagamento()!=null){
			String descrizioneArricchita = modPag.getModalitaPagamento().getDescrizioneInfo().getDescrizioneArricchita();
			model.getReintroitoOrdinativoStep1Model().setDescrArrModPag(descrizioneArricchita);
		} else {
			model.getReintroitoOrdinativoStep1Model().setDescrArrModPag(null);
		}
		//
		
		//setto i dati del capitolo:
		caricaDatiCapitolo(ordinativoTrovato.getCapitoloUscitaGestione(), null, model.getReintroitoOrdinativoStep1Model().getCapitoloOrdinativoDaReintroitare());
		//
		
		//RIGENERO la tabella nello step2:
		popolaTabellaDeiReintroiti();
	}
	
	/**
	 * Metodo di comodo per verificare il caso in cui e' stato indicato
	 *  il numero dell'ordinativo che si intende reintroitare
	 *  ma non e' stato caricato 
	 *  (potrebbe anche non essere stato caricato perche' non andato a buon fine come ricerca)
	 * @return
	 */
	private boolean ordinativoIndicatoMaNonCaricato(){
		
		if(isOrdinativoPresente()) {
			return model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento()!=null
					&& model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento().intValue()>0
					&& model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento().compareTo(BigInteger.valueOf(model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare().getNumero().longValue())) != 0;
		}
		
		return model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento()!=null
				&& model.getReintroitoOrdinativoStep1Model().getNumeroOrdinativoPagamento().intValue()>0;
	}
	
	/**
	 * Metodo che gestisce il tasto prosegui
	 * verso lo step2
	 * @return
	 */
	public String prosegui(){
		
		//setto a false i flag pop up per evitare che si apra in pagina 
		//da passaggi precedenti:
		pulisciChecksWarningPopUp();
		
		//1. Se l'ordinativo non e' stato cercato ma 
		//solo indicato come numero effettuo la ricerca:
		if(ordinativoIndicatoMaNonCaricato()){
			//cerchiamo l'ordinativo:
			if(!cercaOrdinativoPagamentoRoutine()){
				return INPUT;
			}
		}
		//END 1 --> ok se non siamo usciti per errori l'ordinativo e' stato caricato
		
		//2. Se il provv non e' carico provvediamo a caricarlo:
		if (provvedimentoUnico() && !model.getReintroitoOrdinativoStep1Model().isProvvedimentoSelezionato()){
			
			//verifichiamo che i campi siano stati compilati:
			if(!compilatiCampiProvvedimentoPerProvvedientoUnico()){
				return INPUT;
			}
			
			//avendo i campi compilati posso cercare il provvedimento:
			boolean erroriProvvedimento = controlloServizioProvvedimento(model.getReintroitoOrdinativoStep1Model().getProvvedimento(), model.getReintroitoOrdinativoStep1Model().isProvvedimentoSelezionato());
			if(erroriProvvedimento){
				//provvedimento non trovato o non accettabile
				return INPUT;
			}
		}
		
		//END 2.
		
		
		//3. CONTROLLI PROSEGUI:
		if(!controlliPerProsegui()){
			//ci sono errori
			return INPUT;
		}
		
		//ok vado a step2
		return "prosegui";
	}
	
	/**
	 * Pulisce i dati immessi tramite il tasto annulla
	 * @return
	 */
	public String annullaStep1(){
		super.annullaModelStep1();
		super.annullaModelStep2();
		return SUCCESS;
	}
	
}