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
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
			
       			 <s:form  method="post" cssClass="form-horizontal" id="form">
       			 
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					
					
					<h3>Ricerca conto corrente</h3>


					
					
					<div class="step-content">
			<div class="step-pane active" id="step1">
			
			<fieldset class="form-horizontal margin-large">



				<div class="control-group">
					<label class="control-label" for="anno">Anno *</label>

					<div class="controls">
					<s:select id="anno" list="elencoAnniBilancio" name="criteriRicerca.anno" cssClass="span1"  />
					</div>
				</div>


				<div class="control-group">
					<label for="conto" class="control-label">Conto corrente *</label>
					<div class="controls">
						<s:hidden id="codiceConto" name="criteriRicerca.contoCorrente" />
						<s:select id="conto" list="elencoContiCorrenti" name="criteriRicerca.idClassifConto" cssClass="span3"
							headerKey="" 
							headerValue="" 
							listKey="uid" 
							disabled="%{elencoContiCorrenti.empty}"
							listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>

				<div class="control-group">
					<label class="control-label" for="">Data</label>
					<div class="controls">
						<span class="al">
							<label for="dataInizio" class="radio inline">Da</label>
						</span>
						<input type="text" name="criteriRicerca.dataInizioStr" value="" id="dataInizio" 
							class="lbTextSmall span2 datepicker" placeholder="gg/mm/aaaa" tabindex="-1">
						<span class="al">
							<label for="dataFine" class="radio inline">A</label>
						</span>
						<input type="text" name="criteriRicerca.dataFineStr" value="" id="dataFine" 
							class="lbTextSmall span2 datepicker" placeholder="gg/mm/aaaa" tabindex="-1">
					</div>
				</div>
				
			</fieldset>  

		</div>
		</div>
			
          <p class="margin-medium">
			  <a class="btn btn-secondary" href="<s:property value="#urlCruscotto" />">indietro</a>   
			  <a class="btn btn-secondary" href="ricercaContoCorrente.do">annulla</a>
			  
 			  <s:submit cssClass="btn btn-primary pull-right" method="cerca" value="cerca" />  
 			  <s:submit id="reload" cssClass="nascosto" method="execute" value="" />  

		  </p>
		  
		  
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
$(document).ready(function() {
	$('#anno').change(function() {
		  $('#reload').trigger('click');
	});


	$('#conto').change(function() {
		$('#codiceConto').val($( "#conto option:selected" ).text());
	});
	
});
	
</script>  

<s:include value="/jsp/include/footer.jsp" />