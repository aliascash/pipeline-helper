def call(Map params) {
    checkAndSetParams(params)
    return createTag(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')
    params.preRelease = params.preRelease == 'true' ? '--pre-release' : ''

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

private boolean createTag(Map params) {
    def user = params.user
    def repository = params.repository
    def tag = params.tag
    def name = params.name
    def description = params.description
    def preRelease = params.preRelease
    def statusCode = sh(
            script: "docker run \\\n" +
                    "    --rm \\\n" +
                    "    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                    "    spectreproject/github-uploader:latest \\\n" +
                    "    github-release release \\\n" +
                    "        --user ${user} \\\n" +
                    "        --repo ${repository} \\\n",
                    "        --tag ${tag} \\\n",
                    "        --name \"${name}\" \\\n",
                    "        --description \"${description}\"\\\n",
                    "        ${preRelease}",
            returnStatus: true
    )
    return statusCode == 0
}