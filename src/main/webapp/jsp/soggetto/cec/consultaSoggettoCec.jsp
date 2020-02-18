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

<div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    
        <s:form id="consultaSoggetto" action="consultaSoggetto.do" method="post">
        
        <h3 class="step-pane">Soggetto <s:property value="dettaglioSoggetto.codiceSoggetto"/> - <s:property value="dettaglioSoggetto.denominazione"/> </h3>
        
<!--         <form class="form-horizontal"> -->
         <ul class="nav nav-tabs">
    <li class="active">
    <a href="#soggetto" data-toggle="tab">Soggetto</a>
    </li>
    <li><a href="#modalita" data-toggle="tab">Modalit&agrave; pagamento</a></li>
    </ul>
    
    <div class="tab-content">
<div class="tab-pane active" id="soggetto">
	<h4>Data inserimento: <s:property value="%{dettaglioSoggetto.dataCreazione}"/> -   Data ultimo aggiornamento: <s:property value="%{dettaglioSoggetto.dataModifica}"/> -   Utente ultimo aggiornamento: <s:property value="dettaglioSoggetto.loginModifica"/> </h4>
	<h4>Stato: <s:property value="dettaglioSoggetto.statoOperativo"/></h4>
	
	 
	
<!--     <dl class="dl-horizontal"> -->
	<div class="boxOrSpan2">
	    <div class="boxOrInLeft">
	    	<p>Dati soggetto</p>
	        <ul class="htmlelt">
	        	
			   
			    <li>
			    	<dfn>Data stato</dfn>
			    	<dl><s:property value="%{dettaglioSoggetto.dataStato}"/>&nbsp;</dl>
			    </li>
			    <s:if test="isInModifica">
			        <li> 
				    	<dfn>In Modifica</dfn>
				    	<dl>SI</dl>
				    </li>
				    <li> 
				    	<dfn>Utente Modifica:</dfn>
				    	<dl><s:property value="utentePropostaModifica"/></dl>
				    </li>
				    <li>
				    	<dfn>Data Modifica</dfn>
				    	<dl><s:property value="%{dataPropostaModifica}"/></dl>
				    </li>
			    </s:if>
			    
			    <li>
			    <dfn>Codice</dfn>
			    <dl><s:property value="dettaglioSoggetto.codiceSoggetto"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Tipo soggetto</dfn>
			    <dl><s:property value="dettaglioSoggetto.tipoSoggetto.soggettoTipoDesc"/>&nbsp;</dl>
			    </li>
			    
			    <li>
			    <dfn>Natura giuridica</dfn>
			    <dl><s:property value="dettaglioSoggetto.naturaGiuridicaSoggetto.soggettoTipoDesc"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Codice fiscale</dfn>
			    <dl><s:property value="dettaglioSoggetto.codiceFiscale"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Residente estero</dfn>
			    <dl><s:property value="convertiBooleanToString(dettaglioSoggetto.residenteEstero)"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Codice fiscale estero</dfn>
			    <dl><s:property value="dettaglioSoggetto.codiceFiscaleEstero"/>&nbsp;</dl>
                </li>
                <li>			    
			    <dfn>Partita IVA</dfn>
			    <dl><s:property value="dettaglioSoggetto.partitaIva"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Denominazione</dfn>
			    <dl><s:property value="dettaglioSoggetto.denominazione"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Matricola</dfn>
			    <dl><s:property value="dettaglioSoggetto.matricola"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Data di nascita</dfn>
			    <dl><s:property value="%{dettaglioSoggetto.dataNascita}"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Stato/Provincia/Comune</dfn>
			    <dl><s:property value="dettaglioSoggetto.comuneNascita.nazioneDesc"/>/<s:property value="dettaglioSoggetto.comuneNascita.regioneDesc"/>/<s:property value="dettaglioSoggetto.comuneNascita.codiceBelfiore"/>&nbsp;</dl>
			    </li>
			    <li>
			    <dfn>Note</dfn>
			    <dl><s:property value="dettaglioSoggetto.note"/>&nbsp;</dl>
			   	</li>
			   	
			</ul>	
		</div>
		
		<div class="boxOrInRight">
		    <p>Recapiti</p>
		    <ul class="htmlelt">
			    <s:iterator value="dettaglioSoggetto.indirizzi" status="varStatusIndirizzo" var="currentIndirizzo">
			        <li>
				    <dfn>Indirizzo <s:if test="#varStatusIndirizzo.index != 0">${varStatusIndirizzo.index + 1}</s:if></dfn>
				    <dl><s:property value="indirizzoFormattato"/>&nbsp;</dl>
				    </li>
			    </s:iterator>
			    <s:iterator value="dettaglioSoggetto.contatti" status="varStatusContatto" var="currentContatto">
			        <li>
				    <dfn>Recapiti</dfn>
				    <dl><s:property value="contattoFormattato"/>&nbsp;</dl>
				    </li>
			    </s:iterator>
		    </ul>
	    </div>
