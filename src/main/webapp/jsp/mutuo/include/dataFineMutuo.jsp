<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<span class="al">
	<label class="radio inline" for="dataFine">Data fine</label>
</span>
<s:textfield id="dataFine" title="gg/mm/aaaa" name="dataFine" cssClass="lbTextSmall span2 datepicker" disabled="true" />