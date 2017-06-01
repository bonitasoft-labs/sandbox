#testRNN <- read.csv("/home/bonita74/project/ProcessSequencePrediction/data/helpdesk.csv", sep = ",")
testRNN <- read.csv("/home/bonita74/project/bonitaDataPSP/data/tahiti-data.csv", sep = ",")
attach(testRNN)
#completeTime <- strptime(CompleteTimestamp, format="%Y-%m-%d %H:%M:%S")
#duree <- as.numeric(completeTime) * 86400000
#testRNN <- cbind(testRNN,duree)

duration <- list()
CurrentCaseID <- 0

for(i in 1:length(unique(ActivityID))){
  duration[unique(ActivityID)[i]] <- c(1)
}

for(i in 1:dim(testRNN)[1]){ 
  
  print("~~~")
  print(c("Pointer case id", CurrentCaseID))
  print(c("Current case id", testRNN$CaseID[i]))
  
  if(CurrentCaseID == testRNN$CaseID[i]){
    print("Equals -> then ACT")
    print(c("current i", i, "activity in i", ActivityID[i]))
    duration[[ ActivityID[i] ]] <- c(duration[[ ActivityID[i] ]], as.numeric(difftime(testRNN$CompleteTimestamp[i],testRNN$CompleteTimestamp[i-1],units = "secs")))
    # append(duration[ActivityID[i]], as.numeric(difftime(testRNN$CompleteTimestamp[i],testRNN$CompleteTimestamp[i-1],units = "secs")))
  }
  CurrentCaseID <- testRNN$CaseID[i]
}

