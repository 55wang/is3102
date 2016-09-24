window.onload = function() {
  countdownBegin();
}; 

function countdownBegin(){
      var timer = document.getElementById("token-timer");
      var countdown = 600;
      setInterval(function () {
        if (--countdown > -1) {
            min = countdown/60 - 1;
            if(min < 0) min = 0;
            sec = countdown%60;
            if(min > 1)
                minstr = min.toFixed(0) + " minutes";
            else
                minstr = min.toFixed(0) + " minute";
            if(sec > 1)
                secstr = sec.toFixed(0) + " seconds";
            else
                secstr = sec.toFixed(0) + " second";
            if(countdown > 1)
                timer.innerHTML = minstr + " " + secstr;
            else
                timer.innerHTML = minstr + " " + secstr;
        }
    }, 1000);
  }