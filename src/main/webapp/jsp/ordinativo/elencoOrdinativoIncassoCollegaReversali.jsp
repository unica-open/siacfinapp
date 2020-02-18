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



<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 ">
			<div class="contentPage"> 
			
				<s:form id="mainForm" method="post" action="elencoOrdinativoIncassoCollegaReversali.do">  
				<s:hidden name="uidOrdCollegaReversali" />
				<s:hidden id="uidOrdIncassoSelezionati" name="uidOrdIncassoSelezionati" />
				<s:hidden id="totImportoSelezionati" name="totImportoSelezionati" />
				<s:hidden id="uidOrdIncassoSoggettoDiff" name="uidOrdIncassoSoggettoDiff" />

					<%-- Messaggio di ERROR --%>
					<div class="alert alert-error hide">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
					</div>

					<%-- Messaggio di INFO --%>
					<div class="alert alert-success hide">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<%-- Rimosso Attenzione!! per JIRA SIAC-5248 --%>
						<strong></strong><br>
						<ul>
						</ul>
					</div>

					<%-- Messaggio di WARNING --%>
					<div class="alert alert-warning hide">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
					</div>
				
				<h3 id="ordinativoPagamento" data-codice-soggetto="<s:property value="ordinativoPagamento.soggetto.codiceSoggetto"/>">Ordinativo <s:property value="ordinativoPagamento.numero" /> 
					del <s:property value="%{ordinativoPagamento.dataEmissione}"/> - 
					Stato <s:property value="ordinativoPagamento.statoOperativoOrdinativo"/> 
					dal <s:property value="%{ordinativoPagamento.dataInizioValidita}"/></h3>
									
							
					
					<label class="radio inline">Importo ordinativo di spesa</label>
					<label class="radio inline"><span id="importoOrdinativo"><s:property value="getText('struts.money.format', {ordinativoPagamento.importoOrdinativo})"/></span></label>
					<label class="radio inline">Numero ordinativi di incasso selezionati</label>
					<label class="radio inline"><span id="numSelezionati">0</span></label>
					<label class="radio inline">Totale importo ordinativi di incasso selezionati</label>
					<label class="radio inline"><span id="totImportoSelezionatiStr">0,00</span></label>
					
					
					<h3>Risultati di ricerca degli Ordinativi di incasso</h3>
					
		<display:table name="elencoOrdinativoIncasso" 
			class="table table-hover tab_left" 
							 keepStatus="true"
							 clearStatus="false"
			                 summary="riepilogo ordinativi incasso"
			                 pagesize="10"
			                 partialList="true" size="resultSize"
			                 requestURI="elencoOrdinativoIncassoCollegaReversali.do"
							 uid="ricercaOrdinativoIncassoID">
			
		<display:column headerClass="sel-all" title="">
			<input class="sel-ord" type="checkbox" 
				value="<s:property value="%{#attr.ricercaOrdinativoIncassoID.uid}" />"
				data-codice-soggetto="<s:property value="%{#attr.ricercaOrdinativoIncassoID.soggetto.codiceSoggetto}" />"/>
		</display:column>	
		<display:column title="Numero" property="numero" />	
		<display:column title="Descrizione" property="descrizione" />	 
	 	<display:column title="Data emissione">
			<s:property value="%{#attr.ricercaOrdinativoIncassoID.dataEmissione}" /></display:column>	 			 					
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
		<display:column class="importo" title="Importo" property="importoOrdinativo"  
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>


 	     <display:footer>
			<tr> 
				<th>Totale</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th class="tab_Right"><s:property value="getText('struts.money.format', {totImporti})" /></th>
			</tr>  
	  </display:footer>
		 
</display:table>
         
        <s:hidden id="annoOrdinativoIncassoAnnullato" name="annoOrdinativoIncassoAnnullato"/>
        <s:hidden id="numeroOrdinativoIncassoAnnullato" name="numeroOrdinativoIncassoAnnullato"/>
        
        <p class="marginLarge"> 
  	   		<s:include value="/jsp/include/indietro.jsp" />
  	   		
		 <s:submit cssClass="btn btn-primary pull-right" method="collegaReversali" value="collega reversali" id="collegaReversali"/> 
  		</p>  	                                 
      </s:form>
      </div>	
    </div>	
  </div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />
<script type="text/javascript" src="${jspath}ordinativo/elencoOrdinativoIncassoCollegaReversali.js"></script>

	