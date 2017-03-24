<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<style>
<!--
.index {
	position:fixed;
	top:50%;
	left:50%;
	transform: translate(-50%,-50%);
}
.link1 {
	position: fixed;
	top:58%;
	left:50%;
	transform: translate(-50%,-50%);
}

.link2 {
	position: fixed;
	top:65%;
	left:50%;
	transform: translate(-50%,-50%);
}
-->
</style>

<security:authorize access="isAuthenticated()">
	<jsp:forward page="/welcome/index.do" />
</security:authorize>

<div class="index">
	<img src="http://i.imgur.com/hhPlN89.png" alt="Acme Car'n Go"  />
</div>

<div class="link1">
	<acme:link href="security/login.do" code="misc.login" />
</div>

<div class="link2">
	<acme:link href="customer/edit.do" code="misc.register"/>
</div>
