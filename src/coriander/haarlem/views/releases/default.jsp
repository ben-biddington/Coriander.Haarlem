<%@ page import="java.util.Collection" %>
<%@page import="jetbrains.buildServer.serverSide.tracker.EventSubscription" %>
<%@include file="../include-internal.jsp"%>
<c:set var="loadingWarningDisabled" value="true" scope="request"/>
<c:set var="title" value="Recent releases" scope="request"/>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>

<bs:page>
    <jsp:attribute name="head_include">
        <bs:linkCSS>
            /css/progress.css
            /css/filePopup.css
            /css/overviewTable.css
            /css/myChanges.css
            /css/viewModification.css
            /css/testDetailsDialog.css
        </bs:linkCSS>
    </jsp:attribute>

    <jsp:attribute name="body_include">
        <p>${results.now}</p>
        <p>Results (from: ${results.interval.start} to: ${results.interval.end})</p>

        <!-- @see: ROOT/WEB-INF/tags/historyTable.tag -->
        <table cellspacing="0" class="testList historyList dark borderBottom">
            <tr>
                <th class="firstCell">#</th>
                <th>Build</th>
                <th>Results</th>
                <th>Artifacts</th>
                <th>Changes</th>
                <th class="sorted">Started</th>
                <th>Duration</th>
                <th>Agent</th>
                <th>Tags</th>
                <th class="autopin">&nbsp;</th>
            </tr>
            <c:forEach var="entry" items="${results.builds}" varStatus="recordStatus">
                <jsp:useBean id="entry" type="jetbrains.buildServer.serverSide.SFinishedBuild"/>
                <c:set var="buildId" value="${entry.buildId}"/>
                <c:set var="rowClass" value="${not empty entry.canceledInfo ? 'cancelledBuild' : ''}"/>
                <c:set var="rowClass" value="${rowClass} ${entry.outOfChangesSequence ? 'outOfSequence ' : ''}"/>

                <tr <c:if test="${not empty highlightRecord && recordStatus.count == highlightRecord + 1}">style="background-color: #FFFFCC;"</c:if>>
                    <td style="text-align:center;" class="${rowClass}">
                      <bs:buildNumber buildData="${entry}"/>
                      <bs:buildCommentIcon build="${entry}"/>
                    </td>

                    <td>${entry.fullName}</td>

                    <td class="${rowClass}">
                      <bs:buildDataIcon buildData="${entry}"/>
                      <bs:resultsLink build="${entry}" skipChangesArtifacts="true">${entry.statusDescriptor.text}</bs:resultsLink>
                    </td>

                    <td class="${rowClass}">
                      <c:if test="${entry.artifactsExists}">
                        <bs:artefactsIcon/>
                        <bs:artefactsLink build="${entry}">View</bs:artefactsLink>
                      </c:if>
                      <c:if test="${not entry.artifactsExists}">
                        <span style="color: #888;">None</span>
                      </c:if>
                    </td>

                    <td class="${rowClass}"><bs:changesLinkFull buildPromotion="${entry.buildPromotion}"/></td>

                    <td class="${rowClass}"><bs:date value="${entry.startDate}"/></td>

                    <td class="${rowClass}">
                      <bs:printTime time="${entry.duration}" showIfNotPositiveTime="&lt; 1s"/>
                    </td>

                    <td class="${rowClass}"><bs:agentDetailsLink agentName="${entry.agentName}"/></td>
                    <td class="${rowClass}">
                      <t:tagsInfo build="${entry}"/>
                    </td>

                    <td class="${rowClass}">
                      <c:if test="${entry.usedByOtherBuilds}">
                        <bs:_viewLog build="${entry}" tab="dependencies" title="This build is used by other builds"
                          ><img src="<c:url value="/img/pin-a.gif"/>" alt="" class="pinImg"
                          title="This build is used by other builds"
                          /></bs:_viewLog>
                      </c:if>&nbsp;
                    </td>
                </tr>
            </c:forEach>
        </table>
        <!-- /@see: ROOT/WEB-INF/tags/historyTable.tag -->
    </jsp:attribute>
</bs:page>