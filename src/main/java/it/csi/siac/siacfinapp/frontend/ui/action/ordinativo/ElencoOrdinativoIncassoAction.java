/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinser.CostantiFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoOrdinativoIncassoAction extends WizardRicercaOrdinativoAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco ordinativi incasso");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		// se la ricerca torna degli errori ritorna
		// degli errori torno sulla form di ricerca 
		if(executeRicercaOrdinativoIncasso().equals(INPUT)){
					
			return "gotoErroreRicerca";
		}
		
	    return SUCCESS;
	}
	/**
	 * verifica abilitazione tasto annulla ordinativo su elenco jsp
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAnnulla(String stato){
		boolean returnValue = true;
		
		if(!isFaseBilancioAbilitata()){
			returnValue=false;
		}
		
		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_AGGORDINC)){
			returnValue=false;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)) {
			returnValue=false;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_TRASMESSO) || stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_FIRMATO)) {
		}else if(!stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_INSERITO)) {
			returnValue=false;
		}
		
		return returnValue;
	}
	/**
	 * verifica abilitazione tasto aggiorna su elenco jsp
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAggiorna(String stato){
		boolean returnValue = true;
		
		if(!isFaseBilancioAbilitata()){
			returnValue=false;
		}

		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_AGGORDINC)){
			returnValue=false;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)) {
			returnValue=false;
		}
		
		if (stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_TRASMESSO) || 
				stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_FIRMATO) || 
				stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_QUIETANZATO)) {
			if (!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_VARORDINC)) {
				returnValue=false;
			}
		}else if(!stato.equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_INSERITO)) {
			returnValue=false;
		}
		
		return returnValue;
	}
}