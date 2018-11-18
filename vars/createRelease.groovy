def call(Map params) {
    checkAndSetParams(params)
    return createRelease(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')
    params.preRelease = params.preRelease == 'true' ? '--pre-release' : ''

    if (params.name == null) {
        params.nameOption = ''
        params.name = ''
    } else {
        params.nameOption = '--name'
    }
    if (params.description == null) {
        params.descriptionOption = ''
        params.description = ''
    } else {
        params.descriptionOption = '--description'
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

private Boolean createRelease(Map params) {
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    env.GITHUB_NAMEOPTION = params.nameOption
    env.GITHUB_NAME = params.name
    env.GITHUB_DESCRIPTIONOPTION = params.descriptionOption
    env.GITHUB_DESCRIPTION = params.description
    env.GITHUB_PRERELEASE = params.preRelease
    def statusCode = sh(
            script: '''
                docker run \\
                    --rm \\
                    -t \\
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                    spectreproject/github-uploader:latest \\
                    github-release release \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}" \\
                        ${GITHUB_NAMEOPTION} "${GITHUB_NAME}" \\
                        ${GITHUB_DESCRIPTIONOPTION} "${GITHUB_DESCRIPTION}" \\
                        ${GITHUB_PRERELEASE}
            ''',
            returnStatus: true
    )
    return statusCode == 0
}
