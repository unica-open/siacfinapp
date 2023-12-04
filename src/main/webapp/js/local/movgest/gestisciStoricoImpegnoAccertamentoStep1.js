/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	var actionForm = $('form').attr('action');
	var splitted = actionForm && actionForm.split('.do');
	var baseUrl = splitted && splitted.length > 0? splitted[0] : "";
	
	$("#compilazioneGuidataImpegnoStep1").click(function() {	
		//popolo i campi della modale
		$("#annoimpegno").val($("#annoImpegno").val());
		$("#numeroimpegno").val($("#numeroImpegno").val());
		
		$.ajax({
              url: baseUrl + '_azzeraModaleImpegno.do',
              type: "GET",
           	  success: function(data){
              	$("#refreshPopupModal").html(data);
              }
       });
		
	});
});