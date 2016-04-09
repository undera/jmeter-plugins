#! /bin/sh -xe

mvn -Dmaven.test.skip=true clean package javadoc:jar source:jar gpg:sign

mkdir -p bundle
# 
for C in . common standard extras extraslibs webdriver hadoop xmpp blazemeter; do
    tmpdir=`mktemp -d`
    if ls $C/target/*.jar 1> /dev/null 2>&1; then
        cp $C/target/*.jar $tmpdir
    fi
    cp $C/target/*.pom $tmpdir
    cp $C/target/*.asc $tmpdir
    rm -f $tmpdir/original*
    rm -f $tmpdir/*.zip.asc

    jar cvf bundle/jmeter-plugins-$C-bundle.jar -C $tmpdir .
done
