# Backend Mini Project (Bank Account) #

Through this web service, one can query about the balance, deposit money, and withdraw
money. Just like any Bank, there are restrictions on how many transactions/amounts it can
handle. 


### Requirements ###
This application is built on The Play framework, which requires the following installed. 
	
- Java 1.8
- Scala Build tool (sbt)

##### The application  also requires a MYSQL database #####

create a database with named tala and run this SQL query to create the table.

```
CREATE TABLE `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `type` varchar(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
```


to Fire up the rest server, run this command on the teminal in the project Directory:
 
	sbt "run 8280"

The web service will run on  http://localhost:8280/ 


### API Endpoints ###
---
######GET CURRENT ACCOUNT BALANCE######
Make a GET request to ``/account/balance`` to get the current account balance. This should return data in the format below

```
{
	"success": true,
	"message": "Current account balance fetched successfully.",
	"balance": 3000
}
```  
--
######MAKE A DEPOSIT######
Make a POST request to ``/account/deposit`` 

the Body of the request should have a JSON object with the field ``amount`` required, as shown in the example below.

```
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:8280/account/deposit
```
A sample response to the deposit request is shown below.

```
{
	"success": true,
	"message": "Deposit successful.",
	"balance": 7000
}
``` 
--
######MAKE A WITHDRAWAL######
Make a POST request to ``/account/withdraw`` 

the Body of the request should have a JSON object with the field ``amount`` required, as shown in the example below.

```
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:8280/account/withdraw
```
A sample response to the deposit request is shown below.

```
{
	"success": true,
	"balance": 4000,
	"amount_withdrawn": 3000,
	"message": "Withdrawal successful"
}
```
Below is a sample error response 

```
{
	"success": false,
	"message": "You cannot withdraw more than  $20000 at once"
}
```

---
### TESTING ###
To run  tests. Run the following sbt command in your command line
    
    sbt test
    
 to run code coverage,  use the Jacoco SBT plugin by running this command in your command line
 
 ```
 sbt jacoco:cover
 ```