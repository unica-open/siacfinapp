/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaEstera;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class GestioneCartaModel  extends GenericPopupModel{
	
	private static final long serialVersionUID = 1L;
	
	//titolo dello step:
	private String titoloStep;
	
	//flag vari:
	private boolean aggiornamento;
	private boolean soggettoObbligatorio;
	private boolean modPagamentoObbligatoria;
	
	//anno e numero carta:
	private String annoCartaInAggiornamento;
	private String numeroCartaInAggiornamento;
	
	//model del provvedimento:
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//lista valuta:
	private List<CodificaFin> listaValuta = new ArrayList<CodificaFin>();
	
	//lista commissioni estero:
	private List<CodificaFin> listaCommissioniEstero = new ArrayList<CodificaFin>();

	//Informazioni carta contabile
	private boolean provvedimentoSelezionato=false;
	private boolean optionsPagamentoEstero;
	private String descrizioneCarta;
	private String causaleCarta;
	private String motivoUrgenzaCarta;
	private String noteCarta;
	private String dataValutaCarta;
	private String dataScadenzaCarta;
	private String statoOperativoCarta;
	private String dataStatoOperativoCarta;
	private String numeroRegistrazione;
	
	//Pagamento valuta estera
	private String codiceDivisa;
	private String codiceDivisaCaricato;
	private String causalePagamentoEstero;
	private String speseECommissioni;
	private String modalitaPagamento;
	private String istrPartPagamentoEstero;
	private boolean checkEsecutoreTitolare;
	
	private String firma1;
	private String firma2;
	
	private String intestazioneFirma1;
	private String intestazioneFirma2;
	
	//ricerca documeti per collega:
	private GestioneCartaRicercaDocumentoModel ricercaDocumenti = new GestioneCartaRicercaDocumentoModel();
	
	//lista pre documento carta:
	private List<PreDocumentoCarta> listaRighe=new ArrayList<PreDocumentoCarta>();
	
	
	//**************************** VARIABILI PER RIGA *****************************//
	
	//Variabili di appoggio per la gestione del soggetto da associare alla riga 
	
	//liste sedi e mod pag:
	private List<SedeSecondariaSoggetto> listaSediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<SedeSecondariaSoggetto> listaSediSecondarieVisualizza = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoVisualizza = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<ClassificazioneSoggetto> listaClassificazioneSoggetto = new ArrayList<ClassificazioneSoggetto>();
	
	//sede e mod pag:
	private SedeSecondariaSoggetto sedeSelezionata;
	private ModalitaPagamentoSoggetto modpagamentoSelezionata;
	private int radioModPagSelezionato;
	private int radioSediSecondarieSoggettoSelezionato;
	private boolean soggettoSelezionato = false;
	
	//avviso:
	private String avvisoFrase;
	
	//model del soggetto:
	private SoggettoImpegnoModel soggetto;
	//END variabili di appoggio soggetto - riga
	
	//Variabili di appoggio per la gestione del soggetto da associare alla riga 
	private ImpegnoLiquidazioneModel impegno;

	//END variabili di appoggio impegno - riga
	
	//variabili dati riga
	private String dataEsecuzioneRiga;
	private BigDecimal importoRiga;
	private BigDecimal importoEsteroRiga;
	private String descrizioneRiga;
	private String noteRiga;	
	private List<CodificaFin> listaContoTesoriereRiga = new ArrayList<CodificaFin>();
	private String contoTesoriereRiga;
	private PreDocumentoCarta rigaInModifica = new PreDocumentoCarta();
	private BigDecimal importoDaRegolarizzare;
	private String importoPopupFormattato;
	
	//lista sub doc:
	private List<SubdocumentoSpesa> listaSubDocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	
	private SubdocumentoSpesa documentoInModifica = new SubdocumentoSpesa();
	
	//*************************** END VARIABILI PER RIGA *****************************//
	
	/*************************** GESTIONE DINAMICA  *****************************/
    private Map<String, String> labels = new HashMap<String, String>();
    /****************************************************************************/

    // regolarizzazione righe
    private CartaContabile cartaContabileDaRicerca;
    private BigDecimal sommaRigheCarta;
    private String numeroRighe;
    private CartaEstera valutaPerRegolarizzazione;
    private String numeroRigaSelezionata;
    private PreDocumentoCarta dettaglioRiga;
    private boolean toggleCollegaImpegnoAperto = false;
    private BigDecimal sommaDocCollegatiRegolazione;
    private boolean hasCollegaImpegnoBloccato = false;
    private boolean presenzaRigheSelezionabili = false;
    
    
    private boolean toggleCollegaDocumentoAperto=false;
    
    // aggiorna riga da movimento
    private BigDecimal sommaDocumentiCollegati;

	public Map<String, String> getLabels() {
		return labels;
	}


	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}


	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}


	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}


	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}


	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}


	public String getDescrizioneCarta() {
		return descrizioneCarta;
	}


	public void setDescrizioneCarta(String descrizioneCarta) {
		this.descrizioneCarta = descrizioneCarta;
	}


	public String getCausaleCarta() {
		return causaleCarta;
	}


	public void setCausaleCarta(String causaleCarta) {
		this.causaleCarta = causaleCarta;
	}


	public String getMotivoUrgenzaCarta() {
		return motivoUrgenzaCarta;
	}


	public void setMotivoUrgenzaCarta(String motivoUrgenzaCarta) {
		this.motivoUrgenzaCarta = motivoUrgenzaCarta;
	}


	public String getNoteCarta() {
		return noteCarta;
	}


	public void setNoteCarta(String noteCarta) {
		this.noteCarta = noteCarta;
	}


	public String getDataValutaCarta() {
		return dataValutaCarta;
	}


	public void setDataValutaCarta(String dataValutaCarta) {
		this.dataValutaCarta = dataValutaCarta;
	}


	public String getDataScadenzaCarta() {
		return dataScadenzaCarta;
	}


	public void setDataScadenzaCarta(String dataScadenzaCarta) {
		this.dataScadenzaCarta = dataScadenzaCarta;
	}


	public List<CodificaFin> getListaValuta() {
		return listaValuta;
	}


	public void setListaValuta(List<CodificaFin> listaValuta) {
		this.listaValuta = listaValuta;
	}


	public String getCodiceDivisa() {
		return codiceDivisa;
	}


	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}


	public List<CodificaFin> getListaCommissioniEstero() {
		return listaCommissioniEstero;
	}


	public void setListaCommissioniEstero(List<CodificaFin> listaCommissioniEstero) {
		this.listaCommissioniEstero = listaCommissioniEstero;
	}


	public boolean isOptionsPagamentoEstero() {
		return optionsPagamentoEstero;
	}


	public void setOptionsPagamentoEstero(boolean optionsPagamentoEstero) {
		this.optionsPagamentoEstero = optionsPagamentoEstero;
	}


	public String getCausalePagamentoEstero() {
		return causalePagamentoEstero;
	}


	public void setCausalePagamentoEstero(String causalePagamentoEstero) {
		this.causalePagamentoEstero = causalePagamentoEstero;
	}


	public String getIstrPartPagamentoEstero() {
		return istrPartPagamentoEstero;
	}


	public void setIstrPartPagamentoEstero(String istrPartPagamentoEstero) {
		this.istrPartPagamentoEstero = istrPartPagamentoEstero;
	}


	public boolean isCheckEsecutoreTitolare() {
		return checkEsecutoreTitolare;
	}


	public void setCheckEsecutoreTitolare(boolean checkEsecutoreTitolare) {
		this.checkEsecutoreTitolare = checkEsecutoreTitolare;
	}


	public List<PreDocumentoCarta> getListaRighe() {
		return listaRighe;
	}


	public void setListaRighe(List<PreDocumentoCarta> listaRighe) {
		this.listaRighe = listaRighe;
	}


	public List<SedeSecondariaSoggetto> getListaSediSecondarie() {
		return listaSediSecondarie;
	}


	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamento() {
		return listaModalitaPagamento;
	}


	public List<SedeSecondariaSoggetto> getListaSediSecondarieVisualizza() {
		return listaSediSecondarieVisualizza;
	}


	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoVisualizza() {
		return listaModalitaPagamentoVisualizza;
	}


	public void setListaSediSecondarie(List<SedeSecondariaSoggetto> listaSediSecondarie) {
		this.listaSediSecondarie = listaSediSecondarie;
	}


	public void setListaModalitaPagamento(List<ModalitaPagamentoSoggetto> listaModalitaPagamento) {
		this.listaModalitaPagamento = listaModalitaPagamento;
	}


	public void setListaSediSecondarieVisualizza(List<SedeSecondariaSoggetto> listaSediSecondarieVisualizza) {
		this.listaSediSecondarieVisualizza = listaSediSecondarieVisualizza;
	}


	public void setListaModalitaPagamentoVisualizza(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoVisualizza) {
		this.listaModalitaPagamentoVisualizza = listaModalitaPagamentoVisualizza;
	}


	public SedeSecondariaSoggetto getSedeSelezionata() {
		return sedeSelezionata;
	}


	public ModalitaPagamentoSoggetto getModpagamentoSelezionata() {
		return modpagamentoSelezionata;
	}


	public int getRadioModPagSelezionato() {
		return radioModPagSelezionato;
	}


	public int getRadioSediSecondarieSoggettoSelezionato() {
		return radioSediSecondarieSoggettoSelezionato;
	}


	public void setSedeSelezionata(SedeSecondariaSoggetto sedeSelezionata) {
		this.sedeSelezionata = sedeSelezionata;
	}


	public void setModpagamentoSelezionata(ModalitaPagamentoSoggetto modpagamentoSelezionata) {
		this.modpagamentoSelezionata = modpagamentoSelezionata;
	}


	public void setRadioModPagSelezionato(int radioModPagSelezionato) {
		this.radioModPagSelezionato = radioModPagSelezionato;
	}


	public void setRadioSediSecondarieSoggettoSelezionato(int radioSediSecondarieSoggettoSelezionato) {
		this.radioSediSecondarieSoggettoSelezionato = radioSediSecondarieSoggettoSelezionato;
	}


	public String getAvvisoFrase() {
		return avvisoFrase;
	}


	public void setAvvisoFrase(String avvisoFrase) {
		this.avvisoFrase = avvisoFrase;
	}


	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}


	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}


	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}


	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}


	public ImpegnoLiquidazioneModel getImpegno() {
		return impegno;
	}


	public void setImpegno(ImpegnoLiquidazioneModel impegno) {
		this.impegno = impegno;
	}


	public String getDataEsecuzioneRiga() {
		return dataEsecuzioneRiga;
	}


	public BigDecimal getImportoRiga() {
		return importoRiga;
	}


	public BigDecimal getImportoEsteroRiga() {
		return importoEsteroRiga;
	}


	public String getDescrizioneRiga() {
		return descrizioneRiga;
	}


	public String getNoteRiga() {
		return noteRiga;
	}


	public List<CodificaFin> getListaContoTesoriereRiga() {
		return listaContoTesoriereRiga;
	}


	public String getContoTesoriereRiga() {
		return contoTesoriereRiga;
	}


	public void setDataEsecuzioneRiga(String dataEsecuzioneRiga) {
		this.dataEsecuzioneRiga = dataEsecuzioneRiga;
	}


	public void setImportoRiga(BigDecimal importoRiga) {
		this.importoRiga = importoRiga;
	}


	public void setImportoEsteroRiga(BigDecimal importoEsteroRiga) {
		this.importoEsteroRiga = importoEsteroRiga;
	}


	public void setDescrizioneRiga(String descrizioneRiga) {
		this.descrizioneRiga = descrizioneRiga;
	}


	public void setNoteRiga(String noteRiga) {
		this.noteRiga = noteRiga;
	}


	public void setListaContoTesoriereRiga(List<CodificaFin> listaContoTesoriereRiga) {
		this.listaContoTesoriereRiga = listaContoTesoriereRiga;
	}


	public void setContoTesoriereRiga(String contoTesoriereRiga) {
		this.contoTesoriereRiga = contoTesoriereRiga;
	}


	public PreDocumentoCarta getRigaInModifica() {
		return rigaInModifica;
	}


	public void setRigaInModifica(PreDocumentoCarta rigaInModifica) {
		this.rigaInModifica = rigaInModifica;
	}
	public CartaContabile getCartaContabileDaRicerca() {
		return cartaContabileDaRicerca;
	}


	public void setCartaContabileDaRicerca(CartaContabile cartaContabileDaRicerca) {
		this.cartaContabileDaRicerca = cartaContabileDaRicerca;
	}


	public BigDecimal getSommaRigheCarta() {
		return sommaRigheCarta;
	}


	public void setSommaRigheCarta(BigDecimal sommaRigheCarta) {
		this.sommaRigheCarta = sommaRigheCarta;
	}


	public String getNumeroRighe() {
		return numeroRighe;
	}


	public void setNumeroRighe(String numeroRighe) {
		this.numeroRighe = numeroRighe;
	}


	public CartaEstera getValutaPerRegolarizzazione() {
		return valutaPerRegolarizzazione;
	}


	public void setValutaPerRegolarizzazione(CartaEstera valutaPerRegolarizzazione) {
		this.valutaPerRegolarizzazione = valutaPerRegolarizzazione;
	}


	public String getNumeroRigaSelezionata() {
		return numeroRigaSelezionata;
	}


	public void setNumeroRigaSelezionata(String numeroRigaSelezionata) {
		this.numeroRigaSelezionata = numeroRigaSelezionata;
	}


	public PreDocumentoCarta getDettaglioRiga() {
		return dettaglioRiga;
	}


	public void setDettaglioRiga(PreDocumentoCarta dettaglioRiga) {
		this.dettaglioRiga = dettaglioRiga;
	}


	public String getModalitaPagamento() {
		return modalitaPagamento;
	}


	public void setModalitaPagamento(String modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}


	public String getSpeseECommissioni() {
		return speseECommissioni;
	}


	public void setSpeseECommissioni(String speseECommissioni) {
		this.speseECommissioni = speseECommissioni;
	}
	public boolean isToggleCollegaImpegnoAperto() {
		return toggleCollegaImpegnoAperto;
	}


	public void setToggleCollegaImpegnoAperto(boolean toggleCollegaImpegnoAperto) {
		this.toggleCollegaImpegnoAperto = toggleCollegaImpegnoAperto;
	}

	public String getTitoloStep() {
		return titoloStep;
	}


	public void setTitoloStep(String titoloStep) {
		this.titoloStep = titoloStep;
	}


	public boolean isAggiornamento() {
		return aggiornamento;
	}


	public void setAggiornamento(boolean aggiornamento) {
		this.aggiornamento = aggiornamento;
	}

	public boolean isSoggettoObbligatorio() {
		return soggettoObbligatorio;
	}


	public void setSoggettoObbligatorio(boolean soggettoObbligatorio) {
		this.soggettoObbligatorio = soggettoObbligatorio;
	}

	public boolean isModPagamentoObbligatoria() {
		return modPagamentoObbligatoria;
	}


	public void setModPagamentoObbligatoria(boolean modPagamentoObbligatoria) {
		this.modPagamentoObbligatoria = modPagamentoObbligatoria;
	}


	public String getAnnoCartaInAggiornamento() {
		return annoCartaInAggiornamento;
	}


	public void setAnnoCartaInAggiornamento(String annoCartaInAggiornamento) {
		this.annoCartaInAggiornamento = annoCartaInAggiornamento;
	}


	public String getNumeroCartaInAggiornamento() {
		return numeroCartaInAggiornamento;
	}


	public void setNumeroCartaInAggiornamento(String numeroCartaInAggiornamento) {
		this.numeroCartaInAggiornamento = numeroCartaInAggiornamento;
	}


	public String getStatoOperativoCarta() {
		return statoOperativoCarta;
	}


	public void setStatoOperativoCarta(String statoOperativoCarta) {
		this.statoOperativoCarta = statoOperativoCarta;
	}


	public String getDataStatoOperativoCarta() {
		return dataStatoOperativoCarta;
	}


	public void setDataStatoOperativoCarta(String dataStatoOperativoCarta) {
		this.dataStatoOperativoCarta = dataStatoOperativoCarta;
	}

	public BigDecimal getSommaDocCollegatiRegolazione() {
		return sommaDocCollegatiRegolazione;
	}


	public void setSommaDocCollegatiRegolazione(BigDecimal sommaDocCollegatiRegolazione) {
		this.sommaDocCollegatiRegolazione = sommaDocCollegatiRegolazione;
	}


	public List<ClassificazioneSoggetto> getListaClassificazioneSoggetto() {
		return listaClassificazioneSoggetto;
	}


	public void setListaClassificazioneSoggetto(List<ClassificazioneSoggetto> listaClassificazioneSoggetto) {
		this.listaClassificazioneSoggetto = listaClassificazioneSoggetto;
	}


	public SubdocumentoSpesa getDocumentoInModifica() {
		return documentoInModifica;
	}


	public void setDocumentoInModifica(SubdocumentoSpesa documentoInModifica) {
		this.documentoInModifica = documentoInModifica;
	}


	public String getNumeroRegistrazione() {
		return numeroRegistrazione;
	}


	public void setNumeroRegistrazione(String numeroRegistrazione) {
		this.numeroRegistrazione = numeroRegistrazione;
	}


	public List<SubdocumentoSpesa> getListaSubDocumentoSpesa() {
		return listaSubDocumentoSpesa;
	}


	public void setListaSubDocumentoSpesa(List<SubdocumentoSpesa> listaSubDocumentoSpesa) {
		this.listaSubDocumentoSpesa = listaSubDocumentoSpesa;
	}


	public BigDecimal getImportoDaRegolarizzare() {
		return importoDaRegolarizzare;
	}


	public void setImportoDaRegolarizzare(BigDecimal importoDaRegolarizzare) {
		this.importoDaRegolarizzare = importoDaRegolarizzare;
	}


	public BigDecimal getSommaDocumentiCollegati() {
		return sommaDocumentiCollegati;
	}


	public void setSommaDocumentiCollegati(BigDecimal sommaDocumentiCollegati) {
		this.sommaDocumentiCollegati = sommaDocumentiCollegati;
	}


	public String getImportoPopupFormattato() {
		return importoPopupFormattato;
	}


	public void setImportoPopupFormattato(String importoPopupFormattato) {
		this.importoPopupFormattato = importoPopupFormattato;
	}


	public boolean isHasCollegaImpegnoBloccato() {
		return hasCollegaImpegnoBloccato;
	}


	public void setHasCollegaImpegnoBloccato(boolean hasCollegaImpegnoBloccato) {
		this.hasCollegaImpegnoBloccato = hasCollegaImpegnoBloccato;
	}

	public String getCodiceDivisaCaricato() {
		return codiceDivisaCaricato;
	}

	public void setCodiceDivisaCaricato(String codiceDivisaCaricato) {
		this.codiceDivisaCaricato = codiceDivisaCaricato;
	}


	public boolean isPresenzaRigheSelezionabili() {
		return presenzaRigheSelezionabili;
	}


	public void setPresenzaRigheSelezionabili(boolean presenzaRigheSelezionabili) {
		this.presenzaRigheSelezionabili = presenzaRigheSelezionabili;
	}


	public boolean isToggleCollegaDocumentoAperto() {
		return toggleCollegaDocumentoAperto;
	}


	public void setToggleCollegaDocumentoAperto(boolean toggleCollegaDocumentoAperto) {
		this.toggleCollegaDocumentoAperto = toggleCollegaDocumentoAperto;
	}


	public GestioneCartaRicercaDocumentoModel getRicercaDocumenti() {
		return ricercaDocumenti;
	}


	public void setRicercaDocumenti(GestioneCartaRicercaDocumentoModel ricercaDocumenti) {
		this.ricercaDocumenti = ricercaDocumenti;
	}


	public String getFirma1() {
		return firma1;
	}


	public void setFirma1(String firma1) {
		this.firma1 = firma1;
	}


	public String getFirma2() {
		return firma2;
	}


	public void setFirma2(String firma2) {
		this.firma2 = firma2;
	}


	public String getIntestazioneFirma1() {
		return intestazioneFirma1;
	}


	public void setIntestazioneFirma1(String intestazioneFirma1) {
		this.intestazioneFirma1 = intestazioneFirma1;
	}


	public String getIntestazioneFirma2() {
		return intestazioneFirma2;
	}


	public void setIntestazioneFirma2(String intestazioneFirma2) {
		this.intestazioneFirma2 = intestazioneFirma2;
	}
	

}
