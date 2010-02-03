<%@include file="../include-internal.jsp"%>
<html>
    <body>
        <c:out value="${results.error}" escapeXml="false" />
            
        <c:forEach items="${results.builds}" var="dashboardInfo">
            <h3><c:out value="${dashboardInfo.build.extendedName}" escapeXml="true" /></h3>
                <!-- Artifacts: <c:out value="${dashboardInfo.build.lastChangesSuccessfullyFinished.artifactsDirectory}" /> -->
            <div>
                ${dashboardInfo.html}                
            </div>
        </c:forEach>
    </body>
</html>

