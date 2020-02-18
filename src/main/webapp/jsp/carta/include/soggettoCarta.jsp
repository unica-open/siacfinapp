<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>



<fieldset class="form-horizontal">
	<div class="control-group">
		<s:if test="soggettoObbligatorio">
			<label class="control-label" >Codice * </label>
		</s:if>
		<s:if test="!soggettoObbligatorio">
			<label class="control-label" >Codice</label>
		</s:if>
		<div class="controls">
				<s:textfield id="codCreditore" name="soggetto.codCreditore" cssClass="span2" onkeyup="return checkItNumbersOnly(event)" disabled="soggettoSelezionato"></s:textfield> 				  
			<span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
		</div>
	</div>

</fieldset>