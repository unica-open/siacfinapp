

$(document).ready(function() {
	jQuery("input.datepicker").each(function() {
	    var self = jQuery(this).datepicker({
	        weekStart : 1,
	        language : "it",
	        startDate : "01/01/1901",
	        autoclose : true,
	        daysOfWeekHighlighted: "0,6",
	        todayBtn: "linked"
	    }).attr("tabindex", -1);
	});
});


