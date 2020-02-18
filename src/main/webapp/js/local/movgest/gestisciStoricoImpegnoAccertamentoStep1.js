/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	$("#compilazioneGuidataImpegnoStep1").click(function() {	
		//popolo i campi della modale
		$("#annoimpegno").val($("#annoImpegno").val());
		$("#numeroimpegno").val($("#numeroImpegno").val());
		
		$.ajax({
              url: '<s:url method="azzeraModaleImpegno"></s:url>',
              type: "GET",
           	  success: function(data){
              	$("#refreshPopupModal").html(data);
              }
       });
		
	});
});