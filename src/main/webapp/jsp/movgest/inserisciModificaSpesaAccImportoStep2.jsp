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
  
<div class="container-fluid-banner">
<!-- 
=======================================================================
											*****************
											inclusione Banner portale
											*****************
=======================================================================
-->


     <!--#include virtual="../../ris/servizi/siac/include/portalheader.html" -->

<!--
=======================================================================
											*****************
											fine ////////inclusione Banner portale
											*****************
=======================================================================



    <div class="navbarLogin">
     
        <div class="container-fluid"><p class="login-text pull-left">Esercizio 2013 EC - Esercizio Provvisorio con carimento bilancio di previsione</p>
                 <p class="login-text pull-right">
             Mario Rossi <a href="#" class="navbar-link">x</a>
            </p>
          
        
        </div>
   
    </div>
   PAOLO -->

<!--
=======================================================================
											*****************
											inclusione Banner applicativo
											*****************
=======================================================================
-->
   <!--#include virtual="../../ris/servizi/siac/include/applicationHeader.html" -->
<!--
=======================================================================
											*****************
											fine //////inclusione Banner applicativo
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											inclusione login
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											fine //////inclusione login
											*****************
=======================================================================
-->
<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<div class="container-fluid">
  <div class="row-fluid">  
    <div class="span12 contentPage">          
    <!-- <h3>Accertamento ${model.movimentoSpesaModel.accertamento.annoMovimento}/${model.movimentoSpesaModel.accertamento.numero} - ${model.movimentoSpesaModel.accertamento.descrizione} - <s:property value="getText('struts.money.format', {model.movimentoSpesaModel.accertamento.importoAttuale})"/></h3>	--> 

	<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
      
     <s:form id="mainForm" method="post" action="inserisciMovSpesaAccImportoStep2.do" cssClass="form-horizontal">
     	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
		 <s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
      <%--   <s:if test="hasActionErrors()">
		Messaggio di ERROR
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		Messaggio di WARNING
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>            --%>
		
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		
        <div class="step-content"> 
          <div class="step-pane active" id="step2">    
                    <h4>Modifica ${model.movimentoSpesaModel.entrata.numeroModificaMovimentoGestione}
                   <s:if test="%{model.movimentoSpesaModel.modificaImpegnoSubimpegno}">
                   	  - Accertamento e SubAccertamento
                    </s:if>
                    <s:else>
                    	- Accertamento
                    </s:else>
                     - Importo <s:property value="getText('struts.money.format', {model.movimentoSpesaModel.entrata.importoOld})"/>
                    </h4>
           	<%-- <s:include value="/jsp/movgest/include/headerDettaglioModMovGestEntrata.jsp" /> --%>
           	<s:include value="/jsp/movgest/include/headerDettaglioMovGest.jsp" />

            <div class="control-group anniPluriennale">
              <label class="control-label" for="motivo">Anni di pluriennale</label>
              <div class="controls">
              	<s:textfield id="annipluri" name="model.movimentoSpesaModel.numeroPluriennali" cssClass="lbTextSmall span2" />
                <span class="al">
                  <label class="radio inline" for="numero3">Totale importi inseriti</label>
                </span>
                <s:textfield disabled="true" cssClass="lbTextSmall span2" id="totimp" name="model.movimentoSpesaModel.importoTotFormatted" />
              </div>
            </div>
             <%int indice = 1; %>
            <display:table name="movimentoSpesaModel.listaImpegniPluriennali"  
                                 class="table table-striped table-bordered table-hover" 
                                 summary="riepilogo soggetti"
                                 requestURI="inserisciMovSpesaAccImportoStep2.do"
				                 uid="ricercaImplegnoPlur" >
					 <display:column title="Anno" sortable="true" ><s:textfield id="annoImpPluriennale" cssClass="input-small" onkeyup="return checkItNumbersOnly(event)" name="movimentoSpesaModel.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].annoImpPluriennale"></s:textfield> </display:column>
		             <display:column title="Importo" ><s:textfield id="importoImpPluriennale_%{#attr.ricercaImplegnoPlur_rowNum}" cssClass="input-small soloNumeri decimale" name="movimentoSpesaModel.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].importoImpPluriennaleString"></s:textfield></display:column>
		             <display:column title="Data Scadenza" ><s:textfield id="dataScadenzaImpPluriennale_%{#attr.ricercaImplegnoPlur_rowNum}" cssClass="input-small" name="movimentoSpesaModel.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].dataScadenzaImpPluriennaleString"></s:textfield></display:column>
		             <display:column title="Azioni Modifica" >
		             <div class="btn-group">
		             	<a class="btn dropdown-toggle" href="#" onclick="pulisciCampi(<%=indice%>);"  >Pulisci</a>
		             </div>
		             <%indice++; %> 
		             </display:column>				 
				 </display:table>
          </div>
        </div>
         <s:include value="/jsp/movgest/include/modal.jsp" /> 
        <p class="margin-large">
            <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" />  
        	<a class="btn" href="">annulla</a>    
        	<s:submit cssClass="btn btn-primary pull-right" value="Salva" method="salvaAccertamentiPluriennali" />
        	
			<a id="linkMsgProseguiPlurAcc" href="#msgControlloProseguiPlurAcc" data-toggle="modal" style="display:none;"></a>
        </p>     
      </s:form>
    </div>
  </div>	 
</div>	


<script type="text/javascript">

$(document).ready(function() {
<s:if test="checkProseguiPlurAccertamento">
	$("#linkMsgProseguiPlurAcc").click();
</s:if>
	
	
});


function pulisciCampi(numero){

	document.getElementById("ricercaImplegnoPlur").rows[numero].cells[0].childNodes[0].value='';
	document.getElementById("ricercaImplegnoPlur").rows[numero].cells[1].childNodes[0].value='';
	document.getElementById("ricercaImplegnoPlur").rows[numero].cells[2].childNodes[0].value='';
		
/* 	$('#importoImpPluriennale_' + numero).val('');
	$('#dataScadenzaImpPluriennale_' + numero).val('');
 */	
}

</script>


<s:include value="/jsp/include/footer.jsp" />