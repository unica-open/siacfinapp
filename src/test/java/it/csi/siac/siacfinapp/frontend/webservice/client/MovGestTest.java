/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.webservice.client;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinapp.BaseJunit4TestCase;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

public class MovGestTest extends BaseJunit4TestCase {

	@Autowired @Qualifier("movimentoGestioneFinService")
	private transient MovimentoGestioneService movimentoGestioneFinService;
	
	@Test
	public void testRicercaImpegno() {
		//VALORI DA PASSARE A RICHIESTA
		String numImpegno = "4628";
		String annoImp = "2020";
		String annoEs = "2020";
		String enteUid = "2";
		///////////////////////////////
		
		
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
		BigDecimal numeroImpegno = new BigDecimal(numImpegno);
		
		impegnoDaCercare.setAnnoEsercizio(Integer.valueOf(annoEs));
		impegnoDaCercare.setNumeroImpegno(numeroImpegno);
		impegnoDaCercare.setAnnoImpegno(new Integer(annoImp));
		Ente enteProva = getEnteTest(new Integer(enteUid));
			
		parametroRicercaPerChiave.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		parametroRicercaPerChiave.setEnte(enteProva);
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);
		
		//PER SIAC-5785 - serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliSubs = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		//serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
		datiOpzionaliSubs.setCaricaCig(true);
		//il cup viene gratis assieme al cig (sono attr entrambi vedi funzonamento servizio)
		//quindi tanto vale farlo caricare:
		datiOpzionaliSubs.setCaricaCup(true);
		parametroRicercaPerChiave.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliSubs);
		parametroRicercaPerChiave.setCaricaSub(false);
		
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(parametroRicercaPerChiave);
		
		assertNotNull(response);
		assertTrue(Esito.SUCCESSO.equals(response.getEsito()));
		assertTrue(response.getImpegno().getProgetto().getCronoprogrammi().size() > 0);
		assertTrue(response.getImpegno().getIdCronoprogramma() != null);
		
		
	}
	
}
