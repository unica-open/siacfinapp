/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
function showAlert(type, msgs) {
	if (msgs.length > 0) {
		
		var alertMsg = $(".alert-" + type);
		var alertMsgUl = alertMsg.find('ul');
		alertMsgUl.find("li").remove();
		
		for (var mi in msgs) {
			alertMsgUl.append('<li>' + msgs[mi].codice +  ' - '  + msgs[mi].descrizione +  '</li>');	
		}
		
		alertMsg.show();
		
		alertMsg.get(0).scrollIntoView();

		return msgs.length > 0;
	}
	
	return hideAlert(type);
}

function hideAlert(type) {

	var alertMsg = $(".alert-" + type);

	alertMsg.find("ul li").remove();

	alertMsg.hide();
	
	return 0;
}

function alertHidden(type) {
	return $(".alert-" + type).is(':hidden');
}

function hideErrors() {
	hideAlert('error');
}

function hideWarnings() {
	hideAlert('warning');
}


function today() {
	var now = new Date();
	return now.getDate() + '/' + (now.getMonth()+1) + '/' + now.getFullYear();
}

function todayDDMMYYYY() {
	var now = new Date();
	return now.getDate() + '/' + (now.getMonth()<9 ? '0' : '') + (now.getMonth()+1) + '/' + now.getFullYear();
}



/**
 * Funzionalita che permette di accettare solamente numeri, backup della vecchia versione
 * @param evt
 * @returns {Boolean}
 */
function checkItNumbersOnlyOLD(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


function checkItNumbersOnly(e) {
	var obj = e.srcElement || e.target;
	
    if(obj.value.match(/\D/g))
        obj.value = obj.value.replace(/\D/g, "");
}


function importoToFloat(importo) {
	return parseFloat(importo.replace(/\./g, '').replace(',', '.'));
}


function floatToImporto(n) {
    var s = Number(n).toString().replace(/\.(\d{1,2})\d*$/g, ',$1')
		 .replace(/\./g, '');

    if (s.match(/^([1-9]\d*|0)(\,|\,\d{0,2})?$/) == null)
    	return ''

    return s.replace(/[^\d\,]/g, '')
			.replace(/^(\d+)\,?$/, '$1,00')
			.replace(/(\,\d)$/, '$10')
			.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}


$.fn.formatDecimal = function() {

	var selected = false;
	
	var format = function(input) {
	    input.val(input.val().replace(/\.(\d{1,2})\d*$/g, ',$1')
	    					.replace(/\./g, '')
	    );
	    
	    if (input.val().match(/^[\-\+]?([1-9]\d*|0)(\,|\,\d{0,2})?$/) == null)
	    	input.val('')
	    else
	    	input.val(input.val().replace(/[^\+\-\d\,]/g, '')
	    						.replace(/^([\-\+]?\d+)\,?$/, '$1,00')
	    						.replace(/(\,\d)$/, '$10'));
	}
	
	$(this).each(function(){
		format($(this));
	});
	
	$(this).keypress(function(e) 
	{
		if (e.which < 32)
			return true;

	    var t = [$(this).val().slice(0, this.selectionStart), e.key, $(this).val().slice(this.selectionEnd)].join('');

	    if (t.match(/^([\-\+]|[\-\+]?[1-9]\d*|[\-\+]?0)(\,|\,\d{0,2})?$/) != null) {
	     // $(this).val(t);
	      selected = false;
	      return true;
	    }

	    return false;

	  }).focus(function(e) {
	    $(this).val($(this).val().replace(/\./g, ''));
	    
	  }).blur(function(e) {
	      format($(this));
	  }).select(function(e) {
	    selected = true;
	  });    
}




/**
 * Estensioni JQuery
 */
jQuery.fn.gestioneCig = function() {
    return this.on("keypress", onkeypress);

    function onkeypress(evt) {
		var len = this.value.length; 
    	var code = evt.charCode || evt.keyCode;
    	
    	if (code < 32 || evt.charCode === 0 || evt.ctrlKey || evt.altKey) {
            return;
        }
        
		if (len < 10 && /[A-Z0-9]/i.test(evt.key)) {
			this.value = this.value + evt.key.toUpperCase();
		}
		
		evt.preventDefault();
		return false;
  	}
};

jQuery.fn.gestioneCup = function() {
    return this.on("keypress", onkeypress);

    function onkeypress(evt) {
		var len = this.value.length; 
    	var code = evt.charCode || evt.keyCode;
    	
    	if (code < 32 || evt.charCode === 0 || evt.ctrlKey || evt.altKey) {
            return;
        }
        
		if (len < 15) {
			if (/[A-Z]/i.test(evt.key) || (len != 0 && len != 3 && /\d/.test(evt.key))) {
				this.value = this.value + evt.key.toUpperCase();
			}
		}
		
		evt.preventDefault();
		return false;
  	}
};


/**
 * Funzionalit� che permette di accettare solamente numeri
 * @param evt
 * @returns {Boolean}
 */
function checkItNumbersCommaAndDotOnly(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    	/*Controllo per punto e virgola*/
    	if (charCode != 44 && charCode != 46) {
    		return false;
    	}
    }
    return true;
}


