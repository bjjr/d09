<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="request/customer/create.do" modelAttribute="requestForm">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="banned"/>
	
	<acme:textbox code="trip.title" path="title" />
	
	<acme:textarea code="trip.description" path="description" />
	
	<acme:datebox code="trip.moment" path="moment"/>
	
	<fieldset style="width: 400px">
		<legend align="left">
			<spring:message code="trip.origin" />
		</legend>
		<acme:textbox code="place.address" path="origin.address" />
		<acme:textbox code="place.coordinates" path="origin.coordinates" />
	</fieldset>
	
	<fieldset style="width: 400px">
		<legend align="left">
			<spring:message code="trip.destination" />
		</legend>
		<acme:textbox code="place.address" path="destination.address" />
		<acme:textbox code="place.coordinates" path="destination.coordinates" />
	</fieldset>
	
	<br />
	
	<!-- Buttons -->
	
	<acme:submit name="save" code="misc.save"/>
	
	<input type="button" name="cancel"
		value="<spring:message code="misc.cancel" />"
		onclick="window.location='request/customer/list.do'" />

</form:form>