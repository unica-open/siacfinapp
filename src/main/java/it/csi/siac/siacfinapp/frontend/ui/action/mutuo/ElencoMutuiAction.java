/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoMutuiAction extends WizardRicercaMutuoAction{

	private static final long serialVersionUID = 1L;
	
	private String codiceMutuoAnnullato;
	private boolean clearPagina;
	private boolean fromAggiorna;
	private String pulisciPagine;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco Mutui");
	}	
	
	
	public boolean isMutuoModificabile(){
		
		//OP-MUT-gestisciMutuo
		boolean modificabile = false;
		
		FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), CodiciOperazioni.OP_MUT_gestisciMutuo);
		
		return modificabile;
		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		debug("pulisce pagine", pulisciPagine);
		if(null!=pulisciPagine && pulisciPagine.equalsIgnoreCase(WebAppConstants.Si)){
			setClearPagina(true);
		}
		//se e' presente la paginazione nell'elenco dei mutui allora la mantengo in sessione
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			
			// ho cliccato sulla paginazione
			int paginata = paginazioneRichiesta(ServletActionContext.getRequest());
			if(paginata!=0){
				model.setPremutoPaginazione(paginata);
			}
			
		}
		
		// se la ricerca torna degli errori ritorna
		// degli errori torno sulla form di ricerca 
		if(executeRicerca().equals(INPUT)){
			
			return GOTO_ERRORE_RICERCA;
		}
		
	    return SUCCESS;
	}
	
	
	public String annullaMutuo(){
		
		debug("annullaMutuo", "entro qui --> "+getCodiceMutuoAnnullato());
		
		AnnullaMutuo annullaMutuo = new AnnullaMutuo();
		Mutuo mutuo = new Mutuo();
		mutuo.setCodiceMutuo(getCodiceMutuoAnnullato());
		annullaMutuo.setMutuoDaAnnullare(mutuo);
		annullaMutuo.setRichiedente(sessionHandler.getRichiedente());
		annullaMutuo.setEnte(sessionHandler.getEnte());
		//richiamo al servizio di annullamento
		AnnullaMutuoResponse response =  mutuoService.annullaMutuo(annullaMutuo);
		if(response.isFallimento()) {
			addErrori("executeRicerca", response);
			return INPUT;
		}	
		// ricarico la lista aggiornata
		executeRicerca();
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
		
		
		return SUCCESS;
	}
	
	/**
	 * utilizzata in jsp di elenco per sapere se abilitare o meno il
	 * bottone di inserimento
	 * @return
	 */
	public boolean isInseribile(){
		 return FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(),CodiciOperazioni.OP_MUT_gestisciMutuo);
		
	}
	
	/**
	 * utilizzata in jsp di elenco per sapere se abilitare o meno il
	 * bottone di aggiornamento
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAggiorna(String stato){
		boolean ritorna = false;
		
		// stato E/A
		// verifico che azione sia lettore
		
		if((Constanti.D_MUTUO_STATO_ANNULLATO.equalsIgnoreCase(stato) || Constanti.D_MUTUO_STATO_ESTINTO.equalsIgnoreCase(stato)) &&
				   FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(),CU_OP_MUT_LEGGI_MUTUO)){
					
		   ritorna = false;	
		   
		   
		// stato D/P
		// verifico che azione sia gestisci
		}else if((Constanti.D_MUTUO_STATO_DEFINITIVO.equalsIgnoreCase(stato) || Constanti.D_MUTUO_STATO_PROVVISORIO.equalsIgnoreCase(stato)) &&
				FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(),CU_OP_MUT_GESTISCI_MUTUO)){
			
		    ritorna = true;
		}		

		return ritorna;
	}
	
	/**
	 * utilizzata in jsp di elenco per sapere se abilitare o meno il
	 * bottone di annullamento
	 * @param stato
	 * @return
	 */
	public boolean isAbilitatoAnnulla(String stato){
		
		boolean ritorna = false;
		//controllo che il mutuo selezionato sia annullabile
		if((Constanti.D_MUTUO_STATO_DEFINITIVO.equalsIgnoreCase(stato) || Constanti.D_MUTUO_STATO_PROVVISORIO.equalsIgnoreCase(stato)) &&
					FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(),CU_OP_MUT_GESTISCI_MUTUO)){
			
			    ritorna = true;
		}		
		
		return ritorna;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getCodiceMutuoAnnullato() {
		return codiceMutuoAnnullato;
	}

	public void setCodiceMutuoAnnullato(String codiceMutuoAnnullato) {
		this.codiceMutuoAnnullato = codiceMutuoAnnullato;
	}

	public boolean isClearPagina() {
		return clearPagina;
	}

	public void setClearPagina(boolean clearPagina) {
		this.clearPagina = clearPagina;
	}

	public boolean isFromAggiorna() {
		return fromAggiorna;
	}

	public void setFromAggiorna(boolean fromAggiorna) {
		this.fromAggiorna = fromAggiorna;
	}

	public String getPulisciPagine() {
		return pulisciPagine;
	}

	public void setPulisciPagine(String pulisciPagine) {
		this.pulisciPagine = pulisciPagine;
	}
}