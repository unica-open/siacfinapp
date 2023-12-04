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
public class GestioneSubimpegnoStep2Action extends ActionKeyAggiornaImpegno {

	private static final long serialVersionUID = 1L;
	
	public GestioneSubimpegnoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBIMPEGNO;
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
		teSupport.setCup(model.getStep1ModelSubimpegno().getCup());
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.SUBIMPEGNO.toString());
		if (caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		} else {
			caricaMissione(model.getStep1Model().getCapitolo());
			caricaProgramma(model.getStep1Model().getCapitolo());
			caricaListaCofog(model.getStep1Model().getCapitolo().getIdProgramma());
		}
		caricaLabelsAggiornaSub(2);
		//SIAC-5943
		model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
		model.setSaltaInserimentoPrimaNota(false);
	    return SUCCESS;
	}
	/**
	 *  funzione salva
	 * @return
	 */
	public String salva() {
		setProseguiStep1(true);
		return salva(false);
	}
	
	/**
	 *  funzione di annullamento
	 */
	public String annulla() throws Exception {
		
		if (model.getImpegnoInAggiornamento().getIdPdc() != null) {
			teSupport.getPianoDeiConti().setUid(model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getUid());
		}
		teSupport.getPianoDeiConti().setCodice(model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getCodice());
		// in descrizione metto codice e descrizione
		teSupport.getPianoDeiConti().setDescrizione( model.getTransazioneElementareSubMovimentoCache().getPianoDeiConti().getCodice()+ "- "+model.getTransazioneElementareCache().getPianoDeiConti().getDescrizione());

		
		//CR-2023 eliminato conto economico

		teSupport.setSiopeSpesa(new SiopeSpesa());
		if (model.getTransazioneElementareCache().getSiopeSpesa().getUid() != 0) {
			teSupport.getSiopeSpesa().setUid(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getUid());
		}
		teSupport.getSiopeSpesa().setCodice(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getCodice());
		teSupport.getSiopeSpesa().setDescrizione(model.getTransazioneElementareSubMovimentoCache().getSiopeSpesa().getDescrizione());
		teSupport.setCofogSelezionato(model.getTransazioneElementareSubMovimentoCache().getCofogSelezionato());
		teSupport.setTransazioneEuropeaSelezionato(model.getTransazioneElementareSubMovimentoCache().getTransazioneEuropeaSelezionato());
		teSupport.setRicorrenteSpesaSelezionato(model.getTransazioneElementareSubMovimentoCache().getRicorrenteSpesaSelezionato());
		teSupport.setPerimetroSanitarioSpesaSelezionato(model.getTransazioneElementareSubMovimentoCache().getPerimetroSanitarioSpesaSelezionato());
		teSupport.setPoliticaRegionaleSelezionato(model.getTransazioneElementareSubMovimentoCache().getPoliticaRegionaleSelezionato());
	    return SUCCESS;
	}
	
	@Override
	public boolean altriClassificatoriAttivi() {
		// non sono da far vedere i classificatori
		return false;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		// oggetto derivante da impegno
		return true;
	}
	
}