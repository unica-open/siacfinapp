/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import xyz.timedrain.arianna.plugin.BreadCrumb;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class NuovaRigaDaMovimentoAction extends ActionKeyGestioneCartaAction {
	

	private static final long serialVersionUID = 1L;

	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Inserisci nuova riga da movimento");					

		StringBuffer titolo=new StringBuffer();
		titolo.append("Carta ");
		titolo.append(model.getDescrizioneCarta());
		
		if (model.isOptionsPagamentoEstero()) {
			titolo.append(" - Valuta Estera ");
			titolo.append(model.getCodiceDivisa());
		}
		this.model.setTitoloStep(titolo.toString());
		
		//caricamento liste per ricerca soggetto
		caricaListePerRicercaSoggetto();
				
		//caricamento lista contoTesoriere
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CONTO_TESORERIA);
		model.setListaContoTesoriereRiga(((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)));
		
		//servono per indicare alla maschera che deve visualizzare l'asterisco di obbligatorieta':
		model.setSoggettoObbligatorio(true);
		model.setModPagamentoObbligatoria(true);
		//
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//servono per indicare alla maschera che deve visualizzare l'asterisco di obbligatorieta':
		model.setSoggettoObbligatorio(true);
		model.setModPagamentoObbligatoria(true);
		//
		controlloStatoBilancio(sessionHandler.getAnnoBilancio(), "GESTIONE", "CARTA CONTABILE");
		
		//Pulisco i campi model riga 
		pulisciCampiRiga();	
		
		//imposto il default conto tesoriere:
		impostaDefaultContoTesoriere(false);
		//
		
		//imposto il default descrizione riga:
		impostaDefaultDescrizioneRiga(false);
		
		return SUCCESS;
	}
	


	
	//cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh della tabella modpagamento
	public String modpagamento(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		caricaListeCreditore(cod);
			
		return MODPAGAMENTO;
	}
		
		
	public String sedisecondarie(){
		return SEDISECONDARIE;
	}
	
			
	public String caricaTitoloModPag(){
	   
	   model.getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getCodice();
	   model.getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getDescrizione();
	   
	   
	   
	   return MODPAGAMENTO;
	}		
	  		
	   
	public String caricaTitoloSedi(){
	   
	   
	   if(model.getRadioSediSecondarieSoggettoSelezionato()!=0){
		   model.getSedeSelezionata().getDenominazione();
	   }
	   
	   return SEDISECONDARIE;
	}		
	
	@Override
	public String selezionaSoggetto() {
		
		return selezionaSoggettoRiga();
	}
	
	
	/*
	 * aggiorna la tabella modalita di pagamento su selezione della sede e aggiorna quindi la lista sedi associate alla modalita' di pagamento scelta(non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.action.ordinativo.WizardGestioneOrdinativoAction#remodpagamento()
	 */
	@Override
	public String remodpagamento(){

	 		return super.remodpagamento();
	}
	
	@Override	
	public String resede(){
		return super.resede();
	}
	
	public String annulla() {
		pulisciCampiRiga();
		impostaDefaultContoTesoriere(false);
		return INPUT;
	}
}
