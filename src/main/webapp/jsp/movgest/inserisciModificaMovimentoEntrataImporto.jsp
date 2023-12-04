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
  
  
  <style>
  #abbinas .checkboxLabel{
    margin-left: 25px;
    position: relative;
    top: -16px;
	}
  
  </style>
  
  
<div class="container-fluid-banner">



<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<s:set var="gestisciForwardAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_gestisciForward'}" />	 
<s:set var="siSalvaAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_salvaConByPassDodicesimi'}" />	          
		
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
      <%-- SIAC-7952 rimuovo .do dalla action --%>
      <s:form id="mainForm" method="post" action="inserisciModificaMovimentoSpesaAccImporto" cssClass="form-horizontal">
      
		<div id="msgControlloProsegui" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-error">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
    				<s:actionerror/>
				</div>
			</div>
			<div class="modal-footer">
				<!-- <button id="noPluri" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
				<!-- task-131 <s:submit id="annullaProseguiBtn" name="btnAnnullaProsegui" method="noProsegui" value="no" cssClass="btn btn-secondary" /> -->
				<s:submit id="annullaProseguiBtn" name="btnAnnullaProsegui" action="inserisciModificaMovimentoSpesaAccImporto_noProsegui" value="no" cssClass="btn btn-secondary" />
				<!-- <button class="btn" data-dismiss="modal" aria-hidden="true">si, prosegui</button> -->
				<!-- task-131 <s:submit id="proseguiBtn" name="btnProsegui" method="siProsegui" value="si" cssClass="btn btn-primary" /> -->
				<s:submit id="proseguiBtn" name="btnProsegui" action="inserisciModificaMovimentoSpesaAccImporto_siProsegui" value="si" cssClass="btn btn-primary" />
			</div>
		</div>
      
      
      
<!--  	<h3>Accertamento ${model.movimentoSpesaModel.accertamento.annoMovimento}/${model.movimentoSpesaModel.accertamento.numero} - ${model.movimentoSpesaModel.accertamento.descrizione} - Importo : <s:property value="getText('struts.money.format', {model.movimentoSpesaModel.accertamento.importoAttuale})"/> </h3>	-->

		<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
       
       	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
       	<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
        <%-- SIAC-7630 --%>
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />   
<%-- 		<s:if test="hasActionErrors()">
		Messaggio di ERROR
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		Messaggio di WARNING
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>       
		
		<s:if test="hasActionWarnings()">
			Messaggio di WARNING
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				   <s:iterator value="actionWarnings">
				       <s:property escapeHtml="false"/><br>
				   </s:iterator>
					
				</ul>
			</div>
		</s:if>  --%>

        <h4>Inserimento modifica </h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           <fieldset class="form-horizontal margin-large">
                    <div class="control-group">
                      <!--label class="control-label" for="subimpegno">Subimpegno</label-->
                      <label class="control-label" for="subimpegno">Abbina a</label>
                      <div class="controls">
                        <div class="radio inline" style="width: 617px;">
                        	<s:radio id="competenze" cssClass="flagCompetenze" name="tipoImpegno" list="tipoImpegnoModificaImportoList"/>  
						</div>   
                      </div>
                    </div>
                    <div id="subDiv" class="control-group">
                    	<label class="control-label" for="subimpegno">Seleziona Sub Accertamento</label>
                  	  	<div class="controls">
							<s:select list="numeroSubImpegnoList" name="subSelected" id="numeroSubImpegnoList" headerKey="" headerValue="" onchange="javascript:document.location.href='inserisciModificaMovimentoSpesaAccImporto_caricaDatiSub.do?tipoImpegno=SubAccertamento&subSelected='+this.value+'&abbinaChk='+abbina.checked" />
							<span id="abbinas" class="radio inline" style="width: 160px;" >
								<s:checkboxlist id="abbina" cssClass="flagAbbina" name="abbina" list="abbinaList"/>
							</span>                     
                   	  	</div>
                   	</div>
                    <div id="infosImp" class="control-group datiVisibili1">              
                      <div class="controls">              
                        <dl class="dl-horizontal">
                          <dt>Importo Accertamento</dt>
                          
                          <s:if test="!model.flagSuperioreTreAnni">
                         	 <dd>Modifica Accertamento compresa tra <s:property value="model.minImportoCalcolatoMod" /> e <s:property value="model.maxImportoCalcolatoMod" /></dd>        
                          </s:if>
                          <s:else>
                          	<dd>Modifica Accertamento non inferiore a <s:property value="model.minImportoCalcolatoMod" /></dd>
                          </s:else>
                          
                          
                        </dl>             
                      </div>
                    </div>
                    <s:if test="model.subImpegnoSelectedMod">
                   		<div id="subInfo" class="control-group datiVisibili2">
                   	   		<div class="controls">             
                        		<dl class="dl-horizontal">
                          			<dt style="width:220px;">Codice Sub Accertamento &nbsp;</dt>
                          			<dd style="margin-left:200px;"><s:property value="model.numeroSubImpegnoMod" /></dd>
                          			<dt style="width:220px;">Importo Attuale Sub Accertamento &nbsp;</dt>
                          			<dd style="margin-left:200px;"><s:property value="model.importoAttualeSubImpegnoMod" /></dd>
                          			<dt>Importo</dt>
                          			<dd>Modifica Sub Accertamento compresa tra <s:property value="model.minImportoSubCalcolatoMod" /> e <s:property value="model.maxImportoSubCalcolatoMod" /></dd>
                        		</dl>             
                      		</div>
                    	</div> 
                    </s:if>    
                    
                    <s:set var="consultaModificheProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_consultaModificheProvvedimento'}" />
					<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_consultaModificheProvvedimentoSub'}" />
                    
