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
<s:form id="mainForm" method="post" action="elencoImpegni">
<%-- SIAC-7630 --%>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%--  Messaggio di ERROR
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
</s:if> --%>
<h3>Risultati di ricerca Impegni ROR</h3> 

<div class="displayTableFrame" style="overflow-x: auto; overflow-y: hidden;width: 100%;">   

<display:table name="sessionScope.RISULTATI_RICERCA_IMPEGNI" 
	class="table tab_left table-hover" 
	summary="riepilogo impegni" 
	pagesize="10" partialList="true" size="resultSize" 
	requestURI="elencoImpegni.do" uid="ricercaImpegnoID" 
	keepStatus="${status}" clearStatus="${status}">
	
		<display:column title="Anno" property="annoMovimento" />
<%-- 		<display:column title="Numero"> --%>
<%-- 			<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaImpegnoID.descrizione}"/>"> --%>
<%-- 				<s:property value="%{#attr.ricercaImpegnoID.numero.intValue()}"/> --%>
<!-- 			</a> -->
<%-- 		</display:column> --%>
		
		<display:column title="Numero" >
		<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaImpegnoID.descrizione}"/>">
			<s:property value="%{#attr.ricercaImpegnoID.numeroImpegnoPadre.intValue()}"/>
		</a>
		
		</display:column>
		<display:column title="Numero Sub" ><s:property value="%{#attr.ricercaImpegnoID.numero.intValue()}"/></display:column>
		<display:column title="Struttura Comp" ><s:property value="%{#attr.ricercaImpegnoID.strutturaCompetenteObj.codice}"/></display:column>
		
		
		<display:column title="Soggetto">
              <s:if test="%{#attr.ricercaImpegnoID.classeSoggetto == null}">
              	<s:property value="%{#attr.ricercaImpegnoID.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaImpegnoID.soggetto.denominazione}"/>
              </s:if>                   
              <s:else>
             	 <s:property value="%{#attr.ricercaImpegnoID.classeSoggetto.codice}"/>-<s:property value="%{#attr.ricercaImpegnoID.classeSoggetto.descrizione}"/>
              </s:else>
		</display:column>
		
		<display:column title="Capitolo"><s:property value="%{#attr.ricercaImpegnoID.capitoloUscitaGestione.annoCapitolo}"/>/<s:property value="%{#attr.ricercaImpegnoID.capitoloUscitaGestione.numeroCapitolo}"/>/<s:property value="%{#attr.ricercaImpegnoID.capitoloUscitaGestione.numeroArticolo}"/>/<s:property value="%{#attr.ricercaImpegnoID.capitoloUscitaGestione.numeroUEB}"/> </display:column>	 
		<display:column title="Strutt. Amm." property="capitoloUscitaGestione.strutturaAmministrativoContabile.codice" />	 
		<!-- SIAC 7349 -->
		<display:column title="Componente" property="componenteBilancioImpegno.descrizioneTipoComponente"/>
		<display:column title="Stato" property="descrizioneStatoOperativoMovimentoGestioneSpesa"/>
		
		<display:column title="Pren.">
		   <s:if test="%{#attr.ricercaImpegnoID.flagPrenotazione == true}">
              	SI
              </s:if>                   
              <s:else>
             	NO
              </s:else>
		</display:column>
		
	  <display:column title="Visto">
		   <s:if test="%{#attr.ricercaImpegnoID.parereFinanziario == true}">
              	SI
              </s:if>                   
              <s:else>
             	NO
              </s:else>
		</display:column>
		
		
		<display:column title="Provvedimento">
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.anno}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.numero}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.tipoAtto.codice}"/>
			</a>
		</display:column>
		
		<!-- SIAC-6929 -->
<%-- 		 <display:column title="Blocco Rag."> --%>
<%-- 		   <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo == null || #attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == null }"> --%>
<!--               	N/A -->
<%--               </s:if>   --%>
<%--               <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == false}"> --%>
<!--               	NO -->
<%--               </s:if>  --%>
<%--               <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == true}"> --%>
<!--               	SI -->
<%--               </s:if>                      --%>
             
