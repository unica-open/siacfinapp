/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class CommonConsulta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//impegno o accertamento:
	public static final int IMPEGNO = 0;
	public static final int ACCERTAMENTO = 1;
	
	//tipo:
	private int	tipoMovimento;
	
	//descrizione:
	private String descMovimento;
	private String descDispMovimento;
	
	//utente creazione
	private String utenteCreazione;
	
	//utente modifica
	private String utenteModifica;
	
	//stato operativo
	private String statoOperativo;
	
	//data inserimento
	private Date dataInserimento;
	
	//data modifica
	private Date dataModifica;
	
	//data stato operativo
	private Date dataStatoOperativo;
	
	//importo
	private BigDecimal importo;
	
	//reimputazione:
	private Integer	annoReimputazione;
	private String	reimputazione;
	private String reanno;
	
	//Inner classes:
	
	public String getReanno() {
		return reanno;
	}

	public void setReanno(String reanno) {
		this.reanno = reanno;
	}

	//INNER CLASS ImportoCapitolo
	public class ImportoCapitolo implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//anno competenza
		private String annoCompetenza;
		//disponibilita impegnare
		private BigDecimal disponibilitaImpegnare;
		//disponibilita pagare
		private BigDecimal disponibilitaPagare;
		//disponibilita variare
		private BigDecimal disponibilitaVariare;
		
		public String getAnnoCompetenza() {
			return annoCompetenza;
		}
		public void setAnnoCompetenza(String annoCompetenza) {
			this.annoCompetenza = annoCompetenza;
		}
		public BigDecimal getDisponibilitaImpegnare() {
			return disponibilitaImpegnare;
		}
		public void setDisponibilitaImpegnare(BigDecimal disponibilitaImpegnare) {
			this.disponibilitaImpegnare = disponibilitaImpegnare;
		}
		public BigDecimal getDisponibilitaPagare() {
			return disponibilitaPagare;
		}
		public void setDisponibilitaPagare(BigDecimal disponibilitaPagare) {
			this.disponibilitaPagare = disponibilitaPagare;
		}
		public BigDecimal getDisponibilitaVariare() {
			return disponibilitaVariare;
		}
		public void setDisponibilitaVariare(BigDecimal disponibilitaVariare) {
			this.disponibilitaVariare = disponibilitaVariare;
		}
	}
	
	//INNER CLASS Capitolo
	public class Capitolo implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//anno, numero, ueb, descrizione, struttura, tipoFinanziamento, numeroArticolo:
		private String anno, numero, ueb, descrizione, struttura, tipoFinanziamento, numeroArticolo;
		//lista importi:
		private final ArrayList<ImportoCapitolo> importi = new ArrayList<ImportoCapitolo>(3);

		public String getAnno() {
			return anno;
		}
		public void setAnno(String anno) {
			this.anno = anno;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public String getUeb() {
			return ueb;
		}
		public void setUeb(String ueb) {
			this.ueb = ueb;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}		
		public String getStruttura() {
			return struttura;
		}
		public void setStruttura(String struttura) {
			this.struttura = struttura;
		}
		public String getTipoFinanziamento() {
			return tipoFinanziamento;
		}
		public void setTipoFinanziamento(String tipoFinanziamento) {
			this.tipoFinanziamento = tipoFinanziamento;
		}
		public List<ImportoCapitolo> getImporti() {
			return importi;
		}
		
		public String getNumeroArticolo() {
			return numeroArticolo;
		}
		public void setNumeroArticolo(String numeroArticolo) {
			this.numeroArticolo = numeroArticolo;
		}
		
		public void addImporto(String anno, BigDecimal dispImpegnare, BigDecimal dispPagare, BigDecimal dispVariare) {
			//inizializzo un nuovo importo: 
			ImportoCapitolo importo = new ImportoCapitolo();
			//setto i dati
			importo.setAnnoCompetenza(anno);
			importo.setDisponibilitaImpegnare(dispImpegnare);
			importo.setDisponibilitaPagare(dispPagare);
			importo.setDisponibilitaVariare(dispVariare);
			//lo aggiungo in lista:
			importi.add(importo);
		}
	}

	//INNER CLASS Provvedimento
	public class Provvedimento implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//anno, numero, tipo, oggetto, struttura, stato:
		private String anno, numero, tipo, oggetto, struttura, stato, bloccoRagioneria;
		// SIAC-5220
		private String strutturaCompleta;
		
		public String getBloccoRagioneria() {
			return bloccoRagioneria;
		}
		public void setBloccoRagioneria(String bloccoRagioneria) {
			this.bloccoRagioneria = bloccoRagioneria;
		}
		public String getAnno() {
			return anno;
		}
		public void setAnno(String anno) {
			this.anno = anno;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getOggetto() {
			return oggetto;
		}
		public void setOggetto(String oggetto) {
			this.oggetto = oggetto;
		}
		public String getStruttura() {
			return struttura;
		}
		public void setStruttura(String struttura) {
			this.struttura = struttura;
		}
		public String getStato() {
			return stato;
		}
		public void setStato(String stato) {
			this.stato = stato;
		}
		/**
		 * @return the strutturaCompleta
		 */
		public String getStrutturaCompleta() {
			return strutturaCompleta;
		}
		/**
		 * @param strutturaCompleta the strutturaCompleta to set
		 */
		public void setStrutturaCompleta(String strutturaCompleta) {
			this.strutturaCompleta = strutturaCompleta;
		}
	}

	//INNER CLASS Soggetto
	public class Soggetto implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//CODICE, DENOMINAZIONE, CODICEFISCALE, PARTITAIVA, CLASSESOGGETTOCODICE, CLASSESOGGETTODESCRIZIONE:
		private String codice, denominazione, codiceFiscale, partitaIva, classeSoggettoCodice, classeSoggettoDescrizione;

		public String getCodice() {
			return codice;
		}
		public void setCodice(String codice) {
			this.codice = codice;
		}
		public String getDenominazione() {
			return denominazione;
		}
		public void setDenominazione(String denominazione) {
			this.denominazione = denominazione;
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
		public String getClasseSoggettoCodice() {
			return classeSoggettoCodice;
		}
		public void setClasseSoggettoCodice(String classeSoggettoCodice) {
			this.classeSoggettoCodice = classeSoggettoCodice;
		}
		public String getClasseSoggettoDescrizione() {
			return classeSoggettoDescrizione;
		}
		public void setClasseSoggettoDescrizione(String classeSoggettoDescrizione) {
			this.classeSoggettoDescrizione = classeSoggettoDescrizione;
		}
		
		public boolean isSoggetto () {
			return (this.codice != null);
		}
	}
	
	//GETTER E SETTER:		

	public int getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(int tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
		this.descMovimento = (tipoMovimento == IMPEGNO)? "impegn" : "accertament";
		this.descDispMovimento = (tipoMovimento == IMPEGNO)? "impegn" : "accert";
	}

	public String getDescDispMovimento() {	
		return StringUtils.lowerCase(descDispMovimento);
	}
	public String getDescMovimentoLower() {
		return StringUtils.lowerCase(descMovimento);
	}
	public String getDescMovimentoUpper() {
		return  StringUtils.upperCase(descMovimento);
	}
	public String getDescMovimentoCapital() {
		return StringUtils.capitalize(descMovimento);
	}
	public String getDescDispMovimentoLower() {
		return StringUtils.lowerCase(descDispMovimento);
	}
	public String getDescDispMovimentoUpper() {
		return  StringUtils.upperCase(descDispMovimento);
	}
	public String getDescDispMovimentoCapital() {
		return StringUtils.capitalize(descDispMovimento);
	}

	public boolean isImpegno () {
		return (tipoMovimento == IMPEGNO);
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

	public String getStatoOperativo() {
		return statoOperativo;
	}

	public void setStatoOperativo(String statoOperativo) {
		this.statoOperativo = statoOperativo;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Date getDataStatoOperativo() {
		return dataStatoOperativo;
	}

	public void setDataStatoOperativo(Date dataStatoOperativo) {
		this.dataStatoOperativo = dataStatoOperativo;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public Integer getAnnoReimputazione() {
		return annoReimputazione;
	}

	public void setAnnoReimputazione(Integer annoReimputazione) {
		this.annoReimputazione = annoReimputazione;
	}

	public String getReimputazione() {
		return reimputazione;
	}

	public void setReimputazione(String reimputazione) {
		this.reimputazione = reimputazione;
	}

	// WORKAORUND PER PESSIMA GESTIONE E MODELLAZIONE
	public String getCodiceTipoMovimento() {
		String codiceTipoMovimento = null;
		switch (getTipoMovimento()) {
			case IMPEGNO:
				codiceTipoMovimento = "I";
				break;
			case ACCERTAMENTO:
				codiceTipoMovimento = "A";
				break;
			default:
				break;
		}
		return codiceTipoMovimento;
	}
	
}
