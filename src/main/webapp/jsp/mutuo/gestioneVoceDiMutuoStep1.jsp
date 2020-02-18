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
				<s:form id="gestioneVoceDiMutuoStep1" action="gestioneVoceDiMutuoStep1.do" method="post">
					<h3>Inserimento voce mutuo</h3>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active"><span class="badge">1</span>Scelta provvedimento<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Inserimento voce<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<s:include value="/jsp/mutuo/include/provvedimentoMutuo.jsp" />
							<s:include value="/jsp/include/modalProvvedimenti.jsp" />
						</div>
					</div>
					
					<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
					
					<p class="margin-medium">
					   <s:include value="/jsp/include/indietro.jsp" /> 
					   <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" />	
					</p>       
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {
		$("#linkCompilazioneGuidataProvvedimento").click(function(){
			initRicercaGuidataProvvedimento(
					$("#annoProvvedimento").val(), 
					$("#numeroProvvedimento").val(),
					$("#listaTipiProvvedimenti").val()
			);
		});
	});
	
	$("#prosegui").click(function() {
		
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
	
</script>  

<s:include value="/jsp/include/footer.jsp" />