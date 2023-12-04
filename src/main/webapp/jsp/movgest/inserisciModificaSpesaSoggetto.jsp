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
 <s:if test="%{tipoMovimentoImpegno}">
	<s:set var="gestisciForwardAction" value="%{'inserisciMovSpesaSoggetto_gestisciForward'}" />			    
	<s:set var="siSalvaAction" value="%{'inserisciMovSpesaSoggetto_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'inserisciMovSpesaSoggetto_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'inserisciMovSpesaSoggetto_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'inserisciMovSpesaSoggetto_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'inserisciMovSpesaSoggetto_annullaMovGestSpesa'}" />	 
	<s:set var="eliminaSubImpegnoAction" value="%{'inserisciMovSpesaSoggetto_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'inserisciMovSpesaSoggetto_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'inserisciMovSpesaSoggetto_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisciMovSpesaSoggetto_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisciMovSpesaSoggetto_salvaConByPassDodicesimi'}" />	  
	<s:set var="consultaModificheProvvedimentoAction" value="%{'inserisciMovSpesaSoggetto_consultaModificheProvvedimento'}" />
	<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisciMovSpesaSoggetto_consultaModificheProvvedimentoSub'}" />
 </s:if>
 <s:else>
	<s:set var="gestisciForwardAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_gestisciForward'}" />			    
	<s:set var="siSalvaAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_annullaMovGestSpesa'}" />
		 
	<s:set var="eliminaSubImpegnoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_salvaConByPassDodicesimi'}" />	  
	<s:set var="consultaModificheProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_consultaModificheProvvedimento'}" />
	<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_consultaModificheProvvedimentoSub'}" />
 </s:else>       
		
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->



<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
<!-- <h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} - ${model.movimentoSpesaModel.impegno.importoAttuale}</h3> -->  
     <!-- <h3><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> ${model.movimentoSpesaModel.accertamento.annoMovimento}/${model.movimentoSpesaModel.accertamento.numero} - ${model.movimentoSpesaModel.accertamento.descrizione} - <s:property value="getText('struts.money.format', {movimentoSpesaModel.accertamento.importoAttuale})"/></h3> -->
<!--  <h3><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> ${model.step1Model.annoImpegno}/ ${model.step1Model.numeroImpegno} - ${model.step1Model.oggettoImpegno} - <s:property value="getText('struts.money.format', {step1Model.importoImpegno})"/></h3>	-->

	<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
  
      <s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">     
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
		</s:if> --%>  
		
        <h4>Inserimento modifica soggetto</h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           <fieldset class="form-horizontal margin-large">
              <div class="control-group">
                <!--label class="control-label" for="subimpegno">Subimpegno</label--> 
	            <label class="control-label" for="subimpegno">Abbina a</label>
                <div class="controls">
                        <div class="radio inline">
                        	<s:radio id="competenze" cssClass="flagCompetenze" name="tipoImpegno" list="tipoImpegnoModificaSoggettoList" disabled="tipoImpegnoModificaSoggettoList.size() > 1"/>
						</div>		       
                      </div>
              </div>
              <div id="subDiv" class="control-group">
              	<label class="control-label" for="subimpegno">Seleziona <s:property value="%{labels.OGGETTO_GENERICO}"/></label>
                <div class="controls">
                  <s:if test="%{tipoMovimentoImpegno}">
					<s:select list="numeroSubImpegnoList" name="subSelected" id="numeroSubImpegnoList" headerKey="" headerValue="" onchange="javascript:document.location.href='inserisciMovSpesaSoggetto_caricaDatiSub.do?tipoImpegno=SubImpegno&subSelected='+this.value" />
	<!-- 			<span id="abbinas" class="radio inline" style="width: 160px;" >
						<s:checkboxlist id="abbina" cssClass="flagAbbina" name="abbina" list="abbinaList"/>
					</span>
	-->			  </s:if>
				  <s:if test="%{!tipoMovimentoImpegno}">
					<s:select list="numeroSubImpegnoList" name="subSelected" id="numeroSubImpegnoList" headerKey="" headerValue="" onchange="javascript:document.location.href='inserisciModificaMovimentoSpesaAccSoggetto_caricaDatiSub.do?tipoImpegno=SubAccertamento&subSelected='+this.value" />
	<!-- 			<span id="abbinas" class="radio inline" style="width: 160px;" hidden="true">
 						<s:checkboxlist id="abbina" cssClass="flagAbbina" name="abbina" list="abbinaList"/>		
					</span>
	-->			  </s:if>	                     
               	</div>
            </div>
  	           <s:if test="%{subImpegnoSelected}">
                   		<div class="control-group datiVisibili2">
                   	   		<div class="controls">             
                        		<dl class="dl-horizontal">
                          			<dt style="width:220px;">Codice <s:property value="%{labels.OGGETTO_GENERICO}"/></dt>
                          			<dd style="margin-left:200px;"> ${numeroSubImpegno}</dd>
                          			<dt style="width:220px;">Codice Soggetto <s:property value="%{labels.OGGETTO_GENERICO}"/></dt>
                          			<dd style="margin-left:200px;"> ${soggettoSubAttuale.codiceSoggetto}</dd>
                        		</dl>             
                      		</div>
                    	</div> 
                    </s:if>       

               <h4>Dati modifica</h4>     
                    <div class="control-group">
                      <label class="control-label" for="motivo">Modifica  motivo *</label>
                      <div class="controls">

                        <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivo" headerKey="" 
          		   				headerValue="" name="idTipoMotivo" listKey="codice" listValue="descrizione"  cssClass="span9"/>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span> </label>
                      <div class="controls">
                      	<%-- SIAC-8506 --%>
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" maxlength="500" />  
                      </div>
                    </div>

                  </fieldset>
                     
