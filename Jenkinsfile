pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/SharifzodaIqbol/reqres-api-automation.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t reqres-automation .'
            }
        }

        stage('Run Tests in Docker') {
            steps {
                script {
                    sh 'rm -rf allure-results && mkdir allure-results'

                    withCredentials([string(credentialsId: 'REQRES_API_TOKEN', variable: 'SECRET_KEY')]) {
                        sh """
                            docker run --name test-container \
                            -e REQRES_API_KEY=${SECRET_KEY} \
                            reqres-automation
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                sh 'docker cp test-container:/app/target/allure-results/. ./allure-results/ || true'

                sh 'docker rm -f test-container || true'

                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }

            withCredentials([
                string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
            ]) {
                script {
                    def status = currentBuild.result ?: 'SUCCESS'
                    def icon = (status == 'SUCCESS') ? '✅ Автотесты успешно завершились!' : '❌ Автотесты не прошли!'

                    sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                        -d chat_id=${CHAT_ID} \\
                        -d text='${icon} Тест: ${env.JOB_NAME} [#${env.BUILD_NUMBER}]%0AСтатус: ${status}%0AСсылка: ${env.BUILD_URL}'
                    """
                }
            }
        }
    }
}