// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2020 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String gitTag = params.get("gitTag")
    String gitCommitShort = params.get("gitCommitShort")

    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/windows/Spectrecoin-Installer.exe",
                    destination: "${WORKSPACE}/Spectrecoin-${GIT_TAG_TO_USE}-${GIT_COMMIT_SHORT}-Win64-Installer.exe"),
    ])
    archiveArtifacts allowEmptyArchive: false, artifacts: "Spectrecoin-${GIT_TAG_TO_USE}-${GIT_COMMIT_SHORT}-Win64-Installer.exe"
}
