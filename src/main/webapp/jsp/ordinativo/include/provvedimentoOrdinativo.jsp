<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/javascriptTree.jsp" />
<s:if test="gestioneOrdinativoStep1Model.provvedimentoSelezionato">
	<h4>
		Provvedimento: <s:property value="gestioneOrdinativoStep1Model.provvedimento.annoProvvedimento"/>  / <s:property value="gestioneOrdinativoStep1Model.provvedimento.numeroProvvedimento"/> 
		- <s:property value="gestioneOrdinativoStep1Model.provvedimento.tipoProvvedimento"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.oggetto"/>
		- <s:property value="gestioneOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="gestioneOrdinativoStep1Model.provvedimento.stato"/>
	</h4>
	
</s:if>
<s:else>
	<h4>Provvedimento:</h4>
</s:else>
<fieldset class="form-horizontal" id="gestioneProvvedimento">
  <div class="control-group" id="divProvvedimento">
    <label class="control-label" for="annoProvvedimento">Anno *</label>
    <div class="controls">   
      <s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" maxlength="4" name="gestioneOrdinativoStep1Model.provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="gestioneOrdinativoStep1Model.provvedimentoSelezionato"></s:textfield>     
      <span class="al">
        <label class="radio inline" for="numeroProvvedimento">Numero *</label>
      </span>
      <s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2" maxlength="6" name="gestioneOrdinativoStep1Model.provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="gestioneOrdinativoStep1Model.provvedimentoSelezionato"></s:textfield>      
      <span class="al">
        <label class="radio inline" for="tipo3">Tipo </label>
      </span>
      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti"
				   name="gestioneOrdinativoStep1Model.provvedimento.idTipoProvvedimento" cssClass="span4"  
			       headerKey="" headerValue="" 
       	 	       listKey="uid" listValue="descrizione" 
       	 	       disabled="gestioneOrdinativoStep1Model.provvedimentoSelezionato" /> 
  	 	       
      <s:if test="!sonoInAggiornamentoIncasso()">
	      <span class="radio guidata">
	      	<a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
	      </span>
      </s:if>
      
   </div>
   </div>
	     
	<label class="control-label">Struttura Amministrativa </label>
<!--       <div class="controls">                     -->
       <div class="accordion span9" class="struttAmm">
        <div class="accordion-group">
         <div id="testataStruttura" class="accordion-heading">    
         
	         <s:if test="gestioneOrdinativoStep1Model.provvedimentoSelezionato">
	         	<a id="lineaStruttura" class="accordion-toggle" data-parent="#struttAmm" style="cursor: not-allowed">
	         </s:if> <s:else>
	        	 <a id="lineaStruttura" class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3h">
	         </s:else>
	         
			         <s:if test="gestioneOrdinativoStep1Model.provvedimentoSelezionato">
			           				<s:property value="gestioneOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativaPadre"/> <s:property value="gestioneOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.strutturaAmministrativa"/></s:if>
			         <s:else>Seleziona la Struttura amministrativa </s:else>
			         <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmOrdinativoIncasso"></i>
			         
			     </a>
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
       
<%--        <s:hidden name="idHiddenPassAlbero" id="idHiddenPassAlbero" value="%{gestioneOrdinativoStep1Model.provvedimentoSelezionato}"/>
       <s:hidden name="idPassaggioAlbero" id="idPassaggioAlbero" value="%{gestioneOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa}"/>
       <s:hidden name="idPassaggioLivello" id="idPassaggioLivello" value="%{gestioneOrdinativoStep1Model.provvedimento.livello}"/> --%>
   <s:hidden name="idHiddenPassAlbero" id="idHiddenPassAlbero" value="%{gestioneOrdinativoStep1Model.isProvvedimentoSelezionato()}"/>
   <s:hidden name="idPassaggioAlbero" id="idPassaggioAlbero" value="%{gestioneOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa}"/>
   <s:hidden name="idPassaggioLivello" id="idPassaggioLivello" value="%{gestioneOrdinativoStep1Model.provvedimento.livello}"/>

</fieldset>


		
<!--       </div> -->
<!--   </div> -->
  
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
