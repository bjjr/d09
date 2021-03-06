<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${myOffers == true}">

	<display:table pagesize="5" class="displaytag" 
		name="offers" requestURI="${requestURI}" id="row">
	
		<!-- Attributes -->
		
		<acme:column code="trip.title" property="${row.title}" sortable="true"/>
	
		<acme:column code="trip.description" property="${row.description}"/>
	
		<acme:column code="trip.moment" property="${row.moment}" sortable="true"/>
		
		<spring:message code="trip.origin" var="originHeader" />
		<display:column title="${originHeader}">
			<acme:display code="place.address" property="${row.origin.address}"/>
			<acme:display code="place.coordinates" property="${row.origin.coordinates}"/>
		</display:column>
	
		<spring:message code="trip.destination" var="destinationHeader" />
		<display:column title="${destinationHeader}">
			<acme:display code="place.address" property="${row.destination.address}"/>
			<acme:display code="place.coordinates" property="${row.destination.coordinates}"/>
		</display:column>
		
		<display:column>
			<jstl:if test="${row.banned == true}">
				<spring:message code="trip.banned"/>
			</jstl:if>
		</display:column>
		
		<display:column>
			<a href="application/customer/listByTrip.do?tripId=${row.id}">
				<spring:message code="trip.applications"/>
			</a>
		</display:column>
		
		<display:column>
			<a href="comment/listTrip.do?tripId=${row.id}">
				<spring:message code="trip.listComments"/>
			</a>
		</display:column>
	
	</display:table>
	<br />

	<input type="button" name="create"
		value="<spring:message code="offer.create" />"
		onclick="window.location='offer/customer/create.do'" />
	<br />

</jstl:if>

<jstl:if test="${myOffers == false}">

	<!-- Search Form -->
	
	<security:authorize access="hasRole('CUSTOMER')">
		<form:form>
			<input type="text" name="keyword"/>
			<input type="submit" name="search" value="<spring:message code="offer.search"/>"/>
		</form:form>
		<br />
	</security:authorize>

	<!-- Listing grid -->

	<display:table pagesize="5" class="displaytag" 
		name="offers" requestURI="${requestURI}" id="row">
	
		<!-- Attributes -->
	
		<acme:column code="trip.title" property="${row.title}"/>
	
		<acme:column code="trip.description" property="${row.description}"/>
	
		<acme:column code="trip.moment" property="${row.moment}"/>
	
		<spring:message code="trip.origin" var="originHeader" />
		<display:column title="${originHeader}">
			<acme:display code="place.address" property="${row.origin.address}"/>
			<acme:display code="place.coordinates" property="${row.origin.coordinates}"/>
		</display:column>
	
		<spring:message code="trip.destination" var="destinationHeader" />
		<display:column title="${destinationHeader}">
			<acme:display code="place.address" property="${row.destination.address}"/>
			<acme:display code="place.coordinates" property="${row.destination.coordinates}"/>
		</display:column>
	
		<security:authorize access="hasRole('CUSTOMER')">
			<display:column>
				<jstl:if test="${myOffersWithApplicationsMine.size() == 0}">
					<a href="application/customer/create.do?tripId=${row.id}">
						<spring:message code="trip.makeAnApplication"/>
					</a>
				</jstl:if>
				<jstl:if test="${myOffersWithApplicationsMine.size() != 0}">
					<jstl:if test="${myOffersWithApplicationsMine.contains(row)}">
						<spring:message code="trip.applicationDone"/>
					</jstl:if>
					<jstl:if test="${!myOffersWithApplicationsMine.contains(row)}">
						<a href="application/customer/create.do?tripId=${row.id}">
							<spring:message code="trip.makeAnApplication"/>
						</a>
					</jstl:if>
				</jstl:if>
			</display:column>
		</security:authorize>
		
		<display:column>
			<a href="comment/createTrip.do?tripId=${row.id}">
				<spring:message code="trip.writeComment"/>
			</a>
		</display:column>
		
		<display:column>
			<a href="comment/listTrip.do?tripId=${row.id}">
				<spring:message code="trip.listComments"/>
			</a>
		</display:column>
	
		<security:authorize access="hasRole('ADMIN')">
			<display:column>
				<jstl:if test="${row.banned == false}">
					<acme:link href="offer/administrator/ban.do?offerId=${row.id}" code="trip.ban"/>
				</jstl:if>
				<jstl:if test="${row.banned == true}">
					<spring:message code="trip.banned" />
				</jstl:if>
			</display:column>
		</security:authorize>

	</display:table>
</jstl:if>
