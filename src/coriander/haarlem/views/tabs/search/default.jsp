<%@include file="../../include-internal.jsp"%>
<%@include file="input.jsp"%>

<p>Build log results for <strong>${buildName}</strong> # ${buildId} (${artifactsDirectory}):</p>

<table>
  <c:forEach items="${buildLogResults}" var="logMessage">
    <tr>
        <td align="left">
            <c:out value="${logMessage.text}" escapeXml="false" />
        </td>
    </tr>
  </c:forEach>
</table>