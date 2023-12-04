<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<%-- <p> E' possibile associare all'<s:property value="%{labels.OGGETTO_GENERICO_LOWER_CASE}"/> un soggetto o una classe di soggetti</p> --%>
<fieldset class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="codCreditore">Codice </label>
    <div class="controls">
      <!--- IN AGGIORNAMENTO - CAMPO VALORIZZATO E DISABILITATO -->
     <s:if test="sonoInInserimento()">
         <!-- INSERIMENTO --> 
		 <s:if test="step1Model.soggettoSelezionato">
	       <s:textfield id="codCreditore" name="step1Model.soggetto.codCreditore" cssClass="span2" disabled="true"></s:textfield>
	      </s:if>
	      <s:else><s:textfield id="codCreditore" name="step1Model.soggetto.codCreditore" cssClass="span2"></s:textfield></s:else>
	      
	      <!--- FINE IN AGGIORNAMENTO -->
	      <!-- SEZ SUBIMPEGNO: IL CAMPO NON e' PRESENTE -->
	      <span class="al">
	        <label class="radio inline" for="classe">Classe</label>
	      </span>
	<%--       <select name="classe" id="classe" class="span5"><option>xxxxx</option></select> --%>
	      
	      <s:if test="not listaClasseSoggetto.empty" >
	       <s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
	          		   headerValue="" name="step1Model.soggetto.idClasse" cssClass="span5"  
	       	 	       listKey="id" listValue="codice+' - '+descrizione" /> 
	       	 	       
	     </s:if>
	     <s:else>
	     	 <s:select list="#{}" id="listaClasseSoggetto" 
	          		   cssClass="span5"  
	       	 	      /> 
	     </s:else>
	      
	      <!-- FINE SEZ SUBIMPEGNO -->
	      <span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	  </s:if>
	  <s:else>
		  <!-- AGGIORNAMENTO --> 
		  <s:if test="step1Model.soggettoSelezionato">
			 	<s:if test="step1Model.soggetto.codCreditore!=''">
		       		<s:textfield id="codCreditore" name="step1Model.soggetto.codCreditore" cssClass="span2" disabled="true"></s:textfield>
		       	</s:if>
      	  </s:if>
     	  <s:else><s:textfield id="codCreditore" name="step1Model.soggetto.codCreditore" cssClass="span2"></s:textfield></s:else>
      
		      <!--- FINE IN AGGIORNAMENTO -->
		      <!-- SEZ SUBIMPEGNO: IL CAMPO NON e' PRESENTE -->
		      <span class="al">
		        <label class="radio inline" for="classe">Classe</label>
		      </span>
		<%--       <select name="classe" id="classe" class="span5"><option>xxxxx</option></select> --%>
		      
		      <s:if test="%{!flagSoggettoValido}">
		       	<s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
		          		   headerValue="" name="step1Model.soggetto.classe" cssClass="span5"  
		       	 	       listKey="codice" listValue="codice+' - '+descrizione" />
		      </s:if>
		      <s:else>
		      	<s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
		          		   headerValue="" name="step1Model.soggetto.classe" cssClass="span5"  
		       	 	       listKey="codice" listValue="codice+' - '+descrizione" disabled="true"/>
		      </s:else> 	 	       
		       	 	        
		      
		      <!-- FINE SEZ SUBIMPEGNO -->
		      <s:if test="%{!flagSoggettoValido}">
		      	<span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
		      </s:if>	
		  
		  
	  </s:else>
	  
	      
    </div>
  </div>
</fieldset>
