clean:
	@rm -rf bin

build:
	@mkdir -p bin
	@cp .env bin/
	@cp app/run bin/
	@cp -r app/spark-3.3.1 bin/
	@docker-compose up --force-recreate app-build
	@docker-compose up --force-recreate cli-build
	@mv app/app.jar bin/