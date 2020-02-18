/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	'use strict';
	
	window.loadedRefreshModalitaPagamento = window.loadedRefreshModalitaPagamento || false;
    if(window.loadedRefreshModalitaPagamento) {
    	return;
    }
	
	var url = $('#HIDDEN_url_remodpagamento').val();
	var lastSelection = 0;
    var radioChecked = false;
    
    $(document).ready(function() {
    	var initialRadioChecked = $('input[type=radio][name=radioSediSecondarieSoggettoSelezionato]:checked');
    	if(initialRadioChecked.length) {
    		lastSelection = initialRadioChecked.val();
    		radioChecked = true;
    	}
    	
    	$(document).on('click', 'input[type=radio][name=radioSediSecondarieSoggettoSelezionato]', function() {
    		var selectionValue;
    		var $this = $(this);
    		if(lastSelection==this.value){
    			radioChecked = !radioChecked;
    			$this.prop('checked', radioChecked);
    		}
    		radioChecked = $this.prop('checked');
    		
    		lastSelection=this.value;
    		
    		if(!radioChecked){
    			selectionValue = 'false';
    		} else {
    			selectionValue = this.value;
    		}
    		ricaricaSediModPag(selectionValue);
    	});
    	
    	ricaricaSediModPag(radioChecked ? lastSelection : 'false');
    	window.loadedRefreshModalitaPagamento = true;
    });
    
    function ricaricaSediModPag (selectiondata) {
    	var modpagOrig = $('input[type="radio"][name="radioModPagSelezionato"]:checked').val() || 0;
    	$.ajax({
    		url: url,
    		type: "GET",
    		data: { selection: selectiondata, modpagOrig: modpagOrig },
    		success: function(data){
    			$("#refreshModPagamento").html(data);
    		}
    	});
    }
});
