/*
Created by bmb0205 on 5/29/16.
JavaScript functions called by ../index.html and ../url.html
Uses jQuery and AJAX in scrolling functions and GET/POSTS requests
*/

// auto-scrolls to 'Why RecipeHealth?' div
function scrollToWhy() {
    $('html, body').animate({
        scrollTop: $('.why').offset().top
    }, 1000);
    return false;
}

// auto-scrolls to 'Flavonoids' div
function scrollToFlav() {
    $('html, body').animate({
        scrollTop: $('.flav').offset().top
    }, 1000);
    return false;
}

// auto-scrolls to 'Isoflavones' div
function scrollToIso() {
    $('html, body').animate({
        scrollTop: $('.iso').offset().top
    }, 1000);
    return false;
}

// auto-scrolls to 'Proanthocyanidins' div
function scrollToPro() {
    $('html, body').animate({
        scrollTop: $('.pro').offset().top
    }, 1000);
    return false;
}

// auto-scrolls to 'About Us' div
function scrollToAbout() {
    $('html, body').animate({
        scrollTop: $('.about').offset().top
    }, 1000);
    return false;
}

/*
AJAX POST request connecting to GetRecipeURL.do Java servlet which handles request
and queries PostgreSQL database before writing results to a table and appending to DOM
*/
function getURL() {
    var $urlString = $('.txtUrl').val();
    var params = {url: $urlString};
    var jsonDataString = JSON.stringify(params);
    var $jumboContainer = $('.jumboContainer');
    $.ajax({
        type: "POST",
        url: "GetRecipeURL.do",
        contentType: "application/json",
        dataType: "text",
        data: jsonDataString,
        success: function(data) {
            $jumboContainer.empty();
            $jumboContainer.append(data);
            $jumboContainer.css("font-size", "18");
        },
        error: function(data) {
            $jumboContainer.clear();
            $jumboContainer.append("Could not load the data!")
        }
    });
    return false;
}

window.onscroll = stickToTop();

function stickToTop() {
    var $navbar = $('.navbar-header .navbar-nav');
    if (window.pageYOffSet > 100) {
        $navbar.css('position', 'fixed');
        $navbar.css('top', '0');
    } else {
        $nvarbar.css('position', '');
        $navbar.css('top', '');
    }
}