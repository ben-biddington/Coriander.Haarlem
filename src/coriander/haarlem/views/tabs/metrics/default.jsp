<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.User" scope="request"/>

<style type="text/css">
    div.status {
        text-align:left;
        background-color:#FFF;
        padding:5px;
        margin-bottom:2px;
        border: 1px solid #CECECE;
        border-left-width: 0;
        border-right-width: 0;
        border:0;
        width:100%;
    }
    
    div.status img { vertical-align:middle; margin-bottom:3px; }

    div.charts { clear: both; }
    div.chart, div.metric { float: left; margin-right: 5px; }
    div.metric div.values h2 { font-size: normal; }

    h4.metric-title { width: 200px; margin-top:0px; }
    h4.metric-body { padding-left:5px; }
    div.metric-icon { float:left; padding:2px; }
    div.metric-percent { float:left; padding:2px; }
    div.metric-some-other-number { float:left; padding:2px; }
    table.metric td { vertical-align:top; }

    div.build { width:100%; }

    div.project {
        margin: 0;
        padding: 0 10px;
        background: #4b4b4a url(/img/bgBuildTitle.gif) repeat-x 0 0;
        border-top: solid 1px #41403f;
        border-left: solid 1px #41403f;
        border-right: solid 1px #41403f;
    }

    div.project h2 {
        margin: 0;
        padding: 3px 0 2px 0;
        font-family: trebuchet ms, verdana, tahoma, arial, sans-serif;
        color: #fff;
        font-size: 130%;
        font-weight: normal;
    }

    div.project h2, div.project a { color: #FFF; }
    a.buildName {
        margin: 0 0 0 6px;
        padding: 0 0 0 5px;
        font-family: tahoma, arial, sans-serif;
        font-size: 110%;
        font-weight:bold;
    }

    img#status_icon { width:16px; height:16px; }
</style>
<div id="coriander.haarlem.tabs.metrics.status" class="status"></div>
<div id="coriander.haarlem.tabs.metrics.results"></div>

<script language="javascript">
    var loadingGraphic          = '../../img/ajax-loader.gif';
    var anotherLoadingGraphic   = '../../img/buildStates/running_green_transparent.gif';
    var okGraphic 				= '../../img/buildStates/success_small.gif';
    var currentUser = "";
    
    document.observe("dom:loaded", function() {
        showLoading("Collecting dashboards...");

        currentUser = "${currentUser.id}";
        
        window.setTimeout(fill, 500);
    });

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

    function fill() {
        var url = "/metrics.html?user=" + currentUser;
        
        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
                showLoading("");
                
                $("status_icon").setAttribute("src", okGraphic);

				flashAndHide("status_icon");
					
                $("coriander.haarlem.tabs.metrics.results").update(result.responseText || "<none>");

                $("coriander.haarlem.tabs.metrics.results").update(result.responseText || "<none>");

                // TODO: Do we need to do this just to stop flashing?
                $("coriander.haarlem.tabs.metrics.results").setStyle({ "display" : "block", "opacity" : "0" });

                new Effect.Opacity(
                    'coriander.haarlem.tabs.metrics.results',
                    { duration: 0.5, from: 0, to: 1, queue: 'front' }
                );
            },
            onFailure: function(result) {
                showStatus("Failed: " + result.responseText || "<none>");                
            }
        });
    }

    function flashAndFade(what) {
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
</script>