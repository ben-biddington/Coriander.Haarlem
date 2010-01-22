<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="message" type="java.lang.String" scope="request"/>
<html><body>
<c:out value="${message}"/>
</body></html>