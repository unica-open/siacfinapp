<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

 
<div class="container-fluid">
	<div class="row-fluid">
				<div class="span12 ">

						<div class="contentPage">  
						   
						   <h3>Soggetti associati alla classe</h3>
						   <p>Da questo elenco puoi rimuovere soggetti dalla classe:</p>  
						   <display:table name="sessionScope.RISULTATI_RICERCA_SOGGETTI"  
						                 class="table tab_left table-hover" 
						                 summary="riepilogo soggetti"
						                 pagesize="10"
						                 partialList="true" size="resultSizeSoggettiDellaClasse"
						                 requestURI="gestioneClassiSoggetto.do"
										 uid="ricercaSoggettoID" keepStatus="true" clearStatus="${statusTabellaSoggettiDellaClasse}" >
							        <display:column title="Codice" property="codiceSoggettoNumber"  />
							        <display:column title="Codice Fiscale" property="codiceFiscale" />
							        <display:column title="Partita IVA" property="partitaIva"/>
							        <display:column title="Denominazione" property="denominazione" />
							        <display:column title="Stato" property="statoOperativo" />
							        
							        <display:column title="" class="tab_Right">
								    	<div class="btn-group">
								    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
								    		<ul class="dropdown-menu pull-right" id="ul_action">
								    		
												<li><a id="linkRimuovi_<s:property value="%{#attr.ricercaSoggettoID.uid}"/>_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggettoNumber.intValue()}"/>" href="#msgRimuovi" data-toggle="modal" class="linkRimuoviSoggetto"> 
						                         rimuovi</a>
						                         </li> 
						                         
								    		</ul>
										</div>
									</display:column>
							        
							         
							        
							</display:table>
							
							<s:hidden id="uidSoggettoDaRimuovere" name="uidSoggDaRimuovere"/>
					        <s:hidden id="codiceSoggettoDaRimuovere" name="codiceSoggDaRimuovere"/>
							
						    
						    <div class="Border_line"></div>  
						  	                                 
						</div>	
				</div>	
	</div>	 
</div>	


<!-- Modale rimuovi soggetto -->
  <div id="msgRimuovi" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgRimuoviLabel" aria-hidden="true">
    <div class="modal-body">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p><strong>Attenzione!</strong></p>
        <p>Stai per rimuovere il soggetto selezionato dalla classe, sei sicuro di voler proseguire?</p>
      </div>
    </div>
    <div class="modal-footer">
      <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
      <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="rimuoviSoggettoDaClasse"/>
    </div>
  </div>  
 <!--/modale annulla -->

<script type="text/javascript">
	$(document).ready(function() {
		$(".linkRimuoviSoggetto").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			
			if (supportId != null && supportId.length > 0) {
// 				alert(supportId[1]);
// 				alert(supportId[2]);
				$("#uidSoggettoDaRimuovere").val(supportId[1]);
				$("#codiceSoggettoDaRimuovere").val(supportId[2]);
			}
		});
		
	});
</script>