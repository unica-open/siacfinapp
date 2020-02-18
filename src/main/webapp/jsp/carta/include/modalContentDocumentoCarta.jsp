<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<s:if test="inPopup">
	<div style="padding: 0px 20px 0px 20px">
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
	</div>
</s:if>



	<div class="modal-body">		
		<fieldset class="form-horizontal"> 
		
		<div class="control-group">
			<label class="control-label">Anno *</label>
			<div class="controls">
<!-- 				<input id="annoDoc" name="annoDoc"  class="lbTextSmall span2" type="text" value="" required /> -->
				
				<s:textfield id="annoDoc"cssClass="lbTextSmall span2" name="annoDoc" maxlength="4" onkeyup="return checkItNumbersOnly(event)"></s:textfield> <span class="al">
				<label class="radio inline" >Numero *</label>
				</span>
<!-- 				<input id="numDoc" name="numDoc" class="lbTextSmall span2" type="text" value="" />			 -->
				<s:textfield id="numDoc" cssClass="lbTextSmall span2" name="numDoc"></s:textfield>
			</div>
		</div>
		
		
		
		<div class="control-group">
					<label class="control-label">Tipo</label>
					<div class="controls">
					  <s:if test="listaDocTipoSpesa!=null">
							<s:select list="listaDocTipoSpesa"
								id="listaDocTipoSpesa"
								cssClass="parametroRicercaCapitolo"
								headerKey="" headerValue=""
								name="codiceTipoDoc"
								listKey="id" listValue="codice+' - '+descrizione" />
 						</s:if>
					
					</div>
		</div>
		

		<div class="control-group">
			<label class="control-label">Soggetto codice</label>
			<div class="controls">
				<s:textfield id="soggDoc" cssClass="lbTextSmall span7" name="soggDoc" maxlength="7" readonly="true" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
				<span class="al"><a id="ricercaGuidataDocumento" class="btn btn-primary"><i class="icon-search icon"></i>cerca&nbsp;</a></span>
			</div>
		</div>
		
		<s:if test="isnSubImpegno">	
			<s:include value="/jsp/carta/include/risultatoRicercaElencoDocumentiCarta.jsp" />
		</s:if>
					
		</fieldset>
		
	</div>
	
	<script type="text/javascript">	

	
	function initRicercaGuidataDocumento(){
		$.ajax({
			url: '<s:url method="pulisciRicercaDocumento"/>',
			type: "POST",
			data: $("#aggiornaRigaDaMovimento").serialize(), 
		    success: function(data)  {
			    $("#refreshPopupModalDoc").html(data);
			}
		});
	}
	
	
	$("#ricercaGuidataDocumento").click(function() {
		var annoimpegno = $("#annoDoc").val();
		var numeroimpegno = $("#numDoc").val();
		var soggetto = $("#soggDoc").val();
		var tipoDoc = $("#listaDocTipoSpesa").val();
		
		//if(annoimpegno && numeroimpegno){
		if(true){
			$.ajax({
				url: '<s:url method="ricercaGuidataDocumento"/>',
				type: 'POST',
				data: $("#aggiornaRigaDaMovimento").serialize(), 
			    success: function(data)  {
			    	$("#refreshPopupModalDoc").html(data);
				}
			});
		}
// 		$("#annoImpegno").val($("#annoimpegno").val());
// 		$("#numeroImpegno").val($("#numeroimpegno").val());
// 		$("#numeroSub").val(null);
// 		$("#numeroMutuo").val(null);
	});	
	
	$(".linkRadioDocumentoSelezionato").click(function() {
		$("#confermaCompGuidataSubDoc").prop('disabled', false);
		
	});
	
	
	$(".linkRadioSubDocumentoSelezionato").click(function() {
		$("#confermaCompGuidataSubDoc").prop('disabled', false);
	});
	
	
	
	
	
</script>