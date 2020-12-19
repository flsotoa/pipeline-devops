/*
        forma de invocación de método call:
        def ejecucion = load 'script.groovy'
        ejecucion.call()
*/

def call(){
            stage("Build") {
                    sh 'mvn clean compile -e'
            }
            stage("Test") {
                sh 'mvn clean test -e'

            }
            stage("Jar") {
                                sh 'mvn.cmd clean package -e'

            }
            stage("SonarQube") {
                     withSonarQubeEnv(installationName: 'sonar-fsa') {
                     sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'                 }
             }
            stage("Run Jar") {
                     sh 'mvn spring-boot:run &'
                    }
            stage("Sleep") {
                     sh 'sleep 200'
                }
            stage("Testing_aplication") {
                        sh 'curl -X GET http://localhost:8085/rest/mscovid/test?msg=testing'
                    }
            stage("Upload Nexus") {
                    nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: '/Users/nicolas/code/estudios/usach/unidad3/forks/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]

            }
}

return this;


