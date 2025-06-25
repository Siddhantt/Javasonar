pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'MySonarQube'           // SonarQube name in Jenkins → Manage Jenkins → Configure
        DOCKER_HUB_CREDENTIALS = 'dockerhub-creds' // Jenkins credentials ID for Docker Hub
        DOCKER_IMAGE = 'siddhantt/javasonar'       // Your Docker Hub repo
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/Siddhantt/Javasonar.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE
                    '''
                }
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed.'
        }
        success {
            echo 'Pipeline completed successfully.'
        }
    }
}
