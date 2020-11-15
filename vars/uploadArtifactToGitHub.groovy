// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <hlxeasy@gmail.com>>
//
// ----------------------------------------------------------------------------

def call(Map params) {
    checkAndSetParams(params)
    uploadArtifact(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')
    params.artifactNameLocal = params.get('artifactNameLocal', params.artifactNameRemote)

    echo "Running with parameters:\n${params}"
}

private void checkParams(Map params) {

    String checkParamsErrorMessage = ''

    Set REQUIRED_PARAMS = ['user', 'repository', 'artifactNameRemote']
    for (String param : REQUIRED_PARAMS) {
        if (!params.containsKey(param)) {
            checkParamsErrorMessage += "missing required parameter: '${param}'\n"
        }
    }

    Set ALL_PARAMS = ['user', 'repository', 'tag', 'artifactNameLocal', 'artifactNameRemote', 'githubCIToken']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private void uploadArtifact(Map params) {
    env.GITHUB_CI_TOKEN = params.githubCIToken
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    env.GITHUB_ARTIFACTNAMELOCAL = params.artifactNameLocal
    env.GITHUB_ARTIFACTNAMEREMOTE = params.artifactNameRemote
    sh(
            script: '''
                docker run \\
                    --rm \\
                    -t \\
                    -v ${WORKSPACE}:/filesToUpload \\
                    aliascash/github-uploader:latest \\
                    github-release upload \\
                        --security-token "${GITHUB_CI_TOKEN}" \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}" \\
                        --name "${GITHUB_ARTIFACTNAMEREMOTE}" \\
                        --file "/filesToUpload/${GITHUB_ARTIFACTNAMELOCAL}" \\
                        --replace
            '''
    )
}
