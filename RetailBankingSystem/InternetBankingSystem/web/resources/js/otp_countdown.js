function countdownBegin() {
    var timer = document.getElementById("token-timer");
    var countdown = 60;
    setInterval(function () {
        if (--countdown > -1) {
            var sec = countdown % 60;
            var secstr = "";
            if (sec > 1)
                secstr = sec.toFixed(0) + " seconds";
            else
                secstr = sec.toFixed(0) + " second";
            if (countdown > 1)
                timer.innerHTML = secstr;
            else
                timer.innerHTML = secstr;
        }
    }, 1000);
}