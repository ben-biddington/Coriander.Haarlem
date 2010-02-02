<%@include file="../../include-internal.jsp"%>
<%@include file="input.jsp"%>

<p>Build log results for <strong>${buildName}</strong> build #${buildId}:</p>

<c:if test="${buildLogResults != null}">
     <c:out value="Results (${resultCount})" />
</c:if>

<table class="plain" style="margin-left:0">
  <c:forEach items="${buildLogResults}" var="logMessage">
    <tr style="vertical-align:top">
        <td align="left" width="100">
            <c:out value="${logMessage.timestamp}" escapeXml="false" />
        </td>
        <td align="left" width="100">
            <c:out value="${logMessage.status}" escapeXml="false" />
        </td>
        <td align="left">
            <c:out value="${logMessage.text}" escapeXml="false" />
        </td>
    </tr>
  </c:forEach>
</table>