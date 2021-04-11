(ns clj-grpc.core
  (:gen-class)
  (:require [clj-grpc.service]
            [mount.core :as mount :refer [defstate]])
  (:import
    [io.grpc
     Server
     ServerBuilder]
    [io.grpc.stub StreamObserver]
    [clj-grpc.service GreeterServiceImpl]))

(def SERVER_PORT 50051)

(defn start []
  (let [greeter-service (new GreeterServiceImpl)
        server (-> (ServerBuilder/forPort SERVER_PORT)
                   (.addService greeter-service)
                   (.build)
                   (.start))]
    (-> (Runtime/getRuntime)
        (.addShutdownHook
          (Thread. (fn []
                     (if (not (nil? server))
                       (.shutdown server))))))
    server))
    ;(if (not (nil? server))
    ;    (.awaitTermination server))))

(defstate server
          :start (start)
          :stop (.shutdown server))

(defn go []
  (print "Now listening on ports: " SERVER_PORT)
  (mount/start)
  :ready)

(defn stop []
  (mount/stop)
  :stopped)

(defn -main []
  (print "Now listening on port: " SERVER_PORT)
  (mount/start))
