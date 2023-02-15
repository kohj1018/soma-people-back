#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc  # tee 는 왼쪽에서 읽어서 오른쪽 .inc 파일에 덮어 쓰는 명령어

    echo "> 엔진엑스 Reload"
    sudo service nginx reload # restart와는 다르게 끊김 없이 다시 불러온다. (외부 설정 파일인 service-url을 다시 불러오는 것이기에 reload로 가능하다)
}
