<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />	


</head>

<s:include value="/jsp/include/header.jsp" />

  <body>
<div class="container-fluid-banner">
<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


<div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    
        <s:form method="post" action="validaSoggetto.do">
   
   <s:if test="hasActionErrors()">
		<%-- Messaggio di ERROR --%>
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			    <s:actionerror/>
				
			</ul>
		</div>
   </s:if>
   <s:if test="hasActionMessages()">
		<%-- Messaggio di WARNING --%>
		<div class="alert alert-success">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<ul>
				<s:actionmessage/>
			</ul>
		</div>
   </s:if>
    <ul class="nav nav-tabs">
	    <li class="active">
	    <a href="#soggetto" data-toggle="tab">Soggetto</a>
	    </li>
   	</ul>
   
    <div class="tab-content">
<div class="tab-pane active" id="soggetto">
<%-- <s:if test="dettaglioSoggettoMod.tipoSoggetto.soggettoTipoDesc != 'PROVVISORIO'"> --%>
   <div class="span6"> 
    
      <s:if test="dettaglioSoggettoMod == null">
 		<h4>Da validare</h4>
 	 </s:if>
     <s:if test="dettaglioSoggettoMod != null">
 			<h4>Attualmente presente</h4>
 	 </s:if>
    
    
    <dl class="dl-horizontal well">
   		<dt>Stato Soggetto</dt>
	    <dd><s:property value="dettaglioSoggetto.statoOperativo"/>&nbsp;
	    <dt>Data stato</dt>
	    <dd><s:property value="%{dettaglioSoggetto.dataStato}"/>&nbsp;</dd>
	    <dt>Codice</dt>
	    <dd><s:property value="dettaglioSoggetto.codiceSoggetto"/>&nbsp;</dd>
	    <dt>Tipo soggetto</dt>
	    <dd><s:property value="dettaglioSoggetto.tipoSoggetto.soggettoTipoDesc"/>&nbsp;</dd>
	    <dt>Natura giuridica</dt>
	    <dd><s:property value="dettaglioSoggetto.naturaGiuridicaSoggetto.soggettoTipoDesc"/>&nbsp;</dd>
	    <dt>Codice fiscale</dt>
	    <dd><s:property value="dettaglioSoggetto.codiceFiscale"/>&nbsp;</dd>
	    <dt>Residente estero</dt>
	    <dd><s:property value="convertiBooleanToString(dettaglioSoggetto.residenteEstero)"/>&nbsp;</dd>
	    <dt>Codice fiscale estero</dt>
	    <dd><s:property value="dettaglioSoggetto.codiceFiscaleEstero"/>&nbsp;</dd>
	    <dt>Partita IVA</dt>
	    <dd><s:property value="dettaglioSoggetto.partitaIva"/>&nbsp;</dd>
	    <dt>Denominazione</dt>
	    <dd><s:property value="dettaglioSoggetto.denominazione"/>&nbsp;</dd>
	    <dt>Matricola</dt>
	    <dd><s:property value="dettaglioSoggetto.matricola"/>&nbsp;</dd>
	    <dt>Data di nascita</dt>
	    <dd><s:property value="%{dettaglioSoggetto.dataNascita}"/>&nbsp;</dd>
	    <dt>Stato/Provincia/Comune</dt>
	    <dd><s:property value="dettaglioSoggetto.comuneNascita.nazioneDesc"/>/<s:property value="dettaglioSoggetto.comuneNascita.siglaProvincia"/>/<s:property value="dettaglioSoggetto.comuneNascita.codiceBelfiore"/>&nbsp;</dd>
	    <dt>Note</dt>
	    <dd><s:property value="dettaglioSoggetto.note"/>&nbsp;</dd>
	    
	    <s:if test="dettaglioSoggetto.tipoSoggetto.soggettoTipoCode != 'PF'">
	    <dt>Data fine validit&agrave; DURC</dt>
	    <dd><s:property value="%{dettaglioSoggetto.dataFineValiditaDurc}"/>&nbsp;</dd>
	    <dt>Fonte DURC</dt>
	    <dd><s:property value="dettaglioSoggetto.descrizioneFonteDurc"/>&nbsp;</dd>
	    <dt>Note DURC</dt>
	    <dd><s:property value="dettaglioSoggetto.noteDurc"/>&nbsp;</dd>
		</s:if>
			    
	   	<dt>Classificazione</dt>
	    <dd><s:property value="classiFormattate"/>&nbsp;</dd>
	    <dt>Oneri</dt>
		<dd><s:property value="oneriFormattati"/>&nbsp;</dd>
	    <s:iterator value="dettaglioSoggetto.indirizzi" status="varStatusIndirizzo" var="currentIndirizzo">
		    <dt>Indirizzo<s:if test="#varStatusIndirizzo.index != 0">${varStatusIndirizzo.index + 1}</s:if></dt>
		    <dd><s:property value="indirizzoFormattato"/>&nbsp;</dd>
	    </s:iterator>
	    <s:iterator value="dettaglioSoggetto.contatti" status="varStatusContatto" var="currentContatto">
		    <dt>Recapiti</dt>
		    <dd><s:property value="contattoFormattato"/>&nbsp;</dd>
	    </s:iterator>
	    
	    <dt>Canale PA / PRIVATI</dt>
	    <dd><s:property value="dettaglioSoggetto.canalePA"/>&nbsp;</dd>
	    <dt>Codice destinatario / IPA</dt>
	    <dd><s:property value="dettaglioSoggetto.codDestinatario"/>&nbsp;</dd>
	    <dt>email PEC</dt>
	    <dd><s:property value="dettaglioSoggetto.emailPec"/>&nbsp;</dd>
	    	    
    </dl>         
	</div>
