function DilbertTabView(tab, tabImage, cartoon, status) {
	this.tab 		= tab;
	this.tabImage 	= tabImage;
	this.cartoon 	= cartoon;
	this.status 	= status;
	
	this.prototype.show = function(html) {	}
	
	function showCartoon(url) {
		cartoon.setStyle({ "opacity" : "0", "visibility" : "visible" });
		
		cartoon.setAttribute('src', url);
		
		showDilbert();
		
		showStatus("");
		
		cartoon.fade({ duration: 1.0, from: 0, to: 1 });
	}
	
	function showTab() {
		var slideDownBy = 25;
			
		new Effect.Move(tab.getAttribute('id'), 
			{ 
				y: slideDownBy, 
				mode: 'relative', 
				queue: 'end', 
				duration: 0.0, 
				afterFinish : function() { tabImage.setAttribute('src', dilbertGraphic); } 
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
	
	function showStatus(message) {
		statusMessage.update(message);
	}
}

function DilbertTabModel() {
	this.prototype.onload = function(doWhat) {
		
	}
}