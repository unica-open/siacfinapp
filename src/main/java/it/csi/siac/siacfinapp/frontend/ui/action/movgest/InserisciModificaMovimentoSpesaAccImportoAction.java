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
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaAccImportoAction extends ActionKeyAggiornaAccertamento{
	
	private static final long serialVersionUID = -1894703170048881209L;

	public InserisciModificaMovimentoSpesaAccImportoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	} 
	
	private String minImp;
	private String maxImp;
	private String minSub;
	private String maxSub;
	private String minAnche;
	private String maxAnche;
	private String tipoImpegno;
	private String importoImpegnoModImp;
	private String importoAttualeSubImpegno;
	private String descrizione;
	private List<String> tipoImpegnoModificaImportoList = new ArrayList<String>();
	private List<String> numeroSubImpegnoList = new ArrayList<String>();
	private List<String> abbinaList = new ArrayList<String>();
	private String abbina;
	private String abbinaChk;
	private boolean subImpegnoSelected = false;
	private String subSelected;
	private String anniPluriennali;
	private String idTipoMotivo;
	
	//Parametri SubImpegno
	private String numeroSubImpegno;
		
	
	//IMPORTI CALCOLATI
	private String minImportoCalcolato;
	private String maxImportoCalcolato;
	private String idSub;
	private String minImportoSubCalcolato;
	private String maxImportoSubCalcolato;
	private BigDecimal minImportoImpegnoApp;
	private BigDecimal maxImportoImpegnoApp;
	private BigDecimal minImportoSubApp;
	private BigDecimal maxImportoSubApp;
	
	private Integer uidSubSelected;
	
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return false;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//Reimputazione:
		inizializzaReimputazioneInInserimentoNelModel();
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Importo");
		
		//caricaListaMotivi();
		//Carico Lista Tipo Modifiche
		if(abilitaListaRORAccertamento()){
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		}else{
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}
		
		//Preparo Attibruti per la pagina
		getAbbinaList().add("Modifica Anche Accertamento");
		
		if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0){
			
			//ciclo sui sub:
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(sub.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")){
					if(sub.getNumero() != null){
						//aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumero()));
					}	
				}
				
			}
			
			if(numeroSubImpegnoList.size() > 0){
				//ci sono sub
				tipoImpegnoModificaImportoList.add("Accertamento");
				tipoImpegnoModificaImportoList.add("SubAccertamento");
				
			} else {
				//non ci sono sub
				tipoImpegnoModificaImportoList.add("Accertamento");	
			}

		} else {
			//non ci sono sub
			tipoImpegnoModificaImportoList.add("Accertamento");
		}
	
		//Calcolo importi
		String minImpApp ="";
		
		BigDecimal disponibilitaUtilizzare = BigDecimal.ZERO;
		if(model.getAccertamentoInAggiornamento().getDisponibilitaUtilizzare()!=null){
			disponibilitaUtilizzare = model.getAccertamentoInAggiornamento().getDisponibilitaUtilizzare();
		}
		
		if(!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")){
			// non e' definitivo
			BigDecimal minTemp = model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare().min(disponibilitaUtilizzare);
			minImpApp = convertiBigDecimalToImporto(minTemp);
		} else {
			//e' definitivo
			BigDecimal minTemp = model.getAccertamentoInAggiornamento().getDisponibilitaIncassare().min(disponibilitaUtilizzare);
			minImpApp = convertiBigDecimalToImporto(minTemp);
		}
		
		if(!minImpApp.contains("-")){
			minImpApp = "-" + minImpApp;
		}
		
		log.debug("",minImpApp);//
		
		minImportoImpegnoApp = convertiImportoToBigDecimal(minImpApp);
		
		if(model.getAccertamentoInAggiornamento().getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
			//mi riconduco a questa casistica in modo da non avere un massimo:
			model.setFlagSuperioreTreAnni(true);
			//
		} else if(model.getAccertamentoInAggiornamento().isFlagDaRiaccertamento()){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else {
			//SIAC-580
			
			if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()){
				// anno corrente
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno1();
				
			}else if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == (model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()+1 )){
//				anno  +1
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno2();
			}else if(model.getAccertamentoInAggiornamento().getAnnoMovimento() == (model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getAnnoCapitolo().intValue()+2 )){
//				anno +2
				maxImportoImpegnoApp = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitoloEG().getDisponibilitaAccertareAnno3();
			}else{
				//  da GESTIRE NELLA PAGINA
				model.setFlagSuperioreTreAnni(true);
			}
		}
		
		if(minImportoImpegnoApp == null){
			//pongo a zero
			minImportoImpegnoApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp == null){
			maxImportoImpegnoApp = new BigDecimal(0);
		}else if(maxImportoImpegnoApp.compareTo(BigDecimal.ZERO)<0){
				maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//Setto il massimo e i minimo nelle variabili finali:
		setMaxImp(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		setMinImp(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMaxImpMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMinImpMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		
		setMinImportoCalcolato(convertiBigDecimalToImporto(minImportoImpegnoApp));
		setMaxImportoCalcolato(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMinImportoCalcolatoMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMaxImportoCalcolatoMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		
		if (StringUtils.isEmpty(getAnniPluriennali())) {
			setAnniPluriennali("1");
		}
				
	}
	
	/**
	 * per verificare se ci sono limiti all'incremento
	 * @return
	 */
	public boolean isImportoSenzaLimiti(){
		boolean senzaLimiti = false;
		
		boolean selezionatoAncheAccertamento = false;
		
		if("Accertamento".equals(tipoImpegno) || "Modifica Anche Accertamento".equalsIgnoreCase(abbina) ){
			//senza limiti
			selezionatoAncheAccertamento = true;
		}
		
		if(model.isFlagSuperioreTreAnni() && selezionatoAncheAccertamento){
			//senza limiti
			senzaLimiti = true;
		}
		
		return senzaLimiti;
	}
	
	/**
	 * execute della action
	 * @return
	 * @throws Exception
	 */
	@Override
	public String execute() throws Exception {
		setMethodName("execute");
		
		tipoImpegno = "Accertamento";
		
		creaMovGestModelCache();
		
		//mi salvo temporaneamente il provvedimento per reimpostarlo nell'impegno e associare il nuovo provv alle sole modifiche
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			model.getStep1Model().setProvvedimentoSupport(clone(model.getStep1Model().getProvvedimento()));
		}
		
		model.getStep1Model().setSaltaControlloLegamiStoricizzati(false);
		
		return SUCCESS;
	}
	
	
	public String caricaDatiSub(){
		setMethodName("caricaDatiSub");
		
		if (getAbbinaChk()!= null && getAbbinaChk().equalsIgnoreCase("true")) {
			setAbbina("Modifica Anche Accertamento");
		} else {
			setAbbina(null);
		}
		
		if(StringUtils.isEmpty(getTipoImpegno())){
			setTipoImpegno("Accertamento");
			setSubImpegnoSelected(false);
			model.setSubImpegnoSelectedMod(false);
		} else if(getTipoImpegno().equalsIgnoreCase("SubAccertamento")){
			if(!StringUtils.isEmpty(getSubSelected())){
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equals(getSubSelected())){
						
						setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						setNumeroSubImpegno(String.valueOf(sub.getNumero()));
						model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumero()));
						
						//BigDecimal importoAttualeSubImpegno = sub.getImportoAttuale()

						//minImportoSubApp = new BigDecimal(0).subtract(importoAttualeSubImpegno)
						BigDecimal importoSubAccertato;
						if(sub.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")){
							importoSubAccertato= sub.getDisponibilitaIncassare();
							if(null==importoSubAccertato){
								importoSubAccertato = BigDecimal.ZERO;
							}
						} else {
							importoSubAccertato = new BigDecimal(0);
						}
						
						minImportoSubApp =BigDecimal.ZERO.subtract(importoSubAccertato);
						

					
						if(sub.getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
							maxImportoSubApp = new BigDecimal(0);
						} else if(sub.isFlagDaRiaccertamento()){
							maxImportoSubApp = new BigDecimal(0);
						} else{
							maxImportoSubApp =model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare();
						}
						
						if(minImportoSubApp == null){
							minImportoSubApp = new BigDecimal(0);
						}
						
						if(maxImportoSubApp == null){
							maxImportoSubApp = new BigDecimal(0);
						}
						
						setMaxSub(convertiBigDecimalToImporto(maxImportoSubApp));
						setMinSub(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMaxSubMod(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMinSubMod(convertiBigDecimalToImporto(minImportoSubApp));
						
						setMinImportoSubCalcolato(convertiBigDecimalToImporto(minImportoSubApp));
						setMaxImportoSubCalcolato(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMinImportoSubCalcolatoMod(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMaxImportoSubCalcolatoMod(convertiBigDecimalToImporto(maxImportoSubApp));

						//Modifica importi richiesta
						setMinAnche(convertiBigDecimalToImporto(minImportoSubApp));
						model.getMovimentoSpesaModel().setMinAncheImpegno(minImportoSubApp);
						model.setMinAncheMod(convertiBigDecimalToImporto(minImportoSubApp));
						
						//Modifica importi richiesta
						setMaxAnche(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						model.getMovimentoSpesaModel().setMaxAncheImpegno(maxImportoImpegnoApp);
						model.setMaxAncheMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
					}
				}
				
			} else {
				setSubImpegnoSelected(false);
				model.setSubImpegnoSelectedMod(false);
				tipoImpegno = "Accertamento";
			}
		}
		
		
		return SUCCESS;
	}
	
	public String salvaPopup() throws Exception{
		setMethodName("salvaPopup");
		
		model.getStep1Model().setCheckproseguiMovimentoSpesa(true);
		
		return salva();
	}
	
	public String siProsegui() throws Exception{
		setMethodName("siProsegui");
		
		model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		return prosegui();
	}
	
	public String noProsegui() throws Exception{
		setMethodName("noProsegui");
		
		model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		return GOTO_ELENCO_MODIFICHE;
	}
	
	public String salvaProsegui() throws Exception{
		setMethodName("salvaProsegui");
		
		model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		String esitoSalva=salva();
		
		if (!esitoSalva.equals(GOTO_ELENCO_MODIFICHE)) {
			return esitoSalva;
		}
		
		return prosegui();
	}
	
	public String salva(){
		setMethodName("salva");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		List<Errore> listaWarning = new ArrayList<Errore>();
		
		boolean erroreProvvedimento = false;
		
		String tipoMotivo = getIdTipoMotivo();
		//Motivo
		if(StringUtils.isEmpty(getIdTipoMotivo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			addErrori(listaErrori);
			return INPUT;
		}
		
		//Controllo dei campi
		if(tipoImpegno.equalsIgnoreCase("Accertamento")){
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
			checkProvvedimento(listaErrori);
			
			listaErrori = controlloGestisciAccertamentoDecentratoPerModifica(listaErrori);
			
			
			//SIAC-
			//Importo Obbligatorio
			controlloImporto(listaErrori, listaWarning);
			
			checkDescrizioneROR(listaErrori);
			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "accertamento", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento && listaWarning.isEmpty()){
				
				model.getStep1Model().setSaltaControlloLegamiStoricizzati(false);
				
				List<ModificaMovimentoGestioneEntrata> modificheList = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
				
				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				}
				
				ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
				
				BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
				BigDecimal importoAttuale = model.getAccertamentoInAggiornamento().getImportoAttuale();
				
				BigDecimal importoCambiato = importoAttuale.add(importoDiInput);

				spesa.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
				spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
				spesa.setImportoNew(importoCambiato);
				model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
				
				model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
				model.getStep1Model().setImportoImpegno(importoCambiato);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
				
				spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				if(getDescrizione()!=null){
					spesa.setDescrizione(getDescrizione().toUpperCase());
				}
				spesa.setTipoMovimento("ACC");
				
				settaDatiReimputazioneDalModel(spesa);
				
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				modificheList.add(spesa);
				model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(modificheList);
				
				//Mi salvo il capitolo
				CapitoloEntrataGestione vecchioCapitolo = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione();  
				
				//Info per step2
				List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
				if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null){
					for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
						listaIdModificaEsistenti.add(spesaCorr.getUid());
					}
				}

				//Non veniva popolto il componente con i dati della trans. elemen.
				popolaStrutturaTransazioneElementareAcc();
				
				//compongo la request:
				AggiornaAccertamento requestAggiorna = convertiModelPerChiamataServiziInserisciAggiornaModifiche(true);
				
				//invoco il servizio:
				AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(requestAggiorna);
				
				//setto prosegui con warning a false:
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					//presenza errori
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
				
				Accertamento accertamentoAggiornato = response.getAccertamento();
				accertamentoAggiornato.setCapitoloEntrataGestione(vecchioCapitolo);
				
				model.setAccertamentoInAggiornamento(accertamentoAggiornato);				
				model.setListaSubaccertamenti(accertamentoAggiornato.getElencoSubAccertamenti());
				model.setDisponibilitaSubImpegnare(accertamentoAggiornato.getDisponibilitaSubAccertare());
				
				//Ottimizzazione richiamo ai servizi
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
				
				//Info per step2
				if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null){
					for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
						if (!listaIdModificaEsistenti.contains(spesaCorr.getUid())) {
							model.getMovimentoSpesaModel().setEntrata(spesaCorr);
							model.getMovimentoSpesaModel().setModificaImpegnoSubimpegno(false);
						}
					}
				}

				creaMovGestModelCache();

				//SIAC-640/645
				//pop-up riaccertamento su salva
				if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO) && model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
					//errore per riaccertamento automatico
					listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("ACCERTAMENTO"));
					addErrori(listaErrori);
					return INPUT;
				} else {
					
					//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
					//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
					if(model.getStep1Model().getProvvedimentoSupport()!=null){
						AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
						impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
					}
					
					return GOTO_ELENCO_MODIFICHE;
				}

			} else {
				if(!listaErrori.isEmpty()){
					//presenza errori
					addErrori(listaErrori);
				}
				//tutto ok
				return INPUT;
			}
	
			
		}  else if(tipoImpegno.equals("SubAccertamento") && abbina == null ){
			
			//controllo selezione subImpegno
			if(getSubSelected()==null || StringUtils.isEmpty(getSubSelected())){
				//errore per sub omesso
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subAccertamento"));
				addErrori(listaErrori);
				return INPUT;
			}
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
			checkProvvedimento(listaErrori);
			
			//Importo Obbligatorio
			controlloImportoSubaccertamento(listaErrori);
			
			checkDescrizioneROR(listaErrori);
			
			if(getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)){
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare :  Modifica Anche Accertamento"));
			}
			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "subAccertamento", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento){
				
				List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
						modificheList = sub.getListaModificheMovimentoGestioneEntrata();
					}
				}
				
				
				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				}
				
				ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
				BigDecimal importoDiInputSub = convertiImportoToBigDecimal(importoImpegnoModImp);
				BigDecimal importoAttualeSub = convertiImportoToBigDecimal(getImportoAttualeSubImpegno());
				
				BigDecimal importoCambiatoSub = importoAttualeSub.add(importoDiInputSub);
				//setto anche uid del sub selezionato
				spesa.setUidSubAccertamento(getUidSubSelected());
				spesa.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
				spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
				spesa.setImportoNew(importoCambiatoSub);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
				
				spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				if(getDescrizione()!=null){
					spesa.setDescrizione(getDescrizione().toUpperCase());
				}
				spesa.setTipoMovimento("SAC");
				
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				modificheList.add(spesa);
				
				List<SubAccertamento> nuovaSubImpegnoList = new ArrayList<SubAccertamento>();
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
						sub.setListaModificheMovimentoGestioneEntrata(modificheList);
						sub.setImportoAttuale(importoCambiatoSub);
					}
					nuovaSubImpegnoList.add(sub);
				}
				
				model.setListaSubaccertamenti(nuovaSubImpegnoList);
				
				//Mi salvo il capitolo
				CapitoloEntrataGestione vecchioCapitolo = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione();  
				
				//Info per step2
				List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
				if (model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null) {

					for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
						listaIdModificaEsistenti.add(spesaCorr.getUid());
					}
				}

				//Non veniva popolto il componente con i dati della trans. elemen.
				popolaStrutturaTransazioneElementareAcc();
				
				//compongo la request per il servizio di aggiornamento:
				AggiornaAccertamento requestAggiorna = convertiModelPerChiamataServiziInserisciAggiornaModifiche(true);
				
				//invoco il servizio:
				AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(requestAggiorna);
				
				//setto prosegui con warning a false:
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					//presenza errori
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
			
				Accertamento accertamentoAggiornato = response.getAccertamento();
				accertamentoAggiornato.setCapitoloEntrataGestione(vecchioCapitolo);
				
				model.setAccertamentoInAggiornamento(accertamentoAggiornato);				
				model.setListaSubaccertamenti(accertamentoAggiornato.getElencoSubAccertamenti());
				model.setDisponibilitaSubImpegnare(accertamentoAggiornato.getDisponibilitaSubAccertare());
				
				//Ottimizzazione richiamo ai servizi
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
				
				//Info per step2
				if (model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null) {
					for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
						if (!listaIdModificaEsistenti.contains(spesaCorr.getUid())) {
							model.getMovimentoSpesaModel().setEntrata(spesaCorr);
							model.getMovimentoSpesaModel().setModificaImpegnoSubimpegno(false);
						}
					}
				}

				creaMovGestModelCache();

				//SIAC-640/645
				//pop-up riaccertamento su salva
				if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO) && model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
					//errore per riaccertamento
					listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("ACCERTAMENTO"));
					addErrori(listaErrori);
					return INPUT;

				} else {
					
					//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
					//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
					if(model.getStep1Model().getProvvedimentoSupport()!=null){
						AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
						impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
					}
					
					return GOTO_ELENCO_MODIFICHE;
				}

			} else {
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				caricaDatiSub();
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				addErrori(listaErrori);
				return INPUT;
			}
			
		} else if(tipoImpegno.equals("SubAccertamento") && abbina.equalsIgnoreCase("Modifica Anche Accertamento") ){
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
			//controllo selezione subImpegno
			if(getSubSelected()==null || StringUtils.isEmpty(getSubSelected())){
				//errore per sub omesso
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subAccertamento"));
				addErrori(listaErrori);
				return INPUT;
			}			
			
			checkProvvedimento(listaErrori);
			
			
			checkImportoSubAccertamentoConModificaAccertamento(listaErrori);
			
			checkDescrizioneROR(listaErrori);

			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "subAccertamento", listaErrori);
			
			if(!listaErrori.isEmpty() || erroreProvvedimento) {
				//presenza errori
				addErrori(listaErrori);
				return INPUT;
			}
			
			List<ModificaMovimentoGestioneEntrata> modificheList = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
			
			if(modificheList == null){
				modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
			}
			
			ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();
			
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttuale = model.getAccertamentoInAggiornamento().getImportoAttuale();
			
			BigDecimal importoCambiato = importoAttuale.add(importoDiInput);

			spesa.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
			spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesa.setImportoNew(importoCambiato);
			model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
			
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);

			//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
			spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
			
			spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesa.setDescrizione(getDescrizione().toUpperCase());
			}
			spesa.setTipoMovimento("ACC");
			
			settaDatiReimputazioneDalModel(spesa);
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheList.add(spesa);
			model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(modificheList);
			
			
			//Subimpegno
			List<ModificaMovimentoGestioneEntrata> modificheSubList = new ArrayList<ModificaMovimentoGestioneEntrata>();
			
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					modificheSubList = sub.getListaModificheMovimentoGestioneEntrata();
				}
			}
			
			
			if(modificheSubList == null){
				modificheSubList = new ArrayList<ModificaMovimentoGestioneEntrata>();
			}
			
			ModificaMovimentoGestioneEntrata spesaSub = new ModificaMovimentoGestioneEntrata();
			BigDecimal importoDiInputSub = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttualeSub = convertiImportoToBigDecimal(getImportoAttualeSubImpegno());
			
			BigDecimal importoCambiatoSub = importoAttualeSub.add(importoDiInputSub);
			
			spesaSub.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
			spesaSub.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesaSub.setImportoNew(importoCambiatoSub);
			spesaSub.setUidSubAccertamento(getUidSubSelected());
			
			//DATI DEL PROVVEDIMENTO DELLA MODIFICA SUB:
			spesaSub = settaDatiProvvDalModel(spesaSub, model.getStep1Model().getProvvedimento());
			
			spesaSub.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesaSub.setDescrizione(getDescrizione().toUpperCase());
			}
			spesaSub.setTipoMovimento("SAC");
			
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheSubList.add(spesaSub);
			
			List<SubAccertamento> nuovaSubImpegnoList = new ArrayList<SubAccertamento>();
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					sub.setListaModificheMovimentoGestioneEntrata(modificheSubList);
					sub.setImportoAttuale(importoCambiatoSub);
					
				}
				nuovaSubImpegnoList.add(sub);
			}
			
			model.setListaSubaccertamenti(nuovaSubImpegnoList);
			
			//Mi salvo il capitolo
			CapitoloEntrataGestione vecchioCapitolo = model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione();  				
			
			//Info per step2
			List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
			if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null){
				for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
					listaIdModificaEsistenti.add(spesaCorr.getUid());
				}
			}

			//Non veniva popolto il componente con i dati della trans. elemen.
			popolaStrutturaTransazioneElementareAcc();
			
			//compongo la request per il servizio di aggiornamento:
			AggiornaAccertamento requestAggiorna = convertiModelPerChiamataServiziInserisciAggiornaModifiche(true);
			
			//invoco il servizio:
			AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(requestAggiorna );
			
			//setto prosegui con warning a false:
			model.setProseguiConWarning(false);
			
			if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
				//presenza errori
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				return INPUT;
			}
			
			Accertamento accertamentoAggiornato = response.getAccertamento();
			accertamentoAggiornato.setCapitoloEntrataGestione(vecchioCapitolo);
			
			model.setAccertamentoInAggiornamento(accertamentoAggiornato);				
			model.setListaSubaccertamenti(accertamentoAggiornato.getElencoSubAccertamenti());
			model.setDisponibilitaSubImpegnare(accertamentoAggiornato.getDisponibilitaSubAccertare());
			
			//Ottimizzazione richiamo ai servizi
			model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
			
			//Info per step2
			if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()!=null){
				for (ModificaMovimentoGestioneEntrata spesaCorr : model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata()) {
					if (!listaIdModificaEsistenti.contains(spesaCorr.getUid())) {
						if (spesaCorr.getTipoMovimento().equalsIgnoreCase("ACC")) {
							model.getMovimentoSpesaModel().setEntrata(spesaCorr);
							model.getMovimentoSpesaModel().setModificaImpegnoSubimpegno(true);
						}
					}
				}
			}

			creaMovGestModelCache();

			//SIAC-640/645
			//pop-up riaccertamento su salva
			if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO) && model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
				//errore per riaccertamento automatico
				listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("ACCERTAMENTO"));
				addErrori(listaErrori);
				return INPUT;

			}
			
			//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
			//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
			if(model.getStep1Model().getProvvedimentoSupport()!=null){
				AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
				impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
			}
			
			return GOTO_ELENCO_MODIFICHE;
			
		}
		
		return GOTO_ELENCO_MODIFICHE;
	}

	/**
	 * @param listaErrori
	 */
	private void checkImportoSubAccertamentoConModificaAccertamento(List<Errore> listaErrori) {
		
		if(StringUtils.isEmpty(importoImpegnoModImp)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", "")) || !(importoImpegnoModImp.split(",").length <= 2) || !(importoImpegnoModImp.split(".").length <= 2)){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);

		BigDecimal importoAttualeSub = new BigDecimal(0);

		for(SubAccertamento sub : model.getListaSubaccertamenti()){
			if(getSubSelected().equals(String.valueOf(sub.getNumero()))){
				importoAttualeSub = sub.getImportoAttuale();
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(importoAttualeSub));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(importoAttualeSub));
				setUidSubSelected(sub.getUid());
			}
			
			int minoreImpegno = importoDiInput.compareTo(model.getMovimentoSpesaModel().getMinAncheImpegno());
			int maggioreImpegno = 	importoDiInput.compareTo(model.getMovimentoSpesaModel().getMaxAncheImpegno());
			
			if(minoreImpegno == -1 ){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				break;
			}
			if(maggioreImpegno == 1 && !isImportoSenzaLimiti()){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				break;
			}


			//Controllo range ok sub impegno
			int minoreSubImpegno = importoDiInput.compareTo(model.getMovimentoSpesaModel().getMinAncheImpegno());
			int maggioreSubImpegno = importoDiInput.compareTo(model.getMovimentoSpesaModel().getMaxAncheImpegno());
			
			boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
			
			boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) == 0;
			
			if(isRORDaMantenere && !isImportoZero){
				listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			
			if(!isRORDaMantenere && (minoreSubImpegno == -1 || importoDiInput.compareTo(BigDecimal.ZERO)==0)){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			if(!isRORDaMantenere && maggioreSubImpegno == 1 && !isImportoSenzaLimiti()){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}

		}	
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO))){
			if(importoDiInput.intValue()>0){
				Errore errore = CODICE_MOTIVO_RIACCERTAMENTO.equalsIgnoreCase(getIdTipoMotivo())? ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Accertamento") : ErroreFin.VALORE_NON_VALIDO.getErrore("importo", "(deve essere negativo)");
				listaErrori.add(errore);
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
	}

	/**
	 * @param listaErrori
	 */
	private void controlloImportoSubaccertamento(List<Errore> listaErrori) {
		if(StringUtils.isEmpty(importoImpegnoModImp)) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!(importoImpegnoModImp.split(",").length <= 2)){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!(importoImpegnoModImp.split(".").length <= 2)){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));	
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
					
		for(SubAccertamento sub : model.getListaSubaccertamenti()){
		
			if(getSubSelected().equals(String.valueOf(sub.getNumero()))){
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				setNumeroSubImpegno(String.valueOf(sub.getNumero()));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumero()));
				
				setUidSubSelected(sub.getUid());
				
				BigDecimal importoAttualeSubImpegno = sub.getImportoAttuale();
				minImportoSubApp = new BigDecimal(0).subtract(importoAttualeSubImpegno);
			
				if(sub.getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else if(sub.isFlagDaRiaccertamento()){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else{
					maxImportoSubApp =model.getAccertamentoInAggiornamento().getDisponibilitaSubAccertare();
				}
				
				if(minImportoSubApp == null){
					minImportoSubApp = new BigDecimal(0);
				}
			
				if(maxImportoSubApp == null){
				maxImportoSubApp = new BigDecimal(0);
				}
			}
		}
		
		if(minImportoSubApp == null){
			minImportoSubApp = new BigDecimal(0);
		}
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
		
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
		
		int maxCompare = importoDiInput.compareTo(maxImportoSubApp);
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) ==0;
		
		if(isRORDaMantenere && !isImportoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		if(!isRORDaMantenere && importoDiInput.compareTo(minImportoSubApp) < 0){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		if(!isRORDaMantenere && (maxCompare == 1 || isImportoZero)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

				

		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO))){
			if(importoDiInput.intValue()>0){
				if (getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
					listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Accertamento"));
				}else{
					listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("importo", "(deve essere negativo)"));
				}
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
	}

	/**
	 * @param listaErrori
	 * @param tipoMotivo
	 */
	private void checkDescrizioneROR(List<Errore> listaErrori) {
		if(getIdTipoMotivo() != null && Arrays.asList(CODICI_MOTIVO_ROR).contains(getIdTipoMotivo()) && StringUtils.isEmpty(getDescrizione()) ){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione modifica con motivo ROR"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}

	/**
	 * @param listaErrori
	 * @param listaWarning
	 */
	private void controlloImporto(List<Errore> listaErrori, List<Errore> listaWarning) {
		//importo obbligatorio
		if(StringUtils.isEmpty(importoImpegnoModImp)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!(importoImpegnoModImp.split(",").length <= 2)) {
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!(importoImpegnoModImp.split(".").length <= 2)){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));	
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
		
		boolean importoMinoreDelMinimoAmmissibile = importoDiInput.compareTo(minImportoImpegnoApp)<0;
		boolean importoZero = importoDiInput.compareTo(BigDecimal.ZERO)==0;
		
		if(isRORDaMantenere && !importoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		if(!isRORDaMantenere && (importoMinoreDelMinimoAmmissibile|| importoZero)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		//SIAC-6586
		

		//Jira SIAC-3438
		if(!model.isFlagSuperioreTreAnni() && importoDiInput.compareTo(maxImportoImpegnoApp) >0 && !model.isProseguiConWarningModificaPositivaAccertamento()){ 
			Errore erroreMax = ErroreFin.RANGE_NON_VALIDO.getErrore("massimo");
			listaWarning.add(erroreMax);
			
			addPersistentActionWarning(erroreMax.getCodice() + " - " + "Superamento importo massimo consentito");
			
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			model.setProseguiConWarningModificaPositivaAccertamento(true);
					
		}
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO))){
			if(importoDiInput.intValue()>0){
				Errore errore = getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)? ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Accertamento") : ErroreFin.VALORE_NON_VALIDO.getErrore("importo", "(deve essere negativo)");
				listaErrori.add(errore);
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		//SIAC-6702
		if(!model.getStep1Model().isSaltaControlloLegamiStoricizzati() && BigDecimal.ZERO.compareTo(importoDiInput)>0 && Boolean.TRUE.equals(model.getConLegameStoricizzato())) {
			listaWarning.add(ErroreFin.WARNING_STORICIZZAZIONE_IMPEGNI.getErrore());
			addPersistentActionWarning(ErroreFin.WARNING_STORICIZZAZIONE_IMPEGNI.getErrore().getTesto());
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			model.getStep1Model().setSaltaControlloLegamiStoricizzati(true);
		}
	}

	/**
	 * @param listaErrori
	 */
	private void checkProvvedimento(List<Errore> listaErrori) {
		//SIAC-6586
		//Anno Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		//Numero Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		//Tipo Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}
	
	public String prosegui(){
		setMethodName("prosegui");
	
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Controllo dei campi
		if(tipoImpegno.equalsIgnoreCase("Accertamento")){
			
			return checkAccertamento(listaErrori);
	
			
		}  
		if(tipoImpegno.equals("SubAccertamento") && abbina == null ){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare :  Modifica Anche Accertamento"));
			
			
			if(!listaErrori.isEmpty()){
				//presenza errori
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				caricaDatiSub();
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				addErrori(listaErrori);
				
				setIdTipoMotivo(CODICE_MOTIVO_RIACCERTAMENTO);
				
				return INPUT;
			}
			
			return "prosegui";
			
		} else if(tipoImpegno.equals("SubAccertamento") && abbina.equalsIgnoreCase("Modifica Anche Accertamento") ){
			
			return checkSubAccertamento(listaErrori);
		}
		
		return "prosegui";
	
	}

	/**
	 * @param listaErrori
	 * @return
	 */
	private String checkSubAccertamento(List<Errore> listaErrori) {
		if(!StringUtils.isEmpty(getAnniPluriennali())){
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getAccertamentoInAggiornamento().getAnnoMovimento());
			
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Anni Pluriennali"));
		}
		
		//Anno Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		}
		
		//Numero Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
		}
		
		//Tipo Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}
		
		//Importo Obbligatorio
		if(!StringUtils.isEmpty(importoImpegnoModImp)){
			if(NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
				model.getMovimentoSpesaModel().setImportoTot(convertiImportoToBigDecimal(importoImpegnoModImp));
			} else {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
		}
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO))){
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);				
			if(importoDiInput.intValue()>0){
				if (getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
					listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Accertamento"));
				}else{
					listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("importo", "(deve essere negativo)"));
				}
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		if(listaErrori.isEmpty()){
			//non ci sono errori
			
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getAccertamentoInAggiornamento().getAnnoMovimento());
			List<ImpegniPluriennaliModel> cacheList = new ArrayList<ImpegniPluriennaliModel>();
			model.getMovimentoSpesaModel().setListaImpegniPluriennali(cacheList);
			
			creaMovGestModelCache();
			
			return "prosegui";
			
		} else {
			//presenza errori
			addErrori(listaErrori);
			setIdTipoMotivo(CODICE_MOTIVO_RIACCERTAMENTO);
			return INPUT;
		}
	}

	/**
	 * @param listaErrori
	 * @return
	 */
	private String checkAccertamento(List<Errore> listaErrori) {
		if(!StringUtils.isEmpty(getAnniPluriennali())){
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getAccertamentoInAggiornamento().getAnnoMovimento());
			
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Anni Pluriennali"));
		}
		
		
		//Anno Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		}
		
		//Numero Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
		}
		
		//Tipo Provvedimento Obbligatorio
		if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() != null){
			if(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento() == 0){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}
		
		//Importo Obbligatorio
		if(!StringUtils.isEmpty(importoImpegnoModImp)){
			if(NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
				model.getMovimentoSpesaModel().setImportoTot(convertiImportoToBigDecimal(importoImpegnoModImp));
			}else {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));	
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
		}
		
		//Controllo Motivo obbligatorio
		if(StringUtils.isEmpty(getIdTipoMotivo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Motivo"));
		}
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO))){
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);				
			if(importoDiInput.intValue()>0){
				if (getIdTipoMotivo().equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
					listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Accertamento"));
				}else{
					listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("importo", "(deve essere negativo)"));
				}
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		
		if(listaErrori.isEmpty()){
			//NON CI SONO ERRORI
			
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getAccertamentoInAggiornamento().getAnnoMovimento());
			List<ImpegniPluriennaliModel> cacheList = new ArrayList<ImpegniPluriennaliModel>();
			model.getMovimentoSpesaModel().setListaImpegniPluriennali(cacheList);
			
			//creo la cache del model:
			creaMovGestModelCache();
			
			return "prosegui";
			
		} else {
			//PRESENZA ERRORI
			addErrori(listaErrori);
			setIdTipoMotivo(CODICE_MOTIVO_RIACCERTAMENTO);
			return INPUT;
		}
	}
	
	@Override 
	public String getActionDataKeys() {
		return "model,ta,am";
	}

	public String annulla(){
		setMethodName("annulla");
		return SUCCESS;
	}
	
	public String indietro(){
		setMethodName("indietro");
		return GOTO_ELENCO_MODIFICHE;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getMinImp() {
		return minImp;
	}
	public void setMinImp(String minImp) {
		this.minImp = minImp;
	}
	public String getMaxImp() {
		return maxImp;
	}
	public void setMaxImp(String maxImp) {
		this.maxImp = maxImp;
	}
	public String getMinSub() {
		return minSub;
	}
	public void setMinSub(String minSub) {
		this.minSub = minSub;
	}
	public String getMaxSub() {
		return maxSub;
	}
	public void setMaxSub(String maxSub) {
		this.maxSub = maxSub;
	}
	public String getMinAnche() {
		return minAnche;
	}
	public void setMinAnche(String minAnche) {
		this.minAnche = minAnche;
	}
	public String getMaxAnche() {
		return maxAnche;
	}
	public void setMaxAnche(String maxAnche) {
		this.maxAnche = maxAnche;
	}
	public String getTipoImpegno() {
		return tipoImpegno;
	}
	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}
	public String getImportoAttualeSubImpegno() {
		return importoAttualeSubImpegno;
	}
	public void setImportoAttualeSubImpegno(String importoAttualeSubImpegno) {
		this.importoAttualeSubImpegno = importoAttualeSubImpegno;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public List<String> getTipoImpegnoModificaImportoList() {
		return tipoImpegnoModificaImportoList;
	}
	public void setTipoImpegnoModificaImportoList(
			List<String> tipoImpegnoModificaImportoList) {
		this.tipoImpegnoModificaImportoList = tipoImpegnoModificaImportoList;
	}
	public List<String> getNumeroSubImpegnoList() {
		return numeroSubImpegnoList;
	}
	public void setNumeroSubImpegnoList(List<String> numeroSubImpegnoList) {
		this.numeroSubImpegnoList = numeroSubImpegnoList;
	}
	public List<String> getAbbinaList() {
		return abbinaList;
	}
	public void setAbbinaList(List<String> abbinaList) {
		this.abbinaList = abbinaList;
	}
	public String getAbbina() {
		return abbina;
	}
	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}
	public boolean isSubImpegnoSelected() {
		return subImpegnoSelected;
	}
	public void setSubImpegnoSelected(boolean subImpegnoSelected) {
		this.subImpegnoSelected = subImpegnoSelected;
	}
	public String getSubSelected() {
		return subSelected;
	}
	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}
	public String getAnniPluriennali() {
		return anniPluriennali;
	}
	public void setAnniPluriennali(String anniPluriennali) {
		this.anniPluriennali = anniPluriennali;
	}
	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}
	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}
	public String getNumeroSubImpegno() {
		return numeroSubImpegno;
	}
	public void setNumeroSubImpegno(String numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}
	public String getMinImportoCalcolato() {
		return minImportoCalcolato;
	}
	public void setMinImportoCalcolato(String minImportoCalcolato) {
		this.minImportoCalcolato = minImportoCalcolato;
	}
	public String getMaxImportoCalcolato() {
		return maxImportoCalcolato;
	}
	public void setMaxImportoCalcolato(String maxImportoCalcolato) {
		this.maxImportoCalcolato = maxImportoCalcolato;
	}
	public String getIdSub() {
		return idSub;
	}
	public void setIdSub(String idSub) {
		this.idSub = idSub;
	}
	public String getMinImportoSubCalcolato() {
		return minImportoSubCalcolato;
	}
	public void setMinImportoSubCalcolato(String minImportoSubCalcolato) {
		this.minImportoSubCalcolato = minImportoSubCalcolato;
	}
	public String getMaxImportoSubCalcolato() {
		return maxImportoSubCalcolato;
	}
	public void setMaxImportoSubCalcolato(String maxImportoSubCalcolato) {
		this.maxImportoSubCalcolato = maxImportoSubCalcolato;
	}
	public BigDecimal getMinImportoImpegnoApp() {
		return minImportoImpegnoApp;
	}
	public void setMinImportoImpegnoApp(BigDecimal minImportoImpegnoApp) {
		this.minImportoImpegnoApp = minImportoImpegnoApp;
	}
	public BigDecimal getMaxImportoImpegnoApp() {
		return maxImportoImpegnoApp;
	}
	public void setMaxImportoImpegnoApp(BigDecimal maxImportoImpegnoApp) {
		this.maxImportoImpegnoApp = maxImportoImpegnoApp;
	}
	public BigDecimal getMinImportoSubApp() {
		return minImportoSubApp;
	}
	public void setMinImportoSubApp(BigDecimal minImportoSubApp) {
		this.minImportoSubApp = minImportoSubApp;
	}
	public BigDecimal getMaxImportoSubApp() {
		return maxImportoSubApp;
	}
	public void setMaxImportoSubApp(BigDecimal maxImportoSubApp) {
		this.maxImportoSubApp = maxImportoSubApp;
	}

	public String getImportoImpegnoModImp() {
		return importoImpegnoModImp;
	}

	public void setImportoImpegnoModImp(String importoImpegnoModImp) {
		this.importoImpegnoModImp = importoImpegnoModImp;
	}

	public String getAbbinaChk() {
		return abbinaChk;
	}

	public void setAbbinaChk(String abbinaChk) {
		this.abbinaChk = abbinaChk;
	}


	public Integer getUidSubSelected() {
		return uidSubSelected;
	}


	public void setUidSubSelected(Integer uidSubSelected) {
		this.uidSubSelected = uidSubSelected;
	}


	
}