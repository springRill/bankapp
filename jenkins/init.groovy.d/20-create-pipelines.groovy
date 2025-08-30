import jenkins.model.*
import hudson.plugins.git.*
import org.jenkinsci.plugins.workflow.job.*
import org.jenkinsci.plugins.workflow.cps.*

// Общие настройки
def repoUrl = "https://github.com/springRill/bankapp.git"
def branchName = "helm-and-jenkins"

// Массив с описанием пайплайнов
def jobs = [
        [name: "01_ingress-nginx",    path: "bank-app/ingress-nginx/Jenkinsfile"],
        [name: "02_keycloak",    path: "bank-app/charts/keycloak/Jenkinsfile"],
        [name: "03_postgresql",    path: "bank-app/postgresql/Jenkinsfile"],
        [name: "04_exchange-api", path: "bank-app/charts/exchange-api/Jenkinsfile"],
        [name: "05_exchange-generator", path: "bank-app/charts/exchange-generator/Jenkinsfile"],
        [name: "06_blocker-api", path: "bank-app/charts/blocker-api/Jenkinsfile"],
        [name: "07_notifications-api", path: "bank-app/charts/notifications-api/Jenkinsfile"],
        [name: "08_accounts-api", path: "bank-app/charts/accounts-api/Jenkinsfile"],
        [name: "09_transfer-api", path: "bank-app/charts/transfer-api/Jenkinsfile"],
        [name: "10_cash-api", path: "bank-app/charts/cash-api/Jenkinsfile"],
        [name: "11_front-ui",  path: "bank-app/charts/front-ui/Jenkinsfile"]
]

def jenkins = Jenkins.instance

jobs.each { cfg ->
    def jobName = cfg.name
    def jenkinsfilePath = cfg.path

    def job = jenkins.getItem(jobName)

    if (job == null) {
        println("Создаём Pipeline job: ${jobName}")
        job = jenkins.createProject(WorkflowJob, jobName)
    } else {
        println("Обновляем существующую Pipeline job: ${jobName}")
    }

    // SCM конфигурация
    def remoteConfigs = [new UserRemoteConfig(repoUrl, null, null, null)]
    def branchSpecs = [new BranchSpec(branchName)]

    def scm = new GitSCM(remoteConfigs, branchSpecs, false, [], null, null, [])
    def definition = new CpsScmFlowDefinition(scm, jenkinsfilePath)
    definition.setLightweight(true)

    job.setDefinition(definition)
    job.save()

    println("Job '${jobName}' готова.")
}