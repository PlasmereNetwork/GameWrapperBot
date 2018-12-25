FROM itzg/minecraft-server

COPY entrypoint.sh /entrypoint.sh
COPY build/bootScripts/GameWrapperBot /data/bin/GameWrapperBot
COPY build/libs/GameWrapperBot-1.0-SNAPSHOT.jar /data/lib/

ENTRYPOINT ["/entrypoint.sh"]
