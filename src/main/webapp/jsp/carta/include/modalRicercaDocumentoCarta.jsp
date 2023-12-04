<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<!--modale collegaDocumento-->
<div id="collegaDocumento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="collegaDocumentoLabel" aria-hidden="true">
     <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona documento</h4>
<!-- 		<p>&Eacute; necessario inserire oltre all'anno almeno il numero, il soggetto oppure il tipo</p> -->
	</div>
	
	<div id="refreshPopupModalDoc">
		<s:include value="/jsp/carta/include/modalContentDocumentoCarta.jsp" />	
	</div>
	
	<div class="modal-footer">
			<!-- task-131 <s:submit id="confermaCompGuidataSubDoc" name="conferma" value="conferma" method="confermaCompGuidataDocumento" disabled="true" cssClass="btn btn-primary" /> -->
			<s:submit id="confermaCompGuidataSubDoc" name="conferma" value="conferma" action="%{#confermaCompGuidataDocumentoAction}" disabled="true" cssClass="btn btn-primary" />
	</div>
</div>
<!--/modale -->