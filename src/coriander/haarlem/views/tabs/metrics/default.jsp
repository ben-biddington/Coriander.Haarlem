<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.User" scope="request"/>
<link rel="stylesheet" type="text/css" href="${includeUrl}/metrics-tab.css" />
<div id="coriander.haarlem.tabs.metrics.status" class="status"></div>
<div id="coriander.haarlem.tabs.metrics.results"></div>

<script language="javascript">
    var loadingGraphic          = '../../img/ajax-loader.gif';
    var anotherLoadingGraphic   = '../../img/buildStates/running_green_transparent.gif';
    var okGraphic 				= '../../img/buildStates/success_small.gif';
    var currentUser = "";

    var results, statusIcon;
     
    document.observe("dom:loaded", function() {
        showLoading("Collecting dashboards...");

        results     = $("coriander.haarlem.tabs.metrics.results")
        statusIcon  = $("status_icon")

        currentUser = "${currentUser.id}";
        
        window.setTimeout(fill, 500);
    });

    function fill() {
        var url = "/metrics.html?user=" + currentUser;

        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
                showLoading("");
                
                statusIcon.setAttribute("src", okGraphic);

				flashAndHide("status_icon");
					
                results.update(result.responseText || "<none>");

                results.setStyle({ "display" : "block", "opacity" : "0" });

                new Effect.Opacity(
                    results,
                    { duration: 0.5, from: 0, to: 1, queue: 'front' }
                );
            },
            onFailure: function(result) {
                showStatus("Failed: " + result.responseText || "<none>");                
            }
        });
    }

    function flashAndHide(what) {
        new Effect.Pulsate(
            what,
            { duration: 0.75, pulses: 3, queue: 'front' }
        );

        new Effect.Opacity(
            what,
            { duration: 0.75, from: 1, to: 0, queue: 'end' }
        );

        hide(what);
	}

	function hide(what) {
        new Effect.BlindUp(what);
	}

    // HACK: See dashboard.xsl which calls these
    function toggleVisibleAndChangeImage(imageId, id) {
        var theElementToToggle = $(id);
        var theImage = $(imageId);

        var isVisible = theElementToToggle.visible();
        
        var _to      = isVisible ? 0 : 1
        var _from    = isVisible ? 1 : 0

        if (false == isVisible) {
            theElementToToggle.show();
            theImage.setAttribute("src", "/img/minus.gif")
        } else {
            theImage.setAttribute("src", "/img/plus.gif")
        }
        
        new Effect.Fade(
            id,
            { duration: 0.5, from: _from, to: _to }
        );
    }

    function showLoading(message) {
        showStatus("<img id=\"status_icon\" src=\"" + anotherLoadingGraphic + "\" /> " + message);
    }

    function showStatus(message) {
        $("coriander.haarlem.tabs.metrics.status").show()
        $("coriander.haarlem.tabs.metrics.status").update(message)
    }

    function hideStatus() {
        new Effect.Opacity(
            'coriander.haarlem.tabs.metrics.status',
            { duration: 1.0, from: 1, to: 0 }
        );
    }
</script>