mvn clean install -DskipTests=true
mvn assembly:single
scp ./target/RiskEventsTopology-0.0.?-SNAPSHOT-dep.jar storm@zkserver1:~/running_storm/

