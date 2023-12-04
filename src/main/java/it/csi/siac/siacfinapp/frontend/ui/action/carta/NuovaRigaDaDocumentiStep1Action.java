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

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.RicercaQuotaSpesaModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class NuovaRigaDaDocumentiStep1Action extends ActionKeyNuovaRigaDaDocumentiAction{
	

	private static final long serialVersionUID = 1L;
	
	public NuovaRigaDaDocumentiStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}

	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		init();
	}
	
	private void init(){
		
		//setto il titolo:
		this.model.setTitolo("Inserisci nuova riga da documenti");			
		
		buildTitoloDiPagina();
		
		//caricamento liste per ricerca soggetto
		caricaListePerRicercaSoggetto();
		
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_DOCUMENTO_SPESA);
	   	model.setListaDocTipoSpesa((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_DOCUMENTO_SPESA));	
	   	
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		removeListaPreDocDaDocumentiInSession();
		return SUCCESS;
	}
	
	public String cerca(){
		
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		
		// al click di cerca riazzero l'elenco delle righe
		model.setElencoSubdocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new ArrayList<SubdocumentoSpesa>());
		model.setResultSize(0);
		
		// levo da sessione eventuali parametri lanciati in ricerche precedenti
		sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new RicercaQuotaSpesaModel());
		
		List<Errore> listaErrori= new ArrayList<Errore>();

		//controllo esistenza provvedimento valido
		boolean erroriProvvedimento = controlloServizioProvvedimento(model.getProvvedimento(), model.isProvvedimentoSelezionato());
				

		if(!listaErrori.isEmpty() || erroriProvvedimento){
			addErrori(listaErrori);
			return INPUT;
		}
		

		return "cerca";
	}
	
	
	public String indietro(){
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		return "vaiStep2DiGestioneCarta";
	}
	
	public String annulla() {
		pulisciModelRicerca(this.model);
		init();
		removeListaPreDocDaDocumentiInSession();
		return INPUT;
	}
	
}
