<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="comments" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	<acme:column code="comment.title" property="title"/>
	
	<acme:column code="comment.text" property="text"/>
	
	<acme:column code="comment.moment" property="moment"/>
	
	<acme:column code="comment.stars" property="stars"/>
	
	<security:authorize access="hasRole('ADMIN')">
		<acme:column code="comment.banned" property="banned"/>
		
		<display:column title="">
			<jstl:if test="${row.banned == false}" >
				<jstl:if test="${tripId == null}">
					<acme:link href="comment/ban.do?commentId=${row.id}" code="comment.ban"/>
				</jstl:if>
				<jstl:if test="${tripId != null}">
					<acme:link href="comment/banTrip.do?tripId=${tripId}&commentId=${row.id}" code="comment.ban"/>
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<acme:column code="comment.actor" property="actor.name"/>
	
	<jstl:if test="${tripId == null}">
		<acme:column code="comment.commentableEntity" property="commentableEntity.name"/>
	</jstl:if>
	<jstl:if test="${tripId != null}">
		<acme:column code="comment.commentableEntity" property="commentableEntity.title"/>
	</jstl:if>
	
</display:table>

<!-- Action links -->
<security:authorize access="isAuthenticated()">
	<jstl:if test="${tripId == null}">
		<acme:link href="comment/create.do" code="misc.create"/>
	</jstl:if>
	<jstl:if test="${tripId != null}">
		<acme:link href="comment/createTrip.do?tripId=${tripId}" code="misc.create"/>
	</jstl:if>
</security:authorize>