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
		
		   <s:form method="post" action="ricercaCarta.do" id="ricercaCarta">
			<!--#include virtual="include/alertErrorSuccess.html" -->
			 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<h3>Ricerca Carta Contabile</h3>
			<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
			
			
			<div class="step-content">
				<div class="step-pane active" id="step1">
					
					<p class="margin-medium">
					  <s:submit name="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />  
					</p>
					<br>
					<h4>Dati carta</h4>
					
					<fieldset class="form-horizontal">	
						<div class="control-group">
							<label class="control-label">Numero carta </label>
							<div class="controls">  
								<span class="al">
									<label class="radio inline">Da </label>
								</span>
								<s:textfield id="numeroCartaDa" name="numeroCartaDa" onkeyup="return checkItNumbersOnly(event)"  cssClass="span2 soloNumeri" maxlength="9"/>
								<span class="al">
									<label class="radio inline">A</label>                
								</span>
								<s:textfield id="numeroCartaA" name="numeroCartaA"  onkeyup="return checkItNumbersOnly(event)" cssClass="span2 soloNumeri" maxlength="9"/>  
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">Data scadenza </label>
							<div class="controls">  
								<span class="al">
									<label class="radio inline">Da</label>
								</span>
								
								<s:textfield id="dataScadenzaCartaDa" name="dataScadenzaCartaDa" cssClass="span2 datepicker" maxlength="10"/>  
								<span class="al">
									<label class="radio inline" for="ScadenzaCartaContabileA">A</label>                
								</span>
								
								<s:textfield id="dataScadenzaCartaA" name="dataScadenzaCartaA" cssClass="span2 datepicker" maxlength="10"/>  
							</div>
						</div>
						
						
						
						<div class="control-group">
							<label class="control-label">Stato</label>
							<div class="controls">
								 <s:if test="null!=listaStatoCartaContabile"> 
		   							<s:select list="listaStatoCartaContabile" id="listaStatoCartaContabile" name="statoCarta" cssClass="span4" 
		   							          headerKey="" headerValue=""
		   							   		  listKey="codice" listValue="descrizione" />
		   						 </s:if>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">Descrizione</label>
							<div class="controls">
							
							    <s:textarea id="descrizioneCarta" name="descrizioneCarta" rows="1" cols="15" cssClass="span9"  />
							</div>
						</div>
						
						 <s:hidden id="toggleImputazioneContabiliAperto" name="toggleImputazioneContabiliAperto" />
						
 							<s:include value="/jsp/carta/include/provvedimentoCarta.jsp" />									
						
						<div class="accordion" id="ImpContaRicCartaContabile">
							<div class="accordion-group">
								
								<div class="accordion-heading">    
									<a class="accordion-toggle collapsed" data-toggle="collapse" id="imputazioni" data-parent="#ImpContaRicCartaContabile" href="#ImputazionContRigaMovimento">Imputazioni contabili<span class="icon">&nbsp;</span></a>
								</div>
								
								<div id="ImputazionContRigaMovimento" class="accordion-body collapse">
									<div class="accordion-inner">
									
									 
									<div id="refreshHeaderCapitolo">
		            						<s:include value="/jsp/ordinativo/include/headerCapitoloOrdinativoRicerca.jsp"/>		            	
		           					 </div>             
										<s:include value="/jsp/carta/include/capitoloCarta.jsp" />							
										<s:include value="/jsp/carta/include/impegnoCarta.jsp" />
										
										<div id="refreshHeaderSoggetto">
		            						<s:include value="/jsp/ordinativo/include/headerSoggettoOrdinativoRicerca.jsp"/>
		            					</div>							
										<s:include value="/jsp/carta/include/soggettoCarta.jsp" />
									</div>
								</div>
								
							</div>
						</div>
						
						
					</fieldset>
						
					 

				</div>
			</div>
			  
			
			<!-- Modal -->
			<s:include value="/jsp/include/modalCapitolo.jsp" />
            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
            <s:include value="/jsp/include/modalSoggetto.jsp" />
            <s:include value="/jsp/carta/include/modalImpegnoCarta.jsp" />
            <!-- Fine Modal -->  
			  
			  
<%-- 			<s:include value="/jsp/carta/include/modalCarta.jsp" /> --%>
			<p class="margin-medium">
			
				<s:include value="/jsp/include/indietro.jsp" />
			
				<s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary" />  
				<span class="pull-right">
					<s:submit name="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />  
				</span>
			</p>       
			  
		  </s:form>
		  
		  
		</div>
	</div>	 
</div>	



<script type="text/javascript">

// $(window).load(function() {
// 	// apertura automatica struttura amministrativa
// 	if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
	 	   
// 	       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
	           
// 	    	   $("#testataStruttura").click();
	    	   
// 	    	   $("#lineaStruttura").click();
	    	   
	    	   
// 	       }
// 	}    
	
// });	

$(document).ready(function() {
	
	
	var toggleImputazioneContabiliAperto = $("#toggleImputazioneContabiliAperto").val();
	if(toggleImputazioneContabiliAperto=="true"){
		$("#imputazioni").click();
	}
	
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
			
	$("#linkCompilazioneGuidataSoggetto").click(function(){
		initRicercaGuidataSoggetto(
			$("#codCreditore").val(),
			null
		);
	});
	
	
	$("#linkCompilazioneGuidataImpegno").click(function(){
		initRicercaGuidataImpegno(
				$("#annoImpegno").val(), 
				$("#numeroImpegno").val()
		);
	});
	
	$("#cercaCapitoloSubmit").click(function(){
		$("#capitolo").attr("disabled", true);
		$("#articolo").attr("disabled", true);
		$("#ueb").attr("disabled", true);
	});

	

			
	
});	

</script>  
 
<s:include value="/jsp/include/footer.jsp" />