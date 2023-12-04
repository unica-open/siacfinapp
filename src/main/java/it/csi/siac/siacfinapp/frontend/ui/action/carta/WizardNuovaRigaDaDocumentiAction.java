/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import com.opensymphony.xwork2.ActionContext;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaRicercaDocumentoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCartaResponse;

public class WizardNuovaRigaDaDocumentiAction extends AbstractCartaAction<GestioneCartaRicercaDocumentoModel> {

	private static final long serialVersionUID = 1L;
	
	protected void buildTitoloDiPagina(){
		GestioneCartaModel gestioneCartaModel = (GestioneCartaModel) ActionContext.getContext().getSession().get("KEY_GESTIONE_CARTA_MODEL");
		
		//setto il titolo:
		this.model.setTitolo("Inserisci nuova riga da documenti");

		StringBuffer titolo=new StringBuffer();
		titolo.append("Carta ");
		titolo.append(gestioneCartaModel.getDescrizioneCarta());
		
		if (gestioneCartaModel.isOptionsPagamentoEstero()) {
			titolo.append(" - Valuta Estera ");
			titolo.append(gestioneCartaModel.getCodiceDivisa());
		}
		this.model.setTitoloStep(titolo.toString());
	}
	
	
	protected String executeRicercaSubDocumentiDaAssociare(){
		
		//1. Costruisco l'oggetto RicercaQuotaSpesa:
		RicercaQuotaSpesa ricercaQuotaSpesa = buildRicercaQuotaSpesaPerRicercaSubDocumenti(this.model);
		//
		
		//2. Costruisco la requst per il servizio:
		RicercaDocumentiCarta ricercaDocumentiCarta = buildRicercaDocumentiCartaPerRicercaSubDocumenti(ricercaQuotaSpesa);
		//
		
		//3. Chiamata al servizio di ricerca 
		RicercaDocumentiCartaResponse ricercaDocCartaResp = cartaContabileService.ricercaDocumentiCarta(ricercaDocumentiCarta);
		//
		
		//4. Controllo errori nella response:
		if(ricercaDocCartaResp!=null && ricercaDocCartaResp.isFallimento()){
			addErrori(ricercaDocCartaResp.getErrori());
			return INPUT;
		}
		
		RicercaQuotaSpesaResponse response = ricercaDocCartaResp.getResponseQuoteSpesa();
		
		if(response!=null && response.isFallimento()){
			addErrori(response.getErrori());
			return INPUT;
		}
		
		//5. Gestione della response:
		gestioneResponseRicercaSubDocumenti(response, ricercaQuotaSpesa, this.model);
		
		/*
		// gestione della response
		if(response!=null && response.getListaSubdocumenti()!=null && response.getListaSubdocumenti().size()>0){
			// setto i risultati della ricerca
			model.setElencoSubdocumentoSpesa(response.getListaSubdocumenti());
			
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, response.getListaSubdocumenti());
			
			model.setResultSize(response.getTotaleElementi());
		}else{
			model.setElencoSubdocumentoSpesa(new ArrayList<SubdocumentoSpesa>());
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, new ArrayList<SubdocumentoSpesa>());
			model.setResultSize(0);
		}
		if(model.getPremutoPaginazione()!=0){
			ricercaQuotaSpesa.getParametriPaginazione().setNumeroPagina(model.getPremutoPaginazione());
			ricercaQuotaSpesa.getParametriPaginazione().setElementiPerPagina(DEFAULT_PAGE_SIZE);
	    }else{
		    //numeratore per la paginazione
		   addNumAndPageSize(ricercaQuotaSpesa.getParametriPaginazione(), "ricercaSubDocPerNuovaRigaCartaID");
	    }*/
		
		
		return SUCCESS;
	}

