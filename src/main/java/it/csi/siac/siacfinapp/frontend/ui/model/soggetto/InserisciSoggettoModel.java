/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class InserisciSoggettoModel extends SoggettoModel {

	private static final long serialVersionUID = 4897965120809144749L;

	// radio residenza
	private List<String> radioResidenza = new ArrayList<String>();

	// flag residenza
	private String flagResidenza;

	// flag sesso
	private String flagSesso;

	// radio sesso
	private List<String> radioSesso = new ArrayList<String>();

	// codice fiscale
	private String codiceFiscale;

	// partita iva
	private String partitaIva;

	// codice fiscale estero
	private String codiceFiscaleEstero;

	// nome e cognome
	private String cognome;
	private String nome;

	// data di nascita
	private String dataNascita;

	// Comune
	private String comune;
	private String idComune;

	// altri campi:
	private String note;
	private String idTipoSoggetto;
	private String denominazione;
	private String sesso;
	private String stato;
	private String[] idClasseSoggetto;
	private String[] idTipoOnere;

	// id nazione
	private String idNazione;

	// id natura giuridica
	private String idNaturaGiuridica;

	// hidde per il comune
	private String hiddenComune;

	// parametro ricerca controlla dati
	private String parametroRicercaControllaDati;

	// soggetti
	private HashMap<String, SoggettoDaFontiAnagModel> soggetti = new HashMap<String, SoggettoDaFontiAnagModel>();

	// flag effettuata ricerca anagrafica
	private boolean effettuataRicercaAnagrafica;

	// stato e codice soggetto:
	private String statoSoggetto, codiceSoggetto;

	// fallimnento
	private boolean fallimento;

	// indirizzo
	private IndirizzoModel indirizzo;

	// mappa indirizzi
	private HashMap<String, IndirizzoModel> indirizzi;

	// recapito
	private RecapitoModel recapito;

	// mappa recapiti
	private HashMap<String, Contatto> recapiti;

	// nuovo campo
	private String matricola;

	private Date dataFineValiditaDurc;
	private Character tipoFonteDurc;
	private Integer fonteDurcClassifId;
	private String noteDurc;

	private String elencoStruttureAmministrativoContabiliJson;
	
	private String canalePA;
	private String codDestinatario;
	private String emailPec;

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public InserisciSoggettoModel() {
		setTitolo("Inserimento soggetto");
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCodiceFiscaleEstero() {
		return codiceFiscaleEstero;
	}

	public void setCodiceFiscaleEstero(String codiceFiscaleEstero) {
		this.codiceFiscaleEstero = codiceFiscaleEstero;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public HashMap<String, SoggettoDaFontiAnagModel> getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(HashMap<String, SoggettoDaFontiAnagModel> soggetti) {
		this.soggetti = soggetti;
	}

	public String getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(String idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getFlagResidenza() {
		return flagResidenza;
	}

	public void setFlagResidenza(String flagResidenza) {
		this.flagResidenza = flagResidenza;
	}

	public List<String> getRadioResidenza() {
		return radioResidenza;
	}

	public void setRadioResidenza(List<String> radioResidenza) {
		this.radioResidenza = radioResidenza;
	}

	public String getIdNazione() {
		return idNazione;
	}

	public void setIdNazione(String idNazione) {
		this.idNazione = idNazione;
	}

	public String getHiddenComune() {
		return hiddenComune;
	}

	public void setHiddenComune(String hiddenComune) {
		this.hiddenComune = hiddenComune;
	}

	public String getFlagSesso() {
		return flagSesso;
	}

	public void setFlagSesso(String flagSesso) {
		this.flagSesso = flagSesso;
	}

	public List<String> getRadioSesso() {
		return radioSesso;
	}

	public void setRadioSesso(List<String> radioSesso) {
		this.radioSesso = radioSesso;
	}

	public String getIdComune() {
		return idComune;
	}

	public void setIdComune(String idComune) {
		this.idComune = idComune;
	}

	public String[] getIdClasseSoggetto() {
		return idClasseSoggetto;
	}

	public void setIdClasseSoggetto(String[] idClasseSoggetto) {
		this.idClasseSoggetto = idClasseSoggetto;
	}

	public String getParametroRicercaControllaDati() {
		return parametroRicercaControllaDati;
	}

	public void setParametroRicercaControllaDati(String parametroRicercaControllaDati) {
		this.parametroRicercaControllaDati = parametroRicercaControllaDati;
	}

	public String[] getIdTipoOnere() {
		return idTipoOnere;
	}

	public void setIdTipoOnere(String[] idTipoOnere) {
		this.idTipoOnere = idTipoOnere;
	}

	public String getIdNaturaGiuridica() {
		return idNaturaGiuridica;
	}

	public void setIdNaturaGiuridica(String idNaturaGiuridica) {
		this.idNaturaGiuridica = idNaturaGiuridica;
	}

	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

	public HashMap<String, IndirizzoModel> getIndirizzi() {
		return indirizzi;
	}

	public void setIndirizzi(HashMap<String, IndirizzoModel> indirizzi) {
		this.indirizzi = indirizzi;
	}

	public RecapitoModel getRecapito() {
		return recapito;
	}

	public void setRecapito(RecapitoModel recapito) {
		this.recapito = recapito;
	}

	public HashMap<String, Contatto> getRecapiti() {
		return recapiti;
	}

	public void resetRecapiti() {
		this.recapiti = new HashMap<String, Contatto>();
	}

	public void setRecapiti(HashMap<String, Contatto> recapiti) {
		if (this.recapiti == null)
			this.recapiti = new HashMap<String, Contatto>();
		this.recapiti.putAll(recapiti);
	}

	public boolean isEffettuataRicercaAnagrafica() {
		return effettuataRicercaAnagrafica;
	}

	public void setEffettuataRicercaAnagrafica(boolean effettuataRicercaAnagrafica) {
		this.effettuataRicercaAnagrafica = effettuataRicercaAnagrafica;
	}

	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	public String getStatoSoggetto() {
		return statoSoggetto;
	}

	public void setStatoSoggetto(String statoSoggetto) {
		this.statoSoggetto = statoSoggetto;
	}

	public boolean isFallimento() {
		return fallimento;
	}

	public void setFallimento(boolean fallimento) {
		this.fallimento = fallimento;
	}

	public Date getDataFineValiditaDurc() {
		return dataFineValiditaDurc;
	}

	public void setDataFineValiditaDurc(Date dataFineValiditaDurc) {
		this.dataFineValiditaDurc = dataFineValiditaDurc;
	}

	public Character getTipoFonteDurc() {
		return tipoFonteDurc;
	}

	public void setTipoFonteDurc(Character tipoFonteDurc) {
		this.tipoFonteDurc = tipoFonteDurc;
	}

	public Integer getFonteDurcClassifId() {
		return fonteDurcClassifId;
	}

	public void setFonteDurcClassifId(Integer fonteDurcClassifId) {
		this.fonteDurcClassifId = fonteDurcClassifId;
	}

	public String getNoteDurc() {
		return noteDurc;
	}

	public void setNoteDurc(String noteDurc) {
		this.noteDurc = noteDurc;
	}

	public String getElencoStruttureAmministrativoContabiliJson() {
		return elencoStruttureAmministrativoContabiliJson;
	}

	public void setElencoStruttureAmministrativoContabiliJson(String elencoStruttureAmministrativoContabiliJson) {
		this.elencoStruttureAmministrativoContabiliJson = elencoStruttureAmministrativoContabiliJson;
	}

	public String getCanalePA() {
		return canalePA;
	}

	public void setCanalePA(String canalePA) {
		this.canalePA = canalePA;
	}

	public String getCodDestinatario() {
		return codDestinatario;
	}

	//SIAC-7151
	public void setCodDestinatario(String codDestinatario) {
		this.codDestinatario = codDestinatario.toUpperCase();
	}

	public String getEmailPec() {
		return emailPec;
	}

	public void setEmailPec(String emailPec) {
		this.emailPec = emailPec;
	}

}