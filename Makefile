ES_VERSION ?= 8.15.2
JAVA_HOME ?= /usr/lib/jvm/java-17-openjdk-amd64

.PHONY: all build

all: build

build:
	@echo "Building with Elasticsearch version $(ES_VERSION)"
	@export JAVA_HOME=$(JAVA_HOME) && \
	export PATH=$$JAVA_HOME/bin:$$PATH && \
	./gradlew build -Pelasticsearch.version=$(ES_VERSION)
