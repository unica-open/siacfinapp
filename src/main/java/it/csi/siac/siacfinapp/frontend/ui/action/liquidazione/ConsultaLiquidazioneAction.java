/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.Date;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.RicercaLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaLiquidazioneAction extends WizardRicercaLiquidazioneAction{

	private static final long serialVersionUID = 1L;
	
	//anno e numero:
	private Integer anno;
	private BigDecimal numero;

	@Override
	public void prepare() throws Exception {
		//richiamo il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Consulta liquidazione");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//1. istanzio la request per il servizio ricercaLiquidazionePerChiave:
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		
		//2. setto i dati nella request:
		RicercaLiquidazioneK prl = new RicercaLiquidazioneK(); 
		Liquidazione liq = new Liquidazione();
		
		liq.setAnnoLiquidazione(anno);
		liq.setNumeroLiquidazione(numero);
		
		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_CONSULTA_LIQUIDAZIONE);
		prl.setLiquidazione(liq);
		prl.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		req.setEnte(sessionHandler.getAccount().getEnte());
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setDataOra(new Date());
		req.setpRicercaLiquidazioneK(prl);
		
		//3. invoco il servizio ricercaLiquidazionePerChiave:
		RicercaLiquidazionePerChiaveResponse response = liquidazioneService.ricercaLiquidazionePerChiave(req);
		
		//4. analizzo la response:
		if(response!=null && response.getLiquidazione()!=null){
			
			//prima pulisco il model:
			model.setAttoAmministrativoConsulta(null);
			model.setSoggettoConsulta(null);
			model.setCapitoloConsulta(null);
			model.setImpegnoConsulta(null);
			model.setNumeroMutuoConsulta(null);
			
			Liquidazione liqConsulta = response.getLiquidazione();
			
			//setto la liquidazione nel model:
			model.setLiquidazioneConsulta(liqConsulta);
			
			//SIOPE PLUS:
	        impostaDatiSiopePlusNelModelDiConsultazione(liqConsulta, model);
			// end siope plus
			
			//Atto Amministrativo
			if(response.getLiquidazione().getAttoAmministrativoLiquidazione()!=null){			
				model.setAttoAmministrativoConsulta(response.getLiquidazione().getAttoAmministrativoLiquidazione());
			}	
			
			//Soggetto
			if(response.getLiquidazione().getSoggettoLiquidazione()!=null){
				model.setSoggettoConsulta(response.getLiquidazione().getSoggettoLiquidazione());
			}	
			
			//Capitolo
			if(response.getLiquidazione().getCapitoloUscitaGestione()!=null){
				model.setCapitoloConsulta(response.getLiquidazione().getCapitoloUscitaGestione());				
			}
			
			//Impegno
			if(response.getLiquidazione().getImpegno()!=null){
				model.setImpegnoConsulta(response.getLiquidazione().getImpegno());
			}		
			
			//Importo
			if(response.getLiquidazione().getImportoLiquidazione()!=null){
				model.setImportoLiquidazioneBigDecimal(response.getLiquidazione().getImportoLiquidazione());
			}
			
			//Disponibilita
			if(response.getLiquidazione().getDisponibilitaPagare()!=null){
				model.setDisponibilita(response.getLiquidazione().getDisponibilitaPagare());
			}
			
			
			//TRANSAZIONE ELEMENTARE:
			model.setTransazioneElementare(FinStringUtils.smartConcatMult(
	        		" - ",
	        		response.getLiquidazione().getCodMissione(),
	        		response.getLiquidazione().getCodProgramma(), 
	        		response.getLiquidazione().getCodPdc(),
	        		response.getLiquidazione().getCodCofog(),
	        		response.getLiquidazione().getCodSiope()));
			
			
			//Numero mutuo
			if(response.getLiquidazione().getNumeroMutuo()!=null){
				model.setNumeroMutuoConsulta(new BigDecimal(response.getLiquidazione().getNumeroMutuo()));
			}
			//Da Aggiustare TO-DO
			if(response.getLiquidazione().getImpegno()!=null && response.getLiquidazione().getImpegno().getElencoSubImpegni()!=null){
				model.setSubImpegnoConsulta(response.getLiquidazione().getImpegno().getElencoSubImpegni().get(0));
			}		
			//mdp
			if(response.getLiquidazione().getSoggettoLiquidazione()!=null && response.getLiquidazione().getSoggettoLiquidazione().getModalitaPagamentoList()!=null){
				model.setMdpConsulta(response.getLiquidazione().getModalitaPagamentoSoggetto());
			}
		}
		
	    return SUCCESS;
	}
	
	protected void impostaDatiSiopePlusNelModelDiConsultazione(Liquidazione liqDaCuiLeggere,RicercaLiquidazioneModel modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(descrizioneMotivazioneAssenzaCig(liqDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(liqDaCuiLeggere.getSiopeTipoDebito()));
	}

	//GETTER E SETTER:
	
	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer annoLiquidazione) {
		this.anno= annoLiquidazione;
	}

	public BigDecimal getNumero() {
		return numero;
	}

	public void setNumero(BigDecimal numeroLiquidazione) {
		this.numero= numeroLiquidazione;
	}
	
}
