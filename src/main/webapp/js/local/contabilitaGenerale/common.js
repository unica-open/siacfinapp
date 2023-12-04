/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* ***************************
 ****** Gestione errori ******
 *****************************/
//*

/**
 * Funzionamento di default dei preDrawCallback di dataTable.
 * @param opts le opzioni del dataTable
 */
//function defaultPreDraw(opts) {
//    $('#' + opts.nTable.id + '_processing').parent('div').show();
//}

/**
 * Funzionamento di default dei drawCallback di dataTable.
 * @param opts le opzioni del dataTable
 */
//function defaultDrawCallback(opts) {
//    $('#' + opts.nTable.id + '_processing').parent('div').hide();
//}
//
///**
// * Funzionamento di default dei drawCallback di dataTable per i risultati di ricerca sintetica
// * @param numField (string) il campo in cui applicare il numero dei record
// * @returns (function(any) => void) la funzione richiamata dal dataTable
// */
//function defaultDrawCallbackRisultatiRicerca(numField) {
//    return function(opts) {
//        var records = opts.fnRecordsTotal();
//        var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
//        $(numField).html(testo);
//        // Nascondo il div del processing
//        $('#' + opts.nTable.id + '_processing').parent('div').hide();
//    };
//}

/**
 * Crea un default per l'invocazione di dataTable
 * @param sel          (string) il selettore delle proprieta' nell'oggetto
 * @param defaultValue (any) il valore di default
 * @param operation    (function) la funzione da invocare sul risultato
 * @returns (function) una funzione per l'applicazione di dataTable
 */
function defaultPerDataTable(sel, defaultValue, operation) {
    var split = sel.split('.');
    var length = split.length;
    var defVal = defaultValue !== undefined ? defaultValue : '';
    var oper = operation !== undefined && typeof operation === 'function' ? operation : passThrough;

    return function(source) {
        var res = source;
        var i;
        for(i = 0 ; i < length; i++) {
            if(res[split[i]] === undefined) {
                return oper(defVal);
            }
            res = res[split[i]];
        }
        return oper(res);
    };
}
/**
 * Funzione che restituisce l'argomento invariato
 * @param e (any) l'argomento
 * @returns (any) l'argomento
 */
//function passThrough(e) {
//    return e;
//}

/*
 * ***************************
 * **** Aggiunte a jQuery ****
 * ***************************
 */

// Statiche
/**
 * Crea una funzione per l'invio via POST della chiamata AJAX per recuperare il JSON.
 * <br>
 * Utilizzo:
 * <pre>
 *   jQuery.postJSON(...)
 * </pre>
 *
 * @param url     (String)   l'url cui effettuare la chiamata
 * @param data    (String)   i dati da inviare
 * @param success (Function) la funzione da eseguire in caso di successo
 * @param async   (Boolean)  se la chiamata sia asincrona (Optional, default = true)
 *
 * @returns (Deferred) l'oggetto jQuery associato alla chiamata AJAX
 */
jQuery.postJSON = function(url, data, success, /* Optional */async) {
    var result, innerAsyncTemp = async, innerSuccess = success, innerData = data, innerAsync;
    // Se non ho preimpostato il il parametro data, shifto le variabili di uno
    // indietro
    if (jQuery.isFunction(data)) {
        innerAsyncTemp = async || success;
        innerSuccess = data;
        innerData = undefined;
    }

    innerAsync = (innerAsyncTemp === undefined ? true : innerAsyncTemp);

    try {
        result = jQuery.ajax({
            type : "POST",
            url : url,
            data : innerData,
            success : innerSuccess,
            dataType : "json",
            async : innerAsync
        }).fail(function(jqXHR) {
            var stringaErrore = "Errore nella chiamata al servizio: " + jqXHR.status + " - " + jqXHR.statusText;
//            doLog(stringaErrore, 'error');
            if (jqXHR.status !== 0) {
//                bootboxAlert(stringaErrore);
            }
        });
    } catch (errore) {
//        doLog(errore, 'error');
        result = jQuery.Deferred().resolve();
    }
    return result;
};


/**
 * Substitutes an handler for an event.
 *
 * @see <a href="http://api.jquery.com/on/">jQuery.fn.on</a>
 *
 * @param events   (String)   one or more space-separated event types and optional namespaces, such as "click" or "keydown.myPlugin"
 * @param selector (String)   a selector string to filter the descendants of the selected elements that trigger the event.
 *                            If the selector is <code>null</code> or omitted, the event is always triggered when it reaches the selected element
 * @param data     (Object)   data to be passed to the handler in event.data when an event is triggered
 * @param handler  (Function) a function to execute when the event is triggered. The value <code>false</code> is also allowed as a shorthand for a
 *                            function that simply does <code>return false</code>.
 */
