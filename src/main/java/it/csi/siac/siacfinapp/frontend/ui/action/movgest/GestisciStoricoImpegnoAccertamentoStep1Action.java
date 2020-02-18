/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciStoricoImpegnoAccertamentoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * The Class GestisciStoricoImpegnoAccertamentoStep1Action.
 * @author elisa
 * @version 1.0.0 - 09-07-2019
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestisciStoricoImpegnoAccertamentoStep1Action extends GenericPopupAction<GestisciStoricoImpegnoAccertamentoModel> {

	private static final long serialVersionUID = 1L;
	private static final String PROSEGUI = "prosegui";
	private static final String ANNULLA = "annulla";	
	
	@Autowired
	private MovimentoGestioneService movimentoGestionService;
	
	public GestisciStoricoImpegnoAccertamentoStep1Action() {
	   	
	}

	@Override
	public String getActionKey() {
		return "storicoImpegnoAccertamento";
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Getione storico - Ricerca Impegno");
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {	
		//controlli di abilitazioni preliminari:
		isFunzionalitaAbilitata();
		return "success";
	}
	
	/**
	 * esegue la validazioni iniziali per il caso d'uso
	 * @return
	 */
	private boolean isFunzionalitaAbilitata(){
		
		//fase bilancio abilitata:
		if(!isFaseBilancioAbilitata()){
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("inserisci liquidazione", sessionHandler.getFaseBilancio()));
			model.setRicercaImpegnoStep1Abilitata(false);
			return false;
		}	
		
		return true;
	}

	/**
	 * Gestione tasto prosegui
	 * @return
	 */
	public String prosegui() {
		//chiamo il metodo core di validazione dello step:
		return validateStep();
	}

	/**
	 * funzione di annulla valori nei campi.
	 *
	 * @return il risultato dell'invocazione
	 */
	public String annulla() {
		//richiamo il metodo che pulisce il model:
		pulisciCampiImpegno();
		return ANNULLA;
	}
	
	/**
	 * Viene pulito il moel relativo alla modale di ricerca dell'impegno.
	 *
	 * @return il risultato dell'invocazione
	 */
	public String azzeraModaleImpegno(){
		pulisciListeeSchedaPopup();		
		return "refreshPopupModalImpegno";
	}

	/**
	 * Effettua i controlli per validare i dati immessi prima
	 * di consentire il passaggio allo step successivo.
	 *
	 * @return il risultato dell'invocazione
	 */
	private String validateStep() {
		model.setInPopup(false);
		
		//RICHIAMO I CONTROLLI INIZIALI PER SICUREZZA:
		if (!isFunzionalitaAbilitata()) {
			pulisciCampiImpegno();
			return INPUT;
		}
		//END CONTROLLI INIZIALI
		
		if (!isAnnoENumeroCorretamenteCompilati() || !isImpegnoSubImpegnoValido()) {
			//ANNO E NUMERO non compilati
			return INPUT;
		}
		
		//fix per anomalia: SIAC-2257 (veniva perso il teSupport)
		model.setHasImpegnoSelezionatoPopup(true);
		model.setHasImpegnoSelezionatoXPopup(true);
		return PROSEGUI;
	}
	
	/**
	 * Metodo che controlla la validita dell'impegno indicato andando ad effettuare la chiamata al
	 * servizio ricerca impegno per chiave.
	 *
	 * @return true, if is impegno sub impegno valido
	 */
	private boolean isImpegnoSubImpegnoValido(){
		
		//FLAG IMPEGNO APPENA SELEZIONATO:
		model.setImpegnoAppenaSelezionatoDaCompGuidata(false);
		
		//CHIAMIAMO IL SERVIZIO DI RICERCA IMPEGNO
		boolean indicatoSub = isCompilatoNumeroSubImpegno();
		//(specificando se e' stato compilato o meno il sub, in tal caso cambia la request da comporre)
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = ricercaImpegno(indicatoSub);
		
		if (respRk == null || respRk.getImpegno() == null) {
			addErrore(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));
			return false;
		}
			
		model.setImpegno(respRk.getImpegno());
		
			//determino i sub impegni di riferimento:
		List<SubImpegno> elencoSubImpegni = elencoSubDiRiferimento(indicatoSub, respRk);
		boolean impegnoConSub = !isEmpty(elencoSubImpegni); 

		if(indicatoSub) {
			SubImpegno subCorrispondente = FinUtility.findSubImpegnoByNumero(elencoSubImpegni, model.getNumeroSub());
			if(subCorrispondente == null) {
				addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
				return false;
			}
			model.setSubImpegno(subCorrispondente);
		}
		
		
		if(impegnoConSub && indicatoSub) {
			SubImpegno subCorrispondente = FinUtility.findSubImpegnoByNumero(elencoSubImpegni, model.getNumeroSub());
			if(subCorrispondente == null) {
				addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
				return false;
			}
			model.setSubImpegno(subCorrispondente);
		}
		
		return true;
			
	}
			
	
	/**
	 * Semplice metodo che controlla che siano stati compilati anno e numero.
	 *
	 * @return true, if is anno E numero corretamente compilati
	 */
	private boolean isAnnoENumeroCorretamenteCompilati(){
		boolean tuttoOk = true;
		List<Errore> listaErrori = new ArrayList<Errore>();	
		//ANNO
		if (this.model.getAnnoImpegno() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Anno "));
		}
		//NUMERO
		if (this.model.getNumeroImpegno() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Numero "));
		}
		//AGGIUNGO GLI EVENTUALI ERRORI:
		if (!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			tuttoOk = false;
		}
		return tuttoOk;
	}
	
	/**
	 * Ritorna true se model.getNumeroImpegno() diverso da null ed il suo numero
	 * e' diverso da model.getNumeroImpegno()
	 *
	 * @return true, if successful
	 */
	private boolean impegnoValorizzatoENumeroCambiato(){
		boolean impegnoValorizzatoENumeroCambiato = false;
		if(model.getImpegno()!=null && this.model.getNumeroImpegno()!=null){
			//impegno diverso da null e numero impegno diverso da null
			BigDecimal numeroBD = new BigDecimal(this.model.getNumeroImpegno());
			impegnoValorizzatoENumeroCambiato = !(numeroBD).equals(model.getImpegno().getNumero());
		}
		return impegnoValorizzatoENumeroCambiato;
	}
	
	
		/**
		 * pulisci i campi della parte relativa ad impegno.
		 */
	private void pulisciCampiImpegno() {
		model.setAnnoImpegno(null);
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		model.setNumeroMutuoPopup(null);
		model.setDescrizioneImpegnoPopup(null);
		model.setDescrizioneMutuoPopup(null);
		model.setImpegnoPopup(new Impegno());
		model.setHasImpegnoSelezionatoPopup(false);
		model.setDisponibilita(null);
	}

	/**
	 * Gestisce la chiamata al servizio di ricerca impegno.
	 *
	 * @param indicatoSub the indicato sub
	 * @return the ricerca impegno per chiave ottimizzato response
	 */
 	private RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegno(boolean indicatoSub){
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
	 * Per verificare se l'utente ha compilato o meno il campo del sub impegno.
	 *
	 * @return true, if is compilato numero sub impegno
	 */
	private boolean isCompilatoNumeroSubImpegno(){
		return model.getNumeroSub()!=null && model.getNumeroSub().intValue()>0; 
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
	private List<SubImpegno> elencoSubDiRiferimento(boolean indicatoSub, RicercaImpegnoPerChiaveOttimizzatoResponse respRk){
		if(respRk==null){
			return null;
		}
		Impegno impegno = respRk.getImpegno();
		if(indicatoSub && impegno!=null){
			//Se ho indicato un sub avro' l'elenco COMPLETO DI TUTTE LE INFO con il SOLO sub Esplicitamente RICHIESTO
			return impegno.getElencoSubImpegni();
		}
			//Se NON ho indicato un sub avro' solo l'elenco con LE INFO MINIME per tutti i sub
			//ma non e' un problema perche' tanto verra' lanciato l'errore che se NON INDICO UN SUB e CI SONO DEI SUB 
			//DOVEVA essere obbligatorio indicarne uno
		return respRk.getElencoSubImpegniTuttiConSoloGliIds();
	}
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return true;
	}

	@Override
	public boolean cofogAttivo() {
		return true;
	}

	@Override
	public boolean cupAttivo() {
		return true;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return true;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return SUCCESS;
	}


	@Override
	public String confermaSiope() {
		return SUCCESS;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return true;
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

	@Override
	public String listaClasseSoggettoChanged() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setErroreCapitolo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		// TODO Auto-generated method stub
		
	}
}
