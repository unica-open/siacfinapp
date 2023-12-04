<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

   <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
    
  
<style>
.chosen-drop {
	width: 150% !important;
	font-size: 11px;
}

.chosen-container {
	width: 250px !important;
}  

</style>   
    
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
<div class="container-fluid-banner">




<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage"> 
			<%-- SIAC-7952 rimuovo .do dalla action --%>
			<s:form id="mainForm" method="post" action="elencoProvvisorioCassa">  

			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<h3>Elenco provvisori cassa</h3>		
			<s:if test="resultSize!=0">
				<h4><span class="num_result"><s:property value="resultSize"/> </span> Risultati trovati</h4>
			</s:if>
					
			<h3>Risultati ricerca provvisori cassa</h3>

			<display:table name="elencoProvvisoriCassa" 
			class="table table-hover tab_left" 
			                 summary="riepilogo provvisori cassa"
							 keepStatus="true"
							 clearStatus="false"
			                 pagesize="10"
			                 partialList="true" size="resultSize"
			                 requestURI="elencoProvvisorioCassa.do"
							 uid="ricercaProvvisorioCassaID">
    
				<display:column title="Tipo" property="descTipoProvvisorioDiCassa" /> 	
				<display:column title="Num." property="numero" /> 						 		
				<display:column title="Conto evidenza">
					<s:if test="%{#attr.ricercaProvvisorioCassaID.codiceContoEvidenza != null }" >
			 			<s:property value="%{#attr.ricercaProvvisorioCassaID.codiceContoEvidenza + ' - ' + #attr.ricercaProvvisorioCassaID.descrizioneContoEvidenza}" />	 			 					
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</display:column> 						 		
		 		<display:column title="Data emissione">
			 		<s:property value="%{#attr.ricercaProvvisorioCassaID.dataEmissione}" />
			 	</display:column>	 			 					
				<display:column title="Data annullamento">			 
			 		<s:property value="%{#attr.ricercaProvvisorioCassaID.dataAnnullamento}" />
				</display:column>			 
				<display:column title="Descrizione Versante" property="denominazioneSoggetto"/>
				<display:column title="Descrizione Causale">
				 	<a href="#" data-trigger="hover" rel="popover" title="Descrizione sub causale" data-content="<s:property value="%{#attr.ricercaProvvisorioCassaID.subCausale}"/>">
				 		<s:property value="%{#attr.ricercaProvvisorioCassaID.causale}"/>
				 	</a>			 	
				 </display:column>		
				 <display:column title="Accettato">
				 	<s:if test="%{#attr.ricercaProvvisorioCassaID.accettato}">S&igrave;</s:if>
				 	<s:elseif test="%{#attr.ricercaProvvisorioCassaID.accettato != null}">No</s:elseif>
				 	<s:elseif test="%{#attr.ricercaProvvisorioCassaID.accettato == null}">&nbsp;</s:elseif>
			 	</display:column>	 			 					
				 
				<display:column title="Data accettazione / rifiuto">
			 		<s:property value="%{#attr.ricercaProvvisorioCassaID.dataPresaInCaricoServizio}" />
			 		<s:property value="%{#attr.ricercaProvvisorioCassaID.dataRifiutoErrataAttribuzione}" />
			 	</display:column>	 			 					
				<display:column class="sac" title="Struttura amministrativo contabile"> 
					<s:if test="utenteAmministratore">
			        	<s:select 
				        	headerKey="0" headerValue="" 
				        	listKey="uid" listValue="%{codiceCompleto + ' ' + descrizione}" 
				        	list="elencoStruttureAmministrativoContabili"  
		 		        	name="provvisoriSacSel[%{#attr.ricercaProvvisorioCassaID_rowNum - 1}].strutturaAmministrativoContabile.uid"   /> 
				       	
				       	<s:hidden name="provvisoriSacSel[%{#attr.ricercaProvvisorioCassaID_rowNum - 1}].uid" />
					</s:if>
					<s:else>
						<s:property value="%{descrSacSel[#attr.ricercaProvvisorioCassaID_rowNum - 1]}" />
					</s:else>
					                
			    </display:column>					 		
				<display:column title="Importo" property="importo"  
		                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
				<display:column title="Da Regolarizzare" property="importoDaRegolarizzare"  
		                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
				<display:column title="Da Emettere" property="importoDaEmettere"  
		                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
	        
		        <display:column title="" class="tab_Right">
			    	<div class="btn-group">
			    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
			    		<ul class="dropdown-menu pull-right" id="ul_action">
			    			
		    			<%-- task-131<s:url id="aggiornaUrl" action="aggiornaProvvisorioCassa.do" escapeAmp="false"> --%>
			        	<s:url var="aggiornaUrl" action="aggiornaProvvisorioCassa" escapeAmp="false">
			        		<s:param name="numeroProvv" value="%{#attr.ricercaProvvisorioCassaID.numero}" />
			        		<s:param name="annoProvv" value="%{#attr.ricercaProvvisorioCassaID.anno}" />
			        		<s:param name="tipoProvv" value="%{#attr.ricercaProvvisorioCassaID.tipoProvvisorioDiCassa}" />
	                    </s:url>
	                    
	                    <%-- task-131 action="consultaProvvisorioCassa.do" --%>
	                    <s:url var="consultaUrl" action="consultaProvvisorioCassa" escapeAmp="false">
			        		<s:param name="numeroProvv" value="%{#attr.ricercaProvvisorioCassaID.numero}" />
			        		<s:param name="annoProvv" value="%{#attr.ricercaProvvisorioCassaID.anno}" />
			        		<s:param name="tipoProvv" value="%{#attr.ricercaProvvisorioCassaID.tipoProvvisorioDiCassa}" />
	                    </s:url>
	                    
 	                    <s:if test="!utenteLettore and !(utenteDecentrato and not provvisoriConSacUtente[#attr.ricercaProvvisorioCassaID_rowNum - 1])"> 
	   						<li><a href="<s:property value="aggiornaUrl"/>">
								aggiorna
							</a></li>
						</s:if>
					
						<li><a href="<s:property value="consultaUrl"/>">
							consulta
						</a></li>               
		    		</ul>
				</div>
		 	</display:column>            
		</display:table>
		
		<s:set var="pageNumParameter" value="%{new org.displaytag.util.ParamEncoder('ricercaProvvisorioCassaID').encodeParameterName(@org.displaytag.tags.TableTagParameters@PARAMETER_PAGE)}" />        
    					
		<s:hidden name="parameterPageName" value="%{#pageNumParameter}"  />
		<s:hidden name="parameterPageValue" value="%{#parameters[#pageNumParameter]}"  />
        
        <p class="marginLarge"> 
  	   		<s:include value="/jsp/include/indietro.jsp" />
  	   		<s:if test="utenteAmministratore">
  	   			<s:if test="not elencoProvvisoriCassa.empty">
  	   		 		<!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="aggiornaSac" id="aggiorna-sac" value="aggiorna SAC" /> -->
  	   		 		<s:submit cssClass="btn btn-primary pull-right" action="elencoProvvisorioCassa_aggiornaSac" id="aggiorna-sac" value="aggiorna SAC" />
  	   			</s:if>  
  	   		</s:if>  
  	   		
  		</p>  	  
  		                               
      </s:form>
      </div>	
    </div>	
  </div>	 


<s:include value="/jsp/include/footer.jsp" />

<script type="text/javascript" src="${jspath}provvisorio/elencoProvvisorioCassa.js" charset="utf-8"></script>

