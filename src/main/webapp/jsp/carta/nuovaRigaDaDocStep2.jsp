<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

 <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
   	<s:include value="/jsp/include/javascriptTree.jsp" />
    
</head>

<body>                       
	<!-- NAVIGAZIONE -->
	<s:include value="/jsp/include/header.jsp" />  	
	<%-- SIAC-7952 rimuovo .do dalla action --%>
	<s:form id="mainForm" method="post" action="nuovaRigaDaDocumentiStep2">
	<!-- /NAVIGAZIONE -->
	<hr />
<div class="container-fluid-banner">
	<a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage"> 
			<%-- SIAC-7952 rimuovo .do dalla action --%>
			<form method="post" action="nuovaRigaDaDocumentiStep2">  
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />	   
				<h3><s:property value="model.titoloStep"/></h3>
				
				<div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1"><span class="badge badge-success">1</span>Ricerca documenti<span class="chevron"></span></li>
						<li data-target="#step2" class="active"><span class="badge">2</span>Associa documenti<span class="chevron"></span></li>
					</ul>
				</div>
				
				<div class="step-content">
					<div class="step-pane active" id="step1">
				
				<p><!--task-131 <s:submit name="inserisci in carta (selezionati)" value="inserisci in carta (selezionati)" method="inserisciInCarta" cssClass="btn btn-primary pull-right" /> -->
					<s:submit name="inserisci in carta (selezionati)" value="inserisci in carta (selezionati)" action="nuovaRigaDaDocumentiStep2_inserisciInCarta" cssClass="btn btn-primary pull-right" />
				</p>
				<h4>Documenti da collegare</h4>
					
				<fieldset class="form-horizontal">
					
					<input id="selectall" type="checkbox">&nbsp;<b>Seleziona tutto</b>

                    <display:table name="elencoSubdocumentoSpesa" 
									class="table table-hover tab_left" 
									summary="riepilogo carte contabili" 
									pagesize="10" requestURI="nuovaRigaDaDocumentiStep2.do" 
									partialList="true" size="resultSize"
									uid="ricercaSubDocPerNuovaRigaCartaID" keepStatus="true" clearStatus="${clearPagina}">
									
						   <display:column class="ckRigaClass">
							    <s:checkboxlist id="ckRiga" list="%{#attr.ricercaSubDocPerNuovaRigaCartaID.uid}"  name="ckRigaSelezionata" theme="displaytag" />
				       	   </display:column>
							
							
							<display:column title="Documento">
								<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.anno}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.tipoDocumento.codice}"/>
								/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.numero}"/>
							</display:column>	 
							
							<display:column title="Data" property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>	 
							
							<display:column title="Stato">
								<a href="#" data-trigger="hover" rel="popover" title="Stato" data-content="<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.statoOperativoDocumento.descrizione}"/>">
									<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.statoOperativoDocumento.codice}"/>
								</a>
							</display:column>
							
							
							
							<display:column title="Soggetto"><s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.soggetto.denominazione}"/></display:column>
							
							<display:column title="Quota" property="numero"></display:column>
							
							<display:column title="Impegno" >
								 <s:if test="%{#attr.ricercaSubDocPerNuovaRigaCartaID.subImpegno == null}">
	 	     						<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.annoMovimento}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.numero.intValue()}"/>
	  							 </s:if>
								  <s:else>
								 	<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.annoMovimento}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.numero.intValue()}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.subImpegno.numero.intValue()}"/>
								 </s:else>
							</display:column>
							
							<display:column title="Importo" property="importo"  		
									decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
							 	
									
					</display:table>				



				
				</fieldset>
				
				</div>
			   </div>


			 
				<p class="margin-medium">
					<!--task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary"/> -->
					<s:submit name="indietro" value="indietro" action="nuovaRigaDaDocumentiStep2_indietro" cssClass="btn btn-secondary"/>
					<span class="pull-right">
						<!--task-131 <s:submit name="inserisci in carta (selezionati)" value="inserisci in carta (selezionati)" method="inserisciInCarta" cssClass="btn btn-primary" /> -->
						<s:submit name="inserisci in carta (selezionati)" value="inserisci in carta (selezionati)" action="nuovaRigaDaDocumentiStep2_inserisciInCarta" cssClass="btn btn-primary"/>
					</span>
				</p>       
				
			  
			</form>
		  
		</div>
	</div>	 
</div>

</s:form>


 <script type="text/javascript">
 
 
 
	
   $(document).ready(function() {		
   
	      // add multiple select / deselect functionality
		   $("#selectall").click(function(){
			   
			   $('[id^=ckRiga]').attr("checked", this.checked );
		   });
	      
			// if all checkbox are selected, check the selectall checkbox
		   // and viceversa
		   $('[id^=ckRiga]').click(function(){

		       if($('[id^=ckRiga]').length == $("[id^=ckRiga]:checked").length) {
		           $("#selectall").attr("checked", "checked");
		       } else {
		           $("#selectall").removeAttr("checked");
		       }

		   });
		   
		
	});
   
</script>
	
	
	
<s:include value="/jsp/include/footer.jsp" />
