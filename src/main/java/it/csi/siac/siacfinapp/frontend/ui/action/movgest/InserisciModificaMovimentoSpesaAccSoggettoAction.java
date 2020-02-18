/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaAccSoggettoAction extends ActionKeyAggiornaAccertamento {

	private static final long serialVersionUID = 1L;
	
	private Impegno impegnoDaModificare = new Impegno();
	
	private String tipoImpegno;
	private String descrizione;
	private List<String> tipoImpegnoModificaSoggettoList = new ArrayList<String>();
	private List<String> numeroSubImpegnoList = new ArrayList<String>();
	private boolean subImpegnoListExist = false;
	private Boolean subImpegnoSelected = false;
	private String subSelected;
	private String abbina;
	private List<String> abbinaList = new ArrayList<String>();
		
	private StatoOperativoAnagrafica statoSoggettoSelezionato;
	
	//Parametri SubImpegno
	private String numeroSubImpegno;
	
	//SOGGETTI
	private String idSub;							
	private Soggetto soggettoImpegnoEffettivo;			
	private Soggetto soggettoSubEffettivo;
	private ClasseSoggetto classeSoggettoAttuale;
	private ClasseSoggetto classeSoggettoEffettivo;		
	private ClasseSoggetto classeSoggettoSubAttuale;
	private ClasseSoggetto classeSoggettoSubEffettivo;		
	private String idTipoMotivo;
	private Boolean tipoMovimentoImpegno;
	
	private String soggettoDesiderato;
	
	private String aperturaIniziale;
	
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		//per questo scenario non e' previsto inserimento di provvedimenti
		return false;
	}
	
	public InserisciModificaMovimentoSpesaAccSoggettoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
	/**
	 * Metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Movimento Spesa Soggetto");
		
		//Carico Lista Tipo Modifiche
		caricaListaMotivi();
		
		//Preparo Attibruti per la pagina
		getAbbinaList().add("Modifica anche Accertamento");
			
		//preparo i dati per i tendini di scelta se si intende modificare un accertamento o un sub
		if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0){
			//itero sui sub impegni e mi salvo in numeroSubImpegnoList
			//solo quelli con stato DEFINTIVO:
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(sub.getStatoOperativoMovimentoGestioneEntrata().equals("D")){
					if(sub.getNumero() != null){
						numeroSubImpegnoList.add(String.valueOf(sub.getNumero()));
					}		
				}
			}
			
			if(numeroSubImpegnoList.size() > 0){
				//presenza di sub in numeroSubImpegnoList, quindi si potra' scegliere
				//tra accertamento e subaccertamento:
				tipoImpegnoModificaSoggettoList.add("Accertamento");
				tipoImpegnoModificaSoggettoList.add("SubAccertamento");
				tipoImpegno = "SubAccertamento";			
			} else {
				//non ci sono sub in numeroSubImpegnoList, si puo' scegliere solo accertamento:
				tipoImpegnoModificaSoggettoList.add("Accertamento");
				tipoImpegno = "Accertamento";
			}

		} else {
			//non esistono proprio sub, l'unica scelta non puo'
			//che essere accertamento:
			tipoImpegno = "Accertamento";
			tipoImpegnoModificaSoggettoList.add("Accertamento");
		}		
			
		//Soggetto:
		if(model.getAccertamentoInAggiornamento().getSoggetto()!=null  && model.getSoggettoImpegnoAttuale()==null){
			Soggetto soggCloned = clone(model.getAccertamentoInAggiornamento().getSoggetto());
			model.setSoggettoImpegnoAttuale(soggCloned);
		}
		
		//classe soggetto:
		if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null){
			setClasseSoggettoAttuale(model.getAccertamentoInAggiornamento().getClasseSoggetto());
			setClasseSoggettoEffettivo(model.getAccertamentoInAggiornamento().getClasseSoggetto());
		}
		
		//codice soggetto:
		if(model.getStep1Model().getSoggettoImpegno()!=null && model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()!=null){	
			// jira 958
			if(model.getAccertamentoInAggiornamento().getSoggetto()!=null && (!model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto().equalsIgnoreCase(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto()))){
				//set Soggetto Impegno Effettivo( model . getStep1Model() . getSoggettoImpegno())
			} else {
				setSoggettoImpegnoEffettivo(model.getAccertamentoInAggiornamento().getSoggetto());
			}	
		}
		
		//tipo movimento impegno a false, in quanto accertamento:
		tipoMovimentoImpegno = false;
	}
	
	/**
	 * Metodo execute della action
	 */
	@Override
	public String execute() throws Exception {
		
		if(aperturaIniziale!=null && aperturaIniziale.equals("true")){
			pulisciSoggettoSelezionato();
			aperturaIniziale = null;
		}
		
		//tipoImpegno "Accertamento"
		caricaLabelsAggiorna(4);
		
		//mi salvo temporaneamente il provvedimento per reimpostarlo nell'impegno e associare il nuovo provv alle sole modifiche
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			
			model.getStep1Model().setProvvedimentoSupport(clone(model.getStep1Model().getProvvedimento()));
		}
		return SUCCESS;
	}
	
	/**
	 * Individua il sub selezionato e setta i dati opportuni nelle variabili 
	 * della action
	 * @return
	 */
	public String caricaDatiSub(){
		setMethodName("caricaDatiSub");
		
		if(StringUtils.isEmpty(getTipoImpegno())){
			//se tipo e' vuoto non e' stato selezionato nulla
			setTipoImpegno("Accertamento");
			setSubImpegnoSelected(false);
		} else if(getTipoImpegno().equalsIgnoreCase("SubAccertamento")){ 
			
			if(!StringUtils.isEmpty(getSubSelected())){
				//sub selezionato
				setSubImpegnoSelected(true);
				
				//itero tra i sub cercando quello selezionato:
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equals(getSubSelected())){
						//trovato
						
						//Setto i dati del sug trovato:
						setNumeroSubImpegno(String.valueOf(sub.getNumero()));
						
						if(sub.getSoggetto()!=null){
							setSoggettoSubEffettivo(sub.getSoggetto());
						}
							
						if(sub.getClasseSoggetto()!=null){
							setClasseSoggettoSubEffettivo(sub.getClasseSoggetto());
						}
					}
				}
				
			} else {
				//sub non selezionato
				setSubImpegnoSelected(false);
				tipoImpegno = "Accertamento";
			}
			
		}
		
		return SUCCESS;
	}
	
	/**
	 * Salvataggio della modifica
	 * @return
	 * @throws Exception
	 */
	public String salva() throws Exception {
		
		//info per debug:
		setMethodName("salva");
		
		//soggetto desiderato:
		if(model.getSoggettoImpegnoAttuale()!=null){
			setSoggettoDesiderato(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
		}
		
		//istanzio la lista di eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreProvvedimento = false;
		
		if(StringUtils.isEmpty(tipoImpegno)){
			
			//non e' stato scelto cosa si vuole modificare tra accertamento e sub accertamento
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo accertamento"));
			addErrori(listaErrori);
			return INPUT;
			
		}else if(tipoImpegno.equalsIgnoreCase("Accertamento")){
		
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
			
			//Controllo gestisci decentrato:
			listaErrori = controlloGestisciAccertamentoDecentratoPerModifica(listaErrori);
			
			//Motivo
			if(StringUtils.isEmpty(getIdTipoMotivo())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
			}
			
			// in caso di soggetto e classe non selezionato rimandare un errore!
			if(StringUtils.isEmpty(getSoggettoDesiderato()) && StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto / Classe"));
			}
			
			if (listaErrori!=null && listaErrori.size()>0) {
				//presenza errori
				addErrori(listaErrori);
				return INPUT;
			}
			
			//controllo stati subaccertamenti
			if(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti()!=null){
				List<SubAccertamento> listaSubAccertamenti = model.getAccertamentoInAggiornamento().getElencoSubAccertamenti();
				for(SubAccertamento subAccertamento : listaSubAccertamenti){
					String stato = subAccertamento.getStatoOperativoMovimentoGestioneEntrata();
					if(stato.equalsIgnoreCase("D")){
						listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("entita: subaccertamento " + subAccertamento.getNumero(), "DEFINITIVO"));
					}
				}
			}
			
			//controllo esistenza soggetto associato a impegno
			if((model.getStep1Model().getSoggettoImpegno()==null || StringUtils.isEmpty(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()))
					&& (model.getAccertamentoInAggiornamento().getClasseSoggetto()==null || StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice()))){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto non presente"));
			}
			
			//controllo validita' soggetto
			if (model.getSoggettoImpegnoAttuale().getCodiceSoggetto() != null && !"".equals(model.getSoggettoImpegnoAttuale().getCodiceSoggetto())) {
				SoggettoImpegnoModel soggettoDaVerificare = new SoggettoImpegnoModel();
				soggettoDaVerificare.setCodCreditore(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
				//verifica validita'
				if(!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(soggettoDaVerificare), false, oggettoDaPopolare)){
					return INPUT;
				} else {
					SoggettoImpegnoModel soggettoCaricatoDaServizio = model.getStep1Model().getSoggetto();
					model.getSoggettoImpegnoAttuale().setCodiceFiscale(soggettoCaricatoDaServizio.getCodfisc());
					model.getSoggettoImpegnoAttuale().setDenominazione(soggettoCaricatoDaServizio.getDenominazione());
				}
			}
			
			//soggetto desiderato
			if(getSoggettoDesiderato()!=null && 
			   model.getStep1Model().getSoggettoImpegno()!=null && 
			   model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()!=null &&
			   StringUtils.isNotEmpty(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto())){
				
				if(getSoggettoDesiderato().equals(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto())){
					listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il soggetto da variare deve essere diverso da quello esistente"));					
				}
				
			}
			
			//controllo che la classe da variare sia diversa da quella associata effettivamente
			if(model.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse()) ){
		    	for(CodificaFin cod:model.getListaClasseSoggetto()){
		    		if(String.valueOf(cod.getUid()).equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse())){
		    			String idClasse = cod.getCodice();
		    			if(getClasseSoggettoAttuale()!=null){
			    			if(idClasse.equalsIgnoreCase(getClasseSoggettoAttuale().getCodice())){
			    				listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la classe da variare deve essere diversa da quella esistente"));
			    				model.getStep1Model().getSoggetto().setClasse(null);
			    			}
		    			}	
		    		}
		    	}
		    }
			
			//controlli rispetto al provvedimento:
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "accertamento", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento){
				//non ci sono errori
				
				Soggetto soggettoNew = new Soggetto();
				ClasseSoggetto classeSoggettoNew = new ClasseSoggetto();
				
				List<ModificaMovimentoGestioneEntrata> modificheAccList = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
					
				if(modificheAccList == null){
					modificheAccList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				}
					
				ModificaMovimentoGestioneEntrata modEntrata = new ModificaMovimentoGestioneEntrata();
				
				//Soggetto OLD
				//FIX PER JIRA  SIAC-3463 - mancava controllo null pointer dentro questa if:
				if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getSoggetto()!=null &&
						model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto()!=null
					&& model.getAccertamentoInAggiornamento().getSoggettoCode()!=null){
					
					Soggetto soggTemp = new Soggetto();
					soggTemp.setCodiceSoggetto(model.getAccertamentoInAggiornamento().getSoggettoCode());
					modEntrata.setSoggettoOldMovimentoGestione(soggTemp);
				}
				else{	
					modEntrata.setSoggettoOldMovimentoGestione(model.getAccertamentoInAggiornamento().getSoggetto());						
				}
				
				//Soggetto NEW
				if(model.getSoggettoImpegnoAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoImpegnoAttuale().getCodiceSoggetto())){
					soggettoNew.setCodiceSoggetto(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
					modEntrata.setSoggettoNewMovimentoGestione(soggettoNew);
				
					model.getStep1Model().getSoggetto().setCodCreditore(soggettoNew.getCodiceSoggetto());
					model.getMovimentoSpesaModel().setSoggettoNewMovimentoGestione(soggettoNew);
				}	
									
				
				//Classe OLD
				if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null){
					modEntrata.setClasseSoggettoOldMovimentoGestione(model.getAccertamentoInAggiornamento().getClasseSoggetto());
				}	
				
				//Classe NEW
				if(model.getStep1Model().getSoggetto() != null && !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
					classeSoggettoNew.setCodice(model.getStep1Model().getSoggetto().getClasse());
					modEntrata.setClasseSoggettoNewMovimentoGestione(classeSoggettoNew);
					
					model.getMovimentoSpesaModel().setClasseSoggettoNewMovimentoGestione(classeSoggettoNew);
				}	
			
				//inserimento, settiamo a zero l'uid:
				modEntrata.setUid(0);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				modEntrata = settaDatiProvvDalModel(modEntrata, model.getStep1Model().getProvvedimento());
				
				modEntrata.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				modEntrata.setDescrizione(getDescrizione());
				modEntrata.setTipoMovimento(Constanti.MODIFICA_TIPO_ACC);
				
				//soggetto:
				if(soggettoNew!=null){
					model.getStep1Model().setSoggettoImpegno(soggettoNew);
				}
				
				//classe:
				if(classeSoggettoNew != null && !StringUtils.isEmpty(classeSoggettoNew.getCodice())){
					model.getStep1Model().getSoggetto().setClasse(classeSoggettoNew.getCodice());
				}
				
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				modificheAccList.add(modEntrata);
				
				model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(modificheAccList);
					
				//Non veniva popolto il componente con i dati della trans. elemen.
				popolaStrutturaTransazioneElementareAcc();
				
				//richiamo il servizio di aggiornamento:
				AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(convertiModelPerChiamataServiziInserisciAggiornaModifiche(true));
				model.setProseguiConWarning(false);
				
				//analizzo la risposta del servizio:
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					//errori dal servizio
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
						
				List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSoggetti = new ArrayList<ModificaMovimentoGestioneEntrata>();
				nuovaListaModificheSoggetti = response.getAccertamento().getListaModificheMovimentoGestioneEntrata();
				model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSoggetti);
						
				model.setAccertamentoInAggiornamento(response.getAccertamento());
				
				//Ottimizzazione richiamo ai servizi
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
				
				List<SubAccertamento> subDefinitivoList = new ArrayList<SubAccertamento>();
				if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size()> 0){
					for(SubAccertamento subSession : model.getListaSubaccertamenti()){
						int idSubSessione = subSession.getUid();
						for(SubAccertamento subResponse : response.getAccertamento().getElencoSubAccertamenti()){
							int idSubResponse = subResponse.getUid();
							if(idSubSessione == idSubResponse){
								List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSubSoggetti = new ArrayList<ModificaMovimentoGestioneEntrata>();
								nuovaListaModificheSubSoggetti = subResponse.getListaModificheMovimentoGestioneEntrata();
								subSession.setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSubSoggetti);
								subDefinitivoList.add(subSession);
							}
						}
					}
				}
					
				//reimposto il soggetto effettivo per l'impegno
				SoggettoImpegnoModel soggettoTmp = new SoggettoImpegnoModel();
				ClasseSoggetto classeSoggettoTmp = new ClasseSoggetto();
				
				if(model.getSoggettoImpegnoAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoImpegnoAttuale().getCodiceSoggetto())){
					soggettoTmp.setCodCreditore(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
				}						
				
				//se non e' stato associato il soggetto associo la classe in sessione
				if(response.getAccertamento().getClasseSoggetto()!=null && !StringUtils.isEmpty(response.getAccertamento().getClasseSoggetto().getCodice()) && StringUtils.isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
					classeSoggettoTmp.setCodice(response.getAccertamento().getClasseSoggetto().getCodice());
					
					//pulisco l'header del soggetto se stiamo salvando la classe
					model.getStep1Model().getSoggetto().setDenominazione("");
					model.getStep1Model().getSoggetto().setCodfisc("");
				}		
				
				model.getStep1Model().getSoggetto().setCodCreditore(soggettoTmp.getCodCreditore());					
				model.getStep1Model().getSoggetto().setClasse(classeSoggettoTmp.getCodice());		
				
				model.getStep1Model().setSoggettoSelezionato(true);
				model.setListaSubaccertamenti(subDefinitivoList);
				
				
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
				if(model.getStep1Model().getProvvedimentoSupport()!=null){
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
					impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
				}
				
				
				//FIX PER  SIAC-3444
				forceReload=true;
				//

				return GOTO_ELENCO_MODIFICHE;
				
			} else {
				//presenza errori
				addErrori(listaErrori);
				return INPUT;
			}
	
			
		}  else if(tipoImpegno.equals("SubAccertamento") ){

			//FACCIAMO PRIMA I WARNING:
			boolean presenzaWarningsSubimpegno = presenzaWarningsSubaccertamentoPerModificaSoggetto();
			if(presenzaWarningsSubimpegno){
				return INPUT;
			}
			//
			
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
			
			
			//Motivo
			if(StringUtils.isEmpty(getIdTipoMotivo())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modifica motivo"));
			}
			
			////////////////CONTROLLI SOGGETTO - SUBIMPEGNO/////////////////
			
			//controllo esistenza soggetto associato a subaccertamento
			for(SubAccertamento sub : model.getListaSubaccertamenti()){
				if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
					if(sub.getSoggetto().getCodiceSoggetto()==null||sub.getClasseSoggetto()!=null){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto per il sub accertamento non presente"));
					}					
				}					
			}			
			
			//controllo validita' soggetto
			if (model.getSoggettoSubAttuale().getCodiceSoggetto() != null && !"".equals(model.getSoggettoSubAttuale().getCodiceSoggetto())) {
				SoggettoImpegnoModel soggettoDaVerificare = new SoggettoImpegnoModel();
				soggettoDaVerificare.setCodCreditore(model.getSoggettoSubAttuale().getCodiceSoggetto());
				//verifica validita'
				if(!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(soggettoDaVerificare), false, oggettoDaPopolare)){
					return INPUT;
				}				
			}
			
			//controlli rispetto al provvedimento:
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "subAccertamento", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento){
				//non ci sono errori
				
				List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
					
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
						modificheList = sub.getListaModificheMovimentoGestioneEntrata();
					}
				}				

				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();
				}
				
				ModificaMovimentoGestioneEntrata modEntrata = new ModificaMovimentoGestioneEntrata();
												
				//Soggetto OLD
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
						modEntrata.setSoggettoOldMovimentoGestione(sub.getSoggetto());
					}
				}		
								
				//Soggetto NEW
				if(model.getSoggettoSubAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoSubAttuale().getCodiceSoggetto())){
					Soggetto soggettoSubNew = new Soggetto();
					soggettoSubNew.setCodiceSoggetto(model.getSoggettoSubAttuale().getCodiceSoggetto()); 
					modEntrata.setSoggettoNewMovimentoGestione(soggettoSubNew);
				}	
				
				
				//Classe OLD
				if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null){
					modEntrata.setClasseSoggettoOldMovimentoGestione(model.getAccertamentoInAggiornamento().getClasseSoggetto()); //da verificare
				}
				
				//Classe NEW
				if(model.getStep1Model().getSoggetto()!=null && !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
					ClasseSoggetto classeSoggettoSubNew = new ClasseSoggetto();
					classeSoggettoSubNew.setCodice(model.getStep1Model().getSoggetto().getClasse());//da verificare
					modEntrata.setClasseSoggettoNewMovimentoGestione(classeSoggettoSubNew);
				}	
					
				//inserimento, setto l'uid a zero:
				modEntrata.setUid(0);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				modEntrata = settaDatiProvvDalModel(modEntrata, model.getStep1Model().getProvvedimento());
				
				modEntrata.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				modEntrata.setDescrizione(getDescrizione());
				modEntrata.setTipoMovimento(Constanti.MODIFICA_TIPO_SAC);
				
				
				//Inserisco nell accertamento che andra nel servizio aggiorna accertmento
				modificheList.add(modEntrata);
				
				List<SubAccertamento> nuovaSubAccertamentoList = new ArrayList<SubAccertamento>();
				for(SubAccertamento sub : model.getListaSubaccertamenti()){
					if(String.valueOf(sub.getNumero()).equalsIgnoreCase(subSelected)){
						sub.setListaModificheMovimentoGestioneEntrata(modificheList);
					}
					nuovaSubAccertamentoList.add(sub);
				}
				
				model.setListaSubaccertamenti(nuovaSubAccertamentoList);
				
				//per evitare lo stato definitivo all'impegno e mantenere lo stato definitivo non liquidabile
				//reimposto il soggetto effettivo e la classe effettiva per l'impegno
				SoggettoImpegnoModel soggettoNew = new SoggettoImpegnoModel();
				
				if(soggettoImpegnoEffettivo!=null && !StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto())){
					soggettoNew.setCodCreditore(soggettoImpegnoEffettivo.getCodiceSoggetto());
				}
				model.getStep1Model().setSoggetto(soggettoNew);								
				
				ClasseSoggetto classeSoggettoNew = new ClasseSoggetto();
				
				if((classeSoggettoEffettivo!=null && !StringUtils.isEmpty(classeSoggettoEffettivo.getCodice()))
						&& (soggettoImpegnoEffettivo==null || StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto()))){
					classeSoggettoNew.setCodice(classeSoggettoEffettivo.getCodice());
				}		
				model.getStep1Model().getSoggetto().setClasse(classeSoggettoNew.getCodice());				
								
				//Gestisco l abbina anche accertamento
				if(!StringUtils.isEmpty(abbina)){
					if(abbina.equalsIgnoreCase("Modifica anche Accertamento")){
					}	
				}
				
				//Non veniva popolato il componente con i dati della trans. elemen.
				popolaStrutturaTransazioneElementareAcc();
		
				//richiamo il servizio di aggiornamento:
				AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(convertiModelPerChiamataServiziInserisciAggiornaModifiche(true));
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					//ci sono errori dal servizio
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
				
				
				//Ottimizzazione richiamo ai servizi
				model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
					
				List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSoggetti = new ArrayList<ModificaMovimentoGestioneEntrata>();
				nuovaListaModificheSoggetti = response.getAccertamento().getListaModificheMovimentoGestioneEntrata();
				model.getAccertamentoInAggiornamento().setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSoggetti);
				
				List<SubAccertamento> subDefinitivoList = new ArrayList<SubAccertamento>();
				if(model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size()> 0){
					for(SubAccertamento subSession : model.getListaSubaccertamenti()){
						int idSubSessione = subSession.getUid();

						for(SubAccertamento subResponse : response.getAccertamento().getElencoSubAccertamenti()){
							int idSubResponse = subResponse.getUid();
							if(idSubSessione == idSubResponse){
								List<ModificaMovimentoGestioneEntrata> nuovaListaModificheSubSoggetti = new ArrayList<ModificaMovimentoGestioneEntrata>();
								nuovaListaModificheSubSoggetti = subResponse.getListaModificheMovimentoGestioneEntrata();
								subSession.setListaModificheMovimentoGestioneEntrata(nuovaListaModificheSubSoggetti);
								subDefinitivoList.add(subSession);
							}
						}
					}
				}
				
				//da testare
				if(model.getStep1Model().getSoggetto()!=null && model.getStep1Model().getSoggettoImpegno()!=null){
					model.getStep1Model().getSoggetto().setCodCreditore(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto());
				}
				model.setListaSubaccertamenti(subDefinitivoList);	
			
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				//va fatto qui perche' in caso di esito negativo del servizio aggiorna rovinerei il valore del model in modifica
				if(model.getStep1Model().getProvvedimentoSupport()!=null){
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimentoSupport());
					impostaProvvNelModel(attoImpegno, model.getStep1Model().getProvvedimento());
				}
				
				
				//FIX PER  SIAC-3444
				forceReload=true;
				//
				
				return GOTO_ELENCO_MODIFICHE;
				
			} else {
				
				setSubImpegnoSelected(true);
				caricaDatiSub();
				
				setSubImpegnoSelected(true);addErrori(listaErrori);
				return INPUT;
			}			
		}
		
		return GOTO_ELENCO_MODIFICHE;
	}
	
	   /**
	    * Per inserisci modifica soggetto su sub 
	    * @return
	    */
	   protected boolean presenzaWarningsSubaccertamentoPerModificaSoggetto() {
		boolean presenzaWarningsSubaccertamento = false;
		
		Errore errorWarning = null;
		
		//controllo presenza soggetto in altri subAccertamenti - validi
		if(model.getListaSubaccertamenti()!=null){
			List<SubAccertamento> listaSubAccertamenti = model.getListaSubaccertamenti();
			for(SubAccertamento subAccertamento : listaSubAccertamenti){
				if(subAccertamento.getSoggetto()!=null){
					String stato = subAccertamento.getStato().name();
					if(stato.equalsIgnoreCase(Constanti.STATO_VALIDO) && model.getSoggettoSubAttuale().getCodiceSoggetto().equalsIgnoreCase(subAccertamento.getSoggetto().getCodiceSoggetto())){
						errorWarning = ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Creditore ", subAccertamento.getSoggetto().getCodiceSoggetto() + " " + subAccertamento.getSoggetto().getDenominazione());
					}
				}
			}
		}
		
	    if (errorWarning!=null) {
	    	
	    	if(!model.isProseguiConWarningControlloSoggetti()){
	    		
	    		//presento il messaggio di warning
				addPersistentActionWarning(errorWarning.getDescrizione());
				
				//setto prosegui con warning a true, cosi quando l'utente
				//clicchera' di nuovo verra bypassato questo warning
				presenzaWarningsSubaccertamento = Boolean.TRUE;
				model.setProseguiConWarningControlloSoggetti(true);
			}
	    }
		return presenzaWarningsSubaccertamento;
	}
	
	/**
	 * Metodo di selezione del soggetto
	 */
	public String selezionaSoggetto() {	
		//info per debug:
		setMethodName("selezionaSoggetto");
		
		//istanzio la lista degli eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		if(StringUtils.isEmpty(getRadioCodiceSoggetto())){
			//nessun soggetto selezionato
			if (model.getListaRicercaSoggetto() != null && model.getListaRicercaSoggetto().size() > 0) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
				addErrori(listaErrori);
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
			}
			return INPUT;
		} else {
			//soggetto selezionato
			for(Soggetto s: model.getListaRicercaSoggetto()){	
				//itero cercando il soggetto selezionato
				if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
					//soggetto trovato
					
					//controllo sullo stato:
					if(s.getStatoOperativo() != null && (!s.getStatoOperativo().name().equalsIgnoreCase(Constanti.STATO_VALIDO)) && (!s.getStatoOperativo().name().equalsIgnoreCase(Constanti.STATO_IN_MODIFICA))){
						//stato non compatibile
						listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("entita: soggetto " + s.getCodiceSoggetto(), s.getStatoOperativo().name()));
						addErrori(listaErrori);
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						return INPUT;
					}else{
						//stato compatibile
						
						//setto i dati del soggetto nel model:
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						if(!StringUtils.isEmpty(tipoImpegno) && tipoImpegno.equalsIgnoreCase("Impegno")){
							model.setSoggettoImpegnoAttuale(clone(s));
						} else {
							model.setSoggettoSubAttuale(clone(s));
						}
						
						if(!StringUtils.isEmpty(tipoImpegno) && tipoImpegno.equalsIgnoreCase("Accertamento")){
							model.setSoggettoImpegnoAttuale(clone(s));
						} else {
							model.setSoggettoSubAttuale(clone(s));
						}
					}
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * gestione tasto indietro
	 * @return
	 */
	public String indietro(){
		
		//info per debug:
		setMethodName("indietro");
		
		//reimposto il soggetto effettivo nel caso di doppia modifica
		SoggettoImpegnoModel soggettoNew = new SoggettoImpegnoModel();
		
		if(soggettoImpegnoEffettivo!=null && !StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto())){
			//codice creditore
			soggettoNew.setCodCreditore(soggettoImpegnoEffettivo.getCodiceSoggetto());
		}
		model.getStep1Model().setSoggetto(soggettoNew);
		
		//reimposto la classe soggetto:
		ClasseSoggetto classeSoggettoNew = new ClasseSoggetto();
		
		if((classeSoggettoEffettivo!=null && !StringUtils.isEmpty(classeSoggettoEffettivo.getCodice()))
				&& (soggettoImpegnoEffettivo==null || StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto()))){
			//codice classe
			classeSoggettoNew.setCodice(classeSoggettoEffettivo.getCodice());
		}		
		model.getStep1Model().getSoggetto().setClasse(classeSoggettoNew.getCodice());		
		
		return GOTO_ELENCO_MODIFICHE;
	}
		
	/**
	 * gestione tasto annulla
	 */
	public String annulla(){
		setMethodName("annulla");
		return SUCCESS;
	}
	
	public String listaClasseSoggettoChanged() {
		model.getStep1Model().getSoggetto().setCodCreditore(null);
		model.getStep1Model().setSoggettoSelezionato(false);
		return "headerSoggetto";
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public Impegno getImpegnoDaModificare() {
		return impegnoDaModificare;
	}

	public void setImpegnoDaModificare(Impegno impegnoDaModificare) {
		this.impegnoDaModificare = impegnoDaModificare;
	}

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public List<String> getTipoImpegnoModificaSoggettoList() {
		return tipoImpegnoModificaSoggettoList;
	}

	public void setTipoImpegnoModificaSoggettoList(
			List<String> tipoImpegnoModificaSoggettoList) {
		this.tipoImpegnoModificaSoggettoList = tipoImpegnoModificaSoggettoList;
	}

	public List<String> getNumeroSubImpegnoList() {
		return numeroSubImpegnoList;
	}

	public void setNumeroSubImpegnoList(List<String> numeroSubImpegnoList) {
		this.numeroSubImpegnoList = numeroSubImpegnoList;
	}

	public boolean isSubImpegnoListExist() {
		return subImpegnoListExist;
	}

	public void setSubImpegnoListExist(boolean subImpegnoListExist) {
		this.subImpegnoListExist = subImpegnoListExist;
	}

	public String getIdSub() {
		return idSub;
	}

	public void setIdSub(String idSub) {
		this.idSub = idSub;
	}

	public Soggetto getSoggettoImpegnoEffettivo() {
		return soggettoImpegnoEffettivo;
	}

	public void setSoggettoImpegnoEffettivo(Soggetto soggettoImpegnoNuovo) {
		this.soggettoImpegnoEffettivo = soggettoImpegnoNuovo;
	}

	public Soggetto getSoggettoSubEffettivo() {
		return soggettoSubEffettivo;
	}

	public void setSoggettoSubEffettivo(Soggetto soggettoSubNUovo) {
		this.soggettoSubEffettivo = soggettoSubNUovo;
	}

	public ClasseSoggetto getClasseSoggettoAttuale() {
		return classeSoggettoAttuale;
	}

	public void setClasseSoggettoAttuale(ClasseSoggetto classeSoggettoAttuale) {
		this.classeSoggettoAttuale = classeSoggettoAttuale;
	}

	public ClasseSoggetto getClasseSoggettoEffettivo() {
		return classeSoggettoEffettivo;
	}

	public void setClasseSoggettoEffettivo(ClasseSoggetto classeSoggettoNew) {
		this.classeSoggettoEffettivo = classeSoggettoNew;
	}

	public String getSubSelected() {
		return subSelected;
	}

	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public StatoOperativoAnagrafica getStatoSoggettoSelezionato() {
		return statoSoggettoSelezionato;
	}

	public void setStatoSoggettoSelezionato(
			StatoOperativoAnagrafica statoSoggettoSelezionato) {
		this.statoSoggettoSelezionato = statoSoggettoSelezionato;
	}

	public Boolean getSubImpegnoSelected() {
		return subImpegnoSelected;
	}

	public void setSubImpegnoSelected(Boolean subImpegnoSelected) {
		this.subImpegnoSelected = subImpegnoSelected;
	}

	public String getNumeroSubImpegno() {
		return numeroSubImpegno;
	}

	public void setNumeroSubImpegno(String numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}

	public ClasseSoggetto getClasseSoggettoSubAttuale() {
		return classeSoggettoSubAttuale;
	}

	public void setClasseSoggettoSubAttuale(ClasseSoggetto classeSoggettoSubAttuale) {
		this.classeSoggettoSubAttuale = classeSoggettoSubAttuale;
	}

	public ClasseSoggetto getClasseSoggettoSubEffettivo() {
		return classeSoggettoSubEffettivo;
	}

	public void setClasseSoggettoSubEffettivo(ClasseSoggetto classeSoggettoSubNew) {
		this.classeSoggettoSubEffettivo = classeSoggettoSubNew;
	}

	public String getAbbina() {
		return abbina;
	}

	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}

	public List<String> getAbbinaList() {
		return abbinaList;
	}

	public void setAbbinaList(List<String> abbinaList) {
		this.abbinaList = abbinaList;
	}

	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}

	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}

	@Override 
	public String getActionDataKeys() {
		return "model,subImpegnoSelected,tipoImpegno, soggettoSubEffettivo,ta,am, soggettoImpegnoEffettivo, classeSoggettoEffettivo";
	}

	public Boolean getTipoMovimentoImpegno() {
		return tipoMovimentoImpegno;
	}

	public void setTipoMovimentoImpegno(Boolean tipoMovimentoImpegno) {
		this.tipoMovimentoImpegno = tipoMovimentoImpegno;
	}

	public String getSoggettoDesiderato() {
		return soggettoDesiderato;
	}

	public void setSoggettoDesiderato(String soggettoDesiderato) {
		this.soggettoDesiderato = soggettoDesiderato;
	}

	public String getAperturaIniziale() {
		return aperturaIniziale;
	}

	public void setAperturaIniziale(String aperturaIniziale) {
		this.aperturaIniziale = aperturaIniziale;
	}

}