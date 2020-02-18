/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.SoggettoDaFontiAnagModel;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;

public abstract class SoggettoAction<M extends SoggettoModel> extends GenericFinAction<M> {

	private static final long serialVersionUID = 6331235948921990704L;
	
	@Autowired
	protected transient SoggettoService soggettoService;
	
	protected final static String ABILITAZIONE_INSERIMENTO_DECENTRATO = CodiciOperazioni.OP_SOG_inserisciSoggDec;
	protected final static String ABILITAZIONE_GESTIONE = CodiciOperazioni.OP_SOG_gestisciSogg;
	protected final static String ABILITAZIONE_GESTIONE_DECENTRATO = CodiciOperazioni.OP_SOG_gestisciSoggDec;
	protected final static String GOTO_ELENCO_SOGGETTI = "gotoElencoSoggetti"; 
	protected final static Integer STATO_VALIDO = 2;
	protected String methodName;
	
	//Codici relativi allo stato dei soggetti
	public enum CodiciStatoSoggetto{PROVVISORIO, VALIDO, ANNULLATO, BLOCCATO, SOSPESO, IN_MODIFICA}
	//Gestione con costanti numeriche relative alle possibili azioni per un soggetto
	protected final static int CONSULTA = 1;
	protected final static int AGGIORNA = 2;
	protected final static int ANNULLA = 3;
	protected final static int SOSPENDI = 4;
	protected final static int BLOCCA = 5;
	protected final static int CANCELLA = 6;
	protected final static int VALIDA = 7;
	protected final static int COLLEGA = 8;
    // Tipologie di soggetto	PF PFI PG PGI
	protected final static String PF = "PF";
	protected final static String PFI = "PFI";
	protected final static String PG = "PG";
	protected final static String PGI = "PGI";
		
	/**
	 * Effettua un controllo formale sul comune
	 * tramite l'utilizzo del servizio di ricerca
	 * @param comune
	 * @param stato
	 * @return
	 */
	public ListaComunePerNomeResponse controlloPuntualeComune(String comune, String stato) {
		//istanzio la request per il servizio findComunePerNome:
		ListaComunePerNome comunePerNome = new ListaComunePerNome();
		comunePerNome.setNomeComune(comune);
		comunePerNome.setCodiceNazione(stato);
		comunePerNome.setRichiedente(sessionHandler.getRichiedente());
		comunePerNome.setRicercaPuntuale(true);
		//invoco il servizio findComunePerNome:
		ListaComunePerNomeResponse comunePerNomeResponse = genericService.findComunePerNome(comunePerNome);
		return comunePerNomeResponse;
	}
	
	/**
	 * Verifica che ci siano nazioni nel model
	 * @return
	 */
	public boolean isNazioniPresenti(){
		//verifichiamo che model.nazioni sia valorizzato:
		return (model.getNazioni()!=null && model.getNazioni().size()>0);
	}	
	
	/**
	 * Costruisce una mappa la cui chiave e' l'id soggetto e il contenuto e' un oggetto SoggettoDaFontiAnagModel
	 * @param soggetti
	 * @return
	 */
	protected HashMap<String,SoggettoDaFontiAnagModel> riempiElencoSoggFonti(List<Soggetto> soggetti){
		
		HashMap<String,SoggettoDaFontiAnagModel>  ar = new HashMap<String,SoggettoDaFontiAnagModel>();
		
		//iteriamo sui soggetti:
		Iterator it = soggetti.iterator();
		
		while(it.hasNext()){
			//soggetto itarato
			Soggetto soggetto = (Soggetto)it.next();
			SoggettoDaFontiAnagModel sdfa = new SoggettoDaFontiAnagModel();
			
			sdfa.setIdSoggetto(String.valueOf(soggetto.getUid()));
			sdfa.setCodiceFiscale(soggetto.getCodiceFiscale());
			sdfa.setDescrizione(soggetto.getDenominazione());
			
			if(null!=soggetto.getTipoSoggetto()){
				//tipo soggetto
				sdfa.setTipo(soggetto.getTipoSoggetto().getDescrizione());
			}
			
			ar.put(sdfa.getIdSoggetto(), sdfa);
		}
	
		return ar;
	}

