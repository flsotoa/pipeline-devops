def call(String seleccion){
    println "Stages recibidos: ${seleccion}"
	println "Stages recibidos: ${seleccion}"
    
	if (seleccion == '') {seleccion='build;test;sonar;run;rest;nexus'}
    
    String[] items = seleccion.toLowerCase().split(";")
	
    for (String item: items) {
    	println "Stage en proceso: ${item}"

	try {
    	   switch (item) {
		   
    	     case 'Build_test':
    	         stage("Build_test") {
					sh "gradle clean build"
    	      }; break
				 
	      case 'Sonar':
    	         stage("Sonar") {
				def scannerHome = tool 'sonar';
				withSonarQubeEnv('sonar-fsa') {
				bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
				}
		}; break

		case 'Run':
			 stage("Run") {
				sh "gradle bootRun &"
			} break

		case 'Sleep':
			stage("Sleep") {
				 sh 'sleep 200'
			}; break

		case 'Tes_Rest':
			stage("Tes_Rest") {
				sh "curl -X GET localhost:8085/rest/mscovid/test?msg=testing -O  && dir"
			}; break

		case 'uploadNexus':
			stage("uploadNexus") {
			nexusPublisher nexusInstanceId: 'Nexus',
			nexusRepositoryId: 'test-nexus',
			packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'C:\\Users\\Flavio\\.jenkins\\workspace\\job-nexus\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
			}; break
		default:
			println "Parametro ${item} incorrecto"
			};
		} 
	catch(Exception e) {
		println "Se produjo el error ${e}"}
	}
}

return this;

