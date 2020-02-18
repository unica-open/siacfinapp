/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MutuoConsulta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AccertamentoDettaglioImporti;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ImpegnoDettaglioImporti;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;


public class ConsultaMovimentiModel extends MovGestModel {
	
	private static final long serialVersionUID = 1L;
	
	private final MovimentoConsulta movimento = new MovimentoConsulta();
	
	private AccertamentoDettaglioImporti dettaglioImportiAcc;
	private ImpegnoDettaglioImporti dettaglioImporti;
	
	private AccertamentoDettaglioImporti dettaglioImportiSubAccSelezionato;
	private ImpegnoDettaglioImporti dettaglioImportiSubSelezionato;
	
	private BigDecimal totImportiAttualiSubValidi;
	
	private List<MovimentoConsulta> listaSub = new ArrayList<MovimentoConsulta>();
	private List<ModificaConsulta> listaModifiche = new ArrayList<ModificaConsulta>(); 
	private List<MutuoConsulta> listaMutui  = new ArrayList<MutuoConsulta>();
	
	//LISTE PER I TAB DEI VINCOLI:
	private List<VincoloImpegno> listaVincoliImpegno = new ArrayList<VincoloImpegno>();
	private List<VincoloAccertamento> listaVincoliAccertamento = new ArrayList<VincoloAccertamento>();
	//
	
	//Tot vincoli accertamento:
	private BigDecimal totVincoliAccertamento;
	//
	
	//valore non ancora vincolato:
	private BigDecimal quotaNonVincolataAccertamento;
	
	private MovimentoConsulta	subDettaglio = new MovimentoConsulta();
	private ModificaConsulta 	modificaDettaglio = new ModificaConsulta();
	
	private List<AttoAmministrativoStoricizzato> listaModificheProvvedimento = new ArrayList<AttoAmministrativoStoricizzato>();
	
    /*************************** GESTIONE DINAMICA  *****************************/
    private Map<String, String> labels = new HashMap<String, String>();
    /****************************************************************************/
    
    //SIAC-6702
    private int resultSize;
    

	/* **************************************************************************** */
	/*   Getter / Setter															*/
	/* **************************************************************************** */
	
	
	
	public List<MovimentoConsulta> getListaSub() {
		return listaSub;
	}
	/**
	 * @return the listaModificheProvvedimento
	 */
	public List<AttoAmministrativoStoricizzato> getListaModificheProvvedimento() {
		return listaModificheProvvedimento;
	}
	/**
	 * @param listaModificheProvvedimento the listaModificheProvvedimento to set
	 */
	public void setListaModificheProvvedimento(
			List<AttoAmministrativoStoricizzato> listaModificheProvvedimento) {
		this.listaModificheProvvedimento = listaModificheProvvedimento;
	}
	public void setListaSub(List<MovimentoConsulta> listaSub) {
		this.listaSub = listaSub;
	}
	public List<ModificaConsulta> getListaModifiche() {
		return listaModifiche;
	}
	public void setListaModifiche(List<ModificaConsulta> listaModifiche) {
		this.listaModifiche = listaModifiche;
	}
	public MovimentoConsulta getSubDettaglio() {
		return subDettaglio;
	}
	public void setSubDettaglio(MovimentoConsulta subDettaglio) {
		this.subDettaglio = subDettaglio;
	}
	public ModificaConsulta getModificaDettaglio() {
		return modificaDettaglio;
	}
	public void setModificaDettaglio(ModificaConsulta modificaDettaglio) {
		this.modificaDettaglio = modificaDettaglio;
	}
	public MovimentoConsulta getMovimento() {
		return movimento;
	}
	
