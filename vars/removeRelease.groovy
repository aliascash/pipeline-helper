// ----------------------------------------------------------------------------
//  Copyright (c) 2018 The Spectrecoin developers
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params) {
    checkAndSetParams(params)
    return removeRelease(params)
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

private Boolean removeRelease(Map params) {
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    def statusCode = sh(
            script: '''
                docker run \\
                    --rm \\
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                    spectreproject/github-uploader:latest \\
                    github-release delete \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}"
            ''',
            returnStatus: true
    )
    return statusCode == 0
}
