<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/javascriptTree.jsp" />


<span id="bloccoTitoloProvvedimentoUnico">
	<s:if test="reintroitoOrdinativoStep1Model.provvedimentoSelezionato">
		<h4>
			Provvedimento: <s:property value="reintroitoOrdinativoStep1Model.provvedimento.annoProvvedimento"/>  / <s:property value="reintroitoOrdinativoStep1Model.provvedimento.numeroProvvedimento"/> 
			- <s:property value="reintroitoOrdinativoStep1Model.provvedimento.tipoProvvedimento"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimento.oggetto"/>
			- <s:property value="reintroitoOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="reintroitoOrdinativoStep1Model.provvedimento.stato"/>
		</h4>
		
	</s:if>
	<s:else>
		<h4>Provvedimento</h4>
	</s:else>
</span>
<span id="bloccoTitoloProvvedimentoDaMovimenti">
	<h4>Provvedimento</h4>
</span>

<fieldset class="form-horizontal" id="sceltaProvvedimentoDaUsare">
  <div class="control-group">
	    <span class="control-label">Provvedimento da utilizzare</span>
	    <div class="controls">    
	      <s:radio id="tipoDebitoSiope" name="reintroitoOrdinativoStep1Model.provvedimentoDaUsare" cssClass="flagResidenza" list="reintroitoOrdinativoStep1Model.scelteProvvedimentoDaUsare"></s:radio> 
	    </div>
	  </div>
</fieldset>


<span id="bloccoProvvedimentoUnico">

	<fieldset class="form-horizontal" id="gestioneProvvedimento">
	  <div class="control-group" id="divProvvedimento">
	    <label class="control-label" for="annoProvvedimento">Anno *</label>
	    <div class="controls">   
	      <s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" maxlength="4" name="reintroitoOrdinativoStep1Model.provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="reintroitoOrdinativoStep1Model.provvedimentoSelezionato"></s:textfield>     
	      <span class="al">
	        <label class="radio inline" for="numeroProvvedimento">Numero *</label>
	      </span>
	      <s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2" maxlength="6" name="reintroitoOrdinativoStep1Model.provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="reintroitoOrdinativoStep1Model.provvedimentoSelezionato"></s:textfield>      
	      <span class="al">
	        <label class="radio inline" for="tipo3">Tipo </label>
	      </span>
	      
	      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti"
					   name="reintroitoOrdinativoStep1Model.provvedimento.idTipoProvvedimento" cssClass="span4"  
				       headerKey="" headerValue="" 
	       	 	       listKey="uid" listValue="descrizione" 
	       	 	       disabled="reintroitoOrdinativoStep1Model.provvedimentoSelezionato" /> 
	  	 	       
	      <span class="radio guidata">
	      	<a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a>
	      </span>
	      
	   </div>
	   </div>
		     
		<label class="control-label">Struttura Amministrativa </label>
	
	       <div class="accordion span9" class="struttAmm">
	        <div class="accordion-group">
	         <div id="testataStruttura" class="accordion-heading">    
	         
		         <s:if test="reintroitoOrdinativoStep1Model.provvedimentoSelezionato">
		         	<a id="lineaStruttura" class="accordion-toggle" data-parent="#struttAmm" style="cursor: not-allowed">
		         </s:if> <s:else>
		        	 <a id="lineaStruttura" class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3h">
		         </s:else>
		         
				         <s:if test="reintroitoOrdinativoStep1Model.provvedimentoSelezionato">
				           				<s:property value="reintroitoOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativaPadre"/> <s:property value="reintroitoOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimento.strutturaAmministrativa"/></s:if>
				         <s:else>Seleziona la Struttura amministrativa </s:else>
				         <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmOrdinativoIncasso"></i>
				         
				     </a>
	         </div>
	         <div id="3h" class="accordion-body collapse">
			   <!-- ALBERO VISUALIZZATO -->
	           <div class="accordion-inner" id="strutturaAmministrativaOrdinativoIncassoDiv">
	             <ul id="strutturaAmministrativaOrdinativoIncasso" class="ztree treeStruttAmm"></ul>
	           </div>
	           <!-- ALBERO IN ATTESA -->
	           <div class="accordion-inner" id="strutturaAmministrativaOrdinativoIncassoWait">
	             Attendere prego..
	            </div>
	            
	          </div>
	        </div>
	       </div>
	       
	   <s:hidden name="idHiddenPassAlbero" id="idHiddenPassAlbero" value="%{reintroitoOrdinativoStep1Model.isProvvedimentoSelezionato()}"/>
	   <s:hidden name="idPassaggioAlbero" id="idPassaggioAlbero" value="%{reintroitoOrdinativoStep1Model.provvedimento.CodiceStrutturaAmministrativa}"/>
	   <s:hidden name="idPassaggioLivello" id="idPassaggioLivello" value="%{reintroitoOrdinativoStep1Model.provvedimento.livello}"/>
	
	</fieldset>

</a>
  
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
	
	var tipoProvvedimentoDaUsareVar = $("input[name='reintroitoOrdinativoStep1Model.provvedimentoDaUsare']");
	var bloccoProvvedimentoUnico = $("#bloccoProvvedimentoUnico");
	
	var bloccoTitoloProvvedimentoUnico = $("#bloccoTitoloProvvedimentoUnico");
	var bloccoTitoloProvvedimentoDaMovimenti = $("#bloccoTitoloProvvedimentoDaMovimenti");
	
	
	tipoProvvedimentoDaUsareVar.change(toggleProvvedimentoDaUsare);
	toggleProvvedimentoDaUsare.bind(tipoProvvedimentoDaUsareVar.filter(':checked')[0])();
	
	function toggleProvvedimentoDaUsare(){
		var valoreSelezionato = this ? this.value : '';
		// ':checked')[0] ORA:   this e' di tipo HTMLInputElement 
		// ':checked')    PRIMA: this era di tipo jQuery
		// HTMLInputElement.value e' una proprieta'
		// jQuery.value e' undefined
		
		//var listaSceltaProvvedimentoDaUsareVar = document.getElementById('listaMotivazioniAssenzaCigId');
		
		if(valoreSelezionato === 'Provvedimento Unico'){
			bloccoProvvedimentoUnico.show();
			bloccoTitoloProvvedimentoUnico.show();
			bloccoTitoloProvvedimentoDaMovimenti.hide();
		} else {
			bloccoProvvedimentoUnico.hide();
			bloccoTitoloProvvedimentoUnico.hide();
			bloccoTitoloProvvedimentoDaMovimenti.show();
			//listaSceltaProvvedimentoDaUsareVar.selectedIndex = 0;
			//listaSceltaProvvedimentoDaUsareVar.value = '';
		}
	}
	
	
});   

</script>  