<%--                    <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" /> --%>
                        <s:include value="/jsp/movgest/include/provvedimento.jsp" />

                   <div id="refreshHeaderSoggetto">
            			<s:include value="/jsp/movgest/include/headerSoggetto_modif.jsp"/>
            		</div>
            	   <s:include value="/jsp/movgest/include/soggetto_modif.jsp" /> 
            	   
            	   
	 				<!--  per soggetto -->
	 				<s:if test="%{tipoMovimentoImpegno}">
	 					<s:set var="selezionaSoggettoAction" value="%{'inserisciMovSpesaSoggetto_selezionaSoggetto'}" />
	 					
	 					<s:set var="selezionaProvvedimentoAction" value="%{'inserisciMovSpesaSoggetto_selezionaProvvedimento'}" />
                   		<s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciMovSpesaSoggetto_clearRicercaProvvedimento'}" />	          
		       			<s:set var="ricercaProvvedimentoAction" value="%{'inserisciMovSpesaSoggetto_ricercaProvvedimento'}" />	          
		       			<s:set var="eliminaAction" value="%{'inserisciMovSpesaSoggetto_elimina'}" />	  
		            	<s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisciMovSpesaSoggetto_selezionaProvvedimentoInserito'}" />	
						<s:set var="inserisciProvvedimentoAction" value="%{'inserisciMovSpesaSoggetto_inserisciProvvedimento'}" />	
						<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisciMovSpesaSoggetto_pulisciRicercaSoggetto'}" />	          
						<s:set var="ricercaSoggettoAction" value="%{'inserisciMovSpesaSoggetto_ricercaSoggetto'}" />
						<s:set var="listaClasseSoggettoChangedAction" value="%{'inserisciMovSpesaSoggetto_listaClasseSoggettoChanged'}" />
					</s:if>
					<s:else>
						<s:set var="selezionaSoggettoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_selezionaSoggetto'}" />
						
						<s:set var="selezionaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_selezionaProvvedimento'}" />
                   		<s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_clearRicercaProvvedimento'}" />	          
		       			<s:set var="ricercaProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_ricercaProvvedimento'}" />	          
		       			<s:set var="eliminaAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_elimina'}" />	  
		            	<s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_selezionaProvvedimentoInserito'}" />	
						<s:set var="inserisciProvvedimentoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_inserisciProvvedimento'}" />	
						<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_pulisciRicercaSoggetto'}" />	          
						<s:set var="ricercaSoggettoAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_ricercaSoggetto'}" />
						<s:set var="listaClasseSoggettoChangedAction" value="%{'inserisciModificaMovimentoSpesaAccSoggetto_listaClasseSoggettoChanged'}" />
					</s:else>
	 					
			
					
                   <s:include value="/jsp/movgest/include/modal.jsp" />
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
                                    
                  <!--#include virtual="include/creditore.html" -->
            <!--#include virtual="include/modal.html" --> 
            <!-- Fine Modal --> 
            
            	  
                                   
            <br/> <br/>  
            <p>
<%--             <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> --%>
                 
            <a class="btn btn-secondary" href="">annulla</a>
              <span class="pull-right singleButton">
              <s:if test="%{tipoMovimentoImpegno}">
	 			<!-- task-131 <s:submit method="salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit> -->
              	<s:submit action="inserisciMovSpesaSoggetto_salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit>
              </s:if>
              <s:else>
				 	<s:submit action="inserisciModificaMovimentoSpesaAccSoggetto_salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit>
         	  </s:else>
	 			
              </span>
              
			<!-- DODICESIMI: -->
			<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
              
            </p>
            
          </div>
        </div>
      </s:form>
    </div>
  </div>	 
