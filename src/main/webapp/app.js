function field_focus(field, email) {
	if (field.value == email) {
		field.value = '';
	}
}

function field_blur(field, email) {
	if (field.value == '') {
		field.value = email;
	}
}

//Stop click event
$('a').click(function(event) {
	event.preventDefault();
});

var webSocketChat = new WebSocket('ws://192.168.15.19:8080/websocket-web/wschat');
var webSocketUser = new WebSocket('ws://192.168.15.19:8080/websocket-web/wsuser');
var login2 = false;

function login() {
	login2 = true;
	$("#main").show();
	$("#loginDiv").hide();
	webSocketUser.send($('#login').val());
	
	webSocketChat.onmessage = function(event) {
		onMessage(event)
	};
	webSocketUser.onmessage = function(event) {
		onMessageUser(event)
	};

	function onError(event) {
		alert(event.data);
	}
}

function onMessage(event) {
	var msg = $('#messages').html();
	$('#messages').html(event.data + '<br/>'+msg);
}
function onMessageUser(event) {
	$('#users').html('');
	$('#users').html('<h1>Usuarios</h1> <br/>' + event.data + '<br/>');
}

function send() {
	webSocketChat.send($('#login').val()+': '+$('#text').val());
	$('#text').val('');

	return false;
}

$(document).keypress(function(e){
    if (e.which == 13 && login2 == true && $('#text').val().trim() != ''){
        $("#send").click();
    }
});

$(document).ready(function() {
	$('.box').hide().fadeIn(1000);
	$("#main").hide();
	$('#text').val('');
	login2 = false;
});