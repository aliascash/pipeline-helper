// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author Yves Schumann <yves@alias.cash>
//
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
                        --build-arg ALIAS_RELEASE=${gitTag} \
                        --build-arg REPLACE_EXISTING_ARCHIVE=--replace \
                        -t ${dockerTag} \
                        .
                """
        )
    }
}