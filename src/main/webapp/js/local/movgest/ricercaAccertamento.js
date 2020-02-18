/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	$(document).ready(function() {
		var mainForm = $("#mainForm");
		$(".linkAnnullaImpegno").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#uidAccertamentoDaAnnullare").val(supportId[1]);
				$("#numeroAccertamentoDaAnnullare").val(supportId[2]);
				$("#numeroAccertamentoDaPassare").val(supportId[3]);
				$("#annoAccertamentoAccertamentoDaPassare").val(supportId[4]);
				$("#annoAccertamentoAccertamentoDaAnnullare").val(supportId[5]);
				
				
			}
		});


       $('li[treenode]').click(function() { 
    		$('#lineaStruttura').text($(this).find('a').attr('title'))                   
    	});

		
	});
});