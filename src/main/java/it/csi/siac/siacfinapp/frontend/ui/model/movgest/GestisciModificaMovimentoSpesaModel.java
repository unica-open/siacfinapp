/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GestisciModificaMovimentoSpesaModel extends GenericFinModel {
	
	private static final long serialVersionUID = 8121434197925895303L;

	//listaTipoMotivo
	private List<CodificaFin> listaTipoMotivo = new ArrayList<CodificaFin>();
	//SIAC-7838
	private List<CodificaFin> listaTipoMotivoAggiudicazione = new ArrayList<CodificaFin>();
	
	//Per inserimento modifica importo
	private BigDecimal minAncheImpegno;
	private BigDecimal maxAncheImpegno;
	
	//PER IMPEGNI PLURIENNALI:
	
	//lista impegni pluriennali
	private List<ImpegniPluriennaliModel> listaImpegniPluriennali = new ArrayList<ImpegniPluriennaliModel>();
	
	//numero pluriennali
	private  Integer numeroPluriennali;
	
	//anno impegno
	private Integer annoImpegno;
	
	//importo tot formatted
	private String importoTotFormatted;
	
	//importo tot
	private BigDecimal importoTot;
	
	//modifica movimento gestione entrata
	private ModificaMovimentoGestioneEntrata entrata = new ModificaMovimentoGestioneEntrata();
	
	//modifica movimento gestione spesa
	private ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
	
	//importo pluriennale tot
	private BigDecimal importoPluriennaleTot;
	
	//sub impegno spesa
	private List<SubImpegno> subImpegnoSpesa = new ArrayList<SubImpegno>();
	
	//modifica impegno sub impegno
	private Boolean modificaImpegnoSubimpegno=false;
	
	//
	
	//Per Aggiornamento Modifica
	private ModificaMovimentoGestioneSpesa modificaAggiornamento = new ModificaMovimentoGestioneSpesa();
	private String descrizioneOrigine;
	private String modificaPadre;
	
	//Per Aggiornamento Modifica Accertamento
	private ModificaMovimentoGestioneEntrata modificaAggiornamentoAcc = new ModificaMovimentoGestioneEntrata();
	
	//Mappatura oggetto modificaMovimentoGestioneSpesa
	private Accertamento accertamento;
	private Impegno impegno;
	private SubImpegno subImpegno;
	private Accertamento subAccertamento;
	private AttoAmministrativo attoAmministrativo;
	private ClassificatoreGenerico motivoModificaSpesa;
	private List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
	private List<ModificaMovimentoGestioneEntrata> listaModificheEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();
	
	
	//MODIFICHE RELATIVE ALLA SIAC-7349
	private List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
	
	
	
	//Mappatura oggetto modificaMovimentoGestione
	private int idModificaMovimentoGestione;
	private int numeroModificaMovimentoGestione;
	private String descrizioneModificaMovimentoGestione;
	private Date dataModificaMovimentoGestione;
	private Date dataEmissione;
	private Date dataAnnullamento;
	
	//importo old e new:
	private BigDecimal importoOld;
	private BigDecimal importoNew;
	
	//user:
	private String utenteCreazione;
	private String utenteModifica;
	private String utenteCancellazione;
	
	//new e old movimento gestione:
	private Soggetto soggettoNewMovimentoGestione;
	private ClasseSoggetto classeSoggettoNewMovimentoGestione;
	private Soggetto soggettoOldMovimentoGestione;
	private ClasseSoggetto classeSoggettoOldMovimentoGestione;
	
	//new e old soggetto code:
	private String newSoggettoCodeMovimentoGestione;
	private int idClasseSoggettoNewMovimentoGestione;
	private String oldSoggettoCodeMovimentoGestione;
	private int idClasseSoggettoOldMovimentoGestione;
	
	//atto amministrativo:
	private String attoAmministrativoAnno;
	private Integer attoAmministrativoNumero;
	private String attoAmministrativoTipoCode;
	
	//stato operativo modifica movimentogestione:
	private StatoOperativoModificaMovimentoGestione statoOperativoModificaMovimentoGestione;

	//flag da modifica spesa:
	private boolean fromModificaSpesa = false;
	
	//reimputazione:
	private List<String> daReimputazione = new ArrayList<String>();
	private String reimputazione;
	private Integer annoReimputazione;
	
	//SIAC-6997
	private List<String> daElaboratoRor = new ArrayList<String>();
	private String elaboratoRor;
	
	//SIAC-6865
	private Boolean aggiudicazione;
	private SoggettoImpegnoModel soggettoAggiudicazioneModel;
	private boolean abilitaGestioneAggiudicazione;
	//SIAC-7838
	private Boolean flagAggiudicazioneSenzaSoggetto;
	
	//SIAC-8184
	private String nuovaDescrizioneEventualeImpegnoAggiudicazione;
	
	//SIAC-8811
	private String EventualeNuovoCUPImpegno;
	
	//GETTER E SETTER:
	
	public List<String> getDaElaboratoRor() {
		return daElaboratoRor;
	}

	public void setDaElaboratoRor(List<String> daElaboratoRor) {
		this.daElaboratoRor = daElaboratoRor;
	}

	public String getElaboratoRor() {
		return elaboratoRor;
	}

	public void setElaboratoRor(String elaboratoRor) {
		this.elaboratoRor = elaboratoRor;
	}

	public Impegno getImpegno() {
		return impegno;
	}
	
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	public ClassificatoreGenerico getMotivoModificaSpesa() {
		return motivoModificaSpesa;
	}

	public void setMotivoModificaSpesa(ClassificatoreGenerico motivoModificaSpesa) {
		this.motivoModificaSpesa = motivoModificaSpesa;
	}

	public int getIdModificaMovimentoGestione() {
		return idModificaMovimentoGestione;
	}

	public void setIdModificaMovimentoGestione(int idModificaMovimentoGestione) {
		this.idModificaMovimentoGestione = idModificaMovimentoGestione;
	}

	public int getNumeroModificaMovimentoGestione() {
		return numeroModificaMovimentoGestione;
	}

	public void setNumeroModificaMovimentoGestione(int numeroModificaMovimentoGestione) {
		this.numeroModificaMovimentoGestione = numeroModificaMovimentoGestione;
	}

	public String getDescrizioneModificaMovimentoGestione() {
		return descrizioneModificaMovimentoGestione;
	}

	public void setDescrizioneModificaMovimentoGestione(String descrizioneModificaMovimentoGestione) {
		this.descrizioneModificaMovimentoGestione = descrizioneModificaMovimentoGestione;
	}

	public Date getDataModificaMovimentoGestione() {
		return dataModificaMovimentoGestione;
	}

	public void setDataModificaMovimentoGestione(Date dataModificaMovimentoGestione) {
		this.dataModificaMovimentoGestione = dataModificaMovimentoGestione;
	}

	public Date getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	public BigDecimal getImportoOld() {
		return importoOld;
	}

	public void setImportoOld(BigDecimal importoOld) {
		this.importoOld = importoOld;
	}

	public BigDecimal getImportoNew() {
		return importoNew;
	}

	public void setImportoNew(BigDecimal importoNew) {
		this.importoNew = importoNew;
	}

	public String getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public String getUtenteModifica() {
		return utenteModifica;
	}

	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}

	public String getUtenteCancellazione() {
		return utenteCancellazione;
	}

	public void setUtenteCancellazione(String utenteCancellazione) {
		this.utenteCancellazione = utenteCancellazione;
	}

	public Soggetto getSoggettoNewMovimentoGestione() {
		return soggettoNewMovimentoGestione;
	}

	public void setSoggettoNewMovimentoGestione(Soggetto soggettoNewMovimentoGestione) {
		this.soggettoNewMovimentoGestione = soggettoNewMovimentoGestione;
	}

	public ClasseSoggetto getClasseSoggettoNewMovimentoGestione() {
		return classeSoggettoNewMovimentoGestione;
	}

	public void setClasseSoggettoNewMovimentoGestione(ClasseSoggetto classeSoggettoNewMovimentoGestione) {
		this.classeSoggettoNewMovimentoGestione = classeSoggettoNewMovimentoGestione;
	}

	public Soggetto getSoggettoOldMovimentoGestione() {
		return soggettoOldMovimentoGestione;
	}

	public void setSoggettoOldMovimentoGestione(Soggetto soggettoOldMovimentoGestione) {
		this.soggettoOldMovimentoGestione = soggettoOldMovimentoGestione;
	}

	public ClasseSoggetto getClasseSoggettoOldMovimentoGestione() {
		return classeSoggettoOldMovimentoGestione;
	}

	public void setClasseSoggettoOldMovimentoGestione(ClasseSoggetto classeSoggettoOldMovimentoGestione) {
		this.classeSoggettoOldMovimentoGestione = classeSoggettoOldMovimentoGestione;
	}

	public String getNewSoggettoCodeMovimentoGestione() {
		return newSoggettoCodeMovimentoGestione;
	}

	public void setNewSoggettoCodeMovimentoGestione(String newSoggettoCodeMovimentoGestione) {
		this.newSoggettoCodeMovimentoGestione = newSoggettoCodeMovimentoGestione;
	}

	public int getIdClasseSoggettoNewMovimentoGestione() {
		return idClasseSoggettoNewMovimentoGestione;
	}

	public void setIdClasseSoggettoNewMovimentoGestione(int idClasseSoggettoNewMovimentoGestione) {
		this.idClasseSoggettoNewMovimentoGestione = idClasseSoggettoNewMovimentoGestione;
	}

	public String getOldSoggettoCodeMovimentoGestione() {
		return oldSoggettoCodeMovimentoGestione;
	}

	public void setOldSoggettoCodeMovimentoGestione(String oldSoggettoCodeMovimentoGestione) {
		this.oldSoggettoCodeMovimentoGestione = oldSoggettoCodeMovimentoGestione;
	}

	public int getIdClasseSoggettoOldMovimentoGestione() {
		return idClasseSoggettoOldMovimentoGestione;
	}

	public void setIdClasseSoggettoOldMovimentoGestione(int idClasseSoggettoOldMovimentoGestione) {
		this.idClasseSoggettoOldMovimentoGestione = idClasseSoggettoOldMovimentoGestione;
	}

	public String getAttoAmministrativoAnno() {
		return attoAmministrativoAnno;
	}

	public void setAttoAmministrativoAnno(String attoAmministrativoAnno) {
		this.attoAmministrativoAnno = attoAmministrativoAnno;
	}

	public Integer getAttoAmministrativoNumero() {
		return attoAmministrativoNumero;
	}

	public void setAttoAmministrativoNumero(Integer attoAmministrativoNumero) {
		this.attoAmministrativoNumero = attoAmministrativoNumero;
	}

	public String getAttoAmministrativoTipoCode() {
		return attoAmministrativoTipoCode;
	}

	public void setAttoAmministrativoTipoCode(String attoAmministrativoTipoCode) {
		this.attoAmministrativoTipoCode = attoAmministrativoTipoCode;
	}

	public StatoOperativoModificaMovimentoGestione getStatoOperativoModificaMovimentoGestione() {
		return statoOperativoModificaMovimentoGestione;
	}

	public void setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione statoOperativoModificaMovimentoGestione) {
		this.statoOperativoModificaMovimentoGestione = statoOperativoModificaMovimentoGestione;
	}

	public List<ModificaMovimentoGestioneSpesa> getListaModifiche() {
		return listaModifiche;
	}

	public void setListaModifiche(List<ModificaMovimentoGestioneSpesa> listaModifiche) {
		this.listaModifiche = listaModifiche;
	}

	public List<ImpegniPluriennaliModel> getListaImpegniPluriennali() {
		return listaImpegniPluriennali;
	}

	public void setListaImpegniPluriennali(List<ImpegniPluriennaliModel> listaImpegniPluriennali) {
		this.listaImpegniPluriennali = listaImpegniPluriennali;
	}

	public Integer getNumeroPluriennali() {
		return numeroPluriennali;
	}

	public void setNumeroPluriennali(Integer numeroPluriennali) {
		this.numeroPluriennali = numeroPluriennali;
	}

	public Integer getAnnoImpegno() {
		return annoImpegno;
	}

	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}

	public String getImportoTotFormatted() {
		return importoTotFormatted;
	}

	public void setImportoTotFormatted(String importoTotFormatted) {
		this.importoTotFormatted = importoTotFormatted;
	}

	public BigDecimal getImportoTot() {
		return importoTot;
	}

	public void setImportoTot(BigDecimal importoTot) {
		this.importoTot = importoTot;
	}

	public boolean isFromModificaSpesa() {
		return fromModificaSpesa;
	}

	public void setFromModificaSpesa(boolean fromModificaSpesa) {
		this.fromModificaSpesa = fromModificaSpesa;
	}

	public ModificaMovimentoGestioneSpesa getSpesa() {
		return spesa;
	}

	public void setSpesa(ModificaMovimentoGestioneSpesa spesa) {
		this.spesa = spesa;
	}

	public BigDecimal getImportoPluriennaleTot() {
		return importoPluriennaleTot;
	}

	public void setImportoPluriennaleTot(BigDecimal importoPluriennaleTot) {
		this.importoPluriennaleTot = importoPluriennaleTot;
	}

	public List<SubImpegno> getSubImpegnoSpesa() {
		return subImpegnoSpesa;
	}

	public void setSubImpegnoSpesa(List<SubImpegno> subImpegnoSpesa) {
		this.subImpegnoSpesa = subImpegnoSpesa;
	}

	public List<CodificaFin> getListaTipoMotivo() {
		return listaTipoMotivo;
	}

	public void setListaTipoMotivo(List<CodificaFin> listaTipoMotivo) {
		this.listaTipoMotivo = listaTipoMotivo;
	}
	

	public List<CodificaFin> getListaTipoMotivoAggiudicazione() {
		return listaTipoMotivoAggiudicazione;
	}

	public void setListaTipoMotivoAggiudicazione(List<CodificaFin> listaTipoMotivoAggiudicazione) {
		this.listaTipoMotivoAggiudicazione = listaTipoMotivoAggiudicazione != null? listaTipoMotivoAggiudicazione : new ArrayList<CodificaFin>();
	}

	public ModificaMovimentoGestioneSpesa getModificaAggiornamento() {
		return modificaAggiornamento;
	}

	public void setModificaAggiornamento(ModificaMovimentoGestioneSpesa modificaAggiornamento) {
		this.modificaAggiornamento = modificaAggiornamento;
	}

	public String getDescrizioneOrigine() {
		return descrizioneOrigine;
	}

	public void setDescrizioneOrigine(String descrizioneOrigine) {
		this.descrizioneOrigine = descrizioneOrigine;
	}

	public String getModificaPadre() {
		return modificaPadre;
	}

	public void setModificaPadre(String modificaPadre) {
		this.modificaPadre = modificaPadre;
	}

	public BigDecimal getMinAncheImpegno() {
		return minAncheImpegno;
	}

	public void setMinAncheImpegno(BigDecimal minAncheImpegno) {
		this.minAncheImpegno = minAncheImpegno;
	}

	public BigDecimal getMaxAncheImpegno() {
		return maxAncheImpegno;
	}

	public void setMaxAncheImpegno(BigDecimal maxAncheImpegno) {
		this.maxAncheImpegno = maxAncheImpegno;
	}

	public Accertamento getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	public List<ModificaMovimentoGestioneEntrata> getListaModificheEntrata() {
		return listaModificheEntrata;
	}

	public void setListaModificheEntrata(List<ModificaMovimentoGestioneEntrata> listaModificheEntrata) {
		this.listaModificheEntrata = listaModificheEntrata;
	}

	public Accertamento getSubAccertamento() {
		return subAccertamento;
	}

	public void setSubAccertamento(Accertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

	public ModificaMovimentoGestioneEntrata getEntrata() {
		return entrata;
	}

	public void setEntrata(ModificaMovimentoGestioneEntrata entrata) {
		this.entrata = entrata;
	}

	public ModificaMovimentoGestioneEntrata getModificaAggiornamentoAcc() {
		return modificaAggiornamentoAcc;
	}

	public void setModificaAggiornamentoAcc(ModificaMovimentoGestioneEntrata modificaAggiornamentoAcc) {
		this.modificaAggiornamentoAcc = modificaAggiornamentoAcc;
	}

	public Boolean getModificaImpegnoSubimpegno() {
		return modificaImpegnoSubimpegno;
	}

	public void setModificaImpegnoSubimpegno(Boolean modificaImpegnoSubimpegno) {
		this.modificaImpegnoSubimpegno = modificaImpegnoSubimpegno;
	}

	public List<String> getDaReimputazione() {
		return daReimputazione;
	}

	public void setDaReimputazione(List<String> daReimputazione) {
		this.daReimputazione = daReimputazione;
	}

	public String getReimputazione() {
		return reimputazione;
	}

	public void setReimputazione(String reimputazione) {
		this.reimputazione = reimputazione;
	}

	public Integer getAnnoReimputazione() {
		return annoReimputazione;
	}

	public void setAnnoReimputazione(Integer annoReimputazione) {
		this.annoReimputazione = annoReimputazione;
	}

	/**
	 * @return the listaModificheSpeseCollegata
	 */
	public List<ModificaMovimentoGestioneSpesaCollegata> getListaModificheSpeseCollegata() {
		return listaModificheSpeseCollegata;
	}

	/**
	 * @param listaModificheSpeseCollegata the listaModificheSpeseCollegata to set
	 */
	public void setListaModificheSpeseCollegata(
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheSpeseCollegata) {
		this.listaModificheSpeseCollegata = listaModificheSpeseCollegata;
	}

	/**
	 * @return the aggiudicazione
	 */
	public Boolean getAggiudicazione() {
		return aggiudicazione;
	}

	/**
	 * @param aggiudicazione the aggiudicazione to set
	 */
	public void setAggiudicazione(Boolean aggiudicazione) {
		this.aggiudicazione = aggiudicazione;
	}

	/**
	 * @return the soggettoAggiudicazioneModel
	 */
	public SoggettoImpegnoModel getSoggettoAggiudicazioneModel() {
		return soggettoAggiudicazioneModel;
	}

	/**
	 * @param soggettoAggiudicazioneModel the soggettoAggiudicazioneModel to set
	 */
	public void setSoggettoAggiudicazioneModel(SoggettoImpegnoModel soggettoAggiudicazioneModel) {
		this.soggettoAggiudicazioneModel = soggettoAggiudicazioneModel;
	}
	
	public String getIntestazioneSoggettoAggiudicazioneSelezionato() {
		StringBuilder intestazione = new StringBuilder().append("Soggetto ");
		
		if(soggettoAggiudicazioneModel == null || (StringUtils.isEmpty(soggettoAggiudicazioneModel.getCodCreditore()) && StringUtils.isEmpty(soggettoAggiudicazioneModel.getClasse()) )) {
			return intestazione.toString();
		}
		intestazione.append(StringUtils.defaultIfEmpty(soggettoAggiudicazioneModel.getCodCreditore(), " "))
			.append(" - ")
			.append(StringUtils.defaultIfEmpty(soggettoAggiudicazioneModel.getDenominazione(), " "));
		if(!StringUtils.isEmpty(soggettoAggiudicazioneModel.getClasse())) {
			intestazione.append(" n.d. - Classe ")
				.append(soggettoAggiudicazioneModel.getClasse());
		}
		
		return intestazione.toString();
	}

	/**
	 * @return the abilitaGestioneAggiudicazione
	 */
	public boolean isAbilitaGestioneAggiudicazione() {
		return abilitaGestioneAggiudicazione;
	}

	/**
	 * @param abilitaGestioneAggiudicazione the abilitaGestioneAggiudicazione to set
	 */
	public void setAbilitaGestioneAggiudicazione(boolean abilitaGestioneAggiudicazione) {
		this.abilitaGestioneAggiudicazione = abilitaGestioneAggiudicazione;
	}

	public Boolean getFlagAggiudicazioneSenzaSoggetto() {
		return flagAggiudicazioneSenzaSoggetto;
	}

	public void setFlagAggiudicazioneSenzaSoggetto(Boolean flagAggiudicazioneSenzaSoggetto) {
		this.flagAggiudicazioneSenzaSoggetto = flagAggiudicazioneSenzaSoggetto;
	}
	
	public String getEventualeNuovoCUPImpegno() {
		return EventualeNuovoCUPImpegno;
	}

	public void setEventualeNuovoCUPImpegno(String eventualeNuovoCUPImpegno) {
		EventualeNuovoCUPImpegno = eventualeNuovoCUPImpegno;
	}

	public String getNuovaDescrizioneEventualeImpegnoAggiudicazione() {
		return nuovaDescrizioneEventualeImpegnoAggiudicazione;
	}

	public void setNuovaDescrizioneEventualeImpegnoAggiudicazione(String nuovaDescrizioneEventualeImpegnoAggiudicazione) {
		this.nuovaDescrizioneEventualeImpegnoAggiudicazione = nuovaDescrizioneEventualeImpegnoAggiudicazione;
	}
	
}
