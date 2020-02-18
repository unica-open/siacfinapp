/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.InserisciLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

public abstract class WizardInserisciLiquidazioneAction extends LiquidazioneAction<InserisciLiquidazioneModel> {
	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		return "inserisciLiquidazione";
	}

	@BreadCrumb("%{model.titolo}")
	public String doExecute() throws Exception {
		//richiamo la execute della super classe:
		return execute();
	}
	
	//Metodi per la profilazione degli impegni
	public boolean isAbilitatoInsLiq(){
		//verifichiamo l'abilitazione all'azione OP_SPE_insLiq
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_insLiq);
	}
	
	protected void caricaListaMotivazioniAssenzaCig() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.MOTIVAZIONE_ASSENZA_CIG);
		model.setListaMotivazioniAssenzaCig((List<CodificaFin>)mappaListe.get(TipiLista.MOTIVAZIONE_ASSENZA_CIG));
	}
	
	/**
	 * Gestisce la chiamata al servizio di ricerca impegno
	 * @param indicatoSub
	 * @return
	 */
	protected RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegno(boolean indicatoSub){
		//MARZO 2016: RITORNIAMO AL ricercaImpegnoPerChiaveOttimizzato che HA SUBITO LE OTTIMIZZAZIONI SUI SUB:
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		rip.setEnte(sessionHandler.getEnte());
		rip.setRichiedente(sessionHandler.getRichiedente());
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		k.setAnnoImpegno(this.model.getAnnoImpegno());
		k.setNumeroImpegno(new BigDecimal(this.model.getNumeroImpegno()));
		
		//MARZO 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		if(indicatoSub){
			//Selezionato un SUB 
			rip.setCaricaSub(true);
			k.setNumeroSubDaCercare(new BigDecimal(model.getNumeroSub()));
		} else {
			//Non selezionato un SUB
			k.setNumeroSubDaCercare(null);
			rip.setCaricaSub(false);
		}
		//
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliDiRicerca = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliDiRicerca.setEscludiAnnullati(true);// usato se ricerco l'impegno senza specificare il sub
		
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliDiRicerca);
		rip.setEscludiSubAnnullati(true); // usato se ricerco l'impegno con sub diretto 
		
		rip.setpRicercaImpegnoK(k);
		
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		
		return respRk;
	}
	
	/**
	 * Per verificare se l'utente ha compilato o meno il campo del sub impegno
	 * @return
	 */
	protected boolean isCompilatoNumeroSubImpegno(){
		boolean compilato = false;
		if(model.getNumeroSub()!=null && model.getNumeroSub().intValue()>0){
			//compilato
			compilato = true;
		}
		return compilato;
	}
	
	/**
	 * Ritorna la lista di sub impegni alla quale riferirsi.
	 * 
	 * ATTENZIONE: lavora in stetta dipendenza da come e' stata invocata la request.
	 * 
	 * Dipende tutto dal valore del boolean indicatoSub rispetto alla composizione della request che ha
	 * determinato la respPk.
	 * 
	 * @param indicatoSub
	 * @param respRk
	 * @return
	 */
	protected List<SubImpegno> elencoSubDiRiferimento(boolean indicatoSub, RicercaImpegnoPerChiaveOttimizzatoResponse respRk){
		List<SubImpegno> elencoSubImpegni = null;
		if(respRk!=null){
			if(indicatoSub){
				Impegno impegno = respRk.getImpegno();
				if(impegno!=null){
					//Se ho indicato un sub avro' l'elenco COMPLETO DI TUTTE LE INFO con il SOLO sub Esplicitamente RICHIESTO
					elencoSubImpegni = impegno.getElencoSubImpegni();
				}
			} else {
				//Se NON ho indicato un sub avro' solo l'elenco con LE INFO MINIME per tutti i sub
				//ma non e' un problema perche' tanto verra' lanciato l'errore che se NON INDICO UN SUB e CI SONO DEI SUB 
				//DOVEVA essere obbligatorio indicarne uno
				elencoSubImpegni =  respRk.getElencoSubImpegniTuttiConSoloGliIds();
			}
		}
		return elencoSubImpegni;
	}
	
	/**
	 * Metodo interno a preparaCampiInserisciLiquidazione.
	 */
	protected void impostaDisponibilitaLiquidare(){
		if(model.getNumeroMutuoPopup()!=null){
			//numero mutuo inserito
			if(!isEmpty(model.getListaVociMutuo())){
				//ci sono voci mutuo
				VoceMutuo mutuoCorrispondente = FinUtility.findVoceMutuoByNumero(model.getListaVociMutuo(), model.getNumeroMutuoPopup());
				if(mutuoCorrispondente!=null){
					//trovato mutuo corrispondente
					model.setDisponibilita(mutuoCorrispondente.getImportoDisponibileLiquidareVoceMutuo());
				}
			}
		}else{
			model.setDisponibilita(model.getImpegno().getDisponibilitaLiquidare());
		}
	}
	
	protected boolean settaVociMutuoNelModel(){
		//SETTIAMO LE VARIABILI NEL MODEL:
		boolean ciSonoMutui = ciSonoMutui(model.getImpegno());
		List<VoceMutuo> listaVociMutuo = new ArrayList<VoceMutuo>();
		if(ciSonoMutui){
			listaVociMutuo = model.getImpegno().getListaVociMutuo();
			model.setHasMutui(true);
		}
		model.setListaVociMutuo(listaVociMutuo);
		return ciSonoMutui;
	}
	
	/**
	 * Per verificare che l'impegno abbia voci di mutuo
	 * @return
	 */
	protected boolean ciSonoMutui(Impegno impegno){
		boolean ciSonoMutui =  false;
		if(impegno!=null && !isEmpty(impegno.getListaVociMutuo())){
			//ok lista voci mutui ha elementi
			ciSonoMutui = true;
		}
		return ciSonoMutui;
	}
	
}
