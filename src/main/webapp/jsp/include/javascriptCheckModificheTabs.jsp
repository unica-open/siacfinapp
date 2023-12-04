<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<!-- Link utilizzati per la gestione controllo modifiche -->
<a id="linkMsgDatipersi" href="#msgDatipersi" data-toggle="modal" style="display:none;"></a>
<!-- task-131 <s:submit id="btnGestisciForward" method="gestisciForward" cssStyle="display:none;"/> -->
<s:submit id="btnGestisciForward" action="%{#gestisciForwardAction}" cssStyle="display:none;"/>

<s:hidden id="forward" name="forward"/>
<s:hidden id="forceReload" name="forceReload"/>	
<s:hidden id="ambito" name="ambito" />
<%-- Gestione per controllo modifiche tabs da parte dell'utente --%>
<script type="text/javascript" src="${jspath}generic/checkModificheTabs.js"></script>
