package com.absolutegalaber.buildz.jenkins
import groovy.json.JsonSlurper

class MyBuild {

    static Long buildNumber(script, String project, String branch) {
        //dump into a file for a POST request
        String nextVersionRequest = """
{
    "project": "${project}",
    "branch":"${branch}"
}
"""
        //POST to API into a File
        String response = script.sh(
                returnStdout: true,
                "curl -X POST -H \"Content-Type: application/json\" --data ${nextVersionRequest} http://buildz-api-vpint05.vpint.o2online.de/api/v1/build-numbers/next"
        )
        //read the returned json into a string
        return new JsonSlurper().parseText(response).counter
    }
}