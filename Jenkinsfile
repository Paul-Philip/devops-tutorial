pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Sonarscan') {
            steps{
                withSonarQubeEnv('SonarQube') {
                    sh "mvn clean package sonar:sonar"
                }
            }
        }
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