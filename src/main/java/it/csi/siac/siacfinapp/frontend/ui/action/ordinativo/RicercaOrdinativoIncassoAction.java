/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.CausaleEntrataTendinoModel;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaOrdinativoIncassoAction extends WizardRicercaOrdinativoAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//tendini della causale:
		caricaTipiCausaleEntrata();
		
		//setto il titolo:
		this.model.setTitolo("Ricerca ordinativi di incasso");
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	   	
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_INCASSO;
		caricaListeCombo();
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		if(!isFaseBilancioAbilitata()){
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("ricerca ordinativo incasso", sessionHandler.getFaseBilancio()));
		}		

		if(!isAzioneAbilitata(CodiciOperazioni.OP_ENT_RICORDINC)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
		}

		//Pulisco i campi 
		if (!hasActionErrors()) {
			pulisciCampi();
		}

	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   	
	   	if(teSupport==null){
			pulisciTransazioneElementare();
		}
	   	
	   	if(caricaListaTipoFinanziamento()){
	   		return INPUT;
	   	}

		return SUCCESS;
	}

	
	/**
	 * Si occupa di caricare la lista dei tipi causale entrata chiamando
	 * il relativo servizio
	 */
	private void caricaTipiCausaleEntrata(){
		CausaleEntrataTendinoModel modelCausali = model.getCausaleEntrataTendino();
		if(modelCausali==null){
			//inizializzazione del model
			model.setCausaleEntrataTendino(new CausaleEntrataTendinoModel());
			modelCausali = model.getCausaleEntrataTendino();
		}
		caricaTipiCausaleEntrata(modelCausali);
	}
	
	/**
	 * Gestisce l'evento di selezione di un elemento nel tendino del tipo causale
	 * @return
	 */
	public String tipoCausaleEntrataChanged(){
		CausaleEntrataTendinoModel modelTendiniCausali = model.getCausaleEntrataTendino();
		//chiamo il metodo comune:
		tipoCausaleEntrataChanged(modelTendiniCausali);
		//
		return "tendinoCausaliPerRicerca";//ritorno il redirect specifico di questo caso d'uso.
	}
	
	public String ricercaOrdinativoIncasso(){

		resetPageNumberTableId("ricercaOrdinativoIncassoID"); 

		List<Errore> listaErrori = checkParametri();

		if(!isAzioneAbilitata(CodiciOperazioni.OP_ENT_RICORDINC)){
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			return INPUT;
		}
		
		//VALIDO
		if(listaErrori.isEmpty()){			
			return model.getUidOrdCollegaReversali() == null ? "gotoElencoOrdinativoIncasso" : "gotoElencoOrdinativoIncasso_CollegaReversali";
		} else {
			addErrori(listaErrori);
			return INPUT;
		}
		
	}
	
	protected void riempiListaClassGenBil(LeggiClassificatoriGenericiByTipoElementoBilResponse bilResponse) {
		
		if (bilResponse.getClassificatoriTipoFinanziamento() != null && bilResponse.getClassificatoriTipoFinanziamento().size() > 0) {
			model.setListaTipoFinanziamento(bilResponse.getClassificatoriTipoFinanziamento());
		} else
			model.setListaTipoFinanziamento(new ArrayList<TipoFinanziamento>());
	}
}
