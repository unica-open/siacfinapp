<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<s:if test="oggettoDaPopolarePagamento()">
	<s:set var="aggiornaCoperturaAction" value="%{'gestioneOrdinativoPagamentoStep3_aggiornaCopertura'}" />	   		           								
</s:if>
<s:else>
	<s:set var="aggiornaCoperturaAction" value="%{'gestioneOrdinativoIncassoStep3_aggiornaCopertura'}" />	   		           								
</s:else>

<!-- Modal aggProvvisorioCassa-->
<!-- <div id="aggProvvisorioCassa" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggProvvisorioCassaLabel" aria-hidden="true"> -->
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Regolarizzazione provvisorio </h4>
	</div>
	<div class="modal-body">       
		<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="num1">Numero</label>
			<div class="controls">    
				
				<s:textfield id="num1" cssClass="lbTextSmall span2" readonly="true" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.anno" />
				<span class="alRight">
					<s:textfield id="num1" cssClass="lbTextSmall span2" readonly="true" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.numero" />	
				</span>
			</div>
		</div>
	  
		<div class="control-group">
			<label class="control-label" for="OggCassa">Oggetto</label>
			<div class="controls">    
			    <s:textfield id="OggCassa" cssClass="lbTextSmall span9" readonly="true" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.causale" /> 
				
				 <s:if test="!oggettoDaPopolarePagamento()"> 
				
					<span class="al">
					<label class="radio inline" for="riportaInTestata">Riporta in testata</label>
					</span>
					<s:if test="!sonoInAggiornamentoOR()">
			  			<s:checkbox id="riportaInTestataPageAggiornaProvvisorio" name="gestioneOrdinativoStep3Model.riportaInTestataPageAggiornaProvvisorio" onclick="impostaValoreCheckbox()"/>
			  		</s:if>
			  		<s:else>
				  		<s:checkbox id="riportaInTestataPageAggiornaProvvisorio" name="gestioneOrdinativoStep3Model.riportaInTestataPageAggiornaProvvisorio" disabled="true"/>
				  	</s:else>	
					<s:hidden id="hiddenRiportaInTestataPageAggiornaProvvisorio" name="gestioneOrdinativoStep3Model.hiddenRiportaInTestataPageAggiornaProvvisorio" />
				
				</s:if>
				
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="creditoreCassa">Creditore</label>
			<div class="controls">
			    <s:textfield id="creditoreCassa" cssClass="lbTextSmall span9" readonly="true" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.denominazioneSoggetto" />  
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="impCassa">Importo</label>
			<div class="controls">                         
				
				 <s:textfield id="impCassa" cssClass="lbTextSmall span3 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)"
				              name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.importoFormatString" /> 
				
				
				<s:if test="!oggettoDaPopolarePagamento()"> 
					<span class="alRight">
						<label class="radio inline" for="impReCassa">Da regolarizzare</label>
					</span>
					 <s:textfield id="impReCassa" cssClass="lbTextSmall span3" readonly="true" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.importoDaRegolarizzare" />
				 </s:if>  
				
			</div>
		</div>
		
	</fieldset>
	        <!-- numero provvisorio -->
            <s:hidden id="numQuotaPopUp" name="gestioneOrdinativoStep3Model.visualizzaProvvisorio.idProvvisorioDiCassa"/> 
	</div>
	<div class="modal-footer">
<!-- 		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button> -->
		<!-- task-131 <s:submit id="submitBtnAggCopertura" name="btnAggCopertura" value="conferma" cssClass="btn btn-primary" method="aggiornaCopertura"/> -->
		<s:submit id="submitBtnAggCopertura" name="btnAggCopertura" value="conferma" cssClass="btn btn-primary" action="%{#aggiornaCoperturaAction}"/>
	</div>
<!-- </div>  -->
<!-- /Modal --->
<script type="text/javascript">
	$(document).ready(function() {
		$(".soloNumeri").allowedChars({numeric: true});
		 /* Formattazione corretta dei campi numerici */
    	$(".decimale").gestioneDeiDecimali();
		
		 
    	
	});
	
	
	
	function impostaValoreCheckbox(){
		
		cbObj = document.getElementById("riportaInTestataPageAggiornaProvvisorio");
        /* if (cbObj.checked)    	
            alert("CheckBox spuntato");
        else{
            alert("CheckBox non spuntato"); */
		
        var valore = cbObj.checked;
        
        $("#hiddenRiportaInTestataPageAggiornaProvvisorio").val(valore);
        
        //alert("valore hidden: " + $("#hiddenRiportaInTestataPageAggiornaProvvisorio").val());
	}
	
</script>