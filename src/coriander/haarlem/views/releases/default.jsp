<%@ page import="java.util.Collection" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Interval" %>
<%@ page import="org.joda.time.format.DateTimeFormat" %>
<%@page import="jetbrains.buildServer.serverSide.tracker.EventSubscription" %>
<%@include file="../include-internal.jsp"%>
<c:set var="xxx" scope="request" value="${pageContext.request.contextPath}"/>
<c:set var="currentUrl" scope="request">
    <c:url value="/releases.html">
        <c:param name="matching" value="${param.matching}"/>
        <c:param name="last" value="${param.last}"/>
        <c:param name="since" value="${param.since}"/>
    </c:url>
</c:set>
                        
<c:set var="loadingWarningDisabled" value="true" scope="request"/>
<c:set var="title" value="Recent releases" scope="request"/>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>
<bs:page>
    <jsp:attribute name="head_include">
        <bs:linkCSS>
            /css/pager.css
            /css/progress.css
            /css/modificationListTable.css
            /css/compatibilityList.css
            /css/buildTypeSettings.css
            /css/forms.css
            /css/filePopup.css
            /css/viewType.css
            /css/buildQueue.css
            /css/historyTable.css
            /css/agentsInfoPopup.css
            /css/tags.css
            /css/buildTypeDetails.css
        </bs:linkCSS>
        <bs:linkScript>
            /js/bs/blocks.js
            /js/bs/blocksWithHeader.js
            /js/bs/blockWithHandle.js
            /js/bs/changesBlock.js
            /js/bs/collapseExpand.js
            /js/bs/modalDialog.js
            /js/bs/forms.js
            /js/bs/pin.js
            /js/bs/buildComment.js
            /js/bs/runningBuilds.js
            /js/bs/stopBuild.js
            /js/bs/vcsProblemsMonitor.js
            /js/bs/tags.js
            /js/bs/overflower.js
            /js/bs/queueLikeSorter.js
            /js/bs/buildQueue.js
            /js/bs/historyTable.js
            /js/bs/buildType.js
        </bs:linkScript>
        <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Vollkorn|Lobster|Droid+Serif" />
        <link rel="stylesheet" type="text/css" href="releases.css" />

        <style type="text/css">
            div#results { }

            div#today, div#intervalStart {
                width: 75px;
                padding: 2px;
                border: 1px solid #D0D0D0;
                margin: 0px auto;
                border-top-left-radius: 4px;
                border-top-right-radius: 4px;
                border-bottom-right-radius: 4px;
                border-bottom-left-radius: 4px;
                -moz-border-top-left-radius: 4px;
                -moz-border-top-right-radius: 4px;
                -moz-border-bottom-right-radius: 4px;
                -moz-border-bottom-left-radius: 4px;
                -webkit-border-top-left-radius: 4px;
                -webkit-border-top-right-radius: 4px;
                -webkit-border-bottom-right-radius: 4px;
                -webkit-border-bottom-left-radius: 4px;
            }

            div#today div.day {
                color: #FF0000;
                background-color: #F0F0F0;
            }
            div#today div.month { color: black; }

            div#today div.day, div#intervalStart div.day {
                font-family: "Droid Serif";
                font-size: 2em;
                background-color: #FFF;
                padding: 2px;
                height: 50px;
            }

            div#intervalStart div.day {
                color: #00FF00;
            }

            div.day h1 { margin:0; line-spacing:0px; line-height:normal; }

            div#results table.testList { width:100%; }

            div#results table.testList a.project {
                width:100px;
                overflow:hidden;
                display: inline-block;
                text-overflow: ellipsis;
                white-space:nowrap;
            }
            div#results p.summary { padding:2px; padding-left:5px; }

            table.buildTypeHeader td.header { width: 125px; }
            table.buildTypeHeader td#info {
                padding-right:10px;
                padding-left:10px;
                text-align:center;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="body_include">

        <c:out value="${xxx}" />
        <p class="summary">
            Results (${results.count})
            for ${results.intervalInDays} days
            (${results.intervalStart} - ${results.intervalEnd})
        </p>            

        <table class="buildTypeHeader" cellspacing="0">
            <tbody>
                <tr>
                    <td class="header">&nbsp;</td>
                    <td class="details">
                        <form method="get" action="${xxx}">
                            <input type="text" id="matching" name="matching"
                                size="100" style="margin:0; padding:0; width: 8em;"
                                value="${param.matching}" />
                            <input type="submit" value="Filter" />
                        </form>
                    </td>
                </tr>
                <tr>
                    <td id="info" class="header">
                        <div id="intervalStart" title="${results.intervalStart}">
                            <div class="day"><h1>${results.intervalStartDay}</h1></div>
                            <div class="month">${results.intervalStartMonth}</div>
                        </div>
                        <div id="today" title="${results.today}">
                            <div class="day"><h1>${results.dayOfMonth}</h1></div>
                            <div class="month">${results.monthOfYear}</div>
                        </div>
                    </td>
                    <td class="details">
                    <div id="results">
                        <!-- @see: ROOT/WEB-INF/tags/historyTable.tag -->
                        <table cellspacing="0" class="testList historyList dark borderBottom">
                            <tr>
                                <th class="firstCell">#</th>
                                <th>Project</th>
                                <th>Build</th>
                                <th>Results</th>
                                <th>Artifacts</th>
                                <!--<th>Changes</th>-->
                                <th class="sorted">Started</th>
                                <th>Duration</th>
                                <!--<th>Agent</th>-->
                                <!--<th>Tags</th>-->
                                <th class="autopin">&nbsp;</th>
                            </tr>
                            <c:if test="${empty results.builds}">
                                <td colspan="9">No results</td>
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
                                            <strong><a title="Open &lt;${entry.buildType.projectName}&gt; project page" class="project" href="/project.html?projectId=${entry.buildType.projectId}">${entry.buildType.projectName}</a></strong>
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

                                        <!--<td class="${rowClass}"><bs:changesLinkFull buildPromotion="${entry.buildPromotion}"/></td>-->

                                        <td class="${rowClass}"><bs:date value="${entry.startDate}"/></td>

                                        <td class="${rowClass}">
                                          <bs:printTime time="${entry.duration}" showIfNotPositiveTime="&lt; 1s"/>
                                        </td>

                                        <!--<td class="${rowClass}"><bs:agentDetailsLink agentName="${entry.agentName}"/></td>-->
                                        <!--<td class="${rowClass}"><t:tagsInfo build="${entry}"/></td>-->

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
                        </table>
                        <!-- /@see: ROOT/WEB-INF/tags/historyTable.tag -->
                    </div>
                    </td>
                </tr>
                <tr>
                    <td class="header">Permalinks</td>
                    <td class="details permalinks">
                        <div>You can bookmark these links for quicker navigation:</div>
                        <img src="img/star.gif" width="16" height="16" /><a href="releases.html?last=20&matching=live">Last 20 live releases</a>
                        <img src="img/star.gif" width="16" height="16" /><a href="releases.html?since=7-days-ago&matching=live">All live releases for the past week</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </jsp:attribute>
</bs:page>