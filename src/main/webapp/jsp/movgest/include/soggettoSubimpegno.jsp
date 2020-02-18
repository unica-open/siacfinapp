<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="codCreditoreSubimpegno">Codice </label>
    <div class="controls">
      <s:textfield id="codCreditoreSubimpegno" name="step1ModelSubimpegno.soggetto.codCreditore" cssClass="span2" disabled="step1ModelSubimpegno.soggettoSelezionato"></s:textfield>
      <span class="radio guidata">
      	<s:if test="campoDisabilitato()">
      		<s:submit id="linkCompilazioneGuidataSoggettoSubimpegnoNoClick" disabled="true" cssClass="btn btn-primary" value="compilazione guidata"/>
      	</s:if><s:else>
      		<a id="linkCompilazioneGuidataSoggettoSubimpegno" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
      	</s:else>
      </span>
    </div>
  </div>
</fieldset>