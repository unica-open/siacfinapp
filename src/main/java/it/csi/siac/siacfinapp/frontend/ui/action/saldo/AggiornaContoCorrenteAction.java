/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.AggiornaContoCorrenteModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAddebitiContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAddebitiContoCorrenteResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrenteResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.saldo.AddebitoContoCorrente;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaContoCorrenteAction extends BaseContoCorrenteAction<AggiornaContoCorrenteModel>{
	private static final long serialVersionUID = 1L;

	//POS:
	private Integer pos;

	@Override
	protected String getTitolo() {
		//settiamo il titolo di pagina
		return "Aggiorna Conto Corrente";
	}

	/**
	 * Metodo per validare il saldo iniziale
	 * @throws Exception
	 */
	public void validateAggiornaSaldo() throws Exception {
		if (model.getVociContoCorrente().getSaldoIniziale() == null){
			//mancata obbligatorieta
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("saldo iniziale"));
		}
	}

	/**
	 * Metodo per aggiornare il saldo
	 * @return
	 * @throws Exception
	 */
	public String aggiornaSaldo() throws Exception {
		
		//istanzio la requeste del servizio:
		AggiornaSaldoInizialeContoCorrente asiccReq = new AggiornaSaldoInizialeContoCorrente();

		//setto i dati nella request:
		asiccReq.setVociContoCorrente(leggiVociContoCorrente());
		asiccReq.getVociContoCorrente().setSaldoIniziale(model.getVociContoCorrente().getSaldoIniziale());
		asiccReq.setEnte(sessionHandler.getEnte());
		asiccReq.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio:
		AggiornaSaldoInizialeContoCorrenteResponse asiccRes = saldoService.aggiornaSaldoInizialeContoCorrente(asiccReq);

		//ricarichiamo i dati:
		cerca();
		
		if (asiccRes.hasErrori()){
			//presenza errori
			addErrori(asiccRes.getErrori());
			return INPUT;
		}

		//tutto ok:
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore());

		return SUCCESS;
	}

	/**
	 * Inserimento addebito
	 * @return
	 * @throws Exception
	 */
	public String inserisciAddebito() throws Exception{
		model.insAddebitoContoCorrente();

		//aggiornamento dei dati:
		aggiornaAddebiti();

		//ricarico i dati:
		cerca();

		return SUCCESS;
	}

	/**
	 * Metodo per l'aggiornamento degli addebiti
	 * @return
	 * @throws Exception
	 */
	public String aggiornaAddebiti() throws Exception{
		
		//istanzio la request per il servizio
		AggiornaAddebitiContoCorrente aaccReq = new AggiornaAddebitiContoCorrente();

		aaccReq.setElencoAddebiti(model.getVociContoCorrente().getElencoAddebiti());

		aaccReq.setEnte(sessionHandler.getEnte());
		aaccReq.setRichiedente(sessionHandler.getRichiedente());

		//richiamo il servizio:
		AggiornaAddebitiContoCorrenteResponse aaccRes = saldoService.aggiornaAddebitiContoCorrente(aaccReq);

		//ricarico i dati:
		cerca();
		
		if (aaccRes.hasErrori()){
			//ci sono errori
			addErrori(aaccRes.getErrori());
			return INPUT;
		}

		//tutto ok:
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore());

		return SUCCESS;
	}
	
	/**
	 * Metodo per eliminare un addebito
	 * @return
	 * @throws Exception
	 */
	public String eliminaAddebito() throws Exception{
		
		//ricarico i dati:
		cerca();  

		//istanzio la request per il servizio:
		AggiornaAddebitiContoCorrente aaccReq = new AggiornaAddebitiContoCorrente();

		List<AddebitoContoCorrente> elencoAddebiti = model.getVociContoCorrente().getElencoAddebiti();
		
		elencoAddebiti.get(pos).setDataCancellazione(new Date());
		
		aaccReq.setElencoAddebiti(new ArrayList<AddebitoContoCorrente>(elencoAddebiti.subList(pos, pos + 1)));

		aaccReq.setEnte(sessionHandler.getEnte());
		aaccReq.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio di aggiornamento:
		AggiornaAddebitiContoCorrenteResponse aaccRes = saldoService.aggiornaAddebitiContoCorrente(aaccReq);

		
		if (aaccRes.hasErrori()){
			//presenza errori da servizio
			addErrori(aaccRes.getErrori());
			return INPUT;
		}

		//TUTTO OK:
		
		elencoAddebiti.remove(pos.intValue());
		
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore());

		return SUCCESS;
	}

	//GETTER E SETTER:
	
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
}
