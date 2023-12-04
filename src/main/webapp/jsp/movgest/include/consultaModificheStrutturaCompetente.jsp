<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

 

  <div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Elenco storico Strutture Competenti</h4> 
  </div>    
  <div class="modal-body">
	<fieldset class="form-horizontal">    
	 
        
        
        <display:table id="tabellaConsultaStorico" name="listaModificheStruttureCompetenti"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="elenco strutture competenti"
					 uid="consultaStruttureCompetentiID">

	      <display:column title="Nome Strutture Competenti"><s:property value="%{#attr.consultaStruttureCompetentiID.codice}" /> - <s:property value="%{#attr.consultaStruttureCompetentiID.descrizione}" /></display:column>
	      <display:column title="Data cancellazione">
		 		<s:property value="%{#attr.consultaStruttureCompetentiID.dataCancellazione}" /></display:column>
	     
	    </display:table>
    
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