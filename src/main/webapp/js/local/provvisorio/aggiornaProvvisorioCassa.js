/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {
	
	$('label[for=accettatonull]').hide();
	
	var on_changeDate_dataInvioServizio; 
	var on_clearDate_dataInvioServizio; 
	on_changeDate_dataInvioServizio = on_clearDate_dataInvioServizio = function(evt) {
		if (! $('#tipoUtente').data('is-amministratore') || $(this).data('orig-val') === $(this).val()) {
			return;
		}
		
		if ($(this).data('orig-val').length > 0) {
			$("input[name='model.accettatoStr']").attr('checked', false);
			$('#dataPresaInCaricoServizioGrp').hide();
			$('#dataRifiutoErrataAttribuzioneGrp').hide();
			$("#dataPresaInCaricoServizio").val("");
			$("#dataRifiutoErrataAttribuzione").val("");
			$('#accettatonull').attr('checked', true);
		}
		
		$(this).data('orig-val', this.value);
     };
	
	$('#dataInvioServizio')
		.each(function(){
			$(this).data('orig-val', this.value);

			if ($('#tipoUtente').data('is-decentrato') && $('#dataInvioServizio').val().length === 0) {
				$("#dataPresaInCaricoServizio").attr('readonly', true);
				$("#dataRifiutoErrataAttribuzione").attr('readonly', true);
			} 
		})
		.on('changeDate', on_changeDate_dataInvioServizio)
		.on('clearDate', on_clearDate_dataInvioServizio);
	
	
	var onClick_input_name_tipoDocumentoProv = function(val) {
		$('#dataInvioServizio').parents('div.control-group').toggle(val === 'Entrata');
	}
	
	onClick_input_name_tipoDocumentoProv($("input[name='tipoDocumentoProv']:checked").val());
	
	$("input[name='tipoDocumentoProv']").click(function(){
		onClick_input_name_tipoDocumentoProv(this.value);
	});
	
	var onClick_input_name_model_accettato = function(val) {
		$('#dataPresaInCaricoServizioGrp').toggle(val === 'true');
		$('#dataRifiutoErrataAttribuzioneGrp').toggle(val === 'false');
	}
	
	onClick_input_name_model_accettato($("input[name='model.accettatoStr']:checked").val());
	
	$("input[name='model.accettatoStr']").click(function() {
		if ($('#tipoUtente').data('is-decentrato')) {
			if (this.id === 'accettatotrue') {
				return false;
			}
			
			if (this.id === 'accettatofalse') {
				$('#dataRifiutoErrataAttribuzione').datepicker('remove').prop('readonly', true).val(todayDDMMYYYY());
				$("#dataPresaInCaricoServizio").val("");
			}
		}

		onClick_input_name_model_accettato(this.value);
	});
	
	$("#dataPresaInCaricoServizio").click(function() {
		$("#dataRifiutoErrataAttribuzione").val("");
	});
	
	$("#dataRifiutoErrataAttribuzione").click(function() {
		$("#dataPresaInCaricoServizio").val("");
	});
	
});


$(document).on("ztree:init", function(ztree) {
	var ztree = $.fn.zTree.getZTreeObj("strutturaAmministrativaAggiornamentoProvvisorio");
	
	if (ztree != null && $('#tipoUtente').data('is-amministratore')) {
		
		var fnClick = ztree.setting.callback.onClick || function(){};
		var fnCheck = ztree.setting.callback.onCheck || function(){};
		   
	     var setAccettatoAndDataPresaInCaricoServizio = function() {
	   		$("#accettatotrue").trigger('click');
			$('#dataInvioServizio').val(todayDDMMYYYY());
			$('#dataPresaInCaricoServizio').val(todayDDMMYYYY());
			$("#dataRifiutoErrataAttribuzione").val("");
	     };
	
		   ztree.setting.callback.onClick = function(srcEvent, treeId, node, clickFlag) {
			   fnClick(srcEvent, treeId, node, clickFlag);
			   setAccettatoAndDataPresaInCaricoServizio();  
		   }
		   
		   ztree.setting.callback.onCheck = function(srcEvent, treeId, node, clickFlag) {
			   fnCheck(srcEvent, treeId, node, clickFlag);
			   setAccettatoAndDataPresaInCaricoServizio();
		   }
	}
	
});
