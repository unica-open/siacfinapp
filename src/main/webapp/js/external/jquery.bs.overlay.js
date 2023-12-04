/* Overlay for divs */
(function ($) {
    'use strict';

    // Inner variables definition
        // Constructor for the Overlay Object
    var Overlay,
        // the old value stored inside jQuery
        old,
        // Defaults options for the observer
        observerOptions,
        // see https://developer.mozilla.org/en-US/docs/Web/API/MutationObserver
        MutationObserver;

    // Definition for the mutation observer by use in the browser
    MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
    // Definition for the options for the observer
    observerOptions = {subtree: false, attributes: true};

    /**
     * Computes the CSS for an element.
     *
     * @param $elt        (jQuery Object) the element whose css are to be computed
     * @param usePosition (boolean)       if the position should be used to compute the CSS properties
     *
     * @returns (Object) an object representing the CSS properties of the element
     */
    function computeCss($elt, usePosition) {
        var res = {
            position: 'absolute',
            height:   $elt.outerHeight(),
            width:    $elt.outerWidth()
        };
        if(usePosition) {
            res.top = $elt.position().top;
            res.left = $elt.position().left;
        }
        return res;
    }

    /**
     * Registers the observer for the element.
     *
     * @param $elt (jQuery Object)   the element on which the observer should be registered
     * @param obs  (Observer Object) the observer to register
     */
    function registerObserver($elt, obs) {
        var element = $elt.length > 0 && $elt[0];
        if (element) {
            obs.observe(element, observerOptions);
        }
    }

    /**
     * Checks Support for Event.
     *
     * @param eventName (String)        the name of the event
     * @param $elt      (jQuery Object) the element to test support against
     *
     * @return (Boolean) returns result of test - true or false
     */
    function isEventSupported(eventName, $elt) {
        var el = $elt.length > 0 && $elt[0],
            event = 'on' + eventName,
            supported = false;
        if (!el) {
            return false;
        }
        supported = el.hasOwnProperty(event);
        if (!supported) {
            el.setAttribute(event, 'return;');
            supported = (typeof el[event] === 'function');
        }
        return supported;
    }

    /**
     * Initialises the overlay.
     *
     * @param overlay (Overlay Object) the overlay to initialize
     *
     * @returns (Overlay Object) the initialised overlay
     */
    function initialise(overlay) {
        overlay.$overlayed.addClass('div-overlay');
        overlay.$element.before(overlay.$overlayed);
        overlay.$overlayed.hide();
        return overlay;
    }

    // OVERLAY CLASS DEFINITION
    Overlay = function (element, options) {
        this.$element = $(element);
        this.$options = $.extend(true, {}, $.fn.overlay.defaults, options);
        this.$overlayed = $('<div>');
        this.observer = undefined;
        this.$spinner = $();
        if(this.$options.loader) {
            this.$spinner = $('<i>', {'class': 'overlay-spinner spinner icon icon-spin icon-refresh'});
            this.$overlayed.append(this.$spinner);
        }
        if(this.$options.useMessage) {
            this.$messageSpan = $('<div class="overlay-message alert alert-success span8">' + this.$options.message + '</div>');
            this.$overlayed.append(this.$messageSpan);
        }
        if(!this.$options.rebind) {
            initialise(this);
        }
    };

    /** The contructor for the Overlay */
    Overlay.prototype.constructor = Overlay;

    /** Shows the overlay */
    Overlay.prototype.show = function () {
        var e = $.Event('show.overlay'),
            cssOptions = computeCss(this.$element, this.$options.usePosition),
            self = this;

        this.$element.trigger(e);
        if (e.isDefaultPrevented()) {
            return;
        }
        if(this.$options.rebind) {
            this.$overlayed.remove();
            initialise(this);
        }
        this.$overlayed.css(cssOptions)
            .fadeIn();
        if(this.$overlayed.height() > 100) {
            this.$spinner.addClass('activated');
        }
        if(this.$messageSpan){
		    var to = this.$messageSpan.offset().top;
		    jQuery('html, body').animate({
		        scrollTop : to
		    }, 800);
        }
        this.$element.trigger('shown.overlay');

        /**
         * Function to resize the overlay based on the varied css of the relative element.
         */
        function resizer() {
            cssOptions = computeCss(self.$element, self.$options.usePosition);
            self.$overlayed.css(cssOptions);
        }

        // Binds resize of the overlay to a style modification of the undelying element
        if (this.observer) {
            registerObserver(this.$element, this.observer);
        } else if (MutationObserver) {
            this.observer = new MutationObserver(resizer);
            registerObserver(this.$element, this.observer);
        } else if (isEventSupported('DOMAttrModified', this.$element)) {
            // Fallback for older event
            this.$element.on('DOMAttrModified', resizer);
        } else if (isEventSupported('propertychange', this.$element)) {
            // Fallback for older event on IE
            this.$element.on('propertychange', resizer);
        }
        return this.$overlayed;
    };

    /** Hides the overlay */
    Overlay.prototype.hide = function () {
        var e = $.Event('hide.overlay');
        this.$element.trigger(e);
        if (e.isDefaultPrevented()) {
            return;
        }
        this.$spinner.removeClass('activated');
        this.$overlayed.fadeOut();
        this.$element.trigger('hidden.overlay');

        // Unbinds resize
        if (this.observer) {
            this.observer.disconnect();
        } else if (isEventSupported('DOMAttrModified', this.$element)) {
            // Fallback for older event
            this.$element.off('DOMAttrModified');
        } else if (isEventSupported('propertychange', this.$element)) {
            // Fallback for older event on IE
            this.$element.off('propertychange');
        }
        return this.$overlayed;
    };

    /** Refreshes the overlay */
    Overlay.prototype.refresh = function () {
        var cssOptions = computeCss(this.$element, this.$options.usePosition);
        this.$overlayed.css(cssOptions);
        return this.$overlayed;
    };

    /** Removes the overlay */
    Overlay.prototype.remove = function () {
        this.$overlayed.remove();
        this.$element.removeData('overlay');
        this.observer = undefined;
    };

    /** Overrides the definition of the toString method of Object.prototype */
    Overlay.prototype.toString = function () {
        return '[object Overlay]';
    };

    // OVERLAY PLUGIN DEFINITION

    // Old value for the object
    old = $.fn.overlay;

    /**
     * Plugin for jQuery.
     *
     * @param option (String / Object / undefined) the option to pass to the plugin
     * @param args   (Object) the arguments to pass to the plugin's methos
     *
     * @returns (jQuery Object) the jQuery object referring to the overlay
     */
    $.fn.overlay = function (option, args) {
        return this.each(function () {
            var self = $(this),
                data = self.data('overlay'),
                options = (typeof option === 'object') && option;

            if (!data) {
                data = new Overlay(this, options);
                self.data('overlay', data);
            }
            if (typeof option === 'string') {
                data[option](args);
            }
        });
    };

    /** The default options of the plugin */
    $.fn.overlay.defaults = {
        rebind: false,
        loader: false,
        usePosition: false,
        useMessage: false,
        message: 'Operazione in corso, si prega di attendere...'
    };

    // OVERLAY NO CONFLICT
    /**
     * Reverts to the old definition of $().overlay.
     *
     * @returns (Function) the plugin constructor function
     */
    $.fn.overlay.noConflict = function () {
        $.fn.overlay = old;
        return this;
    };

    // OVERLAY DATA API
    // Initialises the overlay for each element with 'data-overlay' set
    $('[data-overlay]').overlay();
}(window.jQuery));