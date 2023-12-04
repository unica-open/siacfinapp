<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="r" uri="http://www.csi.it/taglibs/remincl-1.0"%>
<%@taglib prefix="display" uri="/display-tags"%>

	
	
<script type="text/javascript">

	$("#linkTabQuote").click(function() {
		$.ajax({
			//task-131 --> url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="%{#cambiaTabFolderAction}"/>',
			type: 'POST',
			data: 'tabFolder=tabQuote',
		    success: function(data)  {
			}
		});
	});	
	
	$("#linkTabOrdinativiCollegati").click(function() {
		$.ajax({
			//task-131 --> url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="%{#cambiaTabFolderAction}"/>',
			type: 'POST',
			data: 'tabFolder=tabOrdinativiCollegati',
		    success: function(data)  {
			}
		});
	});	
	
	$("#linkTabOrdinativoIncasso").click(function() {
		$.ajax({
			// task-131 --> url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="%{#cambiaTabFolderAction}"/>',
			type: 'POST',
			data: 'tabFolder=tabOrdinativoIncasso',
		    success: function(data)  {
			}
		});
	});
	
	$("#linkTabOrdinativoPagamento").click(function() {
		$.ajax({
			//task-131 --> url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="%{#cambiaTabFolderAction}"/>',
			type: 'POST',
			data: 'tabFolder=tabOrdinativoPagamento',
		    success: function(data)  {
			}
		});
	});
	
	$("#linkTabProvvisori").click(function() {
		$.ajax({
			//task-131 --> url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="%{#cambiaTabFolderAction}"/>',
			type: 'POST',
			data: 'tabFolder=tabProvvisori',
		    success: function(data)  {
			}
		});
	});

</script>