/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){

	var toggleRicercaCronop = $("#provvedimento").data('provvedimento-stato') === 'PROVVISORIO'; 
	$("#spanRicercaCronop").toggle(toggleRicercaCronop);
	
	$("#testataStruttura").click(function() {

       
       if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
    	   
	       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
	    	   
	    	   vaiStrutturaAlberoGenerico($("#idPassaggioAlbero").val(), $("#idPassaggioLivello").val());
	    	   $("#idPassaggioAlbero").val("");
	    	   $("#idHiddenPassAlbero").val("");
	    	   $("#idPassaggioLivello").val("");
	       }
       }
	});    
	
		
	$("#linkConsultaModificheProvvedimento").click(function() {
		
		//$('div').remove('.nome_classe');
		
		var url = $('#consultaModificheProvvedimento').val();
		
		$.ajax({
			url: url,
			type: 'POST',
			success: function(data)  {
				$("#modConsultaModificheProvvedimento").html(data);
			}
		});
	});	
	

	
});
