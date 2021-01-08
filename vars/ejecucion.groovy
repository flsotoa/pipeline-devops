def call(){

    pipeline {
        agent any

        stages {
            stage('Pipeline') {
                steps {
                    script {
                        
                        figlet 'Gradle'
                        figlet env.GIT_BRANCH
                        
                        if(env.GIT_BRANCH =='develop' || env.GIT_BRANCH.contains('feature'))
                        {
                         gradle-ci.call()
                        } else if(env.GIT_BRANCH.contains('release')) {
                            gradle-cd.call()
                        } else { //nada
                        }
                    }
                }
            }
        }
    }

}

return this;
