<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="oggettoDaPopolarePagamento()">
		<p>
			<!-- task-131 <s:submit name="annulla inserimento" value="annulla inserimento" method="pulisciProvvisorio" cssClass="btn btn-secondary" /> -->
			<s:submit name="annulla inserimento" value="annulla inserimento" action="gestioneOrdinativoIncassoStep3_pulisciProvvisorio" cssClass="btn btn-secondary" />
						  	
			<span class="pull-right"> 
				  <!-- task-131 <s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" method="inserisciProvvisorio"  cssClass="btn btn-primary pull-right" />-->
				  <s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" action="gestioneOrdinativoPagamentoStep3_inserisciProvvisorio"  cssClass="btn btn-primary pull-right" />
			</span> 
		</p>
</s:if>
<s:else>

		<p>
			<!-- task-131 <s:submit name="annulla inserimento" value="annulla inserimento" method="pulisciProvvisorio" cssClass="btn btn-secondary" /> -->
			<s:submit name="annulla inserimento" value="annulla inserimento" action="gestioneOrdinativoIncassoStep3_pulisciProvvisorio" cssClass="btn btn-secondary" />
						  	
			<span class="pull-right"> 
				<s:if test="disabilitaInserisciProvvisorio()">
           			<!-- task-131 <s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" method="inserisciProvvisorio" disabled="true" cssClass="btn btn-primary pull-right" /> -->
      			 	<s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" action="gestioneOrdinativoIncassoStep3_inserisciProvvisorio" disabled="true" cssClass="btn btn-primary pull-right" />
      			 </s:if>
      			 <s:else>
      			 	<!-- task-131 <s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" method="inserisciProvvisorio"  cssClass="btn btn-primary pull-right" /> -->
      				<s:submit name="Inserisci provvisorio" value="Inserisci provvisorio" action="gestioneOrdinativoIncassoStep3_inserisciProvvisorio" cssClass="btn btn-primary pull-right" />	
      			 </s:else>
				  
			</span> 
		</p>
       

 


</s:else>