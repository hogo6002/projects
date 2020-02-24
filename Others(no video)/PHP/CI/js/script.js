$(document).ready(function () {
    $("#search").keyup(function () {
        $.ajax({
            type: "POST",
            url: "https://infs3202-bcf7bd3a.uqcloud.net/CI/search/GetCountryName",
            data: {
                keyword: $("#search").val()
            },
            dataType: "json",
            success: function (data) {
                if (data.length > 0) {
                    $('#searchDropDown').empty();
                    $('#search').attr("data-toggle", "searchDropDown");
                    $('#searchDropDown').css("display", "block");
                    // $('#searchDropDown').dropdown('toggle');
                }
                else if (data.length == 0) {
                    $('#searchDropDown').empty();
                    $('#search').attr("data-toggle", "");
                } else if ($("#search").val() == "") {
                    $('#searchDropDown').empty();
                }
                
                $first = "";
                $.each(data, function (key,value) {
                    if (data.length >= 0 && $("#search").val() != "") {
                        $('#searchDropDown').append('<li class = "search-list">' + value['itemName'] + '</li>');
                    }
                    
                });
            }
        });
    });
    $('#searchDropDown').on('click', 'li', function () {
        $i = -1;
        $('#search').val($(this).text());
        
    });
    $i = -1;
    $(document).keydown(function(e){
        if (e.which == 40) {
            $i = $i + 1; 
            $('#search').val($(".search-list:eq("+$i+")").text());
        $(".search-list:eq("+$i+")").css("background", "#c0c0c0");
        } else if (e.which == 38) {
            $i;
            $('#search').val($(".search-list:eq("+$i+")").text());
        $(".search-list:eq("+$i+")").css("background", "#c0c0c0");
        }
        
    });
});

$(document.body).click( function() {
    $i = -1;
    $('#searchDropDown').css("display", "none");
});





var myIndex = 0;
var slideIndex = 1;
var slideIndex1 = 1;
showDivs(slideIndex);
showDivs1(slideIndex1);
carousel();
var login=document.getElementById("login-button");

/*********************automatic slideshow*********************/
function carousel() {
  var i;
  var x = document.getElementsByClassName("home-slides");
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";  
  }
  myIndex++;
  if (myIndex > x.length) {myIndex = 1} 
  changeColor();
  x[myIndex-1].style.display = "block";  
  $('.container').mouseover(function(event){
    var y = document.getElementsByClassName("img-container");
    y[0].style.opacity = "0.5";
    });
    $('.container').mouseout(function(event){
        var y = document.getElementsByClassName("img-container");
        y[0].style.opacity = "1";
        });
  setTimeout(carousel, 3000); // Change image every 3 seconds
}

/**************************change text colour********************/
function changeColor() {
    var x = document.getElementById("centered-text");
    if (myIndex % 2 == 0) {
        x.style.color = "black";
    } else {
        x.style.color = "#EEEEEE";
    }
}

/************************************change top picks*****************/
function plusDivs(n) {
  showDivs(slideIndex += n);
}

function showDivs(n) {
  var i;
  var x = document.getElementsByClassName("top-items");
  if (n > x.length) {slideIndex = 1}
  if (n < 1) {slideIndex = x.length}
  for (i = 0; i < x.length; i++) {
     x[i].style.display = "none";  
  }
  x[slideIndex-1].style.display = "flex";  
}

/************************************change for you**********************/
function plusDivs1(n) {
    showDivs1(slideIndex1 += n);
}

function showDivs1(n) {
    var i;
    var x = document.getElementsByClassName("for-you-items");
    if (n > x.length) {slideIndex1 = 1}
    if (n < 1) {slideIndex1 = x.length}
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";  
    }
    x[slideIndex1-1].style.display = "flex";  
  }


/*******************login******************/
// function openLogin() {
//     var form = document.getElementById("login-form");
//     form.style.display = "block";
//     // document.getElementById("register-form").style.display = "none";
// }

