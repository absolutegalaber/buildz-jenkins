package com.absolutegalaber.buildz.jenkins

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class MyBuild {

    static Long nextBuildzNumber(script, String project, String branch) {
        String nextVersionRequest = """
{
    "project": "${project}",
    "branch":"${branch}"
}
"""
        script.writeFile file: 'nextVersionRequest.json', text: nextVersionRequest
        String response = script.sh(
                returnStdout: true,
                script: """curl -X POST -H "Content-Type: application/json" --data @nextVersionRequest.json http://buildz-api-vpint05.vpint.o2online.de/api/v1/build-numbers/next"""
        )
        //read the returned json into a string
        return new JsonSlurper().parseText(response).counter
    }

    static Long createBuildzBuild(script, String project, String branch, Long buildNumber) {
        String createBuildRequest = """
{
    "project": "${project}",
    "branch":"${branch}",
    "buildNumber":${buildNumber}
}
"""
        script.writeFile file: 'createBuildRequest.json', text: createBuildRequest
        String response = script.sh(
                returnStdout: true,
                script: """curl -X POST -H "Content-Type: application/json" --data @createBuildRequest.json http://buildz-api-vpint05.vpint.o2online.de/api/v1/builds/create"""
        )
        //read the returned json into a string
        return new JsonSlurper().parseText(response).id
    }

    static void addBuildzLabels(script, Long buildId, Map<String, String> labels) {
        def toWrite = """["""
        labels.forEach{ Map.Entry<String, String> entry->
            toWrite += """ {"key":"${entry.key}", value:"${entry.value}"} """
        }
        toWrite += """]"""
        script.writeFile file: 'addLabelsRequest.json', text: toWrite
        script.sh(
                script: """curl -X POST -H "Content-Type: application/json" --data @addLabelsRequest.json http://buildz-api-vpint05.vpint.o2online.de/api/v1/builds/add-labels/${buildId}"""
        )
    }
}