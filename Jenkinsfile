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
                            docker run --rm \
                            -e REQRES_API_KEY=${SECRET_KEY} \
                            -v \$WORKSPACE/allure-results:/app/target/allure-results \
                            reqres-automation
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]

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
                        -d text='${icon} Тест: ${env.JOB_NAME} [%23${env.BUILD_NUMBER}]%0AСтатус: ${status}%0AСсылка: ${env.BUILD_URL}'
                    """
                }
            }
        }
    }
}