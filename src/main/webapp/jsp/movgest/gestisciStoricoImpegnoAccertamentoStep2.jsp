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
                    
  <hr />
<div class="container-fluid-banner">


  <a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
    	<s:form id="reintroitoOrdinativoPagamentoStep2" action="gestisciStoricoImpegnoAccertamentoStep2" method="post" cssClass="form-horizontal">
	      	<s:include value="/jsp/include/actionMessagesErrors.jsp" />

			<h3>Storico impegno accertamento</h3> 
			
			<div id="MyWizard" class="wizard">
				<ul class="steps">
					<li data-target="#step1" ><span class="badge">1</span>ricerca impegno<span class="chevron"></span></li>
					<li data-target="#step2" class="active"><span class="badge">2</span>associa accertamento<span class="chevron"></span></li>
				</ul>
			</div>

        	<div class="step-content">
          		<div class="step-pane active" id="step2">
	          		<h3>storico su impegno 
			       		<s:if test="impegno!=null">
				       		<s:property value="%{impegno.annoMovimento +' / ' +  impegno.numero}"/> 
							<s:if test="subImpegno != null "> 
								<s:property value="%{ ' - ' + subImpegno.numero}"/>
							</s:if>
						</s:if>
					</h3>
        	
				<s:include value="/jsp/movgest/include/tabellaStoricoImpegnoAccertamento.jsp" />
				
				<h4>Storico </h4>	      
			    <fieldset class="form-horizontal" id="fieldsetInserisciModificaStorico">
			      	<div class="control-group">
			        	<label class="control-label" for="annoProvvedimentoInserimento">Anno *</label>
			          	<div class="controls"> 
			          		<s:hidden id="idStorico" name="storicoImpegnoAccertamentoInModifica.uid"/>
			            	<s:textfield id="annoAccertamento" cssClass="lbTextSmall span2" name="storicoImpegnoAccertamentoInModifica.accertamento.annoMovimento" onkeyup="return checkItNumbersOnly(event)" maxlength="4" />
				            <span class="al">
				              <label class="radio inline" for="numeroAccertamento">Numero &nbsp;</label>
				            </span>
			            	<s:textfield id="numeroAccertamento" cssClass="lbTextSmall span2" name="storicoImpegnoAccertamentoInModifica.accertamento.numero" 
			            	onkeyup="return checkItNumbersOnly(event) && "  maxlength="6"/>
				            	      
			        		<span class="al">
				              <label class="radio inline" for="numeroSubAccertamento">Numero sub &nbsp;</label>
				            </span>
				            <s:textfield id="numeroSubAccertamento" cssClass="lbTextSmall span2" name="storicoImpegnoAccertamentoInModifica.subAccertamento.numero" onkeyup="return checkItNumbersOnly(event)"  maxlength="6"/>       
			        	
			        	<!-- task-131 <s:submit id="inserimentoStoricoSubmit" cssClass="btn btn-primary freezePagina" method="inserisciStorico" value="inserisci" name="inserisci" /> -->
			        	<!-- task-131 <s:submit id="aggiornamentoStoricoSubmit" cssClass="btn btn-primary freezePagina hide" method="aggiornaStorico" value="aggiorna" name="aggiorna" /> -->
			        	<s:submit id="inserimentoStoricoSubmit" cssClass="btn btn-primary freezePagina" action="gestisciStoricoImpegnoAccertamentoStep2_inserisciStorico" value="inserisci" name="inserisci" />
			        	<s:submit id="aggiornamentoStoricoSubmit" cssClass="btn btn-primary freezePagina hide" action="gestisciStoricoImpegnoAccertamentoStep2_aggiornaStorico" value="aggiorna" name="aggiorna" />			        	
			        	</div>
			        </div>
			  	</fieldset>
			  	
			  	<s:set var="eliminaAction" value="%{'gestisciStoricoImpegnoAccertamentoStep2_elimina'}" />	                                
			  	<s:include value="/jsp/include/modalElimina.jsp" />
				  	
				<p class="margin-medium"> 
					<s:include value="/jsp/include/indietroSubmit.jsp" /> 
					<!-- task-131 <s:submit cssClass="btn btn-secondary" method="annullaStep2" value="annulla" name="annulla" /> -->
					<s:submit cssClass="btn btn-secondary" action="gestisciStoricoImpegnoAccertamentoStep2_annullaStep2" value="annulla" name="annulla" />
				</p>      
		    
            </div>
        </div> 
		

      </s:form>
    </div>
  </div>	 
</div>		
<script type="text/javascript" src="${jspath}movgest/gestisciStoricoImpegnoAccertamentoStep2.js"></script>
<s:include value="/jsp/include/footer.jsp" />
