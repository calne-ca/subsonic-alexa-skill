mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package && \
aws lambda update-function-code --function-name alexa-skill-subsonic --zip-file fileb://target/subsonic-alexa-skill-1.0-jar-with-dependencies.jar