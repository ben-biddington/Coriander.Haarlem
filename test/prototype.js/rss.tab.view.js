function DilbertTabView(tab, tabImage, cartoon, status) {
	this.tab 		= tab;
	this.tabImage 	= tabImage;
	this.cartoon 	= cartoon;
	this.status 	= status;
	
	var tabGraphic 	= 'img/dilbert.gif';
	
	this.show = function(html) {	}
	
	this.showCartoon = function(url) {
		cartoon.setStyle({ "opacity" : "0", "visibility" : "visible" });
		
		cartoon.setAttribute('src', url);
		
		this.showTab();
		
		this.showStatus("");
		
		cartoon.fade({ duration: 1.0, from: 0, to: 1 });
	}
	
	this.showTab = function() {
		var slideDownBy = 25;
			
		new Effect.Move(tab.getAttribute('id'), 
			{ 
				y: slideDownBy, 
				mode: 'relative', 
				queue: 'end', 
				duration: 0.0, 
				afterFinish : function() { tabImage.setAttribute('src', tabGraphic) } 
			}
		)	
		
		new Effect.Move(tab.getAttribute('id'), 
			{ 
				y: -(slideDownBy), 
				mode: 'relative', 
				queue: 'end',
				duration: 1.0
			}
		)
		
		new Effect.Pulsate(
			tabImage.getAttribute('id'),
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