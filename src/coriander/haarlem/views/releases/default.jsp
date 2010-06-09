<%@ page import="java.util.Collection" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Interval" %>
<%@ page import="org.joda.time.format.DateTimeFormat" %>
<%@page import="jetbrains.buildServer.serverSide.tracker.EventSubscription" %>
<%@include file="../include-internal.jsp"%>
<c:set var="currentUrl" scope="request">
    <c:url value="/releases.html">
        <c:param name="matching" value="${param.matching}"/>
        <c:param name="last" value="${param.last}"/>
        <c:param name="since" value="${param.since}"/>
    </c:url>
</c:set>
                        
<c:set var="loadingWarningDisabled" value="true" scope="request"/>
<c:set var="showCalendars" value="false" scope="request"/>
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
                max-width:300px;
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

            div.error { margin-left:auto; }
            div.error img { float:left; margin-right:5px; }

            form label { float:none; vertical-align:center; }
        </style>
    </jsp:attribute>

    <jsp:attribute name="body_include">
        <c:if test="${not empty results.errors}">
            <div class="error">
                <c:forEach var="entry" items="${results.errors}">
                    <p><img src="/img/attentionCommentRed.gif" border="0" /><c:out value="${entry}" escapeXml="true" /></p>
                </c:forEach>
            </div>            
        </c:if>
        <table class="buildTypeHeader" cellspacing="0">
            <tbody>
                <tr>
                    <td class="header">&nbsp;</td>
                    <td class="details">
                        <form method="get" action="${currentUrl}">
                            <label for="matching">Filter by build:</label>
                            <input type="text" id="matching" name="matching"
                                size="100" style="margin:0; padding:0; width: 8em;"
                                value="${param.matching}" />
                            <input type="hidden" name="since" id="since" value="${param.since}" />
                            <input type="hidden" name="last" id="last" value="${param.last}" />
                            <input type="submit" value="Filter" />
                            <input type="checkbox" checked="checked" id="useRegexp" name="useRegexp" disabled="disabled" />
                            <label for="useRegexp">Regexp</label>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td class="header">Results (${results.count})</td>
                    <td class="details">
                        <c:if test="${results.interval != null}">
                            <p class="summary" title="${results.intervalString}">
                                spanning ${results.intervalInDays} days
                                (${results.intervalStart} - ${results.intervalEnd})
                            </p>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td id="info" class="header">
                        <c:if test="${showCalendars}">
                            <div id="today" title="${results.intervalEnd}">
                                <div class="day"><h1>${results.intervalEndDay}</h1></div>
                                <div class="month">${results.intervalEndMonth}</div>
                            </div>
                            <div id="intervalStart" title="${results.intervalStart}">
                                <div class="day"><h1>${results.intervalStartDay}</h1></div>
                                <div class="month">${results.intervalStartMonth}</div>
                            </div>
                        </c:if>
                        <c:if test="${not showCalendars}">&nbsp;</c:if>
                    </td>
                    <td class="details">
                    <div id="results">
                        <!-- @see: ROOT/WEB-INF/tags/historyTable.tag -->
                        <c:set var="cols" value="7"/>
                        <table cellspacing="0" class="testList historyList dark borderBottom">
                            <tr>
                                <th class="firstcell">&nbsp;</th>
                                <th>Project</th>
                                <th>Build</th>
                                <th>Results</th>
                                <th class="sorted">Completed</th>
                                <th>Duration</th>
                                <th class="autopin">&nbsp;</th>
                            </tr>
                            <c:if test="${empty results.builds}">
                                <td colspan="${cols}">No results</td>
                            </c:if>
                            <c:if test="${not empty results.builds}">
                                <c:forEach var="entry" items="${results.builds}" varStatus="recordStatus">
                                    <jsp:useBean id="entry" type="jetbrains.buildServer.serverSide.SFinishedBuild"/>
                                    <c:set var="buildId" value="${entry.buildId}"/>
                                    <c:set var="rowClass" value="${not empty entry.canceledInfo ? 'cancelledBuild' : ''}"/>
                                    <c:set var="rowClass" value="${rowClass} ${entry.outOfChangesSequence ? 'outOfSequence ' : ''}"/>

                                    <tr <c:if test="${not empty highlightRecord && recordStatus.count == highlightRecord + 1}">style="background-color: #FFFFCC;"</c:if>>
                                        <td style="text-align:center;"><c:out value="${recordStatus.index + 1}"/></td>
                                        <td><strong><a title="Open &lt;${entry.buildType.projectName}&gt; project page" class="project" href="/project.html?projectId=${entry.buildType.projectId}">${entry.buildType.projectName}</a></strong></td>

                                        <td>
                                            <a title="Open build page" href="/viewType.html?buildTypeId=${entry.buildTypeId}">${entry.buildTypeName}</a>
                                        </td>

                                        <td class="${rowClass}">
                                          <bs:buildDataIcon buildData="${entry}"/>
                                          <bs:resultsLink build="${entry}" skipChangesArtifacts="true">${entry.statusDescriptor.text}</bs:resultsLink>
                                        </td>

                                        <td class="${rowClass}"><bs:date value="${entry.finishDate}"/></td>

                                        <td class="${rowClass}">
                                          <bs:printTime time="${entry.duration}" showIfNotPositiveTime="&lt; 1s"/>
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
                        </table>
                        <!-- /@see: ROOT/WEB-INF/tags/historyTable.tag -->
                    </div>
                    </td>
                </tr>
                <c:if test="${results.rickrollable}">
                    <tr>
                        <td class="header">Special!</td>
                        <td class="details">
                            <p>${currentUser.descriptiveName} has been rickrolled, (ha ha).</p>
                            <object width="445" height="364"><param name="movie" value="http://www.youtube.com/v/oHg5SJYRHA0&hl=en_GB&fs=1&rel=0&border=1"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="http://www.youtube.com/v/oHg5SJYRHA0&hl=en_GB&fs=1&rel=0&border=1" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="445" height="364"></embed></object>
                        </td>
                    </tr>
                </c:if>
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