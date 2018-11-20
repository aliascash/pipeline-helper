/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */

def call(Map params) {
    checkAndSetParams(params)
    return createTag(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
//    params.tag = params.get('tag', 'latest')

    if (params.comment == null) {
        params.comment = params.tag
    }

    echo "Running with parameters:\n${params}"
}

private void checkParams(Map params) {

    String checkParamsErrorMessage = ''

    Set REQUIRED_PARAMS = ['tag', 'commit']
    for (String param : REQUIRED_PARAMS) {
        if (!params.containsKey(param)) {
            checkParamsErrorMessage += "missing required parameter: '${param}'\n"
        }
    }

    Set ALL_PARAMS = ['tag', 'commit', 'comment']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private Boolean createTag(Map params) {
    env.GITHUB_TAG = params.tag
    env.GITHUB_COMMIT = params.commit
    env.GITHUB_COMMENT = params.comment
    def statusCode = sh(
            script: '''
                git tag -fa "${GITHUB_TAG}" -m "${GITHUB_COMMENT}" ${GITHUB_COMMIT}
                git push --tags
            ''',
            returnStatus: true
    )
    return statusCode == 0
}
