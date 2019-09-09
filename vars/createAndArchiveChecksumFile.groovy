// ----------------------------------------------------------------------------
//  Copyright (c) 2019 The Spectrecoin developers
//
//  @author   HLXEasy <helix@spectreproject.io>
// ----------------------------------------------------------------------------

def call(Map params = [:]) {
    String filename = params.get("filename")
    String checksumfile = params.get("checksumfile")
    sh "./scripts/createChecksums.sh $filename $checksumfile"
    archiveArtifacts allowEmptyArchive: true, artifacts: "$checksumfile"
}
