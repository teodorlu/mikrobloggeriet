(ns olorm.cli
  (:require
   [babashka.cli :as cli]
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [olorm.lib :as lib]
   [babashka.process :refer [shell]]))

(defn config-folder [] (str (fs/xdg-config-home) "/olorm"))
(defn config-file [] (str (config-folder) "/config.edn"))

(defn repo-path []
  (-> (config-file)
      slurp
      edn/read-string
      :repo-path))

(declare subcommands)

(defn olorm-help [{:keys [_opts]}]
  (println "Available subcommands:")
  (println)
  (doseq [{:keys [cmds]} subcommands]
    (when (seq cmds)
      (println (str "  olorm " (str/join " " cmds))))))

(defn olorm-set-repo-path [{:keys [opts]}]
  (let [repo-path (:repo-path opts)]
    (when-not repo-path
      (println (str/trim "
Usage:

    olorm set-repo-path REPO_PATH

REPO_PATH is the path to the OLORM repo. The CLI can be used from any folder on
your system, so we need to know where to find OLORM pages.
"))
      (System/exit 1))
    (let [repo-path (fs/absolutize repo-path)]
      (fs/create-dirs (config-folder))
      (spit (config-file) (prn-str {:repo-path (str repo-path)})))))

(defn olorm-repo-path [{:keys [_opts]}]
  (if (fs/exists? (config-file))
    (println (repo-path))
    (do
      (println "Error: config file not found")
      (System/exit 1))))

(defn olorm-create [{}]
  (let [repo-path (repo-path)
        olorms (lib/olorms {:repo-path repo-path})
        last-olorm (->> olorms (map :olorm) sort last)
        next-olorm (inc (or last-olorm 0))
        next-dir (lib/olorm-path {:repo-path repo-path :olorm next-olorm})
        ]
    (fs/create-dirs next-dir)
    (let [next-index-md (str next-dir "/index.md")]
      (shell {:dir repo-path} "git pull --rebase")
      (spit next-index-md (lib/md-skeleton {:olorm next-olorm}))
      (shell {:dir repo-path} (System/getenv "EDITOR") next-index-md)
      (shell {:dir repo-path} "git add .")
      (shell {:dir repo-path} "git commit -m" (str "olorm-" next-olorm))
      (shell {:dir repo-path} "git push"))))

(def subcommands
  [
   {:cmds ["create"]        :fn olorm-create}
   {:cmds ["help"]          :fn olorm-help}
   {:cmds ["repo-path"]     :fn olorm-repo-path}
   {:cmds ["set-repo-path"] :fn olorm-set-repo-path :args->opts [:repo-path]}
   {:cmds []                :fn olorm-help}])

(defn -main [& args]
  (cli/dispatch subcommands args))
