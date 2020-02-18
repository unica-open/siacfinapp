/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaEstera;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneCartaStep1Action extends ActionKeyGestioneCartaAction {

	private static final long serialVersionUID = 1L;

	//NUMERO E ANNO CARTA:
	private String numeroCarta;
	private String annoCarta;

	
	public GestioneCartaStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}

	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();

		//setto il titolo:
		this.model.setTitolo("Gestione carta contabile");
		
		//titolo dello step:
		this.model.setTitoloStep("Gestione carta contabile");

		//carico la lista tipi provv:
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}

		//carico le liste generiche:
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.VALUTA, TipiLista.COMMISSIONI_ESTERO);
		model.setListaValuta(((List<CodificaFin>)mappaListe.get(TipiLista.VALUTA)));
		model.setListaCommissioniEstero(((List<CodificaFin>)mappaListe.get(TipiLista.COMMISSIONI_ESTERO)));
		
		if(!model.isOptionsPagamentoEstero()){
			model.setOptionsPagamentoEstero(false);
		}
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//settiamo le intestazioni firme impostate per l'ente:
		model.setIntestazioneFirma1(getCodiceLivelloByTipo(TipologiaGestioneLivelli.FIRMA_CARTA_1));
		model.setIntestazioneFirma2(getCodiceLivelloByTipo(TipologiaGestioneLivelli.FIRMA_CARTA_2));
		//
		
		//controlliamo lo stato del bilancio:
		controlloStatoBilancio(Integer.parseInt(sessionHandler.getAnnoEsercizio()), "GESTIONE", "CARTA CONTABILE");

		if(!model.isAggiornamento()){
			//non siamo nello scenario di aggiornamento
			if(getAnnoCarta()!= null && getNumeroCarta()!=null){
				model.setAnnoCartaInAggiornamento(getAnnoCarta());
				model.setNumeroCartaInAggiornamento(getNumeroCarta());
				model.setAggiornamento(true);
			}else{
				model.setAggiornamento(false);
			}
		}
		
		if (!model.isAggiornamento()) {
			if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_INSCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		} else {
			if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		}
		
		if (model.isAggiornamento() && forceReload) {
			String esitoRicerca = caricaDatiCartaContabile();
			this.model.setTitoloStep(creaTitoloCarta());

			return esitoRicerca;
		}
		
		if(!model.isAggiornamento()){
			//default per inserimento:
			if(isEmpty(model.getDataScadenzaCarta())){
				model.setDataScadenzaCarta(DateUtility.getCurrentDate());
			}
		}
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////

		
		return SUCCESS;
	}
	
	private void cancellaCampiStep1() {
		//Provvedimento
		model.setProvvedimento(new ProvvedimentoImpegnoModel());
		model.setProvvedimentoSelezionato(false);
		
		//Info carta
		model.setListaValuta(new ArrayList<CodificaFin>());
		model.setListaCommissioniEstero(new ArrayList<CodificaFin>());
		model.setOptionsPagamentoEstero(false);
		model.setDescrizioneCarta(null);
		model.setCausaleCarta(null);
		model.setMotivoUrgenzaCarta(null);
		model.setNoteCarta(null);
		model.setDataValutaCarta(null);
		model.setDataScadenzaCarta(null);
		
		//Valuta Estera
		model.setCodiceDivisa(null);
		model.setCausalePagamentoEstero(null);
		model.setSpeseECommissioni(null);
		model.setModalitaPagamento(null);
		model.setIstrPartPagamentoEstero(null);
		model.setCheckEsecutoreTitolare(false);
		
		model.setListaRighe(new ArrayList<PreDocumentoCarta>());
	}
	
	private String caricaDatiCartaContabile() {
		String esito=SUCCESS;
		
		//istanzio la request per il servizio di ricerca:
		RicercaCartaContabilePerChiave request = new RicercaCartaContabilePerChiave();
    	
		RicercaCartaContabileK ricercaCartaContabileK = new RicercaCartaContabileK();
	    	
		CartaContabile cartaContabile = new CartaContabile();
		
		//numero carta
		cartaContabile.setNumero(Integer.parseInt(getNumeroCarta()));
		
		//bilancio
		ricercaCartaContabileK.setBilancio(this.sessionHandler.getBilancio());
		
		//setto la carta contabile nella request:
		ricercaCartaContabileK.setCartaContabile(cartaContabile);
		
		//ente
		request.setEnte(this.sessionHandler.getEnte());
		//richiedente
		request.setRichiedente(this.sessionHandler.getRichiedente());
		request.setDataOra(new Date());
		request.setpRicercaCartaContabileK(ricercaCartaContabileK);
		request.setCercaMdpCessionePerChiaveModPag(true);
		 
		
		//invoco il servizio:
		RicercaCartaContabilePerChiaveResponse response = this. cartaContabileService.ricercaCartaContabilePerChiave(request);
		
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0))) {
			//presenza errori
			addErrori(response);
			return INPUT;
		}
		
		CartaContabile cartaContabileTrovata=response.getCartaContabile();
		
		//Dati intestazione carta
		//Provvedimento
		if (cartaContabileTrovata.getAttoAmministrativo()!=null) {
			impostaProvvNelModel(cartaContabileTrovata.getAttoAmministrativo(),model.getProvvedimento());
			// SIAC-6083: aggiungo l'informazione che il provvedimento e' selezionato
			model.setProvvedimentoSelezionato(true);
		}
		
		//Informazioni
		model.setNumeroRegistrazione(cartaContabileTrovata.getNumRegistrazione());
		model.setDescrizioneCarta(cartaContabileTrovata.getOggetto());
		model.setMotivoUrgenzaCarta(cartaContabileTrovata.getMotivoUrgenza());
		model.setCausaleCarta(cartaContabileTrovata.getCausale());
		model.setNoteCarta(cartaContabileTrovata.getNote());
		model.setDataValutaCarta(DateUtility.convertiDataInGgMmYyyy(cartaContabileTrovata.getDataEsecuzionePagamento()));
		model.setDataScadenzaCarta(DateUtility.convertiDataInGgMmYyyy(cartaContabileTrovata.getDataScadenza()));
		model.setStatoOperativoCarta(cartaContabileTrovata.getStatoOperativoCartaContabile().toString());
		model.setDataStatoOperativoCarta(DateUtility.convertiDataInGgMmYyyy(cartaContabileTrovata.getDataStatoOperativo()));
		model.setFirma1(cartaContabileTrovata.getFirma1());
		model.setFirma2(cartaContabileTrovata.getFirma2());

		//Valuta Estera
		if (cartaContabileTrovata.getListaCarteEstere()!=null && cartaContabileTrovata.getListaCarteEstere().size()==1) {
			CartaEstera cartaEstera=cartaContabileTrovata.getListaCarteEstere().get(0);
			model.setOptionsPagamentoEstero(true);
			model.setCodiceDivisa(cartaEstera.getValuta().getCodice());
			model.setCodiceDivisaCaricato(cartaEstera.getValuta().getCodice());
			model.setCausalePagamentoEstero(cartaEstera.getCausalePagamento());
			model.setSpeseECommissioni(cartaEstera.getCommissioniEstero().getCodice());
			model.setModalitaPagamento(cartaEstera.getTipologiaPagamento());
			model.setIstrPartPagamentoEstero(cartaEstera.getIstruzioni());
			model.setCheckEsecutoreTitolare(FinStringUtils.stringToBooleanForDb(cartaEstera.getDiversoTitolare()));
		}
		
		//Dati righe
		model.setListaRighe(cartaContabileTrovata.getListaPreDocumentiCarta());
		
		//Imposto la modalita' di pagamento nei campi utili al front-end
		for(PreDocumentoCarta riga: cartaContabileTrovata.getListaPreDocumentiCarta()){
			if(riga.getSoggetto().getModalitaPagamentoList()!=null && riga.getSoggetto().getModalitaPagamentoList().size()>0){
				//caricamento mdp da soggetto
				riga.getSoggetto().setElencoModalitaPagamento(riga.getSoggetto().getModalitaPagamentoList());		
			} else {				
				//caricamento mdp da sede
				if(riga.getSedeSecondariaSoggetto()!=null && riga.getSedeSecondariaSoggetto().getModalitaPagamentoSoggettos()!=null && riga.getSedeSecondariaSoggetto().getModalitaPagamentoSoggettos().size()>0){
					riga.getSoggetto().setElencoModalitaPagamento(riga.getSedeSecondariaSoggetto().getModalitaPagamentoSoggettos());					
				}
			}
			riga.setDataEsecuzioneRiga(riga.getDataDocumento());
		}
		
		return esito;
	}

	public boolean disableStep1() {
		boolean disabled=false;

		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				disabled=true;
			}
		}

		return disabled;

	}
	
	/**
	 * Il campo numero registrazione deve essere disabilitato in caso di aggiornamento se lo stato e' TRASMESSO
	 * @return
	 */
	public boolean disableCampoNumeroRegistrazione() {
		boolean disabled=false;
		if (model.isAggiornamento() && model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString()) ){
				disabled=true;
		}
		return disabled;

	}

	public boolean disableProvvedimento() {
		boolean disable=false;

		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					!model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				disable=true;
			} else if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
				disable=true;
			}
		}

		return disable;
	}

	
	public String prosegui(){
		List<Errore> listaErrori= new ArrayList<Errore>();

		if (model.isAggiornamento()) {
			if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		} else {
			if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_INSCARTA)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}
		
		if(controlloStatoBilancio(Integer.parseInt(sessionHandler.getAnnoEsercizio()),"Gestione","Carta Contabile")){
			return INPUT;
		}

		listaErrori=controlloCampiTestataCarta();
		
		//controllo esistenza provvedimento valido
		boolean erroriProvvedimento = controlloServizioProvvedimento(model.getProvvedimento(), model.isProvvedimentoSelezionato());
				

		if(!listaErrori.isEmpty() || erroriProvvedimento){
			//presenza errori
			addErrori(listaErrori);
			return INPUT;
		}

		return "prosegui";
	}
	
	public String annulla() {
		if (!model.isAggiornamento()) {
			cancellaCampiStep1();
		} else {
			setNumeroCarta(model.getNumeroCartaInAggiornamento());
			setAnnoCarta(model.getAnnoCartaInAggiornamento());
			
			String esitoRicerca = caricaDatiCartaContabile();
			this.model.setTitoloStep(creaTitoloCarta());

			return esitoRicerca;
		}

		return INPUT;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getNumeroCarta() {
		return numeroCarta;
	}

	public void setNumeroCarta(String numeroCarta) {
		this.numeroCarta = numeroCarta;
	}

	public String getAnnoCarta() {
		return annoCarta;
	}

	public void setAnnoCarta(String annoCarta) {
		this.annoCarta = annoCarta;
	}
}
