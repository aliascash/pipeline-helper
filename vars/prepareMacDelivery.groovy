// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author HLXEasy <hlxeasy@gmail.com>>
//
// ----------------------------------------------------------------------------

def call() {
    def exists = fileExists 'Tor.zip'
    if (exists) {
        echo 'Archive \'Tor.zip\' exists, nothing to download.'
    } else {
        echo 'Archive \'Tor.zip\' not found, downloading...'
        fileOperations([
                fileDownloadOperation(
                        password: '',
                        targetFileName: 'Tor.zip',
                        targetLocation: "${WORKSPACE}",
                        url: 'https://github.com/aliascash/resources/raw/master/resources/Alias.Tor.libraries.macOS.zip',
                        userName: '')
        ])
    }
    // Unzip Tor and remove debug content
    fileOperations([
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/src/bin/Alias.app/Contents/MacOS/Tor"),
            fileUnZipOperation(
                    filePath: "${WORKSPACE}/Tor.zip",
                    targetLocation: "${WORKSPACE}/"),
            folderDeleteOperation(
                    folderPath: "${WORKSPACE}/src/bin/debug"),
    ])
}
