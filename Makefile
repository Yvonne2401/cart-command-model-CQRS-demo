.PHONY: *

# The first command will be invoked with `make` only and should be `build`
build:
	./mvnw verify -Pformat

ci: clean build test

clean:
	./mvnw clean

format: clean
	./mvnw test-compile -Pformat

test:
	./mvnw verify -Pcomponent-test

update:
	./mvnw versions:update-parent versions:update-properties versions:use-latest-versions

yolo:
	./mvnw verify -T0.5C -DskipTests -Dquality.skip