<%--                     <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" />  --%>
                    <s:include value="/jsp/movgest/include/provvedimento.jsp" /> 
                    <div class="control-group">
                      <label class="control-label" for="motivo">Modifica motivo *</label>
                      <div class="controls">

                        <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivo" headerKey="" 
          		   				headerValue="" name="idTipoMotivo" listKey="codice" listValue="descrizione"  cssClass="span9"/>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
                      <div class="controls">
                      	<%-- SIAC-8506 --%>
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" maxlength="500" />  
                      </div>
                    </div>
					<div class="control-group anniPluriennale">
                      <label class="control-label" for="motivo">Anni di pluriennale</label>
                      <div class="controls">
                      	<s:textfield id="anniPluriennali" onkeyup="return checkItNumbersOnly(event)" cssClass="span1" name="anniPluriennali" />
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="importo">Importo </label>
                      <div class="controls">
                      
                     	<s:if test="!isImportoSenzaLimiti()">
                      		<s:textfield name="importoImpegnoModImp" id="importoImpegnoModImp" cssClass="span2 soloNumeri decimale" /> <span id="imp">Modifica compresa tra <s:property value="model.minImpMod" /> e <s:property value="model.maxImpMod" /> </span> <span id="sub"> Modifica compresa tra <s:property value="model.minSubMod" /> e <s:property value="model.maxSubMod" /> </span> <span id="anche"> Modifica compresa tra <s:property value="model.minAncheMod" /> e <s:property value="model.maxAncheMod" /> </span>
                      	</s:if>
                      	<s:else>
                      		<s:textfield name="importoImpegnoModImp" id="importoImpegnoModImp" cssClass="span2 soloNumeri decimale" /> Modifica Accertamento non inferiore a <s:property value="model.minImportoCalcolatoMod" /></dd>
                      	</s:else>
                      	<span id="HIDDEN_importoRORM"></span>
                      </div>
                    </div>
                    
                   <div class="control-group">    
                      <label class="control-label" for="reimputazione">Reimputazione </label>
                      <div class="controls">
					      <s:radio id="radioReimputato" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" list="model.movimentoSpesaModel.daReimputazione"></s:radio> 
					      <span class="riaccVisible" id="bloccoReimputato">
					      	&nbsp; Anno reimputazione:
					      	&nbsp; <s:textfield id="annoReimp" name="model.movimentoSpesaModel.annoReimputazione" onkeyup="return checkItNumbersOnly(event)" cssClass="span1 soloNumeri" />&nbsp;
					      	&nbsp; Elaborato ROR - Reimp. in corso d&lsquo;anno
					      	&nbsp; <s:radio id="radioElaboratoRor" disabled="true" name="model.movimentoSpesaModel.elaboratoRor" cssClass="flagResidenza" list="model.movimentoSpesaModel.daElaboratoRor"></s:radio>
					      </span>
                      </div>
                   </div> 
                    
                    
                  </fieldset>
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
                                   
                  <!--#include virtual="include/creditore.html" -->
            <s:set var="selezionaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_selezionaProvvedimento'}" />
            <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_clearRicercaProvvedimento'}" />	          
        	<s:set var="ricercaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_ricercaProvvedimento'}" />	          
         	<s:set var="eliminaAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_elimina'}" />	  
            <s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_selezionaProvvedimentoInserito'}" />	
			<s:set var="inserisciProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccImporto_inserisciProvvedimento'}" />	
	
            <s:include value="/jsp/movgest/include/modal.jsp" /> 
            <!-- Fine Modal --> 
                         
      		<%-- SIAC-7349 Inzio SR180 CM 08/04/2020 - 08/04/2020 Introduzione della tabella delle associazioni nel tab modifiche di accertamento --%>
      		<s:include value="/jsp/movgest/include/listaCollegataSpesa.jsp" /> 
			<%-- SIAC-7349 Fine SR180 CM 01/04/2020 - 08/04/2020 --%>      
                        
                                   
            <br/> <br/>
 
                                                     
            <p>
