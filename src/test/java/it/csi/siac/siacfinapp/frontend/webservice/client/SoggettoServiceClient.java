/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.webservice.client;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;


public class SoggettoServiceClient {

	private final static String ENDPOINT = "http://localhost:8180/siacfinser/SoggettoService?wsdl";
	private it.csi.siac.siacfinser.frontend.webservice.client.SoggettoServiceClient client;
	
	@Before
	public void setUp() throws Exception {
		
		client = new it.csi.siac.siacfinser.frontend.webservice.client.SoggettoServiceClient();
		client.setEndpoint(ENDPOINT);
	}

	
	@Test
	public void testInserisceSoggetto() {
		
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("AAAAAA00A11B000J");	// demo 21
		richiedente.setOperatore(operatore);
		
		InserisceSoggetto req = new InserisceSoggetto();
		req.setRichiedente(richiedente);
		
		InserisceSoggettoResponse res = client.getPort().inserisceSoggetto(req);
//		Assert.assertTrue("Il servizio non restituisce alcun account", res.getAccounts().size()>0);
		Assert.assertTrue("Il servizio è OK", res != null);
		//log.debug("","Il servizio è OK");
		
		
	}
	

}
