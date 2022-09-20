pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Build JAR File') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/josemiguel-chvz/spring-api-example']]])
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t josemiguelchvz/catapp .'
            }
        }
        stage('Push docker image') {
            steps {
                withCredentials([string(credentialsId: 'dckrhubpassword', variable: 'dckpass')]) {
                    sh 'docker login -u josemiguelchvz -p ${dckpass}'
                }
                sh 'docker push josemiguelchvz/catapp'
            }
        }
    }
    post {
        always {
            sh 'docker logout'
        }
    }
}