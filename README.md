# logAnalyser

This Application is used to analyse huge logs and create events in DB by using optimal approach 

# Pre Requisit
  1. Java 8 or higher version
  2. Maven 
  3. Postman 
  4. HSQLDB dependencies
  5. IDE of ur choice .
  
# Algorithm 

  1. we have created a get API (" http://localhost:9071/logfile/analyse/event "), this API can be integrated with any scheduler and can trigger this api call based on the cron defined 
  2. from there we our request reaches the service layer 
    1.  we have utilised ASYNC call by calling COMPLETABLE FUTURES .
    2.  this will be responsable to read all the JSON from the logfile.txt , kept in src/main/recource.
    3.  convert jsonStrings to POJO.
    4.  convert the POJO to the DO to store the DO in database (convertion are made using spans and analysing the timestaps)
    5.  for the event greater them 4ms we will make it as true.
    6.  All this is happening in ASYNC.
    7.  and will return list of all the DOs
  3. Again the list of DOs will be processed in ASYNC
    1. we will divide the list in Batched
    2. For simplicity we have used the Batch SIZE = 2 , this is configurable and kept in application.properties file
    3. all these batched are processed parallelly by using parallel streams.
    4. the batches are stored in DB 
  4. For simplicity post all the operations we have called findALl method of JPA and return it in our response to show the Data has been stored.
