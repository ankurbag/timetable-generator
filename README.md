# Project: Time Table Generator 
Academic project to implement the Genetic Algorithm. We are aiming to create a rest project for generating Timetable(Class Schedule).

## URL to test the REST API
http://timetable-api.us-east-1.elasticbeanstalk.com/swagger-ui.html
![image](https://user-images.githubusercontent.com/3647390/33792551-49542ace-dc71-11e7-9c02-346d9e3a3593.png)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

We need following things for set up-

#### IDE & project configuration
```
Eclipse or STS
```
```
Java-8
```
```
Spring MVC
```

### Installing and running on the Local

We have 1 project in the repository
#### API project - Spring MVC web framework

1) In the command line
```
git clone https://github.com/ankurbag/INFO6205_06.git
```
2) Inside Eclipse/STS Import the project
```
File -> Import -> Maven -> Existing Maven project
```
3) Set-up server(Tomcat Apache 8.5) https://tomcat.apache.org/download-80.cgi

4) Add project to the server

5) Go to the chrome browser
```
http://localhost:8080/api/swagger-ui.html
```

## Running the tests

We used JUnit, which is a Java library to help you perform unit testing. Unit testing is the process of examining a small "unit" of software (usually a single class) to verify that it meets its expectations or specification.

1) In the pom.xml
```
<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.7</version>
			<scope>test</scope>
		</dependency>
```
2) Inside Eclipse/STS Right click on the the project and do the following
```
<Project_Name_Folder> -> Run as -> JUnit Test
```
3) If JUnit Test not configured, then run individually on the ``GeneticTest`` and ``TimetableTest``

4) You should see the following result -
![image](https://user-images.githubusercontent.com/3647390/33802135-3074d88c-dd3e-11e7-8e0f-185d7f7433f8.png)

## Report 
Our report is present in the repository under the report folder.

## Authors
* Ishista B.
* Elton B.
* Ankur B.

## License
* [MIT License](http://www.opensource.org/licenses/mit-license.php)
 
