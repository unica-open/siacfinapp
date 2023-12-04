<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<div class="accordion" id="soggetto1">
  <div class="accordion-group">
    <div class="accordion-heading">          
      <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#soggetto1" href="#1">
      Transazione elementare XXXXXXXXXXXXXXXXX<span class="icon">&nbsp;</span></a>
    </div>
    <div id="1" class="accordion-body collapse">
      <div class="accordion-inner">
        
	        <div class="control-group">
	          <label for="nomeautore" class="control-label">Missione</label>
	          <div class="controls"> 
	        
	         <s:if test="gestioneOrdinativoStep1Model.transazione.listaMissione!=null">
	         	<s:select list="gestioneOrdinativoStep1Model.transazione.listaMissione" id="listaMissione"  disabled="true"
	                   name="gestioneOrdinativoStep1Model.transazione.missioneSelezionata" cssClass="span10"  
	       	 	       listKey="codice" listValue="descrizione" /> 
	       		<s:hidden value="gestioneOrdinativoStep1Model.transazione.missioneSelezionata"/>
	       	 </s:if>	       
	          </div>
	        </div>
        
        

        
	        <div class="control-group">
	          <label for="soggetto5" class="control-label">Programma <!--a class="tooltip-test" title="selezionare prima la Missione" href="#"><i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima la Missione</span></i></a--></label>
	          <div class="controls">
	          <s:if test="gestioneOrdinativoStep1Model.transazione.listaMissione!=null">
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaProgramma" id="listaProgramma"  disabled="true"
	                   name="gestioneOrdinativoStep1Model.transazione.programmaSelezionato" cssClass="span10"  
	       	 	       listKey="codice" listValue="descrizione" /> 
	       	 	<s:hidden value="gestioneOrdinativoStep1Model.transazione.programmaSelezionato"/>
	       	 </s:if>	
	          </div>
	        </div>
	               
        
        
        <div class="control-group">
          <label for="pdc" class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario <!--a class="tooltip-test" title="selezionare prima il macroaggregato" href="#"><i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il macroaggregato</span></i></a--></label>
          <div class="controls">
            <!-- gestisce l'albero -->
           <s:property value="gestioneOrdinativoStep1Model.transazione.pianoDeiConti.codice"/> - <s:property value="gestioneOrdinativoStep1Model.transazione.pianoDeiConti.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModal" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Piano dei Conti</a>
            <!-- Modal -->
            <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 id="myModalLabel">Piano dei Conti</h3>
              </div>
              <div class="modal-body">
                <ul id="elementiPdcInserisciImpegnoStep2" class="ztree"></ul>
              </div>
              <div class="modal-footer">
              		<s:submit value="conferma" name="conferma" method="confermaPdc" cssClass="btn btn-primary" aria-hidden="true"></s:submit>
              </div>              
            </div>
            <!-- fine modale-->
          </div>
        </div>
        
	<%-- CR-2023 da eliminare     
		<div class="control-group">
          <label for="contoEconomicoLabel" class="control-label">Conto economico</label>
          <div class="controls">
            <!-- gestisce l'albero -->
            <s:property value="gestioneOrdinativoStep1Model.transazione.contoEconomico.codice"/> - <s:property value="gestioneOrdinativoStep1Model.transazione.contoEconomico.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModalContoEconomico" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Conto Economico</a>
            <!-- Modal -->
            <div id="myModalContoEconomico" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalContoEconomicoLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 id="myModalContoEconomicoLabel">Conto Economico</h3>
              </div>
              <div class="modal-body">
                <ul id="contoEconomicoInserisciImpegnoStep2" class="ztree"></ul>
              </div>
              <div class="modal-footer">
              		<s:submit value="conferma" name="confermaContoEconomico" method="confermaContoEconomico" cssClass="btn btn-primary" data-dismiss="modal" aria-hidden="true"></s:submit>
              </div>               
            </div>
            <!-- fine modale-->
          </div>
        </div>  --%>
                  
        <div class="control-group">
          <label for="Titolo" class="control-label">Cofog</label>
          <div class="controls">
          <s:if test="listaCofog!=null">
             <s:select list="gestioneOrdinativoStep1Model.transazione.listaCofog" id="listaCofog"  headerKey="" headerValue="" 
                   name="gestioneOrdinativoStep1Model.transazione.cofogSelezionato" cssClass="span10"  
       	 	       listKey="codice" listValue="descrizione" />
       	  </s:if>	        
          </div>
        </div>
      
        <div class="control-group">
          <label for="Macroaggregato" class="control-label">Codifica Transazione Europea</label>
          <div class="controls">
          	<s:if test="gestioneOrdinativoStep1Model.transazione.listaTransazioneEuropeaSpesa!=null">
	            <s:select list="gestioneOrdinativoStep1Model.transazione.listaTransazioneEuropeaSpesa" headerKey="" headerValue="" id="listaTransEuropea"  
	                   name="gestioneOrdinativoStep1Model.transazione.transazioneEuropeaSelezionato" cssClass="span10"  
	       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
            </s:if>
            
            
          </div>
        </div>
        <div class="control-group">
          <label class="control-label">SIOPE</label>
          <div class="controls">
            <!-- gestisce l'albero -->
            <s:property value="gestioneOrdinativoStep1Model.transazione.siopeSpesa.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModalSiopeSpesa" role="button" class="btn  btn-primary" data-toggle="modal">seleziona SIOPE</a>                      
            <!-- Modal -->
            <div id="myModalSiopeSpesa" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalSiopeSpesaLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 id="myModalSiopeSpesaLabel">SIOPE</h3>
              </div>
              <div class="modal-body">
                <ul id="siopeInserisciImpegnoStep2" class="ztree"></ul>
              </div>     
               <div class="modal-footer">
              		<s:submit value="conferma" name="confermaSiope" method="confermaSiope" cssClass="btn btn-primary" aria-hidden="true"></s:submit>
              </div>        
            </div>
            <!-- fine modale-->
          </div>
        </div>
         
	        <div class="control-group">
	          <label for="CUP" class="control-label">CUP</label>
	          <div class="controls">
	             <s:textfield name="gestioneOrdinativoStep1Model.transazione.cup" id="cup" readonly="true"  /> 
	          </div>
	        </div>    
                  
        <div class="control-group">
          <label for="Ricorrente" class="control-label">Ricorrente</label>
          <div class="controls">
            <s:if test="gestioneOrdinativoStep1Model.transazione.listaRicorrenteSpesa!=null">
             <s:select list="gestioneOrdinativoStep1Model.transazione.listaRicorrenteSpesa" id="listaRicorrenteSpesa"  
             		   headerKey="" headerValue=""
                       name="gestioneOrdinativoStep1Model.transazione.ricorrenteSpesaSelezionato" cssClass="span10"  
       	 	           listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
            </s:if>
            
          </div>
        </div>
                         
        <div class="control-group">
          <label for="ASL" class="control-label">Capitoli perimetro sanitario</label>
          <div class="controls">
          
           <s:if test="gestioneOrdinativoStep1Model.transazione.listaPerimetroSanitarioSpesa!=null">
             <s:select list="gestioneOrdinativoStep1Model.transazione.listaPerimetroSanitarioSpesa" id="listaPerimetroSanitarioSpesa"  
                   name="gestioneOrdinativoStep1Model.transazione.perimetroSanitarioSpesaSelezionato" cssClass="span10" 
                   headerKey="" headerValue="" 
       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
       	  </s:if>	       
          </div>
        </div>               
        <div class="control-group">
          <label for="Unitaria" class="control-label">Programma Pol. Reg. Unitaria</label>
          <div class="controls">
          <s:if test="gestioneOrdinativoStep1Model.transazione.listaPerimetroSanitarioSpesa!=null">
	        <s:select list="gestioneOrdinativoStep1Model.transazione.listaPoliticheRegionaliUnitarie" id="listaPoliticheRegionaliUnitarie"  
	                   name="gestioneOrdinativoStep1Model.transazione.politicaRegionaleSelezionato" cssClass="span10" 
	                   headerKey="" headerValue="" 
	       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
	       </s:if>	 	       
          </div>
        </div>          
        
        
            </div>
    </div>
  </div>
</div>        
      