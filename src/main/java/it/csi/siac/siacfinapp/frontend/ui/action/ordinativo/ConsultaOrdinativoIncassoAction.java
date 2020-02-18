/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.ArrayList;
import java.util.Date;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ConsultaOrdinativoIncassoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaOrdinativoIncassoAction extends WizardConsultaOrdinativoAction<ConsultaOrdinativoIncassoModel> {

	private static final long serialVersionUID = -8192912865379160597L;
	
	protected static final String TIPO_ORDINATIVO_INCASSO_I = "I";
	
	//per andare ad inserimento ripeti:
	private String numeroOrdinativoRipeti;
	private String annoOrdinativoRipeti;
	private String ripeti;
	//
	
	public String getActionKey() {
		return "consultaOrdinativoIncasso";
	}

	/**
	 * prepare della action
	 */
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Consulta ordinativo");
	}
	
	/**
	 * Evento ripeti
	 * @return
	 */
	public String ripeti() {
		//leggo numero e anno, necessari ad identificare l'ordinativo da ripetere:
		this.numeroOrdinativoRipeti = model.getOrdinativoIncasso().getNumero().toString();
		this.annoOrdinativoRipeti = model.getOrdinativoIncasso().getAnno().toString();
		//setto ripeti a si:
		this.ripeti = "si";
		//rimando alla pagina di inserimento che sara' pilotata da questi dati:
		return "gotoRipeti";
	}

	/**
	 * execute della action
	 */
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");

		//ricerco l'ordinativo da consultare:
		RicercaOrdinativoIncassoPerChiaveResponse response = getOrdinativoIncasso();
		
		if(response == null) {
			// Errori nel caricamento dei dati: esco
			return "input";
		}

		//leggo l'ordinativo dalla response del servizio di ricerca:
		OrdinativoIncasso ordinativoIncasso = response.getOrdinativoIncasso();
		
		//setto l'ordinativo nel model:
		model.setOrdinativoIncasso(ordinativoIncasso);
		
		model.setTotaleOrdinativiCollegati(ordinativoIncasso.getTotaleOrdinativiCollegati());
		
		//carico le liste dei classificatori generici:
		caricaListeFinOrdinativoIncasso();
		
		return "success";
	}
	
	/**
	 * caricamento delle liste fin relativo all'ordinativo di incasso
	 * @return
	 */
	protected boolean caricaListeFinOrdinativoIncasso() {
		
		//compongo la request per il servizio di ricerca dei classificatori:
		LeggiClassificatoriGenericiByTipoOrdinativoGest ll = new LeggiClassificatoriGenericiByTipoOrdinativoGest();
		ll.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		ll.setCodiceTipoOrdinativoGestione(TIPO_ORDINATIVO_INCASSO_I);
		ll.setDataOra(new Date());
		ll.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		ll.setRichiedente(sessionHandler.getRichiedente());
		
		//richiamo il servizio per cercare i classificatori generici:
		LeggiClassificatoriGenericiByTipoOrdinativoGestResponse respClassFin =  classificatoreFinService.leggiClassificatoriGenericiByTipoOrdinativoGest(ll);
		if(respClassFin.isFallimento() || (respClassFin.getErrori() != null && respClassFin.getErrori().size() > 0)) {
			//errori nel servizio
			return true;
		}
		
		//SETTO I RISULTATI NEL MODEL:
		
		//CLASSIFICATORI GENERICI 26:
		if (null != respClassFin.getClassificatoriGenerici26() && respClassFin.getClassificatoriGenerici26().size() > 0) {
			model.setListaClassificatoriGen26(respClassFin.getClassificatoriGenerici26());
		}else{
			model.setListaClassificatoriGen26(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 27:
		if (null != respClassFin.getClassificatoriGenerici27() && respClassFin.getClassificatoriGenerici27().size() > 0) {
			model.setListaClassificatoriGen27(respClassFin.getClassificatoriGenerici27());
		}else{
			model.setListaClassificatoriGen27(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 28:
		if (null != respClassFin.getClassificatoriGenerici28() && respClassFin.getClassificatoriGenerici28().size() > 0) {
			model.setListaClassificatoriGen28(respClassFin.getClassificatoriGenerici28());
		}else{
			model.setListaClassificatoriGen28(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 29:
		if (null != respClassFin.getClassificatoriGenerici29() && respClassFin.getClassificatoriGenerici29().size() > 0) {
			model.setListaClassificatoriGen29(respClassFin.getClassificatoriGenerici29());
		}else{
			model.setListaClassificatoriGen29(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 30:
		if (null != respClassFin.getClassificatoriGenerici30() && respClassFin.getClassificatoriGenerici30().size() > 0) {
			model.setListaClassificatoriGen30(respClassFin.getClassificatoriGenerici30());
		}else{
			model.setListaClassificatoriGen30(new ArrayList<ClassificatoreGenerico>());
		}
		

		if (null != respClassFin.getClassificatoriStipendi()&& respClassFin.getClassificatoriStipendi().size() > 0) {
			model.setListaClassificatoriStipendi(respClassFin.getClassificatoriStipendi());
		}else{
			model.setListaClassificatoriStipendi(new ArrayList<ClassificatoreStipendi>());
		}
		
		
		return false;
	}
	
	/**
	 * per il dettaglio ordinativo inc
	 * @return
	 */
	public String dettaglioQuoteOrdinativoIncassoInc(){
		 debug("dettaglioQuoteOrdinativoIncassoInc", "numero " + getNumeroOrdinativoSelezionato());
		 //verifico se ci sono ordinativi collegati:
		 if(model.getOrdinativoIncasso().getElencoOrdinativiCollegati() != null) {
			 //itero su essi:
			 for (Ordinativo ord : model.getOrdinativoIncasso().getElencoOrdinativiCollegati()) {
				//il controllo instance of e' per evitare di incappare nel raro caso di
				//ordinativi di incasso e pagamento con lo stesso numero
				 if(FinUtility.instanceofIncasso(ord) && ord.getNumero() == Integer.parseInt(getNumeroOrdinativoSelezionato())) {
					 //trovato quello selezionato
					 OrdinativoIncasso ordInc = (OrdinativoIncasso) ord;
					 model.setOrdinativoCollegatoSelezionato(ordInc);
				 }
			}
		 }
		 return "dettaglioQuoteOrdinativoIncassoInc";
	}
	
	/**
	 * per il dettaglio ordinativo pag
	 * @return
	 */
	public String dettaglioQuoteOrdinativoIncassoPag(){
		 debug("dettaglioQuoteOrdinativoIncassoPag", "numero " + getNumeroOrdinativoSelezionato());
		 //verifico se ci sono ordinativi collegati:
		 if(model.getOrdinativoIncasso().getElencoOrdinativiCollegati() != null) {
			 //itero su essi:
			 for (Ordinativo ord : model.getOrdinativoIncasso().getElencoOrdinativiCollegati()) {
				//il controllo instance of e' per evitare di incappare nel raro caso di
				//ordinativi di incasso e pagamento con lo stesso numero
				 if(FinUtility.instanceofPagamento(ord) && ord.getNumero() == Integer.parseInt(getNumeroOrdinativoSelezionato())) {
					 //trovato quello selezionato
					 OrdinativoPagamento ordPag = (OrdinativoPagamento) ord;
					 model.setOrdinativoCollegatoSelezionato(ordPag);
				 }
			}
		 }
		 return "dettaglioQuoteOrdinativoIncassoPag";
	}
	
	/**
	 * Si occupa di gestire il richiamo del servizio 
	 * di ricerca e ritornarne la response
	 * @return
	 */
	private RicercaOrdinativoIncassoPerChiaveResponse getOrdinativoIncasso() {
		
		//inizializzo la request:
		RicercaOrdinativoIncassoPerChiave request = new RicercaOrdinativoIncassoPerChiave();
	    	
		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
	    	
		//bilancio
		Bilancio bilancio = this.sessionHandler.getBilancio();
		
		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
		
		//numero ordinativo da cercare
		ordinativoIncasso.setNumero(Integer.parseInt(numero));
		
		//anno ordinativo da cercare
		ordinativoIncasso.setAnno(Integer.parseInt(anno));
		
		pRicercaOrdinativoIncassoK.setBilancio(bilancio);
		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(ordinativoIncasso);
		
		//ente
		request.setEnte(this.sessionHandler.getEnte());
		
		//richiedente
		request.setRichiedente(this.sessionHandler.getRichiedente());
		request.setDataOra(new Date());
		request.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
		
		// SIAC-6328
		request.setPaginazioneOrdinativiCollegati(new ParametriPaginazione(readPageNumberTableId("ordCollID") - 1, 5));
		
		//invocazione del servizio:
		RicercaOrdinativoIncassoPerChiaveResponse response = this.ordinativoService.ricercaOrdinativoIncassoPerChiave(request);
		
		//analisi della response del servizio:
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0))) {
			//ci sono errori
			debug(this.methodName, new Object[] { "Errore nella risposta del servizio" });
			addErrori(this.methodName, response);
			return null;
		}
		
		//tutto ok
		return response;
	}
	
	/**
	 * per abilitare /disbilitare il tasto ripeti
	 * @return
	 */
	public boolean abilitatoRipetiOrdinativoIncasso(){
		boolean abilitato = false;
		//per ora l'unica condizione riguarda l'abilitazione all'azione OP-ENT-insOrdInc
		//implementare qui altre eventuali condizioni che dovessero essere richeste sul tasto
		//ripeti dentro la maschera di consulta ordinativo di incasso
		if (isAzioneAbilitata(CodiciOperazioni.OP_ENT_insOrdInc)) {
			abilitato = true;
		}
		return abilitato;
	}

	public String getNumeroOrdinativoRipeti() {
		return numeroOrdinativoRipeti;
	}

	public void setNumeroOrdinativoRipeti(String numeroOrdinativoRipeti) {
		this.numeroOrdinativoRipeti = numeroOrdinativoRipeti;
	}

	public String getAnnoOrdinativoRipeti() {
		return annoOrdinativoRipeti;
	}

	public void setAnnoOrdinativoRipeti(String annoOrdinativoRipeti) {
		this.annoOrdinativoRipeti = annoOrdinativoRipeti;
	}

	public String getRipeti() {
		return ripeti;
	}

	public void setRipeti(String ripeti) {
		this.ripeti = ripeti;
	}
	
}
