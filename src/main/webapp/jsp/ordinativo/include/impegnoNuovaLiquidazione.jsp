<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	<s:if test="hasImpegnoSelezionatoXPopup">
		<h4 class="step-pane">Impegno: <s:property value="annoImpegno" default=" "/>/<s:property value="numeroImpegno" default=" "/>/<s:property value="numeroSub" default=" "/> - <s:property value="descrizioneImpegnoPopup" default=" "/> - <s:property value="descrizioneTipoImpegnoPopup" default=" "/></h4>
	</s:if>
	<s:else>
		<h4>Impegno</h4>
	</s:else>

	      
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="anno1">Anno *</label>
			<div class="controls">					  
			  <s:textfield id="annoImpegno" cssClass="lbTextSmall span1" name="nuovaLiquidazioneModel.annoImpegno" maxlength="4" onkeyup="return checkItNumbersOnly(event)" disabled="presenzaQuoteCompetenza()"></s:textfield>			   
			  <span class="al">
				<label class="radio inline" for="numero">Numero *</label>
			  </span>
			  <s:textfield id="numeroImpegno" cssClass="lbTextSmall span2" name="nuovaLiquidazioneModel.numeroImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>   
			  <span class="al">
				<label class="radio inline" for="sub">Sub </label>
			  </span>
			  <s:textfield id="numeroSub" cssClass="lbTextSmall span2" name="nuovaLiquidazioneModel.numeroSub" onkeyup="return checkItNumbersOnly(event)"></s:textfield>

			  <span class="radio guidata"><a id="compilazioneGuidataImpegnoStep1" href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
			</div>
		</div>
	
	</fieldset>

	
	<script type="text/javascript">
		$("#compilazioneGuidataImpegnoStep1").click(function() {				
			if($("#annoImpegno").val()){
				$("#annoimpegno").val($("#annoImpegno").val());
			}
			if($("#numeroImpegno").val()){
				$("#numeroimpegno").val($("#numeroImpegno").val());
			}
			$.ajax({
				//task-131 url: '<s:url method="pulisciRicercaImpegno"/>',
				url: '<s:url action="%{#pulisciRicercaImpegnoAction}"/>',
			    success: function(data)  {
			    	$("#refreshPopupModal").html(data);
				}
			});
		});		
	</script>