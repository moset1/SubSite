# Build 단계
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle gradle ./gradle

# gradle의 daemon을 사용하지 않고 test는 건너뛰기
# 의존성을 우선 다운로드해서 레어어에 저장
RUN gradle build --no-daemon -x test

# 앞선 레이어에 소스코드를 합쳐서 jar 파일 생성
COPY --chown=gradle:gradle src ./src
RUN gradle build --no-daemon -x test

# Package 단계
FROM openjdk:17-jre-slim
WORKDIR /app
# build 단계에서 생성된 jar 파일만 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","app.jar"]
