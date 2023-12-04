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
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>
  
  <body>     
  <s:include value="/jsp/include/header.jsp" />    

<div class="container-fluid-banner">



	<a name="A-contenuti" title="A-contenuti"></a>
</div>



<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage"> 
		    <%-- SIAC-7952 rimuovo .do dalla action --%>
			<s:form id="mainForm" method="post" action="gestRigheCarta">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<h3>Carta <s:property value="cartaContabileDaRicerca.numero"/> (stato <s:property value="cartaContabileDaRicerca.statoOperativoCartaContabile" /> dal <s:property value="%{cartaContabileDaRicerca.dataCreazione}" />)</h3>
				
				<!--  valuta estera -->
				
				<s:if test="valutaPerRegolarizzazione!=null">
						<h4>Valuta : <s:property value="valutaPerRegolarizzazione.valuta.codice" />  (<s:property value="valutaPerRegolarizzazione.valuta.descrizione" />)</h4>
				</s:if>
				
				
				<h4 class="step-pane">Importo carta: <s:property value="getText('struts.money.format', {sommaRigheCarta})" /> 
                                      <s:if test="valutaPerRegolarizzazione!=null">
                                       (<s:property value="getText('struts.money.format', {valutaPerRegolarizzazione.totaleValutaEstera})" />)
                                      </s:if>				
				</h4>
				
<!-- 					<p class="clear paddingBottom"> -->
<!-- 							<a class="pull-right">completa riga</a> -->
<!-- 					        <a class="btn btn-primary pull-right">regolarizza (selezionati)</a> -->
<!-- 					</p> -->

                      <span class="pull-right">
                            
                            <s:if test="presenzaRigheSelezionabili">
	                            <a id="linkRegolarizza_<s:property value="ckRigaSelezionata"/>_<s:property value="ckRigaSelezionata"/>" href="#msgRegolarizza" data-toggle="modal" class="btn btn-primary linkRegolarizza"> 
		                         regolarizza (selezionati)</a>
                            </s:if>
                            <s:if test="!presenzaRigheSelezionabili">
	                            <a disabled="true" data-toggle="modal" class="btn btn-primary"> 
		                         regolarizza (selezionati)</a>
                            </s:if>
                            
							&nbsp;
	                        <!-- task-131 <s:submit disabled="!presenzaRigheSelezionabili" name="completaRiga" value="completa riga" method="completaRiga" cssClass="btn btn-primary" /> -->
	                        <s:submit disabled="!presenzaRigheSelezionabili" name="completaRiga" value="completa riga" action="gestRigheCarta_completaRiga" cssClass="btn btn-primary" />
                      </span>

					<h4><span class="num_result"><s:property value="NumeroRighe"/> </span> righe</h4>
				
				    <br/>
				    
					<fieldset class="form-horizontal">
					
					<div id="paolo">
					     <display:table name="cartaContabileDaRicerca.listaPreDocumentiCarta" 
								class="table table-hover tab_left" 
								summary="riepilogo accertamenti" 
								pagesize="10" requestURI="gestRigheCarta.do" 
								uid="righeCartaContID" >
					
					        
							<display:column>
							        <!-- se importo da reg >0 allora posso vedere il checkbox -->
							        <s:if test="%{#attr.righeCartaContID.importoDaRegolarizzare >0}">
							        	<s:checkboxlist id="ckRiga" list="%{#attr.righeCartaContID.numero}"  name="ckRigaSelezionata" theme="displaytag" />
							        </s:if> 
							        <s:else>
							        	<!-- non stampo neanche il checkbox -->
							        </s:else>
				      				
				      			
				       		</display:column>
							
							<display:column title="Numero" property="numero" />
					       
					        <display:column title="Descrizione" property="descrizione" />
					        <display:column title="Conto del tesoriere" property="contoTesoreria.codice" />
					        
					        
					        <display:column title="Impegno">
					                <s:if test="%{#attr.righeCartaContID.subImpegno==null}">
					                	<s:property value="%{#attr.righeCartaContID.impegno.annoMovimento}" />/<s:property value="%{#attr.righeCartaContID.impegno.numero.intValue()}" />
					                </s:if>
					       			<s:else>
					       			    <!-- stampo anche il numero sub impegno -->
					       			    <s:property value="%{#attr.righeCartaContID.impegno.annoMovimento}" />/<s:property value="%{#attr.righeCartaContID.impegno.numero.intValue()}" /> - <s:property value="%{#attr.righeCartaContID.subImpegno.numero.intValue()}" />
					       			</s:else>
					        </display:column>
					       
					        <display:column title="Soggetto">
					       		<s:property value="%{#attr.righeCartaContID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.righeCartaContID.soggetto.denominazione}" />
					        </display:column>
					       
					        <display:column class="tab_Right" title="Importo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
					        <display:column class="tab_Right" title="Da regolarizzare" property="importoDaRegolarizzare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
					
					        <display:footer>
					        		<tr> 
										<th>Totale</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th class="tab_Right"><s:property value="getText('struts.money.format', {sommaRigheCarta})" /></th>
					                    <th>&nbsp;</th>
					                </tr>  
					        </display:footer>
					
					     </display:table>
					</div>
					
					

					</fieldset>

					
					<!--#include virtual="include/modal.html" -->
					<div class="Border_line"></div>
					<p>
