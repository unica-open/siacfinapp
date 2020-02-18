<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>




  
	   <div class="control-group" >
	    <label class="control-label" for="progetto">Progetto</label>
	      
	      <div class="controls">
	      
	       <s:textfield id="progetto" name="step1Model.progetto" cssClass="lbTextSmall span3" ></s:textfield>
	      
      <s:if test="oggettoDaPopolareImpegno()"> 
	       
	       <span class="al">
        	<label class="radio inline" for="scadenza">Cronoprogramma </label>
      	   </span>
      	   
	       <s:textfield id="cronoprogramma" var="varCronoprogramma" name="step1Model.cronoprogramma" cssClass="lbTextSmall span4" readonly="true"></s:textfield>
      	   <s:hidden id="idCronoprogramma" var="varIdCronoprogramma" name="step1Model.idCronoprogramma" />
	       
	       <span class="hide"><s:include value="/jsp/movgest/include/labelProgetto.jsp" /></span>
	       
       </s:if>
       <s:else>
	       		<s:include value="/jsp/movgest/include/labelProgetto.jsp" />
       </s:else>
       
	       <span class="radio guidata"><a id="linkCompilazioneGuidataProgetto" href="#guidaProg" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
	       
	     </div>



<!-- <br/> -->
	  	       
<!--   	  <div class="control-group"> -->
<%-- 	    <span class="control-label">Attivit&agrave; prevista </span> --%>
<!-- 	    <div class="controls">     -->
<%-- 	       <s:textfield id="attivitaPrevista" name="step1Model.attivitaPrevista" cssClass="lbTextSmall span4" readonly="true" ></s:textfield> --%>
<%--       	    --%>
<!-- 	    </div> -->
<!-- 	  </div> -->
	       
	       <s:hidden id="idSpesaCronoprogramma" var="varIdSpesaCronoprogramma" name="step1Model.idSpesaCronoprogramma" />
	       
	  </div>
  