// function closeLogin() {
//     var form = document.getElementById("login-form");
//     form.style.display = "none";
//     document.getElementById("firstname").value = "";
//     document.getElementById("lastname").value = "";
//     document.getElementById("pass0").value = "";
//     document.getElementById("pass1").value = "";
//     document.getElementsByClassName("email")[0].value = "";
//     document.getElementsByClassName("email")[1].value = "";
//     document.getElementById("firstname").style.border = "0 solid green";
//     document.getElementById("lastname").style.border = "0 solid green";
//     document.getElementById("pass0").style.border = "0 solid green";
//     document.getElementById("pass1").style.border = "0 solid green";
//     document.getElementsByClassName("email")[0].style.border = "0 solid green";
//     document.getElementsByClassName("email")[1].style.border = "0 solid green";
//     document.getElementById("email-reminder").style.display = "none";
//     document.getElementById("pass-reminder").style.display = "none";
// }


// $('.login-button').click(function (event) {
//     closeReg();
//     openLogin();
//     event.preventDefault();
//     var cover = document.getElementById("cover");
//     cover.style.display = "block";
//     $('body').addClass('stop-scrolling');
// });

// $('.close').click(function (event) {
//     closeLogin();
//     var cover = document.getElementById("cover");
//     cover.style.display = "none";
//     $('body').removeClass('stop-scrolling');
// });

// function openReg() {
//     closeLogin();
//     var form = document.getElementById("register-form");
//     form.style.display = "block";
// }

// function closeReg() {
//     var form = document.getElementById("register-form");
//     form.style.display = "none";
//     document.getElementById("firstname").value = "";
//     document.getElementById("lastname").value = "";
//     document.getElementById("pass0").value = "";
//     document.getElementById("pass1").value = "";
//     document.getElementsByClassName("email")[0].value = "";
//     document.getElementsByClassName("email")[1].value = "";
//     document.getElementById("firstname").style.border = "0 solid green";
//     document.getElementById("lastname").style.border = "0 solid green";
//     document.getElementById("pass0").style.border = "0 solid green";
//     document.getElementById("pass1").style.border = "0 solid green";
//     document.getElementsByClassName("email")[0].style.border = "0 solid green";
//     document.getElementsByClassName("email")[1].style.border = "0 solid green";
//     document.getElementById("email-reminder").style.display = "none";
//     document.getElementById("pass-reminder").style.display = "none";
// }


// $('.register-button').click(function (event) {
//     event.preventDefault();
//     openReg();
//     var cover = document.getElementById("cover");
//     cover.style.display = "block";
//     $('body').addClass('stop-scrolling');
//     document.getElementById("email-reminder").style.display = "none";
// });

// $('.close').click(function (event) {
//     closeReg();
//     var cover = document.getElementById("cover");
//     cover.style.display = "none";
//     $('body').removeClass('stop-scrolling');
//     document.getElementById("firstname").value = "";
//     document.getElementById("lastname").value = "";
//     document.getElementById("pass0").value = "";
//     document.getElementById("pass1").value = "";
//     document.getElementsByClassName("email")[0].value = "";
//     document.getElementsByClassName("email")[1].value = "";
//     document.getElementById("firstname").style.border = "0 solid green";
//     document.getElementById("lastname").style.border = "0 solid green";
//     document.getElementById("pass0").style.border = "0 solid green";
//     document.getElementById("pass1").style.border = "0 solid green";
//     document.getElementsByClassName("email")[0].style.border = "0 solid green";
//     document.getElementsByClassName("email")[1].style.border = "0 solid green";
//     document.getElementById("email-reminder").style.display = "none";
//     document.getElementById("pass-reminder").style.display = "none";
// });


/****************dropdown menu********************/
$('#motor-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[0].style.display = "block";
});

$('#motor-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[0].style.display = "none";
});

$('#bags-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[1].style.display = "block";
});

$('#bags-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[1].style.display = "none";
});

