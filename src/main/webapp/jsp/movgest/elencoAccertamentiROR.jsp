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
<%--corpo pagina--%>
<%--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> --%>


<div class="container-fluid">
<div class="row-fluid">
<div class="span12 ">
<div class="contentPage">
<%-- SIAC-7952 rimuovo .do dalla action --%>  
<s:form id="mainForm" method="post" action="elencoAccertamenti">
<%-- SIAC-7630 --%>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%-- Messaggio di ERROR
<s:if test="hasActionErrors()">
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
</s:if>--%>
<h3>Risultati di ricerca Accertamenti ROR</h3> 
<div class="displayTableFrame" style="overflow-x: auto; overflow-y: hidden;width: 100%;">
<display:table name="sessionScope.RISULTATI_RICERCA_ACCERTAMENTI" 
	class="table tab_left table-hover" 
	summary="riepilogo accertamenti" 
	partialList="true" size="resultSize"
	pagesize="10" requestURI="elencoAccertamenti.do" 
	uid="ricercaAccertamentoID" keepStatus="${status}" clearStatus="${status}">
		<display:column title="Anno" property="annoMovimento" />
		
		
<%-- 		<display:column title="Numero"> --%>
<%-- 			<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaAccertamentoID.descrizione}"/>"> --%>
<%-- 				<s:property value="%{#attr.ricercaAccertamentoID.numero.intValue()}"/> --%>
<%-- 			</a> --%>
<%-- 		</display:column> --%>
		
		
		
		<display:column title="Numero" >
		<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaAccertamentoID.descrizione}"/>">
			<s:property value="%{#attr.ricercaAccertamentoID.numeroAccertamentoPadre.intValue()}"/>
		</a>
		
		</display:column>
		<display:column title="Numero Sub" ><s:property value="%{#attr.ricercaAccertamentoID.numero.intValue()}"/></display:column>
		<display:column title="Struttura Comp" ><s:property value="%{#attr.ricercaAccertamentoID.strutturaCompetenteObj.codice}"/></display:column>
		
		
		
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
		
<%-- 	    <display:column title="Sub"> --%>
<%-- 		   <s:if test="%{#attr.ricercaAccertamentoID.presenzaSubNonAnnullati == true}"> --%>
<%--               	SI --%>
<%--               </s:if>                    --%>
<%--               <s:else> --%>
<%--              	NO --%>
<%--               </s:else> --%>
<%-- 		</display:column> --%>
		
		<display:column title="Provvedimento">
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.anno}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.numero}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaAccertamentoID.attoAmministrativo.tipoAtto.codice}"/>
			</a>
		</display:column>
		<%-- SIAC-6929 --%>