<%-- 						<s:include value="/jsp/include/indietro.jsp" /> --%>
						<!-- task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn" /> -->
						<s:submit name="indietro" value="indietro" action="gestRigheCarta_indietro" cssClass="btn" />
						
						<span class="pull-right">
	                         
	                         <s:if test="presenzaRigheSelezionabili">
	                           <a id="linkRegolarizza_<s:property value="ckRigaSelezionata"/>_<s:property value="ckRigaSelezionata"/>" href="#msgRegolarizza" data-toggle="modal" class="btn btn-primary linkRegolarizza"> 
	                         	regolarizza (selezionati)</a>
                            </s:if>
                            <s:if test="!presenzaRigheSelezionabili">
	                            <a disabled="true" data-toggle="modal" class="btn btn-primary"> 
		                         regolarizza (selezionati)</a>
                            </s:if>
	                         
	                         &nbsp;
							<!-- task-131 <s:submit disabled="!presenzaRigheSelezionabili" name="completaRiga" value="completa riga" method="completaRiga" cssClass="btn btn-primary" /> -->
							<s:submit disabled="!presenzaRigheSelezionabili" name="completaRiga" value="completa riga" action="gestRigheCarta_completaRiga" cssClass="btn btn-primary" />
						</span>					
					</p> 
					
					<s:hidden id="numeroDaRegol" name="numeroDaRegolarizzare"/>
			
			
			
				<!-- Modal regolarizza -->
		            <div id="msgRegolarizza" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		              <div class="modal-body">
		                <div class="alert alert-error regolarizzaOK">
		                  <button type="button" class="close" data-dismiss="alert">&times;</button>
		                  
		                  <p><strong>Attenzione!</strong></p>
		                  <p>FIN_CON_267 - Con questa operazione si perderanno eventuali aggiornamenti fatti alla carta.</p>
		                  <p>Per non perderli prima SALVA. Righe selezionate: <s:textfield id="numeroDaRegolarizzare" name="numeroDaRegolarizzarePassato" disabled="true" cssStyle="width:50px;" />&nbsp;</p>
		                  <p>Proseguire con la regolarizzazione ?</p>
		                </div>
		                
		                <div class="alert alert-error regolarizzaKO" style="display:none;">
		                  <button type="button" class="close" data-dismiss="alert">&times;</button>
		                  
		                  <p><strong>Attenzione!</strong></p>
		                  <p> COR_ERR_0015 - Nessun elemento selezionato</p>
		                </div>
		                
		                
		              </div>
		              
		              
		              <div class="modal-footer regolarizzaOK">
		                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		                 <!-- task-131 <s:submit id="submitBtn" name="btnRegolarizza" value="si, prosegui" cssClass="btn btn-primary" method="regolarizzaSelezionati"/> -->
		                 <s:submit id="submitBtn" name="btnRegolarizza" value="si, prosegui" cssClass="btn btn-primary" action="gestRigheCarta_regolarizzaSelezionati"/>
		             </div>
		              
		               <div class="modal-footer regolarizzaKO" style="display:none;">
		                <button class="btn" data-dismiss="modal" aria-hidden="true">indietro</button>
		              </div>
		              
		              
		            </div>  
	            <!--/modale regolarizza -->
			
			
			</s:form>
				
		</div>		  
	</div>	 
</div>	



<s:include value="/jsp/include/footer.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		
		$(".linkRegolarizza").click(function() {

			var selected = [];
			$('#paolo input:checked').each(function() {
			    selected.push($(this).val());
			});
			
			$("#numeroDaRegol").val(selected);
			$("#numeroDaRegolarizzare").val(selected);
			
			if(selected.length==0){
				$(".regolarizzaKO").css('visibility', 'visible');
				$(".regolarizzaKO").css('display', 'block');
				$(".regolarizzaOK").css('display', 'none');
			}else{
				$(".regolarizzaKO").css('display', 'none');
				$(".regolarizzaOK").css('visibility', 'visible');
				$(".regolarizzaOK").css('display', 'block');
			}
		});
	});
</script>
