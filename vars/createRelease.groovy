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

private boolean createRelease(Map params) {
    def user = params.user
    def repository = params.repository
    def tag = params.tag
    def nameOption = params.nameOption
    def name = params.name
    def descriptionOption = params.descriptionOption
    def description = params.description
    def preRelease = params.preRelease
    def statusCode = sh(
            script: """
                docker run \\
                    --rm \\
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                    spectreproject/github-uploader:latest \\
                    github-release release \\
                        --user ${user} \\
                        --repo ${repository} \\
                        --tag ${tag} \\
                        ${nameOption} "${name}" \\
                        ${descriptionOption} "${description}" \\
                        ${preRelease}
            """,
            returnStatus: true
    )
    return statusCode == 0
}
