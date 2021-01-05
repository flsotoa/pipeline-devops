/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/
def call(){

pipeline {
    agent any

    parameters { choice(name: 'herramienta', choices: ['gradle', 'maven'], description: '') }
    string(name: 'stage' defaultValue:'', description: '')
	
    stages {
        stage('Pipeline') {
            steps {
                script {
                         echo params.herramienta

                        if (params.herramienta == 'gradle') {
                        gradle.call()
                        }
                        else {
                        maven.call()
			}
		     }		
		}
	    }
	}
    }	
}

return this;
