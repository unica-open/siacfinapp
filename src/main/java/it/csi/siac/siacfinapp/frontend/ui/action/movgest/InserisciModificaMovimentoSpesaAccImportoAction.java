/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaAccImportoAction extends BaseRorEntrata {
	
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
		//SIAC-6997
		inizializzaElaboraRorReannoInInserimentoNelModel();
		
		 
		
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
					if(sub.getNumeroBigDecimal() != null){
						//aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumeroBigDecimal()));
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
		
		//SIAC-7349 Inizio  SR180 FL 08/04/2020
	 	setListaModificheSpeseCollegata(
	 			//SIAC-8041 filtro la lista modifiche per le reimputazioni
	 			initListaModificheSpesaCollegataSoloReimp(
	 					model.getAccertamentoInAggiornamento()
	 						.getListaModificheMovimentoGestioneSpesaCollegata()
	 			)
	 	);
		//SIAC-7349 Fine SR180 FL 08/04/2020
		
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
		
		if(model.getAccertamentoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
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
		model.setProseguiConWarningModificaPositivaAccertamento(false);
		
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
					if(String.valueOf(sub.getNumeroBigDecimal()).equals(getSubSelected())){
						
						setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						setNumeroSubImpegno(String.valueOf(sub.getNumeroBigDecimal()));
						model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumeroBigDecimal()));
						
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
						

					
						if(sub.getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
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
		
		//eliminazione degli errori precedenti se risalvo
		model.setErrori(new ArrayList<Errore>());
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		List<Errore> listaWarning = new ArrayList<Errore>();
		
		boolean erroreProvvedimento = false;	
		//SIAC-7349 Inizio SR180 CM 08/04/2020 aggiunto controllo su importo collegamento
		boolean erroreListaCollegata = false;
		//SIAC-7349 Fine SR180 CM 08/04/2020 aggiunto controllo su importo collegamento
		
		//SIAC-8506
		controllaLunghezzaMassimaDescrizioneMovimento(getDescrizione(), listaErrori);
		
		String tipoMotivo = getIdTipoMotivo();
		//SIAC-8818				
		// se ROR-REIMP o ROR-REANNO non posso mettere flag reimputazione = No 
		// se flag reimp a si non posso mettere tipi diversi da REIMP o REANNO 
		// se flag reimp a si o ROR-REIMP - ROR-REANNO l'importo deve essere minore di 0 (controllo gia presente)
		String reimputazione = model.getMovimentoSpesaModel().getReimputazione();
		if (tipoMotivo.equalsIgnoreCase("REIMP") || tipoMotivo.equalsIgnoreCase("REANNO")) {
				if(WebAppConstants.No.equals(reimputazione)) {		
					controllaFlagReimp(listaErrori);
				}
		} 
		
		if (WebAppConstants.Si.equals(reimputazione)) {
			if (!tipoMotivo.equalsIgnoreCase("REIMP") && !tipoMotivo.equalsIgnoreCase("REANNO")) {
				controllaIncompatibilitaFlagReimpMotivo(listaErrori);
			}
		}
		
		
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
				//inizio SIAC-7349
				
				
				
				//List<ModificaMovimentoGestioneSpesaCollegata> modificheCollegateList = model.getMovimentoSpesaModel().getListaModificheSpeseCollegata();
				//fine SIAC-7349
				
				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				}
				
				ModificaMovimentoGestioneEntrata modificaEntrataInInserimento = new ModificaMovimentoGestioneEntrata();
				
				BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
				BigDecimal importoAttuale = model.getAccertamentoInAggiornamento().getImportoAttuale();
				
				BigDecimal importoCambiato = importoAttuale.add(importoDiInput);

				modificaEntrataInInserimento.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
				modificaEntrataInInserimento.setImportoOld(convertiImportoToBigDecimal(importoImpegnoModImp));
				modificaEntrataInInserimento.setImportoNew(importoCambiato);
				model.getMovimentoSpesaModel().getAccertamento().setImportoAttuale(importoCambiato);
				
				model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
				model.getStep1Model().setImportoImpegno(importoCambiato);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				modificaEntrataInInserimento = settaDatiProvvDalModel(modificaEntrataInInserimento, model.getStep1Model().getProvvedimento());
				
				modificaEntrataInInserimento.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				if(getDescrizione()!=null){
					modificaEntrataInInserimento.setDescrizione(getDescrizione().toUpperCase());
				}
				modificaEntrataInInserimento.setTipoMovimento("ACC");
				
				settaDatiReimputazioneDalModel(modificaEntrataInInserimento);
				
				//SIAC-7349 CONTABILIA-257 CM 30/06/2020 inizio
				controlloImportoLimiteInferiore(modificaEntrataInInserimento, listaErrori, listaWarning);
				
				if(!listaErrori.isEmpty()){
					addErrori(listaErrori);
					return INPUT;
				}
				//SIAC-7349 CONTABILIA-257 CM 30/06/2020 fine
				
				//SIAC-7349 Inizio SR180 CM 08/04/2020 aggiunto controllo su importo collegamento
				if (CollectionUtils.isNotEmpty(getListaModificheSpeseCollegata())) {

					erroreListaCollegata = checkListaCollegataImpegni( modificaEntrataInInserimento,listaErrori);
					if(!listaErrori.isEmpty() || erroreListaCollegata){
						//presenza errori
						addErrori(listaErrori);
						return INPUT;
					}
					//CALCOLO DEL RESIDUO
					for (ModificaMovimentoGestioneSpesaCollegata modificheSpeseCollegata : getListaModificheSpeseCollegata()) {
						if (modificaEntrataInInserimento.getTipoModificaMovimentoGestione().equals(modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
								&& modificaEntrataInInserimento.getAnnoReimputazione() != null && modificaEntrataInInserimento.getAnnoReimputazione().equals(modificheSpeseCollegata.getModificaMovimentoGestioneSpesa().getAnnoReimputazione())
								) {
									BigDecimal residuoNew = modificheSpeseCollegata.getImportoResiduoCollegare().subtract(modificheSpeseCollegata.getImportoCollegamento());
									modificheSpeseCollegata.setImportoResiduoCollegare(residuoNew);
						}
					}
					modificaEntrataInInserimento.setListaModificheMovimentoGestioneSpesaCollegata(getListaModificheSpeseCollegata());
				}
				//SIAC-7349 Fine SR180 CM 08/04/2020 
				
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				for (Iterator<ModificaMovimentoGestioneEntrata> iterator = modificheList.iterator(); iterator.hasNext();) {
					ModificaMovimentoGestioneEntrata mm = iterator.next();
					if(mm.getUid() == 0) {
						iterator.remove();
					}
					
				}
				modificheList.add(modificaEntrataInInserimento);
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
				AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(requestAggiorna);
				
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
	
			//SIAC-8279 controllo campo blank
		}  else if(tipoImpegno.equals("SubAccertamento") && StringUtils.isBlank(abbina)){
			
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
					if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
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
					if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
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
				AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(requestAggiorna);
				
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
				if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
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
			
			//SIAC-8051
			settaDatiReimputazioneDalModel(spesaSub);
			
			
			//Inserisco nell impegno che andra nel servizio aggiorna impegno
			modificheSubList.add(spesaSub);
			
			List<SubAccertamento> nuovaSubImpegnoList = new ArrayList<SubAccertamento>();
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
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
			AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(requestAggiorna );
			
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

	//SIAC-7349 Inizio SR180 CM 08/04/2020 aggiunto controllo su importo collegamento
	

	protected boolean checkListaCollegataImpegni(ModificaMovimentoGestioneEntrata spesa, List<Errore> listaErrori) {
			
		if(spesa.isReimputazione() &&  model.getMovimentoSpesaModel().getAnnoReimputazione() != null 
				&& model.getMovimentoSpesaModel().getAnnoReimputazione() > 0){
			//SIAC-8140 si usa 
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheReimputazione = getListaModificheSpeseCollegata();
			
			if(CollectionUtils.isNotEmpty(listaModificheReimputazione)){
				//inizializziamo a 0 la variabile che dovra' contenere la somma degli importi collegabili inseriti e totali della tabella
				
				BigDecimal sommaImportiCollegabiliPrioritariePerAnnoSelezionato = new BigDecimal(0);
				BigDecimal sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato = new BigDecimal(0);
				
				BigDecimal sommaImportiMaxCollegabilePerAnnoSelezionato = new BigDecimal(0);
				BigDecimal sommaImportoCollegamentoPerAnnoSelezionato = new BigDecimal(0);
				
				
				BigDecimal sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato = new BigDecimal(0);
				
				
				int countNumeroPrioritariePerAnnoSelezionato=0;
				int countNumeroPrioritarieAll=0;
				
				int numeroReimputazionePerAnnoSelezionato=0;
				
				int numeroReimputazionePerALLAnnualita=0;
				int numeroReimputazionePerDiffAnnualita=0;
			 
				
				int countErrorCollegabile=0;
				int countErrorDifferimneto=0;
				
				
				
				BigDecimal sommaImportiMaxCollegabilePerAnniNonSelezionato = new BigDecimal(0);
				BigDecimal sommaImportoCollegamentoPerAnniNonSelezionato = new BigDecimal(0);
				
				//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Inizio CM 08/07/2020
				boolean isImportoCollegamentoNegativo = false;
				//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Fine CM 08/07/2020
				
				//Primo controllo: L'importo inserito nel campo aperto "Importo Collegato" sia minore dell'importo "Massimo Collegabile"
				for(int i=0; i<listaModificheReimputazione.size(); i++) {
					
					BigDecimal importoMaxCollegabile = listaModificheReimputazione.get(i).getImportoMaxCollegabile();
					
					//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Inizio CM 08/07/2020
					if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(BigDecimal.ZERO) < 0) {
						isImportoCollegamentoNegativo = true;
					}
					//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Fine CM 08/07/2020
					
					if (spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
							&& listaModificheReimputazione.get(i).getImportoMaxCollegabile().compareTo(BigDecimal.ZERO) > 0
							) {
					
						if (!spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())){
							numeroReimputazionePerDiffAnnualita++;
						}
						
						numeroReimputazionePerALLAnnualita++;
						
						if(!spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())) {	
							sommaImportoCollegamentoPerAnniNonSelezionato = sommaImportoCollegamentoPerAnniNonSelezionato.add(listaModificheReimputazione.get(i).getImportoCollegamento());
							sommaImportiMaxCollegabilePerAnniNonSelezionato = sommaImportiMaxCollegabilePerAnniNonSelezionato.add(importoMaxCollegabile);
						}
						
						if (listaModificheReimputazione.get(i).isVincoloEsplicito()   ) {
							countNumeroPrioritarieAll++;	
						}
					
						if(spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())) {		
							
							if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(listaModificheReimputazione.get(i).getImportoMaxCollegabile()) > 0 ) {
							   	countErrorCollegabile++;
							}
							if(listaModificheReimputazione.get(i).getImportoCollegamento().compareTo(spesa.getImportoOld().abs()) > 0 ) {
								countErrorDifferimneto++;
							}
							
							numeroReimputazionePerAnnoSelezionato++;
							sommaImportiMaxCollegabilePerAnnoSelezionato =sommaImportiMaxCollegabilePerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoMaxCollegabile());
							sommaImportoCollegamentoPerAnnoSelezionato =sommaImportoCollegamentoPerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoCollegamento());
							
							if (listaModificheReimputazione.get(i).isVincoloEsplicito()) {
								countNumeroPrioritariePerAnnoSelezionato++;
								sommaImportiCollegabiliPrioritariePerAnnoSelezionato = sommaImportiCollegabiliPrioritariePerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoCollegamento());
								sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato = sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato.add(listaModificheReimputazione.get(i).getImportoMaxCollegabile());
							}
							
						} 
					} 
					
					
					if (!spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
							|| !spesa.getAnnoReimputazione().equals(listaModificheReimputazione.get(i).getModificaMovimentoGestioneSpesa().getAnnoReimputazione())
							) {
					
							listaModificheReimputazione.get(i).setImportoCollegamento(BigDecimal.ZERO);
					}
					
					
				}
				
				if(numeroReimputazionePerALLAnnualita == numeroReimputazionePerDiffAnnualita	
						&& sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0	
						) {
					listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa: Esaurire prima tutte le righe di differimento di spesa"));
				}else {
				
					sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato = sommaImportoCollegamentoPerAnnoSelezionato.subtract(sommaImportiCollegabiliPrioritariePerAnnoSelezionato);
					
					//controllo di esaurimento importo differimento spalmati anche su tutti gli anni 07/05/2020
					if ((spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato) > 0
							&& spesa.getImportoOld().abs().compareTo(sommaImportoCollegamentoPerAnnoSelezionato)>0
							//&& numeroReimputazionePerALLAnnualita > numeroReimputazionePerAnnoSelezionato 
							&& sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0 ) 
						|| (spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)>=0 
							&& sommaImportoCollegamentoPerAnnoSelezionato.compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)<0)) {
						
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa."));
					}
					
					
					//CM 01/07/2020 - commentato perch. unito al controllo sopra per non far duplicare il messaggio di errore a seguito delle correzioni ai messaggi di errore date da Pietro Gambino
