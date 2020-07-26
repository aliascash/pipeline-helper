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
    removeRelease(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')

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

    Set ALL_PARAMS = ['user', 'repository', 'tag']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private void removeRelease(Map params) {
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    sh(
            script: '''
                docker run \\
                    --rm \\
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                    spectreproject/github-uploader:latest \\
                    github-release delete \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}"
            '''
    )
}
