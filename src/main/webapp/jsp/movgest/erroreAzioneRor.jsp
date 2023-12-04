<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>	
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<fmt:setBundle basename="globalMessages"/>
	<s:include value="/jsp/include/header.jsp" />

<div class="container-fluid"> 
		<div class="row-fluid">
			<div class="span12 contentPage">

				<div>

					<div id="applicationError">
						<h4>
							<fmt:message key="error.fatalerror.warning" /><br />
							<fmt:message key="error.unauthorized.fasemessage" />	
						</h4>
					</div>
					<div id="applicationError_links">
						<div>
							<s:a action="redirectToCruscotto" cssClass="btn btn-secondary"><fmt:message key="error.fatalerror.btnmsg" /></s:a>
						</div>
					</div>
					
					
					
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript">
		
		//$("#applicationError").on("tripleclick", function() {$("#exceptionInformations").toggle();});
		
		
		$(document).ready(function() {
			
			$("#exceptionInformations").hide();
								   
		    $("#buttonEccezione").click(function() {
		 	  
		    	$("#exceptionInformations").toggle();

			});	
			
		});	
		
	</script>
</body>
</html>