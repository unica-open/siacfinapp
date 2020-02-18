<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<s:include value="/jsp/include/header.jsp" />
<body>
	<div class="container-fluid-banner">

		<div class="container-fluid-banner">
			<a name="A-contenuti" title="A-contenuti"></a>
		</div>
		<!--corpo pagina-->
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span12 contentPage">

					<s:form id="aggiornaSediSecondarie" action="aggiornaSediSecondarie.do"	method="post">

<s:include value="/jsp/include/actionMessagesErrors.jsp" />

						<ul class="nav nav-tabs">
						
							<s:hidden name="fromGestisciSede"/> 
							<s:hidden name="soggetto"/> 
							<s:hidden id="toggleSedeAperto" name="toggleSedeAperto"/> 
					 	
					    <s:if test="fromGestisciSede">
						    <li class="active"><a href="#">Sedi Secondarie</a></li>
					    </s:if>
					    <s:else>
							<li><s:a action="aggiornaSoggetto" method="doExecute">Soggetto</s:a></li>
							<li class="active"><a href="#">Sedi Secondarie</a></li>
							<li><s:a action="modalitaPagamentoSoggetto">Modalit&agrave; pagamento</s:a></li>
						</s:else>
						</ul>

		<h3>Codice Soggetto: <s:property value="dettaglioSoggetto.codiceSoggetto" /> 
		-  <s:property value="dettaglioSoggetto.denominazione" /> 
		 (<s:property value="dettaglioSoggetto.statoOperativo" /> dal <s:property value="%{dettaglioSoggetto.dataValidita}" />) </h3>     

						<h4>Sedi secondarie</h4>
						<display:table name="listaSecondariaSoggetto"  class="table tab_left table-hover" summary="riepilogo sedi secondarie"
					         requestURI="aggiornaSediSecondarie.do"
							 uid="tabSediSecondarieID"  >
						 	 <display:column title="Denominazione" property="denominazione" />
							 <display:column title="Indirizzo">
							 	<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.sedime" /> 
							 	<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.denominazione" />
							 	<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.numeroCivico" />,
							 			
							 	<s:if test='#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.codiceNazione.equals("IT")  '>
							 		<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.cap" />							 	
							 		<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.comune" />
							 		<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.provincia" />
							 	</s:if>
							 	<s:else>
							 		<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.comune" />
							 		<s:property value="#attr.tabSediSecondarieID.indirizzoSoggettoPrincipale.nazione" />
							 	</s:else>
							 </display:column>
							 <display:column title="Stato">
							 	<s:property value="#attr.tabSediSecondarieID.descrizioneStatoOperativoSedeSecondaria" />
							 	<s:if test="#attr.tabSediSecondarieID.inModifica"> (in modifica)</s:if>
							 </display:column>
							 <display:column title="" class="tab_Right" >
							    
							     <div class="btn-group">
							     
									<button class="btn dropdown-toggle" data-toggle="dropdown">
										Azioni <span class="caret"></span>
									</button>
									<ul class="dropdown-menu pull-right">
										<li><a href="#" class="consultaSedeSecondaria" id="consulta_<%= pageContext.getAttribute("tabSediSecondarieID_rowNum")%>">consulta</a></li>
									
									<s:if test="!'#attr.tabSediSecondarieID.descrizioneStatoOperativoSedeSecondaria'.equalsIgnoreCase('ANNULLATO')">
									
									<s:if test="isAbilitato(2, '#attr.tabSediSecondarieID.statoOperativoSedeSecondaria', '#attr.tabSediSecondarieID.utenteModifica', '#attr.tabSediSecondarieID.utentePropostaModifica')">
		     			     			<li><a href="#" class="aggiornaSedeSecondaria" id="aggiorna_<%= pageContext.getAttribute("tabSediSecondarieID_rowNum")%>">aggiorna</a></li>
		     			 			</s:if>
									<s:if test="isAbilitato(3, '#attr.tabSediSecondarieID.statoOperativoSedeSecondaria', '#attr.tabSediSecondarieID.utenteModifica', '#attr.tabSediSecondarieID.utentePropostaModifica')">
		     			     			<li><a id="linkAnnulla_<s:property value="%{#attr.tabSediSecondarieID_rowNum}"/>" href="#msgAnnulla" data-toggle="modal" class="linkAnnullaSede">annulla</a></li>
		     			 			</s:if>
									<s:if test="isAbilitato(6, '#attr.tabSediSecondarieID.statoOperativoSedeSecondaria', '#attr.tabSediSecondarieID.utenteModifica', '#attr.tabSediSecondarieID.utentePropostaModifica')">
		     			     			<li><a id="linkElimina_<s:property value="%{#attr.tabSediSecondarieID_rowNum}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaSede">elimina</a></li>
		     			 			</s:if>
									<s:if test="isAbilitato(7, '#attr.tabSediSecondarieID.statoOperativoSedeSecondaria', '#attr.tabSediSecondarieID.utenteModifica', '#attr.tabSediSecondarieID.utentePropostaModifica')">
										<s:url id="validaUrl" method="gestioneValidaSede">
											<s:param name="idSedeSecondaria" value="%{#attr.tabSediSecondarieID_rowNum}" />
										</s:url>
										<li><a href="<s:property value="%{validaUrl}"/>">valida</a></li>                        	
		     			 			</s:if>
		     			 			 
		     			 			</s:if>
		     			 			
									</ul>
								</div>
							 </display:column>	
		
						</display:table>
						
						<a id="openConsSede" href="#consSede" data-toggle="modal" data-target="#consSede"></a>
						<div id="consSede" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consUPB" aria-hidden="true">
						</div>
						
						<p>
							<a id="ancoretta"></a>
							<a id="openInsSede" class="btn" >inserisci nuove sedi</a>
						</p>
						<a id="openAggSede" href="#insInd" data-toggle="collapse" data-target="#insInd"></a>
						<div id="insInd" class="collapse">
<s:include value="/jsp/soggetto/include/salvaSedeSnippet.jsp" />
						</div>

						<s:hidden id="idSedeSecondariaDaEliminare" name="idSedeSecondariaDaEliminare"/>
						<s:hidden id="fromPulsanteInserisciNuova" name="fromPulsanteInserisciNuova"/>

						<!-- Modal -->
						<div id="msgElimina" class="modal hide fade" tabindex="-1"
							role="dialog" aria-labelledby="msgEliminaLabel"
							aria-hidden="true">
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-dismiss="alert">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no,	indietro</button>
								<s:submit id="eliminaBtn" name="btnEliminaSede" value="si, prosegui" cssClass="btn btn-primary" method="gestioneEliminaSede"/>
							</div>
						</div>
						<!--/modale elimina -->
						<!-- Modal -->
						<div id="msgAnnulla" class="modal hide fade" tabindex="-1"
							role="dialog" aria-labelledby="msgAnnullaLabel"
							aria-hidden="true">
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-dismiss="alert">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no,	indietro</button>
								<s:submit id="annullaBtn" name="btnAnnullaSede" value="si, prosegui" cssClass="btn btn-primary" method="gestioneAnnullaSede"/>
							</div>
						</div>
						<!--/modale annulla -->

 <!--/modale annulla -->
  <p class="marginLarge"> 
  	   <s:include value="/jsp/include/indietro.jsp" />
  	   <!--  pulsante che salva tutto e richiama il webservice   
  	   
  	   Jira-130
  	   
  	   <s:submit name="salva" value="salva" method="salvaGenerale" cssClass="btn btn-primary pull-right" /> 
  	   -->
  </p>  	                                 

					</s:form>
				</div>
			</div>
		</div>

	</div>
<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />


<script type="text/javascript">
	$(document).ready(function() {
		
		// blocca pagina su salva 
		$('#salvaId').click(function() {
			bloccaPagina('#salvaId');
		});
				
		var openConsSede = $("#openConsSede"),		consSede = $("#consSede"),
			openAggSede = $("#openAggSede"),		insSede = $("#insInd"),
			openValSede = $("#openValSede"),		valSede = $("#valSede");
		var toggleSedeAperto = $("#toggleSedeAperto");
		if (toggleSedeAperto.val() == "true") {
			openAggSede.click();
		} 
		$(".consultaSedeSecondaria").click(function() {
			var id = $(this).attr("id").split("_");
			$.ajax({
				url: '<s:url method="consultaSede"/>',
				data: 'idSedeSecondaria=' + id[1],
				success: function(data)  {
					consSede.html(data);
					openConsSede.click();
				}
			});
		});
		$(".aggiornaSedeSecondaria").click(function() {
			var id = $(this).attr("id").split("_");
			$.ajax({
				url: '<s:url method="modificaSede"/>',
				data: 'idSedeSecondaria=' + id[1],
				success: function(data)  {
					insSede.html(data);
					openAggSede.click();
					autocompleteCitta();
					autocompleteSedimi();
				}
			});
		});
		$("#annullaInserimento").click(function() {
			$.ajax({
				url: '<s:url method="pulisciSede"/>',
				success: function(data)  {
					insSede.html(data);
				}
			});
		});
		$("#openInsSede").click(function() {
			$("#fromPulsanteInserisciNuova").val(true);
			$("#idSedeSecondaria").val(null);
			openAggSede.click();
			spostaLAncora('ancoretta');
//  			$.ajax({
//  				url: '<s:url method="pulisciSede"/>',
//  				success: function(data)  {
//  					insSede.html(data);
//  					openAggSede.click();
//  				}
//  			});
		});
		$(".linkEliminaSede").click(function() {
			var id = $(this).attr("id").split("_");
			if (id != null && id.length > 0) {
				$("#idSedeSecondariaDaEliminare").val(id[1]);
			}
		});
		$(".linkAnnullaSede").click(function() {
			var id = $(this).attr("id").split("_");
			if (id != null && id.length > 0) {
				$("#idSedeSecondariaDaEliminare").val(id[1]);
			}
		});
		
		
		
		
		$("#idNazione").change(function() {
			$("#comune").val($("option:selected", this).text()).trigger({type: 'keydown', keyCode: 1, which: 1, charCode: 1});
		});
		
	});
</script>
<s:include value="/jsp/include/footer.jsp" />