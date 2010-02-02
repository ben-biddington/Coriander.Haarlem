<%@include file="../include-internal.jsp"%>
<html>
    <body>
        <c:out value="${results.error}" escapeXml="false" />
        <c:out value="${results.user.email} (${results.user.email})" escapeXml="true" />
            
        <table class="plain" style="margin-left:0;width:100%" border="1">
            <c:forEach items="${results.projects}" var="project">
                <tr>
                    <td align="left">
                        Artifacts
                    </td>
                    <td align="left">
                        <c:out value="${project.artifactsDirectory}" escapeXml="true" />
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        Name
                    </td>
                    <td align="left">
                        <c:out value="${project.extendedName}" escapeXml="true" />
                    </td>
                </tr>
                <tr style="vertical-align:top">
                    <td align="left">
                        Build configurations
                    </td>
                    <td align="left">
                         <c:forEach items="${project.buildTypes}" var="buildType">
                            <div style="border:1px solid #F0F0F0; padding:2px;">
                                <strong><c:out value="${buildType.extendedName}" escapeXml="true" /></strong>
                                <p>
                                    Last successful:
                                    <c:out value="${buildType.lastChangesSuccessfullyFinished}" escapeXml="true" />
                                </p>
                                <p>
                                    Artifacts: <c:out value="${buildType.lastChangesSuccessfullyFinished.artifactsDirectory}" />
                                </p>
                            </div>
                         </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>

