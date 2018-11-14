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

private boolean removeRelease(Map params) {
    def user = params.user
    def repository = params.repository
    def tag = params.tag
    def statusCode = sh(
            script: "docker run \\\n" +
                    "    --rm \\\n" +
                    "    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                    "    spectreproject/github-uploader:latest \\\n" +
                    "    github-release delete \\\n" +
                    "        --user ${user} \\\n" +
                    "        --repo ${repository} \\\n" +
                    "        --tag ${tag}",
            returnStatus: true
    )
    return statusCode == 0
}