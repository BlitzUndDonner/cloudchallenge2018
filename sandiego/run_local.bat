
mvn compile -e exec:java -Dexec.mainClass=com.zuehlke.cloudchallenge.DataFlowMain -Dexec.args="--project=cloud-hackathon-team-athena --stagingLocation=gs://cloud-hackathon-team-athena/staging/ --tempLocation=gs://cloud-hackathon-team-athena/staging/ --runner=DirectRunner"

