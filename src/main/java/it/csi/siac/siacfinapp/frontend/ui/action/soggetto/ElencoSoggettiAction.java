/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoSoggettiAction extends WizardRicercaSoggettoAction{

	private static final long serialVersionUID = 7726699474402486489L;

	private Integer idSoggetto;
	private String codiceSoggetto;
	private String radioCodiceSoggetto;
	public enum TipoOperazioneEnum{ANNULLA, SOSPENDI, BLOCCA};
	public TipoOperazioneEnum tipoOperazione;
	public boolean ricaricaPagina;
	public boolean status;
	public boolean ancoraVisualizza = false;
	

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco Soggetti");
		setStatus(true);
	}	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		//info per debug:
		setMethodName("execute");
		
		if (ricaricaPagina) {
			//messaggio caricamento ok
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
			return reload();
		}
		
		//eseguo la ricerca:
		executeRicerca();
		
		// pulisce le liste quando effettuo una ricerca
		model.setDettaglioSoggetto(null);
		
	    return SUCCESS;
	}
	
	@Override
	public boolean isBottoneIndietroDiTipoSecondBtn() {
		return true;
	}
	
	/**
	 * gestione del dettaglio visualizzato
	 * @return
	 * @throws Exception
	 */
	public String visualizzaDettaglio() throws Exception{
		//info per debug:
		setMethodName("visualizzaDettaglio");
		
		//istanzio la lista di eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// se ho premuto visual dettaglio e poi paginazione
		// porta all'interno del get il method:visualizza che non permette di paginare
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		
		if(StringUtils.isEmpty(getRadioCodiceSoggetto())){
			//Se non viene selezionato alcun soggetto, mostro l'errore
			listaErrori.add(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore("Soggetto"));
			addErrori(listaErrori);
			return INPUT;
		} else {
			
			//preparo la request per il servizio di ricerca del soggetto per chiave:
			RicercaSoggettoPerChiave ricercaSoggettoPerChiave = convertiModelPerChiamataServizioRicercaPerChiave(getRadioCodiceSoggetto());
			
			//setto l'ambito:
			ricercaSoggettoPerChiave.setCodificaAmbito(getCodiceAmbito());
			
			//richiamo il servizio:
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(ricercaSoggettoPerChiave);
			
			//Salvo il dettaglio del soggetto selezionato e le relative sedi e modalita' di pagamento associate
			model.setDettaglioSoggetto(response.getSoggetto());
			model.setModalitaPagamento(response.getListaModalitaPagamentoSoggetto());
			model.setSediSecondarie(response.getListaSecondariaSoggetto());
			
			// setto l'ancora della pagina
			setAncoraVisualizza(true);
			
			return SUCCESS;
		}
	}
	
	/**
	 * Metodo che gestisce l'aggiornamento di stato del soggetto per annulla, blocca e sospendi
	 * @return string
	 */
	public String gestisciAggiornamentoStato() {
		
		if(!presenzaPaginazione(ServletActionContext.getRequest())){
			
			//compongo la request per il servizio di aggiornamento:
			AggiornaSoggetto aggiornaSoggetto = creaRequestPerAggiornamento();
			
			//imposto l'ambito:
			aggiornaSoggetto.setCodificaAmbito(getCodiceAmbito());
			
			//invoco il servizio:
			AggiornaSoggettoResponse response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			
			//analizzo l'esito del servizio:
			if(response.getErrori() != null && response.getErrori().size() > 0) {
				//presenza errori
				addErrori(methodName, response);
				return INPUT;
			} else {
				//operazione conculsa correttamente
				addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
			}
		}
		return reload();
	}
	
	/**
	 * Compone la request per il servizio di aggiornamento.
	 * 
	 * In particolare rispetto al fatto di cambiargli lo stato.
	 * 
	 * @return
	 */
	private AggiornaSoggetto creaRequestPerAggiornamento() {
		StatoOperativoAnagrafica statoOperativo = null;
		
		//deduco in tipoOperazione il cambiamento di stato voglio andare a richiedere:
		if (tipoOperazione != null) {
			
			if (tipoOperazione.equals(TipoOperazioneEnum.ANNULLA)) {
				//ANNULLA
				statoOperativo = StatoOperativoAnagrafica.ANNULLATO;
				
			} else if (tipoOperazione.equals(TipoOperazioneEnum.BLOCCA)) {
				//BLOCCA
				statoOperativo = StatoOperativoAnagrafica.BLOCCATO;
				
			} else if (tipoOperazione.equals(TipoOperazioneEnum.SOSPENDI)) {
				//SOSPENDI
				statoOperativo = StatoOperativoAnagrafica.SOSPESO;
				
			} else {
				//RESTA VALIDO
				statoOperativo = StatoOperativoAnagrafica.VALIDO;
			}
		} else {
			//RESTA VALIDO
			statoOperativo = StatoOperativoAnagrafica.VALIDO;
		}
		
		//istanzio la request:
		AggiornaSoggetto soggetto = new AggiornaSoggetto();
		
		//setto i dati nella request:
		soggetto.setEnte(sessionHandler.getEnte());
		soggetto.setRichiedente(sessionHandler.getRichiedente());
		soggetto.setSoggetto(new Soggetto());
		soggetto.getSoggetto().setStatoOperativo(statoOperativo);
		soggetto.getSoggetto().setCodiceSoggetto(codiceSoggetto);
		soggetto.getSoggetto().setUid(idSoggetto);
		soggetto.getSoggetto().setControlloSuSoggetto(false);
		soggetto.getSoggetto().setNotaStatoOperativo(model.getDettaglioSoggetto().getNotaStatoOperativo());
		
		//ritorno la request al chiamante:
		return soggetto;
	}
	
	/**
	 * Metodo che gestisce la cancellazione di un soggetto
	 * @return
	 */
	public String gestioneEliminaSoggetto() {
		
		if(!presenzaPaginazione(ServletActionContext.getRequest())){
		
			//compongo la request per il servizio di cancellazione:
			CancellaSoggetto cancellaSoggetto = new CancellaSoggetto();
			
			//setto l'ente:
			cancellaSoggetto.setEnte(sessionHandler.getEnte());
			
			//setto il richidente:
			cancellaSoggetto.setRichiedente(sessionHandler.getRichiedente());
			
			//indico quale soggetto voglio che venga eliminato:
			Soggetto soggetto = new Soggetto();
			soggetto.setUid(idSoggetto);
			cancellaSoggetto.setSoggetto(soggetto);
			
			//richiamo il servizio:
			CancellaSoggettoResponse response = soggettoService.cancellaSoggetto(cancellaSoggetto);
			
			//analizzo la risposta del serizio:
			if(response.getErrori() != null && response.getErrori().size() > 0) {
				//presenza errori nel servizio
				addErrori(methodName, response);
				return INPUT;
			} else {
				//tutto ok
				addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
			}
		}
		return reload();
	}
	
	/**
	 * Metodo che permette la visualizzazione o meno del pulsante per la visualizzazione del dettaglio del soggetto
	 * @return boolean
	 */
	public boolean isVisualizzaDettaglioVisibile() {
		List<Soggetto> supportListSoggetti = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_SOGGETTI);
		return (supportListSoggetti != null && supportListSoggetti.size() > 0) ? true : false;
	}

	/**
	 * Metodo che controlla se le azioni relative ad un soggetto sono disponibili o meno
	 * @param decodificaOperazione
	 * @param codiceStato
	 * @param utenteUltimaModifica
	 * @return boolean 
	 */
	public boolean isAbilitato(Integer decodificaOperazione, String codiceStato, String utenteUltimaModifica) {
		boolean abilitato = false;
		boolean gestSogg = FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE);
		boolean gestSoggDec = FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO);
		String supportStato = (codiceStato != null && stack(codiceStato) != null) ? stack(codiceStato).toString() : null;
		String supportUtenteUltimaModifica = (utenteUltimaModifica != null && stack(utenteUltimaModifica) != null) ? stack(utenteUltimaModifica).toString() : null;
		if (supportStato != null && !"".equalsIgnoreCase(supportStato) && decodificaOperazione != null) {
			switch (decodificaOperazione) {
			case CONSULTA:
				abilitato = true;
				break;
			case AGGIORNA:
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg || gestSoggDec;
				} else if (CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato)) {
					if(gestSogg){
						//sono il master
						abilitato = false;
					} else{
						//sono decentrato
						boolean sonoUltimoAModificare = decentratoSuPropriaOccorrenza(supportUtenteUltimaModifica);
						abilitato = sonoUltimoAModificare;
					}
				}else if (CodiciStatoSoggetto.IN_MODIFICA.toString().equalsIgnoreCase(supportStato)) {
					if(gestSogg){
						//sono il master
						abilitato = false;
					} else{
						//sono decentrato
						boolean sonoUltimoAModificare = decentratoSuPropriaOccorrenza(supportUtenteUltimaModifica);
						abilitato = sonoUltimoAModificare;
					}
				}
				break;
			case ANNULLA:
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.BLOCCATO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.SOSPESO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				}
				break;
			case SOSPENDI:
			case BLOCCA:
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				}
				break;
			case CANCELLA:
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				} else if (CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSoggDec && decentratoSuPropriaOccorrenza(supportUtenteUltimaModifica);
				}
				break;
			case VALIDA:
				if (CodiciStatoSoggetto.BLOCCATO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.SOSPESO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato)
						|| CodiciStatoSoggetto.IN_MODIFICA.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				}
				break;
			case COLLEGA:
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.BLOCCATO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.SOSPESO.toString().equalsIgnoreCase(supportStato)
						) {
					abilitato = gestSogg;
				}
				break;
			default:
				break;
			}
		}
		return abilitato;
	}
	
	/**
	 * Ricarica i dati
	 * @return
	 */
	public String reload() {
		//info per debuf:
		setMethodName("reload");
		debug(methodName, "entro nel reload ");
		
		//compongo la request per la ricerca dei soggetti:
		RicercaSoggetti ricercaSoggetti = convertiModelPerChiamataServizioRicerca();
		
		//setto l'ambito:
		ricercaSoggetti.setCodiceAmbito(getCodiceAmbito());
		
		//richiamo il servizio di ricerca:
		RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(ricercaSoggetti);
		//Controllo che il servizio non restituisca errori
		if(response.isFallimento()) {
			//presenza errori
			addErrori(methodName, response);
			return INPUT;
		}
		//Metto in sessione la lista ricevuta per utilizzarla in Elenco Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SOGGETTI, response.getSoggetti());
		
		return SUCCESS;
	}	
	
	
	/**
	 * Metodo che controlla che il soggetto in esame sia decentrato per l'utente corrente
	 * @param utenteUltimaModifica 
	 * @return boolean 
	 */
	private boolean decentratoSuPropriaOccorrenza(String utenteUltimaModifica) {
		boolean isDecentratoSuPropriaOccorrenza = isUtenteLoggato(utenteUltimaModifica);
		return isDecentratoSuPropriaOccorrenza;
	}
	
	/**
	 * Metodo che controlla se il soggetto in esame, e' bloccato o sospeso
	 * @param stato
	 * @return boolean
	 */
	public boolean isSoggettoSospesoBloccato(StatoOperativoAnagrafica stato) {
		return stato.equals(StatoOperativoAnagrafica.BLOCCATO) || stato.equals(StatoOperativoAnagrafica.SOSPESO);
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	public String getRadioCodiceSoggetto() {
		return radioCodiceSoggetto;
	}

	public void setRadioCodiceSoggetto(String radioCodiceSoggetto) {
		this.radioCodiceSoggetto = radioCodiceSoggetto;
	}

	public TipoOperazioneEnum getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazioneEnum tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public Integer getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Integer idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public boolean isRicaricaPagina() {
		return ricaricaPagina;
	}

	public void setRicaricaPagina(boolean ricaricaPagina) {
		this.ricaricaPagina = ricaricaPagina;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public boolean isAncoraVisualizza() {
		return ancoraVisualizza;
	}

	public void setAncoraVisualizza(boolean ancoraVisualizza) {
		this.ancoraVisualizza = ancoraVisualizza;
	}
	
}
