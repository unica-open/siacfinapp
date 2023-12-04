/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class RicercaOrdinativoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;

	// model del capitolo
	private CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
	// model del soggetto
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	// model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();

	// impegno
	private ImpegnoLiquidazioneModel impegno = new ImpegnoLiquidazioneModel();

	// flag vari
	private boolean soggettoSelezionato = false, provvedimentoSelezionato = false, capitoloSelezionato = false;

	// campi vari
	private BigInteger numeroOrdinativoDa;
	private BigInteger numeroOrdinativoA;
	private String dataEmissioneDa;
	private String dataEmissioneA;
	private String statoOperativo;
	private String distinta;
	private String contoTesoriere;
	private String dataTrasmissioneOIL;

	// liquidazione
	private Integer annoLiquidazione;
	private BigInteger numeroLiquidazione;
	private String numeroLiquidazioneString;

	// accertamento
	private Integer annoAccertamento;
	private Integer numeroAccertamento;
	private Integer numeroSubAccertamento;

	// liste codifiche
	private List<CodificaFin> listaDistinta = new ArrayList<CodificaFin>();
	private List<CodificaFin> listaContoTesoriere = new ArrayList<CodificaFin>();
	private List<CodificaFin> listaStatoOperativo = new ArrayList<CodificaFin>();

	// lista tipo finanziamneto
	private List<TipoFinanziamento> listaTipoFinanziamento = new ArrayList<TipoFinanziamento>();

	// paginazione
	private int resultSize;

	// liste ordinativo
	private List<Ordinativo> elencoOrdinativo = new ArrayList<Ordinativo>();
	private List<OrdinativoPagamento> elencoOrdinativoPagamento = new ArrayList<OrdinativoPagamento>();
	private List<OrdinativoIncasso> elencoOrdinativoIncasso = new ArrayList<OrdinativoIncasso>();

	// tot importi
	private BigDecimal totImporti;

	private BigInteger numeroOrdinativoPagamento;

	private String descrizioneOrdinativo;

	private Boolean escludiAnnullati = Boolean.FALSE;
	private Boolean hiddenPerEscludiAnnullati;

	private Integer daTrasmettere;

	// causale:
	private CausaleEntrataTendinoModel causaleEntrataTendino;// per ordinativi
																// incasso, se
																// servisse per
																// quelli di
																// pagamento
																// fara
																// l'analogo per
																// spesa
	//

	// model del soggetto legato a mod pag cessione incasso:
	private SoggettoImpegnoModel soggettoDue = new SoggettoImpegnoModel();
	private boolean soggettoSelezionatoDue = false;
	//

	// SIAC-6585
	private Integer uidOrdCollegaReversali;
	private OrdinativoPagamento ordinativoPagamento;
	private String[] uidOrdIncassoSelezionati;
	private String[] totImportoSelezionati;
	private String[] uidOrdIncassoSoggettoDiff;

	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}

	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}

	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(CapitoloImpegnoModel capitolo) {
		this.capitolo = capitolo;
	}

	public ImpegnoLiquidazioneModel getImpegno() {
		return impegno;
	}

	public void setImpegno(ImpegnoLiquidazioneModel impegno) {
		this.impegno = impegno;
	}

	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}

	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}

	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}

	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}

	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}

	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}

	public BigInteger getNumeroOrdinativoDa() {
		return numeroOrdinativoDa;
	}

	public void setNumeroOrdinativoDa(BigInteger numeroOrdinativoDa) {
		this.numeroOrdinativoDa = numeroOrdinativoDa;
	}

	public BigInteger getNumeroOrdinativoA() {
		return numeroOrdinativoA;
	}

	public void setNumeroOrdinativoA(BigInteger numeroOrdinativoA) {
		this.numeroOrdinativoA = numeroOrdinativoA;
	}

	public String getDataEmissioneDa() {
		return dataEmissioneDa;
	}

	public void setDataEmissioneDa(String dataEmissioneDa) {
		this.dataEmissioneDa = dataEmissioneDa;
	}

	public String getDataEmissioneA() {
		return dataEmissioneA;
	}

	public void setDataEmissioneA(String dataEmissioneA) {
		this.dataEmissioneA = dataEmissioneA;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public List<Ordinativo> getElencoOrdinativo() {
		return elencoOrdinativo;
	}

	public void setElencoOrdinativo(List<Ordinativo> elencoOrdinativo) {
		this.elencoOrdinativo = elencoOrdinativo;
	}

	public List<OrdinativoPagamento> getElencoOrdinativoPagamento() {
		return elencoOrdinativoPagamento;
	}

	public void setElencoOrdinativoPagamento(List<OrdinativoPagamento> elencoOrdinativoPagamento) {
		this.elencoOrdinativoPagamento = elencoOrdinativoPagamento;
	}

	public String getDistinta() {
		return distinta;
	}

	public void setDistinta(String distinta) {
		this.distinta = distinta;
	}

	public String getContoTesoriere() {
		return contoTesoriere;
	}

	public void setContoTesoriere(String contoTesoriere) {
		this.contoTesoriere = contoTesoriere;
	}

	public List<CodificaFin> getListaDistinta() {
		return listaDistinta;
	}

	public void setListaDistinta(List<CodificaFin> listaDistinta) {
		this.listaDistinta = listaDistinta;
	}

	public List<CodificaFin> getListaContoTesoriere() {
		return listaContoTesoriere;
	}

	public void setListaContoTesoriere(List<CodificaFin> listaContoTesoriere) {
		this.listaContoTesoriere = listaContoTesoriere;
	}

	public Integer getAnnoLiquidazione() {
		return annoLiquidazione;
	}

	public void setAnnoLiquidazione(Integer annoLiquidazione) {
		this.annoLiquidazione = annoLiquidazione;
	}

	public BigInteger getNumeroLiquidazione() {
		return numeroLiquidazione;
	}

	public void setNumeroLiquidazione(BigInteger numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}

	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}

	public String getDataTrasmissioneOIL() {
		return dataTrasmissioneOIL;
	}

	public void setDataTrasmissioneOIL(String dataTrasmissioneOIL) {
		this.dataTrasmissioneOIL = dataTrasmissioneOIL;
	}

	public String getNumeroLiquidazioneString() {
		return numeroLiquidazioneString;
	}

	public void setNumeroLiquidazioneString(String numeroLiquidazioneString) {
		this.numeroLiquidazioneString = numeroLiquidazioneString;
	}

	public BigInteger getNumeroOrdinativoPagamento() {
		return numeroOrdinativoPagamento;
	}

	public void setNumeroOrdinativoPagamento(BigInteger numeroOrdinativoPagamento) {
		this.numeroOrdinativoPagamento = numeroOrdinativoPagamento;
	}

	public String getStatoOperativo() {
		return statoOperativo;
	}

	public void setStatoOperativo(String statoOperativo) {
		this.statoOperativo = statoOperativo;
	}

	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}

	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}

	public Integer getNumeroAccertamento() {
		return numeroAccertamento;
	}

	public void setNumeroAccertamento(Integer numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}

	public Integer getNumeroSubAccertamento() {
		return numeroSubAccertamento;
	}

	public void setNumeroSubAccertamento(Integer numeroSubAccertamento) {
		this.numeroSubAccertamento = numeroSubAccertamento;
	}

	public List<CodificaFin> getListaStatoOperativo() {
		return listaStatoOperativo;
	}

	public void setListaStatoOperativo(List<CodificaFin> listaStatoOperativo) {
		this.listaStatoOperativo = listaStatoOperativo;
	}

	public List<OrdinativoIncasso> getElencoOrdinativoIncasso() {
		return elencoOrdinativoIncasso;
	}

	public void setElencoOrdinativoIncasso(List<OrdinativoIncasso> elencoOrdinativoIncasso) {
		this.elencoOrdinativoIncasso = elencoOrdinativoIncasso;
	}

	public String getDescrizioneOrdinativo() {
		return descrizioneOrdinativo;
	}

	public void setDescrizioneOrdinativo(String descrizioneOrdinativo) {
		this.descrizioneOrdinativo = descrizioneOrdinativo;
	}

	public Boolean getEscludiAnnullati() {
		return escludiAnnullati;
	}

	public void setEscludiAnnullati(Boolean escludiAnnullati) {
		this.escludiAnnullati = escludiAnnullati;
	}

	public Boolean getHiddenPerEscludiAnnullati() {
		return hiddenPerEscludiAnnullati;
	}

	public void setHiddenPerEscludiAnnullati(Boolean hiddenPerEscludiAnnullati) {
		this.hiddenPerEscludiAnnullati = hiddenPerEscludiAnnullati;
	}

	public BigDecimal getTotImporti() {
		return totImporti;
	}

	public void setTotImporti(BigDecimal totImporti) {
		this.totImporti = totImporti;
	}

	public CausaleEntrataTendinoModel getCausaleEntrataTendino() {
		return causaleEntrataTendino;
	}

	public void setCausaleEntrataTendino(CausaleEntrataTendinoModel causaleEntrataTendino) {
		this.causaleEntrataTendino = causaleEntrataTendino;
	}

	public SoggettoImpegnoModel getSoggettoDue() {
		return soggettoDue;
	}

	public void setSoggettoDue(SoggettoImpegnoModel soggettoDue) {
		this.soggettoDue = soggettoDue;
	}

	public boolean isSoggettoSelezionatoDue() {
		return soggettoSelezionatoDue;
	}

	public void setSoggettoSelezionatoDue(boolean soggettoSelezionatoDue) {
		this.soggettoSelezionatoDue = soggettoSelezionatoDue;
	}

	public Integer getDaTrasmettere() {
		return daTrasmettere;
	}

	public void setDaTrasmettere(Integer daTrasmettere) {
		this.daTrasmettere = daTrasmettere;
	}

	public Integer getUidOrdCollegaReversali() {
		return uidOrdCollegaReversali;
	}

	public void setUidOrdCollegaReversali(Integer uidOrdCollegaReversali) {
		this.uidOrdCollegaReversali = uidOrdCollegaReversali;
	}

	public OrdinativoPagamento getOrdinativoPagamento() {
		return ordinativoPagamento;
	}

	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		this.ordinativoPagamento = ordinativoPagamento;
	}

	public String[] getUidOrdIncassoSelezionati() {
		return uidOrdIncassoSelezionati;
	}

	public void setUidOrdIncassoSelezionati(String[] uidOrdIncassoSelezionati) {
		this.uidOrdIncassoSelezionati = normalize(uidOrdIncassoSelezionati);
	}

	public String[] getTotImportoSelezionati() {
		return totImportoSelezionati;
	}

	public void setTotImportoSelezionati(String[] totImportoSelezionati) {
		this.totImportoSelezionati = normalize(totImportoSelezionati);
	}
	
	private String[] normalize(String[] a) {
		return a!= null && a.length > 1 ? new String[]{a[a.length-1]} : a;
	}

	public String[] getUidOrdIncassoSoggettoDiff() {
		return uidOrdIncassoSoggettoDiff;
	}

	public void setUidOrdIncassoSoggettoDiff(String[] uidOrdIncassoSoggettoDiff) {
		this.uidOrdIncassoSoggettoDiff = normalize(uidOrdIncassoSoggettoDiff);
	}

	public Integer[] getUidOrdIncassoSelezionatiSplit() {
		if (uidOrdIncassoSelezionati == null || uidOrdIncassoSelezionati.length == 0) {
			return null;
		}
		
		String[] tmp = StringUtils.split(uidOrdIncassoSelezionati[0], "-");
		Integer[] uidOrdIncassoSelezionatiSplit = new Integer[tmp.length];

		for (int i = 0; i < tmp.length; i++) {
			uidOrdIncassoSelezionatiSplit[i] = Integer.parseInt(tmp[i]);
		}
		
		return uidOrdIncassoSelezionatiSplit;
	}
}