<%--             <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> --%>
            <a class="btn btn-secondary" href="">annulla</a>
              <span class="pull-right buttonPluriennale">
                <!-- task-131 <s:submit method="salvaProsegui" value="Salva e Prosegui" cssClass="btn btn-primary confermaPluri"  /> &nbsp; -->
              	<!-- task-131 <s:submit method="salvaPopup" value="Salva" cssClass="btn btn-primary confermaPluri"  /> -->         	
              	<s:submit action="inserisciModificaMovimentoSpesaAccImporto_salvaProsegui" value="Salva e Prosegui" cssClass="btn btn-primary confermaPluri"  /> &nbsp;
              	<s:submit action="inserisciModificaMovimentoSpesaAccImporto_salvaPopup" value="Salva" cssClass="btn btn-primary confermaPluri"  />
              	
              </span> 
              <span class="pull-right singleButton">
              	<!-- task-131 <s:submit method="salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit> -->
              	<s:submit action="inserisciModificaMovimentoSpesaAccImporto_salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit>
              	
              </span>
        	<a id="linkMsgControlloProsegui" href="#msgControlloProsegui" data-toggle="modal" style="display:none;"></a>
            </p>
          </div>
        </div>
        
</s:form>
    </div>
  </div>	 
</div>	


<%-- <script type="text/javascript" src="<%= request.getContextPath() %>/js/movgest/inserisciModificaMovimentoEntrataImporto.js"></script> --%>
<script type="text/javascript" src="${jspath}movgest/inserisciModificaMovimentoEntrataImporto.js"></script>

 <script type="text/javascript">
 function checkNullToEmptyString(e) {
     
	    var obj = e.srcElement || e.target;
	    if(obj.value.match(/^\s*$/g)){
	        obj.value = obj.value.replace(/^\s*$/g, "0,00");
	    }
	}
 /* questo non capisco cosa sia, lo lascio */
	 function proseguiMsg(){
    	$('#msgPros').modal();
     }
     
     
     <s:if test="step1Model.checkproseguiMovimentoSpesa">
     	$("#linkMsgControlloProsegui").click();
     </s:if>
     
</script>

<s:include value="/jsp/include/footer.jsp" />