/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.AggiornaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.InserisciSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RecapitoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RicercaElencoSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RicercaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.CFGenerator;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.VerificaPartitaIva;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

	
public final class ValidationUtils {
		
		/** Non instanziare la classe */
		private ValidationUtils() {}
		
		
		protected  static LogUtil log = new LogUtil(ValidationUtils.class);
		
		public final static Integer RICERCA_VUOTA = -1;
		
		/**
		 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
		 * 
		 * @param listaErrori	la lista degli errori su cui apporre l'errore
		 * @param campo			il campo da controllare
		 * @param nomeCampo		il nome del campo
		 */
		public static void checkNotNull(List<Errore> listaErrori, Object campo, String nomeCampo) {
			if(campo == null) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			}
		}
		
		/**
		 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; e di non vacuit&agrave; per un dato campo.
		 * 
		 * @param listaErrori	la lista degli errori su cui apporre l'errore
		 * @param campo			il campo da controllare
		 * @param nomeCampo		il nome del campo
		 */
		public static void checkNotNullNorEmpty(List<Errore> listaErrori, String campo, String nomeCampo) {
			if(StringUtils.trimToNull(campo) == null) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			}
		}
		
		/**
		 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo, che controlla
		 * anche che l'UID non sia invalido.
		 * 
		 * @param listaErrori	la lista degli errori su cui apporre l'errore
		 * @param campo			il campo da controllare
		 * @param nomeCampo		il nome del campo
		 */
		public static void checkNotNullNorInvalidUid(List<Errore> listaErrori, Entita campo, String nomeCampo) {
			if(campo == null || campo.getUid() == 0) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			}
		}	
		
		
		
		
		
		/**
		 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo, che controlla
		 * anche che il valore inserito non sia invalido.
		 * <br>
		 * Esemp&icirc; di campi numerici non validi:
		 * <ul>
		 * 	<li> {@link Integer} - pari a zero </li>
		 *  <li> {@link Double} - minore o pari a zero </li>
		 *  <li> {@link BigDecimal} - minore o pari a zero </li>
		 * </ul>
		 * 
		 * @param listaErrori	la lista degli errori su cui apporre l'errore
		 * @param campo			il campo da controllare
		 * @param nomeCampo		il nome del campo
		 */
		public static void checkNotNullNorInvalid(List<Errore> listaErrori, Number campo, String nomeCampo) {
			if(campo == null) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			} else if(campo instanceof Integer && (Integer)campo == 0) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			} else if(campo instanceof Double && (Double)campo <= 0) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, "Minore di zero"));
			} else if(campo instanceof BigDecimal && ((BigDecimal)campo).compareTo(BigDecimal.ZERO) <= 0) {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, "Minore di zero"));
			}
		}
		
		
		public static void checkNonVuoto(List<Errore> listaErrori, Object campo, String nomeCampo) {
			if(campo == null || StringUtils.isEmpty(campo.toString().trim())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo));
			}
		}
		
		/**
		 * validazione recapiti
		 * @param listaErrori
		 * @param model
		 */
		public static void validaRecapitiSoggetto(List<Errore> listaErrori, RecapitoModel model) {
			
			if(StringUtils.isEmpty(model.getPec()) &&
				Boolean.valueOf(model.getCheckAvvisoPec())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("PEC"));
			}
			
			if(StringUtils.isEmpty(model.getEmail()) &&
				Boolean.valueOf(model.getCheckAvvisoEmail())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("E-Mail"));
			}
		}
		
		
		public static void validaCampiInserimentoSoggettoPrimaParte(List<Errore> listaErrori,InserisciSoggettoModel model){
			final String methodName = "validaCampiInserimentoSoggettoPrimaParte";
			
			log.debug(methodName, "Entro nei controlli ");
			
			if(StringUtils.isEmpty(model.getIdTipoSoggetto())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Soggetto"));
			}
			
			if(null==model.getIdNaturaGiuridica()){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Natura giuridica"));
			}
			
			
			if(model.getIdTipoSoggetto().equals("PFI")){
				
				  if(StringUtils.isEmpty(model.getPartitaIva())){
					listaErrori.add(ErroreFin.PARTITA_IVA_OBBLIGATORIO.getErrore("Partita IVA"));
				  } else if(!VerificaPartitaIva.controllaPIVA(model.getPartitaIva()).equalsIgnoreCase("OK")){
					  
				       log.debug(methodName, "errore nella partita iva ");
				       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore());
				  
			      }
				  
				  if(StringUtils.isEmpty(model.getIdNaturaGiuridica())){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Natura giuridica"));
				  }
				  
			}else if( model.getIdTipoSoggetto().equals("PG") ||
					   model.getIdTipoSoggetto().equals("PGI")){
					
				    if( model.getIdTipoSoggetto().equals("PGI")){
				    	
				    	log.debug(methodName, "controllo i dati di persona giuridica PGI");
						if(StringUtils.isEmpty(model.getPartitaIva())){
								listaErrori.add(ErroreFin.PARTITA_IVA_OBBLIGATORIO.getErrore("Partita IVA"));
						}else if(!VerificaPartitaIva.controllaPIVA(model.getPartitaIva()).equalsIgnoreCase("OK")){
							  
						       log.debug(methodName, "errore nella partita iva ");
						       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore());
						  
					     }
				    }
				    
			    	if( model.getIdTipoSoggetto().equals("PG")){
				    	
				    	log.debug(methodName, "controllo i dati di persona giuridica PG ");
						if( StringUtils.isNotEmpty(model.getPartitaIva()) &&  
								!VerificaPartitaIva.controllaPIVA(model.getPartitaIva()).equalsIgnoreCase("OK")){
							  
						       log.debug(methodName, "errore nella partita iva ");
						       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore());
						  
					     }
				    }
					
					// natura giuridica
					 if(StringUtils.isEmpty(model.getIdNaturaGiuridica())){
							listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Natura giuridica"));
					  }
					
						if(StringUtils.isNotEmpty(model.getFlagResidenza()) &&
								model.getFlagResidenza().equalsIgnoreCase(WebAppConstants.MSG_SI)){
								
								if(StringUtils.isEmpty(model.getCodiceFiscaleEstero())){
									listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Fiscale Estero"));
								}
								
						}else if(StringUtils.isEmpty(model.getCodiceFiscale())){
						listaErrori.add(ErroreFin.CODICE_FISCALE_OBBLIGATORIO_PG.getErrore("Codice fiscale"));
				    }
					
			}
						
			// tipologia del soggetto
			if(model.getIdTipoSoggetto().equals("PF") ||
					   model.getIdTipoSoggetto().equals("PFI")){
				log.debug(methodName, "controllo i dati di persona fisica");
				if(StringUtils.isNotEmpty(model.getFlagResidenza()) &&
						model.getFlagResidenza().equalsIgnoreCase(WebAppConstants.MSG_SI)){
						
						if(StringUtils.isEmpty(model.getCodiceFiscaleEstero())){
							listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Fiscale Estero"));
						}
						
					}else	if(StringUtils.isEmpty(model.getCodiceFiscale())){
						listaErrori.add(ErroreFin.CODICE_FISCALE_OBBLIGATORIO_PF.getErrore("Codice Fiscale"));
					}
				
				
				// verifico il codice fiscale estero
				log.debug(methodName, "radio estero "+model.getFlagResidenza());
				
			
				
			}
		}
		
		/**
		 *  verifca inserimento indirizzi del soggetto
		 * @param listaErrori
		 * @param model
		 */
		public static void validaCampiInserimentoIndirizzi(List<Errore> listaErrori,InserisciSoggettoModel model){
			
			final String methodName = "validaCampiInserimentoIndirizzi";
			log.debug(methodName, "Entro nei controlli inserimento indirizzi");
			
			// comune
			if(StringUtils.isEmpty(model.getIndirizzo().getComune())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
			}
			// sedime - solo ITALIA (=1)
			if (WebAppConstants.CODICE_ITALIA.equals(model.getIndirizzo().getStato()))
				if (StringUtils.isEmpty(model.getIndirizzo().getSedime()))
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Sedime"));
		
			// nome descrizione della via
			if(StringUtils.isEmpty(model.getIndirizzo().getNomeVia())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Nome via"));
			}
			
			
		}
		
		public static void validaCampiInserimentoIndirizzi(List<Errore> listaErrori,IndirizzoSoggetto indirizzo){
			
			final String methodName = "validaCampiInserimentoIndirizzi";
			log.debug(methodName, "Entro nei controlli inserimento indirizzi");
			
			if(StringUtils.isEmpty(indirizzo.getComune())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
			}
			
			if (WebAppConstants.CODICE_ITALIA.equals(indirizzo.getCodiceNazione()))
				if(StringUtils.isEmpty(indirizzo.getSedime())){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Sedime"));
				}
			
			if(StringUtils.isEmpty(indirizzo.getDenominazione())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Nome via"));
			}
		}
		
		
		public static void validaCampiAggiornamentoIndirizzi(List<Errore> listaErrori,
															 IndirizzoSoggetto model){
			
			final String methodName = "validaCampiAggiornamentoIndirizzi";
			log.debug(methodName, "Entro nei controlli aggiornamento indirizzi");
			

			
			if(StringUtils.isEmpty(model.getComune())
					&& !WebAppConstants.CODICE_ITALIA.equals(model.getCodiceNazione())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
			}
			
			if (WebAppConstants.CODICE_ITALIA.equals(model.getCodiceNazione()))
				if(StringUtils.isEmpty(model.getSedime())){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Sedime"));
				}
				
			if(StringUtils.isEmpty(model.getDenominazione())){
				
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Nome via"));
			}
			
		}
		
			
		
		public static void validaCampiInserimentoSoggetto(List<Errore> listaErrori,InserisciSoggettoModel model){
			final String methodName = "validaCampiInserimentoSoggetto";
			log.debug(methodName, "Entro nei controlli");
			
			//model.getIdTipoSoggetto().equals("PFI") ||
			if(model.getIdTipoSoggetto().equals("PG") ||
			   model.getIdTipoSoggetto().equals("PGI")){
				
				if(null==model.getIdNaturaGiuridica() || StringUtils.isEmpty(model.getIdNaturaGiuridica())){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Natura giuridica"));
				}
				// trim degli spazi
				model.setDenominazione(model.getDenominazione().trim());
				
				if (StringUtils.isEmpty(model.getDenominazione()))
				{
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ragione sociale"));
				}
				else
				{
					if ((model.getDenominazione() + model.getIdNaturaGiuridica()).length() > 139) // considera spazio concatenazione
						listaErrori
								.add(ErroreCore.VALORE_NON_CONSENTITO
										.getErrore("Ragione Sociale",
												"(La concatenazione di Ragione sociale e Natura giuridica non deve superare i 140 caratteri)"));
				}
			}
			
			if(model.getIdTipoSoggetto().equals("PF") ||
					   model.getIdTipoSoggetto().equals("PFI")){
				log.debug(methodName, "controllo i dati di persona fisica");
				
				// trim degli spazi
				model.setCognome(model.getCognome().trim());
				model.setNome(model.getNome().trim());
				
				checkNonVuoto(listaErrori, model.getCognome(), "Cognome");
				checkNonVuoto(listaErrori, model.getNome(), "Nome");
				checkNonVuoto(listaErrori, model.getDataNascita(), "Data di nascita");
				
				checkNonVuoto(listaErrori, model.getComune(), "Comune");
				
				
				if ((model.getCognome() + model.getNome()).length() > 139) // considera spazio concatenazione
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Cognome, Nome",
							"(La concatenazione di Cognome e Nome non deve superare i 140 caratteri)"));
			}
			
			
			if(StringUtils.isEmpty(model.getIdTipoSoggetto())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Soggetto"));
			}
			
			if(StringUtils.isNotEmpty(model.getDataNascita())){

				if(model.getDataNascita().length()!=10){
					log.debug(methodName, "errore sulla lunghezza");
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Nascita","dd/mm/yyyy"));
				}else {
					
					Date dataInserita = DateUtility.parse(model.getDataNascita());
					log.debug(methodName, "data inserita "+dataInserita);
					if(null==dataInserita){
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Nascita","dd/mm/yyyy"));
					}
				}
			}
			
		
		}
		
		
		

		public static void validaCampiAggiornamentoSoggetto(List<Errore> listaErrori,  AggiornaSoggettoModel model, String dataNascitaStringa){
			final String methodName = "validaCampiAggiornamentoSoggetto";
			log.debug(methodName, "Entro nei controlli");
			
			//model.getIdTipoSoggetto().equals("PFI") ||
			if(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PG") ||
					model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
				
				if(null==model.getDettaglioSoggetto().getNaturaGiuridicaSoggetto().getSoggettoTipoCode() || 
				   StringUtils.isEmpty(model.getDettaglioSoggetto().getNaturaGiuridicaSoggetto().getSoggettoTipoCode())){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Natura Giuridica"));
				}
				
				// trim degli spazi
				model.getDettaglioSoggetto().setDenominazione(model.getDettaglioSoggetto().getDenominazione().trim());
				
				if(StringUtils.isEmpty(model.getDettaglioSoggetto().getDenominazione())){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Ragione Sociale"));
				}
				
				
				if ((model.getDettaglioSoggetto().getDenominazione() + model.getDettaglioSoggetto()
						.getNaturaGiuridicaSoggetto().getSoggettoTipoCode()).length() > 139) // considera spazio concatenazione
					listaErrori
							.add(ErroreCore.VALORE_NON_CONSENTITO
									.getErrore("Ragione Sociale",
											"(La concatenazione di Ragione sociale e Natura giuridica non deve superare i 140 caratteri)"));
				
				
				
			   if( model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PGI")){
			    	
			    	log.debug(methodName, "controllo i dati di persona giuridica PGI");
					if(StringUtils.isEmpty(model.getDettaglioSoggetto().getPartitaIva())){
							listaErrori.add(ErroreFin.PARTITA_IVA_OBBLIGATORIO.getErrore("Partita IVA"));
					}else if(!VerificaPartitaIva.controllaPIVA(model.getDettaglioSoggetto().getPartitaIva()).equalsIgnoreCase("OK")){
						  
					       log.debug(methodName, "errore nella partita iva ");
					       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore());
					  
				     }
			    }else if( model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PG")){
				
					log.debug(methodName, "controllo i dati di persona giuridica PG ");
					
					
					if(StringUtils.isNotEmpty(model.getDettaglioSoggetto().getPartitaIva()) && 
						 !VerificaPartitaIva.controllaPIVA(model.getDettaglioSoggetto().getPartitaIva()).equalsIgnoreCase("OK")){
						 log.debug(methodName, "errore nella partita iva ");
					       listaErrori.add(ErroreFin.PARTITA_IVA_ERRATO.getErrore());
							
					 }      
					   	
			     }

				if(model.getDettaglioSoggetto().isResidenteEstero()){
						
						if(StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceFiscaleEstero())){
							listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Fiscale Estero"));
						}
						
				}else if(StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceFiscale())){
					listaErrori.add(ErroreFin.CODICE_FISCALE_OBBLIGATORIO_PG.getErrore("Codice fiscale"));
			    }	
				
			}
			
			if(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PF") ||
			   model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals("PFI")){
				log.debug(methodName, "controllo i dati di persona fisica");
				
				
				// trim degli spazi
				model.getDettaglioSoggetto().setCognome(model.getDettaglioSoggetto().getCognome().trim());
				model.getDettaglioSoggetto().setNome(model.getDettaglioSoggetto().getNome().trim());
				
				
				checkNonVuoto(listaErrori, model.getDettaglioSoggetto().getCognome(), "Cognome");
				checkNonVuoto(listaErrori, model.getDettaglioSoggetto().getNome(), "Nome");
				checkNonVuoto(listaErrori, dataNascitaStringa, "Data di nascita");
				
				if (WebAppConstants.CODICE_ITALIA.equals(model.getDettaglioSoggetto().getComuneNascita().getNazioneCode()))
					checkNonVuoto(listaErrori, model.getDettaglioSoggetto().getComuneNascita().getDescrizione(), "Comune");
				
				// verifico il codice fiscale estero
				if(model.getDettaglioSoggetto().isResidenteEstero()){
					
					if(StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceFiscaleEstero())){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Fiscale Estero"));
					}
				}
				
				if ((model.getDettaglioSoggetto().getCognome() + model.getDettaglioSoggetto().getNome()).length() > 139) // considera spazio concatenazione
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Cognome, Nome",
							"(La concatenazione di Cognome e Nome non deve superare i 140 caratteri)"));
			}
			
			if(StringUtils.isEmpty(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode())){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Soggetto"));
			}
		}
		
		
		
		/**
		 * Validazione per il cdu Ricerca Soggetto con WIZARD
		 * @param listaErrori
		 * @param listaWarnings 
		 * @param model
		 */
		public static void validaRicercaSoggetto(List<Errore> listaErrori, List<String> listaWarnings, RicercaElencoSoggettoModel model) {
			//Se viene inserito il campo codice, viene effettuata una ricerca per chiave
			//altrimenti, si controlla la validita' dei campi inseriti per effettuare la ricerca
			if (model.getCodice() == null) {
				if (model.getIdNaturaGiuridica() != null && !RICERCA_VUOTA.equals(model.getIdNaturaGiuridica())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else if (model.getSesso() != null && !"".equalsIgnoreCase(model.getSesso())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else if (model.getProvinciaNascita() != null && !"".equalsIgnoreCase(model.getProvinciaNascita())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				}
				//Controlli di validita' sintattica della partita iva e del codice fiscale
				if (model.getCodiceFiscale() != null && !"".equalsIgnoreCase(model.getCodiceFiscale())) {
					CFGenerator cfGen = new CFGenerator();
					if (!(cfGen.verificaFormaleCodiceFiscale(model.getCodiceFiscale()) || cfGen.verificaFormaleCodiceFiscaleNumerico(model.getCodiceFiscale().trim()))) {
						listaWarnings.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Codice Fiscale", "codice fiscale").getTesto());
					}
				}
				if (model.getPartitaIva() != null && !"".equalsIgnoreCase(model.getPartitaIva())) {
					//:Controllo solo la lunghezza.. serve altro?
					if (model.getPartitaIva().length() < 11) {
						listaWarnings.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Partita Iva", "Numerico").getTesto());
					}
				}
			} 
		}

		/**
		 * Metodo di validazione per il campo Denominazione per la Ricerca Soggetto
		 * @param listaErrori
		 * @param model
		 */
		private static void validaRicercaSoggettoDenominazione(List<Errore> listaErrori, RicercaElencoSoggettoModel model) {
			if (model.getDenominazione() != null && !"".equalsIgnoreCase(model.getDenominazione())) {
				String[] supportControllo = model.getDenominazione().split("%");
				if (supportControllo != null && supportControllo.length > 0) {
					for (String supportString : supportControllo) {
						if (!"".equalsIgnoreCase(supportString) && supportString.length() < 3) {
							listaErrori.add(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("LA DENOMINAZIONE DEVE ESSERE LUNGA ALMENO 3 CARATTERI (% ESCLUSO)"));
							break;
						}
					}
				}
			} 
		}
		
		
		
		/**
		 * Validazione per il cdu Ricerca Soggetto e' duplicato e ricavato da quelle con WIZARD
		 * @param listaErrori
		 * @param model
		 */
		public static void validaRicercaSoggetto(List<Errore> listaErrori, RicercaSoggettoModel model) {
			//Se viene inserito il campo codice, viene effettuata una ricerca per chiave
			//altrimenti, si controlla la validita' dei campi inseriti per effettuare la ricerca
			if (model.getCodice() == null) {
				if (model.getIdNaturaGiuridica() != null && !RICERCA_VUOTA.equals(model.getIdNaturaGiuridica())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else if (model.getSesso() != null && !"".equalsIgnoreCase(model.getSesso())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else if (model.getProvinciaNascita() != null && !"".equalsIgnoreCase(model.getProvinciaNascita())) {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				} else {
					validaRicercaSoggettoDenominazione(listaErrori, model);
				}
				//Controlli di validita' sintattica della partita iva e del codice fiscale
				if (model.getCodiceFiscale() != null && !"".equalsIgnoreCase(model.getCodiceFiscale())) {
					CFGenerator cfGen = new CFGenerator();
					if (!cfGen.verificaFormaleCodiceFiscale(model.getCodiceFiscale())) {
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Codice Fiscale"));
					}
				}
				if (model.getPartitaIva() != null && !"".equalsIgnoreCase(model.getPartitaIva())) {
					//:Controllo solo la lunghezza.. serve altro?
					if (model.getPartitaIva().length() < 11) {
						listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Partita Iva"));
					}
				}
			} 
		}

		/**
		 * Metodo di validazione per il campo Denominazione per la Ricerca Soggetto
		 * @param listaErrori
		 * @param model
		 */
		private static void validaRicercaSoggettoDenominazione(List<Errore> listaErrori, RicercaSoggettoModel model) {
			if (model.getDenominazione() != null && !"".equalsIgnoreCase(model.getDenominazione())) {
				String[] supportControllo = model.getDenominazione().split("%");
				if (supportControllo != null && supportControllo.length > 0) {
					for (String supportString : supportControllo) {
						if (!"".equalsIgnoreCase(supportString) && supportString.length() < 3) {
							listaErrori.add(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Denominazione"));
							break;
						}
					}
				}
			} 
		}
		
		/**
		 * Per gli scenari di salva impegno e accertamento pluriennali
		 * @param currentImpegnoPluriennale
		 * @return
		 */
		public static List<Errore> validaDataScadenzaPluriennale(ImpegniPluriennaliModel currentImpegnoPluriennale){
			
			List<Errore> listaErrori = new ArrayList<Errore>();
			
			//converto l'importo:
			BigDecimal importoBD=FinActionUtils.convertiImportiPluriennaleString(currentImpegnoPluriennale);
			
			if (!StringUtils.isEmpty(currentImpegnoPluriennale.getImportoImpPluriennaleString()) && importoBD.compareTo(BigDecimal.ZERO)==1) {
				if (!StringUtils.isEmpty(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString())) {
					
					DateFormat df=null;
					// controllo corretta formattazione data
					if (currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString().length()==8) {
						df = new SimpleDateFormat("dd/MM/yy");
					} else {
						df = new SimpleDateFormat("dd/MM/yyyy");
					}
					
					Calendar cal  = Calendar.getInstance();

					try {
						Date parsedTime=df.parse(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString());
						cal.setTime(parsedTime);
						//MARCO - Impostare l'ora a 23.59.59
						currentImpegnoPluriennale.setDataScadenzaImpPluriennale(parsedTime);
					} catch (ParseException e) {
						listaErrori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA.getErrore(""));
					}
					
					if (cal.get(Calendar.YEAR) != currentImpegnoPluriennale.getAnnoImpPluriennale()) {
							listaErrori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA.getErrore(""));
					}
				} else {
					listaErrori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA.getErrore(""));
				}
			}
			return listaErrori;
		}
		
	}
