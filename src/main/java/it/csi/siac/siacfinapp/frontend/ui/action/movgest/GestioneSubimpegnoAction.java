/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinser.Constanti;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneSubimpegnoAction extends ActionKeyAggiornaImpegno {

	private static final long serialVersionUID = 1L;

	public GestioneSubimpegnoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBIMPEGNO;
	}
	
	

	public String annulla() throws Exception {
		if(model.getStep1ModelSubimpegno().getUid() == null){
	    	setInserimentoSubimpegno(true);
	    	model.getStep1ModelSubimpegno().setOperazioneInserimento(true);
		}else{
			setIdSubImpegno(model.getStep1ModelSubimpegno().getUid());
		}
		executeSub();
		caricaLabelsAggiornaSub(1);
	    return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		
	}
	
	
	public boolean verificaInserimentoSub(){
		
		return !model.getStep1ModelSubimpegno().isOperazioneInserimento();
	}
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		executeSub(true);
		
		
		if(model.getStep1ModelSubimpegnoCache()!= null && model.getStep1ModelSubimpegnoCache().getSoggetto().getCodCreditore()!= null && 
				StringUtils.isNotEmpty(model.getStep1ModelSubimpegnoCache().getSoggetto().getCodCreditore())){
			model.setStep1ModelSubimpegno(clone(model.getStep1ModelSubimpegnoCache()));
			model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		}
		
		caricaLabelsAggiornaSub(1);
		model.getStep1ModelSubimpegno().setOperazioneInserimento(inserimentoSubimpegno);
		//SIAC-5943
		model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
		model.setSaltaInserimentoPrimaNota(false);
	    return SUCCESS;
	}
	
	public boolean isAbilitatoAggiornamentoCampiSiopePlus(){
		//il siope pluse e' aggiornabile se:
		if(model.getStep1ModelSubimpegno().getUid()==null || model.getStep1ModelSubimpegno().getUid()==0){
			//alla creazione di un nuovo sub lasciamo che si possa modificare il siope
			return true;
		} else {
			//in aggiornamento e' modificabile se il sub e' provvisorio:
			//return isSubImpegnoInAggiornamentoProvvisorio();
			
			if(isSubImpegnoInAggiornamentoProvvisorio()){
				//se e' provvisorio ritorno direttamente true
				return true;
			}
			
			//se sono qui e' definitivo:
			BigDecimal disponibilitaLiqDaFunction = model.getStep1ModelSubimpegno().getDisponibileLiquidareDaFunction();
			BigDecimal importoAttualeSub = model.getStep1ModelSubimpegno().getImportoImpegnoMod();
			
			if(disponibilitaLiqDaFunction.compareTo(importoAttualeSub)==0){
				return true;
			} else {
				return false;
			}
			
		}
	}
	
	/**
	 * semplice utility per verificare se lo stato del sub impegno in aggiornamento e' o meno provvisorio
	 * @return
	 */
	private boolean isSubImpegnoInAggiornamentoProvvisorio(){
		if(Constanti.MOVGEST_STATO_PROVVISORIO.equals(model.getStep1ModelSubimpegno().getStatoOperativo())){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 *  funzione salva
	 * @return
	 */
	public String salva() {
		return salva(true);
	}
	
	
	//jira 945
	public String siProsegui() throws Exception{
		setMethodName("siProsegui");

		setFromPopup(true);
		
		//cr-3224
		setFromModaleConfermaProseguiModificaProvvedimento(true);
		return prosegui();
	}

	//jira 945
	public String siSalva() throws Exception{
		setMethodName("siSalva");
		setFromPopup(true);
		
		//cr-3224
		setFromModaleConfermaModificaProvvedimento(true);
		setProseguiStep1(true);
		return salva();
	}
	
}