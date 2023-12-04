/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.util.Element;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.AccertamentoRicercaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestioneCruscottoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciModificaMovimentoSpesaModel;
import it.csi.siac.siacfinapp.frontend.ui.util.CruscottoRorUtilities;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoROR;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/*
 * Action per gestisci Impegno Ror...SIAC-6997*/
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaModificaAccRorAction extends BaseRorEntrata {

	/**
	 * 
	 */
	// proprietà di aggiornaImpegnoStep1
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS_SALVA = "success_salva";
	private static final String SUCCESS_AGGIORNA = "success_salva";
	private String numeroImpegno;
	private String annoImpegno;

	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	private Boolean abilitaAzioneInserimentoProvvedimento = true;
	private boolean abilitaPropagaDaSub;

	// proprietà
	private String minImp;
	private String maxImp;
	private String minSub;
	private String maxSub;
	private String minAnche;
	private String maxAnche;
	private String tipoImpegno;
	private String importoImpegnoModImp;
	private String importoAttualeSubImpegno;
	private String descrizione;
	private List<String> tipoImpegnoModificaImportoList = new ArrayList<String>();
	private List<String> numeroSubImpegnoList = new ArrayList<String>();
	private List<String> abbinaList = new ArrayList<String>();
	private String abbina;
	private String abbinaChk;
	private boolean subAccertamentoSelected = false;
	private String subSelected;
	private String anniPluriennali;
	private String idTipoMotivo;
	private boolean modificheDaPropagarePieno;

	private boolean propagaSelected = false;

	// Parametri SubImpegno
	private String numeroSubAccertamento;
	private Integer uidMovgest;
	// IMPORTI CALCOLATI
	private String minImportoCalcolato;
	private String maxImportoCalcolato;
	private String idSub;
	private String minImportoSubCalcolato;
	private String maxImportoSubCalcolato;
	private BigDecimal minImportoImpegnoApp;
	private BigDecimal maxImportoImpegnoApp;
	private BigDecimal minImportoSubApp;
	private BigDecimal maxImportoSubApp;

	private AttoAmministrativo am;

	private static final String SI = "Si";
	private static final String NO = "No";

	// SIAC-6997 Properties
	private CodificaFin reimputazione;
	private CodificaFin cancellazioneInsussistenza;
	private CodificaFin cancellazioneInesigibilita;
	private CodificaFin mantenere;

	private String descrizioneMotivoReimp;
	private String descrizioneMotivoRorCancellazioneInsussistenza;
	private String descrizioneMotivoRorCancellazioneInesigibilita;
	private String descrizioneMotivoRorMantenere;

	// SIAC-6997
	// modifiche da cruscotto
	@Element(value = GestioneCruscottoModel.class)
	private List<GestioneCruscottoModel> listaReimputazioneTriennio;
	private Integer indiceRimozioneAnno;

	private GestioneCruscottoModel rorCancellazioneInesigibilita;
	private GestioneCruscottoModel rorCancellazioneInsussistenza;
	private GestioneCruscottoModel rorDaMantenere;
	private Boolean checkDaMantenere;
	
	private boolean disabledDaMantenere;

	// per aggiornamento
	private List<ModificaMovimentoGestioneEntrata> listaModificheRor;
	private List<ModificaMovimentoGestioneEntrata> listaModificheRorDaAggiornare;

	//SIAC-7349 Inizio  SR190 FL 15/04/2020
	private GestioneCruscottoModel reimputazioneAnnualita;
	//SIAC-7349 Fine  SR190 FL 15/04/2020
	
	private Boolean insussistenzaRor;
	private Boolean inesigibilitaRor;
	private Boolean daMantenerePresente;
	private String uidModifica;
	

	/// motivi di con

	public Boolean getCheckDaMantenere() {
		if(checkDaMantenere==null){
			return false;
		}
		return checkDaMantenere;
	}

	public void setCheckDaMantenere(Boolean checkDaMantenere) {
		this.checkDaMantenere = checkDaMantenere;
	}

	private boolean inInserimento = true;

	// property di elenco modifiche spesa
	// SIAC-7968  Carico le MOTIVAZIONE  dell'accertamento
//	private MotiviReimputazioneROR[] motiviRorReimputazione;
//	private MotiviCancellazioneROR[] motiviRorCancellazione;
//	private MotiviMantenimentoROR[] motiviRorMantenimento;
	private MotiviReimputazioneAccROR[] motiviRorReimputazione;
	private MotiviCancellazioneAccROR[] motiviRorCancellazione;
	private MotiviMantenimentoAccROR[] motiviRorMantenimento;

	public AggiornaModificaAccRorAction() {
		// setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}

	public Boolean getFlagValido() {
		return flagValido;
	}

	public void setFlagValido(Boolean flagValido) {
		this.flagValido = flagValido;
	}

	public Boolean getFlagSoggettoValido() {
		return flagSoggettoValido;
	}

	public void setFlagSoggettoValido(Boolean flagSoggettoValido) {
		this.flagSoggettoValido = flagSoggettoValido;
	}

	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getAnnoImpegno() {
		return annoImpegno;
	}

	public void setAnnoImpegno(String annoImpegno) {
		this.annoImpegno = annoImpegno;
	}

	public Boolean getAbilitaAzioneInserimentoProvvedimento() {
		return abilitaAzioneInserimentoProvvedimento;
	}

	public void setAbilitaAzioneInserimentoProvvedimento(Boolean abilitaAzioneInserimentoProvvedimento) {
		this.abilitaAzioneInserimentoProvvedimento = abilitaAzioneInserimentoProvvedimento;
	}

	public String getMinImp() {
		return minImp;
	}

	public void setMinImp(String minImp) {
		this.minImp = minImp;
	}

	public String getMaxImp() {
		return maxImp;
	}

	public void setMaxImp(String maxImp) {
		this.maxImp = maxImp;
	}

	public String getMinSub() {
		return minSub;
	}

	public void setMinSub(String minSub) {
		this.minSub = minSub;
	}

	public String getMaxSub() {
		return maxSub;
	}

	public void setMaxSub(String maxSub) {
		this.maxSub = maxSub;
	}

	public String getMinAnche() {
		return minAnche;
	}

	public void setMinAnche(String minAnche) {
		this.minAnche = minAnche;
	}

	public String getMaxAnche() {
		return maxAnche;
	}

	public void setMaxAnche(String maxAnche) {
		this.maxAnche = maxAnche;
	}

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public String getImportoImpegnoModImp() {
		return importoImpegnoModImp;
	}

	public void setImportoImpegnoModImp(String importoImpegnoModImp) {
		this.importoImpegnoModImp = importoImpegnoModImp;
	}

	public String getImportoAttualeSubImpegno() {
		return importoAttualeSubImpegno;
	}

	public void setImportoAttualeSubImpegno(String importoAttualeSubImpegno) {
		this.importoAttualeSubImpegno = importoAttualeSubImpegno;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<String> getTipoImpegnoModificaImportoList() {
		return tipoImpegnoModificaImportoList;
	}

	public void setTipoImpegnoModificaImportoList(List<String> tipoImpegnoModificaImportoList) {
		this.tipoImpegnoModificaImportoList = tipoImpegnoModificaImportoList;
	}

	public List<String> getNumeroSubImpegnoList() {
		return numeroSubImpegnoList;
	}

	public void setNumeroSubImpegnoList(List<String> numeroSubImpegnoList) {
		this.numeroSubImpegnoList = numeroSubImpegnoList;
	}

	public List<String> getAbbinaList() {
		return abbinaList;
	}

	public void setAbbinaList(List<String> abbinaList) {
		this.abbinaList = abbinaList;
	}

	public String getAbbina() {
		return abbina;
	}

	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}

	public String getAbbinaChk() {
		return abbinaChk;
	}

	public void setAbbinaChk(String abbinaChk) {
		this.abbinaChk = abbinaChk;
	}

	public boolean isSubAccertamentoSelected() {
		return subAccertamentoSelected;
	}

	public void setSubAccertamentoSelected(boolean subAccertamentoSelected) {
		this.subAccertamentoSelected= subAccertamentoSelected;
	}

	public String getSubSelected() {
		return subSelected;
	}

	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}

	public String getAnniPluriennali() {
		return anniPluriennali;
	}

	public void setAnniPluriennali(String anniPluriennali) {
		this.anniPluriennali = anniPluriennali;
	}

	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}

	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}

	public String getNumeroSubAccertamento() {
		return numeroSubAccertamento;
	}

	public void setNumeroSubAccertamento(String numeroSubAccertamento) {
		this.numeroSubAccertamento = numeroSubAccertamento;
	}

	public String getMinImportoCalcolato() {
		return minImportoCalcolato;
	}

	public void setMinImportoCalcolato(String minImportoCalcolato) {
		this.minImportoCalcolato = minImportoCalcolato;
	}

	public String getMaxImportoCalcolato() {
		return maxImportoCalcolato;
	}

	public void setMaxImportoCalcolato(String maxImportoCalcolato) {
		this.maxImportoCalcolato = maxImportoCalcolato;
	}

	public String getIdSub() {
		return idSub;
	}

	public void setIdSub(String idSub) {
		this.idSub = idSub;
	}

	public String getMinImportoSubCalcolato() {
		return minImportoSubCalcolato;
	}

	public void setMinImportoSubCalcolato(String minImportoSubCalcolato) {
		this.minImportoSubCalcolato = minImportoSubCalcolato;
	}

	public String getMaxImportoSubCalcolato() {
		return maxImportoSubCalcolato;
	}

	public void setMaxImportoSubCalcolato(String maxImportoSubCalcolato) {
		this.maxImportoSubCalcolato = maxImportoSubCalcolato;
	}

	public BigDecimal getMinImportoImpegnoApp() {
		return minImportoImpegnoApp;
	}

	public void setMinImportoImpegnoApp(BigDecimal minImportoImpegnoApp) {
		this.minImportoImpegnoApp = minImportoImpegnoApp;
	}

	public BigDecimal getMaxImportoImpegnoApp() {
		return maxImportoImpegnoApp;
	}

	public void setMaxImportoImpegnoApp(BigDecimal maxImportoImpegnoApp) {
		this.maxImportoImpegnoApp = maxImportoImpegnoApp;
	}

	public BigDecimal getMinImportoSubApp() {
		return minImportoSubApp;
	}

	public void setMinImportoSubApp(BigDecimal minImportoSubApp) {
		this.minImportoSubApp = minImportoSubApp;
	}

	public BigDecimal getMaxImportoSubApp() {
		return maxImportoSubApp;
	}

	public void setMaxImportoSubApp(BigDecimal maxImportoSubApp) {
		this.maxImportoSubApp = maxImportoSubApp;
	}

	public AttoAmministrativo getAm() {
		return am;
	}

	public void setAm(AttoAmministrativo am) {
		this.am = am;
	}
	// SIAC-7968 inizio
	
	
