def call(Map params = [:]) {
    String dockerfile = params.get("dockerfile")
    String tag = params.get("tag")
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh(
                script: """
                    docker build \
                        -f $dockerfile \
                        --rm \
                        -t $tag \
                        .
                """
        )
    }
}
