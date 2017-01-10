#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printInt(int x) {
	printf("%d\n", x);
}

void printString(char* s) {
	printf("%s\n", s);
}


char* readString() {
        char* ret;
        int default_size = 128;
        ret = malloc (default_size + 1);
        getline(&ret, &default_size, stdin);
        ret[strlen(ret) - 1] = 0;

        return ret;
}


int readInt() {
	int x;
	char* str = readString();
	sscanf(str, "%d", &x);

	return x;
}

char* concat(char* s1, char* s2) {
	char* ret = malloc(strlen(s1) + strlen(s2) + 1);
	strcpy(ret, s2);
	strcat(ret, s1);

	return ret;
}

void error() {
	printf("runtime error\n");
	exit(1);
}

/*
int main() {
	char* s1 = readString();
	char* s2 = readString();
	printString(s1);
	printString(s2);

	char* s3 = concat(s1, s2);
	printString(s3);

	return 0;
}
*/
/*
int main() {
	int x = readInt();
	char* s1 = readString();
	char* s2 = readString();

	return 0;
}
*/
/*
int main() {
	printf("hello");
	if (1)
		error();
	printf("world");


	return 0;
}
*/
