/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.VincoliCapitoloUEGest;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class CapitoloImpegnoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//dati principali:
	private Integer anno, articolo, numCapitolo, uid, idMacroAggregato, idPianoDeiConti, idProgramma, uidStruttura;
	
	//disponilbili:
	private BigDecimal disponibileAnno1, disponibileAnno2, disponibileAnno3;
	
	//importi:
	private List<ImportiCapitoloModel> importiCapitolo = new ArrayList<ImportiCapitoloModel>();
	
	//importi ug:
	private List<ImportiCapitoloUG> importiCapitoloUG = new ArrayList<ImportiCapitoloUG>();
	
	//importi eg:
	private List<ImportiCapitoloEG> importiCapitoloEG = new ArrayList<ImportiCapitoloEG>();
	
	//altri dati vari:
	private String annoCompetenza, stanziamento, disponibile, tipoFinanziamento, descrizione, classificazione,  titoloSpesa, codiceMacroAggregato, titoloEntrata, tipologia, codiceStrutturaAmministrativa;
	private String codiceStatoCapitolo,tipoFinanziamentoSelezionato, descrizioneStrutturaAmministrativa, codicePdcFinanziario, descrizionePdcFinanziario, codiceMissione, codiceProgramma, descrizioneMissione, descrizioneProgramma, codiceSiopeSpesa, descrizioneSiopeSpesa ;
	private String codicePoliticheRegionaliUnitarie, codicePerimetroSanitarioSpesa, codiceRicorrenteSpesa, codiceTransazioneEuropeaSpesa,codiceTransazioneEuropeaEntrata, codiceRicorrenteEntrata,codicePerimetroSanitarioEntrata, codiceClassificazioneCofog, cup;
	
	//stato capitolo:
	private StatoEntita statoCapitolo;
	
	//tipo classificatore pdc:
	private TipoClassificatore tipoClassificatorePdc;
	
	//flag impegnabile:
	private boolean flagImpegnabile;
	
	//lista vincoli ug gest:
	private List<VincoliCapitoloUEGest> listaVincoliCapitoloUEGest;
	
	//classificatori generici:
	private List<ClassificatoreGenerico> classificatoriGenerici;
	
	//ueb:
	private BigInteger ueb;
	
	//elemento piano dei conti:
	private ElementoPianoDeiConti elementoPianoDeiConti;
	
	//tipo finanziamento:
	private TipoFinanziamento finanziamento;
	
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public Integer getArticolo() {
		return articolo;
	}
	public void setArticolo(Integer articolo) {
		this.articolo = articolo;
	}
	public BigInteger getUeb() {
		return ueb;
	}
	public void setUeb(BigInteger ueb) {
		this.ueb = ueb;
	}
	public String getAnnoCompetenza() {
		return annoCompetenza;
	}
	public void setAnnoCompetenza(String annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}
	public String getStanziamento() {
		return stanziamento;
	}
	public void setStanziamento(String stanziamento) {
		this.stanziamento = stanziamento;
	}
	public String getDisponibile() {
		return disponibile;
	}
	public void setDisponibile(String disponibile) {
		this.disponibile = disponibile;
	}
	public Integer getNumCapitolo() {
		return numCapitolo;
	}
	public void setNumCapitolo(Integer numCapitolo) {
		this.numCapitolo = numCapitolo;
	}
	public String getTipoFinanziamento() {
		return tipoFinanziamento;
	}
	public void setTipoFinanziamento(String tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public BigDecimal getDisponibileAnno1() {
		return disponibileAnno1;
	}
	public void setDisponibileAnno1(BigDecimal disponibileAnno1) {
		this.disponibileAnno1 = disponibileAnno1;
	}
	public BigDecimal getDisponibileAnno2() {
		return disponibileAnno2;
	}
	public void setDisponibileAnno2(BigDecimal disponibileAnno2) {
		this.disponibileAnno2 = disponibileAnno2;
	}
	public BigDecimal getDisponibileAnno3() {
		return disponibileAnno3;
	}
	public void setDisponibileAnno3(BigDecimal disponibileAnno3) {
		this.disponibileAnno3 = disponibileAnno3;
	}
	public String getClassificazione() {
		return classificazione;
	}
	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}
	public String getCodiceStrutturaAmministrativa() {
		return codiceStrutturaAmministrativa;
	}
	public void setCodiceStrutturaAmministrativa(
			String codiceStrutturaAmministrativa) {
		this.codiceStrutturaAmministrativa = codiceStrutturaAmministrativa;
	}
	public String getDescrizioneStrutturaAmministrativa() {
		return descrizioneStrutturaAmministrativa;
	}
	public void setDescrizioneStrutturaAmministrativa(String descrizioneStrutturaAmministrativa) {
		this.descrizioneStrutturaAmministrativa = descrizioneStrutturaAmministrativa;
	}
	public String getCodicePdcFinanziario() {
		return codicePdcFinanziario;
	}
	public void setCodicePdcFinanziario(String codicePdcFinanziario) {
		this.codicePdcFinanziario = codicePdcFinanziario;
	}
	public String getDescrizionePdcFinanziario() {
		return descrizionePdcFinanziario;
	}
	public void setDescrizionePdcFinanziario(String descrizionePdcFinanziario) {
		this.descrizionePdcFinanziario = descrizionePdcFinanziario;
	}
	public Integer getIdMacroAggregato() {
		return idMacroAggregato;
	}
	public void setIdMacroAggregato(Integer idMacroAggregato) {
		this.idMacroAggregato = idMacroAggregato;
	}
	public String getCodiceMissione() {
		return codiceMissione;
	}
	public void setCodiceMissione(String codiceMissione) {
		this.codiceMissione = codiceMissione;
	}
	public String getCodiceProgramma() {
		return codiceProgramma;
	}
	public void setCodiceProgramma(String codiceProgramma) {
		this.codiceProgramma = codiceProgramma;
	}
	public String getDescrizioneMissione() {
		return descrizioneMissione;
	}
	public void setDescrizioneMissione(String descrizioneMissione) {
		this.descrizioneMissione = descrizioneMissione;
	}
	public String getDescrizioneProgramma() {
		return descrizioneProgramma;
	}
	public void setDescrizioneProgramma(String descrizioneProgramma) {
		this.descrizioneProgramma = descrizioneProgramma;
	}
	public List<ImportiCapitoloModel> getImportiCapitolo() {
		return importiCapitolo;
	}
	public void setImportiCapitolo(List<ImportiCapitoloModel> importiCapitolo) {
		this.importiCapitolo = importiCapitolo;
	}
	public Integer getIdPianoDeiConti() {
		return idPianoDeiConti;
	}
	public void setIdPianoDeiConti(Integer idPianoDeiConti) {
		this.idPianoDeiConti = idPianoDeiConti;
	}
	public String getTipoFinanziamentoSelezionato() {
		return tipoFinanziamentoSelezionato;
	}
	public void setTipoFinanziamentoSelezionato(String tipoFinanziamentoSelezionato) {
		this.tipoFinanziamentoSelezionato = tipoFinanziamentoSelezionato;
	}
	public Integer getIdProgramma() {
		return idProgramma;
	}
	public void setIdProgramma(Integer idProgramma) {
		this.idProgramma = idProgramma;
	}
	public Integer getUidStruttura() {
		return uidStruttura;
	}
	public void setUidStruttura(Integer uidStruttura) {
		this.uidStruttura = uidStruttura;
	}
	public String getCodiceStatoCapitolo() {
		return codiceStatoCapitolo;
	}
	public void setCodiceStatoCapitolo(String codiceStatoCapitolo) {
		this.codiceStatoCapitolo = codiceStatoCapitolo;
	}
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}
	public List<ImportiCapitoloUG> getImportiCapitoloUG() {
		return importiCapitoloUG;
	}
	public void setImportiCapitoloUG(List<ImportiCapitoloUG> importiCapitoloUG) {
		this.importiCapitoloUG = importiCapitoloUG;
	}
	public TipoFinanziamento getFinanziamento() {
		return finanziamento;
	}
	public void setFinanziamento(TipoFinanziamento finanziamento) {
		this.finanziamento = finanziamento;
	}
	public String getCodicePoliticheRegionaliUnitarie() {
		return codicePoliticheRegionaliUnitarie;
	}
	public void setCodicePoliticheRegionaliUnitarie(String codicePoliticheRegionaliUnitarie) {
		this.codicePoliticheRegionaliUnitarie = codicePoliticheRegionaliUnitarie;
	}
	public String getCodicePerimetroSanitarioSpesa() {
		return codicePerimetroSanitarioSpesa;
	}
	public void setCodicePerimetroSanitarioSpesa(String codicePerimetroSanitarioSpesa) {
		this.codicePerimetroSanitarioSpesa = codicePerimetroSanitarioSpesa;
	}
	public String getCodiceRicorrenteSpesa() {
		return codiceRicorrenteSpesa;
	}
	public void setCodiceRicorrenteSpesa(String codiceRicorrenteSpesa) {
		this.codiceRicorrenteSpesa = codiceRicorrenteSpesa;
	}
	public String getCodiceTransazioneEuropeaSpesa() {
		return codiceTransazioneEuropeaSpesa;
	}
	public void setCodiceTransazioneEuropeaSpesa(String codiceTransazioneEuropeaSpesa) {
		this.codiceTransazioneEuropeaSpesa = codiceTransazioneEuropeaSpesa;
	}
	public String getCodiceTransazioneEuropeaEntrata() {
		return codiceTransazioneEuropeaEntrata;
	}
	public void setCodiceTransazioneEuropeaEntrata(String codiceTransazioneEuropeaEntrata) {
		this.codiceTransazioneEuropeaEntrata = codiceTransazioneEuropeaEntrata;
	}
	public String getCodiceRicorrenteEntrata() {
		return codiceRicorrenteEntrata;
	}
	public void setCodiceRicorrenteEntrata(String codiceRicorrenteEntrata) {
		this.codiceRicorrenteEntrata = codiceRicorrenteEntrata;
	}
	public String getCodicePerimetroSanitarioEntrata() {
		return codicePerimetroSanitarioEntrata;
	}
	public void setCodicePerimetroSanitarioEntrata(String codicePerimetroSanitarioEntrata) {
		this.codicePerimetroSanitarioEntrata = codicePerimetroSanitarioEntrata;
	}
	public List<ImportiCapitoloEG> getImportiCapitoloEG() {
		return importiCapitoloEG;
	}
	public void setImportiCapitoloEG(List<ImportiCapitoloEG> importiCapitoloEG) {
		this.importiCapitoloEG = importiCapitoloEG;
	}
	public String getCodiceClassificazioneCofog() {
		return codiceClassificazioneCofog;
	}
	public void setCodiceClassificazioneCofog(String codiceClassificazioneCofog) {
		this.codiceClassificazioneCofog = codiceClassificazioneCofog;
	}
	public StatoEntita getStatoCapitolo() {
		return statoCapitolo;
	}
	public void setStatoCapitolo(StatoEntita statoCapitolo) {
		this.statoCapitolo = statoCapitolo;
	}
	public String getCodiceSiopeSpesa() {
		return codiceSiopeSpesa;
	}
	public void setCodiceSiopeSpesa(String codiceSiopeSpesa) {
		this.codiceSiopeSpesa = codiceSiopeSpesa;
	}
	public String getDescrizioneSiopeSpesa() {
		return descrizioneSiopeSpesa;
	}
	public void setDescrizioneSiopeSpesa(String descrizioneSiopeSpesa) {
		this.descrizioneSiopeSpesa = descrizioneSiopeSpesa;
	}
	public TipoClassificatore getTipoClassificatorePdc() {
		return tipoClassificatorePdc;
	}
	public void setTipoClassificatorePdc(TipoClassificatore tipoClassificatorePdc) {
		this.tipoClassificatorePdc = tipoClassificatorePdc;
	}
	public boolean isFlagImpegnabile() {
		return flagImpegnabile;
	}
	public void setFlagImpegnabile(boolean flagImpegnabile) {
		this.flagImpegnabile = flagImpegnabile;
	}
	public List<VincoliCapitoloUEGest> getListaVincoliCapitoloUEGest() {
		return listaVincoliCapitoloUEGest;
	}
	public void setListaVincoliCapitoloUEGest(List<VincoliCapitoloUEGest> listaVincoliCapitoloUEGest) {
		this.listaVincoliCapitoloUEGest = listaVincoliCapitoloUEGest;
	}
	public List<ClassificatoreGenerico> getClassificatoriGenerici() {
		return classificatoriGenerici;
	}
	public void setClassificatoriGenerici(List<ClassificatoreGenerico> classificatoriGenerici) {
		this.classificatoriGenerici = classificatoriGenerici;
	}
	/**
	 * @return the tipologia
	 */
	public String getTipologia() {
		return tipologia;
	}
	/**
	 * @param tipologia the tipologia to set
	 */
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	/**
	 * @return the titoloEntrata
	 */
	public String getTitoloEntrata() {
		return titoloEntrata;
	}
	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(String titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}
	/**
	 * @return the titoloSpesa
	 */
	public String getTitoloSpesa() {
		return titoloSpesa;
	}
	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(String titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}
	/**
	 * @return the codiceMacroAggregato
	 */
	public String getCodiceMacroAggregato() {
		return codiceMacroAggregato;
	}
	/**
	 * @param codiceMacroAggregato the codiceMacroAggregato to set
	 */
	public void setCodiceMacroAggregato(String codiceMacroAggregato) {
		this.codiceMacroAggregato = codiceMacroAggregato;
	}
	
	
}