//	public MotiviReimputazioneROR[] getMotiviRorReimputazione() {
//		return motiviRorReimputazione;
//	}
//
//	public void setMotiviRorReimputazione(MotiviReimputazioneROR[] motiviRorReimputazione) {
//		this.motiviRorReimputazione = motiviRorReimputazione;
//	}
//
//	public MotiviCancellazioneROR[] getMotiviRorCancellazione() {
//		return motiviRorCancellazione;
//	}
//
//	public void setMotiviRorCancellazione(MotiviCancellazioneROR[] motiviRorCancellazione) {
//		this.motiviRorCancellazione = motiviRorCancellazione;
//	}
//
//	public MotiviMantenimentoROR[] getMotiviRorMantenimento() {
//		return motiviRorMantenimento;
//	}
//
//	public void setMotiviRorMantenimento(MotiviMantenimentoROR[] motiviRorMantenimento) {
//		this.motiviRorMantenimento = motiviRorMantenimento;
//	}
	/**
	 * @return the motiviRorReimputazione
	 */
	public MotiviReimputazioneAccROR[] getMotiviRorReimputazione()
	{
		return motiviRorReimputazione;
	}

	/**
	 * @param motiviRorReimputazione the motiviRorReimputazione to set
	 */
	public void setMotiviRorReimputazione(MotiviReimputazioneAccROR[] motiviRorReimputazione)
	{
		this.motiviRorReimputazione = motiviRorReimputazione;
	}

	/**
	 * @return the motiviRorCancellazione
	 */
	public MotiviCancellazioneAccROR[] getMotiviRorCancellazione()
	{
		return motiviRorCancellazione;
	}

	/**
	 * @param motiviRorCancellazione the motiviRorCancellazione to set
	 */
	public void setMotiviRorCancellazione(MotiviCancellazioneAccROR[] motiviRorCancellazione)
	{
		this.motiviRorCancellazione = motiviRorCancellazione;
	}

	/**
	 * @return the motiviRorMantenimento
	 */
	public MotiviMantenimentoAccROR[] getMotiviRorMantenimento()
	{
		return motiviRorMantenimento;
	}

	/**
	 * @param motiviRorMantenimento the motiviRorMantenimento to set
	 */
	public void setMotiviRorMantenimento(MotiviMantenimentoAccROR[] motiviRorMantenimento)
	{
		this.motiviRorMantenimento = motiviRorMantenimento;
	}
	//SIAC-7968 Fine

	public CodificaFin getReimputazione() {
		return reimputazione;
	}

	

	public void setReimputazione(CodificaFin reimputazione) {
		this.reimputazione = reimputazione;
	}

	public String getDescrizioneMotivoReimp() {
		return descrizioneMotivoReimp;
	}

	public void setDescrizioneMotivoReimp(String descrizioneMotivoReimp) {
		this.descrizioneMotivoReimp = descrizioneMotivoReimp;
	}

	public String getDescrizioneMotivoRorCancellazioneInsussistenza() {
		return descrizioneMotivoRorCancellazioneInsussistenza;
	}

	public void setDescrizioneMotivoRorCancellazioneInsussistenza(
			String descrizioneMotivoRorCancellazioneInsussistenza) {
		this.descrizioneMotivoRorCancellazioneInsussistenza = descrizioneMotivoRorCancellazioneInsussistenza;
	}

	public boolean isInInserimento() {
		return inInserimento;
	}

	public void setInInserimento(boolean inInserimento) {
		this.inInserimento = inInserimento;
	}

	public List<GestioneCruscottoModel> getListaReimputazioneTriennio() {
		return listaReimputazioneTriennio;
	}

	public void setListaReimputazioneTriennio(List<GestioneCruscottoModel> listaReimputazioneTriennio) {
		this.listaReimputazioneTriennio = listaReimputazioneTriennio;
	}

	public Integer getIndiceRimozioneAnno() {
		return indiceRimozioneAnno;
	}

	public void setIndiceRimozioneAnno(Integer indiceRimozioneAnno) {
		this.indiceRimozioneAnno = indiceRimozioneAnno;
	}

	public GestioneCruscottoModel getRorCancellazioneInesigibilita() {
		return rorCancellazioneInesigibilita;
	}

	public void setRorCancellazioneInesigibilita(GestioneCruscottoModel rorCancellazioneInesigibilita) {
		this.rorCancellazioneInesigibilita = rorCancellazioneInesigibilita;
	}

	public GestioneCruscottoModel getRorCancellazioneInsussistenza() {
		return rorCancellazioneInsussistenza;
	}

	public void setRorCancellazioneInsussistenza(GestioneCruscottoModel rorCancellazioneInsussistenza) {
		this.rorCancellazioneInsussistenza = rorCancellazioneInsussistenza;
	}

	public GestioneCruscottoModel getRorDaMantenere() {
		return rorDaMantenere;
	}

	public void setRorDaMantenere(GestioneCruscottoModel rorDaMantenere) {
		this.rorDaMantenere = rorDaMantenere;
	}

	public CodificaFin getCancellazioneInsussistenza() {
		return cancellazioneInsussistenza;
	}

	public void setCancellazioneInsussistenza(CodificaFin cancellazioneInsussistenza) {
		this.cancellazioneInsussistenza = cancellazioneInsussistenza;
	}

	public CodificaFin getCancellazioneInesigibilita() {
		return cancellazioneInesigibilita;
	}

	public void setCancellazioneInesigibilita(CodificaFin cancellazioneInesigibilita) {
		this.cancellazioneInesigibilita = cancellazioneInesigibilita;
	}

	public CodificaFin getMantenere() {
		return mantenere;
	}

	public void setMantenere(CodificaFin mantenere) {
		this.mantenere = mantenere;
	}

	public String getDescrizioneMotivoRorCancellazioneInesigibilita() {
		return descrizioneMotivoRorCancellazioneInesigibilita;
	}

	public void setDescrizioneMotivoRorCancellazioneInesigibilita(
			String descrizioneMotivoRorCancellazioneInesigibilita) {
		this.descrizioneMotivoRorCancellazioneInesigibilita = descrizioneMotivoRorCancellazioneInesigibilita;
	}

	public String getDescrizioneMotivoRorMantenere() {
		return descrizioneMotivoRorMantenere;
	}

	public void setDescrizioneMotivoRorMantenere(String descrizioneMotivoRorMantenere) {
		this.descrizioneMotivoRorMantenere = descrizioneMotivoRorMantenere;
	}

	public List<ModificaMovimentoGestioneEntrata> getListaModificheRor() {
		return listaModificheRor;
	}

	public void setListaModificheRor(List<ModificaMovimentoGestioneEntrata> listaModificheRor) {
		this.listaModificheRor = listaModificheRor;
	}

	public List<ModificaMovimentoGestioneEntrata> getListaModificheRorDaAggiornare() {
		return listaModificheRorDaAggiornare;
	}

	public void setListaModificheRorDaAggiornare(List<ModificaMovimentoGestioneEntrata> listaModificheRorDaAggiornare) {
		this.listaModificheRorDaAggiornare = listaModificheRorDaAggiornare;
	}

	public Boolean getInesigibilitaRor() {
		return inesigibilitaRor;
	}

	public void setInesigibilitaRor(Boolean inesigibilitaRor) {
		this.inesigibilitaRor = inesigibilitaRor;
	}

	public Boolean getInsussistenzaRor() {
		return insussistenzaRor;
	}

	public void setInsussistenzaRor(Boolean insussistenzaRor) {
		this.insussistenzaRor = insussistenzaRor;
	}

	public Boolean getDaMantenerePresente() {
		return daMantenerePresente;
	}

	public void setDaMantenerePresente(Boolean daMantenerePresente) {
		this.daMantenerePresente = daMantenerePresente;
	}

	public String getUidModifica() {
		return uidModifica;
	}

	public void setUidModifica(String uidModifica) {
		this.uidModifica = uidModifica;
	}

	public boolean isAbilitaPropagaDaSub() {
		return abilitaPropagaDaSub;
	}

	public void setAbilitaPropagaDaSub(boolean abilitaPropagaDaSub) {
		this.abilitaPropagaDaSub = abilitaPropagaDaSub;
	}

	public boolean isModificheDaPropagarePieno() {
		return modificheDaPropagarePieno;
	}

	public void setModificheDaPropagarePieno(boolean modificheDaPropagarePieno) {
		this.modificheDaPropagarePieno = modificheDaPropagarePieno;
	}

	public boolean isDisabledDaMantenere() {
		return disabledDaMantenere;
	}

	public void setDisabledDaMantenere(boolean disabledDaMantenere) {
		this.disabledDaMantenere = disabledDaMantenere;
	}
	
	@Autowired
	ProvvedimentoService provvedimentoService;

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");

		// leggo eventuali informazioni

		// invoco il prepare della super classe:
		super.prepare();
		this.model.setTitolo("Gestisci Accertamento ROR");

		/**
		 * *modifiche in aggiornamento queste, vanno filtrate per stato valido.
		 * Non mostro quelle annullate
		 */

		List<ModificaMovimentoGestioneEntrata> modificheOld = sessionHandler
				.getParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO);// Modifiche
																						// attualmente
																						// in
																						// DB
		List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
		if (modificheOld != null) {

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(modificheOld, modifichePerCruscotto);
		}

		setListaModificheRorDaAggiornare(modifichePerCruscotto);
		
		

		
		// Variazioni delle modifiche da salvare -> nuovi valori per
		// aggiornamento
		List<ModificaMovimentoGestioneEntrata> modificheList = model.getAccertamentoInAggiornamento()
				.getListaModificheMovimentoGestioneEntrata();
		
		if(!modifichePerCruscotto.isEmpty()){
			setListaModificheRorDaAggiornare(modifichePerCruscotto);
			
		}else{
			setListaModificheRorDaAggiornare(modificheList);
		}

		// Filtro modifiche
		if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);
		}

		// Inizializzazione
		// controllo se invece si tratta di un subimpegno
		//FIXME getnSubAccertamento() ->  || model.getnSubImpegno() != null
		if (getNumeroSubAccertamento() != null || model.getnSubImpegno() != null) {
			modifichePerCruscotto = checkSubHasNotModifies();
		}

		if (modifichePerCruscotto == null || modifichePerCruscotto.isEmpty()) {
			setInInserimento(true);
		} else {
			setInInserimento(false);
			checkInsussistenzaInsegibilitaMantenerePresenti(modifichePerCruscotto);

		}

		// prepare di aggiorna impegno. Carico tutte le configurazioni
		this.model.setOperazioneAggiorna(true);
		try {
			if (model.getListaTipiProvvedimenti() == null || model.getListaTipiProvvedimenti().size() == 0) {
				caricaTipiProvvedimenti();
			}

			if (model.getListaTipiAmbito() == null || model.getListaTipiAmbito().isEmpty()) {
				caricaListaAmbiti();
			}

			caricaStatiOperativiAtti();

			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();

			model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
			model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());

			// SIAC-6997
			model.getStep1Model().setDaReanno(buildListaSiNo());

			model.getStep1Model().setDaPrenotazione(buildListaSiNo());
			model.getStep1Model().setDiCassaEconomale(buildListaSiNo());

			model.getStep1Model().setScelteFrazionabile(buildListaFrazionabile());

			// non agisce l'ancora dei vincoli
			model.getStep1Model().setPortaAdAltezzaVincoli(false);

			// tipo debito siope:
			model.getStep1Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());
			prepareAggiornaModifiche();
			// inizializzaListaModifiche();

		} catch (Exception e) {
			log.error("prepare", e.getMessage());
		}

		setVisualizzaLinkConsultaModificheProvvedimento(false);

		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}
		
		if(model.getCodicePdc()!=null){
			inizializzaDescrizioniEMotivi();			
		}

		setAbilitazioni();
		//SIAC-7349 Inizio  SR190 FL 15/04/2020
		setListaModificheSpeseCollegata(
				//SIAC-8041 filtro la lista modifiche per reimputazioni
				//SIAC-8609, gestione aggiornamento associazioni gia' inserite
				initListaModificheSpesaCollegataSoloReimpAggiornamento(
						modifichePerCruscotto
				)
		);
		//SIAC-7349 Fine SR190 FL 15/04/2020

	}

	protected void setMaxMinImporti() {
		String minImpApp ="";
		
		BigDecimal disponibilitaUtilizzare = BigDecimal.ZERO;
		if(model.getAccertamentoInAggiornamento().getDisponibilitaUtilizzare()!=null){
			disponibilitaUtilizzare = model.getAccertamentoInAggiornamento().getDisponibilitaUtilizzare();
		}
		
		if(!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")){
			// non e' definitivo
			BigDecimal minTemp = model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare().min(disponibilitaUtilizzare);
			minImpApp = convertiBigDecimalToImporto(minTemp);
		} else {
			//e' definitivo
			BigDecimal minTemp = model.getAccertamentoInAggiornamento().getDisponibilitaIncassare().min(disponibilitaUtilizzare);
			minImpApp = convertiBigDecimalToImporto(minTemp);
		}
		
		if(!minImpApp.contains("-")){
			minImpApp = "-" + minImpApp;
		}
		
		log.debug("",minImpApp);//
		
		minImportoImpegnoApp = convertiImportoToBigDecimal(minImpApp);
		
		if(model.getAccertamentoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
			//mi riconduco a questa casistica in modo da non avere un massimo:
			model.setFlagSuperioreTreAnni(true);
			//
		} else if(model.getAccertamentoInAggiornamento().isFlagDaRiaccertamento()){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else {
			//SIAC-580
			
			if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()){
				// anno corrente
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno1();
				
			}else if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == (model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()+1 )){
//				anno  +1
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno2();
			}else if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == (model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()+2 )){
//				anno +2
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno3();
			}else{
				//  da GESTIRE NELLA PAGINA
				model.setFlagSuperioreTreAnni(true);
			}
		}
		
		if(minImportoImpegnoApp == null){
			//pongo a zero
			minImportoImpegnoApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp == null){
			maxImportoImpegnoApp = new BigDecimal(0);
		}else if(maxImportoImpegnoApp.compareTo(BigDecimal.ZERO)<0){
				maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//Setto il massimo e i minimo nelle variabili finali:
		setMaxImp(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		setMinImp(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMaxImpMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMinImpMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		
		setMinImportoCalcolato(convertiBigDecimalToImporto(minImportoImpegnoApp));
		setMaxImportoCalcolato(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMinImportoCalcolatoMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMaxImportoCalcolatoMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
	}

	private List<ModificaMovimentoGestioneEntrata> checkSubHasNotModifies() {
		List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
		//FIXME  || model.getnSubAccertameto() != null
		if (getNumeroSubAccertamento() != null || model.getnSubImpegno() != null) {

			List<SubAccertamento> listaSubimpegni = model.getListaSubaccertamenti();
			SubAccertamento sub = new SubAccertamento();
			for (int i = 0; i < listaSubimpegni.size(); i++) {
				if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(model.getnSubImpegno()))) {
					sub = listaSubimpegni.get(i);
					break;
				}
			}
			List<ModificaMovimentoGestioneEntrata> listaModifiche = new ArrayList<ModificaMovimentoGestioneEntrata>();
			listaModifiche = sub.getListaModificheMovimentoGestioneEntrata();
			if (listaModifiche == null) {
				listaModifiche = new ArrayList<ModificaMovimentoGestioneEntrata>();
			}

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(listaModifiche,
					modifichePerCruscotto);

			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);

		}

		return modifichePerCruscotto;
	}

	// Per la gestione delle modifiche pregresse -> Serve a mantenere una
	// coerenza con la maschera di Inserisci (quando non ve ne sono presenti)
	private void checkInsussistenzaInsegibilitaMantenerePresenti(List<ModificaMovimentoGestioneEntrata> modificheList) {

		int ineRor = 0;
		int insRor = 0;
		int manRor = 0;

		for (int i = 0; i < modificheList.size(); i++) {
			if (!modificheList.get(i).getTipoModificaMovimentoGestione().equals("REIMP")) {
				if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("INEROR")) {
					ineRor++;
				} else if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("INSROR")) {
					insRor++;
				} else if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("RORM")) {
					manRor++;
				}
			}
		}

		if (ineRor > 0) {
			setInesigibilitaRor(true);
		} else {
			setInesigibilitaRor(false);
			rorCancellazioneInesigibilita = new GestioneCruscottoModel();
		}

		if (insRor > 0) {
			setInsussistenzaRor(true);
		} else {
			setInsussistenzaRor(false);
			rorCancellazioneInsussistenza = new GestioneCruscottoModel();
		}

		if (manRor > 0) {
			setDaMantenerePresente(true);
		} else {
			setDaMantenerePresente(false);
			rorDaMantenere = new GestioneCruscottoModel();
		}

	}

	// Inizializza la lista del triennio reimputazione, per costruire la mappa
	// nel cruscotto
	private void inizializzaListaModifiche() {
		GestioneCruscottoModel modifica0 = new GestioneCruscottoModel();
		GestioneCruscottoModel modifica1 = new GestioneCruscottoModel();
		GestioneCruscottoModel modifica2 = new GestioneCruscottoModel();

		listaReimputazioneTriennio = new ArrayList<GestioneCruscottoModel>();

		if (listaReimputazioneTriennio == null || listaReimputazioneTriennio.isEmpty()) {
			// anno di reimputazione deve essere maggiore dell'anno di bilancio
			Integer anno = Integer.valueOf(sessionHandler.getBilancio().getAnno());
			modifica0.setAnno(anno + 1);
			modifica0.setIndex(0);
			getListaReimputazioneTriennio().add(modifica0);
			modifica1.setAnno(anno + 2);
			modifica1.setIndex(1);
			getListaReimputazioneTriennio().add(modifica1);
			modifica2.setAnno(anno + 3);
			modifica2.setIndex(2);
			getListaReimputazioneTriennio().add(modifica2);
		}

	}

	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// ripopoliamo i dati provv dal model support:
		// setta nel mode
		ripopolaProvvedimentoDaSupport();
		
		model.setProseguiConWarningModificaPositivaAccertamento(false);

		// Anno capitolo
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}

		// Anno impegno
		if (getAnnoImpegno() != null) {
			model.getStep1Model().setAnnoImpegno(Integer.valueOf(getAnnoImpegno()));
			model.getStep1Model().setNumeroImpegno(Integer.valueOf(getNumeroImpegno()));
		} else {
			model.getStep1Model().setAnnoImpegno(model.getAnnoImpegno());
			model.getStep1Model().setNumeroImpegno(model.getNumeroImpegno());
		}
		
		//il numero va nel model del subimpegno
		if ((getNumeroSubAccertamento() != null && !getNumeroSubAccertamento().equals(""))) {
			model.getStep1Model().setNumeroSubImpegno(Integer.valueOf(getNumeroSubAccertamento()));
		}

		// transazione elementare:
		if (teSupport == null) {
			pulisciTransazioneElementare();
		}
		// utilizzato per la transazione e le condizioni di obbligatorieta
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());

		if (forceReload) {

			// CARICAMENTO AVANZOVINCOLI:
			caricaListaAvanzovincolo();
			initSceltaAccertamentoAvanzoList();
			//

			sessionHandler.cleanSafelyExcluding(FinSessionParameter.ACCERTAMENTO_CERCATO);
			caricaDatiAccertamento(false);
			
			//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
			if(model.hasErrori()) {
				return INPUT;
			}
			
			sessionHandler.setParametro(FinSessionParameter.ACCERTAMENTO_CERCATO, getAccertamentoToUpdate());
			// gestione subimpegni

			// Jira - 1298 altrimenti carica ad ogni giro tutti i dati da
			// bilancio
			if (caricaListeBil(WebAppConstants.CAP_EG)) {
				return INPUT;
			}

			if(model.getAccertamentoInAggiornamento().getProgetto()!=null && model.getAccertamentoInAggiornamento().getProgetto().getCodice()!=null){
				popolaProgetto(model.getAccertamentoInAggiornamento().getProgetto());
			}

			if(((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.ACCERTAMENTO_CERCATO)).isFlagDaRiaccertamento()){
				model.getStep1Model().setRiaccertato(SI);
			} else {
				model.getStep1Model().setRiaccertato(NO);
			}

		}

		

		//inizio SIAC-6997
		if(((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.ACCERTAMENTO_CERCATO)).isFlagDaReanno()){
			model.getStep1Model().setReanno(SI);
		} else {
			model.getStep1Model().setReanno(NO);
		}
		//fine SIAC-6997

		creaMovGestModelCache();

		// controlliamo lo stato provvedimento:
		controllaStatoProvvedimento();

		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());

		// disabilito il caricamento degli alberi inutili per questo scnario (in
		// AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		// CR-2023
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////

		// setto l'anno capitolo per la ricerca guidata del vincolo
		if (model.getStep1Model().getCapitolo() != null && model.getStep1Model().getCapitolo().getAnno() != null) {
			model.getStep1Model().setAccertamentoRicerca(new AccertamentoRicercaModel());
			model.getStep1Model().getAccertamentoRicerca()
					.setAnnoCapitolo(String.valueOf(model.getStep1Model().getCapitolo().getAnno()));
		}

		//calcolaTotaliUtilizzabile();

		if (salvaDaSDFANormale()) {
			addActionWarningFin(ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE);
		}

		// leggiEventualiErroriEWarningAzionePrecedente(true);
		clearActionErrors();
		sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE, null);
		
		// carico dati dell'impegno
		caricaDatiTotali(null);

		checkIfIsSub();

		List<StrutturaAmministrativoContabile> lista = sessionHandler.getAccount()
				.getStruttureAmministrativeContabili();

		if (lista != null && !lista.isEmpty()) {

			if (lista.size() > 1) {
				// devo filtrare l'elenco per codice
				for (int j = 0; j < lista.size(); j++) {

					if (lista.get(j).getUid() == Integer
							.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())) {

						model.getStep1Model().setStrutturaSelezionataCompetenteDesc(
								lista.get(j).getCodice() + "-" + lista.get(j).getDescrizione());
					}
				}
			}
		}

		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}

		// Inizializzazione -> Vedi spiegazione alla prepare();

		inizializzaDescrizioniEMotivi();
		
		
		//FIXME dati di riepilogo da sistemare //SIAC-7960 inizio commento
//		BigDecimal importoImpegno = (model.getStep1Model().getImportoImpegno());
//		// int importoPagatoAnnoAntecedente = model.getStep1Model().getI???
//		BigDecimal importoPagatoAnnoAntecedente = (new BigDecimal("-1000"));
//
//		BigDecimal importoDaRiaccertare = importoImpegno.subtract(importoPagatoAnnoAntecedente);
//
//		model.getStep1Model().setImportoDaRiaccertare(importoDaRiaccertare);

		List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
		// fixme modifiche per subimpegno
		if ((getNumeroSubAccertamento() != null && !getNumeroSubAccertamento().equals(""))) {
			List<SubAccertamento> listaSubimpegni = model.getListaSubaccertamenti();
			SubAccertamento sub = new SubAccertamento();
			for (int i = 0; i < listaSubimpegni.size(); i++) {
				if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(getNumeroSubAccertamento()))) {
					sub = listaSubimpegni.get(i);
					break;
				}
			}
			modificheList = sub.getListaModificheMovimentoGestioneEntrata();

		} else {
			modificheList = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
		}
		List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
		if (modificheList != null) {

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(modificheList,
					modifichePerCruscotto);
		}
		if (modifichePerCruscotto != null) {
			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);

		}
		
		//SIAC-7349 Inizio  SR190 FL 15/04/2020
		setListaModificheSpeseCollegata(
				//SIAC-8041 filtro la lista modifiche per reimputazioni
				//SIAC-8609, gestione aggiornamento associazioni gia' inserite
				initListaModificheSpesaCollegataSoloReimpAggiornamento(
						modifichePerCruscotto
				)
		);
		//SIAC-7349 Fine SR190 FL 15/04/2020
		
		if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {

			boolean reimpPresent = CruscottoRorUtilities.checkReimpInModifAcc(modifichePerCruscotto);
			List<ModificaMovimentoGestioneEntrata> cloneModifiche = clone(modifichePerCruscotto);
			sessionHandler.setParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO, cloneModifiche);
			
			//SIAC-7502 visualizzo tutto in positivo
			modifichePerCruscotto =convertiImportiInPositivo(modifichePerCruscotto, true);
			
			setInInserimento(false);
			checkInsussistenzaInsegibilitaMantenerePresenti(modifichePerCruscotto);
			if (reimpPresent) {
				List<ModificaMovimentoGestioneEntrata> listaReimputazioniPresenti = new ArrayList<ModificaMovimentoGestioneEntrata>();
				listaReimputazioniPresenti = CruscottoRorUtilities.getListaReimputazioniPresentiAcc(modifichePerCruscotto);
				modifichePerCruscotto = CruscottoRorUtilities
						.getListaOtherCruscottoModifichePresentiAcc(modifichePerCruscotto);
				listaReimputazioniPresenti.addAll(modifichePerCruscotto);
				
				//SIAC-7349 Inizio  SR190 FL 15/04/2020 Se in aggiornamento del cruscotto vengono valorizzati gli importi residuo e maxcollegabile
				//SIAC-8047
//				List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
//				if (listaModificheReimputazione != null) {
//					for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : listaReimputazioniPresenti) {
//						List<ModificaMovimentoGestioneSpesaCollegata> listaModificheMovimentoGestioneSpesaCollegata = modificaMovimentoGestioneEntrata.getListaModificheMovimentoGestioneSpesaCollegata();
//						if (listaModificheMovimentoGestioneSpesaCollegata != null) {
//							for (ModificaMovimentoGestioneSpesaCollegata modificheMovimentoGestioneSpesaCollegata : listaModificheMovimentoGestioneSpesaCollegata) {
//								//altra soluzione e come aggingi annualita verificare se esiste o meno l'oggetto
//								for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegataAll : listaModificheReimputazione) {
//									if ( modificheSpeseCollegataAll.getModificaMovimentoGestioneSpesa().getImpegno().getNumero().equals(modificheMovimentoGestioneSpesaCollegata.getModificaMovimentoGestioneSpesa().getImpegno().getNumero())
//											&& modificheSpeseCollegataAll.getModificaMovimentoGestioneSpesa().getNumeroModificaMovimentoGestione() == modificheMovimentoGestioneSpesaCollegata.getModificaMovimentoGestioneSpesa().getNumeroModificaMovimentoGestione())
//									{
//										modificheSpeseCollegataAll.setImportoCollegamento(modificheMovimentoGestioneSpesaCollegata.getImportoCollegamento());
//										modificheSpeseCollegataAll.setImportoResiduoCollegare(modificheMovimentoGestioneSpesaCollegata.getImportoResiduoCollegare().add(modificheMovimentoGestioneSpesaCollegata.getImportoCollegamento()));
//										modificheSpeseCollegataAll.setImportoMaxCollegabile(modificheMovimentoGestioneSpesaCollegata.getImportoResiduoCollegare().add(modificheMovimentoGestioneSpesaCollegata.getImportoCollegamento()));
//									}
//								}
//							}
//						}
//					}
//				}
				//SIAC-7349 Fine  SR190 FL 15/04/2020

				setListaModificheRor(listaReimputazioniPresenti);
			} else {
				List<ModificaMovimentoGestioneEntrata> listeNuove = CruscottoRorUtilities.addReimputazioneInAggiornamentoAcc(
						modifichePerCruscotto, Integer.valueOf(sessionHandler.getBilancio().getAnno()));
				setListaModificheRor(listeNuove);
			}

		} else {
			setInInserimento(true);
		}
		
		//SIAC-7960 modifiche 
