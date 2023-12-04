/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaProvvisorioCassaAction extends WizardRicercaProvvisorioAction {

	private static final long serialVersionUID = 1L;
		
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Ricerca provvisori di cassa");
		
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CONTO_TESORERIA);

		// SIAC-6353
		@SuppressWarnings("unchecked")
		List<CodificaFin> listaContoTesoreria = (List<CodificaFin>) mappaListe.get(TipiLista.CONTO_TESORERIA);
		model.setListaContoTesoriere(listaContoTesoreria != null	? listaContoTesoreria : new ArrayList<CodificaFin>());
		
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_LEGGIPROVVCASSA)){
			//errore per utenza non abilitata
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
		}
		
		resetPageNumberTableId("ricercaProvvisorioCassaID");
		
		//Pulisco i campi 
	   	pulisciCampi();
	   	
	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
	   		//setto l'anno provvisorio uguale all'anno di esercizio come default
			model.setAnnoProvvisorio(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   		   	
	   	return SUCCESS;
	}
	
	public void primaDiRicercaProvvisorioDiCassa(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String struttSel = request.getParameter("struttAmmSelezionata");
		model.setStrutturaSelezionataSuPagina(struttSel);
	}
	
	public String ricercaProvvisorioCassa(){
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean noInputData = true;

		if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_LEGGIPROVVCASSA)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		
		//Condizioni ricerca - Provvisorio
		if(StringUtils.isEmpty(model.getTipoDocumentoProv())){	
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento obbligatorio"));
		}
		
		//numero provvisorio
		if(model.getNumeroProvvisorio() != null){
			noInputData=false;
		}
		
		//anno provvisorio da
		if(model.getAnnoProvvisorioDa() != null){
			noInputData=false;
		}	
		
		//anno provvisorio a
		if(model.getAnnoProvvisorioA() != null){
			noInputData=false;
		}
		
		//numero provvisorio da
		if(model.getNumeroProvvisorioDa() != null){
			noInputData=false;
		}
		
		//numero provvisorio a
		if(model.getNumeroProvvisorioA() != null){
			noInputData=false;
		}
		
		//descrizione causale
		if(StringUtils.isNotEmpty(model.getDescCausale())){
			noInputData=false;
		}
		
		//denominazione soggetto
		if(StringUtils.isNotEmpty(model.getDenominazioneSoggetto())){
			noInputData=false;
		}
		
		//sub causale
		if(StringUtils.isNotEmpty(model.getSubCausale())){
			noInputData=false;
		}
		
		//conto tesor
		if(StringUtils.isNotEmpty(model.getContoTesoriere())){
			noInputData=false;
		}
		
		//data inizio emissione
		if(StringUtils.isNotEmpty(model.getDataInizioEmissione())){
			noInputData=false;
		}
		
		//data fine emissione
		if(StringUtils.isNotEmpty(model.getDataFineEmissione())){
			noInputData=false;
		}
		
		//data inizio trasmissione
		if(StringUtils.isNotEmpty(model.getDataInizioTrasmissione())){
			noInputData=false;
		}
		
		//data fine trasmissione
		if(StringUtils.isNotEmpty(model.getDataFineTrasmissione())){
			noInputData=false;
		}
		
		if(StringUtils.isNotEmpty(model.getDataInizioInvioServizio())){
			noInputData=false;
		}
		if(StringUtils.isNotEmpty(model.getDataFineInvioServizio())){
			noInputData=false;
		}
		
		if(StringUtils.isNotEmpty(model.getDataInizioPresaInCaricoServizio())){
			noInputData=false;
		}
		if(StringUtils.isNotEmpty(model.getDataFinePresaInCaricoServizio())){
			noInputData=false;
		}
		
		if(StringUtils.isNotEmpty(model.getDataInizioRifiutoErrataAttribuzione())){
			noInputData=false;
		}
		if(StringUtils.isNotEmpty(model.getDataFineRifiutoErrataAttribuzione())){
			noInputData=false;
		}
		
		//importo da
		if(model.getImportoDa() != null){
			noInputData=false;
		}
		
		//importo a
		if(model.getImportoA() != null){
			noInputData=false;
		}
		
		//struttura selezionata su pagina
		if(model.getStrutturaSelezionataSuPagina() != null){
			noInputData=false;
		}
		
		
		//Condizioni ricerca - Da-A importo
		if (model.getImportoDa()!=null && model.getImportoA()!=null) {
			if (model.getImportoDa().compareTo(model.getImportoA())==1) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo Da/Importo A","(Importo Da deve essere minore di Importo A)"));
			}
		}

		//Condizioni ricerca - Da-A numOrd
		if (model.getNumeroProvvisorioDa()!=null && model.getNumeroProvvisorioA()!=null) {
			if (model.getNumeroProvvisorioDa().compareTo(model.getNumeroProvvisorioA())==1) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Numero Provvisorio Da/Numero Provvisorio A","(Numero Provvisorio Da deve essere minore di Numero Provvisorio A)"));
			}
		}
		
		//Condizioni ricerca - Da-A dataOrd
		
		if (!StringUtils.isEmpty(model.getDataInizioEmissione()) || !StringUtils.isEmpty(model.getDataFineEmissione())) {
			noInputData=false;
			checkDateInizioFine(model.getDataInizioEmissione(), model.getDataFineEmissione(), "emissione", listaErrori);
		}
		
		if (!StringUtils.isBlank(model.getDataInizioTrasmissione()) || !StringUtils.isBlank(model.getDataFineTrasmissione())) {
			noInputData=false;
			checkDateInizioFine(model.getDataInizioTrasmissione(), model.getDataFineTrasmissione(), "trasmissione", listaErrori);
		}
		
		if (!StringUtils.isBlank(model.getDataInizioInvioServizio()) || !StringUtils.isBlank(model.getDataFineInvioServizio())) {
			noInputData=false;
			checkDateInizioFine(model.getDataInizioInvioServizio(), model.getDataFineInvioServizio(), "invio servizio", listaErrori);
		}
		
		if (!StringUtils.isBlank(model.getDataInizioRifiutoErrataAttribuzione()) || !StringUtils.isBlank(model.getDataFineRifiutoErrataAttribuzione())) {
			noInputData=false;
			checkDateInizioFine(model.getDataInizioRifiutoErrataAttribuzione(), model.getDataFineRifiutoErrataAttribuzione(), "rifiuto per errata attribuzione", listaErrori);
		}
		
		if(model.getFlagAnnullatoProv()!=null){
			noInputData=false;
		}
		// se c'e' almeno un true vuol dire che c'e' errore
		if (noInputData) {
			listaErrori.add(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}

		//VALIDO
		if(listaErrori.isEmpty()){			

			return "gotoElencoProvvisorioCassa";
		} else {
			addErrori(listaErrori);
			return INPUT;
		}		
	}

	private void checkDateInizioFine(String dataInizio, String dataFine, String tipo, List<Errore> listaErrori) {
		//Condizioni ricerca - Da-A dataOrd
		DateFormat df=null;
		Date parsedTimeDa=null;
		Date parsedTimeA=null;
		// verifica corretta forma della data
		if (!StringUtils.isEmpty(dataInizio)) {
			if (dataInizio.length() == 8) {
				df = new SimpleDateFormat("dd/MM/yy");
			} else {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}
			
			try {
				parsedTimeDa=df.parse(dataInizio);
			} catch (ParseException e) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data inizio " + tipo,""));				}
		}

		// verifica della data fine se presente
		if (!StringUtils.isEmpty(dataFine)) {
			if (dataFine.length() == 8) {
				df = new SimpleDateFormat("dd/MM/yy");
			} else {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}
			
			try {
				parsedTimeA=df.parse(dataFine);
			} catch (ParseException e) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data fine " + tipo,""));				
			}
		}

		if (parsedTimeDa==null && parsedTimeA!=null) {
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore(String.format("Data %s", tipo), "(non e' possibile inserire Data fine senza Data inizio)"));
		}

		if (parsedTimeDa!=null && parsedTimeA!=null && parsedTimeDa.after(parsedTimeA)) {
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore(String.format("Data %s", tipo), "(Data inizio deve essere minore di Data fine)"));
		}			
	}
	
	
	/**
	 * inserisce il valore di default nella chekboxList della pagina
	 * @return
	 */
	public String[] getDefaultValueAnnulla(){
		return new String [] {"no"};
	}

	
}
