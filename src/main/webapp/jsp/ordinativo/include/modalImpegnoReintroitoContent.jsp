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
			<label class="control-label" for="annoimpegno">Anno *</label>
			<div class="controls">
				<s:textfield id="annoimpegno" maxlength="4" cssClass="lbTextSmall span2" name="nAnno" onkeyup="return checkItNumbersOnly(event)"></s:textfield> <span class="al">
				<label	class="radio inline" for="numeroimpegno">Numero *</label>
				</span> 
				 <s:textfield id="numeroimpegno" cssClass="lbTextSmall span2" name="nImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield> <span class="al"> 
					<a id="ricercaGuidataImpegno" class="btn btn-primary pull-right"><i class="icon-search icon"></i>cerca&nbsp;</a>
				</span>
			</div>
		</div>
	</fieldset>
	<s:if test="isnSubImpegno">	
		<h4>Elenco elementi trovati</h4>
	</s:if>
	
	<s:include value="/jsp/ordinativo/include/risultatoRicercaImpegniReintroito.jsp" />		
	<div class="Border_line"></div>	
	
</div>
<div class="modal-footer">
	<s:if test="hasImpegnoSelezionatoPopup">		
		<!-- task-131 <s:submit name="conferma" value="conferma" method="confermaCompGuidataImpegno" cssClass="btn btn-primary" /> -->
		<s:submit name="conferma" value="conferma" action="reintroitoOrdinativoPagamentoStep2_confermaCompGuidataImpegno" cssClass="btn btn-primary" />
	</s:if>
	<s:else>
		<!-- task-131 <s:submit name="conferma" value="conferma" method="confermaCompGuidataImpegno" disabled="true" cssClass="btn btn-primary" /> -->
		<s:submit name="conferma" value="conferma" action="reintroitoOrdinativoPagamentoStep2_confermaCompGuidataImpegno" disabled="true" cssClass="btn btn-primary" />
	</s:else>
</div>

<script type="text/javascript">	
	if($("#annoImpegno").val()){
		$("#annoimpegno").val($("#annoImpegno").val());
	}
	if($("#numeroImpegno").val()){
		$("#numeroimpegno").val($("#numeroImpegno").val());
	}
	$("#ricercaGuidataImpegno").click(function() {
		var annoimpegno = $("#annoimpegno").val();
		var numeroimpegno = $("#numeroimpegno").val();
		//if(annoimpegno && numeroimpegno){
		if(true){
			$.ajax({
				//task-131 url: '<s:url method="ricercaImpegnoCompilazioneGuidata"/>',
				url: '<s:url action="reintroitoOrdinativoPagamentoStep2_ricercaImpegnoCompilazioneGuidata"/>',
				type: 'GET',
				data: { anno: annoimpegno, numero: numeroimpegno},
			    success: function(data)  {
			    	$("#refreshImpegnoPopupModal").html(data);
				}
			});
		}
		$("#annoImpegno").val($("#annoimpegno").val());
		$("#numeroImpegno").val($("#numeroimpegno").val());
		$("#numeroSub").val(null);
	});		
</script>