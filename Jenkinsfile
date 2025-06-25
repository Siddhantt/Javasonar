pipeline {
    agent any

    tools {
        jdk 'jdk17' // Make sure 'jdk17' is configured in Jenkins → Global Tool Configuration
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/Siddhantt/Javasonar.git'
            }
        }

        stage('Compile Java Files') {
            steps {
                sh 'mkdir -p out && javac -d out $(find . -name "*.java")'
            }
        }

        stage('Run App') {
            steps {
                script {
                    // Run main class (replace with actual if known)
                    sh 'cd out && java Main' 
                }
            }
        }

        stage('Archive Class Files') {
            steps {
                archiveArtifacts artifacts: 'out/**/*.class', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline finished successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check the logs.'
        }
    }
}
