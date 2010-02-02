<%@include file="../../include-internal.jsp"%>
<%@include file="input.jsp"%>

<c:if test="${buildLogResults != null}">
    
    <table class="plain" style="margin-left:0;width:100%">
        <tr>
            <th>Timestamp</th>
            <th>Status</th>
            <th>Message</th>
        </tr>
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
</c:if>