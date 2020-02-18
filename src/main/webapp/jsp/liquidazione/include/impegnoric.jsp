<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	<s:if test="hasImpegnoSelezionatoXPopup">
		<h4 class="step-pane">Impegno: <s:property value="impegno.annoImpegno" default=" "/><s:if test="%{impegno.annoImpegno != null && impegno.annoImpegno != ''}">/</s:if><s:property value="impegno.numeroImpegno" default=" "/><s:if test="%{impegno.numeroImpegno != null && impegno.numeroImpegno != ''}">/</s:if><s:property value="impegno.numeroSub" default=" "/><s:if test="%{impegno.numeroSub != null && impegno.numeroSub != ''}"> - </s:if><s:property value="impegnoPopup.descrizione" default=" "/><s:if test="%{impegnoPopup.descrizione != null && impegnoPopup.descrizione != ''}"> - </s:if><s:property value="impegnoPopup.tipoImpegno.descrizione" default=" "/></h4>
	</s:if>
	<s:else>
		<h4>Impegno </h4>
	</s:else>

	      
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="anno1">Anno *</label>
			<div class="controls">					  
			  <s:textfield id="annoImpegno" cssClass="lbTextSmall span1 required" name="impegno.annoImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>			   
			  <span class="al">
				<label class="radio inline" for="numero">Numero *</label>
			  </span>
			  <s:textfield id="numeroImpegno" cssClass="lbTextSmall span2 required" name="impegno.numeroImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>   
			  <span class="al">
				<label class="radio inline" for="sub">Sub </label>
			  </span>
			  <s:textfield id="numeroSub" cssClass="lbTextSmall span2 required" name="impegno.numeroSub" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
			  <span class="al">
				<label class="radio inline" for="mutuo">Mutuo </label>
			  </span>
	 			  <s:textfield id="numeroMutuo" cssClass="lbTextSmall span2 required" name="model.numeroMutuoImpegnoString" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
			  <!--span class="radio guidata"><a href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span-->
			  <span class="radio guidata"><a id="compilazioneGuidataImpegno" href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
			</div>
		</div>
	
	</fieldset>
	
	
	<script type="text/javascript">
	
	$("#compilazioneGuidataImpegno").click(function() {			
		var annomanuale = $("#annoImpegno").val();
		var numeromanuale = $("#numeroImpegno").val();		
		if(annomanuale){
			$("#annoimpegno").val(annomanuale);
		}
		if(numeromanuale){
			$("#numeroimpegno").val(numeromanuale);
		}
		
		$.ajax({
            url: '<s:url method="azzeraModaleImpegno"></s:url>',
            type: "GET",
         	  success: function(data){
            	$("#refreshPopupModal").html(data);
            }
     	});
	});	
	
	
	</script>