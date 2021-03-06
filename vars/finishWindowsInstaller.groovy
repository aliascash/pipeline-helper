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
    String gitTag = params.get("gitTag")
    String gitCommitShort = params.get("gitCommitShort")

    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/windows/Alias-Installer.exe",
                    destination: "${WORKSPACE}/Alias-${GIT_TAG_TO_USE}-${GIT_COMMIT_SHORT}-Win64-Installer.exe"),
    ])
    archiveArtifacts allowEmptyArchive: false, artifacts: "Alias-${GIT_TAG_TO_USE}-${GIT_COMMIT_SHORT}-Win64-Installer.exe"
}
