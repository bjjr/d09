<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="offer/customer/edit.do" modelAttribute="offer">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="moment"/>
	<form:hidden path="banned"/>
	
	<acme:textbox code="trip.title" path="title" />
	
	<acme:textarea code="trip.description" path="description" />
	
	<spring:message code="trip.origin" />
	<acme:textbox code="place.address" path="address" />
	<acme:textbox code="place.coordinates" path="coordinates" />
	
	<spring:message code="trip.destination" />
	<acme:textbox code="place.address" path="address" />
	<acme:textbox code="place.coordinates" path="coordinates" />
	
	<!-- Buttons -->
	
	<acme:submit name="save" code="misc.save"/>
	
	<acme:cancel url="offer/listByCustomer.do?customerId=${customerId}" code="misc.cancel"/>

</form:form>