//		BigDecimal importoGestito = CruscottoRorUtilities.calcolaImportoGestitoAcc(modificheList);
//		model.getStep1Model().setImportoGestito(importoGestito);
//
//		BigDecimal importoDaGestire = importoDaRiaccertare.subtract(importoGestito);
//		model.getStep1Model().setImportoDaGestire(importoDaGestire);

		prepareAggiornaModifiche();

		creaMovGestModelCache();

		// controllo se si tratta di subimpegno e setto nel model il
		// provvedimento associato al subimpegno: se non ci sono modifiche
		// imposto nel model il provvedimento relativo al subimpegno; se ci sono
		// modifiche, setto il provvedimento associato alle modifiche del
		// subimpegno.
		setAbilitazioni();

		// Controllo eventuali messaggi di successo provenienti dal salva
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
		setMaxMinImporti();
		

		
		inizializzaListaModifiche();

		return SUCCESS;

	}

	private List<ModificaMovimentoGestioneEntrata> convertiImportiInPositivo(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2, boolean b) {
		for(ModificaMovimentoGestioneEntrata mmgs : listaModificheRor2){
			if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				BigDecimal importoPositivo = mmgs.getImportoOld().abs();
				mmgs.setImportoOld(importoPositivo);				
			}
		}
		return listaModificheRor2;
	}

	private void checkIfIsSub() {
		if ((getNumeroSubAccertamento() != null && !getNumeroSubAccertamento().equals("")) || model.getnSubImpegno() != null) {
			setTipoImpegno("SubAccertamento");
			setSubSelected(String.valueOf(model.getnSubImpegno()));
			caricaDatiSub();
			
			if (model.getStep1Model().getNumeroSubImpegno().equals(Integer.valueOf(model.getnSubImpegno()))) {
				List<SubAccertamento> listaSubimpegni = model.getListaSubaccertamenti();
				SubAccertamento sub = new SubAccertamento();
				for (int i = 0; i < listaSubimpegni.size(); i++) {
					if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(model.getnSubImpegno()))) {
						sub = listaSubimpegni.get(i);
						break;
					}
				}
				List<ModificaMovimentoGestioneEntrata> listaModifiche = sub.getListaModificheMovimentoGestioneEntrata();
				List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
				if (listaModifiche != null) {

					modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(listaModifiche,
							modifichePerCruscotto);
				}

				if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
					modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);
				}
				//Fix: considerava anche le modifiche annullate e prendeva i provvedimenti sbagliati.
				//deve settare quello dellhe modifiche valide per il cruscotto
				if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
					model.getStep1Model().setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(
							modifichePerCruscotto.get(0).getAttoAmministrativo()));
					model.getStep1ModelCache().setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(
							modifichePerCruscotto.get(0).getAttoAmministrativo()));

					// model.setImpegnoInAggiornamento(sub);
				} else {
					setInInserimento(true);
					model.getStep1Model()
							.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));
				}
				model.setSubAccertamentoInAggiornamento(sub);

			}
		} else {
			List<ModificaMovimentoGestioneEntrata> listaModifiche = model.getAccertamentoInAggiornamento()
					.getListaModificheMovimentoGestioneEntrata();
			List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
			if (listaModifiche != null) {

				modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(listaModifiche,
						modifichePerCruscotto);
			}
			if (modifichePerCruscotto != null) {
				modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);
			}

			if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
				model.getStep1Model().setProvvedimento(
						CruscottoRorUtilities.mapAttoToProvv(modifichePerCruscotto.get(0).getAttoAmministrativo()));
			} else {
				if (null != model.getStep1Model().getProvvedimento()
						&& null != model.getStep1Model().getProvvedimento().getAnnoProvvedimento()) {
					// CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new"
					// in modo da evitare ogni incrocio di dati con il
					// provvedimento
					// salvato in memoria che verra' usato momentaneamente per
					// la modifica movimento:
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimento());
					setAm(attoImpegno);
				}

			}

		}
	}

	private void inizializzaDescrizioniEMotivi() {
		//SIAC-7968 Inizio
//		motiviRorReimputazione = CruscottoRorUtilities.filterByPdc(MotiviReimputazioneROR.values(), model.getCodicePdc());
//		motiviRorCancellazione = CruscottoRorUtilities.filterByPdc(MotiviCancellazioneROR.values(), model.getCodicePdc());
//		motiviRorMantenimento = CruscottoRorUtilities.filterByPdc(MotiviMantenimentoROR.values(), model.getCodicePdc());

		motiviRorReimputazione = CruscottoRorUtilities.filterAccByPdc(MotiviReimputazioneAccROR.values(), model.getCodicePdc());
		motiviRorCancellazione = CruscottoRorUtilities.filterAccByPdc(MotiviCancellazioneAccROR.values(), model.getCodicePdc());
		motiviRorMantenimento = CruscottoRorUtilities.filterAccByPdc(MotiviMantenimentoAccROR.values(), model.getCodicePdc());
		//SIAC-7968 Fine
		
		//SIAC-7989 refactoring for null safety - Begin
		List<CodificaFin> listaTipoMotivo = model.getMovimentoSpesaModel() != null && 
				model.getMovimentoSpesaModel().getListaTipoMotivo() != null 
					? model.getMovimentoSpesaModel().getListaTipoMotivo() : new ArrayList<CodificaFin>();
					
		reimputazione = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "REIMP");
		cancellazioneInsussistenza = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "INSROR");
		cancellazioneInesigibilita = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "INEROR");
		mantenere = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, CODICE_MOTIVO_ROR_MANTENERE);

		descrizioneMotivoReimp = reimputazione != null && reimputazione.getDescrizione() != null ?
				reimputazione.getDescrizione() : new String();
		descrizioneMotivoRorCancellazioneInsussistenza = cancellazioneInsussistenza != null && cancellazioneInsussistenza.getDescrizione() != null ?
				cancellazioneInsussistenza.getDescrizione() : new String();
		descrizioneMotivoRorCancellazioneInesigibilita = cancellazioneInesigibilita != null && cancellazioneInesigibilita.getDescrizione() != null ?
				cancellazioneInesigibilita.getDescrizione() : new String();
		descrizioneMotivoRorMantenere = mantenere != null && mantenere.getDescrizione() != null ?
				mantenere.getDescrizione() : new String();
		//SIAC-7989 refactoring for null safety - End
		
	}

	// FIXME Da Modificare con i calcoli che ha gia effettuato Vincenzo
	public String caricaDatiSub() {
		setMethodName("caricaDatiSub");

		if (getAbbinaChk() != null && getAbbinaChk().equalsIgnoreCase("true")) {
			setAbbina("Modifica Anche Accertamento");
		} else {
			setAbbina(null);
		}

		if (StringUtils.isEmpty(getTipoImpegno())) {
			setTipoImpegno("Accertamento");
			setSubAccertamentoSelected(subAccertamentoSelected);
			model.setSubImpegnoSelectedMod(false);
		} else if (getTipoImpegno().equalsIgnoreCase("SubAccertamento")) {
			if (!StringUtils.isEmpty(getSubSelected()) || !model.getStep1Model().getNumeroSubImpegno().equals("")) {
				setSubAccertamentoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				List<SubAccertamento> listaSubAcc = model.getListaSubaccertamenti();
				for (SubAccertamento sub : listaSubAcc) {
					if (String.valueOf(sub.getNumeroBigDecimal()).equals(String.valueOf(model.getStep1Model().getNumeroSubImpegno()))) {

						setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						setNumeroSubAccertamento(String.valueOf(sub.getNumeroBigDecimal()));
						model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumeroBigDecimal()));

						if (sub.getDisponibilitaSubAccertare() == null) {
							sub.setDisponibilitaSubAccertare(BigDecimal.ZERO);
						}

						minImportoSubApp = sub.getDisponibilitaIncassare().negate();

						if (model.getAccertamentoInAggiornamento().getAnnoMovimento() < Integer
								.valueOf(sessionHandler.getAnnoEsercizio())) {
							maxImportoSubApp = new BigDecimal(0);
						} else if (model.getAccertamentoInAggiornamento().isFlagDaRiaccertamento()) {
							maxImportoSubApp = new BigDecimal(0);
						} else {
							maxImportoSubApp = model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare();
						}

						if (minImportoSubApp == null) {
							minImportoSubApp = new BigDecimal(0);
						}

						if (maxImportoSubApp == null) {
							maxImportoSubApp = new BigDecimal(0);
						}

						setMaxSub(convertiBigDecimalToImporto(maxImportoSubApp));
						setMinSub(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMaxSubMod(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMinSubMod(convertiBigDecimalToImporto(minImportoSubApp));

						setMinImportoSubCalcolato(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMinImportoSubCalcolatoMod(convertiBigDecimalToImporto(minImportoSubApp));
						setMaxImportoSubCalcolato(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMaxImportoSubCalcolatoMod(convertiBigDecimalToImporto(maxImportoSubApp));

						// Parte per anche impegno

						// Modifica importi richiesta
						setMinAnche(convertiBigDecimalToImporto(minImportoSubApp));
						model.getMovimentoSpesaModel().setMinAncheImpegno(minImportoSubApp);
						model.setMinAncheMod(convertiBigDecimalToImporto(minImportoSubApp));

						if (model.getStep1Model().getAnnoImpegno().intValue() == model.getAccertamentoInAggiornamento()
								.getCapitoloEntrataGestione().getAnnoCapitolo().intValue()) {
							// anno corrente
							maxImportoImpegnoApp = model.getAccertamentoInAggiornamento()
									.getCapitoloEntrataGestione()
									.getImportiCapitolo().getDisponibilitaAccertareAnno1();
						} else if (model.getStep1Model().getAnnoImpegno()
								.intValue() == (model.getAccertamentoInAggiornamento()
										.getCapitoloEntrataGestione().getAnnoCapitolo().intValue() + 1)) {
							// anno +1
							maxImportoImpegnoApp = model.getAccertamentoInAggiornamento()
									.getCapitoloEntrataGestione()
									.getImportiCapitolo().getDisponibilitaAccertareAnno2();
						} else if (model.getStep1Model().getAnnoImpegno()
								.intValue() == (model.getAccertamentoInAggiornamento()
										.getCapitoloEntrataGestione().getAnnoCapitolo().intValue() + 2)) {
							// anno +2
							maxImportoImpegnoApp = model.getAccertamentoInAggiornamento()
									.getCapitoloEntrataGestione()
									.getImportiCapitolo().getDisponibilitaAccertareAnno3();
						} else {
							// da GESTIRE NELLA PAGINA
							model.setFlagSuperioreTreAnni(true);
						}

						// GESTIONE FLAG SDF:
						
						//

						// Modifica importi richiesta
						setMaxAnche(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						model.getMovimentoSpesaModel().setMaxAncheImpegno(maxImportoImpegnoApp);
						model.getMovimentoSpesaModel().setMinAncheImpegno(minImportoImpegnoApp);
						model.setMaxAncheMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));

					}
				}

			} else {
				setSubAccertamentoSelected(false);
				model.setSubImpegnoSelectedMod(false);
				tipoImpegno = "Impegno";
			}
		}

		return SUCCESS;
	}

	private void caricaListaAmbiti() {
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(sessionHandler.getAnnoBilancio());
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);
		model.setListaTipiAmbito(response.getTipiAmbito());
	}

	private void setAbilitazioni() {
		if (model.getStep1Model().getNumeroImpegno() != null) {

			setAbilitaModificaImporto(true);

			boolean bilPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();

			setFlagValido(false);
			setFlagSoggettoValido(false);
			// stato D o ND
			if (!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("P")) {
				setFlagValido(true);

				// nel caso in cui fossimo in predisposizione consuntivo,
				// rimettiamo flagvalido a false
				// in modo che l'importo ritorni modificabile:

				if (bilPrecInPredisposizioneConsuntivo && isResiduo()) {
					setAbilitaModificaImporto(true);
				} else {
					setAbilitaModificaImporto(false);
				}
				//

				setFlagSoggettoValido(true);
			}

			// JIRA SIAC-3506 in caso di residuo con presenza di modifiche di
			// importo valide, importo non modificabile:
			List<ModificaMovimentoGestioneEntrata> modifiche = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
			if (presenteModificaDiImportoValidaEntrata(modifiche) && isResiduo()) {
				setAbilitaModificaImporto(false);
			}
			//

			// stato
			if (model.getAccertamentoInAggiornamento().getElencoSubAccertamenti() != null
					|| (model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0)) {
				setFlagSoggettoValido(true);
			}

			// jira-1582
			// se e' in stato N e ci sono dei sub allora posso modificare il
			// soggetto
			if (model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
				if (!presenteAlmenoUnMovValido(model.getListaSubaccertamenti(),
						OggettoDaPopolareEnum.SUBACCERTAMENTO.toString())) {
					setFlagSoggettoValido(false);
				}
			}
		}
		if (model.getStep1Model().getProgetto() != null) {
			model.getStep1ModelSubimpegno().setProgetto(model.getStep1Model().getProgetto());
		}
		if (model.getAccertamentoInAggiornamento() != null
				&& model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata() != null
				&& model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("P")) {
			setAbilitaPropagaDaSub(false);
		} else {
			setAbilitaPropagaDaSub(true);
		}
	}

	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return abilitaAzioneInserimentoProvvedimento;
	}

	// carica dati dell'impegno
	private void caricaDatiTotali(Accertamento accertamentoFresco) {

		List<ModificaMovimentoGestioneEntrata> listaModificheEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();

		// Scommento per risolvere Jira ????
		RicercaAccertamentoPerChiaveOttimizzatoROR parametroRicercaPerChiave = new RicercaAccertamentoPerChiaveOttimizzatoROR();
		RicercaAccertamentoK accertamentoDaCercare = new RicercaAccertamentoK();
		BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
		
		//setto i dati di chiave dell'accertamento:
		accertamentoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		accertamentoDaCercare.setNumeroAccertamento(numeroImpegno);
		accertamentoDaCercare.setAnnoAccertamento(model.getStep1Model().getAnnoImpegno());
				
		//RICHIEDENTE
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setCaricalistaModificheCollegate(Boolean.FALSE);
		//ENTE
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaAccertamentoK(accertamentoDaCercare);
		
		//Richiamo il servizio di ricerca:
		//RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(parametroRicercaPerChiave);
		if(getUidMovgest()!=null){
			model.setUidMovgest(getUidMovgest());			
		}
		parametroRicercaPerChiave.setUidMovGest(getUidMovgest() != null ? getUidMovgest() : model.getUidMovgest());
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzatoROR(parametroRicercaPerChiave);
		
		
		//analizzo la risposta del servizio:
		if(!respRk.isFallimento() && respRk.getAccertamento()!= null){
			//SIAC-7503 pdc coerente
			accertamentoFresco = respRk.getAccertamento();
			String codePdc = CruscottoRorUtilities.codePdc(accertamentoFresco.getCodPdc());
			model.setCodicePdc(codePdc);
			model.setImportoDaRiaccertare(accertamentoFresco.getImportoDaRiaccertare());
			model.setImportoMassimoDaRiaccertare(accertamentoFresco.getImportoMaxDaRiaccertare());
			model.setImportoModifiche(accertamentoFresco.getImportoModifiche());
			model.setResiduoEventualeDaMantenere(accertamentoFresco.getResiduoDaMantenere());
			model.setDocumentiNoIncAnnoSuccessivo(accertamentoFresco.getDocumentiNoIncassatiAnnoSuccessivo());
			model.setIncassatoAnnoSuccessivo(accertamentoFresco.getIncassatoAnnoSuccessivo());
			//FIXME Vincenzo: possibile nei submovimenti o refuso?
			model.setNumeroTotaleModifiche(accertamentoFresco.getNumeroTotaleModifcheMovimento());
			
			BigDecimal residuoAZero = new BigDecimal("0.00");
			if(accertamentoFresco.getResiduoDaMantenere().equals(residuoAZero)){
				setDisabledDaMantenere(true);				
			}else{
				setDisabledDaMantenere(false);
			}
			
		}
		
		if(null!=accertamentoFresco){
			// setto il model con il fresco
			model.setAccertamentoRicaricatoDopoInsOAgg(accertamentoFresco);
//			model.getMovimentoSpesaModel().setMinAncheImpegno();
//			model.getMovimentoSpesaModel().setMaxAncheImpegno();
		}
		// FIXME
		// setTipoImpegno(impegnoFresco.getTipoMovimentoDesc());
		// controllo se si tratta del subimpegno quindi prima faccio if
		// getNumero

		if (getNumeroSubAccertamento() != null) {
			if (accertamentoFresco.getStatoOperativoMovimentoGestioneEntrata().equals("P")) {
				setAbilitaPropagaDaSub(false);
			} else {
				setAbilitaPropagaDaSub(true);
			}

		}
		model.setAccertamentoInAggiornamento(accertamentoFresco);
		// .........era commentato sino a qui!

		// SETTO L'IMPEGNO NEL MODEL:
		if (accertamentoFresco != null) {
			model.setAccertamentoRicaricatoDopoInsOAgg(accertamentoFresco);
		}
		if(model.getAccertamentoRicaricatoDopoInsOAgg()!= null){
			model.setAccertamentoInAggiornamento(model.getAccertamentoRicaricatoDopoInsOAgg());
			caricaDatiAccertamento(true);
			//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
			if(model.hasErrori()) {
				return;
			}
			model.setAccertamentoRicaricatoDopoInsOAgg(null);
		}

		// APRILE 2016 - ottimizzazioni sub
		List<SubAccertamento> elencoSubImpegni = null;
		if (respRk != null) {
			elencoSubImpegni = respRk.getElencoSubAccertamentiTuttiConSoloGliIds();
		}
		if ((getNumeroSubAccertamento() != null && !getNumeroSubAccertamento().equals("")) && elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
			SubAccertamento sub = new SubAccertamento();
			for (int i = 0; i < elencoSubImpegni.size(); i++) {
				if (elencoSubImpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(getNumeroSubAccertamento()))) {
					sub = elencoSubImpegni.get(i);
					break;
				}
			}

			// check qui è il subAccertamento
			model.setnSubImpegno(Integer.valueOf(sub.getNumeroBigDecimal().intValue()));

			//13032020: fix devo prendere il provvedimento delle modifiche per il cruscotto quindi inserisco filtro

			String codePdc = CruscottoRorUtilities.codePdc(sub.getCodPdc());
			model.setCodicePdc(codePdc);

			if (sub.getListaModificheMovimentoGestioneEntrata() != null
					&& !sub.getListaModificheMovimentoGestioneEntrata().isEmpty()) {
				List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneEntrata>();
				modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscottoAcc(sub.getListaModificheMovimentoGestioneEntrata(),
							modifichePerCruscotto);
			
				if (modifichePerCruscotto != null) {
					modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValidoAcc(modifichePerCruscotto);
				}
				if(modifichePerCruscotto !=null && !modifichePerCruscotto.isEmpty()){
					model.getStep1ModelCache().setProvvedimento(CruscottoRorUtilities
							.mapAttoToProvv(modifichePerCruscotto.get(0).getAttoAmministrativo()));					
				}else{
					model.getStep1ModelCache()
					.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));
				}
				model.getStep1ModelCache().setProvvedimento(CruscottoRorUtilities
						.mapAttoToProvv(modifichePerCruscotto.get(0).getAttoAmministrativo()));
			} else if (sub.getListaModificheMovimentoGestioneEntrata() == null
					|| sub.getListaModificheMovimentoGestioneEntrata().isEmpty()) {
				model.getStep1ModelCache()
						.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));

			}

		}

		model.getStep1Model().setImportoImpegno(model.getAccertamentoInAggiornamento().getImportoAttuale());
		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(model.getAccertamentoInAggiornamento().getImportoAttuale()));
		model.setTotaleSubImpegno(model.getAccertamentoInAggiornamento().getTotaleSubAccertamentiBigDecimal());
		model.setDisponibilitaSubImpegnare(model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare());

		model.setAccertamentoInAggiornamento(model.getAccertamentoInAggiornamento());
		if (model.getAccertamentoInAggiornamento() != null && model.getAccertamentoInAggiornamento().getElencoSubAccertamenti() != null && model.getAccertamentoInAggiornamento().getElencoSubAccertamenti().size() > 0) {
			model.setListaSubaccertamenti(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti());	//in attesa dei sub-accertamenti
		}else {
			model.setListaSubaccertamenti(new ArrayList<SubAccertamento>());	//in attesa dei sub-accertamenti
		}

		// componiamo una lista modifiche comprendente sia quelle del movimento
		// che quelle dei suoi sub:

		// aggiungo quelle del movimento:
		if (model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata() != null
				&& model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata().size() > 0) {
			listaModificheEntrata.addAll(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata());
		}

		// aggiungo quelle dei sub:
		if (model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0) {
			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				if (sub.getListaModificheMovimentoGestioneEntrata() != null
						&& sub.getListaModificheMovimentoGestioneEntrata().size() > 0) {
					listaModificheEntrata.addAll(sub.getListaModificheMovimentoGestioneEntrata());
				}
			}
		}

		// settiamo le modifiche nel model:
		model.getMovimentoSpesaModel().setListaModificheEntrata(listaModificheEntrata);
		model.getMovimentoSpesaModel().setAccertamento(model.getAccertamentoInAggiornamento());

		// creo il model cache:
		creaMovGestModelCache();

		// ripulisco i model di step1:
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		model.setAccertamentoRicaricatoDopoInsOAgg(new Accertamento());

	}

	// subito dopo, equivale alla prepare della InserisciModifica
	private void prepareAggiornaModifiche() {
		// Reimputazione:
		inizializzaReimputazioneInInserimentoNelModel();

		// Carico Lista Tipo Modifiche
		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}

		if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0){
			
			//ciclo sui sub:
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(sub.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")){
					if(sub.getNumeroBigDecimal() != null){
						//aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumeroBigDecimal()));
					}	
				}
				
			}
			
			if(numeroSubImpegnoList.size() > 0){
				//ci sono sub
				tipoImpegnoModificaImportoList.add("Accertamento");
				tipoImpegnoModificaImportoList.add("SubAccertamento");
				
			} else {
				//non ci sono sub
				tipoImpegnoModificaImportoList.add("Accertamento");	
			}

		} else {
			//non ci sono sub
			tipoImpegnoModificaImportoList.add("Accertamento");
		}
	
		setMaxMinImporti();
		
		
		//SIAC-7551	22/05/2020 GM	
		//"importo massimo differibile" dovrà essere calcolato come "importo massimo da riaccertare" – "importo modifiche"
		//"importo massimo cancellabile" dovrà essere calcolato come "importo massimo da riaccertare" – "importo modifiche"
		BigDecimal massimoDifferibileCancellabile = BigDecimal.ZERO; 
		if(model.getImportoMassimoDaRiaccertare() != null && model.getImportoModifiche() != null && 
			model.getImportoMassimoDaRiaccertare().compareTo(model.getImportoModifiche()) > 0){
			massimoDifferibileCancellabile = (model.getImportoMassimoDaRiaccertare().subtract(model.getImportoModifiche())).abs();
		}
		model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(massimoDifferibileCancellabile));
		model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(massimoDifferibileCancellabile));
