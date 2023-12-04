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
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaModificaMovimentoEntrataAction  extends ActionKeyAggiornaAccertamento {

	private static final long serialVersionUID = -4151762885979120026L;

	private String movimentoSpesaId;
	private String importoNewFormatted;


	//Valori di confronto
	private String annoProvvedimento;
	private Integer numeroProvvedimento;
	private String idTipoProvvedimento;
	private String descrizione;
	private String motivo;
	private String motivoOld;
	
	public AggiornaModificaMovimentoEntrataAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
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
		
		//carico la lista motivi:
		caricaListaMotivi();
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
		inizializzaReimputazioneInAggiornamentoNelModel(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc());
		//
		
		return SUCCESS;
	}
	
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
			List<ModificaMovimentoGestioneEntrata> listaModificheAccertamento = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
			boolean find = false;
			if(listaModificheAccertamento != null && listaModificheAccertamento.size() > 0){
				find = false;
				movSpesaId = Integer.valueOf(getMovimentoSpesaId());
				//itero sulle modifiche:
				for(ModificaMovimentoGestioneEntrata spesa :listaModificheAccertamento){
					if(movSpesaId == spesa.getUid()){
						//trovata
						find = true;
						spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_ACC);
						setDescrizione(spesa.getDescrizioneModificaMovimentoGestione());
						model.getMovimentoSpesaModel().setModificaAggiornamentoAcc(spesa);
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
				List<SubAccertamento> subAccertamenti = model.getListaSubaccertamenti();
				if(subAccertamenti != null && subAccertamenti.size() > 0){
					//itero sui sub:
					for(SubAccertamento sub : subAccertamenti){
						List<ModificaMovimentoGestioneEntrata> listaModificheSubAccertamento = sub.getListaModificheMovimentoGestioneEntrata();
						if(listaModificheSubAccertamento != null && listaModificheSubAccertamento.size() > 0){
							movSpesaId = Integer.valueOf(getMovimentoSpesaId());
							//per ogni sub itero le sue modifiche:
							for(ModificaMovimentoGestioneEntrata spesa :listaModificheSubAccertamento){
								if(movSpesaId == spesa.getUid()){
									//trovata
									find = true;
									spesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SAC);
									setDescrizione(spesa.getDescrizioneModificaMovimentoGestione());
									model.getMovimentoSpesaModel().setModificaAggiornamentoAcc(spesa);
									if(spesa.getTipoModificaMovimentoGestione()!=null){
										motivo = spesa.getTipoModificaMovimentoGestione();
										motivoOld = spesa.getTipoModificaMovimentoGestione();
									}	
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

		if(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoNew() != null ){
			//Nel caso in cui si tratti di una modifica di importo mi formatto il valore
			BigDecimal importoNew =  model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoNew();
			int compare = importoNew.compareTo(new BigDecimal(0));
			if(compare != 0){
				//setto il nuovo importo
				setImportoNewFormatted(convertiBigDecimalToImporto(importoNew));
			}

			//setto i dati del provvedimento e descrizione origine:
			settaDatiProvvEDescrizioneOrigine();

		} else {
			//Nel caso in cui si tratti di una modifica di soggetto mi formatto il valore
			
			//setto i dati del provvedimento e descrizione origine:
			settaDatiProvvEDescrizioneOrigine();
		}
		
		return movSpesaId;
		
	}
	
	/**
	 * semplice metodo che pono queste istruzioni a fattor comune
	 * dato che erano replicate tali e quali in due punti diversi
	 */
	private void settaDatiProvvEDescrizioneOrigine(){
		//anno provvedimento
		setAnnoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getAttoAmministrativoAnno());
		//numero provvedimento
		setNumeroProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getAttoAmministrativoNumero());
		//id tipo provvedimento
		setIdTipoProvvedimento(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getAttoAmministrativoTipoCode());
		//descrizione origine
		model.getMovimentoSpesaModel().setDescrizioneOrigine(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getDescrizioneModificaMovimentoGestione());
	}
	

	/**
	 * Metodo di salvataggio
	 * @return
	 */
	public String salva(){
		setMethodName("salva");

		boolean changed = false;
		boolean flagModificaDescrizione = false;

		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//SIAC-8506
		controllaLunghezzaMassimaDescrizioneMovimento(getDescrizione(), listaErrori);

		//SIAC-8818
		// controllo immodificabilit da ROR-REIMP e ROR-REANNO ad altro e viceversa
		/* se il tipo diverso dal tipo vecchio &&
		   (se il tipo vecchio reimp o reanno oppure  il tipo nuovo reimp o reanno)
		*/
		if(!getMotivo().equalsIgnoreCase(getMotivoOld()) &&
				( (getMotivoOld().equalsIgnoreCase("REIMP") || getMotivoOld().equalsIgnoreCase("REANNO")) ||
				  (getMotivo().equalsIgnoreCase("REIMP") || getMotivo().equalsIgnoreCase("REANNO"))
				) ) 
		{
			controllaImmodificabilitaTipoMotivoReimp(listaErrori);
		}
		
		//SIAC-8818
		// se ROR-REIMP o ROR-REANNO non posso cambiare il flag reimputazione 
		String reimputazione = model.getMovimentoSpesaModel().getReimputazione();
		if ((getMotivo().equalsIgnoreCase("REIMP") || getMotivo().equalsIgnoreCase("REANNO")) && WebAppConstants.No.equals(reimputazione)) {		
			controllaFlagReimp(listaErrori);
		}
				
		//controllo descrizione non vuota
		//controllo descizione nuove diversa dalla vecchia
		if(!FinStringUtils.sonoUgualiTrimmed(model.getMovimentoSpesaModel().getDescrizioneOrigine(), getDescrizione())){
			flagModificaDescrizione = true;
		}
		
		if(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoOld() != null) {
			setImportoNewFormatted(convertiBigDecimalToImporto(model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoOld()));
		}
	
		if(StringUtils.isEmpty(getMotivo())){
			//errore dato obbligatorio non indicato
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
			return INPUT;
		}

		//se nuova descrizione uguale alla vecchia controllo se nuovo provvedimento uguale a quello vecchio  
		if(flagModificaDescrizione == false){
			if(	(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0 && Integer.valueOf(getAnnoProvvedimento()).compareTo(model.getStep1Model().getProvvedimento().getAnnoProvvedimento())!=0)
				||	(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0 && model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != getNumeroProvvedimento())
				||	(!StringUtils.isEmpty(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode()) && (!model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode().equalsIgnoreCase(getIdTipoProvvedimento())))){
				//CAMBIATO	per provvedimento		
				changed = true;				
			}	
		} else {
			//CAMBIATO	
			changed = true;
		}
		
		//SIAC-7018
		//SIAC-6586
		BigDecimal importoOld = model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoOld();
		
		if(importoOld!=null){
			if(!getMotivo().equalsIgnoreCase(getMotivoOld()) && getMotivo().equalsIgnoreCase("RIAC") &&  importoOld!=null && (BigDecimal.ZERO.compareTo(importoOld)>=0)) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Motivo"));
			}
			
			boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equalsIgnoreCase(getMotivo());
			boolean isImportoZero = importoOld.compareTo(BigDecimal.ZERO) == 0;
			
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
		
		log.warn("accertamento - accertamento - accertamento - accertamento - accertamento - accertamento - accertamento", "controlli passati");
		
		//StringUtils.isNotEmpty(getDescrizione()

		if(changed == false && flagModificaDescrizione == false){
			if(!getMotivo().equalsIgnoreCase(getMotivoOld())){
				//CAMBIATO	per modifica descrizione
				changed=true;
			}
		}

		//controllo importo per motivo RIAC:
		if(!getMotivo().equalsIgnoreCase(getMotivoOld())){
			if (getMotivo().equalsIgnoreCase("RIAC")) {
				BigDecimal importo=model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getImportoOld();
				if (importo!=null && (importo.compareTo(BigDecimal.ZERO)!=-1)) {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Motivo"));
				}
				
			}
		}

		//ATTO CHANGED:
		if(	(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null && 
				model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != 0 && 
				Integer.valueOf(getAnnoProvvedimento()).compareTo(model.getStep1Model().getProvvedimento().getAnnoProvvedimento())!=0) ||
			(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null && 
				model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != 0 && 
				model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() != getNumeroProvvedimento()) ||
			(!StringUtils.isEmpty(model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode()) && 
				(!model.getMovimentoSpesaModel().getModificaAggiornamento().getAttoAmministrativoTipoCode().equalsIgnoreCase(getIdTipoProvvedimento())))){									
					changed = true;				
		}


		//Gestione provvedimento per utente decentrato
		// jira 2302: se utente decentrato rilancio un errore bloccante se il controllo sul provvedimento da esito negativo, 
		// in caso di utente non decentrato solo un warning 
		boolean soggettoDecentrato = isSoggettoDecentrato();
		boolean erroreProvvedimento = false;
		
		
		//Modifica di subaccertamento
		if (model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getNumeroSubAccertamento() != null) {

			//lanciamo i controlli reimputazione e motivo:
			listaErrori = controlliReimputazioneEMotivoInAggiornamento(listaErrori, "Entrata", motivo);
			
			int annoProvvedimentoSubaccertamento=0;
			int numeroProvvedimentoSubaccertamento=0;
			String tipoProvvedimentoSubaccertamento="";
			
			for (SubAccertamento subAccertamentoCorrente : model.getAccertamentoInAggiornamento().getElencoSubAccertamenti()) {
				if (subAccertamentoCorrente.getNumeroBigDecimal().intValue()==model.getMovimentoSpesaModel().getModificaAggiornamentoAcc().getNumeroSubAccertamento().intValue()) {
					annoProvvedimentoSubaccertamento=subAccertamentoCorrente.getAttoAmministrativo().getAnno();
					numeroProvvedimentoSubaccertamento=subAccertamentoCorrente.getAttoAmministrativo().getNumero();
					tipoProvvedimentoSubaccertamento=subAccertamentoCorrente.getAttoAmministrativo().getTipoAtto().getCodice();
					break;
				}
			}
			
			int annoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue();
			int numeroProvvedimentoModifica = model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue();
			String tipoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento();
			
			if (annoProvvedimentoSubaccertamento==annoProvvedimentoModifica &&
					numeroProvvedimentoSubaccertamento==numeroProvvedimentoModifica &&
					tipoProvvedimentoSubaccertamento.equals(tipoProvvedimentoModifica)) {
				
				if(!soggettoDecentrato){
					
					
					if(!model.isProseguiConWarning()){
						addPersistentActionWarning(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subaccertamento").getCodice()+" - "+
							ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subaccertamento").getDescrizione());
						model.setProseguiConWarning(true);
						erroreProvvedimento = true;
					}
					
				}else {
					
					erroreProvvedimento = true;
					listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello del subaccertamento"));
				}
			}
			
		}else { 
			//Modifica di accertamento
			
			listaErrori = controlliReimputazioneEMotivoInAggiornamento(listaErrori, "Entrata", motivo);
			
			int annoProvvedimentoAccertamento=model.getAccertamentoInAggiornamento().getAttoAmministrativo().getAnno();
			int numeroProvvedimentoAccertamento=model.getAccertamentoInAggiornamento().getAttoAmministrativo().getNumero();
			String tipoProvvedimentoAccertamento=model.getAccertamentoInAggiornamento().getAttoAmministrativo().getTipoAtto().getCodice();
			
			int annoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getAnnoProvvedimento().intValue();
			int numeroProvvedimentoModifica = model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue();
			String tipoProvvedimentoModifica = model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento();

			
			
			
			
			if (annoProvvedimentoAccertamento==annoProvvedimentoModifica &&
					numeroProvvedimentoAccertamento==numeroProvvedimentoModifica &&
					tipoProvvedimentoAccertamento.equals(tipoProvvedimentoModifica)) {
				
				if(!soggettoDecentrato){
					if(!model.isProseguiConWarning()){
						addPersistentActionWarning(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'accertamento").getCodice()+" - "+
							ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'accertamento").getDescrizione());
						model.setProseguiConWarning(true);
						
						erroreProvvedimento = true;
					}
					
				}else {
					
					
					//modifica per incongruenza provvisorio nel caso in cui si tratti di decentrato
					erroreProvvedimento = true;
					listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il provvedimento deve essere diverso da quello dell'accertamento"));
				}
			}
		}
	
		//Controllo gestisci decentrato:
		
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
		
		listaErrori = controlloGestisciAccertamentoDecentratoPerModifica(listaErrori, true);
		
		
		if(!erroreProvvedimento) {
			changed = true;
		}

		if(listaErrori.isEmpty() && !erroreProvvedimento){
			
			if(changed){

				ModificaMovimentoGestioneEntrata spesa = model.getMovimentoSpesaModel().getModificaAggiornamentoAcc();
				
				if(getDescrizione()!=null){
					spesa.setDescrizioneModificaMovimentoGestione(getDescrizione().toUpperCase());
				}
				
				//Reimputazione:
				settaDatiReimputazioneDalModel(spesa);

				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
				
				spesa.setTipoModificaMovimentoGestione(getMotivo());
				
				
				if(spesa.getTipoMovimento().equalsIgnoreCase(CostantiFin.MODIFICA_TIPO_ACC)){
					List<ModificaMovimentoGestioneEntrata> listaAccertamento = new ArrayList<ModificaMovimentoGestioneEntrata>();
					if(model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata() != null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata().size() > 0){
						for(ModificaMovimentoGestioneEntrata spesaIt : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()){
							Integer idIter = spesaIt.getUid();
							if(spesa.getUid() == idIter){
								listaAccertamento.add(spesa);
							} else {
								listaAccertamento.add(spesaIt);
							}
						}
					}
					
					model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(listaAccertamento);
					
					//Non veniva popolto il componente con i dati della trans. elemen.
					popolaStrutturaTransazioneElementareAcc();
					
					AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(convertiModelPerChiamataServiziInserisciAggiornaModifiche(true));
					model.setProseguiConWarning(false);
					
					if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
						//presenza errori
						debug(methodName, "Errore nella risposta del servizio");
						addErrori(methodName, response);
						return INPUT;
					}
					
					
					//Ottimizzazione richiamo ai servizi
					model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
					
					List<ModificaMovimentoGestioneEntrata> nuovaListaModificheImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
					nuovaListaModificheImporti = response.getAccertamento().getListaModificheMovimentoGestioneEntrata();
					model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(nuovaListaModificheImporti);
					
					List<SubAccertamento> subDefinitivoList = new ArrayList<SubAccertamento>();
					if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size()> 0){
						for(SubAccertamento subSession : model.getListaSubaccertamenti()){
							int idSubSessione = subSession.getUid();

							for(SubAccertamento subResponse : response.getAccertamento().getElencoSubAccertamenti()){
								int idSubResponse = subResponse.getUid();
								if(idSubSessione == idSubResponse){
									List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSubImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
									nuovaListaModificheSubImporti = subResponse.getListaModificheMovimentoGestioneEntrata();
									subSession.setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSubImporti);
									subDefinitivoList.add(subSession);
								}
							}
						}
					}
					
					model.setListaSubaccertamenti(subDefinitivoList);
					
					setMotivo(null);
					setMotivoOld(null);
					
				} else {
					
					List<SubAccertamento> subListDef = new ArrayList<SubAccertamento>();
					if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() >0){
						for(SubAccertamento sub : model.getListaSubaccertamenti()){
							Integer uidSub = sub.getUid();
							if(uidSub == spesa.getUidSubAccertamento()){
								if(sub.getListaModificheMovimentoGestioneEntrata() != null && sub.getListaModificheMovimentoGestioneEntrata().size() > 0){
									List<ModificaMovimentoGestioneEntrata> spsList = new ArrayList<ModificaMovimentoGestioneEntrata>();
									for(ModificaMovimentoGestioneEntrata spesaIter : sub.getListaModificheMovimentoGestioneEntrata()){
										Integer idSpesaIt = spesaIter.getUid();
										if(idSpesaIt == spesa.getUid()){
											spsList.add(spesa);
										} else {
											spsList.add(spesaIter);
										}
									}
									sub.setListaModificheMovimentoGestioneEntrata(spsList);
								}
								subListDef.add(sub);
							} else {
								subListDef.add(sub);
							}
						}
					}
					
					model.setListaSubaccertamenti(subListDef);
					
					//Non veniva popolto il componente con i dati della trans. elemen.
					popolaStrutturaTransazioneElementareAcc();
					
					//Richiamo il servizio:
					AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(convertiModelPerChiamataServiziInserisciAggiornaModifiche(true));
					model.setProseguiConWarning(false);
					
					if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
						//presenza errori
						debug(methodName, "Errore nella risposta del servizio");
						addErrori(methodName, response);
						return INPUT;
					}
					
					//Ottimizzazione richiamo ai servizi
					model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
					
					List<ModificaMovimentoGestioneEntrata> nuovaListaModificheImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
					nuovaListaModificheImporti = response.getAccertamento().getListaModificheMovimentoGestioneEntrata();
					model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(nuovaListaModificheImporti);
					
					List<SubAccertamento> subDefinitivoList = new ArrayList<SubAccertamento>();
					if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size()> 0){
						for(SubAccertamento subSession : model.getListaSubaccertamenti()){
							int idSubSessione = subSession.getUid();

							for(SubAccertamento subResponse : response.getAccertamento().getElencoSubAccertamenti()){
								int idSubResponse = subResponse.getUid();
								if(idSubSessione == idSubResponse){
									List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSubImporti = new ArrayList<ModificaMovimentoGestioneEntrata>();
									nuovaListaModificheSubImporti = subResponse.getListaModificheMovimentoGestioneEntrata();
									subSession.setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSubImporti);
									subDefinitivoList.add(subSession);
								}
							}
						}
					}
					
					model.setListaSubaccertamenti(subDefinitivoList);
					
					setMotivo(null);
					setMotivoOld(null);
					
				}
				
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
				if(model.getStep1Model().getProvvedimentoSupport()!=null){
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
					impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento()); // rimesso come prima Raffa
				}
				
				
			} 
			

		} else {
			//presenza errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		return GOTO_ELENCO_MODIFICHE;
	}
	
	
	

	@Override 
	public String getActionDataKeys() {
		return "model, ta, am, idTipoProvvedimento, numeroProvvedimento, annoProvvedimento, motivo, motivoOld";
	}
	
	public String annulla(){
		setMethodName("annulla");
		
		//fix per anomalia: SIAC-2311, non era gestisto il tasto annulla:
		cleanVariabili();
		
		model.setMovimentoSpesaModel(model.getMovimentoSpesaModelCache());
		model.setStep1Model(clone(model.getStep1ModelCache()));
		
		int id = model.getMovimentoSpesaModelCache().getIdModificaMovimentoGestione();
		setMovimentoSpesaId(Integer.toString(id));
		
		init();
		
		model.getMovimentoSpesaModelCache().setIdModificaMovimentoGestione(id);
		
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

}
