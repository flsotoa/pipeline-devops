def call(){

    pipeline {
        agent any
        parameters { 
        choice(name: 'HERRAMIENTA', choices: ['Gradle'], description: '') 
        
        stages {
            stage('pipeline') {
                steps {
                    script {
                        
                        figlet params.HERRAMIENTA
                        
                        if(env.GIT_BRANCH=='develop' || env.GIT_BRANCH.contains('feature'))
                        {
                            gradle-ci.call();
                        } else if(env.GIT_BRANCH.contains('release')) {
                            gradle-cd.call();
                        } else {
                        }
                    }
                }
            }
        }
    }

}

return this;
