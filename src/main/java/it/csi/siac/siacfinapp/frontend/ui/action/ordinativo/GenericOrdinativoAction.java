/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoFamigliaCausale;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.CausaleEntrataTendinoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public abstract class GenericOrdinativoAction<M extends GenericPopupModel> extends GenericPopupAction<M> {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected transient PreDocumentoEntrataService preDocumentoEntrataService;
	
	/**
	 * Metodo centralizzato per caricare in soggImpModel i dati presenti in soggetto o classe soggetto
	 * @param soggImpModel
	 * @param soggetto
	 * @param classeSoggetto
	 */
	protected void caricaDatiSoggettoOrdinativo(SoggettoImpegnoModel soggImpModel, Soggetto soggetto, ClasseSoggetto classeSoggetto){	
		if(soggetto.getCodiceSoggetto()!=null){ 
			soggImpModel.setCodCreditore(soggetto.getCodiceSoggetto());
		}
		
		if(!StringUtils.isEmpty(soggetto.getCodiceFiscale())){
			soggImpModel.setCodfisc(soggetto.getCodiceFiscale());
		}
		
		if(!StringUtils.isEmpty(soggetto.getDenominazione())){
			soggImpModel.setDenominazione(soggetto.getDenominazione());
		}
		
		if(classeSoggetto!=null && classeSoggetto.getCodice()!=null){
			soggImpModel.setClasse(classeSoggetto.getCodice());
		}
		
		//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
		inserisciDescClasseSoggettoOrdinativo(soggImpModel);
	}
	
	protected void inserisciDescClasseSoggettoOrdinativo(SoggettoImpegnoModel soggImpModel){
	    if(soggImpModel.getClasse() != null && !"".equalsIgnoreCase(soggImpModel.getClasse()) ){
	    	for(CodificaFin cod:model.getListaClasseSoggetto()){
	    		if(cod.getCodice().equalsIgnoreCase(soggImpModel.getClasse())){
	    			soggImpModel.setClasseDesc(cod.getDescrizione());
	    		}
	    	}
	    }
	}
	
	/**
	 * Metodo centralizzato per settare nel model indicato i dati del capitolo trovato
	 * @param capitoloUscita
	 * @param capitoloEntrata
	 * @param capitoloModel
	 */
	protected void caricaDatiCapitolo(CapitoloUscitaGestione capitoloUscita,CapitoloEntrataGestione capitoloEntrata, CapitoloImpegnoModel capitoloModel){	//Da spostare	
		
		if(capitoloUscita==null){
			capitoloUscita = new CapitoloUscitaGestione();
		}
		
		if(capitoloEntrata==null){
			capitoloEntrata = new CapitoloEntrataGestione();
		}
		
		if(capitoloUscita.getAnnoCapitolo()!=null){ 
			capitoloModel.setAnno(capitoloUscita.getAnnoCapitolo());
		}else{
			capitoloModel.setAnno(capitoloEntrata.getAnnoCapitolo());
		}
		
		if(capitoloUscita.getNumeroCapitolo()!=null){ 
			capitoloModel.setNumCapitolo(capitoloUscita.getNumeroCapitolo());
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getNumeroCapitolo()!=null){
			capitoloModel.setNumCapitolo(capitoloEntrata.getNumeroCapitolo());
		}
		
		if(capitoloUscita.getNumeroArticolo()!=null){ 
			capitoloModel.setArticolo(capitoloUscita.getNumeroArticolo());
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getNumeroArticolo()!=null){
			capitoloModel.setArticolo(capitoloEntrata.getNumeroArticolo());
		}
		
		if(capitoloUscita.getNumeroUEB()!=null){ 
			capitoloModel.setUeb(new BigInteger(String.valueOf(capitoloUscita.getNumeroUEB())));
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getNumeroUEB()!=null){
			capitoloModel.setUeb(new BigInteger(String.valueOf(capitoloEntrata.getNumeroUEB())));
		}
		
		if(capitoloUscita.getUid()!=0){ 
			capitoloModel.setUid(capitoloUscita.getUid());
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getUid()!=0){
			capitoloModel.setUid(capitoloEntrata.getUid());
		}
		
		if(capitoloUscita.getTipoFinanziamento()!=null){ 
			capitoloModel.setTipoFinanziamento(capitoloUscita.getTipoFinanziamento().getDescrizione());
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getTipoFinanziamento()!=null){
			capitoloModel.setTipoFinanziamento(capitoloEntrata.getTipoFinanziamento().getDescrizione());
		}
		
		if (capitoloEntrata.getCategoriaTipologiaTitolo() != null) {
			capitoloModel.setIdMacroAggregato(capitoloEntrata.getCategoriaTipologiaTitolo().getUid());
			teSupport.setIdMacroAggregato(capitoloEntrata.getCategoriaTipologiaTitolo().getUid());
		}
		
		if (capitoloUscita.getElementoPianoDeiConti() != null) {
			capitoloModel.setCodicePdcFinanziario(capitoloUscita.getElementoPianoDeiConti().getCodice());
			capitoloModel.setDescrizionePdcFinanziario(capitoloUscita.getElementoPianoDeiConti().getCodice()+"-"+capitoloUscita.getElementoPianoDeiConti().getDescrizione());
			capitoloModel.setIdPianoDeiConti(capitoloUscita.getElementoPianoDeiConti().getUid());
		}else if(capitoloEntrata.getAnnoCapitolo()!=null && capitoloEntrata.getElementoPianoDeiConti() != null){
			capitoloModel.setCodicePdcFinanziario(capitoloEntrata.getElementoPianoDeiConti().getCodice());
			capitoloModel.setDescrizionePdcFinanziario(capitoloEntrata.getElementoPianoDeiConti().getCodice()+"-"+capitoloEntrata.getElementoPianoDeiConti().getDescrizione());
			capitoloModel.setIdPianoDeiConti(capitoloEntrata.getElementoPianoDeiConti().getUid());
		}
		
		if (capitoloUscita.getMacroaggregato() != null) {
			capitoloModel.setIdMacroAggregato(capitoloUscita.getMacroaggregato().getUid());
			 teSupport.setIdMacroAggregato(capitoloUscita.getMacroaggregato().getUid());
		}//non macroaggragato per capEntrata
		
		if (capitoloUscita.getMissione() != null) {
			capitoloModel.setCodiceMissione(capitoloUscita.getMissione().getCodice());
			capitoloModel.setDescrizioneMissione(capitoloUscita.getMissione().getDescrizione());
		}
		
		if (capitoloUscita.getProgramma() != null) {
			capitoloModel.setCodiceProgramma(capitoloUscita.getProgramma().getCodice());
			capitoloModel.setDescrizioneProgramma(capitoloUscita.getProgramma().getDescrizione());
			capitoloModel.setIdProgramma(capitoloUscita.getProgramma().getUid());
		} 
		
		if (capitoloUscita.getDescrizione() != null) {
			capitoloModel.setDescrizione(capitoloUscita.getDescrizione());
		} else if(capitoloEntrata.getDescrizione() != null){
			capitoloModel.setDescrizione(capitoloEntrata.getDescrizione());
		}
	
		if (capitoloUscita.getElementoPianoDeiConti() != null) {
			capitoloModel.setElementoPianoDeiConti(capitoloUscita.getElementoPianoDeiConti());
		} else if(capitoloEntrata.getDescrizione() != null){
			capitoloModel.setElementoPianoDeiConti(capitoloEntrata.getElementoPianoDeiConti());
		}
		
		if (capitoloUscita.getTipoFinanziamento()!= null) {
			capitoloModel.setFinanziamento(capitoloUscita.getTipoFinanziamento());
		} else if(capitoloEntrata.getDescrizione() != null){
			capitoloModel.setFinanziamento(capitoloEntrata.getTipoFinanziamento());
		}
		
		if (capitoloUscita.getStrutturaAmministrativoContabile()!= null && capitoloUscita.getStrutturaAmministrativoContabile().getDescrizione() != null) {
			capitoloModel.setCodiceStrutturaAmministrativa(capitoloUscita.getStrutturaAmministrativoContabile().getCodice());
			capitoloModel.setDescrizioneStrutturaAmministrativa(capitoloUscita.getStrutturaAmministrativoContabile().getDescrizione());
			capitoloModel.setUidStruttura(capitoloUscita.getStrutturaAmministrativoContabile().getUid());
		} else if(capitoloEntrata.getStrutturaAmministrativoContabile()!= null && capitoloEntrata.getStrutturaAmministrativoContabile().getDescrizione() != null){
			capitoloModel.setCodiceStrutturaAmministrativa(capitoloEntrata.getStrutturaAmministrativoContabile().getCodice());
			capitoloModel.setDescrizioneStrutturaAmministrativa(capitoloEntrata.getStrutturaAmministrativoContabile().getDescrizione());
			capitoloModel.setUidStruttura(capitoloEntrata.getStrutturaAmministrativoContabile().getUid());
		}
		
		if(!isEmpty(capitoloUscita.getListaImportiCapitolo())){
			capitoloModel.setImportiCapitolo(new ArrayList<ImportiCapitoloModel>());
			capitoloModel.setImportiCapitoloUG(capitoloUscita.getListaImportiCapitolo());
			ImportiCapitoloModel supportImporti;
			for (ImportiCapitoloUG currentImporto : capitoloUscita.getListaImportiCapitolo()) {
				supportImporti = new ImportiCapitoloModel();
				supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
				supportImporti.setCassa(currentImporto.getStanziamentoCassa());
				supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
				supportImporti.setCompetenza(currentImporto.getStanziamento());
				// RM 08042015, commentato perchè non usato, verifica pulizia functione e attibuti entità non piu usate
				capitoloModel.getImportiCapitolo().add(supportImporti);
			}
			model.getDatoPerVisualizza().setImportiCapitolo(capitoloModel.getImportiCapitolo());
		} else if(!isEmpty(capitoloEntrata.getListaImportiCapitolo())){
			capitoloModel.setImportiCapitolo(new ArrayList<ImportiCapitoloModel>());
			capitoloModel.setImportiCapitoloEG(capitoloEntrata.getListaImportiCapitolo());
			ImportiCapitoloModel supportImporti;
			for (ImportiCapitoloEG currentImporto : capitoloEntrata.getListaImportiCapitolo()) {
				supportImporti = new ImportiCapitoloModel();
				supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
				supportImporti.setCassa(currentImporto.getStanziamentoCassa());
				supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
				supportImporti.setCompetenza(currentImporto.getStanziamento());
				
				if (currentImporto.getAnnoCompetenza().intValue() == capitoloModel.getAnno().intValue()) {
					capitoloModel.setDisponibileAnno1(capitoloEntrata.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
				} else if (currentImporto.getAnnoCompetenza().intValue() == (capitoloModel.getAnno().intValue() + 1)) {
					capitoloModel.setDisponibileAnno2(capitoloEntrata.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
				} else {
					capitoloModel.setDisponibileAnno3(capitoloEntrata.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
				}
				capitoloModel.getImportiCapitolo().add(supportImporti);
			}
			model.getDatoPerVisualizza().setImportiCapitolo(capitoloModel.getImportiCapitolo());
		} 
	}	
	
	/**
	 * Si occupa di caricare la lista dei tipi causale entrata
	 * 
	 * Viene utilizzato dagli ordinativi di incasso tipicamente.
	 * 
	 * @return
	 */
	protected List<TipoCausale> caricaListaTipiCausaleEntrata(){
		List<TipoCausale> tipiCausale = null;
		
		//costruisco la request:
		LeggiTipiCausaleEntrata reqTipi = new LeggiTipiCausaleEntrata();
		reqTipi.setDataOra(new Date());
		reqTipi.setRichiedente(sessionHandler.getRichiedente());
		reqTipi.setEnte(sessionHandler.getEnte());
		
		//invoco il servizio che carica i tipi causale:
		LeggiTipiCausaleEntrataResponse response = preDocumentoEntrataService.leggiTipiCausaleEntrata(reqTipi);
		
		if(!isFallimento(response)){
			//ok esito positivo
			tipiCausale = response.getTipiCausale();
		}
		
		return tipiCausale;
	}
	
	/**
	 * Dato un tipo causale si occupa di cercare tutte le causali di tale tipo.
	 * 
	 * Viene usato sia al variare della scelta del tipo in inserimento o ricerca,
	 * sia impostando i dati in aggiornamento a 
	 * partire dal tipo gia noto di un ordinativo appena caricato.
	 * 
	 * @param tipoSelezionato
	 */
	protected List<CausaleEntrata> caricaTendinoCausaliEntrata(TipoCausale tipoSelezionato){

		List<CausaleEntrata> listaCausali = null;
		
		if(tipoSelezionato!=null){
			
			//costruisco la request per chiamare il servizio ricercaSinteticaCausaleSpesa
			//il quale mi restituisce le causali legate al tipo selezionato:
			RicercaSinteticaCausaleEntrata reqCaus = new RicercaSinteticaCausaleEntrata();
			reqCaus.setRichiedente(sessionHandler.getRichiedente());
			reqCaus.setDataOra(new Date());
			ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
			parametriPaginazione.setNumeroPagina(0);
			parametriPaginazione.setElementiPerPagina(1000);
			reqCaus.setParametriPaginazione(parametriPaginazione);
			CausaleEntrata causaleEntrata = new CausaleEntrata();
			causaleEntrata.setTipoCausale(tipoSelezionato);
			tipoSelezionato.setTipoFamigliaCausale(TipoFamigliaCausale.PREDOC_ENTRATA);
			causaleEntrata.setEnte(sessionHandler.getEnte());
			reqCaus.setCausaleEntrata(causaleEntrata);
			
			//effettuo la chiamata:
			RicercaSinteticaCausaleEntrataResponse rspeCaus = preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(reqCaus);
			
			if(!isFallimento(rspeCaus)){
				//ok esito positibo
				ListaPaginata<CausaleEntrata> listaCausaliPagina = rspeCaus.getCausaliEntrata();
				if(listaCausaliPagina!=null){
					//ok ci sono risultati (ma potrebbero non esserci causali legate ad un certo tipo)
					listaCausali = (List<CausaleEntrata>) listaCausaliPagina;
				}
			}
		}
		return listaCausali;
	}
	
	/**
	 * Gestisce l'evento di selezione di un elemento nel tendino del tipo causale.
	 * @return
	 */
	protected void tipoCausaleEntrataChanged(CausaleEntrataTendinoModel modelTendiniCausali){
		
		//pulisco i dati del secondo tendino:
		tipoCausaleEntrataCleanDati(modelTendiniCausali);
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//leggo il codice del tipo selezionato:
		String codSel = request.getParameter("selezionata");
		
		//cerco l'oggetto intero riferito al codice selezioato:
		TipoCausale tipoSelezionato = tipoCausaleByCodiceSelezionato(codSel,modelTendiniCausali);
		
		//metodo "core" che popola la lista a partire dal tipo:
		List<CausaleEntrata> listaCausali = caricaTendinoCausaliEntrata(tipoSelezionato);
		
		//setto il codice tipo nel model:
		modelTendiniCausali.setCodiceTipoCausale(codSel);
		
		//setto la lista della causali nel model:
		modelTendiniCausali.setListaCausali(listaCausali);
		
	}
	
	/**
	 * Pulisce i dati del tendino delle causali al cambio del tipo
	 */
	protected void tipoCausaleEntrataCleanDati(CausaleEntrataTendinoModel modelCausali){
		//resetto la lista della causali nel model:
		modelCausali.setListaCausali(null);
		//azzero il codice causale eventualemente selezionato in precedenza (perche' la lista e' appena cambiata):
		modelCausali.setCodiceCausale(null);
	}
	
	/**
	 * cerca dentro la lista dei tipi causale l'elemento con il codice indicato
	 * @param codice
	 * @return
	 */
	protected TipoCausale tipoCausaleByCodiceSelezionato(String codice,CausaleEntrataTendinoModel modelCausali){
		TipoCausale trovato = null;
		if(modelCausali!=null){
			List<TipoCausale> listaTipi = modelCausali.getListaTipiCausale();
			trovato = FinUtility.tipoCausaleByCodice(codice,listaTipi);
		}
		return trovato;
	}
	
	/**
	 * cerca dentro la lista delle causali l'elemento con il codice indicato
	 * @param codice
	 * @return
	 */
	protected CausaleEntrata causaleByCodiceSelezionato(String codice,CausaleEntrataTendinoModel modelCausali){
		CausaleEntrata trovato = null;
		if(modelCausali!=null){
			List<CausaleEntrata> listaCausali = modelCausali.getListaCausali();
			trovato = FinUtility.causaleByCodice(codice, listaCausali);
		}
		return trovato;
	}
	
	/**
	 * Si occupa di inizializzare la lista dei tipi causale entrata chiamando
	 * il relativo servizio di caricamento (solo se la lista e' vuota).
	 * 
	 * Viene invocato alla creazione della pagina.
	 * 
	 */
	protected void caricaTipiCausaleEntrata(CausaleEntrataTendinoModel modelCausali){
		if(modelCausali!=null && isEmpty(modelCausali.getListaTipiCausale())){
			//essendo vuota significa che siamo appena atterrati nella pagina e' dobbiamo ricaricare la lista:
			//carico la lista da servizio:
			List<TipoCausale> listaTipi = caricaListaTipiCausaleEntrata();
			//imposto il dato nel model:
			modelCausali.setListaTipiCausale(listaTipi);
		}
	}
	
}
