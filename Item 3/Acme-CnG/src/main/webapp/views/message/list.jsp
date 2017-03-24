
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
<display:table pagesize="5" class="displaytag"
	name="messages" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	<acme:column code="message.moment" property="moment"/>
	
	<acme:column code="message.title" property="title"/>
	
	<acme:column code="message.text" property="text"/>
	
	<spring:message code="message.attachments" var="att" />
	<display:column title="${att}">
		<jstl:forEach items="${row.attachments}" var="attachment">
			<a href="${attachment}"><jstl:out value="${attachment}" /></a>
			<br/>
		</jstl:forEach>
	</display:column>

	<acme:column code="message.recipient" property="recipient.userAccount.username"/>

	<display:column>
		<a href="message/forward.do?messageId=${row.id }"><spring:message code="message.forward" /></a>
	</display:column>
	
	<jstl:if test="${isReceivedView}" >
		<display:column>
			<a href="message/reply.do?messageId=${row.id }"><spring:message code="message.reply" /></a>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${!isReceivedView}" >
		<display:column>
			<spring:message code="misc.confirm.delete" var="confirm"></spring:message>
			<a href="message/delete.do?messageId=${row.id}" onclick="return confirm('${confirm}')"><spring:message code="misc.delete" /></a>
		</display:column>
	</jstl:if>
	
</display:table>

<security:authorize access="isAuthenticated()">
	<acme:link href="message/send.do" code="message.send"/>
</security:authorize>