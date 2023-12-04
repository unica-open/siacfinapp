<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Quota Numero: <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.numeroQuota"/></h4>		 
</div>


<s:if test="oggettoDaPopolarePagamento()">
	<s:set var="aggiornaQuotaOrdinativoAction" value="%{'gestioneOrdinativoPagamentoStep2_aggiornaQuotaOrdinativo'}" />	   		           								
</s:if>
<s:else>
	<s:set var="aggiornaQuotaOrdinativoAction" value="%{'gestioneOrdinativoIncassoStep2_aggiornaQuotaOrdinativo'}" />	   		           								
</s:else>


<div class="modal-body">
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="anno1">Descrizione*</label>
			<div class="controls">    
			  <s:textfield id="descPopUp" name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.descrizione" cssClass="lbTextSmall span9"  />	  
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="anno1">Importo*</label>
			<div class="controls">                         
			
			  <s:if test="editaImportoQuota() && (isSonoInAggiornamento() || isSonoInAggiornamentoIncasso() )  "> 
			  	<s:textfield id="impQuotaOrdinativo" cssClass="lbTextSmall span2 soloNumeri decimale" onkeyup="return checkItNumbersCommaAndDotOnly(event)" 
			               name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.importoQuotaFormattato" maxlength="15" /> 
			
			  </s:if>
			  <s:else>
			  	<s:textfield id="impQuotaOrdinativo" cssClass="lbTextSmall span2 soloNumeri decimale"  onkeyup="return checkItNumbersCommaAndDotOnly(event)"
			               name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.importoQuotaFormattato" maxlength="15" readonly="true"/> 
			  </s:else>
			  
			  <s:if test="oggettoDaPopolarePagamento()"> 
				  <span class="al">
				  
					<label class="radio inline" for="capitolo">Data esecuzione pagamento</label>
				  </span>
				  
				  <s:if test="gestioneOrdinativoStep2Model.listaSubOrdinativiPagamenti.size == 1">
					  <s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa"
					  			   name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataEsecuzionePagamento" />
				  </s:if>
				  <s:else> 
				  
				  	<s:if test="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.editaDataEsecuzionePagamento">
				  	
				  		<s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa"
					  			   name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataEsecuzionePagamento" />
					</s:if>
					<s:else>  			   
				  		<s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa"
					  			   name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataEsecuzionePagamento" disabled="true"/>
					
					</s:else>
					  			   
				 </s:else>		   
				  		
			  </s:if>
			  <s:else>
				  <span class="al">
					  
						<label class="radio inline" for="capitolo">Data Scadenza ordinativo</label>
					  </span>
					<s:if test="gestioneOrdinativoStep2Model.listaSubOrdinativiIncasso.size == 1">	  
					  <s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa"
					  			   name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataScadenzaSubOrd" />
					</s:if>
				  	<s:else>
				  		<s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa"
					  	name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataScadenzaSubOrd" disabled="true" />				  	
				  	 </s:else>
				  						  			   
			  </s:else>
			</div>
		</div>
	</fieldset>    
      
      <s:hidden id="numQuotaPopUp" name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.numeroQuota"/> 
       
</div>
<div class="modal-footer">
	<!-- task-131 <s:submit id="submitBtnAggOrdinativo" name="btnAggOrdinativo" value="conferma" cssClass="btn btn-primary" method="aggiornaQuotaOrdinativo"/> -->
	<s:submit id="submitBtnAggOrdinativo" name="btnAggOrdinativo" value="conferma" cssClass="btn btn-primary" action="%{#aggiornaQuotaOrdinativoAction}"/>

</div>

<script type="text/javascript" src="${jspathexternal}datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${jspathexternal}datepicker/bootstrap-datepicker.it.js"></script>
<script src="${jspathexternal}datepicker/datepicker-init.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".soloNumeri").allowedChars({numeric: true});
		 /* Formattazione corretta dei campi numerici */
    	$(".decimale").gestioneDeiDecimali();
    	$('.datepicker').datepicker({language:'it'}); 
	});
</script>