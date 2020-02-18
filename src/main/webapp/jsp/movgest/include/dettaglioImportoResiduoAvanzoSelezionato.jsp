<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
	       
 <div id="divImportoResiduoAvanzoSelezionato" style="display: inline">
	 <label class="radio inline"><b>Importo disponibile: <s:property value="getText('struts.money.format', {getImportoResiduoAvanzoVincoloSelezionato()})" /> </b></label>
 </div>
	