/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.util.List;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaMutuo;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMutuoAction extends WizardRicercaMutuoAction {
	
	private static final long serialVersionUID = 1L;

	public RicercaMutuoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.MUTUO;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca Mutuo");
		
		//se la lista tipi provvedimento e' vuota, la carico
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));	
	   	model.setListaTipiMutuo((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		if(teSupport==null) pulisciTransazioneElementare();
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		
		// arrivo da indietro elenco
		if(!forceReload){
			pulisceDatiRicerca();
		}
		
	    return SUCCESS;
	}
	
	
	
	public String cerca(){
		//passo all'elenco dei mutui dove verra' affettuata la ricerca
		model.setPremutoPaginazione(0);
		sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_MUTUO, new ParametroRicercaMutuo());
		return GOTO_ELENCO_MUTUI;
	}
	
	/**
	 * ritorna false e quindi non deve far
	 * uscire l'asterisco
	 * 
	 * @return boolean
	 */
	public boolean soggettoObbligatorio(){
		return false;
	}
	
	
}
