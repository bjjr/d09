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

<form:form action="${formAction}" modelAttribute="comment">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="comment.title" path="title" />
	<br />
	
	<acme:textbox code="comment.text" path="text" />
	<br />
	
	<acme:textbox code="comment.stars" path="stars" />
	<br />
	
	<acme:textbox code="comment.moment" path="moment" readonly="true"/>
	<br />
	
	<jstl:if test="${isTrip == false}">
		<acme:select items="${commentableEntities}" itemLabel="name" code="comment.commentableEntity" path="commentableEntity"/>
		<br />
	</jstl:if>
	
	<div>
		<jstl:if test="${isTrip == true}">
			<acme:submit name="saveAsTrip" code="misc.save"/>
		</jstl:if>
		<jstl:if test="${isTrip == false}">
			<acme:submit name="save" code="misc.save"/>
		</jstl:if>
		<acme:cancel url="comment/list.do" code="misc.cancel"/>
	</div>

</form:form>