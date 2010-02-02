<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>
<div id="coriander.haarlem.tabs.metrics.results"></div>

<script language="javascript">
    function showStatus(message) {
        $("coriander.haarlem.tabs.metrics.results").update(message)
    }

    function fill() {
        var currentUserId = "${currentUser.id}";
        var url = "/metrics.html?user=" + currentUserId;

        showStatus("<img src=\"../../img/ajax-loader.gif\" />");

        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(transport) {
                var response = transport.responseText || "<none>";

                showStatus(response);
            }
        });
    }

    document.observe("dom:loaded", function() {
        fill();
    });
</script>