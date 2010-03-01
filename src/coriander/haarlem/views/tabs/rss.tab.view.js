function RssTabView(tab, tabImage, tabGraphicUrl, statusMessage, content) {
	this.tab 		    = tab;
	this.tabImage 	    = tabImage;
	this.statusMessage 	= statusMessage;
	this.content 	    = content;
	this.tabGraphicUrl  = tabGraphicUrl;
	
    var effectScope = 'tab_effect';

	this.showCartoon = function(html) {
		content.setStyle({ "opacity" : "0", "visibility" : "visible" });
		
		this.showStatus("");

		content.update(html);

		content.fade({
		    duration    : 0.25,
		    from        : 0,
		    to          : 1,
		    queue       : { position: 'front', scope: effectScope }
		});

		this.showTab();
	}
	
	this.showTab = function() {
		var slideDownBy = 25;

        new Effect.Move(tab, {
            y           : slideDownBy,
            mode        : 'relative',
            queue       : { position: 'end', scope: effectScope },
            duration    : 0.5,
            afterFinish : function() {
                tabImage.setAttribute('src', tabGraphicUrl);
            }
		});

		new Effect.Move(tab, {
            y           : -(slideDownBy),
            mode        : 'relative',
            queue       : { position: 'end', scope: effectScope },
            duration    : 1.0
		});

		new Effect.Pulsate(tabImage, {
		    duration    : 1.0,
		    pulses      : 3,
		    queue       : { position: 'end', scope: effectScope }
		});
	}
	
	this.setTabIcon = function(url) {
		tabImage.setAttribute('src', url);
	} 
	
	this.showStatus = function(message) {
		statusMessage.update(message);
	}

	function log(what) {
	    if (typeof(console) == 'object') {
	        console.debug(what);
	    }
	}
}