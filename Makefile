all:
	mkdir -p Build
	javac -s src/com/github/abepralle/JValue -d Build src/com/github/abepralle/JValue/*.java
	java -cp Build com.github.abepralle.JValue.Test

clean:
	rm -rf Build
