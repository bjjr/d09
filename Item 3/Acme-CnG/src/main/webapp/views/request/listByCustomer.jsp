<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="offers" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<acme:column code="trip.title" property="title"/>
	
	<acme:column code="trip.description" property="description"/>
	
	<acme:column code="trip.moment" property="moment"/>
	
	<acme:column code="trip.origin" property="origin"/>
	
	<acme:column code="trip.destination" property="destination"/>
	
	<display:column>
		<a href="application/customer/list.do?offerId=${row.id}">
			<spring:message code="trip.applications"/>
		</a>
	</display:column>
	
	<display:column>
		<a href="offer/customer/edit.do?offerId=${row.id}">
			<spring:message code="misc.edit"/>
		</a>
	</display:column>
	
</display:table>

<input type="button" name="create"
		value="<spring:message code="offer.create" />"
		onclick="window.location='offer/customer/create.do'" />
<br />