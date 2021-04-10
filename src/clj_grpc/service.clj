(ns clj-grpc.service
  (:gen-class
    :name clj-grpc.service.GreeterServiceImpl
    :extends
    cljgrpc.helloworld.GreeterGrpc$GreeterImplBase)
  (:import
    [io.grpc.stub StreamObserver]
    [cljgrpc.helloworld
     HelloReply]))


(defn -sayHello [this req res]
  (let [name (.getName req)]
    (doto res
      (.onNext (-> (HelloReply/newBuilder)
                   (.setMessage (str "Hello, " name))
                   (.build)))
      (.onCompleted))))
