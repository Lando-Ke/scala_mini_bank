# Backend Mini Project (Bank Account) #

Through this web service, one can query about the balance, deposit money, and withdraw
money. Just like any Bank, there are restrictions on how many transactions/amounts it can
handle. 


### Requirements ###
This application is built on The Play framework, which requires the following installed. 
	
- Java 1.8
- Scala Build tool (sbt)

##### The application  also requires a MYSQL database #####

create a database with named tala_test and run this SQL query to create the tables and insert the data.

```
CREATE TABLE `accounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_number` bigint(20) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
```
CREATE TABLE `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_number` bigint(20) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `type` varchar(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
```
INSERT INTO `accounts` (`id`, `account_number`, `balance`)
VALUES
	(1,515276006,500);

```


to Fire up the rest server, run this command on the teminal in the project Directory:
 
	sbt run

The web service will run on  http://localhost:9000/ 


### API Endpoints ###
---
######GET CURRENT ACCOUNT BALANCE######
Make a GET request to ``/account/51527606/balance`` to get the current account balance. This should return data in the format below

```
 {
    "success": true,
    "message": "Account balance retrieved successfully.",
    "balance": 24870
 }
```  
--
######MAKE A DEPOSIT######
Make a POST request to ``/account/51527606/deposit`` 

the Body of the request should have a JSON object with the field ``amount`` required, as shown in the example below.

```
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:9000/account/51527606/deposit
```
A sample response to the deposit request is shown below.

```
{
    "success": true,
    "message": "Deposit successful.",
    "transaction_type": "deposit",
    "amount": 9002,
    "balance": 15070
}
``` 
--
######MAKE A WITHDRAWAL######
Make a POST request to ``/account/51527606/withdraw`` 

the Body of the request should have a JSON object with the field ``amount`` required, as shown in the example below.

```
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:9000/account/51527606/withdraw
```
A sample response to the withdrawal request is shown below.

```
{
    "success": true,
    "message": "Withdrawal successful",
    "transaction_type": "withdrawal",
    "amount": 9002,
    "balance": 6068
}
```
Below is a sample error response 

```
{
	"success": false,
	"message": "You cannot withdraw more than  KES20000 at once"
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