	/**
	 * Carica le liste per inserisci soggetto
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListeInserisciSoggetto(){
		final String methodName = "caricaListeInserisciSoggetto";
		log.debug(methodName, "carico le liste");
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, 
				                                                              TipiLista.NATURA_GIURIDICA, 
				                                                              TipiLista.NAZIONI, 
				                                                              TipiLista.GIURIDICA_SOGGETTO,
				                                                              TipiLista.TIPO_ONERE);
		//setto i dati nel model:
		model.setNazioni((List<CodificaFin>)mappaListe.get(TipiLista.NAZIONI));
		model.setListaNaturaGiuridica((List<CodificaExtFin>)mappaListe.get(TipiLista.GIURIDICA_SOGGETTO));
		model.setListaTipoSoggetto((List<CodificaExtFin>)mappaListe.get(TipiLista.NATURA_GIURIDICA));
		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
		model.setListaTipoOnere((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_ONERE));
		
		List<CodificaFin> codPA=new ArrayList<CodificaFin>();
		CodificaFin a=new CodificaFin();
		a.setCodice("PA");
		a.setDescrizione("PA");
		codPA.add(a);
		a=new CodificaFin();
		a.setCodice("PRIVATI");
		a.setDescrizione("PRIVATI");
		codPA.add(a);
		model.setListaCanalePA(codPA);
		

		debug(methodName, " FFFFFF "+model.getListaNaturaGiuridica());
	}
	
	/**
	 * cerca nella lista tipi accredito
	 * il tipo accredito indicato tramite
	 * l'id in input
	 * 
	 * @param idTipoAccredito
	 * @return
	 */
	protected TipoAccredito getTipoAccreditoById(Integer idTipoAccredito) {
		List<? extends CodificaFin> listaTipiAccredito = getCodifiche(TipiLista.TIPO_ACCREDITO).get(TipiLista.TIPO_ACCREDITO);
		//cicliamo:
		for (CodificaFin c : listaTipiAccredito){
			if (c.getId().equals(idTipoAccredito)){
				//trovato
				return TipoAccredito.valueOf(c.getCodice());
			}
		}
		return null;
	}
	
	/**
	 * Si occupa del caricamento della lista delle modalita'
	 * di pagamento
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListeModalitaPagamento(){
		final String methodName = "caricaListeModalitaPagamento";
		log.debug(methodName, "carico le liste");
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_ACCREDITO, TipiLista.NAZIONI);
		//setto i dati nel model:
		model.setListaTipoAccredito((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_ACCREDITO));
		model.setNazioni((List<CodificaFin>)mappaListe.get(TipiLista.NAZIONI));
	}
	
	/**
	 * Metodo che carica le liste da utilizzare nella maschera di ricerca del soggetto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListeRicercaSoggetto() {
		caricaListeInserisciSoggetto();
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.FORMA_GIURIDICA_TIPO, TipiLista.STATO_OPERATIVO_SOGGETTO);
		//setto i dati nel model:
		model.setListaFormaGiuridicaTipo((List<CodificaFin>)mappaListe.get(TipiLista.FORMA_GIURIDICA_TIPO));
		model.setListaStatoOperativoSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.STATO_OPERATIVO_SOGGETTO));		
	}
	
	/**
	 * Metodo che si occupa dai caricare le liste degli 
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListeInserisciIndirizzoSoggetto(){
		final String methodName = "caricaListeInserisciSoggetto";
		log.debug(methodName, "carico le liste");
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_INDIRIZZO_SEDE, 
				                                                              TipiLista.NAZIONI,
				                                                              TipiLista.RECAPITO_MODO);
		//setto i dati nel model:
		model.setListaTipoIndirizzoSede((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_INDIRIZZO_SEDE));
		model.setNazioni((List<CodificaFin>)mappaListe.get(TipiLista.NAZIONI));
		model.setListaRecapitoModo((List<CodificaFin>)mappaListe.get(TipiLista.RECAPITO_MODO));
		
	}
	
	/**
	 * Carica la lista comuni
	 * @param request
	 * @return
	 */
	protected ListaComuniResponse caricaComuni(ListaComuni request){
		//invoco il servizio cercaComuni:
		return genericService.cercaComuni(request);
	}
	
	/**
	 * Metodo che converte il model utilizzato per il binding della pagina web, nel dto utilizzato nel servizio di Elenco Soggetti
	 * @return RicercaSoggetti
	 */
	protected RicercaSoggettoPerChiave convertiModelPerChiamataServizioRicercaPerChiave(String codiceSoggetto) {
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		ricercaSoggettoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggettoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroRicercaSoggettoK);
		parametroRicercaSoggettoK.setIncludeModif(true);
		return ricercaSoggettoPerChiave;
	}
	
	
	/**
	 * Metodo che converte il model utilizzato per il binding della pagina web, nel dto utilizzato nel servizio di Elenco Soggetti
	 * e che restituisce sempre lo stato Valido anche se in modifica
	 * @return RicercaSoggetti
	 */
	protected RicercaSoggettoPerChiave convertiModelPerChiamataServizioRicercaPerChiavePerConsulta(String codiceSoggetto) {
		RicercaSoggettoPerChiave ricercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		ricercaSoggettoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggettoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(codiceSoggetto);
		ricercaSoggettoPerChiave.setParametroSoggettoK(parametroRicercaSoggettoK);
		parametroRicercaSoggettoK.setIncludeModif(false);
		return ricercaSoggettoPerChiave;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}