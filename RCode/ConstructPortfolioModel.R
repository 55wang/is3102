# myAdd=function(x,y){
#   sum=x+y
#   return(sum)
# }

constructPortfolioModel <- function() {
  print("constructPortfolioModel is called")

  if (Sys.info()['user'] == "wang") {
    setwd("/Users/wang")
  } else if (Sys.info()['user'] == "VIN-S") {
    setwd("/Users/VIN-S")
  } else if (Sys.info()['user'] == "qiuxiaqing") {
    setwd("/Users/qiuxiaqing")
  }
  
  library(xts)
  library(quantmod)
  library(quadprog)
  library(RMySQL)
  
  out=tryCatch({

    correlation = matrix(c(1.0,	0.9,	0.8,	0.8,	0.7,	0.7,	0.0,	-0.1,	0.1,	0.5,	-0.2,
                           0.9,	1.0,	0.9,	0.7,	0.7,	0.7,	0.0,	-0.1,	0.2,	0.5,	-0.1,
                           0.8,	0.9,	1.0,	0.6,	0.6,	0.7,	0.1,	-0.1,	0.2,	0.6,	-0.1,
                           0.8,	0.7,	0.6,	1.0,	0.7,	0.6,	0.0,	0.0,	0.1,	 0.6,	-0.1,
                           0.7,	0.7,	0.6,	0.7,	1.0,	0.6,	0.1,	0.0,	0.3,	0.4,	0.0,
                           0.7,	0.7,	0.7,	0.6,	0.6,	1.0,	0.0,	-0.3,	0.0,	0.5,	-0.3,
                           0.0,	0.0,	0.1,	0.0,	0.1,	0.0,	1.0,	0.5,	0.7,	0.3,	0.7,
                           -0.1,	-0.1,	-0.1,	0.0,	0.0,	-0.3,	0.5,	1.0,	0.6,	0.1,	0.7,
                           0.1,	0.2,	0.2,	0.1,	0.3,	0.0,	0.7,	0.6,	 1.0,	0.5,	0.9,
                           0.5,	0.5,	 0.6,	0.6,	0.4,	0.5,	0.3,	0.1,	0.5,	 1.0,	0.2,
                           -0.2,	-0.1,	-0.1,	-0.1,	0.0,	-0.3,	0.7,	0.7,	0.9,	0.2,	1.0), 
                         nrow=11, ncol=11, byrow = TRUE) 
    
    colnames(correlation) <- c("US_STOCKS", "FOREIGN_STOCKS", "EMERGING_MARKETS",
                               "DIVIDEND_STOCKS", "REAL_ESTATE", "NATURAL_RESOURCES",
                               "TIPS", "MUNICIPAL_BONDS", "CORPORATE_BONDS",
                               "EMERGING_MARKET_BONDS", "US_GOVERNMENT_BONDS")
    rownames(correlation) <- c("US_STOCKS", "FOREIGN_STOCKS", "EMERGING_MARKETS",
                               "DIVIDEND_STOCKS", "REAL_ESTATE", "NATURAL_RESOURCES",
                               "TIPS", "MUNICIPAL_BONDS", "CORPORATE_BONDS",
                               "EMERGING_MARKET_BONDS", "US_GOVERNMENT_BONDS")
    
    all_sd <- c(0.16,0.18,0.24,0.14,0.18,0.22,0.5,0.5,0.5,0.7,0.5)
    all_sd
    standarddev1 <- matrix(rep(all_sd,11), ncol=11)
    standarddev1
    standarddev2 <- matrix(rep(all_sd,each=11), ncol=11)
    standarddev2
    
    covariance <- correlation * standarddev1 * standarddev2
    covariance
    
    avg_returns <- matrix(c(0.053, 0.062, 0.081, 0.037, 0.05, 0.062, -0.005, -0.008, -0.002, 0.01, -0.008))
    rownames(avg_returns) <- c("US_STOCKS", "FOREIGN_STOCKS", "EMERGING_MARKETS",
                               "DIVIDEND_STOCKS", "REAL_ESTATE", "NATURAL_RESOURCES",
                               "TIPS", "MUNICIPAL_BONDS", "CORPORATE_BONDS",
                               "EMERGING_MARKET_BONDS", "US_GOVERNMENT_BONDS")
    
    colnames(avg_returns) <- c("CAPM RETURNS")
    avg_returns   
    min_returns<- 0
    min_returns
    max_returns<- max(avg_returns) 
    max_returns
    increments=1000
    tgt_returns<-seq(min_returns,max_returns,length=increments) 
    tgt_sdresult<-rep(0,length=increments)
    tgt_sdresult
    wgt<-matrix(0,nrow=increments,ncol=length(avg_returns)) 
    length(avg_returns)
    library(quadprog)
    for (i in 1:increments){
      Dmat<-2* covariance
      dvec<-c(rep(0,length(avg_returns))) 
      Amat<-cbind(rep(1,length(avg_returns)),avg_returns,
                  diag(1,nrow=11)) 
      bvec<-c(1,tgt_returns[i],rep(0,11)) 
      soln<- solve.QP(Dmat,dvec,Amat,bvec=bvec) 
      tgt_sdresult[i]<-sqrt(soln$value) 
      wgt[i,]<-soln$solution}
    
    head(tgt_sdresult)
    options(scipen=100) 
    head(wgt)
    colnames(wgt)<-c("US_STOCKS", "FOREIGN_STOCKS", "EMERGING_MARKETS",
                     "DIVIDEND_STOCKS", "REAL_ESTATE", "NATURAL_RESOURCES",
                     "TIPS", "MUNICIPAL_BONDS", "CORPORATE_BONDS",
                     "EMERGING_MARKET_BONDS", "US_GOVERNMENT_BONDS")
    wgt[,1]<-ifelse(abs(wgt[,1])<=0.0000001,0,wgt[,1])
    wgt[,2]<-ifelse(abs(wgt[,2])<=0.0000001,0,wgt[,2])
    wgt[,3]<-ifelse(abs(wgt[,3])<=0.0000001,0,wgt[,3])
    wgt[,4]<-ifelse(abs(wgt[,4])<=0.0000001,0,wgt[,4])
    wgt[,5]<-ifelse(abs(wgt[,5])<=0.0000001,0,wgt[,5])
    wgt[,6]<-ifelse(abs(wgt[,6])<=0.0000001,0,wgt[,6])
    wgt[,7]<-ifelse(abs(wgt[,7])<=0.0000001,0,wgt[,7])
    wgt[,8]<-ifelse(abs(wgt[,8])<=0.0000001,0,wgt[,8])
    wgt[,9]<-ifelse(abs(wgt[,9])<=0.0000001,0,wgt[,9])
    wgt[,10]<-ifelse(abs(wgt[,10])<=0.0000001,0,wgt[,10])
    wgt[,11]<-ifelse(abs(wgt[,11])<=0.0000001,0,wgt[,11])
    
    CHECK<-rowSums(wgt)
    CHECK[1000]
    CHECK
    #hardcoded due to total sum weight error
    wgt <- wgt[1:747,]
    tgt_returns <- tgt_returns[1:747]
    tgt_sdresult <- tgt_sdresult[1:747]
    
    
    tgt_port<-data.frame(cbind(tgt_returns,tgt_sdresult,wgt))
    head(tgt_port)
    tail(tgt_port)
    class(tgt_port)
    
    minvar_port<-subset(tgt_port,tgt_port$tgt_sdresult==min(tgt_port$tgt_sdresult))
    minvar_port<-subset(minvar_port, minvar_port$tgt_returns==max(minvar_port$tgt_returns))
    minvar_port$tgt_returns
    tgt_port <- tgt_port[which(tgt_port$tgt_returns>=minvar_port$tgt_returns), ]
    tgt_port <- tgt_port[order(tgt_port$tgt_sdresult),]
    
    rownames(tgt_port) <- 1:nrow(tgt_port)
    tgt_port
    save(tgt_port, file = "tgt_port.rda")
    
    eff_frontier<-subset(tgt_port,tgt_port$tgt_returns>=minvar_port$tgt_returns)
    eff_frontier[c(1:3,nrow(eff_frontier)),]
    
    plot(x=tgt_port$tgt_sdresult,
         y=tgt_port$tgt_returns,
         col="gray40",
         xlab="Portfolio Risk",
         ylab="Portfolio Return",
         main="Mean-Variance Efficient Frontier of Assets
         Based on the Quadratic Programming Approach")
    abline(h=0,lty=1)
    points(x=minvar_port$tgt_sdresult,y=minvar_port$tgt_returns,pch=17,cex=3)
    #points(x=eff_frontier$tgt_sdresult,y=eff.frontier$tgt_returns)
    
    #write.csv(tgt_port, "/Users/wang/result.csv")
 
    con <- dbConnect(MySQL(),
                     user = 'root',
                     password = 'password',
                     host = '127.0.0.1',
                     dbname='retailbankingsystem')
    dbSendQuery(con, 'drop table if exists PORTFOLIOMODEL')
    dbWriteTable(conn = con, name = 'PORTFOLIOMODEL', value = tgt_port)
    
    return(0)
  }, warning = function(war) {
    print("warning")
    print(war)
    return(1)
  }, error = function(err) {
    print("error")
    print(err)
    return(2)
  }, finally = {
    
  })
  return(out)
}


getSdRegression <- function(US_STOCKS, FOREIGN_STOCKS, EMERGING_MARKETS, DIVIDEND_STOCKS,
                          REAL_ESTATE, NATURAL_RESOURCES, TIPS, MUNICIPAL_BONDS, CORPORATE_BONDS,
                          EMERGING_MARKET_BONDS, US_GOVERNMENT_BONDS)
{
  print("runRegression is called")

  if (Sys.info()['user'] == "wang") {
    setwd("/Users/wang")
  } else if (Sys.info()['user'] == "VIN-S") {
    setwd("/Users/VIN-S")
  } else if (Sys.info()['user'] == "qiuxiaqing") {
    setwd("/Users/qiuxiaqing")
  }
  
  load(file = "tgt_port.rda")
#   tgt_sdresult = 0.127966825248657
#   US_STOCKS = 0.231095810691598
#   FOREIGN_STOCKS = 0
#   EMERGING_MARKETS = 0
#   DIVIDEND_STOCKS = 0.573903570391852
#   REAL_ESTATE = 0
#   NATURAL_RESOURCES = 0.0872074558172317
#   TIPS = 0
#   MUNICIPAL_BONDS = 0.0147893050278205
#   CORPORATE_BONDS = 0
#   EMERGING_MARKET_BONDS = 0
#   US_GOVERNMENT_BONDS = 0.0930038580714983
  
  
  newdata = data.frame(US_STOCKS, FOREIGN_STOCKS, EMERGING_MARKETS, DIVIDEND_STOCKS,
                       REAL_ESTATE, NATURAL_RESOURCES, TIPS, MUNICIPAL_BONDS, CORPORATE_BONDS,
                       EMERGING_MARKET_BONDS, US_GOVERNMENT_BONDS)
  print(newdata)
  fit <- lm(tgt_sdresult~US_STOCKS+FOREIGN_STOCKS+EMERGING_MARKETS+DIVIDEND_STOCKS+REAL_ESTATE+NATURAL_RESOURCES+TIPS+MUNICIPAL_BONDS+CORPORATE_BONDS+EMERGING_MARKET_BONDS+US_GOVERNMENT_BONDS, data=tgt_port)
  print(summary(fit))
  result <- predict(fit, newdata)
  print(result)
  return(result)
}

getReturnRegression <- function(US_STOCKS, FOREIGN_STOCKS, EMERGING_MARKETS, DIVIDEND_STOCKS,
                            REAL_ESTATE, NATURAL_RESOURCES, TIPS, MUNICIPAL_BONDS, CORPORATE_BONDS,
                            EMERGING_MARKET_BONDS, US_GOVERNMENT_BONDS)
{
  print("runRegression is called")

  if (Sys.info()['user'] == "wang") {
    setwd("/Users/wang")
  } else if (Sys.info()['user'] == "VIN-S") {
    setwd("/Users/VIN-S")
  } else if (Sys.info()['user'] == "qiuxiaqing") {
    setwd("/Users/qiuxiaqing")
  }
  
  load(file = "tgt_port.rda")
  #   tgt_sdresult = 0.127966825248657
  #   US_STOCKS = 0.231095810691598
  #   FOREIGN_STOCKS = 0
  #   EMERGING_MARKETS = 0
  #   DIVIDEND_STOCKS = 0.573903570391852
  #   REAL_ESTATE = 0
  #   NATURAL_RESOURCES = 0.0872074558172317
  #   TIPS = 0
  #   MUNICIPAL_BONDS = 0.0147893050278205
  #   CORPORATE_BONDS = 0
  #   EMERGING_MARKET_BONDS = 0
  #   US_GOVERNMENT_BONDS = 0.0930038580714983
  
  
  newdata = data.frame(US_STOCKS, FOREIGN_STOCKS, EMERGING_MARKETS, DIVIDEND_STOCKS,
                       REAL_ESTATE, NATURAL_RESOURCES, TIPS, MUNICIPAL_BONDS, CORPORATE_BONDS,
                       EMERGING_MARKET_BONDS, US_GOVERNMENT_BONDS)
  print(newdata)
  fit <- lm(tgt_returns~US_STOCKS+FOREIGN_STOCKS+EMERGING_MARKETS+DIVIDEND_STOCKS+REAL_ESTATE+NATURAL_RESOURCES+TIPS+MUNICIPAL_BONDS+CORPORATE_BONDS+EMERGING_MARKET_BONDS+US_GOVERNMENT_BONDS, data=tgt_port)
  print(summary(fit))
  result <- predict(fit, newdata)
  print(result)
  return(result)
}


# getSdRegression(0.336042761849641, 0.154074988330303, 0.108887881812506, 0.120734303307862, 0.0867255702674858, 0.130036209698718, 0, 0.00819259424706097, 0, 0, 0.0553056904864223)
# getReturnRegression(0.336042761849641, 0.154074988330303, 0.108887881812506, 0.120734303307862, 0.0867255702674858, 0.130036209698718, 0, 0.00819259424706097, 0, 0, 0.0553056904864223)