jQuery.fn.substituteHandler = function(events, selector, data, handler) {
    return this.off(events).on(events, selector, data, handler);
};

/**
 * Prevents the default action for an event
 *
 * @param eventName (String)   the name of the event
 * @param selector  (String)   the selector for delegating the event (Optional)
 * @param data      (Object)   rhe data to be passed to the handler (Optional)
 * @param callback  (Function) the eventual callback (Optional)
 */
jQuery.fn.eventPreventDefault = function(eventName, selector, data, callback) {
    var innerSelector = selector;
    var innerData = data;
    var innerCallback = callback;
    // From jQuery
    if (innerData == null && innerCallback == null) {
        innerCallback = selector;
        innerData = innerSelector = undefined;
    } else if (innerCallback == null) {
        if (typeof innerSelector === "string") {
            innerCallback = data;
            innerData = undefined;
        } else {
            // ( types, data, fn )
            innerCallback = data;
            innerData = selector;
            innerSelector = undefined;
        }
    }
    return this.substituteHandler(eventName, innerSelector, innerData, function(e) {
        e.preventDefault();
        if(innerCallback) {
            return innerCallback.apply(this, arguments);
        }
    });
};

/**
 * Unqualifies the source object for the injection in Struts2.
 *
 * @param source (Object) the object whose fields are to be unqualified
 * @param indice (Number) the optional index to which to unqualify (Optional, default = 0)
 *
 * @returns (Object) an unqualified copy of the source object
 */
function unqualify(source, /* Optional */index) {
    // Default value for missing or invalid index
    if (isNaN(index) || index < 0) {
        index = 0;
    }
    var key, newKey, dest = {};
    for (key in source) {
        if (source.hasOwnProperty(key)) {
            newKey = key.split(".").slice(index).join(".");
            dest[newKey] = source[key];
        }
    }
    return dest;
}

/**
 * Gestione dei decimali.
 *
 * @param positiveMultiplier il moltiplicatore da utilizzare per i numeri positivi
 * @param negativeMultiplier il moltiplicatore da utilizzare per i numeri negativi
 *
 * @returns (jQuery) l'oggetto jQuery originale
 */
//jQuery.fn.gestioneDeiDecimali = function(positiveMultiplier, negativeMultiplier) {
//    return this.on("blur", onblur);
//    // this.on("keyup", keyup);
//}
    /**
     * Gestione del blur.
     */
//    function onblur() {
//        var self = $(this);
//        var importo = self.val();
//        if(positiveMultiplier !== undefined && importo > 0){
//            self.val(importo * positiveMultiplier);
//        }
//        if(negativeMultiplier !== undefined && importo < 0){
//            self.val(importo * negativeMultiplier);
//        }
//        var numberString = self.val().replace(/\./g, "").replace(/,/g, ".");
//        var number = parseFloat(numberString);
//
//        if (!isNaN(number)) {
//            self.val(number.formatMoney());
//        }
//    }
//
//
///**
// * Scrolls the page to the given element.
// *
// * @param time (Number) the time (in ms) in which the transition is to be completed (Optional - default: 800)
// *
// * @returns (jQuery) the original jQuery element
// */
//jQuery.fn.scrollToSelf = function(/* Optional */time) {
//    var completionTime = (time > 0 && time) || 800;
//    var to = this.offset().top;
//    jQuery('html, body').animate({
//        scrollTop : to
//    }, completionTime);
//    return this;
//};

/**
 * Resets the form
 */
//jQuery.fn.reset = function() {
//    return jQuery(this).each(function() {
//        this.reset();
//    });
//};
//
///**
// * Inverts the selection.
// */
//$.fn.invert = function() {
//    return this.end().not(this);
//};

// Gestione dell'evento speciale

/**
 * Gestione del triplo click.
 *
 * @see http://benalman.com/news/2010/03/jquery-special-events/
 */
//(function($) {
//    // A collection of elements to which the tripleclick event is bound.
//    var elems = $([]),
//    // Initialize the clicks counter and last-clicked timestamp.
//    clicks = 0, last = 0,
//    // Caches the document
//    $document = $(document);
//
//    // Click speed threshold, defaults to 500.
//    $.tripleclickThreshold = 500;
//
//    // Special event definition.
//    $.event.special.tripleclick = {
//        setup : function() {
//            // Add this element to the internal collection.
//            elems = elems.add(this);
//            // If this is the first element to which the event has been bound, bind a handler to document to catch all 'click' events.
//            if (elems.length === 1) {
//                $document.bind('click', clickHandler);
//            }
//        },
//        teardown : function() {
//            // Remove this element from the internal collection.
//            elems = elems.not(this);
//
//            // If this is the last element removed, remove the document 'click' event handler that "powers" this special event.
//            if (elems.length === 0) {
//                $document.unbind('click', clickHandler);
//            }
//        }
//    };
//
//    // This function is executed every time an element is clicked.
//    function clickHandler(event) {
//        var elem = $(event.target);
//
//        // If more than 'threshold' time has passed since the last click, reset the clicks counter.
//        if (event.timeStamp - last > $.tripleclickThreshold) {
//            clicks = 0;
//        }
//
//        // Update the last-clicked timestamp.
//        last = event.timeStamp;
//
//        // Increment the clicks counter. If the counter has reached 3, trigger the "tripleclick" event and reset the clicks counter to 0.
//        // Trigger bound handlers using triggerHandler so the event doesn't propagate.
//        if (++clicks === 3) {
//            elem.trigger('tripleclick');
//            clicks = 0;
//        }
//    }
//}(jQuery));

