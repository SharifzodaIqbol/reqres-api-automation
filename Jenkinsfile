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

                    // Запуск контейнера без переменных REQRES_API_TOKEN
                    sh 'docker run --name test-container reqres-automation'
                }
            }
        }
    }

    post {
        always {
            script {
                // Копируем результаты из контейнера и удаляем его
                sh 'docker cp test-container:/app/target/allure-results/. ./allure-results/ || true'
                sh 'docker rm -f test-container || true'

                // Формирование Allure отчета в Jenkins
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]

                // Подсчет результатов тестов из JSON файлов Allure
                def total = 0
                def passed = 0
                def failed = 0

                try {
                    def files = findFiles(glob: 'allure-results/*-result.json')
                    for (file in files) {
                        def content = readJSON file: file.path
                        total++
                        if (content.status == 'passed') {
                            passed++
                        } else if (content.status == 'failed' || content.status == 'broken') {
                            failed++
                        }
                    }
                } catch (Exception e) {
                    echo "Ошибка при подсчете результатов: ${e.message}"
                }

                withCredentials([
                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                ]) {
                    def status = currentBuild.result ?: 'SUCCESS'

                    def message = """
Результаты тестов: ${status}
Проект: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
Всего тестов: ${total}
Успешно: ${passed}
Ошибки: ${failed}
Ссылка на билд: ${env.BUILD_URL}
""".trim()

                    sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                        -d chat_id=${CHAT_ID} \\
                        -d text='${message}'
                    """
                }
            }
        }
    }
}