//				VECCHIO CODICE COMMENTATO	
//		if(model.getnSubImpegno()==null){
//			model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(minImportoImpegnoApp.abs()));
//			model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(minImportoImpegnoApp.abs()));
//		}else{
//			model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(minImportoSubApp.abs()));
//		model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(minImportoSubApp.abs()));
//		}
		//FINE SIAC-7551
		
		if (StringUtils.isEmpty(getAnniPluriennali())) {
			setAnniPluriennali("1");
		}

	}
	
	//SIAC-7349 Inizio  SR190 FL 15/04/2020
	/**
	 * gestione della selezione di un capitolo
	 * @return
	 */
	public String confermaSpeseCollegate() {
		boolean erroreListaCollegata = false;
		List<Errore> listaErrori = new ArrayList<Errore>();
//		String tipoMovimento = "ACC";// per accertamenti
	 
		//Recupero in dati inseriti nella modale
		ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
		spesa.setImportoOld(convertiImportoToBigDecimal(reimputazioneAnnualita.getImporto()));
		spesa.setTipoModificaMovimentoGestione("REIMP");
		spesa.setAnnoReimputazione(reimputazioneAnnualita.getAnno());
		spesa.setReimputazione(true);
		
		//Effettua i controlli 
		erroreListaCollegata = checkListaCollegataImpegni(spesa,listaErrori);
		//if (!erroreListaCollegata) {addActionError("NON CI SONO ERRORI");}  
		
		if (isInInserimento()) {
			if(listaReimputazioneTriennio != null && !listaReimputazioneTriennio.isEmpty()) {
				for (GestioneCruscottoModel lrt : listaReimputazioneTriennio) {		
					if(lrt.getIndex().equals(reimputazioneAnnualita.getIndex())) {
						lrt.setAnno(reimputazioneAnnualita.getAnno());
						lrt.setImporto(reimputazioneAnnualita.getImporto());
						lrt.setDescrizione(reimputazioneAnnualita.getDescrizione());
						lrt.setValoreSintesi(reimputazioneAnnualita.getValoreSintesi());
					}
				}
			}
		}else{
			if(listaModificheRor != null && !listaModificheRor.isEmpty()) {
				for (ModificaMovimentoGestioneEntrata lmROR : listaModificheRor) {
					
					if(lmROR.getIndex() != null && reimputazioneAnnualita.getIndex() != null) {
						if(lmROR.getIndex().equals(reimputazioneAnnualita.getIndex())) {
							lmROR.setAnnoReimputazione(reimputazioneAnnualita.getAnno());
							lmROR.setImportoOld(convertiImportoToBigDecimal(reimputazioneAnnualita.getImporto()));
							lmROR.setDescrizioneModificaMovimentoGestione(reimputazioneAnnualita.getDescrizione());
							
						}
						
					}

				}
			}
		}
		
		if(!listaErrori.isEmpty() || erroreListaCollegata){
			//presenza errori
			addErrori(listaErrori);
			return SUCCESS; 
		}
		
		return SUCCESS; 
	}
	
	protected boolean checkListaCollegataImpegni(ModificaMovimentoGestioneEntrata spesa, List<Errore> listaErrori) {
				
		if(spesa.isReimputazione() &&  spesa.getAnnoReimputazione() != null && spesa.getAnnoReimputazione() > 0
				){
			//SIAC-8047
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
			//SIAC-8626 e SIAC-8625 (vedasi ultimo commento)
			// dopo approfondimenti interni, si conferma che per tutti gli enti deve valere la regola 'non si differisce entrata senza associare un differimento di spesa'
			// Pertanto la reimputazione di accertamento deve sempre essere associata a reimputazioni di impegno, a totale copertura del suo importo.
			if(listaModificheReimputazione == null  || listaModificheReimputazione.isEmpty() ){
				listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile inserire reimputazioni senza associare un differimento di spesa."));
				return true;
			}
				//inizializziamo a 0 la variabile che dovra' contenere la somma degli importi collegabili inseriti e totali della tabella
				
				BigDecimal sommaImportiCollegabiliPrioritariePerAnnoSelezionato = new BigDecimal(0);
				BigDecimal sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato = new BigDecimal(0);
				
				BigDecimal sommaImportiMaxCollegabilePerAnnoSelezionato = new BigDecimal(0);
				BigDecimal sommaImportoCollegamentoPerAnnoSelezionato = new BigDecimal(0);
				
				
				BigDecimal sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato = new BigDecimal(0);
				
				
				int countNumeroPrioritariePerAnnoSelezionato=0;
				int countNumeroPrioritarieAll=0;
				
				int numeroReimputazionePerAnnoSelezionato=0;
				
				int numeroReimputazionePerALLAnnualita=0;
				int numeroReimputazionePerDiffAnnualita=0;
				
				int countErrorCollegabile=0;
				int countErrorDifferimneto=0;
				
				
				BigDecimal sommaImportiMaxCollegabilePerAnniNonSelezionato = new BigDecimal(0);
				BigDecimal sommaImportoCollegamentoPerAnniNonSelezionato = new BigDecimal(0);
				
				//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 Inizio CM 08/07/2020
				boolean isImportoCollegamentoNegativo = false;
				//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 Fine CM 08/07/2020
				
				//Primo controllo: L'importo inserito nel campo aperto "Importo Collegato" sia minore dell’importo "Massimo Collegabile"
				for(int i=0; i<listaModificheReimputazione.size(); i++) {
					
					
					BigDecimal importoMaxCollegabile = listaModificheReimputazione.get(i).getImportoMaxCollegabile();
					
					//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 Inizio CM 08/07/2020
					if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(BigDecimal.ZERO) < 0) {
						isImportoCollegamentoNegativo = true;
					}
					//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 Fine CM 08/07/2020
					
//						if (listaModificheSpeseCollegata.get(i).getImportoCollegamento().compareTo(BigDecimal.ZERO) != 0 && !isInInserimento()) {
//							importoMaxCollegabile = importoMaxCollegabile.add(listaModificheSpeseCollegata.get(i).getImportoCollegamento());
//						}

					if (spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())) {
						
						if (!spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())){
							numeroReimputazionePerDiffAnnualita++;
						}
						
						numeroReimputazionePerALLAnnualita++;
						

						if(!spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())) {	
							sommaImportoCollegamentoPerAnniNonSelezionato = sommaImportoCollegamentoPerAnniNonSelezionato.add(listaModificheSpeseCollegata.get(i).getImportoCollegamento());
							sommaImportiMaxCollegabilePerAnniNonSelezionato = sommaImportiMaxCollegabilePerAnniNonSelezionato.add(importoMaxCollegabile);
						}
					
						if (listaModificheReimputazione.get(i).isVincoloEsplicito()   ) {
							countNumeroPrioritarieAll++;	
						}
					
						if(spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())) {		
							
							if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(importoMaxCollegabile) > 0 ) {
							   	countErrorCollegabile++;
							}
							if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(spesa.getImportoOld().abs()) > 0 ) {
								countErrorDifferimneto++;
							}
							
							numeroReimputazionePerAnnoSelezionato++;
							sommaImportiMaxCollegabilePerAnnoSelezionato =sommaImportiMaxCollegabilePerAnnoSelezionato.add(importoMaxCollegabile);
							sommaImportoCollegamentoPerAnnoSelezionato =sommaImportoCollegamentoPerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoCollegamento());
							
							if (listaModificheReimputazione.get(i).isVincoloEsplicito()) {
								countNumeroPrioritariePerAnnoSelezionato++;
								sommaImportiCollegabiliPrioritariePerAnnoSelezionato = sommaImportiCollegabiliPrioritariePerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoCollegamento());
								sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato = sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato.add(importoMaxCollegabile);
							}
							
						}else {
							//Ripulisco gli altri valori 
						//	listaModificheSpeseCollegata.get(i).setImportoCollegamento(BigDecimal.ZERO);
						}	
						
						
						
					} else {
						//Ripulisco gli altri valori 
						//listaModificheSpeseCollegata.get(i).setImportoCollegamento(BigDecimal.ZERO);
					}		
					
				}
				
				if(numeroReimputazionePerALLAnnualita == numeroReimputazionePerDiffAnnualita  
						&& sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0	
						) {
					listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa: Esaurire prima tutte le righe di differimento di spesa"));
				}else {
				
					sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato = sommaImportoCollegamentoPerAnnoSelezionato.subtract(sommaImportiCollegabiliPrioritariePerAnnoSelezionato);
					
					//controllo di esaurimento importo differimento spalmati anche su tutti gli anni 07/05/2020
					if ((spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato) > 0
							&& spesa.getImportoOld().abs().compareTo(sommaImportoCollegamentoPerAnnoSelezionato)>0
							//&& numeroReimputazionePerALLAnnualita > numeroReimputazionePerAnnoSelezionato 
							&& sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0)
						|| (spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)>=0 
							&& sommaImportoCollegamentoPerAnnoSelezionato.compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)<0))  {
						
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa."));
					}
					
					//CM 01/07/2020 - commentato perchè unito al controllo sopra per non far duplicare il messaggio di errore a seguito delle correzioni ai messaggi di errore date da Pietro Gambino
//					if (spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)>=0 
//							&& sommaImportoCollegamentoPerAnnoSelezionato.compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)<0 ) {
//						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Impossibile procedere: Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa.")); //qui non c'è più il riferimento all'anno di reimputazione sul quale si verifica l'errore
//					}
					
					//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 Inizio CM 08/07/2020
					if(isImportoCollegamentoNegativo) {
					   	listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("'Importo collegamento' deve essere positivo"));
					}
					//CONTABILIA-260 - Bug fix per test 23.20 - SIAC-7349 fine CM 08/07/2020
					
					if(countErrorCollegabile >0) {
					   	listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("'Importo collegamento' deve essere minore o uguale dell'importo 'Importo max. collegabile' per l'anno "+ spesa.getAnnoReimputazione()));
					}
					
					if(countErrorDifferimneto > 0 && numeroReimputazionePerAnnoSelezionato == 1) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("La somma dei collegamenti impostati supera l'importo della modifica di entrata per l'anno "+ spesa.getAnnoReimputazione()));
					}
					
					if (sommaImportoCollegamentoPerAnnoSelezionato.compareTo(BigDecimal.ZERO) == 0 && sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0	 ) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento senza fornire copertura ad alcuna reimputazione di spesa: Valorizzare 'Importo collegamento' per un anno di Reimputazioni di Spesa "));
					}
					
					//SIAC-8016 inizio
					if (sommaImportoCollegamentoPerAnnoSelezionato.compareTo(BigDecimal.ZERO) == 0 && sommaImportiMaxCollegabilePerAnnoSelezionato.subtract(sommaImportoCollegamentoPerAnnoSelezionato).compareTo(BigDecimal.ZERO)	 !=0	 ) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento senza fornire copertura ad alcuna reimputazione di spesa: Valorizzare 'Importo collegamento' per un anno di Reimputazioni di Spesa "));
					}
					//SIAC-8016 fine 
					
					
					
					if ( sommaImportoCollegamentoPerAnnoSelezionato.compareTo(spesa.getImportoOld().abs()) > 0) {
						if (numeroReimputazionePerAnnoSelezionato > 1 )
							listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("La somma dei collegamenti impostati supera l'importo della modifica di entrata per l'anno " + spesa.getAnnoReimputazione()));
					}
	
					//Controllo sulle righe prioritarie
					int countmsgErrorPri=0;
					int countmsgErrorNonPri=0;
					int countmsgErrorNumeroPrioritarieAll=0;
					int countmsgErrorPrioritariePerAnnoSelezionato=0;
					List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimp = getListaModificheSpeseCollegata();
					for(int z=0; z<listaModificheReimp.size(); z++) {
						
						if (spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimp.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())) {
						
							if (listaModificheReimp.get(z).getImportoCollegamento() != null && listaModificheReimp.get(z).getImportoCollegamento().compareTo(BigDecimal.ZERO) > 0  
									&& spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimp.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
									){
								//tratto righe non vincolate
								if ( !listaModificheReimp.get(z).isVincoloEsplicito()  ) {
									if ( countNumeroPrioritarieAll ==0 ) {
										if( spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato) <= 0 &&
												spesa.getImportoOld().abs().compareTo(sommaImportoCollegamentoPerAnnoSelezionato) >0 ) {
										
										countmsgErrorNonPri++;
										}
									}
									
									if ( countNumeroPrioritarieAll > 0 ) {
										if( spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) <= 0 &&
											spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritariePerAnnoSelezionato) >0 ) {
										countmsgErrorPri++;
										}
										countmsgErrorNumeroPrioritarieAll++;
									}
								}
								
								//tratto righe prioritarie
								if ( listaModificheReimp.get(z).isVincoloEsplicito() && countNumeroPrioritarieAll >1  && spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimp.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())) {
									if (   countNumeroPrioritariePerAnnoSelezionato < numeroReimputazionePerAnnoSelezionato && 
											sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato.compareTo(BigDecimal.ZERO) != 0  ) {
										countmsgErrorPrioritariePerAnnoSelezionato++;
									}
									if ( countNumeroPrioritariePerAnnoSelezionato == numeroReimputazionePerAnnoSelezionato) {
										if( spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) <= 0 &&
												spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritariePerAnnoSelezionato) >0 ) {
											countmsgErrorPri++;
										}
									}
									
//									if(countNumeroPrioritariePerAnnoSelezionato > 1 && checkPresenzaPiuRighePrioritarieConStessoAnno(listaModificheSpeseCollegata, listaModificheSpeseCollegata.get(z).getAnnoReimputazione())) {
//										
//									}
									
								}
							}
						}
					}

					//Messsaggi in presenza di righe prioritarie
					if (countmsgErrorNumeroPrioritarieAll > 0 ||  countmsgErrorPrioritariePerAnnoSelezionato >0){
						if(sommaImportiCollegabiliPrioritariePerAnnoSelezionato.compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) != 0) {
							if (countNumeroPrioritarieAll==1  || countNumeroPrioritariePerAnnoSelezionato==1){
								listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("È stato valorizzato l'importo collegamento di una modifica di impegno non esplicitamente in vincolo senza prima fornire coperture alle modifiche di impegno in vincolo esplicito con il corrente accertamento. Esaurire prima l'importo massimo collegabile della riga 'prioritaria' (Evidenziata)"));
							}else {
								listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("È stato valorizzato l'importo collegamento di una modifica di impegno non esplicitamente in vincolo senza prima fornire coperture alle modifiche di impegno in vincolo esplicito con il corrente accertamento. Esaurire prima l'importo massimo collegabile delle righe 'prioritarie' (Evidenziate)"));	
							}
						}
					}
					if(countmsgErrorPri>0) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' necessario esaurire l'importo Massimo Collegabile di tutte le righe 'prioritarie' (Evidenziate)"));
					}
					if(countmsgErrorNonPri>0) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' necessario esaurire l'importo Massimo Collegabile di tutte le righe"));
					}
				
					//fine controlli
				}
//			}
		}
		if (listaErrori != null && ! listaErrori.isEmpty()) {
			return true;
		}else {
			return false;	
		}
	}	 
	//SIAC-7349 Fine  SR190 FL 15/04/2020
	
