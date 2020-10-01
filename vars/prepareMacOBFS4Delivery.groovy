// ----------------------------------------------------------------------------
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2018 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT
//
// @author Yves Schumann <yves@alias.cash>
//
// ----------------------------------------------------------------------------

def call() {
    fileOperations([
            fileRenameOperation(
                    source: "${WORKSPACE}/src/bin/Alias.app/Contents/MacOS/Tor/torrc-defaults",
                    destination: "${WORKSPACE}/src/bin/Alias.app/Contents/MacOS/Tor/torrc-defaults_plain"),
            fileRenameOperation(
                    source: "${WORKSPACE}/src/bin/Alias.app/Contents/MacOS/Tor/torrc-defaults_obfs4",
                    destination: "${WORKSPACE}/src/bin/Alias.app/Contents/MacOS/Tor/torrc-defaults"),
    ])
}
