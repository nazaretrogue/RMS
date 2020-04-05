#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char **argv){
	int error = 0;
	char variable[2048];

	sprintf(variable, "SET VARIABLE FRASE \"This is a super cool AGI message %s\"\n", argv[1]);

	printf("%s", variable);
	return error;
}
