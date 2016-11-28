(ns codox.writer.rst
  "Document writer that outputs reStructuredText"
  )

(defn- gen-rst-underline
  "Create reStructuredText"
  [character len]
  (apply str
    (seq (char-array len character)))
  )

(defn- gen-page-title
  "Generates the page title for the rst document for a namespace"
  [namespace]
  (str
    (gen-rst-underline \= (.length (:name namespace)))
    (newline)
    (:name namespace)
    (newline)
    (gen-rst-underline \= (.length (:name namespace)))
    (newline)
    (newline)
    ))

(defn- get-public-section
  "Generates the header and section body for public vars/functions"
  [public]
  (str
    (:name public)
    (newline)
    (gen-rst-underline \# (.length (:name public)))
    (newline)
    (newline)
    (:doc public)
    (newline)
    ))

(defn write-docs
  "Take raw documentation info and turn it into formatted reStructuredText."
  [{:keys [output-path] :as project}]
  )
