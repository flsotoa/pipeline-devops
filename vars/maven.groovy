def call(String stagename = '') {
    if (stagename == 'build' || stagename == 'all') {
        stage('build') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh 'mvn clean compile -e'
        }
    }
    if (stagename == 'test' || stagename == 'all') {
        stage('test') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh 'mvn clean test -e'
        }
    }
    if (stagename == 'jar' || stagename == 'all') {
        stage('jar') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh 'mvn.cmd clean package -e'
        }
    }
    if (stagename == 'sonarqube' || stagename == 'all') {
        stage('sonarqube') {
            env.JENKINS_STAGE = env.STAGE_NAME
            withSonarQubeEnv(installationName: 'sonar-fsa') {
                sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
            }
        }
    }
    if (stagename == 'run_jar' || stagename == 'all') {
        stage('run_jar') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh 'mvn spring-boot:run &'
            sh 'sleep 200'
        }
    }
     if (stagename == 'test_app' || stagename == 'all') {
        stage('run_jar') {
            env.JENKINS_STAGE = env.STAGE_NAME
            sh 'curl -X GET http://localhost:8085/rest/mscovid/test?msg=testing'
        }
    }
    if (stagename == 'upload_nexus' || stagename == 'all') {
        stage('upload_nexus') {
            env.JENKINS_STAGE = env.STAGE_NAME
            nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: '/Users/nicolas/code/estudios/usach/unidad3/forks/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
    }
}

return this
