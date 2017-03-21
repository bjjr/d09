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

<h2><spring:message code="administrator.ratioOR" /></h2>
<jstl:out value="${ratioOR}"></jstl:out>

<h2><spring:message code="administrator.avgOC" /></h2>
<jstl:out value="${avgOC}"></jstl:out>

<h2><spring:message code="administrator.avgRC" /></h2>
<jstl:out value="${avgRC}"></jstl:out>

<h2><spring:message code="administrator.avgAO" /></h2>
<jstl:out value="${avgAO}"></jstl:out>

<h2><spring:message code="administrator.avgAR" /></h2>
<jstl:out value="${avgAR}"></jstl:out>

<h2><spring:message code="administrator.customerMAA" /></h2>

<display:table pagesize="5" class="displaytag" name="customerMAA"
requestURI="administrator/dashboard.do" id="customerMAA">

	<acme:column code="actor.name" property="name"/>
	<acme:column code="actor.surname" property="surname"/>

</display:table>

<h2><spring:message code="administrator.customerMAD" /></h2>

<display:table pagesize="5" class="displaytag" name="customerMAD"
requestURI="administrator/dashboard.do" id="customerMAD">

	<acme:column code="actor.name" property="name"/>
	<acme:column code="actor.surname" property="surname"/>

</display:table>

<h2><spring:message code="administrator.avgACA" /></h2>
<jstl:out value="${avgACA}"></jstl:out>

<h2><spring:message code="administrator.avgACO" /></h2>
<jstl:out value="${avgACO}"></jstl:out>

<h2><spring:message code="administrator.avgACR" /></h2>
<jstl:out value="${avgACR}"></jstl:out>

<h2><spring:message code="administrator.avgACPA" /></h2>
<jstl:out value="${avgACPA}"></jstl:out>

<h2><spring:message code="administrator.avgACPC" /></h2>
<jstl:out value="${avgACPC}"></jstl:out>

<h2><spring:message code="administrator.actor10%avgCPA" /></h2>

<display:table pagesize="5" class="displaytag" name="actor10%avgCPA"
requestURI="administrator/dashboard.do" id="actor10%avgCPA">

	<acme:column code="actor.name" property="name"/>
	<acme:column code="actor.surname" property="surname"/>

</display:table>

<h2><spring:message code="administrator.maxMSA" /></h2>
<jstl:out value="${maxMSA}"></jstl:out>

<h2><spring:message code="administrator.minMSA" /></h2>
<jstl:out value="${minMSA}"></jstl:out>

<h2><spring:message code="administrator.avgMSA" /></h2>
<jstl:out value="${avgMSA}"></jstl:out>

<h2><spring:message code="administrator.maxMRA" /></h2>
<jstl:out value="${maxMRA}"></jstl:out>

<h2><spring:message code="administrator.minMRA" /></h2>
<jstl:out value="${minMRA}"></jstl:out>

<h2><spring:message code="administrator.avgMRA" /></h2>
<jstl:out value="${avgMRA}"></jstl:out>

<h2><spring:message code="administrator.actorsMMS" /></h2>

<display:table pagesize="5" class="displaytag" name="actorsMMS"
requestURI="administrator/dashboard.do" id="actorsMMS">

	<acme:column code="actor.name" property="name"/>
	<acme:column code="actor.surname" property="surname"/>

</display:table>

<h2><spring:message code="administrator.actorsMMR" /></h2>

<display:table pagesize="5" class="displaytag" name="actorsMMR"
requestURI="administrator/dashboard.do" id="actorsMMR">

	<acme:column code="actor.name" property="name"/>
	<acme:column code="actor.surname" property="surname"/>

</display:table>