<%-- 		</display:column> --%>
		
		<display:column title="Importo" property="importoAttuale" class="tab_Right"
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
      
       
       
         

		<display:column title="" class="tab_Right">
		    	<div class="btn-group">
		    		
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    			<ul class="dropdown-menu pull-right" id="ul_action">
						
						
						<!--SIAC-6997-->
						<!-- Controllo azione gestisci -->
 						<s:if test="isAbilitatoGestisciCruscottoImpegno(#attr.ricercaImpegnoID.uid)">
						  	<%-- task-131<s:url id="gestisciUrl" action="aggiornaModificaMovimentoRor.do" escapeAmp="false"> --%>
						  	<s:url var="gestisciUrl" action="aggiornaModificaMovimentoRor.do" escapeAmp="false">
								<s:param name="annoImpegno" value="%{#attr.ricercaImpegnoID.annoMovimento}" />
			        			<s:param name="numeroImpegno" value="%{#attr.ricercaImpegnoID.numeroImpegnoPadre}" />
			        			<s:param name="numeroSubImpegno" value="%{#attr.ricercaImpegnoID.numero}" />
			        			<s:param name="uidMovgest" value="%{#attr.ricercaImpegnoID.uid}" />
			              	</s:url>
						  	<li><a href="<s:property value="%{gestisciUrl}"/>">gestisci ROR</a></li>
						</s:if> 
						  <!--End test per SIAC-6997-->
						
						
						
						<s:if test='%{#attr.ricercaImpegnoID.numero == null || #attr.ricercaImpegnoID.numero == ""  }'>
						
						<s:if test="isAbilitatoAggiornaImpegno(#attr.ricercaImpegnoID.uid)">
			    			<s:if test="isAbilitato(2, #attr.ricercaImpegnoID.uid)">
			    			
				    			<s:url var="aggiornaUrl" action="aggiornaImpegnoStep1.do" escapeAmp="false">
					        		<s:param name="annoImpegno" value="%{#attr.ricercaImpegnoID.annoMovimento}" />
					        		<s:param name="numeroImpegno" value="%{#attr.ricercaImpegnoID.numeroImpegnoPadre}" />
			                    </s:url>
			                    <!-- SIAC-6929 -->
		                         <s:if test='%{#attr.ricercaImpegnoID.statoOperativoMovimentoGestioneSpesa != "P" ||  (#attr.ricercaImpegnoID.attoAmministrativo == null || #attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true) }'>
								<li><a href="<s:property value="aggiornaUrl"/>">aggiorna</a></li>
								</s:if>
	                  		</s:if>
						</s:if>
						 <!--SIAC-6997-->
						
                  		<s:url var="consultaUrl" action="consultaImpegno.do" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaImpegnoID.annoMovimento}" />
			        		<s:param name="numero" value="%{#attr.ricercaImpegnoID.numeroImpegnoPadre}" />
	                    </s:url>
	                    <s:if test="isAbilitato(1, #attr.ricercaImpegnoID.uid)">
                  		 	<li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
                  		</s:if>

						<s:if test="isAbilitatoAnnullaImpegno(#attr.ricercaImpegnoID.uid)">
	                         <s:if test="isAbilitato(3, #attr.ricercaImpegnoID.uid)"> 
		                         <!-- SIAC-6929 -->
		                         <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo == null ||  #attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true}">
		                         <li>
		                         	<a id="linkAnnulla_<s:property value="%{#attr.ricercaImpegnoID.uid}"/>_<s:property value="%{#attr.ricercaImpegnoID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaImpegnoID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaImpegnoID.annoMovimento}"/>_<s:property value="%{#attr.ricercaImpegnoID.annoMovimento}"/>" href="#msgAnnulla" data-toggle="modal" class="linkAnnullaImpegno"> 
		                         annulla
		                         </a>
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
			<s:if test="isAbilitatoGestisciImpegno() || isAbilitatoGestisciImpegnoDec()">
				<s:if test="isFaseBilancioAbilitata()"> 
					<!-- task-131 <s:submit name="inserisci" value="inserisci impegno" method="inserisciImpegni" cssClass="btn btn-secondary" />-->
					<s:submit name="inserisci" value="inserisci impegno" action="elencoImpegniROR_inserisciImpegni" cssClass="btn btn-secondary" />
				</s:if>	
			</s:if>	
		</p>
        

     
        
        <s:hidden id="uidImpegnoDaAnnullare" name="uidImpegno"/>
        <s:hidden id="annoImpegnoImpegnoDaPassare" name="annoImpegnoImpegnoDaPassare"/>
        <s:hidden id="numeroImpegnoDaPassare" name="numeroImpegnoDaPassare"/>
        

        <s:set var="selezionaProvvedimentoAction" value="%{'elencoImpegniROR_selezionaProvvedimento'}" />
        <s:set var="clearRicercaProvvedimentoAction" value="%{'elencoImpegniROR_clearRicercaProvvedimento'}" />	          
        <s:set var="ricercaProvvedimentoAction" value="%{'elencoImpegniROR_ricercaProvvedimento'}" />	          
        <s:set var="eliminaAction" value="%{'elencoImpegniROR_elimina'}" />	  
        <s:set var="selezionaProvvedimentoInseritoAction" value="%{'elencoImpegniROR_selezionaProvvedimentoInserito'}" />	
		<s:set var="inserisciProvvedimentoAction" value="%{'elencoImpegniROR_inserisciProvvedimento'}" />	
	     
	    <s:set var="annullaImpegnoAction" value="%{'elencoImpegniROR_annullaImpegno'}" />	
	    
	    <s:set var="gestisciForwardAction" value="%{'elencoImpegniROR_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'elencoImpegniROR_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'elencoImpegniROR_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'elencoImpegniROR_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'elencoImpegniROR_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'elencoImpegniROR_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'elencoImpegniROR_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'elencoImpegniROR_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'elencoImpegniROR_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'elencoImpegniROR_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'elencoImpegniROR_salvaConByPassDodicesimi'}" />	          
		
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
		$(".linkAnnullaImpegno").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#uidImpegnoDaAnnullare").val(supportId[1]);
				$("#numeroImpegnoDaAnnullare").val(supportId[2]);
				$("#numeroImpegnoDaPassare").val(supportId[3]);
				$("#annoImpegnoImpegnoDaPassare").val(supportId[4]);
				$("#annoImpegnoImpegnoDaAnnullare").val(supportId[5]);
				
				
			}
		});
		
	});
</script>