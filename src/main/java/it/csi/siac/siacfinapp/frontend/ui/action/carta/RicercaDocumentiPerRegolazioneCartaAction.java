/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaRicercaDocumentoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.RicercaQuotaSpesaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentiPerRegolazioneCartaAction extends WizardRegolarizzaCartaAction {

	
private static final long serialVersionUID = 1L;
	
	
	public RicercaDocumentiPerRegolazioneCartaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	/**
	 * Costruise il titolo della pagina
	 */
	protected void buildTitoloDiPagina(){
		
		//setto il titolo:
		this.model.setTitolo("Collega a documento - Ricerca");

		StringBuffer titolo=new StringBuffer();
		titolo.append("Carta ");
		titolo.append(model.getDescrizioneCarta());
		
		if (this.model.isOptionsPagamentoEstero()) {
			titolo.append(" - Valuta Estera ");
			titolo.append(this.model.getCodiceDivisa());
		}
		
		//setto il titolo di step:
		this.model.setTitoloStep(titolo.toString());
	}
	
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//da prepare:
		pulisciModelRicerca(this.model.getRicercaDocumenti());
		buildTitoloDiPagina();
		caricaDatiPerMacheraRicercaDocumenti();
		//
		
		//I Documenti di spesa devono pagare lo stesso soggetto della carta:
		// (forzare il soggetto nella maschera di ricerca)
		popolaSoggetto(model.getDettaglioRiga().getSoggetto());
		//
		
		removeListaPreDocDaDocumentiInSession();
	    return SUCCESS;
	}
	
	private void caricaDatiPerMacheraRicercaDocumenti(){
		//caricamento liste per ricerca soggetto
		caricaListePerRicercaSoggetto();
		
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_DOCUMENTO_SPESA);
		
		//I Tipi documento accettati possono essere tutti (compreso ALG) si esclude solo NCD e CCN:
		List<CodificaFin> listaDocTipoSpesa = (List<CodificaFin>)mappaListe.get(TipiLista.TIPO_DOCUMENTO_SPESA);
		listaDocTipoSpesa = FinUtility.removeByCodes(listaDocTipoSpesa, "NCD", "CCN");
	   	model.getRicercaDocumenti().setListaDocTipoSpesa(listaDocTipoSpesa);
	}
	
	/**
	 * Qui gestire la ricerca.
	 * @return
	 */
	public String cercaDocPerCollega(){
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		
		//il soggetto va spostato nel model che usera' la action successiva:
		model.getRicercaDocumenti().setSoggetto(clone(model.getSoggetto()));
		//
		
		//il provvedimento va spostato nel model che usera' la action successiva:
		model.getRicercaDocumenti().setProvvedimento(clone(model.getProvvedimento()));
		//
		
		// al click di cerca riazzero l'elenco delle righe
		model.getRicercaDocumenti().setElencoSubdocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new ArrayList<SubdocumentoSpesa>());
		model.getRicercaDocumenti().setResultSize(0);
		
		// levo da sessione eventuali parametri lanciati in ricerche precedenti
		sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new RicercaQuotaSpesaModel());
		
		//controllo esistenza provvedimento valido
		/*boolean erroriProvvedimento = controlloServizioProvvedimento(model.getRicercaDocumenti().getProvvedimento(), model.getRicercaDocumenti().isProvvedimentoSelezionato());
				

		if(!listaErrori.isEmpty() || erroriProvvedimento){
			addErrori(listaErrori);
			return INPUT;
		}*/

		return "cercaDocumenti";
	}
	
	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);		
	}
	
	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}
	
	public String indietro(){
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		return "vaiARegolazioneCarta";
	}

	/**
	 * Qui gestire il pulsante annulla dei paramentri di ricerca documento.
	 * @return
	 */
	public String annullaRicercaDoc() {
		pulisciModelRicerca(this.model.getRicercaDocumenti());
		caricaDatiPerMacheraRicercaDocumenti();
		removeListaPreDocDaDocumentiInSession();
		return INPUT;
	}
	
	@Override
	protected void pulisciModelRicerca(GestioneCartaRicercaDocumentoModel modelRicerca) {
		super.pulisciModelRicerca(modelRicerca);
		//pulisco anche quelli che non sono nel sotto model di ricerca:
		//Soggetto
		this.model.setSoggetto(new SoggettoImpegnoModel());
		this.model.setSoggettoSelezionato(false);
		this.model.setSoggettoRicerca(new SoggettoImpegnoModel());
		//Provvedimento
		this.model.setProvvedimento(null);
		this.model.setProvvedimentoSelezionato(false);
		this.model.setProvvedimentoRicerca(new ProvvedimentoImpegnoModel());
	}
	
	
	
}
