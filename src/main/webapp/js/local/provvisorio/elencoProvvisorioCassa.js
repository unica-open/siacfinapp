/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {
	
	$('td.sac').each(function() {
		var sel = $("select", this); 
		sel.addClass("chosen-select").data('orig-val', sel.val());
	});

	var updateChosen = function(obj) {
		var a = obj.siblings(".chosen-container").find("a");
		var txt = obj.find("option:selected").text();
		var code = txt.replace(/\s+.+$/, '');
		var desc = txt.replace(/^\S+\s+/, '');
				
		a.find("span").text(code);

		a.attr("data-toggle", "tooltip");
		a.attr("title", desc);

		//a.tooltip();
	};

	$(".chosen-select").bind("chosen:ready", function(evt, params) {
		updateChosen($(this));
	});
	
	$(".chosen-select").bind("change", function(evt, params) {
		updateChosen($(this));
	});

	$(".chosen-select").chosen({ 
		allow_single_deselect: true, 
		placeholder_text_single:' ', 
		max_shown_results: 10, 
		search_contains: true 
	});
	
	
	$('#aggiorna-sac').click(function() {
		$('td.sac').each(function() {
			var sel = $('select', this);
			if (sel.data('orig-val') === sel.val()) {
				$('input[type="hidden"]', this).val(-1);
			}
		});
	});
	
});
