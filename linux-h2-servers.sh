#!/bin/bash

# Diretório base onde o arquivo JAR do H2 está localizado
baseDir="/home/catarinagomes/IdeaProjects/SIDIS"

# Caminho do JAR do H2 (verificar se o arquivo existe)
h2Jar="h2-2.3.232.jar"
if [ ! -f "$baseDir/$h2Jar" ]; then
    echo "Erro: O arquivo $h2Jar não foi encontrado em $baseDir."
    exit 1
fi

# Função para iniciar o servidor H2
start_h2_server() {
    servicePath=$1
    ports=(${@:2})

    # Caminho completo para o diretório do serviço
    fullServicePath="$baseDir/$servicePath"
    if [ ! -d "$fullServicePath" ]; then
        echo "Erro: Caminho não encontrado - $fullServicePath"
        return 1
    fi

    # Navegar para o diretório do serviço
    cd "$fullServicePath" || return 1

    # Iniciar o servidor para cada porta especificada
    for port in "${ports[@]}"; do
        java -cp "$baseDir/$h2Jar" org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort $port -ifNotExists &
        echo "Servidor H2 iniciado para $servicePath na porta $port"
    done

    # Voltar para o diretório inicial do script
    cd "$scriptDir" || return 1
}

# Diretório atual do script
scriptDir=$(pwd)

# Serviços e suas portas
start_h2_server "serviceAuth" 2201 2202
start_h2_server "serviceBookCom" 3201 3202
start_h2_server "serviceBookQuery" 3301 3302
start_h2_server "serviceLendingCom" 4201 4202
start_h2_server "serviceLendingQuery" 4301 4302
start_h2_server "serviceReaderCom" 5201 5202
start_h2_server "serviceReaderQuery" 5301 5302
start_h2_server "serviceTopsQuery" 6301 6302
