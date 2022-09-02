call mvnw clean package -DskipTests
copy /y C:\dev\abn\recipeapi\target\recipeapi-0.0.1-SNAPSHOT.jar C:\dev\abn\recipeapi\src\main\docker\
cd src\main\docker          
docker-compose up -d --build --force-recreate app
docker-compose up
