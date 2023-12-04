/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciModificaMovimentoSpesaSoggettoAction extends ActionKeyAggiornaImpegno {

	/**
	 * 
	 */
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
	
	//SIAC-7738 Inizio  FL 25/08/2020 riporto modifiche relative alla 7349
	private String minImp;
	private String maxImp;
	private String minSub;
	private String maxSub;
	private String minAnche;
	private String maxAnche;
	//IMPORTI CALCOLATI
	private String minImportoCalcolato;
	private String maxImportoCalcolato;
	private String minImportoSubCalcolato;
	private String maxImportoSubCalcolato;
	private BigDecimal minImportoImpegnoApp;
	private BigDecimal maxImportoImpegnoApp;
	private BigDecimal minImportoSubApp;
	private BigDecimal maxImportoSubApp;
	
	//SIAC-7349
	private BigDecimal minImportoImpegnoCompApp;
	private BigDecimal maxImportoImpegnoCompApp;
	//SIAC-7738 Fine  FL 25/08/2020 riporto modifiche relative alla 7349
	
	private AttoAmministrativo am;
	
	private String aperturaIniziale;
	
	public InserisciModificaMovimentoSpesaSoggettoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	@Override
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return false;
	}
	
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Movimento Spesa Soggetto");
		
		//Carico Lista Tipo Modifiche
		caricaListaMotivi();
		
		//Preparo Attibruti per la pagina
		getAbbinaList().add("Modifica anche Impegno");
			
		//preparo i dati per i tendini di scelta se si intende modificare un impegno o un sub
		if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0){
			//itero sui sub impegni e mi salvo in numeroSubImpegnoList
			//solo quelli con stato DEFINTIVO:
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(sub.getStatoOperativoMovimentoGestioneSpesa().equals("D")){
					if(sub.getNumeroBigDecimal() != null){
						numeroSubImpegnoList.add(String.valueOf(sub.getNumeroBigDecimal()));
					}		
				}
			}
			tipoImpegno = "Impegno";		
			if(numeroSubImpegnoList.size() > 0){
				//presenza di sub in numeroSubImpegnoList, quindi si potra' scegliere
				//tra impegno e subimpegno:
				tipoImpegnoModificaSoggettoList.add("Impegno");
				tipoImpegnoModificaSoggettoList.add("SubImpegno");
				tipoImpegno = "SubImpegno";			
			} else {
				//non ci sono sub in numeroSubImpegnoList, si puo' scegliere solo impegno:
				tipoImpegnoModificaSoggettoList.add("Impegno");	
			}

		} else {
			//non esistono proprio sub, l'unica scelta non puo'
			//che essere impegno:
			tipoImpegno = "Impegno";
			tipoImpegnoModificaSoggettoList.add("Impegno");
		}
			
		//Soggetto:
		if(model.getImpegnoInAggiornamento().getSoggetto()!=null && model.getSoggettoImpegnoAttuale()==null){
			Soggetto soggCloned = clone(model.getImpegnoInAggiornamento().getSoggetto());
			model.setSoggettoImpegnoAttuale(soggCloned);
		}
		
		//classe soggetto:
		if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null){
			setClasseSoggettoAttuale(model.getImpegnoInAggiornamento().getClasseSoggetto());
			setClasseSoggettoEffettivo(model.getImpegnoInAggiornamento().getClasseSoggetto());
		}		
		
		//codice soggetto:
		if(model.getStep1Model().getSoggettoImpegno()!=null && model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()!=null){	
			// jira 958			
			if(model.getImpegnoInAggiornamento().getSoggetto()!=null && (!model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto().equalsIgnoreCase(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto()))){
				//NOTHING TO DO
			} else {
				Soggetto soggCloned = clone(model.getImpegnoInAggiornamento().getSoggetto());
				setSoggettoImpegnoEffettivo(soggCloned);
			}	
		}
		
		//tipo movimento impegno a true:
		tipoMovimentoImpegno = true;
		
		//SIAC-7738 Inizio  FL 25/08/2020 riporto modifiche relative alla 7349

		
		//Calcolo importi
		
		if(model.getDisponibilitaImpegnoModifica()==null){
			model.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
		}
		
		//FIX PER JIRA SIAC-3801
		//minImportoImpegnoApp = minoreTraDisponibileInModificaEDispAVincolare().negate();

		// RM JIRA 5371
		minImportoImpegnoApp = model.getDisponibilitaImpegnoModifica().negate();
		
		//SIAC-7349
		//Per come calcocata questa disponibilità (valore impegnato - liquidazioni)
		//il limite sinistro della singola componente equivale a quello dell'impegno
		//in quanto esso è riferito alla componente stessa
		minImportoImpegnoCompApp = minImportoImpegnoApp;
		

		
		
		//
		
		if(model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
			maxImportoImpegnoApp = new BigDecimal(0);
			//SIAC-7349
			maxImportoImpegnoCompApp = new BigDecimal(0);
		} else if(model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()){
			maxImportoImpegnoApp = new BigDecimal(0);
			//SIAC-7349
			maxImportoImpegnoCompApp = new BigDecimal(0);
		} else {
			//SIAC-580
			
			if(model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() ){
				// anno corrente
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno1();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(0);
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +1) ){
//				anno  +1
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno2();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(1);
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +2) ){
//				anno +2
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno3();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(2);
			
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
		
		//SIAC-7349
		if(minImportoImpegnoCompApp == null){
			//pongo a zero
			minImportoImpegnoCompApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp == null){
			//pongo a zero
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//SIAC-7349
		if(maxImportoImpegnoCompApp == null){
			//pongo a zero
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		
		
		//GESTIONE FLAG SDF:
		if(model.getImpegnoInAggiornamento().isFlagSDF()){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//SIAC-7349 importo a zero il maxImportoImpegnoCompApp come fatto in precedenza, a determinate condizioni
		if(model.getImpegnoInAggiornamento().isFlagSDF()){
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//SIAC-7349 importo a zero il maxImportoImpegnoCompApp come fatto in precedenza, a determinate condizioni
		if(maxImportoImpegnoCompApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoCompApp = new BigDecimal(0);
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
		
		//SIAC-7349
		model.setMinImportoImpComp(convertiBigDecimalToImporto(minImportoImpegnoCompApp));
		model.setMaxImportoImpComp(convertiBigDecimalToImporto(maxImportoImpegnoCompApp));
		
		//SIAC-7349 non aggiunte e non valorizzati gli attributi nel model -> minImportoCalcolatoCompMod e maxImportoCalcolatoCompMod  
		//per presunta inutilità
		
		//SIAC-7738 Fine  FL 25/08/2020 riporto modifiche relative alla 7349
		
	}
	
	
	//SIAC-7738 Inizio FL 25/08/2020 riporto modifiche relative alla 7349

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
			}else if(i==1){
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}
			}else if (i==2){
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}
			}			
		}			
		return BigDecimal.ZERO;	
	}
		//SIAC-7349 - End
	//SIAC-7738 Fine  FL 25/08/2020 riporto modifiche relative alla 7349
	
	/**
	 * Metodo execute della action
	 */
	@Override
	public String execute() throws Exception {
		
		if(aperturaIniziale!=null && aperturaIniziale.equals("true")){
			pulisciSoggettoSelezionato();
			aperturaIniziale = null;
		}
		
		model.setProseguiConWarningControlloSoggetti(false);
		//tipoImpegno "Accertamento"
		caricaLabelsAggiorna(3);
		//mi salvo temporaneamente il provvedimento per reimpostarlo nell'impegno e associare il nuovo provv alle sole modifiche
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimento());
			setAm(attoImpegno);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Wrapper di retrocompatibilita' per quando viene chiamato dalla jsp alla selezione di un sub
	 * @return
	 */
	public String caricaDatiSub(){
		return caricaDatiSub(false);
	}
	
	/**
	 * Individua il sub selezionato e setta i dati opportuni nelle variabili
	 * @param soggettoSubAttualeValidatoDaServizio
	 * @return
	 */
	public String caricaDatiSub(boolean soggettoSubAttualeValidatoDaServizio){	//da spostare
		setMethodName("caricaDatiSub");
		
		if(StringUtils.isEmpty(getTipoImpegno())){
			//se tipo e' vuoto non e' stato selezionato nulla
			setTipoImpegno("Impegno");
			setSubImpegnoSelected(false);
		} else if(getTipoImpegno().equalsIgnoreCase("SubImpegno")){
			if(!StringUtils.isEmpty(getSubSelected())){
				//sub selezionato
				setSubImpegnoSelected(true);
				
				//itero tra i sub cercando quello selezionato:
				for(SubImpegno sub : model.getListaSubimpegni()){
					if(String.valueOf(sub.getNumeroBigDecimal()).equals(getSubSelected())){
						//trovato
						
						//Setto i dati del sug trovato:
						setNumeroSubImpegno(String.valueOf(sub.getNumeroBigDecimal()));
						
						if(sub.getSoggetto()!=null){
							Soggetto soggCloned = clone(sub.getSoggetto());
							setSoggettoSubEffettivo(soggCloned);
							
							if(!soggettoSubAttualeValidatoDaServizio){
								model.setSoggettoSubAttuale(soggCloned);
							}
							
						}
							
						if(sub.getClasseSoggetto()!=null){
							setClasseSoggettoSubEffettivo(sub.getClasseSoggetto());
						}
					}
				}
			} else {
				//sub non selezionato
				setSubImpegnoSelected(false);
				tipoImpegno = "Impegno";
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * salvataggio con bypass dodicesimi
	 * @return
	 * @throws Exception
	 */
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
	
	public String salvaInternal(boolean byPassDodicesimi) throws Exception {
		
		//info per debug:
		setMethodName("salva");
		
		//se sub selezionato, setto il suo soggetto effettivo:
		if(subSelected!=null){	
			//cerco su lista sub quello selezionato:
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
					//trovato
					Soggetto soggCloned = clone(sub.getSoggetto());
					setSoggettoSubEffettivo(soggCloned);
					break;
				}					
			}	
		}
		
		//istanzio la lista di eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreProvvedimento = false;
		
		//soggetto desiderato:
		if(model.getSoggettoImpegnoAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoImpegnoAttuale().getCodiceSoggetto())){
			setSoggettoDesiderato(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
		} else if(model.getSoggettoSubAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoSubAttuale().getCodiceSoggetto())){
			setSoggettoDesiderato(model.getSoggettoSubAttuale().getCodiceSoggetto());
		}
		
		
		if(StringUtils.isEmpty(tipoImpegno)){
			
			//non e' stato scelto cosa si vuole modificare tra accertamento e sub accertamento
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo impegno"));
			addErrori(listaErrori);
			
			return INPUT;
			
		}else if(tipoImpegno.equalsIgnoreCase("Impegno")){

			
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
			listaErrori = controlloGestisciImpegnoDecentratoPerModifica(listaErrori);
			
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

			
			//controllo stati subimpegni
			if(model.getImpegnoInAggiornamento().getElencoSubImpegni()!=null){
				List<SubImpegno> listaSubImpegni = model.getImpegnoInAggiornamento().getElencoSubImpegni();
				for(SubImpegno subImpegno : listaSubImpegni){
					String stato = subImpegno.getStatoOperativoMovimentoGestioneSpesa();
					if(stato.equalsIgnoreCase("D")){
						listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("entita: subimpegno " + subImpegno.getNumeroBigDecimal(),"DEFINITIVO"));
					}
				}
			}
			
			//SIAC-8506
			controllaLunghezzaMassimaDescrizioneMovimento(getDescrizione(), listaErrori);
			
			//controllo esistenza soggetto o classe associato a impegno
			if((model.getStep1Model().getSoggettoImpegno()==null || StringUtils.isEmpty(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()))
				&& (model.getImpegnoInAggiornamento().getClasseSoggetto()==null || StringUtils.isEmpty(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice()))){
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
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "impegno", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento){
				//non ci sono errori
				
				List<ModificaMovimentoGestioneSpesa> modificheList = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
					
				//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
				modificheList = FinUtility.rimuoviConIdZero(modificheList);
				//
				
				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
				}
					
				ModificaMovimentoGestioneSpesa modSpesa = new ModificaMovimentoGestioneSpesa();
				
				//FIX PER JIRA  SIAC-3463 - mancava controllo null pointer dentro questa if:
				if(model.getImpegnoInAggiornamento().getSoggettoCode()!=null && model.getSoggettoImpegnoAttuale()!=null && 
						model.getSoggettoImpegnoAttuale().getCodiceSoggetto() !=null &&
						model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto().equalsIgnoreCase(model.getSoggettoImpegnoAttuale().getCodiceSoggetto())
						){
					Soggetto soggTemp = new Soggetto();
					soggTemp.setCodiceSoggetto(model.getImpegnoInAggiornamento().getSoggettoCode());
					modSpesa.setSoggettoOldMovimentoGestione(soggTemp);
				}
				else{	
					modSpesa.setSoggettoOldMovimentoGestione(model.getImpegnoInAggiornamento().getSoggetto());	
				}	
				
				//Soggetto NEW
				Soggetto soggettoNew = new Soggetto();
				soggettoNew.setCodiceSoggetto(model.getSoggettoImpegnoAttuale().getCodiceSoggetto());
				modSpesa.setSoggettoNewMovimentoGestione(soggettoNew);
				
				model.getMovimentoSpesaModel().setSoggettoNewMovimentoGestione(soggettoNew);
					
				//Classe OLD
				if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null){
					modSpesa.setClasseSoggettoOldMovimentoGestione(model.getImpegnoInAggiornamento().getClasseSoggetto());
				}	
						
				//Classe NEW
				ClasseSoggetto classeSoggettoNew = new ClasseSoggetto();
				classeSoggettoNew.setCodice(model.getStep1Model().getSoggetto().getClasse());
				modSpesa.setClasseSoggettoNewMovimentoGestione(classeSoggettoNew);
					
				if(classeSoggettoNew!=null){
					model.getMovimentoSpesaModel().setClasseSoggettoNewMovimentoGestione(classeSoggettoNew);
				}
					
				//inserimento, settiamo a zero l'uid:
				modSpesa.setUid(0);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				modSpesa = settaDatiProvvDalModel(modSpesa, model.getStep1Model().getProvvedimento());
				
				modSpesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				modSpesa.setDescrizione(getDescrizione());
				modSpesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_IMP);
				
				//16 FEBBRAIO 207 salvo soggettoPrimaDiAggiorna per ripristinarlo in caso di errori
				Soggetto soggettoPrimaDiAggiorna = clone(model.getStep1Model().getSoggettoImpegno());
				//
				model.getStep1Model().setSoggettoImpegno(soggettoNew);
				
				if(classeSoggettoNew.getDescrizione()!=null){
					model.getStep1Model().getSoggetto().setClasse(classeSoggettoNew.getDescrizione());
				}
				
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				if(getAm()!=null && getAm().getAnno()!=0){
					impostaProvvNelModel(getAm(), model.getStep1Model().getProvvedimento());
				}
			
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				modificheList.add(modSpesa);
					
				model.getImpegnoInAggiornamento().setListaModificheMovimentoGestioneSpesa(modificheList);
					
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
				
				//richiamo il servizio di aggiornamento:
				AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);
				model.setProseguiConWarning(false);
				
				//analizzo la risposta del servizio:
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					//errori dal servizio
					
					//16 FEBBRAIO 2017 ripristingo il soggetto in caso di errori:
					model.getStep1Model().setSoggettoImpegno(soggettoPrimaDiAggiorna);
					//
					
					//per il modale di conferma per bypassare il controllo dodicesimi:
					if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
						
						setShowModaleConfermaSalvaConBypassDodicesimi(true);
						
						//setto prosegui con warning a true per evitare un loop infinito tra i due byPass
						model.setProseguiConWarning(true);
						
						return INPUT;
					}
					
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
				
				//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
				pulisciTransazioneElementare();
				//
				
				forceReload = true;
				model.setImpegnoRicaricatoDopoInsOAgg(null);
				
				return GOTO_ELENCO_MODIFICHE;
				
			} else {
				//presenza errori
				addErrori(listaErrori);
				return INPUT;
			}
	
			
		}  else if(tipoImpegno.equals("SubImpegno")){
			
			//FACCIAMO PRIMA I WARNING:
			boolean presenzaWarningsSubimpegno = presenzaWarningsSubimpegnoPerModificaSoggetto();
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
			
			//controllo esistenza soggetto associato a subimpegno
			for(SubImpegno sub : model.getListaSubimpegni()){
				if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
					if(sub.getSoggetto().getCodiceSoggetto()==null){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto per il sub impegno non presente"));
					}
				}					
			}	
			
			
			boolean soggettoSubAttualeValidatoDaServizio = false;
			
			//controllo validita' soggetto
			//SIAC-7867
			if (model.getSoggettoSubAttuale() != null && model.getSoggettoSubAttuale().getCodiceSoggetto() != null 
					&& !"".equals(model.getSoggettoSubAttuale().getCodiceSoggetto())) {
				SoggettoImpegnoModel soggettoDaVerificare = new SoggettoImpegnoModel();
				soggettoDaVerificare.setCodCreditore(model.getSoggettoSubAttuale().getCodiceSoggetto());

				//verifica validita'
				if(!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(soggettoDaVerificare), false, oggettoDaPopolare)){
					return INPUT;
				} else {
					soggettoSubAttualeValidatoDaServizio = true;
					SoggettoImpegnoModel soggettoCaricatoDaServizio = model.getStep1Model().getSoggetto();
					model.getSoggettoSubAttuale().setCodiceFiscale(soggettoCaricatoDaServizio.getCodfisc());
					model.getSoggettoSubAttuale().setDenominazione(soggettoCaricatoDaServizio.getDenominazione());
				}				
			}else{
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
			}
			
			erroreProvvedimento = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "subImpegno", listaErrori);
			
			if(listaErrori.isEmpty() && !erroreProvvedimento){
					
				List<ModificaMovimentoGestioneSpesa> modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
					
				for(SubImpegno sub : model.getListaSubimpegni()){
					if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
						modificheList = sub.getListaModificheMovimentoGestioneSpesa();
					}
				}
				
				//16 FEBBRAIO 2017: per evitare che siano rimasti dati sporchi di tentativi di salvataggio precedenti non andati a buon fine:
				modificheList = FinUtility.rimuoviConIdZero(modificheList);
				//
				
				if(modificheList == null){
					modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
				}
				
				
				
				ModificaMovimentoGestioneSpesa modSpesa = new ModificaMovimentoGestioneSpesa();
				
				// Carico info soggetto OLD
				for(SubImpegno sub : model.getListaSubimpegni()){
					if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
						modSpesa.setSoggettoOldMovimentoGestione(sub.getSoggetto());
					}
				}	
				
								
				// Carico info soggetto NUOVO				
				if(model.getSoggettoSubAttuale()!=null && !StringUtils.isEmpty(model.getSoggettoSubAttuale().getCodiceSoggetto())){
					Soggetto soggettoSubNew = new Soggetto();
					soggettoSubNew.setCodiceSoggetto(model.getSoggettoSubAttuale().getCodiceSoggetto()); 
					modSpesa.setSoggettoNewMovimentoGestione(soggettoSubNew);
				}	
				
				// Carico info classe OLD
				if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null){
					modSpesa.setClasseSoggettoOldMovimentoGestione(model.getImpegnoInAggiornamento().getClasseSoggetto());
				}
				
				// Carico info classe NUOVA
				if(model.getStep1Model().getSoggetto()!=null && !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
					ClasseSoggetto classeSoggettoSubNew = new ClasseSoggetto();
					classeSoggettoSubNew.setCodice(model.getStep1Model().getSoggetto().getClasse());
					modSpesa.setClasseSoggettoNewMovimentoGestione(classeSoggettoSubNew);
				}
					
				//inserimento, settiamo a zero l'uid:
				modSpesa.setUid(0);
				
				//DATI DEL PROVVEDIMENTO DELLA MODIFICA:
				modSpesa = settaDatiProvvDalModel(modSpesa, model.getStep1Model().getProvvedimento());
				
				modSpesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
				modSpesa.setDescrizione(getDescrizione());
				modSpesa.setTipoMovimento(CostantiFin.MODIFICA_TIPO_SIM);
								
				//inserisco nel model il provvedimento dell'impegno (per mantanerlo uguale in modo da variare solo quello della modifica)
				if(getAm()!=null && getAm().getAnno()!=0){
					impostaProvvNelModel(getAm(), model.getStep1Model().getProvvedimento());
				}
				
				//Inserisco nell impegno che andra nel servizio aggiorna impegno
				modificheList.add(modSpesa);
				
				List<SubImpegno> nuovaSubImpegnoList = new ArrayList<SubImpegno>();
				for(SubImpegno sub : model.getListaSubimpegni()){
					if(String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)){
						sub.setListaModificheMovimentoGestioneSpesa(modificheList);
					}
					nuovaSubImpegnoList.add(sub);
				}
				
				model.setListaSubimpegni(nuovaSubImpegnoList);
				
				//per evitare lo stato definitivo all'impegno e mantenere lo stato definitivo non liquidabile
				//reimposto il soggetto effettivo e la classe effettiva per l'impegno
				SoggettoImpegnoModel soggettoNew = new SoggettoImpegnoModel();
				
				if(soggettoImpegnoEffettivo!=null && !StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto())){
					soggettoNew.setCodCreditore(soggettoImpegnoEffettivo.getCodiceSoggetto());
				}
				
				//16 FEBBRAIO 207 salvo soggettoPrimaDiAggiorna per ripristinarlo in caso di errori
				SoggettoImpegnoModel soggettoPrimaDiAggiorna = clone(model.getStep1Model().getSoggetto());
				//
				
				model.getStep1Model().setSoggetto(soggettoNew);								
				
				ClasseSoggetto classeSoggettoNew = new ClasseSoggetto();
				
				if((classeSoggettoEffettivo!=null && !StringUtils.isEmpty(classeSoggettoEffettivo.getCodice()))
						&& (soggettoImpegnoEffettivo==null || StringUtils.isEmpty(soggettoImpegnoEffettivo.getCodiceSoggetto()))){
					classeSoggettoNew.setCodice(classeSoggettoEffettivo.getCodice());
				}		
				model.getStep1Model().getSoggetto().setClasse(classeSoggettoNew.getCodice());				
					
				//Gestisco l abbina anche impegno
				if(!StringUtils.isEmpty(abbina)){
					if(abbina.equalsIgnoreCase("Modifica anche Impegno")){
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
				
				
				AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);
				model.setProseguiConWarning(false);
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
					
					//per il modale di conferma per bypassare il controllo dodicesimi:
					if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
						
						//16 FEBBRAIO 2017 ripristingo il soggetto in caso di errori:
						model.getStep1Model().setSoggetto(soggettoPrimaDiAggiorna);
						//
						
						setShowModaleConfermaSalvaConBypassDodicesimi(true);
						
						//setto prosegui con warning a true per evitare un loop infinito tra i due byPass
						model.setProseguiConWarning(true);
						
						return INPUT;
					}
					
					
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
		
				//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
				pulisciTransazioneElementare();
				//
				
				forceReload = true;
				model.setImpegnoRicaricatoDopoInsOAgg(null);
				
				return GOTO_ELENCO_MODIFICHE;
				
			} else {
				
				setSubImpegnoSelected(true);
				caricaDatiSub(soggettoSubAttualeValidatoDaServizio);
				
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
   protected boolean presenzaWarningsSubimpegnoPerModificaSoggetto() {
		boolean presenzaWarningsSubimpegno = false;
		
		Errore errorWarning = null;
		
		if(model.getListaSubimpegni()!=null){
			List<SubImpegno> listaSubImpegni = model.getListaSubimpegni();
			for(SubImpegno subImpegno : listaSubImpegni){
				if(subImpegno.getSoggetto()!=null){
					String stato = subImpegno.getStato().name();
					//SIAC-7867
					if(CostantiFin.STATO_VALIDO.equalsIgnoreCase(stato) && model.getSoggettoSubAttuale() != null 
							&& model.getSoggettoSubAttuale().getCodiceSoggetto() != null && subImpegno.getSoggetto().getCodiceSoggetto() != null
							&& model.getSoggettoSubAttuale().getCodiceSoggetto().equalsIgnoreCase(subImpegno.getSoggetto().getCodiceSoggetto())){							
						errorWarning = ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Creditore ", subImpegno.getSoggetto().getCodiceSoggetto() + " " + subImpegno.getSoggetto().getDenominazione());
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
				presenzaWarningsSubimpegno = Boolean.TRUE;
				model.setProseguiConWarningControlloSoggetti(true);
			}
	    }
		return presenzaWarningsSubimpegno;
	}
	
	/**
	 *  Metodo di selezione del soggetto
	 */
	public String selezionaSoggetto() {		
		setMethodName("selezionaSoggetto");
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
			
			//per non perdersi l'informazione in caso di successiva ricerca andata male:
			Soggetto ultimoSoggettoSelezionatoCorrettamente = model.getUltimoSoggettoSelezionatoCorrettamente();
			Soggetto soggettoAttuale = null;
			if(ultimoSoggettoSelezionatoCorrettamente!=null && !StringUtils.isEmpty(ultimoSoggettoSelezionatoCorrettamente.getCodiceSoggetto())){
				soggettoAttuale = ultimoSoggettoSelezionatoCorrettamente;
			} else {
				Soggetto soggCloned = clone(model.getImpegnoInAggiornamento().getSoggetto());
				soggettoAttuale = soggCloned;
			}
			if(subImpegnoSelected!=null){
				if(subImpegnoSelected){
					model.setSoggettoSubAttuale(soggettoAttuale);
				}else {
					model.setSoggettoImpegnoAttuale(soggettoAttuale);
				}
			}

			return INPUT;
		} else {
			//soggetto selezionato
			for(Soggetto s: model.getListaRicercaSoggetto()){	
				//itero cercando il soggetto selezionato
				if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
					//soggetto trovato
					
					//controllo sullo stato:
					if(s.getStatoOperativo() != null && (!s.getStatoOperativo().name().equalsIgnoreCase(CostantiFin.STATO_VALIDO)) && (!s.getStatoOperativo().name().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA))){
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
						//per non perdersi l'informazione in caso di successiva ricerca andata male:
						model.setUltimoSoggettoSelezionatoCorrettamente(s);
						//
					}
				}
			}
			return SUCCESS;
		}
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
		
		setForceReload(false);
		return GOTO_ELENCO_MODIFICHE;
	}
	
	//AUTOMATED GENERATE

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
		return "model,subImpegnoSelected,soggettoSubNUovo,soggettoSubEffettivo,ta,am";
	}

	public Boolean getTipoMovimentoImpegno() {
		return tipoMovimentoImpegno;
	}

	public void setTipoMovimentoImpegno(Boolean tipoMovimentoImpegno) {
		this.tipoMovimentoImpegno = tipoMovimentoImpegno;
	}

	public AttoAmministrativo getAm() {
		return am;
	}

	public void setAm(AttoAmministrativo am) {
		this.am = am;
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

	//SIAC-7738 Inizio FL 25/08/2020 riporto modifiche relative alla 7349
	/**
	 * @return the minImp
	 */
	public String getMinImp()
	{
		return minImp;
	}

	/**
	 * @param minImp the minImp to set
	 */
	public void setMinImp(String minImp)
	{
		this.minImp = minImp;
	}

	/**
	 * @return the maxImp
	 */
	public String getMaxImp()
	{
		return maxImp;
	}

	/**
	 * @param maxImp the maxImp to set
	 */
	public void setMaxImp(String maxImp)
	{
		this.maxImp = maxImp;
	}

	/**
	 * @return the minSub
	 */
	public String getMinSub()
	{
		return minSub;
	}

	/**
	 * @param minSub the minSub to set
	 */
	public void setMinSub(String minSub)
	{
		this.minSub = minSub;
	}

	/**
	 * @return the maxSub
	 */
	public String getMaxSub()
	{
		return maxSub;
	}

	/**
	 * @param maxSub the maxSub to set
	 */
	public void setMaxSub(String maxSub)
	{
		this.maxSub = maxSub;
	}

	/**
	 * @return the minAnche
	 */
	public String getMinAnche()
	{
		return minAnche;
	}

	/**
	 * @param minAnche the minAnche to set
	 */
	public void setMinAnche(String minAnche)
	{
		this.minAnche = minAnche;
	}

	/**
	 * @return the maxAnche
	 */
	public String getMaxAnche()
	{
		return maxAnche;
	}

	/**
	 * @param maxAnche the maxAnche to set
	 */
	public void setMaxAnche(String maxAnche)
	{
		this.maxAnche = maxAnche;
	}

	/**
	 * @return the minImportoCalcolato
	 */
	public String getMinImportoCalcolato()
	{
		return minImportoCalcolato;
	}

	/**
	 * @param minImportoCalcolato the minImportoCalcolato to set
	 */
	public void setMinImportoCalcolato(String minImportoCalcolato)
	{
		this.minImportoCalcolato = minImportoCalcolato;
	}

	/**
	 * @return the maxImportoCalcolato
	 */
	public String getMaxImportoCalcolato()
	{
		return maxImportoCalcolato;
	}

	/**
	 * @param maxImportoCalcolato the maxImportoCalcolato to set
	 */
	public void setMaxImportoCalcolato(String maxImportoCalcolato)
	{
		this.maxImportoCalcolato = maxImportoCalcolato;
	}

	/**
	 * @return the minImportoSubCalcolato
	 */
	public String getMinImportoSubCalcolato()
	{
		return minImportoSubCalcolato;
	}

	/**
	 * @param minImportoSubCalcolato the minImportoSubCalcolato to set
	 */
	public void setMinImportoSubCalcolato(String minImportoSubCalcolato)
	{
		this.minImportoSubCalcolato = minImportoSubCalcolato;
	}

	/**
	 * @return the maxImportoSubCalcolato
	 */
	public String getMaxImportoSubCalcolato()
	{
		return maxImportoSubCalcolato;
	}

	/**
	 * @param maxImportoSubCalcolato the maxImportoSubCalcolato to set
	 */
	public void setMaxImportoSubCalcolato(String maxImportoSubCalcolato)
	{
		this.maxImportoSubCalcolato = maxImportoSubCalcolato;
	}

	/**
	 * @return the minImportoImpegnoApp
	 */
	public BigDecimal getMinImportoImpegnoApp()
	{
		return minImportoImpegnoApp;
	}

	/**
	 * @param minImportoImpegnoApp the minImportoImpegnoApp to set
	 */
	public void setMinImportoImpegnoApp(BigDecimal minImportoImpegnoApp)
	{
		this.minImportoImpegnoApp = minImportoImpegnoApp;
	}

	/**
	 * @return the maxImportoImpegnoApp
	 */
	public BigDecimal getMaxImportoImpegnoApp()
	{
		return maxImportoImpegnoApp;
	}

	/**
	 * @param maxImportoImpegnoApp the maxImportoImpegnoApp to set
	 */
	public void setMaxImportoImpegnoApp(BigDecimal maxImportoImpegnoApp)
	{
		this.maxImportoImpegnoApp = maxImportoImpegnoApp;
	}

	/**
	 * @return the minImportoSubApp
	 */
	public BigDecimal getMinImportoSubApp()
	{
		return minImportoSubApp;
	}

	/**
	 * @param minImportoSubApp the minImportoSubApp to set
	 */
	public void setMinImportoSubApp(BigDecimal minImportoSubApp)
	{
		this.minImportoSubApp = minImportoSubApp;
	}

	/**
	 * @return the maxImportoSubApp
	 */
	public BigDecimal getMaxImportoSubApp()
	{
		return maxImportoSubApp;
	}

	/**
	 * @param maxImportoSubApp the maxImportoSubApp to set
	 */
	public void setMaxImportoSubApp(BigDecimal maxImportoSubApp)
	{
		this.maxImportoSubApp = maxImportoSubApp;
	}

	/**
	 * @return the minImportoImpegnoCompApp
	 */
	public BigDecimal getMinImportoImpegnoCompApp()
	{
		return minImportoImpegnoCompApp;
	}

	/**
	 * @param minImportoImpegnoCompApp the minImportoImpegnoCompApp to set
	 */
	public void setMinImportoImpegnoCompApp(BigDecimal minImportoImpegnoCompApp)
	{
		this.minImportoImpegnoCompApp = minImportoImpegnoCompApp;
	}

	/**
	 * @return the maxImportoImpegnoCompApp
	 */
	public BigDecimal getMaxImportoImpegnoCompApp()
	{
		return maxImportoImpegnoCompApp;
	}

	/**
	 * @param maxImportoImpegnoCompApp the maxImportoImpegnoCompApp to set
	 */
	public void setMaxImportoImpegnoCompApp(BigDecimal maxImportoImpegnoCompApp)
	{
		this.maxImportoImpegnoCompApp = maxImportoImpegnoCompApp;
	}

	//SIAC-7738 Fine  FL 25/08/2020 riporto modifiche relative alla 7349	
}
