/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class NuovoAccertamentoOrdinativoAction extends ActionKeyGestioneOrdinativoIncassoAction{

	
	private static final long serialVersionUID = 1L;

	private static final String VAI_STEP2 = "vaiStep2";
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Inserimento accertamento ordinativo");
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		if (!presenzaQuote()) {
			pulisciTransazioneElementare();
		}
		// pulice i campi eventualmente sporchi da qualche azione precedente
		pulisciCampi();
		
		if (caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());

		caricaTE();

		return SUCCESS;
	}

	private void pulisciCampi() {
		model.getNuovoAccertamentoModel().setDescrizioneAccertamento(null);
		model.getNuovoAccertamentoModel().setImportoAccertamento(null);
	}

	private void caricaTE(){

		if (!presenzaQuote()) {

			CapitoloImpegnoModel supportCapitolo =  model.getGestioneOrdinativoStep1Model().getCapitolo(); 

			teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
			teSupport.getPianoDeiConti().setCodice(supportCapitolo.getCodicePdcFinanziario());
			teSupport.getPianoDeiConti().setDescrizione(supportCapitolo.getDescrizionePdcFinanziario());
			teSupport.getPianoDeiConti().setUid(supportCapitolo.getIdPianoDeiConti());
			teSupport.setIdMacroAggregato(supportCapitolo.getIdMacroAggregato());

			// codice transazione europea
			teSupport.setTransazioneEuropeaSelezionato(supportCapitolo.getCodiceTransazioneEuropeaEntrata());

			// siope
			if(supportCapitolo.getCodiceSiopeSpesa()!=null && !supportCapitolo.getCodiceSiopeSpesa().equals("")){
				SiopeSpesa siopeSpesa = new SiopeSpesa(supportCapitolo.getCodiceSiopeSpesa(), supportCapitolo.getDescrizioneSiopeSpesa());
				teSupport.setSiopeSpesa( siopeSpesa);
			}

			// ricorrente spesa
			teSupport.setRicorrenteEntrataSelezionato(supportCapitolo.getCodiceRicorrenteEntrata());

			// capitolo sanitario
			teSupport.setPerimetroSanitarioEntrataSelezionato(supportCapitolo.getCodicePerimetroSanitarioEntrata());

		}

	}
	
	/**
	 * controlla campi lanciata al click del salva nuovo accertamento
	 * @return
	 */
	private List<Errore>  controllaCampi(){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		if (FinStringUtils.isEmpty(model.getNuovoAccertamentoModel().getDescrizioneAccertamento())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Accertamento"));
		}

		if (FinStringUtils.isEmpty(model.getNuovoAccertamentoModel().getImportoAccertamento())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Accertamento"));
		}else {
		    if (controlloImporti(sessionHandler.getAnnoBilancio())) {
		    	addPersistentActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getCodice()+": "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore("").getDescrizione());
		    }
		}
		 
		// controllo sul V livello
		if(teSupport.getPianoDeiConti()!=null && teSupport.getPianoDeiConti().getCodice().endsWith(IV_LIVELLO)){
			listaErrori.add(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));			
		}
		
		//Controlli centralizzati:
		abilitazioneCampiTE(OggettoDaPopolareEnum.ACCERTAMENTO);
		
		return listaErrori;
	}

	private boolean controlloImporti(Integer annoImpegno) {
		//verifica degli importi
		boolean erroreImporti = false;
		Integer annoEsercio = sessionHandler.getAnnoBilancio();
		if (annoImpegno != null && annoImpegno.compareTo(annoEsercio + 2) <= 0) {

			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().size()!=0){

				// ricavo la mappa con gli anni/valori
				Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilita();

				// verifico le disponibilita
				BigDecimal bd = mappaAnniValori.get(annoImpegno);
				if (bd!=null && !FinStringUtils.isEmpty(model.getNuovoAccertamentoModel().getImportoAccertamento())){
					BigDecimal importoAccertamento=convertiImportoToBigDecimal(model.getNuovoAccertamentoModel().getImportoAccertamento());
					if(importoAccertamento.compareTo(bd) > 0){
						erroreImporti = true;
					}
				}
			}
		}

		return erroreImporti;
	}   

	/**
	 * utility che ritorna gli anni e le disponibilita correttamente ordinati
	 * @return
	 */
	private Map<Integer, BigDecimal> ritornaMappaAnniDisponibilita(){
		
		Map<Integer, BigDecimal> mappa = new HashMap<Integer, BigDecimal>();
		

    		for(int i =0;i<3;i++){
	    		if(i==0){
	    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
	    		}
	    		if(i==1){
	    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
	
	    		}
	    		
	    		if(i==2){
	    			mappa.put(model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getGestioneOrdinativoStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
	    		}
			}
		
		return mappa;
	}
	/**
	 * salvataggio di un nuovo accertamento
	 */
	public String salva() {
		
		// controlla campi
		List<Errore> listaErrori = controllaCampi();
		
		if(listaErrori!=null && listaErrori.size()>0){
			addErrori(listaErrori);
			return INPUT;
		}
		
		// inserimento dell' accertamento
		InserisceAccertamenti request = new InserisceAccertamenti();
		
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
	    
		//BILANCIO
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(sessionHandler.getAnnoBilancio());
		request.setBilancio(bilancio);
		
		Accertamento accertamento = popolaAccertamento();
		
		request.setPrimoAccertamentoDaInserire(accertamento);

		InserisceAccertamentiResponse resp = movimentoGestioneFinService.inserisceAccertamenti(request);
		
		if(resp!=null && resp.isFallimento()){
			addErrori(resp.getErrori());
			return INPUT;
		}
		
		model.getNuovoAccertamentoModel().setNumeroAccertamento(resp.getElencoAccertamentiInseriti().get(0).getNumeroBigDecimal());
		model.getNuovoAccertamentoModel().setAnnoAccertamento(resp.getElencoAccertamentiInseriti().get(0).getAnnoMovimento());

		RicercaAccertamentoPerChiaveOttimizzatoResponse responsePK = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(convertiModelAccertamentoPerChiamataServizioRicercaPerChiave());
		
		
		if(responsePK!=null && responsePK.isFallimento()){
			addErrori(responsePK.getErrori());
			return INPUT;
		}
		
		model.getGestioneOrdinativoStep2Model().getListaAccertamento().add(responsePK.getAccertamento());

		
		return VAI_STEP2;
	}
	
	protected Accertamento popolaAccertamento() {
		Accertamento accertamento = new Accertamento();
		
		//Capitolo
		if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
			accertamento.setCapitoloEntrataGestione(convertiCapitoloCustomToCapitoloEntrata(model.getGestioneOrdinativoStep1Model().getCapitolo()));
			accertamento.setAnnoCapitoloOrigine(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno());
		}
		
		//Stato Operativo
		accertamento.setStatoOperativoMovimentoGestioneEntrata(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		
		accertamento.setAnnoMovimento(sessionHandler.getAnnoBilancio());
		
		accertamento.setDescrizione(model.getNuovoAccertamentoModel().getDescrizioneAccertamento());

		// popolo tutti e 2 i valori
		accertamento.setImportoIniziale(convertiImportoToBigDecimal(model.getNuovoAccertamentoModel().getImportoAccertamento()));
		accertamento.setImportoAttuale(convertiImportoToBigDecimal(model.getNuovoAccertamentoModel().getImportoAccertamento()));
		
		// albero PDC
		accertamento.setCodPdc(teSupport.getPianoDeiConti().getCodice());
		accertamento.setCodSiope(teSupport.getSiopeSpesaCod());

		accertamento.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
		accertamento.setCodRicorrenteSpesa(teSupport.getRicorrenteEntrataSelezionato());
		accertamento.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioEntrataSelezionato());
		accertamento.setCodPrgPolReg(teSupport.getPoliticaRegionaleSelezionato());
		
		//Provvedimento
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento() != null) {
			accertamento.setAttoAmministrativo(popolaProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento()));
		}
		
		//Soggetto
		Soggetto soggetto = convertiSoggettoCustomToSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto());
		accertamento.setSoggetto(soggetto);
		
		accertamento.setAutomatico(true);
		
		return accertamento;
	}

	/**
	 * convertitore utilizzato per la chiamata a ricerca accertamento per chiave
	 * @return
	 */
	protected RicercaAccertamentoPerChiaveOttimizzato convertiModelAccertamentoPerChiamataServizioRicercaPerChiave() {
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaAccertamentoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoK accertamentoDaCercare = new RicercaAccertamentoK();
		BigDecimal numeroImpegno = model.getNuovoAccertamentoModel().getNumeroAccertamento();
		Ente enteProva = new Ente();
		
		accertamentoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		accertamentoDaCercare.setNumeroAccertamento(numeroImpegno);
		accertamentoDaCercare.setAnnoAccertamento(model.getNuovoAccertamentoModel().getAnnoAccertamento());
		enteProva = model.getEnte();
				
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(enteProva);
		parametroRicercaPerChiave.setpRicercaAccertamentoK(accertamentoDaCercare);
			
		return parametroRicercaPerChiave;
	}
	
	// utilizzato nella jsp
	public boolean presenzaQuote() {
		if (model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso() != null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size() > 0) {
			return true;
		}
		return false;
	}
	
	public String indietroDaAccertamento(){
		
		// se premo tasto indietro e non ci sono delle quote devo ripulire la TE
		
		if(!presenzaQuote()){
			
			setTeSupport(new GestoreTransazioneElementareModel());
		}
		
		
		return "vaiStep2";
	}

	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */

	@Override
	public String confermaPdc() {
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		
		// pulisce gli alberi in caso di cambiamento del pdc
		teSupport.setSiopeSpesa(new SiopeSpesa());
		
		// CONTROLLO SUI QUARTI LIVELLI
		
		if(null!=teSupport.getPianoDeiConti() && teSupport.getPianoDeiConti().getCodice().endsWith("000")){
			teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			
		}
		return SUCCESS;
	}

	@Override
	public String confermaSiope() {
		return SUCCESS;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		 return false;
	}

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
	public boolean transazioneElementareAttiva() {
		return true;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

}
