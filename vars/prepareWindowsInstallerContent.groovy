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
    String archiveLocation = params.get("archiveLocation")
    String archiveName = params.get("archiveName")

    // If directory 'Alias' exists from brevious build, remove it
    def exists = fileExists "${WORKSPACE}/windows/content/Alias"
    if (exists) {
        fileOperations([
                folderDeleteOperation(
                        folderPath: "${WORKSPACE}/windows/content/Alias"),
        ])
    }

    exists = fileExists "${archiveLocation}/${archiveName}"

    if (exists) {
        echo "Archive '${archiveName}' found, extracting it..."
        fileOperations([
                fileUnZipOperation(
                        filePath: "${archiveLocation}/${archiveName}",
                        targetLocation: "${WORKSPACE}/windows/content")
        ])
    } else {
        error "Archive '${archiveName}' not found, nothing to do"
    }
}
