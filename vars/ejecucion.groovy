def call(){

    pipeline {
        agent any

        stages {
            stage('Pipeline') {
                steps {
                    script {
                        
                        figlet 'Gradle'
                        figlet env.GIT_BRANCH
                        
                        if(env.GIT_BRANCH =='develop')
                        {
                         gradle-ci.call()
                        } 
                        if(env.GIT_BRANCH.contains('feature'))
                        {
                         gradle-ci.call()
                        } else { 
                         gradle-cd.call()
                        }
                    }
                }
            }
        }
    }

}

return this;
