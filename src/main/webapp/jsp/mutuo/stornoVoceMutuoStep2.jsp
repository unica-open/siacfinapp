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
				<s:form id="stornoVoceMutuoStep2" action="stornoVoceMutuoStep2.do" method="post">
					<h3>Inserimento storno voce mutuo</h3>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<div id="MyWizard" class="wizard">
							<ul class="steps">
								<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Inserisci importo storno<span class="chevron"></span></li>
								<li data-target="#step2" class="active"><span class="badge">2</span>Seleziona Provvedimento<span class="chevron"></span></li>
								<li data-target="#step3"><span class="badge">3</span>Inserimento voce<span class="chevron"></span></li>
								
							</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<s:include value="/jsp/mutuo/include/provvedimentoMutuo.jsp" />
							<s:include value="/jsp/include/modalProvvedimenti.jsp" />
						</div>
					</div>
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
	
</script>  

<s:include value="/jsp/include/footer.jsp" />