//	
//	public boolean checkPresenzaPiuRighePrioritarieConStessoAnno(List<ModificaMovimentoGestioneSpesaCollegata> listaSpesaColl, int anno){
//		
//		boolean res = false;
//		int count=0;
//		for(int i=0; i<listaSpesaColl.size(); i++) {
//			if(listaSpesaColl.get(i).getAnnoReimputazione() == anno) {
//				res = true;
//				count++;
//				if(count>1) {
//					return res;
//				}
//			}
//		}
//		return res;
//	}
	
	// Inserimento nuove modifiche -> Cruscotto "INSERISCI"
	@SuppressWarnings("unused")
	public String salva() {
		//SIAC-8269
		clearWarningAndError(true);
		
//		String tipoMotivo = getIdTipoMotivo();
		// clono i dati che stanno per essere cambiati nel model per
		// ripristinarli in caso di salvataggio andato male:
		GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva = clone(model.getMovimentoSpesaModel());
		GestisciImpegnoStep1Model step1ModelPrimaDiSalva = clone(model.getStep1Model());
		Accertamento accertamentoInAggiornamentoPrimaDiSalva = clone(model.getAccertamentoInAggiornamento());
		List<SubAccertamento> listaSubPrimaDiSalva = clone(model.getListaSubaccertamenti());
		SubAccertamento subAccertamentoInAggiornamentoPrimaDiSalva = clone(model.getSubAccertamentoInAggiornamento());
		setTipoImpegno(model.getAccertamentoInAggiornamento().getTipoMovimentoDesc());

		List<ModificaMovimentoGestioneEntrata> modificheList = model.getAccertamentoInAggiornamento()
				.getListaModificheMovimentoGestioneEntrata();// dopo va risettato
															// il model
		//per non permettere l'inserimento del da mantenere se nella costruzione è stato portato a zero
		BigDecimal importoInizialeImpegno = BigDecimal.ZERO;
		if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("")){
			setTipoImpegno("SubImpegno");
			importoInizialeImpegno = model.getSubAccertamentoInAggiornamento().getImportoAttuale();
		}else{
			setTipoImpegno("Impegno");
			importoInizialeImpegno = model.getAccertamentoInAggiornamento().getImportoAttuale();
		}
				
				
		// errori da mostrare6
		List<Errore> erroriSalvataggio = new ArrayList<Errore>();
		modificheList = FinUtility.rimuoviConIdZero(modificheList);
		if (modificheList == null) {
			modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
		}

		// controllo se il provvedimento è stato selezionato
		if (model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == null
				&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento() == null) {
			Errore err = new Errore();
			err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
			err.setDescrizione("Non è stato selezionato nessun provvedimento");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;

		}
		//SIAC-7469 il provvedimento deve essere definitivo quello dell'accertamento
		if (model.getStep1ModelCache().getProvvedimento().getStato() != null
				&& !model.getStep1ModelCache().getProvvedimento().getStato().equals(StatoOperativoAtti.DEFINITIVO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			

		}
		//SIAC-7469 il provvedimento per le modifiche può essere anche provvisorio
		if (model.getStep1Model().getProvvedimento().getStato() != null
				&& model.getStep1Model().getProvvedimento().getStato().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo o provvisorio");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			

		}

		// controllo se nella lista ci sono più reimputazioni per lo stesso anno
		erroriSalvataggio = CruscottoRorUtilities.checkAnniReimputazioneRipetuti(listaReimputazioneTriennio,
				erroriSalvataggio);
		erroriSalvataggio = CruscottoRorUtilities.checkAnnoNonCongruente(listaReimputazioneTriennio, erroriSalvataggio,
				sessionHandler.getBilancio().getAnno());
		
		erroriSalvataggio = CruscottoRorUtilities.checkCampiProvvedimentoVuoti(model.getStep1Model(), erroriSalvataggio);
		// controllo nuovo

		if (model.getStep1Model().getNumeroSubImpegno() == null
				|| model.getStep1Model().getNumeroSubImpegno().equals("")) {
			boolean stesso = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(
					model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "Accertamento",
					erroriSalvataggio);
		} else {
			boolean stesso = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(
					model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(),
					"SubAccertamento", erroriSalvataggio);
		}
		
		
		//Controllo se sono stati valorizzati importi e descrizione
		erroriSalvataggio = CruscottoRorUtilities.checkModificaCompleta(erroriSalvataggio, listaReimputazioneTriennio,
				rorCancellazioneInesigibilita, rorCancellazioneInsussistenza, rorDaMantenere, checkDaMantenere);
		
		if(erroriSalvataggio != null && !erroriSalvataggio.isEmpty()){
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;		
		}
        
		List<ModificaMovimentoGestioneEntrata> spese = costruisciListaMovimentiSpesaSUB(listaReimputazioneTriennio,
				rorCancellazioneInesigibilita, rorCancellazioneInsussistenza, rorDaMantenere, checkDaMantenere);
		
		//SIAC-7349 Inizio  SR190 FL 15/04/2020 (INSERIMENTO)rieffettuo i controlli che già sono stati effettua al conferma della modale
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreListaCollegata = false;
		for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : spese) {
			if(modificaMovimentoGestioneEntrata.isReimputazione()) {
				erroreListaCollegata = checkListaCollegataImpegni( modificaMovimentoGestioneEntrata,listaErrori);
			}
		}
		if(!listaErrori.isEmpty() || erroreListaCollegata){
			
			//CONTABILIA-260 - Bug fix per email Inizio FL CM 09/07/2020
			//SIAC-8047
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
			if (listaModificheReimputazione!=null && !listaModificheReimputazione.isEmpty()) {
				for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : listaModificheReimputazione) {
						//calcolo il residuo   
						BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().add(modificheSpeseCollegata.getImportoCollegamento());
						modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
					}
			 } 
			//CONTABILIA-260 - Bug fix per email Fine FL CM 09/07/2020
			
			//presenza errori
			addErrori(listaErrori);
			return INPUT;
		}
		//SIAC-7349 Fine  SR190 FL 15/04/2020
		
		if(checkDaMantenere !=null && checkDaMantenere.equals(true) && !rorDaMantenere.getDescrizione().equals("")){
			BigDecimal residuoZero = new BigDecimal("0.00");
			BigDecimal deltaImportoImpegno = residuoDaMantenereDopoAggiornamento(importoInizialeImpegno);
			BigDecimal residuoDaMantenere = model.getResiduoEventualeDaMantenere();
			residuoDaMantenere = residuoDaMantenere.add(deltaImportoImpegno);
			if(residuoDaMantenere.compareTo(residuoZero)==0){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getErrore("").getCodice());
				err.setCodice(ErroreFin.WARNING_GENERICO.getErrore("Impossibile Inserire la modifica ROR - Da Mantenere: Residuo uguale a zero").getDescrizione());
				erroriSalvataggio.add(err);
				
			}
			if(!erroriSalvataggio.isEmpty()){
				ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
				
			}
		}

		List<ModificaMovimentoGestioneEntrata> speseDaValidare = new ArrayList<ModificaMovimentoGestioneEntrata>();
		if(isPropagaSelected()){
			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					speseDaValidare = sub.getListaModificheMovimentoGestioneEntrata();
					break;
				}
				
			}
			speseDaValidare =  CruscottoRorUtilities.speseDaValidarePerSubPropagaAcc(speseDaValidare);			
			if(speseDaValidare != null && speseDaValidare.isEmpty()){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
				erroriSalvataggio.add(err);
				ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
				
				
			}

			erroriSalvataggio = checkModificheValide(speseDaValidare, erroriSalvataggio, false, null, null,	null, null);			
			
		}else{
			if(model.getnSubImpegno()!=null){
				for (SubAccertamento sub : model.getListaSubaccertamenti()) {
					// TODO subSelected corrrisponde al subimopegno caricato in
					// mappa
					if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
						speseDaValidare = sub.getListaModificheMovimentoGestioneEntrata();
						break;
					}
					
				}
				speseDaValidare =  CruscottoRorUtilities.speseDaValidarePerSubPropagaAcc(speseDaValidare);			
				if(speseDaValidare != null && speseDaValidare.isEmpty()){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
					erroriSalvataggio.add(err);
					ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
					sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
					addErrori(erroriSalvataggio);
					setErroriInSessionePerActionSuccessiva();
					return INPUT;
					
					
				}
				erroriSalvataggio = checkModificheValide(speseDaValidare, erroriSalvataggio, false, null, null,	null, null);
				
			}else{
				
				if(spese != null && spese.isEmpty()){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
					erroriSalvataggio.add(err);
					ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
					sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
					addErrori(erroriSalvataggio);
					setErroriInSessionePerActionSuccessiva();
					return INPUT;				
				}
				
				erroriSalvataggio = checkModificheValide(spese, erroriSalvataggio, false, null, null,	null, null);
			}
			
		}
		

		

		

		// Non veniva popolato il componente con i dati della trans. elemen.
		popolaStrutturaTransazioneElementare();

		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
		ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva,listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		modificheList.addAll(spese);

		model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(modificheList);

		//SIAC-8553
		BigDecimal sommaImportiModifiche = calcolaSommaModifiche();
		List<Errore> listaWarning = new ArrayList<Errore>();

		//issue-12: non dare il warning se il capitolo ha disponibilità negativa  e sommaImportiModifiche (la modifica che sto inserendo) è negativa
		boolean	disponibCapitoloNegativa = model.getAccertamentoInAggiornamento().getAnnoMovimento() == sessionHandler.getAnnoBilancio() &&
										   model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitolo().getDisponibilitaAccertareAnno1().signum() == -1 && 
										   sommaImportiModifiche.signum() <= 0?true:false;
				
		if (!disponibCapitoloNegativa) {	
			if(controlloImportoModificaSuDisponibiltaAccertareCapitolo(sommaImportiModifiche, listaWarning)) {
				model.setProseguiConWarningModificaPositivaAccertamento(true);
				addWarnings(listaWarning, true);
				return INPUT;
			}
		}
			
		AggiornaAccertamento requestAggiorna = convertiModelPerChiamataServizioAggiornaAccertamenti(false);
		
		if (requestAggiorna.getAccertamento().getProgetto() != null) {
			Progetto progetto = requestAggiorna.getAccertamento().getProgetto();

			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());

//			requestAggiorna.getAccertamento().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
//			requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
		}
		
		//SIAC-8675
		AnnullaAggiornaMovimento req = creaAnnullaAggiornaMovimentoRequestBase();
		req.setFlagAnnullaConsentito(false);
		req.setFlagAggiornaConsentito(true);
		req.setAggiornaAccertamento(requestAggiorna);
		
		
		
		AnnullaAggiornaMovimentoResponse responseAnnullaAggiorna = movimentoGestioneFinService.annullaAggiornaImpegno(req);

		if (responseAnnullaAggiorna.isFallimento()) {
			List<Errore> errs = CruscottoRorUtilities.getErroreDaWrapper(responseAnnullaAggiorna.getDescrizioneErrori(), responseAnnullaAggiorna.getErrori());
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, accertamentoInAggiornamentoPrimaDiSalva,listaSubPrimaDiSalva, subAccertamentoInAggiornamentoPrimaDiSalva);
			addErrori("Errore al salvataggio delle modifiche", errs);
			return INPUT;
		}
		
		model.setAccertamentoInAggiornamento(responseAnnullaAggiorna.getAccertamento());

		impostaInformazioneSuccessoAzioneInSessionePerRedirezione(
				ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
						+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());

		return SUCCESS_SALVA;

	}
	
	private BigDecimal calcolaSommaModifiche() {
		BigDecimal sommaImportiModifiche = BigDecimal.ZERO;
		for (ModificaMovimentoGestioneEntrata modifica : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
			if(StatoOperativoModificaMovimentoGestione.VALIDO.equals(modifica.getStatoOperativoModificaMovimentoGestione())) {
				sommaImportiModifiche = sommaImportiModifiche.add(modifica.getImportoOld() != null ? modifica.getImportoOld() : BigDecimal.ZERO);
			}
		}
		return sommaImportiModifiche;
	}

	private void impostaInformazioneSuccessoAzioneInSessionePerRedirezione(String optional) {
		sessionHandler.setParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, optional);
	}
	
	private void impostaMessaggioAzioneInSessionePerRedirezione(String optional) {
		sessionHandler.setParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, optional);
	}

	/**
	 * Controlla se in sessione si abbiano informazioni relative ad un eventuale
	 * successo di un'azione precedente. <br>
	 * In tal caso, pulisce la sessione e comunica all'utente tale successo.
	 */
	protected void leggiEventualiInformazioniAzionePrecedente() {
		// Recupera le informazioni da sessione
		String successo = sessionHandler.getParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE);
		if (successo != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, null);
			addActionMessage(successo);

		}

	}
	
	protected void leggiEventualiMessaggiAzionePrecedente() {
		// Recupera le informazioni da sessione
		String successo = sessionHandler.getParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE);
		if (successo != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, null);
			addActionWarning(successo);

		}

	}



	
	private List<Errore> checkModificheValide(List<ModificaMovimentoGestioneEntrata> spese,
			List<Errore> erroriSalvataggio, boolean aggiornamento, BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, 
			BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, 
			BigDecimal importoAnnullatoReimputazionePerRicreare, 
			BigDecimal importoAnnullatoCancellazionePerRicreare) {
		
		//Per effettuare il controllo degli importi, devo passare come lista le spese che non contengono RORM e una che contiene solo i rorm Dunque devo creare due arraylist di appoggio
//				List<ModificaMovimentoGestioneEntrata> speseSenzaRorm = CruscottoRorUtilities.getSpeseRorAcc(spese, "");
				List<ModificaMovimentoGestioneEntrata> speseRorm =  CruscottoRorUtilities.getSpeseRorAcc(spese, "RORM");

		for (int i = 0; i < spese.size(); i++) {
			ModificaMovimentoGestioneEntrata spesaSelezionata = spese.get(i);
			if(spesaSelezionata.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)){
					
				if (tipoImpegno.equals("Accertamento")) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno, null,
							spesaSelezionata);
						
				} else if (tipoImpegno.equals("SubAccertamento") && !isPropagaSelected()) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno,
							"No Propaga", spesaSelezionata);
				} else if (tipoImpegno.equals("SubAccertamento") && isPropagaSelected()) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno,
							"Modifica Anche Accertamento", spesaSelezionata);

				}
			}
		}
		erroriSalvataggio = controlloGestisciImpegnoDecentratoPerModifica(erroriSalvataggio);
			
		if(!erroriSalvataggio.isEmpty()){
			return erroriSalvataggio;
		}
			
			
			
			//qui controllo importi
		if (tipoImpegno.equals("Accertamento")) {
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());
			
			BigDecimal limiteDestro = convertiImportoToBigDecimal(model.getMaxImportoCalcolatoMod());
			
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellAcc(erroriSalvataggio, spese, maxDiff, maxCanc, limiteDestro,
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare);
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellAcc(erroriSalvataggio, spese, maxDiff, maxCanc,limiteDestro,  null, null, null, null);				
			}
				
		} else if (tipoImpegno.equals("SubAccertamento") && !isPropagaSelected()) {
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());
			
			BigDecimal limiteDestro = convertiImportoToBigDecimal(model.getMaxImportoSubCalcolatoMod());
			
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellAcc(erroriSalvataggio, spese, maxDiff, maxCanc, limiteDestro,
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare);
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellAcc(erroriSalvataggio, spese, maxDiff, maxCanc,limiteDestro, null, null, null, null);				
			}
		} else if (tipoImpegno.equals("SubAccertamento") && isPropagaSelected()) {
			
			//qui gli importi vanno gestiti solo per il subimpegno, in quanto per l'impegno padre non sono previste modifiche
			BigDecimal limiteDestroPadre = convertiImportoToBigDecimal(model.getMaxImportoCalcolatoMod());
			BigDecimal limiteDestroSub = convertiImportoToBigDecimal(model.getMaxImportoSubCalcolatoMod());
			
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());//questo è riferito al subimpegno
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());//questo è riferito al subimpegno
			BigDecimal maxDiffPadre = convertiImportoToBigDecimal(model.getMinImpMod());//questo è riferito al subimpegno
			BigDecimal maxCancPadre = convertiImportoToBigDecimal(model.getMinImpMod());//questo è riferito al subimpegno
				//qui eseguo i controlli anche sull'impegno padre
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellSubTestAcc(erroriSalvataggio, spese, maxDiff, maxCanc, maxDiffPadre, maxCancPadre, 
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare,limiteDestroPadre, limiteDestroSub);
				
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellSubTestAcc(erroriSalvataggio, spese, maxDiff, maxCanc, maxDiffPadre, maxCancPadre, 
						null, null, null, null, limiteDestroPadre, limiteDestroSub);				
			}
			
			erroriSalvataggio = controllaRorMantenerePadreAcc(erroriSalvataggio, speseRorm);
	
		}
			
			
			
		return erroriSalvataggio;

	}

	private List<Errore> controllaRorMantenerePadreAcc(List<Errore> erroriSalvataggio,
		List<ModificaMovimentoGestioneEntrata> speseRorm) {
		if(speseRorm.isEmpty()){
			return erroriSalvataggio;
		}
		List<SubAccertamento> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI);
		Integer numeroAccertamentoPadre = model.getNumeroImpegno();
		
		Accertamento accertamento = CruscottoRorUtilities.getAccertamentoByNumero(list, numeroAccertamentoPadre);
		if(accertamento.getResiduoDaMantenere().equals(BigDecimal.ZERO)){
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"Non &egrave; possibile propagare la modifica ROR - Da Mantenere all'impegno padre. Residuo eventuale da mantenere uguale a zero");
			erroriSalvataggio.add(err);
			
		}

		return erroriSalvataggio;
}

	// metodo di aggiungi/rimuovi anno reimputazione
	public String aggiungiAnnoReimputazione() {
//		Integer anno = sessionHandler.getAnnoBilancio();

		if (isInInserimento()) {
			
			//SIAC-7349 Inizio  SR190 FL 15/04/2020
			setListaModificheSpeseCollegata(
					//SIAC-8041 filtro la lista modifiche per le reimputazioni
					initListaModificheSpesaCollegataSoloReimp(
							model.getAccertamentoInAggiornamento()
								.getListaModificheMovimentoGestioneSpesaCollegata()
					)
			);
			//SIAC-7349 Fine SR190 FL 15/04/2020
			int numeroAnniSuccessivi = listaReimputazioneTriennio.size();
			GestioneCruscottoModel modifica = new GestioneCruscottoModel();
			modifica.setIndex(numeroAnniSuccessivi);
			listaReimputazioneTriennio.add(modifica);
		} else {
			ModificaMovimentoGestioneEntrata e = new ModificaMovimentoGestioneEntrata();
			e.setReimputazione(true);
			e.setTipoModificaMovimentoGestione("REIMP");
			e.setReimputazione(true);
			getListaModificheRor().add(e);
		}

		return SUCCESS;
	}

	public String rimuoviAnnoReimputazione() {
//		Integer anno = sessionHandler.getAnnoBilancio();
		Integer indice = getIndiceRimozioneAnno();

		for (int j = 0; j < listaReimputazioneTriennio.size(); j++) {
			if (listaReimputazioneTriennio.get(j).getIndex().equals(indice)) {
				listaReimputazioneTriennio.remove(listaReimputazioneTriennio.get(j));
			}
		}
		return SUCCESS;
	}

	// costruzione lista di modifica spesa
	private List<ModificaMovimentoGestioneEntrata> costruisciListaMovimentiSpesaSUB(
			List<GestioneCruscottoModel> listaReimputazioneTriennio,
			GestioneCruscottoModel rorCancellazioneInesigibilita, GestioneCruscottoModel rorCancellazioneInsussistenza,
			GestioneCruscottoModel rorDaMantenere, Boolean isDaMantenere) {

		BigDecimal importoZero = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneEntrata> listaToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
		String tipoMovimento = "";

		if (model.getStep1Model().getNumeroSubImpegno() != null) {
			setSubSelected(model.getStep1Model().getNumeroSubImpegno().toString());
			setTipoImpegno("SubAccertamento");
		}else{
			setTipoImpegno("Accertamento");
		}

		if (tipoImpegno.equalsIgnoreCase("Accertamento")) {

			tipoMovimento = "ACC";// per impegni
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneEntrata spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {

					//SIAC-7349 Inizio  SR190 FL 15/04/2020 (Per l'inserimento)
					//SIAC-8047
					List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
					if (listaModificheReimputazione!=null && !listaModificheReimputazione.isEmpty()) {
						List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegataAss = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
						for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : listaModificheReimputazione) {
							if (modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().equals(spesa.getAnnoReimputazione())) {
								//calcolo il residuo da salvare
								BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().subtract(modificheSpeseCollegata.getImportoCollegamento());
								modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
								listaModificheSpeseCollegataAss.add(modificheSpeseCollegata);
							}
						} 
						spesa.setListaModificheMovimentoGestioneSpesaCollegata(listaModificheSpeseCollegataAss);
					}
					//SIAC-7349 Fine  SR190 FL 15/04/2020
					listaToReturn.add(spesa);
				}
			}

			ModificaMovimentoGestioneEntrata spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);

			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaIneRor);
			}
			ModificaMovimentoGestioneEntrata spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaInsRor);
			}
			ModificaMovimentoGestioneEntrata spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				listaToReturn.add(spesaManRor);
			}

		} else if (tipoImpegno.equals("SubAccertamento") && !isPropagaSelected()) {

			tipoMovimento = "SAC";
			List<ModificaMovimentoGestioneEntrata> listaToReturnSub = new ArrayList<ModificaMovimentoGestioneEntrata>();
			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					listaToReturnSub = sub.getListaModificheMovimentoGestioneEntrata();
				}
			}
			listaToReturnSub = FinUtility.rimuoviConIdZero(listaToReturnSub);
			//

			if (listaToReturnSub == null) {
				listaToReturnSub = new ArrayList<ModificaMovimentoGestioneEntrata>();
			} else {
				//listaToReturnSub = CruscottoRorUtilities.getModificheStatoValidoAcc(listaToReturn);
			}
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneEntrata spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					listaToReturnSub.add(spesa);
				}
			}

			ModificaMovimentoGestioneEntrata spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturnSub.add(spesaIneRor);
			}
			ModificaMovimentoGestioneEntrata spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturnSub.add(spesaInsRor);
			}
			ModificaMovimentoGestioneEntrata spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				listaToReturnSub.add(spesaManRor);
			}

			List<SubAccertamento> nuovaSubImpegnoList = new ArrayList<SubAccertamento>();

			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					sub.setListaModificheMovimentoGestioneEntrata(listaToReturnSub);
				}
				nuovaSubImpegnoList.add(sub);
			}
			model.setListaSubaccertamenti(nuovaSubImpegnoList);

		} else if (tipoImpegno.equals("SubAccertamento") && isPropagaSelected()) { // abbina.equalsIgnoreCase("Modifica
																				// Anche
																				// Impegno")
			// CARICO I DATI ANCHE PER L'IMPEGNO
			listaToReturn = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();

			if (listaToReturn == null) {
				listaToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
			} else {
				listaToReturn = CruscottoRorUtilities.getModificheStatoValidoAcc(listaToReturn);
			}
			listaToReturn = FinUtility.rimuoviConIdZero(listaToReturn);
			if (listaToReturn == null) {
				listaToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
			}

			tipoMovimento = "ACC";
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneEntrata spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {

					//SIAC-8826 (Per l'inserimento - gestione collegamento come accertamento)
					List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
					if (listaModificheReimputazione!=null && !listaModificheReimputazione.isEmpty()) {
						List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegataAss = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
						for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : listaModificheReimputazione) {
							if (modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().equals(spesa.getAnnoReimputazione())) {
								//calcolo il residuo da salvare
								BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().subtract(modificheSpeseCollegata.getImportoCollegamento());
								modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
								listaModificheSpeseCollegataAss.add(modificheSpeseCollegata);
							}
						} 
						spesa.setListaModificheMovimentoGestioneSpesaCollegata(listaModificheSpeseCollegataAss);
					}
					// SIAC-8826 FINE
					listaToReturn.add(spesa);
				}
				
				
			}

			ModificaMovimentoGestioneEntrata spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaIneRor);
			}
			ModificaMovimentoGestioneEntrata spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaInsRor);
			}
			ModificaMovimentoGestioneEntrata spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				listaToReturn.add(spesaManRor);
			}

			// Subimpegno
			List<ModificaMovimentoGestioneEntrata> modificheSubList = new ArrayList<ModificaMovimentoGestioneEntrata>();

			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(getNumeroSubAccertamento())) {
					modificheSubList = sub.getListaModificheMovimentoGestioneEntrata();
				}
			}
			if (modificheSubList == null) {
				modificheSubList = new ArrayList<ModificaMovimentoGestioneEntrata>();
			} else {
				modificheSubList = CruscottoRorUtilities.getModificheStatoValidoAcc(modificheSubList);
			}
			tipoMovimento = "SAC";
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneEntrata spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					modificheSubList.add(spesa);
				}
			}

			ModificaMovimentoGestioneEntrata spesaIneRorSub = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRorSub.getImportoOld().compareTo(importoZero) != 0
					&& !spesaIneRorSub.getDescrizione().equals("")) {
				modificheSubList.add(spesaIneRorSub);
			}
			ModificaMovimentoGestioneEntrata spesaInsRorSub = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRorSub.getImportoOld().compareTo(importoZero) != 0
					&& !spesaInsRorSub.getDescrizione().equals("")) {
				modificheSubList.add(spesaInsRorSub);
			}
			ModificaMovimentoGestioneEntrata spesaManRorSub = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				modificheSubList.add(spesaManRorSub);
			}

			List<SubAccertamento> nuovaSubImpegnoList = new ArrayList<SubAccertamento>();
			for (SubAccertamento sub : model.getListaSubaccertamenti()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					sub.setListaModificheMovimentoGestioneEntrata(modificheSubList);
				}
				nuovaSubImpegnoList.add(sub);
			}
			// listaToReturn.addAll(modificheSubList);

			model.setListaSubaccertamenti(nuovaSubImpegnoList);

		}

		return listaToReturn;
	}

	private ModificaMovimentoGestioneEntrata costruisciSingolaSpesaSUB(GestioneCruscottoModel modificaDiSpesa,
			String tipoSpesa, String tipoMovimento) {
		ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();

		String importoInInput = "";

		if (tipoSpesa.equals("RORM")) {
			importoInInput = "0.00"; // qui va messo l'intero import FIXME
		} else {

			importoInInput = modificaDiSpesa.getImporto();
		}
		if (importoInInput == null || importoInInput.equals("")) {
			importoInInput = "0.00";
		}

		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoInInput);// da
		//FIX per importi in cancellazione da negare
		if(!tipoSpesa.equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}																		// controllare
																				// per
																				// mettere
																				// modificaDiSpesa.getImporto()
																				// //FIXME
		BigDecimal importoAttuale = BigDecimal.ZERO;
		BigDecimal importoCambiato  = BigDecimal.ZERO;
		
		//FIXME 00.45 18/02/2020
		if(tipoMovimento.equals("SAC")){
			importoAttuale = model.getSubAccertamentoInAggiornamento().getImportoAttuale();			
			importoCambiato = importoAttuale.add(importoDiInput);
			model.getSubAccertamentoInAggiornamento().setImportoAttuale(importoCambiato);
			
			
		}else{
			importoAttuale = model.getAccertamentoInAggiornamento().getImportoAttuale();
			importoCambiato = importoAttuale.add(importoDiInput);
			
			//SIAC-7995 la modifica era gia stata effettuata con la SIAC-7504
			//	SIAC-7534  
			model.getAccertamentoInAggiornamento().setImportoAttuale(importoCambiato);
			//model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
			
			
		}// //FIXME

		spesa.setUid(0);
		spesa.setImportoOld(importoDiInput); 
		spesa.setImportoNew(importoCambiato);
		
		// SIAC-7504
		if(tipoMovimento.equals("ACC")){
			model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);

			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);
		}
		spesa.setAnnoMovimento(model.getMovimentoSpesaModel().getAccertamento().getAnnoMovimento());



		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());

		spesa.setTipoModificaMovimentoGestione(modificaDiSpesa.getIdMotivo());
		if (getDescrizione() != null) {
			spesa.setDescrizione(getDescrizione().toUpperCase());
		}
		spesa.setTipoMovimento(tipoMovimento);

		settaDatiReimputazioneDalModel(spesa);

		spesa.setTipoModificaMovimentoGestione(tipoSpesa);
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
		String descrizione = null;

		if (modificaDiSpesa.getDescrizione() != null && !modificaDiSpesa.getDescrizione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(modificaDiSpesa.getDescrizione());
		} else {
			descrizione = "";
		}

		spesa.setDescrizione(descrizione);

		spesa.setTipoMovimento(tipoMovimento); // spesa.setTipoMovimento("IMP");//
												// TODO poi in base alla
												// tipologia va settato il
												// subimpegno
