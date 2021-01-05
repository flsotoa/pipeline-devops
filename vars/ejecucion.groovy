def call(){
    pipeline {
        agent any

        parameters {
	    choice(name: 'buildtool', choices: ['gradle', 'maven'], description: '')
	    string(name: 'stage', defaultValue: '', description: 'Eleccion de stages')} 
				stages {
					stage('Pipeline') {
						steps {
						   script {
						   println 'Herramienta seleccionada: ' + params.buildtool
						   println 'Stages seleccionados: ' + params.stage
					   if (params.buildtool == 'gradle') {
						  gradle.call(params.stage)
					   } else {
						  maven.call(params.stage)		
					   }
                   }  
                }
            }
        }
    }
}

return this;
