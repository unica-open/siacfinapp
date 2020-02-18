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

<div class="container-fluid">
<div class="row-fluid">
    <div class="span12 contentPage">
    
	<s:form method="post" action="aggiornaSediSecondarie.do">
   
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
		<li><s:a action="aggiornaSoggetto" method="doExecute">Soggetto</s:a></li>
		<li class="active"><a href="#">Sedi Secondarie</a></li>
		<li><s:a action="modalitaPagamentoSoggetto">Modalit&agrave; pagamento</s:a></li>
	</ul>
   
    <div class="tab-content">
		<div class="tab-pane active" id="sedeSecondaria">
			<div class="span6"> 
				<s:hidden id="idSedeSecondariaValidazione" name="idSedeSecondariaValidazione"/>
				<s:hidden id="codiceSedeSecondariaInValidazione" name="codiceSedeSecondariaInValidazione"/>
				
			    
			     <s:if test="selectedSedeMod == null">
    				<h4>Da validare</h4>
    			 </s:if>
			     <s:if test="selectedSedeMod != null">
    				<h4>Attualmente presente</h4>
    			 </s:if>
			    
			    
				<dl class="dl-horizontal well">
					<dt>Denominazione</dt>
					<dd><s:property value="selectedSede.denominazione" />&nbsp;</dd>
					<dt>Indirizzo</dt>
					<dd>
						<s:property value="selectedSede.indirizzoSoggettoPrincipale.sedime" />
						<s:property value="selectedSede.indirizzoSoggettoPrincipale.denominazione" />
						<s:property value="selectedSede.indirizzoSoggettoPrincipale.numeroCivico" />
						&nbsp;
					</dd>
					<dt>Comune</dt>
					<dd><s:property value="selectedSede.indirizzoSoggettoPrincipale.comune" />&nbsp;</dd>
					<dt><acronym title="codice di avviamento postale">C.A.P.</acronym></dt>
					<dd><s:property value="selectedSede.indirizzoSoggettoPrincipale.cap" default="-" /></dd>
					<dt>Telefono</dt>
					<dd><s:property value="getDettaglioContatto('telefono', false)" default="-" /></dd>
					<dt>Cellulare</dt>
					<dd><s:property value="getDettaglioContatto('cellulare', false)" default="-" /></dd>
					<dt>Fax</dt>
					<dd><s:property value="getDettaglioContatto('fax', false)" default="-" /></dd>
					<dt>Sito web</dt>
					<dd><s:property value="getDettaglioContatto('sito', false)" default="-" /></dd>
					<dt>Nome contatto</dt>
					<dd><s:property value="getDettaglioContatto('soggetto', false)" default="-" /></dd>
					<dt>E-mail</dt>
					<dd><s:property value="getDettaglioContatto('email', false)" default="-" /></dd>
					<dt><acronym title="posta elettronica certificata">PEC</acronym></dt>
					<dd><s:property value="getDettaglioContatto('PEC', false)" default="-" /></dd>
				</dl>
			</div>
			<s:if test="selectedSedeMod!=null">
			<!-- Se non c'e' il mod non visualizzo questo lato della pagina -->
				<div class="span6"> 
					<h4>Da validare</h4>
					<dl class="dl-horizontal well" style="background-color: transparent;">
						<dt>Denominazione</dt>
					<s:if test="selectedSedeMod.denominazione != selectedSede.denominazione">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="selectedSedeMod.denominazione" />&nbsp;
						</dd>
						<dt>Indirizzo</dt>
					<s:if test="selectedSedeMod.indirizzoSoggettoPrincipale.stringCompare != selectedSede.indirizzoSoggettoPrincipale.stringCompare">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="selectedSedeMod.indirizzoSoggettoPrincipale.sedime" />
							<s:property value="selectedSedeMod.indirizzoSoggettoPrincipale.denominazione" />
							<s:property value="selectedSedeMod.indirizzoSoggettoPrincipale.numeroCivico" />
							&nbsp;
						</dd>
						<dt>Comune</dt>
					<s:if test="selectedSedeMod.indirizzoSoggettoPrincipale.comune != selectedSede.indirizzoSoggettoPrincipale.comune">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="selectedSedeMod.indirizzoSoggettoPrincipale.comune" />&nbsp;
						</dd>
						<dt><acronym title="codice di avviamento postale">C.A.P.</acronym></dt>
					<s:if test="selectedSedeMod.cap != selectedSede.cap">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="selectedSedeMod.indirizzoSoggettoPrincipale.cap" default="-" />
						</dd>
						<dt>Telefono</dt>
					<s:if test="getDettaglioContatto('telefono', false) != getDettaglioContatto('telefono', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('telefono', true)" default="-" />
						</dd>
						<dt>Cellulare</dt>
					<s:if test="getDettaglioContatto('cellulare', false) != getDettaglioContatto('cellulare', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('cellulare', true)" default="-" />
						</dd>
						<dt>Fax</dt>
					<s:if test="getDettaglioContatto('fax', false) != getDettaglioContatto('fax', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('fax', true)" default="-" />
						</dd>
						<dt>Sito web</dt>
					<s:if test="getDettaglioContatto('sito', false) != getDettaglioContatto('sito', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('sito', true)" default="-" />
						</dd>
						<dt>Nome contatto</dt>
					<s:if test="getDettaglioContatto('soggetto', false) != getDettaglioContatto('soggetto', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('soggetto', true)" default="-" />
						</dd>
						<dt>E-mail</dt>
					<s:if test="getDettaglioContatto('email', false) != getDettaglioContatto('email', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('email', true)" default="-" />
						</dd>
						<dt><acronym title="posta elettronica certificata">PEC</acronym></dt>
					<s:if test="getDettaglioContatto('PEC', false) != getDettaglioContatto('PEC', true)">
						<dd style="background-color: yellow;">
					</s:if>
					<s:else>
						<dd>
					</s:else>
							<s:property value="getDettaglioContatto('PEC', true)" default="-" />
						</dd>
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
		    		<p>Stai per rifiutare le modifiche apportate, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
				<s:submit id="submitRifiutaBtn" name="rifiutaSede" value="si, prosegui" cssClass="btn btn-primary" method="rifiutaSede"/>
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
		    		<p>Stai per validare le modifiche apportate, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
				<s:submit id="submitValidaBtn" name="validaSede" value="si, prosegui" cssClass="btn btn-primary" method="validaSede"/>
			</div>
		</div>  
		<!--/modale rifiuta -->
	</div>
	
	<p>
		<s:a action="aggiornaSediSecondarie" cssClass="btn">Indietro</s:a>
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