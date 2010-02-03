<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>
<style type="text/css">
    div#coriander.haarlem.tabs.metrics.status {
        text-align:center;background-color:#008209;padding:2px;width:200px
    }
    div#coriander.haarlem.tabs.metrics.status img { vertical-align:middle }
</style>

<div id="coriander.haarlem.tabs.metrics.status"></div>
<div id="coriander.haarlem.tabs.metrics.results"></div>

<script language="javascript">
    document.observe("dom:loaded", function() {
        showLoading("Waiting...");
        window.setTimeout(fill, 2500);
    });

    function showLoading(message) {
        var loadingGraphic = '../../img/ajax-loader.gif';
        var anotherLoadingGraphic = '../../img/buildStates/running_green_transparent.gif';

        showStatus("<img width=\"16\" height=\"16\" style=\"vertical-align:middle\" src=\"" + anotherLoadingGraphic + "\" /> Waiting...");
    }

    function showStatus(message) {
        $("coriander.haarlem.tabs.metrics.status").update(message)
    }

    function hideStatus() {
        new Effect.Opacity(
            'coriander.haarlem.tabs.metrics.status',
            { duration: 3.0, from: 1, to: 0 }
        );
    }

    function fill() {
        var url = "/metrics.html?user=" + "${currentUser.id}";
        
        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
                hideStatus();
                $("coriander.haarlem.tabs.metrics.results").update(result.responseText || "<none>");
            },
            onFailure: function(result) {
                showStatus("Failed: "+ result.responseText || "<none>");                
            }
        });
    }
</script>