<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="message/send.do" modelAttribute="messageEntity" >
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${forward == true}">
		<acme:textbox code="message.title" path="title" readonly="true" />
		<acme:textbox code="message.text" path="text" readonly="true"/>
		<acme:textbox code="message.attachments" path="attachments" readonly="true"/>
	</jstl:if>
	
	<jstl:if test="${forward == false}">
		<acme:textbox code="message.title" path="title" />
		<acme:textbox code="message.text" path="text" />
		<acme:textarea code="message.attachments" path="attachments" />
	</jstl:if>
	
	<acme:select items="${actors}" itemLabel="userAccount.username" code="message.recipient" path="recipient" />
	
	<jstl:if test="${reply == false and forward == false}">
			<acme:submit name="send" code="message.send"/>
	</jstl:if>

	<jstl:if test="${forward == true}">
			<acme:submit name="forward" code="message.send"/>
	</jstl:if>
	
	<jstl:if test="${reply == true}">
			<acme:submit name="reply" code="message.send"/>
	</jstl:if>
	
	<acme:cancel url="/" code="misc.cancel"/>

</form:form>