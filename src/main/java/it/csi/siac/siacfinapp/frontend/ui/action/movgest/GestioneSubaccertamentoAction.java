/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneSubaccertamentoAction extends ActionKeyAggiornaAccertamento {

	private static final long serialVersionUID = 1L;

	public GestioneSubaccertamentoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.SUBACCERTAMENTO;
	}
	
	/**
	 * funzione di annulla subAccertamento
	 */
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
	}
	
	public boolean verificaInserimentoSub(){
		
		return !model.getStep1ModelSubimpegno().isOperazioneInserimento();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		model.getStep1ModelSubimpegno().setOperazioneInserimento(inserimentoSubimpegno);
		executeSub();
		
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		
		if(model.getStep1ModelSubimpegnoCache()!= null && model.getStep1ModelSubimpegnoCache().getSoggetto().getCodCreditore()!= null && 
				StringUtils.isNotEmpty(model.getStep1ModelSubimpegnoCache().getSoggetto().getCodCreditore())){
			model.setStep1ModelSubimpegno(clone(model.getStep1ModelSubimpegnoCache()));
		}
		
		model.getStep1ModelSubimpegno().setOperazioneInserimento(inserimentoSubimpegno);
		
		caricaLabelsAggiornaSub(1);
		//SIAC-5943
		model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
		model.setSaltaInserimentoPrimaNota(false);
	    return SUCCESS;
	}
	
	/**
	 * funznioe di salvataggio sub accertamento
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