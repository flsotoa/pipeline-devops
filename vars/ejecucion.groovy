def call(){

    pipeline {
        agent any

        stages {
            stage('pipeline') {
                steps {
                    script {
                        
                        if(env.GIT_BRANCH=='develop' || env.GIT_BRANCH.contains('feature'))
                        {
                            gradle-ci.call();
                        } else if(env.GIT_BRANCH.contains('release')) {
                            gradle-ci.call();
                        } else {
                        }
                    }
                }
            }
        }
    }

}

return this;
