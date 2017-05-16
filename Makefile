all:
	mkdir -p Build
	javac -s src/com/github/abepralle/JData -d Build src/com/github/abepralle/JData/*.java
	java -cp Build com.github.abepralle.JData.Test

clean:
	rm -rf Build
