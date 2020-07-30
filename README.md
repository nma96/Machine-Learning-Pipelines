# Machine Learning Pipelines

[![JDK](https://img.shields.io/badge/Java%20JDK-1.8-green)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html#license-lightbox)
[![Clickhouse](https://img.shields.io/badge/Clickhouse-DB-red)](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-clickhouse-on-ubuntu-18-04)
[![SPARK](https://img.shields.io/badge/SPARK-2.4.6-green)](https://www.apache.org/dyn/closer.lua/spark/spark-2.4.6/spark-2.4.6-bin-hadoop2.7.tgz)
[![Scala](https://img.shields.io/badge/Scala-2.11.12-brightgreen)](https://www.scala-lang.org/download/2.11.12.html)
![GitHub last commit](https://img.shields.io/github/last-commit/nma96/Machine-Learning-Pipelines)

## Introduction to each package
### Database to Clickhouse Pipeline (DBtoCH)
This pipeline reads data from 2 tables in a database on the host, combines the tables (inner join), adds 3 new columns to the joined table and writes onto a remote clickhouse database. 

### Flight Data Read Pipeline
This pipeline reads flight data from a csv on host machine and writes it onto a remote clickhouse database

### Purchase Prediction
This class is to predict the purchase variable in the csv using Logistic Regression and Random Forests Algorithms. Accuracy achieved: LR-79% RF-90%.

### Score Prediction
This class is to predict the result variable in the csv using Logistic Regression. Accuracy achieved: LR 79% 
 
### Data Cleansing Pipeling
This class uses the Titanic Dataset from Kaggle, performs some data cleansing and returns a clean dataframe for further analysis/prediction 
 
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
