# Always use a release version or a specific commit hash below. Don't use
# a branch name since we'd keep getting updates as long as there are new
# commits to that branch and one of them can break the build
FROM eclipse-temurin:17-jdk

# Install dependencies
RUN apt-get update && apt-get install -y wget ca-certificates
# Git is needed in order to update the dls submodule
RUN apt-get install -y git bash curl

RUN git config --global --add safe.directory /cadence-java-client

RUN mkdir /cadence-java-client
WORKDIR /cadence-java-client
