<%@include file="../include-internal.jsp"%>
<html>
    <body>
        <c:out value="${results.error}" escapeXml="false" />
        <c:out value="${results.user.email} (${results.user.email})" escapeXml="true" />
            
        <p>Builds with dashboards:</p>
        
        <c:forEach items="${results.builds}" var="build">
            <strong><c:out value="${build.extendedName}" escapeXml="true" /></strong>
            <p>
                Last successful:
                <c:out value="${build.lastChangesSuccessfullyFinished}" escapeXml="true" />
            </p>
            <p>
                Artifacts: <c:out value="${build.lastChangesSuccessfullyFinished.artifactsDirectory}" />
            </p>
        </c:forEach>
    </body>
</html>

