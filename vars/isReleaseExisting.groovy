def call(Map params) {
    checkAndSetParams(params)
    return checkReleaseExistence(params)
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

// See https://stackoverflow.com/questions/7103531/how-to-get-the-part-of-file-after-the-line-that-matches-grep-expression-first
// - sed statement to get all content below 'releases:'
// - 1st grep to filter out first line of each release. They look i. e. like this: "- 2.1.0, name: 'Spectrecoin v2.1.0'..."
//   Must be done this way as the whole release notes will be shown here
// - 2nd grep to find the line with the desired release
private boolean checkReleaseExistence(Map params) {
    def user = params.user
    def repository = params.repository
    def tag = params.tag
    def statusCode = sh(
            script: "docker run \\\n" +
                    "    --rm \\\n" +
                    "    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                    "    spectreproject/github-uploader:latest \\\n" +
                    "    github-release info \\\n" +
                    "        --user ${user} \\\n" +
                    "        --repo ${repository} | sed -e '1,/releases:/d' | grep -- '- .*, name:' | grep '${tag}'",
            returnStatus: true
    )
    return statusCode == 0
}