//		if (model.getnSubImpegno() == null) {
//			spesa.setIdStrutturaAmministrativa(model.getAccertamentoInAggiornamento().getIdStrutturaAmministrativa());
//			spesa.getAttoAmministrativo().setStrutturaAmmContabile(
//					model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile());
//			spesa.getAttoAmministrativo().setUid(model.getAccertamentoInAggiornamento().getAttoAmministrativo().getUid());
//		}

		if (tipoSpesa.equals("REIMP")) {
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(modificaDiSpesa.getAnno());
		}
		return spesa;
	}

	// Metodo per aggiornare le modifiche da cruscotto
	public String aggiornaModifiche() {
		//SIAC-8269
		clearWarningAndError(true);
		
		GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiAggiorna = clone(model.getMovimentoSpesaModel());
		GestisciImpegnoStep1Model step1ModelPrimaDiAggiorna = clone(model.getStep1Model());
		Accertamento accertamentoInAggiornamentoPrimaDiAggiorna = clone(model.getAccertamentoInAggiornamento());
		List<SubAccertamento> listaSubPrimaDiAggiorna = clone(model.getListaSubaccertamenti());
		SubAccertamento subPrimaDiAggiorna = clone(model.getSubAccertamentoInAggiornamento());
		AnnullaAggiornaMovimento req = creaAnnullaAggiornaMovimentoRequestBase();
		List<ModificaMovimentoGestioneEntrata> modificheNuoveDaAggiungere = new ArrayList<ModificaMovimentoGestioneEntrata>();// quelle nuove
		List<ModificaMovimentoGestioneEntrata> modificheDaAggiornare = new ArrayList<ModificaMovimentoGestioneEntrata>(); //quelle esistenti da aggiornare
		List<ModificaMovimentoGestioneEntrata> modifichePreesistentiAggiornate = new ArrayList<ModificaMovimentoGestioneEntrata>(); //quelle modificate
		List<ModificaMovimentoGestioneEntrata> listaModificheOld = sessionHandler //quelle ottenute dal BE
				.getParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO);
		setModificheDaPropagarePieno(false);
		
		BigDecimal importoInizialeAcc = BigDecimal.ZERO;
		
		StatoOperativoAtti statoAttiAccertamento;
		if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("")){
			setTipoImpegno("SubAccertamento");
			importoInizialeAcc = model.getSubAccertamentoInAggiornamento().getImportoAttuale();
			statoAttiAccertamento = subPrimaDiAggiorna.getAttoAmministrativo() == null ? null : subPrimaDiAggiorna.getAttoAmministrativo().getStatoOperativoAtti();
		}else{
			setTipoImpegno("Accertamento");
			importoInizialeAcc = model.getAccertamentoInAggiornamento().getImportoAttuale();
			statoAttiAccertamento = accertamentoInAggiornamentoPrimaDiAggiorna.getAttoAmministrativo() == null ? null : accertamentoInAggiornamentoPrimaDiAggiorna.getAttoAmministrativo().getStatoOperativoAtti();
		}
		
		List<Errore> erroriSalvataggio = new ArrayList<Errore>();
		
		if (model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == null
				&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento() == null) {
			Errore err = new Errore();
			err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
			err.setDescrizione("Non è stato selezionato nessun provvedimento");
			erroriSalvataggio.add(err);

		}
		//SIAC-7469 provvedimento delle modifiche anche provvsorie
		if (model.getStep1Model().getProvvedimento().getStato() != null
				&& model.getStep1Model().getProvvedimento().getStato().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;

		}
		//SIAC-7469 il provvedimento deve essere definitivo quello dell'accertamento
		if (statoAttiAccertamento == null || !statoAttiAccertamento.getDescrizione().equals(StatoOperativoAtti.DEFINITIVO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;

		}
		//check per aggiornare le modifiche presenti nel caso di cambio provvedimento
		boolean stessoProvvedimento = stessoProvvedimento(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento());
		
		boolean precedenteBloccatoDallaRagioneria = CruscottoRorUtilities.provvedimentoBloccato(model.getStep1ModelCache().getProvvedimento());
		
		if(precedenteBloccatoDallaRagioneria){
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			Errore err = new Errore();
			err.setCodice(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(model.getStep1ModelCache().getProvvedimento().getOggetto()).getCodice());
			err.setDescrizione(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(model.getStep1ModelCache().getProvvedimento().getOggetto()).getDescrizione());
			erroriSalvataggio.add(err);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			
			
		}
		
		// devo fare un metodo per ottenere i dati da annullare
		List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareDescImpNull = CruscottoRorUtilities
				.modificheDaAnnullareAcc(listaModificheRor, checkDaMantenere);
		
		//qui ho necessita di avere le informazioni della modifica, dunque devo andarla a prendere dalal sessione
		List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareComplete = CruscottoRorUtilities
				.modificheDaAnnullareCompleteAcc(modificheDaAnnullareDescImpNull, listaModificheOld);
		
		// Lista delle modifiche nuove da aggiungere in seguito ad una annualità
		erroriSalvataggio = CruscottoRorUtilities.checkCampiProvvedimentoVuoti(model.getStep1Model(), erroriSalvataggio);
		
		erroriSalvataggio = CruscottoRorUtilities.checkAnniReimputazioneRipetutiInAggAcc(listaModificheRor,
				erroriSalvataggio);
		erroriSalvataggio = CruscottoRorUtilities.checkAnnoNonCongruenteInAggAcc(listaModificheRor, erroriSalvataggio,
				sessionHandler.getBilancio().getAnno());
		
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna,listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
			}
		
		//Anni ripetuti
		
		if (listaModificheRor.size() > listaModificheOld.size()) {
			modificheNuoveDaAggiungere = CruscottoRorUtilities.modificheValideDaAggiungereAcc(listaModificheRor); // setto																									// modifiche
			erroriSalvataggio = CruscottoRorUtilities.checkModificheCompleteInAggAcc(erroriSalvataggio, modificheNuoveDaAggiungere);
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna,listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		//questi if servono per costruire nuove modifiche in aggiornamento
		if(getInesigibilitaRor().equals(false) && !CruscottoRorUtilities.checkEmptyModif(rorCancellazioneInesigibilita, "")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneEntrata nuovaInesigibilita = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestioneAcc(rorCancellazioneInesigibilita, "INEROR");
			modificheNuoveDaAggiungere.add(nuovaInesigibilita);
			
		}else if(getInesigibilitaRor().equals(false) && (rorCancellazioneInesigibilita.getImporto() != null)){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, rorCancellazioneInesigibilita, null, null, null);
			
		}
		if(getInsussistenzaRor().equals(false) && !CruscottoRorUtilities.checkEmptyModif(rorCancellazioneInsussistenza, "")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneEntrata nuovaIsussistenza = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestioneAcc(rorCancellazioneInsussistenza, "INSROR");
			modificheNuoveDaAggiungere.add(nuovaIsussistenza);
			
		}else if(getInsussistenzaRor().equals(false) && (rorCancellazioneInsussistenza.getImporto() != null)){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, null, rorCancellazioneInsussistenza,null, null);
		}
		if(getDaMantenerePresente().equals(false) && getCheckDaMantenere() && !CruscottoRorUtilities.checkEmptyModif(rorDaMantenere, "RORM")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneEntrata nuovoMantenere = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestioneAcc(rorDaMantenere, "RORM");
			modificheNuoveDaAggiungere.add(nuovoMantenere);
			
		}else if(getDaMantenerePresente().equals(false) && getCheckDaMantenere()){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, null, null, rorDaMantenere, getCheckDaMantenere());
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna,listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		// prima però devo escludere quelli aggiunti da lista modifiche ror
		modifichePreesistentiAggiornate = CruscottoRorUtilities.modificheMovimentiPreesistentiAcc(listaModificheRor);
		
		// Lista modifiche da annullare e inserire nuovamente, in quanto si è modificato l'importo
		List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareERicerare = CruscottoRorUtilities
				.modificheDaAnnullarePerRicreareAcc(modifichePreesistentiAggiornate, listaModificheOld, stessoProvvedimento);
				
			
				
		if(modificheDaAnnullareERicerare != null && modificheDaAnnullareERicerare.size()>0 && isPropagaSelected()){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione("Non &egrave; possibile modificare un importo in modalit&agrave; propaga");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
						
		}
		
		if(stessoProvvedimento){
			modificheDaAggiornare = CruscottoRorUtilities.getMovimentiDaAggiornareAcc(modifichePreesistentiAggiornate,
					listaModificheOld);//
			
		}
		modificheDaAggiornare.addAll(modificheNuoveDaAggiungere);
		

		// Le modifiche da annullare sono quelle che hanno subito -> modifiche
		// in importo == 0 e descrizione null, che non andranno piu salvate
		// e in piu devo annullare quelle che hanno subito una modifica, dunque
		// quelle presenti in modifiche da aggiornare che coincidono
		/**
		 * modificheDaAnnullareDescImpNull modificheDaAggiornare
		 * 
		 */
		// a partire da queste liste, ne va creata una nuova con la quale viene
		// creata la request di annullamento modifiche
		List<ModificaMovimentoGestioneEntrata> modifichePerAnnullamenti = new ArrayList<ModificaMovimentoGestioneEntrata>();
		if (!modificheDaAnnullareDescImpNull.isEmpty() || !modificheDaAnnullareERicerare.isEmpty()) {
			modifichePerAnnullamenti.addAll(modificheDaAnnullareDescImpNull);
			//essendo che le reimputazioni vengono istanziate all'atterraggio, il metodo le vedrà da annullare. Dunque rimuovo quelle con uid 0
			modifichePerAnnullamenti.addAll(modificheDaAnnullareERicerare);
			modifichePerAnnullamenti = CruscottoRorUtilities.remuoviReimputazioniAccNonInserite(modifichePerAnnullamenti);
		}
		
		//questo calcolo mi serve per gestire la situazione mista aggiornamento e annullamento
		BigDecimal importoAnnullatoPrimaDiAggiornamento = BigDecimal.ZERO;
		//02/03/2020 
		//qui ho necessita degli importi reimputati e cancellati per il ricalcolo dei limiti importi di inserimento		
		BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione = BigDecimal.ZERO;
		BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione = BigDecimal.ZERO;
		if(!modificheDaAnnullareDescImpNull.isEmpty()){
			importoAnnullatoPrimaDiAggiornamento = CruscottoRorUtilities.getImportoAttualeDaAnnullaInserisciAcc(modificheDaAnnullareDescImpNull, listaModificheOld);
			
			importoAnnullatoPrimaDiAggiornamentoReimputazione = CruscottoRorUtilities.getImportoReimputatoAnnullatoAcc(modificheDaAnnullareDescImpNull, listaModificheOld);
			importoAnnullatoPrimaDiAggiornamentoCancellazione = CruscottoRorUtilities.getImportoCancellatoAnnullatoAcc(modificheDaAnnullareDescImpNull, listaModificheOld);
			
		}
		

		//per ricreare, clono quelle contenute nell'array delle modifiche da annullare e ricreare, e setto UID == 0
		List<ModificaMovimentoGestioneEntrata> nuoveDaRicreare = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		BigDecimal importoAnnullatoPerRicreare = BigDecimal.ZERO;
		
		BigDecimal importoAnnullatoReimputazionePerRicreare = BigDecimal.ZERO;
		BigDecimal importoAnnullatoCancellazionePerRicreare = BigDecimal.ZERO;
		if(modificheDaAnnullareERicerare.size()>0){
			nuoveDaRicreare = clone(modificheDaAnnullareERicerare);
			importoAnnullatoPerRicreare = CruscottoRorUtilities.getImportoAttualeDaAnnullaInserisciAcc(nuoveDaRicreare, listaModificheOld);
			
			importoAnnullatoReimputazionePerRicreare = CruscottoRorUtilities.getImportoReimputatoAnnullatoAcc(nuoveDaRicreare, listaModificheOld);
			importoAnnullatoCancellazionePerRicreare = CruscottoRorUtilities.getImportoCancellatoAnnullatoAcc(nuoveDaRicreare, listaModificheOld);
			
			
			BigDecimal nuovoImporto = BigDecimal.ZERO;
			if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("")){
				nuovoImporto = model.getSubAccertamentoInAggiornamento().getImportoAttuale().subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
				model.getSubAccertamentoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("SubAccertamento");

			}else{
				nuovoImporto = model.getAccertamentoInAggiornamento().getImportoAttuale().subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
				model.getAccertamentoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("Accertamento");
				
			}
			for(ModificaMovimentoGestioneEntrata mmgs : nuoveDaRicreare){
				mmgs.setUid(0);
			}
				
			modificheDaAggiornare.addAll(nuoveDaRicreare);
		}else{
			BigDecimal nuovoImporto = BigDecimal.ZERO;
			//le modifiche di cui e' stata modificata solo la descrizione, hanno gia' decurtato l'importo attuale
			BigDecimal importoDelleModificheDicuiModificatoSoloDesc =CruscottoRorUtilities.getImportoModificheConSolaDescrizioneModificataEntrata(modificheDaAggiornare, listaModificheOld);
			if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("")){
				
				nuovoImporto = model.getSubAccertamentoInAggiornamento().getImportoAttuale().add(importoDelleModificheDicuiModificatoSoloDesc);	
				//lasciato per retrocompatibilita', dubbi che possa essere giusto.
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
				
				model.getSubAccertamentoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("SubAccertamento");

			}else{
				//aggiungo questo importo in quanto ha gia' decurtato l'importo attuale.
				nuovoImporto = model.getAccertamentoInAggiornamento().getImportoAttuale().add(importoDelleModificheDicuiModificatoSoloDesc);	
				//lasciato per retrocompatibilita', dubbi che possa essere giusto.
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
			
				model.getAccertamentoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("Accertamento");
				
			}
		}

		erroriSalvataggio = CruscottoRorUtilities.checkModificheCompleteInAggAcc(erroriSalvataggio, modificheNuoveDaAggiungere);

		// Costruisci request per aggiorna. Ciclo la lista delle modifiche, se
		// trovo importi e descrizione a null non le aggiungo come
		// nuovi movimento da inserire.
		//FIXME trova in rorm una modifica da aggiornare
		List<ModificaMovimentoGestioneEntrata> modificheList = costruisciListaMovimentiSpesa(modificheDaAggiornare); // new
		
		if(modificheDaAggiornare != null && modificheDaAggiornare.isEmpty() && modifichePerAnnullamenti!=null && modifichePerAnnullamenti.isEmpty()){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione("Salvataggio non effettuato: non &egrave; stata aggiornata nessuna modifica");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;				
		}
		
		//Gestione residuo da mantenere a zero
		//posso calcolare il residuo sottraendo al aggiungendo il delta importo impegno
		if(getDaMantenerePresente() && getCheckDaMantenere() != null && getCheckDaMantenere()==true){
			BigDecimal residuoZero = new BigDecimal("0.00");
			BigDecimal deltaImportoImpegno = residuoDaMantenereDopoAggiornamento(importoInizialeAcc);
			BigDecimal residuoDaMantenere = model.getResiduoEventualeDaMantenere();
			residuoDaMantenere = residuoDaMantenere.add(deltaImportoImpegno);
			if(residuoDaMantenere.compareTo(residuoZero)==0){
				ModificaMovimentoGestioneEntrata mmgs = getDaMantenerePerAnnullare(listaModificheOld);
				modifichePerAnnullamenti.add(mmgs);
			}
		}
		
		if(modificheList != null && !modificheList.isEmpty()){
			
			erroriSalvataggio = checkModificheValide(modificheList, erroriSalvataggio, true,
					importoAnnullatoPrimaDiAggiornamentoReimputazione, importoAnnullatoPrimaDiAggiornamentoCancellazione,
					importoAnnullatoReimputazionePerRicreare, importoAnnullatoCancellazionePerRicreare);
		}
		
		erroriSalvataggio = CruscottoRorUtilities.checkCampiProvvedimentoVuoti(model.getStep1Model(), erroriSalvataggio);

		//essendo che le modifiche vengono annullate e modificate per UID, viene in automatico il salvatagiio passando nella request, l'uid della modifica dell'impegno o subimpegno
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna,listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
			}
		
		/*******CONTROLLO ERRORI PER PROVVEDIMENTO BLOCCATO****/
		if(modificheList.size()>0){
			ModificaMovimentoGestioneEntrata mmgs = modificheList.get(0);
			Integer idAtto = mmgs.getAttoAmministrativo().getUid();
			erroriSalvataggio = checkProvvedimentoInAggiorna(erroriSalvataggio, idAtto);
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		if(modificheDaAnnullareComplete.size()>0){
			ModificaMovimentoGestioneEntrata mmgs = modificheDaAnnullareComplete.get(0);
			Integer idAtto = mmgs.getAttoAmministrativo().getUid();
			erroriSalvataggio = checkProvvedimentoInAggiorna(erroriSalvataggio, idAtto);	
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		AggiornaAccertamento requestAggiorna = new AggiornaAccertamento();
		requestAggiorna = convertiModelPerChiamataServizioAggiornaAccertamenti(false);
		//se vuoto, non chiamo il servizio, altrimenti annulla tutto l'impegno
		if(!modifichePerAnnullamenti.isEmpty()){
			AnnullaMovimentoEntrata requestAnnulla = convertiModelPerChiamataServizioAnnullaListaMovimenti(
					modifichePerAnnullamenti); // old list
			req.setAnnullaMovimentoEntrata(requestAnnulla);
			req.setFlagAnnullaConsentito(true);
		}else{
			req.setFlagAnnullaConsentito(false);
		}
		//parte aggiorna
		
		
		//SIAC-7349 Inizio  SR190 FL 15/04/2020 (AGGIORNAMENTO) Rieffettuo i controlli che già sono stati effettua al conferma della modale
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreListaCollegata = false;
		if(numeroSubAccertamento == null || numeroSubAccertamento.equals("") ){
			for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : modificheList) {
					if(modificaMovimentoGestioneEntrata.isReimputazione()) {
						erroreListaCollegata = checkListaCollegataImpegni( modificaMovimentoGestioneEntrata,listaErrori);
					}
				}
				if(!listaErrori.isEmpty() || erroreListaCollegata){
					
					//SIAC-VVVV
					List<ModificaMovimentoGestioneSpesaCollegata> modificheReimputazione = getListaModificheSpeseCollegata();
					//CONTABILIA-260 - Bug fix per email Inizio FL CM 09/07/2020
					if (modificheReimputazione!=null && !modificheReimputazione.isEmpty()) {
						for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : modificheReimputazione) {    
							//calcolo il residuo   
							
							//SIAC-8009 
							for (int i = 0; i < modificheDaAggiornare.size(); i++) {
								ModificaMovimentoGestioneEntrata mmgs = modificheDaAggiornare.get(i);
								BigDecimal importoZero = new BigDecimal("0.00");
								if (!mmgs.getImportoOld().equals(importoZero)
										&& !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
									if (modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().equals(mmgs.getAnnoReimputazione())) {
										
										BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().add(modificheSpeseCollegata.getImportoCollegamento());
										modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
										
									}
									
								}
							}
							
						}
					 } 
					//CONTABILIA-260 - Bug fix per email Fine FL CM 09/07/2020
					
					//presenza errori
					//SIAC-7995 ripristino i dati 
					ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
					addErrori(listaErrori);
					return INPUT;
			}
		}
		//SIAC-7349 Fine  SR190 FL 15/04/2020
		
		if(!modificheList.isEmpty()){
			req.setFlagAggiornaConsentito(true);
			if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("") ){
				BigDecimal numero = new BigDecimal(numeroSubAccertamento);
				for(int i=0; i<requestAggiorna.getAccertamento().getElencoSubAccertamenti().size(); i++){
					if((requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getNumeroBigDecimal()).equals(numero)){
						if(requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata()==null){
							List<ModificaMovimentoGestioneEntrata> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneEntrata>();
							listaNuovaDaAggiungere.addAll(modificheList);
							requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).setListaModificheMovimentoGestioneEntrata(listaNuovaDaAggiungere);
						}else{
							for(int j=0; j<requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().size(); j++){
								for(int h=0; h<modificheList.size();h++){
									if(modificheList.get(h).getUid() !=0 && requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().get(j).getUid()==modificheList.get(h).getUid()){
										modificheList.get(h).setUidSubAccertamento(requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().get(j).getUid());
										modificheList.get(h).setTipoMovimento("SIM");
										requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().get(j).setDescrizioneModificaMovimentoGestione(modificheList.get(h).getDescrizioneModificaMovimentoGestione());
										requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().get(j).setImportoOld(modificheList.get(h).getImportoOld());
										
									}
								}
							}							
							for(int h=0; h<modificheList.size(); h++){
								if(modificheList.get(h).getUid()==0){
									requestAggiorna.getAccertamento().getElencoSubAccertamenti().get(i).getListaModificheMovimentoGestioneEntrata().add(modificheList.get(h));
								}
							}								
						}
	
					}
				
				}
				
				
				//se propaga selezionato
				if(isPropagaSelected()==true){
					
					List<ModificaMovimentoGestioneEntrata> listeModifichePropagate = costruisciListaMovimentiSpesaPropaga(modificheDaAggiornare);					
					
					if(requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata()==null){
						List<ModificaMovimentoGestioneEntrata> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneEntrata>();
						listaNuovaDaAggiungere.addAll(listeModifichePropagate);
						if(!listeModifichePropagate.isEmpty()){
							setModificheDaPropagarePieno(true); 							
						}
						requestAggiorna.getAccertamento().setListaModificheMovimentoGestioneEntrata(listaNuovaDaAggiungere);
						
					}else{
						if(!listeModifichePropagate.isEmpty()){
							setModificheDaPropagarePieno(true); 							
						}
						//requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().addAll(listeModifichePropagate);
						for(int j=0; j<listeModifichePropagate.size(); j++){
							if(listeModifichePropagate.get(j).getUid()==0){
								requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(listeModifichePropagate.get(j));
							}
						}
					}
				}

			}else{
				if(requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata()==null){
					List<ModificaMovimentoGestioneEntrata> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneEntrata>();
					listaNuovaDaAggiungere.addAll(modificheList);
					requestAggiorna.getAccertamento().setListaModificheMovimentoGestioneEntrata(listaNuovaDaAggiungere);
					
				}else{
					
					for(int i=0; i<requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().size(); i++){
						for(int j=0; j<modificheList.size(); j++){
							if(modificheList.get(j).getUid() !=0 && requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().get(i).getUid()==modificheList.get(j).getUid()){
								requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().get(i).setDescrizioneModificaMovimentoGestione(modificheList.get(j).getDescrizioneModificaMovimentoGestione());
								requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().get(i).setImportoOld(modificheList.get(j).getImportoOld());
							}
						}
					}
					for(int j=0; j<modificheList.size(); j++){
						if(modificheList.get(j).getUid()==0){
							requestAggiorna.getAccertamento().getListaModificheMovimentoGestioneEntrata().add(modificheList.get(j));
						}
					}
					
				}
				
			}

			req.setAggiornaAccertamento(requestAggiorna);
		
		}else{
			req.setFlagAggiornaConsentito(false);
		}
		

		AnnullaAggiornaMovimentoResponse response = movimentoGestioneFinService.annullaAggiornaImpegno(req);
		if(response.isFallimento()){
			List<Errore> errs = CruscottoRorUtilities.getErroreDaWrapper(response.getDescrizioneErrori(), response.getErrori());
			addErrori("Errore al salvataggio delle modifiche", errs);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, accertamentoInAggiornamentoPrimaDiAggiorna,listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			
			return INPUT;
		}
		
		sessionHandler 
		.setParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO, null);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione(
				ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
						+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		if(isPropagaSelected()==true && isModificheDaPropagarePieno()==true){
			impostaMessaggioAzioneInSessionePerRedirezione(ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getCodice() + " "
					+ ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getErrore("accertamento").getDescrizione());			
		}
		
//		if(getCheckDaMantenere() != null && getCheckDaMantenere()==true && modificheNuoveDaAggiungere.isEmpty()){
//			impostaMessaggioAzioneInSessionePerRedirezione(ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getCodice() + " "
//					+ ErroreFin.DA_MANTENERE_NON_INSERITA.getErrore("").getDescrizione());
//		}
		
		return SUCCESS_AGGIORNA;

	}

	// metodo che mi consente di inserire nuove annualita
	

	

	private ModificaMovimentoGestioneEntrata getDaMantenerePerAnnullare(
			List<ModificaMovimentoGestioneEntrata> listaModificheOld) {
		for(ModificaMovimentoGestioneEntrata mmgs : listaModificheOld){
			if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) &&
					mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				return mmgs;
			}
		}
		return null;
	}

	private BigDecimal residuoDaMantenereDopoAggiornamento(BigDecimal importoInizialeAcc) {
		BigDecimal importoFinale = BigDecimal.ZERO;
		if(numeroSubAccertamento != null && !numeroSubAccertamento.equals("")){
			importoFinale=model.getSubAccertamentoInAggiornamento().getImportoAttuale();
		}else{
			importoFinale=model.getAccertamentoInAggiornamento().getImportoAttuale();
		}
		return importoFinale.subtract(importoInizialeAcc);

	}

	private List<ModificaMovimentoGestioneEntrata> costruisciListaMovimentiSpesa(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2) {
		List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
		BigDecimal importoZero = new BigDecimal("0.00");
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneEntrata mmgs = listaModificheRor2.get(i);
			if (!mmgs.getImportoOld().equals(importoZero)
					&& !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
				modificheList.add(singolaSpesaAggiorna(mmgs));
			}
			
			if(mmgs.getTipoModificaMovimentoGestione().equals("RORM") && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")){
				modificheList.add(singolaSpesaAggiorna(mmgs));
				
			}

		}

		return modificheList;
	}

	private ModificaMovimentoGestioneEntrata singolaSpesaAggiorna(ModificaMovimentoGestioneEntrata mmgs) {
		ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
		BigDecimal importoInInput = new BigDecimal("0.00");

		if (!mmgs.getTipoModificaMovimentoGestione().equals("RORM")) {
			importoInInput = mmgs.getImportoOld();
			if (importoInInput == null || importoInInput.equals("")) {
				importoInInput = new BigDecimal("0.00");
			}
		}

		BigDecimal importoDiInput = importoInInput;// da
		//SIAC-7405 in cancellazione positivo da negare
		if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}
		BigDecimal importoAttuale = model.getAccertamentoInAggiornamento().getImportoAttuale();
	
		spesa.setTipoMovimento("ACC");//qui devo metterlo dinamico
		if(numeroSubAccertamento!=null && !numeroSubAccertamento.equals("")){
			spesa.setNumeroSubAccertamento(Integer.valueOf(numeroSubAccertamento));
			spesa.setTipoMovimento("SAC");//qui devo metterlo dinamico
			importoAttuale = model.getSubAccertamentoInAggiornamento().getImportoAttuale();
		}
		BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
		spesa.setUid(mmgs.getUid());
		spesa.setImportoOld(importoDiInput); // da																	// //FIXME
		spesa.setImportoNew(importoCambiato);
		
		if(numeroSubAccertamento!=null && !numeroSubAccertamento.equals("")){
			model.getSubAccertamentoInAggiornamento().setImportoAttuale(importoCambiato);	
		}else{
			model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
			model.getAccertamentoInAggiornamento().setImportoAttuale(importoCambiato);
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);
		}


		// DATI DEL PROVVEDIMENTO DELLA MODIFICA:
		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());

		spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
		if (mmgs.getDescrizione() != null) {
			spesa.setDescrizione(mmgs.getDescrizione().toUpperCase());
		}

		settaDatiReimputazioneDalModel(spesa);


		spesa.setTipoModificaMovimentoGestione(mmgs.getTipoModificaMovimentoGestione());
		String descrizione = null;

		if (mmgs.getDescrizioneModificaMovimentoGestione() != null && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(mmgs.getDescrizioneModificaMovimentoGestione());
		} else {
			descrizione = "";
		}
		spesa.setDescrizioneModificaMovimentoGestione(descrizione);
		spesa.setDescrizione(descrizione);

