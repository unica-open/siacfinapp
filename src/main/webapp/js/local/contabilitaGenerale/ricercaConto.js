/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
    "use strict";
    var exports = {};
    var Conto;
    var alertErroriModale = $("#ERRORI_modaleRicercaConto");

    var zTreeSettingsBase = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "all"
        },
        data: {
            key: {
                name: "testo",
                children: "sottoElementi"
            },
            simpleData: {
                enable: true
            }
        }
    };

    function selectVuota (objectVal) {
        return !objectVal;
    }

    /**
     * Impostazione della tabella con i conti figli ottenuti dalla ricerca.
     */
    function impostaTabellaRisultati() {
        var tableId = "#tabellaRisultatiRicercaConto";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaContoAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti conti per i parametri selezionati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun conto disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Conti trovati") : ("1 Conto trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: [
                 {aTargets : [ 0 ], mData : function() {
                    return "<input type='radio' name='checkConto'/>";
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalConto", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('livello')},
                {aTargets: [2], mData: defaultPerDataTable('codice')},
                {aTargets: [3], mData: defaultPerDataTable('descrizione')},
                {aTargets: [4], mData: defaultPerDataTable('contoDiLegge', '', formatSN)},
                {aTargets: [5], mData: defaultPerDataTable('contoFoglia', '', formatSN)}
            ]
        };
        $(tableId).dataTable(opts);
    }

    function formatSN(flag) {
        return !!flag ? "S" : "N";
    }

    function popolaTabella(dataconto){
        impostaTabellaRisultati();
    }

   
    /**
     * Costruttore per il conto.
     *
     * @param selectorClasse           (String)  il selettore CSS della classe del pianod ei conti
     * @param selectorCodificaInterna  (String)  il selettore CSS del codice interno del conto
     * @param selectorCodiceConto      (String)  il selettore CSS del codice  del conto
     * @param selectorDescrizioneConto (String)  il selettore CSS della descrizione del conto
     * @param automaticSelectConto     (Boolean) se la gestione della select del conto sia da effettuarsi in automatico (Optional - default: true)
     */
     Conto = function(selectorClasse, selectorCodificaInterna, selectorCodiceConto, selectorDescrizioneConto, automaticSelectConto) {
        // Campi della pagina
        this.$classe = $(selectorClasse);
        this.$codice = $(selectorCodiceConto);
        this.$descrizione = $(selectorDescrizioneConto);
        this.automaticSelectConto = automaticSelectConto !== undefined ? !!automaticSelectConto : true;

        // I campi del modale
        this.$fieldsetRicerca = $("#fieldsetModaleRicercaConto");
        this.$classeModale = $("#classePianoDeiConti_modale");
        this.$codiceModale = $("#codicePianoDeiContiRicerca_modale");
        this.$titoloSpesaModale = $("#titoloSpesa");
        this.$macroaggregatoModale = $("#macroaggregato");
        this.$titoloEntrataModale = $("#titoloEntrata");
        this.$tipologiaTitoloModale = $("#tipologiaTitolo");
        this.$categoriaTipologiaTitoloModale = $("#categoriaTipologiaTitolo");
        this.$livelloPianoDeiContiRicerca = $("#livelloPianoDeiContiRicerca_modale");
        
        this.$divRisultati = $("#risultatiRicercaConto");
        this.$spinnerRicerca = $("#spinnerModaleRicercaPDC");
        this.$tabellaRisultatiRicercaConto = $("#tabellaRisultatiRicercaConto");
        this.$bottoneCercaModaleRicercaPDCModale = $("#bottoneCercaModaleRicercaPDC");
        this.$pulsanteConfermaModaleRicercaConto = $("#pulsanteConfermaModaleRicercaConto");
        this.$modal = $("#comp-CodConto");
        this.isClasseStartEmpty= false;
    };

    /**
     * Ricerca il piano dei conti.
     */
    Conto.prototype.ricercaPianoDeiConti = function() {
        alertErroriModale.slideUp();
        var obj = this.$fieldsetRicerca.serializeObject();
        if (this.$classeModale.is(":disabled")) {
            obj['conto.pianoDeiConti.classePiano.uid'] = this.$classeModale.val();
        }
        var spinner = this.$spinnerRicerca.addClass("activated");

        this.$divRisultati.slideUp();
        var divRisultatiRicercaConto = this.$divRisultati;
        // Effettuo la ricerca
        $.postJSON("ricercaConto_effettuaRicercaModale.do", obj)
        .then(function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            popolaTabella(data.conto);
            divRisultatiRicercaConto.slideDown();
        }).always(function() {
            spinner.removeClass("activated");
        });
    };
    Conto.prototype.cleanAll = function (){
    	 this.$codiceModale.val("");
    	 
    	 this.$titoloSpesaModale.val("");
         this.$macroaggregatoModale.val("");
         this.$titoloEntrataModale.val("");
         this.$tipologiaTitoloModale.val("");
         this.$categoriaTipologiaTitoloModale.val("");
         this.$livelloPianoDeiContiRicerca.val("");
        
         this.svuotaPDC();
         $("input[type='radio'][data-spesa]").removeAttr("checked");
         $("input[type='radio'][data-entrata]").removeAttr("checked");
         //disabilito e nascondo dove serve
         
    };
    
    Conto.prototype.resetAll = function() {
    	this.cleanAll();
    };
    Conto.prototype.apriModale = function() {
        this.init();
        this.$fieldsetRicerca.find(":input").not("[data-maintain]").val("");
        this.isClasseStartEmpty = (this.$classeModale.val() === "" || this.$classeModale.val() === "0") ;
        //Popolo campi modale
        if(this.automaticSelectConto) {
            this.$classeModale.val(this.$classe.val());
            this.abilitaDisabilitaPerClasse();
        } else {
            this.$classeModale.removeAttr("disabled");
        }
        this.cleanAll();
        this.$codiceModale.val(this.$codice.val());

        
        if($("#HIDDEN_ambitoContoFIN").val() === $("#HIDDEN_ambitoConto").val()){
        	$("#divContoFIN").slideDown();
        }else{
        	$("#divContoFIN").slideUp();
        }
       
        this.$divRisultati.slideUp();
        alertErroriModale.slideUp();
        this.$modal.modal("show");
    };

    Conto.prototype.svuotaPDC = function() {
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
        impostaZTree("treePDC", zTreeSettingsBase, null);
        this.$tabellaRisultatiRicercaConto.find("[name='checkConto']:checked").attr("checked", false);
    };

    Conto.prototype.abilitaDisabilitaPerClasse = function() {
    	//azzero/disabilito/nascondo i campi perchè in ogni caso ho modificato il campo classe
    	this.resetAll();
        if (this.$classeModale.val() === "" || this.$classeModale.val() === "0") {
            this.$classeModale.removeAttr("disabled");
            $(":input[data-noclassepdc]").attr("disabled", true);
            
            
        } else {
            $(":input[data-noclassepdc]").removeAttr("disabled");
            //disabilito la select perchè ha un valore dalla pagina principale
            if (!this.isClasseStartEmpty) {
            	this.$classeModale.attr("disabled", true);
            }
        }
        this.gestioneEntrateSpese();
        
    };

    Conto.prototype.gestisciMacroaggregato = function() {
        if (selectVuota(this.$titoloSpesaModale.val())) {
            this.$macroaggregatoModale.val("");
            this.$macroaggregatoModale.attr("disabled", true);
        } else {
            this.$macroaggregatoModale.removeAttr("disabled");
            this.caricaMacroAgregato();
        }
    };

    Conto.prototype.gestisciTipologia = function() {
        if (selectVuota(this.$titoloEntrataModale.val())) {
            this.$tipologiaTitoloModale.val ("");
            this.$tipologiaTitoloModale.attr("disabled", true);
            this.$categoriaTipologiaTitoloModale.val("");
            this.$categoriaTipologiaTitoloModale.attr("disabled", true);
        } else {
            this.$tipologiaTitoloModale.removeAttr("disabled");
            this.caricaTipologia();
        }
    };

    Conto.prototype.gestisciCategoria= function() {
        if (selectVuota(this.$tipologiaTitoloModale.val())) {
            this.$categoriaTipologiaTitoloModale.val("");
            this.$categoriaTipologiaTitoloModale.attr("disabled", true);
        } else {
            this.$categoriaTipologiaTitoloModale.removeAttr("disabled");
            this.caricaCategoria();
        }
    };

    Conto.prototype.gestioneEntrateSpese = function() {
        if ($("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaConto").is(":checked")){
            $("#campiSpesa").show();

            this.$titoloSpesaModale.removeAttr("disabled");
            if (!selectVuota(this.$titoloSpesaModale.val())) {
                this.$macroaggregatoModale.removeAttr("disabled");
            }

            $("#campiEntrata").hide();
            $(":input[data-campoentrata]", "#fieldsetModaleRicercaConto").attr("disabled", true);

        }
        if ($("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaConto").is(":checked")){
            $("#campiSpesa").hide();
            $(":input[data-campospesa]", "#fieldsetModaleRicercaConto").attr("disabled", true);
            $("#campiEntrata").show();
            this.$titoloEntrataModale.removeAttr("disabled");
            if (!selectVuota(this.$titoloEntrataModale.val())) {
                this.$tipologiaTitoloModale.removeAttr("disabled");
                if (!selectVuota(this.$tipologiaTitoloModale.val())) {
                    this.$categoriaTipologiaTitoloModale.removeAttr("disabled");
                }
            }
        }
        if (!$("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaConto").is(":checked")
        	&& !$("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaConto").is(":checked")){
        	 $("#campiEntrata").hide();
        	 $("#campiSpesa").hide();
        	
        }
        	
    };



    /* ***** Funzioni per le chiamate AJAX *****/
    /**
     * Carica i dati nella select della Tipologia da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    Conto.prototype.caricaTipologia = function () {
        var idTitoloEntrata = $("#titoloEntrata").val();
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti

        // Pulisco la select della categoria
        $("#categoriaTipologiaTitolo").val("");

        return $.postJSON("ajax/tipologiaTitoloAjax.do", {"id" : idTitoloEntrata})
        .then(function (data) {
            var listaTipologiaTitolo = (data.listaTipologiaTitolo);
            var errori = data.errori;
            var options = $("#tipologiaTitolo");
            var selectCategoria = $("#categoriaTipologiaTitolo");
            var bottonePdC = $("#bottonePdC");

            options.find('option').remove().end();

            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                selectCategoria.attr("disabled", "disabled");
                bottonePdC.attr("disabled", "disabled");
                return;
            }
            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            if (selectCategoria.attr("disabled") !== "disabled") {
                selectCategoria.attr("disabled", "disabled");
            }
            if (bottonePdC.attr("disabled") !== "disabled") {
                bottonePdC.attr("disabled", "disabled");
            }

            options.append($("<option />").val("").text(""));
            $.each(listaTipologiaTitolo, function () {
                options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
            });
        });
    };

    /**
     * Carica i dati nella select della Categoria da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    Conto.prototype.caricaCategoria = function () {
        var idTipologiaTitolo = $("#tipologiaTitolo").val();

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");

        return $.postJSON("ajax/categoriaTipologiaTitoloAjax.do", {"id" : idTipologiaTitolo})
        .then(function (data) {
            var listaCategoriaTipologiaTitolo = (data.listaCategoriaTipologiaTitolo);
            var errori = data.errori;
            var options = $("#categoriaTipologiaTitolo");
            var bottonePdC = $("#bottonePdC");

            options.find('option').remove().end();

            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                return;
            }

            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            if (bottonePdC.attr("disabled") !== "disabled") {
                bottonePdC.attr("disabled", "disabled");
            }

            options.append($("<option />").val("").text(""));
            $.each(listaCategoriaTipologiaTitolo, function () {
                options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
            });
        });
    };

    /**
     * Carica i dati nella select del Macroaggregato da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    Conto.prototype.caricaMacroaggregato = function () {
        var idTitoloSpesa = $("#titoloSpesa").val();

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");

        // Effettuo la chiamata JSON
        return $.postJSON("ajax/macroaggregatoAjax.do", {"id" : idTitoloSpesa})
        .then(function (data) {
            var listaMacroaggregato = (data.listaMacroaggregato);
            var errori = data.errori;
            var options = $("#macroaggregato");
            var bottonePdC = $("#bottonePdC");

            options.find('option').remove().end();
            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                return;
            }

            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            if (bottonePdC.attr("disabled") !== "disabled") {
                bottonePdC.attr("disabled", "disabled");
            }

            options.append($("<option />").val("").text(""));
            $.each(listaMacroaggregato, function () {
                options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
            });
        });
    };

    /**
     * Prepara i parametri per la Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     *
     * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    Conto.prototype.gestisciPDC = function () {
         if (!selectVuota(this.$categoriaTipologiaTitoloModale.val())) {
             this.caricaPianoDeiConti (this.$categoriaTipologiaTitoloModale);
         }
         if (!selectVuota(this.$macroaggregatoModale.val())) {
             this.caricaPianoDeiConti (this.$macroaggregatoModale);
         }
         if ((selectVuota(this.$categoriaTipologiaTitoloModale.val())) && (selectVuota(this.$macroaggregatoModale.val()))) {
            $("#HIDDEN_ElementoPianoDeiContiUid").val("");
            $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
            $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
         }
    };
    /**
     * Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     * @param obj {Object} l'oggetto chiamante
     *
     * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    Conto.prototype.caricaPianoDeiConti = function (obj) {
        var id = obj.val();
        /* Settings dello zTree */
        var zTreeSettings = $.extend(true, {}, zTreeSettingsBase, {callback: {beforeCheck: controllaLivelloPianoDeiConti, onCheck: impostaValueElementoPianoDeiConti}});
        /* Spinner */
        var spinner = $("#SPINNER_ElementoPianoDeiConti");
        var self = this;

        /* Attiva lo spinner */
        spinner.addClass("activated");

        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");

        return $.postJSON("ajax/elementoPianoDeiContiAjax.do", {"id" : id})
        .then(function (data) {
            var listaElementoPianoDeiConti = (data.listaElementoCodifica);
            var options = $("#bottonePdC");
            var elementoPianoDeiContiGiaSelezionato = $("#HIDDEN_ElementoPianoDeiContiUid").val();
            var codiceTitolo;
            var albero;
            var isCodiceTitoloImpostato;
            var isListaElementoPianoDeiContiNonVuota = listaElementoPianoDeiConti && listaElementoPianoDeiConti.length > 0;

            if (!selectVuota(self.$titoloEntrataModale.val())) {
                codiceTitolo = self.$titoloEntrataModale.val();
                isCodiceTitoloImpostato = !!codiceTitolo;
            }
            if (!selectVuota(self.$titoloSpesaModale.val())) {
                codiceTitolo = self.$titoloEntrataModale.val();
                isCodiceTitoloImpostato = !!codiceTitolo;
            }
            impostaZTree("treePDC", zTreeSettings, listaElementoPianoDeiConti);
            // Se il bottone è disabilitato, lo si riabiliti
            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }

            // Controllo se sono nel caso d'uso di entrata: in tal caso, effettuo il check del primo elemento nel caso in cui non vi sia un altro elemento selezionato
            if(isCodiceTitoloImpostato && elementoPianoDeiContiGiaSelezionato === "" && isListaElementoPianoDeiContiNonVuota) {
                albero = $.fn.zTree.getZTreeObj("treePDC");
                albero && albero.checkNode(albero.getNodes()[0], true, true, true);
            }
        }).always(function() {
            // Disattiva lo spinner anche in caso di errore
            spinner.removeClass("activated");
        });
    };

    /* ***** zTree *****/

    /**
     * Funzione per la creazione di una stringa contenente le informazioni gerarchiche della selezione. Genera solo il codice degli elementi non-foglia.
     *
     * @param treeNode       {Object}  il nodo selezionato
     * @param messaggioVuoto {String}  messaggio da impostare qualora non sia stato selezionato nulla
     * @param regressione    {Boolean} indica se effettuare una regressione sui vari codici
     *
     * @returns {String} la stringa gerarchica creata
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        var nodes = treeNode;
        var parent;
        var string;

        if (!nodes.checked) {
            return messaggioVuoto;
        }
        if (!regressione) {
            return nodes.testo;
        }

        string = nodes.testo;
        parent = nodes.getParentNode();
        while (parent !== null) {
            nodes = parent;
            string = nodes.codice + " - " + string;
            parent = nodes.getParentNode();
        }

        return string;
    }

    /**
     * Funzione per l'impostazione dello zTree.
     *
     * @param idList       {String} l'id dell'elemento in cui sar&agrave; posto lo zTree
     * @param setting      {Object} le impostazioni dello zTree
     * @param jsonVariable {Object} la variabile con i dati JSON per il popolamento dello zTree
     */
    function impostaZTree(idList, setting, jsonVariable) {
        var tree = $.fn.zTree.init($("#" + idList), setting, jsonVariable);
        var idCampo = "ElementoPianoDeiConti";
        var uid = parseInt($("#HIDDEN_" + idCampo + "Uid").val(), 10);
        var node;
        if (jsonVariable== null) {
            tree.destroy();
        } else {
            // Se l'uid è selezionato l'elemento corrispondente
            if(!isNaN(uid)) {
                node = tree.getNodeByParam("uid", uid);
                // Evito il check nel caso in cui il nodo sia null
                !!node && tree.checkNode(node, true, true, true);
            }
        }
    }

    /**
     * Metodo di utilit&agrave; per l'impostazione dei dati da zTree a un campo hidden.
     *
     * @param treeNode      {Object} l'oggetto JSON corrispondente alla selezione
     * @param idCampoHidden {String} l'id del campo hidden, senza la sotto-stringa HIDDEN_
     * @param stringa       {String} la stringa contenente la descrizione estesa (ovvero comprendente la descrizione degli elementi superiori in gerarchia) della selezione
     */
    function valorizzaCampi(treeNode, idCampoHidden, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "Uid").val(treeNode.uid);
        } else {
            $("#HIDDEN_" + idCampoHidden + "Uid").val("");
        }
        $("#HIDDEN_" + idCampoHidden + "Stringa").val(stringa);
        $("#HIDDEN_" + idCampoHidden + "CodiceTipoClassificatore").val(treeNode.codiceTipo);
        $("#SPAN_" + idCampoHidden).html(stringa);
    }

    /**
     * Calcola il livello del piano dei conti.
     *
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Integer} il livello del piano dei conti
     */
    function calcolaLivelloPianoDeiConti(treeNode) {
        var array = treeNode.codice.split(".");
        var index;
        for(index = 1; index < array.length && array[index] > 0; index++) {/* Empty */}
        return --index;
    }

    /**
     * Controllare che la selezione dell'elemento del Piano dei Conti corrisponda almeno al quarto livello dello stesso.
     *
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Boolean} <code>true</code> qualora il livello selezionato sia valido, e scatena pertanto l'evento onClick;
     *                    <code>false</code> in caso contrario, e inibisce lo scatenarsi dell'evento onclick.
     */
    function controllaLivelloPianoDeiConti(treeId, treeNode) {
        var livello;
        if(treeId === undefined) {
            throw new ReferenceError("Nessun treeId fornito");
        }
        livello = calcolaLivelloPianoDeiConti(treeNode);

        // Se il livello non è almeno pari a 4, segnalo l'errore
        if (livello <= 4) {
            bootboxAlert("Selezionare almeno il quinto livello del Piano dei Conti. Livello selezionato: " + livello);
        }
        return livello > 4;
    }

    /**
     * Imposta il valore dell'Elemento del Piano Dei Conti in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueElementoPianoDeiConti(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessun P.d.C. finanziario selezionato", false);
        valorizzaCampi(treeNode, "ElementoPianoDeiConti", stringa);
    }

    Conto.prototype.init = function() {
        var self = this;

        $("input[type='radio']", "#fieldsetModaleRicercaConto").substituteHandler("change", self.gestioneEntrateSpese.bind(self));

        $("#campiSpesa").hide();
        $(":input[data-campospesa]", "#fieldsetModaleRicercaConto").attr("disabled", true);
        $("#campiEntrata").hide();
        $(":input[data-campoentrata]", "#fieldsetModaleRicercaConto").attr("disabled", true);

        this.$classeModale.substituteHandler("change", self.abilitaDisabilitaPerClasse.bind(self));
        this.$titoloSpesaModale.substituteHandler("change", self.caricaMacroaggregato.bind(self));
        this.$macroaggregatoModale.substituteHandler("change", self.gestisciPDC.bind(self));
        this.$titoloEntrataModale.substituteHandler("change", self.gestisciTipologia.bind(self));
        this.$tipologiaTitoloModale.substituteHandler("change", self.gestisciCategoria.bind(self));
        this.$categoriaTipologiaTitoloModale.substituteHandler("change", self.gestisciPDC.bind(self));
        this.$bottoneCercaModaleRicercaPDCModale.substituteHandler("click", self.ricercaPianoDeiConti.bind(self));
        this.$pulsanteConfermaModaleRicercaConto.substituteHandler("click", self.impostaConto.bind(self));
    };

    /**
     * Imposta i dati della conto all'interno dei campi selezionati.
     *
     * @returns (Conto) l'oggetto su cui e' atata effettuata l'invocazione
     */
    Conto.prototype.impostaConto = function() {
        var checkedConto= this.$tabellaRisultatiRicercaConto.find("[name='checkConto']:checked");

        // I campi da popolare
        var conto;
        var contoConto;

        // Se non ho selezionato nulla, esco subito
        if(checkedConto.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un conto"], alertErroriModale, false);
            return;
        }

        // Chiudo il modale e distruggo il datatable
        this.$modal.modal("hide");

        // Ottengo i dati del soggetto salvati
        conto = checkedConto.data("originalConto");
        // Imposto i valori dei campi
        contoConto = conto.Conto;

        // Copio i campi ove adeguato
        this.$classe.val(conto.pianoDeiConti.classePiano.uid);
        this.$codice.val(conto.codice);
        this.$descrizione.html(conto.descrizione);
        this.$codice.trigger('contoCaricato', conto);
    };

    exports.inizializza = function(selectorClasse, selectorCodificaInterna, selectorCodiceConto, selectorDescrizioneConto, selectorPulsante, automaticSelectConto) {
        var datiConto = new Conto(selectorClasse, selectorCodificaInterna, selectorCodiceConto, selectorDescrizioneConto, automaticSelectConto);
        $(selectorPulsante).click(datiConto.apriModale.bind(datiConto));
    };

    // Esportazione delle funzionalita'
    global.Conto = exports;

}(jQuery, this);