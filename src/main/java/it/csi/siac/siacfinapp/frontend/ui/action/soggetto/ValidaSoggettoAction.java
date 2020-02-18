/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.ValidaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorClassificazioneSoggettoByCodice;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorContatto;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorIndirizzoSoggettoByIndirizzoId;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorOnereByCodice;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ValidaSoggettoAction extends SoggettoAction<ValidaSoggettoModel>
{

	private static final long serialVersionUID = 7726699474402486489L;

	private String soggetto;

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//istanzio la request per il servizio ricercaSoggettoPerChiave:
		RicercaSoggettoPerChiave req = convertiModelPerChiamataServizioRicercaPerChiave(getSoggetto());
		//invoco il servizio ricercaSoggettoPerChiave:
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(req);
		model.setDettaglioSoggetto(response.getSoggetto());
		if (model.getDettaglioSoggetto().getStatoOperativo().equals(StatoOperativoAnagrafica.IN_MODIFICA)){
			model.getDettaglioSoggetto().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
		}
		if (response.getSoggettoInModifica() != null){
			if (response.getSoggettoInModifica().getStatoOperativo() == null){
				response.getSoggettoInModifica().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
			}
			if (response.getSoggettoInModifica().getDataStato() == null){
				long currMillisec = System.currentTimeMillis();
				Date dateInserimento = new Date(currMillisec);
				response.getSoggettoInModifica().setDataStato(dateInserimento);
			}
	
			model.setDettaglioSoggettoMod(response.getSoggettoInModifica());
		}

		ordinaElenchi();

		return SUCCESS;
	}

	/**
	 * Metodo per ordinare gli elenchi
	 */
	private void ordinaElenchi() {
		Soggetto dettaglioSoggetto = model.getDettaglioSoggetto();

		sortCollection(dettaglioSoggetto.getContatti(), ComparatorContatto.INSTANCE);
		sortCollection(dettaglioSoggetto.getIndirizzi(), ComparatorIndirizzoSoggettoByIndirizzoId.INSTANCE);
		sortCollection(dettaglioSoggetto.getElencoOneri(), ComparatorOnereByCodice.INSTANCE);
		sortCollection(dettaglioSoggetto.getElencoClass(), ComparatorClassificazioneSoggettoByCodice.INSTANCE);

		Soggetto dettaglioSoggettoMod = model.getDettaglioSoggettoMod();

		if (dettaglioSoggettoMod != null) {
			sortCollection(dettaglioSoggettoMod.getContatti(), ComparatorContatto.INSTANCE);
			sortCollection(dettaglioSoggettoMod.getIndirizzi(), ComparatorIndirizzoSoggettoByIndirizzoId.INSTANCE);
			sortCollection(dettaglioSoggettoMod.getElencoOneri(), ComparatorOnereByCodice.INSTANCE);
			sortCollection(dettaglioSoggettoMod.getElencoClass(), ComparatorClassificazioneSoggettoByCodice.INSTANCE);
		}
	}

	private <T extends Entita> void sortCollection(List<T> list, Comparator<T> comparator){
		if (list != null){
			Collections.sort(list, comparator);
		}
	}

	/**
	 * Metodo che rifiuta le modifiche apportate su un soggetto
	 * 
	 * @return forward
	 */
	public String rifiutaSoggetto(){
		if (model.getDettaglioSoggetto().getStatoOperativo() != null
				&& model.getDettaglioSoggetto().getStatoOperativo().name()
						.equalsIgnoreCase(StatoOperativoAnagrafica.PROVVISORIO.name())){
			//istanzio la request per il servizio cancellaSoggetto:
			CancellaSoggetto cancellaSoggetto = new CancellaSoggetto();
			cancellaSoggetto.setEnte(sessionHandler.getEnte());
			cancellaSoggetto.setRichiedente(sessionHandler.getRichiedente());
			cancellaSoggetto.setSoggetto(model.getDettaglioSoggetto());
			//invoco il servizio cancellaSoggetto:
			CancellaSoggettoResponse response = soggettoService.cancellaSoggetto(cancellaSoggetto);
			if (response.getErrori() != null && response.getErrori().size() > 0){
				addErrori(methodName, response);
				return INPUT;
			}
		} else {
			AnnullaSoggettoInModifica annullaSoggettoInModifica = new AnnullaSoggettoInModifica();
			annullaSoggettoInModifica.setEnte(sessionHandler.getEnte());
			annullaSoggettoInModifica.setRichiedente(sessionHandler.getRichiedente());
			annullaSoggettoInModifica.setSoggetto(model.getDettaglioSoggettoMod());
			AnnullaSoggettoInModificaResponse responseAnnulla = soggettoService
					.annullaSoggettoInModifica(annullaSoggettoInModifica);
			if (responseAnnulla.getErrori() != null && responseAnnulla.getErrori().size() > 0){
				addErrori(methodName, responseAnnulla);
				return INPUT;
			}
		}
		return GOTO_ELENCO_SOGGETTI;
	}

	/**
	 * Metodo che valida le modifiche apportate su un soggetto
	 * 
	 * @return forward
	 */
	public String validaSoggetto(){
		//istanzio la request per il servizio aggiornaSoggetto:
		AggiornaSoggetto soggetto = new AggiornaSoggetto();
		soggetto.setEnte(sessionHandler.getEnte());
		soggetto.setRichiedente(sessionHandler.getRichiedente());
		if (model.getDettaglioSoggetto().getStatoOperativo() != null
				&& model.getDettaglioSoggetto().getStatoOperativo().name()
						.equalsIgnoreCase(StatoOperativoAnagrafica.PROVVISORIO.name())){
			model.getDettaglioSoggetto().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
			soggetto.setSoggetto(model.getDettaglioSoggetto());
		} else {
			model.getDettaglioSoggettoMod().setControlloSuSoggetto(false);
			model.getDettaglioSoggettoMod().setUidSoggettoPadre(model.getDettaglioSoggetto().getUid());
			model.getDettaglioSoggettoMod().setCodiceSoggettoPadre(model.getDettaglioSoggetto().getCodiceSoggetto());
			model.getDettaglioSoggettoMod().setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
			soggetto.setSoggetto(model.getDettaglioSoggettoMod());
		}
		//invoco il servizio aggiornaSoggetto:
		AggiornaSoggettoResponse response = soggettoService.aggiornaSoggetto(soggetto);
		if (response.getErrori() != null && response.getErrori().size() > 0){
			addErrori(methodName, response);
			return INPUT;
		}
		return GOTO_ELENCO_SOGGETTI;
	}

	/**
	 * Verifica stato provincia e comune
	 * @return
	 */
	public boolean controlloStatoProvinciaComune(){
		int a = 0;
		if (model.getDettaglioSoggetto().getComuneNascita().getNazioneDesc()
				.equals(model.getDettaglioSoggettoMod().getComuneNascita().getNazioneDesc())){
			a++;
		}
		if (model.getDettaglioSoggetto().getComuneNascita().getCodiceBelfiore()
				.equals(model.getDettaglioSoggettoMod().getComuneNascita().getCodiceBelfiore())){
			a++;
		}
		if (a == 2) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Controllo differenze sul 
	 * codice fiscale
	 * @return
	 */
	public boolean controlloDifferenzaCF() {
		if (!model.getDettaglioSoggetto().getCodiceFiscale().trim()
				.equals(model.getDettaglioSoggettoMod().getCodiceFiscale().trim())){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Controllo differenze su
	 * indirizzi
	 * @return
	 */
	public boolean controlloDifferenzaIndirizzi() {
		boolean flag = true;
		for (int i = 0; i < model.getDettaglioSoggettoMod().getIndirizzi().size(); i++){
			try{
				if (model
						.getDettaglioSoggetto()
						.getIndirizzi()
						.get(i)
						.getIndirizzoFormattato()
						.equalsIgnoreCase(
								model.getDettaglioSoggettoMod().getIndirizzi().get(i).getIndirizzoFormattato())){
					flag = false;
				}
			} catch (Exception e) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * Controllo differenze su recapiti
	 * @param i
	 * @return
	 */
	public boolean controlloDifferenzaRecapiti(int i) {
		String oldRecap = model.getDettaglioSoggetto().getContatti().get(i).getContattoFormattato(); 
		String newRecap = model.getDettaglioSoggettoMod().getContatti().get(i).getContattoFormattato();
		return ! StringUtils.equals(oldRecap, newRecap);
	}

	/**
	 * 	Controllo su natura giuridica
	 * @return
	 */
	public boolean controlloNaturaGiuridica() {
		boolean flag = true;
		if (model.getDettaglioSoggetto().getNaturaGiuridicaSoggetto() != model.getDettaglioSoggettoMod().getNaturaGiuridicaSoggetto()){
			flag = false;
		}
		return flag;
	}

	@Override
	public String getActionKey() {
		return "validaSoggetto";
	}

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

}