//		spesa.setIdStrutturaAmministrativa(model.getAccertamentoInAggiornamento().getIdStrutturaAmministrativa());
//		spesa.getAttoAmministrativo().setStrutturaAmmContabile(
//				model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile());
//		spesa.getAttoAmministrativo().setUid(model.getAccertamentoInAggiornamento().getAttoAmministrativo().getUid());
		if (mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
		List<ModificaMovimentoGestioneSpesaCollegata> modificheDiReimputazione = getListaModificheSpeseCollegata();
			//SIAC-7349 Inizio  SR190 FL 15/04/2020 (AGGIORNAMENTO) VALORIZZO LA LISTA REIMPUTAZIONE DI SPESA
			if (modificheDiReimputazione!=null && !modificheDiReimputazione.isEmpty()) {
				List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegataAss = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
				for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : modificheDiReimputazione) {
	
					if (modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getAnnoReimputazione().equals(mmgs.getAnnoReimputazione())) {
						//calcolo il residuo   
						BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().subtract(modificheSpeseCollegata.getImportoCollegamento());
						modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
						listaModificheSpeseCollegataAss.add(modificheSpeseCollegata);
					}
				} 
				spesa.setListaModificheMovimentoGestioneSpesaCollegata(listaModificheSpeseCollegataAss);
			}
			//SIAC-7349 Fine  SR190 FL 15/04/2020				
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(mmgs.getAnnoReimputazione());
		}
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);

		return spesa;
	}



	private AnnullaMovimentoEntrata convertiModelPerChiamataServizioAnnullaListaMovimenti(
			List<ModificaMovimentoGestioneEntrata> listaModificheRorDaAggiornare2) {

		AnnullaMovimentoEntrata request = new AnnullaMovimentoEntrata();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());

		// setto impegno
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(model.getMovimentoSpesaModel().getAccertamento().getUid());

		// setto uid movimento spesa

		List<ModificaMovimentoGestioneEntrata> listaMGP = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < listaModificheRorDaAggiornare2.size(); i++) {
			ModificaMovimentoGestioneEntrata mgs = new ModificaMovimentoGestioneEntrata();
			mgs.setUid(Integer.valueOf(listaModificheRorDaAggiornare2.get(i).getUid()));
			listaMGP.add(mgs);
		}

		accertamento.setListaModificheMovimentoGestioneEntrata(listaMGP);
		request.setAccertamento(accertamento);
		return request;

	}

	/**
	 * pilota l'abilitazione alla modifica della propagazione all'impegno
	 * 
	 * @return
	 */
	public boolean abilitaModificaSubAccertamento() {
		boolean abilitazioneModifica = false;

		if (getTipoImpegno().equalsIgnoreCase("SubAccertamento")) {
			if (!StringUtils.isEmpty(getSubSelected())) {
				abilitazioneModifica = true;
				setSubAccertamentoSelected(true);
				model.setSubImpegnoSelectedMod(true);
			}
		}

		return abilitazioneModifica;
	}

	/**
	 * @return the propagaSelected
	 */
	public boolean isPropagaSelected() {
		return propagaSelected;
	}

	/**
	 * @param propagaSelected
	 *            the propagaSelected to set
	 */
	public void setPropagaSelected(boolean propagaSelected) {
		this.propagaSelected = propagaSelected;
	}

	protected List<Errore> controlliReimputazioneInInserimentoEMotivoRIAC(List<Errore> listaErrori, String tipoImpegno,
			String abbina, ModificaMovimentoGestioneEntrata spesaSelezionata) {
		String reimputazione = null;
		if (spesaSelezionata.isReimputazione() == false) {
			reimputazione = WebAppConstants.No;
		} else {
			reimputazione = WebAppConstants.Si;
		}

		Bilancio bilancio = this.sessionHandler.getBilancio();
		int annoBilancio = bilancio.getAnno();

		if (listaErrori == null) {
			listaErrori = new ArrayList<Errore>();
		}

		WebAppConstants.Si.equals(reimputazione);
		boolean modificaSulSubSenzaIndicarneLaTestata = (tipoImpegno.equals("SubImpegno")
				&& !"Modifica Anche Impegno".equalsIgnoreCase(abbina));
				// SIAC-8826 su accertamento non deve dare errore
				//|| (tipoImpegno.equals("SubAccertamento") && !"Modifica Anche Accertamento".equalsIgnoreCase(abbina));
		//fix inserita per permettere l'aggiornamento delle reimputazioni quando si è in fase di aggiorna per le reimputazioni del subimpegno
		//15/03/2020 DA RIVEDERE in quanto visto che si tratta di un nuovo inserimento, per logica andrebbe propagata. 
		//tuttavia, la modifica di un importo non si puòà fare in modalita propaga in quanto andrebbe ad inserire una nuova
		//modifica al padre. Da togliere, se necessario && spesaSelezionata.getUid()==0
		if (modificaSulSubSenzaIndicarneLaTestata && WebAppConstants.Si.equals(reimputazione) && spesaSelezionata.getUid()==0) {
			// La modifica e' sul sub, ma non e' stato selezionato anche
			// l'impegno, errore:
			// SIAC-8826 su accertamento non deve dare errore
			/*Errore errore = tipoImpegno.equals("SubImpegno")
					? ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_IMP_CRUSC.getErrore(spesaSelezionata.getAnnoReimputazione().toString())
					: ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_ACC_CRUSC.getErrore(spesaSelezionata.getAnnoReimputazione().toString());
			*/
			Errore errore = ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_IMP_CRUSC.getErrore(spesaSelezionata.getAnnoReimputazione().toString());
			listaErrori.add(errore);
			return listaErrori;// questo errore maschera quelli dopo, perche'
								// tanto l'utente dovra' mettere reimputazione
								// no
		}
		boolean isRORDaMantenere = "RORM".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), ""));
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(spesaSelezionata.getImportoOld()) == 0;
		
		if (isRORDaMantenere && !isImportoZero) {
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ",
					"deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
//		boolean isCancellazioni = "INEROR".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), "")) || "INSROR".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), ""));
//		boolean isImportoMinDiZero = BigDecimal.ZERO.compareTo(spesaSelezionata.getImportoOld()) > 0;
//		if(isCancellazioni && isImportoMinDiZero){
//			String motivo = spesaSelezionata.getTipoModificaMovimentoGestione().equals("INSROR") ? "ROR - Cancellazione per Insussistenza" : "ROR - Cancellazione per Inesigibilit&agrave;" ;
//			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo "+motivo, "l' importo deve essere maggiore di zero"));
//		}
		

		// controlli su reimputazione:
		controlliSuReimputazioneCrusc(listaErrori, tipoImpegno, String.valueOf(spesaSelezionata.getImportoOld()),
				spesaSelezionata.getTipoModificaMovimentoGestione(), reimputazione, annoBilancio, spesaSelezionata);


		return listaErrori;
	}

	private void controlliSuReimputazioneCrusc(List<Errore> listaErrori, String tipoImpegno,
			String importoImpegnoModImp, String tipoMotivo, String reimputazione, int annoBilancio,
			ModificaMovimentoGestioneEntrata spesaSelezionata) {
		if (!WebAppConstants.Si.equals(reimputazione)) {
			// non e' una reimputazione, non devo effettuare i controlli sulla
			// reimputazione
			return;
		}

		// se selezionato si, deve essere compilato l'anno:
		if (spesaSelezionata.getAnnoReimputazione() == null || spesaSelezionata.getAnnoReimputazione() <= 0) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Reimputazione"));
		} else if (spesaSelezionata.getAnnoReimputazione().intValue() <= Integer
				.valueOf(sessionHandler.getAnnoEsercizio())) {
			// inoltre l'anno di reimputazione deve essere maggiore dell'anno di
			// bilancio:
			listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Anno Reimputazione",
					"(deve essere maggiore dell'anno di bilancio)"));
		}

		// in caso di reimputazione, l'importo della modifica puo' essere solo
		// negativo:
		//SIAC-7502 reimputazioni devono essere positive
