<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<div id="refreshtable">	


	<s:if test="isnSubImpegno || hasImpegnoSelezionatoPopup">
		Accertamento <s:property value="accertamentoPopup.annoMovimento"/>/<s:property value="accertamentoPopup.numero.intValue()"/> - 
		<s:if test="accertamentoPopup.descrizione!=null && accertamentoPopup.descrizione!=''">
			<s:property value="accertamentoPopup.descrizione"/> -
		</s:if> 
		<s:property value="getText('struts.money.format', {accertamentoPopup.importoAttuale})"/> (${accertamentoPopup.descrizioneStatoOperativoMovimentoGestioneEntrata} dal 
		<s:property value="%{accertamentoPopup.dataStatoOperativoMovimentoGestioneEntrata}" />)
	</s:if>

	<s:if test="isnSubImpegno">	
	
	
		<table class="table table-hover tab_left">
	      <thead>
	        <tr>
			  <th scope="col">Capitolo</th>		  
	          <th scope="col">Provvedimento</th>
	          <th scope="col">Soggetto</th>
	          <th scope="col">Importo</th>
	          <th scope="col">Disponibile</th>
	        </tr>
	      </thead>
	      <tbody>
	        <tr>   
	          <td>
	          	<s:property value="capitoloEntrataPopup.annoCapitolo"/><s:if test="%{capitoloEntrataPopup.annoCapitolo != null && capitoloEntrataPopup.annoCapitolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroCapitolo"/><s:if test="%{capitoloEntrataPopup.numeroCapitolo != null && capitoloEntrataPopup.numeroCapitolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroArticolo"/><s:if test="%{capitoloEntrataPopup.numeroArticolo != null && capitoloEntrataPopup.numeroArticolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroUEB"/><s:if test="%{capitoloEntrataPopup.numeroUEB != null && capitoloEntrataPopup.numeroUEB != ''}"> - </s:if><s:property value="capitoloEntrataPopup.descrizione"/><s:if test="%{capitoloEntrataPopup.descrizione != null && capitoloEntrataPopup.descrizione != ''}"> - </s:if><s:property value="capitoloEntrataPopup.strutturaAmministrativoContabile.codice"/><s:if test="%{capitoloEntrataPopup.strutturaAmministrativoContabile.codice != null && capitoloEntrataPopup.strutturaAmministrativoContabile.codice != ''}"><s:if test="%{capitoloEntrataPopup.tipoFinanziamento.descrizione != null && capitoloEntrataPopup.tipoFinanziamento.descrizione != ''}"> - </s:if>tipo Finanziamento: <s:property value="capitoloEntrataPopup.tipoFinanziamento.descrizione"/></s:if>
	          </td>    
	          <td>
				<s:property value="provvedimentoPopup.annoProvvedimento"/><s:if test="%{provvedimentoPopup.annoProvvedimento != null && provvedimentoPopup.annoProvvedimento != ''}">/</s:if><s:property value="provvedimentoPopup.numeroProvvedimento"/><s:if test="%{provvedimentoPopup.numeroProvvedimento != null && provvedimentoPopup.numeroProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.tipoProvvedimento"/><s:if test="%{provvedimentoPopup.tipoProvvedimento != null && provvedimentoPopup.tipoProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.oggetto"/><s:if test="%{provvedimentoPopup.oggetto != null && provvedimentoPopup.oggetto != ''}"> - </s:if><s:property value="provvedimentoPopup.strutturaAmministrativa"/><s:if test="%{provvedimentoPopup.strutturaAmministrativa != null && provvedimentoPopup.strutturaAmministrativa != ''}"> - </s:if><s:if test="%{provvedimentoPopup.stato != null && provvedimentoPopup.stato != ''}"> Stato: </s:if><s:property value="provvedimentoPopup.stato"/>
			  </td>
	          <td>
	           	<s:property value="soggettoPopup.codCreditore"/><s:if test="%{soggettoPopup.codCreditore != null && soggettoPopup.codCreditore != ''}"> - </s:if><s:property value="soggettoPopup.denominazione"/><s:if test="%{soggettoPopup.denominazione != null && soggettoPopup.denominazione != ''}"> - </s:if><s:if test="%{soggettoPopup.codfisc != null && soggettoPopup.codfisc != ''}">CF: <s:property value="soggettoPopup.codfisc"/></s:if><s:if test="%{soggettoPopup.piva != null && soggettoPopup.piva != ''}"> - PIVA : <s:property value="soggettoPopup.piva"/> </s:if><s:if test="%{soggettoPopup.classe != null && soggettoPopup.classe != ''}"> - Classe: <s:property value="soggettoPopup.classe"/> </s:if>
			  </td>
	          <td>
	          	<s:property value="accertamentoPopup.importoAttuale"/> (iniziale: <s:property value="accertamentoPopup.importoIniziale"/>)
			  </td>
	          <td>
	          	 <s:property value="accertamentoPopup.disponibilitaLiquidare"/>
	          </td>
	        </tr>
	      </tbody>
	      <tfoot>
	      </tfoot>    
	    </table>
		  
		<div style="width: 100%; height: 200px; overflow-y: scroll;">  
			<display:table name="listaAccertamentiCompGuidata" class="table table-hover tab_left" summary="lista impegni" uid="impegnotab" >
			       <display:column>
			      		<s:radio id="checkImpegno" list="%{#attr.impegnotab.uid}" name="radioImpegnoSelezionato" theme="displaytag"></s:radio>
			       </display:column>
			 	<display:column title="Numero SubImpegno" property="numero" />
			 	<display:column title="Descrizione" property="descrizione"/>
			 	<display:column title="Soggetto" property="soggetto.denominazione" />
			 	<display:column class="tab_Right" title="Importo" property="importoAttuale" />
			 	<display:column class="tab_Right" title="Disponibilit&agrave; a incassare" property="disponibilitaIncassare" />
			</display:table>
		</div>
		
	</s:if>
	<s:else>	
	
		<s:if test="hasImpegnoSelezionatoPopup">
			<table class="table table-hover tab_left">
		      <thead>
		        <tr>
				  <th scope="col">Capitolo</th>		  
		          <th scope="col">Provvedimento</th>
		          <th scope="col">Soggetto</th>
		          <th scope="col">Importo</th>
		          <th scope="col">Disponibile</th>
		        </tr>
		      </thead>
		      <tbody>
		        <tr>   
		          <td>
		          	<s:property value="capitoloEntrataPopup.annoCapitolo"/><s:if test="%{capitoloEntrataPopup.annoCapitolo != null && capitoloEntrataPopup.annoCapitolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroCapitolo"/><s:if test="%{capitoloEntrataPopup.numeroCapitolo != null && capitoloEntrataPopup.numeroCapitolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroArticolo"/><s:if test="%{capitoloEntrataPopup.numeroArticolo != null && capitoloEntrataPopup.numeroArticolo != ''}">/</s:if><s:property value="capitoloEntrataPopup.numeroUEB"/><s:if test="%{capitoloEntrataPopup.numeroUEB != null && capitoloEntrataPopup.numeroUEB != ''}"> - </s:if><s:property value="capitoloEntrataPopup.descrizione"/><s:if test="%{capitoloEntrataPopup.descrizione != null && capitoloEntrataPopup.descrizione != ''}"> - </s:if><s:property value="capitoloEntrataPopup.strutturaAmministrativoContabile.codice"/><s:if test="%{capitoloEntrataPopup.strutturaAmministrativoContabile.codice != null && capitoloEntrataPopup.strutturaAmministrativoContabile.codice != ''}"><s:if test="%{capitoloEntrataPopup.tipoFinanziamento.descrizione != null && capitoloEntrataPopup.tipoFinanziamento.descrizione != ''}"> - </s:if>tipo Finanziamento: <s:property value="capitoloEntrataPopup.tipoFinanziamento.descrizione"/></s:if>
		          </td>    
		          <td>
					<s:property value="provvedimentoPopup.annoProvvedimento"/><s:if test="%{provvedimentoPopup.annoProvvedimento != null && provvedimentoPopup.annoProvvedimento != ''}">/</s:if><s:property value="provvedimentoPopup.numeroProvvedimento"/><s:if test="%{provvedimentoPopup.numeroProvvedimento != null && provvedimentoPopup.numeroProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.tipoProvvedimento"/><s:if test="%{provvedimentoPopup.tipoProvvedimento != null && provvedimentoPopup.tipoProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.oggetto"/><s:if test="%{provvedimentoPopup.oggetto != null && provvedimentoPopup.oggetto != ''}"> - </s:if><s:property value="provvedimentoPopup.strutturaAmministrativa"/><s:if test="%{provvedimentoPopup.strutturaAmministrativa != null && provvedimentoPopup.strutturaAmministrativa != ''}"> - </s:if><s:if test="%{provvedimentoPopup.stato != null && provvedimentoPopup.stato != ''}"> Stato: </s:if><s:property value="provvedimentoPopup.stato"/>
				  </td>
		          <td>
		           	<s:property value="soggettoPopup.codCreditore"/><s:if test="%{soggettoPopup.codCreditore != null && soggettoPopup.codCreditore != ''}"> - </s:if><s:property value="soggettoPopup.denominazione"/><s:if test="%{soggettoPopup.denominazione != null && soggettoPopup.denominazione != ''}"> - </s:if><s:if test="%{soggettoPopup.codfisc != null && soggettoPopup.codfisc != ''}">CF: <s:property value="soggettoPopup.codfisc"/></s:if><s:if test="%{soggettoPopup.piva != null && soggettoPopup.piva != ''}"> - PIVA : <s:property value="soggettoPopup.piva"/> </s:if><s:if test="%{soggettoPopup.classe != null && soggettoPopup.classe != ''}"> - Classe: <s:property value="soggettoPopup.classe"/> </s:if>
				  </td>
		          <td>
		          	<s:property value="accertamentoPopup.importoAttuale"/> (iniziale: <s:property value="accertamentoPopup.importoIniziale"/>)
				  </td>
		          <td>
		          	 <s:property value="accertamentoPopup.disponibilitaIncassare"/>
		          </td>
		        </tr>
		      </tbody>
		      <tfoot>
		      </tfoot>    
		    </table>
		</s:if>		
	 
	 
	</s:else>		
</div>
 
 <script type="text/javascript">	
		
 </script>