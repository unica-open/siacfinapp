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
        <s:form method="post" nane="ricercaAccertamento" action="ricercaAccertamento.do" cssClass="form-horizontal">  
         <s:include value="/jsp/include/actionMessagesErrors.jsp" />
               
         <h3>Ricerca Accertamento</h3>
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
          	<s:submit name="cerca" value="cerca" method="ricercaAccertamento" cssClass="btn btn-primary pull-right" />
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
			
			<div class="control-group">
    			<span class="control-label">Accertamento da riaccertamento</span>
    			<div class="controls">    
  				<!--CHIEDERE COME GESTIRE QUESTI RADIO  -->
      				<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"></s:radio> 

      				<!-- CAMPI VISIBILI SE RADIOBUTTON SI=CHECKED -->       
      				<span class="riaccVisible">
      					&nbsp; <s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span1 " title="Anno" />&nbsp;
      					<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span2 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
     			 	</span>
     		 	<!-- FINE CAMPI VISIBILI -->      
   			 </div>
  			</div>
            <div class="control-group">
            	<label class="control-label" >Accertamento di origine</label>
              	<div class="controls">	
              	<s:textfield id="annoImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoImpOrigine" maxLength="4" cssClass="span1" title="Anno"/>	
              	<s:textfield id="numeroImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpOrigine" cssClass="lbTextSmall span2" title="Numero"/>	
              				        
              	</div>
            </div>	
            
              <!-- PROGETTO -->
   			<s:include value="/jsp/movgest/include/progetto.jsp" />
            	
            
             <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/capitolo.jsp" />
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />   
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />  
			
			
			<!-- Modal -->
			
             <s:include value="/jsp/movgest/include/modal.jsp" /> 
          
            
            <!--modale progetto -->
			<s:include value="/jsp/movgest/include/modalProgetto.jsp"/>	
			<!--/modale progetto -->
			
			
			  <!-- Fine Modal -->
            
            <br/> <br/> 
            
            
		  <p class="margin-medium">
          	<s:include value="/jsp/include/indietro.jsp" />    
          	<a class="btn btn-secondary" href="">annulla</a>
          	<s:submit id="cerca" name="cerca" value="cerca" method="ricercaAccertamento" cssClass="btn btn-primary pull-right" />
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
				url: '<s:url method="codiceProgettoChanged"></s:url>',
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
				url: '<s:url method="listaClasseSoggettoChanged"/>',
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

		riaccertatoNo.change(function(){
			riaccVisible.hide();
			annoImpRiacc.hide();
			numImpRiacc.hide();
		});
		
		riaccertatoSi.change(function(){
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		});
		
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