/**
 * Funzionalit� che permette di accettare solamente numeri
 * @param evt
 * @returns {Boolean}
 */
function checkItNumbersCommaAndDotAndMinusOnly(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    	/*Controllo per punto e virgola e carattere meno */
    	if (charCode != 44 && charCode != 46 && charCode != 45) {
    		return false;
    	}
    }
    return true;
}

/**
 * Funzionalit� che controlla la validit� sintattica di una mail
 * @param field
 * @returns {Boolean}
 */
function checkEmail(field) {
	var email = document.getElementById(field);
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (!filter.test(email.value)) {
		document.getElementById(field).style.backgroundColor="yellow";
		document.getElementById(field).value="Inserire una e-mail valida";
		return false;
	}
	else{
		document.getElementById(field).style.backgroundColor="";
		return true;
	}
}

function autocompleteCitta() {
	// AUTOCOMPLETE DI COMUNE
	 $("#comune").autocomplete({
		 minLength:2,
		 source: function  (request, response) {
	        $.getJSON("ajax/comuniLikeAjax.do?term="+ request.term+"&idNazione="+$( "#idNazione option:selected" ).val(), function (data) {
	            response($.map(data.listaComuni, function (value, key) {
	            	// visualizzo solo il paese se nn c'e' provincia 
//	            	var labelStr = value.descrizione + ' '+value.siglaProvincia;
//	            	if (null==value.siglaProvincia) {
//	            		labelStr = value.descrizione;
//	            	} else {
//	            		labelStr = value.descrizione + ' ('+value.siglaProvincia+')';
//	            	}
	                return {
	                	label: value.descrizione,
	                    value: value.codiceIstat
	                };
	            }));
	        });
	    },
//	    select: function(event, ui){
//	    	$("#hiddenComune").val(ui.item.value);
//	    }
	    focus: function(event, ui) {
	    	$("#comune").val(ui.item.label);
	    	return false;
	    },
	    select: function(event, ui) {
	    	$("#comune").val(ui.item.label);
	    	$("#comuneId").val(ui.item.value);
	    	return false;
	    }
	});
}

function autocompleteSedimi() {
	$("#sedimiList").autocomplete({
		 minLength:2,
		 source: function  (request, response) {
	        $.getJSON("ajax/sedimiLikeAjax.do?term="+ request.term, function (data) {
	            response($.map(data.listaSedimi, function (value, key) {
	            	var labelStr = value.descrizione;
	                return {
	                	label: labelStr,
	                    value: labelStr
	                };
	            }));
	        });
	    },
	    focus: function(event, ui) {
	    	$("#sedimiList").val(ui.item.label);
	    	return false;
	    },
	    select: function(event, ui) {
	    	$("#sedimiList").val(ui.item.label);
	    	return false;
	    }
	});

}



