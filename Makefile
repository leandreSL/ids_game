install: 
	mvn clean install compile package shade:shade

node: target/node.jar
	java -jar target/node.jar $(args)
	
target/node.jar:
	make install
	
mvn_node:
	mvn exec:java@node -Dexec.args="$(args)"

player:
	mvn exec:java@player
	
logger:
	mvn exec:java@logger

clean:
	mvn clean

javafx:
	mvn javafx:run -Dexec.args="$(args)"