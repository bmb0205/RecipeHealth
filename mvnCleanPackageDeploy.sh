#/bin/sh

appbase='/home/bmb0205/Projects/public/RecipeHealthApp/'

cd '/home/bmb0205/Projects/public/RecipeHealthApp'
mvn clean
mvn package

rm -rf '/home/bmb0205/apps/tomcat/webapps/recipehealth'
rm -f '/home/bmb0205/apps/tomcat/webapps/recipehealth.war'

cp $appbase'target/recipehealth.war' '/home/bmb0205/apps/tomcat/webapps/'