<%-- 	</s:if> --%>
<s:if test="dettaglioSoggettoMod!=null">
    <div class="span6"> 
    <h4>Da validare</h4>
      <dl class="dl-horizontal well" style="background-color: transparent;">
        <dt>Stato Soggetto</dt>
	    <dd><s:property value="dettaglioSoggettoMod.statoOperativo"/>&nbsp;
	    <dt>Data stato</dt>
	    <dd><s:property value="%{dettaglioSoggettoMod.dataStato}"/>&nbsp;</dd>
	    <dt>Codice</dt>
	    <dd><s:property value="dettaglioSoggettoMod.codiceSoggetto"/>&nbsp;</dd>
	    <dt>Tipo soggetto</dt>
	    <s:if test="%{dettaglioSoggetto.tipoSoggetto.soggettoTipoDesc != dettaglioSoggettoMod.tipoSoggetto.soggettoTipoDesc}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.tipoSoggetto.soggettoTipoDesc"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.tipoSoggetto.soggettoTipoDesc"/>&nbsp;</dd></s:else>
	    <dt>Natura giuridica</dt>
	    <s:if test="controlloNaturaGiuridica()">
	    <dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.naturaGiuridicaSoggetto.soggettoTipoDesc"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.naturaGiuridicaSoggetto.soggettoTipoDesc"/>&nbsp;</dd></s:else>	    
	    <dt>Codice fiscale</dt>
	    <s:if test="controlloDifferenzaCF()"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.codiceFiscale"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.codiceFiscale"/>&nbsp;</dd></s:else>
	    <dt>Residente estero</dt>
	    <s:if test="%{convertiBooleanToString(dettaglioSoggetto.residenteEstero) != convertiBooleanToString(dettaglioSoggettoMod.residenteEstero)}"><dd><span style="background-color:yellow"><s:property value="convertiBooleanToString(dettaglioSoggettoMod.residenteEstero)"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="convertiBooleanToString(dettaglioSoggettoMod.residenteEstero)"/>&nbsp;</dd></s:else>
	    <dt>Codice fiscale estero</dt>
	    <s:if test="%{dettaglioSoggetto.codiceFiscaleEstero != dettaglioSoggettoMod.codiceFiscaleEstero }"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.codiceFiscaleEstero"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.codiceFiscaleEstero"/>&nbsp;</dd></s:else>
	    <dt>Partita IVA</dt>
	    <s:if test="%{dettaglioSoggetto.partitaIva != dettaglioSoggettoMod.partitaIva }"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.partitaIva"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.partitaIva"/>&nbsp;</dd></s:else>
	    <dt>Denominazione</dt>
	    <s:if test="%{dettaglioSoggetto.denominazione != dettaglioSoggettoMod.denominazione}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.denominazione"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.denominazione"/>&nbsp;</dd></s:else>
	    <dt>Matricola</dt>
	    <s:if test="%{dettaglioSoggetto.matricola != dettaglioSoggettoMod.matricola}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.matricola"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.matricola"/>&nbsp;</dd></s:else>
	    <dt>Data di nascita</dt>
	    <s:if test="%{dettaglioSoggetto.dataNascita != dettaglioSoggettoMod.dataNascita}"><dd><span style="background-color:yellow"><s:property value="%{dettaglioSoggettoMod.dataNascita}"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="%{dettaglioSoggettoMod.dataNascita}"/>&nbsp;</dd></s:else>
	    <dt>Stato/Provincia/Comune</dt>
	    <s:if test="controlloStatoProvinciaComune()"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.comuneNascita.nazioneDesc"/>/<s:property value="dettaglioSoggettoMod.comuneNascita.siglaProvincia"/>/<s:property value="dettaglioSoggettoMod.comuneNascita.codiceBelfiore"/></span>&nbsp;</dd></s:if>	    
	    <s:else><dd><s:property value="dettaglioSoggettoMod.comuneNascita.nazioneDesc"/>/<s:property value="dettaglioSoggettoMod.comuneNascita.siglaProvincia"/>/<s:property value="dettaglioSoggettoMod.comuneNascita.codiceBelfiore"/>&nbsp;</dd></s:else>
	    <dt>Note</dt>
	    <s:if test="%{dettaglioSoggetto.note != dettaglioSoggettoMod.note}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.note"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.note"/>&nbsp;</dd></s:else>

	    <s:if test="dettaglioSoggetto.tipoSoggetto.soggettoTipoCode != 'PF'">
	    <dt>Data fine validit&agrave; DURC</dt>
	    <s:if test="%{dettaglioSoggetto.dataFineValiditaDurc != dettaglioSoggettoMod.dataFineValiditaDurc}"><dd><span style="background-color:yellow"><s:property value="%{dettaglioSoggettoMod.dataFineValiditaDurc}"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="%{dettaglioSoggettoMod.dataFineValiditaDurc}"/>&nbsp;</dd></s:else>
	    <dt>Fonte DURC</dt>
	    <s:if test="%{dettaglioSoggetto.descrizioneFonteDurc != dettaglioSoggettoMod.descrizioneFonteDurc}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.descrizioneFonteDurc"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.descrizioneFonteDurc"/>&nbsp;</dd></s:else>
	    <dt>Note DURC</dt>
	    <s:if test="%{dettaglioSoggetto.noteDurc != dettaglioSoggettoMod.noteDurc}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.noteDurc"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.noteDurc"/>&nbsp;</dd></s:else>
	    </s:if>
	    
	   	<dt>Classificazione</dt>
	    <s:if test="%{classiFormattate != classiFormattateMod}"><dd><span style="background-color:yellow"><s:property value="classiFormattateMod"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="classiFormattateMod"/>&nbsp;</dd></s:else>
	    <dt>Oneri</dt>
		<s:if test="%{oneriFormattati != oneriFormattatiMod}"><dd><span style="background-color:yellow"><s:property value="oneriFormattatiMod"/></span>&nbsp;</dd></s:if>
		<s:else><dd><s:property value="oneriFormattatiMod"/>&nbsp;</dd></s:else>
	    <s:iterator value="dettaglioSoggettoMod.indirizzi" status="varStatusIndirizzo" var="currentIndirizzo">
		    <dt>Indirizzo<s:if test="#varStatusIndirizzo.index != 0">${varStatusIndirizzo.index + 1}</s:if></dt>
		    <s:if test="controlloDifferenzaIndirizzi()"><dd><span style="background-color:yellow"><s:property value="indirizzoFormattato"/></span>&nbsp;</dd></s:if>
		    <s:else><dd><s:property value="indirizzoFormattato"/>&nbsp;</dd></s:else>
	    </s:iterator>
	    <s:iterator value="dettaglioSoggettoMod.contatti" status="varStatusContatto" var="currentContatto">
		    <dt>Recapiti</dt>
		    <s:if test="controlloDifferenzaRecapiti(#varStatusContatto.index)"><dd><span style="background-color:yellow"><s:property value="contattoFormattato"/></span>&nbsp;</dd></s:if>
		    <s:else><dd><s:property value="contattoFormattato"/>&nbsp;</dd></s:else>
	    </s:iterator>
	    
	    <dt>Canale PA / PRIVATI</dt>
	    <s:if test="%{dettaglioSoggetto.canalePA != dettaglioSoggettoMod.canalePA}"><dd><span style="background-color:yellow"><s:property value="%{dettaglioSoggettoMod.canalePA}"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="%{dettaglioSoggettoMod.canalePA}"/>&nbsp;</dd></s:else>
	    <dt>Codice destinatario / IPA</dt>
	    <s:if test="%{dettaglioSoggetto.codDestinatario != dettaglioSoggettoMod.codDestinatario}"><dd><span style="background-color:yellow"><s:property value="%{dettaglioSoggettoMod.codDestinatario}"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="%{dettaglioSoggettoMod.codDestinatario}"/>&nbsp;</dd></s:else>
	    <dt>email PEC</dt>
	    <s:if test="%{dettaglioSoggetto.emailPec != dettaglioSoggettoMod.emailPec}"><dd><span style="background-color:yellow"><s:property value="dettaglioSoggettoMod.emailPec"/></span>&nbsp;</dd></s:if>
	    <s:else><dd><s:property value="dettaglioSoggettoMod.emailPec"/>&nbsp;</dd></s:else>	    
    </dl>           
	</div>
