rd/s/q summer

call git clone http://ncoppo.com:66/liuj/summer.git

cd summer/ui-web-future
call cnpm install
call npm run build

cd ..
call gradle clean buildAll -x test
