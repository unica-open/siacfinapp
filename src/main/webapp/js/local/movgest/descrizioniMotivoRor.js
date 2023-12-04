/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var DescrizioniMotiviRor = (function(){
	var exports = {};
	
	var descrizioniReimp = {
        "1" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        "1B" : "Per la specificità dell'argomento  si rinvia al principio applicato di contabilità finanziaria 4/2 - paragrafo 5.2 lett. A) ",
        "1C" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",        
        "1D" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        "1E" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        "1F" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        
        "2.1" : "Obbligazioni relative a forniture o prestazioni non completamente rese entro il 31/12, ovvero se totalmente rese entro il 31/12 fatturate dopo il 28/02 dell'anno successivo",
        "2.2" : "Contratti a carattere pluriennale per la parte di prestazione non resa entro il 31/12",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2.1" : "Obbligazioni relative a forniture o prestazioni non completamente rese entro il 31/12, ovvero se totalmente rese entro il 31/12 fatturate dopo il 28/02 dell'anno successivo",
        // "2.2" : "Contratti a carattere pluriennale per la parte di prestazione non resa entro il 31/12",
        
        "2A" : "Differimento ammesso unicamente per prenotazioni di impegno assunte nell'esercizio relative a procedura di gara formalmente indetta per l'affidamento di lavori pubblici in assenza di aggiudicazione definitiva entro l'esercizio",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2A" : "Differimento ammesso unicamente per prenotazioni di impegno assunte nell'esercizio relative a procedura di gara formalmente indetta per l'affidamento di lavori pubblici in assenza di aggiudicazione definitiva entro l'esercizio",

        "3" : "Trasferimenti/contributi in relazione ai quali la rendicontazione NON sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio. Parimenti non sono da conservare gli impegni che si riferiscono programmi/progetti/lavori che abbiano subito ritardi, rallentamenti, proroghe o differimenti di inizio o termine dei programmi/progetti/lavori medesimi e che comportino la conseguente rimodulazione del cronoprogramma. In tal caso, infatti, l’obbligazione giuridica sottostante non sarebbe in tutto o in parte esigibile nel corrente esercizio",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3" : "Trasferimenti/contributi in relazione ai quali la rendicontazione NON sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio. Parimenti non sono da conservare gli impegni che si riferiscono programmi/progetti/lavori che abbiano subito ritardi, rallentamenti, proroghe o differimenti di inizio o termine dei programmi/progetti/lavori medesimi e che comportino la conseguente rimodulazione del cronoprogramma. In tal caso, infatti, l’obbligazione giuridica sottostante non sarebbe in tutto o in parte esigibile nel corrente esercizio",
        // "3" : "Trasferimenti/contributi in relazione ai quali la rendicontazione NON sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio. Parimenti non sono da conservare gli impegni che si riferiscono programmi/progetti/lavori che abbiano subito ritardi, rallentamenti, proroghe o differimenti di inizio o termine dei programmi/progetti/lavori medesimi e che comportino la conseguente rimodulazione del cronoprogramma. In tal caso, infatti, l’obbligazione giuridica sottostante non sarebbe in tutto o in parte esigibile nel corrente esercizio",
        // "3" : "Trasferimenti/contributi in relazione ai quali la rendicontazione NON sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio. Parimenti non sono da conservare gli impegni che si riferiscono programmi/progetti/lavori che abbiano subito ritardi, rallentamenti, proroghe o differimenti di inizio o termine dei programmi/progetti/lavori medesimi e che comportino la conseguente rimodulazione del cronoprogramma. In tal caso, infatti, l’obbligazione giuridica sottostante non sarebbe in tutto o in parte esigibile nel corrente esercizio",

        "3A" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3A" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        // "3A" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        // "3A" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",

        "4" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
        "5" : "Obbligazioni giuridicamente perfezionate per le quali esiste entro il 31/12  l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12"
    };

    var descrizioniCancellazione = {
        "1.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1.2" : "Debiti insussistenti o prescritti",

        "1B1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1B2" : "Debiti insussistenti o prescritti",
        
        "1C1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1C2" : "Debiti insussistenti o prescritti",
        
        "1D1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1D2" : "Debiti insussistenti o prescritti",

        "1E1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1E2" : "Debiti insussistenti o prescritti",
        "1F1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "1F2" : "Debiti insussistenti o prescritti",

        "2.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "2.2" : "Debiti insussistenti o prescritti",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "2.2" : "Debiti insussistenti o prescritti",

        "2A" : "Prenotazione di impegno relativa a procedura di gara formalmente indetta per la quale, entro il termine dell'esercizio, non sia stata assunta dalla Regione l'obbligazione di spesa verso i terzi (non aggiudicata)",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2A" : "Prenotazione di impegno relativa a procedura di gara formalmente indetta per la quale, entro il termine dell'esercizio, non sia stata assunta dalla Regione l'obbligazione di spesa verso i terzi (non aggiudicata)",
        // "2A" : "Prenotazione di impegno relativa a procedura di gara formalmente indetta per la quale, entro il termine dell'esercizio, non sia stata assunta dalla Regione l'obbligazione di spesa verso i terzi (non aggiudicata)",
        // "2A" : "Prenotazione di impegno relativa a procedura di gara formalmente indetta per la quale, entro il termine dell'esercizio, non sia stata assunta dalla Regione l'obbligazione di spesa verso i terzi (non aggiudicata)",

        "3.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "3.2" : "Debiti insussistenti o prescritti",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3.2" : "Debiti insussistenti o prescritti",
        // "3.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3.2" : "Debiti insussistenti o prescritti",
        // "3.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3.2" : "Debiti insussistenti o prescritti",

        "3B1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "3B2" : "Debiti insussistenti o prescritti",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3B1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3B2" : "Debiti insussistenti o prescritti",
        // "3B1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3B2" : "Debiti insussistenti o prescritti",
        // "3B1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        // "3B2" : "Debiti insussistenti o prescritti",

        "4.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "4.2" : "Debiti insussistenti o prescritti",
        
        "5.1" : "Impegni cui non corrispondono obbligazioni formalizzate",
        "5.2" : "Debiti insussistenti o prescritti" 
    }


    var descrizioniMantenere = {
        "1" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",
        "1B" : "Per la specificità dell'argomento  si rinvia al principio applicato di contabilità finanziaria 4/2 - paragrafo 5.2 lett. A) ",
        "1C" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",        
        "1D" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",
        "1E" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",       
        "1F1" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",
        "1F2" : "Spese per somme indebitamente incassate, sono da mantenere tra gli impegni residui le spese per restituzione di somme indebitamente incassate in quanto l’esigibilità dell’obbligazione di restituzione è in re ipsa (non sussistono, infatti, ragioni ostative alla restituzione dell’indebito)",       
        
        "2.1" : "Obbligazioni relative a forniture o prestazioni effettuate o rese entro il 31/12 e fatturate entro il 31/12 (LIQUIDATA) ",
        "2.2" : "Obbligazioni relative a forniture o prestazioni effettuate o rese entro il 31/12 e fatturate e pervenute al Protocollo Generale entro i 2 mesi successivi alla chiusura dell'esercizio (28/2) (LIQUIDABILE)",
        "2.3" : "Obbligazioni relative a forniture o prestazioni che il responsabile della spesa dichiara sotto la propria responsabilità, valutabile ad ogni fine di legge, essere state effettuate o rese entro il 31/12 (LIQUIDABILE)  ",
        
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2.1" : "Obbligazioni relative a forniture o prestazioni effettuate o rese entro il 31/12 e fatturate entro il 31/12 (LIQUIDATA) ",
        // "2.2" : "Obbligazioni relative a forniture o prestazioni effettuate o rese entro il 31/12 e fatturate e pervenute al Protocollo Generale entro i 2 mesi successivi alla chiusura dell'esercizio (28/2) (LIQUIDABILE)",
        // "2.3" : "Obbligazioni relative a forniture o prestazioni che il responsabile della spesa dichiara sotto la propria responsabilità, valutabile ad ogni fine di legge, essere state effettuate o rese entro il 31/12 (LIQUIDABILE)  ",
        
        "2A" : "Prenotazioni di impegno per gare in via di espletamento (Art. 56 comma 4 d. lgs. 118/2011 e circolare in/2014/28447)",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "2A" : "Prenotazioni di impegno per gare in via di espletamento (Art. 56 comma 4 d. lgs. 118/2011 e circolare in/2014/28447)",

        
        "3" : "Il principio applicato alla contabilità finanziaria stabilisce “in caso di trasferimenti a rendicontazione l’amministrazione beneficiaria del contributo accerta l’entrata con imputazione ai medesimi esercizi in cui l’amministrazione erogante ha registrato i corrispondenti impegni”.Pertanto sono da conservare tra gli impegni residui le spese per contributi in relazione ai quali la rendicontazione sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio.Conseguentemente nel caso in cui non sia stata accertata l’effettiva esigibilità dell’obbligazione nei termini anzidetti gli impegni devono essere re-imputati all’esercizio successivo ",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3" : "Il principio applicato alla contabilità finanziaria stabilisce “in caso di trasferimenti a rendicontazione l’amministrazione beneficiaria del contributo accerta l’entrata con imputazione ai medesimi esercizi in cui l’amministrazione erogante ha registrato i corrispondenti impegni”.Pertanto sono da conservare tra gli impegni residui le spese per contributi in relazione ai quali la rendicontazione sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio.Conseguentemente nel caso in cui non sia stata accertata l’effettiva esigibilità dell’obbligazione nei termini anzidetti gli impegni devono essere re-imputati all’esercizio successivo ",
        // "3" : "Il principio applicato alla contabilità finanziaria stabilisce “in caso di trasferimenti a rendicontazione l’amministrazione beneficiaria del contributo accerta l’entrata con imputazione ai medesimi esercizi in cui l’amministrazione erogante ha registrato i corrispondenti impegni”.Pertanto sono da conservare tra gli impegni residui le spese per contributi in relazione ai quali la rendicontazione sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio.Conseguentemente nel caso in cui non sia stata accertata l’effettiva esigibilità dell’obbligazione nei termini anzidetti gli impegni devono essere re-imputati all’esercizio successivo ",
        // "3" : "Il principio applicato alla contabilità finanziaria stabilisce “in caso di trasferimenti a rendicontazione l’amministrazione beneficiaria del contributo accerta l’entrata con imputazione ai medesimi esercizi in cui l’amministrazione erogante ha registrato i corrispondenti impegni”.Pertanto sono da conservare tra gli impegni residui le spese per contributi in relazione ai quali la rendicontazione sia stata trasmessa (fa fede il protocollo) dal beneficiario e validata con formale provvedimento amministrativo (decreto) da Regione entro la fine dell'esercizio.Conseguentemente nel caso in cui non sia stata accertata l’effettiva esigibilità dell’obbligazione nei termini anzidetti gli impegni devono essere re-imputati all’esercizio successivo ",

        "3A" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento). Si tratta di trasferimenti/contributi per i quali non vi siano condizioni sospensive che ne inibiscono l’esigibilità. Nel caso di contributi in conto interessi sono da conservare solo gli impegni che corrispondono a quote di ammortamento scadute nell’esercizio",
        // SIAC-7970 GS 22.01.2021 commentati perchè duplicati
        // "3A" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento). Si tratta di trasferimenti/contributi per i quali non vi siano condizioni sospensive che ne inibiscono l’esigibilità. Nel caso di contributi in conto interessi sono da conservare solo gli impegni che corrispondono a quote di ammortamento scadute nell’esercizio",
        // "3A" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento). Si tratta di trasferimenti/contributi per i quali non vi siano condizioni sospensive che ne inibiscono l’esigibilità. Nel caso di contributi in conto interessi sono da conservare solo gli impegni che corrispondono a quote di ammortamento scadute nell’esercizio",
        // "3A" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento). Si tratta di trasferimenti/contributi per i quali non vi siano condizioni sospensive che ne inibiscono l’esigibilità. Nel caso di contributi in conto interessi sono da conservare solo gli impegni che corrispondono a quote di ammortamento scadute nell’esercizio",

        "4" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)",
        
        "5" : "Obbligazioni giuridicamente perfezionate e scadute entro il 31/12 (si sono verificate le condizioni per il pagamento)"
    
    }
 
    exports.getDescByCodeAndType = function(type, code){
        if(type==="descrizioniCancellazione"){
            return descrizioniCancellazione[code];
        }else if(type==="descrizioniMantenere"){
            return descrizioniMantenere[code];
        }else{
            return descrizioniReimp[code];
        }
        
    }

    
	
	
	return exports;


}());