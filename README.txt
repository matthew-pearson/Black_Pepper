You can import the project into eclipse (or your favourite IDE) as a Maven project, and launch it from there.
Or you can build it with Maven (assuming you have it installed) by running this from the command line:
	mvn package

Then take the generated war file from the target folder, and  drop it into the webapps directory of your tomcat (or other  AS) and start it up.

To GET the saved results, browse to http://localhost:8080,
but before you do this, you'll first need to POST them. I used a Chrome plug-in, advanced-rest-client, to POST, but there are no doubt many other ways of achieving this.
You can POST whatever parameters you feel like.