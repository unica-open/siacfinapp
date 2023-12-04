/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;
import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.CostantiFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoLiquidazioniAction extends WizardRicercaLiquidazioneAction {

	private static final long serialVersionUID = 1L;
	
	//costanti per le azioni:
	protected final static int CONSULTA = 1;
	protected final static int AGGIORNA = 2;
	protected final static int ANNULLA = 3;
	protected final static int INSERISCE = 4;
	
	//uid liquidazione:
	private String uidLiquidazione;
	
	//numero e anno:
	private String numeroLiquidazione;
	private String numeroLiquidazioneDaPassare;
	private String annoLiquidazioneDaPassare;
	private String numeroLiquidazioneDaAnnullare;
	
	//ricarica pagina:
	public boolean ricaricaPagina;
	
	//status:
	public boolean status;
	
	@Override
	public void prepare() throws Exception {
		//richiamo il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco Liquidazioni");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// se la ricerca torna degli errori
		// torno sulla form di ricerca 
		if(executeRicercaLiquidazioni().equals(INPUT)){
			return "gotoErroreRicerca";
		}
		//esito ok:
	    return SUCCESS;
	}
	
	public String inserisciLiquidazione(){		
		return "gotoInserisciLiquidazioni";		
	}
	
	/**
	 * abilitazione all'operazione annulla
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAnnulla(String stato){
		boolean returnValue = false;
		
		if(isFaseBilancioAbilitata()){
			//se fase bilancio abilitata allora e' abilitato
			returnValue=true;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)) {
			//se e' gia annullato non e' abilitato
			returnValue=false;
		}
		
		return returnValue;
	}
	
	/**
	 * abilitazione all'operazione aggiorna
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAggiorna(String stato){
		boolean returnValue = false;
		
		if(isFaseBilancioAbilitata()){
			//se fase bilancio abilitata allora e' abilitato
			returnValue=true;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)) {
			//se e' annullato non e' aggiornabile:
			returnValue=false;
		}
		
		return returnValue;

	}
	
	/**
	 * controlla se si riferisce ad un sub
	 * @param numero
	 * @return
	 */
	public boolean isSubImpegno(String numero){
		boolean returnValue = true;
		if(StringUtils.isEmpty(numero)) {
			//banalmente se il numero e' vuoto non e' un sub
			returnValue=false;
		}
		return returnValue;

	}

	// Getter e setter:
	
	public String getUidLiquidazione() {
		return uidLiquidazione;
	}

	public void setUidLiquidazione(String uidLiquidazione) {
		this.uidLiquidazione = uidLiquidazione;
	}

	public String getNumeroLiquidazione() {
		return numeroLiquidazione;
	}

	public void setNumeroLiquidazione(String numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}

	public String getNumeroLiquidazioneDaPassare() {
		return numeroLiquidazioneDaPassare;
	}

	public void setNumeroLiquidazioneDaPassare(String numeroLiquidazioneDaPassare) {
		this.numeroLiquidazioneDaPassare = numeroLiquidazioneDaPassare;
	}

	public String getAnnoLiquidazioneDaPassare() {
		return annoLiquidazioneDaPassare;
	}

	public void setAnnoLiquidazioneDaPassare(String annoLiquidazioneDaPassare) {
		this.annoLiquidazioneDaPassare = annoLiquidazioneDaPassare;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getNumeroLiquidazioneDaAnnullare() {
		return numeroLiquidazioneDaAnnullare;
	}

	public void setNumeroLiquidazioneDaAnnullare(String numeroLiquidazioneDaAnnullare) {
		this.numeroLiquidazioneDaAnnullare = numeroLiquidazioneDaAnnullare;
	}
	
}
