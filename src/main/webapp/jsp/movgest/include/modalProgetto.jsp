<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="guidaProg" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="guidaProgLabel" aria-hidden="true">
	<div class="modal-body">
		<h4>Seleziona progetto</h4>
		<fieldset class="form-horizontal">
			<div id="campiRicercaProg" class="accordion-body collapse in">
				<div class="control-group">
					<label class="control-label" for="codProgettoRicerca">Codice</label>
					<div class="controls">
						<s:textfield id="codProgettoRicerca"
							name="progettoRicerca.codice"
							cssClass="span3 parametroRicercaProgetto" type="text"  />
						<label class="radio inline" for="descrizioneProgettoRicerca">Descrizione</label>
						<s:textfield id="descrizioneProgettoRicerca"
							name="progettoRicerca.descrizione"
							cssClass="span4 parametroRicercaProgetto" type="text" />
					</div>
				</div>
				
				
            <s:if test="oggettoDaPopolareImpegno()"> 
				<div class="control-group">
					<label for="amb" class="control-label">Ambito</label>
					<div class="controls">
						<s:select list="listaTipiAmbito" cssClass="span8 parametroRicercaProgetto" name="progettoRicerca.tipoAmbito.uid" id="amb" headerKey="0" headerValue="" listKey="uid"
						listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>


				<div class="control-group">
					<label class="control-label">Servizio</label>
					<div class="controls">
						<div class="accordion span8 struttAmm">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" id="accordionPadreStrutturaAmministrativaProgetto" href="#strutturaAmministrativoContabileProgetto">
										<span id="SPAN_StrutturaAmministrativoContabileProgetto">Seleziona la Struttura amministrativa</span>
									</a>
								</div>
								<div id="strutturaAmministrativoContabileProgetto" class="accordion-body collapse">
									<div class="accordion-inner">
										<ul id="treeStrutturaAmministrativoContabileProgetto" class="ztree treeStruttAmm"></ul>
									</div>
								</div>
							</div>
						</div>
	
						<s:hidden class="parametroRicercaProgetto" id="HIDDEN_StrutturaAmministrativoContabileUidProgetto" name="progettoRicerca.strutturaAmministrativoContabile.uid" />
						<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceProgetto" name="progettoRicerca.strutturaAmministrativoContabile.codice" />
						<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneProgetto" name="progettoRicerca.strutturaAmministrativoContabile.descrizione" />
					</div>
				</div>
				</s:if>

				
			</div>
			
		</fieldset>

		<a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataProgetto" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicercaProg">
		  <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"> </span>
		</a>

		<div id="gestioneRisultatoRicercaProgetti" style="clear:both; padding-top:3px;">
			<s:include value="/jsp/movgest/include/risultatoRicercaElencoProgetti.jsp" />
		</div>

	</div>
	
	<div class="modal-footer">
		<!-- task-131 <s:submit cssClass="btn btn-primary" aria-hidden="true" value="Conferma" method="selezionaProgetto"></s:submit> -->
		<s:submit cssClass="btn btn-primary" aria-hidden="true" value="Conferma" action="%{#selezionaProgettoAction}"></s:submit>
	</div>
</div>

<script type="text/javascript">
  
    function initRicercaGuidataProgetto (codice) {
    	
    	$("#codProgettoRicerca").val(codice);
		$("#descrizioneProgettoRicerca").val("");
		
		var url='';
		
		if(codice!=''){
			//task-131 --> url = '<s:url method="ricercaProgetto"/>';
			url = '<s:url action="%{#ricercaProgettoAction}"/>';
			$.ajax({
				url: url,
				type: 'POST',
				data: $(".parametroRicercaProgetto").serialize(),
				success: function(data)  {
				    $("#gestioneRisultatoRicercaProgetti").html(data);
				}
			});
		}else	{
			$.ajax({
				//task-131 --> url: '<s:url method="pulisciRicercaProgetto"/>',
				url: '<s:url action="%{#pulisciRicercaProgettoAction}"/>',
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaProgetti").html(data);
				}
			});
		}
	}

	$(document).ready(function() {
		$("#ricercaGuidataProgetto").click(function() {


			var treeObj = $.fn.zTree.getZTreeObj("treeStrutturaAmministrativoContabileProgetto");
			
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					$('#HIDDEN_StrutturaAmministrativoContabileUidProgetto').val(currentNode.uid);
				});
			}

			// SIAC-7032
			// TASK-267
			//var codiceProgetto = $('HIDDEN_StrutturaAmministrativoContabileCodiceProgetto').val()
			//initRicercaGuidataProgetto(codiceProgetto);
			
			$.ajax({
				//task-131 --> url: '<s:url method="ricercaProgetto"/>',
				url: '<s:url action="%{#ricercaProgettoAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaProgetto").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaProgetti").html(data);
				}
			});			
		});	
	});
	
</script>