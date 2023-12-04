/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class NuovaRigaDaDocumentiStep2Action extends ActionKeyNuovaRigaDaDocumentiAction{
	

	private static final long serialVersionUID = 1L;
	
	private String pulisciPagine;
	private boolean clearPagina;
	
	private String ckRigaSelezionata;
	
	public NuovaRigaDaDocumentiStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		buildTitoloDiPagina();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		if(null!=pulisciPagine && pulisciPagine.equalsIgnoreCase(WebAppConstants.Si)){
			setClearPagina(true);
		}
		
		//se e' presente la paginazione nell'elenco allora la mantengo in sessione
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			
			// ho cliccato sulla paginazione
			int paginata = paginazioneRichiesta(ServletActionContext.getRequest());
			if(paginata!=0){
				model.setPremutoPaginazione(paginata);
			}
			
		}
		
		//exeguiamo la ricerca:
		executeRicercaSubDocumentiDaAssociare();
		
		
		// pulisce le liste quando effettuo una ricerca
		puliscoDatiRicerca();
		
	    return SUCCESS;
	}
	
	private void puliscoDatiRicerca(){
		// pulisci il form di ricerca con 
		// tutti i suoi campi
		model.setProvvedimento(new  ProvvedimentoImpegnoModel());
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setProvvedimentoSelezionato(false);
		model.setSoggettoSelezionato(false);
		model.setAnnoImpegno(null);
		// jira-1380
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		model.setnImpegno(null);
		model.setnSubImpegno(null);
		model.setSoggetto(new SoggettoImpegnoModel());
	}
	
	public String inserisciInCarta(){
		
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		
		//CONTROLLIAMO LA SELEZIONE:
		if(StringUtils.isEmpty(getCkRigaSelezionata())){
			//KO
			addErrore(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore(""));	
		}
		if(hasActionErrors()) return INPUT;
		//
		
		String[] righeSelezionate = getCkRigaSelezionata().split(",");
		
		List<SubdocumentoSpesa> subDocSelezionati = FinUtility.getById(model.getElencoSubdocumentoSpesa(), righeSelezionate);
		
		List<PreDocumentoCarta> righeDaDoc = new ArrayList<PreDocumentoCarta>();
		
		for(SubdocumentoSpesa itSel : subDocSelezionati){
			PreDocumentoCarta itRiga = popolaRigaDaDocumento(itSel);
			righeDaDoc.add(itRiga);
		}
		
		//salviamo la selezione:
		putListaPreDocDaDocumentiInSession(righeDaDoc);
		
		clearActionData();
		
		//Rimuoviamo la fotocopia del model che usiamo per comunicare i dati al sotto caso d'uso nuova riga da documenti:
		ActionContext.getContext().getSession().remove("KEY_GESTIONE_CARTA_MODEL");

		return "vaiStep2";
	}
	
	
	private PreDocumentoCarta popolaRigaDaDocumento(SubdocumentoSpesa subDocumento){
		
		PreDocumentoCarta riga = new PreDocumentoCarta();
		DocumentoSpesa docSpesa = subDocumento.getDocumento();
		
		//DESCRIZIONE:
		riga.setDescrizione(subDocumento.getDescrizione());
		
		//documenti, collegato al sub doc da cui deriva:
		List<SubdocumentoSpesa> subDocs = new ArrayList<SubdocumentoSpesa>();
		subDocs.add(subDocumento);
		riga.setListaSubDocumentiSpesaCollegati(subDocs);
		
		//IMPORTO:
		riga.setImporto(subDocumento.getImporto());
		
		//campi Soggetto
		Soggetto soggetto = docSpesa.getSoggetto();
		if(soggetto!=null && !FinStringUtils.isEmpty(soggetto.getCodiceSoggetto())){			
			riga.setSoggetto(new Soggetto());
			riga.getSoggetto().setCodiceSoggetto(soggetto.getCodiceSoggetto());
			if(!FinStringUtils.isEmpty(soggetto.getDenominazione())){
				riga.getSoggetto().setDenominazione(soggetto.getDenominazione());
			}
			List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoList.add(subDocumento.getModalitaPagamentoSoggetto());
			riga.getSoggetto().setElencoModalitaPagamento(modalitaPagamentoList);
		}
		
		//campi impegno
		Impegno impegno = subDocumento.getImpegno();
		SubImpegno subImp = subDocumento.getSubImpegno();
		if(impegno!=null && impegno.getAnnoMovimento()!=0 && impegno.getNumeroBigDecimal()!=null){
			riga.setImpegno(new Impegno());
			riga.getImpegno().setNumeroBigDecimal(impegno.getNumeroBigDecimal());
			riga.getImpegno().setAnnoMovimento( impegno.getAnnoMovimento());
			if(subImp!=null){
				SubImpegno subImpegno = new SubImpegno();
				List<SubImpegno> listSubImpegni = new ArrayList<SubImpegno>();
				subImpegno.setNumeroBigDecimal(subImp.getNumeroBigDecimal());
				listSubImpegni.add(subImpegno);
				riga.getImpegno().setElencoSubImpegni(listSubImpegni);
			}			
		}

		//CONTO TESORERIA
		if(subDocumento.getContoTesoreria()!=null){
			ContoTesoreria contoTesoreria = new ContoTesoreria();
			contoTesoreria.setCodice(subDocumento.getContoTesoreria().getCodice());
			riga.setContoTesoreria(contoTesoreria);
		} else {
			riga.setContoTesoreria(null);
		}
		
		//MODALITA PAGAMENTO SOGGETTO
		riga.setModalitaPagamentoSoggetto(subDocumento.getModalitaPagamentoSoggetto());
		
		return riga;
	}	
	
	
	public String indietro(){
		//Rimuovo gli eventuali vecchi selezionati salvati in session:
		removeListaPreDocDaDocumentiInSession();
		//
		return "vaiInserisciRigaDaDocumentoStep1";
	}
	

	public String getPulisciPagine() {
		return pulisciPagine;
	}

	public void setPulisciPagine(String pulisciPagine) {
		this.pulisciPagine = pulisciPagine;
	}

	public boolean isClearPagina() {
		return clearPagina;
	}

	public void setClearPagina(boolean clearPagina) {
		this.clearPagina = clearPagina;
	}

	public String getCkRigaSelezionata() {
		return ckRigaSelezionata;
	}

	public void setCkRigaSelezionata(String ckRigaSelezionata) {
		this.ckRigaSelezionata = ckRigaSelezionata;
	}
	
}
