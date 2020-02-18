<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<fieldset class="form-horizontal">
  <div class="control-group">
  
    <s:if test="soggettoObbligatorio()">
    	<label class="control-label" for="codCreditoreMutuo">Codice *</label>
    </s:if>
    <s:else>
   		 <label class="control-label" for="codCreditoreMutuo">Codice</label>
    </s:else>
  
   
    <div class="controls">
    
    <s:if test="!checkStatoMutuoDefinitivo()">
      <s:textfield id="codCreditoreMutuo" name="soggetto.codCreditore" cssClass="span2" disabled="checkSoggettoStato()" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
    </s:if> 
    <s:else>
   	 <s:textfield id="codCreditoreMutuo" name="soggetto.codCreditore" cssClass="span2" disabled="true" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
    </s:else>
     
     
      <s:if test="!checkStatoMutuoDefinitivo()">
      	  <!--  se il mutuo DEFINITIVO non devo visualizzare il btn di ricerca guidata --> 	  
	      <span class="radio guidata">
	      	<a id="linkCompilazioneGuidataSoggettoMutuo" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
	      </span>
      </s:if>
    </div>
  </div>
</fieldset>