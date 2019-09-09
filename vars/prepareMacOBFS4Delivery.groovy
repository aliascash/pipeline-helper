def call() {
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/src/bin/Spectrecoin.app/Contents/MacOS/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/bin/Spectrecoin.app/Contents/MacOS/Tor/torrc-defaults_plain"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/bin/Spectrecoin.app/Contents/MacOS/Tor/torrc-defaults_obfs4",
                    destination: "${WORKSPACE}/src/bin/Spectrecoin.app/Contents/MacOS/Tor/torrc-defaults"),
    ])
}
