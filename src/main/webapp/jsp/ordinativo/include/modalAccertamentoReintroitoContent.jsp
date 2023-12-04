<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!-- Valore inPopup: <s:textfield name="inPopup"></s:textfield>  -->

<s:if test="inPopup">
	<div style="padding: 0px 20px 0px 20px">
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
	</div>
</s:if>

<div class="modal-body">
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="annoaccertamento">Anno *</label>
			<div class="controls">
				<s:textfield id="annoaccertamento" maxlength="4" cssClass="lbTextSmall span2" name="nAnno" onkeyup="return checkItNumbersOnly(event)"></s:textfield> <span class="al">
				<label	class="radio inline" for="numeroaccertamento">Numero *</label>
				</span> 
				 <s:textfield id="numeroaccertamento" cssClass="lbTextSmall span2" name="nImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield> <span class="al"> 
					<a id="ricercaGuidataAccertamento" class="btn btn-primary pull-right"><i class="icon-search icon"></i>cerca&nbsp;</a>
				</span>
			</div>
		</div>
	</fieldset>
	<s:if test="isnSubImpegno">	
		<h4>Elenco elementi trovati</h4>
	</s:if>
	
	<s:include value="/jsp/ordinativo/include/risultatoRicercaAccertamentoReintroito.jsp" />		
	<div class="Border_line"></div>	
	
</div>
<div class="modal-footer">
	<s:if test="hasImpegnoSelezionatoPopup">		
		<!-- task-131 <s:submit name="conferma" value="conferma" method="confermaCompGuidataImpegno" cssClass="btn btn-primary" /> -->
		<s:submit name="conferma" value="conferma" action="reintroitoOrdinativoPagamentoStep2_confermaCompGuidataImpegno" cssClass="btn btn-primary" />
	</s:if>
	<s:else>
		<!-- task-131 <s:submit name="conferma" value="conferma" method="confermaCompGuidataImpegno" disabled="true" cssClass="btn btn-primary" /> -->
		<s:submit name="conferma" value="conferma" method="reintroitoOrdinativoPagamentoStep2_confermaCompGuidataImpegno" disabled="true" cssClass="btn btn-primary" /> -->
	
	</s:else>
</div>

<script type="text/javascript">	
	if($("#annoAccertamento").val()){
		$("#annoaccertamento").val($("#annoAccertamento").val());
	}
	if($("#numeroAccertamento").val()){
		$("#numeroaccertamento").val($("#numeroAccertamento").val());
	}
	$("#ricercaGuidataAccertamento").click(function() {
		var annoaccertamento = $("#annoaccertamento").val();
		var numeroaccertamento = $("#numeroaccertamento").val();
		//if(annoaccertamento && numeroaccertamento){
		if(true){
			$.ajax({
				//task-131 url: '<s:url method="ricercaAccertamentoCompilazioneGuidata"/>',
				url: '<s:url action="reintroitoOrdinativoPagamentoStep2_ricercaAccertamentoCompilazioneGuidata"/>',
				type: 'GET',
				data: { anno: annoaccertamento, numero: numeroaccertamento},
			    success: function(data)  {
			    	$("#refreshAccertamentoPopupModal").html(data);
				}
			});
		}
		$("#annoAccertamento").val($("#annoaccertamento").val());
		$("#numeroAccertamento").val($("#numeroaccertamento").val());
		$("#numeroSub").val(null);
	});		
</script>