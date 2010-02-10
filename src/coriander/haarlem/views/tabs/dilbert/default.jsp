<style type="text/css">
    div.status img { vertical-align:middle; margin-bottom:3px; }
</style>
<div id="coriander.haarlem.tabs.dilbert.status" class="status"></div>
<div id="coriander.haarlem.tabs.dilbert.results"></div>

<script language="javascript">
    var loadingGraphic = '../../img/ajax-loader.gif';
    var anotherLoadingGraphic = '../../img/buildStates/running_green_transparent.gif';
                
    document.observe("dom:loaded", function() {
        showLoading("Collecting Dilbert...");

        window.setTimeout(fill, 500);
    });

    function showLoading(message) {
        showStatus("<img width=\"16\" height=\"16\" src=\"" + anotherLoadingGraphic + "\" /> " + message);
    }

    function showStatus(message) {
        $("coriander.haarlem.tabs.dilbert.status").show()
        $("coriander.haarlem.tabs.dilbert.status").update(message)
    }

    function hideStatus() {
        new Effect.Opacity(
            'coriander.haarlem.tabs.dilbert.status',
            { duration: 1.5, from: 1, to: 0 }
        );
         new Effect.BlindUp(
            'coriander.haarlem.tabs.dilbert.status',
            { duration: 0.5 }
        );
    }

    function fill() {
        var url = "/dilbert.html"

        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
                showLoading("Done.");
                hideStatus();
                $("coriander.haarlem.tabs.dilbert.results").update(result.responseText || "<none>");
            },
            onFailure: function(result) {
                showStatus("Failed: " + result.responseText || "<none>");
            }
        });
    }
</script>