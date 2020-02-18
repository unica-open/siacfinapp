/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.ImpegnoSubimpegnoMutuoModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneVoceDiMutuoStep1Action extends GenericVoceDiMutuoAction {

	private static final long serialVersionUID = 1L;
	private static final String PROSEGUI = "prosegui";
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
	
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Gestione Voce di Mutuo - Provvedimento");
		//carico la lista dei tipi documento
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
	}
	
	
	public GestioneVoceDiMutuoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.MUTUO;
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
	    return SUCCESS;
	}
	
	public String prosegui() {
		List<Errore> listaErrori = new ArrayList<Errore>();
		//controllo se sono stati inseriti i campi obbligatori
		if(model.getProvvedimento().getAnnoProvvedimento() == null || model.getProvvedimento().getAnnoProvvedimento().intValue() == 0){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento "));
	    }
		if((model.getProvvedimento().getNumeroProvvedimento() == null || model.getProvvedimento().getNumeroProvvedimento().intValue() == 0) &&(model.getProvvedimento().getIdTipoProvvedimento() == null || model.getProvvedimento().getIdTipoProvvedimento().toString().equalsIgnoreCase("") )){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento o Tipo Provvedimento "));
	    }
		
		if (!listaErrori.isEmpty()) {
		
			//stampo gli errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		// setto a vuoto la lista cosi' forzo la ricerca
		model.setListaImpegniSubimpegni(new ArrayList<ImpegnoSubimpegnoMutuoModel>());
		
		return PROSEGUI;
	}
	
	
	/**
	 *  utilizzato nella jsp
	 * @return
	 */
	public boolean checkProvvedimentoStato(){
		if(model.isProvvedimentoSelezionato()){
				return true;
			}else{
				return false;
			}
		}
	
}