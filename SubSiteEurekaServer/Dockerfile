# =======================================================
# 1. 빌드(Build) 스테이지
# =======================================================
FROM gradle:8.5-jdk17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /home/gradle/src

# 빌드에 필요한 최소한의 파일만 먼저 복사
COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle gradle ./gradle

# 의존성만 명시적으로 다운로드
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY --chown=gradle:gradle src ./src

# 소스 코드를 포함하여 실제 빌드 실행
RUN gradle build --no-daemon -x test


# 2. 패키징(Packaging) 스테이지

FROM eclipse-temurin:17-jre-jammy

# 작업 디렉토리 설정
WORKDIR /app

# 보안 강화를 위해 non-root 사용자 생성
RUN addgroup --system appuser && adduser --system --ingroup appuser appuser

# 빌드 스테이지에서 생성된 .jar 파일을 복사하고, 소유자를 appuser로 지정
COPY --from=builder --chown=appuser:appuser /home/gradle/src/build/libs/*.jar app.jar

# 생성한 non-root 사용자로 전환
USER appuser


# 포트 명시
EXPOSE 8761

# 최종 실행 명령어
ENTRYPOINT ["java","-jar","app.jar"]
