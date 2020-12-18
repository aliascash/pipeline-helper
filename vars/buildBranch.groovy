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
    String gitTag = params.get("gitTag")
    String gitCommit = params.get("gitCommit")
    String githubToken = params.get("githubCIToken")
    withDockerRegistry(credentialsId: 'DockerHub-Login') {
        def statusCode = sh(
                script: """
                    docker build \
                        -f ${dockerfile} \
                        --rm \
                        --build-arg GITHUB_CI_TOKEN=${githubToken} \
                        --build-arg GIT_COMMIT=${gitCommit} \
                        --build-arg ALIAS_RELEASE=${gitTag} \
                        --build-arg REPLACE_EXISTING_ARCHIVE=--replace \
                        -t ${dockerTag} \
                        .
                """,
                returnStatus: true
        )
        if (statusCode != 0) {
            currentBuild.result = 'FAILURE'
        }
    }
}
