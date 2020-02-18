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
  
   <s:include value="/jsp/include/header.jsp" />
  <!-- NAVIGAZIONE -->
  
  <hr />
<div class="container-fluid-banner">

<a name="A-contenuti" title="A-contenuti"></a>
</div>



<div class="container-fluid">
<div class="row-fluid">

    <div class="span12 contentPage">
    
        <s:form id="nuovoAccertamentoOrdinativo" action="nuovoAccertamentoOrdinativo.do" method="post">
		<!--#include virtual="include/alertErrorSuccess.html" --> 
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<h3>Nuovo accertamento</h3> 
		<div class="step-content"> 
			<div class="step-pane active" id="insLiq">
				<!--#include virtual="include/capitoloAccertamento.html" --> 
	            <div id="refreshHeaderCapitoloOrdinativo">
	            	<s:include value="/jsp/ordinativo/include/headerCapitoloOrdinativo.jsp"/>
	            </div>
	            <s:include value="/jsp/ordinativo/include/capitoloOrdinativo.jsp" />
				<h4>Accertamento</h4> 
				<fieldset class="form-horizontal">
		  
					<div class="control-group">
						<label class="control-label" for="descrizioneAccertamento">Descrizione *</label>
						<div class="controls">    
						  <!-- <textarea rows="1" cols="15" id="descr" class="span9" required ></textarea> -->      
						  <s:textfield id="descrizioneAccertamento" name="model.nuovoAccertamentoModel.descrizioneAccertamento" cssClass="lbTextSmall span9"/>         
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label" for="importoAccertamento">Importo *</label>
						<div class="controls">    
						  <!-- <input id="importo" name="importo" class="lbTextSmall span2" type="text" value="" required /> -->	
						  <s:textfield id="importoAccertamento" name="model.nuovoAccertamentoModel.importoAccertamento" onkeypress="return checkItNumbersCommaAndDotOnly(event)" cssClass="span2 soloNumeri decimale" ></s:textfield>

						</div>
					</div>
				</fieldset> 
				<!--#include virtual="include/datiTransazione.html" -->	
			  	
			  	<%-- <s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
				<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/> --%>
				
				<s:include value="/jsp/include/transazioneElementare.jsp" />	  
			  
			</div>
		</div>
		
       	<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
		
		<p class="margin-medium">
			<!-- <a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a> -->
			<%-- <s:include value="/jsp/include/indietro.jsp" /> --%>
			<s:submit cssClass="btn" method="indietroDaAccertamento" value="indietro" name="indietro" />   
			<a class="btn btn-secondary" href="">annulla</a>    
			<!-- <a class="btn btn-primary pull-right" type="button" value="" href="FIN-InsOrdinativoStep2.shtml">salva</a> -->
			<s:submit cssClass="btn btn-primary pull-right" method="salva" value="salva" name="salva" />  
		</p>     
		<!--#include virtual="include/modal.html" --> 
      </s:form>
    </div>
</div>	 
</div>	
<script type="text/javascript">
	
		$(document).ready(function() {
			
	
		});

		$('#linkCompilazioneGuidataCapitolo').hide();
		
	</script>			
	
<s:include value="/jsp/include/footer.jsp" />