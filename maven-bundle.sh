#! /bin/sh

mvn -Dmaven.test.skip=true clean package javadoc:jar source:jar gpg:sign

# 
for C in . common standard extras extraslibs webdriver hadoop; do
    tmpdir=`mktemp -d`
    cp $C/target/*.pom $tmpdir
    cp $C/target/*.jar $tmpdir
    cp $C/target/*.asc $tmpdir
    rm $tmpdir/original*
    rm $tmpdir/*.zip.asc

    jar cvf target/jmeter-plugins-$C-bundle.jar -C $tmpdir .
done
