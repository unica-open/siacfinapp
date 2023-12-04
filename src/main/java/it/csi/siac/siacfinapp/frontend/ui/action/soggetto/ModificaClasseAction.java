/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;




import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.GestioneClassiModel;


/**
 * 
 * 
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ModificaClasseAction extends SoggettoAction<GestioneClassiModel> {

	
	private static final long serialVersionUID = -6918565117851350145L;

	@Override
	public void prepare() throws Exception {

	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("modifica");

		/*
		 * 
		 Commentato in data 10 luglio 2017, perche' effettivamente non fa nulla.
		 Lascio il blocco di codice se dovesse servire come base di partenza per discriminare tra
		 modifica ed inserimento.
		 
		// recupera l'id della selezione
		GestioneClassiModel model = this.getModel();
		if ( model.getIdClasse() != null && model.getIdClasse() != 0 ) {
			// sono in modifica
		} else {
			// sono in inserimento
		}
		*/
		
		return ActionSupport.SUCCESS;
	}

}
