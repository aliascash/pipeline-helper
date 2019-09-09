def buildBranch(String dockerfile, String dockerTag, String gitTag, String gitCommit) {
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh "docker build \\\n" +
                "-f ${dockerfile} \\\n" +
                "--rm \\\n" +
                "--build-arg GITHUB_TOKEN=${GITHUB_TOKEN} \\\n" +
                "--build-arg GIT_COMMIT=${gitCommit} \\\n" +
                "--build-arg SPECTRECOIN_RELEASE=${gitTag} \\\n" +
                "--build-arg REPLACE_EXISTING_ARCHIVE=--replace \\\n" +
                "-t ${dockerTag} \\\n" +
                "."
    }
}
