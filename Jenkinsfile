pipeline {
  agent any
  environment { 
    LANG = 'C'
  }
  triggers {
    pollSCM('H/2 * * * *')
  }
  options {
    buildDiscarder(logRotator(numToKeepStr:'20'))
  }
  stages {

    // ----------------------------- BUILD
    stage('build') {
      agent { docker { image 'dacr/jenkins-docker-agent-sbt' } }
      steps {
        sh 'sbt package'
      }
      post {
        success {
          archive 'target/**/split*.jar'
          junit 'target/junitresults/**/*.xml'
        }
      }
    }

  }

}
