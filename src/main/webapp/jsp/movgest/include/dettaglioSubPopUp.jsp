<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Sub<s:property value="subDettaglio.descMovimentoLower" />o <s:property value="subDettaglio.numero" /> - <s:property value="subDettaglio.descrizione" /></h4>
</div>
<div class="modal-body">
	<fieldset class="form-horizontal">
		<dl class="dl-horizontal">
			<dt>Inserito</dt>
			<dd>il <s:property value="%{subDettaglio.dataInserimento}" /> da <s:property value="subDettaglio.utenteCreazione" />&nbsp;</dd>
			<dt>Aggiornato</dt>
			<dd>il <s:property value="%{subDettaglio.dataModifica}" /> da <s:property value="subDettaglio.utenteModifica" />&nbsp;</dd>
			<dt>Stato</dt>
			<dd><s:property value="subDettaglio.statoOperativo" /> dal <s:property value="%{subDettaglio.dataStatoOperativo}" />&nbsp;</dd>
			<dt>Dati <s:property value="subDettaglio.descMovimentoLower" />o</dt>
			<dd><s:property value="subDettaglio.descSuper" />&nbsp;</dd>
			
			<s:if test="subDettaglio.impegno">
				<dt>Progetto</dt>
				<dd><s:property value="subDettaglio.progetto" />&nbsp;</dd>
			</s:if>
			
			<s:if test="subDettaglio.soggetto.codice != null">
				<dt>Soggetto</dt>
				<dd>
					<s:property value="subDettaglio.soggetto.codice" /> - <s:property value="subDettaglio.soggetto.denominazione" />
				<s:if test="subDettaglio.soggetto.codiceFiscale != null"> - CF: <s:property value="subDettaglio.soggetto.codiceFiscale" /> </s:if>
				<s:if test="subDettaglio.soggetto.partitaIva != null"> - PIVA: <s:property value="subDettaglio.soggetto.partitaIva" /> </s:if> 
				&nbsp;
				</dd>
			</s:if>
			<s:elseif test="subDettaglio.soggetto.classeSoggettoCodice != null">
				<dt>Classe soggetto</dt>
				<dd><s:property value="subDettaglio.soggetto.classeSoggettoCodice" /> - <s:property value="subDettaglio.soggetto.classeSoggettoDescrizione" />&nbsp;</dd>
			</s:elseif>
		<s:else>
			<dt>Soggetto</dt>
			<dd>&nbsp;</dd>
		</s:else>
			<dt>Provvedimento</dt>
			<dd>
			<s:if test="subDettaglio.provvedimento.tipo != null">
				<s:property value="subDettaglio.provvedimento.anno" /> / <s:property value="subDettaglio.provvedimento.numero" /> - <s:property value="subDettaglio.provvedimento.tipo" /> - <s:property value="subDettaglio.provvedimento.oggetto" /> - <s:property value="subDettaglio.provvedimento.struttura" /> - Stato: <s:property value="subDettaglio.provvedimento.stato" /> - Blocco: <s:property value="subDettaglio.provvedimento.bloccoRagioneria" />&nbsp;
			</s:if>
			&nbsp;
			</dd>
			<dt>Importo</dt>
			<dd><s:property value="getText('struts.money.format', {subDettaglio.importo})" /> (iniziale: <s:property value="getText('struts.money.format', {subDettaglio.importoIniziale})" />)
				<s:if test="subDettaglio.impegno">
					<s:if test="%{dettaglioImportiSubSelezionato.totModProv != null && dettaglioImportiSubSelezionato.totModProv.longValue() > 0 }">
					di cui mod. provv <s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.totModProv})" />
					</s:if>
				</s:if>
				<s:if test="!subDettaglio.impegno">
					<s:if test="%{dettaglioImportiSubAccSelezionato.totModProv != null && dettaglioImportiSubAccSelezionato.totModProv.longValue() > 0 }">
					di cui mod. provv <s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.totModProv})" />
					</s:if>
				</s:if>
			</dd>
			
					<s:if test="subDettaglio.impegno">
			<!-- DATI DEI TOTALI IMPORTI IN CONSULTAZIONE -->
		
							<dt>
								<!-- hack per allineare -->
								<font color="white">-</font>
 							</dt>
							<dd>
									<table>
									      
									      <tr>
									        <th align="left">Tot Carte non regolarizzate.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.carteNonReg.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.carteNonReg.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="left">Tot predoc non liquidati.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.impPredoc.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.impPredoc.importo})" /></td>
									      </tr>
									      
									       <tr>
									        <th align="left">Documenti non liquidati.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.docNonLiq.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.docNonLiq.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="left">Tot pagamenti economali.</th>
									        <td><!-- hack per allineare --><font color="white">-</font></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.totCec})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="left">Totale liquidazioni.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.impLiq.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.impLiq.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="right">Totale movimenti:</th>
									        <td align="left"><!-- hack per allineare --><font color="white">-</font></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubSelezionato.totaleMovimenti})" /></td>
									      </tr>
									      
									      
									      
									    </table>
									    
						  </dd>
						  
			<!-- END DATI DEI TOTALI IMPORTI IN CONSULTAZIONE -->
		</s:if>
		
		<s:if test="!subDettaglio.impegno">
			<!-- DATI DEI TOTALI IMPORTI IN CONSULTAZIONE SUB ACCERTAMENTO-->
		
							<dt>
								<!-- hack per allineare -->
								<font color="white">-</font>
 							</dt>
							<dd>
									<table>
									      
									      <tr>
									        <th align="left">Totale predoc non inc.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.impPredoc.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.impPredoc.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="left">Totale Documenti non inc.</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.docNonInc.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.docNonInc.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="left">Totale Ordinativi (Reversali)</th>
									        <td><s:property value="dettaglioImportiSubSelezionato.impOrd.numero" /></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.impOrd.importo})" /></td>
									      </tr>
									      
									      <tr>
									        <th align="right">Totale movimenti:</th>
									        <td align="left"><!-- hack per allineare --><font color="white">-</font></td>
									        <td><s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.totaleMovimenti})" /></td>
									      </tr>
									      
									      
									      
									    </table>
									    
						  </dd>
						  
			<!-- END DATI DEI TOTALI IMPORTI IN CONSULTAZIONE -->
		</s:if>
			
			
			<s:if test="subDettaglio.impegno">
				<dt data-disponibilita-motivazione-trigger="liquidare (subimpegno)">Disponibile a liquidare</dt>
				<dd>
					<s:property value="getText('struts.money.format', {subDettaglio.disponibilitaLiquidare})" />&nbsp;
					<span class="hide" data-disponibilita-motivazione-data="liquidare (subimpegno)">
						<s:property value="subDettaglio.motivazioneDisponibilitaLiquidare" />
					</span>
				</dd>
				
				<dt data-disponibilita-motivazione-trigger="pagare (subimpegno)">Disponibile a pagare</dt>
				<dd>
					<s:property value="getText('struts.money.format', {subDettaglio.disponibilitaPagare})" />&nbsp;
					<span class="hide" data-disponibilita-motivazione-data="pagare (subimpegno)">
						<s:property value="subDettaglio.motivazioneDisponibilitaPagare" />
					</span>
				</dd>
				
				<dt data-disponibilita-motivazione-trigger="finanziare (subimpegno)">Disponibile a finanziare</dt>
				<dd>
					<s:property value="getText('struts.money.format', {subDettaglio.disponibilitaFinanziare})" />&nbsp;
					<span class="hide" data-disponibilita-motivazione-data="finanziare (subimpegno)">
						<s:property value="subDettaglio.motivazioneDisponibilitaFinanziare" />
					</span>
				</dd>
			</s:if>
								
			<s:if test="!subDettaglio.impegno">
				<dt>Disponibile a incassare (solo Ord)</dt>
				<dd><s:property value="getText('struts.money.format', {dettaglioImportiSubAccSelezionato.disponibileIncassareSoloOrd})" />&nbsp;</dd>
				
				<dt data-disponibilita-motivazione-trigger="incassare (subimpegno)">Disponibilita' Sub Accertamento (con doc)</dt>
				<dd>
					<s:property value="getText('struts.money.format', {subDettaglio.disponibilitaIncassare})" />&nbsp;
					<span class="hide" data-disponibilita-motivazione-data="incassare (subimpegno)">
						<s:property value="subDettaglio.motivazioneDisponibilitaIncassare" />
					</span>
				</dd>
			</s:if>
			
			
			
			<dt>Scadenza</dt>
			<dd><s:property value="%{subDettaglio.dataScadenza}" />&nbsp;</dd>
		
		
		<s:if test="subDettaglio.impegno">
		
			<dt>Tipo debito siope</dt>
			<dd><s:property value="subDettaglio.tipoDebitoSiope" />&nbsp;</dd>
			
			<s:if test="%{subDettaglio.cig != null && subDettaglio.cig != ''}">
				<dt>CIG</dt>
				<dd><s:property value="subDettaglio.cig" />&nbsp;</dd>
			</s:if>
		    <s:else>
				<dt>Motivazione assenza cig</dt>
				<dd><s:property value="subDettaglio.motivazioneAssenzaCig" />&nbsp;</dd>
		    </s:else>
		
			<dt>CUP</dt>
			<dd><s:property value="subDettaglio.cup" />&nbsp;</dd>
			
		</s:if>
			
		
		</dl>							    
									    
	</fieldset>

</div>

<div class="modal-footer">
	<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>
