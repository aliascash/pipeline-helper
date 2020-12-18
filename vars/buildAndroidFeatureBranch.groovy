// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <hlxeasy@gmail.com>>
//
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String dockerfile = params.get("dockerfile")
    String dockerTag = params.get("dockerTag")
    withDockerRegistry(credentialsId: 'DockerHub-Login') {
        withCredentials([string(credentialsId: 'Android-Sign-Keystore', variable: 'KEYSTORE_PASS')]) {
            sh(
                    script: """
                    docker build \
                        -f $dockerfile \
                        --rm \
                        --build-arg KEYSTORE_PASS=${KEYSTORE_PASS} \
                        -t $dockerTag \
                        .
                """,
                    returnStatus: true
            )
        }
    }
}
