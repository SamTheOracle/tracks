./mvnw package -Pnative -Dnative-image.xmx=8g
cp target/*-runner ./
./build-push-to-dockerhub.sh