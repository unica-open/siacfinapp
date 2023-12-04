/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModificaMovimentoGestioneSpesaDefault;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModificaMovimentoGestioneSpesaDefaultResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

//SIAC-7349 Inizio  SR180 FL 08/04/2020
class SortByAnnoReimputazione implements Comparator<ModificaMovimentoGestioneSpesaCollegata> { 
	//Usata per ordinare la lsita collegata Reimpuatzione Spese per anno asc
	public int compare(ModificaMovimentoGestioneSpesaCollegata a, ModificaMovimentoGestioneSpesaCollegata b) { 
	    return a.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().intValue() - b.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().intValue(); 
	} 
  
} 
/**
 * Classe di utilita' per parti comuni dei ROR lato Entrata/Incasso
 * @author atodesco
 *
 */
public class BaseRorEntrata extends ActionKeyAggiornaAccertamento {

	/** serialization */
	private static final long serialVersionUID = 1L;

	//SIAC-7349 Inizio  SR180 FL 08/04/2020
	List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata;
	
	protected List<ModificaMovimentoGestioneSpesaCollegata> initListaModificheSpesaCollegataSoloReimpAggiornamento(List<ModificaMovimentoGestioneEntrata> listaModifiche) {
		List<ModificaMovimentoGestioneSpesaCollegata> listaModificheResult = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();;
		//SIAC-8187 evito di lanciare il servizio se non ho l'accertamento oppure ho un accertamento con un uid non valido
		if(model.getAccertamentoInAggiornamento() == null ||
				(model.getAccertamentoInAggiornamento() != null && model.getAccertamentoInAggiornamento().getUid() == 0)) {
			log.debug("initListaModificheSpesaCollegataSoloReimp", "uid accertamento non passato evito la chiamata");
			return listaModificheResult;
		}
		
		RicercaModificaMovimentoGestioneSpesaDefault request = new RicercaModificaMovimentoGestioneSpesaDefault();
		request.setRichiedente(model.getRichiedente());
		request.setAccertamento(model.getAccertamentoInAggiornamento());
		request.setDataOra(new Date());
		//SIAC-8609
		request.setCaricaModificheGiaCollegateMaModificabili(listaModifiche != null && !listaModifiche.isEmpty());
		
		RicercaModificaMovimentoGestioneSpesaDefaultResponse response = movimentoGestioneFinService.ricercaModificaMovimentoGestioneSpesaDefault(request);
		
		if(response != null && response.hasErrori()) {
			addErrori(response.getErrori());
			return listaModificheResult;
		}
		
		if(CollectionUtils.isNotEmpty(response.getListaModificheMovimentoGestioneSpesaCollegata())) {
			listaModificheResult.addAll(response.getListaModificheMovimentoGestioneSpesaCollegata());
		}
		
		if(CollectionUtils.isNotEmpty(response.getListaModificheSpesaGiaCollegateConResiduoACollegareZero())) {
			listaModificheResult.addAll(response.getListaModificheSpesaGiaCollegateConResiduoACollegareZero());
		}
		
		if(CollectionUtils.isNotEmpty(listaModificheResult)) {
			Collections.sort(listaModificheResult, new SortByAnnoReimputazione());
		}
		
		return listaModificheResult;
	}

	//SIAC-7349 Fine  SR180 FL 08/04/2020
	
	/**
	 * SIAC-8041
	 * Metodo di utilita' per ottenere le reimputazioni dalla lista delle modifiche collegate
	 * @param listaModifiche
	 * @return List<ModificaMovimentoGestioneSpesaCollegata> lista
	 */
	@Override
	protected List<ModificaMovimentoGestioneSpesaCollegata> initListaModificheSpesaCollegataSoloReimp(List<ModificaMovimentoGestioneSpesaCollegata> listaModifiche) {
		//SIAC-8187 evito di lanciare il servizio se non ho l'accertamento oppure ho un accertamento con un uid non valido
		if(model.getAccertamentoInAggiornamento() == null ||
				(model.getAccertamentoInAggiornamento() != null && model.getAccertamentoInAggiornamento().getUid() == 0)) {
			log.debug("initListaModificheSpesaCollegataSoloReimp", "uid accertamento non passato evito la chiamata");
			return new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
		}
		
		RicercaModificaMovimentoGestioneSpesaDefault request = new RicercaModificaMovimentoGestioneSpesaDefault();
		request.setRichiedente(model.getRichiedente());
		request.setAccertamento(model.getAccertamentoInAggiornamento());
		request.setDataOra(new Date());
		
		RicercaModificaMovimentoGestioneSpesaDefaultResponse response = movimentoGestioneFinService.ricercaModificaMovimentoGestioneSpesaDefault(request);
		
		if(response != null && response.hasErrori()) {
			addErrori(response.getErrori());
			return new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
		}
		
		if(CollectionUtils.isNotEmpty(response.getListaModificheMovimentoGestioneSpesaCollegata())) {
			Collections.sort(response.getListaModificheMovimentoGestioneSpesaCollegata(), new SortByAnnoReimputazione());
		}
		
		listaModifiche = response.getListaModificheMovimentoGestioneSpesaCollegata();
		
		return response.getListaModificheMovimentoGestioneSpesaCollegata();
	}
	
