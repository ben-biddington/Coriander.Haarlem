<link rel="stylesheet" type="text/css" href="/plugins/coriander-haarlem/server/tabs/rss.tab.view.css" />
<script src="/plugins/coriander-haarlem/server/tabs/rss.tab.view.js" type="text/javascript"></script>

<div id="coriander-haarlem-carrot-tab-content-structure" style="padding:5px">
    <div id="coriander-haarlem-carrot-tab-tab" class="tab"><a href="javascript:void(0)"><img id="coriander-haarlem-carrot-tab-content-tab_image" border="0"/></a></div><div id="coriander-haarlem-carrot-tab-tab-status"></div>
    <div id="coriander-haarlem-carrot-tab-content"></div>
</div>

<script language="javascript">
    var anotherLoadingGraphic 	= '/img/buildStates/running_green_transparent.gif';
    var tabView					= null;
    var url                     = '/dilbert.html';
    var tabImage,tab,statusMessage,content;

    document.observe('dom:loaded', function() {
        tab 			= $('coriander-haarlem-carrot-tab-tab');
        tabImage 		= $('coriander-haarlem-carrot-tab-content-tab_image');
        statusMessage 	= $('coriander-haarlem-carrot-tab-tab-status');
        content         = $('coriander-haarlem-carrot-tab-content')

        tabView = new DilbertTabView(tab, tabImage, statusMessage, content);

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