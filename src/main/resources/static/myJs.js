


var lastTime;
var img;
var images;

$(document).ready(function() {
    img = document.getElementById("img");
    var container = $("#images")
    img.style.maxWidth = container.innerWidth() + 'px';
    img.style.maxHeight = container.innerHeight() + 'px';
    lastTime = new Date().getTime();
    start()

    $(img)
        .on('load', function(){
            setTimeout(click, 8000)
        })
        .on('error', function() {
            console.log("Error loading image " + img.src);
            click()
        });
});

var click = function() {
    if (images.length == 0) {
        start();
    } else {
        var src = images.pop();
        if (src == null) {
            click();
        } else {
            var newTime = new Date().getTime();
            console.log("Loading new image " + src + " after " + (newTime - lastTime) + " ms");
            lastTime = newTime
            img.src = src;
        }
    }
};

var show = function(newImages) {
    if (newImages.length == 0) {
        console.log("Obtained empty list of next images");
        setTimeout(start, 10000);
    } else {
        images = newImages;
        click();
    }
};


function start() {
    $.ajax({
  		url: '/images/next',
  		dataType: "json"
	})
    .done(function( data ) {
    	show(data);
  	})
  	.fail(function(e) {
  	    console.log(e)
  	    start();
  	})
}



