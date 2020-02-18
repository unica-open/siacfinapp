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
	<!-- NAVIGAZIONE -->
	<s:include value="/jsp/include/header.jsp" />  	
	<!-- /NAVIGAZIONE -->
	<hr />
<div class="container-fluid-banner">
	<a name="A-contenuti" title="A-contenuti"></a>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">    
		 
<!-- 		 <form method="get" action="#">   -->
		 <s:form id="gestioneCartaNuovaRigaDaDocumenti" action="nuovaRigaDaDocumentiStep1.do" method="post">
		 
		 
			<!--#include virtual="include/alertErrorSuccess.html" -->
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			<h3><s:property value="model.titoloStep"/></h3>
			
			<div id="MyWizard" class="wizard">
				<ul class="steps">
					<li data-target="#step1" class="active"><span class="badge">1</span>Ricerca documenti<span class="chevron"></span></li>
					<li data-target="#step2"><span class="badge">2</span>Associa documenti<span class="chevron"></span></li>
				</ul>
			</div>
			
			<div class="step-content">
				<div class="step-pane active" id="step1">    
				 <h4>Documenti</h4>
				 
					<fieldset class="form-horizontal">
						
						<!--   
						<div class="control-group">
							<label class="control-label">Tipo documento</label>
							<div class="controls"> 
								<select class="span8" id="tipoDocumento" name="tipoDocumento">
									<option value="">xxxxxx</option>
									<option value="">xxxxxxx</option>
								</select>				  
							</div>
						</div>  -->
						
						
						<div class="control-group">
							<label class="control-label">Tipo documento</label>
								<div class="controls">
					  				<s:if test="listaDocTipoSpesa!=null">
										<s:select list="listaDocTipoSpesa"
											id="listaDocTipoSpesa"
											cssClass="parametroRicercaCapitolo"
											headerKey="" headerValue=""
											name="codiceTipoDoc"
											listKey="id" listValue="codice+' - '+descrizione" />
 									</s:if>
					
								</div>
						</div>
						
						
						
						<div class="control-group">
							<label class="control-label">Documento</label>
							<div class="controls">   
								<input id="datiDocumentoAnno" name="datiDocumentoAnno" class="lbTextSmall span1" type="text" value="" maxlength="4" placeholder="Anno"/>
								<input id="datiDocumentoNumero"  name="datiDocumentoNumero" class="lbTextSmall span1" type="text" value="" placeholder="numero" />  
								<input id="datiDocumentoQuota" name="datiDocumentoQuota" class="lbTextSmall span2" type="text" value="" placeholder="quota" />      
								<span class="alRight">
									<label class="radio inline">Data documento</label>
								</span>
								<input id="dataDocumento" name="dataDocumento" class="span2 datepicker" type="text" value="" placeholder="11/05/2013" />                  
							</div>
						</div>
			
											  
						<!--#include virtual="include/soggetto.html" -->
						<div id="refreshHeaderSoggetto">
				          	<s:include value="/jsp/carta/include/headerSoggettoCarta.jsp"/>		       	
					       	<s:include value="/jsp/carta/include/soggettoCarta.jsp" /> 
					       	<s:include value="/jsp/include/modalSoggetto.jsp" />									       	
			        	</div>
						
						
						
						<h4>Elenco</h4>
						
						<div class="control-group">
							<label class="control-label">Anno </label>
							<div class="controls">   
								<input id="elencoAnno" name="elencoAnno" class="lbTextSmall span1" type="text" value="" maxlength="4" />    
								<span class="alRight">
									<label class="radio inline">Numero </label>
								</span>
								<input id="elencoNumero" name="elencoNumero" class="lbTextSmall span2" type="text" value="" />                  
							</div>
						</div>
					
					
					<!-- provvedimento -->
						<s:include value="/jsp/carta/include/provvedimentoRicercaRigaDoc.jsp" />									
					<!-- provvedimento -->
						
							
					</fieldset>    
				</div>
			</div>
			
			
			<!--#include virtual="include/modal.html" -->
			<s:include value="/jsp/include/modalProvvedimenti.jsp" />
			
			<p class="margin-medium">
				<s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary"/>
				<!--  <a class="btn btn-secondary" href="#">annulla</a> -->
				<s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary" />
				 
				<span class="pull-right">
					<!-- <a class="btn btn-primary" href="FIN-insRigaDocStep2.shtml">cerca</a> -->
					<s:submit name="prosegui" value="cerca" method="cerca" cssClass="btn btn-primary" />
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

</script>  


<s:include value="/jsp/include/footer.jsp" />

