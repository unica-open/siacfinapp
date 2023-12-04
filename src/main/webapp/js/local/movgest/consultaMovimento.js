/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global) {
    var dettaglioSubPopup = $('#dettaglioSubPopup').val();
    var dettaglioModPopup = $('#dettaglioModPopup').val();
    var consultaModificheProvvedimento = $('#consultaModificheProvvedimento').val();
    var consultaModificheProvvedimentoSub = $('#consultaModificheProvvedimentoSub').val();
    var consultaStorico = $('#loadLegameMovimentoStoricizzato').val();
    var annoMovimentoInConsultazione = $('#annoMovimentoInConsultazione').val();
	var numeroMovimentoInConsultazione = $('#numeroMovimentoInConsultazione').val()
    var tipoDisponibilita;
    $(init);
    
    function handleLoad(url, destination, supportIdField) {
        return function() {
        	var objToSend = {};
            if(supportIdField !== undefined) {
                var supportId = $(this).attr("id").split("_");
                if(!supportId[1]) {
                    // Prevents the call to consultaModificheProvvedimentoSub when we click on #linkConsultaModificheProvvedimento
                    // which shares the class .tooltip-test
                    return;
                }
                objToSend.data = supportIdField + '=' + supportId[1];
            }
           return loadFromRequestedUrl(url, destination, objToSend)();
        }
    }
    
    function loadFromRequestedUrl(url, destination, outerParams, fncSuccess) {
       return function() {
           // var supportId;
            var innerParams = {
                url: url,
                type: 'POST',
                success: function(data) {
                    $(destination).html(data);
                    if(fncSuccess && typeof fncSuccess === 'function'){
                    	fncSuccess();
                    }
                }
            };
            var params = $.extend({}, innerParams, outerParams, true);
            $.ajax(url, params);
        }
    }
    
    function focusWorkaroundKeydown(e) {
        var $target = $(e.target);
        tipoDisponibilita = $target.data('disponibilitaMotivazioneTrigger');
    }
    
    function handlePopupMotivazioneDisponibilita(e) {
        if(!tipoDisponibilita) {
            return true;
        }
        
        if(e.key.toLowerCase() === 'i' && e.ctrlKey && e.shiftKey && e.altKey) {
            bootbox.dialog({
                message: '<strong><pre>' + $('span[data-disponibilita-motivazione-data="' + tipoDisponibilita + '"]').html().trim() + '</pre></strong>',
                title: 'Motivazione disponibilita\' ' + tipoDisponibilita,
                buttons: {
                    'ok': {
                        label: 'Ok',
                        className: 'btn',
                        callback: $.noop
                    }
                }
            });
        }
    }
    
    function handleStorico(destination, e){
    	var $target = e && $(e.target);
    	var paramChiamataAjax = {};
    	if($target.data('loaded')){
    		return;
    	}
    	paramChiamataAjax.data = {'annoMovimento' : '' + annoMovimentoInConsultazione, 'numeroMovimento' : '' + numeroMovimentoInConsultazione  };
    	
    	
    	loadFromRequestedUrl(consultaStorico, destination, paramChiamataAjax, function(){
    		$target.data('loaded', true);
    	})();
    	
    }


    
    function init() {
        var $body = $(document.body);
        $(".consultaSubPopup").click(handleLoad(dettaglioSubPopup, '#divDettaglioSubPopup', 'uidPerDettaglioSub'));
        $(".consultaModPopup").click(handleLoad(dettaglioModPopup, '#divDettaglioModPopup', 'uidPerDettaglioMod'));
        
        
        $("#linkConsultaModificheProvvedimento").click(handleLoad(consultaModificheProvvedimento, '#modConsultaModificheProvvedimento'));
        $(".tooltip-test").click(handleLoad(consultaModificheProvvedimentoSub, '#modConsultaModificheProvvedimento', 'uidSubMovimento'));
        
        //SIAC-6702
        $('#linkAccertamenti').click(handleStorico.bind(undefined, '#divLegameStoricoDaImpegno'));
        $('#linkImpegni').click(handleStorico.bind(undefined, '#divLegameStoricoDaAccertamento'));
        
        $body.on('click', '[data-disponibilita-motivazione-trigger]', focusWorkaroundKeydown);
        $body.on('keydown', handlePopupMotivazioneDisponibilita);
    }
}(this);
