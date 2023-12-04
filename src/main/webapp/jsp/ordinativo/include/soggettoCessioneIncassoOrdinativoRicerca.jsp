<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="codCreditoreMo">Codice </label>
    	<div class="controls">    
		    <s:if test="hasCodiceSoggettoDue">
		    	<s:textfield id="codCreditoreCessioneIncasso" name="soggettoDue.codCreditore" cssClass="span2" disabled="true"></s:textfield> 
			</s:if>
			<s:else>
				<s:textfield id="codCreditoreCessioneIncasso" name="soggettoDue.codCreditore" cssClass="span2" onkeyup="return checkItNumbersOnly(event)"></s:textfield> 				  
		    </s:else> 
		  
	      	<span class="radio guidata">
		      	<s:if test="hasCodiceSoggettoDue">	
				    <!-- non abilito la compilazione guidata -->			
				</s:if>
				<s:else>  
			      	<a id="linkCompilazioneGuidataSoggettoDue" href="#guidaSogDue" data-toggle="modal" class="btn btn-primary">compilazione guidata</a> 
				</s:else>
	      	</span>
    	</div>
  	</div>
</fieldset>