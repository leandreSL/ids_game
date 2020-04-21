compile: 
	mvn clean compile
	
nodeA:
	mvn exec:java@nodeA
	
nodeB:
	mvn exec:java@nodeB
	
nodeC:
	mvn exec:java@nodeC
	
nodeD:
	mvn exec:java@nodeD
	
player:
	mvn exec:java@player
	
logger:
	mvn exec:java@logger

clean:
	mvn clean