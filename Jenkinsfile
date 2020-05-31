pipeline {
    agent {
        docker {
            image 'alpine/git:latest'
        }
    }
    stages {
        stage('Repair') {
            steps{
                sh "git clone https://github.com/SpoonLabs/sonarqube-repair.git"
                sh "cd sonarqube-repair"
                sh "mvn package -DskipTests"
                sh "ls -al"
                sh "ls ../ -al"
            }
        }
    }
}