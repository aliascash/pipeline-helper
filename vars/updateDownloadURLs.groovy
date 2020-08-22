// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
//
// SPDX-License-Identifier: MIT
//
// @author Yves Schumann <yves@alias.cash>
//
// ----------------------------------------------------------------------------

def call(Map params) {
    checkAndSetParams(params)
    updateURLs(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)
    echo "Running with parameters:\n${params}"
}

private void checkParams(Map params) {

    String checkParamsErrorMessage = ''

    Set REQUIRED_PARAMS = ['version', 'gitHash', 'pageId']
    for (String param : REQUIRED_PARAMS) {
        if (!params.containsKey(param)) {
            checkParamsErrorMessage += "missing required parameter: '${param}'\n"
        }
    }

    Set ALL_PARAMS = ['version', 'gitHash', 'pageId']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private void updateURLs(Map params) {
    env.WP_URL = "https://alias.cash"
    env.urlPart1="download.alias.cash/files"
    env.urlPart2="Alias"
    env.PAGE_ID = params.pageId
    env.NEW_VERSION = params.version
    env.GIT_HASH = params.gitHash
    env.TOKEN = ""

    // Examples:
    // <a href="https://download.alias.cash/files/4.2.0/Spectrecoin-4.2.0-38fb6b17-Win64.zip">
    // <a href="https://download.alias.cash/files/4.2.0/Spectrecoin-4.2.0-38fb6b17-Mac.dmg">
    // <a href="https://download.alias.cash/files/4.2.0/Spectrecoin-4.2.0-38fb6b17-Mac-OBFS4.dmg">
    // <a href="https://download.alias.cash/files/4.2.0/Spectrecoin-4.2.0-38fb6b17-Ubuntu-18-04.tgz">
    // <a href="https://download.alias.cash/files/4.2.0/Spectrecoin-4.2.0-38fb6b17-RaspbianLightBuster.zip">
    //
    // RegEX:
    //          |    Match #1    |   |  Match #2   | |             Previous version            | |           |  |
    //          |...${urlPart1}/ |...| /$urlPart2  |-|  MAJOR      .   MINOR      .   BUGFIX   |-| Git-Hash  |- | up to ">"
    sh(
            script: '''
                if [ -z "${WP_USERNAME}" ] ; then
                    echo "Env var WP_USERNAME not set!"
                    exit 1
                fi
                if [ -z "${WP_PASSWORD}" ] ; then
                    echo "Env var WP_PASSWORD not set!"
                    exit 1
                fi
                # Get Bearer token:
                TOKEN=$(curl -s -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' --data "{\\"username\\":\\"${WP_USERNAME}\\", \\"password\\":\\"${WP_PASSWORD}\\",\\"rememberMe\\":false}" ${WP_URL}/wp-json/jwt-auth/v1/token | jq -r '.data.token')
                
                # Get content of current post
                curl -H 'Accept: application/json' -H "Authorization: Bearer ${TOKEN}" ${WP_URL}/wp-json/wp/v2/posts/${PAGE_ID} | jq '{content: .content.rendered}' > content.json
                
                # Modify URLs
                sed -i "s#\\(<a href=\\"${urlPart1}/\\).*\\(/${urlPart2}\\)-[[:digit:]]\\+\\.[[:digit:]]\\+\\.[[:digit:]]\\+-[[:alnum:]]\\+-\\(.*</a>\\)#\\1${NEW_VERSION}\\2-${NEW_VERSION}-${GIT_HASH}-\\3#g" content.json
                
                # Push modified content
                curl -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer ${TOKEN}" ${WP_URL}/wp-json/wp/v2/posts/${PAGE_ID} -d @content.json | jq -r '.content.rendered'
            '''
    )
}
