/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){

	  
    function initRicercaGuidataCronop (codice, classe) {
    	$("#codCreditoreRicerca").val(codice);
		$("#listaClasseCronopRicerca").val(classe);
		$("#campiRicercaSog").attr("class", "accordion-body collapse in");
		$("#campiRicercaSog").attr("style", "height: auto");
		$("#codfiscRicerca").val("");
		$("#pivaRicerca").val("");
		$("#denominazione").val("");
		$.ajax({
			url: '<s:url method="pulisciRicercaCronop"/>',
		    success: function(data)  {
			    $("#gestioneRisultatoRicercaSoggetti").html(data);
			}
		});
	}

	$(document).ready(function() {
		$("#ricercaGuidataCronop").click(function() {
			$.ajax({
				url: '<s:url method="ricercaCronop"/>',
				type: 'POST',
				data: $(".parametroRicercaCronop").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
	});
});