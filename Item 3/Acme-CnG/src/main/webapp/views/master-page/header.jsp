<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="images/logo.png" alt="Acme Car'n Go" />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="customer/create.do"><spring:message code="misc.register" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="misc.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="banner/administrator/edit.do"><spring:message code="master.page.administrator.banner.edit" /></a></li>
					<li><a href="dashboard/administrator/dashboard.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
					<li><a href="comment/listActor.do"><spring:message code="master.page.administrator.comment" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.customer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="offer/customer/list.do"><spring:message code="master.page.offer.customer.list" /></a></li>
					<li><a href="request/customer/list.do"><spring:message code="master.page.request.customer.list" /></a></li>
					<li><a href="application/customer/list.do"><spring:message code="master.page.customer.application.list" /></a></li>
					<li><a href="application/customer/listByTrip.do"><spring:message code="master.page.customer.application.listByTrip" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="message/listSent.do"><spring:message code="master.page.message.list.sent" /></a></li>
			<li><a class="fNiv" href="message/listReceived.do"><spring:message code="master.page.message.list.received" /></a></li>
			<li><a class="fNiv" href="comment/list.do"><spring:message code="master.page.comment.list" /></a></li>
			<li><a class="fNiv" href="offer/list.do"><spring:message code="master.page.offer.list" /></a></li>
			<li><a class="fNiv" href="request/list.do"><spring:message code="master.page.request.list" /></a></li>
			<li><a class="fNiv" href="j_spring_security_logout"><spring:message code="master.page.logout" />(<security:authentication property="principal.username" />)</a></li>
		</security:authorize>
		
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

