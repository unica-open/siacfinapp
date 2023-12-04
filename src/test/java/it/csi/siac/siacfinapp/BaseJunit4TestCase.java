/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
	"/spring/applicationContext.xml", 
	"/spring/jpa-test.xml", 
	"/spring/datasource-test.xml"
})


public abstract class BaseJunit4TestCase extends TestCase {

	protected LogUtil log = new LogUtil(this.getClass()); 

	@Autowired
	protected ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private Properties accountProperties;
	
	@PostConstruct
	private void init() {
		final String methodName = "init";
		
		setApplicationContext(new ClassPathXmlApplicationContext("/siacfinapp/src/main/resources/spring/serviceClientContext.xml"));
		
		accountProperties = new Properties();
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("./spring/account.properties");
		if(is != null) {
			try {
				accountProperties.load(is);
			} catch (IOException e) {
				// Non fare nulla
				log.error(methodName, "Errore nella lettura delle properties", e);
			}
		} else {
			log.error(methodName, "Properties non lette");
		}
	}
	
	/**
	 * Ottiene l'ente con id selezionato.
	 *
	 * @param uid l'uid dell'ente
	 * @return the ente test
	 */
	protected Ente getEnteTest(int uid) {
		Ente ente = new Ente();
		ente.setUid(uid);
		return ente;
	}
	
	/**
	 * Ottiene un richiedente di test.
	 *
	 * @param codiceFiscaleOperatore il codice fiscale dell'operatore
	 * @param uidAccount l'uid dell'account
	 * @param uidEnte l'uid dell'ente
	 * 
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteTest(String codiceFiscaleOperatore, int uidAccount, int uidEnte) {
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale(codiceFiscaleOperatore);
		richiedente.setOperatore(operatore);
		Account account = getAccountTest(uidAccount, uidEnte);
		richiedente.setAccount(account);
		return richiedente;
	}
	
	/**
	 * Ottiene l'account di test.
	 * 
	 * @param uidAccount l'uid dell'account
	 * @param uidEnte    l'uid dell'ente
	 * 
	 * @return l'account
	 */
	protected Account getAccountTest(int uidAccount, int uidEnte) {
		Account account = new Account();
		account.setUid(uidAccount);
		account.setEnte(getEnteTest(uidEnte));
		return account;
	}
	
	protected Bilancio getBilancioTest(int uid, int anno) {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(uid);
		bilancio.setAnno(anno);
		return bilancio;
	}
	
	

}
