# logAnalyser

This Application is used to analyse huge logs and create events in DB by using optimal approach 

# Pre Requisit
  1. Java 8 or higher version
  2. Maven 
  3. Postman 
  4. HSQLDB dependencies
  5. IDE of ur choice .
  
# Algorithm 

  1. we have created a get API (" http://localhost:9071/logfile/analyse/event "), this API can be integrated with any scheduler and will initiate log processing for time duration defined as a cron. 
  2. from there our request will reach the service layer 
    1.  we have utilised ASYNC call by calling COMPLETABLE FUTURES .
    2.  this will be responsable to read all the JSON from the logfile.txt , kept in src/main/recource.
    3.  convert jsonStrings to POJO.
    4.  convert the POJO to the DO to store the DO in database (convertion are made using spans and analysing the timestapms)
    5.  for the event greater them 4ms we will make it as true.
    6.  All this is happening in ASYNC.
    7.  and will return list of all the DOs
  3. Again the list of DOs will be processed in ASYNC
    1. we will divide the list in Batches
    2. For simplicity we have used the Batch SIZE = 2 , this is configurable and kept in application.properties file
    3. all these batched are processed parallelly by using parallel streams.
    4. the batches are stored in DB 
  4. For simplicity post all the operations we have called findALl method of JPA and return it in our response to show the Data has been stored.

# How To Run
  1. Clone the project :
    use command : git clone https://github.com/samarthmahajan/logAnalyser.git
  2. one Cloned use clean install command to create a maven build using .
    use comman : mvn clean install .
  3. import the project in and IDE of ur choice, i used STS.
  4. run the project as a Spring boot project .
  5. once the server is stated import the following command in postman :
    command : curl --location --request GET 'http://localhost:9071/logfile/analyse/event'
    
    or run it http://localhost:9071/logfile/analyse/event in your browser.
   
