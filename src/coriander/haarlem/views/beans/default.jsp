<%@ page import="java.util.Collection" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Interval" %>
<%@ page import="org.joda.time.format.DateTimeFormat" %>
<%@page import="jetbrains.buildServer.serverSide.tracker.EventSubscription" %>
<%@include file="../include-internal.jsp"%>

<c:set var="loadingWarningDisabled" value="true" scope="request"/>
<c:set var="title" value="Beans" scope="request"/>
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
        <style type="text/css">
            table.buildTypeHeader td.header { width: 125px; }
        </style>
    </jsp:attribute>

    <jsp:attribute name="body_include">
        <table class="buildTypeHeader" cellspacing="0">
            <tbody>
                <tr>
                    <td class="header">All beans (${results.count})</td>
                    <td class="details">
                        <ul>
                            <c:forEach items="${results.beans}" var="bean">
                                <li><c:out value="${bean}" escapeXml="true" /> </li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td class="header">Session</td>
                    <td class="details">
                        <ul>
                            <c:forEach items="${results.sessionInfo}" var="item">
                                <li><c:out value="${item}" escapeXml="true" /> </li>
                            </c:forEach>
                        </ul>

                        current user has type: ${currentUser.class}
                    </td>
                </tr>
            </tbody>
        </table>
    </jsp:attribute>
</bs:page>
