/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciLiquidazioneStep3AggiornaAction extends WizardInserisciLiquidazioneAction{
	
	private static final long serialVersionUID = 1L;
	private static final String ANNULLA = "annulla";	
	private static final String MODPAGAMENTO = "modpagamento";
	private static final String SEDISECONDARIE = "sedisecondarie";
	private static final String TRX = "trx";
	private static final String SALVA = "salva";
	
	private Integer anno;
	private BigDecimal numero;	
	
	private String arrivoDaInserimento;
	
	private static final String SI = "Si";
	
	private CapitoloImpegnoModel capitoloLiquidazioneModel; 
	
	public boolean isAbilitatoAggiornamentoCampiSiopePlus(){
		//il siope pluse e' aggiornabile se
		//la liquidazione e' provvisoria
		return StatoOperativoLiquidazione.PROVVISORIO.equals(model.getStatoOperativoLiquidazioneDaAggiornare());
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Aggiornamento Liquidazione");
		this.model.setStep("INSLIQSTEP3");
		
		//lista motivazioni assenza cig:
		caricaListaMotivazioniAssenzaCig();
		
		//tipo debito siope:
	    model.setScelteTipoDebitoSiope(buildListaTipoDebitoSiope());
		
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.LIQUIDAZIONE;
	}	
	
	/**
	 * Per la jsp provvedimentoLiquidazione
	 * 
	 * Creo questo metodo solo per compatibilita' ritorna sempre false
	 * 
	 * @return
	 */
	public boolean checkProvvedimentoStato(){
		return false;
	}
	
	/**
	 * Per verifica se il provvedimento e' modificabile
	 * @return
	 */
	public boolean isProvvedimentoModificabile(){
		
		//Vediamo se ci sono ordinativi collegati:
		boolean presenzaOrdinativi = isPresenzaOrdinativiPerLaLiquidazione();
		
		if(presenzaOrdinativi){
			//NON MODIFICABILE SE CI SONO ORDINATIVI
			return false;
		}
		
		//Controlliamo se e' in stato valido:
		boolean statoValidoLiquidazione = model.getStatoOperativoLiquidazioneDaAggiornare().equals(StatoOperativoLiquidazione.VALIDO);
		if(statoValidoLiquidazione){
			//NON MODIFICABILE SE STATO VALIDO
			return false;
		}
		
		//Presenza documento spesa:
		boolean presenzaDocumentoSpesa = isPresenzaSubDocumentoSpesa();
		if(presenzaDocumentoSpesa){
			//NON MODIFICABILE SE PRESENTE DOCUMENTO
			return false;
		}
		
		return true;
	}
	
	/**
	 * Execute della action
	 */
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		model.setListaModalitaPagamentoSoggetto(model.getListaModalitaPagamentoSoggettoOrigine());
		
		//TIPO CONVALIDA:
		if(model.getTipoConvalidaList()==null || (model.getTipoConvalidaList()!=null && model.getTipoConvalidaList().size()==0)){
			CodificaFin manuale = new CodificaFin();
			manuale.setId(1);
			manuale.setCodice("manuale");
			manuale.setDescrizione("Manuale");
			CodificaFin automatico = new CodificaFin();
			automatico.setId(2);
			automatico.setCodice("automatico");
			automatico.setDescrizione("Automatico");
			model.getTipoConvalidaList().add(manuale);
			model.getTipoConvalidaList().add(automatico);
		}
		
		//arrivo da inserimento o da indietro:
		if(getArrivoDaInserimento()!=null){
			if(getArrivoDaInserimento().equalsIgnoreCase(SI)){
				model.setFlagIndietro(true);
			}	
		} else if(model.getFlagIndietro()==null){
			model.setFlagIndietro(false);
		}
		
		//Chiamo il servizio per recuperare tutte le info su Liquidazione appena inserita o in consulta da ricerca
		
		//costruisco la request per il servizio:
		RicercaLiquidazionePerChiave req = componiRequestRicercaLiquidazionePerChiave();
		
		//Invoco il servizio:
		RicercaLiquidazionePerChiaveResponse response = liquidazioneService.ricercaLiquidazionePerChiave(req);
		
		//Analizzo la risposta del servizio:
		if(response.getLiquidazione()!=null){
			this.model.setListaOrdinativi(response.getLiquidazione().getListaOrdinativi());
		}
		
		//Puliamo la transazione elmenetare:
		pulisciTransazioneElementare();
		
		//settiamo la tipologia di oggetto per la transazione elementare:
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.LIQUIDAZIONE.toString());
		
		if(response!=null && response.getLiquidazione()!=null && response.getEsito().equals(Esito.SUCCESSO)){
			//esito positivo vado avanti a settare i dati nel model:
			caricaModuliLiquidazione(response.getLiquidazione());
		}else{
			//esito negativo, presentiamo gli errori:
			if (!response.getErrori().isEmpty()) {
				addErrori(response.getErrori());
				return INPUT;
			}
		}

		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		
		teSupport.setRicaricaStrutturaAmministrativa(true);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		return "success";
	}
	
	/**
	 * Metodo di comodo per comporre la
	 * request per il servizio di ricerca liquidazione
	 * @return
	 */
	private RicercaLiquidazionePerChiave componiRequestRicercaLiquidazionePerChiave(){
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		RicercaLiquidazioneK prl = new RicercaLiquidazioneK(); 
		Liquidazione liq = new Liquidazione();
		
		//prendo anno e numero dalle variabili di classe
		//e li  setto nel model 
		//(altrimenti al prossimo passaggio dalla execute andrebbero persi):
		if(anno != null && numero!= null){	
			//ANNO LIQUIDAZIONE:
			model.setAnnoLiquidazioneConsulta(anno);
			//NUMERO LIQUIDAZIONE:
			model.setNumeroLiquidazioneConsulta(numero);
		}	
		//setto anno e numero nella request:
		liq.setAnnoLiquidazione(model.getAnnoLiquidazioneConsulta());
		liq.setNumeroLiquidazione(model.getNumeroLiquidazioneConsulta());
		
		prl.setLiquidazione(liq);
		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);
		
		//ANNO ESERCIZIO:
		prl.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//ENTE:
		req.setEnte(sessionHandler.getAccount().getEnte());
		
		//RICHIEDENTE:
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setDataOra(new Date());
		req.setpRicercaLiquidazioneK(prl);
		
		//RITORNO LA REQUEST AL CHIAMANTE:
		return req;
	}
	
	/**
	 * Per verificare la presenza di sub documento spesa
	 * @return
	 */
	public boolean isPresenzaSubDocumentoSpesa(){
		boolean presente = false;
		if(model!=null && model.getSubdocumentoSpesa()!=null){
			//sub doc presente
			presente = true;
		}
		return presente;
	}
	
	/**
	 * Data una liquidazione appena caricata, ne setta i dati dentro al model
	 * @param liquidazione
	 */
	@SuppressWarnings("unchecked")
	private void caricaModuliLiquidazione(Liquidazione liquidazione) {
		
		//Settiamo SubdocumentoSpesa:
		model.setSubdocumentoSpesa(liquidazione.getSubdocumentoSpesa());
		
		//Settiamo AnnoLiquidazioneScheda :
		model.setAnnoLiquidazioneScheda(liquidazione.getAnnoLiquidazione());
		
		//Settiamo NumeroLiquidazioneScheda :
		model.setNumeroLiquidazioneScheda(liquidazione.getNumeroLiquidazione());
		
		//Settiamo ImportoLiquidazioneScheda:
		model.setImportoLiquidazioneScheda(liquidazione.getImportoLiquidazione());
		
		model.setModpagamentoSelezionata(liquidazione.getModalitaPagamentoSoggetto());
		
		//Settiamo DescrizioneLiquidazioneScheda :
		model.setDescrizioneLiquidazioneScheda(liquidazione.getDescrizioneLiquidazione());
		
		//Settiamo StatoLiquidazioneScheda:
		if(liquidazione.getStatoOperativoLiquidazione()!=null){
			model.setStatoLiquidazioneScheda(liquidazione.getStatoOperativoLiquidazione().name());
		}
		
		//Settiamo DisponibilePagareLiquidazioneDaAggiornare:
		model.setDisponibilePagareLiquidazioneDaAggiornare(liquidazione.getDisponibilitaPagare());
		
		//Settiamo ImportoLiquidazioneDaAggiornare:
		model.setImportoLiquidazioneDaAggiornare(liquidazione.getImportoLiquidazione());
		
		//Settiamo StatoOperativoLiquidazioneDaAggiornare:
		model.setStatoOperativoLiquidazioneDaAggiornare(liquidazione.getStatoOperativoLiquidazione());
		
		//Settiamo LiqManualeDaAggiornare:
		model.setLiqManualeDaAggiornare(liquidazione.getLiqManuale());
		
		// setto distinta
		if(liquidazione.getDistinta()!=null){
			model.setDistinta(liquidazione.getDistinta().getCodice());
		}
		// setto conto tesoreria
		if(liquidazione.getContoTesoreria()!=null){
			model.setContoTesoriere(liquidazione.getContoTesoreria().getCodice());
		}
		
		if(liquidazione.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.VALIDO)){
			
			//CASO STATO VALIDO
			
			//CR  SIAC-3601
			if(isEnteAbilitatoAggiornaImportoLiq()){
				model.setLockImportoLiquidazione(false);
			} else {
				model.setLockImportoLiquidazione(true);
			}
			//
			
			model.setLockProvvedimento(true);
			model.setLockTipoConvalida(false);
		}else if(liquidazione.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.PROVVISORIO)){
			
			//CASO STATO PROVVISORIO
			
			model.setLockImportoLiquidazione(false);
			model.setLockProvvedimento(false);
			model.setLockTipoConvalida(true);
		}else{
			
			//CASO NE VALIDO NE PROVVISORIO, DOVREBBE ESSERE ANNULLATO
			
			model.setLockImportoLiquidazione(false);
			model.setLockProvvedimento(false);
			model.setLockTipoConvalida(false);
		}
		
		//in caso di presenza sub documento spesa
		//l'importo deve essere lockato:
		if(isPresenzaSubDocumentoSpesa()){
			model.setLockImportoLiquidazione(true);
		}
		
		//IMPEGNO:
		model.setImpegno(liquidazione.getImpegno());
		model.setImpegnoPerServizio(liquidazione.getImpegno());
		
		//CAPITOLO:
		model.setCapitolo(liquidazione.getCapitoloUscitaGestione());				
		
		//converto capitoloUscita in capitoloImpegnoModel per richiamare il metodo centralizzato dei dati del capitolo
		setCapitoloLiquidazioneModel(convertiCapitoloToCapitoloModel(model.getCapitolo()));
		//seleziono il capitolo per la visualizazzione dei dettagli
		setCapitoloSelezionato(getCapitoloLiquidazioneModel());
		
		//DATI IMPEGNO DA model.impegno a variabili piatte nel model:
		if(model.getImpegno()!=null){
			model.setAnnoImpegno(model.getImpegno().getAnnoMovimento());
			model.setNumeroImpegno(model.getImpegno().getNumero().intValue());
			model.setDisponibilita(model.getImpegno().getDisponibilitaLiquidare());
		}
		
		//JIRA 858
		List<VoceMutuo> listaVociMutuo = new ArrayList<VoceMutuo>();
		if(model.getImpegno()!=null && model.getImpegno().getListaVociMutuo()!=null && model.getImpegno().getListaVociMutuo().size()>0){
			listaVociMutuo = model.getImpegno().getListaVociMutuo();
		}
		model.setListaVociMutuo(listaVociMutuo);
		
		//se c'e' il subimpegno
		if(model.getImpegno().getElencoSubImpegni()!=null && !model.getImpegno().getElencoSubImpegni().isEmpty()){
			SubImpegno subImpegno=model.getImpegno().getElencoSubImpegni().get(0);
			if(subImpegno!=null){
				if(!subImpegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(Constanti.MOVGEST_STATO_ANNULLATO)){
					model.setNumeroSub(subImpegno.getNumero().intValue());
					model.setDisponibilita(subImpegno.getDisponibilitaLiquidare());
	
					//JIRA 858
					if(subImpegno.getListaVociMutuo()!=null && subImpegno.getListaVociMutuo().size()>0){
						listaVociMutuo = subImpegno.getListaVociMutuo();
					}
					model.setListaVociMutuo(listaVociMutuo);
				}
			}
		}
		
		//numero mutuo per pop up:
		model.setNumeroMutuoPopup(liquidazione.getNumeroMutuo());
		
		//JIRA 858
		if(model.getListaVociMutuo()!=null && model.getListaVociMutuo().size()>0){
			for(int j=0; j<model.getListaVociMutuo().size();j++){
				if(model.getNumeroMutuoPopup()!=null && model.getListaVociMutuo().get(j).getNumeroMutuo().equals(model.getNumeroMutuoPopup().toString())){
					model.setDisponibilita(model.getListaVociMutuo().get(j).getImportoDisponibileLiquidareVoceMutuo());
					model.setDescrizioneMutuoPopup(model.getListaVociMutuo().get(j).getDescrizioneMutuo());
				}
			}
			//: Se corrispondenzaMutuo e' FALSE mando messaggio all'utente?
		}

		//PROVVEDIMENTO:
		AttoAmministrativo provvedimento = liquidazione.getAttoAmministrativoLiquidazione();
		if (provvedimento != null) {		
			model.setAnnoAttoAmmDaAggiornare(provvedimento.getAnno());
			model.setNumeroAttoAmmDaAggiornare(provvedimento.getNumero());
			if(provvedimento.getTipoAtto()!=null){
				model.setCodiceTipoAttoAmmDaAggiornare(provvedimento.getTipoAtto().getCodice());
			}
			//ISTANZIO UN NUOVO OGGETTO:
			model.setProvvedimento(new ProvvedimentoImpegnoModel());
			if(provvedimento!=null){
				model.setStatoOperativoAttoAmmDaAggiornare(provvedimento.getStatoOperativo());
				//VALORIZZO I CAMPI DELL'OGGETTO:
				impostaProvvNelModel(provvedimento, model.getProvvedimento());
			}
			model.setProvvedimentoSelezionato(true);
		}
		
		if (liquidazione.getSoggettoLiquidazione() != null && liquidazione.getSoggettoLiquidazione().getCodiceSoggetto() != null) {
			model.setSoggetto(new SoggettoImpegnoModel());
			model.getSoggetto().setCodCreditore(liquidazione.getSoggettoLiquidazione().getCodiceSoggetto());
			model.getSoggetto().setDenominazione(liquidazione.getSoggettoLiquidazione().getDenominazione());
			model.getSoggetto().setCodfisc(liquidazione.getSoggettoLiquidazione().getCodiceFiscale());
			model.getSoggetto().setStato(liquidazione.getSoggettoLiquidazione().getStatoOperativo());
			model.setSoggettoSelezionato(true);			
			model.setListaSediSecondarieSoggetto(liquidazione.getSoggettoLiquidazione().getSediSecondarie());
			
			//jira 1406
			model.setListaModalitaPagamentoSoggetto(FinUtility.filtraValidi(liquidazione.getSoggettoLiquidazione().getModalitaPagamentoList()));
			model.setListaModalitaPagamentoSoggettoOrigine(FinUtility.filtraValidi(liquidazione.getSoggettoLiquidazione().getModalitaPagamentoList()));
		}
		
		model.setDescrizioneLiquidazione(liquidazione.getDescrizioneLiquidazione());
		if(liquidazione.getImportoLiquidazione()!=null){
			model.setImportoLiquidazione(convertiBigDecimalToImporto(liquidazione.getImportoLiquidazione()));
		}		
		
		
		if(liquidazione.getSedeSecondariaSoggetto()!=null){
			model.setRadioSediSecondarieSoggettoSelezionato(liquidazione.getSedeSecondariaSoggetto());
			
			//inizio issue 1147
			if(liquidazione.getSedeSecondariaSoggetto()!=null){
				List<ModalitaPagamentoSoggetto> relistaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
				List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamentoSoggettoOrigine();
				List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarieSoggetto();
				SedeSecondariaSoggetto sedeSelezionata = null;
				if (model.getListaSediSecondarieSoggetto()!=null) {
					for(int i = 0; i<listaSediSecondarie.size(); i++){
						if(liquidazione.getSedeSecondariaSoggetto() == listaSediSecondarie.get(i).getUid()){
							sedeSelezionata = listaSediSecondarie.get(i);
							model.setSedeSelezionata(sedeSelezionata);
							break;
						}
					}
				}
				if(sedeSelezionata!=null && listaModalitaPagamento!=null && listaModalitaPagamento.size()>0){
					for(int j=0; j<listaModalitaPagamento.size(); j++){				
						if(listaModalitaPagamento.get(j).getAssociatoA().equals(sedeSelezionata.getDenominazione())){
							relistaModalitaPagamento.add(listaModalitaPagamento.get(j));
						}
					}
				}
				if(relistaModalitaPagamento.size()>0){
					model.setListaModalitaPagamentoSoggetto(relistaModalitaPagamento);
				}else{
					model.setListaModalitaPagamentoSoggetto(listaModalitaPagamento);
				}
			}		
			//fine issue 1147
		}	
		
		if(liquidazione.getModalitaPagamentoSoggetto()!=null){
			model.setRadioModPagSelezionato(liquidazione.getModalitaPagamentoSoggetto().getUid());
		}
		
		model.setCig(liquidazione.getCig());
		model.setCup(liquidazione.getCup());	
		
		//SIOPE PLUS:
		impostaDatiSiopePlusNelModel(liquidazione, model);
		//FINE SIOPE PLUS
		
		String tipoConvalida= "0";
		if(liquidazione.getLiqManuale()!=null && !liquidazione.getLiqManuale().isEmpty()){
			if("A".equals(liquidazione.getLiqManuale())){
				tipoConvalida = "automatico";
			}else if(Constanti.SESSO_M.equals(liquidazione.getLiqManuale())){
				tipoConvalida = "manuale";
			}	
		}
	   	model.setTipoConvalida(tipoConvalida);
		
		caricaListeBil(WebAppConstants.CAP_UG);
		caricaTipiProvvedimenti();	
		
		CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
		capitolo.setCodiceMissione(model.getCapitolo().getMissione().getCodice());
		capitolo.setDescrizioneMissione(model.getCapitolo().getMissione().getDescrizione());
		capitolo.setCodiceProgramma(model.getCapitolo().getProgramma().getCodice());
		capitolo.setDescrizioneProgramma(model.getCapitolo().getProgramma().getDescrizione());

		caricaMissione(capitolo);
		caricaProgramma(capitolo);
		if(model.getCapitolo().getProgramma()!=null){
			//setta la lista cofog in teSupport.listaCofog:
			caricaListaCofog(model.getCapitolo().getProgramma().getUid());	
		}
		
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.CONTO_TESORERIA, TipiLista.DISTINTA);
	   	if(mappaListe.get(TipiLista.CLASSE_SOGGETTO)!=null){
	   		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));	
	   	}
	   	if(mappaListe.get(TipiLista.CONTO_TESORERIA)!=null){
	   		model.setListContoTesoreria((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA));
	   	}
	   	if(mappaListe.get(TipiLista.DISTINTA)!=null){
	   		model.setListDistinta((List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA));
	   	}
		
		teSupport.setCup(liquidazione.getCup());
	   	
	   	if(liquidazione.getCodMissione()!=null){
			teSupport.setMissioneSelezionata(liquidazione.getCodMissione());
		}
		
		if(liquidazione.getCodProgramma()!=null){
			teSupport.setProgrammaSelezionato(liquidazione.getCodProgramma());
		}

		// PIANO DEI CONTI
		if(liquidazione.getCodPdc()!=null){
			teSupport.getPianoDeiConti().setCodice(liquidazione.getCodPdc());
		}
		
		if(liquidazione.getDescPdc() != null){
			teSupport.getPianoDeiConti().setDescrizione(liquidazione.getCodPdc()+" - "+liquidazione.getDescPdc());
		}		

		if(liquidazione.getIdPdc() != null){
			teSupport.getPianoDeiConti().setUid(liquidazione.getIdPdc());
		}
		
		if(liquidazione.getCodCofog()!=null){
			teSupport.setCofogSelezionato(liquidazione.getCodCofog());
		}
		
		if(liquidazione.getCodTransazioneEuropeaSpesa()!=null){
			teSupport.setTransazioneEuropeaSelezionato(liquidazione.getCodTransazioneEuropeaSpesa());
		}
		
		if(liquidazione.getCodRicorrenteSpesa()!=null){
			teSupport.setRicorrenteSpesaSelezionato(liquidazione.getCodRicorrenteSpesa());
		}
		
		if(liquidazione.getCodCapitoloSanitarioSpesa()!=null){
			teSupport.setPerimetroSanitarioSpesaSelezionato(liquidazione.getCodCapitoloSanitarioSpesa());
		}
		
		if(liquidazione.getCodSiope()!=null){
			SiopeSpesa siopeSpesam = new SiopeSpesa();
			siopeSpesam.setCodice(liquidazione.getCodSiope());
			siopeSpesam.setDescrizione(model.getImpegno().getDescCodSiope());
			teSupport.setSiopeSpesa(siopeSpesam);
			codiceSiopeChangedInternal(liquidazione.getCodSiope());
		}
		
		if(liquidazione.getCodPrgPolReg()!=null){
			teSupport.setPoliticaRegionaleSelezionato(liquidazione.getCodPrgPolReg());
		}
		
		if(liquidazione.getCodClassGen11()!=null){
			teSupport.setClassGenSelezionato1(liquidazione.getCodClassGen11());
		}
		
		if(liquidazione.getCodClassGen12()!=null){
			teSupport.setClassGenSelezionato2(liquidazione.getCodClassGen12());
		}
		
		if(liquidazione.getCodClassGen13()!=null){
			teSupport.setClassGenSelezionato3(liquidazione.getCodClassGen13());
		}
		
		if(liquidazione.getCodClassGen14()!=null){
			teSupport.setClassGenSelezionato4(liquidazione.getCodClassGen14());
		}
		
		if(liquidazione.getCodClassGen15()!=null){
			teSupport.setClassGenSelezionato5(liquidazione.getCodClassGen15());
		}	
		if(liquidazione!=null && liquidazione.getCapitoloUscitaGestione()!=null && 
				liquidazione.getCapitoloUscitaGestione().getMacroaggregato()!=null){
			teSupport.setIdMacroAggregato(liquidazione.getCapitoloUscitaGestione().getMacroaggregato().getUid());
		}	
	}
	
	public String annulla(){
		return ANNULLA;
	}
	
	public String indietro(){
		return "gotoInserisciLiquidazioni";
	}
	
	public String salva(){		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		
		if (this.model.getImportoLiquidazione() == null || StringUtils.isEmpty(this.model.getImportoLiquidazione())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Liquidazione :Importo "));
		}
		
		//ORDINATIVI_COLLEGATI - Blocco se gli ordinativi di pagamento non ci sono o non sono annullati
		if (this.model.getListaOrdinativi()!=null && this.model.getListaOrdinativi().size()!=0) {
			for (OrdinativoPagamento ordinativoPagamento : model.getListaOrdinativi()) {
				if (ordinativoPagamento!=null && !ordinativoPagamento.getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.ANNULLATO)) {
					break;
				}
			}
		}
		
		
		ModalitaPagamentoSoggetto mps = null;
		SedeSecondariaSoggetto sss = null;
		if(model.getListaModalitaPagamentoSoggetto()!=null){
			for(int i=0; i<model.getListaModalitaPagamentoSoggetto().size(); i++){
				if(model.getListaModalitaPagamentoSoggetto().get(i).getUid() == (model.getRadioModPagSelezionato())){
					mps = model.getListaModalitaPagamentoSoggetto().get(i);
					break;
				}
			}	
		}
		if(mps!=null && mps.getModalitaAccreditoSoggetto()!=null && mps.getModalitaAccreditoSoggetto().getCodice()!=null && mps.getModalitaAccreditoSoggetto().getCodice().equals(Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)){
			if(mps.getTipoAccredito().equals(TipoAccredito.CSC)){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
			}else{
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
			}
		}	
		
		if(mps!=null && mps.getDescrizioneStatoModalitaPagamento()!=null && (mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("BLOCCATO") || mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("ANNULLATO") || mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("PROVVISORIO"))){
			if(mps.getTipoAccredito().equals(TipoAccredito.CSC)){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
			}else{
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
			}
		}
		
		if(model.getProvvedimento().getAnnoProvvedimento()==null || model.getProvvedimento().getNumeroProvvedimento()==null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento : Anno e Numero "));
		}
		
		if (this.model.getImportoLiquidazione()!= null && !StringUtils.isEmpty(this.model.getImportoLiquidazione())) {
			
			//Controlli al paragrafo 2.6.1 Importo del caso d'uso BILSPE032 - Aggiorna Liquidazione
			
			BigDecimal importoLiquidazione = convertiImportoToBigDecimal(model.getImportoLiquidazione());
			boolean erroreImporto=false;
			
			
			//1. L'importo della liquidazione deve essere maggiore di zero
			if(BigDecimal.ZERO.compareTo(importoLiquidazione)>=0){
				erroreImporto=true;
			}	
			
			//2. L'importo della liquidazione deve essere minore uguale alla somma tra disponibilita liquidare impegno e importo precedente
			BigDecimal importoPrecedente = model.getImportoLiquidazioneDaAggiornare();
			BigDecimal disponibilitaLiquidareImpegno = model.getDisponibilita();
			
			BigDecimal importoConfronto= disponibilitaLiquidareImpegno.add(importoPrecedente);
			
			if(importoLiquidazione!=null && importoConfronto!=null){
				if(importoLiquidazione.compareTo(importoConfronto)>0){
					erroreImporto=true;
				}
			}
			
			if(erroreImporto){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_NON_VALIDE.getErrore());
			}
			
			boolean statoValidoLiquidazione = model.getStatoOperativoLiquidazioneDaAggiornare().equals(StatoOperativoLiquidazione.VALIDO);
			if(statoValidoLiquidazione){
				
				//3. L'importo della liquidazione deve essere maggiore uguale alla differenza di importo precedente meno disponibilita a pagare
				BigDecimal disponibilitaPagare = model.getDisponibilePagareLiquidazioneDaAggiornare();
				importoConfronto=importoPrecedente.subtract(disponibilitaPagare);
				if(importoLiquidazione!=null && importoConfronto!=null){
					if(importoLiquidazione.compareTo(importoConfronto)<0){
						listaErrori.add(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore());
					}
				}
				
				//4. L'importo della liquidazione deve essere minore uguale dell'importo precedente
				if(importoLiquidazione.compareTo(importoPrecedente)>0){
					listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "(importo solo in diminuzione)"));
				}
				
			}
		}	

		// FIXME jira siac-1760
	    // controllo di congruenza di CIG e CUP
		//e di siope plus
		controlliCigCupESiopePlus(model, listaErrori);
		//
		
		if (!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		}
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		
		if(model.getListaSediSecondarieSoggetto()!=null && !model.getListaSediSecondarieSoggetto().isEmpty()){
		
			for(int i=0; i<model.getListaSediSecondarieSoggetto().size(); i++){
				if(model.getListaSediSecondarieSoggetto().get(i).getUid() == (model.getRadioSediSecondarieSoggettoSelezionato())){
					sss = model.getListaSediSecondarieSoggetto().get(i);
					break;
				}
			}
		}
		
		
		// baco stato liquidazione
		// se la liqudazione ha lo stato provvisiorio e il provvedimento definitivo 
		// allora la liquidazione diventa definitiva
		boolean statoProvvisorioLiquidazione = model.getStatoOperativoLiquidazioneDaAggiornare().equals(StatoOperativoLiquidazione.PROVVISORIO);
		boolean statoProvvisorioProvvedimento = model.getProvvedimento().getStato().equals(Constanti.STATO_PROVVISORIO);
		
		if(statoProvvisorioLiquidazione && !statoProvvisorioProvvedimento){
			if (Constanti.STATO_ANNULLATO.equals(model.getProvvedimento().getStato())){
				model.setStatoOperativoLiquidazioneDaAggiornare(StatoOperativoLiquidazione.ANNULLATO);
			}else if (Constanti.STATO_PROVVISORIO.equals(model.getProvvedimento().getStato())){
				model.setStatoOperativoLiquidazioneDaAggiornare(StatoOperativoLiquidazione.PROVVISORIO);
			}else{
				model.setStatoOperativoLiquidazioneDaAggiornare(StatoOperativoLiquidazione.VALIDO);
			}   
		}
		
		AggiornaLiquidazione request = new AggiornaLiquidazione();
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getAccount().getEnte());
		request.setBilancio(sessionHandler.getBilancio());
		Liquidazione liquidazione = new Liquidazione();	
		
		liquidazione.setAnnoLiquidazione(model.getAnnoLiquidazioneConsulta());
		liquidazione.setNumeroLiquidazione(model.getNumeroLiquidazioneConsulta());		
		liquidazione.setImportoLiquidazioneDaAggiornare(model.getImportoLiquidazioneDaAggiornare());
		liquidazione.setStatoOperativoLiquidazioneDaAggiornare(model.getStatoOperativoLiquidazioneDaAggiornare());
		liquidazione.setStatoOperativoAttoAmmDaAggiornare(model.getStatoOperativoAttoAmmDaAggiornare());
		liquidazione.setLiqManualeDaAggiornare(model.getLiqManualeDaAggiornare());	
		liquidazione.setAnnoAttoAmmDaAggiornare(model.getAnnoAttoAmmDaAggiornare());
		liquidazione.setNumeroAttoAmmDaAggiornare(model.getNumeroAttoAmmDaAggiornare());
		liquidazione.setCodiceTipoAttoAmmDaAggiornare(model.getCodiceTipoAttoAmmDaAggiornare());
		liquidazione.setDescrizioneLiquidazione(model.getDescrizioneLiquidazione());
		
		if(model.getImportoLiquidazione()!=null ||  StringUtils.isEmpty(model.getImportoLiquidazione())){
			BigDecimal importoLiqBD = convertiImportoToBigDecimal(model.getImportoLiquidazione());
			liquidazione.setImportoLiquidazione(importoLiqBD);
		}
		
		liquidazione.setCig(model.getCig());
		liquidazione.setCup(model.getCup());	
		
		//DATI SIOPE PLUS:
		impostaDatiSiopePlusPerInserisciAggiorna(model, liquidazione);
		//
		
		AttoAmministrativo attoAmministrativo = popolaProvvedimento(model.getProvvedimento());
		
		liquidazione.setAttoAmministrativoLiquidazione(attoAmministrativo);
		
		Impegno impegno = model.getImpegnoPerServizio();
		if(model.getImpegnoPerServizio()!=null && model.getImpegno()!=null){
			BigDecimal codImpegnoServ = model.getImpegnoPerServizio().getNumero();
			
			BigDecimal codImpegnoModel = model.getImpegno().getNumero();
			
			if(codImpegnoServ!=null && codImpegnoModel!=null)
			{
				if(codImpegnoServ.compareTo(codImpegnoModel)!=0)
				{
					SubImpegno subImpegno = null;
					
					for(int i = 0; i<impegno.getElencoSubImpegni().size();i++)
					{
						if(impegno.getElencoSubImpegni().get(i).getNumero().intValue()==codImpegnoModel.intValue())
						{
							subImpegno =impegno.getElencoSubImpegni().get(i);
							break;
						}
					}						
					ArrayList<SubImpegno> subImpegni = new ArrayList<SubImpegno>();
					subImpegni.add(subImpegno);
					impegno.setElencoSubImpegni(subImpegni);
				}
			}else{
				impegno = model.getImpegno();
			}			
		}		
		liquidazione.setImpegno(impegno);
		
		Soggetto soggetto = new Soggetto();
		ArrayList<ModalitaPagamentoSoggetto> listmps = new ArrayList<ModalitaPagamentoSoggetto>();
		listmps.add(mps);
		soggetto.setModalitaPagamentoList(listmps);
		ArrayList<SedeSecondariaSoggetto> listsss = new ArrayList<SedeSecondariaSoggetto>();
		
		//jira 1408
		if(sss!=null){
			  listsss.add(sss); 
		}
		listsss.add(sss);
			
		soggetto.setSediSecondarie(listsss);
		soggetto.setCodiceSoggetto(model.getSoggetto().getCodCreditore());
		liquidazione.setSoggettoLiquidazione(soggetto);	
		liquidazione.setModalitaPagamentoSoggetto(mps);

		ContoTesoreria conto = new ContoTesoreria();
		conto.setCodice(model.getContoTesoriere());
		liquidazione.setContoTesoreria(conto);
		
		Distinta distinta = new Distinta();
		distinta.setCodice(model.getDistinta());
		liquidazione.setDistinta(distinta);		
		
		liquidazione.setLiqManuale(model.getTipoConvalida());
		
		liquidazione.setCodMissione(teSupport.getMissioneSelezionata());
		liquidazione.setCodProgramma(teSupport.getProgrammaSelezionato());
		liquidazione.setCodPdc(teSupport.getPianoDeiConti().getCodice());
		
		liquidazione.setCodCofog(teSupport.getCofogSelezionato());
		liquidazione.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
		liquidazione.setCodSiope(teSupport.getSiopeSpesa().getCodice());
		liquidazione.setCodRicorrenteSpesa(teSupport.getRicorrenteSpesaSelezionato());
		liquidazione.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioSpesaSelezionato());
		liquidazione.setCodPrgPolReg(teSupport.getPoliticaRegionaleSelezionato());
		
		liquidazione.setStatoOperativoLiquidazione(model.getStatoOperativoLiquidazioneDaAggiornare());		
		
		// Qui aggiungo il controllo richiesto nella jira 4733:
		// Quando la liquidazione è Provvisoria ed è legata a un Atto Allegato in stato COMPLETATO, lo stato NON deve essere aggiornato ma rimanere a P
		// devo richiamare il servizio di ricerca allegato atto
		RicercaProvvedimento req = new RicercaProvvedimento();
		
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setEnte(sessionHandler.getEnte());
		
		RicercaAtti ricercaAtto = new RicercaAtti();
		ricercaAtto.setAnnoAtto(attoAmministrativo.getAnno());
		ricercaAtto.setNumeroAtto(attoAmministrativo.getNumero());
		ricercaAtto.setTipoAtto(attoAmministrativo.getTipoAtto());
		ricercaAtto.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		ricercaAtto.setStatoOperativo(attoAmministrativo.getStatoOperativo());
		
		req.setRicercaAtti(ricercaAtto);
		
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);	
		
		if(res!=null && !res.isFallimento() && res.getListaAttiAmministrativi()!=null){
			
			AttoAmministrativo atto = res.getListaAttiAmministrativi().get(0);
			
			if(atto.getAllegatoAtto()!=null){
			
				// la liquidazione è Provvisoria ed è legata a un Atto Allegato in stato COMPLETATO lo stato della liquidazione non deve cambiare
				if(model.getStatoLiquidazioneScheda().equalsIgnoreCase(StatoOperativoLiquidazione.PROVVISORIO.toString()) && atto.getAllegatoAtto().getStatoOperativoAllegatoAtto().equals(StatoOperativoAllegatoAtto.COMPLETATO)){
					
					liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
			
				}
			}
			
		}
		
		request.setLiquidazione(liquidazione);
		
		request.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
		AggiornaLiquidazioneResponse response = liquidazioneService.aggiornaLiquidazione(request);
		
		if(response.getEsito()==Esito.SUCCESSO){
			// messaggio di ok nella pagina aggiorna
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
            + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			return SALVA;
		}else{
			addErrori(response.getErrori());
			return INPUT;
		}
	}
	
	public String setcup(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cup = request.getParameter("cup");
		model.getTrxModel().setCup(cup);
		return TRX;
	}
	
	//aggiorna la tabella modalita di pagamento su selezione della sede e aggiorna quindi la lista sedi associate alla modalita' di pagamento scelta
	public String remodpagamento(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("selection");
		String modpagOrig = request.getParameter("modpagOrig");
		int uidModpag = 0;
		try {
			uidModpag = Integer.parseInt(modpagOrig);
		} catch(NumberFormatException nfe) {/* Ignore error, might happen... */}
		
		if(!(cod.equals("false"))){
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamentoSoggettoOrigine();
			List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarieSoggetto();
			SedeSecondariaSoggetto sedeSelezionata = null;
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(Integer.parseInt(cod) == listaSediSecondarie.get(i).getUid()){
					sedeSelezionata = listaSediSecondarie.get(i);
					model.setSedeSelezionata(sedeSelezionata);
					break;
				}
			}
			if(sedeSelezionata!=null){
				List<ModalitaPagamentoSoggetto> relistaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
				int first = 0;
				if(listaModalitaPagamento!=null && listaModalitaPagamento.size()>0){
					for(int j=0; j<listaModalitaPagamento.size(); j++){				
						if(listaModalitaPagamento.get(j).getAssociatoA().equals(sedeSelezionata.getDenominazione())){
							first = first + 1;
							relistaModalitaPagamento.add(listaModalitaPagamento.get(j));
							if(first==1){
								model.setRadioModPagSelezionato(listaModalitaPagamento.get(j).getUid());
							}
						}
					}
				}
				if(relistaModalitaPagamento.size()>0){
					model.setListaModalitaPagamentoSoggetto(relistaModalitaPagamento);
					model.setRadioModPagSelezionato(uidModpag);
				}else{
					model.setListaModalitaPagamentoSoggetto(null);
				}
			}
		}else{
			model.setListaModalitaPagamentoSoggetto(model.getListaModalitaPagamentoSoggettoOrigine());
			model.setRadioModPagSelezionato(uidModpag);
		}
		
		// TODO: verificare: se non esiste nella lista modalita pagamento la mdp con uid == radioModPagSelezionato => radioModPagSelezionato = 0
		
 		return MODPAGAMENTO;
	}
	
	//aggiorna la tabella sedi settando (check) la sede collegata alla modalita' pagamento scelta
	public String resede(){
		//inserire la logica di set della sede associata..
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("selection");
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamentoSoggettoOrigine();
		ModalitaPagamentoSoggetto modpagamentoSelezionato = null; 
		for(int j=0; j<listaModalitaPagamento.size(); j++){
			if(Integer.parseInt(cod) == listaModalitaPagamento.get(j).getUid()){
				modpagamentoSelezionato = listaModalitaPagamento.get(j);
				model.setModpagamentoSelezionata(listaModalitaPagamento.get(j));
				model.setRadioModPagSelezionato(listaModalitaPagamento.get(j).getUid());
			}
		}
		List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarieSoggetto();
		if(listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()){
		boolean corrispondenzaSede = false;
		
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(modpagamentoSelezionato.getAssociatoA().equals(listaSediSecondarie.get(i).getDenominazione())){
					model.setRadioSediSecondarieSoggettoSelezionato(listaSediSecondarie.get(i).getUid());
					corrispondenzaSede = true;
					break;
				}
			}
		
			if(!corrispondenzaSede){
				model.setRadioSediSecondarieSoggettoSelezionato(0);
			}
		}
		return SEDISECONDARIE;
	}
	
	//cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh della tabella modpagamento
	public String modpagamento(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		caricaListeCreditore(cod);
 		return MODPAGAMENTO;
	}
	
	//cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh della tabella sedi secondarie
	public String sedisecondarie(){
		return SEDISECONDARIE;
	}

	public Integer getAnno() {
		return anno;
	}

	public BigDecimal getNumero() {
		return numero;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return true;
	}

	@Override
	public boolean cofogAttivo() {
		return true;
	}

	@Override
	public boolean cupAttivo() {
		return true;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return true;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		
		teSupport.setSiopeSpesa(new SiopeSpesa());
		return SUCCESS;
	}

	@Override
	public String confermaSiope() {
		return SUCCESS;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return true;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}

	public CapitoloImpegnoModel getCapitoloLiquidazioneModel() {
		return capitoloLiquidazioneModel;
	}

	public void setCapitoloLiquidazioneModel(CapitoloImpegnoModel capitoloLiquidazioneModel) {
		this.capitoloLiquidazioneModel = capitoloLiquidazioneModel;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}

	public String getArrivoDaInserimento() {
		return arrivoDaInserimento;
	}

	public void setArrivoDaInserimento(String arrivoDaInserimento) {
		this.arrivoDaInserimento = arrivoDaInserimento;
	}
	
}
