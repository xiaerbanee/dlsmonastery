d:
cd D:\Users\liuj\IdeaProjects\summer\global-cloud
call gradle -PactiveProfiles=prod build

cd..
cd global-tool
call gradle -PactiveProfiles=prod build

cd..
cd summer-basic
call gradle -PactiveProfiles=prod build

cd..
cd summer-discovery
call gradle build

cd..
cd summer-gateway
call gradle -PactiveProfiles=prod build

cd..
cd summer-general
call gradle -PactiveProfiles=prod build

cd..
cd summer-uaa
call gradle -PactiveProfiles=prod build

cd..
cd ws-future
call gradle -PactiveProfiles=prod build
