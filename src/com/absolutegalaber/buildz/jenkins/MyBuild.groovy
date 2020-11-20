package com.absolutegalaber.buildz.jenkins

import groovy.json.JsonSlurper

class MyBuild {

    static Long buildNumber(script, String project, String branch) {
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
}