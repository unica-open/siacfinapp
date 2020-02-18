/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){ 

	var MAX_ORD = 30;
	var globUidOrdIncassoSelezionati = {};
	var uidOrdIncassoSelezionati = $('#uidOrdIncassoSelezionati').val();
	var uidOrdIncassoSelezionatiArr = uidOrdIncassoSelezionati.split('-'); 
	
	if (uidOrdIncassoSelezionati.length > 0) {
		$.each(uidOrdIncassoSelezionatiArr, function(i, v) {
		    $('.sel-ord[value="' + v + '"]').attr('checked', true);
		    globUidOrdIncassoSelezionati[v] = '';
		});
	}

	var codiceSoggettoOrdinativoPagamento = $('#ordinativoPagamento').data('codice-soggetto'); 
	var globUidOrdIncassoSoggettoDiff = {};
	if ($('#uidOrdIncassoSoggettoDiff').val().length > 0) {
		$.each($('#uidOrdIncassoSoggettoDiff').val().split('-'), function(i, v) {
		    globUidOrdIncassoSoggettoDiff[v] = '';
		});
	}
	
	
	$('#numSelezionati').text(uidOrdIncassoSelezionati.length > 0 ? uidOrdIncassoSelezionatiArr.length : 0);
	$('#totImportoSelezionatiStr').text($('#totImportoSelezionati').val() === '' ? '0,00' : $('#totImportoSelezionati').val());

	$('#collegaReversali').attr('disabled', Number($('#numSelezionati').text()) == 0);
	
	$('.sel-all').append('<input type="checkbox" />');
	
	$('.sel-all input').click(function(e) {
		var checked = this.checked;
		
		$('.sel-ord').each(function(){
			if (this.checked !== checked) {
				this.checked = checked;
				calcNumeroEImportiOrdinativi($(this));
			}
		});
	});
	
	$('.sel-ord').click(function(e) {
		if (this.checked) {
			globUidOrdIncassoSelezionati[this.value] = '';
			
			if (codiceSoggettoOrdinativoPagamento !== $(this).data('codice-soggetto')) {
				globUidOrdIncassoSoggettoDiff[this.value] = $(this).data('codice-soggetto');
			}
		} else {
			delete globUidOrdIncassoSelezionati[this.value];
			delete globUidOrdIncassoSoggettoDiff[this.value];
		}
		
		calcNumeroEImportiOrdinativi($(this));
		
		$('#collegaReversali').attr('disabled', Number($('#numSelezionati').text()) == 0);
	});
	
	$('#collegaReversali').click(function(e){
		if (showErrors()) {
			e.preventDefault();
			return false;
		}
		
		if (alertHidden('warning') && showWarnings()) {
			e.preventDefault();
			return false;
		}
		
		fillHiddenValues();
	});

	$('#paginazione a').click(function(e){
		e.preventDefault();

		fillHiddenValues();
		
		$('form').attr('action', $(this).attr('href'));
		$('form').submit();
	});
	
	function fillHiddenValues() {
		$('#uidOrdIncassoSelezionati').val(Object.keys(globUidOrdIncassoSelezionati).join('-'));
		$('#uidOrdIncassoSoggettoDiff').val(Object.keys(globUidOrdIncassoSoggettoDiff).join('-'));
		$('#totImportoSelezionati').val($('#totImportoSelezionatiStr').text());
	}
	
	function calcNumeroEImportiOrdinativi(item) {
		hideErrors();

		var signum = $(item).is(':checked') ? +1 : -1;
		var numeroOrdinativiSelezionati = $('#numSelezionati');
		
		numeroOrdinativiSelezionati.text(Number(numeroOrdinativiSelezionati.text()) + signum);
		
		var sommaImportiOrdinativiSelezionati = $('#totImportoSelezionatiStr');

		sommaImportiOrdinativiSelezionati.text(
			floatToImporto(
				new BigNumber(importoToFloat(sommaImportiOrdinativiSelezionati.text()))
				.plus(new BigNumber(signum * importoToFloat(item.closest('td').siblings('.importo').text())))
				.toNumber()
		));
	}
	
	function pushAlertItem(i, desc) {
		if (desc !== undefined) {
			i.push({ codice: '', descrizione: desc });
		}
	}

	function showErrors() {
		var err = [];
		pushAlertItem(err, checkNumSel());

		return err.length > 0 ? 
				showAlert('error', err) :
				hideErrors();
	}

	function checkNumSel() {
		return Number($('#numSelezionati').text()) > MAX_ORD ?
				"Si possono selezionare al massimo " + MAX_ORD + " ordinativi." :
				undefined;
	}
	
	function showWarnings() {

		var w = [];
		pushAlertItem(w, checkImporto());
		pushAlertItem(w, checkSoggetto());
		
		return w.length > 0 ? 
				showAlert('warning', w) :
				hideWarnings();
	}

	function checkImporto() {
		return 
			new Number($('#numSelezionati').text()) > 0 && BigNumber(importoToFloat($('#importoOrdinativo').text()))
				.gt(new BigNumber(importoToFloat(sommaImportiOrdinativiSelezionati.text()))) ?
				"l'importo del mandato &egrave; superiore all'importo delle reversali selezionate" :
				undefined;
	}
	
	function checkSoggetto() {
		return Object.keys(globUidOrdIncassoSoggettoDiff).length > 0 ? 
			"il soggetto del mandato &egrave; diverso da almeno uno dei soggetti presenti sulle reversali" :
			undefined;
	}
});
