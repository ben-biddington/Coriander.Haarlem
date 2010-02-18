function DilbertTabView(tab, tabImage, statusMessage, content) {
	this.tab 		= tab;
	this.tabImage 	= tabImage;
	this.statusMessage 	= statusMessage;
	this.content 	= content;
	
	var tabGraphic 	= '/plugins/coriander-haarlem/server/tabs/dilbert/dilbert.gif';

	this.showCartoon = function(html) {
		content.setStyle({ "opacity" : "0", "visibility" : "visible" });
		
		this.showStatus("");

		content.update(html);

		content.fade({ duration: 1.0, from: 0, to: 1, queue: 'end' });

		this.showTab();
	}
	
	this.showTab = function() {
		var slideDownBy = 25;
			
		new Effect.Move(tab, 
			{ 
				y: slideDownBy, 
				mode: 'relative', 
				queue: 'end', 
				duration: 0.0, 
				afterFinish : function() { tabImage.setAttribute('src', tabGraphic) } 
			}
		)	
		
		new Effect.Move(tab, 
			{ 
				y: -(slideDownBy), 
				mode: 'relative', 
				queue: 'end',
				duration: 1.0,
				queue: 'end' 
			}
		)
		
		new Effect.Pulsate(
			tabImage,
			{ duration: 1.0, pulses: 3, queue: 'end' }
		);
	}
	
	this.setTabIcon = function(url) {
		tabImage.setAttribute('src', url);
	} 
	
	this.showStatus = function(message) {
		statusMessage.update(message);
	}
}