$.fn.gestioneDeiDecimali = function(ignoreFirst) {
	return this.on("blur", function() {
		var self = $(this);
		// Per gestire il rientro a seguito di una formattazione numerica non corretta
		var number;
		if(self.val().indexOf(",") === -1) {
			// Se non ho virgole, allora parsifico il numero in maniera normale
			number = parseFloat(self.val());
		} else {
			// Se ho una virgola, allora il numero dovrebbe essere formattato
			number = parseFloat(self.val().replace(/\./g, "").replace(/,/g, "."));
		}
		if(!isNaN(number)) {
			self.val(number.formatMoney());
		}
	}).each(function() {
		if(!ignoreFirst) {
			// Richiamo immediatamente la funzione di cui sopra
			$(this).blur();
		}
	});
};
/**
 * Formatta la valuta.
 *
 * @param c (Number) numero di cifre decimali (Default: 2)
 * @param d (String) divisore decimale        (Default: ',')
 * @param t (String) divisore delle triplette (Default: '.')
 * 
 * @returns (String) l'importo formattato
 *
 * @see http://stackoverflow.com/questions/149055
 */
Number.prototype.formatMoney = function (c, d, t) {
    // Numero
    var n = this;
    // Numero di cifre decimali
    var cifre = isNaN(Math.abs(c)) ? 2 : c;
    // Separatore decimale
    var dec = ((d === undefined) ? "," : d);
    // Separatore delle triplette
    var trip = ((t === undefined) ? "." : t);
    // Segno
    var s = n < 0 ? "-" : "";
    // Parte intera del numero
    var i = parseInt(Math.abs(+n || 0).toFixed(cifre), 10) + '';
    var j = i.length > 3 ? (i.length % 3) : 0;
    var k = Math.abs(n) - i;

    return s + (j ? i.substr(0, j) + trip : "") +
    	i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + trip) +
    	(cifre ? dec + Math.abs(k).toFixed(cifre).slice(2) : '');
};

/**
 * Gestione delle componenti jquery in comune
 */
$(function() {
	autocompleteCitta();
	autocompleteSedimi();
	$(document).ready(function() {
		$('.multiSelectCustom').multiSelect();
		$('.datepicker').datepicker();
	});
	$('.siacImportoJQuery').priceFormat({
		prefix: '',
		centsSeparator: ',',
		thousandsSeparator: '.'
	});
	
	$(".soloNumeri").allowedChars({numeric: true});
	
	 /* Formattazione corretta dei campi numerici */
    $(".decimale").gestioneDeiDecimali();
	
	$("input.cig").gestioneCig();
	$("input.cup").gestioneCup();

	// Allineamento TAG 3.16.0-002
	$('[data-toggle="tooltip"]').tooltip({
	    placement : 'top'
	});
});

// metodo che dato id dell'ancora ti sposta con l'animazione
function spostaLAncora(idAncora) {
	var aTag = $("a[id='"+idAncora+"']");
	$("html, body").animate({scrollTop: aTag.offset().top}, 1000);
}


