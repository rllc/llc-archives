# spreadsheet-updater
Archived sermons, backed by the cloud.

[ ![Codeship Status for rllc/spreadsheet-updater](https://codeship.com/projects/21f82550-cf6f-0132-f2fb-0625bb0d2ed5/status?branch=master)](https://codeship.com/projects/76704)
[![Dependency Status](https://www.versioneye.com/user/projects/553eedc61395378a90000047/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/553eedc61395378a90000047)


# getting started
### prerequisite - postgres
```shell
git clone git@github.com:rllc/llc-archives.git
cd llc-archives
./gradlew clean test
./gradlew bootRun
```
open a browser to [http://localhost:8080](http://localhost:8080)

# getting started with docker
```shell
./gradlew clean buildDocker
docker-compose up
```
open a browser to [http://localhost:8080](http://localhost:8080)

# architecture
![asdfdas 1](https://cloud.githubusercontent.com/assets/679510/19615807/1c52b32e-97cb-11e6-9925-8c5377ab1f57.png)
