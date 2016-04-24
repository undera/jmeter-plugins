#! /bin/sh -xe

mvn -Dmaven.test.skip=true -Dadditionalparam=-Xdoclint:none clean package javadoc:jar source:jar gpg:sign

for C in `ls`; do
    tmpdir=`mktemp -d`
    if ls $C/target/*.jar 1> /dev/null 2>&1; then
        cp $C/target/*.jar $tmpdir
        cp $C/target/*.pom $tmpdir
        cp $C/target/*.asc $tmpdir

        rm -f $tmpdir/original-*.jar

        rm -f target/bundle-$C.jar # sonatype does not accept one big bundle
        jar cvf target/bundle-$C.jar -C $tmpdir .
    fi
done