// funzione che preseleziona l'albero della struttura al
// click della struttura scelta da popup
// il nome deve essere sempre 'strutturaAmministrativaOrdinativoIncasso'
function vaiStrutturaAlberoGenerico(idPerAlbero, livello, counter) {
	var maxCounter = 10;
	counter = +counter || 0;
	var treeObjPagina = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
	if(counter >= maxCounter) {
		return;
	}
	if(treeObjPagina == null) {
		setTimeout(() => vaiStrutturaAlberoGenerico(idPerAlbero, livello, counter + 1), 2000);
		return;
	}
     var nodesPagina = treeObjPagina.getNodes();
     if (nodesPagina.length>0) {
  	   for (var i = 0; i < nodesPagina.length; i++) {
  		   
  		           if(livello<=1){
  		        	  //quando e' di livello 1 vuol dire che sto ragionando sui padri 
  		        	  if(nodesPagina[i].id==idPerAlbero){
  						  treeObjPagina.checkNode(nodesPagina[i], true, true);
  						  break;
  					   } 
  		        	   
  		           }else{
  		        	 // se sono di livello 2 allora devo andare a ciclare sui figli  
  		        	 var nodesPaginaChildren = nodesPagina[i].children;
  		        	 if (nodesPaginaChildren!=null && nodesPaginaChildren.length>0) {
  		          	   for (var j = 0; j < nodesPaginaChildren.length; j++) {
  		          		   // il codice sara' quello da andare a mettere in equals
	  		        	   if(nodesPaginaChildren[j].codice==idPerAlbero){
	 						  treeObjPagina.checkNode(nodesPaginaChildren[j], true, false);
	 						  treeObjPagina.expandNode(nodesPagina[i], true);
	 						  break;
	 					   } 
  		        	   }
  		          	}
  		           }
  		   
				  
			}
     }
	
}

	function preselezionaStrutturaPaginaPrincipale() {
		// vado a verificare di aver scelto o meno il provvedimento con 
		// compilazione guidata
		// alert('preselezionaStrutturaPaginaPrincipale');
		
		if($("#strutturaSelezionataSuPagina").val()==null || $("#strutturaSelezionataSuPagina").val()==''){
			
			//alert('strutturaSelezionataSuPagina: ' + $("#strutturaSelezionataSuPagina").val());
		
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					//alert("nodo corrente ", currentNode);
					$("#strutturaSelezionataSuPagina").val(currentNode.uid);
					//alert("strutturaSelezionataSuPagina dopo: "+ $("#strutturaSelezionataSuPagina").val());
				});
			}
		} else {
			
			//DICEMBRE 2017 AGGIUNTO QUESTO RAMO ELSE PER SIAC-5619

			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
			
			var trovato = false;
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				trovato = selectedNode.length > 0;
				selectedNode.forEach(function(currentNode) {
					$("#strutturaSelezionataSuPagina").val(currentNode.uid);
				});
				
			}
			if(trovato == false){
				$("#strutturaSelezionataSuPagina").val('');
			}
		}
	}
	
	
	//metodo che dato id blocca la pagina
	function bloccaPagina(idBtn) {
		  $.blockUI({ message: null }); 
	} 
	
	//metodo che dato id blocca la pagina
	function bloccaPaginaWait(msg) {
		  $.blockUI({ message: msg }); 
	} 

	//metodo che dato id blocca la pagina
	function bloccaPaginaOver(msg) {
		$.blockUI({ message: null, baseZ: 1050 }); 
	} 

	//metodo che dato id blocca la pagina
	function bloccaPaginaWaitOver(msg) {
		$.blockUI({ message: msg, baseZ: 1050 }); 
	} 



$(document).ready(function() {
	// metodo che nei pop up inibisce il return
	$(".modal").on("keypress", "*:input", function(e) {
		 var code = e.charCode || e.keyCode;
	    if(code === 13) {
	        e.preventDefault();
	        e.stopPropagation();
	        return false;
	    }
	});
	
	
    // blocca pagina su salva
	$('body').on('click', '.freezePagina', function(){
		bloccaPagina('blocca');
	});
//  $('.freezePagina').click(function() { 
//	$('body').on('click', '.freezePagina', bloccaPagina.bind(undefined,'blocca'))
//    $('.freezePagina').click(function() { 
// 		bloccaPagina('blocca');
//    }); 
    
    // blocca pagina su salva 
    $('.freezePaginaWait').click(function() { 
    	bloccaPaginaWait('Per favore attendere..');
    }); 
    
    // blocca pagina su salva 
    $('.freezePaginaWaitOp').click(function() { 
    	bloccaPaginaWait('Operazione in corso. Per favore attendere..');
    }); 

    // blocca pagina su salva alzando lo z-index dell'overlay
    $('.freezePaginaOver').click(function() { 
    	bloccaPaginaOver('Operazione in corso. Per favore attendere..');
    }); 
    
    // blocca pagina su salva alzando lo z-index dell'overlay
    $('.freezePaginaWaitOver').click(function() { 
    	bloccaPaginaWaitOver('Operazione in corso. Per favore attendere..');
    }); 
   
   
 
	
});