</s:if>	
    
</div>

<!-- Modal rifiuta -->
<a id="linkDialogRifiuta" href="#msgRifiuta" hidden="true" data-toggle="modal"></a>
<div id="msgRifiuta" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgRifiutaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Stai per rifiutare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<s:submit id="submitRifiutaBtn" name="rifiutaSoggetto" value="si, prosegui" cssClass="btn btn-primary" method="rifiutaSoggetto"/>
	</div>
</div>  
 <!--/modale rifiuta -->
 <!-- Modal valida -->
<a id="linkDialogValida" href="#msgValida" hidden="true" data-toggle="modal"></a>
<div id="msgValida" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="msgValidaLabel" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
    		<p><strong>Attenzione!</strong></p>
    		<p>Stai per validare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<s:submit id="submitValidaBtn" name="validaSoggetto" value="si, prosegui" cssClass="btn btn-primary" method="validaSoggetto"/>
	</div>
</div>  
 <!--/modale rifiuta -->

</div>
			<p>
				<s:include value="/jsp/include/indietro.jsp" />
				<span class="nascosto"> &nbsp;|&nbsp;</span>&nbsp;
				<button id="btnRifiuta" class="btn btn-primary">rifiuta</button>
				<span class="nascosto">&nbsp;|&nbsp;</span> &nbsp;
				<button id="btnValida" class="btn btn-primary pull-right">valida</button>
			</p>

		</form>
    </div>
</div>	 
</div>	
<!-- 
=======================================================================
											*****************
											inclusione footer portale
											*****************
=======================================================================
-->

    <!-- Footer
    ================================================== -->
  <!--#include virtual="../../ris/servizi/siac/include/portalFooter.html" -->



  <!--#include virtual="../../ris/servizi/siac/include/javascript.html" -->
<!--
=======================================================================
											*****************
											fine //////inclusione footer portale
											*****************
=======================================================================
-->

</s:form>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnRifiuta").click(function(){
			$("#linkDialogRifiuta").click();
			return false;
		});
		$("#btnValida").click(function(){
			$("#linkDialogValida").click();
			return false;
		});
	});
</script>
<s:include value="/jsp/include/footer.jsp" />