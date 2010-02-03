<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>
<style type="text/css">
    div.status {
        text-align:left;
        background-color:#FFF;
        padding:2px;
        margin-bottom:1px;
        border-width: 1px 0px 1px 0px;
        border-color: #FEFEFE;	
        width:100%;
    }
    
    div.status img { vertical-align:middle }

    div.charts { clear: both; }
    div.chart { float: left; margin-right: 5px; }
</style>

<div id="coriander.haarlem.tabs.metrics.status" class="status"></div>
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