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
private Boolean checkReleaseExistence(Map params) {
    env.GITHUB_USER = params.user
    env.GITHUB_REPOSITORY = params.repository
    env.GITHUB_TAG = params.tag
    def statusCode = sh(
            script: '''
                docker run \\
                    --rm \\
                    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\
                    spectreproject/github-uploader:latest \\
                    github-release info \\
                        --user "${GITHUB_USER}" \\
                        --repo "${GITHUB_REPOSITORY}" | sed -e '1,/releases:/d' | grep -- "- ${GITHUB_TAG}, name: "
            ''',
            returnStatus: true
    )
    return statusCode == 0
}
