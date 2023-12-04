<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="codCreditoremo">Codice *</label>
    	<div class="controls">    
		    <s:if test="hasCodiceSoggetto">
		    	<s:textfield id="codCreditoreLiquidazione" name="gestioneOrdinativoStep1Model.soggetto.codCreditore" cssClass="span2" disabled="true"></s:textfield> 
			</s:if>
			<s:else>
				<s:textfield id="codCreditoreLiquidazione" name="gestioneOrdinativoStep1Model.soggetto.codCreditore" cssClass="span2" onkeyup="return checkItNumbersOnly(event)" disabled="gestioneOrdinativoStep1Model.soggettoSelezionato"></s:textfield> 				  
		    </s:else> 
	      	<%-- <span class="al">
					<label class="radio inline" for="classe">Classe</label>
			</span>
			<s:textfield id="classe" name="gestioneOrdinativoStep1Model.classeImpegno" cssClass="span2" disabled="true"></s:textfield> --%>
	      	<span class="radio guidata">
		      	<s:if test="hasCodiceSoggetto || gestioneOrdinativoStep1Model.hasCodiceSoggettoAccertamento">	
				    <!-- non abilito la compilazione guidata -->			
			      	<!-- <a id="linkCompilazioneGuidataSoggetto" href="javascript:void(0)" data-toggle="modal" style="cursor: default" class="btn btn-primary">compilazione guidata</a> -->
				</s:if>
				<s:else>  
			      	<a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a> 
				</s:else>
	      	</span>
    	</div>
  	</div>
</fieldset>