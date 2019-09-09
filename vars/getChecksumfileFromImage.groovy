// ----------------------------------------------------------------------------
//  Copyright (c) 2019 The Spectrecoin developers
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String dockerTag = params.get("dockerTag")
    String checksumfile = params.get("checksumfile")
    withDockerRegistry(credentialsId: '051efa8c-aebd-40f7-9cfd-0053c413266e') {
        sh (
                script: """
                    docker run --name tmpContainer -dit ${dockerTag} /bin/sh 
                    docker cp tmpContainer:/filesToUpload/${checksumfile} ${checksumfile}
                    docker stop tmpContainer
                    docker rm tmpContainer
                """
        )
    }
}
