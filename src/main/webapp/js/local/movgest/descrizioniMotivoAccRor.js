/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var DescrizioniMotiviRor = (function(){
	var exports = {};
	
	var descrizioniReimp = {
			
				
		"1A" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",	
		"1B" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
		"2.1" :"Dallo Stato  o altre P.A-Obbligazioni giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto di impegno dell'Ente erogante, esigibili dopo il 31/12 ",
		"2.2" :"Da altri soggetti-Obbligazioni giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
		
		
		
		"2A" :"Obbligazioni giuridicamente perfezionate in base all'atto amministrativo di attribuzione del contributo, ma la rendicontazione delle spese non risulta formalmente approvata  dall'ente beneficiario entro il 31/12",
		
		"3A" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12 ",
		"3B" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12 ",
		"3C" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12 ",
		
		"4A.1" :"Dallo Stato o altre P.A.  - Obbligazioni perfezionate per le quali esiste, entro il 31/12, l'atto di impegno dell'Ente erogante, esigibili dopo il 31/12  ",
		"4A.2" :"Da altri soggetti  -Obbligazioni giuridicamente perfezionate per le quali esiste, entro 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12 ",
		"4A" :"Obbligazioni giuridicamente perfezionate in base all'atto amministrativo di attribuzione del contributo, ma la rendicontazione delle spese non risulta formalmente approvata  dall'ente beneficiario  entro il 31/12",
		"4B" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12",
		"4C" :"Obbligazioni  giuridicamente perfezionate per le quali esiste, entro il 31/12, l'atto formale che conferma l'obbligazione, esigibili dopo il 31/12"
    };

    var descrizioniCancellazione = {
    		
    		
		"1A.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate",
		"1A.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		"1A.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
    		
		"1B.1" :"Residui insussistenti se non corrispondenti ad obbligazioni perfezionate",
		"1B.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		"1B.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		"2.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate ",
		"2.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"2.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		
		"2A.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate",
		"2A.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		"2A.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili.",
		
		
		"3A.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate",
		"3B.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate",
		"3C.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionate",
		
		"3A.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		"3B.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		"3C.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito",
		
		"3A.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		"3B.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		"3C.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		"4A.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4A.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4A.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		
		"4A.1.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4A.1.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4A.1.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		"4A.2.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4A.2.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4A.2.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		"4A.3.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4A.3.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4A.3.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		
		"4B.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4B.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4B.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili",
		
		"4C.1" :"Residui insussistenti se non corrispondenti ad obbligazioni giuridicamente perfezionatezione, esigibili dopo il 31/12",
		"4C.2" :"Crediti riconosciuti insussistenti per l'avvenuta legale estinzione o per indebito o erroneo accertamento del credito ",
		"4C.3" :"Residui potenzialmente esigibili in quanto scaduti riconosciuti assolutamente inesigibili"
    }


    var descrizioniMantenere = {
    		
		"1A" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
		"1B" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
    		
		"2" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
		
		
		"2A" :"Obbligazioni giuridicamente perfezionate per le quali esiste, entro il 31/12, la formale deliberazione dell'Ente erogante di voler finanziare la spesa a rendicontazione e scadute sulla base della formale approvazione della rendicontazione delle spese sostenute (manca solo l'erogazione)",
		
		
		
		"3A" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
		"3B" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
		"3C" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)",
		
		
		
		
		"4A" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso) ",
		"4A.1" :"Obbligazioni giuridicamente perfezionate per le quali esiste, entro il 31/12, la formale deliberazione dell Ente erogante di voler finanziare la spesa a rendicontazione e scadute sulla base della formale approvazione della rendicontazione delle spese sostenute (manca solo l'erogazione)",
		"4B" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso",
		"4C" :"Obbligazioni giuridicamente perfezionale e scadute al 31/12 (si sono verificate le condizioni per l'incasso)  ) "
    
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