#/bin/sh

appbase='/home/bmb0205/Projects/public/RecipeHealth/'
tomcat='/home/bmb0205/apps/tomcat/webapps/'

cd $appbase
mvn clean
mvn package

rm -rf $tomcat'recipehealth'
rm -f $tomcat'recipehealth.war'

cp $appbase'target/recipehealth.war' $tomcat
