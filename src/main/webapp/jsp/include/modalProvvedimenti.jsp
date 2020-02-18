<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

  <!--modale provvedimento -->

  <div id="guidaProv" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
    <div class="modal-body">
      <h4>Consulta provvedimento</h4>	
      <!-- RM l'ho spostato io per rendere piu usabile la modale, sotto il cerca e' illeggibile  -->
      
      
      <fieldset class="form-horizontal">        		 
        <div id="campiRicercaProv" class="accordion-body collapse in">
        <div class="control-group">
          <label class="control-label" for="annoProvvedimentoRicerca">Anno *</label>
          <div class="controls">   
            <s:textfield id="annoProvvedimentoRicerca" cssClass="lbTextSmall parametroRicercaProvvedimento" name="provvedimentoRicerca.annoProvvedimento" onkeyup="return checkItNumbersOnly(event)" maxlength="4" />
            <span class="al">
              <label class="radio inline" for="numeroProvvedimentoRicerca">Numero</label>
            </span>
            <s:textfield id="numeroProvvedimentoRicerca" cssClass="lbTextSmall span2 parametroRicercaProvvedimento" name="provvedimentoRicerca.numeroProvvedimento" onkeyup="return checkItNumbersOnly(event)"  maxlength="6"/>      
            <span class="al">
              <label class="radio inline" for="tipo">Tipo</label>
            </span>
            <s:select id="listaTipiProvvedimentoRicerca" 
            	cssClass="span4 parametroRicercaProvvedimento" 
            	name="provvedimentoRicerca.tipoProvvedimento" 
             	headerKey="" 
              	headerValue=""   
       	 	 	list="listaTipiProvvedimenti" 
       	 	 	listKey="uid" listValue="descrizione" 
       	 	/>
       	  </div>
        </div>
        <div class="control-group">      
          <label class="control-label">Struttura Amministrativa </label>
          <div class="controls">                    
            <div class="accordion span9 struttAmm">
              <div class="accordion-group">
                <div class="accordion-heading">    
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3b">Seleziona la Struttura amministrativa <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmRicercaProvvedimento"></i></a>
                </div>
                <div id="3b" class="accordion-body collapse">
                  <!-- ALBERO VISUALIZZATO -->
                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvedimentoDiv">
                    <ul id="strutturaAmministrativaRicercaProvvedimento" class="ztree treeStruttAmm"></ul>
                  </div>
                  <!-- ALBERO IN ATTESA -->
                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvedimentoWait">
                    Attendere prego..
                  </div>
                  
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="control-group">
          <label class="control-label" for="oggettoImpegno">Oggetto</label>
          <div class="controls">
            <s:textfield id="oggettoImpegno" cssClass="lbTextSmall span9 parametroRicercaProvvedimento" name="provvedimentoRicerca.oggetto" />
            <%-- <a class="btn btn-primary" href="#"><i class="icon-search icon"></i> cerca</a--><!--<a class="btn" href="#">inserisci</a> --%>
          </div>
        </div>
        </div>
      </fieldset>

      <a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataProvvedimento" data-toggle="collapse" data-parent="#guidaProv" href="#campiRicercaProv">
        <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"></span>
      </a>  
      
      <div id="gestioneRisultatoRicercaProvvedimenti" style="clear:both; padding-top:3px;">
        <s:include value="/jsp/movgest/include/risultatoRicercaElencoProvvedimento.jsp" />
      </div>     

    </div>
       
    <div class="modal-footer">
      <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
	  <s:submit id="cercaProvvedimentoSubmit" name="cerca" value="conferma" method="selezionaProvvedimento" cssClass="btn btn-primary pull-right" />
    </div>
  </div>
  
  <script type="text/javascript">
  
  	/**
  	   RM: Queta function non viene usata, e' stata sosituita da  initRicercaGuidataProvvedimentoConStruttura
  	   .. l'ho verificato nella gestione (CR 1839) della ricerca immediata se inseriti anno e numero 
  	*/
    function initRicercaGuidataProvvedimento (anno, numero, tipo) {
    	
    	$("#annoProvvedimentoRicerca").val(anno);
		$("#numeroProvvedimentoRicerca").val(numero);
		$("#listaTipiProvvedimentoRicerca").val(tipo);
		$("#campiRicercaProv").attr("class", "accordion-body collapse in");
		$("#campiRicercaProv").attr("style", "height: auto");
		$("#campiRicercaProv").show();
		
		
		$.ajax({
			url: '<s:url method="clearRicercaProvvedimento"/>',
		    success: function(data)  {
			   	$("#gestioneRisultatoRicercaProvvedimenti").html(data);
			}
		});
	}

	$(document).ready(function() {
		$("#ricercaGuidataProvvedimento").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaProvvedimento");
			var strutturaAmministrativaParam = "";
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = "&strutturaAmministrativaSelezionata=" + currentNode.uid;
				});
			}
			$.ajax({
				url: '<s:url method="ricercaProvvedimento"/>',
				type: 'POST',
				data: $(".parametroRicercaProvvedimento").serialize() + strutturaAmministrativaParam,
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaProvvedimenti").html(data);
				}
			});
		});	
	});
	
	

	
	// ricerca provvedimenti in cui nella pagina compare anche la struttura ad esempio: ricerca carta e ordinativo incasso
	function initRicercaGuidataProvvedimentoConStruttura (anno, numero, tipo, struttura) {
      
		$("#annoProvvedimentoRicerca").val(anno);
        $("#numeroProvvedimentoRicerca").val(numero);
        $("#listaTipiProvvedimentoRicerca").val(tipo);
        
               
		var strutturaAmministrativaParam = "";
		
		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaProvvedimento");
        treeObj.expandAll(false);
        if (struttura!=null) {
        	 
               var node = treeObj.getNodeByParam("uid", struttura.uid, null);
			   if(node!=null){
            	   	 treeObj.checkNode(node, true);
                     if (struttura.parentTId != null) {
                            node = treeObj.getNodeByTId(struttura.parentTId);
	                       
                            if (node != null) {
                            	treeObj.expandNode(node, true);
                            }      
                     }
                     $("#3b").attr("class", "accordion-body collapse in");
                     $("#3b").attr("style", "height: auto");
                     $("#3b").show();
               }else{
                     var selectedNode = treeObj.getCheckedNodes(true);
                     selectedNode.forEach(function(currentNode) {
                            treeObj.checkNode(currentNode, false);
                     });
                     $("#3b").attr("class", "accordion-body collapse");
                     $("#3b").attr("style", "height: 0");
                     $("#3b").show();
               }
               
               
               
        }else{
               var selectedNode = treeObj.getCheckedNodes(true);
               selectedNode.forEach(function(currentNode) {
                     treeObj.checkNode(currentNode, false);
               });
        }
        
        $("#campiRicercaProv").attr("class", "accordion-body collapse in");
        $("#campiRicercaProv").attr("style", "height: auto");
        $("#campiRicercaProv").show();
        
        if (treeObj != null) {
        	
			var selectedNode = treeObj.getCheckedNodes(true);
			selectedNode.forEach(function(currentNode) {
				strutturaAmministrativaParam = "&strutturaAmministrativaSelezionata=" + currentNode.uid;
				
			});
		}
        
        // RM CR 1839, se indicati da paginetta anno e numero, scatta la ricerca
	    // se invece c'e' anno e tipo o sac va indicato almeno un altro parametro
        if((anno!='' && numero!='') || (anno!='' && tipo!='') || (anno!='' && struttura!='')){
        	
			url = '<s:url method="ricercaProvvedimento"/>';
			
			$.ajax({
				url: url,
				type: 'POST',
				data: $(".parametroRicercaProvvedimento").serialize()+ strutturaAmministrativaParam, // per ora commento il requisito e' anno e numero + strutturaAmministrativaParam,
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaProvvedimenti").html(data);
				}
			});
        }else	{
			
			url = '<s:url method="clearRicercaProvvedimento"/>';
			$.ajax({
				url: url,
				success: function(data)  {
				    $("#gestioneRisultatoRicercaProvvedimenti").html(data);
				}
			});
		}
		
       /*  $.ajax({
               url: '<s:url method="clearRicercaProvvedimento"/>',
            success: function(data)  {
                      $("#gestioneRisultatoRicercaProvvedimenti").html(data);
               }
        }); */
 	}
	
  </script>
   
  <!--/modale provvedimento -->
      