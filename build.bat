rd/s/q summer

git clone http://ncoppo.com:66/liuj/summer.git

cd summer/ui-web-future
cnpm install
npm run build

cd ..
gradle clean buildAll -x test
