rd/s/q summer

call git clone http://ncoppo.com:66/liuj/summer.git

cd summer/ui-web-future
call cnpm install
call npm run build

cd ..
md summer-webapp\src\main\resources\static
xcopy ui-web-future\dist\*.* summer-webapp\src\main\resources\static /s /e /h

call gradle clean buildAll -x test

cd ..

pause