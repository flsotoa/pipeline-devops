def call(String stagename = 'all') {

    if (stagename == 'build' || stagename == 'test' || stagename == 'all') {
        stage('build & test') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh "gradle clean build"
        }
    }

    if (stagename == 'sonar' || stagename == 'all') {
        stage('sonar') {
            env.JENKINS_STAGE = env.STAGE_NAME
            def scannerHome = tool 'sonar';
            withSonarQubeEnv('sonar-fsa') { 
                bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
            }
        }
    }
    if (stagename == 'run' || stagename == 'all') {
        stage('run') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh "gradle bootRun &"
            sh 'sleep 200'
        }
    }
    if (stagename == 'rest' || stagename == 'all') {
        stage('rest') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh "curl -X GET localhost:8085/rest/mscovid/test?msg=testing -O  && dir"
        }
    }
    if (stagename == 'nexus' || stagename == 'all') {
        stage('nexus') {
            env.JENKINS_STAGE = env.STAGE_NAME
            nexusPublisher nexusInstanceId: 'Nexus',
                nexusRepositoryId: 'test-nexus',
                packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'C:\\Users\\Flavio\\.jenkins\\workspace\\job-nexus\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
				}
    }
}
return this
