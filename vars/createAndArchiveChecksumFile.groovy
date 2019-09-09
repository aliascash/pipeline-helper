def createAndArchiveChecksumFile(String filename, String checksumfile) {
    sh "./scripts/createChecksums.sh $filename $checksumfile"
    archiveArtifacts allowEmptyArchive: true, artifacts: "$checksumfile"
}
