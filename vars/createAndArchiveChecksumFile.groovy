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
    String filename = params.get("filename")
    String checksumfile = params.get("checksumfile")
    sh "./scripts/createChecksums.sh $filename $checksumfile"
    archiveArtifacts allowEmptyArchive: true, artifacts: "$checksumfile"
}
