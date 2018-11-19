def call(Map params) {
    checkAndSetParams(params)

    String fileName = params.description
    File descriptionFile = new File(fileName)
    if (descriptionFile.exists()) {
        params.description = params.description.replace('@', '\\@')
        return updateReleaseWithReleaseNotes(params)
    } else {
        return updateRelease(params)
    }
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

private Boolean updateRelease(Map params) {
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
                    github-release edit \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}" \\
                        --name "${GITHUB_NAME}" \\
                        --description "${GITHUB_DESCRIPTION}" \\
                        ${GITHUB_PRERELEASE}
            ''',
            returnStatus: true
    )
    return statusCode == 0
}

private Boolean updateReleaseWithReleaseNotes(Map params) {
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
                    github-release edit \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" \\
                        --tag "${GITHUB_TAG}" \\
                        --name "${GITHUB_NAME}" \\
                        --description "$(cat ${GITHUB_DESCRIPTION})" \\
                        ${GITHUB_PRERELEASE}
            ''',
            returnStatus: true
    )
    return statusCode == 0
}
