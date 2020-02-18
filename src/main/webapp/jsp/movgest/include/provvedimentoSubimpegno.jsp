<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="step1ModelSubimpegno.provvedimentoSelezionato">
	<h4>
		Provvedimento: <s:property value="step1ModelSubimpegno.provvedimento.annoProvvedimento"/>  / <s:property value="step1ModelSubimpegno.provvedimento.numeroProvvedimento"/> 
		- <s:property value="step1ModelSubimpegno.provvedimento.tipoProvvedimento"/> - <s:property value="step1ModelSubimpegno.provvedimento.oggetto"/>
		- <s:property value="step1ModelSubimpegno.provvedimento.codiceStrutturaAmministrativa"/> - <s:property value="step1ModelSubimpegno.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="step1ModelSubimpegno.provvedimento.stato"/>
	</h4>
</s:if>
<s:else>
	<h4>Provvedimento:</h4>
</s:else>
<!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="annoProvvedimento">Anno </label>
    <div class="controls">   
      
      <!-- IN AGGIORNAMENTO E IN SEZ SUBIMPEGNO: I CAMPI SONO VALORIZZATI E DISABILITATI -->
      
      <s:textfield id="annoProvvedimento" maxlength="4" cssClass="lbTextSmall span1" name="step1ModelSubimpegno.provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>     
      <span class="al">
        <label class="radio inline" for="numeroProvvedimento">Numero </label>
      </span>
      <s:textfield id="numeroProvvedimento" maxlength="6" cssClass="lbTextSmall span2" name="step1ModelSubimpegno.provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>      
      <span class="al">
        <label class="radio inline" for="tipo3">Tipo </label>
      </span>
      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti"
				   name="step1ModelSubimpegno.provvedimento.idTipoProvvedimento" cssClass="span4"  
			       headerKey="" headerValue="" 
       	 	       listKey="uid" listValue="descrizione" 
       	 	       disabled="true" /> 
		                	 	       
      <!-- FINE IN AGGIORNAMENTO E IN SEZ SUBIMPEGNO -->
      
      <span class="radio guidata">
      	<a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
		<a id="linkConsultaModificheProvvedimento" href="#modConsultaModificheProvvedimento" data-toggle="modal" class="btn btn-primary">storico modifiche</a></span>
      </span>
    </div>
  </div>
  <!-- Modal -->
</fieldset>



<script type="text/javascript">


$(document).ready(function() {

	$("#linkConsultaModificheProvvedimento").click(function() {
		
		$.ajax({
			url: '<s:url method="consultaModificheProvvedimento"/>',
			type: 'POST',
			success: function(data)  {
				$("#modConsultaModificheProvvedimento").html(data);
			}
		});
	});	
	
});

 

</script>