<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>
<body>                       
<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
<a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
    <div class="row-fluid">
		<div class="span12 contentPage">
		
        <s:form id="ricercaMutuo" action="ricercaMutuo.do" method="post">
		<!--#include virtual="include/alertErrorSuccess.html" -->
		<h3>Ricerca mutuo</h3>
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<div class="step-content">
            <div class="step-pane active" id="step1">
            <br/>
            <p>
          		<s:submit name="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />
          	</p>
          	<br>
		
				<h4>Mutuo</h4>
				<fieldset class="form-horizontal">        		              
					<div class="control-group">
						<label class="control-label" for="numero">Numero mutuo</label>
						<div class="controls">   
							<s:textfield id="numeroMutuo" cssClass="lbTextSmall span2" name="numeroMutuo" />
							<span class="al">
							 <label class="radio inline" for="registrazione">Num. registrazione</label>
							</span>
							
							<s:textfield id="numeroRegistrazione" cssClass="lbTextSmall span2" name="numeroRegistrazione" />                      
						</div>
					</div>   
				</fieldset>
				
				<!--#include virtual="include/provvedimento_ricerca.html" -->
				<!--#include virtual="include/soggetto_mutuo.html" -->  
                <!--#include virtual="include/modal.html" -->       
                 <s:include value="/jsp/mutuo/include/provvedimentoMutuo.jsp" />
                 <div id="refreshHeaderSoggetto">
		           	<s:include value="/jsp/mutuo/include/headerSoggettoMutuo.jsp"/>
		         </div>
		         <s:include value="/jsp/mutuo/include/soggettoMutuo.jsp" /> 
                 
                 <s:include value="/jsp/include/modalProvvedimenti.jsp" />
                 <s:include value="/jsp/include/modalSoggetto.jsp" />
                 
                 <br/> <br/>    
				  <p>
				  
			        <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
	           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
						<%-- CR-2023 da eliminare
						<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/>
						--%>
						<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
						<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
					<!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
				  
						<s:include value="/jsp/include/indietro.jsp" /> 
	         					
       					<span class="pull-right">
       						<s:submit name="cerca" value="cerca" method="cerca" cssClass="btn btn-primary" />
						</span>
				   </p>
            </div>
		</div>
		  

                 
        </s:form>
      </div>
    </div>	 
  </div>	
  <script type="text/javascript">
  
  
//   $(window).load(function() {
// 		// apertura automatica struttura amministrativa
// 		if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
		 	   
// 		       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
		           
// 		    	   $("#testataStruttura").click();
		    	   
// 		    	   $("#lineaStruttura").click();
		    	   
		    	   
// 		       }
// 		}    
		
// 	});	

	$(document).ready(function() {
// 		$("#linkCompilazioneGuidataProvvedimento").click(function(){
// 			initRicercaGuidataProvvedimento(
// 					$("#annoProvvedimento").val(), 
// 					$("#numeroProvvedimento").val(),
// 					$("#listaTipiProvvedimenti").val()
// 			);
// 		});
		
		
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
		
		
		$("#linkCompilazioneGuidataSoggettoMutuo").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreMutuo").val(),
				null
			);
		});
	});
	
</script>  
<s:include value="/jsp/include/footer.jsp" />