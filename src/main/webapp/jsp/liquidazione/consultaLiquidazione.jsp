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
					<s:form id="mainForm" method="post" action="consultaLiquidazione">
						<h3>Consultazione liquidazione ${model.liquidazioneConsulta.annoLiquidazione}/${model.liquidazioneConsulta.numeroLiquidazione} <s:if test="%{model.liquidazioneConsulta.descrizioneLiquidazione!=''}">- Descrizione ${model.liquidazioneConsulta.descrizioneLiquidazione}</s:if></h3>
								
						<s:include value="/jsp/include/actionMessagesErrors.jsp" />
						
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Liquidazione</p>
								<ul class="htmlelt">
									<li>
										<dfn>Anno</dfn> 
										<dl>${model.liquidazioneConsulta.annoLiquidazione}</dl>
									</li>
									<li>
										<dfn>Numero</dfn> 
										<dl>${model.liquidazioneConsulta.numeroLiquidazione}</dl>
									</li>
									<li>
										<dfn>Descrizione </dfn> 
										<dl>${model.liquidazioneConsulta.descrizioneLiquidazione}</dl>
									</li>
									<li>
										<dfn>Importo</dfn> 
										<dl><s:property value="getText('struts.money.format', {importoLiquidazioneBigDecimal})"/></dl>
									</li>
									<li>
										<dfn>Disponibilit&agrave; a pagare</dfn> 
										<dl><s:property value="getText('struts.money.format', {disponibilita})"/></dl>
									</li>
									<li>
										<dfn>Transazione Elementare</dfn> 
										<dl>${model.transazioneElementare}</dl>
									</li>
									
									<li>
										<dfn>Tipo debito siope</dfn>
										<dl><s:property value="tipoDebitoSiope" />&nbsp;</dl>
									</li>
									
									<s:if test="%{liquidazioneConsulta.cig != null && liquidazioneConsulta.cig != ''}">
										 <li>
											<dfn>CIG</dfn>
											<dl><s:property value="liquidazioneConsulta.cig" />&nbsp;</dl>
										</li>
									</s:if>
								    <s:else>
								    	<li>
										<dfn>Motivazione assenza CIG</dfn>
										<dl><s:property value="motivazioneAssenzaCig" />&nbsp;</dl>
										</li>
								    </s:else>
									
									<li>
										<dfn>CUP</dfn> 
										<dl>${model.liquidazioneConsulta.cup}</dl>
									</li>
																		
									
									<li>
										<dfn>Stato</dfn> 
										<dl>${model.liquidazioneConsulta.statoOperativoLiquidazione}</dl>
									</li>
									<li>
										<dfn>Data inserimento</dfn> 
										<dl><s:property value="%{liquidazioneConsulta.dataInizioValidita}" /></dl>
									</li>
									<li>
										<dfn>Data aggiornamento</dfn> 
										<dl><s:property value="%{liquidazioneConsulta.dataModifica}" /></dl>
									</li>
									<li>
										<dfn>Data annullamento</dfn> 
										<dl><s:property value="%{liquidazioneConsulta.dataAnnullamento}" /></dl>
									</li>
								</ul>					
							</div>
							<div class="boxOrInRight">
								<p>Quota documento</p>
								<ul class="htmlelt">
									<li>
										<dfn>Anno</dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.documento.anno} </dl>
									</li>
									<li>
										<dfn>Numero</dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.documento.numero}</dl>
									</li>
									<li>
										<dfn>Tipo </dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.documento.tipoDocumento.descrizione}</dl>
									</li>
									<li>
										<dfn>Quota </dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.numero}</dl>
									</li>
									<li>
										<dfn>Creditore documento</dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.documento.soggetto.codiceSoggetto} - ${model.liquidazioneConsulta.subdocumentoSpesa.documento.soggetto.denominazione}</dl>
									</li>
									<li>
										<dfn>Modalit&agrave; di pagamento</dfn> 
										<dl>${model.liquidazioneConsulta.subdocumentoSpesa.modalitaPagamentoSoggetto.descrizioneInfo.descrizioneArricchita}</dl>
									</li>
						
								</ul>					
							</div>
							
							
						</div>
						<div class="clear"></div>
						
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
									<p>Capitolo</p>
								
								<ul class="htmlelt">
									<li>
										<dfn>Capitolo</dfn> 
										<dl>${model.capitoloConsulta.numeroCapitolo}</dl>
									</li>
									<li>
										<dfn>Articolo</dfn> 
										<dl>${model.capitoloConsulta.numeroArticolo}</dl>
									</li>
									<li>
										<dfn>UEB</dfn> 
										<dl>${model.capitoloConsulta.numeroUEB}</dl>
									</li>
									<li>
										<dfn>Descrizione</dfn> 
										<dl>${model.capitoloConsulta.descrizione}</dl>
									</li>
									 
									<li>
										<dfn>Struttura</dfn> 
										<dl>${model.capitoloConsulta.strutturaAmministrativoContabile.codice} ${model.capitoloConsulta.strutturaAmministrativoContabile.descrizione}</dl>
									</li>
									<li>
										<dfn>Tipo fin.</dfn> 
										<dl>${model.capitoloConsulta.tipoFinanziamento.codice} ${model.capitoloConsulta.tipoFinanziamento.descrizione}</dl>
									</li>
								</ul>
											
							</div>
							
							<div class="boxOrInRight">
								<p>Impegno</p>
								<ul class="htmlelt">
									<li>
										<dfn>Anno</dfn> 
										<dl>${model.impegnoConsulta.annoMovimento}</dl>
									</li>
									<li>
										<dfn>Numero</dfn> 
										<dl>${model.impegnoConsulta.numero}</dl>
									</li>
									<li>
										<dfn>SubImpegno</dfn> 
										<dl>${model.subImpegnoConsulta.numero}</dl>
									</li>
									<li>
										<dfn>Descrizione </dfn> 
										<dl>${model.impegnoConsulta.descrizione}</dl>
									</li>
									
								</ul>	
								
							</div>				
						
						</div>
						
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Provvedimento</p>
								
								<ul class="htmlelt">
									<li>
										<dfn>Tipo</dfn> 
										<dl>${model.attoAmministrativoConsulta.tipoAtto.codice}</dl>
									</li>
									<li>
										<dfn>Anno</dfn> 
										<dl>${model.attoAmministrativoConsulta.anno}</dl>
									</li>
									<li>
										<dfn>Numero</dfn> 
										<dl>${model.attoAmministrativoConsulta.numero}</dl>
									</li>
									<li>
										<dfn>Struttura</dfn> 
										<dl>${model.attoAmministrativoConsulta.strutturaAmmContabile.codice} - ${model.attoAmministrativoConsulta.strutturaAmmContabile.descrizione}</dl>
									</li>
									<li>
										<dfn>Oggetto</dfn> 
										<dl>${model.attoAmministrativoConsulta.oggetto}</dl>
									</li>
									<li>
										<dfn>Stato</dfn> 
										<dl>${model.attoAmministrativoConsulta.statoOperativo}</dl>
									</li>
									
								</ul>

												
							</div>
							
							<div class="boxOrInRight">
							<p>Soggetto</p>
								<ul class="htmlelt">
									<li>
										<dfn>Codice</dfn> 
										<dl>${model.soggettoConsulta.codiceSoggetto}</dl>
									</li>
									<li>
										<dfn>Codice Fiscale</dfn> 
										<dl>${model.soggettoConsulta.codiceFiscale}</dl>
									</li>
									<li>
										<dfn>Partita IVA</dfn> 
										<dl>${model.soggettoConsulta.partitaIva}</dl>
									</li>
									<li>
										<dfn>Ragione Sociale</dfn> 
										<dl>${model.soggettoConsulta.denominazione}</dl>
									</li>
									<li>
										<dfn>Modalit&agrave; di pagamento</dfn> 
										<%-- <dl>${mdpConsulta.modalitaAccreditoSoggetto.descrizione}</dl> --%>
										<dl>${mdpConsulta.descrizioneInfo.descrizioneArricchita}</dl>
									</li>
								</ul>	
							</div>				
						
						</div>
						
				 <!-- <p class="margin-medium"> <a href="javascript:history.go(-1)" class="btn">indietro</a>   </p> --> 
				 <s:include value="/jsp/include/indietro.jsp" /> 	                                 
				</s:form>
			</div>	
		</div>	
	</div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />
