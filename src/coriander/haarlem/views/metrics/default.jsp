<%@include file="../include-internal.jsp"%>
<bs:linkCSS>
    /css/progress.css
    /css/overviewTable.css
    /css/overview.css
    /css/filePopup.css
    /css/buildQueue.css
    /css/agentsInfoPopup.css
    /css/pager.css
    /css/project.css
  </bs:linkCSS>
<c:out value="${results.error}" escapeXml="false" />

<c:forEach items="${results.builds}" var="dashboardInfo">
    <div class="build" style="float:left;margin-bottom:10px;">
        <div class="projectHeader">
            <h2><a href="/project.html?projectId=${dashboardInfo.build.project.projectId}&amp;tab=projectOverview" title="Go to the project page">${dashboardInfo.build.project.name}</a>
            <span class="projectDescription">(${dashboardInfo.build.project.description})</span>
            </h2>
        </div>
        <div class="tableCaption"><a class="buildTypeName" href="javascript:void(0);"><c:out value="${dashboardInfo.build.extendedName}" escapeXml="true" /></a></div>
        <div style="padding:10px 5px 5px 15px">
            ${dashboardInfo.html}
        </div>
    </div>
</c:forEach>


