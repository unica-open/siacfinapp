<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

  	 <!-- SIAC-5333 -->
	 <s:if test="%{richiediConfermaRedirezioneContabilitaGenerale}">
          <div id="msgPrimaNota" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
            <div class="modal-body">
              <div class="alert alert-warning">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <p>
                	<s:property value="messaggioProsecuzioneSuContabilitaGenerale" escapeHtml="false"/>
                </p>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn freezePagina" id="inserisciPrimaNotaProvvisoria" data-dismiss="modal" aria-hidden="true">valida in seguito</button>
              <button class="btn freezePaginaWaitOp" id="validaPrimaNota" data-dismiss="modal" aria-hidden="true">valida ora</button>
            </div>
          </div>
	</s:if>
	<s:hidden id="HIDDEN_saltaInserimentoPrimaNota" name="saltaInserimentoPrimaNota"/>
	<s:hidden id="HIDDEN_richiediConfermaUtente" name="richiediConfermaUtente"/>
    <!-- SIAC-5333 --> 

