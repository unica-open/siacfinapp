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
                    
	<!-- NAVIGAZIONE
	<p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
	<ul id="sommario" class="nascosto">
		<li><a href="#A-contenuti">Salta ai contenuti</a></li>
	</ul>
	/NAVIGAZIONE -->
	<hr />
<div class="container-fluid-banner">

	<a name="A-contenuti" title="A-contenuti"></a>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">    
				
       <s:form id="gestioneCartaStep1" action="gestioneCartaStep1.do" method="post">
<%--      <s:form id="%{labels.FORM}" action="%{labels.FORM}.do" method="post">   --%>
		 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<!--#include virtual="include/alertErrorSuccess.html" -->
					<!-- <h3>Inserimento carta contabile</h3> -->
					<h3><s:property value="model.titoloStep"/></h3>
					
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active"><span class="badge">1</span>Dati testata<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Dettaglio<span class="chevron"></span></li>
						</ul>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">  
							<!--#include virtual="include/provvedimento.html" -->
							<!-- provvedimento -->
 							<s:include value="/jsp/carta/include/provvedimentoCarta.jsp" />									
							<!-- provvedimento -->
							
							<h4 class="step-pane">Informazioni</h4>	
							<fieldset class="form-horizontal">


							<div class="control-group">
							
									<label class="control-label">Firma 1
										<a class="tooltip-test" title="<s:property value="intestazioneFirma1"/>" href="#" data-toggle="modal">
											<i class="icon-info-sign">&nbsp;<span class="nascosto"><s:property value="intestazioneFirma1"/></span></i>
										</a>
									</label>
									<div class="controls">
										<s:textfield name="firma1" cssClass="lbTextSmall span9" />         
									</div>
							</div>
							
							<div class="control-group">
									<label class="control-label">Firma 2
										<a class="tooltip-test" title="<s:property value="intestazioneFirma2"/>" href="#" data-toggle="modal">
											<i class="icon-info-sign">&nbsp;<span class="nascosto"><s:property value="intestazioneFirma2"/></span></i>
										</a>
									</label>
									<div class="controls">
										<s:textfield name="firma2" cssClass="lbTextSmall span9" />     
									</div>
							</div>
							
							<s:if test="aggiornamento">
								<div class="control-group">
									<label class="control-label">Numero Registrazione </label>
									<div class="controls">
										<s:textfield id="numeroRegistrazione" name="numeroRegistrazione" cssClass="lbTextSmall span9" disabled="disableCampoNumeroRegistrazione()"/>         
									</div>
								</div>
							</s:if>
							<s:else>
								<div class="control-group">
									<label class="control-label">Pagamento in valuta estera</label>
									<div class="controls">
										<!-- <input id="optionsPagamentoEstero" name="optionsPagamentoEstero" type="checkbox" value=""  /> -->
										<s:checkbox id="optionsPagamentoEstero" name="optionsPagamentoEstero"/>
									</div>
								</div>
							</s:else>
								
								<div class="control-group">
									<label class="control-label">Causale *</label>
									<div class="controls">
										<s:textarea id="causaleCarta" name="causaleCarta" rows="1" cols="15" cssClass="span9" disabled="disableStep1()" />        
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Descrizione</label>								
									<div class="controls">
										<s:textfield id="descrizioneCarta" name="descrizioneCarta" cssClass="lbTextSmall span9" disabled="disableStep1()"/> 
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Motivo urgenza </label>
									<div class="controls">
										<!-- <input id="MotivoUrgenzaCartaContabile" name="MotivoUrgenzaCartaContabile" class="span9" type="text" value="" /> -->
										<s:textfield id="motivoUrgenzaCarta" name="motivoUrgenzaCarta" cssClass="lbTextSmall span9" disabled="disableStep1()"/>         
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Note </label>
									<div class="controls">
										<!-- <textarea id="NoteCartaContabile" name="NoteCartaContabile" rows="2" cols="15" class="span9" type="text" value="" ></textarea> -->
										<s:textarea id="noteCarta" name="noteCarta" rows="2" cols="15" cssClass="span9" disabled="disableStep1()"  />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Data scadenza *</label>
									<div class="controls">
										<!-- <input id="DataValutaCartaContabile" name="DataValutaCartaContabile" class="span2 datepicker" type="text" value="" > -->
										<s:textfield id="dataScadenzaCarta" title="gg/mm/aaaa" name="dataScadenzaCarta" cssClass="lbTextSmall span2 datepicker" disabled="disableStep1()"></s:textfield>
									</div>
								</div>
								
							
								<!-- <div class="accordion" id="AccpagEstero" style="display:none"> -->
							<s:if test="aggiornamento">
								<s:if test="optionsPagamentoEstero">
									<div class="accordion" id="AccpagEstero">
								</s:if>
								<s:else>
									<div class="accordion" id="AccpagEstero" style="display:none">
								</s:else>
							</s:if>
							<s:else>
								<div class="accordion" id="AccpagEstero">
							</s:else>
							
									<div class="accordion-group">
										<div class="accordion-heading">    
											<%-- <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#AccpagEstero" href="#PagamValEstera">Pagamenti valuta estera<span class="icon">&nbsp;</span></a> --%>
											<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#AccpagEstero" href="#PagamValEstera">Pagamenti valuta estera *<span class="icon">&nbsp;</span></a>
										</div>
										<div id="PagamValEstera" class="accordion-body collapse">
											<div class="accordion-inner">  
												<!--#include virtual="include/valutaEstera.html" -->  	
												<s:include value="/jsp/carta/include/valutaEstera.jsp" />
											</div>
										</div>
									</div>
								</div> 
		
							</fieldset>
						</div>
					</div>
					
					<!--#include virtual="include/modal.html" -->
            		<s:include value="/jsp/include/modalProvvedimenti.jsp" />
            		
            		<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>


					<p class="margin-medium">
						<!-- <a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a> -->
						<s:include value="/jsp/include/indietro.jsp" />
						<!-- <a class="btn btn-secondary" href="">annulla</a> -->
						<s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary" />
						
						<span class="pull-right">
							<span class="pull-right">
							<s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary" />
							<s:submit name="salva" value="salva" method="salvaCarta" cssClass="btn btn-primary" disabled="!aggiornamento"/>
							</span>
						</span>					
					</p>       
			
      </s:form>
				
		</div>		  
	</div>	 
</div>	

<script type="text/javascript">

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


});	

/* $("#annoProvvedimento").attr("disabled", false);
$("#numeroProvvedimento").attr("disabled", false);
$("#listaTipiProvvedimenti").attr("disabled", false);
 */
 
$("#optionsPagamentoEstero").change(function() {
	 var $input = $( this );
	 $("#AccpagEstero").toggle($input.prop( "checked" ));
}).change();

<s:if test="disableProvvedimento()">
	$('#annoProvvedimento').attr('disabled', true);
	$('#numeroProvvedimento').attr('disabled', true);
	$('#listaTipiProvvedimenti').attr('disabled', true);
	$('#linkCompilazioneGuidataProvvedimento').attr('disabled', true);
	$('#linkCompilazioneGuidataProvvedimento').removeAttr('href');
</s:if>



	

</script>  


<s:include value="/jsp/include/footer.jsp" />
