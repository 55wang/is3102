 function countdownBegin(){
      var timer = document.getElementById("logout-timer");
      countdown = 20;
      setInterval(function () {
        if (--countdown > -1) {
            if(countdown > 1)
                timer.innerHTML = countdown.toString() + " seconds";
            else
                timer.innerHTML = countdown.toString() + " second";
        }
    }, 1000);
  }