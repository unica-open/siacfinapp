/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoOrdinativoPagamentoAction extends ActionKeyRicercaOrdinativoPagamentoAction {

	private static final long serialVersionUID = 1L;

	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco ordinativi pagamento");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		// se la ricerca torna degli errori ritorna
		// degli errori torno sulla form di ricerca 
		if(executeRicercaOrdinativoPagamento().equals(INPUT)){
					
			return "gotoErroreRicerca";
		}
		
	    return SUCCESS;
	}
	/**
	 * verifica abilitazione tasto annulla su elenco jsp 
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAnnulla(String stato){
		boolean returnValue = false;
		
		if(isFaseBilancioAbilitata()){
			returnValue=true;
		}
		
		if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGMAN)){
			returnValue=false;
		}
		
		if (stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO) || stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_QUIETANZATO)) {
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
		boolean isAggiornaAbilitato = true;
		
		if(!isFaseBilancioAbilitata()){
			return false;
		}
		
		if (stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_ANNULLATO)) {
			return false;
		}
		
		
		boolean isAggiornaAbilitatoOPSPEAggMan = isAzioneAbilitata(CodiciOperazioni.OP_SPE_aggMan);
		boolean isAggiornaAbilitatoOPSPEVarMan = isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan);
				
		if (stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_INSERITO) && !isAggiornaAbilitatoOPSPEAggMan) {
			return false;
		}
		
		if (stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_TRASMESSO) && !isAggiornaAbilitatoOPSPEVarMan) {
			return false;
		}
		
		if ((stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_QUIETANZATO)|| stato.equalsIgnoreCase(Constanti.D_ORDINATIVO_STATO_FIRMATO))  && !isAggiornaAbilitatoOPSPEVarMan) {
			return false;
		}
		
		return isAggiornaAbilitato;
	}
}