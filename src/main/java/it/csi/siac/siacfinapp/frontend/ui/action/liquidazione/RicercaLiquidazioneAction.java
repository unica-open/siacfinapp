/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaLiquidazioneAction extends WizardRicercaLiquidazioneAction {

	private static final long serialVersionUID= -1L;
	
	/**
	 * metodo prepare della action
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca Liquidazione");
		
		//carichiamo i tipi provvedimento:
		caricaTipiProvvedimenti();
		
		//carichiamo la lista delle classi soggetto:
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	   	
	   	//setto la tipologia di oggetto trattato:
	   	oggettoDaPopolare = OggettoDaPopolareEnum.LIQUIDAZIONE;
	   	// jira 3114
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		resetPageNumberTableId("ricercaLiquidazioniID");

		//Pulisco i campi: Da verificare se serve 
		if (!hasActionErrors()) {
		   	pulisciCampi();
		}
	   	
		//jira 3114
	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   	
	   	if (caricaListeBil()) {
	   		//carica liste bil ha dato errore
			return INPUT;
		}
	   	
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
	   	if(teSupport==null) pulisciTransazioneElementare();
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(true);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////

		
		return SUCCESS;
	}	
	
	/**
	 * Gestisce la ricerca delle liquidazioni
	 * @return
	 */
	public String ricercaLiquidazioni(){
		
		//non siamo in pop up:
		model.setInPopup(false);
		
		//istanzio la lista degli eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Condizioni ricerca - Liquidazioni
		if (!StringUtils.isEmpty(model.getNumeroLiquidazioneString()) && !("0".equalsIgnoreCase(model.getNumeroLiquidazioneString()))) {
			try {
				model.setNumeroLiquidazione(convertiImportoToBigDecimal(model.getNumeroLiquidazioneString()));
			} catch (Exception e) {
				//formato non valido per il numero della liquidazione indicato
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Liquidazione "));
			}
		}else {
			model.setNumeroLiquidazione(null);
		}
		
		//Numero Mutuo
		if (!StringUtils.isEmpty(model.getNumeroMutuoString()) && !("0".equalsIgnoreCase(model.getNumeroMutuoString()))) {
			try {
				model.setNumeroMutuo(convertiImportoToBigDecimal(model.getNumeroMutuoString()));
			} catch (Exception e) {
				//formato non valido per il numero mutuo indicato
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Mutuo "));
			}
		}else if (!StringUtils.isEmpty(model.getNumeroMutuoImpegnoString()) && !("0".equalsIgnoreCase(model.getNumeroMutuoImpegnoString()))) {
			try {
				model.setNumeroMutuo(convertiImportoToBigDecimal(model.getNumeroMutuoImpegnoString()));
			} catch (Exception e) {
				//formato non valido per il numero mutuo indicato
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Mutuo "));
			}
		}else {
			model.setNumeroMutuo(null);
		}
		
		if(model.getNumeroLiquidazione()!=null && model.getAnnoLiquidazione()==null){
			//se indico un numero liquidazione, l'anno diventa obbligatorio
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Liquidazione obbligatorio"));
		}
		
		//Condizioni ricerca - Impegni
		if (model.getImpegno()!=null && model.getImpegno().getNumeroImpegno()!=null && model.getImpegno().getAnnoImpegno()==null) {
			//se indico un numero impegno, l'anno impegno e' obbligatorio
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno obbligatorio"));
		}
		
		//Condizioni ricerca nuova adeguata alle altre (vedi impegno/accertamento) - Provvedimento
		if(model.getProvvedimento().getAnnoProvvedimento()!=null && model.getProvvedimento().getAnnoProvvedimento().intValue()>0 ){
			if(model.getProvvedimento().getNumeroProvvedimento()==null && model.getProvvedimento().getIdTipoProvvedimento()==null){
				//se indico un anno provvedimento, devo indicare almeno uno tra numero provvedimento e tipo provvedimento
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero o Tipo Provvedimento obbligatorio con Anno Provvedimento"));
			}
		}

		if(model.getProvvedimento().getNumeroProvvedimento()!=null || model.getProvvedimento().getIdTipoProvvedimento()!=null){
			if(model.getProvvedimento().getAnnoProvvedimento()==null || model.getProvvedimento().getAnnoProvvedimento().intValue()==0 ){
				//se indico numero o tipo provvedimento, l'anno provvedimento diventa obbligatorio
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento obbligatorio con Numero o Tipo Provvedimento"));
			}
		}
		
		//Jira 1907/1908, ricerco impegni anche x uid sac provvedimento
		if(!StringUtils.isEmpty(strutturaSelezionataSuPagina)){
			model.getProvvedimento().setUidStrutturaAmm(Integer.parseInt(strutturaSelezionataSuPagina));
		}

		//VALIDO
		if(listaErrori.isEmpty()){			

			return "gotoElencoLiquidazioni";
		} else {
			addErrori(listaErrori);
			return INPUT;
		}
		
	}
	
	/**
	 * FIX PER JIRA SIAC-3477
	 * @return
	 */
	public String ricercaImpegnoCompilazioneGuidata(){
		return ricercaGuidataImpegno();
	}
	
	public String annullaRicercaLiquidazioni(){
		pulisciCampi();
		return "goToRicercaLiquidazione";
	}
	
	/**
	 * Azzera i dati della modale impegno
	 * @return
	 */
	public String azzeraModaleImpegno(){
		model.setInPopup(false);	
		pulisciListeeSchedaPopup();
		return "refreshPopupModalImpegno";
	}
	
	/**
	 * Carica le liste bil necessarie,
	 * in caso di errori ritorna true.
	 * @return
	 */
	private boolean caricaListeBil() {
		//invoco il servizio che carica i classificatori generici:
		LeggiClassificatoriGenericiByTipoElementoBilResponse respClassBil = caricaClassifGenericiFin("CAP-UG");
		if(respClassBil.isFallimento() || (respClassBil.getErrori() != null && respClassBil.getErrori().size() > 0)) {
			//errori nel servizio
			addErrori("ricercaLiquidazione", respClassBil);
			return true;
		}
		//setto le liste nel model:
		riempiListaClassGenBil(respClassBil);
		//tutto ok ritorno false:
		return false;
	}
	
	/**
	 * A partire dall'esito di un caricamento di classficatori generici setta la lista nel model
	 * @param bilResponse
	 */
	protected void riempiListaClassGenBil(LeggiClassificatoriGenericiByTipoElementoBilResponse bilResponse) {
		if (bilResponse.getClassificatoriTipoFinanziamento() != null && bilResponse.getClassificatoriTipoFinanziamento().size() > 0) {
			//ci sono dati setto nel model la lista
			model.setListaTipoFinanziamento(bilResponse.getClassificatoriTipoFinanziamento());
		} else{
			//non ci sono dati istanzio una lista senza elementi
			model.setListaTipoFinanziamento(new ArrayList<TipoFinanziamento>());
		}
	}
	
}
