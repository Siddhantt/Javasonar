pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'MySonarQube'
        DOCKER_HUB_CREDENTIALS = 'dockerhub-creds'
        DOCKER_IMAGE = 'siddhantt271299/javasonar'
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

        stage('Debug Docker Credentials') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo "🛠️ DEBUG - Docker Username: \$DOCKER_USER"
                        echo "🛠️ DEBUG - Docker Password Length: \${#DOCKER_PASS}"
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE:latest
                    '''
                }
            }
        }
    }

    post {
        failure {
            echo '❌ Pipeline failed.'
        }
        success {
            echo '✅ Pipeline completed successfully.'
        }
    }
}