// Cache per jQuery
//(function(g, $) {
//    var cache = {};
//    g.$cache = innerCache;
//    function innerCache(sel, context, useOriginal) {
//        var key;
//        if(useOriginal) {
//            key = computeKey(sel, context);
//            initCache(key, useOriginal, [sel, context]);
//        } else if(context) {
//            if(typeof context === 'boolean') {
//                key = computeKey(sel);
//                initCache(key, context, [sel]);
//            } else {
//                key = computeKey(sel, context);
//                initCache(key, false, [sel, context]);
//            }
//        } else {
//            initCache(key, false, [sel]);
//        }
//        return cache[key];
//    }
//    function computeKey(sel, context) {
//        if(context) {
//            return sel + ' § ' + context;
//        }
//        return sel + ' §';
//    }
//    function initCache(key, useOriginal, args) {
//        if(!cache[key] || useOriginal) {
//            cache[key] = $.apply(undefined, args);
//        }
//    }
//}(this, jQuery));

/* *****************************************
 * **** Funzioni JavaScript per Struts2 ****
 * *****************************************/

/**
 * Qualifies the source object for the injection in Struts2.
 *
 * @param source        (Object) the object whose fields are to be qualified
 * @param qualification (String) the qualification to use (Optional - default: "", meaning no qualification is done)
 *
 * @returns (Object) a qualified copy of the source object
 */
function qualify(source, /* Optional */qualification) {
    var key,
        newKey,
        index,
        temp,
        dest = {},
        completeQualification = (qualification ? (qualification + ".") : "");
    // Cycle on object properties
    for (key in source) {
        if (source.hasOwnProperty(key) && source[key] !== undefined) {
            if (Object.prototype.toString.call(source[key]) === "[object Array]") {
                // If the property is an array
                for (index in source[key]) {
                    if (source[key].hasOwnProperty(index)) {
                        newKey = completeQualification + key + "[" + index + "]";
                        dest[newKey] = source[key][index];
                    }
                }
            } else if (Object.prototype.toString.call(source[key]) === "[object Object]") {
                // If the property is an object
                if (source[key].hasOwnProperty("_name")) {
                    // It's a Java Enum. Strip the wrapping object
                    newKey = completeQualification + key;
                    dest[newKey] = source[key]._name;
                } else {
                    temp = qualify(source[key], completeQualification + key);
                    for (index in temp) {
                        if (temp.hasOwnProperty(index)) {
                            dest[index] = temp[index];
                        }
                    }
                }
            } else {
                // The property is neither an array nor an object
                newKey = completeQualification + key;
                dest[newKey] = source[key];
            }
        }
    }
    return dest;
}

/**
 * Funzione di utilit&agrave; per l'impostazione dei dati all'interno degli alert
 *
 * @param arrayDaInjettare  (Array)   l'array dei dati da injettare nell'alert
 * @param alertDaPopolare   (jQuery)  l'alert da popolare con i valori dell'alert
 * @param redirezione       (Boolean) se effettuare una redirezione sull'hash dell'alert (Optional, default = true)
 * @param chiudereGliAlerts (Boolean) se sia necessario chiudere gli alerts (Optional, default = true)
 *
 * @returns (Boolean) <code>true</code> se i dati sono stati impostati; <code>false</code> altrimenti
 *
 */
function impostaDatiNegliAlert(arrayDaInjettare, alertDaPopolare, /* Optional */ redirezione, /* Optional */chiudereGliAlerts) {
    var result = false;
    var innerRedirezione = redirezione === undefined ? true : redirezione;
    var innerChiusura = chiudereGliAlerts === undefined ? true : chiudereGliAlerts;
    if (arrayDaInjettare && arrayDaInjettare.length > 0 && alertDaPopolare.length > 0) {
        // Chiudo i vari alert aperti
        if (!!innerChiusura) {
            jQuery(".alert").not(".alert-persistent").slideUp();
            alertDaPopolare.find("ul").find("li").remove();
        }

        // Aggiungo gli errori alla lista
        jQuery.each(arrayDaInjettare, function(key, value) {
            // Se ho un oggetto con codice e descrizione, lo spezzo; in caso contrario utilizzo direttamente il valore in entrata
            if (this.codice !== undefined) {
                alertDaPopolare.find("ul").append(jQuery("<li/>").html(this.codice + " - " + this.descrizione));
            } else {
                alertDaPopolare.find("ul").append(jQuery("<li/>").html(value));
            }
        });
        // Mostro l'alert di errore al termine dell'eventuale animazione
        alertDaPopolare.promise().done(function() {
            this.slideDown();
        });
        // Ritorno true per indicare di aver injettato i parametri
        result = true;
        if (innerRedirezione) {
            // Sposto l'attenzione della pagina sull'alert
            alertDaPopolare.scrollToSelf();
        }
    }
    return result;
}