	/*
	private RicercaQuotaSpesa convertModelPerChiamataRicercaCarta() {
		RicercaQuotaSpesa ricercaQuotaSpesa = new RicercaQuotaSpesa();
		
		ricercaQuotaSpesa.setRichiedente(sessionHandler.getRichiedente());
		ricercaQuotaSpesa.setEnte(sessionHandler.getEnte());
		
		//Documenti
		
		//Tipo documento
		if(StringUtils.isNotEmpty(model.getCodiceTipoDoc())){
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setUid(Integer.parseInt(model.getCodiceTipoDoc()));
			ricercaQuotaSpesa.setTipoDocumento(tipoDocumento );
		}

		//Anno
		if(StringUtils.isNotEmpty(model.getDatiDocumentoAnno())){
			 ricercaQuotaSpesa.setAnnoDocumento(Integer.parseInt(model.getDatiDocumentoAnno()));
		}
		
		//Numero
		if(StringUtils.isNotEmpty(model.getDatiDocumentoNumero())){
			 ricercaQuotaSpesa.setNumeroDocumento(model.getDatiDocumentoNumero());
		}
	
		//Quota
		if(StringUtils.isNotEmpty(model.getDatiDocumentoQuota())){
			ricercaQuotaSpesa.setNumeroQuota(Integer.parseInt(model.getDatiDocumentoQuota()));
		}
		 
	
		//Data documento
		if(StringUtils.isNotEmpty(model.getDataDocumento())){
			ricercaQuotaSpesa.setDataEmissioneDocumento(DateUtility.parse(model.getDataDocumento()));
		}
		
		//Soggetto
		if(model.getSoggetto()!=null && StringUtils.isNotEmpty(model.getSoggetto().getCodCreditore())){
			Soggetto soggettoRicerca = new Soggetto();
			soggettoRicerca.setCodiceSoggetto(model.getSoggetto().getCodCreditore());
			ricercaQuotaSpesa.setSoggetto(soggettoRicerca);
		}
				
				
		//ELENCO:
		if(StringUtils.isNotEmpty(model.getElencoAnno())){
			ricercaQuotaSpesa.setAnnoElenco(Integer.parseInt(model.getElencoAnno()));
		}
		if(StringUtils.isNotEmpty(model.getElencoNumero())){
			ricercaQuotaSpesa.setNumeroElenco(Integer.parseInt(model.getElencoNumero()));
		}
		
		
		//Provvedimento
		ProvvedimentoImpegnoModel provvedimentoSelezionato = model.getProvvedimento();
		
		if(provvedimentoSelezionato!=null){
			
			//Anno
			if(provvedimentoSelezionato.getAnnoProvvedimento()!=null){
				ricercaQuotaSpesa.setAnnoProvvedimento(provvedimentoSelezionato.getAnnoProvvedimento());
			}
			
			
			//Numero
			if(provvedimentoSelezionato.getNumeroProvvedimento()!=null){
				ricercaQuotaSpesa.setNumeroProvvedimento(provvedimentoSelezionato.getNumeroProvvedimento().intValue());
			}
			
			//Tipo
			if(provvedimentoSelezionato.getTipoProvvedimento()!=null){
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setCodice(provvedimentoSelezionato.getTipoProvvedimento());
				ricercaQuotaSpesa.setTipoAtto(tipoAtto);
			}
			
			//Strutt amm
			if(provvedimentoSelezionato.getStrutturaAmministrativa()!=null){
				StrutturaAmministrativoContabile struttAmmContabile = new StrutturaAmministrativoContabile();
				struttAmmContabile.setCodice(provvedimentoSelezionato.getStrutturaAmministrativa());
				ricercaQuotaSpesa.setStruttAmmContabile(struttAmmContabile );
			}
			
		}
	  
		    
		// paginazione
	    ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
	    addNumAndPageSize(parametriPaginazione, "ricercaSubDocPerNuovaRigaCartaID");
	    ricercaQuotaSpesa.setParametriPaginazione(parametriPaginazione);
		
	    // memorizzo in sessione i parametri con il quale ho lanciato la ricerca
	    sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA, ricercaQuotaSpesaReqToModel(ricercaQuotaSpesa));
		
		return ricercaQuotaSpesa;
	} */


	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		//Auto-generated method stub
		
	}

	@Override
	protected void setErroreCapitolo() {
		//Auto-generated method stub
		
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}
	
	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);		
	}
	
	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}
	
	@Override
	public boolean missioneProgrammaAttivi() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean cupAttivo() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		//Auto-generated method stub
		return false;
	}

	@Override
	public String confermaPdc() {
		//Auto-generated method stub
		return null;
	}

	@Override
	public String confermaSiope() {
		//Auto-generated method stub
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		//Auto-generated method stub
		return false;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}
			
}
