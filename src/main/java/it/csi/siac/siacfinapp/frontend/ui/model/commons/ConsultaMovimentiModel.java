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

import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.movimentogestione.MutuoAssociatoMovimentoGestione;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.DettaglioImportiAccertamento;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.DettaglioImportiImpegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.StrutturaAmmContabileFlatStoricizzato;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;


public class ConsultaMovimentiModel extends MovGestModel {
	
	private static final long serialVersionUID = 1L;
	
	private final MovimentoConsulta movimento = new MovimentoConsulta();
	
	private DettaglioImportiAccertamento dettaglioImportiAcc;
	private DettaglioImportiImpegno dettaglioImporti;
	
	private DettaglioImportiAccertamento dettaglioImportiSubAccSelezionato;
	private DettaglioImportiImpegno dettaglioImportiSubSelezionato;
	
	private BigDecimal totImportiAttualiSubValidi;
	
	private List<MovimentoConsulta> listaSub = new ArrayList<MovimentoConsulta>();
	private List<ModificaConsulta> listaModifiche = new ArrayList<ModificaConsulta>(); 
	
	//LISTE PER I TAB DEI VINCOLI:
	private List<VincoloImpegno> listaVincoliImpegno = new ArrayList<VincoloImpegno>();
	private List<VincoloAccertamento> listaVincoliAccertamento = new ArrayList<VincoloAccertamento>();
	//

	//SIAC-8650:
	private List<VincoloImpegno> listaVincoliImpegnoOriginari = new ArrayList<VincoloImpegno>();
	private List<VincoloAccertamento> listaVincoliAccertamentoOriginari = new ArrayList<VincoloAccertamento>();
	private Impegno primoImpCatena;
	private Accertamento primoAccCatena;
	//
	
	//SIAC-6997
	private List<StrutturaAmmContabileFlatStoricizzato> listaModificheStruttureCompetenti = new ArrayList<StrutturaAmmContabileFlatStoricizzato>();
	Impegno impegnoConsulta = new Impegno();
	Accertamento accertamentoConsulta = new Accertamento();
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
    
    private Map<Integer, List<ModificaMovimentoGestioneSpesaCollegata>> mapModificheSpesaCollegate = new HashMap<Integer, List<ModificaMovimentoGestioneSpesaCollegata>>();

	/* **************************************************************************** */
	/*   Getter / Setter															*/
	/* **************************************************************************** */
	
    // task-93

    private List<MutuoAssociatoMovimentoGestione> elencoMutuiAssociati = new ArrayList<MutuoAssociatoMovimentoGestione>() ;
    
	//SIAC-6997
	/**
	 * @return the listaModificheStruttureCompetenti
	 */
	public List<StrutturaAmmContabileFlatStoricizzato> getListaModificheStruttureCompetenti() {
		return listaModificheStruttureCompetenti;
	}

	/**
	 * @param listaModificheStruttureCompetenti the listaModificheStruttureCompetenti to set
	 */
	public void setListaModificheStruttureCompetenti(
			List<StrutturaAmmContabileFlatStoricizzato> listaModificheStruttureCompetenti) {
		this.listaModificheStruttureCompetenti = listaModificheStruttureCompetenti;
	}
	
	/**
	 * @return the accertamentoConsulta
	 */
	public Accertamento getAccertamentoConsulta() {
		return accertamentoConsulta;
	}

	/**
	 * @param accertamentoConsulta the accertamentoConsulta to set
	 */
	public void setAccertamentoConsulta(Accertamento accertamentoConsulta) {
		this.accertamentoConsulta = accertamentoConsulta;
	}

	/**
	 * @return the impegnoConsulta
	 */
	public Impegno getImpegnoConsulta() {
		return impegnoConsulta;
	}

	/**
	 * @param impegnoConsulta the impegnoConsulta to set
	 */
	public void setImpegnoConsulta(Impegno impegnoConsulta) {
		this.impegnoConsulta = impegnoConsulta;
	}

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


