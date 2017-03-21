<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="message/send.do" modelAttribute="message" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:textbox code="message.title" path="title" <jstl:if test="${forward == true }"><jstl:out>readonly="true"</jstl:out></jstl:if> />
	<acme:textbox code="message.text" path="text" <jstl:if test="${forward == true }"><jstl:out>readonly="true"</jstl:out></jstl:if> />
	<acme:textarea code="message.attachments" path="attachments" />
	<acme:select items="${actors}" itemLabel="actor" code="message.recipient" path="recipient" <jstl:if test="${reply == true }"><jstl:out>disabled="true"</jstl:out></jstl:if>/>
	
	<acme:submit name="send" code="message.send"/>
	<acme:cancel url="message/list.do" code="misc.cancel"/>
		
</form:form>