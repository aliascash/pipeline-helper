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
    def exists = fileExists 'Alias.Prebuild.libraries.zip'

    if (exists) {
        echo 'Archive \'Alias.Prebuild.libraries.zip\' exists, nothing to download.'
    } else {
        echo 'Archive \'Alias.Prebuild.libraries.zip\' not found, downloading...'
        fileOperations([
                fileDownloadOperation(
                        password: '',
                        targetFileName: 'Alias.Prebuild.libraries.zip',
                        targetLocation: "${WORKSPACE}",
                        url: 'https://github.com/aliascash/resources/raw/master/resources/Alias.Prebuild.libraries.win64.zip',
                        userName: ''),
                fileUnZipOperation(
                        filePath: 'Alias.Prebuild.libraries.zip',
                        targetLocation: '.'),
                folderCopyOperation(
                        destinationFolderPath: 'leveldb',
                        sourceFolderPath: 'Alias.Prebuild.libraries/leveldb'),
                folderCopyOperation(
                        destinationFolderPath: 'packages64bit',
                        sourceFolderPath: 'Alias.Prebuild.libraries/packages64bit'),
                folderCopyOperation(
                        destinationFolderPath: 'src',
                        sourceFolderPath: 'Alias.Prebuild.libraries/src'),
                folderCopyOperation(
                        destinationFolderPath: 'tor',
                        sourceFolderPath: 'Alias.Prebuild.libraries/tor'),
                folderDeleteOperation(
                        './Alias.Prebuild.libraries'
                )
        ])
    }
    exists = fileExists 'Tor.zip'
    if (exists) {
        echo 'Archive \'Tor.zip\' exists, nothing to download.'
    } else {
        echo 'Archive \'Tor.zip\' not found, downloading...'
        fileOperations([
                fileDownloadOperation(
                        password: '',
                        targetFileName: 'Tor.zip',
                        targetLocation: "${WORKSPACE}",
                        url: 'https://github.com/aliascash/resources/raw/master/resources/Alias.Tor.libraries.win64.zip',
                        userName: '')
        ])
    }
}
