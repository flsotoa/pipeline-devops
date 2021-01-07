def call(){

pipeline {
    agent any

    parameters { 
        choice(name: 'HERRAMIENTA', choices: ['Gradle', 'Maven'], description: '') 
        string(name: 'stage', defaultValue: '', description: '' )
        }

    stages {
        stage('Pipeline') {
            steps {
                script {
                    
                    sh 'env'
                    figlet 'hola'
                    figlet 'DevOps Flavio'
                    figlet params.HERRAMIENTA
                    
                    env.JENKINS_STAGE = ''
                    env.ERROR_MESSAGE = ''
                    
                    if (params.HERRAMIENTA == 'Gradle') {
                        //se definen los stages validos para gradle
                        def valid_stages_gradle = ["build","test","sonar","run","rest","nexus"]
                        //se separan los stages por punto y coma
                        def stagesLowercase = params.stage.tokenize(";").collect{ it.toLowerCase() }
                        //se pasan los stages ingresados a minusculas
                        //def stagesLowercase = stagesList.collect{ it.toLowerCase() }

                        for (String item : stagesLowercase) {
                            //si valida si el stage se encuentra dentro de los validos
                            if (!valid_stages_gradle.contains(item)) {
                                env.ERROR_MESSAGE = "El stage ${item} no es valido para proyecto gradle"
                                error(env.ERROR_MESSAGE)
                            }
                        }
                        //aqui todos los stages ingresados fueron validados
                        //si no se le pasa stages, se corren todos
                        if (stagesLowercase.size() == 0) {
                            gradle.call('all')
                        //si solo se pasa 1, ese debe correr
                        } else if (stagesLowercase.size() == 1) {
                            //según el ejercicio, debiese ejecutarse cuando es 1, por eso no se le agregan validaciones
                            gradle.call(stagesLowercase.get(0))          
                        //si se le pasa varios, se ejecutan secuencial y en orden coherente
                        } else {
                            //si build está en la lista todo OK hasta RUN
                            //si build no está en la lista problemas !
                            if (stagesLowercase.contains("build") || stagesLowercase.contains("test")) {
                                //si NO contiene RUN, pero si rest o nexus
                                if (!stagesLowercase.contains("run") && 
                                    ( stagesLowercase.contains("rest"))) {
                                    env.ERROR_MESSAGE = "Es necesario ejecutar el stage run si se quiere correr rest"
                                    error(env.ERROR_MESSAGE)
                                }
                                //se ejecutan en orden, se toman los validos, se chequea que existan y se ejecutan
                                for (String item : valid_stages_gradle) {
                                    if (stagesLowercase.contains(item)) {
                                        println("Ejecutando stage gradle => "+item);
                                        gradle.call(item)
                                    }
                                    //item.contains(stagesLowercase) ? gradle.call(item) : continue
                                }
                            }else {
                                env.ERROR_MESSAGE = "Primero es necesario ejecutar el stage build o test"
                                error(env.ERROR_MESSAGE)
                            }
                        }
                    }


                    if (params.HERRAMIENTA == 'Maven') {
                        //se definen los stages validos para maven
                        def valid_stages_maven = ["build","test","jar","sonarqube","run_jar", "test_app", "upload_nexus"]
                        //se separan los stages por punto y coma
                        def stagesLowercase = params.stage.tokenize(";").collect{ it.toLowerCase() }
                        
                        for (String item : stagesLowercase) {
                            //si valida si el stage se encuentra dentro de los validos
                            if (!valid_stages_maven.contains(item)) {
                                env.ERROR_MESSAGE = "El stage ${item} no es valido para proyecto maven"
                                error(env.ERROR_MESSAGE)
                            }
                        }
                        //aqui todos los stages ingresados fueron validados
                        //si no se le pasa stages, se corren todos
                        if (stagesLowercase.size() == 0) {
                            maven.call('all')
                        //si solo se pasa 1, ese debe correr
                        } else if (stagesLowercase.size() == 1) {
                            //según el ejercicio, debiese ejecutarse cuando es 1, por eso no se le agregan validaciones
                            maven.call(stagesLowercase.get(0))          
                        //si se le pasa varios, se ejecutan secuencial y en orden coherente
                        } else {
                            //si build está en la lista todo OK hasta RUN
                                if (stagesLowercase.contains("build") || stagesLowercase.contains("jar")) {
                                //si NO contiene RUN, pero si rest o nexus
                                if ( (!stagesLowercase.contains("build") || !stagesLowercase.contains("jar"))  && 
                                    ( stagesLowercase.contains("sonarqube")  ||  stagesLowercase.contains("upload_nexus")  )) {
                                    env.ERROR_MESSAGE = "Es necesario ejecutar el stage build y jar, antes de ejecutar sonarqube o upload_nexus"
                                    error(env.ERROR_MESSAGE)
                                }
                                //se ejecutan en orden, se toman los validos, se chequea que existan y se ejecutan
                                for (String item : valid_stages_maven) {
                                    if (stagesLowercase.contains(item)) {
                                        println("Ejecutando stage maven => "+item);
                                        maven.call(item)
                                    }
                                }
                            }else {
                                env.ERROR_MESSAGE = "Primero es necesario ejecutar el stage build o test"
                                error(env.ERROR_MESSAGE)
                                 }
                            }
                        }
                    }
                }
            }   
        }
    }
}
return this;
