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
  
  <!-- NAVIGAZIONE -->
  
<div class="container-fluid-banner">





<a name="A-contenuti" title="A-contenuti"></a>
</div>


    <div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    	<%-- SIAC-7952 rimuovo .do dalla action --%>
        <s:form id="aggiornaRecapiti" action="aggiornaRecapiti" method="post">

<s:include value="/jsp/include/actionMessagesErrors.jsp" />
        
         <ul class="nav nav-tabs">
		    <li class="active"><a href="#">Soggetto</a></li>
		    <li><s:a action="aggiornaSediSecondarie">Sedi Secondarie</s:a></li>
		    <li><s:a action="modalitaPagamentoSoggetto">Modalit&agrave; pagamento</s:a></li>
	    </ul>
     
              <div id="MyWizard" class="wizard">	
					<ul class="steps">
						<li data-target="#step1" class="complete"><span class="badge  badge-success">1</span>Anagrafica<span class="chevron"></span></li>
						<li data-target="#step2" class="active"><span class="badge  badge-success">2</span>Recapiti<span class="chevron"></span></li>
						<!--<li data-target="#step3"><span class="badge">3</span>Salva<span class="chevron"></span></li> -->
						<!--<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
					</ul>
				</div>
				<div class="step-content">
					<div class="step-pane active" id="step2">

		<h3>Codice Soggetto: <s:property value="dettaglioSoggetto.codiceSoggetto" /> 
		-  <s:property value="dettaglioSoggetto.denominazione" /> 
		 (<s:property value="dettaglioSoggetto.statoOperativo" /> dal <s:property value="%{dettaglioSoggetto.dataValidita}" />) </h3>     

        <!--#include virtual="sogg_contatti.html" -->	
        
        <s:set var="salvaRecapitoAction" value="%{'aggiornaRecapiti_salvaRecapito'}" />	          
	    <s:set var="pulisciContattoAction" value="%{'aggiornaRecapiti_pulisciContatto'}" />	          
	    <s:set var="pulisciIndirizziAction" value="%{'aggiornaRecapiti_pulisciIndirizzi'}" />	          
	    <s:set var="salvaIndirizzoAction" value="%{'aggiornaRecapiti_salvaIndirizzo'}" />	
	    <s:set var="aggiornaRecapitiUrl" value="%{'aggiornaRecapiti'}"/>          
	     
         <s:include value="/jsp/soggetto/include/recapiti.jsp" >
         	<s:param name="aggiornaRecapitiUrl" value="#aggiornaRecapitiUrl"/>
       </s:include>  	
                                          
             <p>  
             
                <s:include value="/jsp/include/indietro.jsp" />  
             
            <!--   <a class="btn btn-link" href="">annulla</a> -->
              <!-- task-131 <s:submit name="annulla" value="annulla" method="annullaRecapito" cssClass="btn"  /> --> 
              <!-- task-131 <s:submit name="salva" value="salva" method="salva" id="salvaId" cssClass="btn btn-primary pull-right"  /> -->   
              <s:submit name="annulla" value="annulla" action="aggiornaRecapiti_annullaRecapito" cssClass="btn"  /> 
              <s:submit name="salva" value="salva" action="aggiornaRecapiti_salva" id="salvaId" cssClass="btn btn-primary pull-right"  />   
              
              </p>
             </div>
				</div>
      </s:form>
    </div>
</div>	 
</div>	
	<script type="text/javascript">
		 $(document).ready(function() {
			// blocca pagina su salva 
			$('#salvaId').click(function() { 
				bloccaPagina('#salvaId');
			}); 
			
			
			
			 
			 
			$("a.aggiornaIndirizzo").click(function() 
			{ 	
				$("#idIndirizzo").val($(this).data('indirizzo_id'));
				$("#tipoSede").val($(this).data('tipo_indirizzo'));
			 	$("#ckPrincipale").attr("checked", true === $(this).data('principale'));
			 	$("#ckAvviso").attr("checked", true === $(this).data('avviso')); 	
				$("#idNazione").val($(this).data('codice_nazione'));
			 	$("#comune").val($(this).data('comune'));
			 	$("#comuneId").val($(this).data('id_comune'));
			 	$("#sedimiList").val($(this).data('sedime'));
			 	$("#nomeVia").val($(this).data('denominazione'));
			 	$("#numciv").val($(this).data('numero_civico'));
			 	$("#cap").val($(this).data('cap'));
			 	
			 	$("#annullaInserimentoIndirizzo").html("annulla aggiornamento");
			 	$("#aggiornaRecapiti_salvaIndirizzo").val("aggiorna");
			 	
			 	$("#pressedButton").val("aggiornaIndirizzo");
			 });
			
			
			copiaNazioneInComune();
			
			
			
			
		 }); 
		 
		 
		 function copiaNazioneInComune()
		 {
			$("#idNazione").change(function() {
				$("#comune").val($("option:selected", this).text()).trigger({type: 'keydown', keyCode: 1, which: 1, charCode: 1});
			});
		 }
		 
		 
	</script>
<s:include value="/jsp/include/footer.jsp" />
