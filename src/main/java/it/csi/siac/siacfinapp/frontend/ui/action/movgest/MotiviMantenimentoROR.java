/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

public enum MotiviMantenimentoROR {
	
	ORGANI_ISTITUZIONALI("1.01","1","SPESE PER ORGANI ISTITUZIONALI"),
	PERSONALE_ONERI_ACCESSORI("1.02","1B", "SPESA PER IL PERSONALE ONERI ACCESSORI"),
	UTILIZZO_BENI_TERZI("1.04","1C","UTILIZZO BENI DI TERZI"),
	INTERESSI_PASSIVI("1.07","1D","INTERESSI PASSIVI E ONERI FINANZ. DIVERSI 1"),
	IMPOSTE_TASSE("1.08","1E","IMPOSTE E TASSE"),	
	
	ONERI_STRAORDINARI_1("1.09","1F1","ONERI STRAORDINARI DELLA GESTIONE CORRENTE 1"),
	ONERI_STRAORDINARI_2("1.09","1F2","ONERI STRAORDINARI DELLA GESTIONE CORRENTE 2"),

	
	ACQUISTO_BENI_SERVIZI_1("1.03","2.1","ACQUISTO DI BENI E SERVIZI 1"),
	ACQUISTO_BENI_SERVIZI_2("1.03","2.2","ACQUISTO DI BENI E SERVIZI 2"),
	ACQUISTO_BENI_SERVIZI_3("1.03","2.3","ACQUISTO DI BENI E SERVIZI 3"),
	
	ACQUISTO_BENI_SERVIZI_4("2.01","2.1","ACQUISTO DI BENI E SERVIZI 1"),
	ACQUISTO_BENI_SERVIZI_5("2.01","2.2","ACQUISTO DI BENI E SERVIZI 2"),
	ACQUISTO_BENI_SERVIZI_6("2.01","2.3","ACQUISTO DI BENI E SERVIZI 3"),
	
	PRENOTAZIONI_DI_IMPEGNO("1.03","2A","PRENOTAZIONI DI IMPEGNO PER GARE IN VIA DI ESPLETAMENTO (ART. 56 COMMA 4 d. Lgs. 118/2011 e CIRCOLARE IN/2014/28447)"),
	PRENOTAZIONI_DI_IMPEGNO_1("2.01","2A","PRENOTAZIONI DI IMPEGNO PER GARE IN VIA DI ESPLETAMENTO (ART. 56 COMMA 4 d. Lgs. 118/2011 e CIRCOLARE IN/2014/28447)"),
	
	TRASFERIMENTI_CONTRIBUTI_SOGGETTI("1.05","3","TRASFERIMENTI/CONTRIBUTI SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_SOGGETTI_1("1.06","3","TRASFERIMENTI/CONTRIBUTI SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_SOGGETTI_2("2.02","3","TRASFERIMENTI/CONTRIBUTI SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_SOGGETTI_3("2.03","3","TRASFERIMENTI/CONTRIBUTI SOGGETTI A RENDICONTAZIONE"),
	
	TRASFERIMENTI_CONTRIBUTI_NON_SOGGETTI("1.05","3A","TRASFERIMENTI/CONTRIBUTI NON SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_NON_SOGGETTI_1("1.06","3A","TRASFERIMENTI/CONTRIBUTI NON SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_NON_SOGGETTI_2("2.02","3A","TRASFERIMENTI/CONTRIBUTI NON SOGGETTI A RENDICONTAZIONE"),
	TRASFERIMENTI_CONTRIBUTI_NON_SOGGETTI_3("2.03","3A","TRASFERIMENTI/CONTRIBUTI NON SOGGETTI A RENDICONTAZIONE"),
	
	RIMBORSO_QUOTA("3.01","4","RIMBORSO QUOTA CAPITALE MUTUI E PRESTITI A CASSA DD.PP."),
	
	RIMBORSO_PRESTITI("3.04","5","RIMBORSO PRESTITI OBBLIGAZIONARI");
	
	private final String pdc;
	private final String key;
    private final String sintesi;


    MotiviMantenimentoROR(String pdc, String key, String sintesi) {
    	this.pdc=pdc;
        this.key = key;
        this.sintesi = sintesi;

    }
    
    public String getPdc() {
        return pdc;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getSintesi() {
        return sintesi;
    }

}
