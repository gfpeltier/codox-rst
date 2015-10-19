# Codox

A tool for generating API documentation from Clojure or ClojureScript
source code.

## Examples

Some examples of API docs generated by Codox in real projects:

* [Medley](https://weavejester.github.io/medley/medley.core.html)
* [Compojure](https://weavejester.github.com/compojure/)
* [Hiccup](https://weavejester.github.com/hiccup/)
* [Ring](https://ring-clojure.github.com/ring/)

## Usage

Include the following plugin in your `project.clj` file or your global
profile:

```clojure
:plugins [[codox "0.9.0-SNAPSHOT"]]
```

Then run:

```
lein codox
```

This will generate API documentation in the "target/doc" subdirectory.


## AOT Compilation

AOT-compiled namespaces will lose their metadata, which mean you'll
lose documentation for namespaces. Avoid having global `:aot`
directives in your project; instead, place them in a specialized
profile, such as `:uberjar`.


## Project Options

Codox can generate documentation from Clojure or ClojureScript. By
default it looks for Clojure source files, but you can change this to
ClojureScript by setting the `:language` key:

```clojure
:codox {:language :clojurescript}
```

### Source Files

By default Codox looks for source files in the `:source-paths` of your
project, but you can change this just for Codox by placing the
following in your `project.clj` file:

```clojure
:codox {:source-paths ["path/to/source"]}
```

The `:namespaces` option can be used to restrict the documentation to
a specific set of namespaces:

```clojure
:codox {:namespaces [library.core library.io]}
```

Regular expressions can also be used for more general matching:

```clojure
:codox {:namespaces [#"^library\."]}
```

For excluding only internal namespaces, it's sometimes useful to use
negative lookahead:

```clojure
:codox {:namespaces "#"^library\.(?!internal)"}
```

To override the namespaces list and include all namespaces, use `:all`
(the default):

```clojure
:codox {:namespaces :all}
```

Codox constructs documentation from metadata on vars and namespaces.
You can specify a set of default metadata using the `:metadata` map:

```clojure
:codox {:metadata {:doc "FIXME: write docs"}}
```

### Documentation Files

As well as source files, Codox also tries to include documentation
files as well. By default it looks for these in the `doc` directory,
but you can change this with:

```clojure
:codox {:doc-paths ["path/to/docs"]}
```

Documentation files will appear in the output sorted by their
filename, so if you want a particular order, prefix your files with
`01`, `02`, etc.

Currently only markdown files (`.md` or `.markdown`) are supported.


### Output Files

To write output to a directory other than the default, use the
`:output-path` key:

```clojure
:codox {:output-path "codox"}
```

To use a different output writer, specify the fully qualified symbol of the
writer function in the `:writer` key:

```clojure
:codox {:writer codox.writer.html/write-docs}
```

By default the writer will include the project name, version and
description in the output. You can override these by specifying a
`:project` map in your Codox configuration:

```clojure
:codoc {:project {:name "Example", :version "1.0", :description "N/A"}}
```

### Source Links

If you have the source available at a URI and would like to have links
to the function's source file in the documentation, you can set the
`:source-uri` key:

```clojure
:codox {:source-uri "https://github.com/foo/bar/blob/master/{filepath}#L{line}"}
```

The URI is a template that may contain the following keys:

* `{filepath}`  - the file path from the root of the repository
* `{classpath}` - the relative path of the file within the source directory
* `{line}`      - the line number of the source file

You can also assign different URI templates to different paths of your
source tree. This is particularly useful for created source links from
generated source code, such as is the case with [cljx][].

For example, perhaps your Clojure source files are generated in
`target/classes`. To link back to the original .cljx file, you could do:

```clojure
:codox
{:source-uri
 {#"target/classes" "https://github.com/foo/bar/blob/master/src/{classpath}x#L{line}"
  #".*"             "https://github.com/foo/bar/blob/master/{filepath}#L{line}"}
```

[cljx]: https://github.com/lynaghk/cljx


## Metadata Options

To force Codox to skip a public var, add `:no-doc true`
to the var's metadata. For example:

```clojure
;; Documented
(defn square
  "Squares the supplied number."
  [x]
  (* x x)

;; Not documented
(defn ^:no-doc hidden-square
  "Squares the supplied number."
  [x]
  (* x x))
```

You can also skip namespaces by adding `:no-doc true` to the
namespace's metadata. *This currently only works for Clojure code, not
ClojureScript.* For example:

```clojure
(ns ^:no-doc hidden-ns)
```

To denote the library version the var was added in, use the `:added`
metadata key:

```clojure
(defn square
  "Squares the supplied number."
  {:added "1.0"}
  [x]
  (* x x))
```

Similar, deprecated vars can be denoted with the `:deprecated`
metadata key:

```clojure
(defn square
  "Squares the supplied number."
  {:deprecated "2.0"}
  [x]
  (* x x))
```


## Docstring Formats

By default, docstrings are rendered by Codox as fixed-width plain
text, as they would be on a terminal. However, you can override this
behavior by specifying an explicit format for your docstrings.

Currently there are only two formats for docstrings: plaintext and
markdown. The markdown format includes extensions for code blocks,
tables, and, like the plaintext format, URLs are automatically encoded
as links.

You can specify docstring formats via a var's metadata, using the
`:doc/format` option:

```clojure
(defn foo
  "A **markdown** formatted docstring."
  {:doc/format :markdown}
  [x])
```

Or you can specify the docstring format globally by adding it to the
`:metadata` map in your project.clj file:

```clojure
:codox {:metadata {:doc/format :markdown}}
```

Markdown docstrings also support wikilink-style relative links, for
referencing other vars. Vars in the current namespace will be matched
first, and then Codox will try and find a best match out of all the
vars its documenting.

```clojure
(defn bar
  "See [[foo]] and [[user/square]] for other examples."
  {:doc/format :markdown}
  [x])
```


## License

Copyright © 2015 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
