// ----------------------------------------------------------------------------
//  Copyright (c) 2019 The Spectrecoin developers
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String dockerfile = params.get("dockerfile")
    String dockerTag = params.get("dockerTag")
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh(
                script: """
                    docker build \
                        -f $dockerfile \
                        --rm \
                        -t $dockerTag \
                        .
                """
        )
    }
}
