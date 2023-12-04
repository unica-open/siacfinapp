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

<s:set var="gestisciForwardAction" value="%{'ricercaAccertamento_gestisciForward'}" />
<s:set var="siSalvaAction" value="%{'ricercaAccertamento_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'ricercaAccertamento_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'ricercaAccertamento_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'ricercaAccertamento_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'ricercaAccertamento_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'ricercaAccertamento_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'ricercaAccertamento_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'ricercaAccertamento_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'ricercaAccertamento_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'ricercaAccertamento_salvaConByPassDodicesimi'}" />	          
		 
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
    	<%-- SIAC-7952 rimuovo .do dalla action --%>     
        <s:form method="post" nane="ricercaAccertamento" action="ricercaAccertamento" cssClass="form-horizontal">  
         <s:include value="/jsp/include/actionMessagesErrors.jsp" />
               
         <h3>
         Ricerca Accertamento
         <s:if test="%{ricercaTipoROR == true}">
              	ROR
          </s:if>
         </h3>
		   <!--#include virtual="include/alertErrorSuccess.html" -->
         
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
         
					<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
					<s:hidden id="codicePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.codice"/>
					<s:hidden id="descrizionePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.descrizione"/>
					<s:hidden id="daRicerca" name="step1Model.daRicerca" />
					<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					<%-- CR-2023 da eliminare
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>			
					
					<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
         
         <s:hidden id="hiddenPerEscludiAnnullati" name="model.ricercaModel.hiddenPerEscludiAnnullati" />
         
          <p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>            
          <div class="step-content">
           <div class="step-pane active" id="step1"> <br>
           <p>
          <!-- task-131 <s:submit name="cerca" value="cerca" method="ricercaAccertamento" cssClass="btn btn-primary pull-right" /> -->         	
          <s:submit name="cerca" value="cerca" action="ricercaAccertamento_ricercaAccertamento" cssClass="btn btn-primary pull-right" />
          </p><br>
          <h4>Accertamento</h4>
          <fieldset class="form-horizontal">  
            <div class="control-group">
              <label class="control-label" for="annoEsercizio">Anno</label>
              <div class="controls">  
                <s:textfield id="annoMovimento" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoMovimento" cssClass="span1" />
                <span class="al">
                  <label class="radio inline" onkeyup="return checkItNumbersOnly(event)" for="model.ricercaModel.numeroImpegno">Numero </label>
                </span>
                <s:textfield id="numeroImpegno" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpegno" cssClass="lbTextSmall span1"/> 
                <span class="al">
                  <label class="radio inline" for="stato">Stato</label>
                </span>
                <s:select list="listaStatoOperativoMovgest" id="listaStatoOperativoMovgest"  headerKey="" 
          		   headerValue="" name="model.ricercaModel.idStatoOperativoMovgest" cssClass="span2"  
       	 	       listKey="codice" listValue="descrizione" /> 
              </div>
            </div>
            
            <div class="control-group">
				<div class="control-label">Escludi annullati</div>
				<div class="controls">
   					<s:checkbox id="escludiAnnullatiCheckBox" name="model.ricercaModel.escludiAnnullati" onclick="impostaValoreEscludiAnnullati()"/>  		   
				</div>
			</div>
           
 
             <div class="control-group">
             <label class="control-label" for="piano">Competenza</label>
             	<div class="controls">					
                	<div class="radio inline">
			    		<s:radio id="competenze" cssClass="flagSesso" name="model.ricercaModel.competenze" list="competenzeList"/>
                	</div>
				</div>
			</div>  
			
			<!-- SIAC-7501 -->
           	<div class="control-group">
		  		<table class="span10 ">
		  			<tr>
		  				<td class="span4 ">
		  					<span class="control-label">Accertamento da riaccertamento</span>
		  					<div class="controls">    
					      		<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"></s:radio>
					    	</div>
		  				</td>
		  				<td rowspan="3" valign="middle" width="80%" class="span8 ">
		  					<span class="riaccVisible" id="bloccoRiaccertato">
	      						&nbsp; 
	      						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span2 " title="Anno" />&nbsp;
	      						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span4 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
	     			 		</span>
		  				</td>			
		  			</tr>
		  			<tr><td>&nbsp;</td></tr>
		  			<!-- SIAC-6997 -->
		  			<tr>
		  				<td>
		  					<span class="control-label">Da reimputazione in corso d&rsquo;anno</span>
		  					<div class="controls">
					      		<s:radio id="reanno" name="step1Model.reanno" cssClass="flagResidenza" list="step1Model.daReanno" onclick="check(this.value)"></s:radio>
					    	</div>
		  				</td>
		  			</tr>
		  		</table>
		  	</div>
		  	
			<!-- div class="control-group">
   				<span class="control-label">Accertamento da riaccertamento</span>
   				<div class="controls">    
 						<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"></s:radio> 
      				<span class="riaccVisible" id="bloccoRiaccertato">
     						&nbsp; 
     						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span1 " title="Anno" />&nbsp;
     						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span2 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
    			 		</span>
    		 		</div>
 				</div>
	  		<div class="control-group">
	    		<span class="control-label">Da reimputazione in corso d&rsquo;anno</span>
	    		<div class="controls">    
	      	  		<s:radio id="reanno" name="step1Model.reanno" cssClass="flagResidenza" list="step1Model.daReanno" onclick="check(this.value)"></s:radio>
	    		</div>
	  		</div--> 
            
            <!-- SIAC-6997 -->
            <div class="control-group">      
				          <label class="control-label">Struttura Compentente</label>
				          <div class="controls">   
				            <s:hidden name="step1Model.strutturaSelezionataCompetente" id="strutturaSelezionataCompetente" />
				                           
				            <div class="accordion span9" class="struttAmmCompetente">
				              <div class="accordion-group">
				                <div class="accordion-heading">    
				                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmCompetente" href="#4n">
				                 Seleziona la Struttura competente
				                  <i class="icon-spin icon-refresh spinner" id="spinnerStrutturaAmministrativaCompetente"></i></a>
				                </div>
				                <div id="4n" class="accordion-body collapse">
				                  <div class="accordion-inner" id="strutturaAmministrativaCompetenteDiv">
				                    <ul id="strutturaAmministrativaCompetente" class="ztree treeStruttAmm"></ul>
				                  </div>
				                  <div class="accordion-inner" id="strutturaAmministrativaCompetenteWait">
				                    Attendere prego..
				                  </div>
				                  
				                </div>
				              </div>
				            </div>
				          </div>
				        </div>
            
            <div class="control-group">
            	<label class="control-label" >Accertamento di origine</label>
              	<div class="controls">	
              	<s:textfield id="annoImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoImpOrigine" maxlength="4" cssClass="span1" title="Anno"/>	
              	<s:textfield id="numeroImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpOrigine" cssClass="lbTextSmall span2" title="Numero"/>	
              				        
              	</div>
            </div>	
            
              <!-- PROGETTO -->
   			<s:include value="/jsp/movgest/include/progetto.jsp" />
            	
            
             <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/capitolo.jsp" />
            
            <s:set var="consultaModificheProvvedimentoAction" value="%{'ricercaAccertamento_consultaModificheProvvedimento'}" />
			<s:set var="consultaModificheProvvedimentoSubAction" value="%{'ricercaAccertamento_consultaModificheProvvedimentoSub'}" />
                  
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />   
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />  
			
			
			<!--per modale provvedimento e elimina (incluse in modal.jsp) -->
		    <s:set var="selezionaProvvedimentoAction" value="%{'ricercaAccertamento_selezionaProvvedimento'}" />
			<s:set var="clearRicercaProvvedimentoAction" value="%{'ricercaAccertamento_clearRicercaProvvedimento'}" />	          
       		<s:set var="ricercaProvvedimentoAction" value="%{'ricercaAccertamento_ricercaProvvedimento'}" />	          
       		<s:set var="eliminaAction" value="%{'ricercaAccertamento_elimina'}" />	  
            
            <!--modale progetto -->
            <s:set var="selezionaProgettoCronopAction" value="%{'ricercaAccertamento_selezionaProgettoCronop'}" />	          
            <s:set var="selezionaProgettoAction" value="%{'ricercaAccertamento_selezionaProgetto'}" />		
            <s:set var="pulisciRicercaProgettoAction" value="%{'ricercaAccertamento_pulisciRicercaProgetto'}" />	          
            <s:set var="ricercaProgettoAction" value="%{'ricercaAccertamento_ricercaProgetto'}" />	          
            <s:set var="codiceProgettoChangedAction" value="%{'ricercaAccertamento_codiceProgettoChanged'}" /> 
	                
	        <s:set var="ricercaCapitoloAction" value="%{'ricercaAccertamento_ricercaCapitolo'}" />
	        <s:set var="pulisciRicercaCapitoloAction" value="%{'ricercaAccertamento_pulisciRicercaCapitolo'}" />
	        <s:set var="selezionaCapitoloAction" value="%{'ricercaAccertamento_selezionaCapitolo'}" />
	        <s:set var="visualizzaDettaglioCapitoloAction" value="%{'ricercaAccertamento_visualizzaDettaglioCapitolo'}" />
	        
	        
	        <!--  per soggetto -->
			<s:set var="selezionaSoggettoAction" value="%{'ricercaAccertamento_selezionaSoggetto'}" />
			<s:set var="pulisciRicercaSoggettoAction" value="%{'ricercaAccertamento_pulisciRicercaSoggetto'}" />	          
			<s:set var="ricercaSoggettoAction" value="%{'ricercaAccertamento_ricercaSoggetto'}" />	    
			<s:set var="listaClasseSoggettoChangedAction" value="%{'ricercaAccertamento_listaClasseSoggettoChanged'}" />
	       
	      
            <s:include value="/jsp/movgest/include/modal.jsp" /> 
          
            
            <!--modale progetto -->
			<s:include value="/jsp/movgest/include/modalProgetto.jsp"/>	
			<!--/modale progetto -->
			
			
			  <!-- Fine Modal -->
            
            <br/> <br/> 
            
            
		  <p class="margin-medium">
          	<s:include value="/jsp/include/indietro.jsp" />    
          	<a class="btn btn-secondary" href="">annulla</a>
          	<!-- task-131 <s:submit id="cerca" name="cerca" value="cerca" method="ricercaAccertamento" cssClass="btn btn-primary pull-right" /> -->         	
          	<s:submit id="cerca" name="cerca" value="cerca" action="ricercaAccertamento_ricercaAccertamento" cssClass="btn btn-primary pull-right" />
         
          </p>
            
          </fieldset>  

           </div>
		  </div> 

         
        </s:form>     	
    </div>	
  </div>	 
