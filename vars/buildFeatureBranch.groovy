// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
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
        def statusCode = sh(
                script: """
                    docker build \
                        -f $dockerfile \
                        --rm \
                        -t $dockerTag \
                        .
                """,
                returnStatus: true
        )
        if (statusCode != 0) {
            currentBuild.result = 'FAILURE'
        }
    }
}
