/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class GestioneOrdinativoStep1Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	//gestione del flg per reintroiti:
	private boolean flagPerReintroiti;
	private boolean flagPerReintroitiOrdinativoInAggiornamento;
	private String codPdcAccertamentoCercato;
	//
	
	private OrdinativoPagamento ordinativo = new OrdinativoPagamento();
	private String commissioneSelezionata;

	//model del capitolo
	private CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
	//model del soggetto
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	
	private boolean capitoloSelezionato = false, provvedimentoSelezionato = false, soggettoSelezionato = false;
	private TransazioneGestioneOrdinativoModel transazione = new TransazioneGestioneOrdinativoModel();
	
	private List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<SedeSecondariaSoggetto> listaSediSecondarieVisualizza = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoVisualizza = new ArrayList<ModalitaPagamentoSoggetto>();
	
	
	private SedeSecondariaSoggetto sedeSelezionata;
	private ModalitaPagamentoSoggetto modpagamentoSelezionata;
	private int radioModPagSelezionato;
	private int radioSediSecondarieSoggettoSelezionato;
	
	private Integer annoLiquidazione;
	private BigInteger numeroLiquidazione;
	private Boolean flagRicercaLiquidazionePerChiave;
	
	private Integer annoAccertamento;
	private BigInteger numeroAccertamento;
	private BigInteger numeroSubAcc;

	private Soggetto soggettoDettaglioPerClasse;
	
	
	private String avvisoFrase;
	
	private boolean hasCodiceSoggettoAccertamento;
	
	private boolean abilitatoACollegareOrdinativiNuovi = true;
	private BigDecimal totaleImportoCollegati = BigDecimal.ZERO;
	private boolean portaAdAltezzaOrdinativiCollegati = false;
	private boolean apriTabOrdinativiCollegati = false;
	private Integer numeroOrdinativoDaCollegare;
	private String annoOrdinativoDaPassare;
	private String numeroOrdinativoDaPassare;
	
	
	//GESTIONE COLLEGA ORDINATIVO DI INCASSO CON CONFERMA:
	private OrdinativoIncasso ordIncassoDaConfermare;
	private boolean checkWarningDaCollegareQuietanziato;
	//
	
			
	/**
	 * @return the annoOrdinativoDaPassare
	 */
	public String getAnnoOrdinativoDaPassare() {
		return annoOrdinativoDaPassare;
	}
	/**
	 * @param annoOrdinativoDaPassare the annoOrdinativoDaPassare to set
	 */
	public void setAnnoOrdinativoDaPassare(String annoOrdinativoDaPassare) {
		this.annoOrdinativoDaPassare = annoOrdinativoDaPassare;
	}
	/**
	 * @return the numeroOrdinativoDaPassare
	 */
	public String getNumeroOrdinativoDaPassare() {
		return numeroOrdinativoDaPassare;
	}
	/**
	 * @param numeroOrdinativoDaPassare the numeroOrdinativoDaPassare to set
	 */
	public void setNumeroOrdinativoDaPassare(String numeroOrdinativoDaPassare) {
		this.numeroOrdinativoDaPassare = numeroOrdinativoDaPassare;
	}
	/**
	 * @return the numeroOrdinativoDaCollegare
	 */
	public Integer getNumeroOrdinativoDaCollegare() {
		return numeroOrdinativoDaCollegare;
	}
	/**
	 * @param numeroOrdinativoDaCollegare the numeroOrdinativoDaCollegare to set
	 */
	public void setNumeroOrdinativoDaCollegare(Integer numeroOrdinativoDaCollegare) {
		this.numeroOrdinativoDaCollegare = numeroOrdinativoDaCollegare;
	}
	/**
	 * @return the abilitatoACollegareOrdinativiNuovi
	 */
	public boolean isAbilitatoACollegareOrdinativiNuovi() {
		return abilitatoACollegareOrdinativiNuovi;
	}
	/**
	 * @param abilitatoACollegareOrdinativiNuovi the abilitatoACollegareOrdinativiNuovi to set
	 */
	public void setAbilitatoACollegareOrdinativiNuovi(
			boolean abilitatoACollegareOrdinativiNuovi) {
		this.abilitatoACollegareOrdinativiNuovi = abilitatoACollegareOrdinativiNuovi;
	}
	/**
	 * @return the totaleImportoCollegati
	 */
	public BigDecimal getTotaleImportoCollegati() {
		return totaleImportoCollegati;
	}
	/**
	 * @param totaleImportoCollegati the totaleImportoCollegati to set
	 */
	public void setTotaleImportoCollegati(BigDecimal totaleImportoCollegati) {
		this.totaleImportoCollegati = totaleImportoCollegati;
	}
	/**
	 * @return the portaAdAltezzaOrdinativiCollegati
	 */
	public boolean isPortaAdAltezzaOrdinativiCollegati() {
		return portaAdAltezzaOrdinativiCollegati;
	}
	/**
	 * @param portaAdAltezzaOrdinativiCollegati the portaAdAltezzaOrdinativiCollegati to set
	 */
	public void setPortaAdAltezzaOrdinativiCollegati(
			boolean portaAdAltezzaOrdinativiCollegati) {
		this.portaAdAltezzaOrdinativiCollegati = portaAdAltezzaOrdinativiCollegati;
	}
	/**
	 * @return the apriTabOrdinativiCollegati
	 */
	public boolean isApriTabOrdinativiCollegati() {
		return apriTabOrdinativiCollegati;
	}
	/**
	 * @param apriTabOrdinativiCollegati the apriTabOrdinativiCollegati to set
	 */
	public void setApriTabOrdinativiCollegati(boolean apriTabOrdinativiCollegati) {
		this.apriTabOrdinativiCollegati = apriTabOrdinativiCollegati;
	}
	public String getAvvisoFrase() {
		return avvisoFrase;
	}
	public void setAvvisoFrase(String avvisoFrase) {
		this.avvisoFrase = avvisoFrase;
	}
	// liste pagina step2
	private List<CodificaFin> listaBollo, listaAvviso, listaDistinta, listaContoTesoriere, listaNoteTesoriere, listaCommissioni;
	
	//causale:
	private CausaleEntrataTendinoModel causaleEntrataTendino;//per ordinativi incasso, se servisse per quelli di pagamento fara l'analogo per spesa
	//
	
	
	public CapitoloImpegnoModel getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloImpegnoModel capitolo) {
		this.capitolo = capitolo;
	}
	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}
	public boolean isCapitoloSelezionato() {
		return capitoloSelezionato;
	}
	public void setCapitoloSelezionato(boolean capitoloSelezionato) {
		this.capitoloSelezionato = capitoloSelezionato;
	}
	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}
	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}
	public TransazioneGestioneOrdinativoModel getTransazione() {
		return transazione;
	}
	public void setTransazione(TransazioneGestioneOrdinativoModel transazione) {
		this.transazione = transazione;
	}
	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}
	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}
	
	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}
	public List<CodificaFin> getListaBollo() {
		return listaBollo;
	}
	public void setListaBollo(List<CodificaFin> listaBollo) {
		this.listaBollo = listaBollo;
	}
	public List<CodificaFin> getListaAvviso() {
		return listaAvviso;
	}
	public void setListaAvviso(List<CodificaFin> listaAvviso) {
		this.listaAvviso = listaAvviso;
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
	public List<CodificaFin> getListaNoteTesoriere() {
		return listaNoteTesoriere;
	}
	public void setListaNoteTesoriere(List<CodificaFin> listaNoteTesoriere) {
		this.listaNoteTesoriere = listaNoteTesoriere;
	}
	public List<CodificaFin> getListaCommissioni() {
		return listaCommissioni;
	}
	public void setListaCommissioni(List<CodificaFin> listaCommissioni) {
		this.listaCommissioni = listaCommissioni;
	}
	public List<SedeSecondariaSoggetto> getListaSediSecondarie() {
		return listaSediSecondarie;
	}
	public void setListaSediSecondarie(
			List<SedeSecondariaSoggetto> listaSediSecondarie) {
		this.listaSediSecondarie = listaSediSecondarie;
	}
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamento() {
		return listaModalitaPagamento;
	}
	public void setListaModalitaPagamento(
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento) {
		this.listaModalitaPagamento = listaModalitaPagamento;
	}
	
	public SedeSecondariaSoggetto getSedeSelezionata() {
		return sedeSelezionata;
	}
	public void setSedeSelezionata(SedeSecondariaSoggetto sedeSelezionata) {
		this.sedeSelezionata = sedeSelezionata;
	}
	public int getRadioModPagSelezionato() {
		return radioModPagSelezionato;
	}
	public void setRadioModPagSelezionato(int radioModPagSelezionato) {
		this.radioModPagSelezionato = radioModPagSelezionato;
	}
	public ModalitaPagamentoSoggetto getModpagamentoSelezionata() {
		return modpagamentoSelezionata;
	}
	public void setModpagamentoSelezionata(
			ModalitaPagamentoSoggetto modpagamentoSelezionata) {
		this.modpagamentoSelezionata = modpagamentoSelezionata;
	}
	public int getRadioSediSecondarieSoggettoSelezionato() {
		return radioSediSecondarieSoggettoSelezionato;
	}
	public void setRadioSediSecondarieSoggettoSelezionato(
			int radioSediSecondarieSoggettoSelezionato) {
		this.radioSediSecondarieSoggettoSelezionato = radioSediSecondarieSoggettoSelezionato;
	}
	public List<SedeSecondariaSoggetto> getListaSediSecondarieVisualizza() {
		return listaSediSecondarieVisualizza;
	}
	public void setListaSediSecondarieVisualizza(
			List<SedeSecondariaSoggetto> listaSediSecondarieVisualizza) {
		this.listaSediSecondarieVisualizza = listaSediSecondarieVisualizza;
	}
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoVisualizza() {
		return listaModalitaPagamentoVisualizza;
	}
	public void setListaModalitaPagamentoVisualizza(
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoVisualizza) {
		this.listaModalitaPagamentoVisualizza = listaModalitaPagamentoVisualizza;
	}
	public String getCommissioneSelezionata() {
		return commissioneSelezionata;
	}
	public void setCommissioneSelezionata(String commissioneSelezionata) {
		this.commissioneSelezionata = commissioneSelezionata;
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
	public OrdinativoPagamento getOrdinativo() {
		return ordinativo;
	}
	public void setOrdinativo(OrdinativoPagamento ordinativo) {
		this.ordinativo = ordinativo;
	}
	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public BigInteger getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(BigInteger numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public BigInteger getNumeroSubAcc() {
		return numeroSubAcc;
	}
	public void setNumeroSubAcc(BigInteger numeroSubAcc) {
		this.numeroSubAcc = numeroSubAcc;
	}
	public Soggetto getSoggettoDettaglioPerClasse() {
		return soggettoDettaglioPerClasse;
	}
	public void setSoggettoDettaglioPerClasse(Soggetto soggettoDettaglioPerClasse) {
		this.soggettoDettaglioPerClasse = soggettoDettaglioPerClasse;
	}
	/**
	 * @return the flagRicercaLiquidazionePerChiave
	 */
	public Boolean getFlagRicercaLiquidazionePerChiave() {
		return flagRicercaLiquidazionePerChiave;
	}
	
	/**
	 * @param flagRicercaLiquidazionePerChiave the flagRicercaLiquidazionePerChiave to set
	 */
	public void setFlagRicercaLiquidazionePerChiave(
			Boolean flagRicercaLiquidazionePerChiave) {
		this.flagRicercaLiquidazionePerChiave = flagRicercaLiquidazionePerChiave;
	}
	/**
	 * @return the hasCodiceSoggettoAccertamento
	 */
	public boolean isHasCodiceSoggettoAccertamento() {
		return hasCodiceSoggettoAccertamento;
	}
	/**
	 * @param hasCodiceSoggettoAccertamento the hasCodiceSoggettoAccertamento to set
	 */
	public void setHasCodiceSoggettoAccertamento(
			boolean hasCodiceSoggettoAccertamento) {
		this.hasCodiceSoggettoAccertamento = hasCodiceSoggettoAccertamento;
	}
	
	public CausaleEntrataTendinoModel getCausaleEntrataTendino() {
		return causaleEntrataTendino;
	}
	public void setCausaleEntrataTendino(CausaleEntrataTendinoModel causaleEntrataTendino) {
		this.causaleEntrataTendino = causaleEntrataTendino;
	}
	public boolean isFlagPerReintroiti() {
		return flagPerReintroiti;
	}
	public void setFlagPerReintroiti(boolean flagPerReintroiti) {
		this.flagPerReintroiti = flagPerReintroiti;
	}
	public boolean isFlagPerReintroitiOrdinativoInAggiornamento() {
		return flagPerReintroitiOrdinativoInAggiornamento;
	}
	public void setFlagPerReintroitiOrdinativoInAggiornamento(boolean flagPerReintroitiOrdinativoInAggiornamento) {
		this.flagPerReintroitiOrdinativoInAggiornamento = flagPerReintroitiOrdinativoInAggiornamento;
	}
	public String getCodPdcAccertamentoCercato() {
		return codPdcAccertamentoCercato;
	}
	public void setCodPdcAccertamentoCercato(String codPdcAccertamentoCercato) {
		this.codPdcAccertamentoCercato = codPdcAccertamentoCercato;
	}
	public OrdinativoIncasso getOrdIncassoDaConfermare() {
		return ordIncassoDaConfermare;
	}
	public void setOrdIncassoDaConfermare(OrdinativoIncasso ordIncassoDaConfermare) {
		this.ordIncassoDaConfermare = ordIncassoDaConfermare;
	}
	public boolean isCheckWarningDaCollegareQuietanziato() {
		return checkWarningDaCollegareQuietanziato;
	}
	public void setCheckWarningDaCollegareQuietanziato(
			boolean checkWarningDaCollegareQuietanziato) {
		this.checkWarningDaCollegareQuietanziato = checkWarningDaCollegareQuietanziato;
	}
	
}
