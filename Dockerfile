FROM itzg/minecraft-server

COPY entrypoint.sh /entrypoint.sh
COPY start.sh /start.sh
COPY build/bootScripts/GameWrapperBot /bin/GameWrapperBot
COPY build/libs/GameWrapperBot-*.jar /lib/

ENTRYPOINT ["/entrypoint.sh"]
