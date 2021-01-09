def call(){

pipeline {
    agent any

    parameters { choice(name: 'herramienta', choices: ['gradle', 'maven'], description: '') }
    stages {
        stage('Pipeline') {
            steps {
                script {

                         echo params.herramienta

                        if (params.herramienta == 'gradle') {
                        def ejecucion = load 'gradle.groovy'
                        ejecucion.call()
                        }
                        else {
                         def ejecucion = load 'maven.groovy'
                        ejecucion.call()
				}
			}
			}
		}
	   }
	}	
}

return this;
