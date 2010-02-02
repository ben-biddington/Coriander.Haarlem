<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>

<table cellspacing="0" class="userSettingsTable">
    <tr><td class="t" colspan="2">All dashboards</td></tr>
    <tr><td>${currentUser.id}</td><td>${currentUser.email}</td></tr>
</table>