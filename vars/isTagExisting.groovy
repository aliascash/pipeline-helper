def call(Map params) {
    checkAndSetParams(params)
    return checkTagExistence(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tagToSearch = params.get('tagToSearch', 'latest')

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

    Set ALL_PARAMS = ['user', 'repository', 'tagToSearch']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private boolean checkTagExistence(Map params) {
    def user = params.user
    def repository = params.repository
    def tagToSearch = params.tagToSearch
    def statusCode = sh(
            script: "docker run \\\n" +
                    "    --rm \\\n" +
                    "    -it \\\n" +
                    "    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                    "    spectreproject/github-deployer:latest \\\n" +
                    "    github-release info \\\n" +
                    "        --user ${user} \\\n" +
                    "        --repo ${repository} | grep '\\- ${tagToSearch} (commit:'",
            returnStatus: true
    )
    return statusCode
}