clean:
	@rm -rf bin

build:
	@mkdir -p bin
	@cp .env bin/
	@cp app/run bin/
	@cp -r app/spark-3.4.0 bin/
	@docker-compose up --force-recreate app-build
	@docker-compose up --force-recreate cli-build
	@cp app/*.jar bin/
	@cp app/run bin/
	@cp -r app/spark* bin/