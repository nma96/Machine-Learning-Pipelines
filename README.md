# Machine Learning Pipelines

## Introduction to each package
### Database to Clickhouse Pipeline (DBtoCH)
This pipeline reads data from 2 tables in a database on the host, combines the tables (inner join), adds 3 new columns to the joined table and writes onto a remote clickhouse database. 

### Flight Data Read Pipeline
This pipeline reads flight data from a csv on host machine and writes it onto a remote clickhouse database

### Purchase Prediction
This class is to predict the purchase variable in the csv using Logistic Regression and Random Forests Algorithms. Accuracy achieved: LR-79% RF-90%.

### Score Prediction
This class is to predict the result variable in the csv using Logistic Regression. Accuracy achieved: LR 79% 
 
## Instructions to run pipelines (DBtoCH & FlightDataRead):
1. Clone the entire repository and open in IntelliJ IDEA
2. Create BuildConfigurations for EACH object (Edit Configurations > + > Application type >  Choose the main class to be the respective .scala files)
3. Before building the project, make sure clickhouse is setup on guest machine (in my case, Ubuntu VM) and you are able to ssh into the VM (Port Forwarding might need to be enabled)
4. Make sure `ckProperties` has the right username and password to clickhouse 
5. Make sure the `.jdbc(url = " ")` reflects the right URL with the right Database name. 
6. To build and run each object, choose the respective build and hit run (while the VM is running)

### Instructions to run Predictors (PurchasePrediction & Score Prediction): 
 1. Clone the entire repository and open in IntelliJ IDEA
 2. Create BuildConfigurations for EACH object (Edit Configurations > + > Application type >  Choose the main class to be the respective .scala files)
 3. To build and run each object, choose the respective build and hit run