	// SIAC-8553
	protected boolean controlloImportoModificaSuDisponibiltaAccertareCapitolo(BigDecimal importoDiInput, List<Errore> listaWarning) {
		BigDecimal nuovaDisponibilitaCapitolo = calcolaNuovaDisponibiltaAccertateCapitolo(model.getAccertamentoInAggiornamento().getAnnoMovimento(),
				model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione(), importoDiInput);
		
		if(nuovaDisponibilitaCapitolo.compareTo(BigDecimal.ZERO) < 0 && !model.isProseguiConWarningModificaPositivaAccertamento()) {
			listaWarning.add(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore());
			addPersistentActionWarningFin(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore());
			return true;
		}

		return false;
	}
	
	private int getDifferenzaAnnoMovimentoSuAnnoCapitolo(Integer annoMovimento, CapitoloEntrataGestione capitoloMovimento) {
		return annoMovimento == null || capitoloMovimento.getAnnoCapitolo() == null ? -1 : 
			annoMovimento.intValue() - capitoloMovimento.getAnnoCapitolo().intValue();
	}
	
	public BigDecimal calcolaDisponibiltaAccertateCapitolo(Integer annoMovimento, CapitoloEntrataGestione capitoloEntrataMovimento) {
		
		BigDecimal disponibilitaAccertareCapitolo = BigDecimal.ZERO;
		
		int diffAnnoMovimentoCapitolo = getDifferenzaAnnoMovimentoSuAnnoCapitolo(annoMovimento, capitoloEntrataMovimento);
		
		switch (diffAnnoMovimentoCapitolo) {
			case 0:
				disponibilitaAccertareCapitolo = capitoloEntrataMovimento.getImportiCapitolo().getDisponibilitaAccertareAnno1();
				break;
			case 1:
				disponibilitaAccertareCapitolo = capitoloEntrataMovimento.getImportiCapitolo().getDisponibilitaAccertareAnno2();
				break;
			case 2:
				disponibilitaAccertareCapitolo = capitoloEntrataMovimento.getImportiCapitolo().getDisponibilitaAccertareAnno3();
				break;
			default:
				model.setFlagSuperioreTreAnni(true);
				break;
		}
		
		return disponibilitaAccertareCapitolo;
	}

	private BigDecimal calcolaNuovaDisponibiltaAccertateCapitolo(Integer annoMovimento, CapitoloEntrataGestione capitoloEntrataMovimento, BigDecimal importoModifica) {
		return calcolaDisponibiltaAccertateCapitolo(annoMovimento, capitoloEntrataMovimento).subtract(importoModifica != null ? importoModifica : BigDecimal.ZERO);
	}
	
	protected ModificaMovimentoGestioneEntrata filtraListaModificheMovgestEntrata(Accertamento accertamento, Integer uidModifica) {
		ModificaMovimentoGestioneEntrata filtrata = null;
		if(uidModifica != null && accertamento != null && CollectionUtils.isNotEmpty(accertamento.getListaModificheMovimentoGestioneEntrata())) {
			for (ModificaMovimentoGestioneEntrata modifica : accertamento.getListaModificheMovimentoGestioneEntrata()) {
				if(uidModifica.compareTo(modifica.getUid()) == 0) {
					filtrata = modifica; 
					break;
				}
			}
		}
		return filtrata;
	}

	protected ModificaMovimentoGestioneSpesaCollegata filtraListaModificheMovgestSpesa(Accertamento accertamento, Integer uidModifica) {
		ModificaMovimentoGestioneSpesaCollegata filtrata = null;
		if(accertamento != null && CollectionUtils.isNotEmpty(accertamento.getListaModificheMovimentoGestioneSpesaCollegata())) {
			for (ModificaMovimentoGestioneSpesaCollegata modifica : accertamento.getListaModificheMovimentoGestioneSpesaCollegata()) {
				if(uidModifica != null && uidModifica.compareTo(modifica.getUid()) == 0) {
					filtrata = modifica; 
					break;
				}
			}
		}
		return filtrata;
	}

	/** GETTER AND SETTER */
	
	/**
	 * @return the listaModificheSpeseCollegata
	 */
	public List<ModificaMovimentoGestioneSpesaCollegata> getListaModificheSpeseCollegata() {
		return listaModificheSpeseCollegata;
	}

	/**
	 * @param listaModificheSpeseCollegata the listaModificheSpeseCollegata to set
	 */
	public void setListaModificheSpeseCollegata(List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata) {
		this.listaModificheSpeseCollegata = listaModificheSpeseCollegata;
	}
	
	
}
