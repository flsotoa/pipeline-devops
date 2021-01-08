def call() {
    figlet env.GIT_BRANCH
    figlet 'Continuous-Delivery'

    stage("downloadNexus"){     
        bat 'curl -X GET -u admin:admin http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O' 
        downloadOK = true;
    }

    stage('runDownloadedJar') {
            sh "gradle bootRun &"
            sh 'sleep 100'
    }

    stage('rest') {
        sh "curl -X GET localhost:8085/rest/mscovid/test?msg=testing -O  && dir"
    }

    stage('nexus') {
        nexusPublisher nexusInstanceId: 'NexusLocal',
		nexusPublisher nexusInstanceId: 'Nexus',
        nexusRepositoryId: 'test-nexus',
        packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'C:\\Users\\Flavio\\.jenkins\\workspace\\job-nexus\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
    }  
}