/* *********************************************
 * **** Caricamento di funzionalita' varie. ****
 * *********************************************/
jQuery(function() {
    var $document = jQuery(document);
    var $body = jQuery(document.body);
    // Previene il reindirizzamento di ogni link avente href="#"
    jQuery('a[href="#"]').click(function(e) {
        e.preventDefault();
    });

    // Popover
//    jQuery('.popover-test').popover();
//    jQuery("a[rel=popover]").popover()
//        .click(function(e) {
//            e.preventDefault();
//        });

    // Tabs
//    jQuery('.nav.nav-tabs').tab()
//    // Impedisce ai tab-pane che non sono il primo di essere visualizzati
//        .find("li").not(".active").each(function() {
//            var idTab = jQuery(this).find("a").attr("href");
//            jQuery(idTab).removeClass("active");
//        });

    // Collapse
    jQuery('.collapse').collapse({
        // Evito che il collapse venga aperto immediatamente
        toggle : false
    }).on('hidden', function() {
        jQuery(this).find(':input')
                .prop("tabindex", "-1")
                .end()
            .parent()
                .find("a.accordion-toggle")
                    .first()
                        .addClass("collapsed");
    }).on('shown', function() {
        jQuery(this).find(':input')
                .removeProp("tabindex")
                .end()
            .parent()
                .find("a.accordion-toggle")
                    .first()
                        .removeClass("collapsed");
    });


//    /**
//     * Shows the overflow as visible.
//     *
//     * @param event         {Event}   the event
//     * @param elt           {Element} the element over which to act
//     * @param selector      {String}  the selector for the element
//     * @param childSelector {String}  the selector for the children of the element
//     */
//    function showOverflow(event, elt, selector, childSelector) {
//        event.stopPropagation();
//        jQuery(elt).find(childSelector)
//                .css("overflow", "visible")
//                .end()
//            .find(selector)
//                .css("overflow", "hidden");
//    }
//
//
//    // AllowedChars
//    jQuery(".soloNumeri").allowedChars({
//        numeric : true
//    });
//    jQuery(".numeroNaturale").allowedChars({
//        regExp : /[0-9]/
//    });
//
//    // Chosen (integrare le opzioni)
//    jQuery(".chosen-select").chosen({
//        allow_single_deselect : true,
//        disable_search_threshold : 5,
//        no_results_text : 'Nessun risultato'
//    });
//
//    // Multi-select
//    jQuery(".multiselect").multiSelect();
//
//    /*
//     * Gestione dell'hide sull'alert. Per ottenere cio', basta sostituire il
//     * 'data-dismiss' con un 'data-hide'. Cfr.
//     * http://stackoverflow.com/questions/13550477/
//     */
//    jQuery("[data-hide]").on("click", function() {
//        var self = jQuery(this);
//        self.closest("." + self.attr("data-hide")).slideUp();
//    });

    /* Formattazione corretta dei campi numerici */
    jQuery(".decimale").gestioneDeiDecimali();
    //jQuery(".decimale-negativo").gestioneDeiDecimaliNegativi();
    jQuery(".decimale-negativo").gestioneDeiDecimali(-1);

    

    jQuery("form").on("click", ".reset", function(e) {
        // Reset del form al click sul pulsante di classe reset
        e.preventDefault();
        jQuery(e.delegateTarget).reset();
    }).on("click", ".submit", function(e) {
        // Submit del form al click sul pulsante di classe submit
        e.preventDefault();
        jQuery(e.delegateTarget).submit();
    }).on("reset", function(e) {
        var campi;
        var $this = jQuery(this);
        // Cancello tutti i dati con il reset. Non ripristino i campi iniziali
        e.preventDefault();
        // Pericoloso?
        $this.find("input[type='checkbox'], input[type='radio']")
            .not("*[data-maintain]")
                .removeAttr("checked");

        campi = $this.find(":input")
            .not("*[data-maintain], input[type='hidden'], input[type='button'], input[type='submit'], input[type='reset'], input[type='radio'], button");
        campi.val("");
        e.campi = campi;
    })

});
