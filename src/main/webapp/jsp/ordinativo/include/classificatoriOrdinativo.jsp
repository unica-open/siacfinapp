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
      
      
    
      
       <s:if test="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen11!=null && gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen11.size()>0">
	        <div class="control-group">
	          <s:iterator  value="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen11" status="statGen11">
	            <s:if test="#statGen11.first == true">
	          		<label for="allegati" class="control-label"><s:property value="transazione.tipoClassificatore.descrizione"/></label>
	          	</s:if>
	          </s:iterator>
	          <div class="controls">     
	            
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen11" id="listaClassificatoriGen11"  
	                   name="gestioneOrdinativoStep1Model.transazione.classGenSelezionato1" 
	                   headerKey="" headerValue=""  
	       	 	       listKey="codice" listValue="descrizione" /> 
	            
	            
	          </div>
	        </div>
        </s:if>
        
        <s:if test="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen12!=null && gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen12.size()>0">
	        <div class="control-group">
	           <s:iterator  value="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen12" status="statGen12">
	           	<s:if test="#statGen12.first == true">
	          		<label for="allegati" class="control-label"><s:property value="gestioneOrdinativoStep1Model.transazione.tipoClassificatore.descrizione"/></label>
	          	</s:if>	
	          </s:iterator>
	          <div class="controls">
	           
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen12" id="listaClassificatoriGen12"  
 	                   name="gestioneOrdinativoStep1Model.transazione.classGenSelezionato2"    
	                   headerKey="" headerValue="" 
 	       	 	       listKey="codice" listValue="descrizione" />  
	          </div>
	        </div>
        </s:if>
        
        <s:if test="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen13!=null && gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen13.size()>0">
	        <div class="control-group">
	          <s:iterator  value="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen13" status="statGen13">
	            <s:if test="#statGen13.first == true">
	          		<label for="allegati" class="control-label"><s:property value="gestioneOrdinativoStep1Model.transazione.tipoClassificatore.descrizione"/></label>
	          	</s:if>	
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen13" id="listaClassificatoriGen13"  
 	                   name="gestioneOrdinativoStep1Model.transazione.classGenSelezionato3" 
	                   headerKey="" headerValue=""    
	       	 	       listKey="codice" listValue="descrizione" />  
	          </div>
	        </div>
	    </s:if>   
	    
	    
	    <s:if test="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen14!=null && gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen14.size()>0"> 
	        <div class="control-group">
	           <s:iterator  value="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen14" status="statGen14">
	             <s:if test="#statGen14.first == true"> 
	          		<label for="allegati" class="control-label"><s:property value="gestioneOrdinativoStep1Model.transazione.tipoClassificatore.descrizione"/></label>
	          	 </s:if>
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen14" id="listaClassificatoriGen14"  
 	                   name="gestioneOrdinativoStep1Model.transazione.classGenSelezionato4" 
 	                   headerKey="" headerValue=""   
 	       	 	       listKey="codice" listValue="descrizione" />  
	          </div>
	        </div>
        </s:if>
        
         <s:if test="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen15!=null && gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen15.size()>0"> 
	        <div class="control-group">
	          <s:iterator  value="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen15" status="statGen15">
	             <s:if test="#statGen15.first == true">
	          		<label for="allegati" class="control-label"><s:property value="gestioneOrdinativoStep1Model.transazione.tipoClassificatore.descrizione"/></label>
	          	 </s:if>
	          </s:iterator>
	          <div class="controls input-append">
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaClassificatoriGen15" id="listaClassificatoriGen15"  
 	                   name="gestioneOrdinativoStep1Model.transazione.classGenSelezionato5"    
 	                   headerKey="" headerValue="" 
 	       	 	       listKey="codice" listValue="descrizione" />  
	          </div>
	        </div>
	      </s:if>  




      </div>
    </div>
  </div>
</div>