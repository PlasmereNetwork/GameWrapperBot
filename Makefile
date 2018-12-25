NAME=game-wrapper-bot
VERSION=$(shell git rev-parse HEAD)
REPO=templexmc
PWD=$(shell pwd)

build:
	docker run --rm \
		-v $(PWD):/home/gradle/project \
		-w /home/gradle/project \
		gradle:4.8.1-jdk8-slim \
		gradle --no-daemon clean build assemble
	docker build -t $(REPO)/$(NAME):latest .

clean:
	rm -rf build/ out/
