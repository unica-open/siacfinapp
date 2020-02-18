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
  	<%-- <s:include value="/jsp/include/javascriptTree.jsp" /> --%>
  </head>
  
  <body>     
  
  <s:include value="/jsp/include/header.jsp" />
                    
	<!-- NAVIGAZIONE
	<p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
	<ul id="sommario" class="nascosto">
		<li><a href="#A-contenuti">Salta ai contenuti</a></li>
	</ul>
	/NAVIGAZIONE -->
	<hr />

<div class="container-fluid-banner">

	<a name="A-contenuti" title="A-contenuti"></a>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">    
				
				<!-- <form class="form-horizontal"> -->
       			<s:form id="gestioneCartaStep2" action="gestioneCartaStep2.do" method="post">
					<!--#include virtual="include/alertErrorSuccess.html" -->
		 			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 			
					<!-- <h3>Inserimento carta contabile</h3> -->
					<h3><s:property value="model.titoloStep"/></h3>
					
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Dati testata<span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>Dettaglio<span class="chevron"></span></li>
						</ul>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">  
							
							<h4 class="nostep-pane">Importo Carta: <s:property value="getText('struts.money.format', {sommaImportiRighe()})"/>
							<s:if test="optionsPagamentoEstero">
							 - Valuta Estera: <s:property value="codiceDivisa" />  <s:property value="getText('struts.money.format', {sommaImportiRigheValutaEstera()})"/>
							 </s:if>
							 </h4>
							<h4 class="step-pane"><s:property value="{numeroRighe()}"/> righe</h4>						
							
		<display:table name="listaRighe"  
			requestURI="gestioneCartaStep2.do"
			pagesize="10" 
			class="table table-hover tab_left" 
			summary="riepilogo righe carta" 
			uid="righeCartaId" >
		              
		        <display:column title="Numero" property="numero" />	
		        <display:column title="Conto del tesoriere" property="contoTesoreria.codice" />	
				<display:column title="Descrizione" property="descrizione" />	 
				<display:column title="Impegno" >
			        
			        <s:if test="(#attr.righeCartaId.impegno!=null)">	
				        <s:if test="(#attr.righeCartaId.impegno.elencoSubImpegni.get(0)!=null)">	
				        	<s:property value="%{#attr.righeCartaId.impegno.annoMovimento}" /> / <s:property value="%{#attr.righeCartaId.impegno.numero.intValue()}" /> / <s:property value="%{#attr.righeCartaId.impegno.elencoSubImpegni.get(0).numero.intValue()}" />			        	
				        </s:if>
				        <s:else>
				        	<s:property value="%{#attr.righeCartaId.impegno.annoMovimento}" /> / <s:property value="%{#attr.righeCartaId.impegno.numero.intValue()}" />
				        </s:else>
			        </s:if>
			        	
				</display:column> 
				<display:column title="Soggetto">	
					<s:property value="%{#attr.righeCartaId.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.righeCartaId.soggetto.denominazione}" /> 
				</display:column> 
		      
		      
		        <display:column title="Documento/Quota" >	
			        <s:if test="(#attr.righeCartaId.listaSubDocumentiSpesaCollegati!=null)">
			        	 <s:if test="(#attr.righeCartaId.listaSubDocumentiSpesaCollegati.get(0)!=null)">
			        	 	<s:if test="(#attr.righeCartaId.listaSubDocumentiSpesaCollegati.size()==1)">
				        	 	<s:property value="%{#attr.righeCartaId.listaSubDocumentiSpesaCollegati.get(0).documento.anno}" />/
					        	<s:property value="%{#attr.righeCartaId.listaSubDocumentiSpesaCollegati.get(0).documento.tipoDocumento.codice}" />/
					        	<s:property value="%{#attr.righeCartaId.listaSubDocumentiSpesaCollegati.get(0).documento.numero}" /> -
					        	<s:property value="%{#attr.righeCartaId.listaSubDocumentiSpesaCollegati.get(0).numero}" />
			        	 	</s:if>
			        	 	<s:else>
			        	 		collegate <s:property value="%{#attr.righeCartaId.listaSubDocumentiSpesaCollegati.size()}" /> quote documenti
			        	 	</s:else>
			        	 </s:if>
			        	<s:else>
			        		-
			        	</s:else>
			        </s:if>
			        <s:else>
			        	-
			        </s:else>
		        </display:column> 
		        
				<display:column title="Importo" property="importo"  
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
                      
				<display:column title="">
			    	<div class="btn-group">
			    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
			    		<ul class="dropdown-menu pull-right">		                 
																																	
								<s:if test="aggiornamento">
									<s:if test="!disableAggiornaRiga(#attr.righeCartaId.numero)">	
										<li><a href="aggiornaRigaDaMovimento.do?rigaId=${righeCartaId.numero}">aggiorna</a></li>
									</s:if>
								</s:if>
								<s:else>
									<li><a id="linkAggiornaRiga_<s:property value="%{#attr.righeCartaId.importo}"/>_<s:property value="%{#attr.righeCartaId.descrizione}"/>_<s:property value="%{#attr.righeCartaId.contoTesoreria.codice}"/>_<s:property value="%{#attr.righeCartaId.note}"/>_<s:property value="%{#attr.righeCartaId.dataEsecuzioneRiga}"/>_<s:property value="%{#attr.righeCartaId.numero}"/>" href="#aggCartaContabile" data-toggle="modal" class="linkAggiornaRiga">aggiorna</a></li>
								</s:else>
															
								<s:if test="!disableStep2()">
									<li><a id="linkEliminaRiga_<s:property value="%{#attr.righeCartaId.numero}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaRiga">elimina</a></li>								
								</s:if>
								
			    		</ul>
					</div>
				 </display:column>
		        
		        
		        <display:footer>
						<tr class="newline">
							<th scope="col" colspan="6">Totali righe</th>
							<th scope="col"><s:property value="getText('struts.money.format', {sommaImportiRighe()})"/>&nbsp;</th>
							<th scope="col">&nbsp;</th>
                		</tr>
				</display:footer>     
		</display:table>               
							
								
								<p>
									<a id="btnNuovaRigaDaMov" class="btn btn-secondary" href="nuovaRigaDaMovimento.do">nuova riga da movimenti</a>
							<!--	<a class="btn btn-secondary" href="FIN-insRigaMov.shtml">nuova riga da movimenti</a> -->
									
									<s:if test="!optionsPagamentoEstero">
										<s:submit name="nuovarigadadocumenti" value="nuova riga da documenti" method="nuovaRigaDaDocumenti" cssClass="btn btn-secondary" />
									</s:if>	
								</p>
							
							</fieldset>
							
						</div>
					</div>
					
					<!-- Modal aggCartaContabile-->
					<s:hidden id="numeroRigaInAggiornamento" name="model.rigaInModifica.numero"/>
					
					<div id="aggCartaContabile" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggCartaContabileLabel"  aria-hidden="true">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4>Aggiorna Carta Contabile</h4>
						</div>
						
						<div class="modal-body">       
						<fieldset class="form-horizontal">
						
							<div class="control-group">
								<label class="control-label" >Importo *</label>
								<div class="controls">    
									<!-- <input id="aggImportoCartaContabile" name="aggImportoCartaContabile"  class="span3" type="text" value="" placeholder="999.000.000,00" required /> -->
									<s:textfield id="importoInAggiornamento" name="model.rigaInModifica.importo" cssClass="span2 soloNumeri decimale"/>
								</div>
							</div>
					
							<div class="control-group">
								<label class="control-label">Descrizione *</label>
								<div class="controls">
										<!-- <textarea id="aggDescCartaContabile" name="aggDescCartaContabile" rows="2" cols="15" class="span10" type="text" value="" required ></textarea> -->
										<s:textfield rows="2" cols="15" id="descrizioneInAggiornamento" name="model.rigaInModifica.descrizione" cssClass="span10" ></s:textfield>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">Conto del tesoriere *</label>
								<div class="controls">                         																		
									<s:if test="null!=model.listaContoTesoriereRiga">
										<s:select list="model.listaContoTesoriereRiga" id="contoTesoriereInAggiornamento" headerKey=""  
											headerValue="" name="model.rigaInModifica.contoTesoreria.codice" cssClass="span10" 
											listKey="codice" listValue="codice+' - '+descrizione" />
									</s:if> 
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" >Note </label>
								<div class="controls">
										<!-- <textarea id="aggNoteCartaContabile" name="aggNoteCartaContabile" rows="2" cols="15" class="span10" type="text" ></textarea> -->
										<s:textfield rows="2" cols="15" id="noteInAggiornamento" name="model.rigaInModifica.note" cssClass="span10" ></s:textfield>
								</div>
							</div>
							
							
						</fieldset>
					
						</div>
						<div class="modal-footer">
							<button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">annulla</button>
							<!-- <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button> -->
							<s:submit cssClass="btn btn-primary pull-right" method="aggiornaRigaDaModale" value="conferma" name="aggiornaRigaDaModale" />							
						</div>
					</div> 
					<!-- Modal aggiorna --->  
										
					
					<!-- Modal elimina -->										
					
					
										
					<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
						<div class="modal-body">
							<div class="alert alert-error">
								<button type="button" class="close" data-dismiss="alert">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p><strong>Elemento selezionato:  <s:textfield id="numeroRigaDaEliminareVisual" name="numeroRigaDaEliminareVisual" disabled="true"></s:textfield></strong></p>
								<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>							

							<s:submit cssClass="btn btn-primary pull-right" method="eliminaRiga" value="si, prosegui" name="eliminaRiga" />							
						</div>
					</div>
					
					<s:hidden id="numeroRigaDaEliminare" name="numeroRigaDaEliminare"/>
					<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
					  
					<!--/modale elimina -->																			
										
																								
					<!--#include virtual="include/modal.html" -->
					<p class="margin-medium">
						<!-- <a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a> -->  
						<%-- <s:include value="/jsp/include/indietro.jsp" /> --%>    
						<s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary"/>
						<!-- <a class="btn btn-primary pull-right" href="">salva</a> -->	
						<span class="pull-right">
							<s:submit name="salva" value="salva" method="salvaCarta" cssClass="btn btn-primary" />				
						</span>					
					</p>       
			
				<!-- </form> -->
      			</s:form>
				
		</div>	  
	</div>	 
</div>	

<script type="text/javascript">
	$(document).ready(function() {		
		$(".linkAggiornaRiga").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#importoInAggiornamento").val(supportId[1]);
				$("#descrizioneInAggiornamento").val(supportId[2]);
				$("#contoTesoriereInAggiornamento").val(supportId[3]);
				$("#noteInAggiornamento").val(supportId[4]);
				$("#dataEsecuzioneInAggiornamento").val(supportId[5]);
				$("#numeroRigaInAggiornamento").val(supportId[6]);
			}
		});
		
		$(".linkEliminaRiga").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {				
				$("#numeroRigaDaEliminare").val(supportId[1]);
				$("#numeroRigaDaEliminareVisual").val(supportId[1]);
			}
		});
		
	});

<s:if test="disableStep2()">
	$('#btnNuovaRigaDaMov').attr('disabled', true);
	$('#btnNuovaRigaDaMov').removeAttr('href');
	$('#btnNuovaRigaDaDoc').attr('disabled', true);
	$('#btnNuovaRigaDaDoc').removeAttr('href');
</s:if>
	
</script>  


<s:include value="/jsp/include/footer.jsp" />
