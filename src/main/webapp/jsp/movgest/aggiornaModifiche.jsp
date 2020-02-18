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
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
 <!--  <h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} - ${model.movimentoSpesaModel.impegno.importoAttuale}</h3>	-->

		<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
	      
      <s:form id="mainForm" method="post" action="aggiornaMovSpesa.do" cssClass="form-horizontal">
      	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
		<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
        <s:if test="hasActionErrors()">
		<%-- Messaggio di ERROR --%>
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		<%-- Messaggio di WARNING --%>
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>  
		
		<s:if test="hasActionWarnings()">
			<%-- Messaggio di WARNING --%>
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				   <s:iterator value="actionWarnings">
				       <s:property/><br>
				   </s:iterator>
					
				</ul>
			</div>
		</s:if>   
        <h4>Aggiornamento modifica</h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           
                   <h4>Modifica ${model.movimentoSpesaModel.modificaAggiornamento.numeroModificaMovimentoGestione}
                   <s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno != null}">
                    	- SubImpegno - ${model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno}
                    </s:if>
                    <s:else>
                    	- Impegno
                    </s:else>
                    </h4>
                  <fieldset class="form-horizontal ">                    
                    <div class="control-group">              
                      <div class="controls">              
                        <dl class="dl-horizontal">
										
										<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld == null}">
                       						<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione != null}">
				                       			<dt>Soggetto attuale</dt>
				                          		<dd>
				                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.codiceSoggetto} - 
				                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.denominazione} - 
				                          		CF: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.codiceFiscale} - 
				                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.partitaIva}
				                          		</dd> 
				                       		</s:if>
				                       		
				                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione != null}">
				                       			<dt>Classe attuale</dt>
				                          		<dd>
				                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione.codice} - 
				                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione.descrizione}
				                          		</dd> 
				                       		</s:if>
				                        				                        	
				                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione != null && model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione != null}">
				                       			<!-- casistica C->S -->
				                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione == null}">
					                       			<dt>Soggetto precedente</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceSoggetto} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.denominazione} - 
					                          		CF: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceFiscale} - 
					                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.partitaIva}
					                          		</dd>
					                          	</s:if>
					                          	<!-- casistica S->C -->
					                          	<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione != null}">
					                          		<dt>Classe precedente</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.codice} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.descrizione}
					                          		</dd> 
					                          	</s:if>	 
				                       		</s:if>
				                       		<s:else>
				                       			<!-- casistica S->S -->
				                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione != null}">
				                       				<dt>Soggetto precedente</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceSoggetto} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.denominazione} - 
					                          		CF: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceFiscale} - 
					                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.partitaIva}
					                          		</dd>
				                       			</s:if>
				                       			<s:else>
				                       				<dt>Classe precedente</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.codice} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.descrizione}
					                          		</dd> 
				                       			</s:else>
				                       		</s:else>
				            			</s:if>
				                       						                        		
                       		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld != null}">
                       		    <dt>Importo</dt>
                          		<dd><s:property value="getText('struts.money.format', {movimentoSpesaModel.modificaAggiornamento.importoOld})" /></dd>
                          		<%-- <dd>${importoNewFormatted}</dd> --%>
                          		
                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld lt 0}">
<%-- 	                          		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno == null}"> --%>
	                          			<div class="control-group">    
					                      <label class="control-label" for="reimputazione">Reimputazione </label>
					                      <div class="controls">
										      <s:radio id="radioReimputato" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" list="model.movimentoSpesaModel.daReimputazione"></s:radio> 
										      <span class="riaccVisible" id="bloccoReimputato">
										      &nbsp; Anno reimputazione:
										      &nbsp; <s:textfield id="annoReimp" name="model.movimentoSpesaModel.annoReimputazione" onkeyup="return checkItNumbersOnly(event)" cssClass="span1 soloNumeri" />&nbsp;
									         </span>
					                      </div>
					                   </div> 
<%-- 	                          		</s:if> --%>
                          		</s:if>
                          		
                          		
                       		</s:if>
                       		
                       		
                       		
                       		
                       		
                        </dl>             
                      </div>
                    </div>       
                    <div class="control-group">
                      <label class="control-label" for="Motivo">Modifica motivo *</label>
                      <div class="controls">
                         <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivo" name="motivo" headerKey="" 
          		   				headerValue="" listKey="codice" listValue="descrizione"  cssClass="span9"/>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica  descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
                      <div class="controls">
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" />  
                      </div>
                    </div>  
                  </fieldset>
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
<%--                   <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" />  --%>
					<s:include value="/jsp/movgest/include/provvedimento.jsp" />
                  
                  <s:include value="/jsp/movgest/include/modal.jsp" /> 
                                   
            <br/> <br/>                                         
            <p><s:include value="/jsp/include/indietro.jsp" /> 
               <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit>
               <span class="pull-right"><s:submit method="salva" value="Salva" cssClass="btn btn-primary" ></s:submit>
            </span> </p>
          </div>
        </div>
        
      </s:form>
    </div>
  </div>	 
</div>	

	<script type="text/javascript" src="${jspath}movgest/aggiornaModifiche.js"></script>

<s:include value="/jsp/include/footer.jsp" />