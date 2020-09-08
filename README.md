# Job-Searching-Engine
An online github job search and recommendation system 

## Overview

![showcase](https://user-images.githubusercontent.com/61830237/92479286-5a04f080-f1b1-11ea-991e-d3fd6d236b5a.gif)

## Introduction

***

|Positions | Functionality of this web service |
|-------------|----------------------------------------------------------------------------------------------------------|
| Front End 1 |  Developed an interactive web page for users to search and apply positions (HTML, CSS, JAVAScript, AJAX) |
|Front End 2  | Used favorite records to provide personalized position recommendation                                    |
| Back End 1 | Created three Java Servelets with RESTFUL APIs to handle HTTP requests and responses                      |
| Back End 2 | Built MySQL database on Amazon RDS to store position data from Github API |
|Back End 3 | Used MonkeyLearn API to extract keywords from description of positions |
| Back End 4| Use content-based recommendation algorithm to implement job recommendation |
| Back End 5| Deployed to Amazon EC2 for better performance | 


***

## How to start the project 

### Method 1:<br/>

[Amazon Virtual Machine](http://13.58.50.104/jupiter/) <br/>

By default, we use <br/>
username: 1111<br/>
password: 2222<br/>

### Method 2:<br/>

Run on local<br/>
(1) Create an account for MonkeyLearn, get the API Key.<br/>
(2) Create an account for AWS, create a DB following the instruction.<br/>
(3) Set up maven project. Run it in Apache Tomcat.<br/>

#### First Step: Test your MonkeyLearnClient Class (Back End 3)
(1) Go to [MonkeyLearn HomePage](https://monkeylearn.com/). Sign up with your email and enjoy your free trial. <br/>
(2) Click KeyWord Extractor --> API and you can see your MODEL_ID and API_KEY <br/>
(3) Copy and paste your API_KEY and MODEL_ID to the MonkeyLearnClient.java. Replace the variables. <br/>
(4) Test the code with two main functions. 

#### Second Step: Test your MySQL and AWS (Back End 5)
(1) Go to the [AWS HomePage](https://aws.amazon.com/) and create your account. <br/>
(2) Log in. Click `Service` at top left corner, then click `EC2` below the Compute category. See the [documentation of Security group](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_SecurityGroups.html#VPCSecurityGroups) if you are confused. <br/>
(3) Follow the [instruction](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html) and create a MySQL DB instance. Your security group should contain the group you create above. !IMPORTANT, make sure your port is 3306. Also, we prefer you to make your master username as "admin"<br/>
(4) Replace the variables INSTANCE, DB_NAME and PASSWORD. 

##Reference 
(1) To know more about MonkeyLearn API for keyword extraction. Please refer to : [MonkeyLearn HomePage](https://monkeylearn.com/) <br />
(2) To know more about Amazon Virtual Machine and AWS. Please refer to : [AWS HomePage](https://aws.amazon.com/)