	public void setListaVincoliImpegno(List<VincoloImpegno> listaVincoliImpegno) {
		this.listaVincoliImpegno = listaVincoliImpegno;
	}
	public Map<String, String> getLabels() {
		return labels;
	}
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	public DettaglioImportiImpegno getDettaglioImporti() {
		return dettaglioImporti;
	}
	public void setDettaglioImporti(DettaglioImportiImpegno dettaglioImporti) {
		this.dettaglioImporti = dettaglioImporti;
	}
	public BigDecimal getTotImportiAttualiSubValidi() {
		return totImportiAttualiSubValidi;
	}
	public void setTotImportiAttualiSubValidi(BigDecimal totImportiAttualiSubValidi) {
		this.totImportiAttualiSubValidi = totImportiAttualiSubValidi;
	}
	public DettaglioImportiImpegno getDettaglioImportiSubSelezionato() {
		return dettaglioImportiSubSelezionato;
	}
	public void setDettaglioImportiSubSelezionato(
			DettaglioImportiImpegno dettaglioImportiSubSelezionato) {
		this.dettaglioImportiSubSelezionato = dettaglioImportiSubSelezionato;
	}
	public DettaglioImportiAccertamento getDettaglioImportiAcc() {
		return dettaglioImportiAcc;
	}
	public void setDettaglioImportiAcc(
			DettaglioImportiAccertamento dettaglioImportiAcc) {
		this.dettaglioImportiAcc = dettaglioImportiAcc;
	}
	public DettaglioImportiAccertamento getDettaglioImportiSubAccSelezionato() {
		return dettaglioImportiSubAccSelezionato;
	}
	public void setDettaglioImportiSubAccSelezionato(
			DettaglioImportiAccertamento dettaglioImportiSubAccSelezionato) {
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
	 * @return the listaVincoliImpegnoOriginari
	 */
	public List<VincoloImpegno> getListaVincoliImpegnoOriginari() {
		return listaVincoliImpegnoOriginari;
	}

	/**
	 * @param listaVincoliImpegnoOriginari the listaVincoliImpegnoOriginari to set
	 */
	public void setListaVincoliImpegnoOriginari(List<VincoloImpegno> listaVincoliImpegnoOriginari) {
		this.listaVincoliImpegnoOriginari = listaVincoliImpegnoOriginari;
	}

	/**
	 * @return the listaVincoliAccertamentoOriginari
	 */
	public List<VincoloAccertamento> getListaVincoliAccertamentoOriginari() {
		return listaVincoliAccertamentoOriginari;
	}

	/**
	 * @param listaVincoliAccertamentoOriginari the listaVincoliAccertamentoOriginari to set
	 */
	public void setListaVincoliAccertamentoOriginari(List<VincoloAccertamento> listaVincoliAccertamentoOriginari) {
		this.listaVincoliAccertamentoOriginari = listaVincoliAccertamentoOriginari;
	}

	/**
	 * @return the primoImpCatena
	 */
	public Impegno getPrimoImpCatena() {
		return primoImpCatena;
	}

	/**
	 * @param primoImpCatena the primoImpCatena to set
	 */
	public void setPrimoImpCatena(Impegno primoImpCatena) {
		this.primoImpCatena = primoImpCatena;
	}

	/**
	 * @return the primoAccCatena
	 */
	public Accertamento getPrimoAccCatena() {
		return primoAccCatena;
	}

	/**
	 * @param primoAccCatena the primoAccCatena to set
	 */
	public void setPrimoAccCatena(Accertamento primoAccCatena) {
		this.primoAccCatena = primoAccCatena;
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
	 * @return the mapModificheSpesaCollegate
	 */
	public Map<Integer, List<ModificaMovimentoGestioneSpesaCollegata>> getMapModificheSpesaCollegate() {
		return mapModificheSpesaCollegate;
	}

	/**
	 * @param mapModificheSpesaCollegate the mapModificheSpesaCollegate to set
	 */
	public void setMapModificheSpesaCollegate(
			Map<Integer, List<ModificaMovimentoGestioneSpesaCollegata>> mapModificheSpesaCollegate) {
		this.mapModificheSpesaCollegate = mapModificheSpesaCollegate;
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
	
	//SIAC-8075-CMTO
	public RicercaSinteticaModulareVincoliImpegno creaRequestRicercaSinteticaModulareVincoliImpegno() {
		RicercaSinteticaModulareVincoliImpegno req = creaRequest(RicercaSinteticaModulareVincoliImpegno.class);
		req.setImpegno(creaMovimento(Impegno.class));
		req.setAnnoBilancio(isMovimentoResiduo() ? getAnnoMovimentoAsInteger() : getAnnoBilancioFromCapitoloMovimento());
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.FALSE);
		return req;
	}

	//SIAC-8075-CMTO
	public RicercaSinteticaModulareVincoliImpegno creaRequestRicercaSinteticaModulareVincoliOriginariImpegno(Integer annoEsercizio) {
		RicercaSinteticaModulareVincoliImpegno req = creaRequest(RicercaSinteticaModulareVincoliImpegno.class);
		req.setImpegno(creaMovimento(Impegno.class));
		req.setAnnoBilancio(annoEsercizio);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		return req;
	}

	//SIAC-8075-CMTO
	public RicercaSinteticaModulareVincoliAccertamento creaRequestRicercaSinteticaModulareVincoliAccertamento() {
		RicercaSinteticaModulareVincoliAccertamento req = creaRequest(RicercaSinteticaModulareVincoliAccertamento.class);
		req.setAccertamento(creaMovimento(Accertamento.class));
		req.setAnnoBilancio(isMovimentoResiduo() ? getAnnoMovimentoAsInteger() : getAnnoBilancioFromCapitoloMovimento());
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.FALSE);
		return req;
	}

	//SIAC-8075-CMTO
	public RicercaSinteticaModulareVincoliAccertamento creaRequestRicercaSinteticaModulareVincoliOriginariAccertamento(Integer annoEsercizio) {
		RicercaSinteticaModulareVincoliAccertamento req = creaRequest(RicercaSinteticaModulareVincoliAccertamento.class);
		req.setAccertamento(creaMovimento(Accertamento.class));
		req.setAnnoBilancio(annoEsercizio);
		req.setCaricaPrimoImpegnoCatenaReimpReanno(Boolean.TRUE);
		return req;
	}
	
	private boolean isMovimentoResiduo() {
		return getMovimento() != null && getAnnoBilancioFromCapitoloMovimento()
				.compareTo(getAnnoMovimentoAsInteger()) > 0;
	}
	
	private Integer getAnnoMovimentoAsInteger() {
		return NumberUtil.safeParseStringToInteger(getMovimento().getAnno());
	}
	
	private Integer getAnnoBilancioFromCapitoloMovimento() {
		return getMovimento().getCapitolo().getAnnoCompetenza() == null ? getMovimento().getCapitolo().getAnno() 
				: NumberUtil.safeParseStringToInteger(getMovimento().getCapitolo().getAnnoCompetenza());
	}
	
	private <MOV extends MovimentoGestione> MOV creaMovimento(Class<MOV> clazz){
		MOV newMovimento = null;
		
		try {
			newMovimento = clazz.newInstance();
		} catch (InstantiationException e){
			throw new IllegalArgumentException("InstantiationException during instance building", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("IllegalAccessException during instance building", e);
		} catch (ExceptionInInitializerError e) {
			throw new IllegalArgumentException("ExceptionInInitializerError during instance building", e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("SecurityException during instance building", e);
		}

		newMovimento.setUid(getMovimento().getIdMovimento());
		newMovimento.setAnnoMovimento(NumberUtil.safeParseInt(getMovimento().getAnno()));
		newMovimento.setNumeroBigDecimal(NumberUtil.safeParseStringToBigDecimal(getMovimento().getNumero()));
		newMovimento.setTipoMovimento(getMovimento().getCodiceTipoMovimento());
		
		adattaCapitolo(newMovimento);
		
		return newMovimento;
	}
	
	private <MOV extends MovimentoGestione> void adattaCapitolo(MOV newMovimento) {
		
		if("A".equals(newMovimento.getTipoMovimento())) {
			CapitoloEntrataGestione capeg = new CapitoloEntrataGestione();
			capeg.setAnnoCapitolo(getMovimento().getCapitolo().getAnno());
			capeg.setNumeroCapitolo(getMovimento().getCapitolo().getNumCapitolo());
			capeg.setNumeroArticolo(getMovimento().getCapitolo().getArticolo());
			capeg.setNumeroUEB(getMovimento().getCapitolo().getUeb().intValue());
			((Accertamento) newMovimento).setCapitoloEntrataGestione(capeg);
		}

		if("I".equals(newMovimento.getTipoMovimento())) {
			CapitoloUscitaGestione capug = new CapitoloUscitaGestione();
			capug.setAnnoCapitolo(getMovimento().getCapitolo().getAnno());
			capug.setNumeroCapitolo(getMovimento().getCapitolo().getNumCapitolo());
			capug.setNumeroArticolo(getMovimento().getCapitolo().getArticolo());
			capug.setNumeroUEB(getMovimento().getCapitolo().getUeb().intValue());
			((Impegno) newMovimento).setCapitoloUscitaGestione(capug);
		}
		
	}

	public List<MutuoAssociatoMovimentoGestione> getElencoMutuiAssociati() {
		return elencoMutuiAssociati;
	}

	public void setElencoMutuiAssociati(List<MutuoAssociatoMovimentoGestione> elencoMutuiAssociati) {
		this.elencoMutuiAssociati = elencoMutuiAssociati;
	}
	
	//task-93
	public RicercaDettaglioImpegno creaRequestRicercaDettaglioImpegnoMutuo() {
		RicercaDettaglioImpegno request = creaRequest(RicercaDettaglioImpegno.class);
		request.setImpegnoModelDetails(ImpegnoModelDetail.MutuiAssociatiImpegno);
		return request;
	}
	
	//task-93
	public RicercaDettaglioAccertamento creaRequestRicercaDettaglioAccertamentoMutuo() {
		RicercaDettaglioAccertamento request = creaRequest(RicercaDettaglioAccertamento.class);
		request.setAccertamentoModelDetails(AccertamentoModelDetail.MutuiAssociatiAccertamento);
		return request;
	}
}
