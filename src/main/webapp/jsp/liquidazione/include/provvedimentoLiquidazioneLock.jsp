<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/javascriptTree.jsp" />
<s:if test="provvedimentoSelezionato">
	<h4>
		Provvedimento: <s:property value="provvedimento.annoProvvedimento"/>  / <s:property value="provvedimento.numeroProvvedimento"/> 
		- <s:property value="provvedimento.tipoProvvedimento"/> - <s:property value="provvedimento.oggetto"/>
		- <s:property value="provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="provvedimento.stato"/>
	</h4>
</s:if>
<s:else>
	<h4>Provvedimento</h4>
</s:else>
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="annoProvvedimento">Anno </label>
    <div class="controls">   
      <s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" maxlength="4" name="provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>     
      <span class="al">
        <label class="radio inline" for="numeroProvvedimento">Numero </label>
      </span>
      <s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2" maxlength="6" name="provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>      
      <span class="al">
        <label class="radio inline" for="tipo3">Tipo </label>
      </span>
      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti"
				   name="provvedimento.idTipoProvvedimento" cssClass="span4"  
			       headerKey="" headerValue="" 
       	 	       listKey="uid" listValue="descrizione" 
       	 	       disabled="true" /> 
    </div>
  </div>
</fieldset>