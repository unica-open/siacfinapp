<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>

<body>   
<s:include value="/jsp/include/header.jsp" />                    
  <p class="nascosto"><a title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
  <hr />
<div class="container-fluid-banner">

  <a title="A-contenuti"></a>
</div>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">			
		<s:form cssClass="form-horizontal" id="inserisciLiquidazioneStep2" action="inserisciLiquidazioneStep2" method="post">     
		<h3>Nuova liquidazione</h3>		
			
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<dl class="dl-horizontal">				
				<dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
				  <dd><s:property value="capitolo.annoCapitolo"/>/<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroArticolo"/>/<s:property value="capitolo.numeroUEB"/>  - <s:property value="capitolo.descrizione"/>  -  <s:property value="capitolo.strutturaAmministrativoContabile.codice"/> - tipo Finanziamento: <s:property value="capitolo.tipoFinanziamento.descrizione"/></dd>	
				   <%-- <dd><s:property value="capitolo.annoCapitolo"/>/<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroUEB"/>  - <s:property value="capitolo.descrizione"/>  -  <s:property value="capitolo.strutturaAmministrativoContabile.codice"/> - tipo Finanziamento: <s:property value="capitolo.tipoFinanziamento.codice"/></dd> --%>
				  
			 <div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
              <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
              <h3 id="myModalLabel2">Dettagli del capitolo</h3>
              </div>
              <div class="modal-body">
              
			   
			   <dl class="dl-horizontal">
			     <dt>Numero</dt>
			      <dd><s:property value="capitolo.annoCapitolo"/> / <s:property value="capitolo.numeroCapitolo"/> / <s:property value="capitolo.numeroArticolo"/> / <s:property value="capitolo.numeroUEB"/> - <s:property value="capitolo.descrizione" /> - <s:property value="capitolo.strutturaAmministrativoContabile.descrizione" />&nbsp;</dd>
			      <dt>Tipo finanziamento</dt>
			      <dd><s:property value="capitolo.tipoFinanziamento.descrizione" />&nbsp;</dd>
			      <dt>Piano dei conti finanziario</dt>
			      <dd><s:property value="capitolo.elementoPianoDeiConti.codice" default=" "/> - <s:property value="capitolo.elementoPianoDeiConti.descrizione" default=" "/>&nbsp;</dd>
			    </dl>
				<table class="table table-hover table-bordered">
			      <tr>
			        <th>&nbsp;</th>
			        <s:iterator value="datoPerVisualizza.importiCapitolo">
						<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
					</s:iterator>
			      </tr>
			      <tr>
			        <th>Stanziamento</th>
			        <s:iterator value="datoPerVisualizza.importiCapitolo">
						<td><s:property value="getText('struts.money.format', {competenza})" /></td>
					</s:iterator>       
			      </tr>
			      <tr>
			        <th>Disponibile</th>

						<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
						<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
					    <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>

			      </tr>
			    </table>
                
                

              </div>
            </div>                                 
				  
				<dt>Impegno</dt>
				<dd><s:property value="annoImpegno"/>/<s:property value="numeroImpegno"/><s:if test="%{numeroSub != null && numeroSub != ''}">/</s:if><s:property value="numeroSub"/><s:if test="%{descrizioneImpegnoPopup != null && descrizioneImpegnoPopup != ''}"> - </s:if><s:property value="descrizioneImpegnoPopup"/><s:if test="%{descrizioneTipoImpegnoPopup != null && descrizioneTipoImpegnoPopup != ''}"> - </s:if><s:property value="descrizioneTipoImpegnoPopup"/></dd>

			</dl>

		 <div id="MyWizard" class="wizard">
			<ul class="steps">
				<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>ricerca impegno<span class="chevron"></span></li>
				<li data-target="#step2" class="active" ><span class="badge">2</span>dati liquidazione<span class="chevron"></span></li>
			</ul>
		</div>
		
        <div class="step-content">	
            <div class="step-pane active" id="step1">	        	
	        	
	        	<s:set var="selezionaSoggettoAction" value="%{'inserisciLiquidazioneStep2_selezionaSoggetto'}" />
				<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisciLiquidazioneStep2_pulisciRicercaSoggetto'}" />	          
				<s:set var="ricercaSoggettoAction" value="%{'inserisciLiquidazioneStep2_ricercaSoggetto'}" />	    
	        	
	        	<s:set var="resedeAction" value="%{'inserisciLiquidazioneStep2_resede'}" />	    
	        	<s:set var="remodpagamentoAction" value="%{'inserisciLiquidazioneStep2_remodpagamento'}" />	    
	        	
	        	<div id="refreshHeaderSoggetto">
		          	<s:include value="/jsp/liquidazione/include/headerSoggettoLiquidazione.jsp"/>		       	
			       	<s:include value="/jsp/liquidazione/include/soggettoLiquidazione.jsp" /> 
			       	<s:include value="/jsp/include/modalSoggetto.jsp" />		        	
		        	<div id="refreshSediSecondarie">
		        		<s:include value="/jsp/liquidazione/include/sediSecondarie.jsp" />	   
		        	</div>		        	
		        	<div id="refreshModPagamento">     	
	        			<s:include value="/jsp/liquidazione/include/modalitaPagamento.jsp" />
		        	</div>
	        	</div> 
	        	
	        	<s:include value="/jsp/liquidazione/include/provvedimentoLiquidazione.jsp" />
				
				<s:set var="selezionaProvvedimentoAction" value="%{'inserisciLiquidazioneStep2_selezionaProvvedimento'}" />
		        <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciLiquidazioneStep2_clearRicercaProvvedimento'}" />	          
		        <s:set var="ricercaProvvedimentoAction" value="%{'inserisciLiquidazioneStep2_ricercaProvvedimento'}" />	              
				<s:include value="/jsp/include/modalProvvedimenti.jsp" />		
				
				<s:include value="/jsp/liquidazione/include/liquidazione.jsp" />	        	
	        	
	        	<s:set var="codiceSiopeChangedAction" value="%{'inserisciLiquidazioneStep2_codiceSiopeChanged'}" />
		        <s:set var="confermaPdcAction" value="%{'inserisciLiquidazioneStep2_confermaPdc'}" />
		        
		        <s:include value="/jsp/include/transazioneElementare.jsp" />
	             	            	        
	        </div>            
        </div>
        
        <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
        
		<p class="margin-medium">
			<!-- task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary"/> -->
			<s:submit name="indietro" value="indietro" action="inserisciLiquidazioneStep2_indietro" cssClass="btn btn-secondary"/>		
			<!-- <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary"/> -->	
			<a id="annulla" class="btn btn-secondary" href="">annulla</a>				
			<!-- task-131 <s:submit name="salva" value="salva" method="verifica" cssClass="btn btn-primary pull-right freezePagina"/> -->
			<s:submit name="salva" value="salva" action="inserisciLiquidazioneStep2_verifica" cssClass="btn btn-primary pull-right freezePagina"/> 
			
			<a id="linkMsgCheckStep2" href="#msgControlloProseguiLiquidazioneStep2" data-toggle="modal" style="display:none;"></a>
			<a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
		</p>          
          
            <!-- modale controllo prosegui pluriennali accertamento -->
            <div id="msgControlloProseguiLiquidazioneStep2" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDatipersi" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-warning">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property escapeHtml="false"/><br>
		   		   </s:iterator>
                  <p></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <%-- SIAC-8060 freezePaginaOver --%>
                <!-- task-131 -->
                <!--<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary freezePaginaOver" method="chiamaSalvaORichiediConferma"/> -->
                <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary freezePaginaOver" action="inserisciLiquidazioneStep2_chiamaSalvaORichiediConferma"/>
              </div>
            </div>  
            <!-- /modale  controllo prosegui --> 
            
            
            
            <!-- SIAC-5333 -->
           <div id="msgPrimaNota" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
             <div class="modal-body">
               <div class="alert alert-warning">
                 <button type="button" class="close" data-dismiss="alert">&times;</button>
                 <p> L'operazione potrebbe portare all'inserimento di una scrittura su contabilit&agrave; generale.
                  Nel caso in cui venga creata, &egrave; possibile inserire una prima nota provvisoria o validarla ora.</p>
               </div>
             </div>
             <div class="modal-footer">
               <button class="btn freezePagina" id="inserisciPrimaNotaProvvisoria" data-dismiss="modal" aria-hidden="true">valida in seguito</button>
               <button class="btn freezePaginaWaitOp" id="validaPrimaNota" data-dismiss="modal" aria-hidden="true">valida ora</button>
             </div>
           </div>
			<s:hidden id="HIDDEN_saltaInserimentoPrimaNota" name="saltaInserimentoPrimaNota"/>
			<s:hidden id="HIDDEN_richiediConfermaUtente" name="richiediConfermaUtente"/>
            <!-- SIAC-5333 --> 
            
          
      </s:form>
     </div>
  </div>
  	 
