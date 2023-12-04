/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.handler.session;

import it.csi.siac.siaccommonapp.handler.session.SessionParameter;

public enum FinSessionParameter implements SessionParameter {
	
	//nomi univoci da utilizzare
	//nelle varie action fin per settare 
	//i parametri in sessione
	
	REPORT_FOLDER,
	MODALITA_PAGAMENTO_MODEL,
	RISULTATI_RICERCA_SOGGETTI,
	RISULTATI_RICERCA_IMPEGNI,
	RISULTATI_RICERCA_SUBIMPEGNI,
	//SIAC-6702 e SIAC-6703
	RISULTATI_RICERCA_STORICO_IMPEGNI_ACCERTAMENTI,
	RISULTATI_RICERCA_ACCERTAMENTI,
	RISULTATI_RICERCA_QUOTE_SPESA,
	RISULTATI_RICERCA_QUOTE_ENTRATA,
	RISULTATI_RICERCA_ORD_PAG,
	RISULTATI_RICERCA_ORD_INC,
	INSERISCI_SOGGETTO_STEP1,
    INSERISCI_SOGGETTO_STEP2,
    EFFETTUATA_RICERCA_IN_ANAGRAFICA,
    INSERIMENTO_SOGGETTO_RESP,
    CODIFICHE,
    ACTION_DATA,
    MESSAGES,
	IMPEGNO_CERCATO,
	RISULTATI_RICERCA_LIQUIDAZIONI,
	ACCERTAMENTO_CERCATO,
	MOVGEST_INIZIALE,
	RIGHE_DA_REGOLARIZZARE,
	RISULTATI_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA,
	PAR_RICERCA_CARTA,
	PAR_RICERCA_SUB_DOCUMENTI_PER_NUOVA_RIGA_CARTA,
	REGOLARIZZAZIONE_AVVENUTA,
	ELENCO_STRUTTURA_AMMINISTRATIVA,
	//SIAC-7477
	ELENCO_STRUTTURA_AMMINISTRATIVA_COMPETENTE,
	MOVIMENTO_GESTIONE_DA_RIPETERE,
	ELENCO_CLASSI_SOGGETTO, 
	CLASSE_SOGGETTO_CORRENTE,
	//SIAC-8503
	SOGGETTO_ACCERTAMENTO_ORIGINAL,
	CLASSE_ACCERTAMENTO_ORIGINAL,
	ELENCO_STRUTTURE_AMMINISTRATIVE_CONTABILI_UTENTE, 
	RICERCA_CONTO_CORRENTE_CRITERI_RICERCA_CONTO_CORRENTE,
	TABLE_TAG_PARAMETER_PAGE,
	//SIAC-5333 costanti copiate da bilapp per duplicare la contabilita' generale
	INFORMAZIONI_AZIONE_PRECEDENTE,
	MESSAGGI_AZIONE_PRECEDENTE,
	ERRORI_AZIONE_PRECEDENTE,
	WARNING_AZIONE_PRECEDENTE,
	NON_PULIRE_MODEL,
	REGISTRAZIONEMOVFIN,
	ULTIMO_TIPO_EVENTO_RICERCATO,
	LIQUIDAZIONE,
	LISTA_CAUSALE_EP_INTEGRATA_GEN,
	ACCERTAMENTO,
	IMPEGNO,
	//SIAC-5943
	SUBIMPEGNO,
	SUBACCERTAMENTO,
	//SIAC-6352
	LISTA_CONTO_TESORERIA,
	//SIAC-6992 provvedimento in sessione da eliminare
	PROVVEDIMENTO_IMPEGNO,
	//SIAC-7523
	PROVVEDIMENTO_SELEZIONATO,
	GESTISCI_IMPEGNO_MODEL,
	LISTA_MODIFICHE_PER_REIMPUTAZIONE,
	LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO,
	
	;
	
	public boolean isEliminabile(){
		//eliminabile
		return true;
	}
	
	
	// prima del 25/11 con i nuovi common qui c'era override !!
	
	public String getName() {
		//ritorno il nome del parametro
		return name();
	}

}
