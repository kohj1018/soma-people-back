#!/usr/bin/env bash

# bash는 return value가 안되니 제일 마지막줄에 echo 해서 결과 출력 후, 클라이언트에서 값을 사용한다

# 쉬고 있는 profile 찾기

function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile) # 현재 정상 작동 되고 있다면 현재의 profile을 받아옴
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"  # 현재 쉬고 있는 profile 출력
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile) # echo로 출력한 값을 받아서 씀

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}