</div>	
  <script type="text/javascript">

	
	<s:if test="checkWarningsLiquidazioneStep2">
		$("#linkMsgCheckStep2").click();
	</s:if><s:elseif test="%{richiediConfermaUtente}">
		$('#linkmsgPrimaNota').click();
		$('#inserisciPrimaNotaProvvisoria').on('click', function(){
			$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
			$('#HIDDEN_richiediConfermaUtente').val(true);
			
			$('form')
			//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
			.append('<input type="hidden" name="action:inserisciLiquidazioneStep2_salva" value="" class="btn" >')
			.submit();
		});
		$('#validaPrimaNota').on('click', function(){
			$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
			$('#HIDDEN_richiediConfermaUtente').val(true);
			$('form')
			//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
			.append('<input type="hidden" name="action:inserisciLiquidazioneStep2_salva" value="" class="btn" >')
			.submit();
		});
	</s:elseif>
	
	$(document).ready(function() {
		
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
		
		$("#annulla").click(function(){
			window.location.reload();
		});	
		
		$("#codCreditoreLiquidazione").change(function(){
			var cod = $("#codCreditoreLiquidazione").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="modpagamento"></s:url>',
				url: '<s:url action="inserisciLiquidazioneStep2_modpagamento"/>',										
				type: "GET",
				data: { id: cod },
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    	//Carico i dati in tabella "Sedi secondarie"
					$.ajax({
						//task-131 url: '<s:url method="sedisecondarie"></s:url>',
						url: '<s:url action="inserisciLiquidazioneStep2_sedisecondarie"/>',										
						type: "GET",
						data: { id: cod },
					    success: function(data)  {
					    	$("#refreshSediSecondarie").html(data);
						}
					});
				}
			});			
		});	
		

// 		$("#cup").change(function(){
// 			var cup = $("#cup").val();		
// 			$.ajax({
// 				url: '<s:url method="setcup"></s:url>',
// 				type: "GET",
// 				data: { cup: cup },
// 			    success: function(data)  {
// 			    	$("#refreshTRX").html(data);
// 				}
// 			});			
// 		});	

		//SIAC-8060
		//$('#submitBtnForward').on('click', function(e){
		//	e && e.stopPropagation();
		//	$('#msgControlloProseguiLiquidazioneStep2').modal('hide');
		//	$('#inserisciLiquidazioneStep2').submit();
		//});
		//
		
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreLiquidazione").val(),
				null
			);
		});
		
		
		var tipoDebitoSiopeVar = $("input[name='tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
		
	});
	
</script> 
<s:include value="/jsp/include/footer.jsp" />
