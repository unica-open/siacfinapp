/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
	

$(document).ready(function() {

	$('textarea').keyup(function(e)
		{
			var val = $(this).val();
	        var MAX = 500;

	        if(val.replace(/\n/g, "\r\n").length > MAX)
			{
	            $(this).val(val.replace(/\n/g, "\r\n").substr(0, MAX).replace(/\r\n/g, "\n"));

	            e.preventDefault();
				e.stopPropagation();
			}
	    });

});
     	