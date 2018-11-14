/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */

def call(Map params) {
    checkAndSetParams(params)
    return uploadArtifact(params)
}

private void checkAndSetParams(Map params) {
    checkParams(params)

    // Set default values
    params.tag = params.get('tag', 'latest')
    params.artifactNameLocal = params.get('artifactNameLocal', params.artifactNameRemote)

    echo "Running with parameters:\n${params}"
}

private void checkParams(Map params) {

    String checkParamsErrorMessage = ''

    Set REQUIRED_PARAMS = ['user', 'repository', 'artifactNameRemote']
    for (String param : REQUIRED_PARAMS) {
        if (!params.containsKey(param)) {
            checkParamsErrorMessage += "missing required parameter: '${param}'\n"
        }
    }

    Set ALL_PARAMS = ['user', 'repository', 'tag', 'artifactNameLocal', 'artifactNameRemote']

    for (String param : params.keySet()) {
        if (!ALL_PARAMS.contains(param)) {
            checkParamsErrorMessage += "unknown parameter: '${param}\n"
        }
    }

    if (checkParamsErrorMessage) {
        echo "${checkParamsErrorMessage}Allowed parameters: ${ALL_PARAMS}"
    }
}

private boolean uploadArtifact(Map params) {
    def user = params.user
    def repository = params.repository
    def tag = params.tag
    def artifactNameLocal = params.artifactNameLocal
    def artifactNameRemote = params.artifactNameRemote
    def statusCode = sh(
            script: "docker run \\\n" +
                    "    --rm \\\n" +
                    "    -it \\\n" +
                    "    -e GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                    "    -v ${WORKSPACE}:/filesToUpload \\\n" +
                    "    spectreproject/github-deployer:latest \\\n" +
                    "    github-release info \\\n" +
                    "        --user ${user} \\\n" +
                    "        --repo ${repository} \\\n" +
                    "        --tag ${tag} \\\n" +
                    "        --name \"${artifactNameRemote}\" \\\n" +
                    "        --file /filesToUpload/${artifactNameLocal} \\\n" +
                    "        --replace",
            returnStatus: true
    )
    return statusCode
}
