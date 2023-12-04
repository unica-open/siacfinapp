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
<%--    <s:include value="/jsp/include/javascriptTree.jsp" /> --%>
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
<div class="container-fluid-banner">



<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


<div class="container-fluid">
<div class="row-fluid">
<div class="span12 ">
<div class="contentPage">
<%-- SIAC-7952 rimuovo .do dalla action --%>    
<s:form id="mainForm" method="post" action="elencoAccertamenti">
<%-- SIAC-7630 --%>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%-- <s:if test="hasActionErrors()">
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
</s:if> --%>
<h3>Risultati di ricerca Accertamenti</h3> 

<display:table name="sessionScope.RISULTATI_RICERCA_ACCERTAMENTI" 
	class="table tab_left table-hover" 
	summary="riepilogo accertamenti" 
	partialList="true" size="resultSize"
	pagesize="10" requestURI="elencoAccertamenti.do" 
	uid="ricercaAccertamentoID" keepStatus="${status}" clearStatus="${status}">
		<display:column title="Anno" property="annoMovimento" />
		<display:column title="Numero">
			<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaAccertamentoID.descrizione}"/>">
				<s:property value="%{#attr.ricercaAccertamentoID.numero.intValue()}"/>
			</a>
		</display:column>
		<display:column title="Struttura Comp" ><s:property value="%{#attr.ricercaAccertamentoID.strutturaCompetente}"/></display:column>
		<display:column title="Soggetto">
              <s:if test="%{#attr.ricercaAccertamentoID.classeSoggetto == null}">
              	<s:property value="%{#attr.ricercaAccertamentoID.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaAccertamentoID.soggetto.denominazione}"/>
              </s:if>                   
              <s:else>
             	 <s:property value="%{#attr.ricercaAccertamentoID.classeSoggetto.codice}"/>-<s:property value="%{#attr.ricercaAccertamentoID.classeSoggetto.descrizione}"/>
              </s:else>
		</display:column>
		
		<display:column title="Capitolo"><s:property value="%{#attr.ricercaAccertamentoID.capitoloEntrataGestione.annoCapitolo}"/>/<s:property value="%{#attr.ricercaAccertamentoID.capitoloEntrataGestione.numeroCapitolo}"/>/<s:property value="%{#attr.ricercaAccertamentoID.capitoloEntrataGestione.numeroArticolo}"/>/<s:property value="%{#attr.ricercaAccertamentoID.capitoloEntrataGestione.numeroUEB}"/> </display:column>
		<display:column title="Strutt. Amm." property="capitoloEntrataGestione.strutturaAmministrativoContabile.codice" />	 
		<display:column title="Stato" property="descrizioneStatoOperativoMovimentoGestioneEntrata"/>
		
	    <display:column title="Sub">
		   <s:if test="%{#attr.ricercaAccertamentoID.presenzaSubNonAnnullati == true}">
              	SI
              </s:if>                   
              <s:else>
             	NO
              </s:else>
		</display:column>
		
		<display:column title="Provvedimento">
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.anno}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.numero}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.tipoAtto.codice}"/>
			</a>
		</display:column>
		<!-- SIAC-6929 -->
		<display:column title="Blocco Rag.">
		   <s:if test="%{#attr.ricercaAccertamentoID.attoAmministrativo == null || #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == null}">
             	N/A
           </s:if>
		   <s:if test="%{ #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == true}">
              	SI
           </s:if>                   
          <s:if test="%{#attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == false}">
             	NO
          </s:if>
		</display:column>
		
		<display:column title="Importo" property="importoAttuale"  class="tab_Right" 
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>

		<display:column title=""  class="tab_Right">
		    	<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right" id="ul_action">
						<s:if test="isAbilitatoAggiornaAccertamento(#attr.ricercaAccertamentoID.uid)">
			    			<s:if test="isAbilitato(2, #attr.ricercaAccertamentoID.uid)">
			    				<s:if test='%{#attr.ricercaAccertamentoID.statoOperativoMovimentoGestioneEntrata != "P" || #attr.ricercaAccertamentoID.attoAmministrativo == null || #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria != true}'>
			    					<li><a href="aggiornaAccertamentoStep1.do?annoAccertamento=${ricercaAccertamentoID.annoMovimento}&numeroAccertamento=${ricercaAccertamentoID.numero}">aggiorna</a></li>
								</s:if>	
	                  		</s:if>
                  		</s:if>
                  		<%-- task-131 <s:url id="consultaUrl" action="consultaAccertamento" escapeAmp="false"> --%>
			        	<s:url var="consultaUrl" action="consultaAccertamento" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaAccertamentoID.annoMovimento}" />
			        		<s:param name="numero" value="%{#attr.ricercaAccertamentoID.numero}" />
	                    </s:url>
	                    <s:if test="isAbilitato(1, #attr.ricercaAccertamentoID.uid)">
                  			<li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
						</s:if>
						  

 						<s:if test="isAbilitatoAnnullaAccertamento(#attr.ricercaAccertamentoID.uid)">
	                         <s:if test="isAbilitato(3, #attr.ricercaAccertamentoID.uid)">
	                         	<s:if test="%{#attr.ricercaAccertamentoID.attoAmministrativo == null || #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria != true}"> 
	                         		<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaAccertamentoID.uid}"/>_<s:property value="%{#attr.ricercaAccertamentoID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaAccertamentoID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaAccertamentoID.annoMovimento}"/>_<s:property value="%{#attr.ricercaAccertamentoID.annoMovimento}"/>" href="#msgAnnullaAccertamento" data-toggle="modal" class="linkAnnullaAccertamento"> 
	                         
	                         		annulla</a>
	                         		</li>
	                         	</s:if>
	                         </s:if>
                         </s:if>

                  		
                  		

		    		</ul>
				</div>
		 </display:column>
</display:table>

		<p>
		</div>
        <div class="Border_line"></div>
		<s:if test="isAbilitatoGestisciAccertamento() || isAbilitatoGestisciAccertamentoDec()">
			<s:if test="isFaseBilancioAbilitata()">
				<!-- task-131 <s:submit name="inserisci" value="inserisci accertamenti" method="inserisciAccertamenti" cssClass="btn btn-secondary" />-->
				<s:submit name="inserisci" value="inserisci accertamenti" action="elencoAccertamenti_inserisciAccertamenti" cssClass="btn btn-secondary" />
			</s:if>	
		</s:if>	
		</p>
		
        <s:hidden id="uidAccertamentoDaAnnullare" name="uidAccertamento"/>
        <s:hidden id="annoAccertamentoAccertamentoDaPassare" name="annoAccertamentoAccertamentoDaPassare"/>
        <s:hidden id="numeroAccertamentoDaPassare" name="numeroAccertamentoDaPassare"/>

		<s:set var="selezionaProvvedimentoAction" value="%{'elencoAccertamenti_selezionaProvvedimento'}" />        
        <s:set var="clearRicercaProvvedimentoAction" value="%{'elencoAccertamenti_clearRicercaProvvedimento'}" />	          
        <s:set var="ricercaProvvedimentoAction" value="%{'elencoAccertamenti_ricercaProvvedimento'}" />	          
        <s:set var="eliminaAction" value="%{'elencoAccertamenti_elimina'}" />	  
        <s:set var="selezionaProvvedimentoInseritoAction" value="%{'elencoAccertamenti_selezionaProvvedimentoInserito'}" />	
		<s:set var="inserisciProvvedimentoAction" value="%{'elencoAccertamenti_inserisciProvvedimento'}" />	
	       
	    <s:set var="annullaAccertamentoAction" value="%{'elencoAccertamenti_annullaAccertamento'}" />        
      
        <s:set var="gestisciForwardAction" value="%{'elencoAccertamenti_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'elencoAccertamenti_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'elencoAccertamenti_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'elencoAccertamenti_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'elencoAccertamenti_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'elencoAccertamenti_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'elencoAccertamenti_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'elencoAccertamenti_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'elencoAccertamenti_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'elencoAccertamenti_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'elencoAccertamenti_salvaConByPassDodicesimi'}" />	          
		    	  	
        <s:include value="/jsp/movgest/include/modal.jsp" /> 
        <p class="marginLarge"> 
  	   		<s:include value="/jsp/include/indietro.jsp" />
  		</p>  	                                 
      </s:form>
      </div>	
    </div>	
  </div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		var mainForm = $("#mainForm");
		$(".linkAnnullaAccertamento").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#uidAccertamentoDaAnnullare").val(supportId[1]);
				//$("#numeroImpegnoDaAnnullare").val(supportId[2]);
				$("#numeroAccertamentoDaAnnullare").val(supportId[2]); //utilizzato solo per accertamento
				$("#numeroAccertamentoDaPassare").val(supportId[3]);
				$("#annoAccertamentoAccertamentoDaPassare").val(supportId[4]);
				//$("#annoImpegnoImpegnoDaAnnullare").val(supportId[5]);
				$("#annoAccertamentoAccertamentoDaAnnullare").val(supportId[5]);//utilizzato solo per accertamento
				
				
			}
		});
		
	});
</script>

