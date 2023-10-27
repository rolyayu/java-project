docker-up:
	docker-compose up --build

docker-stop:
	docker ps -q --filter name=pet -aq | xargs docker container stop

docker-clear:
	make docker-stop && docker ps -a --filter name=pet -q | xargs docker rm --volumes