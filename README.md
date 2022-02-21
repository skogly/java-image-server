## **Java Image Server with WebSocket**

## Introduction

Java application as an alternative to ASP.NET Core to serve as a backend to the [Ionic Gallery app](https://github.com/skogly/ionic-gallery). This is a simple project which can serve an image folder and automatically create smaller image sizes for use in other applications. It can receive (upload) and delete images on disk, and it features real-time notifications when the images are changed on the server side.

## Prerequisites

This project is written in [Java 17](https://www.oracle.com/java/technologies/downloads/#JDK17) and runs on most modern operating systems. It can be hosted directly in an IDE, such as [IntelliJ](https://www.jetbrains.com/idea/), or via [Docker](https://docs.docker.com/get-started/). I like to run most of my applications through Docker together with [Docker-Compose](https://docs.docker.com/compose/install/).

## Installation and usage

You will need [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) to build the project. Then use an IDE or docker to run the application. The fields you need to set are the image folder along with where to store the resized images.
- application.properties => specify imageFolder, mobileFolder and thumbnailFolder.
    -   The image service will find the images located in the ImageFolder directory
    -   It will then create resized images in the mobileFolder and thumbnailFolder specified, following the same folder structure as the original folder
    -   These three folders will be available on their own endpoints

After changing this file, you should now be able to run the application, assuming that you have no problems with file permissions and/or the network firewall. The image service will start before the actual web application and go through the images and create resized versions. It will take some time to start up the first time you run the application depending on your CPU and the amount of images.

If you are running on Mac/Linux and have Docker and Docker-Compose installed, you need to run `./mvnw package` from the project directory and change the docker-compose.yml file to get the app up and running.
- docker-compose.yml -> specify image, mobile and thumbnail directories for volume bindings

Build the docker image: `docker build -t imageserver-java .` and run with docker-compose: `docker-compose up -d`

The web application has 6 endpoints.
- [HttpGet]  images => returns a list of all the image paths found by the image server
- [HttpPost] thumbnail => returns an image with maximum 200 pixels in either direction. Input: one image path from the list
- [HttpPost] mobile => returns an image with maximum 1000 pixels in either direction. Input: one image path from the list
- [HttpPost] image => returns the true image in full resolution. Input: one image path from the list
- [HttpPost] upload => expects to receive an "ImageRequest" which includes a file path and a form (IFormFile)
- [HttpPost] delete => expects the path of one of the images from the list

This project also utilises [WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket/) to notify connected clients about changes when uploading or deleting images.