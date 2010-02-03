<%@include file="../include-internal.jsp"%>
<html>
    <body>
        <c:out value="${results.error}" escapeXml="false" />
        <c:out value="${results.user.email} (${results.user.email})" escapeXml="true" />
            
        <p>Builds with dashboards:</p>
        
        <c:forEach items="${results.builds}" var="dashboardInfo">
            <strong><c:out value="${dashboardInfo.build.extendedName}" escapeXml="true" /></strong>
            <p>
                Last successful:
                <c:out value="${dashboardInfo.build.lastChangesSuccessfullyFinished}" escapeXml="true" />
            </p>
                <!-- Artifacts: <c:out value="${dashboardInfo.build.lastChangesSuccessfullyFinished.artifactsDirectory}" /> -->
            <div>
                ${dashboardInfo.html}                
            </div>
        </c:forEach>
    </body>
</html>

