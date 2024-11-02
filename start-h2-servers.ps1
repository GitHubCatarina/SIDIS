# Definir o diretório base como o local onde o script está a ser executado
$scriptDir = (Get-Location).Path
$baseDir = $scriptDir

# Caminho do JAR do H2 (verificar se o ficheiro existe)
$h2Jar = "h2-2.3.232.jar"
if (!(Test-Path -Path (Join-Path $baseDir $h2Jar))) {
    Write-Output "Erro: O ficheiro $h2Jar não foi encontrado em $baseDir."
    exit
}

# Função para iniciar o servidor H2
function Start-H2Server {
    param (
        [string]$servicePath,
        [int[]]$ports
    )

    # Caminho completo para o diretório do serviço
    $fullServicePath = Join-Path $baseDir $servicePath
    if (!(Test-Path -Path $fullServicePath)) {
        Write-Output "Erro: Caminho não encontrado - $fullServicePath"
        return
    }

    # Navegar para o diretório do serviço
    Set-Location -Path $fullServicePath

    # Iniciar o servidor para cada porta especificada
    foreach ($port in $ports) {
        Start-Process -NoNewWindow -FilePath "java" -ArgumentList "-cp", (Join-Path $baseDir $h2Jar), "org.h2.tools.Server", "-tcp", "-tcpAllowOthers", "-tcpPort", $port, "-ifNotExists"
        Write-Output "Servidor H2 iniciado para $servicePath na porta $port"
    }

    # Voltar para o diretório inicial do script
    Set-Location -Path $scriptDir
}

# Iniciar servidores H2 para cada serviço
Start-H2Server -servicePath "serviceAuth" -ports 9091, 9095
Start-H2Server -servicePath "serviceBook" -ports 9092, 9096
Start-H2Server -servicePath "serviceLending" -ports 9093, 9097
Start-H2Server -servicePath "serviceReader" -ports 9094, 9098