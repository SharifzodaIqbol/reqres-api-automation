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
                sh """
                    if [ \$(docker ps -a -q -f name=test-container) ]; then
                        docker cp test-container:/app/target/allure-results ./allure-results || echo "Results not found"
                        docker rm -f test-container
                    fi
                """

                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]

                withCredentials([
                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                ]) {
                    def status = currentBuild.result ?: 'SUCCESS'
                    def icon = (status == 'SUCCESS') ? '✅ Автотесты успешно завершились!' : '❌ Автотесты не прошли!'

                    sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                        -d chat_id=${CHAT_ID} \\
                        -d text='${icon} Тест: ${env.JOB_NAME} [%23${env.BUILD_NUMBER}]%0AСтатус: ${status}%0AСсылка: ${env.BUILD_URL}'
                    """
                }
            }
        }
    }
}