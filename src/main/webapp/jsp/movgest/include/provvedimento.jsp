<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="step1Model.provvedimentoSelezionato">
	<h4>
		Provvedimento: <s:property value="step1Model.provvedimento.annoProvvedimento"/>  / <s:property value="step1Model.provvedimento.numeroProvvedimento"/> 
		- <s:property value="step1Model.provvedimento.tipoProvvedimento"/> - <s:property value="step1Model.provvedimento.oggetto"/>
		- <s:property value="step1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="step1Model.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="step1Model.provvedimento.stato"/>
		<%-- - Blocco:
		<s:if test="%{step1Model.provvedimento.bloccoRagioneria=='false'}">
			No
		</s:if>
		<s:elseif test="%{step1Model.provvedimento.bloccoRagioneria=='true'}">
			Si
		</s:elseif>
		<s:else >
			N/A
		</s:else> --%>
	</h4>
</s:if>
<s:else>
	<h4>Provvedimento:</h4>
</s:else>
<!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->
<fieldset class="form-horizontal" id="provvedimento" 
	data-provvedimento-stato="<s:property value="step1Model.provvedimento.stato"/>">
  <div class="control-group">
    <label class="control-label" for="annoProvvedimento">Anno </label>
    <div class="controls">   
      
      <!-- IN AGGIORNAMENTO E IN SEZ SUBIMPEGNO: I CAMPI SONO VALORIZZATI E DISABILITATI -->
      <s:if test="sonoInInserimento()">
      		<!-- inserimento impegno/accertamento -->
	      <s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" maxlength="4" name="step1Model.provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="%{step1Model.provvedimentoSelezionato}"></s:textfield>
	      <span class="al">
	        <label class="radio inline" for="numeroProvvedimento">Numero </label>
	      </span>
	      <s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2" maxlength="6" name="step1Model.provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="%{step1Model.provvedimentoSelezionato}"></s:textfield>
	      <span class="al">
	        <label class="radio inline" for="tipo3">Tipo </label>
	      </span>
      
	      <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti" 
	      			   name="step1Model.provvedimento.idTipoProvvedimento" cssClass="span4"  
				       headerKey="" headerValue="" 
	       	 	       listKey="uid" listValue="descrizione" 
	       	 	       disabled="%{step1Model.provvedimentoSelezionato}" />
	   
	   	     	 	       
	     <span class="radio guidata"><a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>	 	    
	     
	    <s:if test="abilitatoAzioneInserimentoProvvedimento()">
	   		 <span class="radio guidata"><a id="linkInserisciProvvedimento" href="#guidaInserisciProv" data-toggle="modal" class="btn btn-primary">inserisci provvedimento</a></span>	 	          
       	</s:if>
       
       </s:if>
       <s:else>
      		 <!-- aggiorna impegno/accertamento -->
            <s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" name="step1Model.provvedimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>     
	     	 <span class="al">
	       	 <label class="radio inline" for="numeroProvvedimento">Numero </label>
	     	 </span>
	     	 <s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2" name="step1Model.provvedimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)" disabled="true"></s:textfield>      
	     	 <span class="al">
	       	 <label class="radio inline" for="tipo3">Tipo </label>
	     	 </span>
           
	       <s:select list="listaTipiProvvedimenti" id="listaTipiProvvedimenti" 
	      			   name="step1Model.provvedimento.idTipoProvvedimento" cssClass="span4"  
				       headerKey="" headerValue="" 
	       	 	       listKey="uid" listValue="descrizione" disabled="true"/>
	       	 	   
	       		       
	       	<span class="radio guidata">
	       	
				<s:if test="%{visualizzaLinkConsultaModificheProvvedimento}">	       	
	       			<a id="linkConsultaModificheProvvedimento" href="#modConsultaModificheProvvedimento" data-toggle="modal" class="btn btn-primary">storico modifiche</a>
	       		</s:if>
	      		<%-- 	<a id="linkConsultaModificheProvvedimento" href="#modConsultaModificheProvvedimento" class="tooltip-test" title="Visualizza dettagli" data-toggle="modal">
	       		<i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli storico</span></i></a> --%>
	       	</span> 	       
	       	
	       <%-- <s:if test="%{!flagValido}"> --%>
	       <s:if test="abilitatoModificaProvvedimento()">
	      		<span class="radio guidata"><a id="linkCompilazioneGuidataProvvedimento" href="#guidaProv" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	       </s:if>
	      	
	      	<s:if test="%{!flagValido}">
		      	<s:if test="abilitatoAzioneInserimentoProvvedimento()">
		      		<span class="radio guidata"><a id="linkInserisciProvvedimento" href="#guidaInserisciProv" data-toggle="modal" class="btn btn-primary">inserisci provvedimento</a></span>
		      	</s:if>
	      		 	          
	  	   </s:if>    	 	       
	       	 	       
       </s:else>	 	       
		             	 	       
      <!-- FINE IN AGGIORNAMENTO E IN SEZ SUBIMPEGNO -->
      
    </div>
  </div>
      
      <label class="control-label">Struttura Amministrativa </label>
       <div class="accordion span9" class="struttAmm">
        <div class="accordion-group">
         <div id="testataStruttura" class="accordion-heading">    
        
         <s:if test="step1Model.provvedimentoSelezionato">
         	<a id="lineaStruttura" class="accordion-toggle" data-parent="#struttAmm" >
         </s:if> <s:else>
        	 <a id="lineaStruttura" class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3h">
         </s:else>
           
           <s:if test="step1Model.provvedimentoSelezionato">
           				<s:property value="step1Model.provvedimento.CodiceStrutturaAmministrativaPadre"/> <s:property value="step1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="step1Model.provvedimento.strutturaAmministrativa"/></s:if>
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
  <!-- Modal -->
   <s:hidden name="idHiddenPassAlbero" id="idHiddenPassAlbero" value="%{step1Model.isProvvedimentoSelezionato()}"/>
   <s:hidden name="idPassaggioAlbero" id="idPassaggioAlbero" value="%{step1Model.provvedimento.CodiceStrutturaAmministrativa}"/>
   <s:hidden name="idPassaggioLivello" id="idPassaggioLivello" value="%{step1Model.provvedimento.livello}"/>
   
   	<s:url method="consultaModificheProvvedimento" var="consultaModificheProvvedimento" />
	<s:url method="consultaModificheProvvedimentoSub" var="consultaModificheProvvedimentoSub" />

	<s:hidden id="consultaModificheProvvedimento" value="%{consultaModificheProvvedimento}" />
	<s:hidden id="consultaModificheProvvedimentoSub" value="%{consultaModificheProvvedimentoSub}" />
   
   
<%--       <span class="radio guidata"><a id="linkCompilazioneGuidataCapitolo" href="#guidaProgCronop" data-toggle="modal" class="btn btn-primary">compilazione yyyyyyyyyyy</a></span> --%>
   
   
            <s:if test="oggettoDaPopolareImpegno()"> 
   <span id="spanRicercaCronop" class="radio guidata"><a 
   		id="linkRicercaCronop" href="#guidaProgCronop" 
   		data-toggle="modal" class="btn btn-primary">ricerca progetto / cronoprogramma</a></span>	 	    
		</s:if>   
</fieldset>

<script src="/siacfinapp/js/local/movgest/provvedimento.js" type="text/javascript"></script>
