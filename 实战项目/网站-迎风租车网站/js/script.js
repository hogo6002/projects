  window.onLoad  = function(){
        var map = new AMap.Map('container', {
            zoom:18,//级别
            center: [109.00885, 28.44584],//中心点坐标
            viewMode:'3D'//使用3D视图

        });
        
         var infoWindow = new AMap.InfoWindow({ //创建信息窗体
        isCustom: true,  //使用自定义窗体
        content:'<div><a href="https://surl.amap.com/48az3dN1w5K3">导航到秀山迎凤汽车租赁</a></div>', //信息窗体的内容可以是任意html片段
        offset: new AMap.Pixel(16, -45)
    });
    var onMarkerClick  =  function(e) {
        infoWindow.open(map, e.target.getPosition());//打开信息窗体
        //e.target就是被点击的Marker
    } 
    var marker = new AMap.Marker({
            position:[109.00885, 28.44584]//位置
        })
    map.add(marker);
    marker.on('click',onMarkerClick);//绑定click事件
  }
  var url = 'https://webapi.amap.com/maps?v=1.4.15&key=81294e08b6fc845c8b8f07c64b2baed4&callback=onLoad';
  var jsapi = document.createElement('script');
  jsapi.charset = 'utf-8';
  jsapi.src = url;
  document.head.appendChild(jsapi);


var login=document.getElementById("login-button");









/*************************Drop down menu********************/
$(document).ready(function(){
    $("nav").toggleClass("not-show");
    $("#nav-btn").click(function(){
        $("nav").toggleClass("not-show");
    });
});    





/*******************login******************/
// function openLogin() {
//     var form = document.getElementById("login-form");
//     form.style.display = "block";
//     document.getElementById("register-form").style.display = "none";
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


// /****************dropdown menu********************/
// $('#motor-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[0].style.display = "block";
// });

// $('#motor-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[0].style.display = "none";
// });

// $('#bags-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[1].style.display = "block";
// });

// $('#bags-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[1].style.display = "none";
// });

// $('#furn-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[2].style.display = "block";
// });

// $('#furn-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[2].style.display = "none";
// });

// $('#pets-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[3].style.display = "block";
// });

// $('#pets-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[3].style.display = "none";
// });

// $('#electronic-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[4].style.display = "block";
// });

// $('#electronic-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[4].style.display = "none";
// });

// $('#clothing-drop').mouseover(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[5].style.display = "block";
// });

// $('#clothing-drop').mouseout(function(event){
//     var content = document.getElementsByClassName("drop-content");
//     content[5].style.display = "none";
// });

// $('#pass1').on('blur', function(){
//     if(this.value.length < 8){ 
//         document.getElementById("pass1").style.border = "3px solid red";
//         document.getElementById("pass-reminder").style.display = "block";
//        $(this).focus(); // focuses the current field.
       
//        return false; // stops the execution.
//     } else {
//         document.getElementById("pass1").style.border = "3px solid green";
//         document.getElementById("email-reminder").style.display = "none";
//     }
// });

// $('#firstname').on('blur', function(){
//     if (this.value.length < 1) {
//         this.style.border = "3px solid red";
//     } else {
//         this.style.border = "3px solid green";
//     }   
// });

// $('#firstname').on('click', function(){
//     document.getElementById("pass1").style.border = "0px solid red";
// });

// $('#lastname').on('blur', function(){
//     if (this.value.length < 1) {
//         this.style.border = "3px solid red";
//     } else {
//         this.style.border = "3px solid green";
//     }
    
// });

// $('#lastname').on('click', function(){
//     document.getElementById("pass1").style.border = "0px solid red";
// });

// $('#pass1').on('click', function(){
//     document.getElementById("pass1").style.border = "0px solid red";
//     document.getElementById("pass-reminder").style.display = "none";
// });

// $('.email').on('blur', function(){
//     var flag = 0;
//     for (i = 0; i < this.value.length; i++) {
//         if (this.value[i] == '@') {
//             flag = 1;
//             break;
//         }
//     }
//     if (flag == 0) {
//         this.style.border = "3px solid red";
//         document.getElementById("email-reminder").style.display = "block";
//     } else {
//         this.style.border = "3px solid green";
//         document.getElementById("email-reminder").style.display = "none";
//     }
// });

// $('.email').on('click', function(){
//     this.style.border = "0px solid red";
//     document.getElementById("email-reminder").style.display = "none";
// });

// $('#firstname').on('click', function(){
//     this.style.border = "0px solid red";
// });
// $('#lastname').on('click', function(){
//     this.style.border = "0px solid red";
// });

// $('#reg-btn').on('click', function(){
//     if(document.getElementById("pass1").value.length < 8){
//         document.getElementById("pass1").style.border = "3px solid red";
//         document.getElementById("pass-reminder").style.display = "block";
//     }
//     var flag = 0;
//     for (i = 0; i < document.getElementsByClassName("email")[1].value.length; i++) {
//         if (document.getElementsByClassName("email")[1].value[i] == '@') {
//             flag = 1;
//             break;
//         }
//     }
//     if (flag == 0) {
//         document.getElementsByClassName("email")[1].style.border = "3px solid red";
//         document.getElementById("email-reminder").style.display = "block";
//     }

//     var firstname = document.getElementById("firstname");
//     if (firstname.value.length < 1) {
//         firstname.style.border = "3px solid red";
//     }
//     var lastname = document.getElementById("lastname");
//     if (lastname.value.length < 1) {
//         lastname.style.border = "3px solid red";
//     }
// });

// $('#login-btn').on('click', function(){
//     if(document.getElementById("pass0").value.length < 1){
//         document.getElementById("pass0").style.border = "3px solid red";
//     }
//     var flag = 0;
// });

// $('#pass0').on('click', function(){
//     document.getElementById("pass0").style.border = "0px solid red";
// });



var myIndex = 0;
carousel();
/*********************automatic slideshow*********************/
function carousel() {
  var i;
  var x = document.getElementsByClassName("home-slides");
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";  
  }
  myIndex++;
  if (myIndex > x.length) {myIndex = 1} 
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

  