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
   <s:form id="mainForm" method="post" action="elencoSoggettiCec.do">
   
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
                 requestURI="elencoSoggettiCec.do"
				 uid="ricercaSoggettoID" keepStatus="true" clearStatus="${status}" >
	        <display:column>
	       		<s:radio list="%{#attr.ricercaSoggettoID.codiceSoggetto}" name="radioCodiceSoggetto" theme="displaytag"></s:radio>
	        </display:column>
	        <display:column title="Codice" property="codiceSoggettoNumber"  />
	        <display:column title="Codice Fiscale" property="codiceFiscale" />
	        <display:column title="Partita IVA" property="partitaIva"/>
	        <display:column title="Denominazione" property="denominazione" />
	        <display:column title="Stato" property="statoOperativo" />
	        <display:column title="" class="tab_Right">
		    	<div class="btn-group">
			    	<s:url id="consultaUrl" action="consultaSoggettoCec.do">
			        	<s:param name="codiceSoggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	               

	                <s:url id="aggiornaUrl" action="aggiornaSoggettoCec.do">
			        	<s:param name="soggetto" value="%{#attr.ricercaSoggettoID.codiceSoggetto}" />
	                </s:url>
	                <s:url id="collegaUrl" action="collegaSoggettiCec.do">
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
                        <s:if test="isAbilitato(3, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
                         	<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>_<s:property value="@it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ElencoSoggettiAction$TipoOperazioneEnum@ANNULLA"/>_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgAnnulla" data-toggle="modal" class="linkAggiornamentoSoggetto">annulla</a></li>
                         </s:if>
                        <s:if test="isAbilitato(6, '#attr.ricercaSoggettoID.statoOperativo', '#attr.ricercaSoggettoID.loginControlloPerModifica')">
							<li><a id="linkElimina_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaSoggetto">elimina</a></li>                        	
                         </s:if>
               
			     	</ul>
			   </div>
		 </display:column>
	</display:table>
	
	<s:if test="isVisualizzaDettaglioVisibile()">
		</div>
		<p> 
			<s:submit name="visualizza_dettaglio" value="visualizza dettaglio" method="visualizzaDettaglio" cssClass="btn" />
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
	<s:submit id="eliminaBtn" name="btnEliminaSoggetto" value="si, prosegui" cssClass="btn btn-primary" method="gestioneEliminaSoggetto"/>
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
		<s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/>
	</div>
</div>  
 <!--/modale annulla -->
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
		<s:submit id="validaBtn" name="btnValidaStato" value="si, prosegui" cssClass="btn btn-primary" method="gestisciAggiornamentoStato"/>
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
		
		
		
		if($("#ancoraVisualizza").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancoretta');
		}

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