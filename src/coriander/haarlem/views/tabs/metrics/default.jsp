<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>
<style type="text/css">
    div.status {
        text-align:left;
        background-color:#FFF;
        padding:5px;
        margin-bottom:2px;
        border: 1px solid #CECECE;
        border-left-width: 0;
        border-right-width: 0;
        width:100%;
    }
    
    div.status img { vertical-align:middle; margin-bottom:1px; }

    div.charts { clear: both; }
    div.chart, div.metric { float: left; margin-right: 5px; }
    div.metric div.values h2 { font-size: normal; }

    h4.metric-title { width: 200px; margin-top:0px; }
    h4.metric-body { padding-left:5px; }
    div.metric-icon { float:left; padding:2px; }
    div.metric-percent { float:left; padding:2px; }
    div.metric-some-other-number { float:left; padding:2px; }
    table.metric td { vertical-align:top; }
</style>
<div id="coriander.haarlem.tabs.metrics.status" class="status"></div>
<div id="coriander.haarlem.tabs.metrics.results"></div>

<script language="javascript">
    var currentUser = "";
    
    document.observe("dom:loaded", function() {
        showLoading("Collecting dashboards...");

        currentUser = "${currentUser.id}";
        
        window.setTimeout(fill, 2500);
    });

    function showLoading(message) {
        var loadingGraphic = '../../img/ajax-loader.gif';
        var anotherLoadingGraphic = '../../img/buildStates/running_green_transparent.gif';

        showStatus("<img width=\"16\" height=\"16\" src=\"" + anotherLoadingGraphic + "\" /> " + message);
    }

    function showStatus(message) {
        $("coriander.haarlem.tabs.metrics.status").update(message)
    }

    function hideStatus() {
        new Effect.Opacity(
            'coriander.haarlem.tabs.metrics.status',
            { duration: 2.0, from: 1, to: 0 }
        );
    }

    function fill() {
        var url = "/metrics.html?user=" + currentUser;
        
        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
                showLoading("Collecting dashboards...done.");
                hideStatus();
                $("coriander.haarlem.tabs.metrics.results").update(result.responseText || "<none>");
            },
            onFailure: function(result) {
                showStatus("Failed: "+ result.responseText || "<none>");                
            }
        });
    }
</script>