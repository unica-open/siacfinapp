<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!--p>Seleziona il capitolo</p-->
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="anno">Anno</label>
    <div class="controls">    
    <s:textfield id="anno" cssClass="lbTextSmall span1" name="step1Model.capitolo.anno" disabled="true"></s:textfield>     
      <span class="al">
        <label class="radio inline" for="capitolo">Capitolo *</label>
      </span>
       <s:textfield id="capitolo" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" name="step1Model.capitolo.numCapitolo" disabled="step1Model.capitoloSelezionato"></s:textfield>           
      <span class="al">
        <label class="radio inline" for="articolo">Articolo *</label>
      </span>
       <s:textfield id="articolo" cssClass="lbTextSmall span1" onkeyup="return checkItNumbersOnly(event)" name="step1Model.capitolo.articolo" disabled="step1Model.capitoloSelezionato"></s:textfield>       
     
     <s:if test ="visualizzaUEB()">
		  <span class="al">
	        <label class="radio inline" for="UEB">UEB</label>
	      </span>
      	  <s:textfield id="ueb" cssClass="lbTextSmall span2" name="step1Model.capitolo.ueb" disabled="step1Model.capitoloSelezionato" ></s:textfield>      
     </s:if> 
      
      <span class="radio guidata"><a id="linkCompilazioneGuidataCapitolo" href="#guidaCap" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
    </div>
  </div>
</fieldset>



  