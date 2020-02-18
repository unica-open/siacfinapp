/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.GestioneMutuoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinser.frontend.webservice.MutuoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuo;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.StatoOperativoMutuo;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.TipoMutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GenericMutuoAction extends GenericPopupAction<GestioneMutuoModel> {

	private static final long serialVersionUID = 1L;
	
	protected final static String GOTO_ELENCO_MUTUI = "gotoElencoMutui";
	
	@Autowired
	protected transient MutuoService mutuoService;

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {}
	
	@Override
	protected void setErroreCapitolo() {}

	/**
	 * Gestise la selezione della classe soggetto
	 */
	@Override
	public String listaClasseSoggettoChanged() {
		//puliamo i dati del soggetto:
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}

	/**
	 * Setta nel model il soggetto selezionato
	 */
	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);
	}

	/**+
	 * Setta nel model il provvedimento selezionato
	 */
	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {		
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
		model.setStato(currentProvvedimento.getStato());
	}
	
	/**
	 *  utilizzato per la chiamata della funzione di inserimento
	 * @param datiMutuo
	 * @return
	 */
	protected InserisceMutuo convertiModelCustomMutuoRequest(GestioneMutuoModel datiMutuo){
		
		//istanzio la request:
		InserisceMutuo support = new InserisceMutuo();
		
		//setto l'ente:
		support.setEnte(sessionHandler.getEnte());
		
		//setto il richiedente:
		support.setRichiedente(sessionHandler.getRichiedente());
		
		//predispongo un oggetto mutuo che verra popolato a partire dal model 
		//e poi inserito nella request:
		Mutuo mutuo = new Mutuo();
		
        // provvedimento
		if(datiMutuo.getProvvedimento() != null && datiMutuo.getProvvedimento().getAnnoProvvedimento() != null){
			//se c'e un provvedimento in dati mutuo:
			AttoAmministrativo am = popolaProvvedimento(datiMutuo.getProvvedimento());
			mutuo.setAttoAmministrativoMutuo(am);
		}
		
		//stato operativo del mutuo:
		mutuo.setStatoOperativoMutuo(StatoOperativoMutuo.valueOf(datiMutuo.getProvvedimento().getStato()));
		
        // SOGGETTO
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(datiMutuo.getSoggetto().getCodCreditore());
		soggetto.setStatoOperativo(datiMutuo.getSoggetto().getStato());
		datiMutuo.getSoggetto().getClasse();
		mutuo.setSoggettoMutuo(soggetto);

		
		//TIPO MUTUO da decommentare quando saranno rfiempiti le tabelle del tipo mutuo su DB
		mutuo.setTipoMutuo(TipoMutuo.valueOf(datiMutuo.getTipoMutuo()));

		//numero registrazione:
		if(datiMutuo.getNumeroRegistrazione() != null){
			mutuo.setNumeroRegistrazioneMutuo(datiMutuo.getNumeroRegistrazione());
		}
		
		//descrizione:
		if(!FinStringUtils.isEmpty(datiMutuo.getDescrizione())){
			mutuo.setDescrizioneMutuo(datiMutuo.getDescrizione());
		}
		
		//IMPORTO ATTUALE
		mutuo.setImportoAttualeMutuo(datiMutuo.getImporto());
		
		//IMPORTO INIZIALE
		mutuo.setImportoInizialeMutuo(datiMutuo.getImporto());
		
		//DURATA
		mutuo.setDurataMutuo(datiMutuo.getDurata().intValue());
		
		//DATA INIZIO
		mutuo.setDataInizioMutuo(DateUtility.parse(datiMutuo.getDataInizio()));
		
		//DATA FINE
		mutuo.setDataFineMutuo(DateUtility.parse(datiMutuo.getDataFine()));
			
		//NOTE
		if(!FinStringUtils.isEmpty(datiMutuo.getNote())){
			mutuo.setNoteMutuo(datiMutuo.getNote());
		}
		
		//setto il mutuo nella request:
		support.setMutuo(mutuo);
		
		//ritorno la request al chiamante:
		return support;
	}
	
	/**
	 *  utilizzato per la chiamata della funzione di aggiornamento
	 * @param datiMutuo
	 * @return
	 */
	protected AggiornaMutuo convertiModelCustomMutuoAggiornaRequest(GestioneMutuoModel datiMutuo) {
		
		//istanzio la request:
		AggiornaMutuo support = new AggiornaMutuo();
		
		//setto l'ente:
		support.setEnte(sessionHandler.getEnte());
		
		//setto il richiedente:
		support.setRichiedente(sessionHandler.getRichiedente());
		
		//predispongo un oggetto mutuo che verra popolato a partire dal model 
		//e poi inserito nella request:
		Mutuo mutuo = new Mutuo();
		
        // provvedimento
		if(datiMutuo.getProvvedimento()!=null && datiMutuo.getProvvedimento().getAnnoProvvedimento()!=null){
			AttoAmministrativo am = popolaProvvedimento(datiMutuo.getProvvedimento());
			mutuo.setAttoAmministrativoMutuo(am);
		}
		
		//stato operativo
		mutuo.setStatoOperativoMutuo(StatoOperativoMutuo.valueOf(datiMutuo.getStato()));
		
        // SOGGETTO
		if (datiMutuo.getSoggetto() != null) {
			Soggetto soggetto = new Soggetto();
			soggetto.setCodiceSoggetto(datiMutuo.getSoggetto().getCodCreditore());
			soggetto.setStatoOperativo(datiMutuo.getSoggetto().getStato());
			soggetto.setUid(datiMutuo.getSoggetto().getUid());
			mutuo.setSoggettoMutuo(soggetto);
		}
		
		//tipo mutuo
		mutuo.setTipoMutuo(TipoMutuo.valueOf(datiMutuo.getTipoMutuo()));
		
		//numero registrazione
		if(datiMutuo.getNumeroRegistrazione() != null){
			mutuo.setNumeroRegistrazioneMutuo(datiMutuo.getNumeroRegistrazione());
		}
		
		//descrizione
		if(!FinStringUtils.isEmpty(datiMutuo.getDescrizione())){
			mutuo.setDescrizioneMutuo(datiMutuo.getDescrizione());
		}
		
		//importo attuale
		mutuo.setImportoAttualeMutuo(datiMutuo.getImporto());
		
		//importo iniziale
		mutuo.setImportoInizialeMutuo(datiMutuo.getImporto());
		
		//durata mutuo
		mutuo.setDurataMutuo(datiMutuo.getDurata().intValue());
		
		//data inizio
		mutuo.setDataInizioMutuo(DateUtility.parse(datiMutuo.getDataInizio()));
		
		//data fine
		mutuo.setDataFineMutuo(DateUtility.parse(datiMutuo.getDataFine()));
			
		//note
		if(!FinStringUtils.isEmpty(datiMutuo.getNote())){
			mutuo.setNoteMutuo(datiMutuo.getNote());
		}
		
		//lista voci mutuo
		mutuo.setListaVociMutuo(datiMutuo.getListaVociMutuo());
		
		//codice mutuo
		mutuo.setCodiceMutuo(datiMutuo.getCodice());
		
		//disponibile mutuo
		mutuo.setDisponibileMutuo(datiMutuo.getDisponibilitaMutuo());
		
		//setto il mutuo nella request:
		support.setMutuo(mutuo);
		
		//ritorno la request al chiamante:
		return support;
	}
	
	/**
	 * controlli precedenti al salvataggio
	 * @param oggettoDaPopolare
	 * @return
	 */
	protected boolean controlloServiziSalvataggioMutuo(OggettoDaPopolareEnum oggettoDaPopolare){
		boolean errori= false;
        // controllo provvedimento
		if (
				(null!=model.getProvvedimento().getAnnoProvvedimento() ||
				null!=model.getProvvedimento().getCodiceTipoProvvedimento() || 
				null!=model.getProvvedimento().getNumeroProvvedimento())){
			 if(null!=model.getProvvedimento().getIdTipoProvvedimento()){
				 model.getProvvedimento().setTipoProvvedimento(String.valueOf(model.getProvvedimento().getIdTipoProvvedimento()));
			 } 
			if (!eseguiRicercaProvvedimento(oggettoDaPopolare, model.getProvvedimento(), false)){
				errori = true;
			}
		}
		//controllo su soggetto
		if ( model.getSoggetto().getCodCreditore() != null && !"".equals(model.getSoggetto().getCodCreditore())) {
			if (!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getSoggetto()), false, oggettoDaPopolare)) {
				errori = true;
			}
		}
		return errori;
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
	public boolean transazioneElementareAttiva() {
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return null;
	}


	@Override
	public String confermaSiope() {
		return null;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		// Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
	}
	
}
