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
                    // Запуск тестов; || true гарантирует переход к блоку post при падении тестов
                    sh 'docker run --name test-container reqres-automation || true'
                }
            }
        }
    }

    post {
        always {
            script {
                // Копируем результаты и удаляем контейнер
                sh 'docker cp test-container:/app/target/allure-results/. ./allure-results/ || true'
                sh 'docker rm -f test-container || true'

                // Генерация Allure
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]

                def total = 0
                def passed = 0
                def failed = 0

                try {
                    // Используем стандартный путь Jenkins workspace
                    def resultsPath = "${env.WORKSPACE}/allure-results"
                    def resultsDir = new File(resultsPath)

                    if (resultsDir.exists()) {
                        resultsDir.eachFileMatch(~/.*-result\.json/) { file ->
                            def text = file.getText("UTF-8")
                            total++
                            if (text.contains('"status":"passed"')) {
                                passed++
                            } else if (text.contains('"status":"failed"') || text.contains('"status":"broken"')) {
                                failed++
                            }
                        }
                    }
                } catch (Exception e) {
                    echo "Ошибка анализа файлов: " + e.getMessage()
                }

                withCredentials([
                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                ]) {
                    def status = currentBuild.result ?: 'SUCCESS'
                    if (failed > 0) { status = 'FAILURE' }

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