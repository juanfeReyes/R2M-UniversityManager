# R2M-UniversityManager


## To export realm keycloak
Run the following command inside de docker container:
```shell
/opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.realmName=university_manager_api_realm \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/university-manager-api-realm.json
```
