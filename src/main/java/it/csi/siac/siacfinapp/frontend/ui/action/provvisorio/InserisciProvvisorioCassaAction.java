/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciProvvisorioCassaAction extends WizardInserisciProvvisorioAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Inserisci provvisorio di cassa");
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//Pulisco i campi 
	   	pulisciCampi();
	   	impostaDatiIniziali();
	   	
	   	return SUCCESS;
	}
	
	public String insericiProvvisorioDiCassa(){
		List<Errore> listaErrori = new ArrayList<Errore>();

		if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_INSPROVVCASSA)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		
		//Dati obbligatori:
		listaErrori = controlliObbligatorietaProvvisorioDiCassa(listaErrori);			
		
	
		//VALIDO
		if(listaErrori.isEmpty()){			
			return executeInserisciProvvisorioCassa();
		} else {
			addErrori(listaErrori);
			return INPUT;
		}		
	}	
	
	/**
	 * inserisce il valore di default nella chekboxList della pagina
	 * @return
	 */
	public String[] getDefaultValueAnnulla(){
		return new String [] {"no"};
	}

	
}
