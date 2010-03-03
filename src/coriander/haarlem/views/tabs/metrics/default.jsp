<%@include file="../../include-internal.jsp"%>
<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.User" scope="request"/>

<link rel="stylesheet" type="text/css" href="${includeUrl}/metrics-tab.css" />
<link rel="stylesheet" type="text/css" href="/plugins/coriander-haarlem/server/tabs/rss.tab.view.css" />
<script src="/plugins/coriander-haarlem/server/tabs/rss.tab.view.js" type="text/javascript"></script>

<div class="coriander-haarlem-tab-container" style="padding:5px">
    <div id="coriander-haarlem-tab" class="tab"><a href="javascript:void(0)"><img id="coriander-haarlem-tab-tab_image" border="0"/></a></div><div id="coriander-haarlem-tab-status"></div>
    <div id="coriander-haarlem-tab-content"></div>
</div>

<script language="javascript">
    var loadingGraphic 	        = '/img/buildStates/running_green_transparent.gif';
    var tabView					= null;
    var url                     = "/metrics.html?user=${currentUser.id}";
    var includeUrl              = "${includeUrl}";
    
    var tabImage,tab,statusMessage,content;

    document.observe('dom:loaded', function() {
        tab 			= $('coriander-haarlem-tab');
        tabImage 		= $('coriander-haarlem-tab-tab_image');
        statusMessage 	= $('coriander-haarlem-tab-status');
        content         = $('coriander-haarlem-tab-content')

        tabView = new RssTabView(
            tab,
            tabImage,
            includeUrl + '/img/traffic-light.ico',
            statusMessage,
            content
        );

        tabView.setTabIcon(loadingGraphic);

        tabView.showStatus("Collecting dashboards...");

        window.setTimeout(loadCartoon, 100);
    });

    function loadCartoon() {
        new Ajax.Request(url, {
            method      : 'get',
            onSuccess   : function(result) {
                tabView.showCartoon(result.responseText);
            },
            onFailure   : function(result) { }
        });
    }

     // HACK: See dashboard.xsl which calls these
    function toggleVisibleAndChangeImage(imageId, id) {
        var theElementToToggle = $(id);
        var theImage = $(imageId);

        var isVisible = theElementToToggle.visible();

        var _to      = isVisible ? 0 : 1
        var _from    = isVisible ? 1 : 0

        if (false == isVisible) {
            theElementToToggle.setStyle({ "opacity" : "0", "visibility" : "visible" });
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