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
					
					
					<h3>Consulta conto corrente al <s:date name="data" format="dd/MM/yyyy" /></h3>


					
					
					<div class="step-content">
			<div class="step-pane active" id="step1">
			
			<fieldset class="form-horizontal margin-large">



				<div class="control-group">
					<label class="control-label" for="anno">Anno</label>

					<div class="controls">
						<s:property value="vociContoCorrente.anno"/>
					</div>
				</div>


				<div class="control-group">
					<label for="conto" class="control-label">Conto corrente</label>
					<div class="controls">
						<s:property value="vociContoCorrente.contoCorrente"/>
					</div>
				</div>

				
				<div class="control-group">
					<label class="control-label" for="saldoIniziale">Saldo iniziale </label>

					<div class="controls">
					<input type="text" id="saldoIniziale"  value="<s:property value="vociContoCorrente.saldoIniziale" />" class="span3 importo" readonly="readonly" />
					</div>
				</div>
	
						<div class="control-group">
					<label class="control-label" for="saldoCassaPrec">Saldo cassa al <s:date name="dataPrec" format="dd/MM/yyyy" /> </label>

					<div class="controls">
					<input type="text" id="saldoCassaPrec"  value="<s:property value="saldoCassaPrec" />" class="span3 importo" readonly="readonly" />
					</div>
				</div>
	
		
				<div class="control-group">
					<label class="control-label" for="saldoCassa">Saldo cassa al <s:date name="data" format="dd/MM/yyyy" /> </label>

					<div class="controls">
					<input type="text" id="saldoCassa"  value="<s:property value="saldoCassa" />" class="span3 importo" readonly="readonly" />
					</div>
				</div>
	
	
		  
						<br/>
				<br/>
	
				
	
	
		  
		  
		  
				
			</fieldset>  

		</div>
		</div>
			
          <p class="margin-medium">
			  <a class="btn btn-secondary" href="consultaContoCorrente.do">indietro</a>   
			  

		  </p>
		  
		  
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
$(document).ready(function() {






	var selected = false;
	
	$('input.importo').keypress(function(e) 
	{
		if (e.which < 32)
			return true;

	    var t = [$(this).val().slice(0, this.selectionStart), e.key, $(this).val().slice(this.selectionEnd)].join('');

	    if (t.match(/^([1-9]\d*|0)(\,|\,\d{0,2})?$/) != null) {
	     // $(this).val(t);
	      selected = false;
	      return true;
	    }

	    return false;

	  }).focus(function(e) {
	    $(this).val($(this).val().replace(/\./g, ''));
	    
	  }).blur(function(e) {
	    $(this).val($(this).val().replace(/\./g, ''));
	    
	    if ($(this).val().match(/^([1-9]\d*|0)(\,|\,\d{0,2})?$/) == null)
	      $(this).val('')
	    else
	      $(this).val($(this).val().replace(/[^\d\,]/g, '').replace(/^(\d+)\,?$/, '$1,00').replace(/(\,\d)$/, '$10').replace(/\B(?=(\d{3})+(?!\d))/g, '.'));
	      
	  }).select(function(e) {
	    selected = true;
	  });
});
	
</script>  

<s:include value="/jsp/include/footer.jsp" />