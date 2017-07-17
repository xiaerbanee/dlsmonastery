d:
cd D:\Users\liuj\IdeaProjects\summer\global-cloud
call gradle clean build -x test

cd..
cd global-tool
call gradle clean build -x test

cd..
cd global-task
call gradle clean build -x test

cd..
cd summer-basic
call gradle clean build -x test

cd..
cd summer-discovery
call gradle clean build -x test

cd..
cd summer-config
call gradle clean build -x test

cd..
cd summer-gateway
call gradle clean build -x test

cd..
cd summer-general
call gradle clean build -x test

cd..
cd summer-uaa
call gradle clean build -x test

cd..
cd ws-future
call gradle clean build -x test

cd..
cd summer-webapp
call gradle clean build -x test
