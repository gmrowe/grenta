# grenta

grenta is a meant to be an exploration of generative art. The word `grenta` is just an anagram of gen-art.

## Requirements

- A clojure/jvm installation is required to run this project.

## Usage

Curently the project is setup for interactive development. The workflow would be to connect the project to a running REPL (details elided) and execute (show-image!) or (update-and-show-image! f) and the image should appear. The image can be manipulated witout restarting by re-evaluating `(update-and-show-image! f)` or `(update-image! f)` followed by `(show-image!)`.

## License

[MIT](LICENSE) © [gmrowe](https://github.com/gmrowe).
