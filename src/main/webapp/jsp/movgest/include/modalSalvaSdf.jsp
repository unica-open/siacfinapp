<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="msgConfermaSDF" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgConfermaSDFLabel" aria-hidden="true">
   <div class="modal-body">
     <div class="alert alert-error">
       <button type="button" class="close" data-dismiss="alert">&times;</button>
       <p><strong>Attenzione!</strong></p>
       
       <p><s:label name="testoConfermaInserisciSdf" /></p>
     </div>
   </div>
   <div class="modal-footer">
     <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
     <!--task-131 <s:submit id="submitSDFBtn" name="btnSalvaConConfermaSDF" value="si, salva" cssClass="btn btn-primary freezePagina" method="proseguiSalva"/>-->
     <s:submit id="submitSDFBtn" name="btnSalvaConConfermaSDF" value="si, salva" cssClass="btn btn-primary freezePagina" action="%{#proseguiSalvaAction}"/>
   </div>
</div>

<div id="msgConfermaSDFDiNuovoDisp" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgConfermaSDFDiNuovoDispLabel" aria-hidden="true">
   <div class="modal-body">
     <div class="alert alert-error">
       <button type="button" class="close" data-dismiss="alert">&times;</button>
       <p><strong>Attenzione!</strong></p>
       
       <p><s:label name="testoConfermaInserisciSdfDiNuovoDisp" /></p>
     </div>
   </div>
   <div class="modal-footer">
     <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
     <!-- task-131 <s:submit id="submitSDFDiNuovoDispBtn" name="btnSalvaConConfermaSDFDiNuovoDisp" value="si, salva" cssClass="btn btn-primary freezePagina" method="proseguiSalva"/> -->
     <s:submit id="submitSDFDiNuovoDispBtn" name="btnSalvaConConfermaSDFDiNuovoDisp" value="si, salva" cssClass="btn btn-primary freezePagina" action="%{#proseguiSalvaAction}"/>
   </div>
</div>

