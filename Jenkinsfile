pipeline {
    agent any
    tools{
        maven 'maven'
    }
    stages {
        stage('Build jar file') {
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
        stage('Build docker image'){
            steps {
                sh 'docker build -t josemiguelchvz/catapp .'
            }
        }
        stage('Push docker image'){
            steps {
                script{
                    withCredentials([string(credentialsId: 'docker-hub-password', variable: 'dckhubpwd')]) {
                        sh 'echo $dckhubpwd'
                        sh 'docker login -u josemiguelchvz -p ${dckhubpwd}'
                    }
                    sh 'docker push josemiguelchvz/catapp'
                }
            }
        }
    }
    post {
		always {
			sh 'docker logout'
		}
	}
}