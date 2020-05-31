# Tutorial - Adding an automated repair tool to your Jenkins Pipeline

In this tutorial you will add an automated repair tool to your jenkins pipeline. This tool will generate a PR to any of your git repository if it finds solutions to possible bugs reported by static analysis tools.

## What you'll learn?

- You will learn some basic Docker scripting.
- You will learn how to setup your own basic Jenkins.
- You will learn how to add Static Analysis Tool to your Jenkins pipeline. SonarQube in this case.
- You will learn how you can use some basic bash scripts to run things in your jenkins pipeline.
- You will learn how to skip the scripting by not re-inventing the wheel and just add a plugin someone already developed for you to use.

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

If you have issues reaching the application it is either still being started or you might be using a quite old computer or setup with docker.
In my case hitting the wall when trying it out on an old mac(read: "very old"). I had to visit http://192.168.99.100:9000/ since the legacy **Docker Toolbox** runs with VirtualBox which used this ip-adress.

### Start up Jenkins

At first I tried creating a quick guide for starting up Jenkins with docker here myself. However, after struggeling with this, I realized it would just be as good giving a link to their own description so you can adapt the method based on which OS you are using.

[Installing Jenkins](https://www.jenkins.io/doc/book/installing/) - Follow the instructions on this link.

After successfully followed the instructions provided by documentation we should see three containers up and running using the `docker ps` command. Looking something like this:

![Three containers up and running](Containers_Running.png)

It might take a while until the jenkins server is up and running. You should however be able to visit http://localhost:8080/ and see your jenkins application when it is ready.

Jenkins now tells us to find our password in the file `/var/jenkins_home/secrets/initialAdminPassword` to unlock it. Either we could run a bash command towards the Jenkins container using the `exec` keyword or we could check the logs of the jenkins container:

```
docker exec jenkins-blueocean cat var/jenkins_home/secrets/initialAdminPassword
```

```
docker logs jenkins
```

1. Use the password to unlock the Jenkins application
2. follow the installation guide, installing the suggested plugins
3. Create a new admin account, e.g. using `user: admin`, `pwd: admin` and some dummy email.

Congratulations! You now got the basic parts of our CI pipeline needed for completing the good stuff of this tutorial!

### Configure Jenkins

We'll now configure jenkins so that we can create a build which runs a sonarqube scan on our files.

#### Install SonarQube scanner plugin

1. Click `Manage Jenkins`->`Manage Plugins`->`Available`
2. Search for `Sonarqube scanner`
3. Tick the box for the tool and then click the `Install without restarting`
4. On the next screen tick the box for restarting the Jenkins when the installation is done and no jobs are running.

#### Configure the SonarQube scanner 

1. When the system has rebooted go back to `Manage Jenkins` again
2. This time click the `Configure (System)`
3. Scroll down to the SonarQube Servers section and click the `Add SonarQube`
4. Here you can specify your SonarQube server adress, name and auth-token, in our case running on docker the URL should be `http://sonarqube:9000/`
5. Generate token for Sonarqube
  * [Go to your SonarQube instance](http://localhost:9000/) - localhost:9000
  * Now login to the SonarQube if you haven't using `username: admin` and `pwd: admin`
  * Click on the user icon at the top right corner and click `my account`
  * Click `Security` -> fill in the Token name: `My-SonarQube-Tutorial-Token` -> `copy` the generated token.
6. Add the token to our Jenkins
  * Back in Jenkins click the Add-button next to the "Server Authentication Token"
  * Set `Kind` to `secret text` and add the paste the copied token into the `secret` input field. 
  * Add any name and description of your liking.

When ever we make any configuration changes we should make a habit out of going to `/restart` to make sure all our changes have been made to the jenkins server.

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