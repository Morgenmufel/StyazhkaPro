# Используем Java 21 с Maven
FROM eclipse-temurin:21-jdk AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY . .

# Собираем приложение с помощью Maven
RUN ./mvnw clean package -DskipTests

# Переходим на более лёгкий рантайм-образ
FROM eclipse-temurin:21-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем порт (если нужно изменить — меняй здесь и в Render)
EXPOSE 8080

# Запускаем Spring Boot приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
