<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!--modale impegno -->
	<div id="guidaImpegno" class="modal hide fade" tabindex="-1">		
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" data-dismiss="modal">&times;</button>
				<h4 class="nostep-pane">Seleziona impegno</h4>
			</div>
			<div id="refreshImpegnoPopupModal">			
				<s:include value="/jsp/ordinativo/include/modalImpegnoReintroitoContent.jsp" />				
			</div>
	</div>
<!-- end modal -->
