/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneSubaccertamentoStep2Action extends ActionKeyAggiornaAccertamento {

	private static final long serialVersionUID = 1L;
	
	public GestioneSubaccertamentoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBACCERTAMENTO;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.SUBACCERTAMENTO.toString());
		sessionHandler.setAnnoEsercizio(model.getStep1Model().getCapitolo().getAnno().toString());
		if(caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}
		caricaLabelsAggiornaSub(2);
		//SIAC-5943
		model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
		model.setSaltaInserimentoPrimaNota(false);
	    return SUCCESS;
	}
	
	public String salva() {
		setProseguiStep1(true);
		return salva(false);
	}
	
	/**
	 * funznioe di annulla nella seconda pagina degli step
	 */
	public String annulla() throws Exception {
		
		if (model.getAccertamentoInAggiornamento().getIdPdc() != null) {
			teSupport.getPianoDeiConti().setUid(model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getUid());
		}
		teSupport.getPianoDeiConti().setCodice(model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getCodice());
		// in descrizione metto codice e descrizione
		teSupport.getPianoDeiConti().setDescrizione( model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getCodice()+ "- "+model.getTransazioneElementareCache().getPianoDeiConti().getDescrizione());
		
		//CR-2023 eliminato conto economico

		// siope
		teSupport.setSiopeSpesa(new SiopeSpesa());
		if (model.getTransazioneElementareCache().getSiopeSpesa().getUid() != 0) {
			teSupport.getSiopeSpesa().setUid(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getUid());
		}
		teSupport.getSiopeSpesa().setCodice(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getCodice());
		teSupport.getSiopeSpesa().setDescrizione(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getDescrizione());
		// transazione europea
		teSupport.setTransazioneEuropeaSelezionato(model.getTransazioneElementareSubMovimentoCache().getTransazioneEuropeaSelezionato());
		// ricorrente entrata
		teSupport.setRicorrenteEntrataSelezionato(model.getTransazioneElementareSubMovimentoCache().getRicorrenteEntrataSelezionato());
		// sanitario entrata
		teSupport.setPerimetroSanitarioEntrataSelezionato(model.getTransazioneElementareSubMovimentoCache().getPerimetroSanitarioEntrataSelezionato());
		
	    return SUCCESS;
	}
	
	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		return false;
	}

	@Override
	public boolean cupAttivo() {
		return false;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}
	
	
	@Override
	public boolean datiUscitaImpegno(){
		return false;
	}
}