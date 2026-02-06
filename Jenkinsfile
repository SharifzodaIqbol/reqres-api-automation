pipeline {
    agent any

    environment {
        TELEGRAM_TOKEN = '8357821954:AAHFTF1kaH_BgwCXEFLIPEd5U2wx8PkLS0I'
        TELEGRAM_CHAT_ID = '5471786276'
        REQRES_API_KEY = 'reqres_99c69c7accc44aef8194e2cfa132bf4f'
    }

    stages {
        stage('Checkout') {
            steps {
                // Явно клонируем репозиторий
                git branch: 'main', url: 'https://github.com/SharifzodaIqbol/reqres-api-automation.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Проверяем, что Docker установлен
                sh 'docker --version'

                // Сборка Docker образа
                sh 'docker build -t reqres-automation .'
            }
        }

        stage('Run Tests in Docker') {
            steps {
                script {
                    // Запускаем контейнер с переменными окружения
                    sh """
                        docker run --name test-container \
                        -e REQRES_API_KEY=${env.REQRES_API_KEY} \
                        reqres-automation
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                // Копируем результаты Allure из контейнера (если контейнер существует)
                sh """
                    if [ \$(docker ps -a -q -f name=test-container) ]; then
                        docker cp test-container:/app/target/allure-results ./allure-results || echo "Allure results not found"
                        docker rm -f test-container
                    fi
                """

                // Определяем статус сборки
                def status = currentBuild.result ?: 'SUCCESS'
                def icon = (status == 'SUCCESS') ? '✅ Автотесты успешно завершились!' : '❌ Автотесты не прошли!'

                // Отправляем уведомление в Telegram
                sh """
                    curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_TOKEN}/sendMessage \
                    -d chat_id=${env.TELEGRAM_CHAT_ID} \
                    -d text='${icon} Тест: ${env.JOB_NAME} [%23${env.BUILD_NUMBER}]%0AСтатус: ${status}%0AСсылка: ${env.BUILD_URL}'
                """
            }
        }
    }
}
