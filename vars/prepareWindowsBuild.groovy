// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2019 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call() {
    def exists = fileExists 'Spectre.Prebuild.libraries.zip'

    if (exists) {
        echo 'Archive \'Spectre.Prebuild.libraries.zip\' exists, nothing to download.'
    } else {
        echo 'Archive \'Spectre.Prebuild.libraries.zip\' not found, downloading...'
        fileOperations([
                fileDownloadOperation(
                        password: '',
                        targetFileName: 'Spectre.Prebuild.libraries.zip',
                        targetLocation: "${WORKSPACE}",
                        url: 'https://github.com/spectrecoin/resources/raw/master/resources/Spectrecoin.Prebuild.libraries.win64.zip',
                        userName: ''),
                fileUnZipOperation(
                        filePath: 'Spectre.Prebuild.libraries.zip',
                        targetLocation: '.'),
                folderCopyOperation(
                        destinationFolderPath: 'leveldb',
                        sourceFolderPath: 'Spectre.Prebuild.libraries/leveldb'),
                folderCopyOperation(
                        destinationFolderPath: 'packages64bit',
                        sourceFolderPath: 'Spectre.Prebuild.libraries/packages64bit'),
                folderCopyOperation(
                        destinationFolderPath: 'src',
                        sourceFolderPath: 'Spectre.Prebuild.libraries/src'),
                folderCopyOperation(
                        destinationFolderPath: 'tor',
                        sourceFolderPath: 'Spectre.Prebuild.libraries/tor'),
                folderDeleteOperation(
                        './Spectre.Prebuild.libraries'
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
                        url: 'https://github.com/spectrecoin/resources/raw/master/resources/Spectrecoin.Tor.libraries.win64.zip',
                        userName: '')
        ])
    }
}
