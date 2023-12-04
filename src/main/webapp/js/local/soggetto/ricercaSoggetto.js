/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){ 

	$(document).keypress(function( e) {
	    if(e.which == 13) {
	        $("#cerca").click();
	    }
	});

	$('.capitalize').on('keyup', function(evt) {
		  $(this).val(function(_, val) {
		    return val.toUpperCase();
	  });
	});
	
});
