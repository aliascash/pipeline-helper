// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2019 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String archiveLocation = params.get("archiveLocation")
    String archiveName = params.get("archiveName")

    // If directory 'Spectrecoin' exists from brevious build, remove it
    def exists = fileExists "${WORKSPACE}/windows/content/Spectrecoin"
    if (exists) {
        fileOperations([
                folderDeleteOperation(
                        folderPath: "${WORKSPACE}/windows/content/Spectrecoin"),
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
        echo "Archive '${archiveName}' not found, nothing to do"
    }
}
