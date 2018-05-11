var mytimeSession;
var tSession;
var tempo = 0;

document.onkeydown = fkey;
document.onkeypress = fkey;
document.onkeyup = fkey;
document.onclick = fkey;
document.onerror = fkey;
document.onchange = fkey;
function fkey(){
    verifyRedirectSessionExpired();
}

function verifyRedirectSessionExpired(){
    var end = 0;
    if (tempo >= end) {
        startTimerSession();
    } else {
        localStorage.setItem("menu","menuHome");
        window.location.href = "/siaag/logout";
    }
}

function display_c_session(start) {
    tempo = parseFloat(start);
    var end = 0; // change this to stop the counter at a higher value
    var refresh = 1000; // Refresh rate in milli seconds
    if (tempo >= end) {
        mytimeSession = setTimeout('display_ct_session()', refresh);
    } else {
        stopTimerSession();
        PF('sessionDialog').show();
    }
}

function display_ct_session() {
    // Calculate the number of days left
    var days = Math.floor(tempo / 86400);
    // After deducting the days calculate the number of hours left
    var hours = Math.floor((tempo - (days * 86400)) / 3600)
    // After days and hours , how many minutes are left
    var minutes = Math.floor((tempo - (days * 86400) - (hours * 3600)) / 60)
    // Finally how many seconds left after removing days, hours and minutes.
    var secs = Math.floor((tempo - (days * 86400) - (hours * 3600) - (minutes * 60)))

    var x = "(" + days + " Days " + hours + " Hours " + minutes + " Minutes and " + secs + " Seconds " + ")";

    tempo = tempo - 1;
    console.log(x);

    tt = display_c_session(tempo);
}

function startTimerSession() {
    stopTimerSession();
    display_c_session(3540);
}

function stopTimerSession() {
    clearTimeout(tSession);
    clearTimeout(mytimeSession);
    //window.stop();
}