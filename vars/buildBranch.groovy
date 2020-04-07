// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2019 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String dockerfile = params.get("dockerfile")
    String dockerTag = params.get("dockerTag")
    String gitTag = params.get("gitTag")
    String gitCommit = params.get("gitCommit")
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh(
                script: """
                    docker build \
                        -f ${dockerfile} \
                        --rm \
                        --build-arg GITHUB_TOKEN=${GITHUB_TOKEN} \
                        --build-arg GIT_COMMIT=${gitCommit} \
                        --build-arg SPECTRECOIN_RELEASE=${gitTag} \
                        --build-arg REPLACE_EXISTING_ARCHIVE=--replace \
                        -t ${dockerTag} \
                        .
                """
        )
    }
}