	public List<VincoloImpegno> getListaVincoliImpegno() {
		return listaVincoliImpegno;
	}
	/**
	 * @return the listaMutui
	 */
	public List<MutuoConsulta> getListaMutui() {
		return listaMutui;
	}
	/**
	 * @param listaMutui the listaMutui to set
	 */
	public void setListaMutui(List<MutuoConsulta> listaMutui) {
		this.listaMutui = listaMutui;
	}
	public void setListaVincoliImpegno(List<VincoloImpegno> listaVincoliImpegno) {
		this.listaVincoliImpegno = listaVincoliImpegno;
	}
	public Map<String, String> getLabels() {
		return labels;
	}
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	public ImpegnoDettaglioImporti getDettaglioImporti() {
		return dettaglioImporti;
	}
	public void setDettaglioImporti(ImpegnoDettaglioImporti dettaglioImporti) {
		this.dettaglioImporti = dettaglioImporti;
	}
	public BigDecimal getTotImportiAttualiSubValidi() {
		return totImportiAttualiSubValidi;
	}
	public void setTotImportiAttualiSubValidi(BigDecimal totImportiAttualiSubValidi) {
		this.totImportiAttualiSubValidi = totImportiAttualiSubValidi;
	}
	public ImpegnoDettaglioImporti getDettaglioImportiSubSelezionato() {
		return dettaglioImportiSubSelezionato;
	}
	public void setDettaglioImportiSubSelezionato(
			ImpegnoDettaglioImporti dettaglioImportiSubSelezionato) {
		this.dettaglioImportiSubSelezionato = dettaglioImportiSubSelezionato;
	}
	public AccertamentoDettaglioImporti getDettaglioImportiAcc() {
		return dettaglioImportiAcc;
	}
	public void setDettaglioImportiAcc(
			AccertamentoDettaglioImporti dettaglioImportiAcc) {
		this.dettaglioImportiAcc = dettaglioImportiAcc;
	}
	public AccertamentoDettaglioImporti getDettaglioImportiSubAccSelezionato() {
		return dettaglioImportiSubAccSelezionato;
	}
	public void setDettaglioImportiSubAccSelezionato(
			AccertamentoDettaglioImporti dettaglioImportiSubAccSelezionato) {
		this.dettaglioImportiSubAccSelezionato = dettaglioImportiSubAccSelezionato;
	}
	public List<VincoloAccertamento> getListaVincoliAccertamento() {
		return listaVincoliAccertamento;
	}
	public void setListaVincoliAccertamento(
			List<VincoloAccertamento> listaVincoliAccertamento) {
		this.listaVincoliAccertamento = listaVincoliAccertamento;
	}
	public BigDecimal getTotVincoliAccertamento() {
		return totVincoliAccertamento;
	}
	public void setTotVincoliAccertamento(BigDecimal totVincoliAccertamento) {
		this.totVincoliAccertamento = totVincoliAccertamento;
	}
	public BigDecimal getQuotaNonVincolataAccertamento() {
		return quotaNonVincolataAccertamento;
	}
	public void setQuotaNonVincolataAccertamento(
			BigDecimal quotaNonVincolataAccertamento) {
		this.quotaNonVincolataAccertamento = quotaNonVincolataAccertamento;
	}
	

	public int  getResultSize() {
		return this.resultSize;
	}
	
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	
	
	/**
	 * Gets the abilita modifiche storico.
	 *
	 * @return the abilita modifiche storico
	 */
	public boolean getAbilitaModificheStorico() {
		return false;
	}
	
	/**
	 * Crea request ricerca storico impegno accertamento.
	 *
	 * @param bilancio the bilancio
	 * @param imp the imp
	 * @param acc the acc
	 * @return the ricerca storico impegno accertamento
	 */
	public RicercaStoricoImpegnoAccertamento creaRequestRicercaStoricoImpegnoAccertamento(Bilancio bilancio,
			Impegno imp, Accertamento acc) {
		RicercaStoricoImpegnoAccertamento requestSt = creaRequest(RicercaStoricoImpegnoAccertamento.class);
		requestSt.setEnte(ente);
		requestSt.setParametroRicercaStoricoImpegnoAccertamento(new ParametroRicercaStoricoImpegnoAccertamento());
		requestSt.getParametroRicercaStoricoImpegnoAccertamento().setStoricoImpegnoAccertamento(new StoricoImpegnoAccertamento());
		requestSt.getParametroRicercaStoricoImpegnoAccertamento().getStoricoImpegnoAccertamento().setImpegno(imp);
		requestSt.getParametroRicercaStoricoImpegnoAccertamento().getStoricoImpegnoAccertamento().setAccertamento(acc);
		requestSt.getParametroRicercaStoricoImpegnoAccertamento().setBilancio(bilancio);
		return requestSt;
	}
	
}
