# Subsonic Alexa Skill

This is a custom Alexa skill that allows you to play music from a [Subsonic](http://www.subsonic.org) server.
This skill also works with every server that implements the [Subsonic API](http://www.subsonic.org/pages/api.jsp) like [Airsonic](https://airsonic.github.io/) or [Libresonic](https://github.com/ef4/libresonic).

## Usage

This skill has not been published which means you need to deploy it yourself to your personal amazon account.
You can do that by following the steps described in the [Setup section](#setup).

After you setup the skill it will be available on all of your Alexa devices that are connected to the same account you used to deploy your skill.
You can now start the skill like any other skill by using its invocation name.
See [Alexa documentation](https://developer.amazon.com/en-US/docs/alexa/custom-skills/understanding-how-users-invoke-custom-skills.html) for more details on how to invoke a skill. 

Currently, this skill only supports the playback of playlists.
You cannot play specific songs or search for songs.

When you have invoked the skill you can play a playlist by saying something along the lines of *play playlist \<playlist name\>*, depending on the locale you chose during the setup.
You can also skip a song the same way you would skip a song in any other audio skill.

## Setup

### Create the Skill
First you need to create a new Alexa skill in the [Alexa Developer console](https://developer.amazon.com/alexa/console).
This general process of creating a skill is described in the [Alexa documentation](https://developer.amazon.com/en-US/docs/alexa/devconsole/create-a-skill-and-choose-the-interaction-model.html).

Use the JSON editor for the skill model and paste in the content of one of the files in the [model directory](model) depending on your locale.
If you want another language or want to use different phrases for the skill's action feel free to edit the model.

After you have saved the model you have to build it using the *Build Model* button.

#### Add Skill Interfaces
Since this skill plays audio on your Alexa device it needs the *Audio Player* interface to be enabled.
Make sure that it's enabled, otherwise the skill won't work.

#### Add Skill Endpoint
As Endpoint add an AWS Lambda ARN endpoint.
Copy the [ARN](https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) that you get in the [Deploy the Lambda function](#deploy-the-lambda-function) step and paste in in the Default Region field.
This will connect the Lambda function to your skill.

### Deploy the Lambda function
Alexa skills use AWS Lambda functions as backend.
See [AWS Lambda documentation](https://docs.aws.amazon.com/lambda/latest/dg/lambda-foundation.html) for more details.

If you don't have an AWS account yet, create one.
Deploying a lambda function can be done as part of the [AWS Free Tier](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/billing-free-tier.html) so it doesn't cost you any money.

Deploy a new lambda function and upload this application as *.zip or .jar file*.
You can create the necessary jar file by running the following maven command (requires [maven](https://maven.apache.org/) to be installed on your machine):

````bash
mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package
````

You can either upload it in the AWS Console or, if you have [aws cli](https://aws.amazon.com/cli/) installed, use the [build.sh](build.sh) script to build it and upload it to your function in one step.
If you do it with the build script replace *alexa-skill-subsonic* with the name of your lambda function first.

### Deploy DynamoDB Table
This skill requires a [DynamoDB table](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithTables.html) to store the playback state and other configuration.
Create a new DynamoDB table and after that create a new [Policy](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_create-console.html) with the following configuration:

````json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "1",
            "Effect": "Allow",
            "Action": [
                "dynamodb:PutItem",
                "dynamodb:GetItem",
                "dynamodb:UpdateItem"
            ],
            "Resource": "arn:aws:dynamodb:*:*:table/subsonic-alexa-skill"
        }
    ]
}
````
This Policy defined read and write access to your DynamoDB table.
Replace *subsonic-alexa-skill* with the name of your table.

After you created the policy, attach it to the execution role of your lambda function.
You can find your execution role under *Configuration -> Permissions -> Execution role* in the lambda function view.
This finally gives your lambda function access to your DynamoDB table.

### Configuration
This skill needs some configuration passed as environment variables.
Go to *Configuration -> Environment variables -> Edit* and configure the following variables:

| Key               | Value                                                                                                                                                                                                                                                                                                                                                                                    |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DYNAMODB_TABLE    | The name of your DynamoDB table                                                                                                                                                                                                                                                                                                                                                          |
| LOCALE            | The locale that should be used for the response phrases (e.g. en-US).<br>This value will be mapped to a file in the [phrases directory](src/main/resources/phrases).<br>If you want to add another locale or change the response phrases you can add a file there or edit one of the exisitng file.<br>Remember to re-build and upload the application again after changing those files. |
| SUBSONIC_URL      | The public URL of your subsonic server                                                                                                                                                                                                                                                                                                                                                   |
| SUBSONIC_USERNAME | The name of the user for your subsonic server                                                                                                                                                                                                                                                                                                                                            |
| SUBSONIC_PASSWORD | The password of the user for your subsonic server                                                                                                                                                                                                                                                                                                                                        |

After you apply the configuration the lambda function will automatically be updated.

The skill should now work.
See [Usage section](#usage) for details on how to use the skill.