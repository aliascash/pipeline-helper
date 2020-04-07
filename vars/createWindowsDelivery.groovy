// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2019 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String version = params.get("version")
    String suffix = params.get("suffix")

    // Unzip Tor and remove debug content
    fileOperations([
            fileUnZipOperation(
                    filePath: "${WORKSPACE}/Tor.zip",
                    targetLocation: "${WORKSPACE}/"),
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/src/bin/debug"),
    ])
    // If directory 'Spectrecoin' exists from brevious build, remove it
    def exists = fileExists "${WORKSPACE}/src/Spectrecoin"
    if (exists) {
        fileOperations([
                folderDeleteOperation(
                        folderPath: "${WORKSPACE}/src/Spectrecoin"),
        ])
    }
    // Rename build directory to 'Spectrecoin' and create directory for content to remove later
    fileOperations([
            folderRenameOperation(
                    source: "${WORKSPACE}/src/bin",
                    destination: "${WORKSPACE}/src/Spectrecoin"),
            folderCreateOperation(
                    folderPath: "${WORKSPACE}/old"),
    ])
    // If archive from previous build exists, move it to directory 'old'
    exists = fileExists "${WORKSPACE}/Spectrecoin.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Spectrecoin.zip",
                        destination: "${WORKSPACE}/old/Spectrecoin.zip"),
        ])
    }
    // If archive from previous build exists, move it to directory 'old'
    exists = fileExists "${WORKSPACE}/Spectrecoin-${version}${suffix}.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Spectrecoin-${version}.zip",
                        destination: "${WORKSPACE}/old/Spectrecoin-${version}.zip"),
        ])
    }
    exists = fileExists "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}.zip",
                        destination: "${WORKSPACE}/old/Spectrecoin-${version}-Win64${suffix}.zip"),
        ])
    }
    exists = fileExists "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}-OBFS4.zip"
    if (exists) {
        fileOperations([
                fileRenameOperation(
                        source: "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}-OBFS4.zip",
                        destination: "${WORKSPACE}/old/Spectrecoin-${version}-Win64${suffix}-OBFS4.zip"),
        ])
    }
    // Remove directory with artifacts from previous build
    // Create new delivery archive
    // Rename build directory back to initial name
    fileOperations([
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/old"),
            fileZipOperation("${WORKSPACE}/src/Spectrecoin")
    ])
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/Spectrecoin.zip",
                    destination: "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}.zip"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults_plain"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults_obfs4",
                    destination: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults"),
            fileZipOperation("${WORKSPACE}/src/Spectrecoin")
    ])
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/Spectrecoin.zip",
                    destination: "${WORKSPACE}/Spectrecoin-${version}-Win64${suffix}-OBFS4.zip"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults_obfs4"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults_plain",
                    destination: "${WORKSPACE}/src/Spectrecoin/Tor/torrc-defaults"),
            folderRenameOperation(
                    source: "${WORKSPACE}/src/Spectrecoin",
                    destination: "${WORKSPACE}/src/bin")
    ])
}
