LIB_PATH=/Users/lion/Downloads/lib 
#
mvn install:install-file -DgroupId=com.ip2location -DartifactId=ip2location -Dversion=db1 -Dpackaging=jar -Dfile=$LIB_PATH/ip2location-db1.jar
#
mvn install:install-file -DgroupId=org.json -DartifactId=json -Dversion=20130618 -Dpackaging=jar -Dfile=$LIB_PATH/json-20130618.jar
#
mvn install:install-file -DgroupId=javapns -DartifactId=JavaPNS -Dversion=2.2-nodep -Dpackaging=jar -Dfile=$LIB_PATH/JavaPNS-2.2-nodep.jar
#
mvn install:install-file -DgroupId=com.paypal.sdk -DartifactId=paypal_base -Dversion=20131202 -Dpackaging=jar -Dfile=$LIB_PATH/paypal_base-20131202.jar
#
mvn install:install-file -DgroupId=com.paypal.sdk -DartifactId=paypal_junit -Dversion=20131202 -Dpackaging=jar -Dfile=$LIB_PATH/paypal_junit-20131202.jar
#
mvn install:install-file -DgroupId=com.energysh -DartifactId=sso -Dversion=1.0 -Dpackaging=jar -Dfile=$LIB_PATH/sso-1.0.jar
#
mvn install:install-file -DgroupId=net.sf.sojo -DartifactId=sojo-optional -Dversion=0.5.0 -Dpackaging=jar -Dfile=$LIB_PATH/sojo-optional-0.5.0.jar
