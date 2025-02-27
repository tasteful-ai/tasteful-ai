#FROM --platform=linux/amd64 openjdk:21-jdk
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]


# OpenJDK 21 slim 기반 이미지 사용
FROM openjdk:21-jdk-slim

# 이미지에 레이블 추가
LABEL type="application"

# /apps 디렉토리를 명확히 생성
RUN mkdir -p /apps

# 작업 디렉토리 설정
WORKDIR /apps

# 애플리케이션 JAR 파일을 컨테이너로 복사
COPY build/libs/tasteful-ai-0.0.1-SNAPSHOT.jar /apps/app.jar

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# 애플리케이션을 실행하기 위한 엔트리포인트 정의
ENTRYPOINT ["java", "-jar", "/apps/app.jar"]
