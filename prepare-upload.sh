#! /bin/sh -ex

REV=`git log -1 --format="%H" | cut -c1-10`

# build
#mvn clean cobertura:cobertura package

rm -rf upload
mkdir -p upload

# site docs
cp -r site/* upload/

# coverage reports
mkdir -p upload/files/coverage
echo "<html><head>" > upload/files/coverage/index.html
echo "<title>Code Coverage Reports for JMeter-Plugins.org</title>" >> upload/files/coverage/index.html
echo "</head><body>" >> upload/files/coverage/index.html
echo "<h1>Code Coverage Reports for JMeter-Plugins.org, revision $REV</h1>" >> upload/files/coverage/index.html
echo "<ul style='font-size: x-large'>" >> upload/files/coverage/index.html

for D in `ls` ; do
    if [ -d $D/target/site/cobertura ] ; then
        cp -r $D/target/site/cobertura upload/files/coverage/$D
	echo "<li><a href='$D'>$D</a></li>" >> upload/files/coverage/index.html
    fi
done
echo "</ul>" >> upload/files/coverage/index.html
echo "</body><html>" >> upload/files/coverage/index.html

# package snapshots
mkdir -p upload/files/nightly

for D in `ls` ; do
    if ls $D/target/JMeterPlugins-*.zip 2>/dev/null ; then
        cp $D/target/JMeterPlugins-*.zip upload/files/nightly/
    fi
done

PAT="s/.zip/_$REV.zip/"
rename $PAT upload/files/nightly/*

# examples
cp -r examples upload/img/

tar -czf upload.tgz upload *.sh
