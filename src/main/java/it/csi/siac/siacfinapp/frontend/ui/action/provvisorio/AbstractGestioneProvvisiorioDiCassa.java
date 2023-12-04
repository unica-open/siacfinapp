/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.GestioneProvvisorioModel;

public class AbstractGestioneProvvisiorioDiCassa extends GenericPopupAction<GestioneProvvisorioModel> {

	private static final long serialVersionUID = 1L;
	
	
	protected List<Errore> controlliObbligatorietaProvvisorioDiCassa(List<Errore> listaErrori ){
		//Dati obbligatori:
		if(StringUtils.isEmpty(model.getTipoDocumentoProv())){	
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento obbligatorio"));
		}
		if(model.getNumeroProvvisorio() == null || model.getNumeroProvvisorio().longValue()<=0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero provvisorio"));
		}
		if(StringUtils.isEmpty(model.getImportoFormattato())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
		}
		
		return listaErrori;
	}
	

	@Override
	public String listaClasseSoggettoChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		// Auto-generated method stub
	}

	@Override
	protected void setErroreCapitolo() {
		// Auto-generated method stub
		
	}

	@Override
	protected void setSoggettoSelezionato(
			SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
		
	}

	@Override
	protected void setProvvedimentoSelezionato(
			ProvvedimentoImpegnoModel currentProvvedimento) {
		// Auto-generated method stub
		
	}

	@Override
	public boolean missioneProgrammaAttivi() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean cupAttivo() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public String confermaPdc() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String confermaSiope() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		// Auto-generated method stub
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
