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
        Phil's view is: "${results.message}"
    </jsp:attribute>
</bs:page>