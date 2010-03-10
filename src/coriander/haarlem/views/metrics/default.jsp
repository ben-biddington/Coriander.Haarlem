<%@include file="../include-internal.jsp"%>
<c:out value="${results.error}" escapeXml="false" />
<c:out value="${results.info}" escapeXml="false" />

<c:forEach items="${results.dashboards}" var="dashboardInfo">
    <div class="build" style="float:left;margin-bottom:10px;">
        <div class="project">
            <h2><a href="/project.html?projectId=${dashboardInfo.build.project.projectId}&amp;tab=projectOverview" title="Go to the project page">${dashboardInfo.build.project.name}</a>
            <span class="projectDescription">(${dashboardInfo.build.project.description})</span>
            </h2>
        </div>
        <div class="tableCaption">
            <a href="javascript:toggleVisibleAndChangeImage('dashboard-toggle-${dashboardInfo.build.buildTypeId}', 'dashboard-${dashboardInfo.build.buildTypeId}')">
                <img id="dashboard-toggle-${dashboardInfo.build.buildTypeId}" src="/img/minus.gif" style="margin-right:10px;" border="0" />
            </a>
            <a class="buildName" href="/viewType.html?buildTypeId=${dashboardInfo.build.buildTypeId}&tab=buildTypeStatusDiv">
                <c:out value="${dashboardInfo.build.name}" escapeXml="true" />
            </a>
            <span class="date" title="Last successful run"><c:out value="${dashboardInfo.build.lastChangesFinished.finishDate}" escapeXml="true" /></span>
        </div>
        <div style="padding:10px 5px 5px 15px" id="dashboard-${dashboardInfo.build.buildTypeId}">
            ${dashboardInfo.html}
        </div>
    </div>
</c:forEach>

<c:if test="${results.dashboardCount == 0}">
    <img src="/img/help.gif" width="10" height="10" style="vertical-align:middle;margin-bottom: 4px;" /> There are no dashboards available for your account, ensure you are <a href="/profile.html?tab=userNotifications">watching at least one metrics build</a>.   
</c:if>


