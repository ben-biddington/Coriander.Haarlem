<%@ page import="java.util.Collection" %>
<%@page import="jetbrains.buildServer.serverSide.tracker.EventSubscription" %>
<%@include file="../include-internal.jsp"%>

<c:set var="loadingWarningDisabled" value="true" scope="request"/>
<c:set var="title" value="Build log search" scope="request"/>
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
    <%@ include file="input.jsp" %>
    <p>Results: </p>
    <p>
    ${results.result}
    </p>
  </jsp:attribute>
</bs:page>