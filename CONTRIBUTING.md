# Contributing to Mikrobloggeriet

Developing on Mikrobloggeriet requires certain dependencies.

## Setup a development environment on Mac with Homebrew

### Clojure

Mikrobloggeriet is written in [Clojure].

[Clojure]: https://clojure.org/

```shell
brew install clojure/tools/clojure
```

For more information on installing Clojure, read the official [Clojure installation Guide](https://clojure.org/guides/install_clojure).

Clojure runs on the Java Virtual Machine (JVM), meaning a JDK build must be installed as well.
We recommend using [Temurin] as it has a permissive license and is activly maintained.

[Temurin]: https://adoptium.net/en-GB/

```shell
brew install --cask temurin
```

### Pandoc
Mikrobloggeriet uses [Pandoc] to convert from Markdown-format to HTML when publishing blog posts. 

[Pandoc]: https://pandoc.org/

```shell
brew install pandoc
```

## CLI

To install dependencies related to the CLI, read the [CLI Quickstart].

[CLI quickstart]: cli-quickstart-mblog.md

## Development environment

### Start a REPL in VSCode

To connect VSCode to the Clojure REPL, we recommend using the VSCode extension [Calva].

1. In the Command Palette, search for **Calva: Start a Project REPL and Connect**
2. For *Project Type*, select **deps.edn**
3. For alias, select **:dev**

This will make Calva start a REPL. In the REPL, evaluate the function `(start!)` to start the server for mikrobloggeriet. 

[Calva]: https://calva.io

### Start the HTTP server

First, start a REPL from within your editor (using a REPL from a terminal is discouraged).

Then, run `start!` from your REPL:

```clojure
user> (start!)
```

To stop the HTTP server from within your REPL, run `(stop!)`.
To restart the HTTP server, run `(start!)` again.

### Deployment to HOPS

HOPS deployment happens automatically.
See `iterapp.toml` for configuration.

### Deployment to Application.garden

To deploy, run:

```
garden deploy
```

Application.garden deployment requires:

- An Application.garden user
- Membership to the `mikrobloggeriet` group on Application.garden

Application.garden private beta signup: https://application.garden/signup

Application.garden docs: https://docs.apps.garden/

### Run the tests

To run all the tests with Kaocha (our test runner), run

    bin/kaocha

in a terminal.
`bin/kaocha --help` gives you all the test runner options.
Having `bin/kaocha --watch --fail-fast` in a terminal next to your editor gives quite fast test feedback.

The tests can also be run from a REPL.
In Calva, see the docs for the [Calva Test Runner].

[Calva Test Runner]: https://calva.io/test-runner/

## Development FAQ

**Q: Where do `start!`, `stop!` and `run-all-tests` come from?**

They are defined in `src/user.clj`.
Have a look!
