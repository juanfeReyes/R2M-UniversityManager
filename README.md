# R2M-UniversityManager

## University manager features

### User
- Create user with ADMIN, DIRECTOR, PROFESSOR and STUDENT Roles
  - Only Admin users can create other users
- Get existing users

### Course
- Create, read and update courses
- Delete course without a assigned subjects

### Subject
- Create subject of a course with the professor and schedules
  - Verify the schedules of the Professor do not overlap

- Update subject
- Get subjects and schedules
- Delete subject and schedules

### Student
- Enroll student to a subject
- Remove student from subject
- Get student

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

## Local configuration for Development
Set the following value environmental value to enable local configuration with docker-compose-local.yml
SPRING_PROFILES_ACTIVE = local
