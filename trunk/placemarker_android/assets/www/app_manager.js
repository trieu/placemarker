var watchID = null;

var getLocation = function(mode) {
	//var loc_des = prompt("Location description", "");
	//showMap($('#theMapView'),'mode=save&loc_des='+ loc_des +'&mobileApp=true&ll=10.7702,106.708338');
  var suc = function(p){
	    //alert(p.coords.latitude + " " + p.coords.longitude);
	    var ll = p.coords.latitude + "," + p.coords.longitude;
	    if(mode == "save") {
	    	var loc_des = prompt("Location description", "");
	    	showMap($('#theMapView'),'mode=save&loc_des='+ loc_des +'&mobileApp=true&ll=' + ll);
	    } else {			    	
	    	if($(node).attr('src').length > 0){
	    		$('#theMapView')[0].contentWindow.updateRootMarker(p.coords.latitude, p.coords.longitude);
	    	} else {
	    		showMap($('#theMapView'),'mobileApp=true&ll=' + ll);
	    	}
	    }
  };
  var fail = function(){ alert('getCurrentPosition failed!');};
  navigator.geolocation.getCurrentPosition(suc,fail,{enableHighAccuracy:true});
}

function test1(){
	$('#theMapView')[0].contentWindow.updateRootMarker(10.7902,106.18338);		    
}

function mapLoaded(node) {
    try {
	    $(node).show();
    } catch(e) {alert(e.message);}
}

function showMap(node,params){
	var map_url = "http://drd-vn-database.com/mobile/v_map_mobile.html?" + params;
	$(node).attr('src',map_url).show();
}

// PhoneGap is ready
//
function onDeviceReady() {
    var options = { frequency: 8000 , enableHighAccuracy:true};  // Update every 8 seconds
    watchID = navigator.geolocation.watchPosition(onWatchPositionSuccess, onWatchPositionError, options);

    var success = function(rs){  };
    var fail = function(rs){  }; 
    PhoneGap.exec(success, fail, "MarkerManager", "sayHi", ['Trieu']);
}

function onLocationChanged(latitude, longitude ) {
    var element = document.getElementById('geolocation');
    element.innerHTML = 'LocationChanged, Latitude: '  + latitude + ', ' + 'Longitude: ' + longitude + '<hr />';
}

function onWatchPositionSuccess(position) {
    try {
        var element = document.getElementById('geolocation');
        element.innerHTML = 'Latitude: '  + position.coords.latitude + ', Longitude: ' + position.coords.longitude + '<hr />';
        $('#theMapView')[0].contentWindow.updateRootMarker(position.coords.latitude, position.coords.longitude);
    } catch(e) {alert(e.message);}
}

function onWatchPositionError(error) {
	// onError Callback receives a PositionError object
    alert('code: '    + error.code    + '\n' +
          'message: ' + error.message + '\n');
}
		
function onLoad(){	
//	showMap($('#theMapView'),'mobileApp=true');		
	document.addEventListener("deviceready", onDeviceReady, false);
}

$(document).ready(function(){
				
});