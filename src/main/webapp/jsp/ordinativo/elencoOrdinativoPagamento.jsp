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




<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 ">
			<div class="contentPage"> 
			
			<!--  -->
				<s:form id="mainForm" method="post" action="elencoOrdinativoPagamento.do">  

				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<h3>Elenco ordinativi pagamento</h3>		
				<s:if test="resultSize!=0">
					<h4><span class="num_result"><s:property value="resultSize"/> </span> Risultati trovati</h4>
				</s:if>
					
					<h3>Risultati di ricerca degli Ordinativi di Pagamento</h3>

		<display:table name="elencoOrdinativoPagamento" 
			class="table table-hover tab_left" 
			                 summary="riepilogo ordinativi pagamento"
							 keepStatus="true" clearStatus="false"
			                 pagesize="10"
			                 partialList="true" size="resultSize"
			                 requestURI="elencoOrdinativoPagamento.do"
							 uid="ricercaOrdinativoPagamentoID">
			
		<display:column title="Numero" property="numero" />	
		<%-- <display:column title="Creditore">	
			<s:property value="%{#attr.ricercaOrdinativoPagamentoID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaOrdinativoPagamentoID.soggetto.denominazione}" /> 
		</display:column> --%> 
		<display:column title="Descrizione" property="descrizione" />	 
		
		
		<display:column title="Data emissione">

	 		<s:property value="%{#attr.ricercaOrdinativoPagamentoID.dataEmissione}" /> 
		 </display:column>	 			 					
		<display:column title="Creditore">	
			<s:property value="%{#attr.ricercaOrdinativoPagamentoID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaOrdinativoPagamentoID.soggetto.denominazione}" /> 
		</display:column>
		
		
 		<display:column title="Capitolo" >
			<s:property value="%{#attr.ricercaOrdinativoPagamentoID.capitoloUscitaGestione.annoCapitolo}"/> / <s:property value="%{#attr.ricercaOrdinativoPagamentoID.capitoloUscitaGestione.numeroCapitolo}"/> / <s:property value="%{#attr.ricercaOrdinativoPagamentoID.capitoloUscitaGestione.numeroArticolo}"/> / <s:property value="%{#attr.ricercaOrdinativoPagamentoID.capitoloUscitaGestione.numeroUEB}"/> 
		</display:column>
		<display:column title="Stato">
			<a href="#" data-trigger="hover" rel="popover" title="Stato Operativo" data-content="<s:property value="%{#attr.ricercaOrdinativoPagamentoID.statoOperativoOrdinativo}"/>">
				<s:property value="%{#attr.ricercaOrdinativoPagamentoID.codStatoOperativoOrdinativo}"/>
			</a>
			<s:if test="%{! #attr.ricercaOrdinativoPagamentoID.daTrasmettere}">
				<a href="#" data-trigger="hover" rel="popover" title="" data-content="Non trasmettere">*</a>
			</s:if>
		</display:column>
		<display:column title="Provvedimento" >
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaOrdinativoPagamentoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaOrdinativoPagamentoID.attoAmministrativo.anno}"/> / 
				<s:property value="%{#attr.ricercaOrdinativoPagamentoID.attoAmministrativo.numero}"/>/
				<s:property value="%{#attr.ricercaOrdinativoPagamentoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaOrdinativoPagamentoID.attoAmministrativo.tipoAtto.codice}"/>
				
			</a>
		</display:column>
		<display:column title="Importo" property="importoOrdinativo"  
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>



		<display:column title="Azioni Ordinativo Pagamento">
		
				<%-- 
				Rm 29-09-2015 commento la definizione di questa url perche' non e' utilizzata
				<s:url id="aggiornaLiquidazioneUrl" action="inserisciLiquidazioneStep3Aggiorna.do" escapeAmp="false">
					<s:param name="anno" value="%{#attr.ricercaLiquidazioniID.annoLiquidazione}" />
					<s:param name="numero" value="%{#attr.ricercaLiquidazioniID.numeroLiquidazione}" />
			   	</s:url> 
			   	
			   	--%>
			   	
		    	<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right">
                  	 	<s:url id="consultaUrl" action="consultaOrdinativoPagamento.do" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaOrdinativoPagamentoID.anno}" />
			        		<s:param name="numero" value="%{#attr.ricercaOrdinativoPagamentoID.numero}" />			        				        	
	                    </s:url>
	                    
		    			<s:url id="aggiornaUrl" action="gestioneOrdinativoPagamentoStep1.do" escapeAmp="false">
			        		<s:param name="annoOrdinativo" value="%{#attr.ricercaOrdinativoPagamentoID.anno}" />
			        		<s:param name="numeroOrdinativo" value="%{#attr.ricercaOrdinativoPagamentoID.numero}" />
	                    </s:url>
		                    
						<s:if test="isAbilitatoAggiorna(#attr.ricercaOrdinativoPagamentoID.codStatoOperativoOrdinativo)">
							<li><a href="<s:property value="aggiornaUrl"/>" class="aggiornaButton">aggiorna</a></li>
						</s:if>
	                    
	               		<li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
	               		
						<s:if test="isAbilitatoAnnulla(#attr.ricercaOrdinativoPagamentoID.codStatoOperativoOrdinativo)">
							<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaOrdinativoPagamentoID.anno}"/>_<s:property value="%{#attr.ricercaOrdinativoPagamentoID.numero}"/>_<s:property value="%{#attr.ricercaOrdinativoPagamentoID.numero}"/>" href="#msgAnnullaOrdinativoPagamento" data-toggle="modal" class="linkAnnullaOrdinativoPagamento">annulla</a></li>
						</s:if>

						<s:if test="#attr.ricercaOrdinativoPagamentoID.canAllegareReversali">
	                  	 	<s:url id="collegaReversaliUrl" action="ricercaOrdinativoIncasso.do" escapeAmp="false">
				        		<s:param name="uidOrdCollegaReversali" value="%{#attr.ricercaOrdinativoPagamentoID.uid}" />
		                    </s:url>
						
							<li><a href="<s:property value="%{collegaReversaliUrl}"/>">collega reversali</a></li>
						</s:if>

		    		</ul>
				</div>
		 </display:column>
		 
	   <display:footer>
			<tr> 
				<th>Totale</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th class="tab_Right"><s:property value="getText('struts.money.format', {totImporti})" /></th>
				<th>&nbsp;</th>
			</tr>  
	  </display:footer>
		 
	</display:table>
        
        <s:hidden id="annoOrdinativoPagamentoAnnullato" name="annoOrdinativoPagamentoAnnullato"/>
        <s:hidden id="numeroOrdinativoPagamentoAnnullato" name="numeroOrdinativoPagamentoAnnullato"/>
        
			<!-- Modal  annulla OrdinativoPagamento -->
            <div id="msgAnnullaOrdinativoPagamento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="modal">&times;</button>                  
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato: <s:textfield id="ordinativoPagamentoDaAnnullare" name="ordinativoPagamentoDaAnnullare" disabled="true"/></strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                
                <button id="submitBtn" class="btn btn-primary" formmethod="post" type="submit" formaction="annullaOrdinativoPagamento.do">si, prosegui</button>
                <%-- <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="annullaOrdinativoPagamento" /> --%>
              </div>
            </div>  
            <!--/modale annulla OrdinativoPagamento -->
        
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
		
		$("#submitBtn").click(function() {
            // update the block message
            /* $.blockUI({ message: "<h1>Remote call in progress...</h1>" }); */
            
			
	            $("#msgAnnullaOrdinativoPagamento").block({ message: null }); 
	         
        });
		
		
		var mainForm = $("#mainForm");
		$(".linkAnnullaOrdinativoPagamento").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#annoOrdinativoPagamentoAnnullato").val(supportId[1]);
				$("#numeroOrdinativoPagamentoAnnullato").val(supportId[2]);
				$("#ordinativoPagamentoDaAnnullare").val(supportId[3]);
			}
		});

	
	});
	
</script>

 
