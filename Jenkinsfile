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
                    sh 'docker rm -f test-container || true'

                    sh 'docker run --name test-container reqres-automation || true'
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

                def total = 0
                def passed = 0
                def failed = 0

                try {
                    // Проверяем наличие файлов, чтобы избежать ошибок в консоли
                    def hasFiles = sh(script: 'ls allure-results/*-result.json >/dev/null 2>&1 && echo "yes" || echo "no"', returnStdout: true).trim()

                    if (hasFiles == "yes") {

                        total = sh(script: 'ls allure-results/*-result.json | wc -l', returnStdout: true).trim().toInteger()

                        passed = sh(script: 'grep -l \'"status":"passed"\' allure-results/*-result.json | wc -l', returnStdout: true).trim().toInteger()

                        failed = sh(script: 'grep -lE \'"status":"(failed|broken)"\' allure-results/*-result.json | wc -l', returnStdout: true).trim().toInteger()
                    }
                } catch (Exception e) {
                    echo "Ошибка подсчета: ${e.message}"
                }

                // 4. Отправляем уведомление в Telegram
                withCredentials([
                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                ]) {
                    // Устанавливаем статус для сообщения
                    def status = currentBuild.result ?: 'SUCCESS'
                    if (failed > 0 || status == 'FAILURE') {
                        status = 'FAILURE'
                    }

                    def message = """
Результаты тестов: ${status}
Проект: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
Всего тестов: ${total}
Успешно: ${passed}
Ошибки: ${failed}
Ссылка: ${env.BUILD_URL}
""".trim()

                    sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                        --data-urlencode "chat_id=${CHAT_ID}" \\
                        --data-urlencode "text=${message}"
                    """
                }
            }
        }
    }
}