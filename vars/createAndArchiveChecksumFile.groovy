// ----------------------------------------------------------------------------
//  SPDX-FileCopyrightText: © 2019 The Spectrecoin developers
//  SPDX-License-Identifier: MIT/X11
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String filename = params.get("filename")
    String checksumfile = params.get("checksumfile")
    sh "./scripts/createChecksums.sh $filename $checksumfile"
    archiveArtifacts allowEmptyArchive: true, artifacts: "$checksumfile"
}
