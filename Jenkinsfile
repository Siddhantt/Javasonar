pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'MySonarQube'
        DOCKER_HUB_CREDENTIALS = 'dockerhub-creds'
        DOCKER_IMAGE = 'siddhant271299/javasonar'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Siddhantt/Javasonar.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Code Quality - SonarQube') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh 'sonar-scanner'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:latest .'
            }
        }

        stage('Security Scan - Trivy') {
            steps {
                sh 'trivy image --exit-code 0 --severity LOW,MEDIUM,HIGH $DOCKER_IMAGE:latest'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_HUB_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE:latest
                    '''
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                script {
                    sh '''
                      kubectl apply -f k8s/deployment.yaml
                      kubectl apply -f k8s/service.yaml
                      kubectl apply -f k8s/ingress.yaml
                      kubectl rollout status deployment/javasonar-deployment
                    '''
                }
            }
        }
    }

    post {
        failure {
            echo '❌ Pipeline failed. Please check logs.'
        }
        success {
            echo '✅ Pipeline completed successfully.'
        }
    }
}
