function DilbertTabView(tab, tabImage, statusMessage, content) {
	this.tab 		= tab;
	this.tabImage 	= tabImage;
	this.statusMessage 	= statusMessage;
	this.content 	= content;
	
	var tabGraphic 	= '/plugins/coriander-haarlem/server/tabs/dilbert/dilbert.gif';
    var effectScope = 'tab_effect';

	this.showCartoon = function(html) {
		content.setStyle({ "opacity" : "0", "visibility" : "visible" });
		
		this.showStatus("");

		content.update(html);

		content.fade({
		    duration: 0.0,
		    from: 0, 
		    to: 1,
		    queue: { position: 'front', scope: effectScope }
		});

		this.showTab();
	}
	
	this.showTab = function() {
		var slideDownBy = 25;

        log('Moving down, position is now <' + tab.positionedOffset() + '>'); 

		new Effect.Move(tab, {
            y           : slideDownBy,
            mode        : 'relative',
            queue       : { position: 'end', scope: effectScope },
            duration    : 0.5,
            afterFinish : function() {
                tabImage.setAttribute('src', tabGraphic);
                log('Moved down, position is now <' + tab.positionedOffset() + '>'); 
            }
		});

		log('Moving up, position is now <' + tab.positionedOffset() + '>');

        new Effect.Move(tab, {
            y           : -(slideDownBy),
            mode        : 'relative',
            queue       : { position: 'end', scope: effectScope },
            duration    : 1.0,
            afterFinish : function() {
                log('Moved up, position is now <' + tab.positionedOffset() + '>');
            }
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