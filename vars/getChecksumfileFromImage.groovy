// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <hlxeasy@gmail.com>>
//
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String dockerTag = params.get("dockerTag")
    String checksumfile = params.get("checksumfile")
    String tmpContainer = ""
    withDockerRegistry(credentialsId: 'DockerHub-Login') {
        sh (
                script: """
                    tmpContainer=${RANDOM}
                    docker run --name tmpContainer${tmpContainer} -dit ${dockerTag} /bin/sh 
                    docker cp tmpContainer${tmpContainer}:/filesToUpload/${checksumfile} ${checksumfile}
                    docker stop tmpContainer${tmpContainer}
                    docker rm tmpContainer${tmpContainer}
                """
        )
    }
}
