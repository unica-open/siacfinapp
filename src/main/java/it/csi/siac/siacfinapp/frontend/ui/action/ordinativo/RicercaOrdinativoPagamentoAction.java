/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.List;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaOrdinativoPagamentoAction extends ActionKeyRicercaOrdinativoPagamentoAction {
	

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Ricerca ordinativi di pagamento");
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	   	
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
		caricaListeCombo();

	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_RICMAN)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
		}

		//Pulisco i campi 
		if (!hasActionErrors()) {
			pulisciCampi();
		}

		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		//non c'e' transazione elementare, la uso solo per pilotare gli alberi inutili da non caricare:
		teSupport = new GestoreTransazioneElementareModel();
		teSupport.setRicaricaAlberoPianoDeiConti(false);
//		teSupport.setRicaricaAlberoContoEconomico(false);CR-2023
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);

	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   	
	   	if(teSupport==null){
			pulisciTransazioneElementare();
		}
	   		   	
	   	if(caricaListaTipoFinanziamento()){
	   		return INPUT;
	   	}

		return SUCCESS;
	}


	
	public String ricercaOrdinativoPagamento(){

		resetPageNumberTableId("ricercaOrdinativoPagamentoID");
		
		List<Errore> listaErrori = checkParametri();

		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_RICMAN)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		
		//VALIDO
		if(listaErrori.isEmpty()){			

			return "gotoElencoOrdinativoPagamento";
		} else {
			addErrori(listaErrori);
			return INPUT;
		}
		
	}

}
