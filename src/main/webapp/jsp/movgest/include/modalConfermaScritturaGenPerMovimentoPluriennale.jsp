<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="msgConfermaPrimeNoteStep2" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgConfermaPrimeNoteStep2Label" aria-hidden="true">
   <div class="modal-body">
     <div class="alert alert-error">
       <button type="button" class="close" data-dismiss="alert">&times;</button>
       <p><strong>Attenzione!</strong></p>
       
       <p><s:label name="testoConfermaPrimeNote" /></p>
     </div>
   </div>
   <div class="modal-footer">
     <button class="btn" data-dismiss="modal" aria-hidden="true">indietro</button>
     <s:submit id="submitSalvaPrimeNoteInCorsoStep2Btn" name="btnSalvaConConfermaPrimeNoteStep2" value="Competenza esercizio in corso" cssClass="btn btn-primary freezePagina" method="impostaPluriennalePrimeNoteEsercizioInCorso" data-dismiss="modal"/>
     <s:submit id="submitSalvaPrimeNoteFuturiStep2Btn" name="btnSalvaConConfermaPrimeNoteStep2" value="Competenza esercizi futuri" cssClass="btn btn-primary freezePagina" method="impostaPluriennalePrimeNoteEsercizioFuturi" data-dismiss="modal"/>
   </div>
</div>
