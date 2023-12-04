/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;




@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaModificaMovimentoSpesaAction extends ActionKeyAggiornaImpegno {



	public AggiornaModificaMovimentoSpesaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}


	private static final long serialVersionUID = -4151762885979120026L;

	private String movimentoSpesaId;
	private String importoNewFormatted;


	private String annoProvvedimento;
	private Integer numeroProvvedimento;
	private String idTipoProvvedimento;
	private String descrizione;
	private String motivo;
	private String motivoOld;
	
	//SIAC-7349 CM 17/07/2020 Inizio gestione per la valorizzazione di DisponibilitaImpegnareComponente per la aggiorna modifiche di impegno
	private BigDecimal maxImportoImpegnoCompApp;
	//SIAC-7349 CM 17/07/2020 Fine
	
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return false;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Aggiorna Modifica Movimento Spesa");
		caricaListaMotivi();
		//SIAC-7838
		caricaListaMotiviAgg();
	}

	@Override
	public String execute() throws Exception {
		setMethodName("execute");
		
		//fix per anomalia: SIAC-2311, non era gestisto il tasto annulla:
		Integer idIndividuato = init();
		creaMovGestModelCache();
		model.getMovimentoSpesaModelCache().setIdModificaMovimentoGestione(idIndividuato);
		//
		
		//Reimputazione:
		inizializzaReimputazioneInAggiornamentoNelModel(model.getMovimentoSpesaModel().getModificaAggiornamento());
		//
		
		return SUCCESS;
	}
	
	/**
	 * Isolata la logica di popolamento del model in questo metodo, in modo da richiamarla sia dell'execute sia
	 * dal tasto annulla
	 * 
	 * (fix per anomalia: SIAC-2311, non era gestisto il tasto annulla)
	 * 
	 * @return
	 */
	private Integer init(){
		
		Integer movSpesaId=null;
		
		//mi salvo temporaneamente il provvedimento per reimpostarlo nell'impegno e associare il nuovo provv alle sole modifiche
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			model.getStep1Model().setProvvedimentoSupport(clone(model.getStep1Model().getProvvedimento()));
			
		}

		//Cerco la modifica di riferimento
		if(!StringUtils.isEmpty(getMovimentoSpesaId())){
			List<ModificaMovimentoGestioneSpesa> listaModificheImpegno = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
			boolean find = false;
			
			if(listaModificheImpegno != null && listaModificheImpegno.size() > 0){
				find = false;
				movSpesaId = Integer.valueOf(getMovimentoSpesaId());
				for(ModificaMovimentoGestioneSpesa spesa :listaModificheImpegno){
					if(movSpesaId == spesa.getUid()){
						find = true;
						spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_IMP);
						model.getMovimentoSpesaModel().setModificaAggiornamento(spesa);
						setDescrizione(spesa.getDescrizioneModificaMovimentoGestione());
						if(spesa.getTipoModificaMovimentoGestione()!=null){
							motivo = spesa.getTipoModificaMovimentoGestione();
							motivoOld = spesa.getTipoModificaMovimentoGestione();
						}
						
						//setto temporaneamente nel model il provvedimento della modifica
						if(spesa.getAttoAmministrativo()!=null && spesa.getAttoAmministrativo().getAnno()!=0 && spesa.getAttoAmministrativo().getNumero()!=0){
							impostaProvvNelModel(spesa.getAttoAmministrativo(),model.getStep1Model().getProvvedimento());
						}								
					}
				}
			}
				//Se non e dell impegno lo cerco nei sub impegni
				if(!find){
					List<SubImpegno> subImpegni = model.getListaSubimpegni();
					if(subImpegni != null && subImpegni.size() > 0){
						for(SubImpegno sub : subImpegni){
							List<ModificaMovimentoGestioneSpesa> listaModificheSubImpegno = sub.getListaModificheMovimentoGestioneSpesa();
							if(listaModificheSubImpegno != null && listaModificheSubImpegno.size() > 0){
							movSpesaId = Integer.valueOf(getMovimentoSpesaId());
							for(ModificaMovimentoGestioneSpesa spesa :listaModificheSubImpegno){
									if(movSpesaId == spesa.getUid()){
										find = true;
										spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SIM);
										setDescrizione(spesa.getDescrizioneModificaMovimentoGestione());
										model.getMovimentoSpesaModel().setModificaAggiornamento(spesa);
										if(spesa.getTipoModificaMovimentoGestione()!=null){
											motivo = spesa.getTipoModificaMovimentoGestione();
											motivoOld = spesa.getTipoModificaMovimentoGestione();
										}
										//setto temporaneamente nel model il provvedimento della modifica
										if(spesa.getAttoAmministrativo()!=null && spesa.getAttoAmministrativo().getAnno()!=0 && spesa.getAttoAmministrativo().getNumero()!=0){
											impostaProvvNelModel(spesa.getAttoAmministrativo(),model.getStep1Model().getProvvedimento());
										}																			
									}
								}
							}
						}
					}
				}
				
				
				setMovimentoSpesaId(Integer.toString(movSpesaId));
				
			}

		if(model.getMovimentoSpesaModel().getModificaAggiornamento().getImportoNew() != null ){
			//Nel caso in cui si tratti di una modifica di importo mi formatto il valore
			BigDecimal importoNew =  model.getMovimentoSpesaModel().getModificaAggiornamento().getImportoNew();
			int compare = importoNew.compareTo(new BigDecimal(0));
			if(compare != 0){
				setImportoNewFormatted(convertiBigDecimalToImporto(importoNew));
			}

			setAnnoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoAnno());
			setNumeroProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoNumero());
			setIdTipoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode());
			model.getMovimentoSpesaModel().setDescrizioneOrigine(model.getMovimentoSpesaModel().getModificaAggiornamento().getDescrizioneModificaMovimentoGestione());

		} else {
			//Nel caso in cui si tratti di una modifica di soggetto mi formatto il valore
			setAnnoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoAnno());
			setNumeroProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoNumero());
			setIdTipoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode());
			model.getMovimentoSpesaModel().setDescrizioneOrigine(model.getMovimentoSpesaModel().getModificaAggiornamento().getDescrizioneModificaMovimentoGestione());
		}
		
		//SIAC-7349 CM 17/07/2020 Inizio CONTABILIA-268
		initMaxImportoImpegnoCompApp();
		//SIAC-7349 CM 17/07/2020 Fine CONTABILIA-268

		return movSpesaId;
	}

	//SIAC-7349 CM 17/07/2020 Inizio CONTABILIA-268
	public void initMaxImportoImpegnoCompApp() {
		
		if (model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()) {
			//SIAC-7349
			maxImportoImpegnoCompApp = new BigDecimal(0);
		} else {
			if (model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento()
					.getCapitoloUscitaGestione().getAnnoCapitolo().intValue()) {
				// anno corrente
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(0);
			} else {
				model.setFlagSuperioreTreAnni(true);
				return;
			}
		}
		//SIAC-7349
		if(maxImportoImpegnoCompApp == null){
					//pongo a zero
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		//SIAC-7349 importo a zero il maxImportoImpegnoCompApp come fatto in precedenza, a determinate condizioni
		if(maxImportoImpegnoCompApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		// Setto il massimo e i minimo nelle variabili finali:		
		//SIAC-7349
		model.setMaxImportoImpComp(convertiBigDecimalToImporto(maxImportoImpegnoCompApp));		
	}
	//SIAC-7349 CM 17/07/2020 Fine CONTABILIA-268

	public String salva(){
		setMethodName("salva");

		boolean changed = false;
		boolean flagModificaDescrizione = false;
		
		//SIAC-7943
		model.setErrori(new ArrayList<Errore>());

		List<Errore> listaErrori = new ArrayList<Errore>();

		//SIAC-8506
		controllaLunghezzaMassimaDescrizioneMovimento(getDescrizione(), listaErrori);
		
		//SIAC-8781
		// controllo immodificabilit. da ROR-REIMP e ROR-REANNO ad altro e viceversa
		if(!getMotivo().equalsIgnoreCase(getMotivoOld()) &&
				 ((getMotivo().equalsIgnoreCase("REIMP") || getMotivoOld().equalsIgnoreCase("REIMP")) ||
				   (getMotivo().equalsIgnoreCase("REANNO") || getMotivoOld().equalsIgnoreCase("REANNO")))
			) {
			controllaImmodificabilitaTipoMotivoReimp(listaErrori);
		}
		//SIAC-8781
		// se ROR-REIMP o ROR-REANNO non posso cambiare il flag reimputazione 
		String reimputazione = model.getMovimentoSpesaModel().getReimputazione();
		if ((getMotivo().equalsIgnoreCase("REIMP") || getMotivo().equalsIgnoreCase("REANNO")) && WebAppConstants.No.equals(reimputazione)) {		
			controllaFlagReimp(listaErrori);
		}
			
		//SIAC-6929
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null){
			pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
			if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
				addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
						pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
				return INPUT;
			}
		}
		

		
		
		String controlloProvv = provvedimentoConsentito();
		if(controlloProvv!=null){
			return controlloProvv;
		}
		
		//controllo descrizione non vuota
		//controllo descizione nuove diversa dalla vecchia
		if(!FinStringUtils.sonoUgualiTrimmed(model.getMovimentoSpesaModel().getDescrizioneOrigine(), getDescrizione())){
			flagModificaDescrizione = true;
		}
		
		if(StringUtils.isEmpty(getMotivo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
			//SIAC-7943
			addErrori(listaErrori);
			return INPUT;
		}

		//se nuova descrizione uguale alla vecchia controllo se nuovo provvedimento uguale a quello vecchio  
		if(flagModificaDescrizione == false){
			if(		(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0 && Integer.valueOf(getAnnoProvvedimento()).compareTo(model.getStep1Model().getProvvedimento().getAnnoProvvedimento())!=0)
				||	(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0 && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != getNumeroProvvedimento())
				||	(!StringUtils.isEmpty(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode()) && (!model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode().equalsIgnoreCase(getIdTipoProvvedimento())))){
									
					changed = true;				
			}	
		} else {
			changed = true;    
		}

		if(changed == false && flagModificaDescrizione == false){
			if(!getMotivo().equalsIgnoreCase(getMotivoOld())){
				changed=true;
			}
		}
		// TODO da capire con elisa se va gestito diversamente oppure . un refuso (copiaincolla)

		
		BigDecimal importoModifica=model.getMovimentoSpesaModel().getModificaAggiornamento().getImportoOld();
			
		if(importoModifica!=null){
			if(!getMotivo().equalsIgnoreCase(getMotivoOld()) && getMotivo().equalsIgnoreCase("RIAC") &&  importoModifica!=null && (BigDecimal.ZERO.compareTo(importoModifica)>=0)) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Motivo"));
			}
			//SIAC-6586
			boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equalsIgnoreCase(getMotivo());
			boolean isImportoZero = BigDecimal.ZERO.compareTo(importoModifica) == 0;
			
			if(isRORDaMantenere && !isImportoZero) {
				listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			}
			if(!isRORDaMantenere && isImportoZero) {
				listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo diverso da ROR-Da mantenere ", "non puo' essere pari a zero"));
			}
			if(Arrays.asList(CODICI_MOTIVO_ROR).contains(getMotivo()) && StringUtils.isBlank(getDescrizione())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione obbligatoria per modifiche di importo relative al ROR"));
			}
		}
		
		
		if(		(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null 
					&& model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0 
					&& Integer.valueOf(getAnnoProvvedimento()).compareTo(model.getStep1Model().getProvvedimento().getAnnoProvvedimento())!=0) ||	
				(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null 
					&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0 
					&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != getNumeroProvvedimento()) ||	
				(!StringUtils.isEmpty(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode()) 
					&& (!model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode().equalsIgnoreCase(getIdTipoProvvedimento())))){									
					changed = true;				
		}

		// Gestione provvedimento per utente decentrato:
		// jira 2302: se utente decentrato rilancio un errore bloccante se il controllo sul provvedimento da esito negativo, 
		// in caso di utente non decentrato solo un warning 
		boolean soggettoDecentrato = isSoggettoDecentrato();
		boolean erroreProvvedimento = false; 
		
		if (model.getMovimentoSpesaModel().getModificaAggiornamento().getNumeroSubImpegno() != null) {
			
			//Modifica di subimpegno
			
			listaErrori = controlliReimputazioneEMotivoInAggiornamento(listaErrori, "Spesa", motivo);
			
			int annoProvvedimentoSubimpegno=0;
			int numeroProvvedimentoSubimpegno=0;
			String tipoProvvedimentoSubimpegno="";
			
			for (SubImpegno subImpegnoCorrente : model.getListaSubimpegni()) {
				if (subImpegnoCorrente.getNumeroBigDecimal().intValue()==model.getMovimentoSpesaModel().getModificaAggiornamento().getNumeroSubImpegno().intValue()) {
					annoProvvedimentoSubimpegno=subImpegnoCorrente.getAttoAmministrativo().getAnno();
					numeroProvvedimentoSubimpegno=subImpegnoCorrente.getAttoAmministrativo().getNumero();
					tipoProvvedimentoSubimpegno=subImpegnoCorrente.getAttoAmministrativo().getTipoAtto().getCodice();
					break;
				}
			}
			
			int annoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue();
			int numeroProvvedimentoModifica = model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue();
			String tipoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento();
			
			if (annoProvvedimentoSubimpegno==annoProvvedimentoModifica &&
					numeroProvvedimentoSubimpegno==numeroProvvedimentoModifica &&
					tipoProvvedimentoSubimpegno.equals(tipoProvvedimentoModifica)) {
				
				if(!soggettoDecentrato){
					if(!model.isProseguiConWarning()){
						addPersistentActionWarning(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subimpegno").getCodice()+" - "+
							ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subimpegno").getDescrizione());
						model.setProseguiConWarning(true);
						erroreProvvedimento = true;
					}
					
				}else {
					erroreProvvedimento = true;
					listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subimpegno"));
				}
				
			}
			
		}else { 
			
			
			listaErrori = controlliReimputazioneEMotivoInAggiornamento(listaErrori, "Spesa", motivo);
			
			//Modifica di impegno
			int annoProvvedimentoImpegno=model.getImpegnoInAggiornamento().getAttoAmministrativo().getAnno();
			int numeroProvvedimentoImpegno=model.getImpegnoInAggiornamento().getAttoAmministrativo().getNumero();
			String tipoProvvedimentoImpegno=model.getImpegnoInAggiornamento().getAttoAmministrativo().getTipoAtto().getCodice();
			
			int annoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue();
			int numeroProvvedimentoModifica = model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue();
			String tipoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento();
			
			if (annoProvvedimentoImpegno==annoProvvedimentoModifica &&
					numeroProvvedimentoImpegno==numeroProvvedimentoModifica &&
					tipoProvvedimentoImpegno.equals(tipoProvvedimentoModifica)) {
				if(!soggettoDecentrato){
					if(!model.isProseguiConWarning()){
						addPersistentActionWarning(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'impegno").getCodice()+" - "+
							ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'impegno").getDescrizione());
						model.setProseguiConWarning(true);
						erroreProvvedimento = true;
					}
				}else {
					erroreProvvedimento = true;
					listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'impegno"));
				}
					
	
			}
		}
		
		//Controllo gestisci decentrato:
		listaErrori = controlloGestisciImpegnoDecentratoPerModifica(listaErrori);
		
		if(!erroreProvvedimento) changed = true;
		
		if(listaErrori.isEmpty() && !erroreProvvedimento){
			if(changed){

				ModificaMovimentoGestioneSpesa spesa = model.getMovimentoSpesaModel().getModificaAggiornamento();
				
				if(getDescrizione()!=null){
					spesa.setDescrizioneModificaMovimentoGestione(getDescrizione().toUpperCase());
				}
				
				//Reimputazione:
				settaDatiReimputazioneDalModel(spesa);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
				
				//tipo mod mov gest:
				spesa.setTipoModificaMovimentoGestione(getMotivo());				
				
				if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_IMP)){
					List<ModificaMovimentoGestioneSpesa> listaImpegno = new ArrayList<ModificaMovimentoGestioneSpesa>();
					if(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa() != null && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().size() > 0){
						for(ModificaMovimentoGestioneSpesa spesaIt : model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()){
							Integer idIter = spesaIt.getUid();
							if(spesa.getUid() == idIter){
								listaImpegno.add(spesa);
							} else {
								listaImpegno.add(spesaIt);
							}
						}
					}
					
					model.getImpegnoInAggiornamento().setListaModificheMovimentoGestioneSpesa(listaImpegno);
					
					//Non veniva popolto il componente con i dati della trans. elemen.
					popolaStrutturaTransazioneElementare();
					
					
					AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(true);

//					// SIAC-7349 GS 13/07/2020 INIZIO - Setto a true flag per bypassare i controlli di disponibilit. in caso di aggiornamento di una modifica di impegno 
//					if (requestAggiorna != null)
//						requestAggiorna.getImpegno().setAggiornaTabModificheImpegno(true); 
//					// SIAC-7349 GS 13/07/2020 FINE
					
					if (requestAggiorna.getImpegno().getProgetto() != null) {
						Progetto progetto = requestAggiorna.getImpegno().getProgetto();
						
						progetto.setTipoProgetto(TipoProgetto.GESTIONE);
						progetto.setBilancio(sessionHandler.getBilancio());

						requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
						requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
					}
					

					//SIAC-6990
					//modifica di tipo IMPEGNO
					AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);
					model.setProseguiConWarning(false);
					
					if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
						debug(methodName, "Errore nella risposta del servizio");
						addErrori(methodName, response);
						return INPUT;
					}
					 
					 //APRILE 2016,
					 //l'aggiorna impegno non richiama piu' ricercaImpegnoPerChiave (benche' ottimizzato) 
					 //e' troppo rischioso perche' l'aggiorna impegno
					 //ha richiesto un po' di tempo e qui il timeout e' quasi assicurato con troppi sub...
					 //..conviene spezzare il caricamento
					 //e rieffettuarlo dalla action per evitare timeout
					
					setMotivo(null);
					setMotivoOld(null);
					
					//OTTIMIZZAZIONI APRILE 2016, richiamo di nuovo il ricercaImpegnoPerChiave:
					forceReload = true;
					model.setImpegnoRicaricatoDopoInsOAgg(null);
					//
					
				} else {
					
					List<SubImpegno> subListDef = new ArrayList<SubImpegno>();
					if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() >0){
						for(SubImpegno sub : model.getListaSubimpegni()){
							Integer uidSub = sub.getUid();
							if(uidSub == spesa.getUidSubImpegno()){
								if(sub.getListaModificheMovimentoGestioneSpesa() != null && sub.getListaModificheMovimentoGestioneSpesa().size() > 0){
									List<ModificaMovimentoGestioneSpesa> spsList = new ArrayList<ModificaMovimentoGestioneSpesa>();
									for(ModificaMovimentoGestioneSpesa spesaIter : sub.getListaModificheMovimentoGestioneSpesa()){
										Integer idSpesaIt = spesaIter.getUid();
										if(idSpesaIt == spesa.getUid()){
											spsList.add(spesa);
										} else {
											spsList.add(spesaIter);
										}
									}
									sub.setListaModificheMovimentoGestioneSpesa(spsList);
								}
								subListDef.add(sub);
							} else {
								subListDef.add(sub);
							}
						}
					}
					
					model.setListaSubimpegni(subListDef);
					
					//Non veniva popolto il componente con i dati della trans. elemen.
					popolaStrutturaTransazioneElementare();
					
					AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(true);

//					// SIAC-7349 GS 13/07/2020 INIZIO - Setto a true flag per bypassare i controlli di disponibilit. in caso di aggiornamento di una modifica di impegno 
//					if (requestAggiorna != null)
//						requestAggiorna.getImpegno().setAggiornaTabModificheImpegno(true); 
//					// SIAC-7349 GS 13/07/2020 FINE
					
					if (requestAggiorna.getImpegno().getProgetto() != null) {
						Progetto progetto = requestAggiorna.getImpegno().getProgetto();
						
						progetto.setTipoProgetto(TipoProgetto.GESTIONE);
						progetto.setBilancio(sessionHandler.getBilancio());

						requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
						requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
					}
					
					//SIAC-6990
					//modifica NON di tipo IMPEGNO
					AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);
					model.setProseguiConWarning(false);
					
					if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
						debug(methodName, "Errore nella risposta del servizio");
						addErrori(methodName, response);
						return INPUT;
					}
					
					
					setMotivo(null);
					setMotivoOld(null);
					
					//OTTIMIZZAZIONI APRILE 2016, richiamo di nuovo il ricercaImpegnoPerChiave:
					forceReload = true;
					model.setImpegnoRicaricatoDopoInsOAgg(null);
					//
					
				}
				
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
				if(model.getStep1Model().getProvvedimentoSupport()!=null){
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
					impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
				}
				
				
			}
		
		} else {
			//lista contenenti errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		return GOTO_ELENCO_MODIFICHE;

	}

	@Override 
	public String getActionDataKeys() {
		return "model, ta, am, idTipoProvvedimento, numeroProvvedimento, annoProvvedimento, motivo, motivoOld,importoNewFormatted";
	}
	
	public String annulla(){
		setMethodName("annulla");
		
		//fix per anomalia: SIAC-2311, non era gestisto il tasto annulla:
		cleanVariabili();
		
		model.setMovimentoSpesaModel(model.getMovimentoSpesaModelCache());
		model.setStep1Model(clone(model.getStep1ModelCache()));
		
		
		int id = model.getMovimentoSpesaModelCache().getIdModificaMovimentoGestione();
		setMovimentoSpesaId(Integer.toString(id));
		
		//richiamo il metodo init:
		init();
		
		model.getMovimentoSpesaModelCache().setIdModificaMovimentoGestione(id);
		
		//creo la cache nel model:
		creaMovGestModelCache();
		//
		
		return SUCCESS;
	}
	
	private void cleanVariabili(){
		 movimentoSpesaId= null;
		 importoNewFormatted= null;
		 annoProvvedimento= null;
		 numeroProvvedimento= null;
		 idTipoProvvedimento= null;
		 descrizione= null;
		 motivo= null;
		 motivoOld= null;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getMovimentoSpesaId() {
		return movimentoSpesaId;
	}

	public void setMovimentoSpesaId(String movimentoSpesaId) {
		this.movimentoSpesaId = movimentoSpesaId;
	}

	public String getImportoNewFormatted() {
		return importoNewFormatted;
	}

	public void setImportoNewFormatted(String importoNewFormatted) {
		this.importoNewFormatted = importoNewFormatted;
	}


	public String getAnnoProvvedimento() {
		return annoProvvedimento;
	}


	public void setAnnoProvvedimento(String annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}


	public String getIdTipoProvvedimento() {
		return idTipoProvvedimento;
	}


	public void setIdTipoProvvedimento(String idTipoProvvedimento) {
		this.idTipoProvvedimento = idTipoProvvedimento;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public String getMotivo() {
		return motivo;
	}


	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMotivoOld() {
		return motivoOld;
	}


	public void setMotivoOld(String motivoOld) {
		this.motivoOld = motivoOld;
	}

	//SIAC-7349 17/07/2020 CM Inizio CONTABILIA-268
	public BigDecimal getMaxImportoImpegnoCompApp() {
		return maxImportoImpegnoCompApp;
	}

	public void setMaxImportoImpegnoCompApp(BigDecimal maxImportoImpegnoCompApp) {
		this.maxImportoImpegnoCompApp = maxImportoImpegnoCompApp;
	}
	//SIAC-7349 17/07/2020 CM Fine CONTABILIA-268

	//SIAC-7349 17/07/2020 Inizio CM rielaborato dal metodo presente in AggiornaModificaMovimentoRorAction per CONTABILIA-268
	//SIAC-7349 - SR90 - MR - 03/2020 - Metodo per ottenere la disponibilita della componente
		private BigDecimal getDisponibilitaModifica(int i) {

			if(model.getImportiComponentiCapitolo() == null || model.getImportiComponentiCapitolo().isEmpty()){
				return BigDecimal.ZERO;	
			}else{
				if(i==0){				
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto();					
				}else{
						return BigDecimal.ZERO;	
					}				
				}			
			}			
				return BigDecimal.ZERO;	
		}
		//SIAC-7349 - End
		//SIAC-7349 17/07/2020 Fine CM CONTABILIA-268
}
