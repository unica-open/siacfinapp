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
	<s:hidden name="idPassaggioAlbero" id="idPassaggioAlbero" value="%{provvedimento.CodiceStrutturaAmministrativa}"/>
</s:if>
<s:else>
	<h4>Provvedimento </h4>
</s:else>
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="annoProvvedimento">Anno </label>
    <div class="controls">   
      <s:textfield id="annoProvvedimento" maxlength="4" cssClass="lbTextSmall span1" name="provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="checkProvvedimentoStato()"></s:textfield>     
      <span class="al">
        <label class="radio inline" for="numeroProvvedimento">Numero </label>
      </span>
      <s:textfield id="numeroProvvedimento" maxlength="6" cssClass="lbTextSmall span2" name="provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="checkProvvedimentoStato()"></s:textfield>      
      <span class="al">
        <label class="radio inline" for="tipo3">Tipo </label>
      </span>
      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti"
				   name="provvedimento.idTipoProvvedimento" cssClass="span4"  
			       headerKey="" headerValue="" 
       	 	       listKey="uid" listValue="descrizione" 
       	 	       disabled="checkProvvedimentoStato()" /> 
      <span class="radio guidata">
      	<a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
      </span>
    </div>
  </div>
  
    	<label class="control-label">Struttura Amministrativa </label>
<!--       <div class="controls">                     -->
       <div class="accordion span9" class="struttAmm">
        <div class="accordion-group">
         <div id="testataStruttura" class="accordion-heading">    
           <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3h">
			<s:if test="provvedimentoSelezionato">
           				<s:property value="provvedimento.CodiceStrutturaAmministrativaPadre"/> <s:property value="provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="provvedimento.strutturaAmministrativa"/></s:if>
           	<s:else>Seleziona la Struttura amministrativa </s:else>
			<i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmOrdinativoIncasso"></i></a>
         </div>
         <div id="3h" class="accordion-body collapse">
<!-- 	              ALBERO VISUALIZZATO -->
           <div class="accordion-inner" id="strutturaAmministrativaOrdinativoIncassoDiv">
             <ul id="strutturaAmministrativaOrdinativoIncasso" class="ztree treeStruttAmm"></ul>
           </div>
<!-- 	              ALBERO IN ATTESA -->
           <div class="accordion-inner" id="strutturaAmministrativaOrdinativoIncassoWait">
             Attendere prego..
            </div>
            
          </div>
        </div>
       </div>
  
  
       <s:hidden name="idHiddenPassAlbero" id="idHiddenPassAlbero" value="%{provvedimentoSelezionato}"/>
       <s:hidden name="idPassaggioLivello" id="idPassaggioLivello" value="%{provvedimento.livello}"/>
</fieldset>
<script type="text/javascript">

$(document).ready(function() {

	
	$("#testataStruttura").click(function() {

       
       if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
    	   
	       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
	    	   
	    	   vaiStrutturaAlberoGenerico($("#idPassaggioAlbero").val(), $("#idPassaggioLivello").val());
	    	   $("#idPassaggioAlbero").val("");
	    	   $("#idHiddenPassAlbero").val("");
	    	   $("#idPassaggioLivello").val("");
	       }
          
       }
      

	});    
}); 
 

</script>  
