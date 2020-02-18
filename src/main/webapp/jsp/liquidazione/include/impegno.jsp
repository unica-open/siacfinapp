<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	<s:if test="hasImpegnoSelezionatoXPopup">
		<h4 class="step-pane">Impegno: 
			<s:if test="%{annoImpegno != null && annoImpegno != ''}"><s:property value="annoImpegno" default=" "/></s:if>
			<s:if test="%{numeroImpegno != null && numeroImpegno != ''}">/<s:property value="numeroImpegno" default=" "/></s:if>
			<s:if test="%{numeroSub != null && numeroSub != ''}">/<s:property value="numeroSub"/></s:if>
			<s:if test="%{numeroMutuoPopup != null && numeroMutuoPopup != ''}"> - <s:property value="numeroMutuoPopup"/></s:if>
			<s:if test="%{descrizioneImpegnoPopup != null && descrizioneImpegnoPopup != ''}"> - <s:property value="descrizioneImpegnoPopup"/></s:if>
		</h4>
	</s:if>
	<s:else>
		<h4>Impegno:</h4>
	</s:else>

	      
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="anno1">Anno *</label>
			<div class="controls">					  
			  <s:textfield id="annoImpegno" maxlength="4" cssClass="lbTextSmall span1 required" name="annoImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>			   
			  <span class="al">
				<label class="radio inline" for="numero">Numero *</label>
			  </span>
			  <s:textfield id="numeroImpegno" cssClass="lbTextSmall span2 required" name="numeroImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>   
			  <span class="al">
				<label class="radio inline" for="sub">Sub </label>
			  </span>
			  <s:textfield id="numeroSub" cssClass="lbTextSmall span2 required" name="numeroSub" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
			  <span class="al">
				<label class="radio inline" for="mutuo">Mutuo </label>
			  </span>
			  <s:textfield id="numeroMutuo" cssClass="lbTextSmall span2 required" name="numeroMutuoPopup" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
			  <s:if test="ricercaImpegnoStep1Abilitata">
			  	<span class="radio guidata"><a id="compilazioneGuidataImpegnoStep1" href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
			  </s:if>
			  <s:else>
			  	<span class="radio guidata"><a id="compilazioneGuidataImpegnoStep1" href="#" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
			  </s:else>
			</div>
		</div>
	
	</fieldset>

	
	<script type="text/javascript">
		$("#compilazioneGuidataImpegnoStep1").click(function() {				
			//if($("#annoImpegno").val()){
				$("#annoimpegno").val($("#annoImpegno").val());
			//}
			//if($("#numeroImpegno").val()){
				$("#numeroimpegno").val($("#numeroImpegno").val());
			//}
			
			$.ajax({
                  url: '<s:url method="azzeraModaleImpegno"></s:url>',
                  type: "GET",
               	  success: function(data){
                  	$("#refreshPopupModal").html(data);
                  }
           });
			
		});		
	</script>