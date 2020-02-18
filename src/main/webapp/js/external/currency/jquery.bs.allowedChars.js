/* Overlay for divs */
(function ($) {
    "use strict";

    // Inner variables definition
    var AllowedChars,               // Constructor
        old,                        // The old variable
        numRegExp = /[\-+0-9\.\,]/; // A commonly-used RegExp, representing numbers with decimal separators (in Italian and English locale) 

    // ALLOWEDCHARS CLASS DEFINITION
    // ========================
    AllowedChars = function (element, options) {
        var opts = $.extend(true, {}, $.fn.allowedChars.defaults, options);
        this.$element = $(element);
        this.$regExp = new RegExp(opts.regExp);
        if (opts.numeric) {
            this.$regExp = numRegExp;
        }
        this.bind();
    };

    /** Definition of the constructor */
    AllowedChars.prototype.constructor = AllowedChars;

    /** Binds the control to the element. */
    AllowedChars.prototype.bind = function () {
        var self = this;

        function filter(e) {
            var text = null,
                code;
            if (e.type === "textinput" || e.type === "textInput") {
                text = (e.originalEvent) ? e.originalEvent.data : e.data;
            } else {
                code = e.charCode || e.keyCode;
                if (code < 32 || e.charCode === 0 || e.ctrlKey || e.altKey) {
                    return;
                }
                text = String.fromCharCode(code);
            }

            if (!self.$regExp.test(text)) {
                e.preventDefault();
                e.returnValue = false;
                return false;
            }
        }

        this.$element.on("keypress textInput textinput", filter);
    };

    /** Unbinds the control from the element. */
    AllowedChars.prototype.unbind = function () {
        this.$element.off("keypress textInput textinput");
    };

    // ALLOWEDCHARS PLUGIN DEFINITION
    // ========================

    // Old value for the object
    old = $.fn.allowedChars;

    /**
     * Plugin for jQuery
     */
    $.fn.allowedChars = function (option) {
        return this.each(function () {
            var self = $(this),
                data = self.data("allowed-characters"),
                optionToUse = option || {regExp: self.data("allowed-chars")},
                options = (typeof optionToUse === "object") && optionToUse;

            if (!data) {
                data = new AllowedChars(this, options);
                self.data("allowed-characters", data);
            }
            if (typeof option === "string") {
                data[option]();
            }
        });
    };

    $.fn.allowedChars.defaults = {
        regExp: '.',   // Every character allowed
        numeric: false // Regexp for numeric purpose
    };

    // ALLOWEDCHARS NO CONFLICT
    // ==================
    $.fn.allowedChars.noConflict = function () {
        $.fn.allowedChars = old;
        return this;
    };

    // ALLOWEDCHARS DATA API
    // ================
    // Initialises the overlay for each element with 'data-allowed-chars' set
    $("[data-allowed-chars]").allowedChars();
}(window.jQuery));