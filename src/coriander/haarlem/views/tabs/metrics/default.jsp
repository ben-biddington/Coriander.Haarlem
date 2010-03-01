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
    var anotherLoadingGraphic 	= '/img/buildStates/running_green_transparent.gif';
    var tabView					= null;
    var url                     = "/metrics.html?user=${currentUser.id}";
    var tabImage,tab,statusMessage,content;

    document.observe('dom:loaded', function() {
        tab 			= $('coriander-haarlem-tab');
        tabImage 		= $('coriander-haarlem-tab-tab_image');
        statusMessage 	= $('coriander-haarlem-tab-status');
        content         = $('coriander-haarlem-tab-content')

        tabView = new RssTabView(
            tab,
            tabImage,
            '/plugins/coriander-haarlem/server/tabs/fail/fail.gif',
             statusMessage,
             content
        );

        tabView.setTabIcon(anotherLoadingGraphic);

        tabView.showStatus("Waiting...");

        window.setTimeout(loadCartoon, 2000);
    });

    function loadCartoon() {
        var req = new Ajax.Request(url, {
            method: 'get',
            onSuccess: function(result) {
               tabView.showCartoon(result.responseText);
            },
            onFailure: function(result) { }
        });
    }
</script>