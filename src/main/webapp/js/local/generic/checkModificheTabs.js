/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {
	$("input,select").change(function() {
		$(this).closest('form').data('changed', true);
	});
	$('.confermaFinApp').click(function() {
		var supportId = $(this).attr("id").split("_");
		if (supportId != null && supportId.length > 0) {
			$("#forward").val(supportId[0]);
			$("#forceReload").val(supportId[1]);
			$("#ambito").val(supportId[2]);
		}
		if ($(this).closest('form').data('changed')) {
			$("#linkMsgDatipersi").click();
		} else {
			$("#btnGestisciForward").click();
		}
	});
	$('.confermaFinApp').css('cursor', 'pointer');
});