//		if (!StringUtils.isEmpty(importoImpegnoModImp)
//				&& NumberUtil.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))) {
//			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
//			// nel caso importo fosse nullo scattera' gia' il controllo piu'
//			// avanti sulla sua obbligatorieta'
//			if (importoDiInput.compareTo(BigDecimal.ZERO) >= 0 && spesaSelezionata.getAnnoReimputazione() != null) {
//				Errore err = new Errore();
//				err.setCodice("FIN_ERR_0127");
//				err.setDescrizione("La reimputazione per l'anno " + spesaSelezionata.getAnnoReimputazione()
//						+ " puo' essere realizzata solo a seguito di una modifica in diminuzione di importo del movimento");
//				listaErrori.add(err);
//			}
//		}
		

		boolean isMovimentoGestioneSpesa = tipoImpegno.equals("Impegno") || tipoImpegno.equals("SubImpegno");
		boolean isMovimentoGestioneEntrata = tipoImpegno.equals("SubAccertamento")
				|| tipoImpegno.equals("Accertamento");
		if (!isMovimentoGestioneSpesa && !isMovimentoGestioneEntrata) {
			// questo non dovrebbe mai capitare, messo per analogia con il
			// comportamento precedente
			return;
		}

		if (tipoMotivo.equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
			// se REIMPUTAZIONE = TRUE inviare un messaggio di errore
			// FIN_ERR_0129 - Riaccertamento residui non ammesso
			// contemporaneamente alla reimputazione, 'Impegno'
			// (o accertamento)
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_IMP.getErrore()
					: ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_ACC.getErrore();
			listaErrori.add(erroreDaLanciare);
		}

		
		//SIAC -7559 Inizio  ROR 10 11 FL 19/05/2020
//		if (annoBilancio > spesaSelezionata.getAnnoMovimento()) {
//			Errore erroreDaLanciare = isMovimentoGestioneSpesa
//					? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
//					: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
//			// se anno Impegno < anno di bilancio FIN_ERR_0129 - Reimputazione
//			// residui non ammesso, 'Accertamento'
//			listaErrori.add(erroreDaLanciare);
//		}
		//SIAC -7559 Fine  ROR 10 11 FL 19/05/2020
	
		

		//SIAC-7551 Modifica FL
		/*
		 * SIAC-7689 
		 * FIX VG anno Movimento non sempre valorizzato
		 */
		if (model.getStep1Model()!= null && model.getStep1Model().getAnnoImpegno()!= null &&
				annoBilancio > model.getStep1Model().getAnnoImpegno().intValue()) {
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
					: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
			// se anno Impegno < anno di bilancio FIN_ERR_0129 - Reimputazione
			// residui non ammesso, 'Accertamento'
			listaErrori.add(erroreDaLanciare);
		}
		//SIAC-7551 FINE Modifica FL

	}

	

	@SuppressWarnings("unused")
	private List<Errore> checkImportoModificaSuTestata(List<Errore> listaErrori, BigDecimal importoDaModificare,
			String tipoModifica) {
		// Importo Obbligatorio
		// SIAC-6586: aggiunto controllo importi e raggruppato, vedere se
		// possibile in un secondo momento riorganizzare

		BigDecimal importoModificaSuTestata = importoDaModificare;

		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(tipoModifica, ""));

		boolean isImportoZero = BigDecimal.ZERO.compareTo(importoModificaSuTestata) == 0;

		if (isRORDaMantenere && !isImportoZero) {
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ",
					"deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		if (!isRORDaMantenere && (importoModificaSuTestata.compareTo(minImportoImpegnoApp) < 0 || isImportoZero)) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"L&apos;importo per la modifica " + tipoModifica + " deve essere essere superiore al minimo");
			listaErrori.add(err);
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		if ((!isRORDaMantenere && importoModificaSuTestata.compareTo(maxImportoImpegnoApp) > 0)) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione("L&apos;importo per la modifica " + tipoModifica + " deve essere inferiore al massimo");
			listaErrori.add(err);
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		// //Controllo modifiche solo negative per RIU e RIAC - jira 1052
		// if(!StringUtils.isEmpty(importoDaModificare) &&
		// getIdTipoMotivo()!=null &&
		// (getIdTipoMotivo().equalsIgnoreCase("RIU") ||
		// getIdTipoMotivo().equalsIgnoreCase("RIAC")) &&
		// importoModificaSuTestata.intValue()>0){
		// Errore errore = getIdTipoMotivo().equalsIgnoreCase("RIAC")?
		// ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno") :
		// ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere
		// negativo)");
		// listaErrori.add(errore);
		// model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		// }

		return listaErrori;
	}
	
	
	
	private ModificaMovimentoGestioneEntrata singolaSpesaAggiornaPropaga(ModificaMovimentoGestioneEntrata mmgs) {
		ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
		BigDecimal importoInInput = new BigDecimal("0.00");

		if (!mmgs.getTipoModificaMovimentoGestione().equals("RORM")) {
			importoInInput = mmgs.getImportoOld();
			if (importoInInput == null || importoInInput.equals("")) {
				importoInInput = new BigDecimal("0.00");
			}
		}

		BigDecimal importoDiInput = importoInInput;// da
		//SIAC-7405 in cancellazione positivo da negare
		if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}
		BigDecimal importoAttuale = model.getMovimentoSpesaModel().getAccertamento().getImportoAttuale();
		BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
		spesa.setUid(mmgs.getUid());
		spesa.setImportoOld(importoDiInput); // da																	// //FIXME
		spesa.setImportoNew(importoCambiato);
		spesa.setTipoMovimento("ACC");//qui devo metterlo dinamico
		model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
		model.getStep1Model().setImportoImpegno(importoCambiato);
		// DATI DEL PROVVEDIMENTO DELLA MODIFICA:
		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());

		spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
		if (mmgs.getDescrizione() != null) {
			spesa.setDescrizione(mmgs.getDescrizione().toUpperCase());
		}

		settaDatiReimputazioneDalModel(spesa);
		spesa.setAttoAmministrativoAnno(
				String.valueOf(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()));
		spesa.setAttoAmministrativoNumero(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
		if (!isEmpty(model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento())) {
			spesa.setAttoAmministrativoTipoCode(
					String.valueOf(model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento()));
		} else {
			spesa.setAttoAmministrativoTipoCode(
					String.valueOf(model.getStep1Model().getProvvedimento().getTipoProvvedimento()));
		}
		spesa.setIdStrutturaAmministrativa(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());
		AttoAmministrativo attoAmm = popolaProvvedimento(model.getStep1Model().getProvvedimento());
		spesa.setAttoAmministrativo(attoAmm);
		// va tenuto coerente:
		TipoAtto attoAmmTipoAtto = attoAmm.getTipoAtto();
		spesa.setAttoAmmTipoAtto(attoAmmTipoAtto);
		//

		spesa.setTipoModificaMovimentoGestione(mmgs.getTipoModificaMovimentoGestione());
		String descrizione = null;

		if (mmgs.getDescrizioneModificaMovimentoGestione() != null && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(mmgs.getDescrizioneModificaMovimentoGestione());
		} else {
			descrizione = "";
		}
		spesa.setDescrizioneModificaMovimentoGestione(descrizione);
		spesa.setDescrizione(descrizione);
		
		//SIAC-7502 non verrà trovato il provvedimento se non commento questo
//		spesa.setIdStrutturaAmministrativa(model.getAccertamentoInAggiornamento().getIdStrutturaAmministrativa());
//		spesa.getAttoAmministrativo().setStrutturaAmmContabile(
//				model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile());
//		spesa.getAttoAmministrativo().setUid(model.getAccertamentoInAggiornamento().getAttoAmministrativo().getUid());
		if (mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(mmgs.getAnnoReimputazione());
		}
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);

		return spesa;
	}
	
	private List<ModificaMovimentoGestioneEntrata> costruisciListaMovimentiSpesaPropaga(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2) {
		List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
		BigDecimal importoZero = new BigDecimal("0.00");
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneEntrata mmgs = listaModificheRor2.get(i);
			if (!mmgs.getImportoOld().equals(importoZero)
					&& !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
				modificheList.add(singolaSpesaAggiornaPropaga(mmgs));
			}
			
			if(mmgs.getTipoModificaMovimentoGestione().equals("RORM") && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")){
				modificheList.add(singolaSpesaAggiornaPropaga(mmgs));
			}

		}

		return modificheList;
	}
	
	//SIAC-6929 
		protected List<Errore> controlloGestisciAccertamentoDecentratoPerModifica(List<Errore> listaErrori) {

			return controlloGestisciAccertamentoDecentratoPerModifica(listaErrori, false);
		}
		
		//SIAC-6929 - aggiunto flag per messaggio corretto da Aggiorna o Inserisci
		protected List<Errore> controlloGestisciAccertamentoDecentratoPerModifica(List<Errore> listaErrori,
				boolean aggiornamento) {

			String messaggioErrore = "Modifica Accertamento Decentrato";
			if (aggiornamento)
				messaggioErrore = "Aggiornamento Accertamento Decentrato";// SIAC-6929

			// Controllo gestisci decentrato: il provveddimento deve essere
			// provvisorio
			if (isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentratoP)
					&& (!MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato())
							|| model.getStep1Model().getProvvedimento().isParereRegolaritaContabile())) {

				listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore(messaggioErrore, "Provvisorio"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			return listaErrori;
		}
		
		
		@SuppressWarnings("unused")
		private List<Errore> checkImportoSubAccertamentoConModificaAccertamento(List<Errore> listaErrori, ModificaMovimentoGestioneEntrata spesa) {
			
			
			
			BigDecimal importoDiInput = spesa.getImportoOld();

			BigDecimal importoAttualeSub = new BigDecimal(0);

			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(getSubSelected().equals(String.valueOf(sub.getNumeroBigDecimal()))){
					importoAttualeSub = sub.getImportoAttuale();
					setImportoAttualeSubImpegno(convertiBigDecimalToImporto(importoAttualeSub));
					model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(importoAttualeSub));
					
					int minoreImpegno = importoDiInput.compareTo(minImportoImpegnoApp);
					int maggioreImpegno = 	importoDiInput.compareTo(maxImportoImpegnoApp);
					
					if(minoreImpegno == -1 ){
						listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
						model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
						break;
					}
					if(maggioreImpegno == 1){
						listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
						model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
						break;
					}
				}
				


				//Controllo range ok sub impegno
				int minoreSubImpegno = importoDiInput.compareTo(minImportoImpegnoApp);
				int maggioreSubImpegno = importoDiInput.compareTo(maxImportoImpegnoApp);
				
				boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
				
				boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) == 0;
				
				if(isRORDaMantenere && !isImportoZero){
					listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				}
				
				if(!isRORDaMantenere && (minoreSubImpegno == -1 || importoDiInput.compareTo(BigDecimal.ZERO)==0)){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				}
				if(!isRORDaMantenere && maggioreSubImpegno == 1){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				}

			}	
			

			return listaErrori;
		}

	
	
	
	
	public String annulla(){
		
		return SUCCESS_SALVA;
		
	}
	
	private void ripristinaDatiNelModelPerSalvataggioConErrori(GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva,
			GestisciImpegnoStep1Model step1ModelPrimaDiSalva,Accertamento impegnoInAggiornamentoPrimaDiSalva,List<SubAccertamento> listaSubPrimaDiSalva, SubAccertamento subAccPrimaDiSalva){
		//setto nel model i backup che mi sono fatto prima di invocare il servizio:
		model.setMovimentoSpesaModel(movimentoSpesaModelPrimaDiSalva);
		model.setStep1Model(step1ModelPrimaDiSalva);
		model.setAccertamentoInAggiornamento(impegnoInAggiornamentoPrimaDiSalva);
		model.setListaSubaccertamenti(listaSubPrimaDiSalva);
		model.setSubAccertamentoInAggiornamento(subAccPrimaDiSalva);
	}
	
	@SuppressWarnings("unused")
	private List<Errore> controlloImportoSubaccertamento(List<Errore> listaErrori, ModificaMovimentoGestioneEntrata spesa) {
		
		
					
		for(SubAccertamento sub : model.getListaSubaccertamenti()){
		
			if(getSubSelected().equals(String.valueOf(sub.getNumeroBigDecimal()))){
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				setNumeroSubAccertamento(String.valueOf(sub.getNumeroBigDecimal()));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumeroBigDecimal()));
				

				
				BigDecimal importoAttualeSubImpegno = sub.getImportoAttuale();
				minImportoSubApp = new BigDecimal(0).subtract(importoAttualeSubImpegno);
			
				if(sub.getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else if(sub.isFlagDaRiaccertamento()){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else{
					maxImportoSubApp =model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare();
				}
				
				if(minImportoSubApp == null){
					minImportoSubApp = new BigDecimal(0);
				}
			
				if(maxImportoSubApp == null){
				maxImportoSubApp = new BigDecimal(0);
				}
			}
		}
		
		if(minImportoSubApp == null){
			minImportoSubApp = new BigDecimal(0);
		}
		
		BigDecimal importoDiInput = spesa.getImportoOld();
		
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(spesa.getTipoModificaMovimentoGestione(), ""));
		
		int maxCompare = importoDiInput.compareTo(maxImportoSubApp);
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) ==0;
		
		if(isRORDaMantenere && !isImportoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		if(!isRORDaMantenere && importoDiInput.compareTo(minImportoSubApp) < 0){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		if(!isRORDaMantenere && (maxCompare == 1 || isImportoZero)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		return listaErrori;
	}
	
	public boolean oggettoDaPopolareImpegno(){
		return false;
	}
	
	//FOrse da utilizzare. lascio commento
//	private List<Errore> controlloImporto(List<Errore> listaErrori, ModificaMovimentoGestioneEntrata spesa) {
//		//importo obbligatorio
//		
//		
//		BigDecimal importoDiInput = spesa.getImportoOld();
//		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
//		
//		boolean importoMinoreDelMinimoAmmissibile = importoDiInput.compareTo(minImportoImpegnoApp)<0;
//		boolean importoZero = importoDiInput.compareTo(BigDecimal.ZERO)==0;
//		
//		if(isRORDaMantenere && !importoZero){
//			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
//			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
//		}
//
//		if(!isRORDaMantenere && (importoMinoreDelMinimoAmmissibile|| importoZero)){
//			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
//			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
//		}
//		
//		//SIAC-6586
//		
//
//		//Jira SIAC-3438
//		if(importoDiInput.compareTo(maxImportoImpegnoApp) >0 ){ 
//			Errore erroreMax = ErroreFin.RANGE_NON_VALIDO.getErrore("massimo");		
//			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
//			listaErrori.add(erroreMax);
//
//					
//		}
//		
//		return listaErrori;
//		
//		
//		
//	}

	/**
	 * @return the uidMovgest
	 */
	public Integer getUidMovgest() {
		return uidMovgest;
	}

	/**
	 * @param uidMovgest the uidMovgest to set
	 */
	public void setUidMovgest(Integer uidMovgest) {
		this.uidMovgest = uidMovgest;
	}
	
	private List<Errore> checkProvvedimentoInAggiorna(List<Errore> erroriSalvataggio, Integer uidAtto){
		RicercaAtti ricercaAtti = new RicercaAtti();
		RicercaProvvedimento ricProvvReq = new RicercaProvvedimento();
		ricProvvReq.setEnte(model.getEnte());
		ricercaAtti.setUid(uidAtto);
		ricProvvReq.setRicercaAtti(ricercaAtti);
		ricProvvReq.setRichiedente(sessionHandler.getRichiedente());
		RicercaProvvedimentoResponse resp = provvedimentoService.ricercaProvvedimento(ricProvvReq);
		if(resp.isFallimento()){
			Errore err = new Errore();
			err.setCodice(ErroreCore.ERRORE_DI_SISTEMA.getErrore("").getCodice());
			err.setDescrizione(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nella ricerca del provvedimento associato").getDescrizione());
			erroriSalvataggio.add(err);
			return erroriSalvataggio;
		}
		
		if(resp.getListaAttiAmministrativi()!=null && !resp.getListaAttiAmministrativi().isEmpty()){
			AttoAmministrativo atto = resp.getListaAttiAmministrativi().get(0);
			if(atto.getBloccoRagioneria() != null && atto.getBloccoRagioneria()==true){
				Errore err = new Errore();
				err.setCodice(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(atto.getOggetto()).getCodice());
				err.setDescrizione(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(atto.getOggetto()).getDescrizione());
				erroriSalvataggio.add(err);
				return erroriSalvataggio;
			}
			
		}
		
		return erroriSalvataggio;
	
	}

	//SIAC-7349 Inizio  SR190 FL 15/04/2020
	/**
	 * @return the reimputazioneAnnualita
	 */
	public GestioneCruscottoModel getReimputazioneAnnualita() {
		return reimputazioneAnnualita;
	}

	/**
	 * @param reimputazioneAnnualita the reimputazioneAnnualita to set
	 */
	public void setReimputazioneAnnualita(GestioneCruscottoModel reimputazioneAnnualita) {
		this.reimputazioneAnnualita = reimputazioneAnnualita;
	}
	//SIAC-7349 Fine SR190 FL 15/04/2020
	//SIAC-8467
	private AnnullaAggiornaMovimento creaAnnullaAggiornaMovimentoRequestBase() {
		AnnullaAggiornaMovimento req = new AnnullaAggiornaMovimento();
		req.setAnnoBilancio(sessionHandler.getAnnoBilancio());
		req.setDataOra(new Date());
		req.setRichiedente(sessionHandler.getRichiedente());
		
		//SIAC-8675
		req.setNumeroSub(numeroSubAccertamento);
		req.setAnnoMovimento(model.getAccertamentoInAggiornamento().getAnnoMovimento());
		req.setNumeroMovimento(model.getAccertamentoInAggiornamento().getNumeroBigDecimal());
		req.setIdMovimento(model.getAccertamentoInAggiornamento().getUid());
		req.setTipoMovimento(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
		req.setEnte(sessionHandler.getAccount().getEnte());
		return req;
	}

}
