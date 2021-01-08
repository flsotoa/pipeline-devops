def call() {
    figlet 'Continuous-Integration'

    stage('buildAndTest') {
         sh "gradle clean build"
    }

    stage('sonar') {
        // Nombre extraido desde Jenkins > Global tool configuration > SonarQube Scanner
            def scannerHome = tool 'sonar';
            withSonarQubeEnv('sonar-fsa') { 
            bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        }
    }

    stage('runJar') {
            sh "gradle bootRun &"
            sh 'sleep 100'
    }

    stage('rest') {
        sh "curl -X GET localhost:8085/rest/mscovid/test?msg=testing -O  && dir"
    }

    stage('nexusCI') {
        nexusPublisher nexusInstanceId: 'NexusLocal',
		nexusPublisher nexusInstanceId: 'Nexus',
                nexusRepositoryId: 'test-nexus',
                packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'C:\\Users\\Flavio\\.jenkins\\workspace\\job-nexus\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
	}
} 
