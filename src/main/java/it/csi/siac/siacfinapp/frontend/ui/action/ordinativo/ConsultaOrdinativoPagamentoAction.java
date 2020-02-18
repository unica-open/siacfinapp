/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ConsultaOrdinativoPagamentoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaOrdinativoPagamentoAction extends WizardConsultaOrdinativoAction<ConsultaOrdinativoPagamentoModel> {

	private static final long serialVersionUID = -8192912865379160597L;
	
	protected static final String TIPO_ORDINATIVO_PAGAMENTO_P = "P";

	public String getActionKey() {
		return "consultaOrdinativoPagamento";
	}

	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Consulta ordinativo");
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		setMethodName("execute");

		//servizio di ricerca:
		RicercaOrdinativoPagamentoPerChiaveResponse response = getOrdinativoPagamento();
		
		if(response == null) {
			// Errori nel caricamento dei dati: esco
			return "input";
		}

		//ordinativo trovato:
		OrdinativoPagamento ordinativoPagamento = response.getOrdinativoPagamento();

		//info per diagnostica:
		if(log.isDebugEnabled()){
			if(ordinativoPagamento.getElencoSubOrdinativiDiPagamento().get(0)!=null &&
					ordinativoPagamento.getElencoSubOrdinativiDiPagamento().get(0).getSubDocumentoSpesa()!=null)  {
				if(ordinativoPagamento.getElencoSubOrdinativiDiPagamento().get(0).getSubDocumentoSpesa().getDocumento()!=null){
					log.debug(methodName," Numero Doc: "+ordinativoPagamento.getElencoSubOrdinativiDiPagamento().get(0).getSubDocumentoSpesa().getDocumento().getNumero());
					log.debug(methodName," Anno Doc: "+ordinativoPagamento.getElencoSubOrdinativiDiPagamento().get(0).getSubDocumentoSpesa().getDocumento().getAnno());
				}
			
			}
		}
		
		// leggo la TE Missione/Programma
		
		// MAGGIO 2017 DALLA JIRA SIAC-4693 PRENDO MISSIONE E PROGRAMMA DAL CAPITOLO DELL'ORDINATIVO:
		// LA JIRA RIPORTA: L'informazione e' reperibile passando dal capitolo in relazione con l'ordinativo (r_ordinativo_bil_elem)
		
		//TOLTO IL VECCHIO CODICE CHE PRENDEVA CODICE E PROGRAMMA DALLA LIQUIDAZIONE
		
		//E USO QUELLI DEL CAPITOLO (nel servizio sono settati in ordinativoPagamento):
		model.setDescMissione(ordinativoPagamento.getDescMissione());
		model.setCodMissione(ordinativoPagamento.getCodMissione());
		model.setCodProgramma(ordinativoPagamento.getCodProgramma());
		model.setDescProgramma(ordinativoPagamento.getDescProgramma());
		//
		
		//GENNAIO 2018 - SOLO PER IL CONSULAT CREO L'ELENCO DI TUTTI
		//GLI ORDINATIVI COLLEGATI IN QUALSIASI VERSO,
		//ESIGENZA NATA PER I REINTROITI:
		List<Ordinativo> allOrd = addAllConNew(ordinativoPagamento.getElencoOrdinativiACuiSonoCollegato(), ordinativoPagamento.getElencoOrdinativiCollegati());
		ordinativoPagamento.setElencoOrdinativiCollegati(allOrd);
		//
		
		//setto l'ordinativo nel model:
		model.setOrdinativoPagamento(ordinativoPagamento);
		
		model.setTotaleOrdinativiCollegati(ordinativoPagamento.getTotaleOrdinativiCollegati());
		
		//SIOPE PLUS:
		impostaDatiSiopePlusNelModelDiConsultazione(ordinativoPagamento, model);
		//END SIOPE PLUS
		
		//carichiamo le liste dei classificatori:
		caricaListeFinOrdinativoPagamento();

		return "success";
	}
	
	protected void impostaDatiSiopePlusNelModelDiConsultazione(OrdinativoPagamento ordDaCuiLeggere,ConsultaOrdinativoPagamentoModel modelInCuiImpostare){
		
		//CIG:
		modelInCuiImpostare.setCig(ordDaCuiLeggere.getCig());
		
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(descrizioneMotivazioneAssenzaCig(ordDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(ordDaCuiLeggere.getSiopeTipoDebito()));
	}
	
	public boolean isCupValido(String cup){
		return FinUtility.cupValido(cup);
	}
	
	public boolean isCigValido(String cig){
		return FinUtility.cigValido(cig);
	}
	
	/**
	 * caricamento delle liste fin relativo all'ordinativo
	 * @return
	 */
	protected boolean caricaListeFinOrdinativoPagamento() {
		
		//compongo la request per il servizio di ricerca dei classificatori:
		LeggiClassificatoriGenericiByTipoOrdinativoGest ll = new LeggiClassificatoriGenericiByTipoOrdinativoGest();
		ll.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		ll.setCodiceTipoOrdinativoGestione(TIPO_ORDINATIVO_PAGAMENTO_P);
		ll.setDataOra(new Date());
		ll.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		ll.setRichiedente(sessionHandler.getRichiedente());
		
		//richiamo il servizio per cercare i classificatori generici:
		LeggiClassificatoriGenericiByTipoOrdinativoGestResponse respClassFin =  classificatoreFinService.leggiClassificatoriGenericiByTipoOrdinativoGest(ll);
		if(respClassFin.isFallimento() || (respClassFin.getErrori() != null && respClassFin.getErrori().size() > 0)) {
			//errori nel servizio
			return true;
		}
		
		//SETTO I RISULTATI NEL MODEL:
		
		//CLASSIFICATORI GENERICI 21:
		if (null != respClassFin.getClassificatoriGenerici21() && respClassFin.getClassificatoriGenerici21().size() > 0) {
			model.setListaClassificatoriGen21(respClassFin.getClassificatoriGenerici21());
		}else{
			model.setListaClassificatoriGen21(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 22:
		if (null != respClassFin.getClassificatoriGenerici22() && respClassFin.getClassificatoriGenerici22().size() > 0) {
			model.setListaClassificatoriGen22(respClassFin.getClassificatoriGenerici22());
		}else{
			model.setListaClassificatoriGen22(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 23:
		if (null != respClassFin.getClassificatoriGenerici23() && respClassFin.getClassificatoriGenerici23().size() > 0) {
			model.setListaClassificatoriGen23(respClassFin.getClassificatoriGenerici23());
		}else{
			model.setListaClassificatoriGen23(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 24:
		if (null != respClassFin.getClassificatoriGenerici24() && respClassFin.getClassificatoriGenerici24().size() > 0) {
			model.setListaClassificatoriGen24(respClassFin.getClassificatoriGenerici24());
		}else{
			model.setListaClassificatoriGen24(new ArrayList<ClassificatoreGenerico>());
		}
		
		//CLASSIFICATORI GENERICI 25:
		if (null != respClassFin.getClassificatoriGenerici25() && respClassFin.getClassificatoriGenerici25().size() > 0) {
			model.setListaClassificatoriGen25(respClassFin.getClassificatoriGenerici25());
		}else{
			model.setListaClassificatoriGen25(new ArrayList<ClassificatoreGenerico>());
		}
		
		if (null != respClassFin.getClassificatoriStipendi()&& respClassFin.getClassificatoriStipendi().size() > 0) {
			model.setListaClassificatoriStipendi(respClassFin.getClassificatoriStipendi());
		}else{
			model.setListaClassificatoriStipendi(new ArrayList<ClassificatoreStipendi>());
		}
		
		return false;
	}

	/**
	 * per il dettaglio ordinativo inc
	 * @return
	 */
	public String dettaglioQuoteOrdinativoPagamentoInc() {
		debug("dettaglioQuoteOrdinativoPagamentoInc", "numero "+ getNumeroOrdinativoSelezionato());
		//verifico se ci sono ordinativi collegati:
		if (model.getOrdinativoPagamento().getElencoOrdinativiCollegati() != null) {
			//itero su essi:
			for (Ordinativo ord : model.getOrdinativoPagamento().getElencoOrdinativiCollegati()) {
				//il controllo instance of e' per evitare di incappare nel raro caso di
				//ordinativi di incasso e pagamento con lo stesso numero
				if (FinUtility.instanceofIncasso(ord) && ord.getNumero() == Integer.parseInt(getNumeroOrdinativoSelezionato())) {
					//trovato quello selezionato
					OrdinativoIncasso ordInc = (OrdinativoIncasso) ord;
					model.setOrdinativoCollegatoSelezionato(ordInc);
				}
			}
		}
		return "dettaglioQuoteOrdinativoPagamentoInc";
	}

	/**
	 * per il dettaglio ordinativo pag
	 * @return
	 */
	public String dettaglioQuoteOrdinativoPagamentoPag() {
		debug("dettaglioQuoteOrdinativoPagamentoPag", "numero "+ getNumeroOrdinativoSelezionato());
		//verifico se ci sono ordinativi collegati:
		if (model.getOrdinativoPagamento().getElencoOrdinativiCollegati() != null) {
			//itero su essi:
			for (Ordinativo ord : model.getOrdinativoPagamento().getElencoOrdinativiCollegati()) {
				//il controllo instance of e' per evitare di incappare nel raro caso di
				//ordinativi di incasso e pagamento con lo stesso numero
				if (FinUtility.instanceofPagamento(ord) && ord.getNumero() == Integer.parseInt(getNumeroOrdinativoSelezionato())) {
					//trovato quello selezionato
					OrdinativoPagamento ordPag = (OrdinativoPagamento) ord;
					model.setOrdinativoCollegatoSelezionato(ordPag);
				}
			}
		}
		return "dettaglioQuoteOrdinativoPagamentoPag";
	}

	/**
	 * Si occupa di gestire il richiamo del servizio 
	 * di ricerca e ritornarne la response
	 * @return
	 */
	private RicercaOrdinativoPagamentoPerChiaveResponse getOrdinativoPagamento() {
		
		//inizializzo la request:
		RicercaOrdinativoPagamentoPerChiave request = new RicercaOrdinativoPagamentoPerChiave();

		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();

		//bilancio
		Bilancio bilancio = this.sessionHandler.getBilancio();

		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		
		//numero ordinativo da cercare
		ordinativoPagamento.setNumero(Integer.parseInt(numero));
		
		//anno ordinativo da cercare
		ordinativoPagamento.setAnno(Integer.parseInt(anno));

		pRicercaOrdinativoPagamentoK.setBilancio(bilancio);
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);

		//ente
		request.setEnte(this.sessionHandler.getEnte());
		
		//richiedente
		request.setRichiedente(this.sessionHandler.getRichiedente());
		request.setDataOra(new Date());
		request.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		
		//GENNAIO 2018 - REINTROITI: richiedo anche gli ordinativi nell'altro verso
		//della realazione DA - A:
		request.setCaricaOrdinativiACuiSonoCollegato(true);
		
		// SIAC-6328
		request.setPaginazioneOrdinativiCollegati(new ParametriPaginazione(readPageNumberTableId("ordCollID") - 1, 5));
		
		//invocazione del servizio:
		RicercaOrdinativoPagamentoPerChiaveResponse response = this.ordinativoService.ricercaOrdinativoPagamentoPerChiave(request);

		//analisi della response del servizio:
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0)) || response.getOrdinativoPagamento()== null) {
			//ci sono errori
			debug(this.methodName, new Object[] { "Errore imprevisto nella risposta del servizio" });
			addErrori(this.methodName, response);
			return null;
		}

		//tutto ok
		return response;
	}

}
