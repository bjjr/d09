<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Search Form -->

<form:form action="" modelAttribute="offer">
	<input type="text" name="offer"/>
	<input type="submit" name="search" value="<spring:message code="offer.search"/>"/>
</form:form>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="offers" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<acme:column code="trip.title" property="title"/>
	
	<acme:column code="trip.description" property="description"/>
	
	<acme:column code="trip.moment" property="moment"/>
	
	<acme:column code="trip.origin" property="origin"/>
	
	<acme:column code="trip.destination" property="destination"/>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<display:column>
			<jstl:if test="${isApplied == false}">
				<a href="application/customer/create.do?offerId=${row.id}">
					<spring:message code="trip.makeAnApplication"/>
				</a>
			</jstl:if>
			<jstl:if test="${isApplied == true}">
				<spring:message code="trip.applicationDone"/>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<display:column>
		<a href="comment/createTrip.do?tripId=${row.id}">
			<spring:message code="trip.writeComment"/>
		</a>
	</display:column>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${row.banned == false}">
				<a href="offer/administrator/ban.do?offerId=${row.id}">
					<spring:message code="trip.ban"/>
				</a>
			</jstl:if>
			<jstl:if test="${row.banned == true}">
				<spring:message code="trip.banned" />
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>