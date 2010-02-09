<%@include file="../include-internal.jsp"%>
<h1>All beans (${results.count})</h1>
<ul>
<c:forEach items="${results.beans}" var="bean">
    <li><c:out value="${bean}" escapeXml="true" /> </li>
</c:forEach>
</ul>