# Tutorial - Adding an automated repair tool to your Jenkins Pipeline

In this tutorial you will add an automated repair tool to your jenkins pipeline. This tool will generate a PR to any of your git repository if it finds solutions to possible bugs reported by static analysis tools.

## What you'll learn?

- You will learn some basic Docker scripting
- You will learn how to setup your own basic Jenkins
- You will learn how to add your own Static Analysis Tool to your Jenkins pipeline. SonarQube in this case.
- You will learn some basic Jenkins scripting
- You will learn how to skip the scripting by not inventing the wheel and just add a plugin some one already developed for you to use.

## Prerequisites

- Docker

To minimize the complexity of having many prerequisites, we'll be using Docker in this tutorial to setup our basic pipeline. Docker gives us the possibility to boot-up and run all our components of our pipeline in containers without having to actually install them on our local machines.

Here you can find and install a docker version suiting you [Get Docker](https://docs.docker.com/get-docker/)

## Setup our pipeline

Assuming you've installed Docker let's move on to getting our jenkins with sonarqube up and running.

### Tidy up our Docker environment

If you've got a docker environment with some containers already running you should either stop your current containers or make sure that the ports are not colliding during this tutorial.

```shell
docker ps       // List our containers
docker stop <my_container>      // Stop any container using their name
```

### Start up SonarQube

```shell
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts
```

The 'docker run' command starts up an application in a container. The -d flag stands for detach which makes the container run in the background. At the end we define which Image we want to use, in this case "sonarqube:lts". If there is no image present locally matching this name, docker will try and pull it from dockerhub.

Let's check that our container is up and running now
```shell
docker ps
```

You should be able to go to http://localhost:9000/ and see your sonarqube instance.

In my case running on an old mac(read: "very old") I had to visit http://192.168.99.100:9000/ since the legacy Docker Toolbox runs with Virtual Machine which uses this ip-adress. If you have similar issues not finding it on http://localhost:9000/ this might be the issue, or you just have to wait a bit longer while the application is booting up on docker.

### Start Jenkins

1. Create a network for our jenkins application
```shell
docker network create jenkins
```

2. Setup a storage for our jenkins, called volumes in docker
```shell
docker volume create jenkins-docker-certs
docker volume create jenkins-data
```

3. To be able to run docker commands inside of our jenkins we need to start a docker container with the  image called docker:dind
```shell
docker container run --name jenkins-docker -d \
  --privileged --network jenkins --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  -p 2376:2376 docker:dind
```

There are a couple of new commands:

* --privileged   -  is required for running the docker in docker image.
* --env  -  Enables the use of TLS on the docker server
* --network, --network-alias  -  connects to the previously created network and exposes the container on the network with the name 'docker'
* --volume   -  maps the /certs/client and the /var/jenkins_home to the volumes created earlier.

4. Download and run Jenkinsci

```shell
docker container run --name jenkins -d \
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  -p 8080:8080 -p 50000:50000 jenkinsci/blueocean
```

docker container run --name jenkins -d \
  --network jenkins -p 8080:8080 -p 50000:50000 jenkinsci/blueocean


* --env  -  In this case sets up our jenkins to connect to our previously created docker environment

Now we should see our three containers up and running using the `docker ps`.

It might take a while until the jenkins server is up and running. You could go grab a coffee or something in the meantime. After a while you should be able to go to http://localhost:8080/ and see your jenkins application running.

### Configure Jenkins

### Unlocking Jenkins and create admin user

Jenkins now tells us to find our password in the folder `/var/jenkins_home/secrets/initialAdminPassword` to unlock it. Either we could connect Jenkins container and navigate to the folder and use `cat` print the we need, or we could check the logs of the jenkins container using:

```
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

```
docker logs jenkins
```

You should now see the line `Please use the following password to proceed to installation:` followed by the password we need to unlock Jenkins.

Moving on you can choose to use the default plugins for Jenkins which gives us some good basic plugins. When that is done you can create your new admin user. For this tutorial you can just use: `user: admin`, `pwd: admin` and some dummy email.




Congratulations you now have the necessary basic parts of a CI pipeline needed for doing the 'real part' of this tutorial!


## Cleaning up after us

Stop our container:
```
docker container stop <container_name>
```

If you just want to clean up everything:
```
docker system prune --volumes
```

If you would like to be a bit more granular:
```
docker container prune  -- removes all stopped containers.
docker image prune -a  -- removes all images that are not used by any existing container.
docker volumes prune  -- removes all volumes not used by at least one container.
docker network prune  -- removes all networks not used by at least one container.
```


### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds


## Links to relevant tools and research in the field

* [Repairnator](http://www.github.com/eclipse/repairnator) - "Software development bot that automatically repairs build failures on continuous integration."

* [SpoonLabs](http://www.github.com/spoonlabs/) - "The laboratory for Java source code analysis and transformation using Spoon"