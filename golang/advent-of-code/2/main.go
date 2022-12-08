package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	//input, err := readLines("test-input.txt")
	input, err := readLines("input.txt")
	if err != nil {
		fmt.Fprintf(os.Stderr, "error: %v\n", err)
		os.Exit(1)
	}
	//output := firstPart(input)
	output := secondPart(input)
	fmt.Println(output)
}

func secondPart(inputs []string) int {
	sum := 0
	scoreMap := map[string]int{
		"A": 1, //Rock A
		"B": 2, //Paper B
		"C": 3, //Scissors C
		"X": 0, //Lose
		"Y": 3, //Draw
		"Z": 6, //Win
	}
	gameMap := map[string]string{
		"C X": "B",
		"A X": "C",
		"B X": "A",
		"C Y": "C",
		"A Y": "A",
		"B Y": "B",
		"C Z": "A",
		"A Z": "B",
		"B Z": "C",
	}

	for _, v := range inputs {
		sum += scoreMap[gameMap[v]]
		sum += scoreMap[string(v[len(v)-1:])]
	}

	return sum
}

func firstPart(inputs []string) int {
	sum := 0
	scoreMap := map[string]int{
		"X":   1, //Rock A
		"Y":   2, //Paper B
		"Z":   3, //Scissors C
		"C X": 6,
		"A X": 3,
		"B X": 0,
		"C Y": 0,
		"A Y": 6,
		"B Y": 3,
		"C Z": 3,
		"A Z": 0,
		"B Z": 6,
	}

	for _, v := range inputs {
		sum += scoreMap[v]
		sum += scoreMap[string(v[len(v)-1:])]
	}

	return sum
}

func readLines(path string) ([]string, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	return lines, scanner.Err()
}
