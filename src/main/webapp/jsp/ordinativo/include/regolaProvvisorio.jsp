<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<h4>Regolarizzazione provvisorio <i>(gestione)</i></h4>
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="num1">Numero</label>
			<div class="controls"> 
			 <s:textfield id="annoProvvisorio" readonly="true" cssClass="lbTextSmall span2" name="gestioneOrdinativoStep3Model.provvisorio.anno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>			      
				<span class="alRight">
				 <s:textfield id="numeroProvvisorio" readonly="true" cssClass="lbTextSmall span2" name="gestioneOrdinativoStep3Model.provvisorio.numero" onkeyup="return checkItNumbersOnly(event)"></s:textfield>			   
				</span>
			</div>
		</div>
	  
		<div class="control-group">
			<label class="control-label" for="OggCassa">Oggetto</label>
			<div class="controls">  
			   <s:textfield id="oggettoProvvisorio" readonly="true"  cssClass="lbTextSmall span9" name="gestioneOrdinativoStep3Model.provvisorio.causale" ></s:textfield>	
			   
			   
			    <s:if test="!oggettoDaPopolarePagamento()"> 
				   <span class="al">
					<label class="radio inline" for="riportaInTestata">Riporta in testata</label>
					</span>
			  		<%-- <s:checkbox id="riportaInTestata" name="gestioneOrdinativoStep3Model.provvisorio.riportaInTestata" fieldValue="true" /> --%>
			  		<s:if test="!sonoInAggiornamentoOR()">
			  			<s:checkbox id="riportaInTestata" name="gestioneOrdinativoStep3Model.riportaInTestataPageInserisciProvvisorio" fieldValue="true" />
			  		</s:if>
			  		<s:else>
			  			<s:checkbox id="riportaInTestata" name="gestioneOrdinativoStep3Model.riportaInTestataPageInserisciProvvisorio" fieldValue="true" disabled="true"/>
			  		</s:else>	
		  		</s:if>
		  			   
			</div>
			
			
			
		</div>
		
		<div class="control-group">
			<label class="control-label" for="creditoreCassa">Creditore</label>
			<div class="controls">    
		  <s:textfield id="creditoreProvvisorio" readonly="true" cssClass="lbTextSmall span9" name="gestioneOrdinativoStep3Model.provvisorio.denominazioneSoggetto" ></s:textfield>			   
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="impCassa">Importo</label>
			<div class="controls">   
				<s:textfield id="importoColl" cssClass="lbTextSmall span3 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" 
				             name="gestioneOrdinativoStep3Model.provvisorio.importoFormatString" maxlength="12" ></s:textfield>			   
				<span class="alRight">
					<label class="radio inline" for="impReCassa">Da regolarizzare</label>
				</span>
				<s:textfield id="impDaReg" disabled="true" cssClass="lbTextSmall span3 soloNumeri decimale" name="gestioneOrdinativoStep3Model.provvisorio.importoDaRegolarizzare" ></s:textfield>			   
			</div>
		</div>
		
	</fieldset>
	<script type="text/javascript">
	
		$(document).ready(function() {
			$(".soloNumeri").allowedChars({numeric: true});
		
		 	/* Formattazione corretta dei campi numerici */
	   		 $(".decimale").gestioneDeiDecimali();
		 	
	   		/* $(".riportaInTestata").change(function(){
		   		
	   			$('#riportaInTestata').prop('checked', true); // Checks it
		   		$('#riportaInTestata').prop('checked', false); // Unchecks it
		   		
	   		} */
		 	
	   		
	   	 //alert("xxxx: "+$('#riportaInTestata').val($(this).is(':checked')));

	     /* $('#riportaInTestata').change(function() {
	         $('#riportaInTestata').val($(this).is(':checked'));
	     }); */

	     /* $('#riportaInTestata').change(function() {
	         if (!$(this).is(':checked')) {
	        	 $('#riportaInTestata').prop('checked', true);
	         }
	     }); */
	   		
		});
		

	
	</script>					 