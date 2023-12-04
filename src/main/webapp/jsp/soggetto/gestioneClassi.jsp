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
   
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />

  
<div class="container-fluid-banner">


<a name="A-contenuti" title="A-contenuti"></a>
</div>

    <div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">

<%-- SIAC-7952 rimuovo .do dalla action --%>
<s:form id="gestioneClassi" action="gestioneClassi" method="post">

	<div class="contentPage">
		
		<s:if test="hasActionErrors() and uidClasseDaRimuovere != 0">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionerror escape="false"/>					
				</ul>
			</div>
		</s:if>	
	
		<s:if test="hasActionMessages() and uidClasseDaRimuovere != 0">
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionmessage escape="false"/>
				</ul>
			</div>
		</s:if>


		<!-- elencoGestioneClasse starts  here  -->
		<s:include value="/jsp/soggetto/include/elencoGestioneClasse.jsp" />
		<!-- elencoGestioneClasse ends here  -->
	</div>

<!--  AGGIUNGERE UNA CLASSE: -->

				<div class="contentPage">
				
					<h3>Aggiungi/Modifica classe</h3>
					
					<%-- Messaggio di ERROR --%>
					<s:if test="hasActionErrors() and uidClasseDaRimuovere == 0">
						<div class="alert alert-error">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<strong>Attenzione!!</strong><br>
							<ul>
								<s:actionerror escape="false"/>
							</ul>
						</div>
					</s:if>	
	
					<s:if test="hasActionMessages() and uidClasseDaRimuovere == 0">
						<%-- Messaggio di WARNING --%>
						<div class="alert alert-warning">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<strong>Attenzione!!</strong><br>
							<ul>
								<s:actionmessage escape="false"/>
							</ul>
						</div>
					</s:if>
					<!-- campi per modifica/inserimento classi -->
					
					<fieldset class="form-horizontal">
					  <s:hidden name='classeSelezionata.id'></s:hidden>
					  <div class="control-group">
					    <label class="control-label" for="classeSelCod">Codice </label>
					    <div class="controls">
							<s:textfield id="classeSelCod" name="classeSelezionata.codice" cssClass="span2" readonly='%{editMode}'/>
					    </div>
					  </div>
					  <div class="control-group">
					    <label class="control-label" for="classeSelDescr">Descrizione</label>
					    <div class="controls">
							<s:textfield id="classeSelDescr" name="classeSelezionata.descrizione" cssClass="span2" />
					    </div>
					  </div>
					  
					  
					</fieldset>

					<div class="Border_line"></div>
					<p>
						<s:if test="editMode">
							<!-- task-131 <s:submit name="modificaClasse" value="Salva classe" method="modificaClasse" cssClass="btn btn-primary pull-left" /> -->
							<!-- task-131 <s:submit name="annullaClasse" value="Annulla" method="annullaModifica" cssClass="btn pull-left" /> -->
							<s:submit name="modificaClasse" value="Salva classe" action="gestioneClassi_modificaClasse" cssClass="btn btn-primary pull-left" />
							<s:submit name="annullaClasse" value="Annulla" action="gestioneClassi_annullaModifica" cssClass="btn pull-left" />
						</s:if>
						<s:else>
							<!-- task-131 <s:submit name="aggiungiClasse" value="Crea classe" method="aggiungiClasse" cssClass="btn btn-primary pull-left" /> -->
							<s:submit name="aggiungiClasse" value="Crea classe" action="gestioneClassi_aggiungiClasse" cssClass="btn btn-primary pull-left" />
						</s:else>
					</p>
					<br/>
				</div>
					
<!--  END AGGIUNGERE UN SOGGETTO -->

</s:form>

</div>	
</div>	 
</div>	
<script type="text/javascript">
$(document).ready(function() {
    // sul click enter scatta la ricerca	
	$(document).keypress(function( e) {
	    if(e.which == 13) {
	        $("#cerca").click();
	    }
	});

    if ( <s:property value='editMode'/> ) {
    	$('div#mytable').block({ 
    		message: null, 
    		overlayCSS:  { 
            	backgroundColor: '#fafafa', 
            	opacity:         0.5, 
            	cursor:          'not-allowed' 
        	}
    	});		
    } else {
    	$('div#mytable').unblock({ message: null });
    } 
});

</script>



<s:include value="/jsp/include/footer.jsp" />
