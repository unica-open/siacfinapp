<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<fieldset class="form-horizontal">
<dl class="dl-horizontal">
    	<s:if test="%{soggettoImpegnoEffettivo!=null && soggettoImpegnoEffettivo.codiceSoggetto!=''}">
	    	<dt>Soggetto <s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/>: </dt>		
    		<dd style="margin-left : 190px">${soggettoImpegnoEffettivo.codiceSoggetto} - ${soggettoImpegnoEffettivo.denominazione} - ${soggettoImpegnoEffettivo.codiceFiscale}&nbsp;</dd>
    	</s:if>
    <!-- <dt>Classe Impegno attuale: </dt> -->
        
    	<s:if test="%{classeSoggettoEffettivo!=null}">
    		<dt>Classe <s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/>: </dt>
    		<dd>${classeSoggettoEffettivo.codice} - ${classeSoggettoEffettivo.descrizione} &nbsp;</dd>
    	</s:if>
 </dl>  
<s:if test="%{subImpegnoSelected!=null&&subImpegnoSelected}">
<dl id="subIinfo" class="dl-horizontal">
    <dt style="width : 180px;">Soggetto <s:property value="%{labels.OGGETTO_GENERICO}"/>: </dt>
    	<s:if test="%{soggettoSubEffettivo!=null}">
    		<dd style="margin-left : 190px">${soggettoSubEffettivo.codiceSoggetto} - ${soggettoSubEffettivo.denominazione} - ${soggettoSubEffettivo.codiceFiscale}</dd>
    	</s:if>
    <%-- <dt>Classe <s:property value="%{labels.OGGETTO_GENERICO}"/>: </dt>
    	<s:if test="%{classeSoggettoSubEffettivo!=null}">
	    	<dd>${classeSoggettoSubEffettivo.codice} - ${classeSoggettoSubEffettivo.descrizione}</dd>
    	</s:if>	 --%>	<!-- sub non ha classe -->
  </dl>  
</s:if>

  <div class="control-group">
  	
<!-- <div class="controls">
          <span class="al">
          	<label class="radio inline" for="soggettoImpegno"> Soggetto attuale ${soggettoImpegnoAttuale.codiceSoggetto}</label>
 			<label class="radio inline" for="classeImpegno"> Classe attuale ${classeSoggettoAttuale.descrizione}</label>
    	  </span>
  </div> --> 
  	 
  
    <label class="control-label" for="codice">Codice</label>
    <div class="controls">
    	 <!--- IN AGGIORNAMENTO - CAMPO VALORIZZATO E DISABILITATO -->
	  
	  	<s:if test="%{subImpegnoSelected!=null && subImpegnoSelected}">
       		<s:textfield id="codCreditore" name="soggettoSubAttuale.codiceSoggetto" cssClass="span2"></s:textfield>
      	</s:if>
      	<s:else> 
      		<s:if test="%{soggettoImpegnoEffettivo!=null || classeSoggettoEffettivo!=null}">
      			<s:textfield id="codCreditore" name="soggettoImpegnoAttuale.codiceSoggetto" cssClass="span2"></s:textfield>
      		</s:if>
      		<s:else> 		
      			<s:textfield id="codCreditore" name="soggettoImpegnoAttuale.codiceSoggetto" cssClass="span2" disabled="true"></s:textfield>
      		</s:else>		
      	</s:else>
      
      <!-- HIDDEN PER LA PAGINA -->
      <s:hidden name="soggettoDesiderato" />
      
      
        <span class="al">
         	<s:if test="%{subImpegnoSelected==null || !subImpegnoSelected}">
        		<label class="radio inline" for="classe">Classe</label>
        	</s:if>
      	</span>
 <!-- <select name="classe" id="classe" cssClass="span5"><option>xxxxx</option></select> -->
   <!--  <s:if test="%{soggettoAbilitato}">  
     	 <span class="radio guidata"><a href="#guidaSog" data-toggle="modal" class="btn btn-primary">complilazione guidata</a></span>
     </s:if> -->
     
      <s:if test="%{subImpegnoSelected==null || !subImpegnoSelected}">
      	<s:if test="%{soggettoImpegnoEffettivo!=null || classeSoggettoEffettivo!=null}">
             <s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
          		   headerValue="" name="step1Model.soggetto.classe" cssClass="span5"  
       	 	       listKey="id" listValue="codice+' - '+descrizione" /> 
      	</s:if>
      	<s:else>
      		     <s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
          		   headerValue="" name="step1Model.soggetto.classe" cssClass="span5"  
       	 	       listKey="id" listValue="codice+' - '+descrizione" disabled="true"/> 
      	</s:else>
      </s:if> 	 	       	 
      
      <s:if test="%{subImpegnoSelected==null || !subImpegnoSelected}">
      	<s:if test="%{soggettoImpegnoEffettivo!=null || classeSoggettoEffettivo!=null}">
	   	  <span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	   	</s:if>
	  </s:if>
	  <s:else>
	  	 	  <span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	  </s:else> 	  
	   	  
    </div>
  </div>
</fieldset>
                