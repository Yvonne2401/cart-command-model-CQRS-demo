# Makefile for a Maven project
# Run `make` or `make help` to list available targets.

# Use the Maven wrapper if present, otherwise fall back to system Maven
MVN        := $(if $(wildcard ./mvnw),./mvnw,mvn)
MVN_FLAGS  ?= -B
PROFILE    ?=
PROFILE_ARG := $(if $(PROFILE),-P$(PROFILE),)
MVN_CMD    := $(MVN) $(MVN_FLAGS) $(PROFILE_ARG)

# Override on the command line, e.g. make run MAIN_CLASS=com.example.App
MAIN_CLASS ?=
TEST       ?=
MODULE     ?=
MODULE_ARG := $(if $(MODULE),-pl $(MODULE) -am,)

.DEFAULT_GOAL := help

.PHONY: help clean compile test test-one verify package install build \
        skip-tests run deps tree updates javadoc site coverage \
        format lint offline version

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-14s\033[0m %s\n", $$1, $$2}'
	@echo ""
	@echo "Variables: PROFILE=<maven profile>  MODULE=<module>  TEST=<TestClass[#method]>  MAIN_CLASS=<fqcn>"
all: clean verify test

clean: ## Remove build output (target/)
	$(MVN_CMD) clean

compile: ## Compile the main sources
	$(MVN_CMD) $(MODULE_ARG) compile

test: ## Run unit tests
	$(MVN_CMD) $(MODULE_ARG) test

test-one: ## Run a single test: make test-one TEST=MyTest#myMethod
	@test -n "$(TEST)" || (echo "Usage: make test-one TEST=MyTest[#method]" && exit 1)
	$(MVN_CMD) $(MODULE_ARG) test -Dtest='$(TEST)' -DfailIfNoTests=false -Dsurefire.failIfNoSpecifiedTests=false

verify: ## Run unit and integration tests plus checks
	$(MVN_CMD) $(MODULE_ARG) verify

package: ## Build the JAR/WAR (runs tests)
	$(MVN_CMD) $(MODULE_ARG) package

install: ## Install artifacts into the local ~/.m2 repository
	$(MVN_CMD) $(MODULE_ARG) install

build: clean package ## Clean, then package

skip-tests: ## Package without running tests
	$(MVN_CMD) $(MODULE_ARG) clean package -DskipTests

run: ## Run the app via exec plugin: make run MAIN_CLASS=com.example.App
	@test -n "$(MAIN_CLASS)" || (echo "Usage: make run MAIN_CLASS=com.example.App" && exit 1)
	$(MVN_CMD) compile exec:java -Dexec.mainClass="$(MAIN_CLASS)"

deps: ## Download all dependencies
	$(MVN_CMD) dependency:resolve dependency:resolve-plugins

tree: ## Print the dependency tree
	$(MVN_CMD) dependency:tree

updates: ## Show available dependency and plugin updates
	$(MVN_CMD) versions:display-dependency-updates versions:display-plugin-updates

javadoc: ## Generate Javadoc
	$(MVN_CMD) javadoc:javadoc

site: ## Generate the Maven site
	$(MVN_CMD) site

coverage: ## Run tests with JaCoCo coverage (requires jacoco plugin)
	$(MVN_CMD) clean test jacoco:report
	@echo "Report: target/site/jacoco/index.html"

format: ## Auto-format code (requires spotless plugin)
	$(MVN_CMD) spotless:apply

lint: ## Check formatting/style (requires spotless or checkstyle plugin)
	$(MVN_CMD) spotless:check

offline: ## Build using only the local repository
	$(MVN_CMD) -o package

version: ## Print Maven and Java versions
	$(MVN) -v
