// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <hlxeasy@gmail.com>>
//
// ----------------------------------------------------------------------------

def call(Map params) {
    checkAndSetParams(params)
    createTag(params)
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

/*
 * At first all local tags must be removed and then fetched again because
 * there might be more tags locally than remote, as old tags could be removed
 * on Github but the workspace on the build slave is reused!
 */
private void createTag(Map params) {
    env.GITHUB_TAG = params.tag
    env.GITHUB_COMMIT = params.commit
    env.GITHUB_COMMENT = params.comment
    sh(
            script: '''
                git tag -l | xargs git tag -d > /dev/null
                git fetch > /dev/null 2>&1
                git tag -fa "${GITHUB_TAG}" -m "${GITHUB_COMMENT}" ${GITHUB_COMMIT}
                git push --tags --force
            '''
    )
}
