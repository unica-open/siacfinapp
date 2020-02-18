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
			
				<s:form id="mainForm" method="post" action="elencoOrdinativoIncasso.do">  

				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<h3>Elenco ordinativi incasso</h3>		
				<s:if test="resultSize!=0">
					<h4><span class="num_result"><s:property value="resultSize"/> </span> Risultati trovati</h4>
				</s:if>
					
					<h3>Risultati di ricerca degli Ordinativi di incasso</h3>
					
		<display:table name="elencoOrdinativoIncasso" 
			class="table table-hover tab_left" 
							 keepStatus="true"
							 clearStatus="false"
			                 summary="riepilogo ordinativi incasso"
			                 pagesize="10"
			                 partialList="true" size="resultSize"
			                 requestURI="elencoOrdinativoIncasso.do"
							 uid="ricercaOrdinativoIncassoID">
			
		<display:column title="Numero" property="numero" />	
		<display:column title="Descrizione" property="descrizione" />	 
	 		<display:column title="Data emissione">
 		 		<s:property value="%{#attr.ricercaOrdinativoIncassoID.dataEmissione}" /> 
		 	</display:column>	 			 					
		<display:column title="Debitore">	
			<s:property value="%{#attr.ricercaOrdinativoIncassoID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaOrdinativoIncassoID.soggetto.denominazione}" /> 
		</display:column>
		<display:column title="Provvedimento" >
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaOrdinativoIncassoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaOrdinativoIncassoID.attoAmministrativo.anno}"/> / 
				<s:property value="%{#attr.ricercaOrdinativoIncassoID.attoAmministrativo.numero}"/> / 
				<s:property value="%{#attr.ricercaOrdinativoIncassoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaOrdinativoIncassoID.attoAmministrativo.tipoAtto.codice}"/>
				
			</a>
		</display:column>
		<display:column title="Stato">
			<a href="#" data-trigger="hover" rel="popover" title="Stato Operativo" data-content="<s:property value="%{#attr.ricercaOrdinativoIncassoID.statoOperativoOrdinativo}"/>">
				<s:property value="%{#attr.ricercaOrdinativoIncassoID.codStatoOperativoOrdinativo}"/>
			</a>
			<s:if test="%{! #attr.ricercaOrdinativoIncassoID.daTrasmettere}">
				<a href="#" data-trigger="hover" rel="popover" title="" data-content="Non trasmettere">*</a>
			</s:if>
		</display:column>
 		<display:column title="Capitolo" >
			<s:property value="%{#attr.ricercaOrdinativoIncassoID.capitoloEntrataGestione.annoCapitolo}"/> / <s:property value="%{#attr.ricercaOrdinativoIncassoID.capitoloEntrataGestione.numeroCapitolo}"/> / <s:property value="%{#attr.ricercaOrdinativoIncassoID.capitoloEntrataGestione.numeroArticolo}"/> / <s:property value="%{#attr.ricercaOrdinativoIncassoID.capitoloEntrataGestione.numeroUEB}"/> 
		</display:column>
		<display:column title="Importo" property="importoOrdinativo"  
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>


		<display:column title="Azioni Ordinativo Incasso">
		    	<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right">
		    			<s:url id="consultaUrl" action="consultaOrdinativoIncasso.do" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaOrdinativoIncassoID.anno}" />
			        		<s:param name="numero" value="%{#attr.ricercaOrdinativoIncassoID.numero}" />			        				        	
	                    </s:url>
	                    
	                    <s:url id="aggiornaUrl" action="gestioneOrdinativoIncassoStep1.do" escapeAmp="false">
				        		<s:param name="annoOrdinativo" value="%{#attr.ricercaOrdinativoIncassoID.anno}" />
				        		<s:param name="numeroOrdinativo" value="%{#attr.ricercaOrdinativoIncassoID.numero}" />
		                </s:url>

						<s:if test="isAbilitatoAggiorna(#attr.ricercaOrdinativoIncassoID.codStatoOperativoOrdinativo)">
							<li><a href="<s:property value="aggiornaUrl"/>" class="aggiornaButton">aggiorna</a></li>
						</s:if>

	               		<li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>

						<s:if test="isAbilitatoAnnulla(#attr.ricercaOrdinativoIncassoID.codStatoOperativoOrdinativo)">
							<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaOrdinativoIncassoID.anno}"/>_<s:property value="%{#attr.ricercaOrdinativoIncassoID.numero}"/>_<s:property value="%{#attr.ricercaOrdinativoIncassoID.numero}"/>" href="#msgAnnullaOrdinativoIncasso" data-toggle="modal" class="linkAnnullaOrdinativoIncasso">annulla</a>
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
         
        <s:hidden id="annoOrdinativoIncassoAnnullato" name="annoOrdinativoIncassoAnnullato"/>
        <s:hidden id="numeroOrdinativoIncassoAnnullato" name="numeroOrdinativoIncassoAnnullato"/>
        
			<!-- Modal  annulla OrdinativoIncasso -->
            <div id="msgAnnullaOrdinativoIncasso" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="modal">&times;</button>                  
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato: <s:textfield id="ordinativoIncassoDaAnnullare" name="ordinativoIncassoDaAnnullare" disabled="true"/></strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <button id="submitBtn" class="btn btn-primary" formmethod="post" type="submit" formaction="annullaOrdinativoIncasso.do">si, prosegui</button>
                <%-- <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="annullaOrdinativoIncasso"/> --%>
              </div>
            </div>  
            <!--/modale annulla OrdinativoIncasso -->
        
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
            
	            $("#msgAnnullaOrdinativoIncasso").block({ message: null }); 
	         
        });
		
		
		$(".linkAnnullaOrdinativoIncasso").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#annoOrdinativoIncassoAnnullato").val(supportId[1]);
				$("#numeroOrdinativoIncassoAnnullato").val(supportId[2]);
				$("#ordinativoIncassoDaAnnullare").val(supportId[3]);
			}
		});
		
	});
	
</script>
	