//					if (spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)>=0 && 
//							sommaImportoCollegamentoPerAnnoSelezionato.compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato)<0 ) {
//						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Impossibile procedere: Si sta tentando di differire una quota di accertamento che non fornisce copertura ad alcuna reimputazione di spesa."));
//					}
					
					//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Inizio CM 08/07/2020
					if(isImportoCollegamentoNegativo) {
					   	listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("'Importo collegamento' deve essere positivo"));
					}
					//CONTABILIA-260 - Bug fix per test 20.17 e 20.30 - SIAC-7349 Fine CM 08/07/2020
					
					if(countErrorCollegabile >0) {
					   	listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("'Importo collegamento' deve essere minore o uguale dell'importo 'Importo max. collegabile' per l'anno "+ spesa.getAnnoReimputazione()));
					}
					
					if(countErrorDifferimneto > 0 && numeroReimputazionePerAnnoSelezionato == 1) {
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("La somma dei collegamenti impostati supera l'importo della modifica di entrata per l'anno "+ spesa.getAnnoReimputazione()));
					}
						
					if ((sommaImportoCollegamentoPerAnnoSelezionato.compareTo(BigDecimal.ZERO) == 0 && sommaImportiMaxCollegabilePerAnniNonSelezionato.subtract(sommaImportoCollegamentoPerAnniNonSelezionato).compareTo(BigDecimal.ZERO)	 !=0)
							|| (sommaImportoCollegamentoPerAnnoSelezionato.compareTo(BigDecimal.ZERO) == 0 && spesa.getImportoOld().abs().compareTo(BigDecimal.ZERO)!=0 && sommaImportiMaxCollegabilePerAnnoSelezionato.compareTo(BigDecimal.ZERO)!=0 )
							) {
						
						listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("Si sta tentando di differire una quota di accertamento senza fornire copertura ad alcuna reimputazione di spesa: Valorizzare 'Importo collegamento' per un anno di Reimputazioni di Spesa "));
					}
					
					if ( sommaImportoCollegamentoPerAnnoSelezionato.compareTo(spesa.getImportoOld().abs()) > 0) {
						if (numeroReimputazionePerAnnoSelezionato > 1 )
							listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("La somma dei collegamenti impostati supera l'importo della modifica di entrata per l'anno " + spesa.getAnnoReimputazione()));
					}
	
					//Controllo sulle righe prioritarie
					int countmsgErrorPri=0;
					int countmsgErrorNonPri=0;
					int countmsgErrorNumeroPrioritarieAll=0;
					int countmsgErrorPrioritariePerAnnoSelezionato=0;
					for(int z=0; z<listaModificheReimputazione.size(); z++) {
						
						if (spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
								&& listaModificheReimputazione.get(z).getImportoMaxCollegabile().compareTo(BigDecimal.ZERO) > 0
								) {

						
								if (listaModificheReimputazione.get(z).getImportoCollegamento() != null && listaModificheReimputazione.get(z).getImportoCollegamento().compareTo(BigDecimal.ZERO) > 0  
										&& spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())
										){
									//tratto righe non vincolate
									if ( !listaModificheReimputazione.get(z).isVincoloEsplicito()   ) {
										if ( countNumeroPrioritarieAll ==0 ) {
											if( spesa.getImportoOld().abs().compareTo(sommaImportiMaxCollegabilePerAnnoSelezionato) <= 0 &&
													spesa.getImportoOld().abs().compareTo(sommaImportoCollegamentoPerAnnoSelezionato) >0 ) {
											
											countmsgErrorNonPri++;
											}
										}
										
										if ( countNumeroPrioritarieAll > 0 ) {
											if( spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) <= 0 &&
												spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritariePerAnnoSelezionato) >0 ) {
											countmsgErrorPri++;
											}
											countmsgErrorNumeroPrioritarieAll++;
										}
									}
									
									//tratto righe prioritarie
									if ( listaModificheReimputazione.get(z).isVincoloEsplicito() && countNumeroPrioritarieAll >1  && spesa.getTipoModificaMovimentoGestione().equals(listaModificheReimputazione.get(z).getModificaMovimentoGestioneSpesa().getTipoModificaMovimentoGestione())) {
										if (   countNumeroPrioritariePerAnnoSelezionato < numeroReimputazionePerAnnoSelezionato && 
												sommaImportoCollegamentoPerAnnoNONPrioritaireSelezionato.compareTo(BigDecimal.ZERO) != 0  ) {
											countmsgErrorPrioritariePerAnnoSelezionato++;
										}
										if ( countNumeroPrioritariePerAnnoSelezionato == numeroReimputazionePerAnnoSelezionato) {
											if( spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) <= 0 &&
													spesa.getImportoOld().abs().compareTo(sommaImportiCollegabiliPrioritariePerAnnoSelezionato) >0 ) {
												countmsgErrorPri++;
											}
										}
									}
								}
							}
						}
						//Messsaggi in presenza di righe prioritarie
						if (countmsgErrorNumeroPrioritarieAll > 0 ||  countmsgErrorPrioritariePerAnnoSelezionato >0){
							if(sommaImportiCollegabiliPrioritariePerAnnoSelezionato.compareTo(sommaImportiCollegabiliPrioritarieMaxCollegabilePerAnnoSelezionato) != 0) {
								if (countNumeroPrioritarieAll==1  || countNumeroPrioritariePerAnnoSelezionato==1){
									 listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' stato valorizzato l'importo collegamento di una modifica di impegno non esplicitamente in vincolo senza prima fornire coperture alle modifiche di impegno in vincolo esplicito con il corrente accertamento. Esaurire prima l'importo massimo collegabile della riga 'prioritaria' (Evidenziata)"));
								}else {
									listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' stato valorizzato l'importo collegamento di una modifica di impegno non esplicitamente in vincolo senza prima fornire coperture alle modifiche di impegno in vincolo esplicito con il corrente accertamento. Esaurire prima l'importo massimo collegabile delle righe 'prioritarie' (Evidenziate)"));	
								}
							}
						}
						if(countmsgErrorPri>0) {
							listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' necessario esaurire l'importo Massimo Collegabile di tutte le righe 'prioritarie' (Evidenziate)"));
						}
						if(countmsgErrorNonPri>0) {
							listaErrori.add(ErroreCore.IMPORTI_DA_VALORIZZARE.getErrore("E' necessario esaurire l'importo Massimo Collegabile di tutte le righe"));
						}
					
					//fine controlli	
				}
			}
		}
		if (listaErrori != null && ! listaErrori.isEmpty()) {
			return true;
		}else {
			return false;	
		}
	}
		//SIAC-7349 Fine SR180 CM 08/04/2020 

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
			if(getSubSelected().equals(String.valueOf(sub.getNumeroBigDecimal()))){
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
		
			if(getSubSelected().equals(String.valueOf(sub.getNumeroBigDecimal()))){
				setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				setNumeroSubImpegno(String.valueOf(sub.getNumeroBigDecimal()));
				model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
				model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumeroBigDecimal()));
				
				setUidSubSelected(sub.getUid());
				
				BigDecimal importoAttualeSubImpegno = sub.getImportoAttuale();
				minImportoSubApp = new BigDecimal(0).subtract(importoAttualeSubImpegno);
			
				if(sub.getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
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
		
//		boolean importoMinoreDelMinimoAmmissibile = importoDiInput.compareTo(minImportoImpegnoApp)<0;
		boolean importoZero = importoDiInput.compareTo(BigDecimal.ZERO)==0;
		
		if(isRORDaMantenere && !importoZero){
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ", "deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}

		//SIAC-7349 CONTABILIA-257 01/07/2020 CM commentata e aggiunta nel metodo controlloImportoLimiteInferiore per bug fix
//		if(!isRORDaMantenere && (importoMinoreDelMinimoAmmissibile|| importoZero)){
//				listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
//				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
//		}
		
		//SIAC-6586

		//Jira SIAC-3438
//		if(!model.isFlagSuperioreTreAnni() && importoDiInput.compareTo(maxImportoImpegnoApp) >0 && !model.isProseguiConWarningModificaPositivaAccertamento()){ 
//			Errore erroreMax = ErroreFin.RANGE_NON_VALIDO.getErrore("massimo");
//			listaWarning.add(erroreMax);
//			
//			addPersistentActionWarning(erroreMax.getCodice() + " - " + "Superamento importo massimo consentito");
//			
//			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
//			model.setProseguiConWarningModificaPositivaAccertamento(true);
//					
//		}
		//SIAC-8553
		//controlliImportoModifica(importoDiInput, listaWarning);
		//issue-11: su accertamento residuo non deve controllare il tetto massimo
		boolean residuo = model.getAccertamentoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio();
		//issue-12: non dare il warning se il capitolo ha disponibilità negativa e importo in input (la modifica che sto inserendo) è negativa
		boolean	disponibCapitoloNegativa = model.getAccertamentoInAggiornamento().getAnnoMovimento() == sessionHandler.getAnnoBilancio() &&
										   model.getAccertamentoInAggiornamento().getCapitoloEntrataGestione().getImportiCapitolo().getDisponibilitaAccertareAnno1().signum() == -1 && 
										   importoDiInput.signum() == -1?true:false;
				
		if (!residuo && !disponibCapitoloNegativa) {	
			controlliImportoModifica(importoDiInput, listaWarning);
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
	
	//SIAC-8553
	private void controlliImportoModifica(BigDecimal importoDiInput, List<Errore> listaWarning) {
		
		boolean throwWarning = controlloImportoModificaSuMassimoConsentito(importoDiInput, listaWarning);

		throwWarning = controlloImportoModificaSuDisponibiltaAccertareCapitolo(importoDiInput, listaWarning) || throwWarning;
		
		
		if(throwWarning) {
			// devo mostrare il warning
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			model.setProseguiConWarningModificaPositivaAccertamento(true);
		}
	}
	
	private boolean controlloImportoModificaSuMassimoConsentito(BigDecimal importoDiInput, List<Errore> listaWarning) {
		//Jira SIAC-3438
		if(!model.isFlagSuperioreTreAnni() && importoDiInput.compareTo(maxImportoImpegnoApp) > 0 && !model.isProseguiConWarningModificaPositivaAccertamento()){ 
			Errore erroreMax = new Errore(ErroreFin.RANGE_NON_VALIDO.getCodice(), "Superamento importo massimo consentito");
			listaWarning.add(erroreMax);
			addPersistentActionWarningFin(erroreMax);
			return true;
		}
		return false;
	}
	
	//SIAC-7349 CONTABILIA-257 01/07/2020 CM implementato per bug fix limite inferiore di importo modifica - INIZIO
	private void controlloImportoLimiteInferiore(ModificaMovimentoGestioneEntrata spesa, List<Errore> listaErrori, List<Errore> listaWarning) {

		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
		boolean isRORDaMantenere = CODICE_MOTIVO_ROR_MANTENERE.equals(StringUtils.defaultIfBlank(getIdTipoMotivo(), ""));
		
		boolean importoMinoreDelMinimoAmmissibile = importoDiInput.compareTo(minImportoImpegnoApp)<0;
		boolean importoZero = importoDiInput.compareTo(BigDecimal.ZERO)==0;

		if(!isRORDaMantenere && (importoMinoreDelMinimoAmmissibile || importoZero) && verificaLimiteInferiore(spesa, listaErrori)){
			listaErrori.add(ErroreFin.RANGE_NON_VALIDO.getErrore("minimo"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
	}
	
	private boolean verificaLimiteInferiore(ModificaMovimentoGestioneEntrata spesa, List<Errore> listaErrori) {
		final String methodName="verificaLimiteInferiore";
		BigDecimal valoreModifica = spesa.getImportoOld();
		Boolean isSpesaReimputazione = WebAppConstants.Si.equals(model.getMovimentoSpesaModel().getReimputazione()) ? true : false;
		List<ModificaMovimentoGestioneSpesaCollegata> listaSpeseCollegate = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
		boolean esitoVerifica = false;
		BigDecimal sommatoriaVincoliPerImpegno = BigDecimal.ZERO;
		BigDecimal nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno = BigDecimal.ZERO;
//		BigDecimal dispUtilizzabileIniziale = BigDecimal.ZERO;
		
			if (valoreModifica.compareTo(BigDecimal.ZERO) < 0) {
				// eseguo il controllo sul disp a incassare solo se ricevo una modifica negativa:
				BigDecimal dispUtilizzabileAccOld = model.getAccertamentoInAggiornamento().getImportoUtilizzabile();
				
				BigDecimal importoUtilizzabileNew = dispUtilizzabileAccOld.subtract(valoreModifica);
				
				ConsultaVincoliAccertamento reqConsVincoli = new ConsultaVincoliAccertamento();
				reqConsVincoli.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
				reqConsVincoli.setAnnoMovimento(Integer.valueOf(model.getAccertamentoInAggiornamento().getAnnoMovimento()));
				reqConsVincoli.setEnte(sessionHandler.getEnte());
				reqConsVincoli.setRichiedente(sessionHandler.getRichiedente());
				reqConsVincoli.setNumeroMovimento(model.getAccertamentoInAggiornamento().getNumeroBigDecimal());
				ConsultaVincoliAccertamentoResponse respVincoli = movimentoGestioneFinService.consultaVincoliAccertamento(reqConsVincoli);
				
				List<VincoloAccertamento> vincoli = respVincoli.getVincoli();
	
				if (vincoli != null && vincoli.size() > 0) {
	
					if(isSpesaReimputazione != null && isSpesaReimputazione.equals(true)){
						listaSpeseCollegate = getListaModificheSpeseCollegata();				
					}
					StringBuilder logString = new StringBuilder();
					log.info(methodName, "importo utilizzabile old: [" + dispUtilizzabileAccOld + "] - valore modifica [" + valoreModifica + "] = importoUtilizzabileNew [" + importoUtilizzabileNew + "]");
					for (VincoloAccertamento it : vincoli) {		
						if(it.getImpegno() != null) {
							sommatoriaVincoliPerImpegno = calcolaSommatoriaImportiCollegamentoPerImpegno(it.getImpegno(), listaSpeseCollegate);	
							
							nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno = it.getImporto().add(sommatoriaVincoliPerImpegno);
							//dispUtilizzabileIniziale = nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno.subtract(dispUtilizzabileAccOld);
							boolean importoUtilizzabileNewMaggioreDiNuovaDispAdUtilizzare =  importoUtilizzabileNew.compareTo(nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno) < 0;
							boolean sommatoriaVincoliPerImpegnoMaggioreDiZero = sommatoriaVincoliPerImpegno.compareTo(BigDecimal.ZERO) > 0;
							logString.append(" SommatoriaImportiCollegamentoPerImpegno [ ").append(sommatoriaVincoliPerImpegno).append(" ] >0?  ").append((sommatoriaVincoliPerImpegno.compareTo(BigDecimal.ZERO) > 0)
									).append("] = importoVincolo [").append(it.getImporto()).append("]").append(" sommatoriaImportiCollegamentoPerImpegno [ ").append(sommatoriaVincoliPerImpegno).append(" ]  = "
											).append(". nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno[ ").append(nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno).append( " ] ."
													).append("importoUtilizzabileNew [").append(importoUtilizzabileNew).append("] < nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno[ ").append(nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno).append( " ]? "
													).append(importoUtilizzabileNewMaggioreDiNuovaDispAdUtilizzare).append(". La sommatoria dei vincoli per impeno e' maggiore di zero? ").append(sommatoriaVincoliPerImpegnoMaggioreDiZero);
							
							log.info(methodName, logString);
							if ((sommatoriaVincoliPerImpegno.compareTo(BigDecimal.ZERO) > 0)  && (importoUtilizzabileNew.compareTo(nuovaDisponibilitaUtilizzabilePerVincoloPerImpegno) < 0)) {
//							if (dispUtilizzabileIniziale.compareTo(BigDecimal.ZERO) < 0) {
								esitoVerifica = true;
								log.info(methodName, "Controllo FE non superato");
							}
						}
					}
				}
			}
		return esitoVerifica;
	}
	
	
	private BigDecimal calcolaSommatoriaImportiCollegamentoPerImpegno(Impegno i, List<ModificaMovimentoGestioneSpesaCollegata> listaSpeseCollegate) {
		
		BigDecimal sommatoria = BigDecimal.ZERO;
		
		if(listaSpeseCollegate != null && !listaSpeseCollegate.isEmpty()){
			for(ModificaMovimentoGestioneSpesaCollegata mmge : listaSpeseCollegate){
				if(mmge.isVincoloEsplicito() 
					&& mmge.getModificaMovimentoGestioneSpesa() != null 
					&& mmge.getModificaMovimentoGestioneSpesa().getImpegno()!= null 
					&& !mmge.getModificaMovimentoGestioneSpesa().isElaboraRorReanno() 
					&& i.getUid() == mmge.getModificaMovimentoGestioneSpesa().getImpegno().getUid() ){
					
						sommatoria = sommatoria.add(mmge.getImportoCollegamento());											
				}
			}
		}	
		return sommatoria;
	}
	
	//SIAC-7349 CONTABILIA-257 01/07/2020 CM implementato per bug fix limite inferiore di importo modifica - FINE
	
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
