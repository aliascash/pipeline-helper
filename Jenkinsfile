#!groovy
// SPDX-FileCopyrightText: © 2020 Alias Developers
// SPDX-FileCopyrightText: © 2016 SpectreCoin Developers
//
// SPDX-License-Identifier: MIT

pipeline {
    agent none
    options {
        timestamps()
        timeout(time: 4, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '1'))
        disableConcurrentBuilds()
    }
    triggers {
        cron('H 5 * * *')
    }
    environment {
        DISCORD_WEBHOOK = credentials('DISCORD_WEBHOOK')
    }
    stages {
        stage('Notification') {
            agent {
                label "Housekeeping"
            }
            steps {
                // Using result state 'ABORTED' to mark the message on discord with a white border.
                // Makes it easier to distinguish job-start from job-finished
                discordSend(
                        description: "Started build slave cleanup #$env.BUILD_NUMBER",
                        image: '',
                        //link: "$env.BUILD_URL",
                        successful: true,
                        result: "ABORTED",
                        thumbnail: 'https://wiki.jenkins-ci.org/download/attachments/2916393/headshot.png',
                        title: "$env.JOB_NAME",
                        webhookURL: DISCORD_WEBHOOK
                )
            }
        }
        stage('Cleanup slaves') {
            //noinspection GroovyAssignabilityCheck
            parallel {
                stage('Housekeeper') {
                    agent {
                        label "Housekeeping"
                    }
                    steps {
                        script {
                            sh "docker system prune --force"
                        }
                    }
                }
                stage('Builder A') {
                    agent {
                        label "Builder-A"
                    }
                    steps {
                        script {
                            sh "docker system prune --force"
                        }
                    }
                }
                stage('Builder B') {
                    agent {
                        label "Builder-B"
                    }
                    steps {
                        script {
                            sh "docker system prune --force"
                        }
                    }
                }
                stage('Builder Raspi') {
                    agent {
                        label "Builder-Raspi"
                    }
                    steps {
                        script {
                            sh "docker system prune --force"
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                if (!hudson.model.Result.SUCCESS.equals(currentBuild.getPreviousBuild()?.getResult())) {
                    emailext(
                            subject: "GREEN: 'Build slave cleanup [${env.BUILD_NUMBER}]'",
                            body: '${JELLY_SCRIPT,template="html"}',
                            recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    )
                }
                discordSend(
                        description: "Build slave cleanup #$env.BUILD_NUMBER finished successfully",
                        image: '',
                        //link: "$env.BUILD_URL",
                        successful: true,
                        thumbnail: 'https://wiki.jenkins-ci.org/download/attachments/2916393/headshot.png',
                        title: "$env.JOB_NAME",
                        webhookURL: DISCORD_WEBHOOK
                )
            }
        }
        failure {
            emailext(
                    subject: "RED: 'Build slave cleanup [${env.BUILD_NUMBER}]'",
                    body: '${JELLY_SCRIPT,template="html"}',
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
            )
            discordSend(
                    description: "Build slave cleanup #$env.BUILD_NUMBER failed!",
                    image: '',
                    //link: "$env.BUILD_URL",
                    successful: false,
                    thumbnail: 'https://wiki.jenkins-ci.org/download/attachments/2916393/headshot.png',
                    title: "$env.JOB_NAME",
                    webhookURL: DISCORD_WEBHOOK
            )
        }
        aborted {
            discordSend(
                    description: "Build slave cleanup #$env.BUILD_NUMBER was aborted",
                    image: '',
                    //link: "$env.BUILD_URL",
                    successful: true,
                    result: "ABORTED",
                    thumbnail: 'https://wiki.jenkins-ci.org/download/attachments/2916393/headshot.png',
                    title: "$env.JOB_NAME",
                    webhookURL: DISCORD_WEBHOOK
            )
        }
    }
}
