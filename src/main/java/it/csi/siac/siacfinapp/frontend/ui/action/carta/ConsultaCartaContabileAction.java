/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.ConsultaCartaContabileModel;
import it.csi.siac.siacfinser.frontend.webservice.CartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;
import xyz.timedrain.arianna.plugin.BreadCrumb;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCartaContabileAction extends GenericFinAction<ConsultaCartaContabileModel> {

	private static final long serialVersionUID = -8192912865379160597L;
	
	@Autowired
	protected transient CartaContabileService cartaContabileService;
	
	private String methodName;
	
	//anno e numero carta contabile:
	private String anno;
	private String numero;
	
	//da inserisci:
	private Boolean fromInserisci;
	
	//tab folder:
	protected String tabFolder;

	public String getActionKey() {
		//ritorno il nome della action key
		return "consultaCartaContabile";
	}

	public void prepare() throws Exception {
		setMethodName("prepare");
		//richiamo il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Consulta carta contabile");
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");

		// chiamata a servizio per la ricerca carta per chiave
		RicercaCartaContabilePerChiaveResponse response = getCartaContabile();

		//leggo la carta contabile dalla response del servizio:
		CartaContabile cartaContabile = response.getCartaContabile();
		
		//setto la carta contabile nel model:
		model.setCartaContabile(cartaContabile);
		
		//settiamo le firme impostate per l'ente:
		model.setIntestazioneFirma1(getTipologiaGestioneLivelli(TipologiaGestioneLivelli.FIRMA_CARTA_1));
		model.setIntestazioneFirma2(getTipologiaGestioneLivelli(TipologiaGestioneLivelli.FIRMA_CARTA_2));
		//

		return Boolean.TRUE.equals(fromInserisci) ? "fromInserisci" : "success";
	}

	/**
	 * Gestisce il caricamento tramite servizio
	 * @return
	 */
	private RicercaCartaContabilePerChiaveResponse getCartaContabile() {
		
		//istanzio la request per il servizio:
		RicercaCartaContabilePerChiave request = new RicercaCartaContabilePerChiave();
	    	
		//compongo i dat della request:
		RicercaCartaContabileK pRicercaCartaContabileK = new RicercaCartaContabileK();
	    	
		Bilancio bilancio = this.sessionHandler.getBilancio();
		
		CartaContabile cartaContabile = new CartaContabile();
		cartaContabile.setNumero(Integer.parseInt(numero));
		
		pRicercaCartaContabileK.setBilancio(bilancio);
		pRicercaCartaContabileK.setCartaContabile(cartaContabile);
		
		
		request.setEnte(this.sessionHandler.getEnte());
		request.setRichiedente(this.sessionHandler.getRichiedente());
		request.setDataOra(new Date());
		request.setpRicercaCartaContabileK(pRicercaCartaContabileK);
		request.setCercaMdpCessionePerChiaveModPag(! Boolean.TRUE.equals(fromInserisci));
		
		//invoco il servizio:
		RicercaCartaContabilePerChiaveResponse response = this.cartaContabileService.ricercaCartaContabilePerChiave(request);
		
		//analizzo l'esito:
		
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0))) {
			debug(this.methodName, new Object[] { "Errore nella risposta del servizio" });
			addErrori(this.methodName, response);
			return null;
		}
		
		//ritorno la response ok:
		return response;
	}
	
	/**
	 * per gestire il cambio tab
	 */
	public void cambiaTabFolder(){
		 
		if("tabCartaContabile".equals(tabFolder)){
			//setto il tab carta contabile
			model.setActiveTab("cartaContabile");
		}
		
		if("tabRighe".equals(tabFolder)){
			//setto il tab righe
			model.setActiveTab("righe");
		}
		
	}

	// GETTER E SETTE:

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getAnno() {
	    return anno;
	}

	public void setAnno(String anno) {
	    this.anno = anno;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Boolean getFromInserisci() {
		return fromInserisci;
	}

	public void setFromInserisci(Boolean fromInserisci) {
		this.fromInserisci = fromInserisci;
	}

	public String getTabFolder() {
		return tabFolder;
	}

	public void setTabFolder(String tabFolder) {
		this.tabFolder = tabFolder;
	}
	
}
