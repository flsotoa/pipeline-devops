/*
        forma de invocación de método call:
        def ejecucion = load 'script.groovy'
        ejecucion.call()
*/

def call(){
        stage("Build & test") {
                sh "gradle clean build"
                    }
        stage("SonarQube analysis") {
                def scannerHome = tool 'sonar';
                withSonarQubeEnv('sonar-fsa') {
                bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        }
                    }
        stage("Run") {
                sh "gradle bootRun &"
                    }
        stage("Sleep") {
                 sh 'sleep 200'
                  }
        stage("Tes_Rest") {
                sh "curl -X GET localhost:8085/rest/mscovid/test?msg=testing -O  && dir"
                    }
        stage("uploadNexus") {
                nexusPublisher nexusInstanceId: 'Nexus',
                nexusRepositoryId: 'test-nexus',
                packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'C:\\Users\\Flavio\\.jenkins\\workspace\\job-nexus\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
                    }
}
return this;


