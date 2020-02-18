/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.ConsultaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaSoggettoAction extends SoggettoAction<ConsultaSoggettoModel>{

	private static final long serialVersionUID = 2652920214934382402L;

	//codice soggetto
	private String codiceSoggetto;
	
	//data propost modifica
	public Date dataPropostaModifica;
	
	//utente proposta modifica
	public String utentePropostaModifica;
	
	//in modifica
	public boolean isInModifica;

	//uid sede secondaria
	private Integer uidSedeSecondaria;
	
	//sede selezionata
	private SedeSecondariaSoggetto selectedSede;

	//mappa contatti:
	private Map<String, Contatto> contattiMap = new HashMap<String, Contatto>();

	@Override
	public void prepare() throws Exception{
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//compongo la requeste per la chiamata al servizio di ricerca:
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = convertiModelPerChiamataServizioRicercaPerChiavePerConsulta(getCodiceSoggetto());

		//setto la codifica dell'ambito
		ricercaSoggettoPerChiave.setCodificaAmbito(getCodificaAmbito());

		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(ricercaSoggettoPerChiave);
		// Salvo il dettaglio del soggetto selezionato e le relative sedi e
		// modalita' di pagamento associate
		boolean gestSoggDec = FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(),ABILITAZIONE_GESTIONE_DECENTRATO);
		
		//analizzo la response in due modi diversi a seconda se sono decentrato o meno:
		
		if (gestSoggDec) {
			
			//gestione decentrata
			
			if (response.getSoggettoInModifica() != null) {
				dataPropostaModifica = response.getSoggettoInModifica().getDataCreazione();
				utentePropostaModifica = response.getSoggettoInModifica().getLoginOperazione().toString();
				isInModifica = true;
				model.setDettaglioSoggetto(response.getSoggettoInModifica());
				model.getDettaglioSoggetto().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
				model.getDettaglioSoggetto().setLoginModifica(response.getSoggettoInModifica().getLoginOperazione());
				model.getDettaglioSoggetto().setDataStato(response.getSoggetto().getDataStato());
				model.getDettaglioSoggetto().setDataCreazione(response.getSoggetto().getDataCreazione());
				model.getDettaglioSoggetto().setDataModifica(response.getSoggetto().getDataModifica());
				model.getDettaglioSoggetto().setLoginModifica(response.getSoggetto().getLoginModifica());
			} else {
				model.setDettaglioSoggetto(response.getSoggetto());
			}
		} else {
			
			//gestione non decentrata
			
			if (response.getSoggettoInModifica() != null) {
				dataPropostaModifica = response.getSoggettoInModifica().getDataCreazione();
				utentePropostaModifica = response.getSoggettoInModifica().getLoginOperazione().toString();
				isInModifica = true;
				model.setDettaglioSoggetto(response.getSoggetto());
				model.getDettaglioSoggetto().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
				model.getDettaglioSoggetto().setDataCreazione(response.getSoggetto().getDataCreazione());
				model.getDettaglioSoggetto().setDataModifica(response.getSoggetto().getDataModifica());
				model.getDettaglioSoggetto().setLoginModifica(response.getSoggetto().getLoginModifica());
			} else {
				model.setDettaglioSoggetto(response.getSoggetto());
				if (model.getDettaglioSoggetto().getIndirizzi() != null){
					for (IndirizzoSoggetto i : model.getDettaglioSoggetto().getIndirizzi()){
						if (i.getNumeroCivico() == null) {
							i.setNumeroCivico("");
						}
					}	
				}
			}
		}

		//gestisce le province:
		cercaProvinceIndirizziSoggetto();

		// JIRA 1074

		//ciclo le sedi secondarie e carico i dati completi per ognuna di esse:
		if (response.getListaSecondariaSoggetto() != null && response.getListaSecondariaSoggetto().size() > 0){
			for (SedeSecondariaSoggetto newSede : response.getListaSecondariaSoggetto()){
				//ricerco la sede iterata:
				RicercaSedeSecondariaPerChiaveResponse resp2 = ricercaSedeSecondariaSoggetto(newSede, true);
				//analizzo il dato ottenuto e setto i dati:
				if (resp2.getSedeSecondariaSoggetto() != null){
					newSede.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_IN_MODIFICA_no_underscore);
				}
			}
		}
		
		//ciclo le modalita di pagamento e carico i dati completi per ognuna di esse:
		List<ModalitaPagamentoSoggetto> listaModPag = new ArrayList<ModalitaPagamentoSoggetto>();
		if (response.getListaModalitaPagamentoSoggetto() != null && response.getListaModalitaPagamentoSoggetto().size() > 0){
			for (ModalitaPagamentoSoggetto newModPag : response.getListaModalitaPagamentoSoggetto()){
				//ricerco la modalita pagamento:
				RicercaModalitaPagamentoPerChiaveResponse resp2 = ricercaModalitaPagamentoSoggetto(newModPag);
				//analizzo il dato ottenuto e setto i dati:
				if (resp2.getModalitaPagamentoSoggettoInModifica() != null){
					resp2.getModalitaPagamentoSoggettoInModifica().setDescrizioneStatoModalitaPagamento(Constanti.STATO_IN_MODIFICA_no_underscore);
					resp2.getModalitaPagamentoSoggettoInModifica().setAssociatoA(newModPag.getAssociatoA());
					listaModPag.add(resp2.getModalitaPagamentoSoggettoInModifica());
				} else {
					listaModPag.add(newModPag);
				}
			}
		}

		//setto la lista modalita pagamento nel model:
		model.setModalitaPagamento(listaModPag);
		
		//setto la lista sedi secondarie nel model:
		model.setSediSecondarie(response.getListaSecondariaSoggetto());

		return super.execute();
	}

	/**
	 * Gestisce il richiamo del servizio di ricerca puntuale di una sede secondaria
	 * @param sedeSecondariaSoggetto
	 * @param mod
	 * @return
	 */
	private RicercaSedeSecondariaPerChiaveResponse ricercaSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto, boolean mod){
		//inizializzo la requet:
		RicercaSedeSecondariaPerChiave ricercaSedeSecondariaPerChiave = new RicercaSedeSecondariaPerChiave();
		//setto i dati nella request:
		ricercaSedeSecondariaPerChiave.setEnte(sessionHandler.getEnte());
		ricercaSedeSecondariaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSedeSecondariaPerChiave.setSoggetto(model.getDettaglioSoggetto());
		ricercaSedeSecondariaPerChiave.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		ricercaSedeSecondariaPerChiave.setMod(mod);
		//richiamo il servizio:
		return soggettoService.ricercaSedeSecondariaPerChiave(ricercaSedeSecondariaPerChiave);
	}
	
	/**
	 * Gestisce il richiamo del servizio di ricerca puntuale di una modalita di pagamento
	 * @param newModPag
	 * @return
	 */
	private RicercaModalitaPagamentoPerChiaveResponse ricercaModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto newModPag){
		//inizializzo la requet:
		RicercaModalitaPagamentoPerChiave request2 = new RicercaModalitaPagamentoPerChiave();
		//setto i dati nella request:
		request2.setEnte(sessionHandler.getEnte());
		request2.setRichiedente(sessionHandler.getRichiedente());
		request2.setSoggetto(model.getDettaglioSoggetto());
		request2.setModalitaPagamentoSoggetto(newModPag);
		request2.setCodificaAmbito(getCodificaAmbito());
		//richiamo il servizio:
		return soggettoService.ricercaModalitaPagamentoPerChiave(request2);
	}

	protected String getCodificaAmbito() {
		//ritorno la codifica dell'ambito
		//in questo scenario e' fin
		return Constanti.AMBITO_FIN;
	}

	/**
	 * Risolve la provincia di un indirizzo
	 */
	private void cercaProvinceIndirizziSoggetto(){
		if (model.getDettaglioSoggetto().getIndirizzi() != null)
			if (!model.getDettaglioSoggetto().getIndirizzi().isEmpty()){
				ListIterator<IndirizzoSoggetto> indirizziSoggettoIterator = model.getDettaglioSoggetto().getIndirizzi().listIterator();
				//ciclo sugli indirizzi:
				while (indirizziSoggettoIterator.hasNext()){
					IndirizzoSoggetto i = indirizziSoggettoIterator.next();
					//richiamo il metodo cercaSiglaProvincia:
					i.setProvincia(cercaSiglaProvincia(i.getComune()));
					// nazione non implementata negli indirizzi,
					//  non presente su db
					indirizziSoggettoIterator.set(i);
				}
			}
	}

	/**
	 * Dato un comune richiama il servizio di ricerca comune e ne ritorna la provincia corretta
	 * @param comuneItaliano
	 * @return
	 */
	private String cercaSiglaProvincia(String comuneItaliano) {
		
		//istanzio la request:
		ListaComuni listaComuni = new ListaComuni();
		
		//setto i dati nella request:
		listaComuni.setIdStato(WebAppConstants.CODICE_ITALIA);
		listaComuni.setDescrizioneComune(comuneItaliano);
		listaComuni.setRichiedente(sessionHandler.getRichiedente());

		//richiamo il servizio di ricerca comune:
		ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

		if (lcr.isFallimento()){
			//esito negativo per fallimento
			return null;
		}
			
		if (lcr.getListaComuni() == null || lcr.getListaComuni().isEmpty()){
			//esito negativo per dati non trovati
			return null;
		}

		//ritorno la provincia del comune trovato:
		return lcr.getListaComuni().get(0).getSiglaProvincia();
	}
	
	/**
	 * Gestione dell'evento consulta sede
	 * @return
	 * @throws Exception
	 */
	public String consultaSede() throws Exception {
		if (uidSedeSecondaria != null) {
			
			//devo caricare i dati completi
			
			//istanzio la request del servizio:
			SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
			sedeSecondariaSoggetto.setUid(uidSedeSecondaria);
			//invoco il servizio:
			RicercaSedeSecondariaPerChiaveResponse ricercaSedeSecondariaPerChiaveResponse = ricercaSedeSecondariaSoggetto(sedeSecondariaSoggetto,false);

			//analizzo i dati ottenuti:
			
			selectedSede = ricercaSedeSecondariaPerChiaveResponse.getSedeSecondariaSoggetto();
			if (selectedSede.getContatti() != null){
				for (Contatto c : selectedSede.getContatti()){
					contattiMap.put(c.getContattoCodModo(), c);
				}
			}
					
		}
		return "consulta";
	}
	
	//GETTER E SETTER:
	
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	public String getUtentePropostaModifica() {
		return utentePropostaModifica;
	}

	public void setUtentePropostaModifica(String utentePropostaModifica) {
		this.utentePropostaModifica = utentePropostaModifica;
	}

	public Date getDataPropostaModifica() {
		return dataPropostaModifica;
	}

	public void setDataPropostaModifica(Date dataPropostaModifica) {
		this.dataPropostaModifica = dataPropostaModifica;
	}

	public boolean isInModifica() {
		return isInModifica;
	}

	public void setInModifica(boolean isInModifica) {
		this.isInModifica = isInModifica;
	}

	public Integer getUidSedeSecondaria() {
		return uidSedeSecondaria;
	}

	public void setUidSedeSecondaria(Integer uidSedeSecondaria) {
		this.uidSedeSecondaria = uidSedeSecondaria;
	}

	public SedeSecondariaSoggetto getSelectedSede() {
		return selectedSede;
	}

	public String getContattoStr(String tipo) {
		Contatto c = contattiMap.get(tipo);
		return c.getDescrizione() + (Constanti.TRUE.equals(c.getAvviso()) ? " (recapito per avviso)" : "");
	}

}
