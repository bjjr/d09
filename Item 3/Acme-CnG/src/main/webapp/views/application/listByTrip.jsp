<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="applications" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<acme:column code="actor.name" property="customer.name" />

	<acme:column code="application.trip" property="trip.title" />

	<acme:column code="application.status" property="status" />

	<jstl:if test="${row.status=='PENDING'}">
	<display:column>
		<acme:link
			href="application/customer/accept.do?applicationId=${row.id }"
			code="application.accept" />
	</display:column>
	
	<display:column>
		<acme:link
			href="application/customer/deny.do?applicationId=${row.id }"
			code="application.deny" />
	</display:column>
		
	</jstl:if>

</display:table>