<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

						   <h3>Anagrafiche Classi di Soggetto</h3>
						   <p>L'elenco mostra le anagrafiche delle classi di soggetto, &egrave; possibile eliminare e/o modificare record</p>
						     <!-- 
						      -->
						   <display:table name="listaClasse"  
						                 class="table tab_left table-hover" 
						                 summary="riepilogo classi"
						                 pagesize="10"
						                 partialList="true" size="totalSizeElencoClassi"
						                 requestURI="gestioneClassi.do"
						                 excludedParams="*"
										 uid="ricercaClasseID" keepStatus="false" clearStatus="${statusTabellaClasse}" >
										 
							        <display:column title="Codice" property="codice"  />
							        <display:column title="Descrizione" property="descrizione"  />
 							        
							        <display:column title="" class="tab_Right">
								    	<div class="btn-group">
								    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
								    		<ul class="dropdown-menu pull-right" id="ul_action">
								    		
												<li>
													<a id="linkRimuovi_<s:property value="%{#attr.ricercaClasseID.uid}"/>_<s:property value="%{#attr.ricercaClasseID.id}"/>" href="#msgRimuovi" data-toggle="modal" class="linkRimuoviClasse"> 
						                         	rimuovi</a>
						                         </li>
						                         <li>
						                         	<s:url id='urlModifica' action="gestioneClassi" method='goEditMode'>
						                         		<s:param name="uidClasseDaModificare" value="%{#attr.ricercaClasseID.uid}" />
						                         	</s:url>
						                         	<s:a href="%{urlModifica}">modifica</s:a>
<%--						                         	
													<a id="linkModifica_<s:property value="%{#attr.ricercaClasseID.uid}"/>_<s:property value="%{#attr.ricercaClasseID.id}"/>" href="#msgModifica" class="linkModificaClasse">modifica</a>
--%>													
						                         </li> 						                         
								    		</ul>
										</div>
									</display:column>
							         
							        
							</display:table>
							<!--  chiude un div generato da display:table ? -->
							</div>
							
							<s:hidden id="uidClasseDaRimuovere" name="uidClasseDaRimuovere"/>
					        <s:hidden id="codiceClasseDaRimuovere" name="codiceClasseDaRimuovere"/>

						    <div class="Border_line"></div>

<!-- Modale rimuovi soggetto -->
  <div id="msgRimuovi" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgRimuoviLabel" aria-hidden="true">
    <div class="modal-body">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p><strong>Attenzione!</strong></p>
        <p>Stai per eliminare la classe, sei sicuro di voler proseguire?</p>
      </div>
    </div>
    <div class="modal-footer">
      <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
      <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="rimuoviClasse"/>
    </div>
  </div>
  
    
 <!--/modale annulla -->

<script type="text/javascript">
	$(document).ready(function() {
		$(".linkRimuoviClasse").click(function() {
			var supportId = $(this).attr("id").split("_"); 

			if (supportId != null && supportId.length > 0) {
// 				alert(supportId[1]);
// 				alert(supportId[2]);
				$("#uidClasseDaRimuovere").val(supportId[1]);
				$("#codiceClasseDaRimuovere").val(supportId[2]);
			}
		});
	});
</script>