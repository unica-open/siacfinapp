<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="accordion" id="soggetto2">
  <div class="accordion-group">
    <div class="accordion-heading">    
      <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#soggetto2" href="#2">
      <!--i class="icon-pencil icon-2x"></i-->Altri classificatori<span class="icon">&nbsp;</span>
      </a>
    </div>
    <div id="2" class="accordion-body collapse">
      <div class="accordion-inner">
      
      
    
      
       <s:if test="listaClassificatoriGen11!=null && listaClassificatoriGen11.size()>0">
	        <div class="control-group">
	          <s:iterator  value="listaClassificatoriGen11" status="statGen11">
	            <s:if test="#statGen11.first == true">
	          		<label for="allegati" class="control-label"><s:property value="tipoClassificatore.descrizione"/></label>
	          	</s:if>
	          </s:iterator>
	          <div class="controls">     
	            
	            <s:select list="listaClassificatoriGen11" id="listaClassificatoriGen11"  
	                   name="step2Model.classGenSelezionato1" 
	                   headerKey="" headerValue=""  
	       	 	       listKey="codice" listValue="descrizione"
	       	 	       cssClass="span9" /> 
	            
	            
	          </div>
	        </div>
        </s:if>
        
        <s:if test="listaClassificatoriGen12!=null && listaClassificatoriGen12.size()>0">
	        <div class="control-group">
	           <s:iterator  value="listaClassificatoriGen12" status="statGen12">
	           	<s:if test="#statGen12.first == true">
	          		<label for="allegati" class="control-label"><s:property value="tipoClassificatore.descrizione"/></label>
	          	</s:if>	
	          </s:iterator>
	          <div class="controls">
	           
	            <s:select list="listaClassificatoriGen12" id="listaClassificatoriGen12"  
	                   name="step2Model.classGenSelezionato2"   
	                   headerKey="" headerValue=""
	       	 	       listKey="codice" listValue="descrizione"
	       	 	       cssClass="span9" /> 
	          </div>
	        </div>
        </s:if>
        
        <s:if test="listaClassificatoriGen13!=null && listaClassificatoriGen13.size()>0">
	        <div class="control-group">
	          <s:iterator  value="listaClassificatoriGen13" status="statGen13">
	            <s:if test="#statGen13.first == true">
	          		<label for="allegati" class="control-label"><s:property value="tipoClassificatore.descrizione"/></label>
	          	</s:if>	
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="listaClassificatoriGen13" id="listaClassificatoriGen13"  
	                   name="step2Model.classGenSelezionato3"
	                   headerKey="" headerValue=""   
	       	 	       listKey="codice" listValue="descrizione"
	       	 	       cssClass="span9" /> 
	          </div>
	        </div>
	    </s:if>   
	    
	    
	    <s:if test="listaClassificatoriGen14!=null && listaClassificatoriGen14.size()>0"> 
	        <div class="control-group">
	           <s:iterator  value="listaClassificatoriGen14" status="statGen14">
	             <s:if test="#statGen14.first == true"> 
	          		<label for="allegati" class="control-label"><s:property value="tipoClassificatore.descrizione"/></label>
	          	 </s:if>
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="listaClassificatoriGen14" id="listaClassificatoriGen14"  
	                   name="step2Model.classGenSelezionato4"
	                   headerKey="" headerValue=""   
	       	 	       listKey="codice" listValue="descrizione"
	       	 	       cssClass="span9" /> 
	          </div>
	        </div>
        </s:if>
        
         <s:if test="listaClassificatoriGen15!=null && listaClassificatoriGen15.size()>0"> 
	        <div class="control-group">
	          <s:iterator  value="listaClassificatoriGen15" status="statGen15">
	             <s:if test="#statGen15.first == true">
	          		<label for="allegati" class="control-label"><s:property value="tipoClassificatore.descrizione"/></label>
	          	 </s:if>
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="listaClassificatoriGen15" id="listaClassificatoriGen15"  
	                   name="step2Model.classGenSelezionato5"   
	                   headerKey="" headerValue=""
	       	 	       listKey="codice" listValue="descrizione"
	       	 	       cssClass="span9" /> 
	          </div>
	        </div>
	      </s:if>  




      </div>
    </div>
  </div>
</div>