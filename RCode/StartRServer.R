# [cmd-a and then cmd-enter] to run the R server with port 6311 
startServer <- function() {
  library(Rserve)
  library(RSclient)
  Rserve(args = "--vanilla", port = 6311) 
}

stopServer <- function() {
  #shutdown rs server
  rsc <- RSconnect(port = 6311)
  RSshutdown(rsc)
}

installPackages <- function() {
  install.packages("Rserve")
  install.packages("RSclient")
  install.packages("xts")
  install.packages("quantmod")
  install.packages("quadprog")
  install.packages("RMySQL")
}


# stopServer()
# installPackages()
startServer()