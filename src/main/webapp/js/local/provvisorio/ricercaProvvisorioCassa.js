/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {
	$(".formatDecimal").formatDecimal();
	
	
	$("#pulsanteCercaProvvisorio").click(function() {
		
		if ($("input[name='tipoDocumentoProv']:checked").val() == 'Spesa') {
			$('#dataInizioInvioServizio, #dataFineInvioServizio').val('');
			$('#dataInizioPresaInCaricoServizio, #dataFinePresaInCaricoServizio').val('');
			$('#dataInizioRifiutoErrataAttribuzione, #dataFineRifiutoErrataAttribuzione').val('');
		}
		
		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaProvvisorio");
		var strutturaAmministrativaParam = "";
		if (treeObj != null) {
			var selectedNode = treeObj.getCheckedNodes(true);
			selectedNode.forEach(function(currentNode) {
				strutturaAmministrativaParam = "&struttAmmSelezionata=" + currentNode.uid;
			});
		}
		
		$.ajax({
			url: url_primaDiRicercaProvvisorioDiCassa,
			type: 'POST',
			data: strutturaAmministrativaParam,
		    success: function(data)  {
			}
		});
		
	});	
	
	
	var onClick_input_name_tipoDocumentoProv = function(val) {
		$('#invioServizioCtrlGrp, #presaInCaricoServizioCtrlGrp, #rifiutoErrataAttribuzioneCtrlGrp').toggle(val === 'Entrata');
	}
	
	onClick_input_name_tipoDocumentoProv($("input[name='tipoDocumentoProv']:checked").val());
	
	$("input[name='tipoDocumentoProv']").click(function(){
		onClick_input_name_tipoDocumentoProv(this.value);
	});
	
	
	$("#documentoEntrata").trigger('click');
	$("#annullatoProv-2").prop('checked', true);
	
	
});