</div>	 
 
 
 
 
<script type="text/javascript">

	$(document).ready(function() {
		var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var riaccVisible = $('.riaccVisible');
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");
	if (riaccertatoNo.is(':checked')) {
			riaccVisible.hide();
			annoImpRiacc.hide();
			numImpRiacc.hide();
		}
		if (riaccertatoSi.is(':checked')) {
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		}


		
		$("#linkCompilazioneGuidataProgetto").click(function(){
		    	initRicercaGuidataProgetto($("#progetto").val());
			});
		
		$("#progetto").change(function(){
			var cod = $("#progetto").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="codiceProgettoChanged"></s:url>',
				url: '<s:url action="%{#codiceProgettoChangedAction}"/>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshDescrizioneProgetto").html(data);
				}
			});			
		});
		
		

		$("#linkCompilazioneGuidataCapitolo").click(function(){
			initRicercaGuidataCapitolo(
					$("#capitolo").val(), 
					$("#articolo").val(),
					$("#ueb").val()
			);
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
       
       //aggiunta RM xche non funziona la selezione del radio della sac (provvedimento)
       $("#cerca").click(function() {
    	   //funcion definita in genericCustom.js
    	   preselezionaStrutturaPaginaPrincipale();

		});	
				
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").html()
			);
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			$("#codCreditore").val("");
			$.ajax({
				//task-131 url: '<s:url method="listaClasseSoggettoChanged"/>',
				url: '<s:url action="%{#listaClasseSoggettoAction}"/>',			    
				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});


		//SIAC-6997- commentata
// 		riaccertatoNo.change(function(){
// 			riaccVisible.hide();
// 			annoImpRiacc.hide();
// 			numImpRiacc.hide();
// 		});
		
// 		riaccertatoSi.change(function(){
// 			riaccVisible.show();
// 			annoImpRiacc.show();
// 			numImpRiacc.show();
// 		});


		//SIAC-6997
		riaccertatoNo.change(function(){
			annoImpRiacc.hide();
			numImpRiacc.hide();
		});
		
		riaccertatoSi.change(function(){
			annoImpRiacc.show();
			numImpRiacc.show();
		});


		//inizio SIAC-6997
		if ($("#riaccertatoNo").is(':checked') || $("#reannoNo").is(':checked')) {
			$("#bloccoRiaccertato").hide();
			
		}
		if ($("#riaccertatoSi").is(':checked') || $("#reannoSi").is(':checked')) {	
			$("#bloccoRiaccertato").show();
			
		}
		
		
		function gestioneRiaccertatoFlagSi(){
			$("#bloccoRiaccertato").show();
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		}
		
		function gestioneRiaccertatoFlagNo(){
			$("#annoImpRiacc").val("");
			$("#numImpRiacc").val("");
			$("#bloccoRiaccertato").hide();
		}
		
		$("#reannoNo").change(function(){
			if($("#riaccertatoNo").is(':checked')){
				gestioneRiaccertatoFlagNo();
			}
		});
		
		$("#riaccertatoNo").change(function(){
			if($("#reannoNo").is(':checked')){
				gestioneRiaccertatoFlagNo();
			}
		});
		
		$("#riaccertatoSi").change(function(){
			gestioneRiaccertatoFlagSi();
			if($("#reannoSi").is(':checked')){
				$("#reannoNo").prop('checked', true);
				$("#reannoSi").prop('checked', false);
			}
		});
		
		$("#reannoSi").change(function(){		  
			gestioneRiaccertatoFlagSi();
			if($("#riaccertatoSi").is(':checked')){
				$("#riaccertatoNo").prop('checked', true);
				$("#riaccertatoSi").prop('checked', false);
			}
		});
		
		//fine SIAC-6997
		
	});
	
	
	function changeRiacc(){
		var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");
		var riaccVisible = $('.riaccVisible');
		if (riaccertatoNo.is(':checked')) {
			riaccVisible.hide();
			annoImpRiacc.hide();
			numImpRiacc.hide();
		}
		if (riaccertatoSi.is(':checked')) {
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		}
	}
	
	function impostaValoreEscludiAnnullati(){
		cbObj = document.getElementById("escludiAnnullatiCheckBox");
	    var valore = cbObj.checked;
	    $("#hiddenPerEscludiAnnullati").val(valore);
	}





	






	
</script>  


<script src="/siacfinapp/js/local/movgest/ricercaAccertamento.js" type="text/javascript"></script>

 
<s:include value="/jsp/include/footer.jsp" />