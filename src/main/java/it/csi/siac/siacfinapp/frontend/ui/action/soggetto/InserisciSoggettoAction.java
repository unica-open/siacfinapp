/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;




import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.CFGenerator;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.ValidaCF;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.VerificaPartitaIva;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;


/**
 * Action per la gestione del inserimento soggetto step1
 * 
 * @author paolos
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciSoggettoAction extends WizardScriviSoggettoAction {

	
	private static final long serialVersionUID = -6918565117851350145L;

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		debug(methodName, "BEGIN");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo (parte alta della pagina):
		this.model.setTitolo("Inserimento soggetto");
		
		FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), "");
		
		model.setErrori(new ArrayList<Errore>());
		
		debug(methodName, "INSERIMENTO_SOGGETTO_RESP "+sessionHandler.getParametro(FinSessionParameter.INSERIMENTO_SOGGETTO_RESP));
		
		debug(methodName, "EFFETTUATA_RICERCA_IN_ANAGRAFICA "+sessionHandler.getParametro(FinSessionParameter.EFFETTUATA_RICERCA_IN_ANAGRAFICA));
		
		debug(methodName, "Luenghezza lista errori "+model.getErrori().size());
		
		
		try {

			debug(methodName, "PAOLOZ "+model);

			// carico i radio di residenza estera
		    List<String> listaRadioResid = new ArrayList<String>();
		    listaRadioResid.add("si");
		    listaRadioResid.add("no");
		    
			
		    model.setRadioResidenza(listaRadioResid);
			// presetto a no la residenza estera
		    model.setFlagResidenza("no");
			
			// flag sesso
		    List<String> listaRadioSesso = new ArrayList<String>();
		    listaRadioSesso.add(Constanti.MASCHIO.toLowerCase());
		    listaRadioSesso.add(Constanti.FEMMINA.toLowerCase());
		    model.setRadioSesso(listaRadioSesso);
			
			debug(methodName, "prima del controllo ");
				
			caricaListeInserisciSoggetto();
			
			model.setElencoStruttureAmministrativoContabiliJson(super.getAlberoStruttureAmministrativeContabiliJson(isDecentrato()));
			
			ListaComuni requestComuni  = new ListaComuni();
			
			requestComuni.setRichiedente(sessionHandler.getRichiedente());
			debug(methodName, "Anno esercizio "+sessionHandler.getAnnoEsercizio());

						
		}catch(Exception e){
			log.error(methodName,e);
			throw new Exception();
		}finally{
			log.debugEnd(methodName, "");
		}
	}
	
	/**
	 * Metodo seleziona soggetto
	 * @return
	 */
	public String selezionaSoggetto() {
		setMethodName("selezionaSoggetto");
		log.debugEnd(methodName, "");
		return SUCCESS;
	}
	
	/**
	 * Metodo controlla dati
	 * @return
	 * @throws Exception
	 */
	public String controllaDati() throws Exception {
		setMethodName("controllaDati");

		debug(methodName, "radio selezionato "+model.getFlagResidenza());

		List<Errore> listaErrori = controllaCampiPrimaParte();
		List<Errore> listaMessaggi = new ArrayList<Errore>(listaErrori);
  
		String valoriCodiceFisc = checkCampiSoggettoBase(listaErrori, listaMessaggi);
		 //SIAC-6565-CR1215  
		checkCampiFEL(listaErrori);
				
		// catturo gli errori
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		} 
		
		if(StringUtils.isNotEmpty(valoriCodiceFisc)){
			// associo i valori del cf nei vari campi
			estrazioneDatiDaCodiceFiscale(valoriCodiceFisc);
		}
		
		// lancio la ricerca per CF o Partita IVa
		
		boolean codFiscaleControllo = false;
		boolean pIvaControllo = false;
		//istanzio la request per il servizio ricercaSoggetti:
		RicercaSoggetti rs = new RicercaSoggetti();
		ParametroRicercaSoggetto prs = new ParametroRicercaSoggetto();
		if(StringUtils.isNotEmpty(model.getCodiceFiscale()) && StringUtils.isNotEmpty(model.getPartitaIva())){
			// se tutti e 2 diversi da null cerco per CF
			prs.setCodiceFiscale(model.getCodiceFiscale());
			codFiscaleControllo = true;
			pIvaControllo = true;
			// setto il param ricerca di controlla dati
			model.setParametroRicercaControllaDati(model.getCodiceFiscale());			
		}else if(StringUtils.isNotEmpty(model.getCodiceFiscale())){
			prs.setCodiceFiscale(model.getCodiceFiscale());
			codFiscaleControllo = true;
			// setto il param ricerca di controlla dati
			model.setParametroRicercaControllaDati(model.getCodiceFiscale());
		}else{
			prs.setPartitaIva(model.getPartitaIva());
			pIvaControllo = true;
			// setto il param ricerca di controlla dati
			model.setParametroRicercaControllaDati(model.getPartitaIva());
		}
		
		rs.setParametroRicercaSoggetto(prs);
		rs.setRichiedente(sessionHandler.getRichiedente());
		rs.setEnte(sessionHandler.getRichiedente().getAccount().getEnte());
		
		RicercaSoggettiResponse response= null;
		if(!model.getFlagResidenza().equalsIgnoreCase("si") &&	!StringUtils.isEmpty(model.getCodiceFiscaleEstero())){
			//invoco il servizio ricercaSoggetti:
			response = soggettoService.ricercaSoggetti(rs);
		}
		
		if(response != null && response.isFallimento()){
			addErrori(response.getErrori());
			return INPUT;
		}
		
		// verfico che ci siano dei dati a db
		if(response!= null){
			if(null!=response.getSoggetti()){
				if(response.getSoggetti().size()>0){
					log.debug(methodName, " trovati record su db");
					String supportErrore = "";
					if (pIvaControllo && codFiscaleControllo) {
						supportErrore = "Codice Fiscale( " + model.getCodiceFiscale().toUpperCase() + " ) e Partita Iva( "+model.getPartitaIva()+" )";
					} else if (codFiscaleControllo) {
						supportErrore = model.getCodiceFiscale().toUpperCase()+" ";
					} else {
						supportErrore = " "+model.getPartitaIva();
					}
					listaMessaggi.add(ErroreFin.SOGGETTO_ESISTENTE.getErrore(supportErrore));
				}
			}
		}
	
		if(listaMessaggi!=null){			
			for(Errore r:listaMessaggi){
				addMessaggio(r);
			}
		}
		
		if(response!= null && null!=response.getSoggetti()){
			// riempio i soggetti
			model.setSoggetti(riempiElencoSoggFonti(response.getSoggetti()));
		}
		
		//  a seconda del risultato ottenuto dalla ricerca
		// abilito/disabilito la parte inferiore della pagina
			
		model.setEffettuataRicercaAnagrafica(true);
		
		if(hasActionMessages()) return INPUT;
		
		
		log.debugEnd(methodName, "");
		return SUCCESS;
	}

	/**
	 * @param listaErrori
	 * @param listaMessaggi
	 * @return
	 */
	private String checkCampiSoggettoBase(List<Errore> listaErrori, List<Errore> listaMessaggi) {
		// ricavo eventualmente i dati del codice fiscale
		String valoriCodiceFisc = "";

		if (StringUtils.isNotEmpty(model.getCodiceFiscale())){

			if ((model.getIdTipoSoggetto().equals("PG") || model.getIdTipoSoggetto().equals("PGI")) && model.getCodiceFiscale().matches("^[0-9]{11}$")) {
				if (!VerificaPartitaIva.controllaPIVA(model.getCodiceFiscale()).equalsIgnoreCase("OK")){
					listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore("Partita IVA"));
				}
			} else if (model.getIdTipoSoggetto().equals("PF") && model.getIdNaturaGiuridica().equals("BA")) {
				// SIAC-5856: Permette di inserire tipi soggetto PF con natura
				listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
			} else {

				log.debug(methodName, "calcolo il CF");

				CFGenerator cfGen = new CFGenerator();

				try {
					valoriCodiceFisc = cfGen.scorporaCF(model.getCodiceFiscale().toUpperCase());

					// verifico il codice di controllo
					ValidaCF validaCF = new ValidaCF();
					String validato = validaCF.controllaCF(model.getCodiceFiscale().toUpperCase());
					debug(validato, "Validatore finale codice fiscale " + validato);

					if (!validato.equalsIgnoreCase("OK"))
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Codice Fiscale", "codice fiscale"));

					// controllo univocita' codice fiscale
					
					//istanzio la request per il servizio:
					RicercaSoggetti rs = new RicercaSoggetti();
					ParametroRicercaSoggetto prs = new ParametroRicercaSoggetto();
					prs.setIncludeModif(true);
					prs.setCodiceFiscale(model.getCodiceFiscale().toUpperCase());
					rs.setParametroRicercaSoggetto(prs);
					rs.setRichiedente(sessionHandler.getRichiedente());
					rs.setEnte(sessionHandler.getRichiedente().getAccount().getEnte());

					rs.setCodiceAmbito(getCodiceAmbito());
					
					//invoco il servizio ricercaSoggetti:
					RicercaSoggettiResponse res = soggettoService.ricercaSoggetti(rs);

					if (res != null && res.getSoggetti() != null){
						Errore erroreSoggettoEsistente = ErroreFin.SOGGETTO_ESISTENTE.getErrore("Codice fiscale ( " + model.getCodiceFiscale().toUpperCase() + " )");
						if (isAzioneDecentrata(sessionHandler.getAzione().getNome())){
							// decentrato
							listaErrori.add(erroreSoggettoEsistente);
						} else {
							// amministratore
							listaMessaggi.add(erroreSoggettoEsistente);
						}
					}

					log.debug(methodName, "VALORI CF " + valoriCodiceFisc);
				} catch (Exception e){
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Codice Fiscale", "codice fiscale"));
				}

			}
		}
			
		
		// CONTROLLO CHE LA PARTITA IVA SIA UNIVOCA PER UN SOLO SOGGETTO
		if (model.getPartitaIva() != null && !model.getFlagResidenza().equalsIgnoreCase("si") && StringUtils.isEmpty(model.getCodiceFiscaleEstero())){
			
			//istanzio la request per il servizio ricercaSoggetti:
			RicercaSoggetti rs = new RicercaSoggetti();
			ParametroRicercaSoggetto prs = new ParametroRicercaSoggetto();
			prs.setIncludeModif(true);
			prs.setPartitaIva(model.getPartitaIva());
			rs.setParametroRicercaSoggetto(prs);
			rs.setRichiedente(sessionHandler.getRichiedente());
			rs.setEnte(sessionHandler.getRichiedente().getAccount().getEnte());
			
			//invoco il servizio ricercaSoggetti:
			RicercaSoggettiResponse response2 = soggettoService.ricercaSoggetti(rs);

			if(response2 != null && response2.getSoggetti() != null){
				Errore erroreSoggettoEsistente = ErroreFin.SOGGETTO_ESISTENTE.getErrore("Partita IVA ( "+ model.getPartitaIva() + " )");
				if (isAzioneDecentrata(sessionHandler.getAzione().getNome())){
					// decentrato
					listaErrori.add(erroreSoggettoEsistente);
				} else {
					// amministratore
					addMessaggio(erroreSoggettoEsistente);
				}
			}

		}
		return valoriCodiceFisc;
	}

	/**
	 * @param listaErrori
	 */
	protected void checkCampiFEL(List<Errore> listaErrori) {
		if (model.getCanalePA()!=null && !model.getCanalePA().isEmpty()) {
			if ("PA".equals(model.getCanalePA()))
			{
				if (model.getCodDestinatario()==null ||model.getCodDestinatario().length()!=6 )
					{
						Errore erroreSoggetto= ErroreFin.CANALEPA_ERROREPA.getErrore("Se il CanalePA e' PA, il cod Destinatario deve esssere lungo 6 caratteri");
						listaErrori.add(erroreSoggetto);
					}

			}
			else
			{
				if (model.getCodDestinatario()==null ||model.getCodDestinatario().length()!=7 )
				{
					if (model.getEmailPec()!=null && verificaEmailPEC(model.getEmailPec()))
					{
						model.setCodDestinatario("0000000");
					}
					else
					{
						Errore erroreSoggetto= ErroreFin.CANALEPA_ERROREPR.getErrore("Se il CanalePA e' PR, devi valorizzare corretamente l'emailPec o il codice destinatario deve eseere di 7 zeri");
						listaErrori.add(erroreSoggetto);
					}
				}
				
			}
		}
		else
		{
			Errore erroreSoggetto= ErroreFin.CANALEPA_ERRATO.getErrore("Il canalePA deve essere valorizzato");
			listaErrori.add(erroreSoggetto);
		}
	}
	

	protected String getCodiceAmbito() {
		return Constanti.AMBITO_FIN;
	}

	private void estrazioneDatiDaCodiceFiscale(String cfDaRimappare){
		setMethodName("estrazioneDatiDaCodiceFiscale");
		
		String[] tmp = StringUtils.split(cfDaRimappare, "||");
		
		String anno = tmp[0];
		String mese = tmp[1];
		String gg = tmp[2];
		String sesso = tmp[3];
		String codiceCatastale = tmp[4];
		
	
		
		if(StringUtils.isNotEmpty(gg)){
			
			
			int calcoloGiorno = Integer.valueOf(gg);
			if(calcoloGiorno>40){
				calcoloGiorno = calcoloGiorno - 40;
			}
			
			gg = String.valueOf(calcoloGiorno);
			
			if(gg.length()==1){
				gg = "0"+gg;
			}
			
		}
		
		if(StringUtils.isNotEmpty(mese)){
			if(mese.length()==1){
				mese = "0"+mese;
			}
		}
		
		// recupero l'id Della citta
		
		//istanzio la request per il servizio cercaComuni:
		ListaComuni listaComuni = new ListaComuni();
		listaComuni.setCodiceCatastale(codiceCatastale);
		listaComuni.setRichiedente(sessionHandler.getRichiedente());
		
		//invoco il servizio cercaComuni:
		ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

		if (!lcr.isFallimento()){
			log.debug(methodName, "Smazzato ID COMUNE " + lcr.getListaComuni());
			if (null != lcr.getListaComuni()){
				ComuneNascita comuneObj = lcr.getListaComuni().get(0);
				model.setIdComune(String.valueOf(comuneObj.getComuneIstatCode()));

				if (codiceCatastale.startsWith("Z")){
					model.setIdNazione(comuneObj.getNazioneCode());
					model.setComune(comuneObj.getNazioneDesc());
				} else {
					model.setComune(comuneObj.getDescrizione());
					model.setIdNazione(WebAppConstants.CODICE_ITALIA);
				}
			}
		}	
		
		
		model.setDataNascita(gg+"/"+mese+"/"+anno);
		if(sesso.equalsIgnoreCase(Constanti.SESSO_M)){
			model.setFlagSesso(Constanti.MASCHIO.toLowerCase());
		}else if(sesso.equalsIgnoreCase(Constanti.SESSO_F)){
			model.setFlagSesso(Constanti.FEMMINA.toLowerCase());
		}
		
	}


	@Deprecated
	/**
	 * era utilizzato nella prima versione
	 * @return
	 */
	public String cerca() {
	   return SUCCESS;
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		debug(methodName, "Eseguo inserisci soggetto");
		
		log.debugEnd(methodName, "");
		model.setIdNazione(WebAppConstants.CODICE_ITALIA);
		return SUCCESS;
	}
	
	/**
	 * controllo prima parte dei campi
	 * @return
	 */
	private List<Errore> controllaCampiPrimaParte(){
		setMethodName("controllaCampiPrimaParte");
		List<Errore> listaErrori = new ArrayList<Errore>();
		ValidationUtils.validaCampiInserimentoSoggettoPrimaParte(listaErrori, model);
		//ritorno gli errori:
		return listaErrori;
	}

	/**
	 * Metodo che controlla i campi
	 * @return
	 */
	private List<Errore> controllaCampi(){
		setMethodName("controllaCampi");
		debug(methodName, " controllo i campi"); 
		debug(methodName, "COMUNE HIDDEN " +model.getHiddenComune());
		List<Errore> listaErrori = new ArrayList<Errore>();
		//richiamiamo validation utils:
		ValidationUtils.validaCampiInserimentoSoggetto(listaErrori, model);
		return listaErrori;
	}
    
	/**
	 * evento pulsante prosegui
	 * @return
	 * @throws Exception
	 */
	public String prosegui() throws Exception {
		final String methodName = "prosegui";

		debug(methodName, " model --> "+model); 
		debug(methodName, " model sh --> "+sessionHandler); 
		
		List<Errore> listaErrori = controllaCampi();
		if(!(model.getIdTipoSoggetto().equals("PG") || model.getIdTipoSoggetto().equals("PGI"))){
			if(model.getFlagSesso() == null || StringUtils.isEmpty((model.getFlagSesso()))){
				listaErrori.add(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Sesso"));
			}
		}
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		}
		
		
		String[] idComune = (String[])getRequest().get("idComune");
		
		if(!(model.getIdTipoSoggetto().equals("PG") || model.getIdTipoSoggetto().equals("PGI")))
			if (idComune == null || "".equalsIgnoreCase(idComune[0])) {
				//istanzio la request per il servizio findComunePerNome:
				ListaComunePerNome comunePerNome = new ListaComunePerNome();
				comunePerNome.setCodiceNazione(model.getIdNazione());
				comunePerNome.setNomeComune(model.getComune());
				comunePerNome.setRichiedente(sessionHandler.getRichiedente());
				//invoco il servizio findComunePerNome:
				ListaComunePerNomeResponse response = genericService.findComunePerNome(comunePerNome);
				if (response != null && response.getListaComuni() != null && response.getListaComuni().size() > 0 && response.getListaComuni().get(0).getCodice() != null) {
					model.setIdComune(response.getListaComuni().get(0).getCodice());
				} else {
					model.setIdComune("");
				}
			}	
		
		String valoriCodiceFisc="";
		CFGenerator cfGen = new CFGenerator();
		if (org.apache.commons.lang.StringUtils.isBlank(model.getCodiceFiscaleEstero())) {
			if(!(cfGen.verificaFormaleCodiceFiscale(model.getCodiceFiscale().toUpperCase()) || cfGen.verificaFormaleCodiceFiscaleNumerico(model.getCodiceFiscale().toUpperCase()))){
				addActionError("Codice fiscale non formalmente valido");			
				return INPUT;
			}else if(cfGen.verificaFormaleCodiceFiscale(model.getCodiceFiscale().toUpperCase())){
				valoriCodiceFisc = cfGen.scorporaCF(model.getCodiceFiscale().toUpperCase());
				String temp=controlloCampiPopolatiDaCF(valoriCodiceFisc);
				if(temp.equals(INPUT)){
					return INPUT;
				}		
			}
		}
		 //SIAC-6565-CR1215  
		checkCampiFEL(listaErrori);
		if(listaErrori!=null && !listaErrori.isEmpty()){			
			for(Errore r:listaErrori){
				addMessaggio(r);
			}
			return INPUT;
		}
		
		
		return "prosegui";
	}	

	/**
	 * pulisce in campi della maschero
	 * @return
	 */
   public String pulisci(){
	   final String methodName = "pulisci";
	   
	   debug(methodName, " PULISCO"); 

	   model.setCodiceFiscale("");
	   model.setCodiceFiscaleEstero("");
	   model.setCognome("");
	   model.setComune("");
	   model.setDataNascita("");
	   model.setDenominazione("");
	   model.setFlagResidenza("no");
	   model.setHiddenComune("");
	   model.setIdNaturaGiuridica(null);
	   model.setIdNazione("");
	   model.setIdClasseSoggetto(new String[0]);
	   model.setIdTipoOnere(new String[0]);
	   model.setIdTipoSoggetto("");
	   model.setNome("");
	   model.setNote("");
	   model.setPartitaIva("");
	   model.setSesso("");
	   model.setTitolo("");
	   model.setIdNazione(WebAppConstants.CODICE_ITALIA);
	   model.setSoggetti(null);
	  
	   model.setEffettuataRicercaAnagrafica(false);
	   
	   sessionHandler.setParametro(FinSessionParameter.EFFETTUATA_RICERCA_IN_ANAGRAFICA, null);
	   
	   return SUCCESS;
   }
 	
	@Override
	public String getExcludedMethods() {
		return "execute";
	}
	
	/**
	 * Controllo campi popolati da codice
	 * fiscale
	 * 
	 * @param cfDaRimappare
	 * @return
	 */
	private String controlloCampiPopolatiDaCF(String cfDaRimappare){
		setMethodName("controlloCampiPopolatiDaCF");
		
		StringTokenizer st = new StringTokenizer(cfDaRimappare, "||");
		int cont = 0;
		String anno = "";
		String mese = "";
		String gg = "";
		String sesso = "";
		String citta = "";
		while (st.hasMoreElements()){
			
			String temp = (String)st.nextElement();
			if(cont==0){
				
				
				anno = temp;
			}
			if(cont==1){
				
				mese = temp;
			}
			if(cont==2){
				debug(methodName, " IL GIORNO E' "+temp);
				gg = temp;
			}
			if(cont==3){
				
				sesso = temp;
			}
			if(cont==4){
				
				citta = temp;
			}
			cont++;
		}
		
		if(StringUtils.isNotEmpty(gg)){
			
			
			int calcoloGiorno = Integer.valueOf(gg);
			if(calcoloGiorno>40){
				calcoloGiorno = calcoloGiorno - 40;
			}
			
			gg = String.valueOf(calcoloGiorno);
			
			if(gg.length()==1){
				gg = "0"+gg;
			}
			
		}
		
		if(StringUtils.isNotEmpty(mese)){
			if(mese.length()==1){
				mese = "0"+mese;
			}
		}
		
		
		ComuneNascita comune = cercaComune(citta);

		String comuneConfronto = comune != null ? comune.getDescrizione() : null;

	
		String dataConfronto=gg+"/"+mese+"/"+anno;
		String sessoConfronto="";
		
		
		if(sesso.equalsIgnoreCase(Constanti.SESSO_M)){
			sessoConfronto=Constanti.MASCHIO.toLowerCase();
		}else if(sesso.equalsIgnoreCase(Constanti.SESSO_F)){
			sessoConfronto=Constanti.FEMMINA.toLowerCase();
		}
		
		boolean error = false;
		

		if (comuneConfronto != null && (error = !model.getComune().equalsIgnoreCase(comuneConfronto))){
			addActionError("I valori inseriti non combaciano con il Codice Fiscale (comune di nascita)");
		} else if (error = !model.getDataNascita().equals(dataConfronto)){
			addActionError("I valori inseriti non combaciano con il Codice Fiscale (data di nascita)");
		} else if (error = !model.getFlagSesso().equalsIgnoreCase(sessoConfronto)){
			addActionError("I valori inseriti non combaciano con il Codice Fiscale (sesso)");
		}

		if (error){
			//errori
			return INPUT;
		}

		return "";
	}
	
	/**
	 * metodo per la ricerca del comune
	 * @param codiceCatastale
	 * @return
	 */
	private ComuneNascita cercaComune(String codiceCatastale) {
		//istanzio la request per il servizio cercaComuni:
		ListaComuni listaComuni = new ListaComuni();
		listaComuni.setCodiceCatastale(codiceCatastale);
		listaComuni.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio cercaComuni:
		ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

		if (!(lcr.isFallimento() || lcr.getListaComuni() == null || lcr.getListaComuni()
				.isEmpty()))
			return lcr.getListaComuni().get(0);
		
		return null;
			
	}

	
	//SIAC-6565-CR1215  
		private boolean verificaEmailPEC(String input) {
			boolean result = true;
			try {
				result = input
						.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				if (result) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	
}
