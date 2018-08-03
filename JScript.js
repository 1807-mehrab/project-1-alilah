

document.getElementById("welcome").innerHTML = "<h1>Hello document.loginform.username.value</h1>"


function welcomeMessage() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                document.getElementById("welcome").innerHTML = xhr.responseText;
            } else {
                console.log("error");
            }
        }
    }
    xhr.open('GET', '/Reservation/controller/LogingServlet');
    xhr.send();
}