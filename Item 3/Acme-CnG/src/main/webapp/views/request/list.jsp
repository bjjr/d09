<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${myRequests == true}">

	<display:table pagesize="5" class="displaytag" keepStatus="true"
		name="requests" requestURI="${requestURI}" id="row">
	
		<!-- Attributes -->
		
		<acme:column code="trip.title" property="title" sortable="true"/>
	
		<acme:column code="trip.description" property="description"/>
	
		<acme:column code="trip.moment" property="moment" sortable="true"/>
		
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
	
	</display:table>
	<br />

	<input type="button" name="create"
		value="<spring:message code="request.create" />"
		onclick="window.location='request/customer/create.do'" />
	<br />

</jstl:if>

<jstl:if test="${myRequests == false}">

	<!-- Search Form -->
	
	<security:authorize access="hasRole('CUSTOMER')">
		<form:form>
			<input type="text" name="keyword"/>
			<input type="submit" name="search" value="<spring:message code="request.search"/>"/>
		</form:form>
		<br />
	</security:authorize>

	<!-- Listing grid -->

	<display:table pagesize="5" class="displaytag" keepStatus="true"
		name="requests" requestURI="${requestURI}" id="row">
	
		<!-- Attributes -->
	
		<acme:column code="trip.title" property="title"/>
	
		<acme:column code="trip.description" property="description"/>
	
		<acme:column code="trip.moment" property="moment"/>
	
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
				<jstl:if test="${myRequestsWithApplicationsMine.size() == 0}">
					<a href="application/customer/create.do?tripId=${row.id}">
						<spring:message code="trip.makeAnApplication"/>
					</a>
				</jstl:if>
				<jstl:if test="${myRequestsWithApplicationsMine.size() != 0}">
					<jstl:if test="${myRequestsWithApplicationsMine.contains(row)}">
						<spring:message code="trip.applicationDone"/>
					</jstl:if>
					<jstl:if test="${!myRequestsWithApplicationsMine.contains(row)}">
						<a href="application/customer/create.do?tripId=${row.id}">
							<spring:message code="trip.makeAnApplication"/>
						</a>
					</jstl:if>
				</jstl:if>
			</display:column>
		</security:authorize>
	
		<security:authorize access="hasRole('ADMIN')">
			<display:column>
				<jstl:if test="${row.banned == false}">
					<acme:link href="request/administrator/ban.do?requestId=${row.id}" code="trip.ban"/>
				</jstl:if>
				<jstl:if test="${row.banned == true}">
					<spring:message code="trip.banned" />
				</jstl:if>
			</display:column>
		</security:authorize>

	</display:table>
</jstl:if>
