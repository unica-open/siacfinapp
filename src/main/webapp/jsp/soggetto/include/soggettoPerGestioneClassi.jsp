<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="codCreditore">Codice </label>
    <div class="controls">
     
		  <s:if test="model.soggettoSelezionato">
	       <s:textfield id="codCreditore" name="model.soggettoSelezionatoDaAggiungere.codCreditore" cssClass="span2" disabled="true"></s:textfield>
	      </s:if>
	      <s:else><s:textfield id="codCreditore" name="model.soggettoSelezionatoDaAggiungere.codCreditore" cssClass="span2"></s:textfield></s:else>
	      
	      
	      <span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	  
	      
    </div>
  </div>
</fieldset>
