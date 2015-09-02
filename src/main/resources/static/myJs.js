const IMAGE_TIME = 10000;
const FADE_TIME = 2000;
const LOAD_TIMEOUT = 20000;

$(document).ready(start);

var urls;

function start() {
    loadNextImage(function (image) {
        appendImage(image);
        advance();
    });
}

function advance() {
    loadNextImage(function (next) {
        appendImage(next);
        setTimeout(function () {
            transitionImages(function () {
                advance();
            });
        }, IMAGE_TIME)
    });
}

function loadUrls(callback) {
    $.ajax({
        url: '/images/next',
        dataType: "json"
    })
        .done(function (data) {
            urls = data;
            console.log("Loaded urls: " + urls.join(" "));
            callback(data);
        })
        .fail(function (e) {
            console.log("Failed to load urls, retrying: " + e);
            loadUrls(callback);
        });
}

function loadNextImage(callback) {
    if (urls && urls.length) {
        var url = urls.pop();
        loadImage(url,
            function (image) {
                callback(image);
            },
            function (error) {
                loadNextImage(callback);
            });
    } else {
        loadUrls(function (urls) {
            loadNextImage(callback);
        });
    }
}

function loadImage(url, success, failure) {
    var image = new Image();
    var complete = false;
    image.onload = function () {
        if (!complete) {
            complete = true;
            console.log("Loaded image: " + url);
            success(image);
        }
    };
    image.onerror = function () {
        if (!complete) {
            complete = true;
            console.log("Error while loading image: " + url);
            failure();
        }
    };
    image.src = url;
    setTimeout(function () {
        if (!complete) {
            complete = true;
            console.log("Timeout while loading image: " + url);
            failure();
        }
    }, LOAD_TIMEOUT);
}

function transitionImages(callback) {
    console.log("Transitioning images");
    var $first = $('#images > div:first');
    $first
        .fadeOut(FADE_TIME)
        .next()
        .fadeIn(FADE_TIME, function () {
            $first.remove();
            callback();
        });
}

function appendImage(image) {
    var $img = $('<div><img src="' + image.src + '"></img></div>');
    $("#images").append($img);
}
