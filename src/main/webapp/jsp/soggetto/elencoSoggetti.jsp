<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />	


</head>

<s:include value="/jsp/include/header.jsp" />

  <body>
  
 
<div class="container-fluid-banner">



<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<!--   TABELLE       RIEPILOGO    -->
 
<div class="container-fluid">
<div class="row-fluid">
<div class="span12 ">

<div class="contentPage">  
   <%-- SIAC-7952 rimuovo .do dalla action --%>
   <s:form id="mainForm" method="post" action="elencoSoggetti">
   
   <s:if test="hasActionErrors()">
		<%-- Messaggio di ERROR --%>
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			    <s:actionerror/>
				
			</ul>
		</div>
   </s:if>
   <s:if test="hasActionMessages()">
		<%-- Messaggio di WARNING --%>
		<div class="alert alert-success">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<ul>
				<s:actionmessage/>
			</ul>
		</div>
   </s:if>
   
<s:if test="hasActionWarnings()">
	<%-- Messaggio di WARNING --%>
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		   <s:iterator value="actionWarnings">
		       <s:property  escapeHtml="false" /><br>
		   </s:iterator>
			
		</ul>
	</div>
</s:if>   
   
   <h3>Risultati di ricerca Soggetto</h3>
   <p>Seleziona un soggetto per visualizzare le sedi e le modalit&agrave; di pagamento collegate</p>  
   <display:table name="sessionScope.RISULTATI_RICERCA_SOGGETTI"  
                 class="table tab_left table-hover" 
                 summary="riepilogo soggetti"
                 pagesize="10"
                 partialList="true" size="resultSize"
                 requestURI="elencoSoggetti.do"
				 uid="ricercaSoggettoID" keepStatus="true" clearStatus="${status}" >
	        <display:column>
	       		<s:radio list="%{#attr.ricercaSoggettoID.codiceSoggetto}" name="radioCodiceSoggetto" theme="displaytag"></s:radio>
	        </display:column>
	        <display:column title="Codice" property="codiceSoggettoNumber"  />
	        <display:column title="Codice Fiscale" property="codiceFiscale" />
	        <display:column title="Partita IVA" property="partitaIva"/>
	        <display:column title="Denominazione" property="denominazione" />
	        <display:column title="Stato" >
	        <s:property value="%{#attr.ricercaSoggettoID.statoOperativo}"/>
	        <s:if test="%{#attr.ricercaSoggettoID.statoOperativo.name().equals('SOSPESO')}">
	        	<a href="#" data-toggle="tooltip" title="Motivo sospensione: <s:property value="%{#attr.ricercaSoggettoID.notaStatoOperativo}"/>" data-container="body"><i class="icon-info-sign"></i></a>
	        </s:if>
			<!-- SIAC-7114 aggiungo l'info anche in caso di soggetto bloccato -->
	        <s:if test="%{#attr.ricercaSoggettoID.statoOperativo.name().equals('BLOCCATO')}">
	        	<a href="#" data-toggle="tooltip" title="Motivo blocco: <s:property value="%{#attr.ricercaSoggettoID.notaStatoOperativo}"/>" data-container="body"><i class="icon-info-sign"></i></a>
	        </s:if>
	        </display:column>
	        <display:column title="" class="tab_Right">
		    	<div class="btn-group">
			    	<%--task-131 <s:url id="consultaUrl" action="consultaSoggetto.do"> --%>
			    	<s:url var="consultaUrl" action="consultaSoggetto">
			        	<s:param name="codiceSoggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	                <%--task-131 <s:url var="validaUrl" action="validaSoggetto.do"> --%>
	                <s:url var="validaUrl" action="validaSoggetto">
			        	<s:param name="soggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	                <%--task-131 <s:url var="aggiornaUrl" action="aggiornaSoggetto.do"> --%>
	                <s:url var="aggiornaUrl" action="aggiornaSoggetto">
			        	<s:param name="soggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	                <%--task-131 <s:url var="collegaUrl" action="collegaSoggetti.do"> --%>
	                <s:url var="collegaUrl" action="collegaSoggetti">
			        	<s:param name="soggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	                <button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
			     	<ul class="dropdown-menu pull-right" id="ul_action">
		     			 <s:if test="isAbilitato(1, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
		     			     <li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
		     			 </s:if>
                         <s:if test="isAbilitato(2, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
                         	<li><a href="<s:property value="%{aggiornaUrl}"/>">aggiorna</a></li>
                         </s:if>
<%--                          <li><a href="<s:property value="%{aggiornaUrl}"/>">aggiorna</a></li> --%>
                         <s:if test="isAbilitato(3, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
                         	<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>_<s:property value="@it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ElencoSoggettiAction$TipoOperazioneEnum@ANNULLA"/>_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgAnnulla" data-toggle="modal" class="linkAggiornamentoSoggetto">annulla</a></li>
                         </s:if>
                         <s:if test="isAbilitato(4, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
                         	<li><a data-nota-stato-sospeso-precedente="<s:property value="%{#attr.ricercaSoggettoID.notaStatoSospesoPrecedente}"/>" id="linkAnnulla_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>_<s:property value="@it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ElencoSoggettiAction$TipoOperazioneEnum@SOSPENDI"/>_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgSospendi" data-toggle="modal" class="linkAggiornamentoSoggetto linkSospendiSoggetto">sospendi</a></li>
                         </s:if>
						 <!-- SIAC-7114 -->
                         <s:if test="isAbilitato(5, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
							<li><a data-nota-stato-blocco-precedente="<s:property value="%{#attr.ricercaSoggettoID.notaStatoBloccoPrecedente}"/>" id="linkBlocca_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>_<s:property value="@it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ElencoSoggettiAction$TipoOperazioneEnum@BLOCCA"/>_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgBlocca" data-toggle="modal" class="linkAggiornamentoSoggetto linkBloccaSoggetto">blocca</a></li>                     	
                         </s:if>
                         <s:if test="isAbilitato(6, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
							<li><a id="linkElimina_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaSoggetto">elimina</a></li>                        	
                         </s:if>
                         <s:if test="isAbilitato(7, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
                        	<s:if test="isSoggettoSospesoBloccato(#attr.ricercaSoggettoID.statoOperativo)">
								<li><a id="linkValida_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgValida" data-toggle="modal" class="linkValidaSoggetto">valida</a></li>                        		
                        	</s:if>
                        	<s:else>
                        		<li><a href="<s:property value="%{validaUrl}"/>">valida</a></li>
                        	</s:else>
                         </s:if>
                         <s:if test="isAbilitato(8, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginModifica')">
                        	<li><a href="<s:property value="%{collegaUrl}"/>">collega</a></li>
                         </s:if>
			     	</ul>
			   </div>
		 </display:column>
	</display:table>


	<s:if test="isVisualizzaDettaglioVisibile()">
	</div>

		<p> 
			<!--task-131 <s:submit name="visualizza_dettaglio" value="visualizza dettaglio" method="visualizzaDettaglio" cssClass="btn" /> -->
			<s:submit name="visualizza_dettaglio" value="visualizza dettaglio" action="elencoSoggetti_visualizzaDettaglio" cssClass="btn" />
		</p>
		
		
	</s:if>
	
	
	<div id="risultatoDettaglioSoggetto">	
	 <a id="ancoretta"></a>
	 <s:include value="/jsp/soggetto/include/risultatoDettaglioSoggetto.jsp" />
    </div> 
    
    <div class="Border_line"></div>  
   	<p>    
    	<s:include value="/jsp/include/indietro.jsp" />  
    </p> 
    <s:hidden id="ancoraVisualizza" name="ancoraVisualizza"/>
	<s:hidden id="codiceSoggettoDaAggiornare" name="codiceSoggetto"/>
	<s:hidden id="tipoOperazioneDaEseguire" name="tipoOperazione"/>
	<s:hidden id="idSoggettoDaAggiornare" name="idSoggetto"/>
 <!-- Modal -->
<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<p><strong>Attenzione!</strong></p>
	<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<!-- task-131 <s:submit id="eliminaBtn" name="btnEliminaSoggetto" value="si, prosegui" cssClass="btn btn-primary" method="gestioneEliminaSoggetto"/> -->
	<s:submit id="eliminaBtn" name="btnEliminaSoggetto" value="si, prosegui" cssClass="btn btn-primary" action="elencoSoggetti_gestioneEliminaSoggetto"/>
</div>
</div>  
<!--/modale elimina -->
<!-- Modal Annulla-->
<div id="msgAnnulla" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgAnnullaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Questa operazione cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<!-- task-131 <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/> -->
		<s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="elencoSoggetti_gestisciAggiornamentoStato"/>
		
	</div>
</div>  
 <!--/modale annulla -->
 <!-- Modal Blocca-->
<!-- SIAC-7114 -->
<div id="msgBlocca" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgAnnullaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Questa operazione cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
			<!-- SIAC-7114 -->
    		<div class="control-group">
	       	   Motivo del blocco &nbsp; &nbsp;
           		<s:textfield maxlength="1000" name="dettaglioSoggetto.notaStatoOperativo" value="" id="motivoBlocco" cssClass="span9" />
	       	</div>
			<!-- SIAC-7114 -->
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<!-- task-131 <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/> -->
		<s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="elencoSoggetti_gestisciAggiornamentoStato"/>
	</div>
</div>  
 <!--/modale Blocca -->
<!-- Modal Sospendi-->
<div id="msgSospendi" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgAnnullaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Questa operazione cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		    <div class="control-group">
	       	   Motivo sospensione &nbsp; &nbsp;
           		<s:textfield maxlength="1000" name="dettaglioSoggetto.notaStatoOperativo" value="" id="motivoSospensione" cssClass="span9" />
	       	</div>
    	</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<!-- task-131 <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/> -->
		<s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="elencoSoggetti_gestisciAggiornamentoStato"/>
	</div>
</div>  
 <!--/modale sospendi -->
 <!-- Modal Valida-->
<div id="msgValida" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgValidaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Stai per validare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<!-- task-131 <s:submit id="validaBtn" name="btnValidaStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/> -->
		<s:submit id="validaBtn" name="btnValidaStato" value="si, prosegui" cssClass="btn btn-primary" action="elencoSoggetti_gestisciAggiornamentoStato"/>
	</div>
</div>  
 <!--/modale valida -->
  <p class="marginLarge"> 
<%--   	   <s:include value="/jsp/include/indietro.jsp" /> --%>
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
		$(".linkAggiornamentoSoggetto").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#codiceSoggettoDaAggiornare").val(supportId[1]);
				$("#tipoOperazioneDaEseguire").val(supportId[2]);
				$("#idSoggettoDaAggiornare").val(supportId[3]);
			}
		});
		$(".linkEliminaSoggetto").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#idSoggettoDaAggiornare").val(supportId[1]);
			}
		});
		$(".linkValidaSoggetto").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#codiceSoggettoDaAggiornare").val(supportId[1]);
				$("#idSoggettoDaAggiornare").val(supportId[2]);
				$("#tipoOperazioneDaEseguire").val(null);
			}
		});

		$(".linkSospendiSoggetto").click(function() {
			$("#motivoSospensione").val($(this).data("nota-stato-sospeso-precedente"));
		});

		$(".linkBloccaSoggetto").click(function() {
			$("#motivoBlocco").val($(this).data("nota-stato-blocco-precedente"));
		});

		
		
		
		if($("#ancoraVisualizza").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancoretta');
		}


		$('*[data-toggle="tooltip"]').tooltip();
	    

		
	/* 	$("#visualizzaDettaglioSoggetto").click(function() {
			$.ajax({
				url: '<s:url method="visualizzaDettaglio"/>',
				type: 'POST',				
			    success: function(data)  {
				    $("#risultatoDettaglioSoggetto").html(data);
				}
			});
		});	
		 */
		
	});
</script>