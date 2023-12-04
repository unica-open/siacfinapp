<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


	<s:if test="hasImpegnoSelezionatoXPopup">
		<%-- <h4 class="step-pane">Impegno: <s:property value="annoImpegno" default=" "/><s:if test="%{annoImpegno != null && annoImpegno != ''}">/</s:if><s:property value="numeroImpegno" default=" "/><s:if test="%{numeroImpegno != null && numeroImpegno != ''}">/</s:if><s:property value="numeroSub" default=" "/><s:if test="%{numeroSub != null && numeroSub != ''}"> - </s:if><s:property value="impegnoPopup.descrizione" default=" "/><s:if test="%{impegnoPopup.descrizione != null && impegnoPopup.descrizione != ''}"> - </s:if><s:property value="impegnoPopup.tipoImpegno.descrizione" default=" "/></h4> --%>
		<h4 class="step-pane">Impegno: 
			<s:if test="%{annoImpegno != null && annoImpegno != ''}"><s:property value="annoImpegno" default=" "/></s:if>
			<s:if test="%{numeroImpegno != null && numeroImpegno != ''}">/<s:property value="numeroImpegno" default=" "/></s:if>
			<s:if test="%{numeroSub != null && numeroSub != ''}">/<s:property value="numeroSub"/></s:if>
			<s:if test="%{descrizioneImpegnoPopup != null && descrizioneImpegnoPopup != ''}"> - <s:property value="descrizioneImpegnoPopup"/></s:if>
		</h4>
	</s:if>
	<s:else>
		<h4>Impegno:</h4>
	</s:else>

	<fieldset class="form-horizontal">   
  
		<div class="control-group">
			<label class="control-label" for="anno1">Anno </label>
			<div class="controls">					  
			  <s:textfield id="annoImpegno" maxlength="4" cssClass="lbTextSmall span1 required" name="annoImpegno" onkeyup="return checkItNumbersOnly(event)" disabled="hasImpegnoSelezionatoXPopup"></s:textfield>			   
			  <span class="al">
				<label class="radio inline" for="numero">Numero </label>
			  </span>
			  <s:textfield id="numeroImpegno" cssClass="lbTextSmall span2 required" name="numeroImpegno" onkeyup="return checkItNumbersOnly(event)" disabled="hasImpegnoSelezionatoXPopup"></s:textfield>   
			  <span class="al">
				<label class="radio inline" for="sub">Sub </label>
			  </span>
			  <s:textfield id="numeroSub" cssClass="lbTextSmall span2 required" name="numeroSub" onkeyup="return checkItNumbersOnly(event)" disabled="hasImpegnoSelezionatoXPopup"></s:textfield>

		  	  <s:if test="disabilitaCompilazioneGuidataPerCollega()">
		  		<span class="radio guidata"><a id="compilazioneGuidataImpegno" href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
		  	  </s:if>
			  	
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
			initRicercaGuidataImpegno(
					$("#annoImpegno").val(), 
					$("#numeroImpegno").val()
			);
		});	 
	
	
	</script>