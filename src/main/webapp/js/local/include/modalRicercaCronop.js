/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	var actionForm = $('form').attr('action');
	var splitted = actionForm && actionForm.split('.do');
	var baseUrl = splitted && splitted.length > 0? splitted[0] : "";
	  
    function initRicercaGuidataCronop (codice, classe) {
    	$("#codCreditoreRicerca").val(codice);
		$("#listaClasseCronopRicerca").val(classe);
		$("#campiRicercaSog").attr("class", "accordion-body collapse in");
		$("#campiRicercaSog").attr("style", "height: auto");
		$("#codfiscRicerca").val("");
		$("#pivaRicerca").val("");
		$("#denominazione").val("");
		$.ajax({
			url: baseUrl + '!pulisciRicercaCronop.do',
		    success: function(data)  {
			    $("#gestioneRisultatoRicercaSoggetti").html(data);
			}
		});
	}

	$(document).ready(function() {
		$("#ricercaGuidataCronop").click(function() {
			$.ajax({
				url: baseUrl + '_ricercaCronop.do',
				type: 'POST',
				data: $(".parametroRicercaCronop").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
	});
});