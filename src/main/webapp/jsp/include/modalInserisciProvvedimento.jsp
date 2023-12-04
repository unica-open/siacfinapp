<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

  <!--modale provvedimento -->

  <div id="guidaInserisciProv" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaInserisciProvLabel" aria-hidden="true">
    <div class="modal-body">
      <h4>Inserisci provvedimento</h4>	
      <!-- RM l'ho spostato io per rendere piu usabile la modale, sotto il cerca e' illeggibile  -->
      
      
       <div id="gestioneEsitoInserimentoProvvedimento" style="clear:both; padding-top:3px;">
        <s:include value="/jsp/movgest/include/risultatoRicercaElencoProvvedimento.jsp" />
      </div> 
      
      <fieldset class="form-horizontal">        		 
        <div id="campiInserimentoProv" class="accordion-body collapse in">
        <div class="control-group">
          <label class="control-label" for="annoProvvedimentoInserimento">Anno *</label>
          <div class="controls">   
            <s:textfield id="annoProvvedimentoInserimento" cssClass="lbTextSmall parametroInserimentoProvvedimento" name="provvedimentoInserimento.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" maxlength="4" />
            <span class="al">
              <label class="radio inline" for="numeroProvvedimentoInserimento">Numero</label>
            </span>
            <s:textfield id="numeroProvvedimentoInserimento" cssClass="lbTextSmall span2 parametroInserimentoProvvedimento" name="provvedimentoInserimento.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)"  maxlength="6"/>      
            <span class="al">
              <label class="radio inline" for="tipo">Tipo</label>
            </span>
            <s:select id="listaTipiProvvedimentoInserimento" 
            	cssClass="span4 parametroInserimentoProvvedimento" 
            	name="provvedimentoInserimento.tipoProvvedimento" 
             	headerKey="" 
              	headerValue=""   
       	 	 	list="listaTipiProvvedimenti" 
       	 	 	listKey="uid" listValue="descrizione" 
       	 	/>
       	  </div>
        </div>
        
        <s:if test="listaStatiProvvedimenti!=null">
	        <div class="control-group">
		          <label class="control-label" for="listaStatiProvvedimentoInserimento">Stato</label>
		          <div class="controls">
		           <s:select id="listaStatiProvvedimentoInserimento" 
		            	cssClass="span4 parametroInserimentoProvvedimento" 
		            	name="provvedimentoInserimento.stato" 
		             	headerKey="" 
		              	headerValue=""   
		       	 	 	list="listaStatiProvvedimenti" 
		       	 	 	listKey="descrizione" listValue="descrizione" 
		       	 	/>
		          </div>
	        </div>
        </s:if>
        
        <div class="control-group">      
          <label class="control-label">Struttura Amministrativa</label>
          <div class="controls">                    
            <div class="accordion span9" class="struttAmmInsProvv">
              <div class="accordion-group">
                <div class="accordion-heading">    
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmInsProvv" href="#4b">Seleziona la Struttura amministrativa <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmInserimentoProvvedimento"></i></a>
                </div>
                <div id="4b" class="accordion-body collapse">
                   <!-- ALBERO VISUALIZZATO  -->
                  <div class="accordion-inner" id="strutturaAmministrativaInserimentoProvvedimentoDiv">
                    <ul id="strutturaAmministrativaInserimentoProvvedimento" class="ztree treeStruttAmm"></ul>
                  </div>
                  <!-- ALBERO IN ATTESA -->
                  <div class="accordion-inner" id="strutturaAmministrativaInserimentoProvvedimentoWait">
                    Attendere prego..
                  </div>
                  
                </div>
              </div>
            </div>
          </div>
        </div>
        
        
        
        <div class="control-group">
          <label class="control-label" for="oggettoProvvedimentoInserimento">Oggetto</label>
          <div class="controls">
            <s:textfield id="oggettoProvvedimentoInserimento" cssClass="lbTextSmall span9 parametroInserimentoProvvedimento" name="provvedimentoInserimento.oggetto" />
            <%-- <a class="btn btn-primary" href="#"><i class="icon-search icon"></i> cerca</a--><!--<a class="btn" href="#">inserisci</a> --%>
          </div>
        </div>
        
        
        
        </div>
      </fieldset>

	
	  <a class="accordion-toggle btn btn-primary pull-right" id="inserimentoProvvedimento" data-toggle="collapse" data-parent="#guidaInserisciProv">
       <i class="icon-search icon"></i>&nbsp;salva&nbsp;<span class="icon"></span>
     </a>  
	 
    
      
<%--       <s:submit id="inserimentoProvvedimentoSubmit" name="salva" value="salva" method="inserisciProvvedimento" cssClass="btn btn-primary pull-right" /> --%>

    </div>
       
    <div class="modal-footer">
	  <!-- task-131 <s:submit id="inserimentoProvvedimentoSubmit" name="conferma" value="conferma" method="selezionaProvvedimentoInserito" cssClass="btn btn-primary pull-right" /> -->
	  <s:submit id="inserimentoProvvedimentoSubmit" name="conferma" value="conferma" action="%{#selezionaProvvedimentoInseritoAction}" cssClass="btn btn-primary pull-right" /> 
    </div>
    
    
  </div>
  
  <script type="text/javascript">
  

  
  
	$(document).ready(function() {
		$("#inserimentoProvvedimento").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaInserimentoProvvedimento");
			var strutturaAmministrativaParam = "";
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = "&strutturaAmministrativaSelezionata=" + currentNode.uid;
				});
			}
			$.ajax({
				// task-131 url: '<s:url method="inserisciProvvedimento"/>',
				url: '<s:url action="%{#inserisciProvvedimentoAction}"/>',				
				type: 'POST',
				data: $(".parametroInserimentoProvvedimento").serialize() + strutturaAmministrativaParam,
			    success: function(data)  {
				    $("#gestioneEsitoInserimentoProvvedimento").html(data);
				}
			});
		});	
	});
	
	
	
  </script>
   
  <!--/modale provvedimento -->
      