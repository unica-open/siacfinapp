<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="refreshtable">	
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
	          	<s:property value="capitoloPopup.annoCapitolo"/><s:if test="%{capitoloPopup.annoCapitolo != null && capitoloPopup.annoCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroCapitolo"/><s:if test="%{capitoloPopup.numeroCapitolo != null && capitoloPopup.numeroCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroArticolo"/><s:if test="%{capitoloPopup.numeroArticolo != null && capitoloPopup.numeroArticolo != ''}">/</s:if><s:property value="capitoloPopup.numeroUEB"/><s:if test="%{capitoloPopup.numeroUEB != null && capitoloPopup.numeroUEB != ''}"> - </s:if><s:property value="capitoloPopup.descrizione"/><s:if test="%{capitoloPopup.descrizione != null && capitoloPopup.descrizione != ''}"> - </s:if><s:property value="capitoloPopup.strutturaAmministrativoContabile.codice"/><s:if test="%{capitoloPopup.strutturaAmministrativoContabile.codice != null && capitoloPopup.strutturaAmministrativoContabile.codice != ''}"><s:if test="%{capitoloPopup.tipoFinanziamento.descrizione != null && capitoloPopup.tipoFinanziamento.descrizione != ''}"> - </s:if>tipo Finanziamento: <s:property value="capitoloPopup.tipoFinanziamento.descrizione"/></s:if>
	          </td>    
	          <td>
				<s:property value="provvedimentoPopup.annoProvvedimento"/><s:if test="%{provvedimentoPopup.annoProvvedimento != null && provvedimentoPopup.annoProvvedimento != ''}">/</s:if><s:property value="provvedimentoPopup.numeroProvvedimento"/><s:if test="%{provvedimentoPopup.numeroProvvedimento != null && provvedimentoPopup.numeroProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.tipoProvvedimento"/><s:if test="%{provvedimentoPopup.tipoProvvedimento != null && provvedimentoPopup.tipoProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.oggetto"/><s:if test="%{provvedimentoPopup.oggetto != null && provvedimentoPopup.oggetto != ''}"> - </s:if><s:property value="provvedimentoPopup.strutturaAmministrativa"/><s:if test="%{provvedimentoPopup.strutturaAmministrativa != null && provvedimentoPopup.strutturaAmministrativa != ''}"> - </s:if><s:if test="%{provvedimentoPopup.stato != null && provvedimentoPopup.stato != ''}"> Stato: </s:if><s:property value="provvedimentoPopup.stato"/>
			  </td>
	          <td>
	           	<s:property value="soggettoPopup.codCreditore"/><s:if test="%{soggettoPopup.codCreditore != null && soggettoPopup.codCreditore != ''}"> - </s:if><s:property value="soggettoPopup.denominazione"/><s:if test="%{soggettoPopup.denominazione != null && soggettoPopup.denominazione != ''}"> - </s:if><s:if test="%{soggettoPopup.codfisc != null && soggettoPopup.codfisc != ''}">CF: <s:property value="soggettoPopup.codfisc"/></s:if><s:if test="%{soggettoPopup.piva != null && soggettoPopup.piva != ''}"> - PIVA : <s:property value="soggettoPopup.piva"/> </s:if><s:if test="%{soggettoPopup.classe != null && soggettoPopup.classe != ''}"> - Classe: <s:property value="soggettoPopup.classe"/> </s:if>
			  </td>
	          <td>
	          	<s:property value="impegnoPopup.importoAttuale"/> (iniziale: <s:property value="impegnoPopup.importoIniziale"/>)
			  </td>
	          <td>
	          	 <s:property value="impegnoPopup.disponibilitaLiquidare"/>
	          </td>
	        </tr>
	      </tbody>
	      <tfoot>
	      </tfoot>    
	    </table>
		  
		<div style="width: 100%; height: 200px; overflow-y: scroll;">  
			<display:table name="listaImpegniCompGuidata" class="table table-hover tab_left" summary="lista impegni" uid="impegnotab" >
			       <display:column>
			      		<s:radio id="checkImpegno" list="%{#attr.impegnotab.uid}" name="radioImpegnoSelezionato" theme="displaytag"></s:radio>
			       </display:column>
			 	<display:column title="Numero SubImpegno" property="numero" />
			 	<display:column title="Descrizione" property="descrizione"/>
			 	<display:column title="Soggetto" property="soggetto.denominazione" />
			 	<display:column class="tab_Right" title="Importo" property="importoAttuale" />
			 	<display:column class="tab_Right" title="Disponibilit&agrave; a liquidare" property="disponibilitaLiquidare" />
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
		          	<s:property value="capitoloPopup.annoCapitolo"/><s:if test="%{capitoloPopup.annoCapitolo != null && capitoloPopup.annoCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroCapitolo"/><s:if test="%{capitoloPopup.numeroCapitolo != null && capitoloPopup.numeroCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroArticolo"/><s:if test="%{capitoloPopup.numeroArticolo != null && capitoloPopup.numeroArticolo != ''}">/</s:if><s:property value="capitoloPopup.numeroUEB"/><s:if test="%{capitoloPopup.numeroUEB != null && capitoloPopup.numeroUEB != ''}"> - </s:if><s:property value="capitoloPopup.descrizione"/><s:if test="%{capitoloPopup.descrizione != null && capitoloPopup.descrizione != ''}"> - </s:if><s:property value="capitoloPopup.strutturaAmministrativoContabile.codice"/><s:if test="%{capitoloPopup.strutturaAmministrativoContabile.codice != null && capitoloPopup.strutturaAmministrativoContabile.codice != ''}"><s:if test="%{capitoloPopup.tipoFinanziamento.descrizione != null && capitoloPopup.tipoFinanziamento.descrizione != ''}"> - </s:if>tipo Finanziamento: <s:property value="capitoloPopup.tipoFinanziamento.descrizione"/></s:if>
		          </td>    
		          <td>
					<s:property value="provvedimentoPopup.annoProvvedimento"/><s:if test="%{provvedimentoPopup.annoProvvedimento != null && provvedimentoPopup.annoProvvedimento != ''}">/</s:if><s:property value="provvedimentoPopup.numeroProvvedimento"/><s:if test="%{provvedimentoPopup.numeroProvvedimento != null && provvedimentoPopup.numeroProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.tipoProvvedimento"/><s:if test="%{provvedimentoPopup.tipoProvvedimento != null && provvedimentoPopup.tipoProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.oggetto"/><s:if test="%{provvedimentoPopup.oggetto != null && provvedimentoPopup.oggetto != ''}"> - </s:if><s:property value="provvedimentoPopup.strutturaAmministrativa"/><s:if test="%{provvedimentoPopup.strutturaAmministrativa != null && provvedimentoPopup.strutturaAmministrativa != ''}"> - </s:if><s:if test="%{provvedimentoPopup.stato != null && provvedimentoPopup.stato != ''}"> Stato: </s:if><s:property value="provvedimentoPopup.stato"/>
				  </td>
		          <td>
		           	<s:property value="soggettoPopup.codCreditore"/><s:if test="%{soggettoPopup.codCreditore != null && soggettoPopup.codCreditore != ''}"> - </s:if><s:property value="soggettoPopup.denominazione"/><s:if test="%{soggettoPopup.denominazione != null && soggettoPopup.denominazione != ''}"> - </s:if><s:if test="%{soggettoPopup.codfisc != null && soggettoPopup.codfisc != ''}">CF: <s:property value="soggettoPopup.codfisc"/></s:if><s:if test="%{soggettoPopup.piva != null && soggettoPopup.piva != ''}"> - PIVA : <s:property value="soggettoPopup.piva"/> </s:if><s:if test="%{soggettoPopup.classe != null && soggettoPopup.classe != ''}"> - Classe: <s:property value="soggettoPopup.classe"/> </s:if>
				  </td>
		          <td>
		          	<s:property value="impegnoPopup.importoAttuale"/> (iniziale: <s:property value="impegnoPopup.importoIniziale"/>)
				  </td>
		          <td>
		          	 <s:property value="impegnoPopup.disponibilitaLiquidare"/>
		          </td>
		        </tr>
		      </tbody>
		      <tfoot>
		      </tfoot>    
		    </table>
		</s:if>		
		
		<!-- 
		<div id="schedaImpegno" style="padding-left: 20px;padding-bottom: 15px;">
			<s:if test="hasImpegnoSelezionatoPopup">
				<dt>Capitolo </dt>
				  <dd>
				  <s:property value="capitoloPopup.annoCapitolo"/><s:if test="%{capitoloPopup.annoCapitolo != null && capitoloPopup.annoCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroCapitolo"/><s:if test="%{capitoloPopup.numeroCapitolo != null && capitoloPopup.numeroCapitolo != ''}">/</s:if><s:property value="capitoloPopup.numeroArticolo"/><s:if test="%{capitoloPopup.numeroArticolo != null && capitoloPopup.numeroArticolo != ''}">/</s:if><s:property value="capitoloPopup.numeroUEB"/><s:if test="%{capitoloPopup.numeroUEB != null && capitoloPopup.numeroUEB != ''}"> - </s:if><s:property value="capitoloPopup.descrizione"/><s:if test="%{capitoloPopup.descrizione != null && capitoloPopup.descrizione != ''}"> - </s:if><s:property value="capitoloPopup.strutturaAmministrativoContabile.codice"/><s:if test="%{capitoloPopup.strutturaAmministrativoContabile.codice != null && capitoloPopup.strutturaAmministrativoContabile.codice != ''}"> - </s:if>tipo Finanziamento: <s:property value="capitoloPopup.tipoFinanziamento.codice"/>
				  </dd>
				<dt>Provvedimento</dt>
				<dd>
				  <s:property value="provvedimentoPopup.annoProvvedimento"/><s:if test="%{provvedimentoPopup.annoProvvedimento != null && provvedimentoPopup.annoProvvedimento != ''}">/</s:if><s:property value="provvedimentoPopup.numeroProvvedimento"/><s:if test="%{provvedimentoPopup.numeroProvvedimento != null && provvedimentoPopup.numeroProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.tipoProvvedimento"/><s:if test="%{provvedimentoPopup.tipoProvvedimento != null && provvedimentoPopup.tipoProvvedimento != ''}"> - </s:if><s:property value="provvedimentoPopup.oggetto"/><s:if test="%{provvedimentoPopup.oggetto != null && provvedimentoPopup.oggetto != ''}"> - </s:if><s:property value="provvedimentoPopup.strutturaAmministrativa"/><s:if test="%{provvedimentoPopup.strutturaAmministrativa != null && provvedimentoPopup.strutturaAmministrativa != ''}"> - </s:if><s:if test="%{provvedimentoPopup.stato != null && provvedimentoPopup.stato != ''}"> Stato: </s:if><s:property value="provvedimentoPopup.stato"/>
				</dd>
				<dt>Soggetto</dt>				
				<s:if test="soggettoSelezionatoPopup">
					<dd>
				    <s:property value="soggettoPopup.codCreditore"/><s:if test="%{soggettoPopup.codCreditore != null && soggettoPopup.codCreditore != ''}"> - </s:if><s:property value="soggettoPopup.denominazione"/><s:if test="%{soggettoPopup.denominazione != null && soggettoPopup.denominazione != ''}"> - </s:if>CF: <s:property value="soggettoPopup.codfisc"/><s:if test="%{soggettoPopup.codfisc != null && soggettoPopup.codfisc != ''}"> - </s:if>PIVA : <s:property value="soggettoPopup.piva"/><s:if test="%{soggettoPopup.piva != null && soggettoPopup.piva != ''}"> - </s:if>Classe <s:property value="soggettoPopup.classe"/>
					</dd>
				</s:if>
				<dt>Importo</dt> <dd><s:property value="impegnoPopup.importoAttuale"/> ( iniziale: <s:property value="impegnoPopup.importoIniziale"/> )</dd>
				<dt>Disponibile</dt><dd> <s:property value="impegnoPopup.disponibilitaLiquidare"/></dd>	
			</s:if>	
		</div>	
		 -->
	 
	 
	</s:else>		
</div>
 
 <script type="text/javascript">	
		
 
 </script>