<!--     </dl>        -->
     </div>
</div>



<div class="tab-pane" id="modalita">
  <h4>Modalit&agrave; pagamento</h4>
  <display:table name="modalitaPagamento" class="table tab_left table-hover margin-large" summary="riepilogo modalita pagamento" requestURI="elencoSoggetti.do" uid="sedeSupport">
        <display:column title="Codice" property="codiceModalitaPagamento" />
	    <display:column title="Modalit&agrave;">
	    	${attr.sedeSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.sedeSupport.modalitaAccreditoSoggetto.descrizione}
	    </display:column>
	    
		<display:column title="Descrizione">
								<s:push value="%{#attr.sedeSupport}" >
									<s:include value="/jsp/include/descrizioneCompletaModalitaPagamento.jsp" />
								</s:push>
		</display:column>
	    
	  	<display:column title="<abbr title='progressivo'>Associato a</abbr>" property="associatoA"/>
	  	<display:column title="Stato" property="descrizioneStatoModalitaPagamento" />
	  	
	  	
	  	
	  	
	  	 	<display:column title="" class="tab_Right" >
							    
		     <div class="btn-group">
		     
				<button class="btn dropdown-toggle" data-toggle="dropdown">
					Azioni <span class="caret"></span>
				</button>
				<ul class="dropdown-menu pull-right">
					<li><a data-toggle="modal" href="#consultaModPag_<s:property value="#attr.sedeSupport.uid" />" 
						class=""  >consulta</a></li>
				</ul>
			</div>
		 </display:column>	
	  	
	  	
	  	
  </display:table>
</div>

</div>       





