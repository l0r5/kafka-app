# Kafka Play App
Demo Spring Boot Kafka App 

## Prerequisites
- Install [Docker](https://www.docker.com/)
- Install [Maven](https://maven.apache.org/) (does not need to be installed explicitly when using intelliJ)
- Additional host entries need to be added in /etc/hosts:

    ```$ sudo nano /etc/hosts```
    
    Add following entries to the list:
    
    ```
       127.0.0.1       kafka-1
       127.0.0.1       kafka-2
       127.0.0.1       kafka-3
  ```

## Setup
- Start the Kafka cluster using docker-compose:
    
    ```$ cd kafka-cluster```
    
    ```$ docker-compose up```
- Build the app with Maven: 
    ```$ mvn clean install```
- Run the KafkaApp Spring Boot App

## Additional Info
- Kafdrop was added to monitor the Kafka cluster. You can access it via http://localhost:9000/ as soon as the docker containers are up & running
- Messages can be sent to the Kafka cluster using the KafkaController which can be accessed via REST Api. To send a message you can use curl:

    ```$ curl -X POST -F 'message=test123' http://localhost:9009/kafka/publish```