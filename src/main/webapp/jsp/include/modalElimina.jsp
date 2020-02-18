<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<div class="modal-body">
	    <div class="alert alert-error">
	      <button type="button" class="close" data-dismiss="alert">&times;</button>
	      <p><strong>Attenzione!</strong></p>
	      <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
	    </div>
	</div>
	<div class="modal-footer">
	    <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	    <button id="btnEliminazione" class="btn btn-primary">si, prosegui</button>
	    <s:submit id="btnEliminazioneSubmit" value="si, prosegui" cssClass="btn btn-primary hide freezePagina" method="elimina"/>
	</div>
</div>  
<!--/modale elimina -->