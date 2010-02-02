<%@include file="../include-internal.jsp"%>
<html>
    <body>
        <c:out value="${results.error}" escapeXml="false" />
        <c:out value="${results.user.email} (${results.user.email})" escapeXml="true" />

        <table class="plain" style="margin-left:0;width:100%">
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
            </c:forEach>
        </table>
    </body>
</html>

