/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.RigaDiReintroitoOrdinativoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenutaSplit;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenute;


/**
 * classe utilizzata per la definizione univoca del modello dati in sessione
 * 
 * @author claudio.picco
 *
 */
public class ActionKeyReintroitoOrdinativoPagamentoAction extends WizardReintroitoOrdinativoAction {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		return "reintroitoOrdinativoPagamento";
	}

	protected void pulisciChecksWarningPopUp(){
		//pulisco i warning per evitare la comparsa dei popup:
		model.getReintroitoOrdinativoStep2Model().setCheckWarningSoggetti(false);
		model.getReintroitoOrdinativoStep2Model().setCheckWarningPianoDeiConti(false);
		//
	}
	
	public List<String> buildListaScelteProvvedimentoDaUsare(){
		List<String> lista = new ArrayList<String>();
		lista.add(WebAppConstants.SCELTA_PROVVEDIMENTO_UNICO);
		lista.add(WebAppConstants.SCELTA_PROVVEDIMENTO_DA_MOVIMENTI);
		return lista;
	}
	
	/**
	 * Inizializza i dati nella tabella dello step 2, va eseguito solo quando si cambia l'ordinativo 
	 * di pagamento dallo step 1
	 */
	protected void popolaTabellaDeiReintroiti(){
		List<Ordinativo> ordinativiCollegati = leggiOrdinativiCollegati();
		List<RigaDiReintroitoOrdinativoModel> listOrdInc = new ArrayList<RigaDiReintroitoOrdinativoModel>();
		BigDecimal importoTotRitenute = new BigDecimal(0);
		
		//NON VANNO CONSIDERATI QUELLI ANNULLATI:
		List<Ordinativo> senzaAnnullati = FinUtility.rimuoviOrdinativiAnnullati(ordinativiCollegati);
		//
		
		for(Ordinativo it: senzaAnnullati){
			if(it!=null && it instanceof OrdinativoIncasso){
				OrdinativoIncasso ordinativoCollegato = (OrdinativoIncasso) it;
				
				RigaDiReintroitoOrdinativoModel riga = new RigaDiReintroitoOrdinativoModel();
				riga.setOrdInc(ordinativoCollegato);
				
				TipoAssociazioneEmissione tipoAssociazione = ordinativoCollegato.getTipoAssociazioneEmissione();
				riga.setCodiceAssociazioneEmissione(tipoAssociazione.toString());
				riga.setLabelRigaOrdinativo("Ordinativo incasso "
				+ordinativoCollegato.getAnno() + "/"
				+ ordinativoCollegato.getNumero() + " "
				+ tipoAssociazione.toString());
				
				riga.setDescrizione(ordinativoCollegato.getDescrizione());
				
				riga.setImporto(ordinativoCollegato.getImportoOrdinativo());
				
				//TOT RITENUTE:
				importoTotRitenute = importoTotRitenute.add(ordinativoCollegato.getImportoOrdinativo());
				
				listOrdInc.add(riga);
			}
		}
		model.getReintroitoOrdinativoStep2Model().setListOrdInc(listOrdInc);
		
		//la riga riepilogativa ritenute va prima inizializzata:
		model.getReintroitoOrdinativoStep2Model().setRitenute(new RigaDiReintroitoOrdinativoModel());
		//e popolato con il solo importo:
		model.getReintroitoOrdinativoStep2Model().getRitenute().setImporto(importoTotRitenute);
		
		//la riga del netto va inizializzata:
		model.getReintroitoOrdinativoStep2Model().setNetto(new RigaDiReintroitoOrdinativoModel());
	}
	
	protected List<Ordinativo> leggiOrdinativiCollegati(){
		List<Ordinativo> collegati = new ArrayList<Ordinativo>();
		if(model.getReintroitoOrdinativoStep1Model()!=null && model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare()!=null
				&& model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare().getElencoOrdinativiCollegati()!=null){
			collegati = model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare().getElencoOrdinativiCollegati();
		}
		return collegati;
	}
	
	/**
	 * Semplice metodo di comodo che ritorna
	 * true se l'utente ha selezionato il radio button provvedimento unico
	 * false se l'utente ha selezionato il radio button provvedimento da movimenti
	 * @return
	 */
	protected boolean provvedimentoUnico() {
		return WebAppConstants.SCELTA_PROVVEDIMENTO_UNICO.equalsIgnoreCase(model.getReintroitoOrdinativoStep1Model().getProvvedimentoDaUsare());
	}
	
	/**
	 * Semplice metodo di comodo per verificare se ci sono elementi nella lista delle ritenute.
	 * La jsp controlla questo booleano per visualizzare o meno le righe relative.
	 * @return
	 */
	public boolean ciSonoRitenute(){
		boolean ciSonoRitenute = false;
		if(model.getReintroitoOrdinativoStep2Model().getListOrdInc()!=null && 
				model.getReintroitoOrdinativoStep2Model().getListOrdInc().size()>0){
			ciSonoRitenute = true;
		}
		return ciSonoRitenute;
	}
	
	/**
	 * Costruisce la versione asincrona della request del servizio di reintroito
	 * @return
	 */
	protected AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento> buildReintroitoOrdinativoPagamentoRequestAsync(){
		AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento> reqAsync = new AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento>();
		reqAsync.setDataOra(new Date());
		reqAsync.setRichiedente(sessionHandler.getRichiedente());
		reqAsync.setAzioneRichiesta(sessionHandler.getAzioneRichiesta());
		ReintroitoOrdinativoPagamento req = buildReintroitoOrdinativoPagamentoRequest();
		reqAsync.setRequest(req);
		return reqAsync;
	}
	
	/**
	 * Si occupa di costruire, a partire dai dati presenti nel model,
	 * la request per richiamare il servizio di reintrito
	 * @return
	 */
	protected ReintroitoOrdinativoPagamento buildReintroitoOrdinativoPagamentoRequest(){
		ReintroitoOrdinativoPagamento reintroitoOrdinativoPagamento = new ReintroitoOrdinativoPagamento();
		
		//SETTIAMO IL PROVVEDIMENTO:
		if(provvedimentoUnico()){
			//SCELTO RADIO BUTTON PROVVEDIMENTO UNICO
			reintroitoOrdinativoPagamento.setUtilizzaProvvedimentoDaMovimento(false);
			//MI ASPETTO QUINDI UN PROVVEDIMENTO INDICATO DA USARE:
			AttoAmministrativo attoAmministrativo = popolaProvvedimento(model.getReintroitoOrdinativoStep1Model().getProvvedimento());
			reintroitoOrdinativoPagamento.setAttoAmministrativo(attoAmministrativo);
		} else {
			//SCELTO RADIO BUTTON PROVVEDIMENTO DA MOVIMENTI
			reintroitoOrdinativoPagamento.setUtilizzaProvvedimentoDaMovimento(true);
		}
		
		//BILANCIO:
		reintroitoOrdinativoPagamento.setBilancio(sessionHandler.getBilancio());
		
		//DATA INVOCAZIONE:
		reintroitoOrdinativoPagamento.setDataOra(new Date());
		
		//ENTE:
		reintroitoOrdinativoPagamento.setEnte(sessionHandler.getEnte());
		
		//RICHIEDENTE:
		reintroitoOrdinativoPagamento.setRichiedente(sessionHandler.getRichiedente());
		
		//L'ORDINATIVO DI PAGAMENTO OGGETTO DEL REINTROITO:
		OrdinativoKey ordinativoPagamento = buildOrdinativoKey(model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare());
		reintroitoOrdinativoPagamento.setOrdinativoPagamento(ordinativoPagamento );
		//
		
		//IMPEGNO DI DESTINAZIONE (DA RIGA NETTO):
		Impegno impegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione();
		Integer numeroSub = model.getReintroitoOrdinativoStep2Model().getNetto().getNumeroSubImpegno();
		reintroitoOrdinativoPagamento.setImpegnoSuCuiSpostare(buildMovimentoKey(impegnoDest, numeroSub));
		//
		
		//LE EVENTUALI RITENUTE:
		
		if(ciSonoRitenute()){
			
			//RIGA RITENUTE PRINCIPALE:
			reintroitoOrdinativoPagamento.setRitenute(null);//le passo nulle perche' lavora sulle split
			//reintroitoOrdinativoPagamento.setRitenute(buildReintroitoRitenute(model.getReintroitoOrdinativoStep2Model().getRitenute()));
			
			//RIGHE DI SPLIT:
			List<ReintroitoRitenutaSplit> righeSplit = buildListReintroitoRitenutaSplit(model.getReintroitoOrdinativoStep2Model().getListOrdInc());
			reintroitoOrdinativoPagamento.setRitenuteSplit(righeSplit);
			
		}
		
		return reintroitoOrdinativoPagamento;
	}
	
	/**
	 * Metodo interno a buildReintroitoOrdinativoPagamentoRequest 
	 * per popolare un oggetto ReintroitoRitenute
	 * a partire da RigaDiReintroitoOrdinativoModel
	 * @param rigaModel
	 * @return
	 */
	protected ReintroitoRitenute buildReintroitoRitenute(RigaDiReintroitoOrdinativoModel rigaModel){
		ReintroitoRitenute reintroitoRitenute = new ReintroitoRitenute();
		if(rigaModel!=null){
			//ok diverso da null
			reintroitoRitenute.setImpegnoDiDestinazione(buildMovimentoKey(rigaModel.getImpegnoDiDestinazione(), rigaModel.getNumeroSubImpegno()));
			reintroitoRitenute.setAccertamentoDiDestinazione(buildMovimentoKey(rigaModel.getAccertamentoDiDestinazione(), rigaModel.getNumeroSubAccertamento()));
		}
		return reintroitoRitenute;
	}
	
	/***
	 * Metodo interno a buildReintroitoOrdinativoPagamentoRequest 
	 * per popolare una List<ReintroitoRitenutaSplit>
	 * a partire da una List<RigaDiReintroitoOrdinativoModel> listaRighe
	 * 
	 * @param listaRighe
	 * @return
	 */
	protected List<ReintroitoRitenutaSplit> buildListReintroitoRitenutaSplit(List<RigaDiReintroitoOrdinativoModel> listaRighe){
		List<ReintroitoRitenutaSplit> righeSplit = new ArrayList<ReintroitoRitenutaSplit>();
		if(listaRighe!=null && listaRighe.size()>0){
			//ok ha elementi
			for(RigaDiReintroitoOrdinativoModel it: listaRighe){
				if(it!=null){
					//ok diverso da null
					ReintroitoRitenutaSplit rigaIt = buildReintroitoRitenutaSplit(it);
					if(it!=null){
						righeSplit.add(rigaIt);
					}
				}
			}
		}
		return righeSplit;
	}
	
	/**
	 * Metodo interno a buildReintroitoOrdinativoPagamentoRequest 
	 * per popolare un oggetto ReintroitoRitenutaSplit
	 * a partire da RigaDiReintroitoOrdinativoModel
	 * 
	 * @param rigaModel
	 * @return
	 */
	protected ReintroitoRitenutaSplit buildReintroitoRitenutaSplit(RigaDiReintroitoOrdinativoModel rigaModel){
		ReintroitoRitenutaSplit reitroitoRitenutaSplit = new ReintroitoRitenutaSplit();
		if(rigaModel!=null){
			//ok diverso da null
			reitroitoRitenutaSplit.setImpegnoDiDestinazione(buildMovimentoKey(rigaModel.getImpegnoDiDestinazione(), rigaModel.getNumeroSubImpegno()));
			reitroitoRitenutaSplit.setAccertamentoDiDestinazione(buildMovimentoKey(rigaModel.getAccertamentoDiDestinazione(), rigaModel.getNumeroSubAccertamento()));
			reitroitoRitenutaSplit.setOrdinativoIncasso(buildOrdinativoKey(rigaModel.getOrdInc()));
		}
		return reitroitoRitenutaSplit;
	}
	
	/**
	 * Metodo interno a buildReintroitoOrdinativoPagamentoRequest 
	 * per popolare un oggetto OrdinativoKey
	 * a partire da Ordinativo
	 * 
	 * @param ord
	 * @return
	 */
	protected OrdinativoKey buildOrdinativoKey(Ordinativo ord){
		OrdinativoKey ordKey = new OrdinativoKey();
		if(ord!=null){
			//ok diverso da null
			ordKey.setAnno(ord.getAnno());
			if(ord.getAnnoBilancio()==null){
				ordKey.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
			} else {
				ordKey.setAnnoBilancio(ord.getAnnoBilancio());
			}
			ordKey.setNumero(ord.getNumero());
		}
		return ordKey;
	}
	
	/**
	 * Metodo interno a buildReintroitoOrdinativoPagamentoRequest 
	 * per popolare un oggetto MovimentoKey
	 * a partire da MovimentoGestione e un eventuale numero sub selezionato
	 * 
	 * @param mov
	 * @param numeroSub
	 * @return
	 */
	protected MovimentoKey buildMovimentoKey(MovimentoGestione mov, Integer numeroSub){
		MovimentoKey movKey = new MovimentoKey();
		if(mov!=null){
			//ok diverso da null
			movKey.setAnnoEsercizio(sessionHandler.getBilancio().getAnno());
			movKey.setNumeroMovimento(mov.getNumeroBigDecimal());
			movKey.setAnnoMovimento(mov.getAnnoMovimento());
			if(numeroSub!=null){
				movKey.setNumeroSubMovimento(new BigDecimal(numeroSub));
			}
		}
		return movKey;
	}
	
	protected String buildNomeEntitaPerMessaggiDiErrore(MovimentoKey mKey, boolean impegno){
		String nomeEntita = "Accertamento";
		if(impegno){
			nomeEntita = "Impegno";
		}
		if(FinUtility.maggioreDiZero(mKey.getNumeroSubMovimento())){
			nomeEntita = "Sub" + nomeEntita;
		}
		return nomeEntita;
	}
	
	protected String buildCodiceEntitaPerMessaggiDiErrore(MovimentoKey mKey){
		String codiceEntita = mKey.getAnnoEsercizio() + " / " + mKey.getAnnoMovimento() + " / " + mKey.getNumeroMovimento();
		if(FinUtility.maggioreDiZero(mKey.getNumeroSubMovimento())){
			codiceEntita = codiceEntita + " / " + mKey.getNumeroSubMovimento();
		}
		return codiceEntita;
	}
	
	public BigDecimal calcolaImportoNettoPerReintroito(OrdinativoPagamento ordinativoPagamentoRicaricato){
		BigDecimal sommaImportiOrdinativiIncasso = sommaImportoOrdinativiIncasso(ordinativoPagamentoRicaricato);
		BigDecimal importoNetto = ordinativoPagamentoRicaricato.getImportoOrdinativo().subtract(sommaImportiOrdinativiIncasso);
		return importoNetto;
	}
	
	/**
	 * Semplice metodo di comodo che somma gli importi degli ordinativi di incasso collegati.
	 * 
	 * NOTA: esclude gli annullati che e' come se non ci fossero.
	 * 
	 * @param ordinativoPag
	 * @return
	 */
	public BigDecimal sommaImportoOrdinativiIncasso(OrdinativoPagamento ordinativoPag){
		BigDecimal sommaImportiOrdinativiIncasso = new BigDecimal(0.0);
		if(ordinativoPag!=null){
			List<Ordinativo> senzaAnnullati = rimuoviOrdinativiAnnullati(ordinativoPag.getElencoOrdinativiCollegati());
			if(ordinativoPag!=null && !FinStringUtils.isEmpty(senzaAnnullati)){
				for(Ordinativo it: senzaAnnullati){
					if(it!=null){
						sommaImportiOrdinativiIncasso = sommaImportiOrdinativiIncasso.add(it.getImportoOrdinativo());
					}
				}
			}
		}
		return sommaImportiOrdinativiIncasso;
	}
	
	/**
	 * Somma gli importi dei soli ordinativi di incasso delle sole righe specificate
	 * @param lista
	 * @return
	 */
	public BigDecimal sommaImportiOrdinativiIncasso(List<RigaDiReintroitoOrdinativoModel> lista){
		BigDecimal somma = new BigDecimal(0.0);
		if(!FinStringUtils.isEmpty(lista)){
			for(RigaDiReintroitoOrdinativoModel it : lista){
				if(it!=null && it.getOrdInc()!=null && it.getOrdInc().getImportoOrdinativo()!=null){
					somma = somma.add(it.getOrdInc().getImportoOrdinativo());
				}
			}
		}
		return somma;
	}
	
	public List<Ordinativo> rimuoviOrdinativiAnnullati(List<Ordinativo> lista){
		List<Ordinativo> senzaAnnullati = new ArrayList<Ordinativo>();
		if(!FinStringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(it!=null && !CostantiFin.D_ORDINATIVO_STATO_ANNULLATO.equals(it.getStatoOperativoOrdinativo())){
					senzaAnnullati.add(clone(it));
				}
			}
		}
		return senzaAnnullati;
	}
	
	protected void annullaModelStep1(){
			
		//ANNO E NUMERO:
		model.getReintroitoOrdinativoStep1Model().setNumeroOrdinativoPagamento(null);
		
		//oggetto principale:
		model.getReintroitoOrdinativoStep1Model().setOrdinativoDaReintroitare(null);
		 
		//importo netto:
		model.getReintroitoOrdinativoStep1Model().setImportoNetto(null);
		 
		//Provvedimento:
		model.getReintroitoOrdinativoStep1Model().setProvvedimentoOrdinativoDaReintroitare(new ProvvedimentoImpegnoModel());
		model.getReintroitoOrdinativoStep1Model().setProvvedimentoDaUsare(WebAppConstants.SCELTA_PROVVEDIMENTO_UNICO);
		model.getReintroitoOrdinativoStep1Model().setProvvedimentoSelezionato(false);
		model.getReintroitoOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
		
		//soggetto:
		model.getReintroitoOrdinativoStep1Model().setSoggettoOrdinativoDaReintroitare(new SoggettoImpegnoModel());
		//
		
		//Modalita pagamento:
		model.getReintroitoOrdinativoStep1Model().setDescrArrModPag(null);
		//
		
		//capitolo:
		model.getReintroitoOrdinativoStep1Model().setCapitoloOrdinativoDaReintroitare(new CapitoloImpegnoModel());
		//
		
	}
	
	protected void annullaModelStep2(){
		pulisciChecksWarningPopUp();
		popolaTabellaDeiReintroiti();
	}
}