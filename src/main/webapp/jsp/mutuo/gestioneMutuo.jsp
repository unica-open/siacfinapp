<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>
<body>
	<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="gestioneMutuo" action="gestioneMutuo.do" method="post">
					<h3><s:property value="%{labels.AZIONE}"/>mutuo  <s:property  value="codiceMutuoCorrente"/></h3>
					<s:if test="codiceMutuoCorrente != null">
					<h4>Totale impegnato voci di mutuo: <s:property value="getText('struts.money.format', {impegnatoVociMutuo})" /> - Disponibile mutuo: <s:property value="getText('struts.money.format', {disponibilitaMutuo})" /> </h4>
					</s:if>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<s:include value="/jsp/mutuo/include/provvedimentoMutuo.jsp" />
							<div id="refreshHeaderSoggetto">
				            	<s:include value="/jsp/mutuo/include/headerSoggettoMutuo.jsp"/>
				            </div>
				            <s:include value="/jsp/mutuo/include/soggettoMutuo.jsp" />              
				         
							<s:include value="/jsp/include/modalProvvedimenti.jsp" />
							<s:include value="/jsp/include/modalSoggetto.jsp" />
							<s:include value="/jsp/mutuo/include/dettaglioMutuo.jsp" />
							<br/> <br/>    
							<p>
							
							        <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
						           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
											
											<%-- CR-2023 da eliminare
												<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/>
											 --%>
											<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
											<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   							<!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
		   							
		   						<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
							
								<s:include value="/jsp/include/indietro.jsp" /> 
            					<s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> 
            					<span class="pull-right">
            						<s:submit id="salva" name="salva" value="salva" method="salvaMutuo" cssClass="btn btn-primary freezePagina" />
								</span>
							</p>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {

	
			
		
		$("#salva").click(function() {
			
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
			var strutturaAmministrativaParam = "";
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = currentNode.uid;
				});
			}
			
			//$("#strutturaDaInserimento").val(strutturaAmministrativaParam);
			//alert("strutturaAmministrativaParam: "+ strutturaAmministrativaParam);
			// Jira 1682, con il currentNode.uid si stettaca prima l'hidden sbagliato
			// hidden che non veniva poi usato lato action
			$("#strutturaSelezionataSuPagina").val(strutturaAmministrativaParam);

		});	
		
		
		$("#linkCompilazioneGuidataProvvedimento").click(function(){

			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
				var strutturaAmministrativaParam = "";
				if (treeObj != null) {
					var selectedNode = treeObj.getCheckedNodes(true);
					selectedNode.forEach(function(currentNode) {
						strutturaAmministrativaParam = currentNode;
					});
				}
			
			
				initRicercaGuidataProvvedimentoConStruttura(
					$("#annoProvvedimento").val(), 
					$("#numeroProvvedimento").val(),
					$("#listaTipiProvvedimenti").val(),
					strutturaAmministrativaParam
			);


		});
		
		$("#linkCompilazioneGuidataSoggettoMutuo").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreMutuo").val(),
				null
			);
		});
	});
	
</script>  

<s:include value="/jsp/include/footer.jsp" />