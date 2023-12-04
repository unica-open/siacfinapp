/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.Parameter;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.CFGenerator;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.VerificaPartitaIva;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorModalitaPagamentoSoggettoByCodice;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;




/**
 * 
 * @author paolos
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSoggettoAction extends AggiornaSoggettoGenericAction{

	private static final long serialVersionUID = -6769232992113508291L;

	private String soggetto;
	
	private final String GOTO_AGGIORNA_RECAPITI = "gotoAggiornaRecapiti";
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
				
		
		// carico le liste delle combo che mi servono nella pagina
		caricaListeInserisciSoggetto();
		
		
		model.setElencoStruttureAmministrativoContabiliJson(super.getAlberoStruttureAmministrativeContabiliJson(isDecentrato()));
		
		// carico radio button
		List<String> listaRadioResid = new ArrayList<String>();
	    listaRadioResid.add("si");
	    listaRadioResid.add("no");
	    model.setRadioResidenza(listaRadioResid);
	    
	    
	    // flag sesso
	    List<String> listaRadioSesso = new ArrayList<String>();
	    listaRadioSesso.add(CostantiFin.MASCHIO.toLowerCase());
	    listaRadioSesso.add(CostantiFin.FEMMINA.toLowerCase());
	    model.setRadioSesso(listaRadioSesso);
	    
	    // per evitare errori
	    // prendo da request il valore della request
	    
	    //per debug:
	    debug(methodName, " eccomi ");
	    
		//setto il titolo:
	    //task-224
	    if(AzioneConsentitaEnum.OP_CEC_SOG_leggiSogg.getNomeAzione().equals(sessionHandler.getAzione().getNome())) {
	    	this.model.setTitolo("Aggiorna Soggetto Cassa Economale");
		}else {
			this.model.setTitolo("Aggiorna Soggetto");	
		}
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		//info per debug:
		setMethodName("execute");
		debug(soggetto, "CODICE--> "+getSoggetto());
		
		
		if (forceReload) {
			//ricarichiamo i dati freschi
			
			//compongo la request per il servizio di ricerca soggetto
			RicercaSoggettoPerChiave ricercaSoggettoPerChiave = convertiModelPerChiamataServizioRicercaPerChiave(getSoggetto());
					
			//setto l'ambito:
			ricercaSoggettoPerChiave.setCodificaAmbito(getCodificaAmbito());
			
			//invoco il servizio di ricerca:
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(ricercaSoggettoPerChiave);
			
			//analizzo la response:
			if(response.isFallimento()) {
				//ci sono errori
				addErrori(methodName, response);
				return INPUT;
			}
			
			// converto la data
			setDataNascitaStringa(convertDateToString(response.getSoggetto().getDataNascita()));
	
			// setto il radio estero si/no
			if(response.getSoggetto().isResidenteEstero()) response.getSoggetto().setResidenteEsteroStringa("si");
			else response.getSoggetto().setResidenteEsteroStringa("no");
			
			// setto soggetto generale
			if(null!=response.getSoggettoInModifica() && response.getSoggettoInModifica().getCodiceSoggetto()!=null){
				debug(methodName, "Trovato soggetto in modifica");
				
				// setto il sesso
				if(!response.getSoggettoInModifica().getTipoSoggetto().getSoggettoTipoCode().equals("PG") && !response.getSoggettoInModifica().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
					//NON E' PG  PGI --> e' persona fisica
					if(response.getSoggettoInModifica().getSesso().equals(Sesso.MASCHIO)){
						//MASCHIO
						response.getSoggettoInModifica().setSessoStringa(CostantiFin.MASCHIO.toLowerCase());
					} else {
						//FEMMINA
						response.getSoggettoInModifica().setSessoStringa(CostantiFin.FEMMINA.toLowerCase());
					}
				}else{
					//E' PG  PGI --> persona giuridica
					ComuneNascita cn = new ComuneNascita();
					response.getSoggetto().setComuneNascita(cn);
					response.getSoggetto().getComuneNascita().setNazioneCode(WebAppConstants.CODICE_ITALIA);
				}
				
				//residenza estero:
				response.getSoggettoInModifica().setResidenteEsteroStringa(response.getSoggetto().getResidenteEsteroStringa());
				
				//dettaglio soggetto:
				model.setDettaglioSoggetto(response.getSoggettoInModifica());
				
				//contatti:
				if(model.getDettaglioSoggetto().getContatti()!=null && model.getDettaglioSoggetto().getContatti().size()>0){
					for(Contatto r :model.getDettaglioSoggetto().getContatti()){
						if(r.getAvviso().equalsIgnoreCase("s")){
							r.setAvviso("true");
						}
					}
				}
				
				//lista mod pag:
				model.setListaModalitaPagamentoSoggetto(response.getListaModalitaPagamentoSoggetto());
				model.getDettaglioSoggetto().setModalitaPagamentoList(response.getListaModalitaPagamentoSoggetto());
				
				//lista sedi:
				model.setListaSecondariaSoggetto(response.getListaSecondariaSoggetto());
				
				//Carico il soggetto da utilizzare per l'annullamento 
				model.setSoggettoPerAnnulla(clone(response.getSoggettoInModifica()));
				model.getSoggettoPerAnnulla().setModalitaPagamentoList(clone(response.getListaModalitaPagamentoSoggetto()));
				
			}else {
				debug(methodName, "NESSUN soggetto in modifica");
				if(!response.getSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PG") &&
				  !response.getSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
					
					
					if(Sesso.MASCHIO.equals(response.getSoggetto().getSesso())){
						//maschio
						response.getSoggetto().setSessoStringa(CostantiFin.MASCHIO.toLowerCase());
					} else if(Sesso.FEMMINA.equals(response.getSoggetto().getSesso())) {
						//femmina
						response.getSoggetto().setSessoStringa(CostantiFin.FEMMINA.toLowerCase());
					} else if(response.getSoggetto().getSesso()==null){
						//e' nullo puo' capitare per dati sporchi da migrazioni varie,
						//mi riconduco ad una casistica non definita:
						response.getSoggetto().setSesso(Sesso.NON_DEFINITO);
						response.getSoggetto().setSessoStringa("non definito");
					}
					
					
				}else{
					// persona giuridica non ha la nazione e la setto di default ad italia
					ComuneNascita cn = new ComuneNascita();
					response.getSoggetto().setComuneNascita(cn);
					response.getSoggetto().getComuneNascita().setNazioneCode(WebAppConstants.CODICE_ITALIA);
				}
			
				model.setDettaglioSoggetto(response.getSoggetto());
				if(model.getDettaglioSoggetto().getContatti()!=null && model.getDettaglioSoggetto().getContatti().size()>0){
					for(Contatto r :model.getDettaglioSoggetto().getContatti()){
						if(r.getAvviso().equalsIgnoreCase("s")){
							r.setAvviso("true");
						}
					}
				}
				model.setListaModalitaPagamentoSoggetto(response.getListaModalitaPagamentoSoggetto());
				model.setListaSecondariaSoggetto(response.getListaSecondariaSoggetto());
				model.getDettaglioSoggetto().setModalitaPagamentoList(response.getListaModalitaPagamentoSoggetto());
				//Carico il soggetto da utilizzare per l'annullamento 
				model.setSoggettoPerAnnulla(clone(response.getSoggetto()));
				model.getSoggettoPerAnnulla().setModalitaPagamentoList(clone(response.getListaModalitaPagamentoSoggetto()));

			}
			log.debug(methodName, "sono qui");
			
			// parte per inserimento indirizzi
			indirizzoSoggetto = new IndirizzoSoggetto();
			
			//gestione id provvisorio per inserimento
			idProvvisorio = 0;
		}
		
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = model.getDettaglioSoggetto().getModalitaPagamentoList();
		
		if (modalitaPagamentoList != null){
			//riordiniamo le modalita di pagamento per codice
			Collections.sort(modalitaPagamentoList, ComparatorModalitaPagamentoSoggettoByCodice.INSTANCE);
		}
			
		//richiamo la execute della super classe:
	    return super.execute();
	}
	
	protected String getCodificaAmbito() {
		return null;
	}

	/**
	 * Controllo dei valori immessi
	 * @return
	 */
	private List<Errore>  controllaCampi(){ 
		
		//info per debug:
		setMethodName("controllaCampi");
		debug(methodName, " controllo i campi"); 
		debug(methodName, "COMUNE HIDDEN " +model.getDettaglioSoggetto().getComuneNascita().getCodiceIstat());
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		ValidationUtils.validaCampiAggiornamentoSoggetto(listaErrori, model, getDataNascitaStringa());
		
		if( !model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PG") &&
				  !model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
		
			if(null!=model.getDettaglioSoggetto().getCodiceFiscale() && !StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceFiscale()) && !model.getDettaglioSoggetto().isResidenteEstero()){
				
				// verifica sintattica CF
				if(!verificaFormaleCodiceFiscale(model.getDettaglioSoggetto().getCodiceFiscale())){
					//errore nel codice fiscale
					listaErrori.add(ErroreFin.CAMPO_ERRATO.getErrore("Codice fiscale"));
				}
					 
			}
			
			// controllo la data di nascita per le persone
			if(getDataNascitaStringa().length()!=10){
				//errore sulla lunghezza della data di nascita
				log.debug(methodName, "errore sulla lunghezza");
				listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Nascita","dd/mm/yyyy"));
			}else {
				if (!DateUtility.isDate(getDataNascitaStringa(), "dd/MM/yyyy")){
					//errore formato della data di nascita
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Nascita","dd/mm/yyyy"));
				}
			}
			
		}
		
		//caso PFI:
		if(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PFI")){
			  if(StringUtils.isEmpty(model.getDettaglioSoggetto().getPartitaIva())){
				listaErrori.add(ErroreFin.PARTITA_IVA_OBBLIGATORIO.getErrore("Partita IVA"));
			  } else if(!VerificaPartitaIva.controllaPIVA(model.getDettaglioSoggetto().getPartitaIva()).equalsIgnoreCase("OK")){
			       log.debug(methodName, "errore nella partita iva ");
			       //errore partita iva estero
			       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore("Partita IVA"));
		      }
			  
		}
		
		return listaErrori;
	}
	
	/**
	 * Esegue la verifica formale del codice fiscale
	 * @param cf
	 * @return
	 */
	private boolean verificaFormaleCodiceFiscale(String cf){
		  if(null!=cf) cf = cf.toUpperCase();
		  return new CFGenerator().verificaFormaleCodiceFiscale(cf);
	 }
	
	public String prosegui() throws Exception {
		setMethodName("prosegui");
		
		debug(methodName, "Tipo soggetto " +model.getIdTipoSoggetto());
		debug(methodName, "Natura giurid " +model.getIdNaturaGiuridica());
		
		debug(methodName, "presenza in request "+getRequest().containsKey("dettaglioSoggetto.tipoOnereId"));
		
		//task-131 
		//String[] idComune = (String[])getRequest().get("dettaglioSoggetto.comuneNascita.uid");
		Parameter idComune = getRequest().get("dettaglioSoggetto.comuneNascita.uid");
		//if (idComune == null || "".equalsIgnoreCase(idComune[0])) {
		if (idComune == null || "".equalsIgnoreCase(idComune.getValue())) {
			//id comune non presente, va cercato per nome
			
			//preparo la request:
			ListaComunePerNome comunePerNome = new ListaComunePerNome();
			comunePerNome.setCodiceNazione(model.getDettaglioSoggetto().getComuneNascita().getNazioneCode());
			comunePerNome.setNomeComune(model.getDettaglioSoggetto().getComuneNascita().getDescrizione());
			comunePerNome.setRichiedente(sessionHandler.getRichiedente());
			
			//invoco il servizio di ricerca per nome:
			ListaComunePerNomeResponse response = genericService.findComunePerNome(comunePerNome);
			
			//analizzo la response del servizio:
			if (response != null && response.getListaComuni() != null && response.getListaComuni().size() > 0 && response.getListaComuni().get(0).getCodice() != null) {
				//comune trovato
				model.getDettaglioSoggetto().getComuneNascita().setCodiceIstat(response.getListaComuni().get(0).getCodice());
				model.setCodiceIstatComune(response.getListaComuni().get(0).getCodice());
			} else {
				//comune non trovato
				model.getDettaglioSoggetto().getComuneNascita().setCodiceIstat("");
				model.setCodiceIstatComune("");
			}
		}
		
		// se deseleziono tutti i valori setto cmq a null
		// cosi verra' tenuto nel model il valore vuoto
 		if(!getRequest().containsKey("dettaglioSoggetto.tipoOnereId")){
		   	model.getDettaglioSoggetto().setTipoOnereId(null);
		}
		
 		//tipo classificazione soggetto:
		if(!getRequest().containsKey("dettaglioSoggetto.tipoClassificazioneSoggettoId")){ 
            model.getDettaglioSoggetto().setTipoClassificazioneSoggettoId(null);
		}	
		
		//Residente Estero:
		if (model.getDettaglioSoggetto().getResidenteEsteroStringa() != null && "si".equalsIgnoreCase(model.getDettaglioSoggetto().getResidenteEsteroStringa())) {
			model.getDettaglioSoggetto().setResidenteEstero(true);
		} else {
			model.getDettaglioSoggetto().setCodiceFiscaleEstero(null);
			model.getDettaglioSoggetto().setResidenteEstero(false);
		}
		
		List<Errore> listaErrori = controllaCampi();

		// controllo esistenza del soggetto:
		
		//preaparo la requeste per il servizio di ricerca dei soggetti
		RicercaSoggetti rs = new RicercaSoggetti();
		ParametroRicercaSoggetto prs = new ParametroRicercaSoggetto();
		prs.setIncludeModif(true);
		prs.setCodiceFiscale(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase());
		rs.setParametroRicercaSoggetto(prs);
		rs.setRichiedente(sessionHandler.getRichiedente());
		rs.setEnte(sessionHandler.getRichiedente().getAccount().getEnte());
		
		//invoco il servizio ricercaSoggetti:
		RicercaSoggettiResponse res = soggettoService.ricercaSoggetti(rs);

		//analizzo la response ottenuta:
		if (res != null && res.getSoggetti() != null){
			for (Soggetto s : res.getSoggetti()){
				if (!s.getCodiceSoggetto().equals(getSoggetto())) {
					Errore erroreSoggettoEsistente = ErroreFin.SOGGETTO_ESISTENTE.getErrore("Codice fiscale ( " + model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase()+ " )");
					if (isAzioneDecentrata(sessionHandler.getAzione().getNome())){
						// decentrato
						listaErrori.add(erroreSoggettoEsistente);
					} else {
						// amministratore
						addActionWarning(erroreSoggettoEsistente.getTesto());
					}
					break;
				}
			}
		}
		
//		SIAC-6565-CR1215
		checkCampiFEL(listaErrori);
		
		
		
		// se tutto ribalto le date
		debug(methodName, "valore data nascita "+getDataNascitaStringa());
		debug(methodName, "congnome "+model.getDettaglioSoggetto().getNome());
		
		model.getDettaglioSoggetto().setDataNascita(DateUtility.parse(getDataNascitaStringa()));
		
		//Controllo la congruenza tra CF e relativi campi			
		String valoriCodiceFisc="";
		CFGenerator cfGen = new CFGenerator();
		if(model.getDettaglioSoggetto().isResidenteEstero()){
			
			if(StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceFiscaleEstero())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Fiscale Estero"));
			}
			
		}else if(!(cfGen.verificaFormaleCodiceFiscale(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase().trim()) || cfGen.verificaFormaleCodiceFiscaleNumerico(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase().trim()))){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("codice fiscale", "Codice fiscale non formalmente valido"));			
			return INPUT;
		}else{
			if (cfGen.verificaFormaleCodiceFiscale(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase().trim())){
				if (!model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PG") && !model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
					valoriCodiceFisc = cfGen.scorporaCF(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase().trim());
					controlloCampiPopolatiDaCF(valoriCodiceFisc, listaErrori);
				}
			} else if(!cfGen.verificaFormaleCodiceFiscaleNumerico(model.getDettaglioSoggetto().getCodiceFiscale().toUpperCase().trim())){
				//perche' qui non fa nulla
			}
		}
		
		
		if(!listaErrori.isEmpty()) {
			// ci sono errori
			addErrori(listaErrori);
			return INPUT;
		} 
		
		return GOTO_AGGIORNA_RECAPITI;
	}

	/**
	 * @param listaErrori
	 */
	protected void checkCampiFEL(List<Errore> listaErrori) {
		if (model.getDettaglioSoggetto().getCanalePA()!=null && !model.getDettaglioSoggetto().getCanalePA().isEmpty()) {
			if ("PA".equals(model.getDettaglioSoggetto().getCanalePA()))
			{
				if (model.getDettaglioSoggetto().getCodDestinatario()==null ||model.getDettaglioSoggetto().getCodDestinatario().length()!=6 )
					{
					Errore erroreSoggetto= ErroreFin.CANALEPA_ERROREPA.getErrore("Se il CanalePA e' PA, il cod Destinatario deve esssere lungo 6 caratteri");
						listaErrori.add(erroreSoggetto);
					}

			}
			else
			{
				if (model.getDettaglioSoggetto().getCodDestinatario()==null ||model.getDettaglioSoggetto().getCodDestinatario().length()!=7 )
				{
					if (model.getDettaglioSoggetto().getEmailPec()!=null && verificaEmailPEC(model.getDettaglioSoggetto().getEmailPec()))
					{
						model.getDettaglioSoggetto().setCodDestinatario("0000000");
					}
					else
					{
						Errore erroreSoggetto= ErroreFin.CANALEPA_ERROREPR.getErrore("Se il CanalePA e PR, devi valorizzare corretamente l'emailPec o il codice destinatario deve eseere di 7 zeri");
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
	
	
	private void controlloCampiPopolatiDaCF(String cfDaRimappare, List<Errore> listaErrori){
		setMethodName("controlloCampiPopolatiDaCF");
		
		
		StringTokenizer st = new StringTokenizer(cfDaRimappare, "||");
		int cont = 0;
		String anno = "";
		String mese = "";
		String gg = "";
		String sesso = "";
		String codiceCatastaleComune = "";
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
				codiceCatastaleComune = temp;
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
		
		String dataConfronto=anno+"-"+mese+"-"+gg;
		String sessoConfronto="";
		
		if(sesso.equalsIgnoreCase(CostantiFin.SESSO_M)){
			//MASCHIO
			sessoConfronto=CostantiFin.MASCHIO.toLowerCase();
		}else if(sesso.equalsIgnoreCase(CostantiFin.SESSO_F)){
			//FEMMINA
			sessoConfronto=CostantiFin.FEMMINA.toLowerCase();
		}
		
		if (! validNazioneOComune(codiceCatastaleComune)) {
			listaErrori.add(new Errore("", "Stato o comune di nascita non coerenti con il codice fiscale"));
		}

		if (dataConfronto != null && !dataConfronto.equals(ObjectUtils.toString(model.getDettaglioSoggetto().getDataNascita()))){
			listaErrori.add(new Errore("", "Data di nascita non coerente con il codice fiscale"));
		}

		if (sessoConfronto != null && !sessoConfronto.equalsIgnoreCase(ObjectUtils.toString(model.getDettaglioSoggetto().getSessoStringa()))){
			listaErrori.add(new Errore("", "Sesso non coerente con il codice fiscale"));
		}
	}
	
	private boolean validNazioneOComune(String codiceCatastaleComune) {
		List<ComuneNascita> comuni = cercaComune(codiceCatastaleComune);

		if (comuni == null || comuni.isEmpty()) {
			return true;
		}
		
		for (ComuneNascita comune : comuni) {
			if (!WebAppConstants.CODICE_ITALIA.equals(comune.getNazioneCode())) {
				if (model.getDettaglioSoggetto().getComuneNascita().getNazioneCode().equalsIgnoreCase(comune.getNazioneCode())) {
					return true;
				}
			} else if (comune.getDescrizione() != null && comune.getDescrizione().equalsIgnoreCase(model.getDettaglioSoggetto().getComuneNascita().getDescrizione())){
				return true;
			}
		}

		return false;
	}

	/**
	 * Gestisce la chiamata a cercaComuni
	 * @param codiceCatastale
	 * @return
	 */
	private List<ComuneNascita> cercaComune(String codiceCatastale) {
		
		//Compongo la request per il servizio:
		ListaComuni listaComuni = new ListaComuni();
		listaComuni.setCodiceCatastale(codiceCatastale);
		listaComuni.setRichiedente(sessionHandler.getRichiedente());

		//Invoco il servizio:
		ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

		//Analizzo la response:
		if (!(lcr.isFallimento() || lcr.getListaComuni() == null || lcr.getListaComuni().isEmpty())){
			//comune trovato
			return lcr.getListaComuni();
		}
		
		//comune non trovato:
		return null;
	}

	public String pulisci(){
	  setMethodName("pulisci");
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
	   
	   return SUCCESS;
	}
	
	public String annullaAggiornaSoggetto() throws Exception{
		model.setDettaglioSoggetto(clone(model.getSoggettoPerAnnulla()));
		model.getDettaglioSoggetto().setModalitaPagamentoList(clone(model.getSoggettoPerAnnulla().getElencoModalitaPagamento()));
		return INPUT;
	}

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}
	
}
