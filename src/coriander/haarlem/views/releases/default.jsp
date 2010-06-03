<%@ page import="java.util.Collection" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.format.DateTimeFormat" %>
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
        <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Vollkorn|Lobster|Droid+Serif" />
        <style type="text/css">
            div#today {
                width:50px; border: 1px solid #D0D0D0;
                padding:2px; text-align: center;
                border-top-left-radius: 4px;
                border-top-right-radius: 4px;
                -moz-border-top-left-radius: 4px;
                -moz-border-top-right-radius: 4px;
                -webkit-border-top-left-radius: 4px;
                -webkit-border-bottom-left-radius: 4px;
                -webkit-border-top-right-radius: 4px;
                -webkit-border-bottom-right-radius: 4px;
            }
            div#today div.day   {
                font-family: "Droid Serif"; font-size: 2em;
                color: red; background-color: #F0F0F0;
                padding: 2px;
                height: 50px;
            }
            div#today div.day h1 { margin:0; line-spacing:0px; line-height:normal; }
            div#today div.month { color: black; }
            div#info    { float:left; padding-left:5px; padding-right:5px;}
            div#results { float:left; }
            div#results table.testList { width:900px; }
            div#results p.summary { padding:2px; padding-left:5px; }
        </style>
    </jsp:attribute>

    <jsp:attribute name="body_include">
        <p class="summary">Results (${results.count}) for ${results.intervalInDays} days (${results.intervalStart} - ${results.intervalEnd})</p>            

        <div id="info">
            <div id="today" title="${results.today}">
                <div class="day"><h1>${results.dayOfMonth}</h1></div>
                <div class="month">${results.monthOfYear}</div>
            </div>
        </div>

        <div id="results">
            <!-- @see: ROOT/WEB-INF/tags/historyTable.tag -->
            <table cellspacing="0" class="testList historyList dark borderBottom">
                <tr>
                    <th class="firstCell">#</th>
                    <th>Project</th>
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
                <c:if test="${empty results.builds}">
                    <td colspan="11">No results</td>
                </c:if>
                <c:if test="${not empty results.builds}">
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

                            <td>
                                <strong><a title="Open project page" href="/project.html?projectId=${entry.buildType.projectId}">${entry.buildType.projectName}</a></strong>
                            </td>

                            <td>
                                <a title="Open build page" href="/viewType.html?buildTypeId=${entry.buildTypeId}">${entry.buildTypeName}</a>
                            </td>

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
                </c:if>
                <tr>
                    <td class="details permalinks" colspan="11">
                        <div style="margin-top:10px;">You can bookmark these links for quicker navigation:</div>
                        <img src="img/star.gif" width="16" height="16" /><a href="releases.html?last=20&matching=live">Last 20 live releases</a>
                        <img src="img/star.gif" width="16" height="16" /><a href="releases.html?since=7-days-ago&matching=live">All live releases for the past week</a>
                    </td>
                </tr>
            </table>
            <!-- /@see: ROOT/WEB-INF/tags/historyTable.tag -->
        </div>
    </jsp:attribute>
</bs:page>