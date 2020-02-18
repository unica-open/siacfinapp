<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


   
    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />

    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>
  
  <body>     
  
  <s:include value="/jsp/include/header.jsp" />
                    
  <hr />
<div class="container-fluid-banner">


  <a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">  

      <s:form id="reintroitoOrdinativoPagamentoStep1" action="reintroitoOrdinativoPagamentoStep1" method="post" cssClass="form-horizontal">
		 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 
		<s:hidden id="liquidazioneTrovata" name="liquidazioneTrovata"/>
		<s:hidden id="accertamentoTrovato" name="accertamentoTrovato"/>

		<h3>Reintroito ordinativo di pagamento</h3>
		
		<div id="MyWizard" class="wizard">
		  <ul class="steps">
				<li data-target="#step1" class="active"><span class="badge">1</span>Step 1<span class="chevron"></span></li>
				<li data-target="#step2" class="complete"><span class="badge">2</span>Step 2<span class="chevron"></span></li>
<%-- 				<li data-target="#step3" class="complete"><span class="badge">3</span>Step 3<span class="chevron"></span></li> --%>
			</ul>
		</div>

        <div class="step-content">
          <div class="step-pane active" id="step1">
           
          <!-- Ricerca ordinativo -->
          	<s:if test="!isOrdinativoPresente()">
        		<h4>Ordinativo di pagamento</h4>
        	</s:if>
        	<s:else>
        		<h4>Ordinativo <s:property value="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.numero"/>  del <s:property value="%{reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.dataEmissione}"/> -  Stato <s:property value="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.statoOperativoOrdinativo"/> dal <s:property value="%{reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.dataInizioValidita}"/> </h4>
        	</s:else>
        	
			<div class="control-group">
			    <label class="control-label" for="annoOrdinativoPagamento">Anno </label>
				<div class="controls">
					<s:textfield id="annoOrdinativoPagamento" name="reintroitoOrdinativoStep1Model.annoOrdinativoPagamento" disabled="true" cssClass="span1" maxlength="4" onkeyup="return checkItNumbersOnly(event)"/>
					Numero
					<s:textfield id="numeroOrdId" name="reintroitoOrdinativoStep1Model.numeroOrdinativoPagamento" cssClass="span1" onkeyup="return checkItNumbersOnly(event)"/>
				 	<span id="searchLiq">
				 		<s:submit id="cercaOrdinativoPagamentoId" cssClass="btn btn-primary freezePagina" method="cercaOrdinativoPagamento" value="cerca" name="cerca" />
				 	</span>
				</div>
			</div>
			
			<s:if test="isOrdinativoPresente()">
				<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativoReintroito.jsp" />
				<s:include value="/jsp/ordinativo/include/modalOrdinativoReintroito.jsp" />
			</s:if>
          
          
          <s:include value="/jsp/ordinativo/include/provvedimentoReintroitoOrdinativo.jsp" /> 


		  <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> --%>
					
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
			
			<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
			
            <!-- Modal -->
            <s:include value="/jsp/include/modalSoggetto.jsp" />	
            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
            <s:include value="/jsp/include/modalCapitolo.jsp" />
            <s:hidden id="strutturaDaInserimento"  name="strutturaDaInserimento"></s:hidden>
            <!-- Fine Modal -->
            
            </div>
        </div> 
        
		<p class="margin-medium"> 
		<s:include value="/jsp/include/indietro.jsp" />    
		<s:submit cssClass="btn btn-secondary" method="annullaStep1" value="annulla" name="annulla" />
		
		<a id="linkAssociaNuovoAccertamento" href="#msgControlloSoggetti" style="display: none;" data-toggle="modal"></a>
        <s:submit id="proseguiId" cssClass="btn btn-primary pull-right freezePagina" method="prosegui" value="prosegui" name="prosegui" />
				
		</p>       

      </s:form>
    </div>
  </div>	 
</div>	


<script type="text/javascript">
	
	$(document).ready(function() {
		
	    $("#linkCompilazioneGuidataProvvedimento").click(function(){

			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
				var strutturaAmministrativaParam = "";
				if (treeObj != null) {
					var selectedNode = treeObj.getCheckedNodes(true);
					selectedNode.forEach(function(currentNode) {
						strutturaAmministrativaParam = currentNode;
					});
				}
			
			
				initRicercaGuidataProvvedimentoConStruttura(
					$("#annoProvvedimento").val(), 
					$("#numeroProvvedimento").val(),
					$("#listaTipiProvvedimenti").val(),
					strutturaAmministrativaParam
			);


		});
	    
		$("#proseguiId").click(function(){
			// vado a verificare di aver scelto o meno il provvedimento con 
			// compilazione guidata, funcion definita in genericCustom.js
			preselezionaStrutturaPaginaPrincipale();
		});
		
		
	});
</script>
	
<s:include value="/jsp/include/footer.jsp" />
