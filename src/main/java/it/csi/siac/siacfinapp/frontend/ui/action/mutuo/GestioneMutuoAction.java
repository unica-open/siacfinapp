/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.RicercaMutuoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneMutuoAction extends GenericMutuoAction {

	private static final long serialVersionUID = 1L;
	
	private static final String LABEL_AZIONE = "AZIONE";
	private String codiceMutuo, numeroMutuo;
	private String methodName;
	private BigDecimal totaleImpegnatoVociMutuo;
	
	public GestioneMutuoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.MUTUO;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Gestione Mutuo");
		
		//Carica la lista dei tipi provvedimento
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.TIPO_MUTUO);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));	
	   	model.setListaTipiMutuo((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_MUTUO));
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		//Se mi ritrovo in aggiornamento del mutuo
		if (codiceMutuo != null && !"".equals(codiceMutuo)) {
			model.setCodiceMutuoCorrente(codiceMutuo);
			RicercaMutuoPerChiave mutuoPerChiave = new RicercaMutuoPerChiave();
			mutuoPerChiave.setEnte(sessionHandler.getEnte());
			mutuoPerChiave.setRichiedente(sessionHandler.getRichiedente());
			RicercaMutuoK ricercaMutuoK = new RicercaMutuoK();
			ricercaMutuoK.setMutCode(codiceMutuo);
			mutuoPerChiave.setpRicercaMutuoK(ricercaMutuoK);
			mutuoPerChiave.setDataOra(new java.util.Date());
			RicercaMutuoPerChiaveResponse response = mutuoService.ricercaMutuoPerChiave(mutuoPerChiave);
			//Controllo che non ci siano stati errori dirante il richiamo al servizio
			if (response != null && !response.isFallimento() && response.getMutuo() != null) {
				//gestisco il provvedimento
				
				if (response.getMutuo().getAttoAmministrativoMutuo() != null) {
					//TRAVASIAMO I DATI DALL'OGGETTO ATTO AMMINISTRATIVO AL MODEL DELLA PAGINA
					impostaProvvNelModel(response.getMutuo().getAttoAmministrativoMutuo(), model.getProvvedimento());
					//MARCHIAMO CHE C'E' UN PROVVEDIMENTO SELEZIONATO
					model.setProvvedimentoSelezionato(true);
				}
				
				//gestisto il soggetto
				if (response.getMutuo().getSoggettoMutuo() != null) {
					model.getSoggetto().setCodCreditore(response.getMutuo().getSoggettoMutuo().getCodiceSoggetto());
					model.getSoggetto().setStato(response.getMutuo().getSoggettoMutuo().getStatoOperativo());
					model.getSoggetto().setDenominazione(response.getMutuo().getSoggettoMutuo().getDenominazione());
					model.getSoggetto().setCodfisc(response.getMutuo().getSoggettoMutuo().getCodiceFiscale());
					model.getSoggetto().setUid(response.getMutuo().getSoggettoMutuo().getUid());
					model.setSoggettoSelezionato(true);
				}
				if (response.getMutuo().getTipoMutuo() != null) {
					model.setTipoMutuo(response.getMutuo().getTipoMutuo().name());
				}
				model.setStato(response.getMutuo().getStato().name());
				if (response.getMutuo().getNumeroRegistrazioneMutuo() != null) {
					model.setNumeroRegistrazione(response.getMutuo().getNumeroRegistrazioneMutuo());
				}
				model.setDescrizione(response.getMutuo().getDescrizioneMutuo());
				model.setImporto(response.getMutuo().getImportoAttualeMutuo());
				model.setImportoFormattato(convertiBigDecimalToImporto(response.getMutuo().getImportoAttualeMutuo()));
				model.setDurata(BigInteger.valueOf(response.getMutuo().getDurataMutuo()));
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				model.setDataInizio(sdf.format(response.getMutuo().getDataInizioMutuo()));
				model.setDataFine(sdf.format(response.getMutuo().getDataFineMutuo()));
				model.setNote(response.getMutuo().getNoteMutuo());
				model.setDisponibilitaMutuo(response.getMutuo().getDisponibileMutuo());
				//Calcola Totale impegnato voci di mutuo
				totaleImpegnatoVociMutuo= new BigDecimal(0);
				//controllo se esistono delle voci di mutuo, correlate al mutuo padre
				if(null!=response.getMutuo().getListaVociMutuo()){
					for(VoceMutuo v: response.getMutuo().getListaVociMutuo()){
						//per ogni voce di mutuo aumento il totale delle voci, aggiungendo
						//l'importo della voce
						totaleImpegnatoVociMutuo = totaleImpegnatoVociMutuo.add(v.getImportoAttualeVoceMutuo());
					}
				}
				model.setImpegnatoVociMutuo(totaleImpegnatoVociMutuo);
				
				if (response.getMutuo().getStatoOperativoMutuo() != null) {
					model.setStato(response.getMutuo().getStatoOperativoMutuo().name());
				}
				model.setIdMutuo(response.getMutuo().getUid());
				model.setCodice(response.getMutuo().getCodiceMutuo());
				model.setListaVociMutuo(response.getMutuo().getListaVociMutuo());
			}
			model.getLabels().put(LABEL_AZIONE, "Aggiornamento ");
			model.setMutuoIniziale(clone(model));
		} else {
			//sono in inserimento del mutuo
			model.getLabels().put(LABEL_AZIONE, "Inserimento ");
			model.setMutuoIniziale(null);
		}
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		if(teSupport==null) pulisciTransazioneElementare();
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
	    return SUCCESS;
	}
	
	@Override
	public String getActionKey() {
		return "gestioneMutuo";
	}
	
	/**
	 * funzione usata in ajax per calcolare il range del mutuo
	 * @return
	 */
	public String calcolaDataFineMutuo() {
		//controllo eventuali null presenti nelle date
		if (model.getDurata() != null && model.getDataInizio() != null && !"".equalsIgnoreCase(model.getDataInizio())) {
			 try {
				 //calcolo la data fine del mutuo
				 SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATEPATTERN);
				 Date supportData = DateUtility.parse(model.getDataInizio());
				 Calendar c = new GregorianCalendar();
				 c.setTime(supportData);
				 c.add(Calendar.YEAR, model.getDurata().intValue());
				 model.setDataFine(sdf.format(c.getTime()));
			} catch (Exception e) {
				model.setDataFine(null);
			}
		} else {
			model.setDataFine(null);
		}
		return "dataFineMutuo";
	}
	
	public String salvaMutuo(){
		setMethodName("salvaMutuo");
		//controllo eventuali cambi obbligatori non istanziati
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(model.getProvvedimento().getAnnoProvvedimento() == null || model.getProvvedimento().getAnnoProvvedimento().intValue() == 0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		}
		if(model.getProvvedimento().getNumeroProvvedimento() == null ||  model.getProvvedimento().getNumeroProvvedimento().intValue() == 0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));		    	
		}
		if(model.getSoggetto().getCodCreditore() == null || "".equalsIgnoreCase(model.getSoggetto().getCodCreditore())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Istituto Mutuante"));		
		}
		if(model.getTipoMutuo() == null || "".equalsIgnoreCase(model.getTipoMutuo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Mutuo"));
		}
		if(model.getImportoFormattato() == null ||
		   model.getImportoFormattato().equals("0") ||
		   model.getImportoFormattato().equals("")){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Mutuo"));
		}else 
			try{
				model.setImporto(convertiImportoToBigDecimal(model.getImportoFormattato()));
				if(model.getImporto().compareTo(BigDecimal.ZERO)<= 0){
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Mutuo", "Positivo"));
				}
		} catch(Exception e) {
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo","Numerico"));
		}
		if(model.getDurata()== null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Durata Anni"));
		}
		if(!model.getDataInizio().isEmpty()){
		  if(StringUtils.isNotEmpty(model.getDataInizio())){
				if(model.getDataInizio().length()!=10){
					log.debug(methodName, "errore sulla lunghezza");
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Inizio Mutuo","dd/mm/yyyy"));
				}else {
					Date dataInizio = DateUtility.parse(model.getDataInizio());
					log.debug(methodName, "data inserita "+dataInizio);
					if(null==dataInizio){
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Inizio Mutuo","dd/mm/yyyy"));
					}
				}
			}
		}else{
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data Inizio Mutuo"));
		}
		
		//se non ho inserito anche solo un dato obbligatorio 
		//restituisco la lista degli errori
		if(!listaErrori.isEmpty()){	
			addErrori(listaErrori);
			return INPUT;
		}else{
			//controllo su provvedimento e soggetto
			if(controlloServiziSalvataggioMutuo(oggettoDaPopolare)){
				return INPUT;
			}
			//se mi trovo in aggiornamento del mutuo...
			if (model.getIdMutuo() != null && model.getIdMutuo() != 0) {
				AggiornaMutuo aggiornaMutuo = convertiModelCustomMutuoAggiornaRequest(model);
				AggiornaMutuoResponse response = mutuoService.aggiornaMutuo(aggiornaMutuo);
				//se il servizio ha restituito qualsiasi tipo di errore, stampo sulla pagina la lista
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					debug(methodName, "Model: " + model);
					return INPUT;
				}
			} else {
				//se mi trovo in inserimento del mutuo...
				InserisceMutuo inserisceMutuo = new InserisceMutuo();
				inserisceMutuo= convertiModelCustomMutuoRequest(model);
				InserisceMutuoResponse response = mutuoService.inserisceMutuo(inserisceMutuo);
				//se il servizio ha restituito qualsiasi tipo di errore, stampo sulla pagina la lista
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					debug(methodName, "Model: " + model);
					return INPUT;
				}
				setCodiceMutuo(response.getMutuo().getCodiceMutuo());
				setNumeroMutuo(null);
			}
		}
		clearActionData();
		//se  l'inserimento/aggiornamento e' andato a buon fine, stampo il messaggio di avvenuta operazione
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		return GOTO_ELENCO_MUTUI;
	}
	
	public boolean checkSoggettoStato(){
		//controllo lo stato del soggetto, sia in inserimento che in aggiornamento
		if(model.getIdMutuo()!= null){
			if(model.getSoggetto().getStato().equals(StatoOperativoAnagrafica.PROVVISORIO)){
				return false;
			}else{
				return true;
			}
		}else if(model.isSoggettoSelezionato()){
			return true;
		}else{
			return false;
		}
	}
	
	// utlizzato nella jsp
	public boolean checkStatoMutuoDefinitivo(){
		if(model.getIdMutuo()!= null && model.getMutuoIniziale().getStato().equals("DEFINITIVO")){
			
			return true;
			
		}else return false;
			
	}
	
	public boolean checkProvvedimentoStato(){
		if(model.getIdMutuo()!= null){
			if(model.getProvvedimento().getStato().equalsIgnoreCase(StatoOperativoAtti.PROVVISORIO.name())){
				return false;
			}else{
				return true;
			}
		}else if(model.isProvvedimentoSelezionato()){
			return true;
		}else{
			return false;
		}
	}
	
	public String annulla(){
		// alla pressione del tasto annulla, riporto alla verione originale la pagine
		//sia che io sia in aggiornamento che in inserimento
		if (model.getMutuoIniziale() != null) {
			model.setProvvedimento(model.getMutuoIniziale().getProvvedimento());
			model.setSoggetto(model.getMutuoIniziale().getSoggetto());
			model.setTipoMutuo(model.getMutuoIniziale().getTipoMutuo());
			model.setStato(model.getMutuoIniziale().getStato());
			model.setNumeroRegistrazione(model.getMutuoIniziale().getNumeroRegistrazione());
			model.setDescrizione(model.getMutuoIniziale().getDescrizione());
			model.setImporto(model.getMutuoIniziale().getImporto());
			model.setImportoFormattato(model.getMutuoIniziale().getImportoFormattato());
			model.setDurata(model.getMutuoIniziale().getDurata());
			model.setDataInizio(model.getMutuoIniziale().getDataInizio());
			model.setDataFine(model.getMutuoIniziale().getDataFine());
			model.setNote(model.getMutuoIniziale().getNote());
		} else {
			model.setProvvedimento(null);
			model.setProvvedimentoSelezionato(false);
			model.setSoggetto(null);
			model.setSoggettoSelezionato(false);
			model.setTipoMutuo(null);
			model.setStato(null);
			model.setNumeroRegistrazione(null);
			model.setDescrizione(null);
			model.setImporto(null);
			model.setImportoFormattato(null);
			model.setDurata(null);
			model.setDataInizio(null);
			model.setDataFine(null);
			model.setNote(null);
		}
		
		return SUCCESS;
	}
	// utilizzato nella jsp
	public boolean checkImportoEStatoMutuo(){
		if(model.getCodiceMutuoCorrente() != null){
			if(model.getStato().equalsIgnoreCase(Constanti.STATO_PROVVISORIO)){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ritorna true e quindi  deve far
	 * uscire l'asterisco
	 * 
	 * @return boolean
	 */
	public boolean soggettoObbligatorio(){
		return true;
	}
	
	@Override
	public String selezionaSoggetto() {
		return super.selezionaSoggettoControlloInModifica();
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getCodiceMutuo() {
		return codiceMutuo;
	}

	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public BigDecimal getTotaleImpegnatoVociMutuo() {
		return totaleImpegnatoVociMutuo;
	}

	public void setTotaleImpegnatoVociMutuo(BigDecimal totaleImpegnatoVociMutuo) {
		this.totaleImpegnatoVociMutuo = totaleImpegnatoVociMutuo;
	}

	public String getNumeroMutuo() {
		return numeroMutuo;
	}

	public void setNumeroMutuo(String numeroMutuo) {
		this.numeroMutuo = numeroMutuo;
	}
	
}