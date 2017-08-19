rd/s/q summer
rd/s/q node_modules

call git clone http://ncoppo.com:66/liuj/summer.git

cd summer/ui-web-basic
call cnpm install
call npm run build

cd ../ui-web-future
call cnpm install
call npm run build

cd ../ui-web-tool
call cnpm install
call npm run build

cd ../ui-web-cloud
call cnpm install
call npm run build


cd ..

call gradle clean buildAll -x test


md build\jars\static
xcopy ui-web-main\*.* build\jars\static /s /e /h
md build\jars\static\basic
xcopy ui-web-basic\dist\*.* build\jars\static\basic /s /e /h
md build\jars\static\future
xcopy ui-web-future\dist\*.* build\jars\static\future /s /e /h
md build\jars\static\tool
xcopy ui-web-tool\dist\*.* build\jars\static\tool /s /e /h
md build\jars\static\cloud
xcopy ui-web-cloud\dist\*.* build\jars\static\cloud /s /e /h

cd ..

pause