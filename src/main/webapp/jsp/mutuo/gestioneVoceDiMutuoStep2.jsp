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
				<s:form id="gestioneVoceDiMutuoStep2" action="gestioneVoceDiMutuoStep2.do" method="post">
					<h3>Inserimento voce mutuo</h3>
					
					
					<s:hidden id="toggleImpAccAperto" name="toggleImpAccAperto"/> 
					
					
					<div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Scelta provvedimento<span class="chevron"></span></li>
						<li data-target="#step2" class="active"><span class="badge">2</span>Inserimento voce<span class="chevron"></span></li>
					</ul>
					</div>	
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<h4 class="nostep-pane">Elenco impegni finanziabili da mutuo</h4>
							<div id="refreshListaImpegniSubimpegniFinanziabili">
								<s:include value="/jsp/mutuo/include/gestioneListaImpegniSubimpegniFinanziabiliStep2.jsp" />
							</div>
						</div>
					</div>
					<p class="margin-medium">
					   <s:include value="/jsp/include/indietro.jsp" /> 
					</p>  
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
	$(document).ready(function(){
			
			var insVoce= $("#selezioneVoceDiMutuo");
			var toggleImpAccAperto = $("#toggleImpAccAperto");
	
			if (toggleImpAccAperto.val() == "true") {
				$('.btnVisible').show();
				insVoce.click();
			} 
			
	
		});
</script>
<s:include value="/jsp/include/footer.jsp" />
