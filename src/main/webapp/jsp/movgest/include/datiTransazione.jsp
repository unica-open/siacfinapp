<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>



        <s:if test="oggettoDaPopolareImpegno()"> 
	        <div class="control-group">
	          <label for="nomeautore" class="control-label">Missione</label>
	          <div class="controls"> 
	        
	         <s:if test="listaMissione!=null">
	         	<s:select list="listaMissione" id="listaMissione"  disabled="true"
	                   name="step2Model.missioneSelezionata" cssClass="span10"  
	       	 	       listKey="codice" listValue="descrizione" /> 
	       		<s:hidden value="step2Model.missioneSelezionata"/>
	       	 </s:if>	       
	          </div>
	        </div>
        
        

        
	        <div class="control-group">
	          <label for="soggetto5" class="control-label">Programma <!--a class="tooltip-test" title="selezionare prima la Missione" href="#"><i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima la Missione</span></i></a--></label>
	          <div class="controls">
	            <s:select list="listaProgramma" id="listaProgramma"  disabled="true"
	                   name="step2Model.programmaSelezionato" cssClass="span10"  
	       	 	       listKey="codice" listValue="descrizione" /> 
	       	 	<s:hidden value="step2Model.programmaSelezionato"/>
	          </div>
	        </div>
	    </s:if>            
        
        
        <div class="control-group">
          <label for="pdc" class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario <!--a class="tooltip-test" title="selezionare prima il macroaggregato" href="#"><i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il macroaggregato</span></i></a--></label>
          <div class="controls">
            <!-- gestisce l'albero -->
           <s:property value="step2Model.pianoDeiConti.codice"/> - <s:property value="step2Model.pianoDeiConti.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModal" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Piano dei Conti</a>
            <!-- Modal -->
            <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
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
        
        <!-- CONTO ECONOMICO - CR 2023 DA ELIMINARE -->
		<%--         
		<div class="control-group">
          <label for="contoEconomicoLabel" class="control-label">Conto economico</label>
          <div class="controls">
            <!-- gestisce l'albero -->
            <s:property value="step2Model.contoEconomico.codice"/> - <s:property value="step2Model.contoEconomico.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModalContoEconomico" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Conto Economico</a>
            <!-- Modal -->
            <div id="myModalContoEconomico" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalContoEconomicoLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
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
        </div>  
        --%>
        
        <s:if test="oggettoDaPopolareImpegno()">              
	        <div class="control-group">
	          <label for="Titolo" class="control-label">Cofog</label>
	          <div class="controls">
	             <s:select list="listaCofog" id="listaCofog"  headerKey="" headerValue="" 
	                   name="step2Model.cofogSelezionato" cssClass="span10"  
	       	 	       listKey="codice" listValue="descrizione" /> 
	          </div>
	        </div>
        </s:if>
        <div class="control-group">
          <label for="Macroaggregato" class="control-label">Codifica Transazione Europea</label>
          <div class="controls">
            <s:select list="listaTransazioneEuropeaSpesa" headerKey="" headerValue="" id="listaTransEuropea"  
                   name="step2Model.transazioneEuropeaSelezionato" cssClass="span10"  
       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
            
            
            
          </div>
        </div>
        <div class="control-group">
          <label class="control-label">SIOPE</label>
          <div class="controls">
            <!-- gestisce l'albero -->
            <s:property value="step2Model.siopeSpesa.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModalSiopeSpesa" role="button" class="btn  btn-primary" data-toggle="modal">seleziona SIOPE</a>                      
            <!-- Modal -->
            <div id="myModalSiopeSpesa" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalSiopeSpesaLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
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
         <s:if test="oggettoDaPopolareImpegno()">
	        <div class="control-group">
	          <label for="CUP" class="control-label">CUP</label>
	          <div class="controls">
	             <s:textfield name="step2Model.cup" id="cup" readonly="true"  /> 
	          </div>
	        </div>    
        </s:if>           
        <div class="control-group">
          <label for="Ricorrente" class="control-label">Ricorrente</label>
          <div class="controls">
            
             <s:select list="listaRicorrenteSpesa" id="listaRicorrenteSpesa"  
             		   headerKey="" headerValue=""
                       name="step2Model.ricorrenteSpesaSelezionato" cssClass="span10"  
       	 	           listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
            
            
          </div>
        </div>
                         
        <div class="control-group">
          <label for="ASL" class="control-label">Capitoli perimetro sanitario</label>
          <div class="controls">
          
          
             <s:select list="listaPerimetroSanitarioSpesa" id="listaPerimetroSanitarioSpesa"  
                   name="step2Model.perimetroSanitarioSpesaSelezionato" cssClass="span10" 
                   headerKey="" headerValue="" 
       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
          </div>
        </div>               
        <div class="control-group">
          <label for="Unitaria" class="control-label">Programma Pol. Reg. Unitaria</label>
          <div class="controls">
          
        <s:select list="listaPoliticheRegionaliUnitarie" id="listaPoliticheRegionaliUnitarie"  
                   name="step2Model.politicaRegionaleSelezionato" cssClass="span10" 
                   headerKey="" headerValue="" 
       	 	       listKey="codice" listValue="%{codice + ' - ' + descrizione}" /> 
          </div>
        </div>               
      