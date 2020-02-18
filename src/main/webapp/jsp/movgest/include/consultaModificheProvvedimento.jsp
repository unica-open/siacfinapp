<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

 

  <div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Elenco storico provvedimenti</h4> 
  </div>    
  <div class="modal-body">
	<fieldset class="form-horizontal">    
	 <%-- <s:if test="storicoModificheProvvedimentoTrovato"> --%>
        
        
        <display:table id="tabellaConsultaStorico" name="listaModificheProvvedimento"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="elenco provvedimenti"
					 uid="consultaProvvedimentoID">

	      <display:column title="Anno"><s:property value="%{#attr.consultaProvvedimentoID.anno}" /></display:column>
	      <display:column title="Numero"><s:property value="%{#attr.consultaProvvedimentoID.numero}" /></display:column>
	      <display:column title="Tipo">
	      	<s:property value="%{#attr.consultaProvvedimentoID.tipoAtto.codice}" /> - <s:property value="%{#attr.consultaProvvedimentoID.tipoAtto.descrizione}" />
	      </display:column>
	      <display:column title="Oggetto">${attr.consultaProvvedimentoID.oggetto}</display:column>
		  <display:column title="<abbr title='Struttura Amministrativa Responsabile'>Strutt Amm Resp</abbr>">
	      	<a href="#" data-trigger="hover" data-placement="left" rel="popover" title="Descrizione" data-content="${attr.consultaProvvedimentoID.strutturaAmmContabile.descrizione}">
				<s:property value="%{#attr.consultaProvvedimentoID.strutturaAmmContabile.codice}"/>
			</a>
	      </display:column>
	      <display:column title="Data cancellazione">
		 		<s:property value="%{#attr.consultaProvvedimentoID.dataCancellazione}" /></display:column>
	     
	    </display:table>
      <%-- </s:if> --%>  
	</fieldset>
</div>     
<div class="modal-footer">
	<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>
     
<script type="text/javascript">

	$(document).ready(function() {

		$("a[rel=popover]").popover();
	});
	
</script> 