</div>

 <script type="text/javascript">
 $(document).ready(function() {
     $('.buttonPluriennale').hide();
     $('.anniPluriennale').hide();
     var competenza = $('.flagCompetenze:checked').val();
     if(competenza == 'Impegno'){
   	  $('#numeroSubImpegnoList').hide();
   	  $('#subDiv').hide();
   	  $('#abbinas').hide();
   	  $('#subInfo').hide();
   	  $('#imp').show();
   	  $('#sub').hide();
   	  $('#anche').hide();
   	  $('#infosImp').show();
     }
     if(competenza == 'SubImpegno'){
   	  $('#numeroSubImpegnoList').show();
   	  $('#subDiv').show();
   	  $('#abbinas').show();
   	  $('#subInfo').show();
   	  $('#imp').hide();
   	  $('#sub').show();
   	  $('#anche').hide();
   	  $('#infosImp').hide();
     }
     if(competenza == 'Accertamento'){
   	  $('#numeroSubImpegnoList').hide();
   	  $('#subDiv').hide();
   	  $('#abbinas').hide();
   	  $('#subInfo').hide();
   	  $('#imp').show();
   	  $('#sub').hide();
   	  $('#anche').hide();
   	  $('#infosImp').show();
     }
     if(competenza == 'SubAccertamento'){
   	  $('#numeroSubImpegnoList').show();
   	  $('#subDiv').show();
   	  $('#abbinas').show();
   	  $('#subInfo').show();
   	  $('#imp').hide();
   	  $('#sub').show();
   	  $('#anche').hide();
   	  $('#infosImp').hide();
     }
     $('.flagCompetenze').change(function(){
   	  var radioSec = $('.flagCompetenze:checked').val();
   	  if(radioSec == 'SubImpegno'){
   		  $('#numeroSubImpegnoList').show();
   		  $('#subDiv').show();
   		  $('#abbinas').show();
   		  $('#subInfo').show();
       	  $('#imp').hide();
       	  $('#sub').show();
       	  $('#anche').hide();
       	  $('#infosImp').hide();
   	  } 
			if(radioSec == 'Impegno') {
   		  $('#numeroSubImpegnoList').hide();
   		  $('#subDiv').hide();
   		  $('#abbinas').hide();
   		  $('#subInfo').hide();
       	  $('#imp').show();
       	  $('#infosImp').show();
       	  $('#sub').hide();
       	  $('#anche').hide();
   	  }
   	  if(radioSec == 'Accertamento'){
   		  $('#numeroSubImpegnoList').hide();
   		  $('#subDiv').hide();
   		  $('#abbinas').hide();
   		  $('#subInfo').hide();
       	  $('#imp').show();
       	  $('#infosImp').show();
       	  $('#sub').hide();
       	  $('#anche').hide();
   	  }
   	  if(radioSec == 'SubAccertamento'){
   		  $('#numeroSubImpegnoList').show();
   		  $('#subDiv').show();
   		  $('#abbinas').show();
   		  $('#subInfo').show();
       	  $('#imp').hide();
       	  $('#sub').show();
       	  $('#anche').hide();
       	  $('#infosImp').hide();
   	  }
     });
     
     $('#abbina-1').click(function(){
   	  var opt = $('#abbina-1').is(':checked');
   	  if(opt){
   		  $('#anche').show();
   		  $('#sub').hide();
   		  $('#infosImp').show();
   		  $('#codCreditore').attr("disabled",true);
   		  $('#codCreditore').attr("value"," ");
   		  $('#listaClasseSoggetto').attr("disabled",true);
   		  $('#listaClasseSoggetto').attr("value"," ");
   		  $('#linkCompilazioneGuidataSoggetto').hide();
   	  } else {
   		  $('#sub').show();
       	  $('#anche').hide();
       	  $('#infosImp').hide();
       	  $('#codCreditore').attr("disabled",false);
       	  $('#listaClasseSoggetto').attr("disabled",false);
       	  $('#linkCompilazioneGuidataSoggetto').show();
   	  }
     });
      
      $("#ricercaGuidataSoggetto").click(function() {
			$.ajax({
				//task-131 url: '<s:url method="ricercaSoggetto"/>',
				url: '<s:url action="%{#ricercaSoggettoAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaSoggetto").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
      
      $("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").val()
			);
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			$("#codCreditore").val("");
			$.ajax({
				//task-131 url: '<s:url method="listaClasseSoggettoChanged"/>',
				url: '<s:url action="%{#listaClasseSoggettoChangedAction}"/>',
				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
      
	    $("#linkCompilazioneGuidataProvvedimento").click(function(){
	    	
	   		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
   			var strutturaAmministrativaParam = "";
   			if (treeObj != null) {
   				var selectedNode = treeObj.getCheckedNodes(true);
   				selectedNode.forEach(function(currentNode) {
   					strutturaAmministrativaParam = currentNode;
   				});
   			}
   		
   		
   			initRicercaGuidataProvvedimentoConStruttura(
   				$("#annoProvvedimento").val(), 
   				$("#numeroProvvedimento").val(),
   				$("#listaTipiProvvedimenti").val(),
   				strutturaAmministrativaParam
   			);
	   	});
 
 
 });
 
 
	<s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
		$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
	</s:if>
 
</script>




<s:include value="/jsp/include/footer.jsp" />