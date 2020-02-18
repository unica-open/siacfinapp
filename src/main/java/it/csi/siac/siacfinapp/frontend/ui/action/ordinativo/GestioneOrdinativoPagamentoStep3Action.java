/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoPagamentoStep3Action extends ActionKeyGestioneOrdinativoPagamentoAction{

	private static final long serialVersionUID = 1L;
	
	public GestioneOrdinativoPagamentoStep3Action () {
		//Specifichiamo la tipologia che puo essere pagamento o incasso,
		//nel nostro caso pagamento:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
	}
	
	@Override
	public void prepare() throws Exception {
		if(model!=null){
			//setto il titolo:
			this.model.setTitolo("Provvisori di cassa");
			
			//invoco il prepare della super classe:
			super.prepare();
		}
		caricaListeStep3();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
        // patch - jira-946
		if(model==null){
			// significa che sono andato in inserimento -> consulta -> click su filo di arianna quote
			return "erroreFiloArianna";
		}		
		if(model.getGestioneOrdinativoStep1Model().getCapitolo()!=null){
			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno()==null){
				
				// significa che sono andato in inserimento -> consulta -> click su filo di arianna quote
				return "erroreFiloArianna";
			}
		}
		// calcola totali nella tabella
		calcolaTotaliFooter();
		
		caricaLabelsInserisci(3, model.getGestioneOrdinativoStep1Model().getOrdinativo().getUid() == 0);
		setClearStatus(true);
		
		// precarico i valori di default della ricerca
		model.getRicercaProvvisorioModel().setAnnoProvvisorioDa(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		model.getRicercaProvvisorioModel().setAnnoProvvisorioA(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		return SUCCESS;
	}
	
	/**
	 * evento che annulla l'inserimento
	 * @return
	 */
	public String pulisciProvvisorio(){
		//Pulisce il dato
		model.getGestioneOrdinativoStep3Model().setProvvisorio(new ProvvisorioDiCassa());
		return INPUT;
	}
	
	public String aggiornaCopertura(){
		
		if(controlloGestioneProvvisorio(OPERAZIONE_MODIFICA).equalsIgnoreCase(INPUT)){
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizza(false);
			return INPUT;
		}
		
		setUidProvvDaRicerca("");
		
		return SUCCESS;
		
	}
	

	
	
	@Override
	public String aggiornaOrdinativo() {
	
		if(null!=model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare() && model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare().compareTo(BigDecimal.ZERO)>0){
		
			addErrore(ErroreFin.TOTALE_PROVVISORI_COLLEGATI_NON_COINCIDE_CON_IMPORTO_ORDINATIVO_SUB.getErrore(""));
			return INPUT;
		}
		return super.aggiornaOrdinativo();
	}
	
	/**
	 * Metodo che parte all'evento di selezione di un provvisorio di cassa
	 * @return
	 */
	public String selezionaProvvisorio(){
		debug("selezionaProvvisorio", "seleziono il "+getUidProvvDaRicerca());
		//richiamo il metodo nella super classe:
		return super.selezionaProvvisorio(getUidProvvDaRicerca()); 
	}
	
	/**
	 * Metodo che parte all'evento di salvataggio dei provvisori
	 * @return
	 */
	public String salvaProvvisori(){
		// click su salva provvisori
		return super.salvaProvvisori();
	}
	
	@Override
	public String dettaglioAggiornaCoperture(){
		// apre il dettalio coperture
		return super.dettaglioAggiornaCoperture();
	}


	
	
	
}