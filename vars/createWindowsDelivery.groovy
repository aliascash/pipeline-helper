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
    String version = params.get("version")
    String suffix = params.get("suffix")

    // Unzip Tor and remove debug content
    fileOperations([
            fileUnZipOperation(
                    filePath: "${WORKSPACE}/Tor.zip",
                    targetLocation: "${WORKSPACE}/"
            ),
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/src/bin/debug"
            ),
    ])
    // If directory 'Alias' exists from brevious build, remove it
    def exists = fileExists "${WORKSPACE}/src/Alias"
    if (exists) {
        fileOperations([
                folderDeleteOperation(
                        folderPath: "${WORKSPACE}/src/Alias"
                ),
        ])
    }
    // Rename build directory to 'Alias' and create directory for content to remove later
    fileOperations([
            folderRenameOperation(
                    source: "${WORKSPACE}/src/bin",
                    destination: "${WORKSPACE}/src/Alias"
            ),
            folderCreateOperation(
                    folderPath: "${WORKSPACE}/old"
            ),
    ])
    // If archive from previous build exists, move it to directory 'old'
    exists = fileExists "${WORKSPACE}/Alias.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Alias.zip",
                        destination: "${WORKSPACE}/old/Alias.zip"
                ),
        ])
    }
    // If archive from previous build exists, move it to directory 'old'
    exists = fileExists "${WORKSPACE}/Alias-${version}${suffix}.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Alias-${version}.zip",
                        destination: "${WORKSPACE}/old/Alias-${version}.zip"
                ),
        ])
    }
    exists = fileExists "${WORKSPACE}/Alias-${version}-Win64${suffix}.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Alias-${version}-Win64${suffix}.zip",
                        destination: "${WORKSPACE}/old/Alias-${version}-Win64${suffix}.zip"
                ),
        ])
    }
    exists = fileExists "${WORKSPACE}/Alias-${version}-Win64${suffix}-OBFS4.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Alias-${version}-Win64${suffix}-OBFS4.zip",
                        destination: "${WORKSPACE}/old/Alias-${version}-Win64${suffix}-OBFS4.zip"
                ),
        ])
    }
    // Remove directory with artifacts from previous build
    // Create new delivery archive
    // Rename build directory back to initial name
    fileOperations([
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/old"
            ),
            fileZipOperation(
                    folderPath: "${WORKSPACE}/src/Alias",
                    outputFolderPath: "${WORKSPACE}"
            )
    ])
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/Alias.zip",
                    destination: "${WORKSPACE}/Alias-${version}-Win64${suffix}.zip"
            ),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Alias/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/Alias/Tor/torrc-defaults_plain"
            ),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Alias/Tor/torrc-defaults_obfs4",
                    destination: "${WORKSPACE}/src/Alias/Tor/torrc-defaults"
            ),
            fileZipOperation(
                    folderPath: "${WORKSPACE}/src/Alias",
                    outputFolderPath: "${WORKSPACE}"
            )
    ])
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/Alias.zip",
                    destination: "${WORKSPACE}/Alias-${version}-Win64${suffix}-OBFS4.zip"
            ),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Alias/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/Alias/Tor/torrc-defaults_obfs4"
            ),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Alias/Tor/torrc-defaults_plain",
                    destination: "${WORKSPACE}/src/Alias/Tor/torrc-defaults"
            ),
            folderRenameOperation(
                    source: "${WORKSPACE}/src/Alias",
                    destination: "${WORKSPACE}/src/bin"
            )
    ])
}
