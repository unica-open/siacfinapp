/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ReintroitoOrdinativoModel;

public class WizardReintroitoOrdinativoAction extends GenericOrdinativoAction<ReintroitoOrdinativoModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * Metodo per verificare se e' stato
	 * popolato l'ordinativo da reintroitare tramite
	 * il cerca ordinativo
	 * 
	 * @return
	 */
	public boolean isOrdinativoPresente(){
		boolean presente= model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare() != null
				&& model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare().getNumero()!=null;
		return presente;
	}
	
	public boolean isDocumentiCollegatiPresenti(){
		return model.getReintroitoOrdinativoStep1Model().getDocumentiCollegati() != null
				&& !model.getReintroitoOrdinativoStep1Model().getDocumentiCollegati().isEmpty();
	}
	
	/**
	 * oggettoDaPopolarePagamento
	 * @return
	 */
	public boolean oggettoDaPopolarePagamento(){
		//tipologia pagamento
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO);
	}
	

	@Override
	public String listaClasseSoggettoChanged() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String listaClasseSoggettoDueChanged() {
		// Auto-generated method stub
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
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
		
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
		
	}

	/**
	 * Setta nel model dello step 1 il provvedimento selezionato
	 * 
	 * Implemente l'interfaccia generica per le action con provvedimenti
	 * 
	 */
	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.getReintroitoOrdinativoStep1Model().setProvvedimento(currentProvvedimento);
		model.getReintroitoOrdinativoStep1Model().setProvvedimentoSelezionato(true);
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
	
}