$('#furn-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[2].style.display = "block";
});

$('#furn-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[2].style.display = "none";
});

$('#pets-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[3].style.display = "block";
});

$('#pets-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[3].style.display = "none";
});

$('#electronic-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[4].style.display = "block";
});

$('#electronic-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[4].style.display = "none";
});

$('#clothing-drop').mouseover(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[5].style.display = "block";
});

$('#clothing-drop').mouseout(function(event){
    var content = document.getElementsByClassName("drop-content");
    content[5].style.display = "none";
});

$('#pass1').on('blur', function(){
    if(this.value.length < 8){ 
        document.getElementById("pass1").style.border = "3px solid red";
        document.getElementById("pass-reminder").style.display = "block";
       $(this).focus(); // focuses the current field.
       
       return false; // stops the execution.
    } else {
        document.getElementById("pass1").style.border = "3px solid green";
        document.getElementById("email-reminder").style.display = "none";
    }
});

$('#firstname').on('blur', function(){
    if (this.value.length < 1) {
        this.style.border = "3px solid red";
    } else {
        this.style.border = "3px solid green";
    }   
});

$('#firstname').on('click', function(){
    document.getElementById("pass1").style.border = "0px solid red";
});

$('#lastname').on('blur', function(){
    if (this.value.length < 1) {
        this.style.border = "3px solid red";
    } else {
        this.style.border = "3px solid green";
    }
    
});

$('#lastname').on('click', function(){
    document.getElementById("pass1").style.border = "0px solid red";
});

$('#pass1').on('click', function(){
    document.getElementById("pass1").style.border = "0px solid red";
    document.getElementById("pass-reminder").style.display = "none";
});

$('.email').on('blur', function(){
    var flag = 0;
    for (i = 0; i < this.value.length; i++) {
        if (this.value[i] == '@') {
            flag = 1;
            break;
        }
    }
    if (flag == 0) {
        this.style.border = "3px solid red";
        document.getElementById("email-reminder").style.display = "block";
    } else {
        this.style.border = "3px solid green";
        document.getElementById("email-reminder").style.display = "none";
    }
});

$('.email').on('click', function(){
    this.style.border = "0px solid red";
    document.getElementById("email-reminder").style.display = "none";
});

$('#firstname').on('click', function(){
    this.style.border = "0px solid red";
});
$('#lastname').on('click', function(){
    this.style.border = "0px solid red";
});

$('#reg-btn').on('click', function(){
    if(document.getElementById("pass1").value.length < 8){
        document.getElementById("pass1").style.border = "3px solid red";
        document.getElementById("pass-reminder").style.display = "block";
    }
    var flag = 0;
    for (i = 0; i < document.getElementsByClassName("email")[1].value.length; i++) {
        if (document.getElementsByClassName("email")[1].value[i] == '@') {
            flag = 1;
            break;
        }
    }
    if (flag == 0) {
        document.getElementsByClassName("email")[1].style.border = "3px solid red";
        document.getElementById("email-reminder").style.display = "block";
    }

    var firstname = document.getElementById("firstname");
    if (firstname.value.length < 1) {
        firstname.style.border = "3px solid red";
    }
    var lastname = document.getElementById("lastname");
    if (lastname.value.length < 1) {
        lastname.style.border = "3px solid red";
    }
});

$('#login-btn').on('click', function(){
    if(document.getElementById("pass0").value.length < 1){
        document.getElementById("pass0").style.border = "3px solid red";
    }
    var flag = 0;
    for (i = 0; i < document.getElementsByClassName("email")[0].value.length; i++) {
        if (document.getElementsByClassName("email")[0].value[i] == '@') {
            flag = 1;
            break;
        }
    }
    if (flag == 0) {
        document.getElementsByClassName("email")[0].style.border = "3px solid red";
    }
});

$('#pass0').on('click', function(){
    document.getElementById("pass0").style.border = "0px solid red";
});

  



/*************AJAX****************** */
