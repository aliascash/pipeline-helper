// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <helix@alias.cash>
//
// ----------------------------------------------------------------------------

def call(Map params) {
    checkAndSetParams(params)
    createRelease(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')
    params.preRelease = params.preRelease == 'true' ? '--pre-release' : ''

    if (params.name == null) {
        params.name = params.tag
    }
    if (params.description == null) {
        params.description = params.tag
    }

    echo "Running with parameters:\n${params}"
}

private void checkParams(Map params) {

    String checkParamsErrorMessage = ''

    Set REQUIRED_PARAMS = ['user', 'repository']
    for (String param : REQUIRED_PARAMS) {
        if (!params.containsKey(param)) {
            checkParamsErrorMessage += "missing required parameter: '${param}'\n"
        }
    }

    Set ALL_PARAMS = ['user', 'repository', 'tag', 'name', 'description', 'preRelease']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private void createRelease(Map params) {
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    env.GITHUB_NAME = params.name
    env.GITHUB_DESCRIPTION = params.description
    env.GITHUB_PRERELEASE = params.preRelease
    sh(
            script: '''
                if test -e ${GITHUB_DESCRIPTION} ; then 
                    docker run \\
                        --rm \\
                        -t \\
                        -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                        spectreproject/github-uploader:latest \\
                        github-release release \\
                            --user "${GITHUB_USER}" \\
                            --repo "${GITHUB_REPOSITORY}" \\
                            --tag "${GITHUB_TAG}" \\
                            --name "${GITHUB_NAME}" \\
                            --description "$(cat ${GITHUB_DESCRIPTION})" \\
                            ${GITHUB_PRERELEASE}
                else
                    docker run \\
                        --rm \\
                        -t \\
                        -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                        spectreproject/github-uploader:latest \\
                        github-release release \\
                            --user "${GITHUB_USER}" \\
                            --repo "${GITHUB_REPOSITORY}" \\
                            --tag "${GITHUB_TAG}" \\
                            --name "${GITHUB_NAME}" \\
                            --description "${GITHUB_DESCRIPTION}" \\
                            ${GITHUB_PRERELEASE}
                fi
            '''
    )
}
