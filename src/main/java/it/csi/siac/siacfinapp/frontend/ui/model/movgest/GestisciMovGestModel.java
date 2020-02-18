/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.MovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class GestisciMovGestModel extends MovGestModel {

	private static final long serialVersionUID = 1L;
	
	//Model di cache per i tab di navigazione
	private GestisciImpegnoStep1Model step1ModelCache = new GestisciImpegnoStep1Model();
	private GestoreTransazioneElementareModel transazioneElementareCache = new GestoreTransazioneElementareModel();
	private GestoreTransazioneElementareModel transazioneElementareSubMovimentoCache = new GestoreTransazioneElementareModel();
	private GestisciImpegnoStep3Model step3ModelCache = new GestisciImpegnoStep3Model();
	private GestisciImpegnoStep1Model step1ModelSubimpegnoCache = new GestisciImpegnoStep1Model();
	private GestisciImpegnoStep2Model step2ModelSubimpegnoCache = new GestisciImpegnoStep2Model();
	private GestisciModificaMovimentoSpesaModel movimentoSpesaModelCache = new GestisciModificaMovimentoSpesaModel();
	//end blocco

	//model step 1
	private GestisciImpegnoStep1Model step1Model = new GestisciImpegnoStep1Model();
	
	//model step 3
	private GestisciImpegnoStep3Model step3Model = new GestisciImpegnoStep3Model();
	
	//Subimpegno per aggiornamento
	private GestisciImpegnoStep1Model step1ModelSubimpegno = new GestisciImpegnoStep1Model();
	private RicercaImpegnoModel ricercaModel = new RicercaImpegnoModel();
	private List<SubImpegno> listaSubimpegni = new ArrayList<SubImpegno>();
	private List<SubImpegno> listaTuttiSubimpegniDatiMinimi = new ArrayList<SubImpegno>();
	private List<SubAccertamento> listaSubaccertamenti = new ArrayList<SubAccertamento>();
	private List<AttoAmministrativoStoricizzato> listaModificheProvvedimento = new ArrayList<AttoAmministrativoStoricizzato>();
	private MovimentoConsulta subDettaglio = new MovimentoConsulta();
	
	//movimento spesa model
	private GestisciModificaMovimentoSpesaModel movimentoSpesaModel = new GestisciModificaMovimentoSpesaModel();
	
	//impegno in aggiornamento
	private Impegno impegnoInAggiornamento = new Impegno();
	
	//accertamento in aggiornamento
	private Accertamento accertamentoInAggiornamento = new Accertamento();
	
	//consultazione di un modifica
	private ModificaConsulta modificaDettaglio = new ModificaConsulta();
	
	//per indietro
	private Boolean flagIndietro;
	
	private Boolean subImpegnoSelectedMod=false;
	private String numeroSubImpegnoMod;
	private String importoAttualeSubImpegnoMod;
	
	//gestione max e min su modfiche importo:
	private String minImportoCalcolatoMod,maxImportoCalcolatoMod,minImportoSubCalcolatoMod,maxImportoSubCalcolatoMod,minImpMod,maxImpMod,minAncheMod,maxAncheMod,minSubMod,maxSubMod;
	
	//flag se superiore ai tre anni:
	private boolean flagSuperioreTreAnni;
	
	//operazione aggiornamento:
	private boolean operazioneAggiorna;
	
	//Impegno utilizzato per evitare le "Mega Query"
	private Impegno impegnoRicaricatoDopoInsOAgg;
	
	//Accertamento utilizzato per evitare le "Mega Query"
	private Accertamento accertamentoRicaricatoDopoInsOAgg;
	
	// usati per i vincoli
	private BigDecimal disponibilitaLiquidare; 
	private BigDecimal sommaLiquidazioneDoc;
    private BigDecimal disponibilitaImpegnoModifica;
    
    //SOGGETTI IN GESTIONE MODIFICHE SOGGETTO:
    private Soggetto soggettoImpegnoAttuale;		
  	private Soggetto soggettoSubAttuale;
	
	
	private int resultSize;
	
	//variabili temporanee per il controllo dell'importo in modifica:
	private BigDecimal impMod;
	private boolean abbchckd;
	private String cmptnz;
	//
	
	//LISTE TEMPORANEE DI CACHE
	List<CapitoloUscitaGestione> capitoliUscita = new ArrayList<CapitoloUscitaGestione>();
	List<CapitoloEntrataGestione> capitoliEntrata = new ArrayList<CapitoloEntrataGestione>();
	List<AttoAmministrativo> atti = new ArrayList<AttoAmministrativo>();
	//end blocco
	
	private BigDecimal disponibilitaSubImpegnare, totaleSubImpegno;
	
    // GESTIONE DINAMICA
    private Map<String, String> labels = new HashMap<String, String>();
    //end blocco
    

    public boolean proseguiConWarningPianoDeiConti;
    
    public boolean proseguiConWarning;

    public boolean proseguiConWarningSacDelProvvPerDecentrato;

    public boolean proseguiConWarningControlloSoggetti;

    public boolean proseguiConWarningModificaPositivaAccertamento;
    
    private boolean visualizzaWarningImpegnoNonTotVincolato;
    
    private boolean gestioneModificaDecentratoEFaseROR;
    
    //SIAC-5333
    private boolean richiediConfermaRedirezioneContabilitaGenerale = false;
    private boolean saltaInserimentoPrimaNota = false;
    private int uidDaCompletare;
    private String messaggioProsecuzioneSuContabilitaGenerale = "L'operazione potrebbe portare all'inserimento di una scrittura su contabilit&agrave; generale. Nel caso in cui venga creata, &egrave; possibile validare la prima nota ora o successivamente";
    
    //SIAC-5943
    //aggiungo questo campo, perche' lo stato dei subimpegni viene impostato subito quando viene caricato il nuovo provvedimento (gestione centralizzata rischiosa da toccare) e poi anche dopo. ho bisogno di sapere, pero', se ci sia stato un passaggio da stato provvisorio a definitivo!!!!
    private String statoMovimentoGestioneOld;
    
    
	private List<Cronoprogramma> listaRicercaCronop;
	
	private Boolean conLegameStoricizzato;
	
	//SIAC-6990
	private AggiornaImpegno impegnoRequestStep1;

    
    //GETTE E SETTER:
    
    /**
	 * @return the proseguiConWarning
	 */
	public boolean isProseguiConWarning() {
		return proseguiConWarning;
	}

	/**
	 * @param proseguiConWarning the proseguiConWarning to set
	 */
	public void setProseguiConWarning(boolean proseguiConWarning) {
		this.proseguiConWarning = proseguiConWarning;
	}
	
    public boolean isProseguiConWarningControlloSoggetti() {
		return proseguiConWarningControlloSoggetti;
	}

	public void setProseguiConWarningControlloSoggetti(boolean proseguiConWarningControlloSoggetti) {
		this.proseguiConWarningControlloSoggetti = proseguiConWarningControlloSoggetti;
	}

	public Accertamento getAccertamentoRicaricatoDopoInsOAgg() {
		return accertamentoRicaricatoDopoInsOAgg;
	}
	public void setAccertamentoRicaricatoDopoInsOAgg(Accertamento accertamentoRicaricatoDopoInsOAgg) {
		this.accertamentoRicaricatoDopoInsOAgg = accertamentoRicaricatoDopoInsOAgg;
	}
	public Impegno getImpegnoRicaricatoDopoInsOAgg() {
		return impegnoRicaricatoDopoInsOAgg;
	}
	public void setImpegnoRicaricatoDopoInsOAgg(Impegno impegnoRicaricatoDopoInsOAgg) {
		this.impegnoRicaricatoDopoInsOAgg = impegnoRicaricatoDopoInsOAgg;
	}
    
	public GestisciImpegnoStep1Model getStep1Model() {
		return step1Model;
	}
	public void setStep1Model(GestisciImpegnoStep1Model step1Model) {
		this.step1Model = step1Model;
	}
	public GestisciImpegnoStep3Model getStep3Model() {
		return step3Model;
	}
	public void setStep3Model(GestisciImpegnoStep3Model step3Model) {
		this.step3Model = step3Model;
	}
	public RicercaImpegnoModel getRicercaModel() {
		return ricercaModel;
	}
	public void setRicercaModel(RicercaImpegnoModel ricercaModel) {
		this.ricercaModel = ricercaModel;
	}
	public List<SubImpegno> getListaSubimpegni() {
		return listaSubimpegni;
	}
	public void setListaSubimpegni(List<SubImpegno> listaSubimpegni) {
		this.listaSubimpegni = listaSubimpegni;
	}
	public MovimentoConsulta getSubDettaglio() {
		return subDettaglio;
	}
	public void setSubDettaglio(MovimentoConsulta subDettaglio) {
		this.subDettaglio = subDettaglio;
	}
	public GestisciImpegnoStep1Model getStep1ModelSubimpegno() {
		return step1ModelSubimpegno;
	}
	public void setStep1ModelSubimpegno(
			GestisciImpegnoStep1Model step1ModelSubimpegno) {
		this.step1ModelSubimpegno = step1ModelSubimpegno;
	}
	public GestisciModificaMovimentoSpesaModel getMovimentoSpesaModel() {
		return movimentoSpesaModel;
	}
	public void setMovimentoSpesaModel(GestisciModificaMovimentoSpesaModel movimentoSpesaModel) {
		this.movimentoSpesaModel = movimentoSpesaModel;
	}
	public BigDecimal getDisponibilitaSubImpegnare() {
		return disponibilitaSubImpegnare;
	}
	public void setDisponibilitaSubImpegnare(BigDecimal disponibilitaSubImpegnare) {
		this.disponibilitaSubImpegnare = disponibilitaSubImpegnare;
	}
	public BigDecimal getTotaleSubImpegno() {
		return totaleSubImpegno;
	}
	public void setTotaleSubImpegno(BigDecimal totaleSubImpegno) {
		this.totaleSubImpegno = totaleSubImpegno;
	}
	public Impegno getImpegnoInAggiornamento() {
		return impegnoInAggiornamento;
	}
	public void setImpegnoInAggiornamento(Impegno impegnoInAggiornamento) {
		this.impegnoInAggiornamento = impegnoInAggiornamento;
	}
	public ModificaConsulta getModificaDettaglio() {
		return modificaDettaglio;
	}
	public void setModificaDettaglio(ModificaConsulta modificaDettaglio) {
		this.modificaDettaglio = modificaDettaglio;
	}
	public Map<String, String> getLabels() {
		return labels;
	}
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	public Accertamento getAccertamentoInAggiornamento() {
		return accertamentoInAggiornamento;
	}
	public void setAccertamentoInAggiornamento(Accertamento accertamentoInAggiornamento) {
		this.accertamentoInAggiornamento = accertamentoInAggiornamento;
	}
	public List<SubAccertamento> getListaSubaccertamenti() {
		return listaSubaccertamenti;
	}
	public void setListaSubaccertamenti(List<SubAccertamento> listaSubaccertamenti) {
		this.listaSubaccertamenti = listaSubaccertamenti;
	}
	public List<CapitoloUscitaGestione> getCapitoliUscita() {
		return capitoliUscita;
	}
	public void setCapitoliUscita(List<CapitoloUscitaGestione> capitoliUscita) {
		this.capitoliUscita = capitoliUscita;
	}
	public List<CapitoloEntrataGestione> getCapitoliEntrata() {
		return capitoliEntrata;
	}
	public void setCapitoliEntrata(List<CapitoloEntrataGestione> capitoliEntrata) {
		this.capitoliEntrata = capitoliEntrata;
	}
	public List<AttoAmministrativo> getAtti() {
		return atti;
	}
	public void setAtti(List<AttoAmministrativo> atti) {
		this.atti = atti;
	}
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	
	/**
	 * @return the conLegameStoricizzato
	 */
	public Boolean getConLegameStoricizzato() {
		return conLegameStoricizzato;
	}

	/**
	 * @param conLegameStoricizzato the conLegameStoricizzato to set
	 */
	public void setConLegameStoricizzato(Boolean conLegameStoricizzato) {
		this.conLegameStoricizzato = conLegameStoricizzato;
	}

	public GestisciImpegnoStep1Model getStep1ModelCache() {
		return step1ModelCache;
	}
	public void setStep1ModelCache(GestisciImpegnoStep1Model step1ModelCache) {
		this.step1ModelCache = step1ModelCache;
	}
	public GestoreTransazioneElementareModel getTransazioneElementareCache() {
		return transazioneElementareCache;
	}
	public void setTransazioneElementareCache(GestoreTransazioneElementareModel transazioneElementareCache) {
		this.transazioneElementareCache = transazioneElementareCache;
	}
	public GestisciImpegnoStep3Model getStep3ModelCache() {
		return step3ModelCache;
	}
	public void setStep3ModelCache(GestisciImpegnoStep3Model step3ModelCache) {
		this.step3ModelCache = step3ModelCache;
	}
	public GestisciImpegnoStep1Model getStep1ModelSubimpegnoCache() {
		return step1ModelSubimpegnoCache;
	}
	public void setStep1ModelSubimpegnoCache(GestisciImpegnoStep1Model step1ModelSubimpegnoCache) {
		this.step1ModelSubimpegnoCache = step1ModelSubimpegnoCache;
	}
	public GestisciImpegnoStep2Model getStep2ModelSubimpegnoCache() {
		return step2ModelSubimpegnoCache;
	}
	public void setStep2ModelSubimpegnoCache(GestisciImpegnoStep2Model step2ModelSubimpegnoCache) {
		this.step2ModelSubimpegnoCache = step2ModelSubimpegnoCache;
	}
	public GestisciModificaMovimentoSpesaModel getMovimentoSpesaModelCache() {
		return movimentoSpesaModelCache;
	}
	public void setMovimentoSpesaModelCache(GestisciModificaMovimentoSpesaModel movimentoSpesaModelCache) {
		this.movimentoSpesaModelCache = movimentoSpesaModelCache;
	}
	public Boolean getFlagIndietro() {
		return flagIndietro;
	}
	public void setFlagIndietro(Boolean flagIndietro) {
		this.flagIndietro = flagIndietro;
	}
	public Boolean getSubImpegnoSelectedMod() {
		return subImpegnoSelectedMod;
	}
	public void setSubImpegnoSelectedMod(Boolean subImpegnoSelectedMod) {
		this.subImpegnoSelectedMod = subImpegnoSelectedMod;
	}
	public String getNumeroSubImpegnoMod() {
		return numeroSubImpegnoMod;
	}
	public void setNumeroSubImpegnoMod(String numeroSubImpegnoMod) {
		this.numeroSubImpegnoMod = numeroSubImpegnoMod;
	}
	public String getImportoAttualeSubImpegnoMod() {
		return importoAttualeSubImpegnoMod;
	}
	public void setImportoAttualeSubImpegnoMod(String importoAttualeSubImpegnoMod) {
		this.importoAttualeSubImpegnoMod = importoAttualeSubImpegnoMod;
	}
	public String getMinImportoCalcolatoMod() {
		return minImportoCalcolatoMod;
	}
	public void setMinImportoCalcolatoMod(String minImportoCalcolatoMod) {
		this.minImportoCalcolatoMod = minImportoCalcolatoMod;
	}
	public String getMaxImportoCalcolatoMod() {
		return maxImportoCalcolatoMod;
	}
	public void setMaxImportoCalcolatoMod(String maxImportoCalcolatoMod) {
		this.maxImportoCalcolatoMod = maxImportoCalcolatoMod;
	}
	public String getMinImportoSubCalcolatoMod() {
		return minImportoSubCalcolatoMod;
	}
	public void setMinImportoSubCalcolatoMod(String minImportoSubCalcolatoMod) {
		this.minImportoSubCalcolatoMod = minImportoSubCalcolatoMod;
	}
	public String getMaxImportoSubCalcolatoMod() {
		return maxImportoSubCalcolatoMod;
	}
	public void setMaxImportoSubCalcolatoMod(String maxImportoSubCalcolatoMod) {
		this.maxImportoSubCalcolatoMod = maxImportoSubCalcolatoMod;
	}
	public String getMinImpMod() {
		return minImpMod;
	}
	public void setMinImpMod(String minImpMod) {
		this.minImpMod = minImpMod;
	}
	public String getMaxImpMod() {
		return maxImpMod;
	}
	public void setMaxImpMod(String maxImpMod) {
		this.maxImpMod = maxImpMod;
	}
	public String getMinAncheMod() {
		return minAncheMod;
	}
	public void setMinAncheMod(String minAncheMod) {
		this.minAncheMod = minAncheMod;
	}
	public String getMaxAncheMod() {
		return maxAncheMod;
	}
	public void setMaxAncheMod(String maxAncheMod) {
		this.maxAncheMod = maxAncheMod;
	}
	public String getMinSubMod() {
		return minSubMod;
	}
	public void setMinSubMod(String minSubMod) {
		this.minSubMod = minSubMod;
	}
	public String getMaxSubMod() {
		return maxSubMod;
	}
	public void setMaxSubMod(String maxSubMod) {
		this.maxSubMod = maxSubMod;
	}
	public GestoreTransazioneElementareModel getTransazioneElementareSubMovimentoCache() {
		return transazioneElementareSubMovimentoCache;
	}
	public void setTransazioneElementareSubMovimentoCache(GestoreTransazioneElementareModel transazioneElementareSubMovimentoCache) {
		this.transazioneElementareSubMovimentoCache = transazioneElementareSubMovimentoCache;
	}
	public boolean isFlagSuperioreTreAnni() {
		return flagSuperioreTreAnni;
	}
	public void setFlagSuperioreTreAnni(boolean flagSuperioreTreAnni) {
		this.flagSuperioreTreAnni = flagSuperioreTreAnni;
	}
	public boolean isOperazioneAggiorna() {
		return operazioneAggiorna;
	}
	public void setOperazioneAggiorna(boolean operazioneAggiorna) {
		this.operazioneAggiorna = operazioneAggiorna;
	}
	public BigDecimal getDisponibilitaLiquidare() {
		return disponibilitaLiquidare;
	}
	public void setDisponibilitaLiquidare(BigDecimal disponibilitaLiquidare) {
		this.disponibilitaLiquidare = disponibilitaLiquidare;
	}
	public BigDecimal getSommaLiquidazioneDoc() {
		return sommaLiquidazioneDoc;
	}
	public void setSommaLiquidazioneDoc(BigDecimal sommaLiquidazioneDoc) {
		this.sommaLiquidazioneDoc = sommaLiquidazioneDoc;
	}
	public BigDecimal getDisponibilitaImpegnoModifica() {
		return disponibilitaImpegnoModifica;
	}
	public void setDisponibilitaImpegnoModifica(BigDecimal disponibilitaImpegnoModifica) {
		this.disponibilitaImpegnoModifica = disponibilitaImpegnoModifica;
	}

	public boolean isProseguiConWarningPianoDeiConti() {
		return proseguiConWarningPianoDeiConti;
	}

	public void setProseguiConWarningPianoDeiConti(boolean proseguiConWarningPianoDeiConti) {
		this.proseguiConWarningPianoDeiConti = proseguiConWarningPianoDeiConti;
	}

	public List<SubImpegno> getListaTuttiSubimpegniDatiMinimi() {
		return listaTuttiSubimpegniDatiMinimi;
	}

	public void setListaTuttiSubimpegniDatiMinimi(List<SubImpegno> listaTuttiSubimpegniDatiMinimi) {
		this.listaTuttiSubimpegniDatiMinimi = listaTuttiSubimpegniDatiMinimi;
	}
	public Soggetto getSoggettoImpegnoAttuale() {
		return soggettoImpegnoAttuale;
	}

	public void setSoggettoImpegnoAttuale(Soggetto soggettoImpegnoAttuale) {
		this.soggettoImpegnoAttuale = soggettoImpegnoAttuale;
	}

	public Soggetto getSoggettoSubAttuale() {
		return soggettoSubAttuale;
	}

	public void setSoggettoSubAttuale(Soggetto soggettoSubAttuale) {
		this.soggettoSubAttuale = soggettoSubAttuale;
	}

	public boolean isProseguiConWarningModificaPositivaAccertamento() {
		return proseguiConWarningModificaPositivaAccertamento;
	}

	public void setProseguiConWarningModificaPositivaAccertamento(boolean proseguiConWarningModificaPositivaAccertamento) {
		this.proseguiConWarningModificaPositivaAccertamento = proseguiConWarningModificaPositivaAccertamento;
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
	public void setListaModificheProvvedimento(List<AttoAmministrativoStoricizzato> listaModificheProvvedimento) {
		this.listaModificheProvvedimento = listaModificheProvvedimento;
	}

	public BigDecimal getImpMod() {
		return impMod;
	}

	public void setImpMod(BigDecimal impMod) {
		this.impMod = impMod;
	}

	public boolean isAbbchckd() {
		return abbchckd;
	}

	public void setAbbchckd(boolean abbchckd) {
		this.abbchckd = abbchckd;
	}

	public String getCmptnz() {
		return cmptnz;
	}

	public void setCmptnz(String cmptnz) {
		this.cmptnz = cmptnz;
	}

	public boolean isProseguiConWarningSacDelProvvPerDecentrato() {
		return proseguiConWarningSacDelProvvPerDecentrato;
	}

	public void setProseguiConWarningSacDelProvvPerDecentrato(boolean proseguiConWarningSacDelProvvPerDecentrato) {
		this.proseguiConWarningSacDelProvvPerDecentrato = proseguiConWarningSacDelProvvPerDecentrato;
	}
	
	/**
	 * @return the visualizzaWarningImpegnoNonTotVincolato
	 */
	public boolean isVisualizzaWarningImpegnoNonTotVincolato() {
		return visualizzaWarningImpegnoNonTotVincolato;
	}

	/**
	 * @param visualizzaWarningImpegnoNonTotVincolato the visualizzaWarningImpegnoNonTotVincolato to set
	 */
	public void setVisualizzaWarningImpegnoNonTotVincolato(
			boolean visualizzaWarningImpegnoNonTotVincolato) {
		this.visualizzaWarningImpegnoNonTotVincolato = visualizzaWarningImpegnoNonTotVincolato;
	}
	
	/**
	 * @return the gestioneModificaDecentratoEFaseROR
	 */
	public boolean isGestioneModificaDecentratoEFaseROR() {
		return gestioneModificaDecentratoEFaseROR;
	}

	/**
	 * @param gestioneModificaDecentratoEFaseROR the gestioneModificaDecentratoEFaseROR to set
	 */
	public void setGestioneModificaDecentratoEFaseROR(boolean gestioneModificaDecentratoEFaseROR) {
		this.gestioneModificaDecentratoEFaseROR = gestioneModificaDecentratoEFaseROR;
	}
	//SIAC-5333
	
	/**
	 * Controlla se l'ente sia o meno abilitato gestione prima nota da finanziaria.
	 *
	 * @return true, if is ente abilitato gestione prima nota da finanziaria false altrimenti
	 */
	public boolean isEnteAbilitatoGestionePrimaNotaDaFinanziaria() {
		return WebAppConstants.TRUE_STRING.equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
	}

	/**
	 * @return the richiediConfermaRedirezioneContabilitaGenerale
	 */
	public boolean isRichiediConfermaRedirezioneContabilitaGenerale() {
		return richiediConfermaRedirezioneContabilitaGenerale;
	}

	/**
	 * @param richiediConfermaRedirezioneContabilitaGenerale the richiediConfermaRedirezioneContabilitaGenerale to set
	 */
	public void setRichiediConfermaRedirezioneContabilitaGenerale(boolean richiediConfermaRedirezioneContabilitaGenerale) {
		this.richiediConfermaRedirezioneContabilitaGenerale = richiediConfermaRedirezioneContabilitaGenerale;
	}

	/**
	 * @return the saltaInserimentoPrimaNota
	 */
	public boolean isSaltaInserimentoPrimaNota() {
		return saltaInserimentoPrimaNota;
	}

	/**
	 * @param saltaInserimentoPrimaNota the saltaInserimentoPrimaNota to set
	 */
	public void setSaltaInserimentoPrimaNota(boolean saltaInserimentoPrimaNota) {
		this.saltaInserimentoPrimaNota = saltaInserimentoPrimaNota;
	}

	/**
	 * @return the uidDaCompletare
	 */
	public int getUidDaCompletare() {
		return uidDaCompletare;
	}

	/**
	 * @param uidDaCompletare the uidDaCompletare to set
	 */
	public void setUidDaCompletare(int uidDaCompletare) {
		this.uidDaCompletare = uidDaCompletare;
	}
	
	/**
	 * Gets the messaggio prosecuzione sucontabilita generale.
	 *
	 * @return the messaggioProsecuzioneSucontabilitaGenerale
	 */
	public String getMessaggioProsecuzioneSuContabilitaGenerale() {
		return messaggioProsecuzioneSuContabilitaGenerale;
	}

	/**
	 * Sets the messaggio prosecuzione sucontabilita generale.
	 *
	 * @param messaggioProsecuzioneSuContabilitaGenerale the messaggioProsecuzioneSucontabilitaGenerale to set
	 */
	public void setMessaggioProsecuzioneSuContabilitaGenerale(String messaggioProsecuzioneSuContabilitaGenerale) {
		this.messaggioProsecuzioneSuContabilitaGenerale = messaggioProsecuzioneSuContabilitaGenerale;
	}

	/**
	 * Checks if is inserimento senza sub.
	 *
	 * @return true, if is inserimento senza sub
	 */
	public boolean isInserimentoSenzaSub() {
		String codCreditore = getStep1Model().getSoggetto().getCodCreditore();
		String classeSoggetto = getStep1Model().getSoggetto().getClasse();
		
		return (StringUtils.isNotBlank(codCreditore) || StringUtils.isNotBlank(classeSoggetto)) ;
	}
	
	/**
	 * Checks if is classificazione spesa corretta per contabilita generale.
	 *
	 * @return true, if is classificazione spesa corretta per contabilita generale
	 */
	@Deprecated
	public boolean isClassificazioneSpesaCorrettaPerContabilitaGenerale() {
		CapitoloImpegnoModel capitolo = getStep1Model().getCapitolo();
		if(capitolo == null) {
			return true;
		}
		String codiceTitolo = capitolo.getTitoloSpesa();
		String codiceMacro = capitolo.getCodiceMacroAggregato();
		return isClassificazioneCorrettaPerGenImpegnoPartitaDiGiro(codiceTitolo , codiceMacro) || isClassificazioneCorrettaPerGenImpegno(codiceTitolo , codiceMacro);
	}
	
	/**
	 * Checks if is classificazione corretta per gen impegno partita di giro.
	 *
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @return true, if is classificazione corretta per gen impegno partita di giro
	 */
	public boolean isClassificazioneCorrettaPerGenImpegnoPartitaDiGiro(String codiceTitoloSpesa, String codiceMacroaggregato) {
		return codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_7) && codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_7010000) ||
				(codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_4) && 
						(codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_4020000) ||
								codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_4030000) ||
								codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_4040000) 
								));
	}
	
	/**
	 * Checks if is classificazione corretta per gen impegno.
	 *
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @return true, if is classificazione corretta per gen impegno
	 */
	public boolean isClassificazioneCorrettaPerGenImpegno(String codiceTitoloSpesa, String codiceMacroaggregato) {
		return (codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_1) && codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_1040000)) ||
				   (codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_2) && 
						(codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_2030000) ||
								codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_2040000) ||
									codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_2050000))) ||
					codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_3)  || codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_5) ||
					(codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_4) && codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_4010000)) ||
					(codiceTitoloSpesa.equalsIgnoreCase(WebAppConstants.TITOLO_7) && 
								codiceMacroaggregato.equalsIgnoreCase(WebAppConstants.MACROAGGREGATO_7020000));
	}
	
	/**
	 * Checks if is provvedimento definitivo.
	 *
	 * @return true, if is provvedimento definitivo
	 */
	public boolean isProvvedimentoDefinitivo() {
		ProvvedimentoImpegnoModel provvedimento = getStep1Model().getProvvedimento();
		return provvedimento != null?  Constanti.ATTO_AMM_STATO_DEFINITIVO.equalsIgnoreCase(provvedimento.getStato()) : false;
	}

	/**
	 * @return the statoMovimentoGestioneOld
	 */
	public String getStatoMovimentoGestioneOld() {
		return statoMovimentoGestioneOld;
	}

	/**
	 * @param statoMovimentoGestioneOld the statoMovimentoGestioneOld to set
	 */
	public void setStatoMovimentoGestioneOld(String statoMovimentoGestioneOld) {
		this.statoMovimentoGestioneOld = statoMovimentoGestioneOld;
	}

	public List<Cronoprogramma> getListaRicercaCronop() {
		return listaRicercaCronop;
	}

	public void setListaRicercaCronop(List<Cronoprogramma> listaRicercaCronop) {
		this.listaRicercaCronop = listaRicercaCronop;
	}
	
	public RicercaDeiCronoprogrammiCollegatiAlProvvedimento creaRequestRicercaCronoprogramma() {
		return creaRequest(RicercaDeiCronoprogrammiCollegatiAlProvvedimento.class);
	}

	//SIAC-6990
	public AggiornaImpegno getImpegnoRequestStep1() {
		return impegnoRequestStep1;
	}

	public void setImpegnoRequestStep1(AggiornaImpegno impegnoRequestStep1) {
		this.impegnoRequestStep1 = impegnoRequestStep1;
	}
	//
	
	
}
