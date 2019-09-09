def buildFeatureBranch(String dockerfile, String tag) {
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh "docker build \\\n" +
                "-f $dockerfile \\\n" +
                "--rm \\\n" +
                "-t $tag \\\n" +
                "."
    }
}
