<style type="text/css">
    div#coriander-haarlem-carrot-tab-content {
        z-index:100;
        height:100%;
        position:relative;
        margin-top:0px;
        padding:10px;
        clear:both;
        border:0px solid #E0E0E0;
        background: url('/img/underline.gif') no-repeat;
        background-position: -700 0;
        background-color: #fff;
    }

    div#coriander-haarlem-carrot-tab-tab { float: left; }
    div#coriander-haarlem-carrot-tab-tab-status { float: left; color: #F0F0F0; padding: 5px; vertical-align: middle; }

    div.tab {
        z-index:0;
        position:relative;
        top: 3px;
        border-color: #F0F0F0;
        border-style: solid;
        border-top-width: 1px;
        border-right-width: 1px;
        border-bottom-width: 0px;
        border-left-width: 1px;
        padding: 5px;
        width: 35px;
        width: 35px;
        text-align:center;
        background: url('img/top_gradient.png') repeat-x;
        background-position: 0 7;
        margin:0px;
        margin-left:5px;
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
        -moz-border-top-left-radius: 4px;
        -moz-border-top-right-radius: 4px;
        -webkit-border-top-left-radius: 4px;
        -webkit-border-top-right-radius: 4px;
    }
</style>
<script src="/plugins/coriander-haarlem/server/rss.tab.view.js" type="text/javascript"></script>

<div id="coriander-haarlem-carrot-tab-content-structure" style="padding:5px">
    <div id="coriander-haarlem-carrot-tab-tab" class="tab"><a href="javascript:void(0)"><img id="coriander-haarlem-carrot-tab-content-tab_image" border="0"/></a></div>
    <div id="coriander-haarlem-carrot-tab-tab-status"></div>
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