<!-- Start Modal Window Consulta Mod Pagamento -->
							<s:iterator value="model.modalitaPagamento"
							var="pagamento">
							<div id="consultaModPag_<s:property value="#pagamento.uid" />"
								class="modal hide fade" tabindex="-1" role="dialog"
								aria-labelledby="consPag" aria-hidden="true">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">&times;</button>
									<h3>Modalit&agrave; di pagamento:
										${pagamento.modalitaAccreditoSoggetto.codice}
										&nbsp;-&nbsp;${pagamento.modalitaAccreditoSoggetto.descrizione}
									</h3>
								</div>

								<!-- START MDP NORMALE -->
								<s:if test="%{#pagamento.inModifica == false}">
									<div class="modal-body">
										<dl class="dl-horizontal">

											<s:if test="%{#pagamento.tipoAccredito != null}">
												<dt style="width: 180px;">Tipo Accredito</dt>
												<dd style="margin-left: 200px;">${pagamento.tipoAccredito}</dd>
											</s:if>

												<dt style="width: 180px;">Stato modalit&agrave;</dt>
												<dd style="margin-left: 200px;">VALIDO</dd>

											<s:if test="%{#pagamento.inModifica}">
												<!-- non entra nemmeno -->
												<dt style="width: 180px;">In Modifica</dt>
												<dd style="margin-left: 200px;">Si</dd>
												<s:if
													test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">
													<dt style="width: 180px;">Utente Modifica</dt>
													<dd style="margin-left: 200px;">${pagamento.loginModifica}</dd>
												</s:if>
												<s:if test="%{#pagamento.dataModifica != null}">
													<dt style="width: 180px;">Data Modifica</dt>
													<dd style="margin-left: 200px;">
														<s:date name="%{#pagamento.dataModifica}"
															format="dd/MM/yyyy" />
													</dd>
												</s:if>
												<s:if
													test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">
													<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>
													<dd style="margin-left: 200px;">${pagamento.loginUltimaModifica}</dd>
												</s:if>
												<s:if test="%{#pagamento.dataUltimaModifica != null}">
													<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>
													<dd style="margin-left: 200px;">
														<s:date name="%{#pagamento.dataUltimaModifica}"
															format="dd/MM/yyyy" />
													</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">
													<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>
													<dd style="margin-left: 200px;">${pagamento.loginUltimaModifica}</dd>
												</s:if>
												<s:else>
													<s:if
														test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">
														<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>
														<dd style="margin-left: 200px;">${pagamento.loginModifica}</dd>
													</s:if>
												</s:else>

												<s:if test="%{#pagamento.dataUltimaModifica != null}">
													<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>
													<dd style="margin-left: 200px;">
														<s:date name="%{#pagamento.dataUltimaModifica}"
															format="dd/MM/yyyy" />
													</dd>
												</s:if>
												<s:else>
													<s:if test="%{#pagamento.dataModifica != null}">
														<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>
														<dd style="margin-left: 200px;">
															<s:date name="%{#pagamento.dataModifica}"
																format="dd/MM/yyyy" />
														</dd>
													</s:if>
												</s:else>
											</s:else>

											<s:if
												test="%{#attr.dettaglioSoggetto.codiceSoggetto != null && #attr.dettaglioSoggetto.codiceSoggetto.trim() != ''}">
												<dt style="width: 180px;">Codice Soggetto</dt>
												<dd style="margin-left: 200px;">${model.dettaglioSoggetto.codiceSoggetto}</dd>
											</s:if>
											<s:if
												test="%{#attr.dettaglioSoggetto.codiceFiscale != null && #attr.dettaglioSoggetto.codiceFiscale.trim() != ''}">
												<dt style="width: 180px;">Codice Fiscale</dt>
												<dd style="margin-left: 200px;">${model.dettaglioSoggetto.codiceFiscale
													}</dd>
											</s:if>
											<s:if
												test="%{#attr.dettaglioSoggetto.partitaIva != null && #attr.dettaglioSoggetto.partitaIva.trim() != ''}">
												<dt style="width: 180px;">P.IVA</dt>
												<dd style="margin-left: 200px;">${model.dettaglioSoggetto.partitaIva}</dd>
											</s:if>
											<s:if
												test="%{#attr.dettaglioSoggetto.denominazione != null && #attr.dettaglioSoggetto.denominazione.trim() != ''}">
												<dt style="width: 180px;">Denominazione</dt>
												<dd style="margin-left: 200px;">${model.dettaglioSoggetto.denominazione}</dd>
											</s:if>

											<s:if
												test="%{#pagamento.iban != null && #pagamento.iban.trim() != ''}">
												<dt style="width: 180px;">Iban</dt>
												<dd style="margin-left: 200px;">${pagamento.iban}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.bic != null && #pagamento.bic.trim() != ''}">
												<dt style="width: 180px;">Bic</dt>
												<dd style="margin-left: 200px;">${pagamento.bic}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.contoCorrente != null && #pagamento.contoCorrente.trim() != ''}">
												<dt style="width: 180px;">Conto Corrente</dt>
												<dd style="margin-left: 200px;">${pagamento.contoCorrente}</dd>
											</s:if>

											<s:if
												test="%{#pagamento.intestazioneConto != null && #pagamento.intestazioneConto.trim() != ''}">
												<dt style="width: 180px;">Intestazione Conto</dt>
												<dd style="margin-left: 200px;">${pagamento.intestazioneConto}</dd>
											</s:if>

											<s:if
												test="%{#pagamento.soggettoQuietanzante != null && #pagamento.soggettoQuietanzante.trim() != ''}">

												<dt style="width: 180px;">Quietanzante</dt>
												<dd style="margin-left: 200px;">${pagamento.soggettoQuietanzante}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.codiceFiscaleQuietanzante != null && #pagamento.codiceFiscaleQuietanzante.trim() != ''}">
												<dt style="width: 180px;">Codice Fiscale Quietanzante</dt>
												<dd style="margin-left: 200px;">${pagamento.codiceFiscaleQuietanzante}</dd>
											</s:if>

											<!-- nuovi campi -->
											<s:if test="%{#pagamento.comuneNascita != null}">

												<dt style="width: 180px;">Comune Nascita quietanzante</dt>
												<dd style="margin-left: 200px;">${pagamento.comuneNascita.descrizione}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataNascitaQuietanzante != null}">

												<dt style="width: 180px;">Data Nascita quietanzante</dt>
												<dd style="margin-left: 200px;">${pagamento.dataNascitaQuietanzante}</dd>
											</s:if>


											<!-- / nuovi campi -->


											<!--   
	                <dt>ModalitÃ  Collegata</dt>
	                <dd></dd>
	             -->
											<s:if
												test="%{#pagamento.cessioneCodSoggetto != null && #pagamento.cessioneCodSoggetto.trim() != ''}">
												<dt style="width: 180px;">Codice Soggetto Ricevente</dt>
												<dd style="margin-left: 200px;">${pagamento.cessioneCodSoggetto}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice != null && #pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice.trim() != ''}">
												<dt style="width: 180px;">Tipo Accredito Ricevente</dt>
												<dd style="margin-left: 200px;">${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice}
													-
													${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.descrizione}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.note != null && #pagamento.note.trim() != ''}">
												<dt style="width: 180px;">Note</dt>
												<dd style="margin-left: 200px;">${pagamento.note}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataFineValidita != null}">
												<dt style="width: 180px;">Data cessazione</dt>
												<dd style="margin-left: 200px;">
													<s:date name="%{#pagamento.dataFineValidita}"
														format="dd/MM/yyyy" />
												</dd>
											</s:if>

											<s:if
												test="%{#pagamento.loginCreazione != null && #pagamento.loginCreazione.trim() != ''}">
												<dt style="width: 180px;">Utente Inserimento</dt>
												<dd style="margin-left: 200px;">${pagamento.loginCreazione}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataCreazione != null}">
												<dt style="width: 180px;">Data Inserimento</dt>
												<dd style="margin-left: 200px;">
													<s:date name="%{#pagamento.dataCreazione}"
														format="dd/MM/yyyy" />
												</dd>
											</s:if>

										</dl>
									</div>



								</s:if>
								<!-- END MDP NORMALE -->


								<!-- START MDP MODIFICA -->
								<s:if test="%{#pagamento.inModifica}">
									<div class="modal-body">
										<dl class="dl-horizontal">

											<s:if test="%{#pagamento.tipoAccredito != null}">
												<dt style="width: 200px;">Tipo Accredito</dt>
												<dd style="margin-left: 225px;">${pagamento.tipoAccredito}</dd>
											</s:if>

											<s:if test="%{#pagamento.inModifica}">
												<dt style="width: 200px;">Stato modalit&agrave;</dt>
												<dd style="margin-left: 225px;">VALIDO</dd>
											</s:if>
											<s:if
												test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">
												<dt style="width: 200px;">Utente Ultimo Aggiornamento</dt>
												<dd style="margin-left: 225px;">${pagamento.loginUltimaModifica}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataUltimaModifica != null}">
												<dt style="width: 200px;">Data Ultimo Aggiornamento</dt>
												<dd style="margin-left: 225px;">
													<s:date name="%{#pagamento.dataUltimaModifica}"
														format="dd/MM/yyyy" />
												</dd>
											</s:if>
											<dt style="width: 200px;">In Modifica</dt>
											<dd style="margin-left: 225px;">Si</dd>
											<s:else>
												<s:if
													test="%{#pagamento.descrizioneStatoModalitaPagamento != null && #pagamento.descrizioneStatoModalitaPagamento.trim() != ''}">
													<dt style="width: 200px;">Stato modalit&agrave;</dt>
													<dd style="margin-left: 225px;">${pagamento.descrizioneStatoModalitaPagamento}</dd>
												</s:if>
											</s:else>

											<s:if
												test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">
												<dt style="width: 200px;">Utente Modifica</dt>
												<dd style="margin-left: 225px;">${pagamento.loginModifica}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataModifica != null}">
												<dt style="width: 200px;">Data Modifica</dt>
												<dd style="margin-left: 225px;">
													<s:date name="%{#pagamento.dataModifica}"
														format="dd/MM/yyyy" />
												</dd>
											</s:if>

											<s:if
												test="%{#attr.dettaglioSoggetto.codiceSoggetto != null && #attr.dettaglioSoggetto.codiceSoggetto.trim() != ''}">
												<dt style="width: 200px;">Codice Soggetto</dt>
												<dd style="margin-left: 225px;">${model.dettaglioSoggetto.codiceSoggetto}</dd>
											</s:if>

											<s:if
												test="%{#attr.dettaglioSoggetto.codiceFiscale != null && #attr.dettaglioSoggetto.codiceFiscale.trim() != ''}">
												<dt style="width: 200px;">Codice Fiscale</dt>
												<dd style="margin-left: 225px;">${model.dettaglioSoggetto.codiceFiscale
													}</dd>
											</s:if>
											<s:if
												test="%{#attr.dettaglioSoggetto.partitaIva != null && #attr.dettaglioSoggetto.partitaIva.trim() != ''}">
												<dt style="width: 200px;">P.IVA</dt>
												<dd style="margin-left: 225px;">${model.dettaglioSoggetto.partitaIva}</dd>
											</s:if>
											<s:if
												test="%{#attr.dettaglioSoggetto.denominazione != null && #attr.dettaglioSoggetto.denominazione.trim() != ''}">
												<dt style="width: 200px;">Denominazione</dt>
												<dd style="margin-left: 225px;">${model.dettaglioSoggetto.denominazione}</dd>
											</s:if>

											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.iban != null && #pagamento.iban.trim() != ''}">
													<dt style="width: 200px;">Iban</dt>
													<dd style="margin-left: 225px;">${pagamento.iban}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.iban != null && #pagamento.modalitaOriginale.iban.trim() != ''}">
													<dt style="width: 200px;">Iban</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.iban}</dd>
												</s:if>
											</s:else>


											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.bic != null && #pagamento.bic.trim() != ''}">
													<dt style="width: 200px;">Bic</dt>
													<dd style="margin-left: 225px;">${pagamento.bic}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.bic != null && #pagamento.modalitaOriginale.bic.trim() != ''}">
													<dt style="width: 200px;">Bic</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.bic}</dd>
												</s:if>
											</s:else>

											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.contoCorrente != null && #pagamento.contoCorrente.trim() != ''}">
													<dt style="width: 200px;">Conto Corrente</dt>
													<dd style="margin-left: 225px;">${pagamento.contoCorrente}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.contoCorrente != null && #pagamento.modalitaOriginale.contoCorrente.trim() != ''}">
													<dt style="width: 200px;">Numero di Conto</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.contoCorrente}</dd>
												</s:if>
											</s:else>

											<!-- nuovi campi INTESTAZIONE CONTO -->
											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.intestazioneConto != null && #pagamento.intestazioneConto.trim() != ''}">
													<dt style="width: 200px;">Intestazione Conto</dt>
													<dd style="margin-left: 225px;">${pagamento.intestazioneConto}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.intestazioneConto != null && #pagamento.modalitaOriginale.intestazioneConto.trim() != ''}">
													<dt style="width: 200px;">Intestazione Conto</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.intestazioneConto}</dd>
												</s:if>
											</s:else>

											<!-- fine - nuovi campi -->

											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.soggettoQuietanzante != null && #pagamento.soggettoQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Soggetto Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.soggettoQuietanzante}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.soggettoQuietanzante != null && #pagamento.modalitaOriginale.soggettoQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Soggetto Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.soggettoQuietanzante}</dd>
												</s:if>
											</s:else>

											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.codiceFiscaleQuietanzante != null && #pagamento.codiceFiscaleQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Codice Fiscale Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.codiceFiscaleQuietanzante}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.codiceFiscaleQuietanzante != null && #pagamento.modalitaOriginale.codiceFiscaleQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Codice Fiscale Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.codiceFiscaleQuietanzante}</dd>
												</s:if>
											</s:else>


											<!-- nuovi campi: data e luogo di nascita-->

											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.dataNascitaQuietanzante != null && #pagamento.dataNascitaQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Data Nascita Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.dataNascitaQuietanzante}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.dataNascitaQuietanzante != null && #pagamento.modalitaOriginale.dataNascitaQuietanzante.trim() != ''}">
													<dt style="width: 200px;">Data Nascita Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.dataNascitaQuietanzante}</dd>
												</s:if>
											</s:else>

											<s:if test="isDecentrato()">
												<s:if test="%{#pagamento.comuneNascita != null}">
													<dt style="width: 200px;">Luogo Nascita Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.comuneNascita.descrizione}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.comuneNascita != null}">
													<dt style="width: 200px;">Luogo Nascita Quietanzante</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.comuneNascita.descrizione}</dd>
												</s:if>
											</s:else>



											<!-- fine - nuovi campi -->

											<!--   
	                <dt>ModalitÃ  Collegata</dt>
	                <dd></dd>
	             -->
											<s:if
												test="%{#pagamento.cessioneCodSoggetto != null && #pagamento.cessioneCodSoggetto.trim() != ''}">
												<dt style="width: 200px;">Codice Soggetto Ricevente</dt>
												<dd style="margin-left: 225px;">${pagamento.cessioneCodSoggetto}</dd>
											</s:if>
											<s:if
												test="%{#pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice != null && #pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice.trim() != ''}">
												<dt style="width: 200px;">Tipo Accredito Ricevente</dt>
												<dd style="margin-left: 225px;">${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice}
													-
													${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.descrizione}</dd>
											</s:if>


											<s:if test="isDecentrato()">
												<s:if
													test="%{#pagamento.note != null && #pagamento.note.trim() != ''}">
													<dt style="width: 200px;">Note</dt>
													<dd style="margin-left: 225px;">${pagamento.note}</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.note != null && #pagamento.modalitaOriginale.note.trim() != ''}">
													<dt style="width: 200px;">Note</dt>
													<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.note}</dd>
												</s:if>
											</s:else>


											<s:if test="isDecentrato()">
												<s:if test="%{#pagamento.dataFineValidita != null}">
													<dt style="width: 200px;">Data cessazione</dt>
													<dd style="margin-left: 225px;">
														<s:date name="%{#pagamento.dataFineValidita}"
															format="dd/MM/yyyy" />
													</dd>
												</s:if>
											</s:if>
											<s:else>
												<s:if
													test="%{#pagamento.modalitaOriginale.dataFineValidita != null}">
													<dt style="width: 200px;">Data cessazione</dt>
													<dd style="margin-left: 225px;">
														<s:date
															name="%{#pagamento.modalitaOriginale.dataFineValidita}"
															format="dd/MM/yyyy" />
													</dd>
												</s:if>
											</s:else>

											<s:if
												test="%{#pagamento.loginCreazione != null && #pagamento.loginCreazione.trim() != ''}">
												<dt style="width: 200px;">Utente Inserimento</dt>
												<dd style="margin-left: 225px;">${pagamento.loginCreazione}</dd>
											</s:if>
											<s:if test="%{#pagamento.dataCreazione != null}">
												<dt style="width: 200px;">Data Inserimento</dt>
												<dd style="margin-left: 225px;">
													<s:date name="%{#pagamento.dataCreazione}"
														format="dd/MM/yyyy" />
												</dd>
											</s:if>




										</dl>

									</div>





								</s:if>
								<!-- END MDP MODIFICA -->


								<div class="modal-footer">
									<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
								</div>
							</div>
						</s:iterator>
						<!-- End Modal Window Consulta Mod Pagamento -->











<script type="text/javascript">
$(document).ready(function() {

	$("div.tab-content").attr("style", "overflow: none")   ;
});


</script>

                                
<p><s:include value="/jsp/include/indietro.jsp" /></p>
</s:form>
</div>
</div>	 
</div>	
<s:include value="/jsp/include/footer.jsp" />