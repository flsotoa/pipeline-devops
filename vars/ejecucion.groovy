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
                         gradleci.call()
                        } 
                        if(env.GIT_BRANCH.contains('feature'))
                        {
                         gradleci.call()
                        }
                        if(env.GIT_BRANCH.contains('realease'))
                        {
                         gradlecd.call()
                        }
                    }
                }
            }
        }
    }

}

return this;