<%-- 		<display:column title="Blocco Rag."> --%>
<%-- 		   <s:if test="%{#attr.ricercaAccertamentoID.attoAmministrativo == null || #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == null}"> --%>
<%--              	N/A --%>
<%--            </s:if> --%>
<%-- 		   <s:if test="%{ #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == true}"> --%>
<%--               	SI --%>
<%--            </s:if>                    --%>
<%--           <s:if test="%{#attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria == false}"> --%>
<%--              	NO --%>
<%--           </s:if> --%>
<%-- 		</display:column> --%>
		
		<display:column title="Importo" property="importoAttuale"  class="tab_Right" 
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
                      
                      
                      
                      
           <!-- SIAC-6997 -->              
        <display:column title="Importo da riaccertare al 31/12" property="importoDaRiaccertare" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
        <display:column title="Importo massimo da riaccertare" property="importoMaxDaRiaccertare" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
        <display:column title="Importo Modifiche" property="importoModifiche" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
        <display:column title="Residuo eventualmente da mantenere" property="residuoDaMantenere" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
        <display:column title="Importo cancellato per insussistenza" property="daCancellareInsussistenza" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
        <display:column title="Importo cancellato per inesigibilita'" property="daCancellareInesigibilita" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
       	<display:column title="Importo Differito ${ricercaModel.annoEsercizio+1}" property="differitoN" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
        <display:column title="Importo Differito ${ricercaModel.annoEsercizio+2}" property="differitoN1" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
        <display:column title="Importo Differito ${ricercaModel.annoEsercizio+3}" property="differitoN2" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
        <display:column title="Importo Differito >${ricercaModel.annoEsercizio+3}" property="differitoNP" class="tab_Right"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuroAbs"/>
                   
                      
                      
                      

		<display:column title=""  class="tab_Right">
		    	<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right" id="ul_action">
		    		
		    		
		    		
		    								<!--SIAC-6997-->
						<!-- Controllo azione gestisci -->
						
			  			<s:if test="isAbilitatoGestisciAccertamento(#attr.ricercaAccertamentoID.uid)">  
						  	<%-- task-131 <s:url id="gestisciUrl" action="aggiornaModificaAccRor.do" escapeAmp="false"> --%>
							<s:url var="gestisciUrl" action="aggiornaModificaAccRor.do" escapeAmp="false">
								<s:param name="annoImpegno" value="%{#attr.ricercaAccertamentoID.annoMovimento}" />
			        			<s:param name="numeroImpegno" value="%{#attr.ricercaAccertamentoID.numeroAccertamentoPadre}" />
			        			<s:param name="numeroSubAccertamento" value="%{#attr.ricercaAccertamentoID.numero}" />
			        			<s:param name="uidMovgest" value="%{#attr.ricercaAccertamentoID.uid}" />
			              	</s:url>
						  	<li><a href="<s:property value="%{gestisciUrl}"/>">gestisci ROR</a></li>
  						 </s:if>  
						  <!--End test per SIAC-6997-->
		    		
		    		
		    		<s:if test='%{#attr.ricercaAccertamentoID.numero == null || #attr.ricercaAccertamentoID.numero == ""  }'>
		    		
		    		
						<s:if test="isAbilitatoAggiornaAccertamento(#attr.ricercaAccertamentoID.uid)">
			    			<s:if test="isAbilitato(2, #attr.ricercaAccertamentoID.uid)">
			    				<s:if test='%{#attr.ricercaAccertamentoID.statoOperativoMovimentoGestioneEntrata != "P" || #attr.ricercaAccertamentoID.attoAmministrativo == null || #attr.ricercaAccertamentoID.attoAmministrativo.bloccoRagioneria != true}'>
			    					<li><a href="aggiornaAccertamentoStep1.do?annoAccertamento=${ricercaAccertamentoID.annoMovimento}&numeroAccertamento=${ricercaAccertamentoID.numeroAccertamentoPadre}">aggiorna</a></li>
								</s:if>	
	                  		</s:if>
                  		</s:if>
                  		<s:url var="consultaUrl" action="consultaAccertamento.do" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaAccertamentoID.annoMovimento}" />
			        		<s:param name="numero" value="%{#attr.ricercaAccertamentoID.numeroAccertamentoPadre}" />
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

                  		</s:if>
                  		

		    		</ul>
				</div>
		 </display:column>
</display:table>

</div>
		<p>
		</div>
        <div class="Border_line"></div>
		<s:if test="isAbilitatoGestisciAccertamento() || isAbilitatoGestisciAccertamentoDec()">
			<s:if test="isFaseBilancioAbilitata()">
				<!-- task-131 <s:submit name="inserisci" value="inserisci accertamenti" method="inserisciAccertamenti" cssClass="btn btn-secondary" />-->		
				<s:submit name="inserisci" value="inserisci accertamenti" action="elencoAccertamentiROR_inserisciAccertamenti" cssClass="btn btn-secondary" />
			</s:if>	
		</s:if>	
		</p>
		
        <s:hidden id="uidAccertamentoDaAnnullare" name="uidAccertamento"/>
        <s:hidden id="annoAccertamentoAccertamentoDaPassare" name="annoAccertamentoAccertamentoDaPassare"/>
        <s:hidden id="numeroAccertamentoDaPassare" name="numeroAccertamentoDaPassare"/>

		<s:set var="selezionaProvvedimentoAction" value="%{'elencoAccertamentiROR_selezionaProvvedimento'}" />
		<s:set var="clearRicercaProvvedimentoAction" value="%{'elencoAccertamentiROR_clearRicercaProvvedimento'}" />	          
        <s:set var="ricercaProvvedimentoAction" value="%{'elencoAccertamentiROR_ricercaProvvedimento'}" />	          
        <s:set var="eliminaAction" value="%{'elencoAccertamentiROR_elimina'}" />	  
        <s:set var="selezionaProvvedimentoInseritoAction" value="%{'elencoAccertamentiROR_selezionaProvvedimentoInserito'}" />	
		<s:set var="inserisciProvvedimentoAction" value="%{'elencoAccertamentiROR_inserisciProvvedimento'}" />	
	 	
	 	<s:set var="annullaAccertamentoAction" value="%{'elencoAccertamentiROR_annullaAccertamento'}" />        
          
        <s:set var="gestisciForwardAction" value="%{'elencoAccertamentiROR_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'elencoAccertamentiROR_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'elencoAccertamentiROR_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'elencoAccertamentiROR_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'elencoAccertamentiROR_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'elencoAccertamentiROR_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'elencoAccertamentiROR_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'elencoAccertamentiROR_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'elencoAccertamentiROR_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'elencoAccertamentiROR_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'elencoAccertamentiROR_salvaConByPassDodicesimi'}" />	          
		    	                
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

