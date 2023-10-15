(ns filtch.twitter
  (:require
   [clojure.data.json :as json]
   [clojure.string :as string]
   [org.httpkit.client :as http]))

(defn get-access-token [cfg]
  (let [{:keys [_ _ body error]}
        @(http/post "https://id.twitch.tv/oauth2/token"
                    {:query-params {"client_id" (:clientId cfg)
                                    "client_secret" (:clientSecret cfg)
                                    "grant_type" "client_credentials"}})]
    (if error
      (do (println (str "Error getting accessToken" error)) nil)
      (get (json/read-str body) "access_token"))))

(defn get-streams-request [cfg accessToken n after]
  (let [params {"game_id" (:gameId cfg) "first" n}
        {:keys [_ _ body error]}
        @(http/get "https://api.twitch.tv/helix/streams"
                   {:query-params
                    (if (nil? after)
                      params
                      (merge params {"after" after}))
                    :headers {"Client-Id" (:clientId cfg)
                              "Authorization" (str "Bearer " accessToken)}})]
    (if error
      (do (println (str "Error getting streams" error)) nil)
      (json/read-str body))))

(defn format-data [data]
  (let [selected (select-keys data ["user_name" "title" "thumbnail_url" "viewer_count" "language"])]
    (assoc
     selected
     "thumbnail_url" (string/replace
                      (get selected "thumbnail_url")
                      #"\{width\}|\{height\}"
                      {"{width}" "340" "{height}" "208"}))))

(defn get-streams [cfg query]
  (let [accessToken (get-access-token cfg)]
    (loop [streams []
           after nil]
      (let [res (get-streams-request cfg accessToken 100 after)
            data (get res "data")
            pagination (get res "pagination")]
        (if (or (empty? data) (nil? data))
          (map format-data streams)
          (recur
           (concat
            streams
            (filter #(.contains
                      (.toLowerCase (get % "title"))
                      (.toLowerCase query)) data))
           (get pagination "cursor")))))))

