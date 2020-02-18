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
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciModificaMovimentoSpesaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaAction extends ActionKeyAggiornaImpegno {

	public InserisciModificaMovimentoSpesaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
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
	
	private AttoAmministrativo am;
	
	private static final long serialVersionUID = 2784367365960444695L;
	
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
		
		//Carico Lista Tipo Modifiche
		if(abilitaListaROR()){
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		}else{
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}
			
		//Preparo Attibruti per la pagina
		getAbbinaList().add("Modifica Anche Impegno");
		
		if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0){
			
			//ciclo sui sub:
			for(SubImpegno sub : model.getListaSubimpegni()){				
				if(sub.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D")){
					if(sub.getNumero() != null){
						//aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumero()));
					}	
				}
				
			}
			
			if(numeroSubImpegnoList.size() > 0){
				//ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");
				tipoImpegnoModificaImportoList.add("SubImpegno");
				
			} else {
				//non ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");	
			}

		} else {
			//non ci sono sub
			tipoImpegnoModificaImportoList.add("Impegno");
		}
	
		
		//Calcolo importi
		
		if(model.getDisponibilitaImpegnoModifica()==null){
			model.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
		}
		
		//FIX PER JIRA SIAC-3801
		//minImportoImpegnoApp = minoreTraDisponibileInModificaEDispAVincolare().negate();

		// RM JIRA 5371
		minImportoImpegnoApp = model.getDisponibilitaImpegnoModifica().negate();
		
		
		//
		
		if(model.getImpegnoInAggiornamento().getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else if(model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else {
			//SIAC-580
			
			if(model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() ){
				// anno corrente
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno1();
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +1) ){
//				anno  +1
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno2();
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +2) ){
//				anno +2
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno3();
			
			//SIAC-6990
			//si consente l'inserimento delle modifiche solo sui tre anni successivi (inerenti alla disponibilita' massima dei capitoli)
			}else{
				//  da GESTIRE NELLA PAGINA
//				addErrore(new Errore("FIN_ERR_0289", "Impossibile inserire modifiche superiori alla durata dei capitoli"));
				model.setFlagSuperioreTreAnni(true);
				return;
			}
		}
		
		if(minImportoImpegnoApp == null){
			//pongo a zero
			minImportoImpegnoApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp == null){
			//pongo a zero
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		
		//GESTIONE FLAG SDF:
		if(model.getImpegnoInAggiornamento().isFlagSDF()){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		//
		
		if(maxImportoImpegnoApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//Setto il massimo e i minimo nelle variabili finali:
		setMaxImp(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImpMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		setMinImp(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImpMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		
		setMinImportoCalcolato(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImportoCalcolatoMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		setMaxImportoCalcolato(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImportoCalcolatoMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		
		if (StringUtils.isEmpty(getAnniPluriennali())) {
			setAnniPluriennali("1");
		}
				
	}

	
	/**
	 * execute della action
	 */
	@Override
	public String execute() throws Exception {
		setMethodName("execute");
		tipoImpegno = "Impegno";
		creaMovGestModelCache();
		//mi salvo temporaneamente il provvedimento per reimpostarlo nell'impegno e associare il nuovo provv alle sole modifiche
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimento());
			setAm(attoImpegno);
		}
		return SUCCESS;
	}
	
	
	public String caricaDatiSub(){
		setMethodName("caricaDatiSub");
		
		if (getAbbinaChk()!= null && getAbbinaChk().equalsIgnoreCase("true")) {
			setAbbina("Modifica Anche Impegno");
		} else {
			setAbbina(null);
		}
		
		if(StringUtils.isEmpty(getTipoImpegno())){
			setTipoImpegno("Impegno");
			setSubImpegnoSelected(false);
			model.setSubImpegnoSelectedMod(false);
		} else if(getTipoImpegno().equalsIgnoreCase("SubImpegno")){
			if(!StringUtils.isEmpty(getSubSelected())){
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				
				for(SubImpegno sub : model.getListaSubimpegni()){
					if(String.valueOf(sub.getNumero()).equals(getSubSelected())){
						
						setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						setNumeroSubImpegno(String.valueOf(sub.getNumero()));
						model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumero()));
						
						if(sub.getDisponibilitaImpegnoModifica()==null){
							sub.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
						}
						
						minImportoSubApp = sub.getDisponibilitaImpegnoModifica().negate();
						
						if(model.getImpegnoInAggiornamento().getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
							maxImportoSubApp = new BigDecimal(0);
						} else if(model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()){
							maxImportoSubApp = new BigDecimal(0);
						} else{
							maxImportoSubApp =model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare();
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
						model.setMinImportoSubCalcolatoMod(convertiBigDecimalToImporto(minImportoSubApp));
						setMaxImportoSubCalcolato(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMaxImportoSubCalcolatoMod(convertiBigDecimalToImporto(maxImportoSubApp));
						
						
						//Parte per anche impegno
						
						//Modifica importi richiesta
						setMinAnche(convertiBigDecimalToImporto(minImportoSubApp));
						model.getMovimentoSpesaModel().setMinAncheImpegno(minImportoSubApp);
						model.setMinAncheMod(convertiBigDecimalToImporto(minImportoSubApp));
						
						if(model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() ){
							// anno corrente
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno1();
						}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +1) ){
							//anno  +1
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno2();
						}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +2) ){
							//anno +2
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno3();
						}else{
							//  da GESTIRE NELLA PAGINA
							model.setFlagSuperioreTreAnni(true);
						}
						
						//GESTIONE FLAG SDF:
						if(model.getImpegnoInAggiornamento().isFlagSDF()){
							maxImportoImpegnoApp = new BigDecimal(0);
						}
						//
						
						//Modifica importi richiesta
						setMaxAnche(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						model.getMovimentoSpesaModel().setMaxAncheImpegno(maxImportoImpegnoApp);
						model.setMaxAncheMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						
					}
				}
				
				
			} else {
				setSubImpegnoSelected(false);
				model.setSubImpegnoSelectedMod(false);
				tipoImpegno = "Impegno";
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
	
	public String salvaConByPassDodicesimi() throws Exception {
		return salvaInternal(true);
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @return
	 * @throws Exception
	 */
	public String salva() throws Exception {
		return salvaInternal(false);
	}
	
	public String salvaInternal(boolean byPassDodicesimi){
		setMethodName("salva");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreProvvedimento = false;
		
		String tipoMotivo = getIdTipoMotivo();
		
		//16 FEBBRAIO 2017 - clono i dati che stanno per essere cambiati nel model per ripristinarli in caso di salvataggio andato male:
		GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva = clone(model.getMovimentoSpesaModel());
		GestisciImpegnoStep1Model step1ModelPrimaDiSalva = clone(model.getStep1Model());
		Impegno impegnoInAggiornamentoPrimaDiSalva = clone(model.getImpegnoInAggiornamento());
		List<SubImpegno> listaSubPrimaDiSalva = clone(model.getListaSubimpegni());
		//
		
		//Controllo dei campi
		if(tipoImpegno.equalsIgnoreCase("Impegno")){
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
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
			
			//Controllo gestisci decentrato:
			listaErrori = controlloGestisciImpegnoDecentratoPerModifica(listaErrori);
			
			//Descrizione
			if(StringUtils.isEmpty(getIdTipoMotivo())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			
			checkImportoModificaSuTestata(listaErrori);
			
			checkDescrizioneROR(listaErrori);
		
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(),"impegno", listaErrori);
			
			if(!listaErrori.isEmpty() || erroreProvvedimento) {
				addErrori(listaErrori);
				return INPUT;
			}
			
			List<ModificaMovimentoGestioneSpesa> modificheList = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
			
			//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
			modificheList = FinUtility.rimuoviConIdZero(modificheList);
			//
			
			if(modificheList == null){
				modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}
			
			ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
			
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttuale = model.getImpegnoInAggiornamento().getImportoAttuale();
			
			BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
	
			//Setto l'uid a zero cosi da riconoscere che e nuovo:
			spesa.setUid(0);
			
			spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesa.setImportoNew(importoCambiato);
			model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
			
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);
		
			//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
			spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
			
	 		spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesa.setDescrizione(getDescrizione().toUpperCase());
			}
			spesa.setTipoMovimento("IMP");
			
			settaDatiReimputazioneDalModel(spesa);
				
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheList.add(spesa);
			model.getImpegnoInAggiornamento().setListaModificheMovimentoGestioneSpesa(modificheList);
			
			//Info per step2
			List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
			if(model.getImpegnoInAggiornamento()!=null && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()!=null){
				for (ModificaMovimentoGestioneSpesa spesaCorr : model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()) {
					listaIdModificaEsistenti.add(spesaCorr.getUid());
				}
			}
	
			//Non veniva popolato il componente con i dati della trans. elemen.
			popolaStrutturaTransazioneElementare();
	
			boolean esito = false; 
			
			
			AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(false);
			
			
			if (requestAggiorna.getImpegno().getProgetto() != null) {
				Progetto progetto = requestAggiorna.getImpegno().getProgetto();
				
				progetto.setTipoProgetto(TipoProgetto.GESTIONE);
				progetto.setBilancio(sessionHandler.getBilancio());
	
				requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
				requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
			}
			
			
			requestAggiorna.getImpegno().setByPassControlloDodicesimi(byPassDodicesimi);
			
			//SIAC-6990
			//si invalida l'inserimento della modifica dell'impegno nel primo step se la modifica e' di tipo RIACCERTAMENTO
			if(!"RIAC".equalsIgnoreCase(tipoMotivo)) {
				
				AggiornaImpegnoResponse response = movimentoGestionService.aggiornaImpegno(requestAggiorna);
		
				model.setProseguiConWarning(false);
		
				esito = checkResponsePostModificheDiImporto(response,byPassDodicesimi,
				movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva,
				impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva);
		
				if(!esito){ return INPUT; }
			
			} else {
				//SIAC-6990
				//passo il dato creato al secondo step dove verrà gestita una transazione unica per le varie richieste
				model.setImpegnoRequestStep1(requestAggiorna);
			}
	
			
			
			//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
			pulisciTransazioneElementare();
			//
			
			//facciamo ricaricare l'impegno:
			forceReload = true;
	
			//SIAC-640/645
			//pop-up riaccertamento su salva
			
			if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase("RIAC")
					&& model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
				listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("IMPEGNO"));
				addErrori(listaErrori);
				return INPUT;
	
			}
			
			return GOTO_ELENCO_MODIFICHE;

			
		}  else if(tipoImpegno.equals("SubImpegno") && abbina == null ){
			
			//controllo selezione subImpegno
			if(getSubSelected()==null || StringUtils.isEmpty(getSubSelected())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subImpegno"));
				addErrori(listaErrori);
				return INPUT;
			}
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
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
			
			//Importo Obbligatorio
			checkImportoModificaSuSub(listaErrori);
			
			checkDescrizioneROR(listaErrori);
			
			
			if(getIdTipoMotivo()!=null && getIdTipoMotivo().equalsIgnoreCase("RIAC")){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare :  Modifica Anche Impegno"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(),"subImpegno", listaErrori);
		
			if(!listaErrori.isEmpty() || erroreProvvedimento) {
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				caricaDatiSub();
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				addErrori(listaErrori);
				return INPUT;
			}
			
			List<ModificaMovimentoGestioneSpesa> modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					modificheList = sub.getListaModificheMovimentoGestioneSpesa();
				}
			}
			
			//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
			modificheList = FinUtility.rimuoviConIdZero(modificheList);
			//
			
			if(modificheList == null){
				modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}
			
			ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
			BigDecimal importoDiInputSub = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttualeSub = convertiImportoToBigDecimal(getImportoAttualeSubImpegno());
			
			BigDecimal importoCambiatoSub = importoAttualeSub.add(importoDiInputSub);
			
			//Setto l'uid a zero cosi da riconoscere che e nuovo:
			spesa.setUid(0);
			
			spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesa.setImportoNew(importoCambiatoSub);
			
			//COPIO I DATI DEL PROVVEDIMENTO:
			spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
			
			spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesa.setDescrizione(getDescrizione().toUpperCase());
			}
			spesa.setTipoMovimento("SIM");
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheList.add(spesa);
			
			List<SubImpegno> nuovaSubImpegnoList = new ArrayList<SubImpegno>();
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					sub.setImportoAttuale(importoCambiatoSub);
					sub.setListaModificheMovimentoGestioneSpesa(modificheList);
				}
				nuovaSubImpegnoList.add(sub);
			}
			
			model.setListaSubimpegni(nuovaSubImpegnoList);

			//Info per step2
			List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
			if(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()!=null  && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().size()>0){
				for (ModificaMovimentoGestioneSpesa spesaCorr : model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()) {
					listaIdModificaEsistenti.add(spesaCorr.getUid());
				}
			}
			//Non veniva popolto il componente con i dati della trans. elemen.
			popolaStrutturaTransazioneElementare();
			
			//Eventuale by pass controllo dodicesimi:
			AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(false);
			requestAggiorna.getImpegno().setByPassControlloDodicesimi(byPassDodicesimi);
			//
			
			
			if (requestAggiorna.getImpegno().getProgetto() != null) {
				Progetto progetto = requestAggiorna.getImpegno().getProgetto();
				
				progetto.setTipoProgetto(TipoProgetto.GESTIONE);
				progetto.setBilancio(sessionHandler.getBilancio());

				requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
				requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
			}
			
			//SIAC-6990
			//si invalida l'inserimento della modifica dell'impegno nel primo step se la modifica e' di tipo RIACCERTAMENTO
			if(!"RIAC".equalsIgnoreCase(tipoMotivo)) {
				
				AggiornaImpegnoResponse response = movimentoGestionService.aggiornaImpegno(requestAggiorna);
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					
					//per il modale di conferma per bypassare il controllo dodicesimi:
					if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
						
						ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva);
						
						setShowModaleConfermaSalvaConBypassDodicesimi(true);
						
						//setto prosegui con warning a true per evitare un loop infinito tra i due byPass
						model.setProseguiConWarning(true);
						
						return INPUT;
					}
					
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
				
			} else {
				//SIAC-6990
				//passo il dato creato al secondo step dove verrà gestita una transazione unica per le varie richieste
				model.setImpegnoRequestStep1(requestAggiorna);
			}
			
			
			//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
			pulisciTransazioneElementare();
			//
			
			forceReload = true;

			//SIAC-640/645
			//pop-up riaccertamento su salva
			if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase("RIAC")
					&& model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
				listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("IMPEGNO"));
				addErrori(listaErrori);
				return INPUT;

			}
			
			return GOTO_ELENCO_MODIFICHE;
			
		} else if(tipoImpegno.equals("SubImpegno") && abbina.equalsIgnoreCase("Modifica Anche Impegno") ){
			
			//controlli su reimputazione:
			listaErrori = controlliReimputazioneInInserimentoEMotivoRIAC(listaErrori, tipoImpegno, abbina, importoImpegnoModImp,tipoMotivo);
			
			//controllo selezione subImpegno
			if(getSubSelected()==null || StringUtils.isEmpty(getSubSelected())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subImpegno"));
				addErrori(listaErrori);
				return INPUT;
			}
			
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
			
			//Importo Obbligatorio
			checkImportoModificaSuSubETestata(listaErrori);
			checkDescrizioneROR(listaErrori);
			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(),"subImpegno", listaErrori);
			
			if(!listaErrori.isEmpty() ||erroreProvvedimento) {
				addErrori(listaErrori);
				return INPUT;
			}
			
			
			//SIAC-6929
			ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
			if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null 
					&& model.getStep1Model().getProvvedimento().getUid().intValue()>0){
				pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
				if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
					addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
					return INPUT;
				}
			}
			
				//Impegno
			List<ModificaMovimentoGestioneSpesa> modificheList = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
			
			//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
			modificheList = FinUtility.rimuoviConIdZero(modificheList);
			//
			
			if(modificheList == null){
				modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}
			
			ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
			
			settaDatiReimputazioneDalModel(spesa);
			
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttuale = model.getImpegnoInAggiornamento().getImportoAttuale();
			
			BigDecimal importoCambiato = importoAttuale.add(importoDiInput);

			//Setto l'uid a zero cosi da riconoscere che e nuovo:
			spesa.setUid(0);
			
			spesa.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesa.setImportoNew(importoCambiato);
			
			model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
			
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);

			//COPIO I DATI DEL PROVVEDIMENTO:
			spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());
			
			//salvo in un clone prima che venga ripristinato:
			ProvvedimentoImpegnoModel provvedimentoModifica = clone(model.getStep1Model().getProvvedimento());
			
			spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesa.setDescrizione(getDescrizione().toUpperCase());
			}
			spesa.setTipoMovimento("IMP");
			
			//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
			if(getAm()!=null && getAm().getAnno()!=0){
				impostaProvvNelModel(getAm(), model.getStep1Model().getProvvedimento());
			}
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheList.add(spesa);
			model.getImpegnoInAggiornamento().setListaModificheMovimentoGestioneSpesa(modificheList);
			
			
			//Subimpegno
			List<ModificaMovimentoGestioneSpesa> modificheSubList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					modificheSubList = sub.getListaModificheMovimentoGestioneSpesa();
				}
			}
			
			//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
			modificheList = FinUtility.rimuoviConIdZero(modificheList);
			//
			
			if(modificheSubList == null){
				modificheSubList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}
			
			ModificaMovimentoGestioneSpesa spesaSub = new ModificaMovimentoGestioneSpesa();
			
			settaDatiReimputazioneDalModel(spesaSub);
			
			BigDecimal importoDiInputSub = convertiImportoToBigDecimal(importoImpegnoModImp);
			BigDecimal importoAttualeSub = convertiImportoToBigDecimal(getImportoAttualeSubImpegno());
			
			BigDecimal importoCambiatoSub = importoAttualeSub.add(importoDiInputSub);
			
			//Setto l'uid a zero cosi da riconoscere che e nuovo:
			spesaSub.setUid(0); 
			
			spesaSub.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
			spesaSub.setImportoNew(importoCambiatoSub);
			
			//SETTO IL PROV ANCHE NELLA MODIFICA DEL SUB:
			spesaSub = settaDatiProvvDalModel(spesaSub, provvedimentoModifica);
			
			spesaSub.setTipoModificaMovimentoGestione(getIdTipoMotivo());
			if(getDescrizione()!=null){
				spesaSub.setDescrizione(getDescrizione().toUpperCase());
			}
			spesaSub.setTipoMovimento("SIM");
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheSubList.add(spesaSub);
			
			List<SubImpegno> nuovaSubImpegnoList = new ArrayList<SubImpegno>();
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					//trovato il sub
					sub.setImportoAttuale(importoCambiatoSub);
					sub.setListaModificheMovimentoGestioneSpesa(modificheSubList);
				}
				nuovaSubImpegnoList.add(sub);
			}
			
			model.setListaSubimpegni(nuovaSubImpegnoList);
			
			//Info per step2
			List<Integer> listaIdModificaEsistenti = new ArrayList<Integer>();
			if(model.getImpegnoInAggiornamento()!=null && model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()!=null){
				for (ModificaMovimentoGestioneSpesa spesaCorr : model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa()) {
					listaIdModificaEsistenti.add(spesaCorr.getUid());
				}
			}

			//Non veniva popolto il componente con i dati della trans. elemen.
			popolaStrutturaTransazioneElementare();
			//
			
			//Eventuale by pass controllo dodicesimi:
			AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(false);
			requestAggiorna.getImpegno().setByPassControlloDodicesimi(byPassDodicesimi);
			//
			
			if (requestAggiorna.getImpegno().getProgetto() != null) {
				Progetto progetto = requestAggiorna.getImpegno().getProgetto();
				
				progetto.setTipoProgetto(TipoProgetto.GESTIONE);
				progetto.setBilancio(sessionHandler.getBilancio());

				requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
				requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
			}
			
			
			//SIAC-6990
			//si invalida l'inserimento della modifica dell'impegno nel primo step se la modifica e' di tipo RIACCERTAMENTO
			if(!"RIAC".equalsIgnoreCase(tipoMotivo)) {
				
				//richiamo il servizio di aggiornamento:
				AggiornaImpegnoResponse response = movimentoGestionService.aggiornaImpegno(requestAggiorna);
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					
					//per il modale di conferma per bypassare il controllo dodicesimi:
					if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
						
						ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva);
						
						setShowModaleConfermaSalvaConBypassDodicesimi(true);
						
						//setto prosegui con warning a true per evitare un loop infinito tra i due byPass
						model.setProseguiConWarning(true);
						
						return INPUT;
					}
					
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
			
			} else {
				//SIAC-6990
				//passo il dato creato al secondo step dove verrà gestita una transazione unica per le varie richieste
				model.setImpegnoRequestStep1(requestAggiorna);
			}
			
			
			//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
			pulisciTransazioneElementare();
			//
			
			forceReload = true;

			//SIAC-640/645
			//pop-up riaccertamento su salva
			if (getIdTipoMotivo() != null && getIdTipoMotivo().equalsIgnoreCase("RIAC")
					&& model.getStep1Model().isCheckproseguiMovimentoSpesa() == true) {
				//errore riaccertamento automatico
				listaErrori.add(ErroreFin.RIACCERTAMENTO_AUTOMATICO.getErrore("IMPEGNO"));
				addErrori(listaErrori);
				return INPUT;

			} 
				return GOTO_ELENCO_MODIFICHE;

			
		}
		
		return GOTO_ELENCO_MODIFICHE;
	}

	/**
	 * @param listaErrori
	 */
	private void checkImportoModificaSuSubETestata(List<Errore> listaErrori) {
		
		if(StringUtils.isEmpty(importoImpegnoModImp)){

			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
			
		}
		
		if(!(importoImpegnoModImp.split(",").length <= 2) || !(importoImpegnoModImp.split(".").length <= 2) || !NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
		BigDecimal importoAttualeSub = new BigDecimal(0);

		for(SubImpegno sub : model.getListaSubimpegni()){
			if(getSubSelected().equals(String.valueOf(sub.getNumero()))){
				importoAttualeSub = sub.getImportoAttuale();
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(importoAttualeSub));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(importoAttualeSub));
			}

			boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
			
			boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) == 0;
			
			if(isRORDaMantenere && !isImportoZero){
				listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}

			//Controllo range ok sub impegno
			
			if(!isRORDaMantenere) {
				
				//Controllo range ok impegno
				int minoreImpegno = importoDiInput.compareTo(model.getMovimentoSpesaModel().getMinAncheImpegno());
				int maggioreImpegno = 	importoDiInput.compareTo(model.getMovimentoSpesaModel().getMaxAncheImpegno());

				if(minoreImpegno == -1 || importoDiInput.compareTo(BigDecimal.ZERO)==0){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
					break;
				}
				if(maggioreImpegno == 1){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
					break;
				}
				
				//controllo range ok sub
				if(importoDiInput.compareTo(model.getMovimentoSpesaModel().getMinAncheImpegno()) <0|| importoDiInput.compareTo(BigDecimal.ZERO)==0){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				}
				if(importoDiInput.compareTo(model.getMovimentoSpesaModel().getMaxAncheImpegno()) > 0){
					listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
				}
			}

			
		}	
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && importoDiInput.intValue()>0 &&getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase("RIAC"))){
			Errore errore = getIdTipoMotivo().equalsIgnoreCase("RIAC")? ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno") : ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere negativo)");
			listaErrori.add(errore);
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}

	/**
	 * @param listaErrori
	 */
	private void checkImportoModificaSuSub(List<Errore> listaErrori) {
		if(StringUtils.isEmpty(importoImpegnoModImp)){

			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
			
		}
		
		if(!(importoImpegnoModImp.split(",").length <= 2) || !(importoImpegnoModImp.split(".").length <= 2) || !NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		for(SubImpegno sub : model.getListaSubimpegni()){
			
			if(getSubSelected().equals(String.valueOf(sub.getNumero()))){
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				
				setNumeroSubImpegno(String.valueOf(sub.getNumero()));
				model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumero()));
				
				BigDecimal importoAttualeSubImpegno = sub.getImportoAttuale();
				minImportoSubApp = new BigDecimal(0).subtract(importoAttualeSubImpegno);
				
				if(sub.getAnnoMovimento() < Integer.valueOf(sessionHandler.getAnnoEsercizio())){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else if(sub.isFlagDaRiaccertamento()){
					maxImportoSubApp = sub.getImportoAttuale().subtract(importoAttualeSubImpegno);
				} else{
					maxImportoSubApp =model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare();
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
		
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(importoDiInput) == 0;
		
		if(isRORDaMantenere && !isImportoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		if(!isRORDaMantenere) {
			if(importoDiInput.compareTo(minImportoSubApp) <0){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
			if(importoDiInput.compareTo(maxImportoSubApp) >0 || isImportoZero){
				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && importoDiInput.intValue()>0 &&  getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase("RIAC"))){
			Errore errore = getIdTipoMotivo().equalsIgnoreCase("RIAC")? ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno") : ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere negativo)");
			listaErrori.add(errore);
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}

	/**
	 * @param listaErrori
	 */
	private void checkImportoModificaSuTestata(List<Errore> listaErrori) {
		//Importo Obbligatorio
		//SIAC-6586: aggiunto controllo importi e raggruppato, vedere se possibile in un secondo momento riorganizzare
		if(StringUtils.isEmpty(importoImpegnoModImp)) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		if(!(importoImpegnoModImp.split(",").length <= 2) || !(importoImpegnoModImp.split(".").length <= 2) || !NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))){
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo : 1,00 con decimali, altrimenti 10000 o 1.000 oppure 1.000,00"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			return;
		}
		
		BigDecimal importoModificaSuTestata = convertiImportoToBigDecimal(importoImpegnoModImp);
		
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(importoModificaSuTestata) == 0;
		
		if(isRORDaMantenere && !isImportoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		if(!isRORDaMantenere && (importoModificaSuTestata.compareTo(minImportoImpegnoApp)<0 || isImportoZero)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		if((!isRORDaMantenere && !model.isFlagSuperioreTreAnni() && importoModificaSuTestata.compareTo(maxImportoImpegnoApp) >0)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("massimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
		
		//Controllo modifiche solo negative per RIU e RIAC - jira 1052
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && 
				(getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase("RIAC")) && importoModificaSuTestata.intValue()>0){
			Errore errore = getIdTipoMotivo().equalsIgnoreCase("RIAC")? ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno") : ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere negativo)");
			listaErrori.add(errore);
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}
	
	/**
	 * @param listaErrori
	 * @param tipoMotivo
	 */
	private void checkDescrizioneROR(List<Errore> listaErrori) {
		if(getIdTipoMotivo()!= null && Arrays.asList(CODICI_MOTIVO_ROR).contains(getIdTipoMotivo()) && StringUtils.isEmpty(getDescrizione()) ){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione modifica con motivo ROR"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}

	/**
	 * @param byPassDodicesimi
	 * @param movimentoSpesaModelPrimaDiSalva
	 * @param step1ModelPrimaDiSalva
	 * @param impegnoInAggiornamentoPrimaDiSalva
	 * @param listaSubPrimaDiSalva
	 */
	public boolean  checkResponsePostModificheDiImporto(
			ServiceResponse sr,
			boolean byPassDodicesimi,
			GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva,
			GestisciImpegnoStep1Model step1ModelPrimaDiSalva,
			Impegno impegnoInAggiornamentoPrimaDiSalva,
			List<SubImpegno> listaSubPrimaDiSalva) {
		
		if(sr.isFallimento() || sr.getErrori()!=null && !sr.getErrori().isEmpty() ){
			
			if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(sr)){
				
				ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva);
				
				setShowModaleConfermaSalvaConBypassDodicesimi(true);
				
				//setto prosegui con warning a true per evitare un loop infinito tra i due byPass
				model.setProseguiConWarning(true);
				
				return false;
			}
			
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, sr);
			
			return false;
		
		}
		
		return true;
	}
	
	/**
	 * In caso di risposa negativo del servizio vengono risettati i dati nel model per evitare
	 * di avere una pagina incompleta
	 * 
	 * @param movimentoSpesaModelPrimaDiSalva
	 * @param step1ModelPrimaDiSalva
	 * @param impegnoInAggiornamentoPrimaDiSalva
	 * @param listaSubPrimaDiSalva
	 */
	private void ripristinaDatiNelModelPerSalvataggioConErrori(GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva,
			GestisciImpegnoStep1Model step1ModelPrimaDiSalva,Impegno impegnoInAggiornamentoPrimaDiSalva,List<SubImpegno> listaSubPrimaDiSalva){
		//setto nel model i backup che mi sono fatto prima di invocare il servizio:
		model.setMovimentoSpesaModel(movimentoSpesaModelPrimaDiSalva);
		model.setStep1Model(step1ModelPrimaDiSalva);
		model.setImpegnoInAggiornamento(impegnoInAggiornamentoPrimaDiSalva);
		model.setListaSubimpegni(listaSubPrimaDiSalva);
	}
	
	/**
	 * Metodo che stabilisce se l'eventuale salvataggio riguardera' un impegno
	 * senza disponibilita' di fondi che passa a normale
	 */
	@Override
	public boolean salvaDaSDFANormale() {
		if(oggettoDaPopolareImpegno()){
			//vale solo per impegni
			if(model.getImpegnoInAggiornamento().isFlagSDF()){
				
				if(coinvoltoImpegno()){
					//La modifica di importo coinvolge l'impegno
					
					BigDecimal disponibilitaImpegnare = disponibilitaImpegnare();
					
					BigDecimal importoModifica = model.getImpMod();
					
					if(importoModifica!=null){
						BigDecimal nuovaDispCapitolo = disponibilitaImpegnare.subtract(importoModifica);
						
						if(nuovaDispCapitolo.compareTo(BigDecimal.ZERO)>=0){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * semplice utility per verificare se la modifica riguarda anche l'impegno
	 * @return
	 */
	private boolean coinvoltoImpegno(){
		boolean coinvoltoImpegno = false;
		String cmptnz = model.getCmptnz();
		boolean abbinaChecked = model.isAbbchckd();
		if(!FinStringUtils.isEmpty(cmptnz)){
			if(cmptnz.equalsIgnoreCase("Impegno")){
				//selezionato direttamente l'impegno
				coinvoltoImpegno = true;
			}
			if(cmptnz.equalsIgnoreCase("SubImpegno") && abbinaChecked){
				//selezionato sub, ma checkato anche abbina impegno
				coinvoltoImpegno = true;
			}
		}
		return coinvoltoImpegno;
	}

	/**
	 * Gestione pulsante prosegui
	 */
	public String prosegui(){
		
		//info per debug:
		setMethodName("prosegui");
		
		//istanzio la lista degli eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Controllo dei campi
		if(tipoImpegno.equalsIgnoreCase("Impegno")){
			
			//richiamo il metodo con i controlli specifici per questa casistica:
			listaErrori = controlliProseguiImpegno(listaErrori);
			
			if(listaErrori.isEmpty()){
				//non sono emersi errori:

				//SIAC-6990_bis
				//si invalida questa richiesta in quanto scala il doppio dei soldi
				
//				model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
//				model.getMovimentoSpesaModel().setAnnoImpegno(model.getImpegnoInAggiornamento().getAnnoMovimento());
//				List<ImpegniPluriennaliModel> cacheList = new ArrayList<ImpegniPluriennaliModel>();
//				model.getMovimentoSpesaModel().setListaImpegniPluriennali(cacheList);
				
//				creaMovGestModelCache();
				//
				
				return "prosegui";
				
			} else {
				//presenza errori
				addErrori(listaErrori);
				setIdTipoMotivo("RIAC");
				return INPUT;
			}
			
		} else if(tipoImpegno.equals("SubImpegno") && abbina == null ){
			
			//occorre selezionare anche l'impegno:
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare :  Modifica Anche Impegno"));
			
			if(!listaErrori.isEmpty()){
				//errore
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				caricaDatiSub();
				
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
				addErrori(listaErrori);
				
				setIdTipoMotivo("RIAC");
				
				return INPUT;
			} 
			
		} else if(tipoImpegno.equals("SubImpegno") && abbina.equalsIgnoreCase("Modifica Anche Impegno") ){
			
			//richiamo il metodo con i controlli specifici per questa casistica:
			listaErrori = controlliProseguiSubEAbbinaAncheImpegno(listaErrori);
			
			if(listaErrori.isEmpty()){
				//non sono emersi errori:
				
				//SIAC-6990_bis
				//si invalida questa richiesta in quanto scala il doppio dei soldi
				
//				model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
//				model.getMovimentoSpesaModel().setAnnoImpegno(model.getImpegnoInAggiornamento().getAnnoMovimento());
//				List<ImpegniPluriennaliModel> cacheList = new ArrayList<ImpegniPluriennaliModel>();
//				model.getMovimentoSpesaModel().setListaImpegniPluriennali(cacheList);
				
//				creaMovGestModelCache();
				//
				
				return "prosegui";
				
			} else {
				//presenza errori
				addErrori(listaErrori);
				setIdTipoMotivo("RIAC");
				return INPUT;
			}
		}
		return "prosegui";
	}
	
	/**
	 * controlli specifici al prosegui avendo selezionato un sub e indicando
	 * di applicare la modifica anche all'impegno
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> controlliProseguiSubEAbbinaAncheImpegno(List<Errore> listaErrori){
		
		//anni pluriennali
		if(!StringUtils.isEmpty(getAnniPluriennali())){
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getImpegnoInAggiornamento().getAnnoMovimento());
			
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
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase("RIAC"))){
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);				
			if(importoDiInput.intValue()>0){
				if (getIdTipoMotivo().equalsIgnoreCase("RIAC")) {
					listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno"));
				}else{
					listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere negativo)"));
				}
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		return listaErrori;
	}
	
	/**
	 * controlli specifici al prosegui avendo selezionato un impegno
	 * 
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> controlliProseguiImpegno(List<Errore> listaErrori){
		
		//SIAC-6990
		//si imposta un limite alla modifica che sia congruente con i capitoli
		if(Integer.valueOf(getAnniPluriennali()) > 2) {
			listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Numero Anni superiore a quelli associabili al capitolo"));
		}
		
		//anni pluriennali
		if(!StringUtils.isEmpty(getAnniPluriennali())){
			model.getMovimentoSpesaModel().setNumeroPluriennali(Integer.valueOf(getAnniPluriennali()));
			model.getMovimentoSpesaModel().setAnnoImpegno(model.getImpegnoInAggiornamento().getAnnoMovimento());
			
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
			
		//Controllo Motivo obbligatorio
		if(StringUtils.isEmpty(getIdTipoMotivo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Motivo"));
		}
		
		//Controllo modifiche solo negative per RIU e RIAC
		if(!StringUtils.isEmpty(importoImpegnoModImp) && getIdTipoMotivo()!=null && (getIdTipoMotivo().equalsIgnoreCase("RIU") || getIdTipoMotivo().equalsIgnoreCase("RIAC"))){
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);				
			if(importoDiInput.intValue()>0){
				if (getIdTipoMotivo().equalsIgnoreCase("RIAC")) {
					listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_MOD_AUM.getErrore("Impegno"));
				}else{
					listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("importo","(deve essere negativo)"));
				}
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		
		return listaErrori;
	}
	
	

	
	/**
	 * gestione pulsante annulla
	 */
	public String annulla(){
		//info per debug:
		setMethodName("annulla");
		//redirect succes:
		return SUCCESS;
	}
	
	/**
	 * gestione pulsante indietro
	 * @return
	 */
	public String indietro(){
		//info per debug:
		setMethodName("indietro");
		//setto force reload a false:
		setForceReload(false);
		//ritorno all'elenco modifiche:
		return GOTO_ELENCO_MODIFICHE;
	}
	
	@Override 
	public String getActionDataKeys() {
		return "model,ta,am";
	}
	
	//GETTER AND SETTER
	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
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


	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getSubSelected() {
		return subSelected;
	}

	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}

	public boolean isSubImpegnoSelected() {
		return subImpegnoSelected;
	}

	public void setSubImpegnoSelected(boolean subImpegnoSelected) {
		this.subImpegnoSelected = subImpegnoSelected;
	}

	public String getNumeroSubImpegno() {
		return numeroSubImpegno;
	}

	public void setNumeroSubImpegno(String numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
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


	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}


	public String getAnniPluriennali() {
		return anniPluriennali;
	}

	public void setAnniPluriennali(String anniPluriennali) {
		this.anniPluriennali = anniPluriennali;
	}


	public String getImportoAttualeSubImpegno() {
		return importoAttualeSubImpegno;
	}


	public void setImportoAttualeSubImpegno(String importoAttualeSubImpegno) {
		this.importoAttualeSubImpegno = importoAttualeSubImpegno;
	}


	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}


	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}


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


	public AttoAmministrativo getAm() {
		return am;
	}


	public void setAm(AttoAmministrativo am) {
		this.am = am;
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

}
